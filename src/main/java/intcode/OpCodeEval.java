package intcode;

import java.math.BigInteger;

public class OpCodeEval {
  final BigInteger nextOperation;
  final BigInteger jumpTarget;
  final BigInteger relativeJumpTarget;
  final BigInteger writeAddress;
  final BigInteger writeOffset;
  final BigInteger writeValue;

  public OpCodeEval(BigInteger nextOperation, BigInteger jumpTarget, BigInteger relativeJumpTarget, BigInteger writeAddress, BigInteger writeOffset, BigInteger writeValue) {
    this.nextOperation = nextOperation;
    this.jumpTarget = jumpTarget;
    this.relativeJumpTarget = relativeJumpTarget;
    this.writeAddress = writeAddress;
    this.writeOffset = writeOffset;
    this.writeValue = writeValue;
  }

}
