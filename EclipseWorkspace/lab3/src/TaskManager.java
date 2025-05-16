import javalib.funworld.*;
import javalib.worldimages.*;

// represents a World program for a group of tasks
class TaskWorld extends World {
  Group taskGroup;

  TaskWorld(Group g) {
    this.taskGroup = g;
  }

  // renders the state of this TaskWorld
  public WorldScene makeScene() {
    WorldScene ws = new WorldScene(600, 400);
    ws = ws.placeImageXY(new TextImage(this.taskGroup.title, 30, java.awt.Color.black), 300, 50);
    return ws.placeImageXY(this.taskGroup.draw(), 300, 200);
  }

  // handles key events for this TaskWorld
  public World onKeyEvent(String key) {
    if (key.equals("right")) {
      return new TaskWorld(this.taskGroup.rotate());
    }
    else if (key.equals(" ")) {
      return new TaskWorld(this.taskGroup.flip());
    }
    else {
      return this;
    }
  }
}
