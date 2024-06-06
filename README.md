Worm Game
Description
This project implements a simple snake-like game called "Worm Game" using Java Swing for the graphical user interface. The player controls a worm that moves around the game board to collect coins while avoiding collisions with walls and its own body. The game features sound effects, pause functionality, and a scoring system.

Features
User Interface: The game has a graphical user interface built using Java Swing components.
User Data Management: User data such as the username and score are managed and persisted between game sessions using serialization.
Game Mechanics: Players control the movement of the worm using the W, A, S, D keys. The objective is to collect coins to increase the worm's length and score without colliding with walls or itself.
Sound Effects: The game includes sound effects for collision, coin pickup, and game over events.
Pause Functionality: Players can pause and resume the game at any time by pressing the SPACE key.
How to Play
Start the Game: Run the WormGame class to launch the game.
Enter Username: Upon startup, the player is prompted to enter their username. If left blank, the default username "Player" is used.
Game Controls:
Use the W, A, S, D keys to move the worm:
W: Up
A: Left
S: Down
D: Right
Press SPACE to pause/resume the game.
Press F to toggle full-screen mode.
Objective: Collect coins to increase your score. Avoid collisions with walls or the worm's body.
Game Over: The game ends when the worm collides with a wall or itself. Players can choose to retry or exit the game.
Dependencies
Java Development Kit (JDK)
Java Swing Library
Installation
Clone the repository to your local machine.
Compile the Java files using javac.
Run the WormGame class to start the game.
Credits
Sound effects sourced from Freesound.org and Mixkit.
Images used in the game obtained from OpenGameArt.org.
License
This project is licensed under the MIT License - see the LICENSE file for details.
