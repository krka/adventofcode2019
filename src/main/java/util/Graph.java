package util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

public class Graph {

  public static <T> List<T> topo(T root, Function<T, Iterable<T>> dependencies) {
    Set<T> visited = new HashSet<>();
    ArrayList<T> res = new ArrayList<>();
    topo(root, res, visited, dependencies);
    return res;
  }

  private static <T> void topo(T node, ArrayList<T> res, Set<T> visited, Function<T, Iterable<T>> dependencies) {
    if (!visited.add(node)) {
      return;
    }
    Iterable<T> nodes = dependencies.apply(node);
    if (nodes != null) {
      for (T other : nodes) {
        topo(other, res, visited, dependencies);
      }
    }
    res.add(node);
  }
}
