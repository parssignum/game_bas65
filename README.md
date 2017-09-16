Hostile Breakout (game)
====

A variant of the classic arcade game Breakout built using JavaFX.

Author: Ben Schwennesen

## Special Files

The file [BreakoutMain](./src/BreakoutMain.java) is used to start the game.

The file [breakout.properties](./src/breakout.properties) holds configuration values for the game. 

The files [cheat-codes.txt](./src/cheat-codes.txt), [game-rules.txt](./src/game-rules.txt), and [story-blurb.txt](./src/story-blurb.txt) contain raw text with information about the game and how to play it. They can be displayed in the game's main menu by pressing the corresponding buttons. 

The package **resources** contains backgrounds for the game's levels and images used to represent the moving actors in the game (the paddle, ball, and bullets). 

# Playing the Game

##### Rules and Controls 
- Press the **LEFT** and **RIGHT** arrows to guide the paddle. The paddle catches the ball automatically when making contact.

- Press the **SPACE** button to throw the ball upwards. If the paddle is on the left half of the paddle, it will be thrown leftwards; otherwise, it will be thrown rightwards. 
   
- Press the **UP** arrow to fire bullets, which are used to destroy brown (rubber) blocks. All other blocks may be destroyed with either the ball or bullets. Be cautious: bullets bounce back and are able to harm the paddle.
   
- If the ball falls off the bottom of the screen or the paddle loses all health points (from bullets), the paddle loses a life.

- Once all blocks are destroyed the player will advance to the next level.

- The player wins the game by completing all levels before running out of lives.

##### Cheat codes

 - **1, 2, 3, 4, 5**: press to advance to a specific level

 - **G**: press to activate "God Mode," wherein the ball will never fall of the screen and the paddle takes no damage

 - **R**: press to reset the ball and the paddle to their initial positions at the bottom-center of the screen.

 - **N**: press to destroy everything on screen and fail immediately (*N stands for "Nuke"*)

 - **S**: press to activate the Speed power-up (which makes the paddle faster)
      
 - **M**: press to activate the Multiply power-up (which spawns more balls)
      
 - **T**: press to activate the Slow Time power-up (which decreases the ball's speed)

# Image Credits

 - The human sprite image used for the paddle was generated [here](http://gaurav.munjal.us/Universal-LPC-Spritesheet-Character-Generator/). 

 - The ball sprite image was obtained [here](https://opengameart.org/content/energy-ball).

 - Background images for the levels were obtained [here](https://opengameart.org/content/backgrounds-for-2d-platformers).
