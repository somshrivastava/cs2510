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

// examples and tests for the deque class
class ExamplesDeque {
  // string deques
  Deque<String> stringDeque1;
  Deque<String> stringDeque2;
  Deque<String> stringDeque3;

  // int deques
  Deque<Integer> intDeque1;
  Deque<Integer> intDeque2;
  Deque<Integer> intDeque3;

  // sentinels and nodes
  Sentinel<String> sentinel;
  Node<String> node1;
  Node<String> node2;
  Node<String> node3;
  Node<String> newNode;
  Node<String> anotherNode;

  ExamplesDeque() {
    initData();
  }

  // initialize data
  public void initData() {
    // empty deque
    this.stringDeque1 = new Deque<String>();

    // deque with one node
    Sentinel<String> stringSentinel2 = new Sentinel<String>();
    new Node<>("abc", stringSentinel2, stringSentinel2);
    this.stringDeque2 = new Deque<>(stringSentinel2);

    // deque with "abc", "bcd", "cde", "def" using nested nodes
    Sentinel<String> stringSentinel3 = new Sentinel<String>();
    this.stringDeque3 = new Deque<>(stringSentinel3);
    new Node<>("abc",
        new Node<>("bcd",
            new Node<>("cde", new Node<>("def", stringSentinel3, stringSentinel3), stringSentinel3),
            stringSentinel3),
        stringSentinel3);

    // empty deque
    this.intDeque1 = new Deque<Integer>();

    // deque with one node
    Sentinel<Integer> intSentinel2 = new Sentinel<Integer>();
    new Node<>(10, intSentinel2, intSentinel2);
    this.intDeque2 = new Deque<>(intSentinel2);

    // deque with 10, 20, 30, 40 using nested nodes
    Sentinel<Integer> intSentinel3 = new Sentinel<Integer>();
    this.intDeque3 = new Deque<>(intSentinel3);
    new Node<>(10, new Node<>(20,
        new Node<>(30, new Node<>(40, intSentinel3, intSentinel3), intSentinel3), intSentinel3),
        intSentinel3);

    // sentinels and nodes
    this.sentinel = new Sentinel<String>();
    this.node1 = new Node<String>("first", sentinel, sentinel);
    this.node2 = new Node<String>("second", sentinel, node1);
    this.node3 = new Node<String>("third", sentinel, node2);
    this.newNode = new Node<String>("new", sentinel, node1);
    this.anotherNode = new Node<String>("another", sentinel, newNode);
  }

  // tests the size method
  public void testSize(Tester t) {
    initData();
    // checks the size of an empty list
    t.checkExpect(this.stringDeque1.size(), 0);
    // checks the size of a list with one node
    t.checkExpect(this.stringDeque2.size(), 1);
    // checks the size of a list with multiple nodes
    t.checkExpect(this.stringDeque3.size(), 4);
    // checks the size of an empty list
    t.checkExpect(this.intDeque1.size(), 0);
    // checks the size of a list with one node
    t.checkExpect(this.intDeque2.size(), 1);
    // checks the size of a list with multiple nodes
    t.checkExpect(this.intDeque3.size(), 4);
  }

  // tests the addAtHead method
  public void testAddAtHead(Tester t) {
    initData();

    // test adding to an empty deque
    this.stringDeque1.addAtHead("abc");
    Deque<String> expectedDeque1 = new Deque<>(new Sentinel<>());
    Sentinel<String> expectedHeader1 = expectedDeque1.header;
    new Node<>("abc", expectedHeader1, expectedHeader1);
    t.checkExpect(this.stringDeque1, expectedDeque1);
    t.checkExpect(this.stringDeque1.size(), 1);

    // test adding to a deque with one node
    this.stringDeque2.addAtHead("bcd");
    Deque<String> expectedDeque2 = new Deque<>(new Sentinel<>());
    Sentinel<String> expectedHeader2 = expectedDeque2.header;
    new Node<>("bcd", expectedHeader2.next, expectedHeader2);
    new Node<>("abc", expectedHeader2, expectedHeader2.next);
    t.checkExpect(this.stringDeque2, expectedDeque2);
    t.checkExpect(this.stringDeque2.size(), 2);

    // test adding to a deque with multiple nodes
    this.stringDeque3.addAtHead("xyz");
    Deque<String> expectedDeque3 = new Deque<>(new Sentinel<>());
    Sentinel<String> expectedHeader3 = expectedDeque3.header;
    new Node<>("xyz",
        new Node<>("abc", new Node<>("bcd",
            new Node<>("cde", new Node<>("def", expectedHeader3, expectedHeader3), expectedHeader3),
            expectedHeader3), expectedHeader3),
        expectedHeader3);
    t.checkExpect(this.stringDeque3, expectedDeque3);
    t.checkExpect(this.stringDeque3.size(), 5);

    // test adding to an empty deque
    this.intDeque1.addAtHead(10);
    Deque<Integer> expectedIntDeque1 = new Deque<>(new Sentinel<>());
    Sentinel<Integer> expectedIntHeader1 = expectedIntDeque1.header;
    new Node<>(10, expectedIntHeader1, expectedIntHeader1);
    t.checkExpect(this.intDeque1, expectedIntDeque1);
    t.checkExpect(this.intDeque1.size(), 1);

    // test adding to a deque with one node
    this.intDeque2.addAtHead(20);
    Deque<Integer> expectedIntDeque2 = new Deque<>(new Sentinel<>());
    Sentinel<Integer> expectedIntHeader2 = expectedIntDeque2.header;
    new Node<>(20, expectedIntHeader2.next, expectedIntHeader2);
    new Node<>(10, expectedIntHeader2, expectedIntHeader2.next);
    t.checkExpect(this.intDeque2, expectedIntDeque2);
    t.checkExpect(this.intDeque2.size(), 2);

    // test adding to a deque with multiple nodes
    this.intDeque3.addAtHead(5);
    Deque<Integer> expectedIntDeque3 = new Deque<>(new Sentinel<>());
    Sentinel<Integer> expectedIntHeader3 = expectedIntDeque3.header;
    new Node<>(5,
        new Node<>(10,
            new Node<>(20,
                new Node<>(30, new Node<>(40, expectedIntHeader3, expectedIntHeader3),
                    expectedIntHeader3),
                expectedIntHeader3),
            expectedIntHeader3),
        expectedIntHeader3);
    t.checkExpect(this.intDeque3, expectedIntDeque3);
    t.checkExpect(this.intDeque3.size(), 5);
  }

  // tests the addAtTail method
  public void testAddAtTail(Tester t) {
    initData();

    // test adding to an empty deque
    this.stringDeque1.addAtTail("abc");
    Deque<String> expectedDeque1 = new Deque<>(new Sentinel<>());
    Sentinel<String> expectedHeader1 = expectedDeque1.header;
    new Node<>("abc", expectedHeader1, expectedHeader1);
    t.checkExpect(this.stringDeque1, expectedDeque1);
    t.checkExpect(this.stringDeque1.size(), 1);

    // test adding to a deque with one node
    this.stringDeque2.addAtTail("bcd");
    Deque<String> expectedDeque2 = new Deque<>(new Sentinel<>());
    Sentinel<String> expectedHeader2 = expectedDeque2.header;
    new Node<>("abc", expectedHeader2.next, expectedHeader2);
    new Node<>("bcd", expectedHeader2, expectedHeader2.next);
    t.checkExpect(this.stringDeque2, expectedDeque2);
    t.checkExpect(this.stringDeque2.size(), 2);

    // test adding to a deque with multiple nodes
    this.stringDeque3.addAtTail("xyz");
    Deque<String> expectedDeque3 = new Deque<>(new Sentinel<>());
    Sentinel<String> expectedHeader3 = expectedDeque3.header;
    new Node<>("abc",
        new Node<>("bcd", new Node<>("cde",
            new Node<>("def", new Node<>("xyz", expectedHeader3, expectedHeader3), expectedHeader3),
            expectedHeader3), expectedHeader3),
        expectedHeader3);
    t.checkExpect(this.stringDeque3, expectedDeque3);
    t.checkExpect(this.stringDeque3.size(), 5);

    // test adding to an empty deque
    this.intDeque1.addAtTail(10);
    Deque<Integer> expectedIntDeque1 = new Deque<>(new Sentinel<>());
    Sentinel<Integer> expectedIntHeader1 = expectedIntDeque1.header;
    new Node<>(10, expectedIntHeader1, expectedIntHeader1);
    t.checkExpect(this.intDeque1, expectedIntDeque1);
    t.checkExpect(this.intDeque1.size(), 1);

    // test adding to a deque with one node
    this.intDeque2.addAtTail(20);
    Deque<Integer> expectedIntDeque2 = new Deque<>(new Sentinel<>());
    Sentinel<Integer> expectedIntHeader2 = expectedIntDeque2.header;
    new Node<>(10, expectedIntHeader2.next, expectedIntHeader2);
    new Node<>(20, expectedIntHeader2, expectedIntHeader2.next);
    t.checkExpect(this.intDeque2, expectedIntDeque2);
    t.checkExpect(this.intDeque2.size(), 2);

    // test adding to a deque with multiple nodes
    this.intDeque3.addAtTail(50);
    Deque<Integer> expectedIntDeque3 = new Deque<>(new Sentinel<>());
    Sentinel<Integer> expectedIntHeader3 = expectedIntDeque3.header;
    new Node<>(10,
        new Node<>(20,
            new Node<>(30,
                new Node<>(40, new Node<>(50, expectedIntHeader3, expectedIntHeader3),
                    expectedIntHeader3),
                expectedIntHeader3),
            expectedIntHeader3),
        expectedIntHeader3);
    t.checkExpect(this.intDeque3, expectedIntDeque3);
    t.checkExpect(this.intDeque3.size(), 5);
  }

  // tests the removeFromHead method
  public void testRemoveFromHead(Tester t) {
    initData();

    // test removing from an empty deque
    t.checkException(new RuntimeException("Cannot remove from an empty deque."), this.stringDeque1,
        "removeFromHead");

    // test removing from a deque with one node
    t.checkExpect(this.stringDeque2.removeFromHead(), "abc");
    Deque<String> expectedDeque2 = new Deque<>(new Sentinel<>());
    t.checkExpect(this.stringDeque2, expectedDeque2);
    t.checkExpect(this.stringDeque2.size(), 0);

    // test removing from a deque with multiple nodes
    t.checkExpect(this.stringDeque3.removeFromHead(), "abc");
    Deque<String> expectedDeque3 = new Deque<>(new Sentinel<>());
    Sentinel<String> expectedHeader3 = expectedDeque3.header;
    new Node<>("bcd",
        new Node<>("cde", new Node<>("def", expectedHeader3, expectedHeader3), expectedHeader3),
        expectedHeader3);
    t.checkExpect(this.stringDeque3, expectedDeque3);
    t.checkExpect(this.stringDeque3.size(), 3);

    // test removing from an empty deque
    t.checkException(new RuntimeException("Cannot remove from an empty deque."), this.intDeque1,
        "removeFromHead");

    // test removing from a deque with one node
    t.checkExpect(this.intDeque2.removeFromHead(), 10);
    Deque<Integer> expectedIntDeque2 = new Deque<>(new Sentinel<>());
    t.checkExpect(this.intDeque2, expectedIntDeque2);
    t.checkExpect(this.intDeque2.size(), 0);

    // test removing from a deque with multiple nodes
    t.checkExpect(this.intDeque3.removeFromHead(), 10);
    Deque<Integer> expectedIntDeque3 = new Deque<>(new Sentinel<>());
    Sentinel<Integer> expectedIntHeader3 = expectedIntDeque3.header;
    new Node<>(20,
        new Node<>(30, new Node<>(40, expectedIntHeader3, expectedIntHeader3), expectedIntHeader3),
        expectedIntHeader3);
    t.checkExpect(this.intDeque3, expectedIntDeque3);
    t.checkExpect(this.intDeque3.size(), 3);

    // tests that removing from an empty sentinel throws an exception
    t.checkException(new RuntimeException("Cannot remove from an empty deque."), sentinel,
        "removeFromHead");
    // tests that removing from a node removes and returns the correct value
    t.checkExpect(node1.removeFromHead(), "first");
    // tests that removing the next node returns the correct value
    t.checkExpect(node2.removeFromHead(), "second");
  }

  // tests the removeFromTail method
  public void testRemoveFromTail(Tester t) {
    initData();

    // test removing from an empty deque
    t.checkException(new RuntimeException("Cannot remove from an empty deque."), this.stringDeque1,
        "removeFromTail");

    // test removing from a deque with one node
    t.checkExpect(this.stringDeque2.removeFromTail(), "abc");
    Deque<String> expectedDeque2 = new Deque<>(new Sentinel<>());
    t.checkExpect(this.stringDeque2, expectedDeque2);
    t.checkExpect(this.stringDeque2.size(), 0);

    // test removing from a deque with multiple nodes
    t.checkExpect(this.stringDeque3.removeFromTail(), "def");
    Deque<String> expectedDeque3 = new Deque<>(new Sentinel<>());
    Sentinel<String> expectedHeader3 = expectedDeque3.header;
    new Node<>("abc",
        new Node<>("bcd", new Node<>("cde", expectedHeader3, expectedHeader3), expectedHeader3),
        expectedHeader3);
    t.checkExpect(this.stringDeque3, expectedDeque3);
    t.checkExpect(this.stringDeque3.size(), 3);

    // test removing from an empty deque
    t.checkException(new RuntimeException("Cannot remove from an empty deque."), this.intDeque1,
        "removeFromTail");

    // test removing from a deque with one node
    t.checkExpect(this.intDeque2.removeFromTail(), 10);
    Deque<Integer> expectedIntDeque2 = new Deque<>(new Sentinel<>());
    t.checkExpect(this.intDeque2, expectedIntDeque2);
    t.checkExpect(this.intDeque2.size(), 0);

    // test removing from a deque with multiple nodes
    t.checkExpect(this.intDeque3.removeFromTail(), 40);
    Deque<Integer> expectedIntDeque3 = new Deque<>(new Sentinel<>());
    Sentinel<Integer> expectedIntHeader3 = expectedIntDeque3.header;
    new Node<>(10,
        new Node<>(20, new Node<>(30, expectedIntHeader3, expectedIntHeader3), expectedIntHeader3),
        expectedIntHeader3);
    t.checkExpect(this.intDeque3, expectedIntDeque3);
    t.checkExpect(this.intDeque3.size(), 3);

    // tests that removing from an empty sentinel throws an exception
    t.checkException(new RuntimeException("Cannot remove from an empty deque."), sentinel,
        "removeFromTail");
    // tests that removing from a node removes and returns the correct value
    t.checkExpect(node3.removeFromTail(), "third");
    // tests that removing the next node returns the correct value
    t.checkExpect(node2.removeFromTail(), "second");
  }

  // tests the changeNext and changePrev method
  public void testChangeNextAndChangePrev(Tester t) {
    initData();

    // link node1 to node2
    node1.changeNext(node2);
    node2.changePrev(node1);

    // tests that node1 initially points to node2 as next
    t.checkExpect(node1.next, node2);
    // tests that node2 initially points to node1 as prev
    t.checkExpect(node2.prev, node1);

    // change node1's next to newNode
    node1.changeNext(newNode);
    newNode.changePrev(node1);

    // tests that node1 now points to newNode as next
    t.checkExpect(node1.next, newNode);
    // tests that newNode correctly points back to node1 as prev
    t.checkExpect(newNode.prev, node1);

    // change newNode's next to anotherNode
    newNode.changeNext(anotherNode);
    anotherNode.changePrev(newNode);

    // tests that newNode now correctly points to anotherNode as next
    t.checkExpect(newNode.next, anotherNode);
    // tests that anotherNode correctly points back to newNode as prev
    t.checkExpect(anotherNode.prev, newNode);

    // change anotherNode's next to node3
    anotherNode.changeNext(node3);
    node3.changePrev(anotherNode);

    // test that anotherNode now correctly points to node3 as next
    t.checkExpect(anotherNode.next, node3);
    // tests that node3 correctly points back to anotherNode as prev
    t.checkExpect(node3.prev, anotherNode);
  }

  // tests the find method
  public void testFind(Tester t) {
    initData();

    // test finding in an empty deque
    t.checkExpect(this.stringDeque1.find(s -> s.equals("abc")), this.stringDeque1.header);

    // test finding an element that does not exist in a deque with one node
    t.checkExpect(this.stringDeque2.find(s -> s.equals("xyz")), this.stringDeque2.header);

    // test finding an element that exists in a deque with one node
    t.checkExpect(this.stringDeque2.find(s -> s.equals("abc")), this.stringDeque2.header.next);

    // test finding an element that exists in a deque with multiple nodes
    Node<String> expectedNode = new Node<>("abc",
        new Node<>("bcd",
            new Node<>("cde", new Node<>("def", this.stringDeque3.header, this.stringDeque3.header),
                this.stringDeque3.header),
            this.stringDeque3.header),
        this.stringDeque3.header);
    t.checkExpect(this.stringDeque3.find(s -> s.equals("cde")), expectedNode.next.next);

    // test finding an element that does not exist in a deque with multiple nodes
    t.checkExpect(this.stringDeque3.find(s -> s.equals("xyz")), this.stringDeque3.header);

    // test finding a duplicate node
    this.stringDeque3.addAtHead("abc");
    t.checkExpect(this.stringDeque3.find(s -> s.equals("abc")), this.stringDeque3.header.next);

    // test finding in an empty deque
    t.checkExpect(this.intDeque1.find(n -> n == 10), this.intDeque1.header);

    // test finding an element that does not exist in a deque with one node
    t.checkExpect(this.intDeque2.find(n -> n == 50), this.intDeque2.header);

    // test finding an element that exists in a deque with one node
    t.checkExpect(this.intDeque2.find(n -> n == 10), this.intDeque2.header.next);

    // test finding an element that exists in a deque with multiple nodes
    Node<Integer> expectedIntNode = new Node<>(10,
        new Node<>(20, new Node<>(30, new Node<>(40, this.intDeque3.header, this.intDeque3.header),
            this.intDeque3.header), this.intDeque3.header),
        this.intDeque3.header);
    t.checkExpect(this.intDeque3.find(n -> n == 30), expectedIntNode.next.next);

    // test finding an element that does not exist in a deque with multiple nodes
    t.checkExpect(this.intDeque3.find(n -> n == 50), this.intDeque3.header);

    // test finding a duplicate node
    this.intDeque3.addAtHead(10);
    t.checkExpect(this.intDeque3.find(n -> n == 10), this.intDeque3.header.next);

    // link nodes before testing
    sentinel.changeNext(node1);
    node1.changePrev(sentinel);
    node1.changeNext(node2);
    node2.changePrev(node1);
    node2.changeNext(node3);
    node3.changePrev(node2);

    // test that finding an element in an empty sentinel returns the sentinel itself
    t.checkExpect(sentinel.find(s -> s.equals("first")), sentinel);
    // test that finding an existing element returns the correct node
    t.checkExpect(node1.find(s -> s.equals("first")), node1);
    // test that finding another existing element returns the correct node
    t.checkExpect(node1.find(s -> s.equals("third")), node3);
    // test that finding a non-existing element returns the sentinel
    t.checkExpect(node1.find(s -> s.equals("fourth")), sentinel);
  }

  // tests the Node constructor exceptions and valid cases
  public void testNodeConstructor(Tester t) {
    // test for invalid constructor calls
    t.checkConstructorException(
        new IllegalArgumentException("Next or previous node cannot be null."), "Node", "abc", null,
        new Sentinel<String>());
    t.checkConstructorException(
        new IllegalArgumentException("Next or previous node cannot be null."), "Node", "abc",
        new Sentinel<String>(), null);
    t.checkConstructorException(
        new IllegalArgumentException("Next or previous node cannot be null."), "Node", "abc", null,
        null);

    // test for valid constructor calls
    Sentinel<String> sentinel = new Sentinel<>();
    Node<String> validNode1 = new Node<>("abc", sentinel, sentinel);
    t.checkExpect(validNode1.data, "abc");
    t.checkExpect(validNode1.next, sentinel);
    t.checkExpect(validNode1.prev, sentinel);

    Node<String> validNode2 = new Node<>("bcd", validNode1, sentinel);
    t.checkExpect(validNode2.data, "bcd");
    t.checkExpect(validNode2.next, validNode1);
    t.checkExpect(validNode2.prev, sentinel);

    // test for invalid constructor calls
    t.checkConstructorException(
        new IllegalArgumentException("Next or previous node cannot be null."), "Node", 10, null,
        new Sentinel<Integer>());
    t.checkConstructorException(
        new IllegalArgumentException("Next or previous node cannot be null."), "Node", 10,
        new Sentinel<Integer>(), null);
    t.checkConstructorException(
        new IllegalArgumentException("Next or previous node cannot be null."), "Node", 10, null,
        null);

    // test for valid constructor calls
    Sentinel<Integer> intSentinel = new Sentinel<>();
    Node<Integer> validIntNode1 = new Node<>(10, intSentinel, intSentinel);
    t.checkExpect(validIntNode1.data, 10);
    t.checkExpect(validIntNode1.next, intSentinel);
    t.checkExpect(validIntNode1.prev, intSentinel);

    Node<Integer> validIntNode2 = new Node<>(20, validIntNode1, intSentinel);
    t.checkExpect(validIntNode2.data, 20);
    t.checkExpect(validIntNode2.next, validIntNode1);
    t.checkExpect(validIntNode2.prev, intSentinel);
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