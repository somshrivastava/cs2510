interface IResource {
}

class Monster implements IResource {
  String name;
  int hp;
  int attack;

  Monster(String name, int hp, int attack) {
    this.name = name;
    this.hp = hp;
    this.attack = attack;
  }
  
  /* TEMPLATE:
  Fields:
    ... this.name ...         -- String
    ... this.hp ...           -- int
    ... this.attack ...       -- int
  */
}

class Fusion implements IResource {
  String name;
  Monster monster1;
  Monster monster2;

  Fusion(String name, Monster monster1, Monster monster2) {
    this.name = name;
    this.monster1 = monster1;
    this.monster2 = monster2;
  }
  
  /* TEMPLATE:
     Fields:
     ... this.name ...         -- String
     ... this.monster1 ...     -- Monster
     ... this.monster2 ...     -- Monster
  */
}

class Trap implements IResource {
  String description;
  boolean continuous;

  Trap(String description, boolean continuous) {
    this.description = description;
    this.continuous = continuous;
  }
  
  /* TEMPLATE:
     Fields:
     ... this.description ...  -- String
     ... this.continuous ...   -- boolean
  */
}

interface IAction {
}

class Attack implements IAction {
  Monster attacker;
  Monster defender;

  Attack(Monster attacker, Monster defender) {
    this.attacker = attacker;
    this.defender = defender;
  }
  
  /* TEMPLATE:
     Fields:
     ... this.attacker ...     -- Monster
     ... this.defender ...     -- Monster
  */
}

class Activate implements IAction {
  Trap trap;
  IResource target;

  Activate(Trap trap, IResource target) {
    this.trap = trap;
    this.target = target;
  }
  
  /* TEMPLATE:
     Fields:
     ... this.trap ...         -- Trap
     ... this.target ...       -- IResource
  */
}

class ExamplesGame {
  Monster kuriboh = new Monster("Kuriboh", 200, 100);
  Monster jinzo = new Monster("Jinzo", 500, 400);
  Fusion kurizo = new Fusion("Kurizo", kuriboh, jinzo);
  Trap trapHole = new Trap("Kills a monster", false);
  Monster kurukoro = new Monster("Kurukoro", 3000, 2500);
  Monster monzario = new Monster("Monzario", 2400, 2100);

  Attack attack1 = new Attack(jinzo, kuriboh);
  Attack attack2 = new Attack(kurukoro, monzario);

  Activate activate1 = new Activate(trapHole, jinzo);
  Activate activate2 = new Activate(trapHole, kurukoro);
}

/*
 * Answer: Group all three under the same interface would make it difficult to enforce restrictions
 * like ensuring that attacks are only on fusions and monsters. Because traps are under
 * the IResource interface as well, technically we can attack a trap when that should not be
 * possible. A solution to this problem would be to create an interface that groups fusions
 * monsters, separate from the Trap. This will allow us to then enforce such restrictions
 * mentioned above.
 * */