package intcode.assembler;

public class StaticAllocation {
  final Variable variable;
  final int size;

  public StaticAllocation(Variable variable, int size) {
    this.variable = variable;
    this.size = size;
  }
}
