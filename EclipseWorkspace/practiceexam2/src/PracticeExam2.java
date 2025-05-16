import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Predicate;
import tester.*;

// represents an abstract node
abstract class ANode<T> {
  ANode<T> next;
  ANode<T> prev;

  // constructor to initialize next and prev to null
  public ANode() {
    this.next = null;
    this.prev = null;
  }

  // abstract method for find to be implemented via double dispatch
  public abstract ANode<T> find(Predicate<T> pred);

  // abstract method for removing the head node
  public abstract T removeFromHead();

  // abstract method for removing the tail node
  public abstract T removeFromTail();

  // changes the next attribute of this node
  public void changeNext(ANode<T> next) {
    this.next = next;
  }

  // changes the prev attribute of this node
  public void changePrev(ANode<T> prev) {
    this.prev = prev;
  }
}

// sentinel node to mark the start and end of the deque
class Sentinel<T> extends ANode<T> {

  // constructor initializes next and prev to point to itself
  public Sentinel() {
    this.next = this;
    this.prev = this;
  }

  // sentinel's find method always returns itself as it has no data
  public ANode<T> find(Predicate<T> pred) {
    return this;
  }

  // removing from an empty deque should throw an exception
  public T removeFromHead() {
    throw new RuntimeException("Cannot remove from an empty deque.");
  }

  public T removeFromTail() {
    throw new RuntimeException("Cannot remove from an empty deque.");
  }
}

// node containing actual data
class Node<T> extends ANode<T> {
  T data;

  // constructor that initializes the data, and next and prev to null
  public Node(T data) {
    this.data = data;
    this.next = null;
    this.prev = null;
  }

  // constructor that initializes the data, next, and prev nodes
  public Node(T data, ANode<T> next, ANode<T> prev) {
    if (next == null || prev == null) {
      throw new IllegalArgumentException("Next or previous node cannot be null.");
    }
    this.data = data;
    this.next = next;
    this.prev = prev;

    // ensure the next and prev nodes point back to this node
    this.next.changePrev(this);
    this.prev.changeNext(this);
  }

  // node's find method checks its own data and delegates to the next node
  public ANode<T> find(Predicate<T> predicate) {
    if (predicate.test(this.data)) {
      return this;
    }
    else {
      return this.next.find(predicate);
    }
  }

  // removes the head node by updating links and returning the data
  public T removeFromHead() {
    this.prev.changeNext(this.next);
    this.next.changePrev(this.prev);
    return this.data;
  }

  // removes the tail node by updating links and returning the data
  public T removeFromTail() {
    this.prev.changeNext(this.next);
    this.next.changePrev(this.prev);
    return this.data;
  }
}

// represents a deque
class Deque<T> {
  Sentinel<T> header;

  // constructor for an empty Deque
  public Deque() {
    this.header = new Sentinel<>();
  }

  // constructor that takes a specific Sentinel value
  public Deque(Sentinel<T> sentinel) {
    this.header = sentinel;
  }

  // method to count the number of nodes (excluding the header)
  public int size() {
    int count = 0;
    ANode<T> current = this.header.next;
    while (current != this.header) {
      count++;
      current = current.next;
    }
    return count;
  }

  // adds a node at the head of the Deque
  public void addAtHead(T value) {
    new Node<>(value, this.header.next, this.header);
  }

  // adds a node at the tail of the Deque
  public void addAtTail(T value) {
    new Node<>(value, this.header, this.header.prev);
  }

  // removes the node from the head of the Deque using double dispatch
  public T removeFromHead() {
    return this.header.next.removeFromHead();
  }

  // removes the node from the tail of the Deque using double dispatch
  public T removeFromTail() {
    return this.header.prev.removeFromTail();
  }

  // finds a node based on a predicate using double dispatch
  public ANode<T> find(Predicate<T> predicate) {
    return this.header.next.find(predicate);
  }
  
  public void specialAppend(Deque<T> given) {
    while (given.size() > 0) {
      this.addAtTail(given.removeFromHead());
    }
  }
}

class ArrayListUtils<T> {

  ArrayListUtils() {
  }

  public boolean containsSequence(ArrayList<Integer> source, ArrayList<Integer> sequence) {
    if (source.size() < sequence.size()) {
      return false;
    }
    for (int i = 0; i <= source.size() - sequence.size(); i++) {
      boolean match = true;
      for (int j = 0; j < sequence.size(); j++) {
        if (!source.get(i + j).equals(sequence.get(j))) {
          match = false;
        }
      }
      if (match) {
        return true;
      }
    }
    return false;
  }
}

interface IList<T> {
  // EFFECT: this list has the items of that list added to the end
  void append(IList<T> that);

  // helps append by returning a new rest of this list
  IList<T> appendHelp(IList<T> that);

  // computes the size of this list
  int length();
}

class ConsList<T> implements IList<T> {
  T first;
  IList<T> rest;

  ConsList(T first, IList<T> rest) {
    this.first = first;
    this.rest = rest;
  }

  // EFFECT: this list has the items of that list added to the end
  public void append(IList<T> that) {
    this.rest = this.rest.appendHelp(that);
  }

  // helps append by returning a new rest of this non-empty list
  public IList<T> appendHelp(IList<T> that) {
    this.rest = this.rest.appendHelp(that);
    return this;
  }

  // computes the size of this non-empty list
  public int length() {
    return 1 + this.rest.length();
  }
}

class MtList<T> implements IList<T> {
  // doesn’t make any changes to the empty list
  public void append(IList<T> that) {
    return;
  }

  // helps append by returning a new rest
  public IList<T> appendHelp(IList<T> that) {
    return that;
  }

  // computes the length of this empty list
  public int length() {
    return 0;
  }
}

class EveryOtherIterator<T> implements Iterator<T> {
  Iterator<T> given;

  EveryOtherIterator(Iterator<T> given) {
    this.given = given;
  }

  public boolean hasNext() {
    return this.given.hasNext();
  }

  public T next() {
    if (!this.hasNext()) {
      throw new NoSuchElementException("There are no more items");
    }
    else {
      T result = this.given.next();
      if (this.given.hasNext()) {
        this.given.next();
      }
      return result;
    }
  }
}

class Examples {

  ArrayListUtils<Integer> utils = new ArrayListUtils<Integer>();

  Examples() { }

  public void testContainsSequence(Tester t) {
    t.checkExpect(utils.containsSequence(new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4)),
        new ArrayList<Integer>(Arrays.asList(1, 2, 3))), true);
    t.checkExpect(utils.containsSequence(new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4)),
        new ArrayList<Integer>(Arrays.asList(2, 3, 4))), true);
    t.checkExpect(utils.containsSequence(new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4)),
        new ArrayList<Integer>(Arrays.asList(1, 3, 4))), false);
  }

  IList<Integer> mt = new MtList<Integer>();
  IList<Integer> ints1 = new ConsList<Integer>(1, mt);
  IList<Integer> ints2 = new ConsList<Integer>(2, new ConsList<Integer>(3, mt));
  IList<Integer> ints3 = new ConsList<Integer>(4, new ConsList<Integer>(5, mt));
  IList<Integer> ints4 = new ConsList<Integer>(6, new ConsList<Integer>(7, mt));

  // Fill in the blanks
  public void testAppend(Tester t) {
    t.checkExpect(ints1.length(), 1);
    t.checkExpect(ints2.length(), 2);
    t.checkExpect(ints3.length(), 2);
    ints1.append(ints2);
    t.checkExpect(ints1.length(), 3);
    ints2.append(ints3);
    t.checkExpect(ints1.length(), 5);
    ints2.append(ints4);
    t.checkExpect(ints2.length(), 6);
    t.checkExpect(ints3.length(), 4);
    ints4.append(ints4);
    // BROKEN
    // t.checkExpect(ints4.length(), 4);
  }

  ArrayList<Integer> nums1 = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4, 5, 6));
  ArrayList<Integer> nums2 = new ArrayList<Integer>(Arrays.asList(10, 20, 30));
  ArrayList<Integer> nums3 = new ArrayList<Integer>(); // empty list

  void testEveryOtherIterator(Tester t) {
    // Create EveryOtherIterator from regular iterators
    Iterator<Integer> it1 = new EveryOtherIterator<Integer>(nums1.iterator());
    Iterator<Integer> it2 = new EveryOtherIterator<Integer>(nums2.iterator());
    Iterator<Integer> it3 = new EveryOtherIterator<Integer>(nums3.iterator());

    // Test 1: Should return 1, 3, 5
    t.checkExpect(it1.hasNext(), true);
    t.checkExpect(it1.next(), 1);
    t.checkExpect(it1.hasNext(), true);
    t.checkExpect(it1.next(), 3);
    t.checkExpect(it1.hasNext(), true);
    t.checkExpect(it1.next(), 5);
    t.checkExpect(it1.hasNext(), false);

    // Test 2: Should return 10, 30
    t.checkExpect(it2.hasNext(), true);
    t.checkExpect(it2.next(), 10);
    t.checkExpect(it2.hasNext(), true);
    t.checkExpect(it2.next(), 30);
    t.checkExpect(it2.hasNext(), false);

    // Test 3: Empty list
    t.checkExpect(it3.hasNext(), false);
  }
  
  Deque<String> deque1;
  Deque<String> deque2;
  Deque<String> emptyDeque;

  void initDeques() {
    this.deque1 = new Deque<>();
    this.deque2 = new Deque<>();
    this.emptyDeque = new Deque<>();

    this.deque1.addAtTail("a");
    this.deque1.addAtTail("b");

    this.deque2.addAtTail("x");
    this.deque2.addAtTail("y");
  }

  // append empty to non-empty
  void testAppendEmptyToNonEmpty(Tester t) {
    initDeques();
    this.deque1.specialAppend(this.emptyDeque);
    t.checkExpect(this.deque1.size(), 2);
    t.checkExpect(this.emptyDeque.size(), 0);
  }

  // append non-empty to empty
  void testAppendNonEmptyToEmpty(Tester t) {
    initDeques();
    this.emptyDeque.specialAppend(this.deque2);
    t.checkExpect(this.emptyDeque.size(), 2);
    t.checkExpect(this.deque2.size(), 0);
  }

  // append non-empty to non-empty
  void testAppendTwoNonEmpty(Tester t) {
    initDeques();
    this.deque1.specialAppend(this.deque2);
    t.checkExpect(this.deque1.size(), 4);
    t.checkExpect(this.deque2.size(), 0);
    // check order: a, b, x, y
    t.checkExpect(this.deque1.removeFromHead(), "a");
    t.checkExpect(this.deque1.removeFromHead(), "b");
    t.checkExpect(this.deque1.removeFromHead(), "x");
    t.checkExpect(this.deque1.removeFromHead(), "y");
  }

  // after appending, source deque is empty
  void testSourceDequeIsEmpty(Tester t) {
    initDeques();
    this.deque1.specialAppend(this.deque2);
    t.checkExpect(this.deque2.size(), 0);
  }
}
