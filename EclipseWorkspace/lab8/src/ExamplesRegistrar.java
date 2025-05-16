import tester.*;
import java.util.function.BiFunction;
import java.util.function.Function;

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
class MapVisitor<T, R> implements IListVisitor<T, IList<R>> {
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

  // accepts a list
  public R apply(IList<T> list) {
    return list.accept(this);
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

// represents an instructor
class Instructor {
  String name;
  IList<Course> courses;

  Instructor(String name) {
    this.name = name;
    this.courses = new MtList<Course>();
  }

  // adds a course to the list of courses the instructor teachers
  public void addCourse(Course c) {
    this.courses = new ConsList<Course>(c, this.courses);
  }

  // determines whether the given student is in more than one of the instructor's
  // classes
  boolean dejavu(Student s) {
    return new FoldRVisitor<Boolean, Integer>(new AddOneIfTrue(), 0).apply(
        new MapVisitor<Course, Boolean>((Course c) -> c.hasStudent(s)).apply(this.courses)) >= 2;
  }
}

// represents a student
class Student {
  String name;
  int id;
  IList<Course> courses;

  Student(String name, int id) {
    this.name = name;
    this.id = id;
    this.courses = new MtList<Course>();
  }

  // adds the course to the list of courses the student is taking
  // adds the student to the list of students in the course
  public void enroll(Course c) {
    this.courses = new ConsList<Course>(c, this.courses);
    c.addStudent(this);
  }

  // determines if the given student and this student are classmates
  public boolean classmates(Student s) {
    return new FoldRVisitor<Boolean, Boolean>((b1, b2) -> b1 || b2, false)
        .apply(new MapVisitor<Course, Boolean>((Course c) -> c.hasStudent(s)).apply(this.courses));
  }
}

// represents a course
class Course {
  String name;
  Instructor prof;
  IList<Student> students;

  Course(String name, Instructor prof) {
    this.name = name;
    this.prof = prof;
    this.students = new MtList<Student>();
    this.prof.addCourse(this);
  }

  // adds the given student to the list of students within the course
  public void addStudent(Student s) {
    this.students = new ConsList<Student>(s, this.students);
  }

  // returns whether or not the course contains the given student
  boolean hasStudent(Student s) {
    return new FoldRVisitor<Boolean, Boolean>((b1, b2) -> b1 || b2, false)
        .apply(new MapVisitor<Student, Boolean>(student -> student == s).apply(this.students));
  }
}

// accumulates based on boolean values
class AddOneIfTrue implements BiFunction<Boolean, Integer, Integer> {

  // adds one if true else returns the same number
  public Integer apply(Boolean b, Integer n) {
    if (b) {
      return n + 1;
    }
    return n;
  }
}

// examples and tests for registrar
class ExamplesRegistrar {
  // example students
  Student som;
  Student ryan;
  Student arthur;
  Student anoushka;
  Student naman;

  // example instructors
  Instructor razzaq;
  Instructor sloan;
  Instructor massey;
  Instructor hamlin;

  // example courses
  Course fundies1;
  Course fundies2;
  Course calc3;
  Course discrete;

  // empty list
  IList<Integer> emptyList = new MtList<>();
  // list with three numbers
  IList<Integer> list1 = new ConsList<>(1, new ConsList<>(2, new ConsList<>(3, new MtList<>())));
  // list with two numbers
  IList<Integer> list2 = new ConsList<>(4, new ConsList<>(5, new MtList<>()));
  // list of numbers
  IList<Integer> numbers = new ConsList<>(1, new ConsList<>(2, new ConsList<>(3, new MtList<>())));

  // lambda function that doubles the integer given
  Function<Integer, Integer> doubleFunc = x -> x * 2;
  // map visitor that doubles every integer in the list
  MapVisitor<Integer, Integer> doubleVisitor = new MapVisitor<>(doubleFunc);

  // lambda function that returns the sum of two given integers
  BiFunction<Integer, Integer, Integer> sumFunc = (x, y) -> x + y;
  // foldr visitor that sums up every integer in the list
  FoldRVisitor<Integer, Integer> sumVisitor = new FoldRVisitor<>(sumFunc, 0);

  // instance of the addOneIfTrue class
  AddOneIfTrue addOne = new AddOneIfTrue();

  // initialize test data
  public void initializeData() {
    // make students
    som = new Student("Som", 1);
    ryan = new Student("Ryan", 2);
    arthur = new Student("Arthur", 3);
    anoushka = new Student("Anoushka", 4);
    naman = new Student("Naman", 5);

    // make instructors
    razzaq = new Instructor("Razzaq");
    sloan = new Instructor("Sloan");
    massey = new Instructor("Massey");
    hamlin = new Instructor("Hamlin");

    // make courses
    fundies1 = new Course("Fundies 1", this.razzaq);
    fundies2 = new Course("Fundies 2", this.razzaq);
    calc3 = new Course("Calc 3", this.massey);
    discrete = new Course("Discrete", this.hamlin);
  }

  // testing the enroll method
  public void testEnroll(Tester t) {
    // initialize data
    initializeData();
    // enrolling a student with no courses to a course
    this.som.enroll(fundies1);
    t.checkExpect(this.som.courses, new ConsList<Course>(this.fundies1, new MtList<Course>()));
    t.checkExpect(this.fundies1.students, new ConsList<Student>(this.som, new MtList<Student>()));

    // enrolling a student into a second course
    this.som.enroll(this.calc3);
    t.checkExpect(this.som.courses, new ConsList<Course>(this.calc3,
        new ConsList<Course>(this.fundies1, new MtList<Course>())));
    t.checkExpect(this.calc3.students, new ConsList<Student>(this.som, new MtList<Student>()));

    // enrolling a second student to a course
    this.ryan.enroll(fundies1);
    t.checkExpect(this.ryan.courses, new ConsList<Course>(this.fundies1, new MtList<Course>()));
    t.checkExpect(this.fundies1.students,
        new ConsList<Student>(this.ryan, new ConsList<Student>(this.som, new MtList<Student>())));
  }

  // testing the classmates method
  public void testClassmates(Tester t) {
    // initialize data
    initializeData();

    // testing two students that are not classmates
    this.som.enroll(fundies1);
    t.checkExpect(this.som.classmates(this.ryan), false);

    // testing two students that are classmates
    this.ryan.enroll(fundies1);
    t.checkExpect(this.som.classmates(this.ryan), true);

    // testing two students that are classmates in more than one class
    this.som.enroll(calc3);
    this.ryan.enroll(calc3);
    t.checkExpect(this.som.classmates(this.ryan), true);

    // testing if a student is a classmate of itself
    t.checkExpect(this.som.classmates(this.som), true);
  }

  // testing the dejavu method
  public void testDejavu(Tester t) {
    // initialize data
    initializeData();

    // the student is in none of the instructor's courses
    t.checkExpect(razzaq.dejavu(this.som), false);

    // the student is in one of the instructor's courses
    this.som.enroll(fundies1);
    t.checkExpect(razzaq.dejavu(this.som), false);

    // the student is in two of the instructor's courses
    this.som.enroll(fundies2);
    t.checkExpect(razzaq.dejavu(this.som), true);
  }

  // testing the addCourse method
  public void testAddCourse(Tester t) {
    // initialize data
    initializeData();

    // tests to see if razzaq has the two courses initially assigned to her
    t.checkExpect(this.razzaq.courses, new ConsList<Course>(this.fundies2,
        new ConsList<Course>(this.fundies1, new MtList<Course>())));

    // tests to see if razzaq has the new course added for her
    this.razzaq.addCourse(this.calc3);
    t.checkExpect(this.razzaq.courses,
        new ConsList<Course>(this.calc3, new ConsList<Course>(this.fundies2,
            new ConsList<Course>(this.fundies1, new MtList<Course>()))));

    // tests to see if razzaq has the other new course added for her
    this.razzaq.addCourse(this.discrete);
    t.checkExpect(this.razzaq.courses,
        new ConsList<Course>(this.discrete,
            new ConsList<Course>(this.calc3, new ConsList<Course>(this.fundies2,
                new ConsList<Course>(this.fundies1, new MtList<Course>())))));
  }

  // testing the addStudent method
  public void testAddStudent(Tester t) {
    // initialize data
    initializeData();

    // tests by adding a student to a course with no students
    this.fundies1.addStudent(this.som);
    t.checkExpect(this.fundies1.students, new ConsList<Student>(this.som, new MtList<Student>()));

    // tests by adding a student to a course with one student
    this.fundies1.addStudent(this.ryan);
    t.checkExpect(this.fundies1.students,
        new ConsList<Student>(this.ryan, new ConsList<Student>(this.som, new MtList<Student>())));

    // tests by adding a student to a course with two students
    this.fundies1.addStudent(this.arthur);
    t.checkExpect(this.fundies1.students, new ConsList<Student>(this.arthur,
        new ConsList<Student>(this.ryan, new ConsList<Student>(this.som, new MtList<Student>()))));
  }

  // testing the hasStudent method
  public void testHasStudent(Tester t) {
    // initialize data
    initializeData();

    // tests to see if a student not in the course is in the course
    t.checkExpect(this.fundies1.hasStudent(this.som), false);

    // tests to see if a student in the course in in the course
    this.fundies1.addStudent(this.som);
    t.checkExpect(this.fundies1.hasStudent(this.som), true);

    // tests to see if a student in another course is in the other course
    this.calc3.addStudent(this.ryan);
    t.checkExpect(this.calc3.hasStudent(this.ryan), true);
  }

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
        new ConsList<>(8, new ConsList<>(10, new MtList<>())));
    // tests the apply method within the foldr visitor
    t.checkExpect(sumVisitor.apply(numbers), 6);
    // tests the apply method within the addOneIfTrue class
    t.checkExpect(addOne.apply(true, 5), 6);
    // tests the apply method within the addOneIfTrue class
    t.checkExpect(addOne.apply(false, 5), 5);
  }

  // tests the visitMtList method
  public void testVisitMtList(Tester t) {
    // visits an empty list with the double visitor
    t.checkExpect(doubleVisitor.visitMtList(new MtList<>()), new MtList<>());
    // visits an empty list with the sum visitor
    t.checkExpect(sumVisitor.visitMtList(new MtList<>()), 0);
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
  }
}