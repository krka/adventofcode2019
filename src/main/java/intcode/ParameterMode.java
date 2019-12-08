package intcode;

enum ParameterMode {
  POSITION {
    @Override
    public ReadParameter resolveRead(IntCode vm, int pc) {
      int address = vm.getParameter(pc);
      int value = vm.readValue(address);
      return new ReadParameter() {
        @Override
        public String pretty() {
          return "addr[" + address + "]:" + value;
        }

        @Override
        public int value() {
          return value;
        }
      };
    }

    @Override
    public WriteParameter resolveWrite(IntCode vm, int pc) {
      int address = vm.getParameter(pc);
      return new WriteParameter() {
        Integer value;
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
        public void write(int value) {
          this.value = value;
          vm.put(address, value);
        }

      };
    }
  },
  IMMEDIATE {
    @Override
    public ReadParameter resolveRead(IntCode vm, int pc) {
      int value = vm.getParameter(pc);
      return new ReadParameter() {
        @Override
        public String pretty() {
          return Integer.toString(value);
        }

        @Override
        public int value() {
          return value;
        }
      };
    }

    @Override
    public WriteParameter resolveWrite(IntCode vm, int pc) {
      throw new IllegalStateException("Not allowed to write value in " + name() + " mode, at position " + pc);
    }
  };

  static ParameterMode from(int i) {
    return ParameterMode.values()[i];
  }

  public void assertType(ParameterMode expected) {
    if (expected != this) {
      throw new RuntimeException("Expected " + expected.name() + " but got " + this.name());
    }
  }

  public abstract ReadParameter resolveRead(IntCode vm, int pc);

  public abstract WriteParameter resolveWrite(IntCode vm, int pc);
}
