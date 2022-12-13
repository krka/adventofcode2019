package aoc2022;

import org.petitparser.parser.Parser;
import org.petitparser.parser.combinators.SettableParser;
import org.petitparser.parser.primitive.CharacterParser;
import util.Day;
import util.Util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Day13 implements Day {
  private final List<String> input;
  public static final Parser PARSER = createParser();

  private static Parser createParser() {
    final Parser posInt = CharacterParser.digit().plus().flatten().map((String s) -> Integer.parseInt(s));
    final Parser comma = CharacterParser.of(',');
    final Parser left = CharacterParser.of('[');
    final Parser right = CharacterParser.of(']');

    final SettableParser element = SettableParser.undefined();
    final SettableParser list = SettableParser.undefined();

    final Parser commaElement = comma.seq(element).map((List o) -> o.get(1));
    final Parser commaSequence = element.seq(commaElement.star()).map((List o) -> flattenTail(o));
    list.set(left.seq(commaSequence.optional(), right).map((List o) -> fixEmptyList((List) o.get(1))));

    element.set(posInt.or(list));

    return list.end();
  }

  private static List flattenTail(List o) {
    final ArrayList res = new ArrayList();
    res.add(o.get(0));
    res.addAll((Collection) o.get(1));
    return res;
  }

  private static List fixEmptyList(List o) {
    if (o == null) {
      return List.of();
    }
    return o;
  }

  public Day13(String name) {
    input = Util.readResource(name);
  }

  @Override
  public long solvePart1() {
    long sum = 0;
    int numPairs= input.size() / 3;
    for (int i = 0; i < numPairs; i++) {
      final String s1 = input.get(3*i);
      final String s2 = input.get(3*i + 1);
      sum += solve(i + 1, s1, s2);
    }
    return sum;
  }

  private long solve(int index, String s1, String s2) {
    List parsed1 = PARSER.parse(s1).get();
    List parsed2 = PARSER.parse(s2).get();
    final int c = compare(parsed1, parsed2);
    if (c < 0) {
      return index;
    }
    return 0;
  }

  private int compare(List parsed1, List parsed2) {
    int n = Math.min(parsed1.size(), parsed2.size());
    for (int i = 0; i < n; i++) {
      final Object v1 = parsed1.get(i);
      final Object v2 = parsed2.get(i);
      if (v1 instanceof List && v2 instanceof List) {
        final int c = compare((List) v1, (List) v2);
        if (c != 0) {
          return c;
        }
      } else if (v1 instanceof List) {
        final int c = compare((List) v1, List.of(v2));
        if (c != 0) {
          return c;
        }
      } else if (v2 instanceof List) {
        final int c = compare(List.of(v1), (List) v2);
        if (c != 0) {
          return c;
        }
      } else {
        int i1 = ((Integer) v1).intValue();
        int i2 = ((Integer) v2).intValue();
        int c = i1 - i2;
        if (c != 0) {
          return c;
        }
      }
    }
    return parsed1.size() - parsed2.size();
  }

  @Override
  public long solvePart2() {
    List<List> packets = new ArrayList();
    int numPairs = input.size() / 3;
    for (int i = 0; i < numPairs; i++) {
      final String s1 = input.get(3*i);
      final String s2 = input.get(3*i + 1);
      packets.add(PARSER.parse(s1).get());
      packets.add(PARSER.parse(s2).get());
    }
    List p2 = List.of(List.of(2));
    List p6 = List.of(List.of(6));
    packets.add(p2);
    packets.add(p6);
    packets.forEach(System.out::println);
    Collections.sort(packets, this::compare);
    final int p2index = packets.indexOf(p2) + 1;
    final int p6index = packets.indexOf(p6) + 1;
    return p2index * p6index;
  }
}


