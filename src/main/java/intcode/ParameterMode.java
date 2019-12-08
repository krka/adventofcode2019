package intcode;

enum ParameterMode {
  POSITION {
    @Override
    int readParameter(IntCode vm, int pc) {
      int address = vm.getParameter(pc);
      return vm.readValue(address);
    }

    @Override
    public void writeValue(IntCode vm, int pc, int value) {
      int address = vm.getParameter(pc);
      vm.put(address, value);
    }

    @Override
    public String pretty(IntCode vm, int pc) {
      int address = vm.getParameter(pc);
      return "program[" + address + "]";
    }
  },
  IMMEDIATE {
    @Override
    int readParameter(IntCode vm, int pc) {
      return vm.getParameter(pc);
    }

    @Override
    public void writeValue(IntCode vm, int pc, int value) {
      throw new IllegalStateException("Not allowed to write value in " + name() + " mode, at position " + pc);
    }

    @Override
    public String pretty(IntCode vm, int pc) {
      return Integer.toString(vm.getParameter(pc));
    }
  };

  abstract int readParameter(IntCode vm, int pc);

  public abstract void writeValue(IntCode vm, int pc, int value);

  public abstract String pretty(IntCode vm, int pc);

  static ParameterMode from(int i) {
    return ParameterMode.values()[i];
  }

  public void assertType(ParameterMode expected) {
    if (expected != this) {
      throw new RuntimeException("Expected " + expected.name() + " but got " + this.name());
    }
  }
}
