package intcode.assembler;

import intcode.assembler.parser.Block;
import intcode.assembler.parser.ExpressionParser;
import util.Util;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

public class Assembler {
  private final Set<String> resources = new HashSet<>();
  private final Map<String, Variable> variables = new HashMap<>();
  private final List<Variable> variableOrdering = new ArrayList<>();

  public final TempSpace tempSpace = new TempSpace("temp_");
  public final ParamSpace paramSpace = new ParamSpace("param_");

  public final Map<String, Function> functions = new HashMap<>();
  public final IntCodeFunction main;
  private final Parameter globalRelBase;
  private final Variable stackSize;

  private String namespace = "<namespace>";
  private IntCodeFunction function;
  private final SetRelBase setRelBase;
  private final List<ReturnValidation> validations = new ArrayList<>();

  public Assembler() {
    setRelBase = new SetRelBase("# Initial stack offset");
    setRelBase.setAddress(0);
    globalRelBase = DeferredParameter.ofInt(ParameterMode.POSITION, () -> setRelBase.getAddress() + 1);
    stackSize = addGlobalVariable(Variable.intVar("__stack_size", BigInteger.ZERO, "current stack size"));
    main = new IntCodeFunction(false, "__main__", "# main function", Collections.emptyList());
    function = main;

    addFunction(new InlineFunction("throw", 0, 0, (caller11, context11, returnVars11) -> caller11.addThrow(context11)));
    addFunction(new InlineFunction("halt", 0, 0, (caller, context, returnVars) -> caller.addHalt(context)));
    addFunction(new InlineFunction("input", 0, 1, (caller, context, returnVars) -> {
      List<Variable> param = paramSpace.get(1);
      caller.operations.add(new Input(param.get(0), context));
    }));
    addFunction(new InlineFunction("output", 1, 0, (caller, context, returnVars) -> {
      List<Variable> param = paramSpace.get(1);
      caller.operations.add(new Output(param.get(0), context));
    }));

  }

  private void addFunction(InlineFunction function) {
    functions.put(function.getName(), function);
  }

  public static AnnotatedIntCode compileAnnotated(String name) {
    Assembler assembler = new Assembler();
    assembler.includeResource(name);
    return assembler.compile();
  }

  public static List<BigInteger> compile(String name) {
    return compileAnnotated(name).getIntCode();
  }

  public void includeResource(String resource) {
    if (!resources.add(resource)) {
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
          ExpressionParser.parseStatement(line).apply(this, function, context);
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

    for (Function func : functions.values()) {
      pc = func.finalize(pc);
    }

    for (Variable variable : variableOrdering) {
      variable.setAddress(pc);
      pc += variable.getLen();
    }

    for (Variable variable : tempSpace.getAll()) {
      variable.setAddress(pc);
      pc += variable.getLen();
    }

    for (Variable variable : paramSpace.getAll()) {
      variable.setAddress(pc);
      pc += variable.getLen();
    }

    setRelBase.setParameter(Constant.of(pc));
    main.writeTo(res);

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
          res.addOperation(AnnotatedOperation.variable("Variable: " + description, value));
        }
      }
    }

    for (Variable variable : tempSpace.getAll()) {
      int len = variable.getLen();
      for (int i = 0; i < len; i++) {
        String description = variable.context;
        res.addOperation(AnnotatedOperation.variable("Variable: " + description, variable.values[i]));
      }
    }

    for (Variable variable : paramSpace.getAll()) {
      int len = variable.getLen();
      for (int i = 0; i < len; i++) {
        String description = variable.context;
        res.addOperation(AnnotatedOperation.variable("Variable: " + description, variable.values[i]));
      }
    }

    return res;
  }

  public Variable addGlobalVariable(Variable variable) {
    String key = namespace + ":" + variable.getName();
    Variable prev = variables.put(key, variable);
    if (prev != null) {
      throw new RuntimeException("Variable " + key + " already defined");
    }
    variableOrdering.add(variable);
    return variable;
  }

  public void setFunction(IntCodeFunction function) {
    this.function = function;
  }

  public class IntCodeFunction implements Function {
    private final List<StackVariable> stackVariables = new Stack<>();
    private final StackVariable returnAddress;
    private final StackVariable parentStackSize;

    public final List<Op> operations = new ArrayList<>();
    final List<StaticAllocation> allocations = new ArrayList<>();
    final Stack<Block> blocks = new Stack<>();
    final Stack<Integer> stackVarIndices = new Stack<>();

    private final Map<String, Label> labels = new HashMap<>();
    private final boolean isStack;

    public final String name;
    private final int injectStackAllocations;
    private int address = -1;

    private final SettableConstant initialStackSize = new SettableConstant();
    private final int numParameters;
    public int numReturnValues = -1;
    private int lastReturn;
    private int numStackVars = 0;

    public IntCodeFunction(boolean isStack, String name, String context, List<String> parameters) {
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

    public void jump(boolean isTrue, String cmpRef, Label destination, String description) {
      jump(isTrue, resolveParameter(cmpRef), destination, description);
    }

    public void jump(boolean isTrue, Parameter parameter, Label destination, String context) {
      operations.add(new Jump(context, isTrue, parameter, null, destination));
    }

    public Label resolveLabel(String label) {
      return labels.computeIfAbsent(namespace + ":" + label, ignore -> new Label(label));
    }

    public Variable resolveVariable(String variableName) {
      Variable stackVariable = resolveStackVariable(variableName);
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

    private Variable resolveStackVariable(String variableName) {
      for (StackVariable stackVariable : stackVariables) {
        if (stackVariable.getName().equals(variableName)) {
          return stackVariable;
        }
      }
      return null;
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
      if (resolveStackVariable(variableName) != null) {
        throw new RuntimeException("Stack variable already defined: " + variableName);
      }
      StackVariable variable = new StackVariable(variableName, stackVariables.size());
      stackVariables.add(variable);
      numStackVars = Math.max(numStackVars, stackVariables.size());
      return variable;
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

    @Override
    public int getNumReturnValues() {
      return numReturnValues;
    }

    @Override
    public String getName() {
      return name;
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
      if (!blocks.isEmpty()) {
        throw new RuntimeException("Can't end function before all blocks are closed");
      }
      if (operations.size() != lastReturn) {
        if (this == main) {
          addHalt("implicit halt");
        } else {
          addReturn("implicit return", 0);
        }
      }
      List<Op> stackAllocationOperations = new ArrayList<>();

      int stackSize = numStackVars;
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

    public void addReturn(String context, int numReturnValues) {
      if (this == main) {
        throw new RuntimeException("Can not return from main");
      }

      if (this.numReturnValues == -1) {
        this.numReturnValues = numReturnValues;
      } else if (this.numReturnValues != numReturnValues) {
        throw new RuntimeException("Function " + this.name + " must always return the same number of values");
      }


      TempVariable temp = tempSpace.getAny();
      operations.add(new MulOp("# negate function relative base", stackSize, Constant.MINUS_ONE, temp));
      operations.add(new AddOp("# revert global relative base", globalRelBase, temp, globalRelBase));
      operations.add(new SetOp("# restore prev stack size", parentStackSize, stackSize));

      operations.add(new SetOp("# copy return address to temp", returnAddress, temp));
      operations.add(new MulOp("# negate parent stack size", parentStackSize, Constant.MINUS_ONE, parentStackSize));
      operations.add(new SetRelBase("# revert relative base").setParameter(parentStackSize));
      operations.add(Jump.toTarget(context, temp));
      tempSpace.release(temp);

      lastReturn = operations.size();
    }

    public void declareArray(String name, Parameter len, String context) {
      Variable pointerVar;
      if (isStack) {
        pointerVar = addStackVariable(name);
      } else {
        pointerVar = addGlobalVariable(Variable.pointer(name, null, context));
      }

      if (isStack && len instanceof Constant) {
        int arraySize = len.value().intValueExact();
        allocations.add(new StaticAllocation(pointerVar, arraySize));
      } else {
        operations.add(new SetOp(context, globalRelBase, pointerVar));
        operations.add(new AddOp("# allocate array", globalRelBase, len, globalRelBase));
        operations.add(new AddOp("# allocate array", stackSize, len, stackSize));
      }
    }

    public void declareString(String name, String value, String context) {
      Variable data = Variable.string(name + "__data__", value, context);
      addGlobalVariable(data);
      if (this == main) {
        addGlobalVariable(Variable.pointer(name, data::getAddress, ""));
      } else {
        StackVariable stackVariable = addStackVariable(name);
        operations.add(new SetOp(context, DeferredParameter.ofInt(ParameterMode.IMMEDIATE, data::getAddress), stackVariable));
      }
    }

    public void addFunctionCall(String funcName, int parameters, int returnVars, String context) {
      Function function = resolveFunction(funcName);

      if (parameters != function.getNumParameters()) {
        throw new RuntimeException("Function " + funcName + " expects " + function.getNumParameters() + " but got " + parameters);
      }
      validations.add(new ReturnValidation(this, function, returnVars, context));

      function.prepareCall(this, context, returnVars);
    }

    @Override
    public int getNumParameters() {
      return numParameters;
    }

    @Override
    public void prepareCall(IntCodeFunction caller, String context, int returnVars) {
      caller.operations.add(new SetRelBase("# set rel base").setParameter(stackSize));
      caller.operations.add(new SetOp("# save relative base revert", stackSize, new StackVariable("stack size", 1)));

      DeferredParameter jumpTarget = DeferredParameter.ofInt(ParameterMode.IMMEDIATE, this::getAddress);
      Jump jump = Jump.toTarget(context, jumpTarget);
      DeferredParameter returnAddress = DeferredParameter.ofInt(ParameterMode.IMMEDIATE, () -> jump.getAddress() + jump.size());

      caller.operations.add(new SetOp("# save return address", returnAddress, new StackVariable("ret addr", 0)));
      caller.operations.add(jump);
    }

    public void pushBlock(Block block) {
      blocks.push(block);
      stackVarIndices.push(stackVariables.size());
    }

    public Block peekBlock() {
      return blocks.peek();
    }

    public Block popBlock() {
      int stackSize = stackVarIndices.pop();
      Util.deleteFromEnd(stackVariables, stackSize);
      return blocks.pop();
    }

    public boolean hasBlocks() {
      return !blocks.isEmpty();
    }

    public void breakOutOfBlock(Assembler assembler, IntCodeFunction caller, String context) {
      int i = blocks.size() - 1;
      while (i >= 0) {
        Block block = blocks.get(i);
        if (block.breakBlock(assembler, caller, context)) {
          return;
        }
        i--;
      }
      throw new RuntimeException("Nothing to break out from");
    }
  }

  public Function resolveFunction(String funcName) {
    Function function = functions.get(funcName);
    if (function == null) {
      throw new RuntimeException("Could not find function " + funcName);
    }
    return function;
  }

}
