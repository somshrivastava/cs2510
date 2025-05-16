class MapVisitor<T, R> implements IListVisitor<T, IList<R>> {
  Function<T, R> func;
  MapVisitor(Function<T, R> func) {
    this.func = func;
  }
  public IList<R> apply(IList<T> element) {
    return element.accept(this);
  }
  public IList<R> visitMtList(MtList<T> list) {
    return new MtList<R>();
  }
  public IList<R> visitConsList(ConsList<T> list) {
    return new ConsList<R>(func.apply(list.first), list.rest.accept(this));
  }
}

class FoldRVisitor<T, R> implements IListVisitor<T, R> {
  BiFunction<T, R, R> func;
  R base;
  FoldRVisitor(BiFunction<T, R, R> func, R base) {
    this.func = func;
    this.base = base;
  }
  public R apply(IList<T> list) {
    return list.accept(this);
  }
  public R visitMtList(MtList<T> mt) {
    return base;
  }
  public R visitConsList(ConsList<T> cons) {
    return func.apply(cons.first, cons.rest.accept(this));
  }
}

class Instructor {
  String name;
  IList<Course> courses;
  Instructor(String name) {
    this.name = name;
    this.courses = new MtList<Course>();
  }
  public void addCourse(Course c) {
    this.courses = new ConsList<Course>(c, this.courses);
  }
  boolean dejavu(Student s) {
    return new FoldRVisitor<Boolean, Integer>(new AddOneIfTrue(), 0).apply(
        new MapVisitor<Course, Boolean>((Course c) -> c.hasStudent(s)).apply(this.courses)) >= 2;
  }
}

class Student {
  String name;
  int id;
  IList<Course> courses;
  Student(String name, int id) {
    this.name = name;
    this.id = id;
    this.courses = new MtList<Course>();
  }
  public void enroll(Course c) {
    this.courses = new ConsList<Course>(c, this.courses);
    c.addStudent(this);
  }
  public boolean classmates(Student s) {
    return new FoldRVisitor<Boolean, Boolean>((b1, b2) -> b1 || b2, false)
        .apply(new MapVisitor<Course, Boolean>((Course c) -> c.hasStudent(s)).apply(this.courses));
  }
}

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
  public void addStudent(Student s) {
    this.students = new ConsList<Student>(s, this.students);
  }
  boolean hasStudent(Student s) {
    return new FoldRVisitor<Boolean, Boolean>((b1, b2) -> b1 || b2, false)
        .apply(new MapVisitor<Student, Boolean>(student -> student == s).apply(this.students));
  }
}