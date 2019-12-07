import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

public class IntcodePipe implements IntcodeInput, IntcodeOutput {

  private final BlockingQueue<Integer> queue = new LinkedBlockingQueue<>();

  @Override
  public int readValue() {
    try {
      return queue.take();
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void writeValue(int value) {
    queue.add(value);
  }

  @Override
  public String toString() {
    return "IntcodePipe{" +
            "queue=" + queue +
            '}';
  }
}
