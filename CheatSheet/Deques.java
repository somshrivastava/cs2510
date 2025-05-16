abstract class ANode<T> {
  ANode<T> next;
  ANode<T> prev;
  public ANode() {
    this.next = null;
    this.prev = null;
  }
  public abstract ANode<T> find(Predicate<T> pred);
  public abstract T removeFromHead();
  public abstract T removeFromTail();
  public void changeNext(ANode<T> next) {
    this.next = next;
  }
  public void changePrev(ANode<T> prev) {
    this.prev = prev;
  }
}

class Sentinel<T> extends ANode<T> {
  public Sentinel() {
    this.next = this;
    this.prev = this;
  }
  public ANode<T> find(Predicate<T> pred) {
    return this;
  }
  public T removeFromHead() {
    throw new RuntimeException("Cannot remove from an empty deque.");
  }
  public T removeFromTail() {
    throw new RuntimeException("Cannot remove from an empty deque.");
  }
}

class Node<T> extends ANode<T> {
  T data;
  public Node(T data) {
    this.data = data;
    this.next = null;
    this.prev = null;
  }
  public Node(T data, ANode<T> next, ANode<T> prev) {
    if (next == null || prev == null) {
      throw new IllegalArgumentException("Next or previous node cannot be null.");
    }
    this.data = data;
    this.next = next;
    this.prev = prev;
    this.next.changePrev(this);
    this.prev.changeNext(this);
  }
  public ANode<T> find(Predicate<T> predicate) {
    if (predicate.test(this.data)) {
      return this;
    }
    else {
      return this.next.find(predicate);
    }
  }
  public T removeFromHead() {
    this.prev.changeNext(this.next);
    this.next.changePrev(this.prev);
    return this.data;
  }
  public T removeFromTail() {
    this.prev.changeNext(this.next);
    this.next.changePrev(this.prev);
    return this.data;
  }
}

class Deque<T> {
  Sentinel<T> header;
  public Deque() {
    this.header = new Sentinel<>();
  }
  public Deque(Sentinel<T> sentinel) {
    this.header = sentinel;
  }
  public int size() {
    int count = 0;
    ANode<T> current = this.header.next;
    while (current != this.header) {
      count++;
      current = current.next;
    }
    return count;
  }
  public void addAtHead(T value) {
    new Node<>(value, this.header.next, this.header);
  }
  public void addAtTail(T value) {
    new Node<>(value, this.header, this.header.prev);
  }
  public T removeFromHead() {
    return this.header.next.removeFromHead();
  }
  public T removeFromTail() {
    return this.header.prev.removeFromTail();
  }
  public ANode<T> find(Predicate<T> predicate) {
    return this.header.next.find(predicate);
  }
  public void specialAppend(Deque<T> given) {
    while (given.size() > 0) {
      this.addAtTail(given.removeFromHead());
    }
  }
}