package aoc2023;

import util.Day;
import util.Pair;
import util.Splitter;
import util.Util;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class Day20 implements Day {
  private final List<List<String>> lines;

  public Day20(String name) {
    this.lines = Util.readResource(name).stream().collect(Util.splitBy(String::isEmpty));
  }

  @Override
  public long solvePart1() {
    final Map<String, Module> modules = createModules(lines.get(0));

    AtomicLong countHigh = new AtomicLong();
    AtomicLong countLow = new AtomicLong();
    final Callback callback = (name, signal) -> {
      if (signal) {
        countHigh.getAndIncrement();
      } else {
        countLow.getAndIncrement();
      }
    };
    for (int i = 0; i < 1000; i++) {
      execute(modules, callback);
    }
    return countLow.get() * countHigh.get();
  }

  @Override
  public long solvePart2() {
    final Map<String, Module> modules = createModules(lines.get(0));

    final String toRx = Util.exactlyOne(modules.get("rx").comeFrom());
    Map<String, AtomicInteger> trackers =
    modules.get(toRx).comeFrom().stream()
            .collect(Collectors.toMap(s -> s, s -> new AtomicInteger()));

    AtomicInteger buttonPresses = new AtomicInteger();

    while (true) {
      buttonPresses.incrementAndGet();
      execute(modules, (name, signal) -> {
        final AtomicInteger value = trackers.get(name);
        if (value != null && !signal) {
          value.set(buttonPresses.get());
        }
      });
      final long lcm = trackers.values().stream().mapToLong(AtomicInteger::get).reduce(1, Util::lcm);
      if (lcm != 0) {
        return lcm;
      }
    }
  }

  private static Map<String, Module> createModules(List<String> input) {
    Map<String, List<String>> comeFrom = new HashMap<>();
    Map<String, Module> modules = new HashMap<>();
    for (String line : input) {
      final List<String> split = Splitter.withoutDelim(" -> ").split(line);
      String name = split.get(0).trim();
      final List<String> targets = Splitter.withoutDelim(", ").split(split.get(1));
      String type = "";
      if (name.startsWith("%") || name.startsWith("&")) {
        type = name.substring(0, 1);
        name = name.substring(1);
      }
      modules.put(name, createModule(type, targets));
      for (String target : targets) {
        comeFrom.computeIfAbsent(target, s -> new ArrayList<>()).add(name);
      }
    }

    comeFrom.forEach((target, from) -> {
      modules.computeIfAbsent(target, x -> createModule("", List.of())).registerFrom(from);
    });
    return modules;
  }

  private void execute(Map<String, Module> modules, Callback callback) {
    Queue<Pair<String, Pair<String, Boolean>>> queue = new ArrayDeque<>();
    queue.add(Pair.of("broadcaster", Pair.of("button", false)));

    while (!queue.isEmpty()) {
      final Pair<String, Pair<String, Boolean>> poll = queue.poll();
      final String name = poll.a();
      final String from = poll.b().a();
      final Boolean signal = poll.b().b();

      callback.accept(name, signal);

      final Module module = modules.get(name);

      final Output outSignal = module.accept(from, signal);
      if (outSignal != Output.NO_SIGNAL) {
        final Pair<String, Boolean> message = Pair.of(name, outSignal.toBoolean());
        module.targets().forEach(target -> queue.add(Pair.of(target, message)));
      }
    }
  }

  enum Output {
    NO_SIGNAL, LOW, HIGH;

    static Output fromBoolean(boolean b) {
      return b ? HIGH : LOW;
    }

    boolean toBoolean() {
      return this == HIGH;
    }
  }

  private static Module createModule(String type, List<String> targets) {
    switch (type) {
      case "": return new Forwarder(targets);
      case "%": return new FlipFlop(targets);
      case "&": return new Conjunction(targets);
    }
    throw new RuntimeException();
  }

  private static abstract class Module {
    final Set<String> comeFrom = new HashSet<>();
    private final List<String> targets;

    public Module(List<String> targets) {
      this.targets = targets;
    }

    abstract Output accept(String from, boolean isHigh);

    public List<String> targets() {
      return targets;
    }

    public final void registerFrom(List<String> from) {
      comeFrom.addAll(from);
    }

    public Set<String> comeFrom() {
      return comeFrom;
    }
  }

  public static class Forwarder extends Module {
    public Forwarder(List<String> targets) {
      super(targets);
    }

    @Override
    public Output accept(String from, boolean isHigh) {
      return Output.fromBoolean(isHigh);
    }
  }

  public static class Conjunction extends Module {
    Map<String, Boolean> memoryFrom = new HashMap<>();

    public Conjunction(List<String> targets) {
      super(targets);
    }

    @Override
    public Output accept(String from, boolean isHigh) {
      if (!comeFrom.contains(from)) {
        throw new RuntimeException();
      }
      memoryFrom.put(from, isHigh);
      boolean res = true;
      for (String s : comeFrom) {
        final Boolean val = memoryFrom.get(s);
        if (val == null || !val) {
          res = false;
          break;
        }
      }
      return Output.fromBoolean(!res);
    }
  }

  public static class FlipFlop extends Module {
    boolean state = false;

    public FlipFlop(List<String> targets) {
      super(targets);
    }

    @Override
    public Output accept(String from, boolean isHigh) {
      if (isHigh) {
        return Output.NO_SIGNAL;
      }
      state = !state;
      return Output.fromBoolean(state);
    }
  }

  interface Callback {
    void accept(String name, boolean signal);
  }
}

