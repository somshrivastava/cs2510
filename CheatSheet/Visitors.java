interface IShape {
  <T> T accept(IShapeVisitor<T> func);
}

class Circle implements IShape {
  int radius;
  Circle(int r) {
    this.radius = r;
  }
  public <T> T accept(IShapeVisitor<T> func) {
    return func.visitCircle(this);
  }
}

class Rectangle implements IShape {
  int length;
  int width;
  Rectangle(int l, int w) {
    this.length = l;
    this.width = w;
  }
  public <T> T accept(IShapeVisitor<T> func) {
    return func.visitRectangle(this);
  }
}

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

interface IList<T> {
  <R> R accept(IListVisitor<T, R> visitor);
}

class MtList<T> implements IList<T> {
  MtList() {
  }
  public <R> R accept(IListVisitor<T, R> visitor) {
    return visitor.visitMtList(this);
  }
}

class ConsList<T> implements IList<T> {
  T first;
  IList<T> rest;
  ConsList(T first, IList<T> rest) {
    this.first = first;
    this.rest = rest;
  }
  public <R> R accept(IListVisitor<T, R> visitor) {
    return visitor.visitConsList(this);
  }
}

interface IListVisitor<T, R> {
  R visitMtList(MtList<T> list);
  R visitConsList(ConsList<T> list);
}

// applies a function to each element of the list
class MapVisitor<T, R> implements IListVisitor<T, IList<R>>, Function<IList<T>, IList<R>> {
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
  public R visitMtList(MtList<T> mt) {
    return base;
  }
  public R visitConsList(ConsList<T> cons) {
    return func.apply(cons.first, cons.rest.accept(this));
  }
}

class AppendVisitor<T> implements IListVisitor<T, IList<T>> {
  IList<T> other;
  AppendVisitor(IList<T> other) {
    this.other = other;
  }
  public IList<T> visitMtList(MtList<T> list) {
    return other;
  }
  public IList<T> visitConsList(ConsList<T> list) {
    return new ConsList<T>(list.first, list.rest.accept(this));
  }
}