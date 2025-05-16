import tester.*;

// NOTE: Templates and purpose statements left out: You should fill them in yourself!
// represents a game piece
interface IGamePiece {
  int getValue();
  
  /*
   * TEMPLATE: 
   * Fields: 
   * 
   * Methods: 
   * this.getValue() -> int
   * 
   * Methods on fields:
  */
}

// represents a base tile
class BaseTile implements IGamePiece {
  int value;

  BaseTile(int value) {
    this.value = value;
  }
  
  /*
   * TEMPLATE: 
   * Fields: 
   * this.value -> int
   * 
   * Methods: 
   * this.getValue() -> int
   * this.merge(BaseTile gamePiece) -> MergeTile   
   * 
   * Methods on fields:
  */

  // returns the value of the tile
  public int getValue() {
    return this.value;
  }

  // merges this tile with a given tile
  public MergeTile merge(BaseTile gamePiece) {
    return new MergeTile(this, gamePiece);
  }
}

// represents a merged tile
class MergeTile implements IGamePiece {
  IGamePiece piece1, piece2;

  MergeTile(IGamePiece piece1, IGamePiece piece2) {
    this.piece1 = piece1;
    this.piece2 = piece2;
  }
  
  /*
   * TEMPLATE: 
   * Fields: 
   * this.piece1 -> IGamePiece
   * this.piece2 -> IGamePiece
   * 
   * Methods: 
   * this.getValue() -> int
   * this.isValid() -> boolean   
   * 
   * Methods on fields:
  */

  // returns value of merged tile
  public int getValue() {
    return this.piece1.getValue() + this.piece2.getValue();
  }

  // returns whether merged tile is valid with 2048 rules
  public boolean isValid() {
    return this.piece1.getValue() == this.piece2.getValue();
  }
}

// examples and tests for GamePiece
class ExamplesGamePiece {
  IGamePiece four = new MergeTile(new BaseTile(2), new BaseTile(2));

  // tests getValue method
  boolean testGetValue(Tester t) {
    return t.checkExpect(four.getValue(), 4)
        && t.checkEquivalent(, null, null);
  }
}