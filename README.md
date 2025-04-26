# 335-Final-Project: Yahtzee Game
### Author: Bryan Frank, Ethan Alter, Frank Chen, Muyang Chen

## Overview
This project implements a Yahtzee game in Java.
The system models Players, Dice, a Cup to roll the dice, Scoring, and GameBoard management. 
There are two types of players:
- **Human**
- **Computer** (AI, play with human player based on difficulty: easy or hard)

---

### Classes:

- **`Dice.java`**
  Represent a single die.
  - Can roll to get a random value 1-6.
  - Used an enum `Value` internally to represent die faces.

- **`Value.java`**
  Enum representing possible die faces: ONE, TWO, THREE, FOUR, FIVE, SIX.

- **`Cup.java`**
  Holds and manages multiple `Dice`.  
  - **inCup**: Dice still active to roll.  
  - **outCup**: Dice kept (not rolled anymore).  
  - Can roll all active dice, move dice out of the cup, or reset them.

- **`Score.java`**  
  Calculates possible scores for a given dice roll.  
  - Evaluates all Yahtzee categories (e.g., Full House, Straights, Yahtzee, Chance).
  - Returns a map of category -> score.

- **`Scoreboard.java`**  
  Manages a player's current scorecard.
  - Can assign scores to available categories.
  - Tracks filled and remaining categories.
  - Can print the current scoreboard.

- **`Player.java`**  
  Represents a human or computer player.  
  - Stores a name, a history of game scores, and flags for dice selection.
  - Tracks how many times a player has rolled in a turn (max 3).

- **`Computer.java`**  
  Represents a computer-controlled(AI) player.
  - Has a difficulty setting (`EASY` or `HARD`).
  - Chooses dice and scoring strategies based on difficulty.
  - `HARD` tries to maximize points or find special combos (e.g., Full House).

- **`PlayerLibrary.java`**  
  Keeps a record of all players and their historical scores across games.
  - Supports adding, removing, ranking players.

- **`GameBoard.java`**  
  Manages the order of players, keeps track of whose turn it is.
  - Determines playing order: human players roll to decide, then computers go after.

---

- **Error Handling:**  
  - Code checks for bounds on dice selection.
  - Scoreboard disallows overwriting a category that has already been filled.

---

## Summary of Files
| File              | Purpose |
|-------------------|---------|
| `Dice.java`        | Single dice logic |
| `Value.java`       | Enum for dice values |
| `Cup.java`         | Holds multiple dice and manages rolls |
| `Score.java`       | Calculates possible scores from dice |
| `Scoreboard.java`  | Tracks scores across categories |
| `Player.java`      | Represents a human or computer player |
| `Computer.java`    | Smart AI player logic |
| `PlayerLibrary.java` | Manages all players' historical scores |
| `GameBoard.java`   | Manages the list and order of players |

---
1. **Computer STRATEGY Interface**
    - Computer.java designs an interface with roll() and choose() methods. This allows for multiple implementations of the Computer interface based on what difficult is chosen
2. **Concrete Strategies**

    - **ComputerEasy** (`proj.strategy.ComputerEasy`)  
      - Uses a lower score threshold (e.g., 5) to decide when to stop rerolling.  
      - Chooses randomly among legal scoring categories when no high-value combo exists.  

    - **ComputerHard** (`proj.strategy.ComputerHard`)  
      - Uses a higher threshold (e.g., 13).  
      - Prioritizes special combinations (Yahtzee, straights, full house) if threshold met.  
      - Otherwise picks the category yielding the maximal points.
---
1. **Dice FLYWEIGHT Pattern**
     - To avoid unnecessary object creation and improve memory efficiency, we applied the Flyweight design pattern for our dice logic. Instead of instantiating a new Dice object each time it's needed, we used a DiceFactory class to manage a shared pool of reusable Dice objects. This pattern ensures that each die is only created once and reused as needed, minimizing memory usage during gameplay.
---
**Avoidance of antipatterns**
1. **Encapsulation & Information Hiding**
    - Each core component (e.g. Dice, Cup, Scoreboard) strictly exposes only the minimal public API needed for clients, while relegating all mutable state and internal algorithms to private members. By bundling  the roll logic, face‐value storage, and reset behavior behind well‐defined method boundaries, we enforce high cohesion within each class and loose coupling between modules. We create copies of any ArrayLists that we return and ensure that the Dice class is immutable to avoid encapsulation issues with escaping Dice references.
2. **Defensive Programming & Input Validation**
    - All client‐facing methods validate their inputs and enforce fail‐fast behavior. For example, Scoreboard.setScore(...) checks that the category exists and hasn’t already been scored, throwing an IllegalArgumentException on invalid calls. Constructors and setters guard against null parameters, and public collections are wrapped in Collections.unmodifiable… to prevent callers from injecting malformed data.
---
**Artificial Intelligence Use**
  - We used AI to assist in creating the GUI through Swift for our Yahtzee app. AI helped create the framework of our View class and assist in the Swift syntax and features. We then connected this graphic design to our backend functions to add functionality to our GUI.

**Model-View-Controller**
  - We connected our View class to our pre-established Model class (in the form of **`GameBoard.java`**) and added a Controller class to act as a listener between these classes to display changes in our backend data to the GUI and vise versa. The use of a Controller class in our MVC setup demonstrates the OBSERVER design pattern, where the View reacts to changes in the Model through event-driven updates. The Controller listens for user input (e.g., button clicks or score selections), updates the Model accordingly, and then notifies the View to reflect those changes.

