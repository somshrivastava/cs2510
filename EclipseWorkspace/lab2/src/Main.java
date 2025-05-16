import tester.*;

// to represent a pet owner
class Person {
    String name;
    IPet pet;
    int age;
 
    Person(String name, IPet pet, int age) {
        this.name = name;
        this.pet = pet;
        this.age = age;
    }
    
    /* TEMPLATE:
       Fields:
       ... this.name ...         -- String
       ... this.pet ...          -- IPet
       ... this.age ...          -- int
     
       Methods:
       ... this.isOlder(Person) ...       -- boolean
       ... this.sameNamePet(Person) ...   -- boolean
       
       Methods on fields:
       ... this.pet.sameNamePet() ...     -- boolean
    */
    
    // is this Person older than the given Person?
    boolean isOlder(Person other) {
      /* TEMPLATE:
        Fields:
        ... this.name ...         -- String
        ... this.pet ...          -- IPet
        ... this.age ...          -- int
    
        Methods:
        ... this.isOlder(Person) ...       -- boolean
        ... this.sameNamePet(Person) ...   -- boolean
      
        Methods on fields:
        ... this.pet.sameNamePet() ...     -- boolean
      */
      return this.age > other.age;
    }
    
    // does the name of this person's pet match the given name?
    boolean sameNamePet(String name) {
      /* TEMPLATE:
        Fields:
        ... this.name ...         -- String
        ... this.pet ...          -- IPet
        ... this.age ...          -- int
    
        Methods:
        ... this.isOlder(Person) ...       -- boolean
        ... this.sameNamePet(Person) ...   -- boolean
      
        Methods on fields:
        ... this.pet.sameNamePet() ...     -- boolean
      */
      return this.pet.sameNamePet(name);
    }
    
    Person perish() {
      /* TEMPLATE:
        Fields:
        ... this.name ...         -- String
        ... this.pet ...          -- IPet
        ... this.age ...          -- int
    
        Methods:
        ... this.isOlder(Person) ...       -- boolean
        ... this.sameNamePet(Person) ...   -- boolean
      
        Methods on fields:
        ... this.pet.sameNamePet() ...     -- boolean
      */ 
      return new Person(this.name, new NoPet(), this.age);
    }
}

// to represent a pet
interface IPet { 
  /* TEMPLATE:
     Methods:
     ... this.sameNamePet(Person) ...   -- boolean
  */
  
  boolean sameNamePet(String name);
}
 
// to represent a pet cat
class Cat implements IPet {
    String name;
    String kind;
    boolean longhaired;
 
    Cat(String name, String kind, boolean longhaired) {
        this.name = name;
        this.kind = kind;
        this.longhaired = longhaired;
    }
    
    /* TEMPLATE:
      Fields:
      ... this.name ...         -- String
      ... this.kind ...         -- String
      ... this.longhaired ...   -- boolean
  
      Methods:
      ... this.sameNamePet(Person) ...   -- boolean
    */
    
    // does the name of this person's pet match the given name?
    public boolean sameNamePet(String name) {
      /* TEMPLATE:
        Fields:
        ... this.name ...         -- String
        ... this.kind ...         -- String
        ... this.longhaired ...   -- boolean
  
        Methods:
        ... this.sameNamePet(Person) ...   -- boolean
      */
      return this.name.equals(name);
    }
}
 
// to represent a pet dog
class Dog implements IPet {
    String name;
    String kind;
    boolean male;
 
    Dog(String name, String kind, boolean male) {
        this.name = name;
        this.kind = kind;
        this.male = male;
    }
    
    /* TEMPLATE:
    Fields:
      ... this.name ...         -- String
      ... this.kind ...         -- String
      ... this.male ...         -- boolean

      Methods:
      ... this.sameNamePet(Person) ...   -- boolean
    */
    
    // does the name of this person's pet match the given name?
    public boolean sameNamePet(String name) {
      /* TEMPLATE:
        Fields:
        ... this.name ...         -- String
        ... this.kind ...         -- String
        ... this.male ...         -- boolean

        Methods:
        ... this.sameNamePet(Person) ...   -- boolean
      */
      return this.name.equals(name);
    }
}

// to represent no pet
class NoPet implements IPet {
  NoPet() { }
  
  /* TEMPLATE:
    Methods:
    ... this.sameNamePet(Person) ...   -- boolean
  */
  
  public boolean sameNamePet(String name) {
    /* TEMPLATE:
      Methods:
      ... this.sameNamePet(Person) ...   -- boolean
    */
    return false;
  }
}

// examples for the classes that represent pet owners
class ExamplesPerson {
  IPet cat1 = new Cat("Luna", "Siamese", true);
  IPet cat2 = new Cat("Milo", "Persian", true);
  IPet dog1 = new Dog("Maggie", "Beagle", true);
  IPet dog2 = new Dog("Daisy", "Bulldog", true);
  IPet nopet = new NoPet();
  
  Person person1 = new Person("Som", cat1, 18);
  Person person2 = new Person("Neal", cat2, 18);
  Person person3 = new Person("Ryan", dog1, 19);
  Person person4 = new Person("Naman", dog2, 18);
  
  // test the method isOlder in the class Person
  boolean testIsOlder(Tester t) {
    return t.checkExpect(this.person3.isOlder(person1), true)
        && t.checkExpect(this.person2.isOlder(person3), false)
        && t.checkExpect(this.person2.isOlder(person1), false);
  }
  
  //test the method sameNamePet in the class Person
  boolean testSameNamePet(Tester t) {
    return t.checkExpect(this.person1.sameNamePet("Luna"), true)
        && t.checkExpect(this.person1.sameNamePet("Maggie"), false)
        && t.checkExpect(this.person2.sameNamePet("Daisy"), false);
  }
   
  //test the method perish in the class Person
  boolean perish(Tester t) {
    return t.checkExpect(this.person1.perish(), new Person("Som", nopet, 18))
        && t.checkExpect(this.person2.perish(), new Person("Neal", nopet, 18))
        && t.checkExpect(this.person3.perish(), new Person("Ryan", nopet, 19));
  }
}