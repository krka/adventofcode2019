package util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class Interval implements Comparable<Interval> {
  public static final Comparator<Interval> COMPARATOR = Comparator.comparingLong(Interval::min).thenComparingLong(Interval::max);

  private final long min;
  private final long max;

  private Interval(long min, long max) {
    this.min = min;
    this.max = max;
  }

  public static Interval of(long min, long max) {
    return new Interval(min, max);
  }

  public static List<Interval> split(List<Interval> intervals, List<Long> points) {
    final ArrayList<Interval> res = new ArrayList<>();
    for (Interval interval : intervals) {
      long curMin = interval.min();
      long max = interval.max();
      for (long point : points) {
        if (point >= max) {
          break;
        }
        if (point > curMin) {
          res.add(of(curMin, point));
          curMin = point;
        }
      }
      res.add(of(curMin, max));
    }
    return res;
  }

  public boolean contains(long x) {
    return min <= x && x < max;
  }

  public long length() {
    return max - min;
  }

  public boolean inside(Interval other) {
    return min() >= other.min() && max() <= other.max();
  }

  public boolean isEmpty() {
    return length() <= 0;
  }

  public boolean nonEmpty() {
    return length() > 0;
  }

  public long min() {
    return min;
  }

  public long max() {
    return max;
  }

  @Override
  public String toString() {
    return "[" + min + ", " + max + ")";
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Interval interval = (Interval) o;
    return min == interval.min && max == interval.max;
  }

  @Override
  public int hashCode() {
    return Objects.hash(min, max);
  }


  @Override
  public int compareTo(Interval interval) {
    return COMPARATOR.compare(this, interval);
  }

  public Interval shift(long delta) {
    return Interval.of(min + delta, max + delta);
  }
}
