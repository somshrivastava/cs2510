// to represent an event
class Event {
    String title;
    String date;
    String location;
    Person host;

    Event(String title, String date, String location, Person host) {
        this.title = title;
        this.date = date;
        this.location = location;
        this.host = host;
    }
}

// to represent examples and tests for events
class ExamplesEvent {
    Event event1 = new Event("Thanksgiving Dinner", "November 27th, 2024", "6 Silvester Ct.", new Person("Som", 18, "Male", new Address("East Brunswick", "NJ")));
    Event event2 = new Event("Christmas Dinner", "December 25th, 2024", "10 Johnny Rd.", new Person("Ryan", 19, "Male", new Address("East Brunswick", "NJ")));
}