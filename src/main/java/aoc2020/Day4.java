package aoc2020;

import util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day4 {
  private static final Set<String> REQUIRED_FIELDS = Stream.of("byr iyr eyr hgt hcl ecl pid".split(" ")).collect(Collectors.toSet());
  private static final Set<String> EYE_COLORS = Stream.of("amb blu brn gry grn hzl oth".split(" ")).collect(Collectors.toSet());

  private final List<String> input;

  public Day4(String name) {
    input = Util.readResource(name);
  }

  public long solvePart1() {
    return readPassports().stream().filter(this::hasRequiredFields).count();
  }

  public long solvePart2() {
    return readPassports().stream().filter(this::isValid).count();
  }

  private boolean hasRequiredFields(Map<String, String> passport) {
    HashSet<String> objects = new HashSet<>();
    objects.addAll(REQUIRED_FIELDS);
    objects.removeAll(passport.keySet());
    return objects.isEmpty();
  }

  private boolean isValid(Map<String, String> passport) {
    if (!hasRequiredFields(passport)) {
      return false;
    }
    String byr = passport.get("byr");
    if (byr.length() != 4 || !inRange(byr, 1920, 2002)) {
      return false;
    }
    String iyr = passport.get("iyr");
    if (iyr.length() != 4 || !inRange(iyr, 2010, 2020)) {
      return false;
    }
    String eyr = passport.get("eyr");
    if (eyr.length() != 4 || !inRange(eyr, 2020,2030)) {
      return false;
    }
    String hgt = passport.get("hgt");
    if (hgt.endsWith("cm")) {
      int cm = Integer.parseInt(hgt.substring(0, hgt.length() - 2));
      if (cm < 150 || cm > 193) {
        return false;
      }
    } else if (hgt.endsWith("in")) {
      int in = Integer.parseInt(hgt.substring(0, hgt.length() - 2));
      if (in < 59 || in > 76) {
        return false;
      }
    } else {
      return false;
    }
    String hcl = passport.get("hcl");
    if (!hcl.matches("#[0-9a-f]{6}")) {
      return false;
    }
    String ecl = passport.get("ecl");
    if (!EYE_COLORS.contains(ecl)) {
      return false;
    }
    String s = passport.get("pid");
    if (!s.matches("[0-9]{9}")) {
      return false;
    }
    return true;
  }

  private List<Map<String, String>> readPassports() {
    return input.stream().collect(Util.toPartitions((o, o2) -> !o2.isEmpty())).stream()
            .map(lines -> {
              Map<String, String> passport = new HashMap<>();
              lines.forEach(s -> {
                String[] parts = s.split(" ");
                for (String part : parts) {
                  String[] split = part.split(":");
                  if (split.length == 2) {
                    passport.put(split[0], split[1]);
                  }
                }
              });
              return passport;
            })
            .collect(Collectors.toList());
  }
  private boolean inRange(String s, int min, int max) {
    int val = Integer.parseInt(s);
    return val >= min && val <= max;
  }

}
