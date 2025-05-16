import tester.*;

// Interface representation of an item on a web page
interface IItem {

  // returns the total image size
  int totalImageSize();

  // returns the text length
  int textLength();

  // returns the images
  String images();
}

// Representation of a Text item
class Text implements IItem {
  String contents;

  Text(String contents) {
    this.contents = contents;
  }

  /*
   * TEMPLATE: 
   * Fields: 
   * ... this.contents ...            -- String
   * 
   * Methods: 
   * ... this.totalImageSize() ...    -- int 
   * ... this.textLength() ...        -- int 
   * ... this.images() ...            -- String
   */

  // returns the total image size
  public int totalImageSize() {
    return 0;
  }

  // returns the text length
  public int textLength() {
    return this.contents.length(); 
  }

  // returns the images
  public String images() {
    return ""; 
  }
}

// Representation of an Image item
class Image implements IItem {
  String fileName;
  int size;
  String fileType;

  Image(String fileName, int size, String fileType) {
    this.fileName = fileName;
    this.size = size;
    this.fileType = fileType;
  }

  /*
   * TEMPLATE: 
   * Fields: 
   * ... this.fileName ...         -- String 
   * ... this.size ...             -- int
   * ... this.fileType ...         -- String
   * 
   * Methods: 
   * ... this.totalImageSize() ... -- int 
   * ... this.textLength() ...     -- int 
   * ... this.images() ...         -- String
   */

  // returns the total image size
  public int totalImageSize() {
    return this.size;
  }

  // returns the text length
  public int textLength() {
    return this.fileName.length() + this.fileType.length();
  }

  // returns the images
  public String images() {
    return this.fileName + "." + this.fileType;
  }
}

// Representation of a Link item
class Link implements IItem {
  String name;
  WebPage page;

  Link(String name, WebPage page) {
    this.name = name;
    this.page = page;
  }

  /*
   * TEMPLATE: 
   * Fields: 
   * ... this.name ...              -- String 
   * ... this.page ...              -- WebPage
   * 
   * Methods: 
   * ... this.totalImageSize() ...  -- int 
   * ... this.textLength() ...      -- int 
   * ... this.images() ...          -- String
   */

  // returns the total image size
  public int totalImageSize() {
    return this.page.totalImageSize(); 
  }

  // returns the text length
  public int textLength() {
    return this.name.length() + this.page.textLength(); 
  }

  // returns the images
  public String images() {
    return this.page.images(); 
  }
}

// Interface for a list of items
interface ILoItem {

  // returns the total image size
  int totalImageSize();

  // returns the text length
  int textLength();

  // returns the images
  String images();
}

// Representation of an empty list of items
class MtLoItem implements ILoItem {
  MtLoItem() {
    // Empty list has no fields
  }

  /*
   * TEMPLATE:
   * Methods:
   * ... this.totalImageSize() ...  -- int
   * ... this.textLength() ...      -- int
   * ... this.images() ...          -- String
   */

  // returns the total image size
  public int totalImageSize() {
    return 0;
  }

  // returns the total text length
  public int textLength() {
    return 0;
  }

  // returns the images
  public String images() {
    return "";
  }
}

// Representation of a non-empty list of items
class ConsLoItem implements ILoItem {
  IItem first;
  ILoItem rest;

  ConsLoItem(IItem first, ILoItem rest) {
    this.first = first;
    this.rest = rest;
  }

  /*
   * TEMPLATE: 
   * Fields: 
   * ... this.first               ... -- Item
   * ... this.rest                ... -- ILoItem
   * 
   * Methods: 
   * ... this.totalImageSize()    ... -- int
   * ... this.textLength()        ... -- int
   * ... this.images()            ... -- String
   * 
   * Methods on Fields: 
   * ... this.first.totalImageSize() ... -- int
   * ... this.first.textLength()     ... -- int
   * ... this.first.images()         ... -- String
   * ... this.rest.totalImageSize()  ... -- int
   * ... this.rest.textLength()      ... -- int
   * ... this.rest.images()          ... -- String
   */

  // returns the total image size
  public int totalImageSize() {
    return this.first.totalImageSize() + this.rest.totalImageSize(); 
  }

  // returns the text length
  public int textLength() {
    return this.first.textLength() + this.rest.textLength(); 
  }

  // returns the images
  public String images() {
    String firstImages = this.first.images();
    String restImages = this.rest.images();
    if (firstImages.isEmpty()) {
      return restImages; 
    } else if (restImages.isEmpty()) {
      return firstImages;
    } else {
      return firstImages + ", " + restImages;
    }
  }
}

// Representation of a web page
class WebPage {
  String title;
  String url;
  ILoItem items;

  WebPage(String title, String url, ILoItem items) {
    this.title = title;
    this.url = url;
    this.items = items;
  }

  /*
   * TEMPLATE: 
   * Fields: 
   * ... this.title               ... -- String
   * ... this.url                 ... -- String
   * ... this.items               ... -- ILoItem
   * 
   * Methods: 
   * ... this.totalImageSize()    ... -- int
   * ... this.textLength()        ... -- int
   * ... this.images()            ... -- String
   * 
   * Methods on Fields: 
   * ... this.items.totalImageSize() ... -- int
   * ... this.items.textLength()     ... -- int
   * ... this.items.images()         ... -- String
   */

  // returns the total image size
  public int totalImageSize() {
    return this.items.totalImageSize(); 
  }

  // returns the text length
  public int textLength() {
    return this.title.length() + this.items.textLength(); 
  }

  // returns the images
  public String images() {
    return this.items.images();
  }
}

// Example data representation of the described webpages
class ExamplesWebPage {
  // WebPage: "HtDP"
  ILoItem htdpItems = new ConsLoItem(new Text("How to Design Programs"),
      new ConsLoItem(new Image("htdp", 4300, "tiff"), new MtLoItem()));

  WebPage htdpPage = new WebPage("HtDP", "htdp.org", htdpItems);

  // WebPage: "OOD"
  ILoItem oodItems = new ConsLoItem(new Text("Stay classy, Java"),
      new ConsLoItem(new Link("Back to the Future", htdpPage), new MtLoItem()));

  WebPage oodPage = new WebPage("OOD", "ccs.neu.edu/OOD", oodItems);

  // WebPage: "Fundies II"
  ILoItem fundiesItems = new ConsLoItem(new Text("Home sweet home"),
      new ConsLoItem(new Image("wvh-lab", 400, "png"),
          new ConsLoItem(new Text("The staff"),
              new ConsLoItem(new Image("profs", 240, "jpeg"),
                  new ConsLoItem(new Link("A Look Back", htdpPage),
                      new ConsLoItem(new Link("A Look Ahead", oodPage), new MtLoItem()))))));

  WebPage fundiesWP = new WebPage("Fundies II", "ccs.neu.edu/Fundies2", fundiesItems);

  // Test the method totalImageSize in the class WebPage
  boolean testTotalImageSize(Tester t) {
    return t.checkExpect(new WebPage("Empty", "empty.org", new MtLoItem()).totalImageSize(), 0)
        && t.checkExpect(htdpPage.totalImageSize(), 4300)
        && t.checkExpect(oodPage.totalImageSize(), 4300)
        && t.checkExpect(fundiesWP.totalImageSize(), 9240);
  }

  // Test the method textLength in the class WebPage
  boolean testTextLength(Tester t) {
    return t.checkExpect(new WebPage("Empty", "empty.org", new MtLoItem()).textLength(), 5)
        && t.checkExpect(htdpPage.textLength(), 34) 
        && t.checkExpect(oodPage.textLength(), 72) 
        && t.checkExpect(fundiesWP.textLength(), 182);
  }

  // Test the method images in the class WebPage
  boolean testImages(Tester t) {
    return t.checkExpect(new WebPage("Empty", "empty.org", new MtLoItem()).images(), "")
        && t.checkExpect(htdpPage.images(), "htdp.tiff")
        && t.checkExpect(oodPage.images(), "htdp.tiff")
        && t.checkExpect(fundiesWP.images(), "wvh-lab.png, profs.jpeg, htdp.tiff, htdp.tiff");
  }
}


/*
 * Explanation of duplication: The image "htdp.tiff" appears twice in the
 * results of the images method because the web page "HtDP" is linked both
 * directly and indirectly (via "OOD") from the "Fundies II" web page. Since the
 * images method recursively collects all image data across linked pages, the
 * same image is included multiple times when those pages are revisited through
 * different links.
 * 
 * Other places of duplication: This duplication issue could occur anywhere in
 * the code where linked pages are traversed recursively, particularly in
 * methods like totalImageSize and textLength if they similarly revisit the same
 * linked pages.
 */