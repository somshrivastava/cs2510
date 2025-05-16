import tester.*;
import java.awt.Color;
import javalib.worldimages.*;

// represents a group of tasks
class Group {
    String title;
    ILoTask tasks;

    Group(String t, ILoTask tasks) {
        this.title = t;
        this.tasks = tasks;
    }
    
    /*  TEMPLATE:
        Fields:
        ... this.title ...         -- String
        ... this.tasks ...         -- ILoTask
      
        Methods:
        ... this.rotate() ...      -- Group
        ... this.flip() ...        -- Group
        ... this.draw() ...        -- WorldImage
        
        Methods on fields:
        ... this.tasks.draw() ...          -- WorldImage
        ... this.tasks.rotate() ...        -- ILoTask
        ... this.tasks.flip() ...          -- ILoTask
        ... this.tasks.append(Task t) ...  -- ILoTask
     */

    // rotate the list of tasks in this group
    Group rotate() {
        return new Group(this.title, this.tasks.rotate());
    }

    // flip the first task to done or not done
    Group flip() {
        return new Group(this.title, this.tasks.flip());
    }

    // draw the current state of this group of tasks
    WorldImage draw() {
        return this.tasks.draw();
    }
}

// represents a task to be checked off a to-do list
class Task {
    String description;
    boolean isDone;

    Task(String d, boolean isDone) {
        this.description = d;
        this.isDone = isDone;
    }
    
    /*  TEMPLATE:
        Fields:
        ... this.description ...         -- String
        ... this.isDone ...              -- boolean
      
        Methods:
        ... this.flip() ...              -- Task
        ... this.draw() ...              -- WorldImage
        ... this.drawCheckbox() ...      -- WorldImage
     */ 

    // flip the completeness of this task
    Task flip() {
        return new Task(this.description, !this.isDone);
    }

    // draw this task as text
    WorldImage draw() {
        WorldImage bg = new RectangleImage(300, 200, "solid", Color.cyan);
        bg = new OverlayImage(new TextImage(this.description, 20, Color.black), bg);
        bg = new OverlayOffsetImage(this.drawCheckbox(), 0, -60, bg);
        return bg;
    }

    // draw the check box for this task
    WorldImage drawCheckbox() {
        if (this.isDone) {
            return new RectangleImage(20, 20, "solid", Color.black);
        } else {
            return new RectangleImage(20, 20, "outline", Color.black);
        }
    }
}

// represents a list of tasks
interface ILoTask {
  
    /*  TEMPLATE:
        Methods:
        ... this.draw() ...              -- WorldImage
        ... this.flip() ...              -- ILoTask
        ... this.rotate() ...            -- ILoTask
        ... this.append(Task t) ...      -- ILoTask
    */ 
  
    // draw the first task's description as text or "No tasks to do" for this ILoTask
    WorldImage draw();

    // rotate the list of tasks
    ILoTask rotate();

    // flip the first task in the list
    ILoTask flip();

    // append a task to the end of the list
    ILoTask append(Task t);
}

// represents an empty list of tasks
class MtLoTask implements ILoTask {
    /*  TEMPLATE:
        Methods:
        ... this.draw() ...              -- WorldImage
        ... this.flip() ...              -- ILoTask
        ... this.rotate() ...            -- ILoTask
        ... this.append(Task t) ...      -- ILoTask
    */
  
    // render that there are no tasks to do since this list is empty
    public WorldImage draw() {
        return new TextImage("No tasks to do", 30, Color.black);
    }

    public ILoTask rotate() {
        return this; 
    }

    public ILoTask flip() {
        return this;
    }

    public ILoTask append(Task t) {
        return new ConsLoTask(t, this);
    }
}

// represents a non-empty list of tasks
class ConsLoTask implements ILoTask {
    Task first;
    ILoTask rest;

    ConsLoTask(Task first, ILoTask rest) {
        this.first = first;
        this.rest = rest;
    }
    
    /*  TEMPLATE:
        Fields:
        ... this.first ...             -- Task
        ... this.rest ...              -- ILoTask
      
        Methods:
        ... this.draw() ...              -- WorldImage
        ... this.flip() ...              -- ILoTask
        ... this.rotate() ...            -- ILoTask
        ... this.append(Task t) ...      -- ILoTask
        
        Methods on fields:
        ... this.first.flip() ...              -- Task
        ... this.first.draw() ...              -- WorldImage
        ... this.first.drawCheckbox() ...      -- WorldImage
        ... this.rest.draw() ...               -- WorldImage
        ... this.rest.flip() ...               -- ILoTask
        ... this.rest.rotate() ...             -- ILoTask
        ... this.rest.append(Task t) ...       -- ILoTask
     */ 

    public WorldImage draw() {
        return this.first.draw();
    }

    public ILoTask rotate() {
        return this.rest.append(this.first);
    }

    public ILoTask flip() {
        return new ConsLoTask(this.first.flip(), this.rest);
    }

    public ILoTask append(Task t) {
        return new ConsLoTask(this.first, this.rest.append(t));
    }
}

class Examples {
  // example Tasks
  Task clean = new Task("Clean room", true);
  Task laundry = new Task("Do a load of laundry", false);
  Task hw = new Task("Work on fundies 2 assignment", false);
  Task sleep = new Task("Sleep 8 hours", true);
  Task eat = new Task("Have breakfast", false);

  // example Lists of Tasks
  ILoTask mt = new MtLoTask(); 
  ILoTask cleaning = new ConsLoTask(this.clean, new ConsLoTask(this.laundry, this.mt));
  ILoTask health = new ConsLoTask(this.sleep, new ConsLoTask(this.eat, this.mt));
  ILoTask singleTask = new ConsLoTask(this.hw, this.mt);

  // example Groups
  Group chores = new Group("Chores", this.cleaning);
  Group wellness = new Group("Wellness", this.health);
  Group singleGroup = new Group("Single Task", this.singleTask);
  Group emptyGroup = new Group("Empty", this.mt);

  // test rotate method for Group
  boolean testGroupRotate(Tester t) {
      Group rotatedChores = new Group("Chores", new ConsLoTask(this.laundry, new ConsLoTask(this.clean, this.mt)));
      Group rotatedSingle = new Group("Single Task", this.singleTask);
      return t.checkExpect(this.chores.rotate(), rotatedChores)
          && t.checkExpect(this.singleGroup.rotate(), rotatedSingle)
          && t.checkExpect(this.emptyGroup.rotate(), this.emptyGroup);
  }

  // test flip method for Group
  boolean testGroupFlip(Tester t) {
      Group flippedChores = new Group("Chores", new ConsLoTask(new Task("Clean room", false), new ConsLoTask(this.laundry, this.mt)));
      Group flippedSingle = new Group("Single Task", new ConsLoTask(new Task("Work on fundies 2 assignment", true), this.mt));
      return t.checkExpect(this.chores.flip(), flippedChores)
          && t.checkExpect(this.singleGroup.flip(), flippedSingle)
          && t.checkExpect(this.emptyGroup.flip(), this.emptyGroup);
  }

  // test flip method for Task
  boolean testTaskFlip(Tester t) {
      Task flippedClean = new Task("Clean room", false);
      Task flippedLaundry = new Task("Do a load of laundry", true);
      Task flippedHW = new Task("Work on fundies 2 assignment", true);
      return t.checkExpect(this.clean.flip(), flippedClean)
          && t.checkExpect(this.laundry.flip(), flippedLaundry)
          && t.checkExpect(this.hw.flip(), flippedHW);
  }
  
  // calls big bang to launch the task manager
  boolean testBigBang(Tester t) {
      TaskWorld world = new TaskWorld(this.chores);
      int worldWidth = 600;
      int worldHeight = 400;
      double tickRate = 0.1;
      return world.bigBang(worldWidth, worldHeight, tickRate);
  }
}


