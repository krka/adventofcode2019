package intcode.assembler;

import util.Util;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class AnnotatedOperation {
  private final String description;
  private final List<BigInteger> intcode;
  private String label;
  private int address;

  public AnnotatedOperation(String description, Integer... values) {
    this.description = description;
    this.intcode = Util.toBigInt(Arrays.asList(values));
  }

  public AnnotatedOperation(String description, BigInteger... values) {
    this.description = description;
    this.intcode = Arrays.asList(values);
  }

  public static AnnotatedOperation variable(String description, BigInteger value) {
    return new AnnotatedOperation(description, value);
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
    return String.format(Locale.ROOT, "%20s  # %5d : %10s %s", toString(intcode), address, label == null ? "" : label, description);
  }

  private String toString(List<BigInteger> intcode) {
    return intcode.stream().map(Object::toString).collect(Collectors.joining(", "));
  }
}
