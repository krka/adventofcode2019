public interface OpCode {
  int size();

  String pretty(IntCode vm, int pc);

  IntCode.State execute(IntCode vm);

  String name();

  static OpCode fetchOpcode(IntCode vm, int pc) {
    int opcode = vm.getParameter(pc);
    int op = opcode % 100;
    ParameterMode firstParam = ParameterMode.from((opcode / 100) % 10);
    ParameterMode secondParam = ParameterMode.from((opcode / 1000) % 10);
    ParameterMode thirdParam = ParameterMode.from((opcode / 10000) % 10);
    switch (op) {
      case 1: return new Add(firstParam, secondParam, thirdParam);
      case 2: return new Mul(firstParam, secondParam, thirdParam);
      case 3: return new Input(firstParam, secondParam, thirdParam);
      case 4: return new Output(firstParam, secondParam, thirdParam);
      case 5: return new JumpIf(true, firstParam, secondParam, thirdParam);
      case 6: return new JumpIf(false, firstParam, secondParam, thirdParam);
      case 7: return new LessThan(firstParam, secondParam, thirdParam);
      case 8: return new Equals(firstParam, secondParam, thirdParam);
      case 99: return new Halt(firstParam, secondParam, thirdParam);
    }
    throw new IllegalStateException("Unknown opcode: " + opcode + " at position " + pc);
  }

  class Add implements OpCode {

    private final ParameterMode firstParam;
    private final ParameterMode secondParam;
    private final ParameterMode thirdParam;

    public Add(ParameterMode firstParam, ParameterMode secondParam, ParameterMode thirdParam) {
      this.firstParam = firstParam;
      this.secondParam = secondParam;
      this.thirdParam = thirdParam;
    }

    @Override
    public int size() {
      return 4;
    }

    @Override
    public String pretty(IntCode vm, int pc) {
      return thirdParam.pretty(vm,pc + 3) + " = " +
              firstParam.pretty(vm, pc + 1) + " + " +
              secondParam.pretty(vm, pc + 2);
    }

    @Override
    public IntCode.State execute(IntCode vm) {
      int pc = vm.pc();
      int first = firstParam.readParameter(vm, pc + 1);
      int second = secondParam.readParameter(vm, pc + 2);
      thirdParam.writeValue(vm, pc + 3, first + second);
      return IntCode.State.RUNNING;
    }

    @Override
    public String name() {
      return "ADD";
    }
  }

  class Mul implements OpCode {

    private final ParameterMode firstParam;
    private final ParameterMode secondParam;
    private final ParameterMode thirdParam;

    public Mul(ParameterMode firstParam, ParameterMode secondParam, ParameterMode thirdParam) {
      this.firstParam = firstParam;
      this.secondParam = secondParam;
      this.thirdParam = thirdParam;
    }

    @Override
    public int size() {
      return 4;
    }

    @Override
    public String pretty(IntCode vm, int pc) {
      return thirdParam.pretty(vm,pc + 3) + " = " +
              firstParam.pretty(vm, pc + 1) + " * " +
              secondParam.pretty(vm, pc + 2);
    }

    @Override
    public IntCode.State execute(IntCode vm) {
      int pc = vm.pc();
      int first = firstParam.readParameter(vm, pc + 1);
      int second = secondParam.readParameter(vm, pc + 2);
      thirdParam.writeValue(vm, pc + 3, first * second);
      return IntCode.State.RUNNING;
    }

    @Override
    public String name() {
      return "MUL";
    }
  }

  class Input implements OpCode {
    private final ParameterMode firstParam;

    public Input(ParameterMode firstParam, ParameterMode secondParam, ParameterMode thirdParam) {
      this.firstParam = firstParam;
      firstParam.assertType(ParameterMode.POSITION);
      secondParam.assertType(ParameterMode.POSITION);
      thirdParam.assertType(ParameterMode.POSITION);
    }

    @Override
    public IntCode.State execute(IntCode vm) {
      Integer value = vm.pollStdin();
      if (value == null) {
        return IntCode.State.WAITING_FOR_INPUT;
      }
      firstParam.writeValue(vm, vm.pc() + 1, value);
      return IntCode.State.RUNNING;
    }

    @Override
    public String name() {
      return "GET";
    }

    @Override
    public int size() {
      return 2;
    }

    @Override
    public String pretty(IntCode vm, int pc) {
      return firstParam.pretty(vm, pc + 1) + " = stdin()";
    }
  }

  class Output implements OpCode {
    private final ParameterMode firstParam;

    public Output(ParameterMode firstParam, ParameterMode secondParam, ParameterMode thirdParam) {
      this.firstParam = firstParam;
      secondParam.assertType(ParameterMode.POSITION);
      thirdParam.assertType(ParameterMode.POSITION);
    }

    @Override
    public IntCode.State execute(IntCode vm) {
      int value = firstParam.readParameter(vm, vm.pc() + 1);
      vm.writeStdout(value);
      return IntCode.State.RUNNING;
    }

    @Override
    public String name() {
      return "PUT";
    }

    @Override
    public int size() {
      return 2;
    }

    @Override
    public String pretty(IntCode vm, int pc) {
      return "put " + firstParam.pretty(vm, pc + 1);
    }
  }

  class JumpIf implements OpCode {
    private final boolean expected;
    private final ParameterMode firstParam;
    private final ParameterMode secondParam;

    public JumpIf(boolean expected, ParameterMode firstParam, ParameterMode secondParam, ParameterMode thirdParam) {
      this.expected = expected;
      this.firstParam = firstParam;
      this.secondParam = secondParam;
      thirdParam.assertType(ParameterMode.POSITION);
    }

    @Override
    public IntCode.State execute(IntCode vm) {
      int val = firstParam.readParameter(vm, vm.pc() + 1);
      int target = secondParam.readParameter(vm, vm.pc() + 2);

      boolean cmp = val != 0;
      if (cmp == expected) {
        vm.jumpTo(target);
      }
      return IntCode.State.RUNNING;
    }

    @Override
    public String name() {
      return "JMP" + (expected ? "1" : "0");
    }

    @Override
    public int size() {
      return 3;
    }

    @Override
    public String pretty(IntCode vm, int pc) {
      String condition = firstParam.pretty(vm, pc + 1);
      String target = secondParam.pretty(vm, pc + 2);
      return "jump to " + target + " if " + condition + " is " + expected;
    }
  }

  class LessThan implements OpCode {
    private final ParameterMode firstParam;
    private final ParameterMode secondParam;
    private final ParameterMode thirdParam;

    public LessThan(ParameterMode firstParam, ParameterMode secondParam, ParameterMode thirdParam) {
      this.firstParam = firstParam;
      this.secondParam = secondParam;
      this.thirdParam = thirdParam;
      thirdParam.assertType(ParameterMode.POSITION);
    }

    @Override
    public IntCode.State execute(IntCode vm) {
      int first = firstParam.readParameter(vm, vm.pc() + 1);
      int second = secondParam.readParameter(vm, vm.pc() + 2);

      int res = first < second ? 1 : 0;
      thirdParam.writeValue(vm, vm.pc() + 3, res);
      return IntCode.State.RUNNING;
    }

    @Override
    public String name() {
      return "LT";
    }

    @Override
    public int size() {
      return 4;
    }

    @Override
    public String pretty(IntCode vm, int pc) {
      String first = firstParam.pretty(vm, pc + 1);
      String second = secondParam.pretty(vm, pc + 2);
      return thirdParam.pretty(vm, vm.pc() + 3) + " = " + first + " < " + second;
    }
  }

  class Equals implements OpCode {
    private final ParameterMode firstParam;
    private final ParameterMode secondParam;
    private final ParameterMode thirdParam;

    public Equals(ParameterMode firstParam, ParameterMode secondParam, ParameterMode thirdParam) {
      this.firstParam = firstParam;
      this.secondParam = secondParam;
      this.thirdParam = thirdParam;
      thirdParam.assertType(ParameterMode.POSITION);
    }

    @Override
    public IntCode.State execute(IntCode vm) {
      int first = firstParam.readParameter(vm, vm.pc() + 1);
      int second = secondParam.readParameter(vm, vm.pc() + 2);

      int res = first == second ? 1 : 0;
      thirdParam.writeValue(vm, vm.pc() + 3, res);
      return IntCode.State.RUNNING;
    }

    @Override
    public String name() {
      return "EQ";
    }

    @Override
    public int size() {
      return 4;
    }

    @Override
    public String pretty(IntCode vm, int pc) {
      String first = firstParam.pretty(vm, pc + 1);
      String second = secondParam.pretty(vm, pc + 2);
      return thirdParam.pretty(vm, vm.pc() + 3) + " = " + first + " == " + second;
    }
  }

  class Halt implements OpCode {
    public Halt(ParameterMode firstParam, ParameterMode secondParam, ParameterMode thirdParam) {
      firstParam.assertType(ParameterMode.POSITION);
      secondParam.assertType(ParameterMode.POSITION);
      thirdParam.assertType(ParameterMode.POSITION);
    }

    @Override
    public IntCode.State execute(IntCode vm) {
      return IntCode.State.HALTED;
    }

    @Override
    public String name() {
      return "HALT";
    }

    @Override
    public int size() {
      return 1;
    }

    @Override
    public String pretty(IntCode vm, int pc) {
      return "";
    }
  }
}
