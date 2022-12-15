package util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class IntervalSet {
  private final List<Interval> intervals;

  private IntervalSet(List<Interval> intervals) {
    this.intervals = intervals;
  }

  public static IntervalSet merge(Collection<Interval> intervals) {
    Interval cur = null;
    final ArrayList<Interval> res = new ArrayList<>();
    final List<Interval> sorted = intervals.stream()
            .filter(Interval::nonEmpty)
            .sorted().collect(Collectors.toList());
    for (Interval interval : sorted) {
      if (cur == null) {
        cur = interval;
      } else {
        if (interval.min() <= cur.max()) {
          cur = Interval.of(cur.min(), Math.max(cur.max(), interval.max()));
        } else {
          res.add(cur);
          cur = interval;
        }
      }
    }
    if (cur != null) {
      res.add(cur);
    }
    return new IntervalSet(res);
  }

  public long length() {
    return intervals.stream().mapToLong(Interval::length).sum();
  }

  @Override
  public String toString() {
    return intervals.toString();
  }
}
