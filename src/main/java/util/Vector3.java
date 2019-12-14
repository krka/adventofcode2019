package util;

import java.util.Objects;

public class Vector3 {
  final long x;
  final long y;
  final long z;
  private final int hash;

  public Vector3(long x, long y, long z) {
    this.x = x;
    this.y = y;
    this.z = z;
    hash = Objects.hash(x, y, z);
  }

  public static Vector3 parse(String line) {
    String[] split = line.split(",");
    long x = Long.parseLong(split[0]);
    long y = Long.parseLong(split[1]);
    long z = Long.parseLong(split[2]);
    return new Vector3(x, y, z);
  }

  public static Vector3 zero() {
    return new Vector3(0, 0, 0);
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

  public Vector3 add(long x, long y, long z) {
    return new Vector3(this.x + x, this.y + y, this.z + z);
  }

  public Vector3 add(Vector3 vector3) {
    return add(vector3.x, vector3.y, vector3.z);
  }

  public long manhattan() {
    return Math.abs(x) + Math.abs(y) + Math.abs(z);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Vector3 vector3 = (Vector3) o;
    return x == vector3.x &&
            y == vector3.y &&
            z == vector3.z;
  }

  @Override
  public int hashCode() {
    return hash;
  }

  public Vector3 sub(Vector3 other) {
    return new Vector3(x - other.x, y - other.y, z - other.z);
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
