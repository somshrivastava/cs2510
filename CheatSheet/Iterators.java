class PersonIterator implements Iterator<Person> {
  ArrayList<Person> allPeople;
  int index;
  PersonIterator(Person person) {
    this.allPeople = new ArrayList<Person>();
    this.allPeople.add(person);
    this.index = 0;
  }
  public boolean hasNext() {
    return index < this.allPeople.size();
  }
  public Person next() {
    if (!this.hasNext()) {
      throw new NoSuchElementException("No more people in this family tree");
    }
    Person toReturn = this.allPeople.get(this.index);
    this.index++;
    ArrayList<Person> childrenToProcess = new ArrayList<Person>(toReturn.listOfChildren);
    for (Person child : childrenToProcess) {
      this.allPeople.add(child);
    }
    return toReturn;
  }
  public void remove() {
    throw new UnsupportedOperationException("Cannot remove");
  }
}
class ListOfLists<T> implements Iterable<T> {
  ArrayList<ArrayList<T>> lists;
  ListOfLists() {
    this.lists = new ArrayList<>();
  }
  public void addNewList() {
    this.lists.add(new ArrayList<>());
  }
  public void add(int index, T object) {
    if (index < 0 || index >= this.lists.size()) {
      throw new IndexOutOfBoundsException("Invalid index");
    }
    this.lists.get(index).add(object);
  }
  public ArrayList<T> get(int index) {
    if (index < 0 || index >= this.lists.size()) {
      throw new IndexOutOfBoundsException("Invalid index");
    }
    return this.lists.get(index);
  }
  public int size() {
    return this.lists.size();
  }
  public Iterator<T> iterator() {
    return new ListOfListsIterator<>(this.lists);
  }
}

class ListOfListsIterator<T> implements Iterator<T> {
  int firstIndex = 0;
  int secondIndex = 0;
  ArrayList<ArrayList<T>> lists;
  ListOfListsIterator(ArrayList<ArrayList<T>> lists) {
    this.lists = lists;
  }
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
  public T next() {
    if (!hasNext()) {
      throw new NoSuchElementException("No more elements");
    }
    return lists.get(firstIndex).get(secondIndex++);
  }
}

class LolDiagonalIterator<T> implements Iterator<T> {
  ListOfLists<T> lol;
  int index;
  LolDiagonalIterator(ListOfLists<T> lol) {
    this.lol = lol;
    this.index = 0;
  }
  public boolean hasNext() {
    return index < lol.size() && index < lol.get(0).size();
  }
  public T next() {
    if (!hasNext()) {
      throw new NoSuchElementException("No more diagonal elements");
    }
    return lol.get(index).get(index++);
  }
}

class CycleIterator<T> implements Iterator<T> {
  Iterable<T> iterable;
  Iterator<T> current;
  CycleIterator(Iterable<T> iterable) {
    this.iterable = iterable;
    this.current = iterable.iterator();
  }
  public boolean hasNext() {
    return iterable.iterator().hasNext();
  }
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