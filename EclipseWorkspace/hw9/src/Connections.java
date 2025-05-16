import tester.*;

/*
 * 
 * EXTRA CREDIT:
 * 
 * Enhance the graphics.
 * Display a message if the player correctly guessed 3 out of 4 of the words in a group.
 * Add a button to allow the player to shuffle the words to display them in a different order.
 * Add a button to allow the player to deselect all selected words.
 * If the player wins, tailor the win message based on how many tries the player used.
 * 
 */

import javalib.worldimages.*;
import javalib.impworld.*;
import java.awt.Color;
import java.util.*;

// game constants
interface GameConstants {
  int WIDTH = 650;
  int HEIGHT = 700;
  int WORD_BOX_WIDTH = 130;
  int WORD_BOX_HEIGHT = 60;
  int TILE_HORIZONTAL_SPACING = 20;
  int TILE_VERTICAL_SPACING = 15;
  Color TILE_COLOR = Color.decode("#efefe6");
  Color TEXT_COLOR = Color.decode("#121212");
  Color SELECTED_COLOR = Color.decode("#5a594e");
  ArrayList<Color> GROUP_COLORS = new ArrayList<>(Arrays.asList(Color.decode("#f9df6d"),
      Color.decode("#a0c35a"), Color.decode("#b0c4ef"), Color.decode("#ba81c5")));
  ArrayList<ArrayList<ConnectionGroup>> ALL_SETS = new ArrayList<>(Arrays.asList(
      new ArrayList<>(Arrays.asList(
          new ConnectionGroup("FRUITS", GROUP_COLORS.get(0),
              Arrays.asList("Apple", "Banana", "Grape", "Orange")),
          new ConnectionGroup("COLORS", GROUP_COLORS.get(1),
              Arrays.asList("Red", "Blue", "Green", "Yellow")),
          new ConnectionGroup("ANIMALS", GROUP_COLORS.get(2),
              Arrays.asList("Cat", "Dog", "Lion", "Tiger")),
          new ConnectionGroup("SCHOOL SUBJECTS", GROUP_COLORS.get(3),
              Arrays.asList("Math", "Science", "History", "English")))),
      new ArrayList<>(Arrays.asList(
          new ConnectionGroup("SEASONS", GROUP_COLORS.get(0),
              Arrays.asList("Spring", "Summer", "Fall", "Winter")),
          new ConnectionGroup("SHAPES", GROUP_COLORS.get(1),
              Arrays.asList("Circle", "Square", "Triangle", "Rectangle")),
          new ConnectionGroup("TRANSPORT", GROUP_COLORS.get(2),
              Arrays.asList("Car", "Bus", "Train", "Plane")),
          new ConnectionGroup("WEATHER", GROUP_COLORS.get(3),
              Arrays.asList("Rain", "Snow", "Sun", "Wind")))),
      new ArrayList<>(Arrays.asList(
          new ConnectionGroup("ICE CREAM FLAVORS", GROUP_COLORS.get(0),
              Arrays.asList("Vanilla", "Chocolate", "Strawberry", "Mint")),
          new ConnectionGroup("SPORTS", GROUP_COLORS.get(1),
              Arrays.asList("Soccer", "Tennis", "Basketball", "Baseball")),
          new ConnectionGroup("TOYS", GROUP_COLORS.get(2),
              Arrays.asList("Doll", "Puzzle", "Lego", "Yo-yo")),
          new ConnectionGroup("CLOTHING", GROUP_COLORS.get(3),
              Arrays.asList("Shirt", "Pants", "Hat", "Shoes")))),
      new ArrayList<>(Arrays.asList(
          new ConnectionGroup("ROOMS IN A HOUSE", GROUP_COLORS.get(0),
              Arrays.asList("Kitchen", "Bathroom", "Bedroom", "Living Room")),
          new ConnectionGroup("FAMILY MEMBERS", GROUP_COLORS.get(1),
              Arrays.asList("Mom", "Dad", "Sister", "Brother")),
          new ConnectionGroup("HOUSEHOLD ITEMS", GROUP_COLORS.get(2),
              Arrays.asList("Chair", "Table", "Lamp", "Couch")),
          new ConnectionGroup("CLEANING SUPPLIES", GROUP_COLORS.get(3),
              Arrays.asList("Soap", "Broom", "Mop", "Sponge")))),
      new ArrayList<>(Arrays.asList(
          new ConnectionGroup("NUMBERS IN WORDS", GROUP_COLORS.get(0),
              Arrays.asList("One", "Two", "Three", "Four")),
          new ConnectionGroup("DAYS OF THE WEEK", GROUP_COLORS.get(1),
              Arrays.asList("Monday", "Tuesday", "Wednesday", "Thursday")),
          new ConnectionGroup("MONTHS", GROUP_COLORS.get(2),
              Arrays.asList("January", "April", "July", "October")),
          new ConnectionGroup("UNITS OF TIME", GROUP_COLORS.get(3),
              Arrays.asList("Second", "Minute", "Hour", "Day"))))));
}

// represents a word tile within the connections game
class ConnectionWord implements GameConstants {
  String word;
  String groupLabel;
  int x;
  int y;
  boolean isSelected;
  boolean isSolved;
  Color color;

  ConnectionWord(String word, String groupLabel, Color color) {
    this.word = word;
    this.groupLabel = groupLabel;
    this.isSelected = false;
    this.isSolved = false;
    this.color = color;
  }

  // sets the position of the ConnectionWord
  // EFFECT: changes x to the given x coordinate and y to the given y coordinate
  public void setPosition(int x, int y) {
    this.x = x;
    this.y = y;
  }

  // draws the word tile onto the world scene
  public WorldImage draw() {
    Color fill = this.getFillColor();
    Color textColor = this.getTextColor();
    WorldImage box = new RectangleImage(WORD_BOX_WIDTH, WORD_BOX_HEIGHT, "solid", fill);
    WorldImage text = new TextImage(this.word.toUpperCase(), 18, textColor);
    return new OverlayImage(text, box);
  }

  // gets the appropriate fill color for the word tile
  public Color getFillColor() {
    if (this.isSolved) {
      return this.color;
    }
    else if (this.isSelected) {
      return SELECTED_COLOR;
    }
    else {
      return TILE_COLOR;
    }
  }

  // gets the appropriate text color for the word tile
  public Color getTextColor() {
    if (this.isSelected) {
      return Color.WHITE;
    }
    else {
      return TEXT_COLOR;
    }
  }

  // determines whether the cursor is hovering over the word tile
  public boolean containsPoint(Posn position) {
    return position.x >= this.x - WORD_BOX_WIDTH / 2 && position.x <= this.x + WORD_BOX_WIDTH / 2
        && position.y >= this.y - WORD_BOX_HEIGHT / 2 && position.y <= this.y + WORD_BOX_HEIGHT / 2;
  }

  // determines whether the group label matches the given label
  public boolean isInGroup(String label) {
    return this.groupLabel.equals(label);
  }

  // makes the ConnectionWord a solved and deselected tile
  // EFFECT: changes isSolved to true and isSelected to false
  public void markSolved() {
    this.isSolved = true;
    this.isSelected = false;
  }

  // EFFECT: marks the word as not solved
  public void markUnsolved() {
    this.isSolved = false;
  }

  // returns the word appended to the end of the given string
  public String addWordToString(String result) {
    result += this.word;
    return result;
  }

  // marks the word tile as deselected
  // EFFECT: changes isSelected to false
  public void markDeselected() {
    this.isSelected = false;
  }

  // toggles the selection of the word tile and adds or removes from selected
  // tiles
  // EFFECT: changes isSelected to the opposite of itself
  public void clickOnTile(Posn pos, ArrayList<ConnectionWord> selectedTiles) {
    if (!this.isSolved && this.containsPoint(pos)) {
      this.isSelected = !this.isSelected;
      if (this.isSelected) {
        selectedTiles.add(this);
      }
      else {
        selectedTiles.remove(this);
      }
    }
  }
}

// represents a connection group
class ConnectionGroup implements GameConstants {
  ArrayList<ConnectionWord> tiles;
  String label;
  Color color;

  ConnectionGroup(String label, Color color, List<String> words) {
    this.label = label;
    this.color = color;
    this.tiles = new ArrayList<ConnectionWord>();
    for (String word : words) {
      this.tiles.add(new ConnectionWord(word, label, color));
    }
  }

  // determines it four tiles are selected and all are in the same group
  public boolean matches(ArrayList<ConnectionWord> selectedTiles) {
    return selectedTiles.size() == 4 && allInSameGroup(selectedTiles);
  }

  // determines if the selected word tiles are part of the same group
  public boolean allInSameGroup(ArrayList<ConnectionWord> selectedTiles) {
    for (ConnectionWord tile : selectedTiles) {
      if (!tile.isInGroup(this.label)) {
        return false;
      }
    }
    return true;
  }

  // marks each selected tile as solved
  // EFFECT: changes isSelected to false and isSolved to true for each
  // ConnectionWord
  public void markSolved(ArrayList<ConnectionWord> selectedTiles) {
    for (ConnectionWord tile : selectedTiles) {
      tile.markSolved();
    }
  }

  // draws the solved connection group
  public WorldImage draw() {
    String words = this.getGroupWords();
    WorldImage labelText = new TextImage(this.label.toUpperCase(), 18, FontStyle.BOLD, Color.BLACK);
    WorldImage wordsText = new TextImage(words.toUpperCase(), 16, TEXT_COLOR);
    WorldImage content = new AboveImage(labelText, wordsText);
    WorldImage background = new RectangleImage(WIDTH - 80, WORD_BOX_HEIGHT + TILE_VERTICAL_SPACING,
        "solid", this.color);
    return new OverlayImage(content, background);
  }

  // gets the words of a solved connection group separated by a comma
  public String getGroupWords() {
    String result = "";
    for (int i = 0; i < this.tiles.size(); i++) {
      ConnectionWord tile = this.tiles.get(i);
      result = tile.addWordToString(result);
      if (i < this.tiles.size() - 1) {
        result += ", ";
      }
    }
    return result;
  }

  // adds tiles to a given list
  public ArrayList<ConnectionWord> addTilesToList(ArrayList<ConnectionWord> result) {
    result.addAll(this.tiles);
    return result;
  }

  // determines if the group label matches the label of the given tile
  public boolean isInGroup(ConnectionWord tile) {
    return tile.isInGroup(this.label);
  }
}

// represents the connections world
class ConnectionsWorld extends World implements GameConstants {
  Random random;
  ArrayList<ConnectionWord> tiles;
  ArrayList<ConnectionGroup> allGroups;
  ArrayList<ConnectionGroup> solvedGroups;
  ArrayList<ConnectionWord> unsolvedTiles;
  ArrayList<ConnectionWord> selectedTiles;
  int triesLeft;
  boolean gameOver;
  boolean won;
  boolean isCloseGuess;
  int closeGuessTimer;

  ConnectionsWorld() {
    this(new Random());
  }

  ConnectionsWorld(Random random) {
    this.random = random;
    this.initGame();
  }

  // starts the game
  // sets all the initial values (triesLeft, gameOver, won, solvedGroups,
  // allGroups, and tiles)
  // shuffles the titles
  // EFFECT: sets triesLeft to 4, gameOver to false, won to false, isCloseGuess to
  // false, closeGuessTimer to 0, solvedGroups to none, allGroups to a random set,
  // unsolvedTiles to the given tiles, selectedtiles to none
  public void initGame() {
    this.triesLeft = 4;
    this.gameOver = false;
    this.won = false;
    this.isCloseGuess = false;
    this.closeGuessTimer = 0;
    this.solvedGroups = new ArrayList<ConnectionGroup>();
    this.allGroups = chooseRandomGroupSet();
    this.tiles = flattenTiles();
    Collections.shuffle(this.tiles, this.random);
    this.unsolvedTiles = new ArrayList<ConnectionWord>();
    this.selectedTiles = new ArrayList<ConnectionWord>();
    for (ConnectionWord tile : this.tiles) {
      this.unsolvedTiles.add(tile);
    }
  }

  // picks a random game out of the 5 possible games
  public ArrayList<ConnectionGroup> chooseRandomGroupSet() {
    List<ConnectionGroup> selectedSet = ALL_SETS.get(this.random.nextInt(ALL_SETS.size()));
    return new ArrayList<ConnectionGroup>(selectedSet);
  }

  // returns all the word tiles in one array list
  public ArrayList<ConnectionWord> flattenTiles() {
    ArrayList<ConnectionWord> result = new ArrayList<ConnectionWord>();
    for (ConnectionGroup group : this.allGroups) {
      result = group.addTilesToList(result);
    }
    for (ConnectionWord tile : result) {
      tile.markUnsolved();
    }
    return result;
  }

  // returns a tailored win message based on how many tries are left
  public String getMessage() {
    if (this.won) {
      if (this.triesLeft == 4) {
        return "Perfect game! You didn't make a single mistake!";
      }
      else if (this.triesLeft == 3) {
        return "Great job! Only one mistake!";
      }
      else if (this.triesLeft == 2) {
        return "Nice! You solved it with two mistakes.";
      }
      else if (this.triesLeft == 1) {
        return "Whew! You made it with only one try left.";
      }
    }
    return "You lost :(";
  }

  // draw the solved groups onto the world scene
  public int drawSolvedGroups(WorldScene scene) {
    int offset = 140;
    for (int i = 0; i < this.solvedGroups.size(); i++) {
      ConnectionGroup group = this.solvedGroups.get(i);
      scene.placeImageXY(group.draw(), WIDTH / 2, offset + 40);
      offset += 80;
    }
    return offset;
  }

  // returns a string of dots that represents how many mistakes left
  public String getMistakeDots() {
    String result = "";
    for (int i = 0; i < this.triesLeft; i++) {
      result += "\u25CF ";
    }
    return result;
  }

  // shuffles the order of the unsolved tiles only
  // EFFECT: changes each tile to the randomized selected tile
  public void shuffleUnsolvedTiles() {
    if (!this.unsolvedTiles.isEmpty()) {
      Collections.shuffle(this.unsolvedTiles, this.random);
      int index = 0;
      for (int i = 0; i < this.tiles.size(); i++) {
        ConnectionWord tile = this.tiles.get(i);
        if (!this.unsolvedTiles.contains(tile)) {
          this.tiles.set(i, this.unsolvedTiles.get(index));
          index++;
        }
      }
    }
  }

  // determine whether the selected tiles are a match when the user clicks ENTER
  // determine whether the game is over
  // EFFECT: modifies triesLeft, isCloseGuess, and isCloseGuessTimer depending on the 
  // state of the game
  public void handleConnectionSubmit() {
    ConnectionGroup match = this.findMatchingGroup();
    if (match != null) {
      match.markSolved(this.selectedTiles);
      this.solvedGroups.add(match);
      this.unsolvedTiles.removeAll(this.selectedTiles);
      this.deselectTiles();
      this.checkWin();
    }
    else if (this.selectedTiles.size() == 4) {
      this.triesLeft -= 1;
      this.isCloseGuess = this.isCloseGuessInSameGroup();
      if (this.isCloseGuess) {
        this.closeGuessTimer = 0;
      }
      this.deselectTiles();
      if (triesLeft == 0) {
        this.gameOver = true;
        this.won = false;
      }
    }
  }

  // returns true if exactly 3 out of 4 selected words belong to the same group
  public boolean isCloseGuessInSameGroup() {
    for (ConnectionGroup group : this.allGroups) {
      int count = 0;
      for (ConnectionWord tile : this.selectedTiles) {
        if (group.isInGroup(tile)) {
          count++;
        }
      }
      if (count == 3) {
        return true;
      }
    }
    return false;
  }

  // determine if the selected words match any of the correct groups
  public ConnectionGroup findMatchingGroup() {
    for (ConnectionGroup group : this.allGroups) {
      if (group.matches(this.selectedTiles)) {
        return group;
      }
    }
    return null;
  }

  // unselect all the tiles that are selected
  // EFFECT: mark each tile of selectedTiles to deselected
  public void deselectTiles() {
    for (ConnectionWord tile : this.selectedTiles) {
      tile.markDeselected();
    }
    this.selectedTiles = new ArrayList<ConnectionWord>();
  }

  // set game to won and over if all tiles are solved
  // EFFECT: change gameOver to true and won to true if the tiles are solved
  public void checkWin() {
    if (this.unsolvedTiles.isEmpty()) {
      this.gameOver = true;
      this.won = true;
    }
  }

  // draws the scene onto the world scene
  public WorldScene makeScene() {
    WorldScene scene = new WorldScene(WIDTH, HEIGHT);
    scene.placeImageXY(new RectangleImage(WIDTH, HEIGHT, "solid", Color.WHITE), WIDTH / 2,
        HEIGHT / 2);
    scene.placeImageXY(new TextImage("Connections", 32, TEXT_COLOR), WIDTH / 2, 40);
    scene.placeImageXY(new TextImage("Create groups of four!", 20, TEXT_COLOR), WIDTH / 2, 90);

    int offset = this.drawSolvedGroups(scene);
    for (int i = 0; i < this.unsolvedTiles.size(); i++) {
      int row = i / 4;
      int col = i % 4;
      ConnectionWord tile = this.unsolvedTiles.get(i);
      int tileX = 90 + col * (WORD_BOX_WIDTH + TILE_HORIZONTAL_SPACING);
      int tileY = offset + 40 + row * (WORD_BOX_HEIGHT + TILE_VERTICAL_SPACING);
      tile.setPosition(tileX, tileY);
      scene.placeImageXY(tile.draw(), tileX, tileY);
    }

    int bottomY = 130 + 5 * (WORD_BOX_HEIGHT + TILE_VERTICAL_SPACING);
    if (this.gameOver) {
      String gameOverText = this.getMessage();
      scene.placeImageXY(new TextImage(gameOverText, 20, TEXT_COLOR), WIDTH / 2, bottomY);
      scene.placeImageXY(new TextImage("Press R to start a new game", 20, TEXT_COLOR), WIDTH / 2,
          bottomY + 35);
    }
    else {
      scene.placeImageXY(new TextImage("Press ENTER to submit connection", 20, TEXT_COLOR),
          WIDTH / 2, bottomY);
      String dots = getMistakeDots();
      scene.placeImageXY(new TextImage("Mistakes remaining: " + dots, 20, TEXT_COLOR), WIDTH / 2,
          bottomY + 35);

      if (this.isCloseGuess) {
        scene.placeImageXY(new TextImage("One away...", 20, FontStyle.ITALIC, Color.GRAY),
            WIDTH / 2, bottomY + 70);
      }

    }
    return scene;
  }

  // restart the game if the user clicks R
  // check if the tiles match or the game is over if the user clicks ENTER
  // deselect tiles if user clicks D
  // shuffle tiles if user clicks S
  public void onKeyEvent(String key) {
    if (key.equals("r")) {
      this.initGame();
    }
    else if (key.equals("enter") && !this.gameOver) {
      this.handleConnectionSubmit();
    }
    else if (key.equals("d")) {
      this.deselectTiles();
    }
    else if (key.equals("s") && !this.gameOver) {
      this.shuffleUnsolvedTiles();
    }
  }

  // change a tile to selected if the mouse is hovering over it
  public void onMouseClicked(Posn pos) {
    if (this.gameOver) {
      return;
    }
    for (ConnectionWord tile : this.tiles) {
      tile.clickOnTile(pos, this.selectedTiles);
    }
  }

  // event handler for every tick
  public void onTick() {
    if (this.isCloseGuess) {
      this.closeGuessTimer += 1;
      if (this.closeGuessTimer >= 100) {
        this.isCloseGuess = false;
        this.closeGuessTimer = 0;
      }
    }
  }
}

// represents examples and tests for connections
class ExamplesConnections implements GameConstants {
  // connection words
  ConnectionWord apple;
  ConnectionWord banana;
  ConnectionWord cat;
  ConnectionWord grape;
  ConnectionWord orange;
  ConnectionWord dog;

  // connection groups
  ConnectionGroup emptyGroup;
  ConnectionGroup fruitGroup;
  ConnectionGroup animalGroup;
  ConnectionGroup colorGroup;

  // array list of connection words
  ArrayList<ConnectionWord> correctFruitGroup;
  ArrayList<ConnectionWord> threeFruitOneAnimal;

  // posns
  Posn centerApple;
  Posn outside;

  // world
  ConnectionsWorld world;
  Random seededRandom;

  // initialize data
  public void initData() {
    this.apple = new ConnectionWord("Apple", "FRUITS", Color.RED);
    this.banana = new ConnectionWord("Banana", "FRUITS", Color.RED);
    this.cat = new ConnectionWord("Cat", "ANIMALS", Color.BLUE);
    this.grape = new ConnectionWord("Grape", "FRUITS", GROUP_COLORS.get(0));
    this.orange = new ConnectionWord("Orange", "FRUITS", GROUP_COLORS.get(0));
    this.dog = new ConnectionWord("Dog", "ANIMALS", GROUP_COLORS.get(1));

    this.apple.setPosition(100, 100);
    this.banana.setPosition(200, 200);
    this.cat.setPosition(300, 300);

    this.centerApple = new Posn(100, 100);
    this.outside = new Posn(300, 300);

    this.emptyGroup = new ConnectionGroup("", Color.WHITE, Arrays.asList());
    this.fruitGroup = new ConnectionGroup("FRUITS", GROUP_COLORS.get(0),
        Arrays.asList("Apple", "Banana", "Grape", "Orange"));
    this.animalGroup = new ConnectionGroup("ANIMALS", GROUP_COLORS.get(1),
        Arrays.asList("Cat", "Dog", "Lion", "Tiger"));
    this.colorGroup = new ConnectionGroup("COLORS", GROUP_COLORS.get(1),
        Arrays.asList("Red", "Blue", "Green", "Yellow"));

    this.correctFruitGroup = new ArrayList<>(Arrays.asList(apple, banana, grape, orange));
    this.threeFruitOneAnimal = new ArrayList<>(Arrays.asList(apple, banana, grape, cat));

    this.seededRandom = new Random(1);
    this.world = new ConnectionsWorld(this.seededRandom);
    this.world.allGroups = new ArrayList<>(Arrays.asList(this.fruitGroup, this.colorGroup));
    this.world.tiles = this.world.flattenTiles();
    this.world.unsolvedTiles = new ArrayList<>(this.world.tiles);
    this.world.solvedGroups = new ArrayList<>();
    this.world.selectedTiles = new ArrayList<>();
    this.world.triesLeft = 4;
    this.world.won = false;
    this.world.gameOver = false;
  }

  // test running the game
  public void testRunGame(Tester t) {
    ConnectionsWorld world = new ConnectionsWorld();
    world.bigBang(WIDTH, HEIGHT, 0.03);
  }

  // tests the setPosition method
  public void testSetPosition(Tester t) {
    this.initData();
    // sets the position of a ConnectionWord to (200, 250)
    this.apple.setPosition(200, 250);
    t.checkExpect(this.apple.x, 200);
    t.checkExpect(this.apple.y, 250);
    // sets the position of another ConnectionWord to (100, 150)
    this.banana.setPosition(100, 150);
    t.checkExpect(this.banana.x, 100);
    t.checkExpect(this.banana.y, 150);
    // sets the position of another ConnectionWord to (50, 100)
    this.cat.setPosition(50, 100);
    t.checkExpect(this.cat.x, 50);
    t.checkExpect(this.cat.y, 100);
  }

  // tests the draw method in ConnectionWord and ConnectionGroup
  public void testConnectionWordDraw(Tester t) {
    this.initData();
    // test initial ConnectionWord
    WorldImage expectedBox = new RectangleImage(WORD_BOX_WIDTH, WORD_BOX_HEIGHT, "solid",
        this.apple.getFillColor());
    WorldImage expectedText = new TextImage("APPLE", 18, this.apple.getTextColor());
    WorldImage expectedImage = new OverlayImage(expectedText, expectedBox);
    t.checkExpect(this.apple.draw(), expectedImage);
    // test solved ConnectionWord
    this.apple.markSolved();
    WorldImage solvedBox = new RectangleImage(WORD_BOX_WIDTH, WORD_BOX_HEIGHT, "solid",
        this.apple.getFillColor());
    WorldImage solvedText = new TextImage("APPLE", 18, this.apple.getTextColor());
    WorldImage expectedSolvedImage = new OverlayImage(solvedText, solvedBox);
    t.checkExpect(this.apple.draw(), expectedSolvedImage);
    // test selected ConnectionWord
    this.apple.isSolved = false;
    this.apple.isSelected = true;
    WorldImage selectedBox = new RectangleImage(WORD_BOX_WIDTH, WORD_BOX_HEIGHT, "solid",
        this.apple.getFillColor());
    WorldImage selectedText = new TextImage("APPLE", 18, this.apple.getTextColor());
    WorldImage expectedSelectedImage = new OverlayImage(selectedText, selectedBox);
    t.checkExpect(this.apple.draw(), expectedSelectedImage);
    // tests drawing a solved ConnectionGroup box
    String label = this.fruitGroup.label.toUpperCase();
    String groupWords = "Apple, Banana, Grape, Orange";
    WorldImage labelText = new TextImage(label, 18, FontStyle.BOLD, Color.BLACK);
    WorldImage wordsText = new TextImage(groupWords.toUpperCase(), 16, TEXT_COLOR);
    WorldImage content = new AboveImage(labelText, wordsText);
    WorldImage background = new RectangleImage(WIDTH - 80, WORD_BOX_HEIGHT + TILE_VERTICAL_SPACING,
        "solid", GROUP_COLORS.get(0));
    WorldImage expectedImage2 = new OverlayImage(content, background);
    t.checkExpect(this.fruitGroup.draw(), expectedImage2);
  }

  // tests the getFillColor method
  public void testGetFillColor(Tester t) {
    this.initData();
    // tests getting the fill color on an unselected ConnectionWord
    t.checkExpect(this.apple.getFillColor(), TILE_COLOR);
    // tests getting the fill color on a selected ConnectionWord
    this.apple.isSelected = true;
    t.checkExpect(this.apple.getFillColor(), SELECTED_COLOR);
    // tests getting the fill color on a solved ConnectionWord
    this.apple.isSelected = false;
    this.apple.isSolved = true;
    t.checkExpect(this.apple.getFillColor(), Color.RED);
  }

  // tests the getTextColor
  public void testGetTextColor(Tester t) {
    this.initData();
    // tests getting the text color of an unselected ConnectionWord
    t.checkExpect(this.apple.getTextColor(), TEXT_COLOR);
    // tests getting the text color of another unselected ConnectionWord
    t.checkExpect(this.banana.getTextColor(), TEXT_COLOR);
    // tests getting the text color of a selected ConnectionWord
    this.apple.isSelected = true;
    t.checkExpect(this.apple.getTextColor(), Color.WHITE);
  }

  // tests the containsPoint method
  public void testContainsPoint(Tester t) {
    this.initData();
    // tests when the cursor is on the word tile
    t.checkExpect(this.apple.containsPoint(this.centerApple), true);
    // tests when the cursor is on another word tile
    t.checkExpect(this.cat.containsPoint(this.outside), true);
    // tests when the cursor is not on the word tile
    t.checkExpect(this.apple.containsPoint(this.outside), false);
  }

  // tests the ConnectionWord isInGroup method
  public void testConnectionWordIsInGroup(Tester t) {
    this.initData();
    // tests a ConnectionWord that is in the group
    t.checkExpect(this.apple.isInGroup("FRUITS"), true);
    // tests another ConnectionWord that is in the group
    t.checkExpect(this.banana.isInGroup("FRUITS"), true);
    // test a ConnectionWord that is not in the group
    t.checkExpect(this.apple.isInGroup("ANIMALS"), false);
  }

  // tests the ConnectionWord markSolved method
  public void testConnectionWordMarkSolved(Tester t) {
    this.initData();
    this.apple.isSelected = true;
    t.checkExpect(this.apple.isSelected, true);
    // tests marking a ConnectionWord that is selected to solved
    this.apple.markSolved();
    t.checkExpect(this.apple.isSolved, true);
    t.checkExpect(this.apple.isSelected, false);
    // tests marking a ConnectionWord that is not selected to solved
    this.banana.markSolved();
    t.checkExpect(this.banana.isSolved, true);
    t.checkExpect(this.banana.isSelected, false);
    // tests marking a ConnectionWord that is already solved
    this.cat.isSolved = true;
    this.cat.markSolved();
    t.checkExpect(this.cat.isSolved, true);
    t.checkExpect(this.cat.isSelected, false);
  }

  // tests the markUnsolved method
  public void testMarkUnsolved(Tester t) {
    this.initData();
    // tests marking a ConnectionWord that is solved to unsolved
    this.apple.isSolved = true;
    t.checkExpect(this.apple.isSolved, true);
    this.apple.markUnsolved();
    t.checkExpect(this.apple.isSolved, false);
    // tests marking another ConnectionWord that is solved to unsolved
    this.banana.isSolved = true;
    t.checkExpect(this.banana.isSolved, true);
    this.banana.markUnsolved();
    t.checkExpect(this.banana.isSolved, false);
    // tests marking a ConnectionWord that is already unsolved to unsolved
    this.cat.markUnsolved();
    t.checkExpect(this.cat.isSolved, false);
  }

  // tests the addWordToString method
  public void testAddWordToString(Tester t) {
    this.initData();
    // tests adding an empty string to a ConnectionWord
    t.checkExpect(this.apple.addWordToString(""), "Apple");
    // tests adding a non-empty string to a ConnectionWord
    t.checkExpect(this.banana.addWordToString("Fruit "), "Fruit Banana");
    // tests adding another non-empty string to a ConnectionWord
    t.checkExpect(this.cat.addWordToString("Dog "), "Dog Cat");
  }

  // tests the markDeselected method
  public void testMarkDeselected(Tester t) {
    this.initData();
    // tests marking a ConnectionWord that is selected as deselected
    this.apple.isSelected = true;
    this.apple.markDeselected();
    t.checkExpect(this.apple.isSelected, false);
    // tests marking another ConnectionWord that is selected as deselected
    this.banana.isSelected = true;
    this.banana.markDeselected();
    t.checkExpect(this.banana.isSelected, false);
    // tests marking a ConnectionWord that is already deselected as deselected
    this.cat.markDeselected();
    t.checkExpect(this.cat.isSelected, false);
  }

  // tests the clickOnTile method
  public void testClickOnTile(Tester t) {
    this.initData();
    ArrayList<ConnectionWord> selectedTiles = new ArrayList<ConnectionWord>();
    t.checkExpect(this.apple.isSelected, false);
    t.checkExpect(selectedTiles.contains(this.apple), false);
    // tests clicking on a ConnectionWord that is not selected
    this.apple.clickOnTile(this.centerApple, selectedTiles);
    t.checkExpect(this.apple.isSelected, true);
    t.checkExpect(selectedTiles.contains(this.apple), true);
    // tests clicking on a ConnectionWord that is selected
    this.apple.clickOnTile(this.centerApple, selectedTiles);
    t.checkExpect(this.apple.isSelected, false);
    t.checkExpect(selectedTiles.contains(this.apple), false);
    // tests clicking out of the bounds of the ConnectionWord
    this.apple.clickOnTile(this.outside, selectedTiles);
    t.checkExpect(this.apple.isSelected, false);
    t.checkExpect(selectedTiles.contains(this.apple), false);
    // tests clicking on a ConnectionWord that is already solved
    this.apple.markSolved();
    this.apple.clickOnTile(this.centerApple, selectedTiles);
    t.checkExpect(this.apple.isSelected, false);
    t.checkExpect(selectedTiles.contains(this.apple), false);
  }

  // tests the matches method
  public void testMatches(Tester t) {
    this.initData();
    // test an empty ConnectionGroup that does not match
    t.checkExpect(this.fruitGroup.matches(new ArrayList<ConnectionWord>()), false);
    // tests a ConnectionGroup that matches
    t.checkExpect(this.fruitGroup.matches(this.correctFruitGroup), true);
    // tests a ConnectionGroup that does not match
    t.checkExpect(this.fruitGroup.matches(this.threeFruitOneAnimal), false);
  }

  // tests the allInSameGroup method
  public void testAllInSameGroup(Tester t) {
    this.initData();
    // test an empty ConnectionGroup that does not match
    t.checkExpect(this.fruitGroup.allInSameGroup(new ArrayList<ConnectionWord>()), true);
    // tests a ConnectionGroup that matches
    t.checkExpect(this.fruitGroup.allInSameGroup(this.correctFruitGroup), true);
    // tests a ConnectionGroup that does not match
    t.checkExpect(this.fruitGroup.allInSameGroup(this.threeFruitOneAnimal), false);
  }

  // tests the ConnectionGroup markSolved method
  public void testConnectionGroupMarkSolved(Tester t) {
    this.initData();
    // tests marking a ConnectionGroup with all four tiles selected as solved
    for (ConnectionWord tile : this.correctFruitGroup) {
      tile.isSelected = true;
    }
    this.fruitGroup.markSolved(this.correctFruitGroup);
    for (ConnectionWord tile : this.correctFruitGroup) {
      t.checkExpect(tile.isSolved, true);
      t.checkExpect(tile.isSelected, false);
    }
    // tests marking a ConnectionGroup that is empty as solved
    ArrayList<ConnectionWord> empty = new ArrayList<ConnectionWord>();
    this.animalGroup.markSolved(empty);
    t.checkExpect(empty.size(), 0);
    t.checkExpect(this.animalGroup.tiles.size(), 4);
    // tests marking a ConnectionGroup with all four tiles unselectedas solved
    ArrayList<ConnectionWord> notSelected = new ArrayList<>(Arrays.asList(cat, dog));
    this.animalGroup.markSolved(notSelected);
    for (ConnectionWord tile : notSelected) {
      t.checkExpect(tile.isSolved, true);
      t.checkExpect(tile.isSelected, false);
    }
  }

  // tests the getGroupWords method
  public void testGetGroupWords(Tester t) {
    this.initData();
    // tests getting the group words of an empty ConnectionGroup
    String emptyGroupWords = this.emptyGroup.getGroupWords();
    t.checkExpect(emptyGroupWords, "");
    // tests getting the group words of a non-empty ConnectionGroup
    String fruitGroupWords = this.fruitGroup.getGroupWords();
    t.checkExpect(fruitGroupWords, "Apple, Banana, Grape, Orange");
    // tests getting the group words of another non-empty ConnectionGroup
    String animalGroupWords = this.animalGroup.getGroupWords();
    t.checkExpect(animalGroupWords, "Cat, Dog, Lion, Tiger");
  }

  // tests the addTilesToList method
  public void testAddTilesToList(Tester t) {
    this.initData();
    // tests adding tiles from an empty ConnectionGroup
    ArrayList<ConnectionWord> resultEmpty = new ArrayList<ConnectionWord>();
    this.emptyGroup.addTilesToList(resultEmpty);
    t.checkExpect(resultEmpty, new ArrayList<ConnectionWord>());
    // tests adding tiles from a non-empty ConnectionGroup
    ArrayList<ConnectionWord> resultFruits = new ArrayList<ConnectionWord>();
    this.fruitGroup.addTilesToList(resultFruits);
    t.checkExpect(resultFruits,
        new ArrayList<ConnectionWord>(
            Arrays.asList(new ConnectionWord("Apple", "FRUITS", GROUP_COLORS.get(0)),
                new ConnectionWord("Banana", "FRUITS", GROUP_COLORS.get(0)),
                new ConnectionWord("Grape", "FRUITS", GROUP_COLORS.get(0)),
                new ConnectionWord("Orange", "FRUITS", GROUP_COLORS.get(0)))));
    // tests adding tiles from another non-empty ConnectionGroup
    ArrayList<ConnectionWord> resultAnimals = new ArrayList<ConnectionWord>();
    this.animalGroup.addTilesToList(resultAnimals);
    t.checkExpect(resultAnimals,
        new ArrayList<ConnectionWord>(
            Arrays.asList(new ConnectionWord("Cat", "ANIMALS", GROUP_COLORS.get(1)),
                new ConnectionWord("Dog", "ANIMALS", GROUP_COLORS.get(1)),
                new ConnectionWord("Lion", "ANIMALS", GROUP_COLORS.get(1)),
                new ConnectionWord("Tiger", "ANIMALS", GROUP_COLORS.get(1)))));
  }

  // test the ConnectionGroup isInGroup method
  public void testConnectionGroupIsInGroup(Tester t) {
    this.initData();
    // tests if a ConnectionWord exists within an empty ConnectionGroup
    t.checkExpect(this.emptyGroup.isInGroup(this.cat), false);
    // tests if a ConnectionWord exists within a non-empty ConnectionGroup
    t.checkExpect(this.fruitGroup.isInGroup(this.apple), true);
    // tests if a ConnectionWord exists within a non-empty ConnectionGroup
    t.checkExpect(this.fruitGroup.isInGroup(this.cat), false);
  }

  // tests the initGame method
  public void testInitGame(Tester t) {
    this.initData();
    // tests a world scene that just started
    this.world.initGame();
    t.checkExpect(this.world.triesLeft, 4);
    t.checkExpect(this.world.won, false);
    t.checkExpect(this.world.gameOver, false);
    t.checkExpect(this.world.solvedGroups.isEmpty(), true);
    t.checkExpect(this.world.selectedTiles.isEmpty(), true);
    t.checkExpect(this.world.unsolvedTiles.size(), 16);
    t.checkExpect(this.world.tiles.size(), 16);
    // tests a world scene that was mid-game
    this.world.triesLeft = 1;
    this.world.won = true;
    this.world.gameOver = true;
    this.world.selectedTiles.add(this.fruitGroup.tiles.get(0));
    this.fruitGroup.markSolved(this.fruitGroup.tiles);
    this.world.solvedGroups.add(this.fruitGroup);
    this.world.unsolvedTiles.clear();
    this.world.initGame();
    t.checkExpect(this.world.triesLeft, 4);
    t.checkExpect(this.world.won, false);
    t.checkExpect(this.world.gameOver, false);
    t.checkExpect(this.world.solvedGroups.isEmpty(), true);
    t.checkExpect(this.world.selectedTiles.isEmpty(), true);
    t.checkExpect(this.world.unsolvedTiles.size(), 16);
    t.checkExpect(this.world.tiles.size(), 16);
  }

  // tests the chooseRandomGroupSet method
  public void testChooseRandomGroupSet(Tester t) {
    this.initData();
    // tests getting a random group set
    t.checkExpect(this.world.chooseRandomGroupSet(), ALL_SETS.get(2));
    // tests getting another random group set
    t.checkExpect(this.world.chooseRandomGroupSet(), ALL_SETS.get(2));
    // tests getting another random group set
    t.checkExpect(this.world.chooseRandomGroupSet(), ALL_SETS.get(1));
  }

  // tests the flattenTiles method
  public void testFlattenTiles(Tester t) {
    this.initData();
    // tests flatting an empty ArrayList of ConnectionGroup
    this.world.allGroups = new ArrayList<ConnectionGroup>();
    t.checkExpect(this.world.flattenTiles(), new ArrayList<ConnectionWord>());
    // tests flattening an ArrayList with four ConnectionGroups
    this.world.allGroups = ALL_SETS.get(0);
    t.checkExpect(this.world.flattenTiles(),
        new ArrayList<ConnectionWord>(
            Arrays.asList(new ConnectionWord("Apple", "FRUITS", GROUP_COLORS.get(0)),
                new ConnectionWord("Banana", "FRUITS", GROUP_COLORS.get(0)),
                new ConnectionWord("Grape", "FRUITS", GROUP_COLORS.get(0)),
                new ConnectionWord("Orange", "FRUITS", GROUP_COLORS.get(0)),
                new ConnectionWord("Red", "COLORS", GROUP_COLORS.get(1)),
                new ConnectionWord("Blue", "COLORS", GROUP_COLORS.get(1)),
                new ConnectionWord("Green", "COLORS", GROUP_COLORS.get(1)),
                new ConnectionWord("Yellow", "COLORS", GROUP_COLORS.get(1)),
                new ConnectionWord("Cat", "ANIMALS", GROUP_COLORS.get(2)),
                new ConnectionWord("Dog", "ANIMALS", GROUP_COLORS.get(2)),
                new ConnectionWord("Lion", "ANIMALS", GROUP_COLORS.get(2)),
                new ConnectionWord("Tiger", "ANIMALS", GROUP_COLORS.get(2)),
                new ConnectionWord("Math", "SCHOOL SUBJECTS", GROUP_COLORS.get(3)),
                new ConnectionWord("Science", "SCHOOL SUBJECTS", GROUP_COLORS.get(3)),
                new ConnectionWord("History", "SCHOOL SUBJECTS", GROUP_COLORS.get(3)),
                new ConnectionWord("English", "SCHOOL SUBJECTS", GROUP_COLORS.get(3)))));
  }

  // tests the getMessage method
  public void testGetMessage(Tester t) {
    this.initData();
    this.world.won = true;
    // tests with four tries left
    this.world.triesLeft = 4;
    t.checkExpect(this.world.getMessage(), "Perfect game! You didn't make a single mistake!");
    // tests with three tries left
    this.world.triesLeft = 3;
    t.checkExpect(this.world.getMessage(), "Great job! Only one mistake!");
    // tests with two tries left
    this.world.triesLeft = 2;
    t.checkExpect(this.world.getMessage(), "Nice! You solved it with two mistakes.");
    // tests with one try left
    this.world.triesLeft = 1;
    t.checkExpect(this.world.getMessage(), "Whew! You made it with only one try left.");
    // tests with zero tries left
    this.world.won = false;
    t.checkExpect(this.world.getMessage(), "You lost :(");
  }

  // tests the drawSolvedGroups method
  public void testDrawSolvedGroups(Tester t) {
    this.initData();
    // tests when there are no solved groups
    WorldScene emptyScene = new WorldScene(WIDTH, HEIGHT);
    int offset = this.world.drawSolvedGroups(emptyScene);
    t.checkExpect(offset, 140);
    // tests when there is one solved group
    this.fruitGroup.markSolved(this.fruitGroup.tiles);
    this.world.solvedGroups.add(this.fruitGroup);
    WorldScene actualScene1 = new WorldScene(WIDTH, HEIGHT);
    int offset1 = this.world.drawSolvedGroups(actualScene1);
    WorldScene expectedScene1 = new WorldScene(WIDTH, HEIGHT);
    expectedScene1.placeImageXY(this.fruitGroup.draw(), WIDTH / 2, 180);
    t.checkExpect(offset1, 220);
    t.checkExpect(actualScene1, expectedScene1);
    // tests with another solved group
    this.animalGroup.markSolved(this.animalGroup.tiles);
    this.world.solvedGroups.add(this.animalGroup);
    WorldScene actualScene2 = new WorldScene(WIDTH, HEIGHT);
    int offset2 = this.world.drawSolvedGroups(actualScene2);
    // tests with both groups placed
    WorldScene expectedScene2 = new WorldScene(WIDTH, HEIGHT);
    expectedScene2.placeImageXY(this.fruitGroup.draw(), WIDTH / 2, 180);
    expectedScene2.placeImageXY(this.animalGroup.draw(), WIDTH / 2, 260);
    t.checkExpect(offset2, 300); // 140 + 80 + 80
    t.checkExpect(actualScene2, expectedScene2);
  }

  // tests the getMistakeDots methodYe
  public void testGetMistakeDots(Tester t) {
    this.initData();
    // tests with four tries left
    this.world.triesLeft = 4;
    t.checkExpect(this.world.getMistakeDots(), "\u25CF \u25CF \u25CF \u25CF ");
    // tests with three tries left
    this.world.triesLeft = 3;
    t.checkExpect(this.world.getMistakeDots(), "\u25CF \u25CF \u25CF ");
    // tests with two tries left
    this.world.triesLeft = 2;
    t.checkExpect(this.world.getMistakeDots(), "\u25CF \u25CF ");
    // tests with one try left
    this.world.triesLeft = 1;
    t.checkExpect(this.world.getMistakeDots(), "\u25CF ");
    // tests with zero tries left
    this.world.triesLeft = 0;
    t.checkExpect(this.world.getMistakeDots(), "");
  }

  // tests the shuffleUnsolvedTiles method
  public void testShuffleUnsolvedTiles(Tester t) {
    this.initData();
    // tests shuffling the tiles
    ArrayList<ConnectionWord> before = new ArrayList<>(this.world.unsolvedTiles);
    this.world.shuffleUnsolvedTiles();
    ArrayList<ConnectionWord> after = new ArrayList<ConnectionWord>(
        Arrays.asList(new ConnectionWord("Apple", "FRUITS", GROUP_COLORS.get(0)),
            new ConnectionWord("Banana", "FRUITS", GROUP_COLORS.get(0)),
            new ConnectionWord("Grape", "FRUITS", GROUP_COLORS.get(0)),
            new ConnectionWord("Orange", "FRUITS", GROUP_COLORS.get(0)),
            new ConnectionWord("Red", "COLORS", GROUP_COLORS.get(1)),
            new ConnectionWord("Blue", "COLORS", GROUP_COLORS.get(1)),
            new ConnectionWord("Green", "COLORS", GROUP_COLORS.get(1)),
            new ConnectionWord("Yellow", "COLORS", GROUP_COLORS.get(1))));
    t.checkExpect(before, after);
    // tests shuffling the tiles again
    this.world.shuffleUnsolvedTiles();
    after = new ArrayList<ConnectionWord>(
        Arrays.asList(new ConnectionWord("Apple", "FRUITS", GROUP_COLORS.get(0)),
            new ConnectionWord("Banana", "FRUITS", GROUP_COLORS.get(0)),
            new ConnectionWord("Grape", "FRUITS", GROUP_COLORS.get(0)),
            new ConnectionWord("Orange", "FRUITS", GROUP_COLORS.get(0)),
            new ConnectionWord("Red", "COLORS", GROUP_COLORS.get(1)),
            new ConnectionWord("Blue", "COLORS", GROUP_COLORS.get(1)),
            new ConnectionWord("Green", "COLORS", GROUP_COLORS.get(1)),
            new ConnectionWord("Yellow", "COLORS", GROUP_COLORS.get(1))));
    t.checkExpect(before, after);
  }

  // tests the handleConnectionSubmit
  public void testHandleConnectionSubmit(Tester t) {
    this.initData();
    // tests with a valid submission
    this.world.selectedTiles.addAll(this.fruitGroup.tiles);
    this.world.handleConnectionSubmit();
    t.checkExpect(this.world.solvedGroups.size(), 1);
    t.checkExpect(this.world.unsolvedTiles.size(), 4);
    t.checkExpect(this.world.selectedTiles.isEmpty(), true);
    t.checkExpect(this.world.gameOver, false);
    t.checkExpect(this.world.won, false);
    // tests with a valid submission that ends the game
    this.world.selectedTiles.addAll(this.fruitGroup.tiles);
    this.world.unsolvedTiles.clear();
    this.world.handleConnectionSubmit();
    t.checkExpect(this.world.gameOver, true);
    t.checkExpect(this.world.won, true);
    // tests with an invalid submission (4 random, from different groups)
    this.initData();
    this.world.selectedTiles.add(this.fruitGroup.tiles.get(0));
    this.world.selectedTiles.add(this.colorGroup.tiles.get(0));
    this.world.selectedTiles.add(this.fruitGroup.tiles.get(1));
    this.world.selectedTiles.add(this.colorGroup.tiles.get(1));
    int triesBefore = this.world.triesLeft;
    this.world.handleConnectionSubmit();
    t.checkExpect(this.world.triesLeft, triesBefore - 1);
    t.checkExpect(this.world.unsolvedTiles.size(), 8);
    t.checkExpect(this.world.selectedTiles.isEmpty(), true);
    t.checkExpect(this.world.gameOver, false);
    t.checkExpect(this.world.won, false);
    // tests with an invalid submission (4 random, from different groups) that ends
    // the game
    this.world.selectedTiles.add(this.fruitGroup.tiles.get(0));
    this.world.selectedTiles.add(this.colorGroup.tiles.get(0));
    this.world.selectedTiles.add(this.fruitGroup.tiles.get(1));
    this.world.selectedTiles.add(this.colorGroup.tiles.get(1));
    this.world.triesLeft = 1;
    this.world.handleConnectionSubmit();
    t.checkExpect(this.world.gameOver, true);
    t.checkExpect(this.world.won, false);
  }

  // tests the isCloseGuessInSameGroup method
  public void testIsCloseGuessInSameGroup(Tester t) {
    this.initData();
    // testing selected tiles that are a close guess
    this.world.selectedTiles.add(this.fruitGroup.tiles.get(0));
    this.world.selectedTiles.add(this.fruitGroup.tiles.get(1));
    this.world.selectedTiles.add(this.fruitGroup.tiles.get(2));
    this.world.selectedTiles.add(this.colorGroup.tiles.get(0));
    t.checkExpect(this.world.isCloseGuessInSameGroup(), true);
    // testing selected tiles that are all four correct
    this.world.selectedTiles.clear();
    this.world.selectedTiles.add(this.fruitGroup.tiles.get(0));
    this.world.selectedTiles.add(this.fruitGroup.tiles.get(1));
    this.world.selectedTiles.add(this.fruitGroup.tiles.get(2));
    this.world.selectedTiles.add(this.fruitGroup.tiles.get(3));
    t.checkExpect(this.world.isCloseGuessInSameGroup(), false);
    // testing another set of selected tiles that are not a close guess or all
    // correct
    this.world.selectedTiles.clear();
    this.world.selectedTiles.add(this.fruitGroup.tiles.get(0));
    this.world.selectedTiles.add(this.fruitGroup.tiles.get(1));
    this.world.selectedTiles.add(this.colorGroup.tiles.get(0));
    this.world.selectedTiles.add(this.colorGroup.tiles.get(1));
    t.checkExpect(this.world.isCloseGuessInSameGroup(), false);
  }

  // tests the findMatchingGroup method
  public void testFindMatchingGroup(Tester t) {
    this.initData();
    // tests successfully finding a matching group from four selected tiles
    this.world.selectedTiles.addAll(this.fruitGroup.tiles);
    t.checkExpect(this.world.findMatchingGroup(), this.fruitGroup);
    // tests unsuccessfully finding a matching group from four selected tiles
    this.world.selectedTiles.clear();
    this.world.selectedTiles.add(this.fruitGroup.tiles.get(0));
    this.world.selectedTiles.add(this.fruitGroup.tiles.get(1));
    this.world.selectedTiles.add(this.colorGroup.tiles.get(0));
    this.world.selectedTiles.add(this.colorGroup.tiles.get(1));
    t.checkExpect(this.world.findMatchingGroup(), null);
    // tests unsuccessfully finding a matching group from two selected tiles
    this.world.selectedTiles.clear();
    this.world.selectedTiles.add(this.fruitGroup.tiles.get(0));
    this.world.selectedTiles.add(this.colorGroup.tiles.get(0));
    t.checkExpect(this.world.findMatchingGroup(), null);
  }

  // tests the deselectTiles method
  public void testDeselectTiles(Tester t) {
    this.initData();
    // tests deselecting tiles are not selected
    this.world.deselectTiles();
    for (ConnectionWord tile : this.fruitGroup.tiles) {
      t.checkExpect(tile.isSelected, false);
    }
    t.checkExpect(this.world.selectedTiles.isEmpty(), true);
    // tests deselecting tiles are all selected
    for (ConnectionWord tile : this.fruitGroup.tiles) {
      tile.isSelected = true;
    }
    this.world.selectedTiles.addAll(this.fruitGroup.tiles);
    this.world.deselectTiles();
    for (ConnectionWord tile : this.fruitGroup.tiles) {
      t.checkExpect(tile.isSelected, false);
    }
    t.checkExpect(this.world.selectedTiles.isEmpty(), true);
  }

  // tests the checkWin method
  public void testCheckWin(Tester t) {
    this.initData();
    // tests checking win on a game that is won and over
    this.world.unsolvedTiles.clear();
    this.world.checkWin();
    t.checkExpect(this.world.won, true);
    t.checkExpect(this.world.gameOver, true);
    // tests checking win on a game that is not won and over
    this.world.initGame();
    this.world.checkWin();
    t.checkExpect(this.world.won, false);
    t.checkExpect(this.world.gameOver, false);
    // tests checking win on a game that is won and over
    this.world.won = true;
    this.world.gameOver = false;
    this.world.checkWin();
    t.checkExpect(this.world.won, true);
    t.checkExpect(this.world.gameOver, false);
  }

  // tests the makeScene method
  public void testMakeScene(Tester t) {
    this.initData();
    // tests when there is no solved groups but game is not over
    this.world.initGame();
    this.world.unsolvedTiles.clear();
    WorldScene expectedInitial = new WorldScene(WIDTH, HEIGHT);
    expectedInitial.placeImageXY(new RectangleImage(WIDTH, HEIGHT, "solid", Color.WHITE), WIDTH / 2,
        HEIGHT / 2);
    expectedInitial.placeImageXY(new TextImage("Connections", 32, TEXT_COLOR), WIDTH / 2, 40);
    expectedInitial.placeImageXY(new TextImage("Create groups of four!", 20, TEXT_COLOR), WIDTH / 2,
        90);
    expectedInitial.placeImageXY(new TextImage("Press ENTER to submit connection", 20, TEXT_COLOR),
        WIDTH / 2, 130 + 5 * (WORD_BOX_HEIGHT + TILE_VERTICAL_SPACING));
    expectedInitial.placeImageXY(
        new TextImage("Mistakes remaining: \u25CF \u25CF \u25CF \u25CF ", 20, TEXT_COLOR),
        WIDTH / 2, 130 + 5 * (WORD_BOX_HEIGHT + TILE_VERTICAL_SPACING) + 35);
    WorldScene actualInitial = this.world.makeScene();
    t.checkExpect(actualInitial, expectedInitial);
    // tests when the game is won
    this.world.unsolvedTiles.clear();
    this.world.won = true;
    this.world.gameOver = true;
    this.world.triesLeft = 3;
    WorldScene expectedWin = new WorldScene(WIDTH, HEIGHT);
    expectedWin.placeImageXY(new RectangleImage(WIDTH, HEIGHT, "solid", Color.WHITE), WIDTH / 2,
        HEIGHT / 2);
    expectedWin.placeImageXY(new TextImage("Connections", 32, TEXT_COLOR), WIDTH / 2, 40);
    expectedWin.placeImageXY(new TextImage("Create groups of four!", 20, TEXT_COLOR), WIDTH / 2,
        90);
    expectedWin.placeImageXY(new TextImage("Great job! Only one mistake!", 20, TEXT_COLOR),
        WIDTH / 2, 130 + 5 * (WORD_BOX_HEIGHT + TILE_VERTICAL_SPACING));
    expectedWin.placeImageXY(new TextImage("Press R to start a new game", 20, TEXT_COLOR),
        WIDTH / 2, 130 + 5 * (WORD_BOX_HEIGHT + TILE_VERTICAL_SPACING) + 35);
    WorldScene actualWin = this.world.makeScene();
    t.checkExpect(actualWin, expectedWin);
    // tests when the game is lost
    this.world.won = false;
    this.world.triesLeft = 0;
    this.world.gameOver = true;
    WorldScene expectedLoss = new WorldScene(WIDTH, HEIGHT);
    expectedLoss.placeImageXY(new RectangleImage(WIDTH, HEIGHT, "solid", Color.WHITE), WIDTH / 2,
        HEIGHT / 2);
    expectedLoss.placeImageXY(new TextImage("Connections", 32, TEXT_COLOR), WIDTH / 2, 40);
    expectedLoss.placeImageXY(new TextImage("Create groups of four!", 20, TEXT_COLOR), WIDTH / 2,
        90);
    expectedLoss.placeImageXY(new TextImage("You lost :(", 20, TEXT_COLOR), WIDTH / 2,
        130 + 5 * (WORD_BOX_HEIGHT + TILE_VERTICAL_SPACING));
    expectedLoss.placeImageXY(new TextImage("Press R to start a new game", 20, TEXT_COLOR),
        WIDTH / 2, 130 + 5 * (WORD_BOX_HEIGHT + TILE_VERTICAL_SPACING) + 35);
    WorldScene actualLoss = this.world.makeScene();
    t.checkExpect(actualLoss, expectedLoss);
  }

  // tests the onKeyEvent method
  public void testOnKeyEvent(Tester t) {
    this.initData();
    // tests pressing "r"
    this.world.won = true;
    this.world.triesLeft = 0;
    this.world.selectedTiles.add(this.apple);
    this.world.onKeyEvent("r");
    t.checkExpect(this.world.won, false);
    t.checkExpect(this.world.triesLeft, 4);
    t.checkExpect(this.world.selectedTiles.isEmpty(), true);
    t.checkExpect(this.world.gameOver, false);
    // tests pressing "d"
    this.apple.isSelected = true;
    this.banana.isSelected = true;
    this.world.selectedTiles.add(this.apple);
    this.world.selectedTiles.add(this.banana);
    this.world.onKeyEvent("d");
    t.checkExpect(this.apple.isSelected, false);
    t.checkExpect(this.banana.isSelected, false);
    t.checkExpect(this.world.selectedTiles.isEmpty(), true);
    // tests pressing "s"
    this.world.initGame();
    ArrayList<ConnectionWord> beforeShuffle = new ArrayList<>(this.world.unsolvedTiles);
    this.world.onKeyEvent("s");
    ArrayList<ConnectionWord> afterShuffle = new ArrayList<>(this.world.unsolvedTiles);
    t.checkExpect(beforeShuffle.equals(afterShuffle), false);
    // tests pressing "enter" when game is not over
    this.world.allGroups = new ArrayList<>(Arrays.asList(this.fruitGroup));
    this.world.tiles = this.fruitGroup.tiles;
    this.world.unsolvedTiles = new ArrayList<>(this.fruitGroup.tiles);
    this.world.selectedTiles = new ArrayList<>(this.fruitGroup.tiles);
    this.world.triesLeft = 4;
    this.world.gameOver = false;
    this.world.onKeyEvent("enter");
    t.checkExpect(this.world.solvedGroups.contains(this.fruitGroup), true);
    t.checkExpect(this.world.selectedTiles.isEmpty(), true);
    // tests pressing "enter" when game is over
    this.world.gameOver = true;
    ArrayList<ConnectionWord> expected = new ArrayList<>(this.world.selectedTiles);
    this.world.onKeyEvent("enter");
    t.checkExpect(this.world.selectedTiles, expected);
  }

  // tests the onMouseClicked method
  public void testOnMouseClicked(Tester t) {
    this.initData();
    this.apple.setPosition(100, 100);
    this.world.tiles = new ArrayList<>(Arrays.asList(this.apple));
    this.world.unsolvedTiles = new ArrayList<>(Arrays.asList(this.apple));
    this.world.selectedTiles = new ArrayList<>();
    this.world.gameOver = false;
    Posn insideApple = new Posn(100, 100);
    Posn outside = new Posn(300, 300);
    // tests clicking inside ConnectionWord
    this.world.onMouseClicked(insideApple);
    t.checkExpect(this.apple.isSelected, true);
    t.checkExpect(this.world.selectedTiles.contains(this.apple), true);
    // tests clicking inside ConnectionWord again
    this.world.onMouseClicked(insideApple);
    t.checkExpect(this.apple.isSelected, false);
    t.checkExpect(this.world.selectedTiles.contains(this.apple), false);
    // tests clicking outside ConnectionWord
    this.world.onMouseClicked(outside);
    t.checkExpect(this.apple.isSelected, false);
    t.checkExpect(this.world.selectedTiles.isEmpty(), true);
    // tests clicking solved ConnectionWord
    this.apple.markSolved();
    this.world.onMouseClicked(insideApple);
    t.checkExpect(this.apple.isSelected, false);
    t.checkExpect(this.world.selectedTiles.isEmpty(), true);
    // tests clicking unsolved ConnectionWord once game is over
    this.apple.isSolved = false;
    this.world.gameOver = true;
    this.world.onMouseClicked(insideApple);
    t.checkExpect(this.apple.isSelected, false);
    t.checkExpect(this.world.selectedTiles.isEmpty(), true);
  }

  // tests the onTick method
  public void testOnTick(Tester t) {
    this.initData();
    // tests when isCloseGuess is false
    this.world.isCloseGuess = false;
    this.world.closeGuessTimer = 0;
    this.world.onTick();
    t.checkExpect(this.world.isCloseGuess, false);
    t.checkExpect(this.world.closeGuessTimer, 0);
    // tests when isCloseGuess is true, timer is under 100
    this.world.isCloseGuess = true;
    this.world.closeGuessTimer = 42;
    this.world.onTick();
    t.checkExpect(this.world.isCloseGuess, true);
    t.checkExpect(this.world.closeGuessTimer, 43);
    // tests when isCloseGuess is true, timer at 99
    this.world.closeGuessTimer = 99;
    this.world.onTick();
    t.checkExpect(this.world.isCloseGuess, false);
    t.checkExpect(this.world.closeGuessTimer, 0);
    // tests when isCloseGuess is already false
    this.world.onTick();
    t.checkExpect(this.world.isCloseGuess, false);
    t.checkExpect(this.world.closeGuessTimer, 0);
  }
}