package intcode.assembler;

import java.io.Closeable;
import java.io.IOException;
import java.math.BigInteger;
import java.util.concurrent.Callable;

public class TempVariable extends Variable implements Closeable {
  private final TempSpace tempSpace;

  public TempVariable(String name, TempSpace tempSpace) {
    super("int", name, 1, new BigInteger[]{BigInteger.ZERO}, null, "Temp variable ");
    this.tempSpace = tempSpace;
  }

  public void release() {
    tempSpace.release(this);
  }

  @Override
  public void close() {
    release();
  }
}
