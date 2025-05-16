// to represent an address
class Address {
    String city;
    String state;

    Address(String city, String state) {
        this.city = city;
        this.state = state;
    }
}

// to represent examples and tests for addresses
class ExamplesAddress {
    Address address1 = new Address("Boston", "MA");
    Address address2 = new Address("Warwich", "RI");
    Address address3 = new Address("Nashua", "NH");
}