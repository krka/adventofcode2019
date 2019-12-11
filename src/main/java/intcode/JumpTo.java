package intcode;

import java.math.BigInteger;

public class JumpTo extends IntCodeException {
  private final BigInteger address;

  public JumpTo(BigInteger address) {
    this.address = address;
  }

  public BigInteger getAddress() {
    return address;
  }
}
