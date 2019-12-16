package aoc;

import org.junit.Test;
import util.Util;

import static org.junit.Assert.*;

public class Day16Test {

  @Test
  public void testExamples() {
    assertEquals("48226158", Day16.fft("12345678", 1));
    assertEquals("34040438", Day16.fft("12345678", 2));
    assertEquals("03415518", Day16.fft("12345678", 3));
    assertEquals("01029498", Day16.fft("12345678", 4));
  }

  @Test
  public void testExamples2() {
    assertEquals("24176176", Day16.fft("80871224585914546619083218645595", 100).substring(0, 8));
    assertEquals("73745418", Day16.fft("19617804207202209144916044189917", 100).substring(0, 8));
    assertEquals("52432133", Day16.fft("69317163492948606335995924319873", 100).substring(0, 8));
  }

  @Test
  public void testPart1() {
    String input = Util.readResource("day16.in").get(0);
    assertEquals("58672132", Day16.fft(input, 100).substring(0, 8));
  }

  @Test
  public void testPart2Examples() {
    assertEquals("84462026", Day16.fft2("03036732577212944063491565474664", 100));
    assertEquals("78725270", Day16.fft2("02935109699940807407585447034323", 100));
    assertEquals("53553731", Day16.fft2("03081770884921959731165446850517", 100));
  }


  @Test
  public void testPart2() {
    // First 7: 5 971 723
    // Input len: 650
    // 650 * 10 000 = 6 500 000
    // Second half
    // Pattern: 0...{x} 1....{x}
    // So pattern is 1 for all inputs
    // Do partial sums for each index
    String input = Util.readResource("day16.in").get(0);
    assertEquals("91689380", Day16.fft2(input, 100));
  }
}