import tester.*;

// interface for entertainment
interface IEntertainment {
  
  /*
   * TEMPLATE: 
   * Fields: 
   * 
   * Methods: 
   * ... this.totalPrice() ...                               -- double
   * ... this.duration() ...                                 -- int
   * ... this.format() ....                                  -- String
   * ... this.sameEntertainment(IEntertainment that) ...     -- boolean
   * ... this.sameMagazine(Magazine that) ...                -- boolean
   * ... this.sameTvSeries(TVSeries that) ...                -- boolean
   * ... this.samePodcast(Podcast that) ...                  -- boolean
   * 
   * Methods on fields:
  */
  
  // compute the total price of this Entertainment
  double totalPrice();

  // computes the minutes of entertainment of this IEntertainment
  int duration();

  // produce a String that shows the name and price of this IEntertainment
  String format();

  // is this IEntertainment the same as that one?
  boolean sameEntertainment(IEntertainment that);

  // is this Magazine the same as that one?
  boolean sameMagazine(Magazine that);

  // is this TV Series the same as that one?
  boolean sameTvSeries(TVSeries that);

  // is this Podcast the same as that one?
  boolean samePodcast(Podcast that);
}

// represents an abstract class for entertainment
abstract class AEntertainment implements IEntertainment {
  String name;
  double price;
  int installments;

  AEntertainment(String name, double price, int installments) {
    this.name = name;
    this.price = price;
    this.installments = installments;
  }
  
  /*
   * TEMPLATE: 
   * Fields: 
   * ... this.name ...                                       -- String
   * ... this.price ...                                      -- double
   * ... this.intallments ...                                -- int
   * 
   * Methods: 
   * ... this.totalPrice() ...                               -- double
   * ... this.duration() ...                                 -- int
   * ... this.format() ....                                  -- String
   * ... this.sameEntertainment(IEntertainment that) ...     -- boolean
   * ... this.sameMagazine(Magazine that) ...                -- boolean
   * ... this.sameTvSeries(TVSeries that) ...                -- boolean
   * ... this.samePodcast(Podcast that) ...                  -- boolean
   * 
   * Methods on fields:
  */

  // compute the total price of this Entertainment
  public double totalPrice() {
    return this.price * this.installments;
  }

  // compute the duration of the entertainment
  public int duration() {
    return this.installments * 50;
  }

  // produce a String that shows the name and price of this IEntertainment
  public String format() {
    return this.name + ", " + this.price + ".";
  }

  // is this IEntertainment the same as that one?
  public abstract boolean sameEntertainment(IEntertainment that);

  // is this Magazine the same as that one?
  public abstract boolean sameMagazine(Magazine that);

  // is this TV Series the same as that one?
  public abstract boolean sameTvSeries(TVSeries that);

  // is this Podcast the same as that one?
  public abstract boolean samePodcast(Podcast that);
}

// represents a magazine
class Magazine extends AEntertainment {
  String genre;
  int pages;

  Magazine(String name, double price, String genre, int pages, int installments) {
    super(name, price, installments);
    this.genre = genre;
    this.pages = pages;
  }
  
  /*
   * TEMPLATE: 
   * Fields: 
   * ... this.name ...                                       -- String
   * ... this.price ...                                      -- double
   * ... this.intallments ...                                -- int
   * ... this.genre ...                                      -- String
   * ... this.pages ...                                      -- int 
   * 
   * Methods: 
   * ... this.totalPrice() ...                               -- double
   * ... this.duration() ...                                 -- int
   * ... this.format() ....                                  -- String
   * ... this.sameEntertainment(IEntertainment that) ...     -- boolean
   * ... this.sameMagazine(Magazine that) ...                -- boolean
   * ... this.sameTvSeries(TVSeries that) ...                -- boolean
   * ... this.samePodcast(Podcast that) ...                  -- boolean
   * 
   * Methods on fields:
  */

  // computes the minutes of entertainment of this Magazine, (includes all
  // installments)
  public int duration() {
    return this.pages * this.installments * 5;
  }

  // is this Magazine the same as that IEntertainment?
  public boolean sameEntertainment(IEntertainment that) {
    /*
     * METHOD TEMPLATE:
     * Same as class template...
     * 
     * Fields on parameters: 
     * ... this.name ...                                       -- String
     * ... this.price ...                                      -- double
     * ... this.intallments ...                                -- int
     * ... this.genre ...                                      -- String
     * ... this.pages ...                                      -- int 
     * 
     * Methods on parameters:
     * ... this.totalPrice() ...                               -- double
     * ... this.duration() ...                                 -- int
     * ... this.format() ....                                  -- String
     * ... this.sameEntertainment(IEntertainment that) ...     -- boolean
     * ... this.sameMagazine(Magazine that) ...                -- boolean
     * ... this.sameTvSeries(TVSeries that) ...                -- boolean
     * ... this.samePodcast(Podcast that) ...                  -- boolean
    */
    return that.sameMagazine(this);
  }

  // is this Magazine the same as that one?
  public boolean sameMagazine(Magazine that) {
    /*
     * METHOD TEMPLATE:
     * Same as class template...
     * 
     * Fields on parameters: 
     * ... this.name ...                                       -- String
     * ... this.price ...                                      -- double
     * ... this.intallments ...                                -- int
     * ... this.genre ...                                      -- String
     * ... this.pages ...                                      -- int 
     * 
     * Methods on parameters:
     * ... this.totalPrice() ...                               -- double
     * ... this.duration() ...                                 -- int
     * ... this.format() ....                                  -- String
     * ... this.sameEntertainment(IEntertainment that) ...     -- boolean
     * ... this.sameMagazine(Magazine that) ...                -- boolean
     * ... this.sameTvSeries(TVSeries that) ...                -- boolean
     * ... this.samePodcast(Podcast that) ...                  -- boolean
    */
    return this.name.equals(that.name) && Math.abs(this.price - that.price) < 0.001
        && this.genre.equals(that.genre) && Math.abs(this.pages - that.pages) < 0.001
        && Math.abs(this.installments - that.installments) < 0.001;
  }

  // is this TV Series the same as that one?
  public boolean sameTvSeries(TVSeries that) {
    return false;
  }

  // is this Podcast the same as that one?
  public boolean samePodcast(Podcast that) {
    return false;
  }
}

// represents a tv series
class TVSeries extends AEntertainment {
  String corporation;

  TVSeries(String name, double price, int installments, String corporation) {
    super(name, price, installments);
    this.corporation = corporation;
  }
  
  /*
   * TEMPLATE: 
   * Fields: 
   * ... this.name ...                                       -- String
   * ... this.price ...                                      -- double
   * ... this.intallments ...                                -- int
   * ... this.corporation ...                                -- String 
   * 
   * Methods: 
   * ... this.totalPrice() ...                               -- double
   * ... this.duration() ...                                 -- int
   * ... this.format() ....                                  -- String
   * ... this.sameEntertainment(IEntertainment that) ...     -- boolean
   * ... this.sameMagazine(Magazine that) ...                -- boolean
   * ... this.sameTvSeries(TVSeries that) ...                -- boolean
   * ... this.samePodcast(Podcast that) ...                  -- boolean
   * 
   * Methods on fields:
  */

  // is this TVSeries the same as that IEntertainment?
  public boolean sameEntertainment(IEntertainment that) {
    /*
     * METHOD TEMPLATE:
     * Same as class template...
     * 
     * Fields on parameters: 
     * ... this.name ...                                       -- String
     * ... this.price ...                                      -- double
     * ... this.intallments ...                                -- int
     * ... this.genre ...                                      -- String
     * ... this.pages ...                                      -- int 
     * 
     * Methods on parameters:
     * ... this.totalPrice() ...                               -- double
     * ... this.duration() ...                                 -- int
     * ... this.format() ....                                  -- String
     * ... this.sameEntertainment(IEntertainment that) ...     -- boolean
     * ... this.sameMagazine(Magazine that) ...                -- boolean
     * ... this.sameTvSeries(TVSeries that) ...                -- boolean
     * ... this.samePodcast(Podcast that) ...                  -- boolean
    */
    return that.sameTvSeries(this);
  }

  // is this Magazine the same as that one?
  public boolean sameMagazine(Magazine that) {
    return false;
  }

  // is this TV Series the same as that one?
  public boolean sameTvSeries(TVSeries that) {
    /*
     * METHOD TEMPLATE:
     * Same as class template...
     * 
     * Fields on parameters: 
     * ... this.name ...                                       -- String
     * ... this.price ...                                      -- double
     * ... this.intallments ...                                -- int
     * ... this.genre ...                                      -- String
     * ... this.pages ...                                      -- int 
     * 
     * Methods on parameters:
     * ... this.totalPrice() ...                               -- double
     * ... this.duration() ...                                 -- int
     * ... this.format() ....                                  -- String
     * ... this.sameEntertainment(IEntertainment that) ...     -- boolean
     * ... this.sameMagazine(Magazine that) ...                -- boolean
     * ... this.sameTvSeries(TVSeries that) ...                -- boolean
     * ... this.samePodcast(Podcast that) ...                  -- boolean
    */
    return this.name.equals(that.name) && Math.abs(this.price - that.price) < 0.001
        && Math.abs(this.installments - that.installments) < 0.001
        && this.corporation.equals(that.corporation);
  }

  // is this Podcast the same as that one?
  public boolean samePodcast(Podcast that) {
    return false;
  }
}

// represents a podcast
class Podcast extends AEntertainment {

  Podcast(String name, double price, int installments) {
    super(name, price, installments);
  }
  
  /*
   * TEMPLATE: 
   * Fields: 
   * ... this.name ...                                       -- String
   * ... this.price ...                                      -- double
   * ... this.intallments ...                                -- int
   * 
   * Methods: 
   * ... this.totalPrice() ...                               -- double
   * ... this.duration() ...                                 -- int
   * ... this.format() ....                                  -- String
   * ... this.sameEntertainment(IEntertainment that) ...     -- boolean
   * ... this.sameMagazine(Magazine that) ...                -- boolean
   * ... this.sameTvSeries(TVSeries that) ...                -- boolean
   * ... this.samePodcast(Podcast that) ...                  -- boolean
   * 
   * Methods on fields:
  */

  // is this Podcast the same as that IEntertainment?
  public boolean sameEntertainment(IEntertainment that) {
    /*
     * METHOD TEMPLATE:
     * Same as class template...
     * 
     * Fields on parameters: 
     * ... this.name ...                                       -- String
     * ... this.price ...                                      -- double
     * ... this.intallments ...                                -- int
     * ... this.genre ...                                      -- String
     * ... this.pages ...                                      -- int 
     * 
     * Methods on parameters:
     * ... this.totalPrice() ...                               -- double
     * ... this.duration() ...                                 -- int
     * ... this.format() ....                                  -- String
     * ... this.sameEntertainment(IEntertainment that) ...     -- boolean
     * ... this.sameMagazine(Magazine that) ...                -- boolean
     * ... this.sameTvSeries(TVSeries that) ...                -- boolean
     * ... this.samePodcast(Podcast that) ...                  -- boolean
    */
    return that.samePodcast(this);
  }

  // is this Magazine the same as that one?
  public boolean sameMagazine(Magazine that) {
    return false;
  }

  // is this TV Series the same as that one?
  public boolean sameTvSeries(TVSeries that) {
    return false;
  }

  // is this Podcast the same as that one?
  public boolean samePodcast(Podcast that) {
    /*
     * METHOD TEMPLATE:
     * Same as class template...
     * 
     * Fields on parameters: 
     * ... this.name ...                                       -- String
     * ... this.price ...                                      -- double
     * ... this.intallments ...                                -- int
     * ... this.genre ...                                      -- String
     * ... this.pages ...                                      -- int 
     * 
     * Methods on parameters:
     * ... this.totalPrice() ...                               -- double
     * ... this.duration() ...                                 -- int
     * ... this.format() ....                                  -- String
     * ... this.sameEntertainment(IEntertainment that) ...     -- boolean
     * ... this.sameMagazine(Magazine that) ...                -- boolean
     * ... this.sameTvSeries(TVSeries that) ...                -- boolean
     * ... this.samePodcast(Podcast that) ...                  -- boolean
    */
    return this.name.equals(that.name) && Math.abs(this.price - that.price) < 0.001
        && Math.abs(this.installments - that.installments) < 0.001;
  }
}

// examples and tests for the entertainment class
class ExamplesEntertainment {
  IEntertainment rollingStone = new Magazine("Rolling Stone", 2.55, "Music", 60, 12);
  IEntertainment somMagazine = new Magazine("Som's Magazine", 8.75, "Sports", 30, 7);
  IEntertainment houseOfCards = new TVSeries("House of Cards", 5.25, 13, "Netflix");
  IEntertainment somTvSeries = new TVSeries("Suits", 3.65, 24, "Netflix");
  IEntertainment serial = new Podcast("Serial", 0.0, 8);
  IEntertainment somPodcast = new Podcast("Joe Rogan", 6.15, 12);

  Magazine rollingStone2 = new Magazine("Rolling Stone", 2.55, "Music", 60, 12);
  Magazine somMagazine2 = new Magazine("Som's Magazine", 8.75, "Sports", 30, 7);
  TVSeries houseOfCards2 = new TVSeries("House of Cards", 5.25, 13, "Netflix");
  TVSeries somTvSeries2 = new TVSeries("Suits", 3.65, 24, "Netflix");
  Podcast serial2 = new Podcast("Serial", 0.0, 8);
  Podcast somPodcast2 = new Podcast("Joe Rogan", 6.15, 12);

  // testing total price method
  boolean testTotalPrice(Tester t) {
    return t.checkInexact(this.rollingStone.totalPrice(), 2.55 * 12, .0001)
        // tests on a magazine
        && t.checkInexact(this.somMagazine.totalPrice(), 8.75 * 7, .0001)
        // tests on another magazine
        && t.checkInexact(this.houseOfCards.totalPrice(), 5.25 * 13, .0001)
        // tests on a tv series
        && t.checkInexact(this.somTvSeries.totalPrice(), 3.65 * 24, .0001)
        // tests on another tv series
        && t.checkInexact(this.serial.totalPrice(), 0.0, .0001)
        // tests on a podcast
        && t.checkInexact(this.somPodcast.totalPrice(), 6.15 * 12, .0001);
    // tests on another podcast
  }

  // testing duration method
  boolean testDuration(Tester t) {
    return t.checkExpect(this.rollingStone.duration(), 3600)
        // tests on a magazine
        && t.checkExpect(this.somMagazine.duration(), 1050)
        // tests on another magazine
        && t.checkExpect(this.houseOfCards.duration(), 650)
        // tests on a tv series
        && t.checkExpect(this.somTvSeries.duration(), 1200)
        // tests on another tv series
        && t.checkExpect(this.serial.duration(), 400)
        // tests on a podcast
        && t.checkExpect(this.somPodcast.duration(), 600);
    // test on another podcast
  }

  // testing format method
  boolean testFormat(Tester t) {
    return t.checkExpect(this.rollingStone.format(), "Rolling Stone, 2.55.")
        // tests on a magazine
        && t.checkExpect(this.somMagazine.format(), "Som's Magazine, 8.75.")
        // tests on another magazine
        && t.checkExpect(this.houseOfCards.format(), "House of Cards, 5.25.")
        // tests on a tv series
        && t.checkExpect(this.somTvSeries.format(), "Suits, 3.65.")
        // tests on another tv series
        && t.checkExpect(this.serial.format(), "Serial, 0.0.")
        // tests on a podcast
        && t.checkExpect(this.somPodcast.format(), "Joe Rogan, 6.15.");
    // tests on another podcast
  }

  // testing sameEntertainment method
  boolean testSameEntertainment(Tester t) {
    return t.checkExpect(this.rollingStone.sameEntertainment(somMagazine), false)
        // tests on a magazine
        && t.checkExpect(this.rollingStone.sameEntertainment(rollingStone), true)
        // tests on another magazine
        && t.checkExpect(this.houseOfCards.sameEntertainment(somTvSeries), false)
        // tests on a tv series
        && t.checkExpect(this.houseOfCards.sameEntertainment(houseOfCards), true)
        // tests on another tv series
        && t.checkExpect(this.serial.sameEntertainment(somPodcast), false)
        // tests on a podcast
        && t.checkExpect(this.serial.sameEntertainment(serial), true);
    // tests on another podcast
  }

  // testing sameMagazine method
  boolean testSameMagazine(Tester t) {
    return t.checkExpect(this.rollingStone2.sameMagazine(somMagazine2), false)
        // tests on a magazine
        && t.checkExpect(this.rollingStone2.sameMagazine(rollingStone2), true)
        // tests on another magazine
        && t.checkExpect(this.houseOfCards2.sameMagazine(somMagazine2), false)
        // tests on a tv series
        && t.checkExpect(this.houseOfCards2.sameMagazine(rollingStone2), false)
        // tests on another tv series
        && t.checkExpect(this.serial2.sameMagazine(somMagazine2), false)
        // tests on a podcast
        && t.checkExpect(this.serial2.sameMagazine(rollingStone2), false);
    // tests on another podcast
  }

  // testing sameTvSeries method
  boolean testSameTvSeries(Tester t) {
    return t.checkExpect(this.rollingStone2.sameTvSeries(somTvSeries2), false)
        // tests on a magazine
        && t.checkExpect(this.rollingStone2.sameTvSeries(houseOfCards2), false)
        // tests on another magazine
        && t.checkExpect(this.houseOfCards2.sameTvSeries(somTvSeries2), false)
        // tests on a tv series
        && t.checkExpect(this.houseOfCards2.sameTvSeries(houseOfCards2), true)
        // tests on another tv series
        && t.checkExpect(this.serial2.sameTvSeries(somTvSeries2), false)
        // tests on a podcast
        && t.checkExpect(this.serial2.sameTvSeries(houseOfCards2), false);
    // tests on another podcast
  }

  // testing samePodcast method
  boolean testSamePodcast(Tester t) {

    return t.checkExpect(this.rollingStone2.samePodcast(somPodcast2), false)
        // tests on a magazine
        && t.checkExpect(this.rollingStone2.samePodcast(serial2), false)
        // tests on another magazine
        && t.checkExpect(this.houseOfCards2.samePodcast(somPodcast2), false)
        // tests on a tv series
        && t.checkExpect(this.houseOfCards2.samePodcast(serial2), false)
        // tests on another tv series
        && t.checkExpect(this.serial2.samePodcast(somPodcast2), false)
        // tests on a podcast
        && t.checkExpect(this.serial2.samePodcast(serial2), true);
    // tests on another podcast
  }
}