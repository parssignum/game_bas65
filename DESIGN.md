# Design Goals
My design goals for this project were to make an easily understood class structure that would allow new features to be added to the game with little effort. More specifically:
1. I wanted my methods and classes to have singular, clear purposes. 
2. I wanted to separate completely the logic and data of my project.
3. I wanted my code to be well-documented and easily understood. 
4. I wanted to have all moving actors in the game share a common abstract ancestor ([Sprite](./src/Sprite.java)) through inheritance. 
    - Pros: methods that are needed for all moving actors, like *collidesWith(Object o)*, are available through inheritance
    - Cons: in the end, some aspects of what were included for the abstract super class were not needed for every moving actor. For example, *updatePosition(double timeStep)* was not needed for the [Paddle](./src/Paddle.java) class since its position was updated by key presses, as opposed to periodic updates using a velocity vector. 
5. I wanted to use factories to generate the objects in my game. Originally, I envisioned a "block factory" and a "sprite factory," however these ended up merged together as one ["level factory"](./src/LevelFactory.java).
    - Pros: when adding a new level, all that needs to be added are new key-value pairs for the elements in the new level in the file [breakout.properties](./src/breakout.properties). The level factory only needs to know how many blocks and power-ups of each type to generate and the level's background to create it.  
    - Cons: the level factory ended up doing too much. I found it difficult to keep the role of factories simple and clear without adding "code smells", specifically "feature envy" and "inappropriate intimacy" (as defined [here](http://www.cs.duke.edu/courses/compsci308/fall17/resources/smellstorefactorings.pdf)). 
6. I wanted to take advantage of JavaFX features as much as possible by having my classes extend native JavaFX classes, where doing so made sense (for example, having [Block](./src/Block.java) extend [Rectangle](https://docs.oracle.com/javase/8/javafx/api/javafx/scene/shape/Rectangle.html)).
    - Pros: makes it easy to take advantage of JavaFX base features, like using Bounds getters from classes extending Node.
    - Cons: the inheritance brings with it many unnecessary features. For example, when extending Node, the extending class gets many methods *OnSomeEvent* (OnKeyPress, OnSwipeUp, OnScroll, etc.) that will never be used. 
7. I wanted to host all values used in configuring the game (window dimensions, positions, ball speed, health points, etc.) in a single *.properties* file, from which a ["properties-getter"](./src/PropertiesGetter.java) static class could read-in values and pass them along to other classes as necessary.
    - Pros: allows configuration values to be easily tinkered-with, thus making it simple to improve the playability of the game.
    - Cons: when first adding a new configuration value, one must add it to the **.properties** file, add the key used to access the configuration value as a private instance variable in the static properties-getter class, and finally add a method to the properties-getter for retrieving the new value. This is a time-consuming practice. 


# Adding Features
- Adding new levels to the game requires only the addition of new key-value pairs in the [breakout.properties](./src/breakout.properties) file; specifically, these pairs will specify the background image for the new level, the number of each type of block it will contain, and the number of each type of power-up it will contain. The level factory handles the logic of actually building the new level. 
    - One small caveat is that the last level was (unconsciously) hard-coded into the end-game logic in the [HostileBreakout](./src/HostileBreakout.java) class (the class that handles gameplay logic, including input, displaying and transitioning levels, and implementing the various phases of the game loop). It would be a small fix to change this, however, by adding a key-value pair for the last level in the *.properties* file, and having the end-game logic get the last level from the properties-getter. This is something I plan to do when refactoring my masterpiece.

- In its current form, adding new types of blocks and power-ups to the project will not be easy. The types of power-ups and blocks were hard-coded into the [level factory](./src/LevelFactory.java) class. To add new types of blocks and power-ups, I would first need to refactor the level factories class to handle arbitrary amounts of types of blocks and power-ups. This is something I plan to do for my masterpiece. Once this is done (by adding a key-value pair in *.properties* specifying the names of the types), adding new types will be as easy as adding key-value pairs to the *.properties* file specifying the number of each type of the new block/power-up to include in each level. Their positions are randomly generated within the level factory. 