import tester.*;
import javalib.worldimages.*;
import javalib.funworld.*;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/*

EXTRA CREDIT:

- score tracker
- variable word lengths
- enhanced graphics

*/

// represents the game constants
interface Constants {
  int SCREEN_HEIGHT = 500;
  int SCREEN_WIDTH = 500;
  int INITIAL_WORD_HEIGHT = 20;

  double TICK_RATE = 0.04;

  String ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

  Color SCREEN_BACKGROUND_COLOR = new Color(32, 32, 32);
  Color ACTIVE_COLOR = new Color(200, 16, 45);
  Color INACTIVE_COLOR = Color.WHITE;

  WorldScene BLANK_SCENE = new WorldScene(SCREEN_WIDTH, SCREEN_HEIGHT).placeImageXY(
      new RectangleImage(SCREEN_WIDTH, SCREEN_HEIGHT, "solid", SCREEN_BACKGROUND_COLOR), 250, 250);
  WorldScene END_SCENE = new WorldScene(SCREEN_WIDTH, SCREEN_HEIGHT).placeImageXY(
      new RectangleImage(SCREEN_WIDTH, SCREEN_HEIGHT, "solid", SCREEN_BACKGROUND_COLOR), 250, 250)
      .placeImageXY(new TextImage("THE GAME IS OVER :(", 40, Color.ORANGE), 250, 225);
}

// represents a list of words
interface ILoWord {

  // returns an ILoWord where any active words starting with the given letter are
  // reduced by removing the first letter
  ILoWord checkAndReduce(String firstLetter);

  // returns an ILoWord with all empty IWords filtered out
  ILoWord filterOutEmpties();

  // returns an ILoWord that adds the given IWord to the end
  ILoWord addToEnd(IWord given);

  // moves an ILoWord down in a WorldScene by a certain distance
  ILoWord moveWordsDown(int distance);

  // determines whether any IWords have reached the bottom of the page
  boolean hasAnyWordsReachedBottom();

  // determines if there are any active IWords
  boolean isAnyActiveWords();

  // finds a new active IWord in the ILoWord, else it keeps the current one
  ILoWord findNewActiveWord(String key);

  // draws the ILoWord onto the given WorldScene
  WorldScene draw(WorldScene given);
}

// represents a word in the ZType game
interface IWord {

  // removes the first letter of an ActiveWord if it matches the given letter
  IWord removeFirstLetter(String firstLetter);

  // determines if the first letter matches the given letter
  boolean isMatching(String firstLetter);

  // writes word onto the given WorldScene
  WorldScene writeWord(WorldScene given);

  // determines if the IWord is empty
  boolean isEmpty();

  // moves a word down by a certain distance
  IWord moveWordDown(int distance);

  // makes the IWord into an ActiveWord if the first letter matches
  IWord makeActiveWord(String key);

  // determines if an IWord is an ActiveWord
  boolean isActive();

  // determines if the word has reached the bottom of the page
  boolean hasWordReachedBottom();
}

// represents a world class to animate a list of words on a scene
class ZTypeWorld extends World implements Constants {
  Random random;
  int timer;
  ILoWord words;
  int score;
  
  /*
    * CLASS TEMPLATE:
    * Fields:
    * this.random -> Random
    * this.timer -> int
    * this.words -> ILoWord
    * this.score -> int
    * 
    * Methods:
    * this.makeScene() -> WorldScene
    * this.onKeyEvent(String key) -> ZTypeWorld
    * this.onTick() -> ZTypeWorld
    * this.worldEnds() -> WorldEnd
    * 
    * Methods on fields:
    * this.rest.checkAndReduce(String firstLetter) -> ILoWord
    * this.rest.filterOutEmpties() -> ILoWord
    * this.rest.addToEnd(IWord given) -> ILoWord
    * this.rest.moveWordsDown(int distance) -> ILoWord
    * this.rest.hasAnyWordsReachedBottom() -> boolean
    * this.rest.isAnyActiveWords() -> boolean
    * this.rest.findNewActiveWord(String key) -> ILoWord
    * this.rest.draw(WorldScene given) -> WorldScene
   */

  // for implementation
  ZTypeWorld() {
    this.random = new Random();
    this.timer = 1;
    this.words = new Utils(random).makeNewList(1, 1);
    this.score = 1;
  }

  // for testing words
  ZTypeWorld(Random random) {
    this.random = random;
    this.timer = 1;
    this.words = new Utils(random).makeNewList(1, 1);
    this.score = 1;
  }

  // for testing everything
  ZTypeWorld(Random random, int timer, ILoWord words, int score) {
    this.random = random;
    this.timer = timer;
    this.words = words;
    this.score = score;
  }

  // draws a scene
  public WorldScene makeScene() {
    return this.words.draw(BLANK_SCENE);
  }

  // changes the scene on each keypress
  public ZTypeWorld onKeyEvent(String key) {
    if (this.words.isAnyActiveWords()) {
      return new ZTypeWorld(this.random, this.timer, this.words.checkAndReduce(key),
          this.score + 1);
    }
    else {
      return new ZTypeWorld(this.random, this.timer, this.words.findNewActiveWord(key), this.score);
    }
  }

  // move the words on the scene
  // adds a new Word at a random location at every tick of the clock
  public ZTypeWorld onTick() {
    // designed to increase the frame rate of the game
    if (timer % 45 == 0) {
      return new ZTypeWorld(this.random, this.timer + 1,
          this.words.addToEnd(new Utils(this.random).newRandomInactiveWord()).moveWordsDown(1)
              .filterOutEmpties(),
          this.score);
    }
    else {
      return new ZTypeWorld(this.random, this.timer + 1,
          this.words.moveWordsDown(1).filterOutEmpties(), this.score);
    }
  }

  // ends the world if any words reach the bottom.
  public WorldEnd worldEnds() {
    if (this.words.hasAnyWordsReachedBottom()) {
      return new WorldEnd(true, END_SCENE.placeImageXY(
          new TextImage("Score: " + Integer.toString(score), 25, Color.WHITE), 250, 325));
    }
    else {
      return new WorldEnd(false, this.makeScene());
    }
  }

}

//to generate random strings for words
class Utils implements Constants {
  Random random;

  Utils(Random random) {
    this.random = random;
  }

  /*
   * CLASS TEMPLATE:
   * Fields:
   * this.random -> Random
   * 
   * Methods:
   * this.createRandomLetter() -> String
   * this.createRandomWord(String letters) -> String
   * this.newRandomInactiveWord() -> IWord
   * this.makeNewList(int numWords, int limit) -> ILoWord
   * 
   * Methods on fields:
   * 
   */

  // generates a random letter
  public String createRandomLetter() {
    int randomInteger = random.nextInt(51);
    return ALPHABET.substring(randomInteger, randomInteger + 1);
  }

  // generates a random word of variable length
  public String createRandomWord(String letters) {
    int randomInteger = 4 + random.nextInt(4);
    if (letters.length() >= randomInteger) {
      return letters;
    }
    else {
      return this.createRandomWord(letters + this.createRandomLetter());
    }
  }

  // returns a new random inactive word
  public IWord newRandomInactiveWord() {
    return new InactiveWord(this.createRandomWord(this.createRandomLetter()), this.random,
        INITIAL_WORD_HEIGHT);
  }

  // generates a new list of random inactive words
  public ILoWord makeNewList(int numWords, int limit) {
    if (numWords > limit) {
      return new MtLoWord();
    }
    else {
      return new ConsLoWord(this.newRandomInactiveWord(), this.makeNewList(numWords + 1, limit));
    }
  }
}

// represents an empty list of words
class MtLoWord implements ILoWord {

  MtLoWord() {
  }

  /*
   * CLASS TEMPLATE:
   * Fields:
   * 
   * Methods:
   * this.checkAndReduce(String firstLetter) -> ILoWord
   * this.filterOutEmpties() -> ILoWord
   * this.addToEnd(IWord given) -> ILoWord
   * this.moveWordsDown(int distance) -> ILoWord
   * this.hasAnyWordsReachedBottom() -> boolean
   * this.isAnyActiveWords() -> boolean
   * this.findNewActiveWord(String key) -> ILoWord
   * this.draw(WorldScene given) -> WorldScene
   * 
   * Methods on fields:
   * 
   */
  
  // returns an ILoWord where any active words starting with the given letter are
  // reduced by removing the first letter
  public ILoWord checkAndReduce(String firstLetter) {
    return this;
  }

  // returns an ILoWord with all empty IWords filtered out
  public ILoWord filterOutEmpties() {
    return this;
  }

  // returns an ILoWord that adds the given IWord to the end
  public ILoWord addToEnd(IWord given) {
    /*
     * METHOD TEMPLATE: Everything from class-wide template Methods of parameters:
     * given.removeFirstLetter(String firstLetter) -> IWord given.isMatching(String
     * firstLetter) -> boolean given.writeWord(WorldScene given) -> WorldScene
     * given.isEmpty() -> boolean given.moveWordDown(int distance) -> IWord
     * given.makeActiveword(String key) -> IWord given.isActive() -> boolean
     * given.hasWordReachedBottom() -> boolean
     */
    return new ConsLoWord(given, new MtLoWord());
  }

  // moves an ILoWord down in a WorldScene by a certain distance
  public ILoWord moveWordsDown(int distance) {
    return this;
  }

  // determines whether any IWords have reached the bottom of the page
  public boolean hasAnyWordsReachedBottom() {
    return false;
  }

  // determines if there are any active IWords
  public boolean isAnyActiveWords() {
    return false;
  }

  // finds a new active IWord in the ILoWord, else it keeps the current one
  public ILoWord findNewActiveWord(String key) {
    return this;
  }

  // draws the ILoWord onto the given WorldScene
  public WorldScene draw(WorldScene given) {
    return given;
  }
}

// represents a non-empty list of words
class ConsLoWord implements ILoWord {
  IWord first;
  ILoWord rest;

  ConsLoWord(IWord first, ILoWord rest) {
    this.first = first;
    this.rest = rest;
  }
  
  /*
  * CLASS TEMPLATE:
  * Fields:
  * this.first -> IWord
  * this.rest -> ILoWord
  * 
  * Methods:
  * this.checkAndReduce(String firstLetter) -> ILoWord
  * this.filterOutEmpties() -> ILoWord
  * this.addToEnd(IWord given) -> ILoWord
  * this.moveWordsDown(int distance) -> ILoWord
  * this.hasAnyWordsReachedBottom() -> boolean
  * this.isAnyActiveWords() -> boolean
  * this.findNewActiveWord(String key) -> ILoWord
  * this.draw(WorldScene given) -> WorldScene
  * 
  * Methods on fields:
  * this.first.removeFirstLetter(String firstLetter) -> IWord
  * this.first.isMatching(String firstLetter) -> boolean
  * this.first.writeWord(WorldScene given) -> WorldScene
  * this.first.isEmpty() -> boolean
  * this.first.moveWordDown(int distance) -> IWord
  * this.first.makeActiveword(String key) -> IWord
  * this.first.isActive() -> boolean
  * this.first.hasWordReachedBottom() -> boolean
  * this.rest.checkAndReduce(String firstLetter) -> ILoWord
  * this.rest.filterOutEmpties() -> ILoWord
  * this.rest.addToEnd(IWord given) -> ILoWord
  * this.rest.moveWordsDown(int distance) -> ILoWord
  * this.rest.hasAnyWordsReachedBottom() -> boolean
  * this.rest.isAnyActiveWords() -> boolean
  * this.rest.findNewActiveWord(String key) -> ILoWord
  * this.rest.draw(WorldScene given) -> WorldScene
  */

  // returns an ILoWord where any active words starting with the given letter are
  // reduced by removing the first letter
  public ILoWord checkAndReduce(String firstLetter) {
    return new ConsLoWord(this.first.removeFirstLetter(firstLetter),
        this.rest.checkAndReduce(firstLetter));
  }

  // returns an ILoWord with all empty IWords filtered out
  public ILoWord filterOutEmpties() {
    if (this.first.isEmpty()) {
      return this.rest.filterOutEmpties();
    }
    else {
      return new ConsLoWord(this.first, this.rest.filterOutEmpties());
    }
  }

  // returns an ILoWord that adds the given IWord to the end
  public ILoWord addToEnd(IWord given) {
    /* METHOD TEMPLATE: 
     * Everything from class-wide template
     * Methods of parameters: 
     * given.removeFirstLetter(String firstLetter) -> IWord
     * given.isMatching(String firstLetter) -> boolean
     * given.writeWord(WorldScene given) -> WorldScene
     * given.isEmpty() -> boolean
     * given.moveWordDown(int distance) -> IWord
     * given.makeActiveword(String key) -> IWord
     * given.isActive() -> boolean
     * given.hasWordReachedBottom() -> boolean
    */
    return new ConsLoWord(this.first, this.rest.addToEnd(given));
  }

  // moves an ILoWord down in a WorldScene by a certain distance
  public ILoWord moveWordsDown(int distance) {
    return new ConsLoWord(this.first.moveWordDown(distance), this.rest.moveWordsDown(distance));
  }

  // determines whether any IWords have reached the bottom of the page
  public boolean hasAnyWordsReachedBottom() {
    return this.first.hasWordReachedBottom() || this.rest.hasAnyWordsReachedBottom();
  }

  // determines if there are any active IWords
  public boolean isAnyActiveWords() {
    return this.first.isActive() || this.rest.isAnyActiveWords();
  }

  // finds a new active IWord in the ILoWord, else it keeps the current one
  public ILoWord findNewActiveWord(String key) {
    if (this.first.makeActiveWord(key).isActive()) {
      return new ConsLoWord(this.first.makeActiveWord(key).removeFirstLetter(key), this.rest);
    }
    else {
      return new ConsLoWord(this.first, this.rest.findNewActiveWord(key));
    }
  }

  // draws the ILoWord onto the given WorldScene
  public WorldScene draw(WorldScene given) {
    return this.rest.draw(this.first.writeWord(given));
  }
}

// represents any word
abstract class AWord implements IWord, Constants {
  String word;
  Random random;
  int x;
  int y;

  AWord(String word, Random random, int x, int y) {
    this.word = word;
    this.random = random;
    this.x = x;
    this.y = y;
  }
  
  /*
   * CLASS TEMPLATE:
   * Fields:
   * this.word -> String
   * this.random -> Random
   * this.x -> int
   * this.y -> int
   * 
   * Methods:
   * this.removeFirstLetter(String firstLetter) -> IWord
   * this.isMatching(String firstLetter) -> boolean
   * this.writeWord(WorldScene given) -> WorldScene
   * this.isEmpty() -> boolean
   * this.moveWordDown(int distance) -> IWord
   * this.isActive() -> boolean
   * this.hasWordReachedBottom() -> boolean
   * 
   * Methods on fields:
   * 
   */

  // removes the first letter of an ActiveWord if it matches the given letter
  public abstract IWord removeFirstLetter(String firstLetter);

  // determines if the first letter matches the given letter
  public boolean isMatching(String firstLetter) {
    if (this.isEmpty()) {
      return false;
    }
    else {
      return firstLetter.equals(this.word.substring(0, 1));
    }
  }

  // writes word onto the given WorldScene
  public WorldScene writeWord(WorldScene given) {
    return given.placeImageXY(new TextImage(this.word, 20, INACTIVE_COLOR), this.x, this.y);
  }

  // determines if the IWord is empty
  public boolean isEmpty() {
    return this.word.isEmpty();
  }

  // moves a word down by a certain distance
  public abstract IWord moveWordDown(int distance);

  // determines if an IWord is an ActiveWord
  public abstract boolean isActive();

  // determines if the word has reached the bottom of the page
  public boolean hasWordReachedBottom() {
    return this.y >= SCREEN_HEIGHT;
  }
}

// represents an active word in the ZType game
class ActiveWord extends AWord {

  ActiveWord(String word, Random random, int x, int y) {
    super(word, random, x, y);
  }
  
  /*
   * CLASS TEMPLATE:
   * Fields:
   * this.word -> String
   * this.random -> Random
   * this.x -> int
   * this.y -> int
   * 
   * Methods:
   * this.removeFirstLetter(String firstLetter) -> IWord
   * this.writeWord(WorldScene given) -> WorldScene
   * this.moveWordDown(int distance) -> IWord
   * this.makeActiveWord(String key) -> IWord
   * this.isActive() -> boolean
   * 
   * Methods on fields:
   * 
   */
  
  // removes the first letter of an ActiveWord if it matches the given letter
  public IWord removeFirstLetter(String firstLetter) {
    if (this.isEmpty()) {
      return this;
    }
    else if (this.isMatching(firstLetter)) {
      return new ActiveWord(this.word.substring(1), this.random, this.x, this.y);
    }
    return this;
  }

  // writes word onto the given WorldScene
  public WorldScene writeWord(WorldScene given) {
    return given.placeImageXY(new TextImage(this.word, 20, ACTIVE_COLOR), this.x, this.y);
  }

  // moves a word down by a certain distance
  public IWord moveWordDown(int distance) {
    return new ActiveWord(this.word, this.random, this.x, this.y + distance);
  }

  // makes the IWord into an ActiveWord if the first letter matches
  public IWord makeActiveWord(String key) {
    return this;
  }

  // determines if an IWord is an ActiveWord
  public boolean isActive() {
    return true;
  }
}

// represents an inactive word in the ZType game
class InactiveWord extends AWord {

  // initializes a word at a specific spot
  InactiveWord(String word, Random random, int x, int y) {
    super(word, random, x, y);
  }

  // initializes a word at a random location
  InactiveWord(String word, Random random, int y) {
    super(word, random, 20 + random.nextInt(SCREEN_WIDTH - 50), INITIAL_WORD_HEIGHT);
  }

  /*
   * CLASS TEMPLATE:
   * Fields:
   * this.word -> String
   * this.random -> Random
   * this.x -> int
   * this.y -> int
   * 
   * Methods:
   * this.removeFirstLetter(String firstLetter) -> IWord
   * this.writeWord(WorldScene given) -> WorldScene
   * this.moveWordDown(int distance) -> IWord
   * this.makeActiveWord(String key) -> IWord
   * this.isActive() -> boolean
   * 
   * Methods on fields:
   * 
   */
    
  // removes the first letter of an InactiveWord if it matches the given letter
  public IWord removeFirstLetter(String firstLetter) {
    return this;
  }

  // moves a word down by a certain distance
  public IWord moveWordDown(int distance) {
    return new InactiveWord(this.word, this.random, this.x, this.y + distance);
  }

  // makes the IWord into an ActiveWord if the first letter matches
  public IWord makeActiveWord(String key) {
    if (this.isMatching(key)) {
      return new ActiveWord(this.word, this.random, this.x, this.y);
    }
    else {
      return this;
    }
  }

  // determines if an IWord is an ActiveWord
  public boolean isActive() {
    return false;
  }
}

// represents examples and tests for the ZType game
class ExamplesZType implements Constants {
  Random random = new Random(1);
  Utils utils = new Utils(random);

  // example words
  IWord activeWord1 = new ActiveWord("hELLo", random, 100, 100);
  IWord activeWord2 = new ActiveWord("wOrLD", random, 100, 100);
  IWord inactiveWord1 = new InactiveWord("jAvA", random, 100, 100);
  IWord inactiveWord2 = new InactiveWord("cOdE", random, 100, 100);
  IWord emptyActiveWord = new ActiveWord("", random, 100, 100);
  IWord emptyInactiveWord = new InactiveWord("", random, 100, 100);
  IWord bottomActiveWord = new ActiveWord("eNd", random, 100, SCREEN_HEIGHT);
  IWord bottomInactiveWord = new InactiveWord("eNd", random, 100, SCREEN_HEIGHT);

  // example list of words
  ILoWord emptyList = new MtLoWord();
  ILoWord singleActiveList = new ConsLoWord(activeWord1, new MtLoWord());
  ILoWord singleInactiveList = new ConsLoWord(inactiveWord1, new MtLoWord());
  ILoWord mixedList1 = new ConsLoWord(activeWord1,
      new ConsLoWord(inactiveWord2, new ConsLoWord(inactiveWord1, new MtLoWord())));
  ILoWord mixedList2 = new ConsLoWord(inactiveWord1,
      new ConsLoWord(activeWord2, new ConsLoWord(activeWord1, new MtLoWord())));
  ILoWord listWithBottomWord = new ConsLoWord(bottomActiveWord,
      new ConsLoWord(inactiveWord2, new MtLoWord()));
  ILoWord listWithEmptyWord = new ConsLoWord(emptyActiveWord,
      new ConsLoWord(activeWord2, new ConsLoWord(emptyInactiveWord, new MtLoWord())));

  // example worlds
  ZTypeWorld worldEmpty = new ZTypeWorld(random, 1, emptyList, 0);
  ZTypeWorld worldSingleWord = new ZTypeWorld(random, 1, singleActiveList, 10);
  ZTypeWorld worldMultipleWords = new ZTypeWorld(random, 1, mixedList1, 20);
  ZTypeWorld worldBottomWord = new ZTypeWorld(random, 1, listWithBottomWord, 30);

  // TEST ZTYPEWORLD

  // tests the make scene method
  boolean testMakeScene(Tester t) {
    return
    // tests on an empty world scene
    t.checkExpect(worldEmpty.makeScene(), BLANK_SCENE)
        // tests on a scene with one word
        && t.checkExpect(worldSingleWord.makeScene(),
            BLANK_SCENE.placeImageXY(new TextImage("hELLo", 20, ACTIVE_COLOR), 100, 100))
        // tests on a scene with multiple words
        && t.checkExpect(worldMultipleWords.makeScene(), BLANK_SCENE

            .placeImageXY(new TextImage("hELLo", 20, ACTIVE_COLOR), 100, 100)
            .placeImageXY(new TextImage("cOdE", 20, INACTIVE_COLOR), 100, 100)
            .placeImageXY(new TextImage("jAvA", 20, INACTIVE_COLOR), 100, 100))
        // tests on a scene with one word at the bottom
        && t.checkExpect(worldBottomWord.makeScene(),
            BLANK_SCENE.placeImageXY(new TextImage("eNd", 20, ACTIVE_COLOR), 100, SCREEN_HEIGHT)
                .placeImageXY(new TextImage("cOdE", 20, INACTIVE_COLOR), 100, 100));
  }

  // tests the onKeyEvent method
  boolean testOnKeyEvent(Tester t) {
    return
    // If 'h' is pressed, the first active word ("hELLo") should be reduced to
    // "ELLo" and score increased
    t.checkExpect(new ZTypeWorld(random, 1, singleActiveList, 5).onKeyEvent("h"),
        new ZTypeWorld(random, 1,
            new ConsLoWord(new ActiveWord("ELLo", random, 100, 100), new MtLoWord()), 6))
        // If 'w' is pressed, active word "wOrLD" should be reduced to "OrLD" in
        // mixedList2
        && t.checkExpect(new ZTypeWorld(random, 1, mixedList2, 10).onKeyEvent("w"),
            new ZTypeWorld(random, 1,
                new ConsLoWord(inactiveWord1,
                    new ConsLoWord(new ActiveWord("OrLD", random, 100, 100),
                        new ConsLoWord(activeWord1, new MtLoWord()))),
                11))
        // If 'j' is pressed in a world with only inactive words, "jAvA" should become
        // active
        && t.checkExpect(new ZTypeWorld(random, 1, singleInactiveList, 15).onKeyEvent("j"),
            new ZTypeWorld(random, 1,
                new ConsLoWord(new ActiveWord("AvA", random, 100, 100), new MtLoWord()), 15))
        // If 'x' is pressed in a world with empty words, nothing should change
        && t.checkExpect(new ZTypeWorld(random, 1, listWithEmptyWord, 20).onKeyEvent("x"),
            new ZTypeWorld(random, 1, listWithEmptyWord, 21));
  }

  // tests the onTick method
  boolean testOnTick(Tester t) {
    return
    // tests when the scene is just made and a new word should be added
    t.checkExpect(new ZTypeWorld(random, 1, singleActiveList, 5).onTick(),
        new ZTypeWorld(random, 2,
            new ConsLoWord(new ActiveWord("hELLo", random, 100, 101), new MtLoWord()), 5))
        // tests when the words should move down by one unit
        && t.checkExpect(new ZTypeWorld(random, 1, mixedList1, 10).onTick(),
            new ZTypeWorld(random, 2, new ConsLoWord(new ActiveWord("hELLo", random, 100, 101),
                new ConsLoWord(new InactiveWord("cOdE", random, 100, 101),
                    new ConsLoWord(new InactiveWord("jAvA", random, 100, 101), new MtLoWord()))),
                10))
        // tests when empty words should be filtered out
        && t.checkExpect(new ZTypeWorld(random, 1, listWithEmptyWord, 15).onTick(),
            new ZTypeWorld(random, 2,
                new ConsLoWord(new ActiveWord("wOrLD", random, 100, 101), new MtLoWord()), 15))
        // tests when new word should pop onto the screen
        && t.checkExpect(new ZTypeWorld(new Random(5), 45, mixedList2, 0).onTick(), new ZTypeWorld(
            new Random(5), 46,
            new ConsLoWord(new InactiveWord("jAvA", new Random(5), 100, 101), new ConsLoWord(
                new ActiveWord("wOrLD", new Random(5), 100, 101),
                new ConsLoWord(new ActiveWord("hELLo", new Random(5), 100, 101),
                    new ConsLoWord(new InactiveWord("fGWIf", new Random(5), 451, 21), emptyList)))),
            0));
  }

  // tests the worldEnds method
  boolean testWorldEnds(Tester t) {
    return
    // tests when no words have reached the bottom, game should continue
    t.checkExpect(new ZTypeWorld(new Random(5), 1, mixedList1, 10).worldEnds(),
        new WorldEnd(false, new ZTypeWorld(new Random(5), 1, mixedList1, 10).makeScene()))
        // tests when a word reaches the bottom, game should end and display score
        && t.checkExpect(new ZTypeWorld(new Random(5), 1, listWithBottomWord, 20).worldEnds(),
            new WorldEnd(true,
                END_SCENE.placeImageXY(new TextImage("Score: 20", 25, Color.WHITE), 250, 325)))
        // tests when the world is empty (no words), game should continue
        && t.checkExpect(new ZTypeWorld(new Random(5), 1, emptyList, 30).worldEnds(),
            new WorldEnd(false, new ZTypeWorld(new Random(5), 1, emptyList, 30).makeScene()));
  }

  // TEST UTILS

  // tests the createRandomLetter method
  boolean testCreateRandomLetter(Tester t) {
    // tests generating a random letter
    return t.checkExpect(new Utils(new Random(5)).createRandomLetter(), "f")
        // tests generating another random letter
        && t.checkExpect(new Utils(new Random(10)).createRandomLetter(), "d")
        // tests generating another random letter
        && t.checkExpect(new Utils(new Random(20)).createRandomLetter(), "V");
  }

  // tests the createRandomWord method
  boolean testCreateRandomWord(Tester t) {
    // tests generating a random word
    return t.checkExpect(new Utils(new Random(5)).createRandomWord(""), "qDoJTE")
        // tests generating another random word
        && t.checkExpect(new Utils(new Random(10)).createRandomWord(""), "yjLUO")
        // tests generating another random word
        && t.checkExpect(new Utils(new Random(20)).createRandomWord(""), "kNaT");
  }

  // tests the newRandomInactiveWord method
  boolean testNewRandomInactiveWord(Tester t) {
    // tests generating a new random inactive word
    return t.checkExpect(new Utils(new Random(5)).newRandomInactiveWord(),
        new InactiveWord("fGWIf", new Random(5), 451, 20))
        // tests generating another new random inactive word
        && t.checkExpect(new Utils(new Random(10)).newRandomInactiveWord(),
            new InactiveWord("dgXzm", new Random(10), 93, 20))
        // tests generating another new random inactive word
        && t.checkExpect(new Utils(new Random(20)).newRandomInactiveWord(),
            new InactiveWord("VWrICo", new Random(20), 205, 20))

        && t.checkExpect(new Utils(new Random(30)).newRandomInactiveWord(),
            new InactiveWord("GrtFx", new Random(30), 101, 20));
  }

  // tests the makeNewList method
  boolean testMakeNewList(Tester t) {
    return // tests making a list of 3 words with Random(5)
    t.checkExpect(new Utils(new Random(5)).makeNewList(1, 3),
        new ConsLoWord(new InactiveWord("fGWIf", new Random(5), 451, 20),
            new ConsLoWord(new InactiveWord("EanRpf", new Random(5), 376, 20),
                new ConsLoWord(new InactiveWord("zxmOR", new Random(5), 177, 20), new MtLoWord()))))
        // tests making a list of 2 words with Random(10)
        && t.checkExpect(new Utils(new Random(10)).makeNewList(1, 2),
            new ConsLoWord(new InactiveWord("dgXzm", new Random(10), 93, 20),
                new ConsLoWord(new InactiveWord("lgKen", new Random(10), 279, 20), new MtLoWord())))
        // tests making a list of 1 word with Random(20)
        && t.checkExpect(new Utils(new Random(20)).makeNewList(1, 1),
            new ConsLoWord(new InactiveWord("VWrICo", new Random(20), 205, 20), new MtLoWord()))
        // tests making a list of 4 words with Random(30)
        && t.checkExpect(new Utils(new Random(30)).makeNewList(1, 4),
            new ConsLoWord(new InactiveWord("GrtFx", new Random(30), 101, 20),
                new ConsLoWord(new InactiveWord("oJMJIOM", new Random(30), 291, 20),
                    new ConsLoWord(new InactiveWord("KssKM", new Random(30), 160, 20),
                        new ConsLoWord(new InactiveWord("INUn", new Random(30), 55, 20),
                            new MtLoWord())))));
  }

  // ILOWORD

  // tests the checkAndReduce method
  boolean testCheckAndReduce(Tester t) {
    // tests on a list with one word having matching letter
    return t.checkExpect(singleActiveList.checkAndReduce("h"),
        new ConsLoWord(new ActiveWord("ELLo", random, 100, 100), new MtLoWord()))
        // tests on a mixed list but no words have matching letters
        && t.checkExpect(mixedList1.checkAndReduce("j"), mixedList1)
        // tests on a mixed list with one word having a matching letter
        && t.checkExpect(mixedList2.checkAndReduce("w"),
            new ConsLoWord(inactiveWord1, new ConsLoWord(new ActiveWord("OrLD", random, 100, 100),
                new ConsLoWord(activeWord1, new MtLoWord()))));
  }

  // tests the filterOutEmpties method
  boolean testFilterOutEmpties(Tester t) {
    // tests on a list with an empty word
    return t.checkExpect(listWithEmptyWord.filterOutEmpties(),
        new ConsLoWord(activeWord2, new MtLoWord()))
        // tests on an empty list
        && t.checkExpect(emptyList.filterOutEmpties(), emptyList)
        // tests on a mixed list with no empty words
        && t.checkExpect(mixedList1.filterOutEmpties(), mixedList1);
  }

  // tests the addToEnd method
  boolean testAddToEnd(Tester t) {
    // tests on an empty list adding an active word
    return t.checkExpect(emptyList.addToEnd(activeWord1),
        new ConsLoWord(activeWord1, new MtLoWord()))
        // tests on an empty list adding an inactive word
        && t.checkExpect(emptyList.addToEnd(inactiveWord1),
            new ConsLoWord(inactiveWord1, new MtLoWord()))
        // tests on a mixed list adding an active word
        && t.checkExpect(mixedList1.addToEnd(activeWord2),
            new ConsLoWord(activeWord1, new ConsLoWord(inactiveWord2,
                new ConsLoWord(inactiveWord1, new ConsLoWord(activeWord2, new MtLoWord())))))
        // tests on a mixed list adding an inactive word
        && t.checkExpect(mixedList2.addToEnd(inactiveWord2),
            new ConsLoWord(inactiveWord1, new ConsLoWord(activeWord2,
                new ConsLoWord(activeWord1, new ConsLoWord(inactiveWord2, new MtLoWord())))));
  }

  // tests the moveWordsDown method
  boolean testMoveWordsDown(Tester t) {
    // tests moving the words down on a list with one active word
    return t.checkExpect(singleActiveList.moveWordsDown(10),
        new ConsLoWord(new ActiveWord("hELLo", random, 100, 110), new MtLoWord()))
        && t.checkExpect(singleInactiveList.moveWordsDown(10),
            new ConsLoWord(new InactiveWord("jAvA", random, 100, 110), new MtLoWord()))
        // tests moving the words down on a mixed list
        && t.checkExpect(mixedList2.moveWordsDown(5),
            new ConsLoWord(new InactiveWord("jAvA", random, 100, 105),
                new ConsLoWord(new ActiveWord("wOrLD", random, 100, 105),
                    new ConsLoWord(new ActiveWord("hELLo", random, 100, 105), new MtLoWord()))))
        // tests moving the words down on another mixed list
        && t.checkExpect(mixedList1.moveWordsDown(5),
            new ConsLoWord(new ActiveWord("hELLo", random, 100, 105),
                new ConsLoWord(new InactiveWord("cOdE", random, 100, 105),
                    new ConsLoWord(new InactiveWord("jAvA", random, 100, 105), new MtLoWord()))));
  }

  // tests the hasAnyWordsReachedBottom method
  boolean testHasAnyWordsReachedBottom(Tester t) {
    // tests on a list with a word at the bottom
    return t.checkExpect(listWithBottomWord.hasAnyWordsReachedBottom(), true)
        // tests on a list with no words at the bottom
        && t.checkExpect(mixedList1.hasAnyWordsReachedBottom(), false)
        // tests on another list with no words at the bottom
        && t.checkExpect(mixedList1.hasAnyWordsReachedBottom(), false);
  }

  // tests the isAnyActiveWords method
  boolean testIsAnyActiveWords(Tester t) {
    // tests on a list with no active words
    return t.checkExpect(singleInactiveList.isAnyActiveWords(), false)
        // tests on a list with active words
        && t.checkExpect(mixedList1.isAnyActiveWords(), true)
        // tests on another list with active words
        && t.checkExpect(mixedList2.isAnyActiveWords(), true);
  }

  // tests the findNewActiveWord method
  boolean testFindNewActiveWord(Tester t) {
    // tests on a list with an active word matching the first letter
    return t.checkExpect(mixedList1.findNewActiveWord("h"),
        new ConsLoWord(new ActiveWord("ELLo", random, 100, 100),
            new ConsLoWord(inactiveWord2, new ConsLoWord(inactiveWord1, new MtLoWord()))))
        // tests on a list with an inactive word matching the first letter
        && t.checkExpect(mixedList2.findNewActiveWord("j"),
            new ConsLoWord(new ActiveWord("AvA", random, 100, 100),
                new ConsLoWord(activeWord2, new ConsLoWord(activeWord1, new MtLoWord()))))
        // tests on a list with no word matching the first letter
        && t.checkExpect(mixedList1.findNewActiveWord("x"), mixedList1);
  }

  // tests the draw method
  boolean testDraw(Tester t) {
    // tests drawing a list with one active word
    return t.checkExpect(singleActiveList.draw(BLANK_SCENE),
        BLANK_SCENE.placeImageXY(new TextImage("hELLo", 20, ACTIVE_COLOR), 100, 100))
        // tests drawing an empty list on a scene
        && t.checkExpect(emptyList.draw(BLANK_SCENE), BLANK_SCENE)
        // tests drawing a mixed list
        && t.checkExpect(mixedList1.draw(BLANK_SCENE),
            BLANK_SCENE.placeImageXY(new TextImage("hELLo", 20, ACTIVE_COLOR), 100, 100)
                .placeImageXY(new TextImage("cOdE", 20, INACTIVE_COLOR), 100, 100)
                .placeImageXY(new TextImage("jAvA", 20, INACTIVE_COLOR), 100, 100))
        // tests drawing a list with one word at the bottom
        && t.checkExpect(listWithBottomWord.draw(BLANK_SCENE),
            BLANK_SCENE.placeImageXY(new TextImage("eNd", 20, ACTIVE_COLOR), 100, SCREEN_HEIGHT)
                .placeImageXY(new TextImage("cOdE", 20, INACTIVE_COLOR), 100, 100));
  }

  // IWORD

  // tests the removeFirstLetter method
  boolean testRemoveFirstLetter(Tester t) {
    // tests an active word that matches the first letter
    return t.checkExpect(this.activeWord1.removeFirstLetter("h"),
        new ActiveWord("ELLo", random, 100, 100))
        // tests an active word that doesn't match the first letter
        && t.checkExpect(this.activeWord1.removeFirstLetter("H"),
            new ActiveWord("hELLo", random, 100, 100))
        // tests an empty active word
        && t.checkExpect(this.emptyActiveWord.removeFirstLetter("h"),
            new ActiveWord("", random, 100, 100))
        // tests an inactive word that matches the first letter
        && t.checkExpect(this.inactiveWord1.removeFirstLetter("j"), this.inactiveWord1)
        // tests an inactive word that doesn't match the first letter
        && t.checkExpect(this.inactiveWord1.removeFirstLetter("j"), this.inactiveWord1)
        // tests an empty inactive word
        && t.checkExpect(this.inactiveWord1.removeFirstLetter("j"), this.inactiveWord1);
  }

  // tests the isMatching method
  boolean testIsMatching(Tester t) {
    // tests an active word with matching first letter
    return t.checkExpect(this.activeWord1.isMatching("h"), true)
        // tests an active word with no matching letter
        && t.checkExpect(this.activeWord1.isMatching("H"), false)
        // tests an empty active word
        && t.checkExpect(this.emptyActiveWord.isMatching("h"), false)
        // tests an inactive word with matching first letter
        && t.checkExpect(this.inactiveWord1.isMatching("j"), true)
        // tests an inactive word with no matching first letter
        && t.checkExpect(this.inactiveWord1.isMatching("J"), false)
        // tests an empty inactive word
        && t.checkExpect(this.emptyInactiveWord.isMatching("J"), false);
  }

  // tests the writeWord method
  boolean testWriteWord(Tester t) {
    // tests writing an active word
    return t.checkExpect(this.activeWord1.writeWord(BLANK_SCENE),
        BLANK_SCENE.placeImageXY(new TextImage("hELLo", 20, ACTIVE_COLOR), 100, 100))
        // test writing an inactive word
        && t.checkExpect(this.inactiveWord1.writeWord(BLANK_SCENE),
            BLANK_SCENE.placeImageXY(new TextImage("jAvA", 20, INACTIVE_COLOR), 100, 100))
        // tests writing an empty active word
        && t.checkExpect(this.emptyActiveWord.writeWord(BLANK_SCENE),
            BLANK_SCENE.placeImageXY(new TextImage("", 20, ACTIVE_COLOR), 100, 100))
        // tests writing an empty inactive word
        && t.checkExpect(this.emptyInactiveWord.writeWord(BLANK_SCENE),
            BLANK_SCENE.placeImageXY(new TextImage("", 20, INACTIVE_COLOR), 100, 100));
  }

  // tests the isEmpty method
  boolean testIsEmpty(Tester t) {
    // tests an active word that is not empty
    return t.checkExpect(this.activeWord1.isEmpty(), false)
        // tests an empty active word
        && t.checkExpect(this.emptyActiveWord.isEmpty(), true)
        // tests an inactive word that is not empty
        && t.checkExpect(this.inactiveWord1.isEmpty(), false)
        // tests an empty inactive word
        && t.checkExpect(this.emptyInactiveWord.isEmpty(), true);
  }

  // tests the moveWordDown method
  boolean testMoveWordDown(Tester t) {
    // tests an active word that is not empty
    return t.checkExpect(this.activeWord1.moveWordDown(1),
        new ActiveWord("hELLo", random, 100, 101))
        // tests an empty active word
        && t.checkExpect(this.emptyActiveWord.moveWordDown(1), new ActiveWord("", random, 100, 101))
        // tests an inactive word that is not empty
        && t.checkExpect(this.inactiveWord1.moveWordDown(1),
            new InactiveWord("jAvA", random, 100, 101))
        // tests an empty inactive word
        && t.checkExpect(this.emptyInactiveWord.moveWordDown(1),
            new InactiveWord("", random, 100, 101));
  }

  // tests the makeActiveWord method
  boolean testMakeActiveWtord(Tester t) {
    // tests an active word that matches the first letter
    return t.checkExpect(this.activeWord1.makeActiveWord("h"), this.activeWord1)
        // tests an active word that does not match the first letter
        && t.checkExpect(this.activeWord1.makeActiveWord("H"), this.activeWord1)
        // tests an empty active word
        && t.checkExpect(this.emptyActiveWord.makeActiveWord("h"), this.emptyActiveWord)
        // tests an inactive word that matches the first letter
        && t.checkExpect(this.inactiveWord1.makeActiveWord("j"),
            new ActiveWord("jAvA", random, 100, 100))
        // tests an inactive word that does not match the first letter
        && t.checkExpect(this.inactiveWord1.makeActiveWord("J"), this.inactiveWord1)
        // tests an empty inactive word
        && t.checkExpect(this.emptyInactiveWord.makeActiveWord("j"), this.emptyInactiveWord);
  }

  // tests the isActive method
  boolean testIsActive(Tester t) {
    // tests an active word
    return t.checkExpect(this.activeWord1.isActive(), true)
        // tests an empty active word
        && t.checkExpect(this.emptyActiveWord.isActive(), true)
        // tests an inactive word
        && t.checkExpect(this.inactiveWord1.isActive(), false)
        // tests an empty inactive word
        && t.checkExpect(this.emptyInactiveWord.isActive(), false);
  }

  // tests the hasWordReachedBottom method
  boolean testHasWordReachedBottom(Tester t) {
    // tests an active word that has not reached the bottom
    return t.checkExpect(this.activeWord1.hasWordReachedBottom(), false)
        // tests an inactive word that has not reached the bottom
        && t.checkExpect(this.inactiveWord1.hasWordReachedBottom(), false)
        // test an active word that has reached the bottom
        && t.checkExpect(this.bottomActiveWord.hasWordReachedBottom(), true)
        // tests an inactive word that has reached the bottom
        && t.checkExpect(this.bottomInactiveWord.hasWordReachedBottom(), true);
  }

  // tests the bigBang method
  boolean testBigBang(Tester t) {
    ZTypeWorld world = new ZTypeWorld(new Random());
    return world.bigBang(SCREEN_WIDTH, SCREEN_HEIGHT, TICK_RATE);
  }
}