package util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Matrix3 {
  public static final Matrix3 IDENTITY = new Matrix3(new long[]{
          1, 0, 0,
          0, 1, 0,
          0, 0, 1
  });

  public static final Matrix3 ROTATE_X = new Matrix3(new long[]{
          1, 0,  0,
          0, 0, -1,
          0, 1,  0
  });

  public static final Matrix3 ROTATE_Y = new Matrix3(new long[]{
          0, 0, -1,
          0, 1,  0,
          1, 0,  0
  });

  public static final Set<Matrix3> ROTATIONS = allRotations();

  public static Map<Matrix3, Matrix3> INVERT_ROTATIONS = invertRotations();

  private static Map<Matrix3, Matrix3> invertRotations() {
    Map<Matrix3, Matrix3> map = new HashMap<>();
    for (Matrix3 a : ROTATIONS) {
      for (Matrix3 b : ROTATIONS) {
        if (a.mul(b).equals(IDENTITY)) {
          map.put(a, b);
        }
      }
    }
    if (map.size() != ROTATIONS.size()) {
      throw new RuntimeException();
    }
    return map;
  }

  private static Set<Matrix3> allRotations() {
    Set<Matrix3> current = new HashSet<>();
    current.add(IDENTITY);
    while (true) {
      Set<Matrix3> next = new HashSet<>();
      for (Matrix3 rotation : current) {
        next.add(rotation);
        next.add(rotation.mul(ROTATE_X));
        next.add(rotation.mul(ROTATE_Y));
      }
      if (next.equals(current)) {
        return current;
      }
      current = next;
    }
  }

  final long[] m;

  private final int hash;

  private Matrix3(long[] m) {
    this.m = m;
    hash = Arrays.hashCode(m);
  }


  public Matrix3 mul(Matrix3 other) {
    long[] m2 = new long[9];
    for (int r = 0; r < 3; r++) {
      for (int c = 0; c < 3; c++) {
        int sum = 0;
        for (int i = 0; i < 3; i++) {
          int thisI = 3 * r + i;
          int otherI = 3 * i + c;
          sum += m[thisI] * other.m[otherI];
        }
        m2[3 * r + c] = sum;
      }
    }
    return new Matrix3(m2);
  }

  public Vector3 mul(Vector3 v) {
    long x = v.getX();
    long y = v.getY();
    long z = v.getZ();

    long x2 = x * m[0] + y * m[1] + z * m[2];
    long y2 = x * m[3] + y * m[4] + z * m[5];
    long z2 = x * m[6] + y * m[7] + z * m[8];
    return new Vector3(x2, y2, z2);
  }

  public Matrix3 transpose() {
    long[] m = new long[9];
    for (int r = 0; r < 3; r++) {
      for (int c = 0; c < 3; c++) {
        m[3 * r + c] = this.m[r + 3 * c];
      }
    }
    return new Matrix3(m);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Matrix3 matrix3 = (Matrix3) o;
    return Arrays.equals(m, matrix3.m);
  }

  @Override
  public int hashCode() {
    return hash;
  }

  @Override
  public String toString() {
    return "Matrix3" + Arrays.toString(m);
  }

}
