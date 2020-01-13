package intcode.assembler;

import util.Util;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class AnnotatedOperation {
  private final String ops;
  private final String description;
  private final List<BigInteger> intcode;
  private String label;
  private int address;

  public AnnotatedOperation(String ops, String description, Integer... values) {
    this.ops = ops;
    this.description = description;
    this.intcode = Util.toBigInt(Arrays.asList(values));
  }

  public AnnotatedOperation(String ops, String description, BigInteger... values) {
    this.ops = ops;
    this.description = description;
    this.intcode = Arrays.asList(values);
  }

  public static AnnotatedOperation variable(String description, BigInteger value) {
    return new AnnotatedOperation("memory", description, value);
  }

  public List<BigInteger> getIntCode() {
    return intcode;
  }

  public int size() {
    return intcode.size();
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public void setAddress(int address) {
    this.address = address;
  }

  @Override
  public String toString() {
    String label = this.label == null ? "" : this.label;
    return String.format(Locale.ROOT, "%15s%6d: %20s -- %-50s -- %s", label, address, Util.toString(intcode), ops, description);
  }

}
