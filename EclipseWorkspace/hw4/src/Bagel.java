import tester.Tester;

// class for a bagel recipe
class BagelRecipe {
  double flour;
  double water;
  double yeast;
  double salt;
  double malt;
  
  /*
   * TEMPLATE: 
   * Fields: 
   * ... this.flour ...            -- Double
   * ... this.water ...            -- Double
   * ... this.yeast ...            -- Double
   * ... this.salt ...             -- Double
   * ... this.malt ...             -- Double
   * 
   * Methods: 
   * ... this.sameRecipe(BagelRecipe other) ...    -- boolean
   * 
   * Methods on fields:
  */

  // private method to validate recipe and throw exception if it is not
  private void validateRecipe(double flour, double water, double yeast, double salt, double malt) {
    if (flour != water) {
      throw new IllegalArgumentException("Flour and water must be equal in weight");
    }
    if (yeast != malt) {
      throw new IllegalArgumentException("Yeast and malt must be equal in weight");
    }
    if (Math.abs((salt + yeast) - (flour / 20.0)) > 0.0001) { 
      throw new IllegalArgumentException("Salt + yeast must be 1/20th of the flour weight");
    }
  }

  // main constructor enforcing perfect bagel ratios
  BagelRecipe(double flour, double water, double yeast, double salt, double malt) {
    validateRecipe(flour, water, yeast, salt, malt);
    this.flour = flour;
    this.water = water;
    this.yeast = yeast;
    this.salt = salt;
    this.malt = malt;
  }

  // constructor with only flour and yeast
  BagelRecipe(double flour, double yeast) {
    this(flour, flour, yeast, (flour / 20) - yeast, yeast);
  }

  // constructor that takes in volumes and converts them to weights
  BagelRecipe(double flourCups, double yeastTsp, double saltTsp) {
    this.flour = flourCups * 4.25;
    this.yeast = (yeastTsp / 48) * 5;
    this.salt = (saltTsp / 48) * 10;
    this.malt = this.yeast;
    this.water = this.flour;
    validateRecipe(this.flour, this.water, this.yeast, this.salt, this.malt);
  }

  // returns true if the same ingredients have the same weights to within 0.001 ounces
  public boolean sameRecipe(BagelRecipe other) {
    return Math.abs(this.flour - other.flour) < 0.001 && Math.abs(this.water - other.water) < 0.001
        && Math.abs(this.yeast - other.yeast) < 0.001 && Math.abs(this.salt - other.salt) < 0.001
        && Math.abs(this.malt - other.malt) < 0.001;
  }
}

// examples and tests for BagelRecipes class
class ExamplesBagels {
  BagelRecipe bagelRecipe1 = new BagelRecipe(20.0, 20.0, 1.0, 0.0, 1.0);
  BagelRecipe bagelRecipe2 = new BagelRecipe(10.0, 0.5);
  BagelRecipe bagelRecipe3 = new BagelRecipe(20.0, 13.6, 13.6);

  // test for same recipe
  boolean testSameRecipe(Tester t) {
    return t.checkExpect(bagelRecipe1.sameRecipe(new BagelRecipe(20.0, 20.0, 1.0, 0.0, 1.0)), true)
        // test a bagel recipe with the main constructor
        && t.checkExpect(bagelRecipe2.sameRecipe(new BagelRecipe(10.0, 10.0, 0.5, 0.0, 0.5)), true)
        // test a bagel recipe with only flour and yeast
        && t.checkExpect(bagelRecipe3.sameRecipe(new BagelRecipe(20 * 4.25, 20 * 4.25,
            (13.6 / 48) * 5, (13.6 / 48) * 10, (13.6 / 48) * 5)), true)
        // test a bagel recipe with two different volumes
        && t.checkExpect(bagelRecipe1.sameRecipe(bagelRecipe2), false)
        // test a bagel recipe that isn't the same as the other bagel recipe
        && t.checkExpect(bagelRecipe2.sameRecipe(bagelRecipe3), false);
    // test another bagel recipe that isn't the same as the other bagel recipe
  }

  // test for invalid recipes
  boolean testInvalidBagelRecipes(Tester t) {
    return t.checkConstructorException(
        new IllegalArgumentException("Flour and water must be equal in weight"), "BagelRecipe",
        10.0, 9.0, 0.5, 0.5, 0.5)
        // test an invalid bagel recipe where flour and water are not equal in weight
        && t.checkConstructorException(
            new IllegalArgumentException("Yeast and malt must be equal in weight"), "BagelRecipe",
            10.0, 10.0, 0.5, 0.5, 0.3)
        // test an invalid bagel recipe where yeast and malt are not equal in weight
        && t.checkConstructorException(
            new IllegalArgumentException("Salt + yeast must be 1/20th of the flour weight"),
            "BagelRecipe", 10.0, 10.0, 0.3, 0.3, 0.3);
    // test an invalid bagel recipe where salt and yeast are not equal to 1/20th of
    // the flour weight
  }
}