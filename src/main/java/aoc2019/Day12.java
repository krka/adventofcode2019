package aoc2019;

import util.Util;
import util.Vector3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class Day12 {
  private final List<Vector3> positions = new ArrayList<>();
  private final List<Vector3> velocities = new ArrayList<>();

  public Day12(String name) {
    for (String line : Util.readResource(name)) {
      positions.add(Vector3.parse(line));
      velocities.add(Vector3.zero());
    }
  }

  public long solve() {
    int n = positions.size();
    int steps = 1000;

    for (int i = 0; i < steps; i++) {
      step();
    }
    long sum = 0;
    for (int i = 0; i < n; i++) {
      sum += positions.get(i).manhattan() * velocities.get(i).manhattan();
    }
    return sum;
  }

  private void step() {
    int n = positions.size();
    for (int j = 0; j < n; j++) {
      Vector3 pair1 = positions.get(j);
      long x = 0, y = 0, z = 0;
      for (int k = 0; k < n; k++) {
        Vector3 pair2 = positions.get(k);
        Vector3 diff = pair2.sub(pair1);
        x += Math.signum(diff.getX());
        y += Math.signum(diff.getY());
        z += Math.signum(diff.getZ());
      }
      velocities.set(j, velocities.get(j).add(x, y, z));
    }
    for (int j = 0; j < n; j++) {
      positions.set(j, positions.get(j).add(velocities.get(j)));
    }
  }

  public long part2() {
    long c1 = findCycle(init(Vector3::getX));
    long c2 = findCycle(init(Vector3::getY));
    long c3 = findCycle(init(Vector3::getZ));

    System.out.println(c1 + ", " + c2 + ", " + c3);
    return lcm(lcm(c1, c2), c3);
  }

  private Moon[] init(Function<Vector3, Long> getter) {
    Moon[] moons = new Moon[4];
    for (int i = 0; i < 4; i++) {
      moons[i] = new Moon(getter.apply(positions.get(i)), 0);
    }
    return moons;
  }

  private long findCycle(Moon[] moons) {
    Moon[] initial = moons.clone();
    int n = moons.length;
    int step = 0;
    while (true) {
      if (step > 0 && Arrays.equals(initial, moons)) {
        return step;
      }
      step++;
      for (int j = 0; j < n; j++) {
        Moon self = moons[j];
        long vel = 0;
        for (Moon other : moons) {
          vel += Math.signum(other.pos - self.pos);
        }
        moons[j] = moons[j].add(0, vel);
      }
      for (int j = 0; j < n; j++) {
        Moon self = moons[j];
        moons[j] = self.add(self.vel, 0);
      }
    }
  }

  static long gcd(long n1, long n2) {
    if (n2 == 0) {
      return n1;
    }
    return gcd(n2, n1 % n2);
  }

  static long lcm(long a, long b) {
    return a * b / gcd(a, b);
  }

  static class Moon {
    final long pos;
    final long vel;

    public Moon(long pos, long vel) {
      this.pos = pos;
      this.vel = vel;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Moon moon = (Moon) o;
      return pos == moon.pos &&
              vel == moon.vel;
    }

    public Moon add(long x, long y) {
      return new Moon(this.pos + x, this.vel + y);
    }

    @Override
    public int hashCode() {
      return Objects.hash(pos, vel);
    }

    @Override
    public String toString() {
      return "Moon{" +
              "pos=" + pos +
              ", vel=" + vel +
              '}';
    }
  }
}
