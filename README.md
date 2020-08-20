# Céu IDE
## Project Code URL
All the main work in this project was done here: https://github.com/magedrifaat/Ceu-Arduino-IDE/tree/ceu-language-integration

Commits starting from 1 jun. 2020 on this branch are all the code written for the project during the GSoC program.

A full diff covering all added code: https://github.com/magedrifaat/Ceu-Arduino-IDE/compare/master...ceu-language-integration
## Project Description
In this project, I implemented full support for the Céu programming language in the Arduino IDE. This includes Céu, pico-Céu and improved support for Céu-Arduino which was firstly implemented last year outside of the program. Language features include opening code files, compiling and running/uploading the scripts, menu items for examples and libraries specific for the language and its bindings, and syntax features like syntax highlighting, auto indentation, auto formatting and code folding.

I also implemented some major features in the IDE itself which will make future development much easier. These features include: A plugin architecture to write plugins for the IDE which makes it easy to add new features to the IDE without having to modify the core code of the IDE. I also implemented support for user defined project types which permits adding complete support for user defined languages and project types through preferences and plugins only.

## Documentation
### Getting Started
To use this IDE, simply download the latest release for your OS.
For linux systems, you will need to install the SDL library for pico-Céu scripts to work:
```
$ sudo apt-get install libsdl2-dev libsdl2-image-dev libsdl2-mixer-dev libsdl2-ttf-dev libsdl2-net-dev libsdl2-gfx-dev
```
### Using Céu features
Out of the box, the IDE can open, compile and upload/run scripts written in Céu which has the ".ceu" extension. This includes Céu scripts, pico-Céu and Céu-Arduino.
In addition to this, there is a "Céu" menu in the menu bar from which you can open examples and include libraries for all three project types of Céu.

You can also use the "Auto Format"feature in the IDE to fix the indentation of the Céu code from "Tools > Auto Format" menu or by pressing Ctrl+T. Also, you can collapse and expand blocks of Céu code by enabling code folding in the preferences.

Also, the console in the IDE now supports user input for runnable scripts. You can write Céu scripts that accept command line user input and run them inside the IDE just like an external terminal.

### Adding custom project types
A major feature added to this IDE is the possibility of adding a completely new language or project type to the IDE without modifying the IDE code. As a proof of concept, all the Céu features implemented in this project were implemented through this new technique without direct modification to the IDE code itself.
A full documentation on how to add a new language to the IDE can be found here: TODO

### Writing Plugins
Another big feature implemented in this project is a new Plugin Architecture developed for the IDE through which you can add new features to the IDE on the go, without modifying the IDE code.
A full tutorial on how to write a plugin can be found here: TODO
