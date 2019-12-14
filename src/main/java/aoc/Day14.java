package aoc;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Day14 {
  public static final BigInteger TRILLION = BigInteger.valueOf(1000000000000L);

  private final Map<String, Req> mapping = new HashMap<>();
  private final Map<String, BigInteger> required = new HashMap<>();
  private final List<String> topo;

  public Day14(String s) throws IOException {
    try (BufferedReader reader = new BufferedReader(Util.fromResource(s))) {
      while (true) {
        String line = reader.readLine();
        if (line == null || line.isEmpty()) {
          break;
        }
        String[] parts = line.split("=>");
        String target = parts[1].trim();
        ArrayList<Resource> list = new ArrayList<>();
        for (String part : parts[0].split(",")) {
          list.add(parseResource(part));
        }
        Resource targetR = parseResource(target);
        Req prev = mapping.put(targetR.name, new Req(targetR, list));
        if (prev != null) {
          throw new RuntimeException("Multiple ways to produce " + targetR.name);
        }
      }
    }
    topo = topo();
    Collections.reverse(topo);
  }

  private Resource parseResource(String x1) {
    String[] split = x1.trim().split(" ");
    int count = Integer.parseInt(split[0].trim());
    String name = split[1];
    return new Resource(count, name);
  }

  public List<String> topo() {
    Set<String> visited = new HashSet<>();
    ArrayList<String> res = new ArrayList<>();
    topo("FUEL", res, visited);
    return res;
  }

  private void topo(String node, ArrayList<String> res, Set<String> visited) {
    if (!visited.add(node)) {
      return;
    }
    Req req = mapping.get(node);
    if (req == null) {
      res.add(node);
      return;
    }

    for (Resource other : req.list) {
      topo(other.name, res, visited);
    }
    res.add(node);
  }

  public BigInteger solve(BigInteger fuel) {
    required.clear();
    required.put("FUEL", fuel);
    for (String node : topo) {
      consume(node);
    }
    return required.get("ORE");
  }

  private void consume(String name) {
    Req req = mapping.get(name);
    if (req == null) {
      return;
    }

    BigInteger required = this.required.getOrDefault(name, BigInteger.ZERO);
    long c = req.target.count;

    BigInteger a = required.add(BigInteger.valueOf(c - 1)).divide(BigInteger.valueOf(c));
    for (Resource other : req.list) {
      BigInteger prev = this.required.getOrDefault(other.name, BigInteger.ZERO);
      BigInteger newValue = prev.add(a.multiply(BigInteger.valueOf(other.count)));
      this.required.put(other.name, newValue);
    }
  }

  public BigInteger solvePart2() {
    BigInteger max = BigInteger.valueOf(10000000000L);
    BigInteger min = BigInteger.ZERO;
    while (min.compareTo(max) < 0) {
      BigInteger test = min.add(max).divide(BigInteger.TWO);
      BigInteger solve = solve(test);
      if (solve.compareTo(TRILLION) > 0) {
        max = test.subtract(BigInteger.ONE);
      } else {
        min = test;
      }
    }
    return min;
  }

  private static class Resource {
    long count;
    String name;

    public Resource(long count, String name) {
      this.count = count;
      this.name = name;
    }

    @Override
    public String toString() {
      return "Resource{" +
              "count=" + count +
              ", name='" + name + '\'' +
              '}';
    }
  }

  private class Req {
    private final Resource target;
    private final ArrayList<Resource> list;

    public Req(Resource target, ArrayList<Resource> list) {
      this.target = target;
      this.list = list;
    }

    @Override
    public String toString() {
      return "Req{" +
              "target=" + target +
              ", list=" + list +
              '}';
    }
  }
}
