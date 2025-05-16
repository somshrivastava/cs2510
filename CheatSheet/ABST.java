abstract class ABST<T> {
  Comparator<T> order;
  ABST(Comparator<T> order) {
    this.order = order;
  }
  abstract ABST<T> insert(T item);
  abstract boolean present(T item);
  abstract T getLeftmost();
  abstract T getLeftmostHelper(T current);
  abstract ABST<T> getRight();
  abstract boolean sameTree(ABST<T> given);
  abstract boolean sameNode(Node<T> given);
  abstract boolean sameData(ABST<T> given);
  abstract boolean sameDataNode(Node<T> given);
  abstract boolean sameDataLeaf(Leaf<T> given);
  abstract IList<T> buildList();
}

class Leaf<T> extends ABST<T> {
  Leaf(Comparator<T> order) {
    super(order);
  }
  public ABST<T> insert(T item) {
    return new Node<>(this.order, item, new Leaf<>(this.order), new Leaf<>(this.order));
  }
  public boolean present(T item) {
    return false;
  }
  public T getLeftmost() {
    throw new RuntimeException("No leftmost item of an empty tree");
  }
  public T getLeftmostHelper(T current) {
    return current;
  }
  public ABST<T> getRight() {
    throw new RuntimeException("No right of an empty tree");
  }
  public boolean sameTree(ABST<T> given) {
    return given.sameDataLeaf(this);
  }
  public boolean sameNode(Node<T> given) {
    return false;
  }
  public boolean sameData(ABST<T> given) {
    return given.sameDataLeaf(this);
  }
  public boolean sameDataNode(Node<T> given) {
    return false;
  }
  public boolean sameDataLeaf(Leaf<T> given) {
    return true;
  }
  public IList<T> buildList() {
    return new MtList<T>();
  }
}

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
  public ABST<T> insert(T item) {
    if (order.compare(item, this.data) < 0) {
      return new Node<>(order, this.data, this.left.insert(item), this.right);
    }
    else {
      return new Node<>(order, this.data, this.left, this.right.insert(item));
    }
  }
  public boolean present(T item) {
    int comparison = order.compare(item, data);
    return comparison == 0 || left.present(item) || right.present(item);
  }
  public T getLeftmost() {
    return this.getLeftmostHelper(this.data);
  }
  public T getLeftmostHelper(T current) {
    return this.left.getLeftmostHelper(this.data);
  }
  public ABST<T> getRight() {
    int comparison = order.compare(this.data, this.getLeftmost());
    if (comparison == 0) {
      return this.right;
    }
    else {
      return new Node<T>(this.order, this.data, this.left.getRight(), this.right);
    }
  }
  public boolean sameTree(ABST<T> given) {
    return given.sameNode(this);
  }
  public boolean sameNode(Node<T> given) {
    return this.order.compare(this.data, given.data) == 0 && this.left.sameTree(given.left)
        && this.right.sameTree(given.right);
  }
  public boolean sameData(ABST<T> given) {
    return given.sameDataNode(this);
  }
  public boolean sameDataNode(Node<T> given) {
    return this.order.compare(this.getLeftmost(), given.getLeftmost()) == 0
        && this.getRight().sameData(given.getRight());
  }
  public boolean sameDataLeaf(Leaf<T> given) {
    return false;
  }
  public IList<T> buildList() {
    return new ConsList<T>(this.getLeftmost(), this.getRight().buildList());
  }
}