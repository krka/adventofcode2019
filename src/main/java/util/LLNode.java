package util;

public class LLNode<T> {
  public T value;
  public LLNode<T> next;
  public LLNode<T> prev;

  private LLNode(T value) {
    this.value = value;
    this.next = null;
    this.prev = null;
  }

  public static <T> LLNode<T> of(T value) {
    return new LLNode<>(value);
  }

  public void addAfter(LLNode<T> other) {
    if (other.isAttached()) {
      throw new RuntimeException("Node not detached");
    }
    other.prev = this;
    other.next = this.next;
    this.next = other;
  }

  public LLNode<T> removeNext() {
    LLNode<T> localNext = next;
    next = localNext.next;
    next.prev = this;
    localNext.prev = null;
    localNext.next = null;
    return localNext;
  }

  public boolean isAttached() {
    return next != null || prev != null;
  }

  @Override
  public String toString() {
    return "LLNode{" +
            "value=" + value +
            '}';
  }
}
