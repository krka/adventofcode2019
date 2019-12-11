package intcode;

import java.math.BigInteger;

enum ParameterMode {
  POSITION {
    @Override
    protected ReadParameter resolveRead(BigInteger address) {
      return new ReadParameter() {
        @Override
        public BigInteger getValue(IntCode vm) {
          return vm.readValue(address);
        }

        @Override
        public String toString() {
          return "mem[" + address + "]";
        }

        @Override
        public String withValue(BigInteger value) {
          return toString() + ":" + value;
        }
      };
    }

    @Override
    protected WriteParameter resolveWrite(BigInteger address) {
      return new WriteParameter() {
        @Override
        public void writeValue(IntCode vm, BigInteger value) {
          vm.put(address, value);
        }

        @Override
        public String withValue(BigInteger value) {
          return toString() + ":" + value;
        }

        @Override
        public String toString() {
          return "mem[" + address + "]";
        }
      };
    }
  },
  IMMEDIATE {
    @Override
    protected ReadParameter resolveRead(BigInteger value) {
      return new ReadParameter() {
        @Override
        public BigInteger getValue(IntCode vm) {
          return value;
        }

        @Override
        public String withValue(BigInteger value) {
          return value.toString();
        }

        @Override
        public String toString() {
          return value.toString();
        }
      };
    }

    @Override
    protected WriteParameter resolveWrite(BigInteger value) {
      throw new IllegalStateException("Not allowed to write value in " + name() + " mode, at position " + value);
    }
  },
  RELATIVE {
    @Override
    protected ReadParameter resolveRead(BigInteger address) {
      return new ReadParameter() {
        @Override
        public BigInteger getValue(IntCode vm) {
          return vm.readValue(address.add(vm.getRelativeBase()));
        }

        @Override
        public String withValue(BigInteger value) {
          return toString() + ":" + value;
        }

        @Override
        public String toString() {
          return "mem[SP+" + address + "]";
        }
      };
    }

    @Override
    protected WriteParameter resolveWrite(BigInteger address) {
      return new WriteParameter() {
        @Override
        public void writeValue(IntCode vm, BigInteger value) {
          vm.put(address.add(vm.getRelativeBase()), value);
        }

        @Override
        public String withValue(BigInteger value) {
          return toString() + ":" + value;
        }

        @Override
        public String toString() {
          return "mem[SP+" + address + "]";
        }
      };
    }
  }
  ;

  static ParameterMode from(int i) {
    return ParameterMode.values()[i];
  }

  public void assertType(ParameterMode expected) {
    if (expected != this) {
      throw new RuntimeException("Expected " + expected.name() + " but got " + this.name());
    }
  }

  public ReadParameter resolveRead(Memory memory, BigInteger opcodeAddress, int offset) {
    BigInteger param = memory.read(opcodeAddress.add(BigInteger.valueOf(offset)));
    return resolveRead(param);
  }

  public WriteParameter resolveWrite(Memory memory, BigInteger opcodeAddress, int offset) {
    BigInteger param = memory.read(opcodeAddress.add(BigInteger.valueOf(offset)));
    return resolveWrite(param);
  }

  protected abstract WriteParameter resolveWrite(BigInteger param);

  protected abstract ReadParameter resolveRead(BigInteger param);
}
