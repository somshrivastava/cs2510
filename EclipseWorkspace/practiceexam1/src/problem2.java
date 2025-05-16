import tester.Tester;

// interface for ancestry tree
interface IAT {
  // returns true if ancestry tree contains some Person with the given name
  boolean containsName(String name);
  
  // returns true if anyone in the ancestry tree has the same name as one of their ancestors
  boolean duplicateNames();
}

// representation of unknown person
class Unknown implements IAT {
  
  // returns true if ancestry tree contains some Person with the given name
  public boolean containsName(String name) {
    return false;
  }
  
  // returns true if anyone in the ancestry tree has the same name as one of their ancestors
  public boolean duplicateNames() {
    return false;
  }
}

// representation of person
class Person implements IAT {
  String name;
  IAT dad, mom;

  Person(String name, IAT dad, IAT mom) {
    this.name = name;
    this.dad = dad;
    this.mom = mom;
  }
  
  /*
   * Fields:
   * this.name -> String
   * this.dad -> IAT
   * this.mom -> IAT
   * 
   * Methods:
   * this.containsName(String name) -> boolean
   * 
   * Methods on fields:
   * this.mom.containsName(String name) -> boolean
   * this.dad.containsName(String name) -> boolean
   * 
   * */
  
  // returns true if ancestry tree contains some Person with the given name
  public boolean containsName(String name) {
    return this.name.equals(name) || this.dad.containsName(name) || this.mom.containsName(name);
  }
  
  // returns true if anyone in the ancestry tree has the same name as one of their ancestors
  public boolean duplicateNames() {
    return this.mom.containsName(this.name) || this.dad.containsName(this.name) || this.mom.duplicateNames() || this.dad.duplicateNames();
  }
}

// test and examples for Person class
class ExamplePerson {
  IAT davisSr = new Person("Davis", new Unknown(), new Unknown());
  IAT edna = new Person("Edna", new Unknown(), new Unknown());
  IAT davisJr = new Person("Davis", davisSr, edna);
  IAT carl = new Person("Carl", new Unknown(), new Unknown());
  IAT candace = new Person("Candace", davisJr, new Unknown());
  IAT claire = new Person("Claire", new Unknown(), new Unknown());
  IAT bill = new Person("Bill", carl, candace);
  IAT bree = new Person("Bree", new Unknown(), claire);
  IAT anthony = new Person("Anthony", bill, bree);
  
  // tests the containsName method within the IAT interface
  boolean testContainsName(Tester t) {
    return t.checkExpect(davisSr.containsName("Edna"), false)
        && t.checkExpect(davisJr.containsName("Edna"), true)
        && t.checkExpect(bree.containsName("Bree"), true);
  }
}