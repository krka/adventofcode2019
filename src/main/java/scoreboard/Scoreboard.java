package scoreboard;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Scoreboard {

  // day-part -> timestamp
  private final String filename;
  private final Map<String, Long> bestTime = new HashMap<>();
  private final List<Member> members = new ArrayList<>();
  private int maxDay = -1;

  public Scoreboard(String filename) {
    this.filename = filename;
  }

  public static void main(String[] args) throws IOException {
    // Input is json file from leaderboard API: https://adventofcode.com/2020/leaderboard/private/view/<id>.json
    Scoreboard scoreboard = new Scoreboard(args[0]);
    scoreboard.print();
  }

  private void print() throws IOException {
    try (FileInputStream inputStream = new FileInputStream(filename)) {
      JSONObject jsonRoot = new JSONObject(new JSONTokener(inputStream));
      JSONObject jsonMembers = jsonRoot.getJSONObject("members");
      for (String memberId : jsonMembers.keySet()) {
        JSONObject jsonMember = jsonMembers.getJSONObject(memberId);
        JSONObject level = jsonMember.getJSONObject("completion_day_level");

        String name;
        if (jsonMember.has("name") && !jsonMember.isNull("name")) {
          name = jsonMember.getString("name");
        } else {
          name = "Anonymous#" + memberId;
        }

        Member member = new Member(name);
        members.add(member);
        for (String day : level.keySet()) {
          maxDay = Math.max(maxDay, Integer.parseInt(day));
          JSONObject dayData = level.getJSONObject(day);
          for (String part : dayData.keySet()) {
            JSONObject partData = dayData.getJSONObject(part);
            long ts = partData.getLong("get_star_ts");
            member.add(day, part, ts);
            bestTime.merge(day + "-" + part, ts, Long::min);
          }
        }
      }
    }
    members.sort(Comparator.comparingDouble(Member::getScore).reversed());
    System.out.println(header());
    for (Member member : members) {
      System.out.println(member.toString());
    }
  }

  private class Member {
    private final Map<String, Long> times = new HashMap<>();
    private final String name;

    private double score = -1;

    public Member(String name) {
      this.name = name;
    }

    public void add(String day, String part, long ts) {
      times.put(day + "-" + part, ts);
    }

    public double getScore() {
      if (score != -1) {
        return score;
      }
      score = calcScore();
      return score;
    }

    private double calcScore() {
      return times.entrySet().stream()
              .mapToDouble(value -> partScore(bestTime.get(value.getKey()), value.getValue()))
              .sum();
    }

    private double partScore(long bestTime, long memberTime) {
      return 1000.0 / (Math.sqrt(2 + (memberTime - bestTime) * 0.001) / Math.sqrt(2));
    }

    @Override
    public String toString() {
      return String.format(Locale.ROOT, "%-30s %4.0f   %s", name, getScore(), allScores());
    }

    private String allScores() {
      StringBuilder sb = new StringBuilder();
      for (int day = 1; day <= maxDay; day++) {
        for (int part = 1; part <= 2; part++) {
          String key = day + "-" + part;
          long time = times.getOrDefault(key, 0L);
          if (time > 0) {
            sb.append(String.format(Locale.ROOT, "%4.0f ", partScore(bestTime.get(key), time)));
          } else {
            sb.append("     ");
          }
        }
      }
      return sb.toString();
    }
  }

  public String header() {
    return String.format(Locale.ROOT, "%-30s Score  %s", "Name", dayHeaders());
  }

  private String dayHeaders() {
    StringBuilder sb = new StringBuilder();
    for (int day = 1; day <= maxDay; day++) {
      sb.append(String.format(Locale.ROOT, "D%-2d  P2   ", day));
    }
    return sb.toString();
  }
}
