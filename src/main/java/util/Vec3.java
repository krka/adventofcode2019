package util;

import java.util.List;
import java.util.Objects;

public class Vec3 {

  public static final List<Vec3> DIR_6 = List.of(
          parse("1,0,0"),
          parse("-1,0,0"),
          parse("0,1,0"),
          parse("0,-1,0"),
          parse("0,0,1"),
          parse("0,0,-1")
  );

  final long x;
  final long y;
  final long z;
  private final int hash;

  public Vec3(long x, long y, long z) {
    this.x = x;
    this.y = y;
    this.z = z;
    hash = Objects.hash(x, y, z);
  }

  public static Vec3 parse(String line) {
    String[] split = line.split("[, ]+");
    long x = Long.parseLong(split[0]);
    long y = Long.parseLong(split[1]);
    long z = Long.parseLong(split[2]);
    return new Vec3(x, y, z);
  }

  public static Vec3 zero() {
    return new Vec3(0, 0, 0);
  }

  public long getX() {
    return x;
  }

  public long getY() {
    return y;
  }

  public long getZ() {
    return z;
  }

  public Vec3 add(long x, long y, long z) {
    return new Vec3(this.x + x, this.y + y, this.z + z);
  }

  public Vec3 add(Vec3 vector3) {
    return add(vector3.x, vector3.y, vector3.z);
  }

  public long manhattan() {
    return Math.abs(x) + Math.abs(y) + Math.abs(z);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Vec3 vector3 = (Vec3) o;
    return x == vector3.x &&
            y == vector3.y &&
            z == vector3.z;
  }

  @Override
  public int hashCode() {
    return hash;
  }

  public Vec3 sub(Vec3 other) {
    return new Vec3(x - other.x, y - other.y, z - other.z);
  }

  public Vec3 negate() {
    return new Vec3(- x, -y, -z);
  }

  @Override
  public String toString() {
    return "(" +
            "" + x +
            ", " + y +
            ", " + z +
            ')';
  }
}
