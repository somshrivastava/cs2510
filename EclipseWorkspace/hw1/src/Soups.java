interface ISoup { }

class Broth implements ISoup {
  String type;
  
  Broth(String type) {
    this.type = type;
  }
  
  /* TEMPLATE:
     Fields:
     ... this.type ...         -- String
  */
}

class Ingredient implements ISoup {
  ISoup more;
  String name;
  
  Ingredient(ISoup more, String name) {
    this.more = more;
    this.name = name;
  }
  
  /* TEMPLATE:
     Fields:
     ... this.more ...         -- ISoup
     ... this.name ...         -- String
  */
}

class ExamplesSoup {
  ISoup yummy = new Ingredient(
      new Ingredient(
          new Ingredient(
              new Broth("chicken"), "carrots"), "celery"), "noodles");
  
  ISoup noThankYou = new Ingredient(
      new Ingredient(
          new Ingredient(
              new Broth("vanilla"), "horseradish"), "hot dogs"), "plum sauce");
}
