import java.util.Random;
import java.awt.Color;
import javalib.funworld.World;
import javalib.funworld.WorldScene;
import javalib.worldimages.RectangleImage;
import javalib.worldimages.TextImage;
import javalib.worldimages.WorldEnd;
import tester.Tester;



/*

 ADDED EXTRA CREDIT:

 ... variable word lengths ...
 ... score ...

 */



//represents all the game constants
interface IUtils {
  int GAME_HEIGHT = 600;
  int GAME_WIDTH = 400;
  int SPAWN_HEIGHT = 20;
  double TICK_RATE = 0.04;
  Color GAME_BG = Color.BLACK;
  Color ACTIVE_COLOR = Color.MAGENTA;
  Color INACTIVE_COLOR = Color.WHITE;
  String ALPHABET = "abcdefghijklmnopqrstuvwxyz";
  WorldScene BLANK_WS = 
      new WorldScene(GAME_WIDTH, GAME_HEIGHT)
      .placeImageXY(new RectangleImage(GAME_WIDTH, GAME_HEIGHT, "solid", GAME_BG), 200, 300); 
  WorldScene END_WS = 
      new WorldScene(GAME_WIDTH, GAME_HEIGHT)
      .placeImageXY(new TextImage("GAME OVER", 50, Color.RED), 200, 300);


}

// represents a list of words
interface ILoWord {

  // takes in a String of length 1 and produces an ILoWord where 
  // any active words in this ILoWord are reduced by removing the first 
  // letter only if the given string matches the first letter
  ILoWord checkAndReduce(String firstLetter); 

  // produces an ILoWord with any IWords that have an empty string are filtered out
  ILoWord filterOutEmpties();

  // takes in a WorldScene and draws all of the words in an ILoWord onto the given WorldScene
  WorldScene draw(WorldScene given);

  // takes in an IWord and produces an ILoWord that is like 
  // this ILoWord but with the given IWord added at the end
  ILoWord addToEnd(IWord given);

  // shifts an entire list of words down in a WorldScene
  ILoWord shiftDown(int dist);

  // checks if any words have reached the bottom on the page. 
  boolean reachedBottom();

  //checks if there are any active words
  boolean anyActive();

  // checks for a new active word in a list, or keeps the current one. 
  ILoWord findNewActive(String key);
} 


//represents a word in the ZType game
interface IWord {

  // removes the first letter of an ActiveWord if it matches the given letter
  IWord removeFirst(String firstLetter);

  // writes this word onto the given WorldScene
  WorldScene wordToText(WorldScene given);

  // helper method to check if the am IWord is empty
  boolean checkEmpty();

  // shifts a word down by a certain amount.
  IWord shiftDownHelp(int dist);

  // checks if the first letter is equal, and converts to an active word 
  IWord makeActiveHelp(String key);

  // checks if a word is active
  boolean isActive();

  // checks if a word has reached the bottom of the page
  boolean reachedEnd();
}


// represents a world class to animate a list of words on a scene
class ZTypeWorld extends World {
  Random rand;
  int timer;
  ILoWord words;
  int score;

  // for implementation
  ZTypeWorld() {
    this.rand = new Random();
    this.timer = 1;
    this.words = new Utils(rand).genList(1, 1);
    this.score = 1;
  } 

  // for testing words
  ZTypeWorld(Random rand) {
    this.rand = rand;
    this.timer = 1;
    this.words = new Utils(rand).genList(1, 1);
    this.score = 1;
  }

  // for testing everything
  ZTypeWorld(Random rand, int timer, ILoWord words, int score) {
    this.rand = rand;
    this.timer = timer;
    this.words = words;
    this.score = score;
  }

  /* CLASS TEMPLATE: 
   * Fields: 
   * ... this.rand ...   -- Random
   * ... this.timer ...  -- int
   * ... this.words ...  -- ILoWord
   * Methods: 
   * ... makeScene() ...                      -- WorldScene
   * ... genList(int numWords, int limit) ... -- ILoWord
   * Methods of Fields: 
   * ... this.words.checkAndReduce(String firstLetter) ... -- ILoWord
   * ... this.words.filterOutEmpties() ...                 -- ILoWord
   * ... this.words.addToEnd(IWord given) ...              -- ILoWord
   * ... this.words.draw(WorldScene given) ...             -- WorldScene
   * ... this.words.addToEnd(IWord given) ...              -- ILoWord
   * ... this.words.anyActive() ...                        -- Boolean
   * ... this.words.shiftDown(int dist) ...                -- ILoWord
   * ... this.words.updateActive(String key)               -- ILoWord
   * ... this.words.findNewActive(String key)              -- ILoWord
   * ... this.words.reachedBottom()                        -- Boolean
   */

  // draws a WorldScene
  public WorldScene makeScene() {
    return this.words.draw(IUtils.BLANK_WS);
  }

  // changes the world on each keypress
  public ZTypeWorld onKeyEvent(String key) {
    if (this.words.anyActive()) {
      return new ZTypeWorld(this.rand, this.timer, this.words.checkAndReduce(key), this.score + 1);
    }
    else {
      return new ZTypeWorld(this.rand, this.timer, this.words.findNewActive(key), this.score);
    }
  }


  // move the words on the scene
  // adds a new Word at a random location at every tick of the clock
  public ZTypeWorld onTick() {
    if (timer % 30 == 0) {
      return new ZTypeWorld(this.rand, this.timer + 1, 
          this.words.addToEnd(new Utils(this.rand).newRandomInactive())
          .shiftDown(1).filterOutEmpties(), this.score);
    }
    else {
      return new ZTypeWorld(this.rand, this.timer + 1, 
          this.words.shiftDown(1).filterOutEmpties(), this.score);
    } 
  }

  // Ends the world if any words reach the bottom. 
  public WorldEnd worldEnds() {
    if (this.words.reachedBottom()) {
      return new WorldEnd(true, IUtils.END_WS
          .placeImageXY(new TextImage("Score: " + Integer.toString(score), 
              25, Color.BLACK), 200, 400));
    }
    else {
      return new WorldEnd(false, this.makeScene());
    }
  }

}

// to generate random strings for words
class Utils {
  Random rand; 

  // constructor
  Utils(Random rand) {
    this.rand = rand;
  }

  /* CLASS TEMPLATE
   * Fields: 
   * ... this.rand ...           -- Random
   * ... this.randInt ...        -- int
   * Methods:  
   * ... randLetter() ...        -- String
   * ... randWord()   ...        -- String
   * ... newRandomInactive() ... -- InactiveWord
   * ... genList(int numWords, int limit) -- ILoWord
   */

  // returns a random letter from the alphabet
  public String randLetter() {
    int randInt = rand.nextInt(25);
    return IUtils.ALPHABET.substring(randInt, randInt + 1);
  }

  // creates a string of length 6 of random letters
  public String randWord(String letters) {
    int randInt = 4 + rand.nextInt(4);
    if (letters.length() >= randInt) {
      return letters;
    }
    else {
      return this.randWord(letters + this.randLetter());
    }
  }

  // returns a new random word
  public IWord newRandomInactive()  {
    return new InactiveWord(this.randWord(this.randLetter()), 
        this.rand, IUtils.SPAWN_HEIGHT); 
  } 

  // generates a list of random inactive words
  public ILoWord genList(int numWords, int limit) {
    if (numWords > limit) {
      return new MtLoWord();
    }
    else {
      return new ConsLoWord(this.newRandomInactive(), 
          this.genList(numWords + 1, limit));
    }
  }
}

//represents an empty list of words
class MtLoWord implements ILoWord {

  /* CLASS TEMPLATE: 
   * Methods: 
   * ... checkAndReduce(String firstLetter) ... -- ILoWord
   * ... filterOutEmpties() ...                 -- ILoWord
   * ... draw(WorldScene given) ...             -- WorldScene
   * ... addToEnd(IWord given) ...              -- ILoWord
   * ... anyActive() ...                        -- Boolean
   * ... shiftDown(int dist) ...                -- ILoWord
   * ... findNewActive(String key)              -- ILoWord
   * ... reachedBottom()                        -- Boolean
   */

  // shortens active words in a list if their first letter matches
  // the input letter
  public ILoWord checkAndReduce(String firstLetter) {
    return this;
  }

  // filters empty strings out of an empty list
  public ILoWord filterOutEmpties() {
    return this;
  }


  // returns the given WorldScene with no more words to draw
  public WorldScene draw(WorldScene given) {
    return given;
  }

  // adds the given word to the end of an empty list
  public ILoWord addToEnd(IWord given) {
    /* TEMPLATE: 
     * Everything from class-wide template
     * Methods of Parameters: 
     * ... given.removeFirst(String firstLetter) -- IWord
     * ... given.checkEmpty()...                 -- Boolean
     * ... given.wordToText()...                 -- TextImage
     */
    return new ConsLoWord(given, new MtLoWord());
  }

  // shifts an entire cons list down by a specified distance 
  public ILoWord shiftDown(int dist) {
    return this;
  }

  // returns false, since there are no words
  public boolean reachedBottom() {
    return false;
  }

  // checks if there are any active words in an empty list.
  public boolean anyActive() {
    return false;
  }

  // return the empty list for the other case. 
  public ILoWord findNewActive(String key) {
    return this;
  }

}

//represents a non-empty list of words
class ConsLoWord implements ILoWord {
  IWord first;
  ILoWord rest;

  ConsLoWord(IWord first, ILoWord rest) {
    this.first = first;
    this.rest = rest;
  }

  /* CLASS TEMPLATE: 
   * Fields: 
   * ... this.first ... -- IWord
   * ... this.rest ...  -  ILoWord
   * Methods: 
   * ... checkAndReduce(String firstLetter) ... -- ILoWord
   * ... filterOutEmpties() ...                 -- ILoWord
   * ... draw(WorldScene given) ...             -- WorldScene
   * ... addToEnd(IWord given) ...              -- ILoWord
   * ... anyActive() ...                        -- Boolean
   * ... shiftDown(int dist) ...                -- ILoWord
   * ... findNewActive(String key)              -- ILoWord
   * ... reachedBottom()                        -- Boolean
   * Methods for Fields:
   * ... this.first.removeFirst(String firstLetter) -- IWord
   * ... this.first.checkEmpty()                    -- Boolean
   * ... this.first.wordToText()                    -- TextImage
   * ... this.first.shiftDownHelp(int dist)         -- IWord
   * ... this.first.makeActiveHelp(String key)      -- IWord
   * ... this.first.isActive() ...                  -- Boolean
   * ... this.rest.checkAndReduce(String firstLetter) ... -- ILoWord
   * ... this.rest.filterOutEmpties() ...                 -- ILoWord
   * ... this.rest.addToEnd(IWord given) ...              -- ILoWord
   * ... this.rest.draw(WorldScene given) ...             -- WorldScene
   * ... this.rest.addToEnd(IWord given) ...              -- ILoWord
   * ... this.rest.anyActive() ...                        -- Boolean
   * ... this.rest.shiftDown(int dist) ...                -- ILoWord
   * ... this.rest.findNewActive(String key)              -- ILoWord
   * ... this.rest.reachedBottom()                        -- Boolean
   */


  // shortens active words in a list if their first letter matches
  // the input letter
  public ILoWord checkAndReduce(String firstLetter) {
    return new ConsLoWord(this.first.removeFirst(firstLetter), 
        this.rest.checkAndReduce(firstLetter));
  }

  // filters empty words out of a list
  public ILoWord filterOutEmpties() {
    if (this.first.checkEmpty()) {
      return this.rest.filterOutEmpties();
    }
    else {
      return new ConsLoWord(this.first, this.rest.filterOutEmpties());
    }
  }

  // draws all of the words in a ILoWord on the given WorldScene
  public WorldScene draw(WorldScene given) {
    return this.rest.draw(this.first.wordToText(given));
  }

  // takes the given word and adds it to the end of the list
  public ILoWord addToEnd(IWord given) {
    /* TEMPLATE:
     * Methods for Parameters: 
     * ... given.removeFirst(String firstLetter) -- IWord
     * ... given.checkEmpty()                    -- Boolean
     * ... given.wordToText()                    -- TextImage
     */
    return new ConsLoWord(this.first, this.rest.addToEnd(given));
  }

  // checks if there exists an active word in the list.
  public boolean anyActive() {
    return this.first.isActive() || this.rest.anyActive();
  }

  // shifts an entire cons list down by a specified distance. 
  public ILoWord shiftDown(int dist) {
    return new ConsLoWord(this.first.shiftDownHelp(dist), this.rest.shiftDown(dist));
  }

  // given a word that doesn't have an active word, finds a new active word
  public ILoWord findNewActive(String key) {
    if (this.first.makeActiveHelp(key).isActive()) {
      return new ConsLoWord(this.first.makeActiveHelp(key).removeFirst(key), this.rest);
    }
    else {
      return new ConsLoWord(this.first, this.rest.findNewActive(key));
    }
  }

  // checks if any words in the list has reached the bottom of the scene
  public boolean reachedBottom() {
    return this.first.reachedEnd() || this.rest.reachedBottom();
  }

}


// represents ANY word. 
abstract class AWord implements IWord {
  String word;
  Random rand;
  int x;
  int y;

  AWord(String word, Random rand, int x, int y) {
    this.word = word;
    this.rand = rand;
    this.x = x;
    this.y = y;
  }

  /* ABSTRACT CLASS TEMPLATE: 
   * Fields: 
   * ... this.word ... -- String
   * ... this.rand ... -- Random
   * ... this.x ...    -- int
   * ... this.y ...    -- int
   * Methods: 
   * ... removeFirst(String firstLetter) -- IWord
   * ... checkLetter(String firstLetter) -- Boolean
   * ... checkEmpty()...                 -- Boolean
   * ... wordToText()...                 -- TextImage
   * ... shiftDownHelp(int dist)         -- IWord
   * ... makeActiveHelp(String key)      -- IWord
   * ... isActive() ...                  -- Boolean
   * ... reachedEnd() ...                -- Boolean
   */

  // removes the first letter of a matching word.
  public abstract IWord removeFirst(String firstLetter);

  // checks if the first letter is equal to a passed in word. 
  public boolean checkLetter(String firstLetter) {
    return firstLetter.equals(this.word.substring(0, 1));
  }

  // checks if a word is empty
  public boolean checkEmpty() {
    return this.word.isEmpty();
  }

  // returns a text image of a default inactive word drawn on the given WorldScene
  public WorldScene wordToText(WorldScene given) {
    return given.placeImageXY(
        new TextImage(this.word, 16, IUtils.INACTIVE_COLOR), this.x, this.y);
  }

  // abstract method to shift individual words down.
  public abstract IWord shiftDownHelp(int dist); 

  // abstract method to see if a word is active
  public abstract boolean isActive();

  // checks if a word has reached the bottom of the page screen
  public boolean reachedEnd() {
    return this.y >= IUtils.GAME_HEIGHT;
  }
}

// represents an active word in the ZType game
class ActiveWord extends AWord {
  ActiveWord(String word, Random rand, int x, int y) {
    super(word, rand, x, y);
  }

  /* CLASS TEMPLATE: 
   * Fields: 
   * ... this.word ... -- String
   * ... this.rand ... -- Random
   * ... this.x ...    -- int
   * ... this.y ...    -- int
   * Methods: 
   * ... removeFirst(String firstLetter) -- IWord
   * ... checkEmpty()...                 -- Boolean
   * ... wordToText()...                 -- TextImage
   * ... shiftDownHelp(int dist)         -- IWord
   * ... makeActiveHelp(String key)      -- IWord
   * ... isActive() ...                  -- Boolean
   */

  // returns a text image of an active word drawn on the given WorldScene
  public WorldScene wordToText(WorldScene given) {
    return given.placeImageXY(
        new TextImage(this.word, 16, IUtils.ACTIVE_COLOR), this.x, this.y);
  }

  // returns an active word with the first letter removed if it matches the given letter
  public IWord removeFirst(String firstLetter) {
    if (this.checkEmpty()) {
      return this; 
    }
    else if (this.checkLetter(firstLetter)) {
      return new ActiveWord(this.word.substring(1), this.rand, this.x, this.y);
    }
    return this;
  }

  // shifts an active word down by a given dist. 
  public IWord shiftDownHelp(int dist) {
    return new ActiveWord(this.word, this.rand, this.x, this.y + dist);
  }

  // makes an active word, active...so the same word. 
  public IWord makeActiveHelp(String key) {
    return this;
  }

  // checks if an active word is active, which is true.
  public boolean isActive() {
    return true;
  }

}

// represents an inactive word in the ZType game
class InactiveWord extends AWord {

  // word at a set spot
  InactiveWord(String word, Random rand, int x, int y) {
    super(word, rand, x, y);
  }

  // generates an inactive word at a random location
  InactiveWord(String word, Random rand, int y) {
    super(word, rand, 20 + rand.nextInt(IUtils.GAME_WIDTH - 40), IUtils.SPAWN_HEIGHT);
  }

  /* CLASS TEMPLATE: 
   * Fields: 
   * ... this.word ... -- String
   * ... this.rand ... -- Random
   * ... this.x ...    -- int
   * ... this.y ...    -- int
   * Methods: 
   * ... removeFirst(String firstLetter) -- IWord
   * ... checkEmpty()...                 -- Boolean
   * ... wordToText()...                 -- TextImage
   * ... shiftDownHelp(int dist)         -- IWord
   * ... makeActiveHelp(String key)      -- IWord
   * ... isActive() ...                  -- Boolean
   * ... reachedEnd() ...                -- Boolean
   */

  // returns the same word since the word is inactive
  // only active words are reduced
  public IWord removeFirst(String firstLetter) {
    return this;
  }

  //shifts an inactive word down by a variable dist.
  public IWord shiftDownHelp(int dist) {
    return new InactiveWord(this.word, this.rand, this.x, this.y + dist);
  }

  // if the letter matches, make an inactive word active. 
  public IWord makeActiveHelp(String key) {
    if (this.checkLetter(key)) {
      return new ActiveWord(this.word, this.rand, this.x, this.y);
    }
    else {
      return this;
    }
  }

  // checks if an inactive word is active (no)
  public boolean isActive() {
    return false;
  }
}

// to represent examples and test methods
class ExamplesZType {
  ExamplesZType() {}

  // completely random object
  Random rand = new Random();
  // seeded random object
  Random randSeed = new Random(5);

  Utils u = new Utils(new Random(5));  

  IWord emptyActive = new ActiveWord("", new Random(5), 0, 0);
  IWord emptyInactive = new InactiveWord("", new Random(5), 0, 0);

  IWord appleActive = new ActiveWord("apple", new Random(5), 10, 10);
  IWord appleInactive = new InactiveWord("ApplE", new Random(5), 20, 20);
  IWord bananaActive = new ActiveWord("banana", new Random(5), 30, 30);
  IWord coconutActive = new ActiveWord("Coconut", new Random(5), 40, 40);

  IWord durianInactive = new InactiveWord("durian", new Random(5), 50, 50);
  IWord elderberryActive = new ActiveWord("ElderBerry", new Random(5), 60, 60);
  IWord figActive = new ActiveWord("FIG", new Random(5), 60, 30);
  IWord kiwiInactive = new InactiveWord("kiwi", new Random(5), 70, 70);
  IWord atBottom = new InactiveWord("bottom", new Random(5), 70, 1000);

  ILoWord atBottomList = new ConsLoWord(this.atBottom, this.empty);
  ILoWord inactive = new ConsLoWord(this.durianInactive, this.empty);
  ILoWord empty = new MtLoWord();
  ILoWord halfFruit1 = new ConsLoWord(this.emptyActive, new ConsLoWord(this.appleActive,
      new ConsLoWord(this.bananaActive, new ConsLoWord(this.coconutActive, new MtLoWord()))));
  ILoWord halfFruit2 = new ConsLoWord(this.durianInactive,
      new ConsLoWord(this.elderberryActive, new ConsLoWord(this.figActive, new MtLoWord())));

  WorldScene s1 = new WorldScene(200, 200)
      .placeImageXY(new RectangleImage(200, 200, "solid", Color.BLACK), 100, 100);
  WorldScene sFruitSorted = new WorldScene(200, 200)
      .placeImageXY(new RectangleImage(200, 200, "solid", Color.BLACK), 100, 100)
      .placeImageXY(new TextImage("", 16, Color.MAGENTA), 0, 0)
      .placeImageXY(new TextImage("apple", 16, Color.MAGENTA), 10, 10)
      .placeImageXY(new TextImage("banana", 16, Color.MAGENTA), 30, 30)
      .placeImageXY(new TextImage("Coconut", 16, Color.MAGENTA), 40, 40);


  ZTypeWorld fruitScene = new ZTypeWorld(new Random(5), 30, this.halfFruit1, 0);
  ZTypeWorld randomScene = new ZTypeWorld(new Random(5));
  ZTypeWorld emptyScene = new ZTypeWorld(new Random(5), 0, this.empty, 0);

  // test the make scene method
  boolean testMakeScene(Tester t) {
    // test with empty list
    return t.checkExpect(emptyScene.makeScene(), IUtils.BLANK_WS)
        // test with given list
        && t.checkExpect(fruitScene.makeScene(), 
            IUtils.BLANK_WS
            .placeImageXY(new TextImage("", 16, IUtils.ACTIVE_COLOR), 0, 0)
            .placeImageXY(new TextImage("apple", 16, IUtils.ACTIVE_COLOR), 10, 10)
            .placeImageXY(new TextImage("banana", 16, IUtils.ACTIVE_COLOR), 30, 30)
            .placeImageXY(new TextImage("Coconut", 16, IUtils.ACTIVE_COLOR), 40, 40))
        // test with random list
        && t.checkExpect(randomScene.makeScene(), 
            IUtils.BLANK_WS
            .placeImageXY(
                new TextImage("mygew", 16, IUtils.INACTIVE_COLOR), 91, IUtils.SPAWN_HEIGHT));
  }

  // tester for addToEnd(IWord given)
  boolean testAddToEnd(Tester t) {
    return
        // adds a word to the end of an empty list
        t.checkExpect(this.empty.addToEnd(this.durianInactive),
            new ConsLoWord(this.durianInactive, new MtLoWord()))
        // adds a word to the end of a filled list
        && t.checkExpect(this.halfFruit1.addToEnd(this.kiwiInactive), new ConsLoWord(emptyActive,
            new ConsLoWord(appleActive, new ConsLoWord(bananaActive,
                new ConsLoWord(coconutActive, new ConsLoWord(kiwiInactive, new MtLoWord()))))));
  }  


  // tests the draw(WorldScene given) function
  boolean testDraw(Tester t) {
    return 
        // draws an empty list of words on a WorldScene, which returns the same WorldScene
        t.checkExpect(this.empty.draw(s1), s1)
        // draws a list on a WorldScene. 
        && t.checkExpect(this.halfFruit1.draw(s1), this.sFruitSorted);
  }

  // tests for filterOutEmpties()
  boolean testFilterOutEmpties(Tester t) {
    return 
        // filters out empty strings from an empty list --> returns empty list
        t.checkExpect(this.empty.filterOutEmpties(), this.empty) 
        // filters out empty strings from a filled list
        && t.checkExpect(this.halfFruit1.filterOutEmpties(),
            new ConsLoWord(this.appleActive, new ConsLoWord(this.bananaActive,
                new ConsLoWord(this.coconutActive, new MtLoWord()))))
        // filters out empty string from a list with no empty string.
        && t.checkExpect(this.halfFruit2.filterOutEmpties(), this.halfFruit2);

  }

  // tests checkEmpty()
  boolean testsCheckEmpty(Tester t) {
    return
        // checks if an empty word has an empty string
        t.checkExpect(this.emptyInactive.checkEmpty(), true)
        // checks if a filled word has an empty string in it.
        && t.checkExpect(this.coconutActive.checkEmpty(), false);
  }

  // test the check and reduce method
  boolean testCheckAndReduce(Tester t) {
    // test on an empty list
    return t.checkExpect(this.empty.checkAndReduce("a"), new MtLoWord())
        // test with str present
        && t.checkExpect(this.halfFruit1.checkAndReduce("z"), this.halfFruit1)
        // test with str not present
        && t.checkExpect(this.halfFruit1.checkAndReduce("a"),  new ConsLoWord(this.emptyActive, 
            new ConsLoWord(new ActiveWord("pple", new Random(5), 10, 10), 
                new ConsLoWord(this.bananaActive, 
                    new ConsLoWord(this.coconutActive, new MtLoWord())))))
        // test on mix with active/inactive
        && t.checkExpect(this.halfFruit2.checkAndReduce("k"), this.halfFruit2);
  }

  // test the remove first method
  boolean testsRemoveFirst(Tester t) {
    return
        // tries to remove the first letter i from an inactive kiwi (fails)
        t.checkExpect(this.kiwiInactive.removeFirst("i"), this.kiwiInactive)
        // tries to remove the first letter k from an inactive fruit (fails)
        && t.checkExpect(this.kiwiInactive.removeFirst("k"), this.kiwiInactive)
        // tries to remove the wrong letter from an active fruit (fails)
        && t.checkExpect(this.appleActive.removeFirst("k"), this.appleActive)
        // tries to remove the correct letter from an active fruit (success)
        && t.checkExpect(this.appleActive.removeFirst("a"), 
            new ActiveWord("pple", new Random(5), 10, 10));
  }

  //tests wordToText(WorldScene given)
  boolean testWordToText(Tester t) {
    return 
        // checks an empty active
        t.checkExpect(this.emptyActive.wordToText(s1), 
            s1.placeImageXY(new TextImage("", 16, Color.MAGENTA), 0, 0))
        // checks an empty inactive
        && t.checkExpect(this.emptyInactive.wordToText(s1), 
            s1.placeImageXY(new TextImage("", 16, Color.WHITE), 0, 0))
        // checks a filled inactive
        && t.checkExpect(this.kiwiInactive.wordToText(s1), 
            s1.placeImageXY(new TextImage("kiwi", 16, Color.WHITE), 70, 70))
        // checks a filled active
        && t.checkExpect(this.appleActive.wordToText(s1), 
            s1.placeImageXY(new TextImage("apple", 16, Color.MAGENTA), 10, 10)); 
  }

  // tests genList(int numWords, int limit)
  boolean testGenList(Tester t) {
    Utils u = new Utils(new Random(5));
    return 
        // generates a random list of 0 words -- which is empty.
        t.checkExpect(u.genList(1, 0), new MtLoWord()) 
        // generates a random list of 5 words
        && t.checkExpect(u.genList(1, 5), 
            new ConsLoWord(new InactiveWord("mygew", new Random(5), 91, 20),
                new ConsLoWord(new InactiveWord("dkpprp", new Random(5), 376, 20), 
                    new ConsLoWord(new InactiveWord("lfpoh", new Random(5), 87, 20),
                        new ConsLoWord(new InactiveWord("fsib", new Random(5), 257, 20),
                            new ConsLoWord(new InactiveWord("qcdam", new Random(5), 43, 20), 
                                new MtLoWord()))))))
        // generates a random list of two more words. 
        && t.checkExpect(u.genList(4, 5), 
            new ConsLoWord(new InactiveWord("magb", new Random(5), 346, 20),
                new ConsLoWord(new InactiveWord("xityift", new Random(5), 113, 20), 
                    new MtLoWord())));
  }

  // tests randWord(String s)
  boolean testRandWord(Tester t) {
    // tests different cases for making a random word, with a starter string (has no impact)
    Utils u = new Utils(new Random(5));
    return t.checkExpect(u.randWord("a"), "aryfq")
        && t.checkExpect(u.randWord("j"), "jgwdd")
        && t.checkExpect(u.randWord("s"), "srpgc")
        && t.checkExpect(u.randWord("l"), "lpoh");  
  }

  // tests randLetter()
  boolean testRandLetter(Tester t) {
    Utils u = new Utils(new Random(5));
    return
        // returns a new letter
        t.checkExpect(u.randLetter(), "m") 
        // new letter with a different seeded random
        && t.checkExpect(new Utils(new Random(3)).randLetter(), "j");
  }

  // tests newRandomInactive()
  boolean testNewRandomInactive(Tester t) {
    return 
        // tests the creation of a random inactive word
        t.checkExpect(new Utils(new Random(5)).newRandomInactive(), 
            new InactiveWord("mygew", new Random(5), 91, 20))
        // tests the creation with a different seeded random
        && t.checkExpect(new Utils(new Random(2)).newRandomInactive(), 
            new InactiveWord("ipog", new Random(5), 27, 20));
  }

  // tests shiftDown(int dist)
  boolean testShiftDown(Tester t) {
    // test moving down empty list
    return t.checkExpect(this.empty.shiftDown(5), new MtLoWord())
        // test moving down non-empty list
        && t.checkExpect(this.halfFruit2.shiftDown(5), new ConsLoWord(
            new InactiveWord("durian", new Random(5), 50, 55), new ConsLoWord(
                new ActiveWord("ElderBerry", new Random(5), 60, 65), new ConsLoWord(
                    new ActiveWord("FIG", new Random(5), 60, 35), new MtLoWord()))));
  }

  // tests reachedBottom()
  boolean testReachedBottom(Tester t) {
    // test if an empty list has reached the bottom
    return t.checkExpect(this.empty.reachedBottom(), false)
        // tests if a non-empty has reached the bottom
        && t.checkExpect(this.halfFruit1.reachedBottom(), false)
        // tests if at bottom
        && t.checkExpect(this.atBottomList.reachedBottom(), true);
  }

  // anyActive()
  boolean testAnyActive(Tester t) {
    // test empty list
    return t.checkExpect(this.empty.anyActive(), false)
        // test with actives
        && t.checkExpect(this.halfFruit1.anyActive(), true);
  }

  // findNewActive(String key)
  boolean testFindNewActive(Tester t) {
    // test empty list
    return t.checkExpect(this.empty.findNewActive("s"), new MtLoWord())
        // test non-empty
        && t.checkExpect(this.halfFruit2.findNewActive("E"), new ConsLoWord(
            this.durianInactive, new ConsLoWord(
                new ActiveWord("lderBerry", new Random(5), 60, 60), new ConsLoWord(
                    this.figActive, new MtLoWord()))));
  }

  // tests shiftDownHelp(int dist)
  boolean testShiftDownHelp(Tester t) {
    // test not moving word
    return t.checkExpect(this.durianInactive.shiftDownHelp(0), this.durianInactive)
        // test moving word
        && t.checkExpect(this.elderberryActive.shiftDownHelp(20),
            new ActiveWord("ElderBerry", new Random(5), 60, 80));
  }

  // tests makeActiveHelp(String key)
  boolean testMakeActiveHelp(Tester t) {
    // test wrong key press
    return t.checkExpect(this.durianInactive.makeActiveHelp("s"), this.durianInactive)
        // test key press
        && t.checkExpect(this.durianInactive.makeActiveHelp("d"), 
            new ActiveWord("durian", new Random(5), 50, 50))
        // test already active
        && t.checkExpect(this.elderberryActive.makeActiveHelp("d"), this.elderberryActive);
  }

  // tests isActive()
  boolean testIsActive(Tester t) {
    // test inactive
    return t.checkExpect(this.durianInactive.isActive(), false)
        // test active
        && t.checkExpect(this.elderberryActive.isActive(), true);
  }

  // tests reachedEnd()
  boolean testReachedEnd(Tester t) {
    // test not reached end
    return t.checkExpect(this.figActive.reachedEnd(), false)
        // test reached end
        && t.checkExpect(this.atBottom.reachedEnd(), true);
  }

  // test onKeyEvent(String key)
  boolean testOnKeyEvent(Tester t) {
    // test empty scene
    return t.checkExpect(emptyScene.onKeyEvent("s"), this.emptyScene)
        // test random scene
        && t.checkExpect(randomScene.onKeyEvent("m"), new ZTypeWorld(new Random(5), 1, 
            new ConsLoWord(new ActiveWord("ygew", new Random(5), 91, 20), this.empty), 1));
  }

  // tests ZTypeWorld onTick()
  boolean testOnTick(Tester t) {
    return t.checkExpect(this.emptyScene.onTick(), new ZTypeWorld(new Random(5), 1, 
        new ConsLoWord(new InactiveWord("mygew", new Random(5), 91, 21), this.empty), 0))
        && t.checkExpect(this.fruitScene.onTick(), new ZTypeWorld(new Random(5), 31, 
            new ConsLoWord(new ActiveWord("apple", new Random(5), 10, 11), 
                new ConsLoWord(new ActiveWord("banana", new Random(5), 30, 31),
                    new ConsLoWord(new ActiveWord("Coconut", new Random(5), 40, 41),
                        new ConsLoWord(new InactiveWord("mygew", new Random(5), 91, 21),
                            this.empty)))), 0));

  }
  
  boolean testWorldEnds(Tester t) {
    //tests for false
    return t.checkExpect(this.emptyScene.worldEnds(), 
        new WorldEnd(false, emptyScene.makeScene()));
  }

  // tests bigbang
  boolean testBigBang(Tester t) {
    ZTypeWorld world = new ZTypeWorld(new Random());
    return world.bigBang(IUtils.GAME_WIDTH, IUtils.GAME_HEIGHT, IUtils.TICK_RATE);
  } 

}