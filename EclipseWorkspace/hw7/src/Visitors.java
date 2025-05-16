import tester.*;
import java.util.function.Function;
import java.util.function.BiFunction;

// represents a generic list
interface IList<T> {
  // returns the result of applying the given visitor to this list
  <R> R accept(IListVisitor<T, R> visitor);
}

// represents an empty generic list
class MtList<T> implements IList<T> {

  MtList() {
  }

  // returns the result of applying the given visitor to this list
  public <R> R accept(IListVisitor<T, R> visitor) {
    return visitor.visitMtList(this);
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

  // returns the result of applying the given visitor to this list
  public <R> R accept(IListVisitor<T, R> visitor) {
    return visitor.visitConsList(this);
  }
}

// represents a generic list visitor
interface IListVisitor<T, R> {

  // visits an empty list
  R visitMtList(MtList<T> list);

  // visits a non-empty list
  R visitConsList(ConsList<T> list);
}

// applies a function to each element of the list
class MapVisitor<T, R> implements IListVisitor<T, IList<R>>, Function<IList<T>, IList<R>> {
  Function<T, R> func;

  MapVisitor(Function<T, R> func) {
    this.func = func;
  }

  // returns the result of the map visitor on the element
  public IList<R> apply(IList<T> element) {
    return element.accept(this);
  }

  // visits an empty list
  public IList<R> visitMtList(MtList<T> list) {
    return new MtList<R>();
  }

  // visits a non-empty list
  public IList<R> visitConsList(ConsList<T> list) {
    return new ConsList<R>(func.apply(list.first), list.rest.accept(this));
  }
}

// folds the list from right to left using a function
class FoldRVisitor<T, R> implements IListVisitor<T, R> {
  BiFunction<T, R, R> func;
  R base;

  FoldRVisitor(BiFunction<T, R, R> func, R base) {
    this.func = func;
    this.base = base;
  }

  // visits an empty list
  public R visitMtList(MtList<T> mt) {
    return base;
  }

  // visits a non-empty list
  public R visitConsList(ConsList<T> cons) {
    return func.apply(cons.first, cons.rest.accept(this));
  }
}

// appends another list to the current list
class AppendVisitor<T> implements IListVisitor<T, IList<T>> {
  IList<T> other;

  AppendVisitor(IList<T> other) {
    this.other = other;
  }

  // visits an empty list
  public IList<T> visitMtList(MtList<T> list) {
    return other;
  }

  // visits a non-empty list
  public IList<T> visitConsList(ConsList<T> list) {
    return new ConsList<T>(list.first, list.rest.accept(this));
  }
}

// represents an ancestor tree
interface IAT {
  <R> R accept(IATVisitor<R> v);
}

// represents an unknown ancestor
class Unknown implements IAT {

  Unknown() {
  }

  // returns the result of applying the given visitor to this person
  public <R> R accept(IATVisitor<R> v) {
    return v.visitUnknown(this);
  }
}

// represents a person in the ancestor tree
class Person implements IAT {
  String name;
  int yob;
  IAT parent1;
  IAT parent2;

  Person(String name, int yob, IAT parent1, IAT parent2) {
    this.name = name;
    this.yob = yob;
    this.parent1 = parent1;
    this.parent2 = parent2;
  }

  // returns the result of applying the given visitor to this person
  public <R> R accept(IATVisitor<R> v) {
    return v.visitPerson(this);
  }
}

// visitor interface for ancestor trees
interface IATVisitor<R> {

  // visits an unknown person
  R visitUnknown(Unknown unknown);

  // visits a person
  R visitPerson(Person person);
}

// extracts a list of names from the ancestor tree
class NamesVisitor implements IATVisitor<IList<String>> {

  NamesVisitor() {
  }

  // visits an unknown person
  public IList<String> visitUnknown(Unknown unknown) {
    return new MtList<>();
  }

  // visits a person
  public IList<String> visitPerson(Person person) {
    IList<String> nameList = new ConsList<>(person.name, new MtList<>());
    return nameList.accept(new AppendVisitor<>(
        person.parent1.accept(this).accept(new AppendVisitor<>(person.parent2.accept(this)))));
  }
}

// represents examples and tests for lists
class ExamplesLists {
  // empty list
  IList<Integer> emptyList = new MtList<>();
  // list with three numbers
  IList<Integer> list1 = new ConsList<>(1, new ConsList<>(2, new ConsList<>(3, new MtList<>())));
  // list with two numbers
  IList<Integer> list2 = new ConsList<>(4, new ConsList<>(5, new MtList<>()));

  // lambda funciton that doubles the integer given
  Function<Integer, Integer> doubleFunc = x -> x * 2;
  // map visitor that doubles every integer in the list
  MapVisitor<Integer, Integer> doubleVisitor = new MapVisitor<>(doubleFunc);

  // lambda function that returns the sum of two given integers
  BiFunction<Integer, Integer, Integer> sumFunc = (x, y) -> x + y;
  // foldr visitor that sums up every integer in the list
  FoldRVisitor<Integer, Integer> sumVisitor = new FoldRVisitor<>(sumFunc, 0);

  // a list that will be appended when called by the visitor
  IList<Integer> appendList = new ConsList<>(4, new ConsList<>(5, new MtList<>()));
  // append visitor that appends the list above to the given list
  AppendVisitor<Integer> appendVisitor = new AppendVisitor<>(appendList);

  // unknown person
  IAT unknown = new Unknown();
  // four different people
  IAT person1 = new Person("Alice", 1980, unknown, unknown);
  IAT person2 = new Person("Bob", 1950, person1, unknown);
  IAT person3 = new Person("Charlie", 1920, person2, person1);
  IAT person4 = new Person("Dave", 1900, person3, person2);

  // name visitor
  NamesVisitor nameVisitor = new NamesVisitor();

  // tests the accept method
  public void testAccept(Tester t) {
    // accepts an empty list
    t.checkExpect(emptyList.accept(doubleVisitor), new MtList<>());
    // accepts a non-empty list
    t.checkExpect(list1.accept(doubleVisitor),
        new ConsList<>(2, new ConsList<>(4, new ConsList<>(6, new MtList<>()))));
    // accepts a non-empty list
    t.checkExpect(list2.accept(doubleVisitor),
        new ConsList<>(8, new ConsList<>(10, new MtList<>())));
  }

  // tests the apply method
  public void testApply(Tester t) {
    // tests the visitor on a empty list
    t.checkExpect(doubleVisitor.apply(emptyList), new MtList<>());
    // tests the visitor on a non-empty list
    t.checkExpect(doubleVisitor.apply(list1),
        new ConsList<>(2, new ConsList<>(4, new ConsList<>(6, new MtList<>()))));
    // tests the visitor on another non-empty list
    t.checkExpect(doubleVisitor.apply(list2),
        new ConsList<>(-2, new ConsList<>(0, new ConsList<>(10, new MtList<>()))));
  }

  // tests the visitMtList method
  public void testVisitMtList(Tester t) {
    // visits an empty list with the double visitor
    t.checkExpect(doubleVisitor.visitMtList(new MtList<>()), new MtList<>());
    // visits an empty list with the sum visitor
    t.checkExpect(sumVisitor.visitMtList(new MtList<>()), 0);
    // visits and empty list with the append visitor
    t.checkExpect(appendVisitor.visitMtList(new MtList<>()), appendList);
  }

  // tests the visitConsList method
  public void testVisitConsList(Tester t) {
    // visits a cons list with one number with the double visitor
    t.checkExpect(doubleVisitor.visitConsList(new ConsList<>(0, new MtList<>())),
        new ConsList<>(0, new MtList<>()));
    // visits a cons list with multiple numbers with the double visitor
    t.checkExpect(
        doubleVisitor.visitConsList(new ConsList<>(10, new ConsList<>(20, new MtList<>()))),
        new ConsList<>(20, new ConsList<>(40, new MtList<>())));
    // visits a cons list with one number with the sum visitor
    t.checkExpect(sumVisitor.visitConsList(new ConsList<>(5, new MtList<>())), 5);
    // visits a cons list with negative numbers with the sum visitor
    t.checkExpect(sumVisitor.visitConsList(new ConsList<>(-3, new ConsList<>(-2, new MtList<>()))),
        -5);
    // visits a cons list with one number with the append visitor
    t.checkExpect(appendVisitor.visitConsList(new ConsList<>(8, new MtList<>())),
        new ConsList<>(8, appendList));
    // visits a cons list with multiple numbers with the append visitor
    t.checkExpect(
        appendVisitor.visitConsList(new ConsList<>(100, new ConsList<>(200, new MtList<>()))),
        new ConsList<>(100, new ConsList<>(200, appendList)));
  }

  // tests the visitUnknown method
  public void testVisitUnknown(Tester t) {
    // visits an unknown person with the name visitor
    t.checkExpect(nameVisitor.visitUnknown(new Unknown()), new MtList<>());
  }

  // tests the visitPerson method
  public void testVisitPerson(Tester t) {
    // visits a singular person with the name visitor
    t.checkExpect(nameVisitor.visitPerson(new Person("Alice", 1980, unknown, unknown)),
        new ConsList<>("Alice", new MtList<>()));
    // visits a person with one parent
    t.checkExpect(nameVisitor.visitPerson(new Person("Bob", 1950, person1, unknown)),
        new ConsList<>("Bob", new ConsList<>("Alice", new MtList<>())));
    // visits a person with multiple generations
    t.checkExpect(nameVisitor.visitPerson(new Person("Charlie", 1920, person2, person1)),
        new ConsList<>("Charlie", new ConsList<>("Bob",
            new ConsList<>("Alice", new ConsList<>("Alice", new MtList<>())))));
    // visits a person with a deep family tree
    t.checkExpect(nameVisitor.visitPerson(new Person("Dave", 1900, person3, person2)),
        new ConsList<>("Dave",
            new ConsList<>("Charlie",
                new ConsList<>("Bob", new ConsList<>("Alice", new ConsList<>("Alice",
                    new ConsList<>("Bob", new ConsList<>("Alice", new MtList<>()))))))));
  }
}
