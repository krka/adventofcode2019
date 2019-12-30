package intcode.assembler;

import java.math.BigInteger;

class DeferredConstant implements ImmediateParameter {

  private final HasAddress hasAddress;

  public DeferredConstant(HasAddress hasAddress) {
    this.hasAddress = hasAddress;
  }

  @Override
  public ParameterMode mode() {
    return ParameterMode.IMMEDIATE;
  }

  @Override
  public BigInteger value() {
    return BigInteger.valueOf(hasAddress.getAddress());
  }

  @Override
  public DeferredConstant derefence() {
    throw new RuntimeException("Can't dereference a constant");
  }
}
