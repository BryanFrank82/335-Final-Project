# 335-Final-Project: Yahtzee Game
### Author: Bryan Frank, Ethan Alter, Frank Chen, Muyang Chen

## Overview
This project implements a Yahtzee game in Java.
The system models Players, Dice, a Cup to roll the dice, Scoring, and GameBoard management. 
There are two types of players:
- **Human**
- **computer** (AI, play with human player based on difficulty: easy or hard)

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
