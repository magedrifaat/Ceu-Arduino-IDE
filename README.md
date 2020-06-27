# Creating Plugins
The new plugin architecture lets the user write plugins that can execute code responding to certain events (hooks) by implementing the handlers of these hooks. The architecture also provides a plugin API object that is passed to all the handlers and has some methods that enables the user to add or modify GUI or perform some action on the IDE.
## Main Skeleton
A user defined plugin is a java class that implements the Plugin interface and provides a public zero-argument constructor, this class can override any of the handlers defined in the Plugin interface (and explained here in another section) to execute code handling the corresponding event.
Here is an example of a simple plugin that handles the onStart hook:
```java
import processing.app.Plugin;
import processing.app.PluginAPI;

public class TestPlugin implements Plugin {
  
  public TestPlugin() { }
  
  @Override
  public void onStart(PluginAPI pluginAPI) {
    System.out.println("Plugin Started!");
  }
}
```
## Deploying the Plugin
To add the plugin to the IDE, you must compile your plugin java file to the compiled .class file that the IDE can load at runtime.
To compile put your java in the Plugins folder under the Arduino folder, and in the same foler, execute the following command:
```
compile.bat [YourPLugin].java
```
or simply drag and drop your java file onto the compile.bat batch file.
The file name of your java file must be the same as the name of the plugin class implemented inside the file.
Make sure that the compilation completed without errors and a .class file was generated.

## Hooks and Handlers
The following are the set of the handlers that can be implemented in the plugin to handle the corresponding hooks:
#### onStart handler
This handler is called when the window is created
```java
public void onStart(PluginAPI pluginAPI)
```
#### onQuit handler
This handler is called when the window is closing or the IDE is quitting
```java
public void onQuit(PluginAPI pluginAPI)
```
#### onCompile handler
This handler is called just before the compilation of the project
```java
public void onCompile(PluginAPI pluginAPI)
```
#### onUpload handler
This handler is called just before uploading the project
```java
public void onUpload(PluginAPI pluginAPI)
```
#### onRun handler
This handler should be called before running the project (not supported yet)
```java
public void onRun(PluginAPI pluginAPI)
```
#### onMenu handler
This handler is called only once when the menu bar is being created, implement this when you want to add your own menu to the menu bar by calling the relevant API functions in your implementation of the handler
```java
public void onMenu(PluginAPI pluginAPI)
```
#### onSide handler
This handler is called only once when the side bar is being created, implement this when you need to add your own panel as another tab in the side bar by calling the relevant API functions in your implementation of the handler
```java
public void onSide(PluginAPI pluginAPI)
```
#### onEnter handler
This handler is called whenever the user presses enter key in the text area of the IDE
```java
public void onEnter(PluginAPI pluginAPI)
```
#### onFormat handler
This handler is called when the user attempts to use the auto-format feature of the IDE
```java
public void onFormat(PluginAPI pluginAPI)
```

## API functions
### GUI functions
#### addMenu
```java
public void addMenu(JMenu newMenu)
```
This function should only be called in the onMenu handler. It takes a JMenu object as a parameter and adds it to the menu bar of the window.

#### addSideTab
```java
public void addSideTab(String tabName, JPanel tabPanel)
```
This function should only be called in the onSide handler. Call it when you want to add your own panel as a tab in the side bar, it takes the tab name as the first argument and a JPanel object as the second argument.

#### showDialog
```java
public Object showDialog(JOptionPane optionPane, String title)
```
This function takes a JOptionPane object and a title and shows a dialog created from teh option pane and with the given title, and returns the result of the shown dialog.

### Utility functions
#### openFile
```java
public void openFile(String fileName)
```
This function takes a full path of a file and opens the file in the IDE as a separate project.

#### saveProject
```java
public boolean saveProject()
```
This function saves any modifications in the current projects and returns true if successful. (Equivalent to ctrl+s)

#### saveProjectAs
```java
public void saveProjectAs()
```
This function prompts the save a copy of the current project.

#### getMainFilePath
```java
public String getMainFilePath()
```
This function returns the full path of the main file of the current project.

#### getCurrentTabText
```java
public String getCurrentTabText()
```
This function returns the entire text in the text area of the current active tab of the project.

#### setCurrentTabText
```java
public void setCurrentTabText(String text)
```
This function sets the entire text in the text area of the current active tab of the project.

#### addLineHighlight
```java
public void addLineHighlight(int line)
public void addLineHighlight(int line, Color color)
```
This function adds highlight to the line of the given line number, the default color of highlight is red but the user can optionally provide a color.

#### removeAllLineHighlights
```java
public void removeAllLineHighlights()
```
This functions removes all line highlights in the current project.

#### getIncludeAction
```java
public AbstractAction getIncludeAction(String folderName)
```
This function takes full path of a library folder and returns an abstract action that when executed, includes the library in the current active file (adds "#include ..." for all the includes of the given library).
This abstract action can be added to the listener of a button or a menu item for example so that it executes when the button or menu item is pressed.
