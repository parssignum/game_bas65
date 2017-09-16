# Project Plan

Author: Ben Schwennesen

The game will be similar to the classic arcade game Breakout with some added variations. 

## Paddle
In my game, the paddle will appear as a human sprite, similar to the one in "Fireball" (https://youtu.be/kVuIZgltuR4), which will be able to catch and 
release certain balls (some will be "hot-potatoes" which must be released within a certain amount of time, or else they 
will harm the paddle in some way). The paddle will have:
1. a "catch-release" capability, similar to the one in "Fireball"
    * this is the default state
2. a "fire" capability, which will fire a bullet directly upwards
    * activated with key press (UP arrow) 
4. a "growth" capability, which will increase the paddle's size and ball-release velocity 
    * activated with key press (G) after receiving power-up
5. a "speed" capability, which will increase the paddle's speed
    * activated with key press (S) after receiving power-up

## Levels
I plan to build approximately five levels, wherein I will generate the blocks for my levels algorithmically, with some degree
of randomness in placement. As the level increases, more blocks will be introduced. This will look something like the following: 

```text
---------------------                ---------------------
|                    |               |         33        |
|        2222        |               |       3    3      |
|      11111111      |               |     3   22   3    |
|        2222        |               |     3  1111  3    |
|                    |               |     3   22   3    |
|                    |       ==>     |       3    3      |       ==>     (so on...)
|                    |               |         33        |
|                    |               |                   |
|                .   |               |                .  |
|         *          |               |         *         |
|        -|-         |               |        -|-        |
|        / \         |               |        / \        |
---------------------                ---------------------
```

Levels will increase in difficulty by adding tougher (harder to destroy) blocks in larger numbers, as well as by increasing 
the speed of the ball slightly. Levels might also introduce new balls, depending on how difficult this will make gameplay. 
Harder levels will also feature "power-downs", which will be similar to power-ups but will have negative effects (such as,
for example, slowing down the player). 

## Blocks
Different varieties of blocks will include: 
    1. a basic block which will be destroyed instantly on contact with the ball
    2. a tougher block which will require more hits to destroy
    3. a "rubber" block, which can only be destroyed with the paddle's "fire" capability 
    
## Power-ups
The power-ups I plan to include are:
    1. a "growth" power-up that will increase the paddle's size and ball-release velocity
    2. a "speed" power-up that will increase the paddle's speed
    3. a "multiply" power-up that will spawn new balls, which can leave the screen without loss of life 
    4. a "slow time" ability that will decrease ball movement speed but leave paddle movement speed the same

## Cheat Keys
The cheat keys I plan to include are:
    1. "level jump" numeric keys to allow the player to play/examine levels without beating the prior levels 
    2. a "god mode" key which will prevent the ball from falling off the screen (it will bounce off all walls) 
        * this will also prevent the player from taking damage in the boss level (see EXTRAS)
    3. a "reset" key to reset the ball and paddle to their initial positions
    4. "power-up" keys that will activate the game's various power-ups without needing to obtain them normally
    5. a "nuke" key to destroy everything on screen and cause the player to lose (primarily for debugging)