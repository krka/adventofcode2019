package intcode.assembler;

import java.math.BigInteger;
import java.util.concurrent.Callable;

class DeferredParameter implements Parameter {

  private final Callable<BigInteger> func;
  private final ParameterMode mode;

  private DeferredParameter(ParameterMode mode, Callable<BigInteger> func) {
    this.func = func;
    this.mode = mode;
  }

  public static DeferredParameter ofBigInt(ParameterMode mode, Callable<BigInteger> func) {
    return new DeferredParameter(mode, func);
  }

  public static DeferredParameter ofInt(ParameterMode mode, Callable<Integer> func) {
    return ofBigInt(mode, () -> BigInteger.valueOf(func.call()));
  }

  @Override
  public ParameterMode mode() {
    return mode;
  }

  @Override
  public BigInteger value() {
    try {
      return func.call();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public String toString() {
    if (mode == ParameterMode.IMMEDIATE) {
      return value().toString();
    } else if (mode == ParameterMode.POSITION) {
      return "mem[" + value() + "]";
    } else {
      throw new RuntimeException();
    }
  }

}
