// to represent a person
class Person {
    String name;
    int age;
    String gender;
    Address address;

    Person(String name, int age, String gender, Address address) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.address = address;
    }
}

// to represent examples and tests for people
class ExamplesPerson {
    Person tim = new Person("Tim", 23, "Male", new Address("Boston", "MA"));
    Person kate = new Person("Kate", 22, "Female", new Address("Warwich", "RI"));
    Person rebecca = new Person("Rebecca", 31, "Non-binary", new Address("Nashua", "NH"));
    Person som = new Person("Som", 18, "Male", new Address("East Brunswick", "NJ"));
    Person ryan = new Person("Ryan", 19, "Male", new Address("East Brunswick", "NJ"));
}