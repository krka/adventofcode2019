package intcode.assembler;

import util.Util;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Assembler {
  private final Set<String> resources = new HashSet<>();
  private final Map<String, Variable> variables = new HashMap<>();
  private final List<Variable> variableOrdering = new ArrayList<>();
  private final List<Variable> arraySpace = new ArrayList<>();
  private final List<Variable> tempSpace = new ArrayList<>();
  private final List<Variable> paramSpace = new ArrayList<>();

  final Map<String, Function> functions = new HashMap<>();
  final Function main = new Function(false, "__main__", "# main function");

  private String namespace = "<namespace>";
  private Function function = main;

  public static AnnotatedIntCode compileAnnotated(String name) {
    Assembler assembler = new Assembler();
    assembler.includeResource(name);
    return assembler.compile();
  }

  public static List<BigInteger> compile(String name) {
    return compileAnnotated(name).getIntCode();
  }

  void includeResource(String resource) {
    if (!resources.add(resource)) {
      // TODO: check for cycles
      return;
    }

    int lineNumber = 0;

    String prevNamespace = namespace;
    namespace = resource;
    try {
      List<String> lines = Util.readResource(resource);
      for (String line : lines) {
        lineNumber++;
        line = line.trim();
        if (!line.isEmpty()) {
          if (!Parser.parse(line, this, function, resource, lineNumber)) {
            throw new RuntimeException("Unexpected line: " + line);
          }
        }
      }
    } catch (RuntimeException e) {
      throw new RuntimeException(resource + ":" + lineNumber + "  " + e.getMessage(), e);
    } finally {
      namespace = prevNamespace;
    }
  }

  private AnnotatedIntCode compile() {
    AnnotatedIntCode res = new AnnotatedIntCode();

    // 1: set stackpointer -    size: 2
    // 2: define main
    // 3: define temp space
    // 4: define functions
    // 5: define variables
    // 6: define array space

    SetRelBase setRelBase = new SetRelBase("# Initial stack offset");

    setRelBase.setAddress(0);
    int pc = setRelBase.size();

    pc = main.finalize(pc);

    for (Variable variable : tempSpace) {
      variable.setAddress(pc);
      pc += variable.getLen();
    }

    for (Variable variable : paramSpace) {
      variable.setAddress(pc);
      pc += variable.getLen();
    }

    for (Function func : functions.values()) {
      pc = func.finalize(pc);
    }

    for (Variable variable : variableOrdering) {
      variable.setAddress(pc);
      pc += variable.getLen();
    }

    for (Variable variable : arraySpace) {
      variable.setAddress(pc);
      pc += variable.getLen();
    }

    setRelBase.setParameter(pc).writeTo(res);

    main.finish();
    main.writeTo(res);

    for (Variable variable : tempSpace) {
      int len = variable.getLen();
      for (int i = 0; i < len; i++) {
        String description = variable.context;
        res.addOperation(AnnotatedOperation.variable(description, variable.values[i]));
      }
    }

    for (Variable variable : paramSpace) {
      int len = variable.getLen();
      for (int i = 0; i < len; i++) {
        String description = variable.context;
        res.addOperation(AnnotatedOperation.variable(description, variable.values[i]));
      }
    }

    for (Function func : functions.values()) {
      func.writeTo(res);
    }

    for (Variable variable : variableOrdering) {
      int len = variable.getLen();
      if (variable.reference != null) {
        if (len != 1) {
          throw new RuntimeException();
        }
        res.addOperation(AnnotatedOperation.variable("Pointer: " + variable.getName(), BigInteger.valueOf(variable.reference.getAddress())));
      } else {
        for (int i = 0; i < len; i++) {
          String description = i == 0 ? variable.context : "";
          res.addOperation(AnnotatedOperation.variable(description, variable.values[i]));
        }
      }
    }

    return res;
  }

  public void declareInt(String name, String value, String context) {
    if (function == main) {
      addVariable(Variable.intVar(name, new BigInteger(value), context));
    } else {
      function.addStackVariable(name);
      function.add(context, function.resolveParameter(name), function.resolveParameter(value), Constant.ZERO);
    }
  }

  public void declareString(String name, String value, String context) {
    if (function == main) {
      Variable string = Variable.string(name + "__data__", value, context);
      addVariable(string);
      addVariable(Variable.pointer(name, string, ""));
    } else {
      // TODO: add support for strings in functions
      throw new RuntimeException();
    }
  }

  public void declareArray(String name, String len, String context) {
    Parameter lenParam = function.resolveParameter(len);
    if (lenParam instanceof Constant) {
      if (function == main) {
        Variable array = Variable.array(name + "__data__", lenParam.value().intValueExact(), context);
        arraySpace.add(array);
        addVariable(Variable.pointer(name, array, ""));
      } else {
        // TODO: add support for static arrays in functions
        throw new RuntimeException();
      }
    } else {
      // TODO: Implement dynamic arrays
      throw new RuntimeException();
    }
  }

  private Variable addVariable(Variable variable) {
    String key = namespace + ":" + variable.getName();
    Variable prev = variables.put(key, variable);
    if (prev != null) {
      throw new RuntimeException("Variable " + key + " already defined");
    }
    variableOrdering.add(variable);
    return variable;
  }

  public void setFunction(Function function) {
    this.function = function;
  }

  public class Function {
    private final Map<String, StackVariable> stackVariables = new HashMap<>();
    final List<Op> operations = new ArrayList<>();
    private final Map<String, Label> labels = new HashMap<>();
    private final boolean isStack;
    private final SetRelBase setRelBase;
    public final String name;
    private int address = -1;

    public Function(boolean isStack, String name, String context) {
      this.isStack = isStack;
      this.name = name;
      this.setRelBase = new SetRelBase(context);
      if (isStack) {
        operations.add(setRelBase);
      }
    }

    public void add(String context, Parameter target, Parameter a, Parameter b) {
      operations.add(new AddOp(context, a, b, target));
    }

    public void mul(Parameter target, Parameter a, Parameter b, String context) {
      operations.add(new MulOp(context, a, b, target));
    }

    public void eq(String target, String a, String b, String context) {
      eq(resolveParameter(target), a, b, context);
    }

    public void eq(Parameter target, String a, String b, String context) {
      operations.add(new EqOp(context, resolveParameter(a), resolveParameter(b), target));
    }

    public void lessThan(String target, String a, String b, String context) {
      lessThan(resolveParameter(target), a, b, context);
    }

    public void lessThan(Parameter target, String a, String b, String context) {
      operations.add(new LessThanOp(context, resolveParameter(a), resolveParameter(b), target));
    }

    public void jump(boolean isTrue, String cmpRef, String destination, String description) {
      jump(isTrue, resolveParameter(cmpRef), destination, description);
    }

    public void jump(boolean isTrue, Parameter parameter, String destination, String context) {
      operations.add(new Jump(context, isTrue, parameter, null, resolveLabel(destination)));
    }

    private Label resolveLabel(String label) {
      return labels.computeIfAbsent(namespace + ":" + label, ignore -> new Label(label));
    }

    public void setArray(String arrayName, String index, String value, String context) {

      // setarray <arrayName> <index> <value>
      // is expressed as:
      // 1 - rewrite: p1:<arrayName = addr> + p2:<index> target:<next op param 3>
      // 2 - add:     p1:<value> = p2:0 + target:(will be overwritten)

      Variable array = resolveVariable(arrayName);
      Parameter indexRef = resolveParameter(index);
      Parameter valueParam = resolveParameter(value);

      AddOp rewriteParam = new AddOp(context, array, indexRef, Address.placeHolder());
      AddOp addOp = new AddOp("# write to array from value", valueParam, Constant.ZERO, Address.placeHolder());

      rewriteParam.setTarget(new AddressableMemory(rewriteParam, 7));
      operations.add(rewriteParam);
      operations.add(addOp);
    }

    public void getArray(String arrayName, String index, String target, String context) {

      // getarray <arrayName> <index> <target>
      // is expressed as:
      // 1 - rewrite: p1:<arrayName = addr> + p2:<index> target:<next op param 1>
      // 2 - add:     p1:(will = be + overwritten) p2:0 target:<target>

      Parameter array = resolveParameter(arrayName);
      Parameter indexRef = resolveParameter(index);
      Variable targetParam = resolveVariable(target);

      AddOp rewriteParam = new AddOp(context, array, indexRef, Address.placeHolder());
      AddOp addOp = new AddOp("# write to variable", Address.placeHolder(), Constant.ZERO, targetParam);

      rewriteParam.setTarget(new AddressableMemory(rewriteParam, 5));
      operations.add(rewriteParam);
      operations.add(addOp);
    }

    Variable resolveVariable(String variableName) {
      StackVariable stackVariable = stackVariables.get(variableName);
      if (stackVariable != null) {
        return stackVariable;
      }

      String key = namespace + ":" + variableName;
      Variable variable = variables.get(key);
      if (variable == null) {
        throw new RuntimeException("No such variable: " + key);
      }
      return variable;
    }

    Parameter resolveParameter(String expression) {
      try {
        return new Constant(new BigInteger(expression));
      } catch (NumberFormatException e) {
        return resolveVariable(expression);
      }
    }

    public void addLabel(String label) {
      operations.add(resolveLabel(label).setDefined());
    }

    public StackVariable addStackVariable(String variableName) {
      if (stackVariables.containsKey(variableName)) {
        throw new RuntimeException("Stack variable already defined: " + variableName);
      }
      StackVariable variable = new StackVariable(stackVariables.size());
      stackVariables.put(variableName, variable);
      return variable;
    }

    public void addInput(String token, String context) {
      operations.add(new Input(resolveVariable(token), context));
    }

    public void addOutput(String token, String context) {
      operations.add(new Output(resolveParameter(token), context));
    }

    public int finalize(int pc) {
      this.address = pc;
      setRelBase.setParameter(getRelBase());
      for (Op operation : operations) {
        operation.setAddress(pc);
        pc += operation.size();
      }
      return pc;
    }

    int getRelBase() {
      return stackVariables.size() + 1;
    }

    public int getAddress() {
      if (address == -1) {
        throw new RuntimeException("Address not defined");
      }
      return address;
    }

    public void writeTo(AnnotatedIntCode res) {
      for (Op operation : operations) {
        operation.safeWrite(res);
      }
    }

    public void addHalt(String context) {
      operations.add(new Halt(context));
    }

    public void finish() {
      for (StackVariable value : stackVariables.values()) {
        value.withOffset(stackVariables.size());
      }
    }
  }

  Variable getTemp(int index) {
    while (tempSpace.size() <= index) {
      tempSpace.add(Variable.intVar("temp_" + index, BigInteger.ZERO, "(temp_" + index + ")"));
    }
    return tempSpace.get(index);
  }

  Variable getParam(int index) {
    while (tempSpace.size() <= index) {
      tempSpace.add(Variable.intVar("param_" + index, BigInteger.ZERO, "(param_" + index + ")"));
    }
    return tempSpace.get(index);
  }
}
