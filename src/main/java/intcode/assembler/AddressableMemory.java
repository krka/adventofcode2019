package intcode.assembler;

import java.math.BigInteger;

class AddressableMemory implements ImmediateParameter {

  private final HasAddress hasAddress;
  private final int offset;

  public AddressableMemory(HasAddress hasAddress, int offset) {
    this.hasAddress = hasAddress;
    this.offset = offset;
  }

  @Override
  public ParameterMode mode() {
    return ParameterMode.POSITION;
  }

  @Override
  public BigInteger value() {
    return BigInteger.valueOf(hasAddress.getAddress() + offset);
  }

  @Override
  public AddressableMemory derefence() {
    throw new RuntimeException("Can't dereference already derefenced");
  }
}
