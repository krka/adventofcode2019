package intcode.assembler;

import java.io.Closeable;
import java.math.BigInteger;

public class TempVariable extends Variable implements Closeable {
  private final TempSpace tempSpace;
  private Throwable throwable;

  public TempVariable(String name, TempSpace tempSpace) {
    super("int", name, 1, new BigInteger[]{BigInteger.ZERO}, null, name);
    this.tempSpace = tempSpace;
  }

  public void release() {
    tempSpace.release(this);
  }

  @Override
  public void close() {
    release();
  }

  public void setCreatedBy(Throwable throwable) {
    this.throwable = throwable;
  }

  public Throwable getThrowable() {
    return throwable;
  }
}
