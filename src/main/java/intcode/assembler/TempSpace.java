package intcode.assembler;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TempSpace {
  private final Set<Variable> allVariables = new HashSet<>();
  private final List<Variable> ordering = new ArrayList<>();
  private final Set<Variable> available = new HashSet<>();

  private final String prefix;
  private int counter = 0;

  public TempSpace(String prefix) {
    this.prefix = prefix;
  }

  public Variable getAny() {
    if (!available.isEmpty()) {
      Variable variable = available.iterator().next();
      available.remove(variable);
      return variable;
    }

    Variable variable = new Variable("int", prefix + counter, 1, new BigInteger[]{BigInteger.ZERO}, null, prefix + counter);
    counter++;
    allVariables.add(variable);
    ordering.add(variable);
    return variable;
  }

  public void release(Variable variable) {
    if (!allVariables.contains(variable)) {
      throw new RuntimeException("Can't release unrelated variable");
    }
    if (!available.add(variable)) {
      throw new RuntimeException("Already released!");
    }
  }

  public List<Variable> getAll() {
    if (available.size() != ordering.size()) {
      throw new RuntimeException("All variables have not been released");
    }
    return ordering;
  }
}
