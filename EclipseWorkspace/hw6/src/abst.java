import tester.*;
import java.util.Comparator;

// represents a list
interface IList<T> {
}

// represents a generic empty list
class MtList<T> implements IList<T> {

  MtList() {
  }
}

// represents a generic non-empty list
class ConsList<T> implements IList<T> {
  T first;
  IList<T> rest;

  ConsList(T first, IList<T> rest) {
    this.first = first;
    this.rest = rest;
  }
}

// represents a book
class Book {
  String title;
  String author;
  int price;

  Book(String title, String author, int price) {
    this.title = title;
    this.author = author;
    this.price = price;
  }
}

// represents an abstract binary search tree
abstract class ABST<T> {
  Comparator<T> order;

  ABST(Comparator<T> order) {
    this.order = order;
  }

  // returns a new BST with the new item inserted in the correct position
  abstract ABST<T> insert(T item);

  // determines whether an item is present within the BST
  abstract boolean present(T item);

  // returns the leftmost item of the BST
  abstract T getLeftmost();

  // a helper for getting the leftmost item of the BST
  abstract T getLeftmostHelper(T current);

  // returns everything but the leftmost item in the BST
  abstract ABST<T> getRight();

  // determines if two BST have the same structure
  abstract boolean sameTree(ABST<T> given);

  // determines if two ABST are the same node
  abstract boolean sameNode(Node<T> given);

  // determines if two BST have the same data
  abstract boolean sameData(ABST<T> given);

  // determines if the two nodes are the same
  abstract boolean sameDataNode(Node<T> given);

  // determines if a given leaf matches the node
  abstract boolean sameDataLeaf(Leaf<T> given);

  // returns a list representation of a BST
  abstract IList<T> buildList();
}

// represents a leaf
class Leaf<T> extends ABST<T> {

  Leaf(Comparator<T> order) {
    super(order);
  }

  // returns a new BST with the new item inserted in the correct position
  public ABST<T> insert(T item) {
    return new Node<>(this.order, item, new Leaf<>(this.order), new Leaf<>(this.order));
  }

  // determines whether an item is present within the BST
  public boolean present(T item) {
    return false;
  }

  // returns the leftmost item of the BST
  public T getLeftmost() {
    throw new RuntimeException("No leftmost item of an empty tree");
  }

  // a helper for getting the leftmost item of the BST
  public T getLeftmostHelper(T current) {
    return current;
  }

  // a helper for getting the leftmost item of the BST
  public ABST<T> getRight() {
    throw new RuntimeException("No right of an empty tree");
  }

  // determines if two BST have the same structure
  public boolean sameTree(ABST<T> given) {
    return given.sameDataLeaf(this);
  }

  // determines if two ABST are the same node
  public boolean sameNode(Node<T> given) {
    return false;
  }

  // determines if two BST have the same data
  public boolean sameData(ABST<T> given) {
    return given.sameDataLeaf(this);
  }

  // determines if the two nodes are the same
  public boolean sameDataNode(Node<T> given) {
    return false;
  }

  // determines if a given leaf matches the node
  public boolean sameDataLeaf(Leaf<T> given) {
    return true;
  }

  // returns a list representation of a BST
  public IList<T> buildList() {
    return new MtList<T>();
  }
}

// represents a node
class Node<T> extends ABST<T> {
  T data;
  ABST<T> left;
  ABST<T> right;

  Node(Comparator<T> order, T data, ABST<T> left, ABST<T> right) {
    super(order);
    this.data = data;
    this.left = left;
    this.right = right;
  }

  // returns a new BST with the new item inserted in the correct position
  public ABST<T> insert(T item) {
    if (order.compare(item, this.data) < 0) {
      return new Node<>(order, this.data, this.left.insert(item), this.right);
    }
    else {
      return new Node<>(order, this.data, this.left, this.right.insert(item));
    }
  }

  // determines whether an item is present within the BST
  public boolean present(T item) {
    int comparison = order.compare(item, data);
    return comparison == 0 || left.present(item) || right.present(item);
  }

  // returns the leftmost item of the BST
  public T getLeftmost() {
    return this.getLeftmostHelper(this.data);
  }

  // a helper for getting the leftmost item of the BST
  public T getLeftmostHelper(T current) {
    return this.left.getLeftmostHelper(this.data);
  }

  // returns everything but the leftmost item in the BST
  public ABST<T> getRight() {
    int comparison = order.compare(this.data, this.getLeftmost());
    if (comparison == 0) {
      return this.right;
    }
    else {
      return new Node<T>(this.order, this.data, this.left.getRight(), this.right);
    }
  }

  // determines if two BST have the same structure
  public boolean sameTree(ABST<T> given) {
    return given.sameNode(this);
  }

  // determines if two ABST are the same node
  public boolean sameNode(Node<T> given) {
    return this.order.compare(this.data, given.data) == 0 && this.left.sameTree(given.left)
        && this.right.sameTree(given.right);
  }

  // determines if two BST have the same data
  public boolean sameData(ABST<T> given) {
    return given.sameDataNode(this);
  }

  // determines if the two nodes are the same
  public boolean sameDataNode(Node<T> given) {
    return this.order.compare(this.getLeftmost(), given.getLeftmost()) == 0
        && this.getRight().sameData(given.getRight());
  }

  // determines if a given leaf matches the node
  public boolean sameDataLeaf(Leaf<T> given) {
    return false;
  }

  // returns a list representation of a BST
  public IList<T> buildList() {
    return new ConsList<T>(this.getLeftmost(), this.getRight().buildList());
  }
}

// compares books by title
class BooksByTitle implements Comparator<Book> {
  // compares the titles of the two given books
  public int compare(Book o1, Book o2) {
    return o1.title.compareTo(o2.title);

  }
}

// compares the authors of two given books
class BooksByAuthor implements Comparator<Book> {
  // compares the authors of two given books
  public int compare(Book o1, Book o2) {
    return o1.author.compareTo(o2.author);
  }
}

// compares the prices of two given books
class BooksByPrice implements Comparator<Book> {
  // compares the price of the given books
  public int compare(Book o1, Book o2) {
    if (o1.price < o2.price) {
      return -1;
    }
    else if (o1.price == o2.price) {
      return 0;
    }
    else {
      return 1;
    }
  }
}

// compares two Integers 
class IntegerComparator implements Comparator<Integer> {
  // compares the prices of two given books
  public int compare(Integer o1, Integer o2) {
    return o1.compareTo(o2);
  }
}

class ExamplesABST {
  // integer comparator
  Comparator<Integer> intComp = new IntegerComparator();

  // example trees with integer comparator
  // an empty bst (contains no elements)
  ABST<Integer> emptyTree = new Leaf<Integer>(intComp);
  // a bst with values [1, 2, 3, 4] in sorted order
  ABST<Integer> bstA = new Node<Integer>(intComp, 3,
      new Node<Integer>(intComp, 2,
          new Node<Integer>(intComp, 1, new Leaf<Integer>(intComp), new Leaf<Integer>(intComp)),
          new Leaf<Integer>(intComp)),
      new Node<Integer>(intComp, 4, new Leaf<Integer>(intComp), new Leaf<Integer>(intComp)));
  // a duplicate of bstA (structurally identical)
  ABST<Integer> bstB = new Node<Integer>(intComp, 3,
      new Node<Integer>(intComp, 2,
          new Node<Integer>(intComp, 1, new Leaf<Integer>(intComp), new Leaf<Integer>(intComp)),
          new Leaf<Integer>(intComp)),
      new Node<Integer>(intComp, 4, new Leaf<Integer>(intComp), new Leaf<Integer>(intComp)));
  // a bst where 2 is the root, and elements are arranged to maintain bst
  // properties
  ABST<Integer> bstC = new Node<Integer>(intComp, 2,
      new Node<Integer>(intComp, 1, new Leaf<Integer>(intComp), new Leaf<Integer>(intComp)),
      new Node<Integer>(intComp, 4,
          new Node<Integer>(intComp, 3, new Leaf<Integer>(intComp), new Leaf<Integer>(intComp)),
          new Leaf<Integer>(intComp)));
  // a bst with values [1, 3, 4, 5] and an additional right child on node 4
  ABST<Integer> bstD = new Node<Integer>(intComp, 3,
      new Node<Integer>(intComp, 1, new Leaf<Integer>(intComp), new Leaf<Integer>(intComp)),
      new Node<Integer>(intComp, 4, new Leaf<Integer>(intComp),
          new Node<Integer>(intComp, 5, new Leaf<Integer>(intComp), new Leaf<Integer>(intComp))));
  // a bst with a single node containing the value 5
  ABST<Integer> bstE = new Node<Integer>(intComp, 5, new Leaf<Integer>(intComp),
      new Leaf<Integer>(intComp));
  // a bst where 5 is the root and has a right child 6
  ABST<Integer> bstF = new Node<Integer>(intComp, 5, new Leaf<Integer>(intComp),
      new Node<Integer>(intComp, 6, new Leaf<Integer>(intComp), new Leaf<Integer>(intComp)));
  // a bst with values [1, 2, 3, 4, 5] in a structured manner
  ABST<Integer> bstG = new Node<Integer>(intComp, 2,
      new Node<Integer>(intComp, 1, new Leaf<Integer>(intComp), new Leaf<Integer>(intComp)),
      new Node<Integer>(intComp, 4,
          new Node<Integer>(intComp, 3, new Leaf<Integer>(intComp), new Leaf<Integer>(intComp)),
          new Node<Integer>(intComp, 5, new Leaf<Integer>(intComp), new Leaf<Integer>(intComp))));

  // example books
  Book book1 = new Book("Animal Farm", "Adams, Douglas", 1);
  Book book2 = new Book("Brave New World", "Brown, Dan", 2);
  Book book3 = new Book("Charlie and the Chocolate Factory", "Clark, Mary", 3);
  Book book4 = new Book("Dracula", "Doyle, Arthur Conan", 4);
  Book book5 = new Book("Ender's Game", "Einstein, Albert", 5);

  // byTitle comparator
  Comparator<Book> byTitle = new BooksByTitle();

  // example trees with byTitle comparator
  // an empty bst for books (sorted by title)
  ABST<Book> emptyBookTreeByTitle = new Leaf<Book>(byTitle);
  // a bst containing books sorted by title
  ABST<Book> bstBookTitleA = new Node<Book>(byTitle, book3,
      new Node<Book>(byTitle, book2,
          new Node<Book>(byTitle, book1, new Leaf<Book>(byTitle), new Leaf<Book>(byTitle)),
          new Leaf<Book>(byTitle)),
      new Node<Book>(byTitle, book4, new Leaf<Book>(byTitle), new Leaf<Book>(byTitle)));
  // a duplicate of bstBookTitleA (structurally identical)
  ABST<Book> bstBookTitleB = new Node<Book>(byTitle, book3,
      new Node<Book>(byTitle, book2,
          new Node<Book>(byTitle, book1, new Leaf<Book>(byTitle), new Leaf<Book>(byTitle)),
          new Leaf<Book>(byTitle)),
      new Node<Book>(byTitle, book4, new Leaf<Book>(byTitle), new Leaf<Book>(byTitle)));
  // a bst with book2 as the root, sorted by title
  ABST<Book> bstBookTitleC = new Node<Book>(byTitle, book2,
      new Node<Book>(byTitle, book1, new Leaf<Book>(byTitle), new Leaf<Book>(byTitle)),
      new Node<Book>(byTitle, book4,
          new Node<Book>(byTitle, book3, new Leaf<Book>(byTitle), new Leaf<Book>(byTitle)),
          new Leaf<Book>(byTitle)));
  // a bst with books sorted by title, containing book5 as the rightmost node
  ABST<Book> bstBookTitleD = new Node<Book>(byTitle, book3,
      new Node<Book>(byTitle, book1, new Leaf<Book>(byTitle), new Leaf<Book>(byTitle)),
      new Node<Book>(byTitle, book4, new Leaf<Book>(byTitle),
          new Node<Book>(byTitle, book5, new Leaf<Book>(byTitle), new Leaf<Book>(byTitle))));
  // a bst with a single book node (book5)
  ABST<Book> bstBookTitleE = new Node<Book>(byTitle, book5, new Leaf<Book>(byTitle),
      new Leaf<Book>(byTitle));
  // a bst where book5 is the root, with book4 as its left child
  ABST<Book> bstBookTitleF = new Node<Book>(byTitle, book5, new Leaf<Book>(byTitle),
      new Node<Book>(byTitle, book4, new Leaf<Book>(byTitle), new Leaf<Book>(byTitle)));
  // a bst containing books sorted by title, including book5
  ABST<Book> bstBookTitleG = new Node<Book>(byTitle, book2,
      new Node<Book>(byTitle, book1, new Leaf<Book>(byTitle), new Leaf<Book>(byTitle)),
      new Node<Book>(byTitle, book4,
          new Node<Book>(byTitle, book3, new Leaf<Book>(byTitle), new Leaf<Book>(byTitle)),
          new Node<Book>(byTitle, book5, new Leaf<Book>(byTitle), new Leaf<Book>(byTitle))));

  // byAuthor comparator
  Comparator<Book> byAuthor = new BooksByAuthor();

  // example trees with byAuthor comparator
  // an empty bst for books (sorted by author)
  ABST<Book> emptyBookTreeByAuthor = new Leaf<Book>(byAuthor);
  // a bst containing books sorted by author
  ABST<Book> bstBookAuthorA = new Node<Book>(byAuthor, book3,
      new Node<Book>(byAuthor, book2,
          new Node<Book>(byAuthor, book1, new Leaf<Book>(byAuthor), new Leaf<Book>(byAuthor)),
          new Leaf<Book>(byAuthor)),
      new Node<Book>(byAuthor, book4, new Leaf<Book>(byAuthor), new Leaf<Book>(byAuthor)));
  // a duplicate of bstBookAuthorA (structurally identical)
  ABST<Book> bstBookAuthorB = new Node<Book>(byAuthor, book3,
      new Node<Book>(byAuthor, book2,
          new Node<Book>(byAuthor, book1, new Leaf<Book>(byAuthor), new Leaf<Book>(byAuthor)),
          new Leaf<Book>(byAuthor)),
      new Node<Book>(byAuthor, book4, new Leaf<Book>(byAuthor), new Leaf<Book>(byAuthor)));
  // a bst with book2 as the root, sorted by author
  ABST<Book> bstBookAuthorC = new Node<Book>(byAuthor, book2,
      new Node<Book>(byAuthor, book1, new Leaf<Book>(byAuthor), new Leaf<Book>(byAuthor)),
      new Node<Book>(byAuthor, book4,
          new Node<Book>(byAuthor, book3, new Leaf<Book>(byAuthor), new Leaf<Book>(byAuthor)),
          new Leaf<Book>(byAuthor)));
  // a bst with books sorted by author, containing book5 as the rightmost node
  ABST<Book> bstBookAuthorD = new Node<Book>(byAuthor, book3,
      new Node<Book>(byAuthor, book1, new Leaf<Book>(byAuthor), new Leaf<Book>(byAuthor)),
      new Node<Book>(byAuthor, book4, new Leaf<Book>(byAuthor),
          new Node<Book>(byAuthor, book5, new Leaf<Book>(byAuthor), new Leaf<Book>(byAuthor))));
  // a bst with a single book node (book5)
  ABST<Book> bstBookAuthorE = new Node<Book>(byAuthor, book5, new Leaf<Book>(byAuthor),
      new Leaf<Book>(byAuthor));
  // a bst where book5 is the root, with book4 as its left child
  ABST<Book> bstBookAuthorF = new Node<Book>(byAuthor, book5, new Leaf<Book>(byAuthor),
      new Node<Book>(byAuthor, book4, new Leaf<Book>(byAuthor), new Leaf<Book>(byAuthor)));
  // a bst containing books sorted by author, including book5
  ABST<Book> bstBookAuthorG = new Node<Book>(byAuthor, book2,
      new Node<Book>(byAuthor, book1, new Leaf<Book>(byAuthor), new Leaf<Book>(byAuthor)),
      new Node<Book>(byAuthor, book4,
          new Node<Book>(byAuthor, book3, new Leaf<Book>(byAuthor), new Leaf<Book>(byAuthor)),
          new Node<Book>(byAuthor, book5, new Leaf<Book>(byAuthor), new Leaf<Book>(byAuthor))));

  // byPrice comparator
  Comparator<Book> byPrice = new BooksByPrice();

  // example trees with byPrice comparator
  // an empty bst for books (sorted by price)
  ABST<Book> emptyBookTreeByPrice = new Leaf<Book>(byPrice);
  // a bst containing books sorted by price
  ABST<Book> bstBookPriceA = new Node<Book>(byPrice, book3,
      new Node<Book>(byPrice, book2,
          new Node<Book>(byPrice, book1, new Leaf<Book>(byPrice), new Leaf<Book>(byPrice)),
          new Leaf<Book>(byPrice)),
      new Node<Book>(byPrice, book4, new Leaf<Book>(byPrice), new Leaf<Book>(byPrice)));
  // a duplicate of bstBookPriceA (structurally identical)
  ABST<Book> bstBookPriceB = new Node<Book>(byPrice, book3,
      new Node<Book>(byPrice, book2,
          new Node<Book>(byPrice, book1, new Leaf<Book>(byPrice), new Leaf<Book>(byPrice)),
          new Leaf<Book>(byPrice)),
      new Node<Book>(byPrice, book4, new Leaf<Book>(byPrice), new Leaf<Book>(byPrice)));
  // a bst with book2 as the root, sorted by price
  ABST<Book> bstBookPriceC = new Node<Book>(byPrice, book2,
      new Node<Book>(byPrice, book1, new Leaf<Book>(byPrice), new Leaf<Book>(byPrice)),
      new Node<Book>(byPrice, book4,
          new Node<Book>(byPrice, book3, new Leaf<Book>(byPrice), new Leaf<Book>(byPrice)),
          new Leaf<Book>(byPrice)));
  // a bst with books sorted by price, containing book5 as the rightmost node
  ABST<Book> bstBookPriceD = new Node<Book>(byPrice, book3,
      new Node<Book>(byPrice, book1, new Leaf<Book>(byPrice), new Leaf<Book>(byPrice)),
      new Node<Book>(byPrice, book4, new Leaf<Book>(byPrice),
          new Node<Book>(byPrice, book5, new Leaf<Book>(byPrice), new Leaf<Book>(byPrice))));
  // a bst with a single book node (book5)
  ABST<Book> bstBookPriceE = new Node<Book>(byPrice, book5, new Leaf<Book>(byPrice),
      new Leaf<Book>(byPrice));
  // a bst where book5 is the root, with book4 as its left child
  ABST<Book> bstBookPriceF = new Node<Book>(byPrice, book5, new Leaf<Book>(byPrice),
      new Node<Book>(byPrice, book4, new Leaf<Book>(byPrice), new Leaf<Book>(byPrice)));
  // a bst containing books sorted by price, including book5
  ABST<Book> bstBookPriceG = new Node<Book>(byPrice, book2,
      new Node<Book>(byPrice, book1, new Leaf<Book>(byPrice), new Leaf<Book>(byPrice)),
      new Node<Book>(byPrice, book4,
          new Node<Book>(byPrice, book3, new Leaf<Book>(byPrice), new Leaf<Book>(byPrice)),
          new Node<Book>(byPrice, book5, new Leaf<Book>(byPrice), new Leaf<Book>(byPrice))));

  // tests the insert method
  public boolean testInsert(Tester t) {
    // tests inserting an item into an empty BST
    return t.checkExpect(this.emptyTree.insert(3),
        new Node<Integer>(intComp, 3, new Leaf<Integer>(intComp), new Leaf<Integer>(intComp)))
        // tests inserting multiple items into an empty BST
        && t.checkExpect(
            this.emptyBookTreeByTitle.insert(book3).insert(book2).insert(book4).insert(book1),
            this.bstBookTitleA)
        // tests inserting multiple items into an empty BST again
        && t.checkExpect(this.emptyBookTreeByAuthor.insert(this.book2).insert(this.book1)
            .insert(this.book4).insert(this.book3), this.bstBookAuthorC)
        // tests inserting an item into a non-empty BST
        && t.checkExpect(this.bstBookPriceA.insert(this.book5),
            new Node<Book>(byPrice, this.book3,
                new Node<Book>(byPrice, this.book2,
                    new Node<Book>(byPrice, this.book1, new Leaf<Book>(byPrice),
                        new Leaf<Book>(byPrice)),
                    new Leaf<Book>(byPrice)),
                new Node<Book>(byPrice, this.book4, new Leaf<Book>(byPrice), new Node<Book>(byPrice,
                    this.book5, new Leaf<Book>(byPrice), new Leaf<Book>(byPrice)))));
  }

  // tests the present method
  public boolean testPresent(Tester t) {
    // checks if the head of the BST is present
    return t.checkExpect(this.bstA.present(3), true)
        // checks if a child node to the left of the BST is present
        && t.checkExpect(this.bstBookTitleA.present(this.book2), true)
        // checks if a child node to the right of the BST is present
        && t.checkExpect(this.bstBookAuthorA.present(this.book4), true)
        // checks if an item in the BST is not present
        && t.checkExpect(this.bstBookPriceA.present(this.book5), false);
  }

  // tests the getLeftmost method
  public boolean testGetLeftmost(Tester t) {
    // gets the leftmost on an empty BST
    return t.checkException(new RuntimeException("No leftmost item of an empty tree"),
        this.emptyTree, "getLeftmost")
        // gets the leftmost on a BST with a left tree
        && t.checkExpect(this.bstBookTitleA.getLeftmost(), this.book1)
        // gets the leftmost on a BST with no left tree
        && t.checkExpect(this.bstBookAuthorF.getLeftmost(), this.book5)
        // gets the leftmost on a BST with no left or right tree
        && t.checkExpect(this.bstBookPriceE.getLeftmost(), this.book5);
  }

  // tests the getLeftmostHelper method public boolean
  public boolean testGetLeftmostHelper(Tester t) {
    // tests on an empty tree with a previous node
    return t.checkExpect(this.emptyTree.getLeftmostHelper(5), 5)
        // tests on a BST with a left tree
        && t.checkExpect(this.bstBookTitleA.getLeftmostHelper(this.book5), this.book1)
        // tests on a BST with no left tree
        && t.checkExpect(this.bstBookAuthorF.getLeftmostHelper(this.book5), this.book5)
        // tests on a BST with no left or right tree
        && t.checkExpect(this.bstBookPriceE.getLeftmostHelper(this.book5), this.book5);
  }

  // tests the getRight method
  public boolean testGetRight(Tester t) {
    // gets right on an empty BST
    return t.checkException(new RuntimeException("No right of an empty tree"), this.emptyTree,
        "getRight")
        // gets right on a BST with a left and right
        && t.checkExpect(this.bstBookTitleA.getRight(), new Node<Book>(byTitle, this.book3,
            new Node<Book>(byTitle, this.book2, new Leaf<Book>(byTitle), new Leaf<Book>(byTitle)),
            new Node<Book>(byTitle, this.book4, new Leaf<Book>(byTitle), new Leaf<Book>(byTitle))))
        // gets right on a BST with no left and right tree
        && t.checkExpect(this.bstBookAuthorE.getRight(), new Leaf<Book>(byAuthor))
        // gets right on a BST with no left tree
        && t.checkExpect(this.bstBookPriceF.getRight(),
            new Node<Book>(byPrice, this.book4, new Leaf<Book>(byPrice), new Leaf<Book>(byPrice)));
  }

  // tests the sameTree method
  public boolean testSameTree(Tester t) {
    // tests two BST that are the same
    return t.checkExpect(this.bstA.sameTree(bstB), true)
        // tests two BST that don't have the same structure or data
        && t.checkExpect(this.bstBookTitleA.sameTree(this.bstBookTitleC), false)
        // tests two BST that don't have the same structure or data
        && t.checkExpect(this.bstBookAuthorA.sameTree(this.bstBookAuthorD), false)
        // tests a non-empty BST with an empty BST
        && t.checkExpect(this.bstBookPriceA.sameTree(this.emptyBookTreeByAuthor), false)
        // tests an empty BST with an empty BST
        && t.checkExpect(this.emptyTree.sameTree(this.emptyTree), true)
        // tests an empty BST with a non-empty BST
        && t.checkExpect(this.emptyTree.sameTree(this.bstA), false);
  }

  // tests the sameNode method
  public boolean testSameNode(Tester t) {
    // tests two identical nodes
    return t.checkExpect(
        new Node<Integer>(intComp, 3, new Leaf<Integer>(intComp), new Leaf<Integer>(intComp))
            .sameNode(new Node<Integer>(intComp, 3, new Leaf<Integer>(intComp),
                new Leaf<Integer>(intComp))),
        true)
        // tests nodes with different values
        && t.checkExpect(
            new Node<Book>(byTitle, this.book3, new Leaf<Book>(byTitle), new Leaf<Book>(byTitle))
                .sameNode(new Node<Book>(byTitle, this.book4, new Leaf<Book>(byTitle),
                    new Leaf<Book>(byTitle))),
            false)
        // tests nodes with different structures
        && t.checkExpect(
            new Node<Book>(byAuthor, this.book3,
                new Node<Book>(byAuthor, this.book2, new Leaf<Book>(byAuthor),
                    new Leaf<Book>(byAuthor)),
                new Leaf<Book>(byAuthor)).sameNode(
                    new Node<Book>(byAuthor, this.book3, new Leaf<Book>(byAuthor), new Node<Book>(
                        byAuthor, this.book2, new Leaf<Book>(byAuthor), new Leaf<Book>(byAuthor)))),
            false);
  }

  // tests the sameData method
  public boolean testSameData(Tester t) {
    // tests two BST that have the same structure and data
    return t.checkExpect(this.bstA.sameData(this.bstB), true)
        // tests two BST that don't have the same structure, but same data
        && t.checkExpect(this.bstBookTitleA.sameData(this.bstBookTitleC), true)
        // tests two BST that don't have the same data
        && t.checkExpect(this.bstBookAuthorA.sameData(this.bstBookAuthorD), false)
        // tests a non-empty BST with an empty BST
        && t.checkExpect(this.bstBookPriceA.sameData(this.emptyBookTreeByPrice), false)
        // tests an empty BST with an empty BST
        && t.checkExpect(this.emptyTree.sameData(this.emptyTree), true)
        // tests an empty BST with a non-empty BST
        && t.checkExpect(this.emptyTree.sameData(this.bstA), false);
  }

  // tests the sameDataLeaf method
  public boolean testSameDataLeaf(Tester t) {
    // tests two empty leaves
    return t.checkExpect(new Leaf<Integer>(intComp).sameDataLeaf(new Leaf<Integer>(intComp)), true)
        // tests two empty leaves with another comparator
        && t.checkExpect(new Leaf<Book>(byTitle).sameDataLeaf(new Leaf<Book>(byTitle)), true)
        // tests two empty leaves with another comparator
        && t.checkExpect(new Leaf<Book>(byAuthor).sameDataLeaf(new Leaf<Book>(byAuthor)), true)
        // tests two empty leaves with another comparator
        && t.checkExpect(new Leaf<Book>(byPrice).sameDataLeaf(new Leaf<Book>(byPrice)), true);
  }

  // tests the sameDataNode method
  public boolean testSameDataNode(Tester t) {
    // tests two identical nodes
    return t.checkExpect(
        new Node<Integer>(intComp, 3, new Leaf<Integer>(intComp), new Leaf<Integer>(intComp))
            .sameDataNode(new Node<Integer>(intComp, 3, new Leaf<Integer>(intComp),
                new Leaf<Integer>(intComp))),
        true)
        // tests nodes with different values
        && t.checkExpect(
            new Node<Book>(byTitle, this.book3, new Leaf<Book>(byTitle), new Leaf<Book>(byTitle))
                .sameDataNode(new Node<Book>(byTitle, this.book4, new Leaf<Book>(byTitle),
                    new Leaf<Book>(byTitle))),
            false)
        // tests nodes with different structures
        && t.checkExpect(
            new Node<Book>(byAuthor, this.book3,
                new Node<Book>(byAuthor, this.book2, new Leaf<Book>(byAuthor),
                    new Leaf<Book>(byAuthor)),
                new Leaf<Book>(byAuthor)).sameDataNode(
                    new Node<Book>(byAuthor, this.book3, new Leaf<Book>(byAuthor), new Node<Book>(
                        byAuthor, this.book2, new Leaf<Book>(byAuthor), new Leaf<Book>(byAuthor)))),
            false);
  }

  // tests the build list method
  public boolean testBuildList(Tester t) {
    // tests on a BST with a left and right tree
    return t.checkExpect(this.bstA.buildList(),
        new ConsList<Integer>(1,
            new ConsList<Integer>(2,
                new ConsList<Integer>(3, new ConsList<Integer>(4, new MtList<Integer>())))))
        // tests on another BST with a left and right tree
        && t.checkExpect(this.bstBookTitleG.buildList(),
            new ConsList<Book>(this.book1,
                new ConsList<Book>(this.book2,
                    new ConsList<Book>(this.book3, new ConsList<Book>(this.book4,
                        new ConsList<Book>(this.book5, new MtList<Book>()))))))
        // tests on a BST without a left tree
        && t.checkExpect(this.bstBookAuthorF.buildList(),
            new ConsList<Book>(this.book5, new ConsList<Book>(this.book4, new MtList<Book>())))
        // tests on a BST without a left and right tree
        && t.checkExpect(this.bstBookPriceE.buildList(),
            new ConsList<Book>(this.book5, new MtList<Book>()))
        // tests on an empty BST
        && t.checkExpect(this.emptyTree.buildList(), new MtList<Book>());
  }
}