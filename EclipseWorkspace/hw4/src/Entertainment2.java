import tester.*;

interface IEntertainment2 {
  // compute the total price of this Entertainment
  double totalPrice();

  // computes the minutes of entertainment of this IEntertainment
  int duration();

  // produce a String that shows the name and price of this IEntertainment
  String format();

  // is this IEntertainment the same as that one?
  boolean sameEntertainment(IEntertainment2 that);

  // is this Magazine the same as that one?
  boolean sameMagazine(Magazine2 that);

  // is this TV Series the same as that one?
  boolean sameTvSeries(TVSeries2 that);

  // is this Podcast the same as that one?
  boolean samePodcast(Podcast2 that);

}

class Magazine2 implements IEntertainment2 {
  String name;
  double price; // represents price per issue
  String genre;
  int pages;
  int installments; // number of issues per year

  Magazine2(String name, double price, String genre, int pages, int installments) {
    this.name = name;
    this.price = price;
    this.genre = genre;
    this.pages = pages;
    this.installments = installments;
  }

  // computes the price of a yearly subscription to this Magazine
  public double totalPrice() {
    return this.price * this.installments;
  }

  // computes the minutes of entertainment of this Magazine, (includes all
  // installments)
  public int duration() {
    return this.pages * 5;
  }

  // is this Magazine the same as that IEntertainment?
  public boolean sameEntertainment(IEntertainment2 that) {
    return that.sameMagazine(this);
  }

  // is this Magazine the same as that one?
  public boolean sameMagazine(Magazine2 that) {
    return this.name.equals(that.name) && this.price == that.price && this.genre.equals(that.genre)
        && this.pages == that.pages && this.installments == that.installments;
  }

  // is this TV Series the same as that one?
  public boolean sameTvSeries(TVSeries2 that) {
    return false;
  }

  // is this Podcast the same as that one?
  public boolean samePodcast(Podcast2 that) {
    return false;
  }

  // produce a String that shows the name and price of this Magazine
  public String format() {
    return this.name + ", " + this.price + ".";
  }
}

class TVSeries2 implements IEntertainment2 {
  String name;
  double price; // represents price per episode
  int installments; // number of episodes of this series
  String corporation;

  TVSeries2(String name, double price, int installments, String corporation) {
    this.name = name;
    this.price = price;
    this.installments = installments;
    this.corporation = corporation;
  }

  // computes the price of a yearly subscription to this TVSeries
  public double totalPrice() {
    return this.price * this.installments;
  }

  // computes the minutes of entertainment of this TVSeries
  public int duration() {
    return this.installments * 50;
  }

  // is this TVSeries the same as that IEntertainment?
  public boolean sameEntertainment(IEntertainment2 that) {
    return that.sameTvSeries(this);
  }

  // is this Magazine the same as that one?
  public boolean sameMagazine(Magazine2 that) {
    return false;
  }

  // is this TV Series the same as that one?
  public boolean sameTvSeries(TVSeries2 that) {
    return this.name.equals(that.name) && this.price == that.price
        && this.installments == that.installments && this.corporation.equals(that.corporation);
  }

  // is this Podcast the same as that one?
  public boolean samePodcast(Podcast2 that) {
    return false;
  }

  // produce a String that shows the name and price of this TVSeries
  public String format() {
    return this.name + ", " + this.price + ".";
  }
}

class Podcast2 implements IEntertainment2 {
  String name;
  double price; // represents price per issue
  int installments; // number of episodes in this Podcast

  Podcast2(String name, double price, int installments) {
    this.name = name;
    this.price = price;
    this.installments = installments;
  }

  // computes the price of a yearly subscription to this Podcast
  public double totalPrice() {
    return this.price * this.installments;
  }

  // computes the minutes of entertainment of this Podcast
  public int duration() {
    return this.installments * 50;
  }

  // is this Podcast the same as that IEntertainment?
  public boolean sameEntertainment(IEntertainment2 that) {
    return that.samePodcast(this);
  }

  // is this Magazine the same as that one?
  public boolean sameMagazine(Magazine2 that) {
    return false;
  }

  // is this TV Series the same as that one?
  public boolean sameTvSeries(TVSeries2 that) {
    return false;
  }

  // is this Podcast the same as that one?
  public boolean samePodcast(Podcast2 that) {
    return this.name.equals(that.name) && this.price == that.price
        && this.installments == that.installments;
  }

  // produce a String that shows the name and price of this Podcast
  public String format() {
    return this.name + ", " + this.price + ".";
  }
}

class ExamplesEntertainment2 {
  IEntertainment2 rollingStone = new Magazine2("Rolling Stone", 2.55, "Music", 60, 12);
  IEntertainment2 somMagazine = new Magazine2("Som's Magazine", 8.75, "Sports", 30, 7);
  IEntertainment2 houseOfCards = new TVSeries2("House of Cards", 5.25, 13, "Netflix");
  IEntertainment2 somTvSeries = new TVSeries2("Suits", 3.65, 24, "Netflix");
  IEntertainment2 serial = new Podcast2("Serial", 0.0, 8);
  IEntertainment2 somPodcast = new Podcast2("Joe Rogan", 6.15, 12);

  // testing total price method
  boolean testTotalPrice(Tester t) {
    return t.checkInexact(this.rollingStone.totalPrice(), 2.55 * 12, .0001)
        && t.checkInexact(this.somMagazine.totalPrice(), 8.75 * 7, .0001)
        && t.checkInexact(this.houseOfCards.totalPrice(), 5.25 * 13, .0001)
        && t.checkInexact(this.somTvSeries.totalPrice(), 3.65 * 24, .0001)
        && t.checkInexact(this.serial.totalPrice(), 0.0, .0001)
        && t.checkInexact(this.somPodcast.totalPrice(), 6.15 * 12, .0001);
  }

  // testing duration method
  boolean testDuration(Tester t) {
    return t.checkExpect(this.rollingStone.duration(), 300)
        && t.checkExpect(this.somMagazine.duration(), 150)
        && t.checkExpect(this.houseOfCards.duration(), 650)
        && t.checkExpect(this.somTvSeries.duration(), 1200)
        && t.checkExpect(this.serial.duration(), 400)
        && t.checkExpect(this.somPodcast.duration(), 600);
  }

  // testing format method
  boolean testFormat(Tester t) {
    return t.checkExpect(this.rollingStone.format(), "Rolling Stone, 2.55.")
        && t.checkExpect(this.somMagazine.format(), "Som's Magazine, 8.75.")
        && t.checkExpect(this.houseOfCards.format(), "House of Cards, 5.25.")
        && t.checkExpect(this.somTvSeries.format(), "Suits, 3.65.")
        && t.checkExpect(this.serial.format(), "Serial, 0.0.")
        && t.checkExpect(this.somPodcast.format(), "Joe Rogan, 6.15.");
  }
  
  // testing sameEntertainment method
  boolean testSameEntertainment(Tester t) {
    return t.checkExpect(this.rollingStone.sameEntertainment(somMagazine), false)
        && t.checkExpect(this.rollingStone.sameEntertainment(rollingStone), true)
        && t.checkExpect(this.houseOfCards.sameEntertainment(somTvSeries), false)
        && t.checkExpect(this.houseOfCards.sameEntertainment(houseOfCards), true)
        && t.checkExpect(this.serial.sameEntertainment(somPodcast), false)
        && t.checkExpect(this.serial.sameEntertainment(serial), true);
  }
}