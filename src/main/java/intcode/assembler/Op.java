package intcode.assembler;

public abstract class Op {
  private int address = -1;

  final void setAddress(int address) {
    this.address = address;
  }

  public final int getAddress() {
    if (address == -1) {
      throw new RuntimeException("Address not set");
    }
    return address;
  }

  abstract int size();

  void safeWrite(AnnotatedIntCode res) {
    assertAddress(res, getAddress());
    writeTo(res);
    assertAddress(res, getAddress() + size());
  }

  abstract protected void writeTo(AnnotatedIntCode res);

  private void assertAddress(AnnotatedIntCode res, int address) {
    if (res.pc() != address) {
      throw new RuntimeException("Address not aligned with result");
    }
  }
}
