package intcode;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Memory {
  Map<BigInteger, BigInteger> data = new HashMap<>();

  BigInteger end;

  public Memory(List<BigInteger> initial) {
    end = BigInteger.valueOf(initial.size() - 1);
    for (int i = 0; i < initial.size(); i++) {
      write(BigInteger.valueOf(i), initial.get(i));
    }
  }

  public BigInteger read(BigInteger address) {
    return data.getOrDefault(address,BigInteger.ZERO);
  }

  public void write(BigInteger address, BigInteger value) {
    data.put(address, value);
    if (end.compareTo(address) < 0) {
      end = address;
    }
  }

  public boolean contains(BigInteger address) {
    return data.containsKey(address);
  }
}
