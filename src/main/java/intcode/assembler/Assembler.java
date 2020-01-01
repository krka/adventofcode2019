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
  final List<Variable> tempSpace = new ArrayList<>();

  final Map<String, Function> functions = new HashMap<>();
  final Function main = new Function(false, "__main__");

  private String namespace = "<namespace>";
  private Function function = main;

  public static List<BigInteger> compile(String name) {
    Assembler assembler = new Assembler();
    assembler.includeResource(name);

    return assembler.compile();
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
          if (!Parser.parse(line, this, function)) {
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

  private List<BigInteger> compile() {
    ArrayList<BigInteger> res = new ArrayList<>();
    // 1: set stackpointer -    size: 2
    // 2: jump to main entry    size: 3
    // 3: define variables
    // 4: define temp space
    // 5: define functions
    // 6: define main

    SetRelBase setRelBase = new SetRelBase();

    int pc = setRelBase.size() + Jump.SIZE;
    for (Variable variable : variableOrdering) {
      variable.setAddress(pc);
      pc += variable.getLen();
    }

    for (Variable variable : tempSpace) {
      variable.setAddress(pc);
      pc += variable.getLen();
    }

    for (Function func : functions.values()) {
      pc = func.finalize(pc);
    }
    pc = main.finalize(pc);

    setRelBase.setParameter(pc).writeTo(res);
    new Jump(false, Constant.ZERO, new Constant(main.getAddress()), null).writeTo(res);

    for (Variable variable : variableOrdering) {
      int len = variable.getLen();
      if (variable.reference != null) {
        if (len != 1) {
          throw new RuntimeException();
        }
        res.add(BigInteger.valueOf(variable.reference.getAddress()));
      } else {
        for (int i = 0; i < len; i++) {
          res.add(variable.values[i]);
        }
      }
    }
    for (Variable variable : tempSpace) {
      int len = variable.getLen();
      for (int i = 0; i < len; i++) {
        res.add(BigInteger.ZERO);
      }
    }

    for (Function func : functions.values()) {
      func.writeTo(res);
    }
    main.writeTo(res);

    return res;
  }

  public void declareInt(String name, String value) {
    addVariable(name, Variable.intVar(new BigInteger(value)));
  }

  public void declareString(String varName, String value) {
    Variable string = Variable.string(value);
    addVariable(varName + "__data__", string);
    addVariable(varName, Variable.pointer(string));
  }

  public void declareArray(String varName, int len) {
    Variable string = Variable.array(len);
    addVariable(varName + "__data__", string);
    addVariable(varName, Variable.pointer(string));
  }

  private Variable addVariable(String name, Variable variable) {
    String key = namespace + ":" + name;
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
    private final List<String> stackVariables = new ArrayList<>();
    final List<Op> operations = new ArrayList<>();
    private final Map<String, Label> labels = new HashMap<>();
    private final boolean isStack;
    private final SetRelBase setRelBase = new SetRelBase();
    public final String name;
    private int address = -1;

    public Function(boolean isStack, String name) {
      this.isStack = isStack;
      this.name = name;
      if (isStack) {
        operations.add(setRelBase);
      }
    }

    public void add(String target, String a, String b) {
      operations.add(new AddOp(resolveParameter(a), resolveParameter(b), resolveParameter(target)));
    }

    public void mul(String target, String a, String b) {
      operations.add(new MulOp(resolveParameter(a), resolveParameter(b), resolveParameter(target)));
    }

    public void eq(String target, String a, String b) {
      eq(resolveParameter(target), a, b);
    }

    public void eq(Parameter target, String a, String b) {
      operations.add(new EqOp(resolveParameter(a), resolveParameter(b), target));
    }

    public void lessThan(String target, String a, String b) {
      lessThan(resolveParameter(target), a, b);
    }

    public void lessThan(Parameter target, String a, String b) {
      operations.add(new LessThanOp(resolveParameter(a), resolveParameter(b), target));
    }

    public void jump(boolean isTrue, String cmpRef, String destination) {
      jump(isTrue, resolveParameter(cmpRef), destination);
    }

    public void jump(boolean isTrue, Parameter parameter, String destination) {
      operations.add(new Jump(isTrue, parameter, null, resolveLabel(destination)));
    }

    private Label resolveLabel(String label) {
      return labels.computeIfAbsent(namespace + ":" + label, ignore -> new Label(label));
    }

    public void setArray(String arrayName, String index, String value) {

      // setarray <arrayName> <index> <value>
      // is expressed as:
      // 1 - rewrite: p1:<arrayName = addr> + p2:<index> target:<next op param 3>
      // 2 - add:     p1:<value> = p2:0 + target:(will be overwritten)

      Variable array = resolveVariable(arrayName);
      Parameter indexRef = resolveParameter(index);
      Parameter valueParam = resolveParameter(value);

      AddOp rewriteParam = new AddOp(array, indexRef, Address.placeHolder());
      AddOp addOp = new AddOp(valueParam, Constant.ZERO, Address.placeHolder());

      rewriteParam.setTarget(new AddressableMemory(rewriteParam, 7));
      operations.add(rewriteParam);
      operations.add(addOp);
    }

    public void getArray(String arrayName, String index, String target) {

      // getarray <arrayName> <index> <target>
      // is expressed as:
      // 1 - rewrite: p1:<arrayName = addr> + p2:<index> target:<next op param 1>
      // 2 - add:     p1:(will = be + overwritten) p2:0 target:<target>

      Parameter array = resolveParameter(arrayName);
      Parameter indexRef = resolveParameter(index);
      Variable targetParam = resolveVariable(target);

      AddOp rewriteParam = new AddOp(array, indexRef, Address.placeHolder());
      AddOp addOp = new AddOp(Address.placeHolder(), Constant.ZERO, targetParam);

      rewriteParam.setTarget(new AddressableMemory(rewriteParam, 5));
      operations.add(rewriteParam);
      operations.add(addOp);
    }

    Variable resolveVariable(String variableName) {
      int i = 0;
      for (String stackVariable : stackVariables) {
        if (stackVariable.equals(variableName)) {
          return new StackVariable(i - stackVariables.size());
        }
        i++;
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

    public void addStackVariable(String variableName) {
      if (stackVariables.contains(variableName)) {
        throw new RuntimeException("Stack variable already defined: " + variableName);
      }
      stackVariables.add(variableName);
    }

    public void addInput(String token) {
      operations.add(new Input(resolveVariable(token)));
    }

    public void addOutput(String token) {
      operations.add(new Output(resolveParameter(token)));
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

    public void writeTo(List<BigInteger> res) {
      for (Op operation : operations) {
        operation.safeWrite(res);
      }
    }

    public void addHalt() {
      operations.add(new Halt());
    }

  }

  void ensureTempSpaceSize(int size) {
    while (tempSpace.size() < size) {
      tempSpace.add(Variable.intVar(BigInteger.ZERO));
    }
  }
}
