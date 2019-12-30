package intcode.assembler;

import java.math.BigInteger;

public class Address {
  public static Parameter of(Variable variable) {
    return new Parameter() {
      @Override
      public ParameterMode mode() {
        return ParameterMode.IMMEDIATE;
      }

      @Override
      public BigInteger value() {
        return variable.value();
      }
    };
  }

  public static Parameter position(BigInteger address) {
    return new Parameter() {

      @Override
      public ParameterMode mode() {
        return ParameterMode.POSITION;
      }

      @Override
      public BigInteger value() {
        return address;
      }
    };
  }

  public static Parameter withOffset(Op op, int offset) {
    return new Parameter() {
      @Override
      public ParameterMode mode() {
        return ParameterMode.POSITION;
      }

      @Override
      public BigInteger value() {
        return BigInteger.valueOf(op.getAddress() + offset);
      }
    };
  }
}
