package intcode.assembler;

import java.math.BigInteger;

public interface Parameter {
  ParameterMode mode();
  BigInteger value();

  default void assertNotImmediate() {
    if (mode() == ParameterMode.IMMEDIATE) {
      throw new RuntimeException("Expected not mode immediate");
    }
  }
}
