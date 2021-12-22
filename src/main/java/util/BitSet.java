package util;

public class BitSet {
  private static final int ADDRESS_BITS_PER_WORD = 6;
  private static final int BITS_PER_WORD = 1 << ADDRESS_BITS_PER_WORD;
  private static final int BIT_INDEX_MASK = BITS_PER_WORD - 1;

  /* Used to shift left or right for a partial word mask */
  private static final long WORD_MASK = 0xffffffffffffffffL;

  long[] words;


  public BitSet(int nbits) {
    words = new long[wordIndex(nbits-1) + 1];
  }

  public void set(int bitIndex, boolean value) {
    if (value) {
      set(bitIndex);
    } else {
      clear(bitIndex);
    }
  }

  private void clear(int bitIndex) {
    int wordIndex = wordIndex(bitIndex);
    words[wordIndex] &= ~(1L << bitIndex);
  }

  private void set(int bitIndex) {
    int wordIndex = wordIndex(bitIndex);
    words[wordIndex] |= (1L << bitIndex); // Restores invariants

  }

  private static int wordIndex(int bitIndex) {
    return bitIndex >> ADDRESS_BITS_PER_WORD;
  }

  public boolean get(int bitIndex) {
    int wordIndex = wordIndex(bitIndex);
    return 0 != (words[wordIndex] & (1L << bitIndex));
  }
}
