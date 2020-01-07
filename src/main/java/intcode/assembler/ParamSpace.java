package intcode.assembler;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class ParamSpace {
  private final List<Variable> variables = new ArrayList<>();

  private final String prefix;

  public ParamSpace(String prefix) {
    this.prefix = prefix;
  }

  public List<Variable> get(int n) {
    while (variables.size() < n) {
      int counter = variables.size();
      Variable variable = new Variable("int", prefix + counter, 1, new BigInteger[]{BigInteger.ZERO}, null, prefix + counter);
      variables.add(variable);
    }
    return variables.subList(0, n);
  }

  public List<Variable> getAll() {
    return variables;
  }
}
