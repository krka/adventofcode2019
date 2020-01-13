package intcode.assembler;

import java.math.BigInteger;

public class Placeholder implements Parameter {

  private static final Placeholder INSTANCE = new Placeholder();

  private Placeholder() {
  }

  public static Placeholder get() {
    return INSTANCE;
  }

  @Override
  public ParameterMode mode() {
    return ParameterMode.POSITION;
  }

  @Override
  public BigInteger value() {
    return BigInteger.ZERO;
  }

  @Override
  public String toString() {
    return "[???]";
  }
}
