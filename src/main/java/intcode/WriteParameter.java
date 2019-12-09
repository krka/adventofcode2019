package intcode;

import java.math.BigInteger;

public interface WriteParameter {
  String pretty();
  void write(BigInteger value);
}
