package intcode;

public interface OpCode {
  int size();

  String pretty();

  IntCode.State execute(IntCode vm);

  String name();

  static OpCode fetchOpcode(IntCode vm, int pc) {
    int opcode = vm.getParameter(pc);
    int op = opcode % 100;
    ParameterMode firstParam = ParameterMode.from((opcode / 100) % 10);
    ParameterMode secondParam = ParameterMode.from((opcode / 1000) % 10);
    ParameterMode thirdParam = ParameterMode.from((opcode / 10000) % 10);
    switch (op) {
      case 1: return new Add(vm, pc, firstParam, secondParam, thirdParam);
      case 2: return new Mul(vm, pc, firstParam, secondParam, thirdParam);
      case 3: return new Input(vm, pc, firstParam, secondParam, thirdParam);
      case 4: return new Output(vm, pc, firstParam, secondParam, thirdParam);
      case 5: return new JumpIf(true, vm, pc, firstParam, secondParam, thirdParam);
      case 6: return new JumpIf(false, vm, pc, firstParam, secondParam, thirdParam);
      case 7: return new LessThan(vm, pc, firstParam, secondParam, thirdParam);
      case 8: return new Equals(vm, pc, firstParam, secondParam, thirdParam);
      case 99: return new Halt(vm, pc, firstParam, secondParam, thirdParam);
    }
    throw new IllegalStateException("Unknown opcode: " + opcode + " at position " + pc);
  }

  class Add implements OpCode {
    private final ReadParameter first;
    private final ReadParameter second;
    private final WriteParameter target;

    public Add(IntCode vm, int pc, ParameterMode firstParam, ParameterMode secondParam, ParameterMode thirdParam) {
      first = firstParam.resolveRead(vm, pc + 1);
      second = secondParam.resolveRead(vm, pc + 2);
      target = thirdParam.resolveWrite(vm, pc + 3);
    }

    @Override
    public int size() {
      return 4;
    }

    @Override
    public String pretty() {
      return target.pretty() + " <- " + first.pretty() + " + " + second.pretty();
    }

    @Override
    public IntCode.State execute(IntCode vm) {
      target.write(first.value() + second.value());
      return IntCode.State.RUNNING;
    }

    @Override
    public String name() {
      return "ADD";
    }
  }

  class Mul implements OpCode {
    private final ReadParameter first;
    private final ReadParameter second;
    private final WriteParameter target;

    public Mul(IntCode vm, int pc, ParameterMode firstParam, ParameterMode secondParam, ParameterMode thirdParam) {
      first = firstParam.resolveRead(vm, pc + 1);
      second = secondParam.resolveRead(vm, pc + 2);
      target = thirdParam.resolveWrite(vm, pc + 3);
    }

    @Override
    public int size() {
      return 4;
    }

    @Override
    public String pretty() {
      return target.pretty() + " <- " + first.pretty() + " * " + second.pretty();
    }

    @Override
    public IntCode.State execute(IntCode vm) {
      target.write(first.value() * second.value());
      return IntCode.State.RUNNING;
    }

    @Override
    public String name() {
      return "MUL";
    }
  }

  class Input implements OpCode {
    private final WriteParameter target;

    public Input(IntCode vm, int pc, ParameterMode firstParam, ParameterMode secondParam, ParameterMode thirdParam) {
      target = firstParam.resolveWrite(vm, pc + 1);
      secondParam.assertType(ParameterMode.POSITION);
      thirdParam.assertType(ParameterMode.POSITION);
    }

    @Override
    public IntCode.State execute(IntCode vm) {
      Integer value = vm.pollStdin();
      if (value == null) {
        return IntCode.State.WAITING_FOR_INPUT;
      }
      target.write(value);
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
    public String pretty() {
      return target.pretty() + " <- stdin()";
    }
  }

  class Output implements OpCode {
    private final ReadParameter input;

    public Output(IntCode vm, int pc, ParameterMode firstParam, ParameterMode secondParam, ParameterMode thirdParam) {
      input = firstParam.resolveRead(vm, pc + 1);
      secondParam.assertType(ParameterMode.POSITION);
      thirdParam.assertType(ParameterMode.POSITION);
    }

    @Override
    public IntCode.State execute(IntCode vm) {
      vm.writeStdout(input.value());
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
    public String pretty() {
      return input.pretty() + " -> stdout()";
    }
  }

  class JumpIf implements OpCode {
    private final boolean expected;
    private final ReadParameter val;
    private final ReadParameter target;

    public JumpIf(boolean expected, IntCode vm, int pc, ParameterMode firstParam, ParameterMode secondParam, ParameterMode thirdParam) {
      this.expected = expected;
      val = firstParam.resolveRead(vm, pc + 1);
      target = secondParam.resolveRead(vm, pc + 2);
      thirdParam.assertType(ParameterMode.POSITION);
    }

    @Override
    public IntCode.State execute(IntCode vm) {
      boolean cmp = val.value() != 0;
      if (cmp == expected) {
        vm.jumpTo(target.value());
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
    public String pretty() {
      return "jump to " + target.pretty() + " if " + val.pretty() + " is " + (expected ? "1" : "");
    }
  }

  class LessThan implements OpCode {
    private final ReadParameter first;
    private final ReadParameter second;
    private final WriteParameter target;

    public LessThan(IntCode vm, int pc, ParameterMode firstParam, ParameterMode secondParam, ParameterMode thirdParam) {
      first = firstParam.resolveRead(vm, pc + 1);
      second = secondParam.resolveRead(vm, pc + 2);
      target = thirdParam.resolveWrite(vm, pc + 3);
    }

    @Override
    public IntCode.State execute(IntCode vm) {
      int res = first.value() < second.value() ? 1 : 0;
      target.write(res);
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
    public String pretty() {
      return target.pretty() + " <- " + first.pretty() + " < " + second.pretty();
    }
  }

  class Equals implements OpCode {
    private final ReadParameter first;
    private final ReadParameter second;
    private final WriteParameter target;

    public Equals(IntCode vm, int pc, ParameterMode firstParam, ParameterMode secondParam, ParameterMode thirdParam) {
      first = firstParam.resolveRead(vm, pc + 1);
      second = secondParam.resolveRead(vm, pc + 2);
      target = thirdParam.resolveWrite(vm, pc + 3);
    }

    @Override
    public IntCode.State execute(IntCode vm) {
      int res = first.value() == second.value() ? 1 : 0;
      target.write(res);
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
    public String pretty() {
      return target.pretty() + " <- " + first.pretty() + " == " + second.pretty();
    }
  }

  class Halt implements OpCode {
    public Halt(IntCode vm, int pc, ParameterMode firstParam, ParameterMode secondParam, ParameterMode thirdParam) {
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
    public String pretty() {
      return "";
    }
  }
}
