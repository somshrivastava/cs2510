import tester.Tester;
import java.awt.Color;
import javalib.funworld.*;
import javalib.worldimages.*;

// represents a list of words
interface ILoString {
  
  // returns the length of the list
  int length();
  
  // keeps every other element of the list
  ILoString keepEveryOther();
  
  // keepEveryNth
  ILoString keepEveryNth(int n);
  
  // keepEveryNthHelper
  ILoString keepEveryNthHelper(int n, int c);
  
  // determines if the list has the same contents as the given list
  boolean hasSameContentAs(ILoString given);
  
  // determines whether given list is a subset of the list
  boolean isSubset(ILoString other);
  
  // determines whether the string exists within the list
  boolean exists(String s);
  
  // determines whether the given list is the same as the list
  boolean sameList(ILoString given);
  
  // determines whether its the same MtLoList
  boolean sameMtLoList(MtLoString other);
  
  // determines whether its the same ConsLoList
  boolean sameConsLoList(ConsLoString other);
  
  // returns a list with duplicates removed
  ILoString keepUnique();
  
  // keepUniqueHelper
  ILoString keepUniqueHelper(ILoString other);
  
  // reverses the list with recursion
  ILoString reverse();
  
  // adds a string to the end of a list
  ILoString addToLast(String s);
  
  // reverse the list with an accumulator
  ILoString reverse2();
  
  // reverse2Helper
  ILoString reverse2Helper(ILoString acc);
}

// represents an empty list of words
class MtLoString implements ILoString {

  /*
   * Fields:
   * 
   * Methods:
   * this.length() -> int
   * 
   * Methods on fields:
   * 
   * */
  
  // returns the length of the list
  public int length() {
    return 0;
  }

  // keeps every other element of the list
  public ILoString keepEveryOther() {
    return new MtLoString();
  }

  // keepEveryNth
  public ILoString keepEveryNth(int n) {
    return new MtLoString();
  }

  // keepEveryNthHelper
  public ILoString keepEveryNthHelper(int n, int c) {
    return new MtLoString();
  }

  // determines if the list has the same contents as the given list
  public boolean hasSameContentAs(ILoString given) {
    return this.isSubset(given) && given.isSubset(this);
  }

  // determines whether given list is a subset of the list
  public boolean isSubset(ILoString other) {
    return true;
  }

  // determines whether the string exists within the list
  public boolean exists(String s) {
    return false;
  }

  // determines whether the given list is the same as the list
  public boolean sameList(ILoString given) {
    return given.sameMtLoList(this);
  }

  // determines whether its the same MtLoList
  public boolean sameMtLoList(MtLoString other) {
    return true;
  }

  // determines whether its the same ConsLoList
  public boolean sameConsLoList(ConsLoString other) {
    return false;
  }

  // returns a list with duplicates removed
  public ILoString keepUnique() {
    return new MtLoString();
  }
   
  // keepUniqueHelper
  public ILoString keepUniqueHelper(ILoString other) {
    return other;
  }
  
  // reverse the list with recursion
  public ILoString reverse() {
    return new MtLoString();
  }
  
  // adds a string to the end of a list
  public ILoString addToLast(String s) {
    return new ConsLoString(s, new MtLoString());
  }
  
  // reverse the list with an accumulator
  public ILoString reverse2() {
    return new MtLoString();
  }
  
  // reverse2Helper
  public ILoString reverse2Helper(ILoString acc) {
    return acc;
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
  
  /*
   * Fields:
   * 
   * Methods:
   * this.length() -> int
   * 
   * Methods on fields:
   * 
   * */
  
  // returns the length of the list
  public int length() {
    return 1 + this.rest.length();
  }
    
  // keeps every other element of the list
  public ILoString keepEveryOther() {
    return this.keepEveryNth(2);
  }
  
  // keepEveryNth
  public ILoString keepEveryNth(int n) {
    return this.keepEveryNthHelper(n, 0);
  }
  
  // keepEveryNthHelper
  public ILoString keepEveryNthHelper(int n, int c) {
    if (c % n == 0) {
      return new ConsLoString(this.first, this.rest.keepEveryNthHelper(n, c + 1));
    } else {
      return this.rest.keepEveryNthHelper(n, c + 1);
    }
  }
    
  // determines if the list has the same contents as the given list
  public boolean hasSameContentAs(ILoString given) {
    return this.isSubset(given) && given.isSubset(this);
  }
  
  // determines whether given list is a subset of the list
  public boolean isSubset(ILoString other) {
    return other.exists(this.first) && this.rest.isSubset(other);
  }
  
  // determines whether the string exists within the list
  public boolean exists(String s) {
    if (this.first.equals(s)) {
      return true;
    } else {
      return this.rest.exists(s);
    }
  }
  
  // determines whether the given list is the same as the given list
  public boolean sameList(ILoString given) {
    return given.sameConsLoList(this);
  }
  
  // determines whether its the same MtLoList
  public boolean sameMtLoList(MtLoString other) {
    return false;
  }
  
  // determines whether its the same ConsLoList
  public boolean sameConsLoList(ConsLoString other) {
    return this.first.equals(other.first) && other.rest.sameList(this.rest);
  }

  // returns a list with duplicates removed
  public ILoString keepUnique() {
    return this.rest.keepUniqueHelper(new ConsLoString(this.first, new MtLoString())).reverse();
  }
  
  // keepUniqueHelper
  public ILoString keepUniqueHelper(ILoString other) {
   if (this.rest.exists(this.first)) {
     return this.rest.keepUniqueHelper(new ConsLoString(this.first, other));
   } else {
     return this.rest.keepUniqueHelper(other);
   }
  }
  
  // reverses the list with recursion
  public ILoString reverse() {
    return this.rest.reverse().addToLast(this.first);
  }
  
  // adds a string to the end of a list
  public ILoString addToLast(String s) {
    return new ConsLoString(this.first, this.rest.addToLast(s));
  }
  
  // reverse the list with an accumulator
  public ILoString reverse2() {
    return this.reverse2Helper(new MtLoString());
  }
  
  // reverse2Helper
  public ILoString reverse2Helper(ILoString acc) {
    return this.rest.reverse2Helper(new ConsLoString(this.first, acc));
  }
}

// examples and tests for list of strings
class ExampleStrings {
  ILoString example1 = new MtLoString();
  ILoString example2 = new ConsLoString("a",
      new ConsLoString("b", new ConsLoString("c", new MtLoString())));
  ILoString example3 = new ConsLoString("a", new ConsLoString("c", new MtLoString()));
  ILoString example4 = new ConsLoString("b", new MtLoString());
  ILoString example5 = new ConsLoString("c", new ConsLoString("a", new MtLoString()));
  ILoString example6 = new ConsLoString("a", new ConsLoString("a", new MtLoString()));
  ILoString example7 = new ConsLoString("a", new MtLoString());
  ILoString example8 = new ConsLoString("a", new ConsLoString("a", new ConsLoString("b", new ConsLoString("b", new MtLoString()))));
  ILoString example9 = new ConsLoString("a", new ConsLoString("b", new MtLoString()));

  // test the length method
  boolean testLength(Tester t) {
    return t.checkExpect(example1.length(), 0)
        // checks the length of an empty list of strings
        && t.checkExpect(example2.length(), 3);
    // checks the length of a filled list of strings
  }

  // test keepEveryOther
  boolean testKeepEveryOther(Tester t) {
    return t.checkExpect(example1.keepEveryOther(), example1)
        // tests for any empty list
        && t.checkExpect(example2.keepEveryOther(), example3);
    // tests for a filled list
  }
  
  // test keepEveryNth
  boolean testKeepEveryNth(Tester t) {
    return t.checkExpect(example1.keepEveryNth(2), example1)
        && t.checkExpect(example2.keepEveryNth(2), example3);
  }
  
  // test reverse
  boolean testReverse(Tester t) {
    return t.checkExpect(example3.reverse(), example5);
  }
  
  // test reverse2
  boolean testReverse2(Tester t) {
    return t.checkExpect(example3.reverse2(), example5);
  }
  
  // test hasSameContentAs
  boolean testHasSameContentAs(Tester t) {
    return t.checkExpect(example1.hasSameContentAs(example1), true)
        && t.checkExpect(example1.hasSameContentAs(example2), false)
        && t.checkExpect(example2.hasSameContentAs(example2), true)
        && t.checkExpect(example2.hasSameContentAs(example1), false)
        && t.checkExpect(example3.hasSameContentAs(example5), true);
  }
  
  // test sameList
  boolean testSameList(Tester t) {
    return t.checkExpect(example1.sameList(example1), true)
        && t.checkExpect(example1.sameList(example2), false)
        && t.checkExpect(example2.sameList(example2), true)
        && t.checkExpect(example2.sameList(example1), false)
        && t.checkExpect(example3.sameList(example5), false);
  }
  
  // test keepUnique
  boolean testKeepUnique(Tester t) {
    return t.checkExpect(example6.keepUnique(), example7)
        && t.checkExpect(example8.keepUnique(), example9);
  }
}