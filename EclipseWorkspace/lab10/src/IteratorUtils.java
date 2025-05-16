import java.util.*;
import tester.*;

// represents a pair
class Pair<L, R> {
  L left;
  R right;

  Pair(L left, R right) {
    this.left = left;
    this.right = right;
  }
}

// represents the zip strict iterator
class ZipStrictIterator<X, Y> implements Iterator<Pair<X, Y>> {
  ArrayList<X> array1;
  ArrayList<Y> array2;
  int index = 0;

  ZipStrictIterator(ArrayList<X> array1, ArrayList<Y> array2) {
    // throws an illegal argument exception if the lists are not the same size
    if (array1.size() != array2.size()) {
      throw new IllegalArgumentException("Lists are not same size");
    }
    this.array1 = array1;
    this.array2 = array2;
  }

  // determines whether the index is less than the size of the first array
  public boolean hasNext() {
    return index < array1.size();
  }

  // advance the iterator to the next value
  public Pair<X, Y> next() {
    if (!hasNext()) {
      throw new NoSuchElementException("There are no more items");
    }
    return new Pair<>(array1.get(index), array2.get(index++));
  }
}

// represents the zip lists iterator
class ZipListsIterator<X, Y> implements Iterator<Pair<X, Y>> {
  ArrayList<X> array1;
  ArrayList<Y> array2;
  int index = 0;
  int minSize;

  ZipListsIterator(ArrayList<X> array1, ArrayList<Y> array2) {
    this.array1 = array1;
    this.array2 = array2;
    this.minSize = Math.min(array1.size(), array2.size());
  }

  // determines whether the index is less than the size of the smaller array
  public boolean hasNext() {
    return index < minSize;
  }
  
  // advance the iterator to the next value
  public Pair<X, Y> next() {
    if (!hasNext()) {
      throw new NoSuchElementException("There are no more items");
    }
    return new Pair<>(array1.get(index), array2.get(index++));
  }
}

// represents the concat iterator
class ConcatIterator<T> implements Iterator<T> {
  Iterator<T> current;
  Iterator<T> iterable2;

  ConcatIterator(Iterator<T> iterable1, Iterator<T> iterable2) {
    this.current = iterable1;
    this.iterable2 = iterable2;
  }

  // determines whether the current iterator or second iterator has a next
  public boolean hasNext() {
    if (current.hasNext()) {
      return true;
    } else if (iterable2.hasNext()) {
      current = iterable2;
      return true;
    } else {
      return false;
    }
  }

  // advances the iterator to the next value
  public T next() {
    if (!hasNext()) {
      throw new NoSuchElementException("There are no more items");
    }
    return current.next();
  }
}

// represents the cycle iterator
class CycleIterator<T> implements Iterator<T> {
  Iterable<T> iterable;
  Iterator<T> current;

  CycleIterator(Iterable<T> iterable) {
    this.iterable = iterable;
    this.current = iterable.iterator();
  }

  // determines whether the iterable iterator has a next
  public boolean hasNext() {
    return iterable.iterator().hasNext();
  }

  // advances the iterator to the next value
  public T next() {
    if (!current.hasNext()) {
      current = iterable.iterator();
      if (!current.hasNext()) {
        throw new NoSuchElementException("There are no more items");
      }
    }
    return current.next();
  }
}

// represents the iterator utils
class IteratorUtils {
  // returns an iterator of pairs of corresponding elements, if the lists are different sizes, throw an exception
  public <X, Y> Iterator<Pair<X, Y>> zipStrict(ArrayList<X> array1, ArrayList<Y> array2) {
    return new ZipStrictIterator<>(array1, array2);
  }

  // returns an iterator of pairs of corresponding elements
  public <X, Y> Iterator<Pair<X, Y>> zipLists(ArrayList<X> array1, ArrayList<Y> array2) {
    return new ZipListsIterator<>(array1, array2);
  }

  // returns a new iterator that concatenates the two iterators
  public <T> Iterator<T> concat(Iterator<T> iterable1, Iterator<T> iterable2) {
    return new ConcatIterator<>(iterable1, iterable2);
  }

  // returns an iterator that cycles through the elements of the iterable forever
  public <T> Iterator<T> cycle(Iterable<T> iterable) {
    return new CycleIterator<>(iterable);
  }
}

// examples and tests for iterator utils
class ExamplesIteratorUtils {
  // iterator utils
  IteratorUtils iteratorUtils = new IteratorUtils();
  
  // list with three numbers
  ArrayList<Integer> list1 = new ArrayList<>(Arrays.asList(1, 2, 3));
  // list with three strings
  ArrayList<String> list2 = new ArrayList<>(Arrays.asList("a", "b", "c"));
  // zipStrict iterator
  Iterator<Pair<Integer, String>> zipStrictIterator = iteratorUtils.zipStrict(list1, list2);
  
  // list with two numbers
  ArrayList<Integer> list3 = new ArrayList<>(Arrays.asList(1, 2));
  // list with three strings
  ArrayList<String> list4 = new ArrayList<>(Arrays.asList("a", "b", "c"));
  // zipLists iterator
  Iterator<Pair<Integer, String>> zipListsIterator = iteratorUtils.zipLists(list3, list4);
  
  // iterator of two numbers
  Iterator<Integer> it1 = Arrays.asList(1, 2).iterator();
  // iterator of two other numbers
  Iterator<Integer> it2 = Arrays.asList(3, 4).iterator();
  // concat iterator
  Iterator<Integer> concatIterator = iteratorUtils.concat(it1, it2);
  
  // cycle iterator
  Iterator<Integer> nonEmptyCycleIterator = iteratorUtils.cycle(Arrays.asList(1, 2, 3));
  Iterator<Integer> emptyCycleIterator = iteratorUtils.cycle(Arrays.asList());
  
  // tests the zipStrict method
  public void testZipStrict(Tester t) {
    t.checkExpect(zipStrictIterator.hasNext(), true);
    t.checkExpect(zipStrictIterator.next(), new Pair<Integer, String>(1, "a"));
    t.checkExpect(zipStrictIterator.hasNext(), true);
    t.checkExpect(zipStrictIterator.next(), new Pair<Integer, String>(2, "b"));
    t.checkExpect(zipStrictIterator.hasNext(), true);
    t.checkExpect(zipStrictIterator.next(), new Pair<Integer, String>(3, "c"));
    t.checkExpect(zipStrictIterator.hasNext(), false);
    // iterator throws exception if it reaches the end of the iterable
    t.checkException(new NoSuchElementException("There are no more items"), zipStrictIterator, "next");
    // iterator throws an exception if the lists are not the same size
    t.checkConstructorException(new IllegalArgumentException("Lists are not same size"), "ZipStrictIterator", list1, list3);
  }

  // tests the zipLists method
  public void testZipLists(Tester t) {
    t.checkExpect(zipListsIterator.hasNext(), true);
    t.checkExpect(zipListsIterator.next(), new Pair<Integer, String>(1, "a"));
    t.checkExpect(zipListsIterator.hasNext(), true);
    t.checkExpect(zipListsIterator.next(), new Pair<Integer, String>(2, "b"));
    t.checkExpect(zipListsIterator.hasNext(), false);
    // iterator throws exception if it reaches the end of the iterable
    t.checkException(new NoSuchElementException("There are no more items"), zipListsIterator, "next");
  }

  // tests the concat method
  public void testConcat(Tester t) {
    t.checkExpect(concatIterator.hasNext(), true);
    t.checkExpect(concatIterator.next(), 1);
    t.checkExpect(concatIterator.hasNext(), true);
    t.checkExpect(concatIterator.next(), 2);
    t.checkExpect(concatIterator.hasNext(), true);
    t.checkExpect(concatIterator.next(), 3);
    t.checkExpect(concatIterator.hasNext(), true);
    t.checkExpect(concatIterator.next(), 4);
    t.checkExpect(concatIterator.hasNext(), false);
    // iterator throws exception if it reaches the end of the iterable
    t.checkException(new NoSuchElementException("There are no more items"), concatIterator, "next");
  }

  // tests the cycle method
  public void testCycle(Tester t) {
    // iterator throws exception if the iterable is empty
    t.checkExpect(emptyCycleIterator.hasNext(), false);
    t.checkException(new NoSuchElementException("There are no more items"), emptyCycleIterator, "next");
    // iterator runs infinite if iterable is not empty
    t.checkExpect(nonEmptyCycleIterator.hasNext(), true);
    t.checkExpect(nonEmptyCycleIterator.next(), 1);
    t.checkExpect(nonEmptyCycleIterator.hasNext(), true);
    t.checkExpect(nonEmptyCycleIterator.next(), 2);
    t.checkExpect(nonEmptyCycleIterator.hasNext(), true);
    t.checkExpect(nonEmptyCycleIterator.next(), 3);
    t.checkExpect(nonEmptyCycleIterator.hasNext(), true);
    t.checkExpect(nonEmptyCycleIterator.next(), 1);
    t.checkExpect(nonEmptyCycleIterator.hasNext(), true);
    t.checkExpect(nonEmptyCycleIterator.next(), 2);
    t.checkExpect(nonEmptyCycleIterator.hasNext(), true);
    t.checkExpect(nonEmptyCycleIterator.next(), 3);
    t.checkExpect(nonEmptyCycleIterator.hasNext(), true);
  }
}