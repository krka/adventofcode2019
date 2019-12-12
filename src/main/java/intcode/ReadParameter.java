package intcode;

import java.math.BigInteger;

public interface ReadParameter {
  BigInteger getValue(IntCode vm);
  String withValue(BigInteger value);

  BigInteger getConstant();
}
