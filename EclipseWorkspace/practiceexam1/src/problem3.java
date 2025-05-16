import tester.Tester;
import java.awt.Color;
import javalib.funworld.*;
import javalib.worldimages.*;

// represents a list of words
interface ILoString {

  PairOfLists unzip();
  
  PairOfLists unzipSecond();

  ILoString helperMethod();

  ILoString helperMethod2();
}

// represents an empty list of words
class MtLoString implements ILoString {

  public PairOfLists unzip() {
    return new PairOfLists(new MtLoString(), new MtLoString());
  };
  
  public PairOfLists unzipSecond() {
    return new PairOfLists(new MtLoString(), new MtLoString());
  }

  public ILoString helperMethod() {
    return new MtLoString();
  }

  public ILoString helperMethod2() {
    return new MtLoString();
  }
}

// represents a list of words
class ConsLoString implements ILoString {
  String first;
  ILoString rest;

  ConsLoString(String first, ILoString rest) {
    this.first = first;
    this.rest = rest;
  }

  public PairOfLists unzip() {
    return this.rest.unzipSecond().addToFirst(this.first);
    // return new PairOfLists(this.helperMethod(), this.rest.helperMethod());
  }
  
  public PairOfLists unzipSecond() {
    return this.rest.unzip().addToSecond(this.first);
  }

  public ILoString helperMethod() {
    return new ConsLoString(this.first, this.rest.helperMethod2());
  }

  public ILoString helperMethod2() {
    return this.rest.helperMethod();
  }
}

class PairOfLists {
  ILoString first, second;

  PairOfLists(ILoString first, ILoString second) {
    this.first = first;
    this.second = second;
  }

  // Produces a new pair of lists, with the given String added to
  // the front of the first list of this pair
  PairOfLists addToFirst(String first) {
    return new PairOfLists(new ConsLoString(first, this.first), this.second);
  }

  // Produces a new pair of lists, with the given String added to
  // the front of the second list of this pair
  PairOfLists addToSecond(String second) {
    return new PairOfLists(this.first, new ConsLoString(second, this.second));
  }
}

class ExampleStrings {
  ILoString example = new ConsLoString("A", new ConsLoString("B",
      new ConsLoString("C", new ConsLoString("D", new ConsLoString("E", new MtLoString())))));
  ILoString mt = new MtLoString();
  ILoString even = new ConsLoString("A", new ConsLoString("B", new ConsLoString("C",
      new ConsLoString("D", new ConsLoString("E", new ConsLoString("F", new MtLoString()))))));

  public boolean testUnzip(Tester t) {
    return t.checkExpect(example.unzip(),
        new PairOfLists(
            new ConsLoString("A", new ConsLoString("C", new ConsLoString("E", new MtLoString()))),
            new ConsLoString("B", new ConsLoString("D", new MtLoString()))))
        && t.checkExpect(mt.unzip(), new PairOfLists(new MtLoString(), new MtLoString()))
    && t.checkExpect(even.unzip(), new PairOfLists(
        new ConsLoString("A", new ConsLoString("C", new ConsLoString("E", new MtLoString()))),
        new ConsLoString("B", new ConsLoString("D", new ConsLoString("F", new MtLoString())))));
  }
}