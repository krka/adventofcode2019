package intcode;

import java.math.BigInteger;
import java.util.function.Consumer;

public interface OpCode {
  int size();

  void execute(IntCode vm, Consumer<String> debug) throws IntCodeException;

  BigInteger address();

  default BigInteger nextAddress() {
    return address().add(BigInteger.valueOf(size()));
  }

  default OpCodeEval evaluate() {
    return new OpCodeEval(nextAddress(), null, null, null, null, null);
  }

  default OpCodeEval evaluateWriteOperation(WriteParameter writeTarget, BigInteger writeValue) {
    BigInteger writeAddress = writeTarget.getConstantAddress();
    BigInteger offset = writeTarget.getRelativeOffset();
    if (writeAddress != null) {
      return new OpCodeEval(nextAddress(), null, null, writeAddress, null, writeValue);
    } else if (offset != null) {
      return new OpCodeEval(nextAddress(), null, null, null, offset, writeValue);
    }
    throw new RuntimeException("Non-constant memory target at " + address());

  }

  static OpCode decode(Memory memory, BigInteger address) {
    int opcode = memory.read(address).intValueExact();
    int op = opcode % 100;
    ParameterMode param1 = ParameterMode.from((opcode / 100) % 10);
    ParameterMode param2 = ParameterMode.from((opcode / 1000) % 10);
    ParameterMode param3 = ParameterMode.from((opcode / 10000) % 10);
    switch (op) {
      case 1: return new Add(memory, address, param1, param2, param3);
      case 2: return new Mul(memory, address, param1, param2, param3);
      case 3: return new Input(memory, address, param1, param2, param3);
      case 4: return new Output(memory, address, param1, param2, param3);
      case 5: return new JumpIf(false, memory, address, param1, param2, param3);
      case 6: return new JumpIf(true, memory, address, param1, param2, param3);
      case 7: return new LessThan(memory, address, param1, param2, param3);
      case 8: return new Equals(memory, address, param1, param2, param3);
      case 9: return new SetRelBase(memory, address, param1, param2, param3);
      case 99: return Halt.INSTANCE;
      default: throw new RuntimeException("Unknown opcode: " + op + " at address " + address);
    }
  }

  class Add implements OpCode {
    private final BigInteger address;

    private final ReadParameter param1;
    private final ReadParameter param2;
    private final WriteParameter target;

    public Add(Memory memory, BigInteger address, ParameterMode param1, ParameterMode param2, ParameterMode param3) {
      this.address = address;
      this.param1 = param1.resolveRead(memory, address, 1);
      this.param2 = param2.resolveRead(memory, address, 2);
      this.target = param3.resolveWrite(memory, address, 3);
    }

    public String toString() {
      return String.format("ADD: %s = %s + %s", target, param1, param2);
    }

    @Override
    public int size() {
      return 4;
    }

    @Override
    public void execute(IntCode vm, Consumer<String> debug) {
      BigInteger value1 = param1.getValue(vm);
      BigInteger value2 = param2.getValue(vm);
      BigInteger result = value1.add(value2);
      target.writeValue(vm, result);
      debug.accept(String.format("ADD: %s = %s + %s",
              target.withValue(result), param1.withValue(value1), param2.withValue(value2)));
    }

    @Override
    public BigInteger address() {
      return address;
    }

    @Override
    public OpCodeEval evaluate() {
      BigInteger v1 = param1.getConstant();
      BigInteger v2 = param2.getConstant();
      BigInteger writeValue = null;
      if (v1 != null && v2 != null) {
        writeValue = v1.add(v2);
      }
      return evaluateWriteOperation(target, writeValue);
    }
  }

  class Mul implements OpCode {
    private final BigInteger address;

    private final ReadParameter param1;
    private final ReadParameter param2;
    private final WriteParameter target;

    public Mul(Memory memory, BigInteger address, ParameterMode param1, ParameterMode param2, ParameterMode param3) {
      this.address = address;
      this.param1 = param1.resolveRead(memory, address, 1);
      this.param2 = param2.resolveRead(memory, address, 2);
      this.target = param3.resolveWrite(memory, address, 3);
    }

    public String toString() {
      return String.format("MUL: %s = %s * %s", target, param1, param2);
    }

    @Override
    public int size() {
      return 4;
    }

    @Override
    public void execute(IntCode vm, Consumer<String> debug) {
      BigInteger value1 = param1.getValue(vm);
      BigInteger value2 = param2.getValue(vm);
      BigInteger result = value1.multiply(value2);
      target.writeValue(vm, result);
      debug.accept(String.format("MUL: %s = %s * %s",
              target.withValue(result), param1.withValue(value1), param2.withValue(value2)));
    }

    @Override
    public BigInteger address() {
      return address;
    }

    @Override
    public OpCodeEval evaluate() {
      BigInteger v1 = param1.getConstant();
      BigInteger v2 = param2.getConstant();
      BigInteger writeValue = null;
      if (v1 != null && v2 != null) {
        writeValue = v1.multiply(v2);
      } else if (BigInteger.ZERO.equals(v1) || BigInteger.ZERO.equals(v2)) {
        writeValue = BigInteger.ZERO;
      }
      return evaluateWriteOperation(target, writeValue);
    }
  }

  class Input implements OpCode {
    private final BigInteger address;

    private final WriteParameter target;

    public Input(Memory memory, BigInteger address, ParameterMode param1, ParameterMode param2, ParameterMode param3) {
      this.address = address;
      target = param1.resolveWrite(memory, address, 1);
      param2.assertType(ParameterMode.POSITION);
      param3.assertType(ParameterMode.POSITION);
    }

    @Override
    public String toString() {
      return "INPUT: " + target;
    }

    @Override
    public int size() {
      return 2;
    }

    @Override
    public void execute(IntCode vm, Consumer<String> debug) throws WaitForInput {
      BigInteger value = vm.pollStdin();
      if (value == null) {
        throw WaitForInput.INSTANCE;
      }
      target.writeValue(vm, value);
      debug.accept(String.format("INPUT: %s", target.withValue(value)));
    }

    @Override
    public BigInteger address() {
      return address;
    }

    @Override
    public OpCodeEval evaluate() {
      return evaluateWriteOperation(target, null);
    }
  }

  class Output implements OpCode {
    private final BigInteger address;
    private final ReadParameter source;

    public Output(Memory memory, BigInteger address, ParameterMode param1, ParameterMode param2, ParameterMode param3) {
      this.address = address;
      source = param1.resolveRead(memory, address, 1);
      param2.assertType(ParameterMode.POSITION);
      param3.assertType(ParameterMode.POSITION);
    }

    @Override
    public String toString() {
      return "OUTPUT: " + source;
    }

    @Override
    public int size() {
      return 2;
    }

    @Override
    public void execute(IntCode vm, Consumer<String> debug) {
      BigInteger value = source.getValue(vm);
      vm.writeStdout(value);
      debug.accept("OUTPUT: " + source.withValue(value));
    }

    @Override
    public BigInteger address() {
      return address;
    }
  }

  class JumpIf implements OpCode {
    public static final int SIZE = 3;
    public static final BigInteger BIG_SIZE = BigInteger.valueOf(SIZE);
    private final BigInteger address;
    private final boolean zero;
    private final ReadParameter target;
    private final ReadParameter test;
    private final String zeroString;

    public JumpIf(boolean zero, Memory memory, BigInteger address, ParameterMode param1, ParameterMode param2, ParameterMode param3) {
      this.zero = zero;
      this.zeroString = zero ? "0" : "not 0";
      this.address = address;
      test = param1.resolveRead(memory, address, 1);
      target = param2.resolveRead(memory, address, 2);
      param3.assertType(ParameterMode.POSITION);
    }

    @Override
    public String toString() {
      return String.format("JUMP: Jump to %s if (%s is %s)", target, test, zeroString);
    }

    @Override
    public int size() {
      return SIZE;
    }

    @Override
    public void execute(IntCode vm, Consumer<String> debug) throws JumpTo {
      BigInteger value = test.getValue(vm);
      boolean isZero = value.equals(BigInteger.ZERO);
      BigInteger targetValue = target.getValue(vm);
      boolean doJump = isZero == zero;
      debug.accept(String.format(
              "JUMP: Jump to %s if (%s is %s): %s",
              target.withValue(targetValue), test.withValue(value), zeroString, doJump ? "Jumped!": "No jump!"));
      if (doJump) {
        throw new JumpTo(targetValue);
      }
    }

    @Override
    public BigInteger address() {
      return null;
    }

    @Override
    public OpCodeEval evaluate() {
      BigInteger value = test.getConstant();
      BigInteger targetValue = target.getConstant();
      BigInteger targetOffset = target.getOffset();
      BigInteger nextOp = address.add(BigInteger.valueOf(size()));
      if (value == null) {
        if (targetValue == null) {
          throw new RuntimeException("Non-constant jump target at " + address);
        }
        return new OpCodeEval(nextOp, targetValue, null, null, null, targetValue);
      } else {
        boolean isZero = value.equals(BigInteger.ZERO);
        boolean doJump = isZero == zero;
        if (doJump) {
          if (targetValue == null && targetOffset == null) {
            throw new RuntimeException("Non-constant jump target at " + address);
          }
          return new OpCodeEval(null, targetValue, targetOffset, null, null, null);
        } else {
          // This is a no-op
          return new OpCodeEval(nextOp, null, null, null, null, null);
        }
      }
    }
  }

  class LessThan implements OpCode {
    private final BigInteger address;

    private final ReadParameter param1;
    private final ReadParameter param2;
    private final WriteParameter target;

    public LessThan(Memory memory, BigInteger address, ParameterMode param1, ParameterMode param2, ParameterMode param3) {
      this.address = address;
      this.param1 = param1.resolveRead(memory, address, 1);
      this.param2 = param2.resolveRead(memory, address, 2);
      this.target = param3.resolveWrite(memory, address, 3);
    }

    @Override
    public String toString() {
      return String.format("LESS_THAN: %s = %s < %s", target, param1, param2);
    }

    @Override
    public int size() {
      return 4;
    }

    @Override
    public void execute(IntCode vm, Consumer<String> debug)  {
      BigInteger value1 = param1.getValue(vm);
      BigInteger value2 = param2.getValue(vm);
      BigInteger targetValue = compare(value1, value2);
      target.writeValue(vm, targetValue);
      debug.accept(String.format("LESS_THAN: %s = %s < %s",
              target.withValue(targetValue), param1.withValue(value1), param2.withValue(value2)));
    }

    private BigInteger compare(BigInteger value1, BigInteger value2) {
      if (value1.compareTo(value2) < 0) {
        return BigInteger.ONE;
      } else {
        return BigInteger.ZERO;
      }
    }

    @Override
    public BigInteger address() {
      return address;
    }

    @Override
    public OpCodeEval evaluate() {
      BigInteger v1 = param1.getConstant();
      BigInteger v2 = param2.getConstant();
      BigInteger writeValue = null;
      if (v1 != null && v2 != null) {
        writeValue = compare(v1, v2);
      }
      return evaluateWriteOperation(target, writeValue);
    }
  }

  class Equals implements OpCode {
    private final BigInteger address;

    private final ReadParameter param1;
    private final ReadParameter param2;
    private final WriteParameter target;

    public Equals(Memory memory, BigInteger address, ParameterMode param1, ParameterMode param2, ParameterMode param3) {
      this.address = address;
      this.param1 = param1.resolveRead(memory, address, 1);
      this.param2 = param2.resolveRead(memory, address, 2);
      this.target = param3.resolveWrite(memory, address, 3);
    }

    @Override
    public String toString() {
      return String.format("EQUALS: %s = %s == %s", target, param1, param2);
    }

    @Override
    public int size() {
      return 4;
    }

    @Override
    public void execute(IntCode vm, Consumer<String> debug) {
      BigInteger value1 = param1.getValue(vm);
      BigInteger value2 = param2.getValue(vm);
      BigInteger targetValue = compare(value1, value2);
      target.writeValue(vm, targetValue);
      debug.accept(String.format("EQUALS: %s = %s == %s",
              target.withValue(targetValue), param1.withValue(value1), param2.withValue(value2)));
    }

    private BigInteger compare(BigInteger value1, BigInteger value2) {
      if (value1.compareTo(value2) == 0) {
        return BigInteger.ONE;
      } else {
        return BigInteger.ZERO;
      }
    }

    @Override
    public BigInteger address() {
      return address;
    }

    @Override
    public OpCodeEval evaluate() {
      BigInteger v1 = param1.getConstant();
      BigInteger v2 = param2.getConstant();
      BigInteger writeValue = null;
      if (v1 != null && v2 != null) {
        writeValue = compare(v1, v2);
      }
      return evaluateWriteOperation(target, writeValue);
    }
  }

  class SetRelBase implements OpCode {
    private final ReadParameter value;

    private final BigInteger address;

    public SetRelBase(Memory memory, BigInteger address, ParameterMode param1, ParameterMode param2, ParameterMode param3) {
      this.address = address;
      value = param1.resolveRead(memory, address, 1);
      param2.assertType(ParameterMode.POSITION);
      param3.assertType(ParameterMode.POSITION);
    }

    @Override
    public String toString() {
      return "SP += " + value;
    }

    @Override
    public int size() {
      return 2;
    }

    @Override
    public void execute(IntCode vm, Consumer<String> debug) {
      BigInteger v = value.getValue(vm);
      vm.adjustRelativeBase(v);
      String s = value.withValue(v);
      if (s.startsWith("-")) {
        debug.accept("SP -= " + s.substring(1));
      } else {
        debug.accept("SP += " + s);

      }
    }

    @Override
    public BigInteger address() {
      return address;
    }
  }

  class Halt extends IntCodeException implements OpCode {
    static final Halt INSTANCE = new Halt();

    @Override
    public String toString() {
      return "HALT";
    }

    @Override
    public int size() {
      return 1;
    }

    @Override
    public void execute(IntCode vm, Consumer<String> debug) throws Halt {
      debug.accept("HALT");
      throw this;
    }

    @Override
    public BigInteger address() {
      return null;
    }

    @Override
    public OpCodeEval evaluate() {
      return new OpCodeEval(null, null, null, null, null, null);
    }
  }

}

