import tester.*;

class Switcheroo {
  int value;

  Switcheroo(int value) {
    this.value = value;
  }

  Switcheroo choose(Switcheroo that, Switcheroo other) {
    if (this.value < that.value) {
      return other.choose(this, that);
    }
    else if (that.value > other.value) {
      return that.choose(other, new Switcheroo(this.value + 1));
    }
    else {
      return new Switcheroo(this.value * 10);
    }
  }
}

class ExampleSwitcheroo {
  Switcheroo s1 = new Switcheroo(20);
  Switcheroo s2 = new Switcheroo(16);
  Switcheroo s3 = new Switcheroo(2510);
  
  boolean testSwitcheroo(Tester t) {
    return t.checkExpect(this.s3.choose(this.s1, this.s2), new Switcheroo(200));
    
  }
}