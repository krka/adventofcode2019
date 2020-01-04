package intcode.assembler;

import java.math.BigInteger;

class DeferredConstant implements ImmediateParameter {

  private final HasAddress hasAddress;
  private final int offset;

  public DeferredConstant(HasAddress hasAddress) {
    this(hasAddress, 0);
  }

  public DeferredConstant(HasAddress hasAddress, int offset) {
    this.hasAddress = hasAddress;
    this.offset = offset;
  }

  @Override
  public ParameterMode mode() {
    return ParameterMode.IMMEDIATE;
  }

  @Override
  public BigInteger value() {
    return BigInteger.valueOf(hasAddress.getAddress() + offset);
  }

}
