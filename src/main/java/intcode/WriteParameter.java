package intcode;

import java.math.BigInteger;

public interface WriteParameter {
  void writeValue(IntCode vm, BigInteger value);
  String withValue(BigInteger value);

  BigInteger getConstantAddress();

  BigInteger getRelativeOffset();
}
