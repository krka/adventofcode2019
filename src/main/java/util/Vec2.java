package util;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Vec2 {

  public static final List<Vec2> DIRS = Stream
          .of("1,0 -1,0 0,-1 0,1".split(" "))
          .map(Vec2::parse).collect(Collectors.toList());

  public static final List<Vec2> DIRS_2 = Stream
          .of("1,0 0,1".split(" "))
          .map(Vec2::parse).collect(Collectors.toList());

  public static final List<Vec2> DIRS_8 = Stream
          .of("1,0 -1,0 0,-1 0,1 -1,-1, -1,1 1,-1 1,1".split(" "))
          .map(Vec2::parse).collect(Collectors.toList());

  public static final Vec2 WEST = Vec2.of(-1, 0);
  public static final Vec2 EAST = Vec2.of(1, 0);
  public static final Vec2 NORTH = Vec2.of(0, -1);
  public static final Vec2 SOUTH = Vec2.of(0, 1);

  final long x;
  final long y;
  private final int hash;

  public Vec2(long x, long y) {
    this.x = x;
    this.y = y;
    hash = Objects.hash(x, y);
  }

  public static Vec2 grid(long row, long col) {
    return new Vec2(col, row);
  }

  public static Vec2 parse(String line) {
    String[] split = line.split("[, ]+");
    long x = Long.parseLong(split[0]);
    long y = Long.parseLong(split[1]);
    return new Vec2(x, y);
  }

  public static Vec2 zero() {
    return new Vec2(0, 0);
  }

  public static Vec2 of(long x, long y) {
    return new Vec2(x, y);
  }

  public long getX() {
    return x;
  }

  public long getY() {
    return y;
  }

  public long col() {
    return x;
  }

  public int icol() {
    return Math.toIntExact(x);
  }

  public long row() {
    return y;
  }

  public int irow() {
    return Math.toIntExact(y);
  }

  public Vec2 add(long x, long y) {
    return new Vec2(this.x + x, this.y + y);
  }

  public Vec2 add(Vec2 v) {
    return add(v.x, v.y);
  }

  public long manhattan() {
    return Math.abs(x) + Math.abs(y);
  }

  /* See https://en.wikipedia.org/wiki/Chebyshev_distance
   */
  public long chessDistance() {
    return Math.max(Math.abs(x), Math.abs(y));
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Vec2 vector3 = (Vec2) o;
    return x == vector3.x &&
            y == vector3.y;
  }

  public Vec2 signum() {
    return new Vec2(Long.signum(x), Long.signum(y));
  }

  @Override
  public int hashCode() {
    return hash;
  }

  public Vec2 sub(Vec2 other) {
    return new Vec2(x - other.x, y - other.y);
  }

  public Vec2 rotateLeft(int n) {
    return rotateRight(-n);
  }

  public Vec2 rotateRight(int n) {
    int n2 = n % 4;
    if (n2 < 0) {
      n2 += 4;
    }
    switch (n2) {
      case 0: return this;
      case 1: return new Vec2(-y, x);
      case 2: return new Vec2(-x, -y);
      case 3: return new Vec2(y, -x);
      default: throw new RuntimeException();
    }
  }

  @Override
  public String toString() {
    return "(" +
            "" + x +
            ", " + y +
            ')';
  }

  public Vec2 divide(long n) {
    return new Vec2(x / n, y / n);
  }

  public Vec2 multiply(long n) {
    return new Vec2(x * n, y * n);
  }

  public boolean vertical() {
    return x == 0;
  }

  public boolean horizontal() {
    return y == 0;
  }

  public long cross(Vec2 other) {
    return x * other.y - y * other.x;
  }

  public Vec2 mod(Vec2 dimensions) {
    return new Vec2(Util.posMod(x, dimensions.x), Util.posMod(y, dimensions.y));
  }
}
