# Sudoku

## Features:

- written in Kotlin
- uses navigation to switch between screens:
  - main menu (high prio)
  - settings (low prio)
  - scoreboard (high prio)
  - level selection (medium prio)
  - in game (singleplayer) (high prio)
  - win screen (medium prio)
  - loss screen (medium prio)
- scoreboard is stored in a database in cloud (high prio)
- share high scores on WhatsApp / Facebook (high prio)
- profile picture upload (low prio)
- store levels in cloud (low prio) otherwise hardcoded / in memory (high prio)

## Game rules:

- standard, well-formed Sudoku, with only one possible solution (http://pi.math.cornell.edu/~mec/Summer2009/Mahmood/More.html)
- NPC problem
- https://en.wikipedia.org/wiki/Sudoku

- player gains points whenever he places a number correctly
- player loses points when he places a number incorrectly
- correct / incorrect combo for extra points
- each level has a time limit depending on the difficulty, you gain or lose points depending on how you do
