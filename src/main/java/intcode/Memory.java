package intcode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Memory {
  Map<Integer, Integer> data = new HashMap<>();

  int end;

  public Memory(List<Integer> initial) {
    for (int i = 0; i < initial.size(); i++) {
      write(i, initial.get(i));
    }
    end = initial.size();
  }

  public int read(int address) {
    return data.getOrDefault(address,0);
  }

  public void write(int address, int value) {
    data.put(address, value);
    end = Math.max(end, address + 1);
  }

  public boolean contains(int address) {
    return data.containsKey(address);
  }
}
