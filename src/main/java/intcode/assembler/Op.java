package intcode.assembler;

import java.math.BigInteger;
import java.util.List;

abstract class Op implements HasAddress {
  private int address = -1;

  final void setAddress(int address) {
    this.address = address;
  }

  @Override
  public final int getAddress() {
    if (address == -1) {
      throw new RuntimeException("Address not set");
    }
    return address;
  }

  abstract int size();

  void safeWrite(List<BigInteger> res) {
    assertAddress(res, getAddress());
    writeTo(res);
    assertAddress(res, getAddress() + size());
  }

  abstract protected void writeTo(List<BigInteger> res);

  private void assertAddress(List<BigInteger> res, int address) {
    if (res.size() != address) {
      throw new RuntimeException("Address not aligned with result");
    }
  }
}
