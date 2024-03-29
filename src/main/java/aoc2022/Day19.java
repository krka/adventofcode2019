package aoc2022;

import util.Day;
import util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Day19 implements Day {

  private final List<String> input;
  private final List<Blueprint> blueprints;

  public Day19(String name) {
    input = Util.readResource(name);
    blueprints = new ArrayList<>();
    for (String s : input) {
      if (s.isEmpty()) {
        continue;
      }
      // Blueprint 1: Each ore robot costs 3 ore. Each clay robot costs 3 ore. Each obsidian robot costs 3 ore and 20 clay. Each geode robot costs 2 ore and 12 obsidian.
      final String[] split = s.split("[ :]+");
      int id = Integer.parseInt(split[1]);
      int oreRobotOreCost = Integer.parseInt(split[6]);
      int clayRobotOreCost = Integer.parseInt(split[12]);
      int obsidianRobotOreCost = Integer.parseInt(split[18]);
      int obsidianRobotClayCost = Integer.parseInt(split[21]);
      int geodeRobotOreCost = Integer.parseInt(split[27]);
      int geodeRobotObsidianCost = Integer.parseInt(split[30]);

      final Blueprint blueprint = new Blueprint(id, oreRobotOreCost, clayRobotOreCost, obsidianRobotOreCost, obsidianRobotClayCost, geodeRobotOreCost, geodeRobotObsidianCost);
      blueprints.add(blueprint);
    }
  }


  @Override
  public long solvePart1() {
    return blueprints.parallelStream().mapToLong(blueprint -> {
      var cache = new HashMap<Key, Long>();
      long maxGeodes = blueprint.solve(24, 1, 0, 0, 0, 0, 0, 0, cache);
      System.out.println("id " + blueprint.id + ", " + maxGeodes);
      return maxGeodes * blueprint.id;
    }).sum();
  }

  @Override
  public long solvePart2() {
    return blueprints.parallelStream().limit(3).mapToLong(blueprint -> {
      var cache = new HashMap<Key, Long>();
      long maxGeodes = blueprint.solve(32, 1, 0, 0, 0, 0, 0, 0, cache);
      System.out.println("id " + blueprint.id + ", " + maxGeodes);
      return maxGeodes;
    }).reduce((a,b) -> a * b).getAsLong();
  }

  private static class Blueprint {
    private final int id;
    private final int oreRobotOreCost;

    private final int clayRobotOreCost;

    private final int obsidianRobotOreCost;
    private final int obsidianRobotClayCost;

    private final int geodeRobotOreCost;
    private final int geodeRobotObsidianCost;
    private final int maxOreUsage;
    private final int maxClayUsage;
    private final int maxObsidianUsage;

    public Blueprint(int id, int oreRobotOreCost, int clayRobotOreCost, int obsidianRobotOreCost, int obsidianRobotClayCost, int geodeRobotOreCost, int geodeRobotObsidianCost) {
      this.id = id;
      this.oreRobotOreCost = oreRobotOreCost;
      this.clayRobotOreCost = clayRobotOreCost;
      this.obsidianRobotOreCost = obsidianRobotOreCost;
      this.obsidianRobotClayCost = obsidianRobotClayCost;
      this.geodeRobotOreCost = geodeRobotOreCost;
      this.geodeRobotObsidianCost = geodeRobotObsidianCost;
      maxOreUsage = Math.max(geodeRobotOreCost, Math.max(obsidianRobotOreCost, Math.max(oreRobotOreCost, clayRobotOreCost)));
      maxClayUsage = obsidianRobotClayCost;
      maxObsidianUsage = geodeRobotObsidianCost;
    }

    public long solve(int timeLeft,
                      int oreRobots, int ore,
                      int clayRobots, int clay,
                      int obsidianRobots, int obsidian,
                      int geodeRobots,
                      Map<Key, Long> cache) {
      final Key key = new Key(timeLeft, oreRobots, ore, clayRobots, clay, obsidianRobots, obsidian, geodeRobots);
      final Long cached = cache.get(key);
      if (cached != null) {
        return cached;
      }
      if (timeLeft == 0) {
        return 0;
      }

      boolean needMoreOre = oreRobots < maxOreUsage;
      boolean needMoreObsidian = obsidianRobots < geodeRobotObsidianCost;
      boolean needMoreClay = needMoreObsidian && clayRobots < obsidianRobotClayCost;

      long best = 0;
      // Alt 4: build a geode robot
      if (ore >= geodeRobotOreCost && obsidian >= geodeRobotObsidianCost) {
        long alt = geodeRobots + solve(timeLeft - 1,
                oreRobots, needMoreOre ? ore - geodeRobotOreCost + oreRobots : maxOreUsage,
                clayRobots, needMoreClay ? clayRobots + clay : clayRobots,
                obsidianRobots, needMoreObsidian ? obsidianRobots + obsidian - geodeRobotObsidianCost : obsidianRobots,
                geodeRobots + 1, cache);
        best = Math.max(best, alt);

        cache.put(key, best);
        return best;
      }

      // Alt 3: build an obsidian robot
      if (ore >= obsidianRobotOreCost && clay >= obsidianRobotClayCost) {
        if (needMoreObsidian) {
          long alt = geodeRobots + solve(timeLeft - 1,
                  oreRobots, needMoreOre ? ore - obsidianRobotOreCost + oreRobots : maxOreUsage,
                  clayRobots, needMoreClay ? clayRobots + clay - obsidianRobotClayCost : clayRobots,
                  obsidianRobots + 1, obsidianRobots + obsidian,
                  geodeRobots, cache);
          best = Math.max(best, alt);
        }
      }

      // Alt 2: build an ore robot
      if (ore >= oreRobotOreCost) {
        if (needMoreOre) {
          long alt = geodeRobots + solve(timeLeft - 1,
        oreRobots + 1, ore - oreRobotOreCost + oreRobots,
                  clayRobots, needMoreClay ? clayRobots + clay : clayRobots,
                  obsidianRobots, needMoreObsidian ? obsidianRobots + obsidian : obsidianRobots,
                  geodeRobots, cache);
          best = Math.max(best, alt);
        }
      }

      // Alt 2: build a clay robot
      if (ore >= clayRobotOreCost) {
        if (needMoreClay && clayRobots <= 4 * oreRobots) {
          long alt = geodeRobots + solve(timeLeft - 1,
                  oreRobots, needMoreOre ? ore - clayRobotOreCost + oreRobots : maxOreUsage,
                  clayRobots + 1, clayRobots + clay,
                  obsidianRobots, needMoreObsidian ? obsidianRobots + obsidian : obsidianRobots,
                  geodeRobots, cache);
          best = Math.max(best, alt);
        }
      }

      // Alt 1: build nothing
      final long alt = geodeRobots + solve(timeLeft - 1,
              oreRobots, needMoreOre ? ore + oreRobots : oreRobots,
              clayRobots, needMoreClay ? clayRobots + clay : clayRobots,
              obsidianRobots, needMoreObsidian ? obsidianRobots + obsidian : obsidianRobots,
              geodeRobots, cache);
      best = Math.max(best, alt);

      cache.put(key, best);
      return best;
    }
  }

  private static class Key {
    final int timeLeft;
    final int oreRobots;
    final int ore;
    final int clayRobots;
    final int clay;
    final int obsidianRobots;
    final int obsidian;
    final int geodeRobots;

    public Key(int timeLeft, int oreRobots, int ore, int clayRobots, int clay, int obsidianRobots, int obsidian, int geodeRobots) {
      this.timeLeft = timeLeft;
      this.oreRobots = oreRobots;
      this.ore = ore;
      this.clayRobots = clayRobots;
      this.clay = clay;
      this.obsidianRobots = obsidianRobots;
      this.obsidian = obsidian;
      this.geodeRobots = geodeRobots;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Key key = (Key) o;
      return timeLeft == key.timeLeft && oreRobots == key.oreRobots && ore == key.ore && clayRobots == key.clayRobots && clay == key.clay && obsidianRobots == key.obsidianRobots && obsidian == key.obsidian && geodeRobots == key.geodeRobots;
    }

    @Override
    public int hashCode() {
      return Objects.hash(timeLeft, oreRobots, ore, clayRobots, clay, obsidianRobots, obsidian, geodeRobots);
    }
  }
}



