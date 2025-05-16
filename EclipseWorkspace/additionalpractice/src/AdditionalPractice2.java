import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.Function;
import tester.*;
import java.util.Iterator;
import java.util.NoSuchElementException;

class ScanLeftIterator<T, X> implements Iterator<T> {
  Iterator<X> source;
  T base;
  BiFunction<X, T, T> func;
  ScanLeftIterator(Iterator<X> source, T base, BiFunction<X, T, T> func) {
    this.source = source;
    this.base = base;
    this.func = func;
  }
  public boolean hasNext() {
    return this.source.hasNext();
  }
  public T next() {
    if (!hasNext()) {
      throw new NoSuchElementException("There are no more items");
    } else {
      T nextValue = this.func.apply(this.source.next(), this.base);
      this.base = nextValue;
      return this.base;
    }
  }
}

// represents a shape
interface IShape {

  // to return the result of applying the given visitor to this shape
  <T> T accept(IShapeVisitor<T> func);
}

// represents a circle
class Circle implements IShape {
  int radius;

  Circle(int r) {
    this.radius = r;
  }

  // returns the result of applying the given visitor to circle
  public <T> T accept(IShapeVisitor<T> func) {
    return func.visitCircle(this);
  }
}

t.checkExpect(this.tennis.names, new ArrayList<String>(Arrays.asList("Alice")));

interface Iterable<T> {
  Iterator<T> iterator();
}
class ArrayList<T> implements Iterable<T> {
  public Iterator<T> iterator() {
    return new ArrayListIterator<T>(this);
  }
}

// represents a rectangle
class Rectangle implements IShape {
  int length;
  int width;

  Rectangle(int l, int w) {
    this.length = l;
    this.width = w;
  }

  // returns the result of applying the given visitor on rectangle
  public <T> T accept(IShapeVisitor<T> func) {
    return func.visitRectangle(this);
  }
}

// represents a shape visitor, to implement a function over Shape objects
// returning a value of type R
interface IShapeVisitor<T> extends Function<IShape, T> {

  T visitCircle(Circle c);

  T visitRectangle(Rectangle r);
}

class ShapePerimeter implements IShapeVisitor<Double> {

  public Double visitCircle(Circle c) {
    return Math.PI * 2.0 * c.radius;
  }

  public Double visitRectangle(Rectangle r) {
    return 2.0 * (r.width + r.length);
  }

  public Double apply(IShape s) {
    return s.accept(this);
  }
}

// Represents a roster of students for a sports team
class Roster {
  // names of the students
  ArrayList<String> names;

  // create a roster from a given list of names
  Roster(ArrayList<String> names) {
    this.names = new ArrayList<String>(names);
  }

  // adds a name to this Roster's list of names
  void addName(String name) {
    this.names.add(name);
  }

  // returns the list of students in this Roster
  ArrayList<String> getNames() {
    return new ArrayList<String>(this.names);
  }
}

class ArraysUtils {

  ArraysUtils() {
  }

  int maxDifference(ArrayList<Integer> list) {
    int max = 0;
    for (int i = 0; i < list.size(); i++) {
      for (int j = i + 1; j < list.size(); j++) {
        if (max < list.get(j) - list.get(i)) {
          max = list.get(j) - list.get(i);
        }
      }
    }
    return max;
  }

  ArrayList<Integer> removeDuplicates(ArrayList<Integer> list) {
    for (int i = 0; i < list.size(); i++) {
      for (int j = i + 1; j < list.size(); j++) {
        if (list.get(i).equals(list.get(j))) {
          list.remove(j);
          j--;
        }
      }
    }
    return list;
    /*
     * ArrayList<Integer> seen = new ArrayList<>(); for (int i = 0; i < list.size();
     * i++) { String current = list.get(i); if (seen.contains(current)) {
     * list.remove(i); i--; } else { seen.add(current); } }
     */
  }
}

//to represent a person with a name 
//and their list of children (for a family tree)
class Person implements Iterable<Person> {
  String name;
  ArrayList<Person> listOfChildren;

  // standard constructor
  Person(String name, ArrayList<Person> children) {
    this.name = name;
    this.listOfChildren = children;
  }

  // convenience constructor
  Person(String name) {
    this.name = name;
    this.listOfChildren = new ArrayList<Person>();
  }

  public Iterator<Person> iterator() {
    return new PersonIterator(this);
  }
}

/*
 * 
 * 
 * class PersonIterator implements Iterator<Person> { ArrayList<Person>
 * allPeople; int index;
 * 
 * PersonIterator(Person person) { this.allPeople = new ArrayList<Person>();
 * this.buildList(person); this.index = 0; }
 * 
 * void buildList(Person p) { this.allPeople.add(p); int i = 0; while (i <
 * allPeople.size()) { Person current = allPeople.get(i);
 * allPeople.addAll(current.listOfChildren); i++; } }
 * 
 * public boolean hasNext() { return index < this.allPeople.size(); }
 * 
 * // returns the next Person of this iterator // EFFECT: advances the iterator
 * to the next person public Person next() { if (!this.hasNext()) { throw new
 * NoSuchElementException("No more people in this family tree"); } return
 * this.allPeople.get(index++); } }
 * 
 */

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

  // returns the next Person of this iterator
  // EFFECT: advances the iterator to the next person
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
    // Returns and THEN increments index
    return lol.get(index).get(index++);
  }
}

class Student {
  String name;
  int age;

  Student(String name, int age) {
    this.name = name;
    this.age = age;
  }
  
  // determines my custom sense of equality for this student
  // does this student equal the given object
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    
    if (! (o instanceof Student)) {
      return false;
    }
    
    Student that = (Student) o;
    
    return this.name.equals(that.name) && this.age == that.age;
  }
  
  // returns the hashcode of this student
  public int hashCode() {
    return this.name.hashCode() * 1000 + this.age;
  }
}

// examples class
class Examples {
  IShape circle = new Circle(5);
  IShape rectangle = new Rectangle(5, 5);
  ShapePerimeter shapePerimeter = new ShapePerimeter();

  // USE T.CHECKINEXACT()

  public void testAccept(Tester t) {
    t.checkExpect(this.circle.accept(shapePerimeter), (2 * Math.PI * 5));
    t.checkInexact(this.rectangle.accept(shapePerimeter), 20.0, 1.0);
    t.checkExpect(this.rectangle.accept(shapePerimeter), 20.0);
  }

  public void testVisitCircle(Tester t) {
    t.checkExpect(this.shapePerimeter.visitCircle((Circle) this.circle), (2 * Math.PI * 5));
  }

  public void testVisitRectangle(Tester t) {
    t.checkExpect(this.shapePerimeter.visitRectangle((Rectangle) this.rectangle), 20.0);
  }

  public void testApply(Tester t) {
    t.checkExpect(this.shapePerimeter.apply(this.circle), (2 * Math.PI * 5));
    t.checkExpect(this.shapePerimeter.apply(this.rectangle), 20.0);
  }

  ArrayList<String> defaultRoster;
  Roster volleyball;
  Roster tennis;
  Roster swimming;
  Roster figureSkating;

  void initData() {
    this.defaultRoster = new ArrayList<String>(Arrays.asList("Alice"));

    this.volleyball = new Roster(this.defaultRoster);
    this.tennis = new Roster(this.defaultRoster);
    this.swimming = new Roster(this.defaultRoster);
    this.figureSkating = new Roster(this.defaultRoster);

  }

  // tests the addName method
  void testAddName(Tester t) {
    this.initData();

    t.checkExpect(this.volleyball.names.size(), 1);
    t.checkExpect(this.volleyball.names, new ArrayList<String>(Arrays.asList("Alice")));
    t.checkExpect(this.tennis.names.size(), 1);
    t.checkExpect(this.tennis.names, new ArrayList<String>(Arrays.asList("Alice")));

    this.volleyball.addName("Bob");

    t.checkExpect(this.volleyball.names.size(), 2);
    t.checkExpect(this.volleyball.names, new ArrayList<String>(Arrays.asList("Alice", "Bob")));

    // why do the two tests below fail?
    t.checkExpect(this.tennis.names.size(), 1);
    t.checkExpect(this.tennis.names, new ArrayList<String>(Arrays.asList("Alice")));

  }

  // tests the getNames method
  void testGetNames(Tester t) {
    this.initData();

    t.checkExpect(this.figureSkating.getNames().size(), 1);
    t.checkExpect(this.figureSkating.getNames(), new ArrayList<String>(Arrays.asList("Alice")));
    t.checkExpect(this.swimming.getNames().size(), 1);
    t.checkExpect(this.swimming.getNames(), new ArrayList<String>(Arrays.asList("Alice")));

    ArrayList<String> fgList = this.figureSkating.getNames();
    fgList.add("Bob");

    t.checkExpect(fgList.size(), 2);
    t.checkExpect(fgList, new ArrayList<String>(Arrays.asList("Alice", "Bob")));

    // why do the three tests below fail?
    t.checkExpect(this.figureSkating.getNames(), new ArrayList<String>(Arrays.asList("Alice")));
    t.checkExpect(this.swimming.getNames().size(), 1);
    t.checkExpect(this.swimming.getNames(), new ArrayList<String>(Arrays.asList("Alice")));
  }

  ArraysUtils utils = new ArraysUtils();

  public void testMaxDifference(Tester t) {
    t.checkExpect(utils.maxDifference(new ArrayList<Integer>(Arrays.asList(11, 2, 6, 4, 10, 7))),
        8);
  }

  public void testRemoveDuplicates(Tester t) {
    t.checkExpect(utils.removeDuplicates(new ArrayList<Integer>(Arrays.asList(0, 0, 0, 0))),
        new ArrayList<Integer>(Arrays.asList(0)));
    t.checkExpect(utils.removeDuplicates(new ArrayList<Integer>(Arrays.asList(0, 1, 2, 2, 3))),
        new ArrayList<Integer>(Arrays.asList(0, 1, 2, 3)));
  }

  // simple example
  Person c;
  Person b;
  Person a;

  // complex example
  Person len;
  Person kim;
  Person jan;
  Person hank;
  Person gabi;
  Person fay;
  Person ed;
  Person dan;
  Person cole;
  Person bob;
  Person ann;

  // initializes the data and the family trees
  void initData2() {
    // simple example

    /*
     * A | (B C)
     * 
     */

    this.c = new Person("C");
    this.b = new Person("B");
    this.a = new Person("A", new ArrayList<Person>(Arrays.asList(this.b, this.c)));

    // complex example

    /*
     * Ann | (Bob Cole Dan) | | | (Ed Fay Gabi Hank) (Jan Kim) () | (Len)
     * 
     * 
     * Ann's children: Bob, Cole, and Dan Bob's children: Ed, Fay, Gabi, and Hank
     * Cole's children: Jan and Kim Fay's children: Len
     * 
     */

    this.len = new Person("Len");
    this.kim = new Person("Kim");
    this.jan = new Person("Jan");
    this.hank = new Person("Hank");
    this.gabi = new Person("Gabi");
    ArrayList<Person> fayChildren = new ArrayList<Person>(Arrays.asList(this.len));
    this.fay = new Person("Fay", fayChildren);
    this.ed = new Person("Ed");
    this.dan = new Person("Dan");
    ArrayList<Person> coleChildren = new ArrayList<Person>(Arrays.asList(this.jan, this.kim));
    this.cole = new Person("Cole", coleChildren);
    ArrayList<Person> bobChildren = new ArrayList<Person>(
        Arrays.asList(this.ed, this.fay, this.gabi, this.hank));
    this.bob = new Person("Bob", bobChildren);
    ArrayList<Person> annChildren = new ArrayList<Person>(
        Arrays.asList(this.bob, this.cole, this.dan));
    this.ann = new Person("Ann", annChildren);
  }

  void testForEachLoopForSimpleExample(Tester t) {
    this.initData2();

    ArrayList<Person> aPeople = new ArrayList<Person>(Arrays.asList(this.a, this.b, this.c));
    ArrayList<Person> aTest = new ArrayList<Person>();

    int testIndex = 0;
    for (Person p : this.a) {
      t.checkExpect(p, aPeople.get(testIndex));
      aTest.add(p);
      testIndex = testIndex + 1;
      // System.out.println(p.name);
    }

    // order should be A -> B -> C
    t.checkExpect(aTest, aPeople);
  }

  void testForEachLoopForComplexExample(Tester t) {
    this.initData2();

    ArrayList<Person> annPeople = new ArrayList<Person>(Arrays.asList(this.ann, this.bob, this.cole,
        this.dan, this.ed, this.fay, this.gabi, this.hank, this.jan, this.kim, this.len));
    ArrayList<Person> annTest = new ArrayList<Person>();

    int testIndex = 0;
    for (Person p : this.ann) {
      t.checkExpect(p, annPeople.get(testIndex));
      annTest.add(p);
      testIndex = testIndex + 1;
      // System.out.println(p.name);
    }

    // order should be Ann -> Bob -> Cole -> Dan -> Ed -> Fay
    // -> Gabi -> Hank -> Jan -> Kim -> Len
    t.checkExpect(annTest, annPeople);
  }

  void testHasNextAndNextForSimpleExample(Tester t) {
    this.initData2();

    Iterator<Person> famIter = this.a.iterator();

    ArrayList<Person> aPeople = new ArrayList<Person>(Arrays.asList(this.a, this.b, this.c));
    int testIndex = 0;
    while (famIter.hasNext()) {
      t.checkExpect(famIter.next(), aPeople.get(testIndex));
      testIndex = testIndex + 1;
    }

    t.checkExpect(famIter.hasNext(), false);
    t.checkException(new NoSuchElementException("No more people in this family tree"), famIter,
        "next");
  }

  void testHasNextAndNextForComplexExample(Tester t) {
    this.initData2();

    Iterator<Person> famIter = this.ann.iterator();

    t.checkExpect(famIter.hasNext(), true);
    t.checkExpect(famIter.next(), this.ann);

    t.checkExpect(famIter.hasNext(), true);
    t.checkExpect(famIter.next(), this.bob);

    t.checkExpect(famIter.hasNext(), true);
    t.checkExpect(famIter.next(), this.cole);

    t.checkExpect(famIter.hasNext(), true);
    t.checkExpect(famIter.next(), this.dan);

    t.checkExpect(famIter.hasNext(), true);
    t.checkExpect(famIter.next(), this.ed);

    t.checkExpect(famIter.hasNext(), true);
    t.checkExpect(famIter.next(), this.fay);

    t.checkExpect(famIter.hasNext(), true);
    t.checkExpect(famIter.next(), this.gabi);

    t.checkExpect(famIter.hasNext(), true);
    t.checkExpect(famIter.next(), this.hank);

    t.checkExpect(famIter.hasNext(), true);
    t.checkExpect(famIter.next(), this.jan);

    t.checkExpect(famIter.hasNext(), true);
    t.checkExpect(famIter.next(), this.kim);

    t.checkExpect(famIter.hasNext(), true);
    t.checkExpect(famIter.next(), this.len);

    t.checkExpect(famIter.hasNext(), false);
    t.checkException(new NoSuchElementException("No more people in this family tree"), famIter,
        "next");
  }

//test diagonal iterator for ListOfLists
  public void testLolDiagonalIterator(Tester t) {
    // example: 3x3 matrix
    ListOfLists<Integer> square = new ListOfLists<>();
    square.addNewList(); // row 0
    square.add(0, 1);
    square.add(0, 2);
    square.add(0, 3);

    square.addNewList(); // row 1
    square.add(1, 4);
    square.add(1, 5);
    square.add(1, 6);

    square.addNewList(); // row 2
    square.add(2, 7);
    square.add(2, 8);
    square.add(2, 9);

    LolDiagonalIterator<Integer> diag1 = new LolDiagonalIterator<>(square);

    t.checkExpect(diag1.hasNext(), true);
    t.checkExpect(diag1.next(), 1);
    t.checkExpect(diag1.hasNext(), true);
    t.checkExpect(diag1.next(), 5);
    t.checkExpect(diag1.hasNext(), true);
    t.checkExpect(diag1.next(), 9);
    t.checkExpect(diag1.hasNext(), false);

    t.checkException(new NoSuchElementException("No more diagonal elements"), diag1, "next");
  }

  // in the examples class in a test method:
  Student alice = new Student("Alice", 20);
  Student bob2 = new Student("Bob", 10);
  
  ArrayList<Student> students = new
      ArrayList<Student>(Arrays.asList(this.alice, this.bob2));
  
  public void testEquals(Tester t) {
    t.checkExpect(this.alice.equals(this.alice), true);
    t.checkExpect(this.alice.equals(new Student("Alice", 20)), true);
  }
  
  // 26.5 - EDGE CASES
  public void testHashCode(Tester t) {
    t.checkExpect(this.alice.hashCode(), -1074141420);
    t.checkExpect(this.alice.hashCode(), new Student("Alice", 20).hashCode());
    t.checkExpect(this.alice.hashCode() != this.bob.hashCode(), true);
  }
  
  public void testContains(Tester t) {
    t.checkExpect(this.students.contains(this.alice), true);
    t.checkExpect(this.students.contains(new Student("Alice", 20)), true);
  }
  
  
}