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
  private final List<Variable> tempSpace = new ArrayList<>();

  final Map<String, Function> functions = new HashMap<>();
  private final Function main = new Function(false, "__main__");

  private String namespace = "<namespace>";

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
      Function function = main;

      List<String> lines = Util.readResource(resource);
      for (String line : lines) {
        lineNumber++;
        line = line.trim();
        if (!line.isEmpty()) {
          if (Parser.parse(line, this, function)) {
            continue;
          }
          String[] tokens = line.split(" ");
          String first = tokens[0];
          if (first.equals("label")) {
            function.addLabel(tokens[1]);
          } else if (first.equals("eq")) {
            function.eq(tokens[1], tokens[2], tokens[3]);
          } else if (first.equals("lessthan")) {
            function.lessThan(tokens[1], tokens[2], tokens[3]);
          } else if (first.equals("jumptrue")) {
            function.jump(true, tokens[1], tokens[2]);
          } else if (first.equals("jumpfalse")) {
            function.jump(false, tokens[1], tokens[2]);
          } else if (first.equals("input")) {
            function.addInput(tokens[1]);
          } else if (first.equals("output")) {
            function.addOutput(tokens[1]);
          } else if (first.equals("setarray")) {
            function.setArray(tokens[1], tokens[2], tokens[3]);
          } else if (first.equals("getarray")) {
            function.getArray(false, tokens[1], tokens[2], tokens[3]);
          } else if (first.equals("getarrayptr")) {
            function.getArray(true, tokens[1], tokens[2], tokens[3]);
          } else if (first.equals("call")) {
            function.addFunctionCall(tokens);

          } else if (first.equals("func")) {
            String funcName = tokens[1];
            int numVariables = tokens.length - 2;
            if (function != main) {
              throw new RuntimeException("Can't define function inside other function: " + funcName);
            }

            function = new Function(true, funcName);
            if (functions.put(funcName, function) != null) {
              throw new RuntimeException("Function already defined: " + funcName);
            }

            for (int i = 0; i < numVariables; i++) {
              function.addStackVariable(tokens[2 + i]);
            }
          } else if (first.equals("endfunc")) {
            if (function == main) {
              throw new RuntimeException("Can not return from main");
            }
            function = main;
          } else if (first.equals("return")) {
            if (function == main) {
              throw new RuntimeException("Can not return from main");
            }
            function.addReturn(tokens);
          } else if (first.equals("halt")) {
            function.addHalt();
          } else {
            throw new RuntimeException("Unexpected instruction: " + first);
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
      for (int i = 0; i < len; i++) {
        res.add(variable.values[i]);
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

  private Variable declareVariable(int len, String varName, BigInteger defaultValue) {
    String key = namespace + ":" + varName;
    BigInteger[] values = new BigInteger[len];
    for (int i = 0; i < len; i++) {
      values[i] = defaultValue;
    }
    Variable variable = new Variable(values);
    Variable prev = variables.put(key, variable);
    if (prev != null) {
      throw new RuntimeException("Variable " + key + " already defined");
    }
    variableOrdering.add(variable);
    return variable;
  }

  public void declareString(String varName, String value) {
    int len = value.length();
    Variable variable = declareVariable(len + 1, varName, BigInteger.ZERO);
    for (int i = 0; i < len; i++) {
      variable.values[i] = BigInteger.valueOf(value.charAt(i));
    }
    variable.values[len] = BigInteger.ZERO;
  }

  public void declareArray(String varName, int len) {
    Variable variable = declareVariable(len, varName, BigInteger.ZERO);
    for (int i = 0; i < len; i++) {
      variable.values[i] = BigInteger.ZERO;
    }
  }


  public void declareInt(String name, String value) {
    declareVariable(1, name, new BigInteger(value));
  }

  class Function {
    private final List<String> stackVariables = new ArrayList<>();
    private final List<Op> operations = new ArrayList<>();
    private final Map<String, Label> labels = new HashMap<>();
    private final boolean isStack;
    private final SetRelBase setRelBase = new SetRelBase();
    public final String name;
    private int address = -1;

    private Function(boolean isStack, String name) {
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
      operations.add(new EqOp(resolveParameter(a), resolveParameter(b), resolveParameter(target)));
    }

    public void lessThan(String target, String a, String b) {
      operations.add(new LessThanOp(resolveParameter(a), resolveParameter(b), resolveParameter(target)));
    }

    public void jump(boolean isTrue, String cmpRef, String destination) {
      operations.add(new Jump(isTrue, resolveParameter(cmpRef), null, resolveLabel(destination)));
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

      AddOp rewriteParam = new AddOp(array.dereference(), indexRef, Address.placeHolder());
      AddOp addOp = new AddOp(valueParam, Constant.ZERO, Address.placeHolder());

      rewriteParam.setTarget(new AddressableMemory(rewriteParam, 7));
      operations.add(rewriteParam);
      operations.add(addOp);
    }

    public void getArray(boolean isPtr, String arrayName, String index, String target) {

      // getarray <arrayName> <index> <target>
      // is expressed as:
      // 1 - rewrite: p1:<arrayName = addr> + p2:<index> target:<next op param 1>
      // 2 - add:     p1:(will = be + overwritten) p2:0 target:<target>

      Parameter array = resolveParameter(arrayName);
      Parameter indexRef = resolveParameter(index);
      Variable targetParam = resolveVariable(target);

      Parameter address;
      if (isPtr) {
        address = array;
      } else {
        address = array.dereference();
      }
      AddOp rewriteParam = new AddOp(address, indexRef, Address.placeHolder());
      AddOp addOp = new AddOp(Address.placeHolder(), Constant.ZERO, targetParam);

      rewriteParam.setTarget(new AddressableMemory(rewriteParam, 5));
      operations.add(rewriteParam);
      operations.add(addOp);
    }

    private Variable resolveVariable(String variableName) {
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

    private Parameter resolveParameter(String expression) {
      if (expression.startsWith("&")) {
        return resolveVariable(expression.substring(1)).dereference();
      }
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

    public void addReturn(String[] tokens) {
      List<Parameter> returnValues = new ArrayList<>();
      for (int i = 1; i < tokens.length; i++) {
        returnValues.add(resolveParameter(tokens[i]));
      }
      while (tempSpace.size() < returnValues.size()) {
        tempSpace.add(new Variable(1));
      }
      operations.add(new Return(this, returnValues, tempSpace));
    }

    public void addInput(String token) {
      operations.add(new Input(resolveVariable(token)));
    }

    public void addOutput(String token) {
      operations.add(new Output(resolveParameter(token)));
    }

    public void addFunctionCall(String[] tokens) {
      String funcName = tokens[1];

      boolean outputMode = false;
      List<Parameter> parameters = new ArrayList<>();
      List<Variable> outputs = new ArrayList<>();
      for (int i = 2; i < tokens.length; i++) {
        if (tokens[i].equals(":")) {
          outputMode = true;
        } else {
          if (outputMode) {
            outputs.add(resolveVariable(tokens[i]));
          } else {
            parameters.add(resolveParameter(tokens[i]));
          }
        }
      }
      operations.add(new FunctionCall(Assembler.this, funcName, parameters, outputs));
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

}
