/*

Design a class called ScanLefIterator that implements Iterator<T>. 
Its constructor takes in a source Iterator, a base value, and a BiFunction, 
and produces the sequence of objects obtained from scanning the function 
across the results produced by the source iterator.

Example: an Iterator called iter produces : "a", "bb", "ccc"

new ScanLeftIterator( iter, function to add a string-length to a number, 0) produces 1, 3, 6  (one at a time)

 */

class ScanLeftIterator<T, X> implements Iterator<T> {
  Iterator<T> source;
  X base;
  BiFunction<T, X, X> func;

  ScanLeftIterator(Iterator<T> source, X base, BiFunction<T, X, X> func) {
    this.source = source;
    this.base = base;
    this.func = func;
  }

  // determines if there is a next element in the iterator
  public boolean hasNext() {
    return this.source.hasNext();
  }

  // returns the next element of the iterator
  public T next() {
    if (!hasNext()) {
      throw new NoSuchElementException("There are no more items");
    } else {
      X nextValue = this.func.apply(this.source.next(), this.base);
      this.base = nextValue;
      return this.base;
    }
  }
}