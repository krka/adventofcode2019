package intcode;

import java.math.BigInteger;

enum ParameterMode {
  POSITION {
    @Override
    public ReadParameter resolveRead(IntCode vm, BigInteger pc) {
      BigInteger address = vm.getParameter(pc);
      BigInteger value = vm.readValue(address);
      return new ReadParameter() {
        @Override
        public String pretty() {
          return "addr[" + address + "]:" + value;
        }

        @Override
        public BigInteger value() {
          return value;
        }
      };
    }

    @Override
    public WriteParameter resolveWrite(IntCode vm, BigInteger pc) {
      BigInteger address = vm.getParameter(pc);
      return new WriteParameter() {
        BigInteger value;
        @Override
        public String pretty() {
          String s = "addr[" + address + "]";
          if (value != null) {
            return s + ":" + value;
          } else {
            return s;
          }
        }

        @Override
        public void write(BigInteger value) {
          this.value = value;
          vm.put(address, value);
        }

      };
    }
  },
  IMMEDIATE {
    @Override
    public ReadParameter resolveRead(IntCode vm, BigInteger pc) {
      BigInteger value = vm.getParameter(pc);
      return new ReadParameter() {
        @Override
        public String pretty() {
          return value.toString();
        }

        @Override
        public BigInteger value() {
          return value;
        }
      };
    }

    @Override
    public WriteParameter resolveWrite(IntCode vm, BigInteger pc) {
      throw new IllegalStateException("Not allowed to write value in " + name() + " mode, at position " + pc);
    }
  },
  RELATIVE {
    @Override
    public ReadParameter resolveRead(IntCode vm, BigInteger pc) {
      BigInteger address = vm.getParameter(pc);
      BigInteger value = vm.readValue(address.add(vm.getRelativeBase()));
      return new ReadParameter() {
        @Override
        public String pretty() {
          return "addr[" + vm.getRelativeBase() + "+" + address + "]:" + value;
        }

        @Override
        public BigInteger value() {
          return value;
        }
      };
    }

    @Override
    public WriteParameter resolveWrite(IntCode vm, BigInteger pc) {
      BigInteger address = vm.getParameter(pc);
      return new WriteParameter() {
        BigInteger value;
        @Override
        public String pretty() {
          String s = "addr[" + vm.getRelativeBase() + "+" + address + "]";
          if (value != null) {
            return s + ":" + value;
          } else {
            return s;
          }
        }

        @Override
        public void write(BigInteger value) {
          this.value = value;
          vm.put(address.add(vm.getRelativeBase()), value);
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

  public abstract ReadParameter resolveRead(IntCode vm, BigInteger pc);

  public abstract WriteParameter resolveWrite(IntCode vm, BigInteger pc);
}
