import java.util.*;
import tester.*;

// represents a list of lists iterable
class ListOfLists<T> implements Iterable<T> {
  ArrayList<ArrayList<T>> lists;

  ListOfLists() {
    this.lists = new ArrayList<>();
  }

  // EFFECT: adds a new empty list
  public void addNewList() {
    this.lists.add(new ArrayList<>());
  }

  // EFFECT: adds an item to the list at the given index
  public void add(int index, T object) {
    if (index < 0 || index >= this.lists.size()) {
      throw new IndexOutOfBoundsException("Invalid index");
    }
    this.lists.get(index).add(object);
  }

  // gets the list at the given index
  public ArrayList<T> get(int index) {
    if (index < 0 || index >= this.lists.size()) {
      throw new IndexOutOfBoundsException("Invalid index");
    }
    return this.lists.get(index);
  }

  // returns number of lists
  public int size() {
    return this.lists.size();
  }

  // returns an iterator that iterates over all items in all lists in order
  public Iterator<T> iterator() {
    return new ListOfListsIterator<>(this.lists);
  }
}

// represents a list of lists iterator
class ListOfListsIterator<T> implements Iterator<T> {
  int firstIndex = 0;
  int secondIndex = 0;
  ArrayList<ArrayList<T>> lists;

  ListOfListsIterator(ArrayList<ArrayList<T>> lists) {
    this.lists = lists;
  }

  // determines if there is a next element
  public boolean hasNext() {
    while (firstIndex < lists.size()) {
      if (secondIndex < lists.get(firstIndex).size()) {
        return true;
      }
      else {
        firstIndex++;
        secondIndex = 0;
      }
    }
    return false;
  }

  // returns the next element
  public T next() {
    if (!hasNext()) {
      throw new NoSuchElementException("No more elements");
    }
    return lists.get(firstIndex).get(secondIndex++);
  }
}

// represents examples and tests for listOfLists
class ExamplesListOfLists {
  ListOfLists<Integer> lol; 
  
  // init data
  public void initData() {
    this.lol = new ListOfLists<Integer>();
  }
  
  // tests the addNewList method
  public void testAddNewList(Tester t) {
    this.initData();
    // tests empty lol
    t.checkExpect(this.lol.size(), 0);
    t.checkExpect(this.lol.lists, new ArrayList<>());
    // adds empty list to lol
    this.lol.addNewList();
    t.checkExpect(this.lol.size(), 1);
    t.checkExpect(this.lol.lists, new ArrayList<>(Arrays.asList(new ArrayList<>())));
    // adds another empty list to lol
    this.lol.addNewList();
    t.checkExpect(this.lol.size(), 2);
    t.checkExpect(this.lol.lists, new ArrayList<>(Arrays.asList(new ArrayList<>(), new ArrayList<>())));
  }

  // tests the add method
  public void testAdd(Tester t) {
    this.initData();
    this.lol.addNewList();
    this.lol.addNewList();

    this.lol.add(0, 1);
    this.lol.add(0, 2);
    this.lol.add(1, 3);

    // tests adding elements to different lists in lol
    t.checkExpect(this.lol.get(0), new ArrayList<>(Arrays.asList(1, 2)));
    t.checkExpect(this.lol.get(1), new ArrayList<>(Arrays.asList(3)));
    // tests adding to an invalid index
    t.checkException(new IndexOutOfBoundsException("Invalid index"), this.lol, "add", 5, 10);
  }

  // tests the get method
  public void testGet(Tester t) {
    this.initData();
    this.lol.addNewList();
    this.lol.add(0, 1);
    // tests getting an element within the lol
    ArrayList<Integer> expected = new ArrayList<>(Arrays.asList(1));
    t.checkExpect(this.lol.get(0), expected);
    // tests getting an element with invalid index
    t.checkException(new IndexOutOfBoundsException("Invalid index"), this.lol, "get", 3);
  }

  // tests the size method
  public void testSize(Tester t) {
    this.initData();
    // tests the size of empty lol
    t.checkExpect(this.lol.size(), 0);
    this.lol.addNewList();
    // tests the size of a lol with one list
    t.checkExpect(this.lol.size(), 1);
    this.lol.addNewList();
    // tests the size of lol with two lists
    t.checkExpect(this.lol.size(), 2);
    this.lol.addNewList();
  }

  // tests the iterator method
  void testIterator(Tester t) {
    this.initData();
    // tests an iterator with multiple lists
    this.lol.addNewList();
    this.lol.addNewList();
    this.lol.add(0, 1);
    this.lol.add(0, 2);
    this.lol.add(1, 3);
    Iterator<Integer> lolIterator = this.lol.iterator();
    t.checkExpect(lolIterator.hasNext(), true);
    t.checkExpect(lolIterator.next(), 1);
    t.checkExpect(lolIterator.hasNext(), true);
    t.checkExpect(lolIterator.next(), 2);
    t.checkExpect(lolIterator.hasNext(), true);
    t.checkExpect(lolIterator.next(), 3);
    t.checkExpect(lolIterator.hasNext(), false);
    t.checkException(new NoSuchElementException("No more elements"), lolIterator, "next");
    
    // tests the iterator with a lol with an empty list and non-empty list
    this.initData();
    this.lol.addNewList();
    this.lol.addNewList();
    this.lol.add(1, 10);
    lolIterator = this.lol.iterator();
    t.checkExpect(lolIterator.hasNext(), true);
    t.checkExpect(lolIterator.next(), 10);
    t.checkExpect(lolIterator.hasNext(), false);
    
    // tests the iterator with a lol with only an empty list
    this.initData();
    lolIterator = this.lol.iterator();
    t.checkExpect(lolIterator.hasNext(), false);
    t.checkException(new NoSuchElementException("No more elements"), lolIterator, "next");
  }

  // tests the list of lists method
  void testListOfLists(Tester t) {
    ListOfLists<Integer> lol = new ListOfLists<>();
    lol.addNewList();
    lol.addNewList();
    lol.addNewList();

    lol.add(0, 1);
    lol.add(0, 2);
    lol.add(0, 3);

    lol.add(1, 4);
    lol.add(1, 5);
    lol.add(1, 6);

    lol.add(2, 7);
    lol.add(2, 8);
    lol.add(2, 9);

    int count = 1;
    for (Integer val : lol) {
      t.checkExpect(val, count);
      count++;
    }
  }
}

