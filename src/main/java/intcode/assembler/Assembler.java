package intcode.assembler;

import intcode.assembler.parser.ExpressionParser;
import intcode.assembler.parser.FunctionCallStatement;
import intcode.assembler.parser.SetStatement;
import util.Util;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Assembler {
  private final Set<String> resources = new HashSet<>();
  private final Map<String, Variable> variables = new HashMap<>();
  private final List<Variable> variableOrdering = new ArrayList<>();

  public final TempSpace tempSpace = new TempSpace("temp_");
  public final ParamSpace paramSpace = new ParamSpace("param_");

  final Map<String, Function> functions = new HashMap<>();
  final Function main;
  private final Parameter globalRelBase;
  private final Variable stackSize;

  private String namespace = "<namespace>";
  private Function function;
  private final SetRelBase setRelBase;
  private final List<ReturnValidation> validations = new ArrayList<>();

  public Assembler() {
    setRelBase = new SetRelBase("# Initial stack offset");
    setRelBase.setAddress(0);
    globalRelBase = DeferredParameter.ofInt(ParameterMode.POSITION, () -> setRelBase.getAddress() + 1);
    stackSize = addVariable(Variable.intVar("__stack_size", BigInteger.ZERO, "current stack size"));
    main = new Function(false, "__main__", "# main function", Collections.emptyList());
    function = main;
  }

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
          String context = resource + ":" + lineNumber + "    " + line;
          if (!Parser.parse(line, this, function, context)) {
            SetStatement setStatement = ExpressionParser.parseSetStatement(line);
            if (setStatement != null) {
              setStatement.apply(this, function, context);
            } else {
              FunctionCallStatement functionCallStatement = ExpressionParser.parseFunctionCall(line);
              if (functionCallStatement != null) {
                functionCallStatement.apply(this, function, context);
              } else {
                throw new RuntimeException("Unexpected line: " + line);
              }
            }
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
    main.endFunc();

    validations.forEach(ReturnValidation::validate);

    AnnotatedIntCode res = new AnnotatedIntCode();

    // 1: set stackpointer -    size: 2
    // 2: define main
    // 3: define temp space
    // 4: define functions
    // 5: define variables
    // 6: define array space

    int pc = 0;
    main.operations.add(0, setRelBase);

    pc = main.finalize(pc);

    for (Variable variable : tempSpace.getAll()) {
      variable.setAddress(pc);
      pc += variable.getLen();
    }

    for (Variable variable : paramSpace.getAll()) {
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

    setRelBase.setParameter(Constant.of(pc));
    main.writeTo(res);

    for (Variable variable : tempSpace.getAll()) {
      int len = variable.getLen();
      for (int i = 0; i < len; i++) {
        String description = variable.context;
        res.addOperation(AnnotatedOperation.variable(description, variable.values[i]));
      }
    }

    for (Variable variable : paramSpace.getAll()) {
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
        try {
          res.addOperation(AnnotatedOperation.variable("Pointer: " + variable.getName(), BigInteger.valueOf(variable.reference.call())));
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      } else {
        for (int i = 0; i < len; i++) {
          String description = i == 0 ? variable.context : "";
          BigInteger value = variable.values != null ? variable.values[i] : BigInteger.ZERO;
          res.addOperation(AnnotatedOperation.variable(description, value));
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
      function.operations.add(new SetOp(context, function.resolveParameter(value), function.resolveParameter(name)));
    }
  }

  public void declareString(String name, String value, String context) {
    if (function == main) {
      Variable string = Variable.string(name + "__data__", value, context);
      addVariable(string);
      addVariable(Variable.pointer(name, string::getAddress, ""));
    } else {
      // TODO: add support for strings in functions
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
    private final StackVariable returnAddress;
    private final StackVariable parentStackSize;

    public final List<Op> operations = new ArrayList<>();
    final List<StaticAllocation> allocations = new ArrayList<>();

    private final Map<String, Label> labels = new HashMap<>();
    private final boolean isStack;

    public final String name;
    private final int injectStackAllocations;
    private int address = -1;

    private final SettableConstant initialStackSize = new SettableConstant();
    private final int numParameters;
    int numReturnValues = -1;
    private int lastReturn;

    public Function(boolean isStack, String name, String context, List<String> parameters) {
      this.isStack = isStack;
      this.name = name;

      numParameters = parameters.size();
      returnAddress = addStackVariable("__return_address");
      parentStackSize = addStackVariable("__parent_size");

      operations.add(new SetOp(context, initialStackSize, stackSize));
      operations.add(new AddOp("# add global relative base", globalRelBase, initialStackSize, globalRelBase));

      List<Variable> paramVars = paramSpace.get(parameters.size());

      int i = 0;
      for (String parameter : parameters) {
        Variable inParam = paramVars.get(i);
        StackVariable to = addStackVariable(parameter);
        operations.add(new SetOp("# copy param " + i, inParam, to));
        i++;
      }
      injectStackAllocations = operations.size();
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

    public Variable resolveVariable(String variableName) {
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
        return Constant.of(new BigInteger(expression));
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
      StackVariable variable = new StackVariable(variableName, stackVariables.size());
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
      for (Op operation : operations) {
        operation.setAddress(pc);
        pc += operation.size();
      }
      return pc;
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

      // Halting is kind of returning, I suppose.
      lastReturn = operations.size();
    }

    public void addThrow(String context) {
      operations.add(new Throw(context));

      // Throwing is kind of returning, I suppose.
      lastReturn = operations.size();
    }

    public void endFunc() {
      if (operations.size() != lastReturn) {
        addReturn(Collections.emptyList(), "implicit return");
      }
      List<Op> stackAllocationOperations = new ArrayList<>();

      int stackSize = stackVariables.size();
      TempVariable temp = tempSpace.getAny();
      for (StaticAllocation allocation : allocations) {
        stackAllocationOperations.add(new AddOp("", globalRelBase, Constant.of(stackSize), temp));
        stackAllocationOperations.add(new SetOp("# allocate array", temp, allocation.variable));
        stackSize += allocation.size;
      }
      tempSpace.release(temp);
      operations.addAll(injectStackAllocations, stackAllocationOperations);
      initialStackSize.setValue(stackSize);
    }

    public void addReturn(List<Parameter> returnValues, String context) {
      if (numReturnValues == -1) {
        numReturnValues = returnValues.size();
      } else if (numReturnValues != returnValues.size()) {
        throw new RuntimeException("Function " + name + " must always return the same number of values");
      }

      // Copy return values to temp param space
      List<Variable> params = paramSpace.get(returnValues.size());
      for (int i = 0; i < returnValues.size(); i++) {
        operations.add(new SetOp("# copy return value " + i + " to param space", returnValues.get(i), params.get(i)));
      }

      TempVariable temp = tempSpace.getAny();
      operations.add(new MulOp("# negate function relative base", stackSize, Constant.MINUS_ONE, temp));
      operations.add(new AddOp("# revert global relative base", globalRelBase, temp, globalRelBase));
      operations.add(new SetOp("# restore prev stack size", parentStackSize, stackSize));

      operations.add(new SetOp("# copy return address to temp", returnAddress, temp));
      operations.add(new MulOp("# negate parent stack size", parentStackSize, Constant.MINUS_ONE, parentStackSize));
      operations.add(new SetRelBase("# revert relative base").setParameter(parentStackSize));
      operations.add(new Jump(context, false, Constant.ZERO, temp, null));
      tempSpace.release(temp);

      lastReturn = operations.size();
    }

    public void declareArray(String name, String len, String context) {
      Variable pointerVar;
      if (isStack) {
        pointerVar = addStackVariable(name);
      } else {
        pointerVar = addVariable(Variable.pointer(name, null, context));
      }

      Parameter lenParam = function.resolveParameter(len);

      if (isStack && lenParam instanceof Constant) {
        int arraySize = lenParam.value().intValueExact();
        allocations.add(new StaticAllocation(pointerVar, arraySize));
      } else {
        operations.add(new SetOp(context, globalRelBase, pointerVar));
        operations.add(new AddOp("# allocate array", globalRelBase, lenParam, globalRelBase));
        operations.add(new AddOp("# allocate array", stackSize, lenParam, stackSize));
      }
    }

    public void addFunctionCall(String funcName, int parameters, int returnVars, String context) {
      if (funcName.equals("output")) {
        if (parameters == 1 && returnVars == 0) {
          List<Variable> param = paramSpace.get(1);
          operations.add(new Output(param.get(0), context));
        } else {
          throw new RuntimeException("output() requires only one parameter and zero return values");
        }
      } else if (funcName.equals("input")) {
        if (parameters == 0 && returnVars == 1) {
          List<Variable> param = paramSpace.get(1);
          operations.add(new Input(param.get(0), context));
        } else {
          throw new RuntimeException("input() requires only one return value and zero parameters");
        }
      } else if (funcName.equals("halt")) {
        if (parameters == 0 && returnVars == 0) {
          addHalt(context);
        } else {
          throw new RuntimeException("halt() requires only zero return values and zero parameters");
        }
      } else if (funcName.equals("throw")) {
        if (parameters == 0 && returnVars == 0) {
          addThrow(context);
        } else {
          throw new RuntimeException("throw() requires only zero return values and zero parameters");
        }
      } else {
        addFunctionCall2(funcName, parameters, returnVars, context);
      }
    }

    private void addFunctionCall2(String funcName, int parameters, int returnVars, String context) {
      Function function = functions.get(funcName);
      if (function == null) {
        throw new RuntimeException("Could not find function " + funcName);
      }

      if (parameters != function.numParameters) {
        throw new RuntimeException("Function " + funcName + " expects " + function.numParameters + " but got " + parameters);
      }

      operations.add(new SetRelBase("# set rel base").setParameter(stackSize));
      operations.add(new SetOp("# save relative base revert", stackSize, new StackVariable("stack size", 1)));

      DeferredParameter jumpTarget = DeferredParameter.ofInt(ParameterMode.IMMEDIATE, function::getAddress);
      Jump jump = new Jump(context, false, Constant.ZERO, jumpTarget, null);
      DeferredParameter returnAddress = DeferredParameter.ofInt(ParameterMode.IMMEDIATE, () -> jump.getAddress() + jump.size());

      operations.add(new SetOp("# save return address", returnAddress, new StackVariable("ret addr", 0)));
      operations.add(jump);

      validations.add(new ReturnValidation(function, returnVars));
    }
  }
}
