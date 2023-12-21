package util;

import java.util.List;

public class Polynomial {
  final Fraction[] coefficients;

  private Polynomial(Fraction[] coefficients) {
    this.coefficients = coefficients;
  }

  public Fraction evaluate(long x) {
    Fraction ans = Fraction.ZERO;
    long accX = 1;
    for (Fraction coefficient : coefficients) {
      ans = ans.add(coefficient.mul(accX));
      accX *= x;
    }
    return ans;
  }

  public static Polynomial solve(List<Vec2> points) {
    final int n = points.size();
    Fraction[][] data = new Fraction[n][n + 1];
    for (int i = 0; i < n; i++) {
      final Vec2 p = points.get(i);
      data[i][n] = Fraction.of(p.getY());
      final long x = p.getX();
      Fraction acc = Fraction.ONE;
      for (int pow = 0; pow < n; pow++) {
        data[i][pow] = acc;
        acc = acc.mul(x);
      }
    }

    for (int p = 0; p < n; p++) {
      // find pivot row and swap
      int max = p;
      for (int i = p + 1; i < n; i++) {
        final Fraction left = data[i][p].abs();
        final Fraction right = data[max][p].abs();
        if (left.compareTo(right) > 0) {
          max = i;
        }
      }
      Fraction[] temp = data[p];
      data[p] = data[max];
      data[max] = temp;

      // singular or nearly singular
      if (data[p][p].isZero()) {
        throw new ArithmeticException("Matrix is singular or nearly singular");
      }

      // pivot within A and b
      for (int i = p + 1; i < n; i++) {
        Fraction alpha = data[i][p].div(data[p][p]);
        for (int j = p; j < n + 1; j++) {
          data[i][j] = data[i][j].sub(data[p][j].mul(alpha));
        }
      }
    }

    // back substitution
    Fraction[] x = new Fraction[n];
    for (int i = n - 1; i >= 0; i--) {
      Fraction sum = Fraction.ZERO;
      for (int j = i + 1; j < n; j++) {
        sum = sum.add(data[i][j].mul(x[j]));
      }
      x[i] = (data[i][n].sub(sum)).div(data[i][i]);
    }
    return new Polynomial(x);
  }
}
