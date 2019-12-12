package intcode;

import java.math.BigInteger;

public class OpCodeEval {
  final BigInteger address1;
  final BigInteger address2;
  final BigInteger writeAddress;

  public OpCodeEval(BigInteger address1, BigInteger nextAddress2, BigInteger writeAddress) {
    this.address1 = address1;
    this.address2 = nextAddress2;
    this.writeAddress = writeAddress;
  }

  public static OpCodeEval jumpTo(BigInteger address1, BigInteger address2) {
    return new OpCodeEval(address1, address2, null);
  }

  public static OpCodeEval jumpTo(BigInteger address) {
    return new OpCodeEval(address, null, null);
  }
}
