package intcode.assembler;

import java.math.BigInteger;

public class Address {

  public static Parameter placeHolder() {
    return new PlaceHolderPositionParameter();
  }

  private static class PlaceHolderPositionParameter implements Parameter {

    @Override
    public ParameterMode mode() {
      return ParameterMode.POSITION;
    }

    @Override
    public BigInteger value() {
      return BigInteger.ZERO;
    }

    @Override
    public ImmediateParameter dereference() {
      throw new RuntimeException("Can't derefence a placeholder");
    }
  }
}
