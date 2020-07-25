import processing.app.Plugin;
import processing.app.PluginAPI;

import java.io.File;

import javax.swing.JMenu;
import javax.swing.JMenuItem;


public class CeuPlugin implements Plugin {
  public CeuPlugin() {
    
  }
  
  @Override
  public void onMenu(PluginAPI pluginAPI) {
    JMenu ceuMenu = new JMenu("Ceu");

    // Get the path that this plugin is in
    // stackoverflow.com/questions/11747833
    String pluginPath = CeuPlugin.class.getProtectionDomain().getCodeSource().getLocation().getPath();

    JMenu ceuSubMenu = createSubmenu("Ceu", "ceu", pluginPath, pluginAPI);
    JMenu ceuArduinoSubMenu = createSubmenu("Ceu-Arduino", "ceu-arduino", pluginPath, pluginAPI);
    JMenu picoSubMenu = createSubmenu("pico-Ceu", "pico-ceu", pluginPath, pluginAPI);

    ceuMenu.add(ceuSubMenu);
    ceuMenu.add(ceuArduinoSubMenu);
    ceuMenu.add(picoSubMenu);
    
    pluginAPI.addMenu(ceuMenu);
  }

  JMenu createSubmenu(String menuName, String folderName, String pluginPath, PluginAPI pluginAPI) {
    JMenu subMenu = new JMenu(menuName);
    
    String examplesPath = pluginPath + "/CeuPluginData/" + folderName + "/Examples/";
    File examplesDir = new File(examplesPath);
    JMenu examples = recursiveExamplesMenu(examplesDir, examplesDir.getName(), pluginAPI);
    
    JMenu libraries = new JMenu("Include Library");
    String libsPath = pluginPath + "/CeuPluginData/" + folderName + "/Include/";
    File libsDir = new File(libsPath);
    for (File file : libsDir.listFiles()) {
      if (file.isDirectory()) {
        JMenuItem libItem = new JMenuItem(file.getName());
        libItem.addActionListener(pluginAPI.getIncludeAction(file.getAbsolutePath()));
        libraries.add(libItem);
      }
    }
    
    subMenu.add(examples);
    subMenu.add(libraries);
    
    return subMenu;
  }
  
  /**
   * Recursively follow sub-folders to find all examples and load them as a menu tree.
   */
  JMenu recursiveExamplesMenu(File exampleDir, String parentName, PluginAPI pluginAPI) {
    JMenu rootMenu = new JMenu(parentName);
    for (File file : exampleDir.listFiles()) {
      if (file.isDirectory()) {
        File subFile = new File(file, file.getName() + ".ceu");
        if (subFile.exists()) {
          JMenuItem example = new JMenuItem(file.getName());
          example.addActionListener((e) -> pluginAPI.openFile(subFile.getAbsolutePath()));
          rootMenu.add(example);
        }
        else {
          // Skip over an intermediate "examples" folder
          if (file.getName().equals("examples")) {
            rootMenu = recursiveExamplesMenu(file, parentName, pluginAPI);
          }
          else {
            rootMenu.add(recursiveExamplesMenu(file, file.getName(), pluginAPI));
          }
        }
      }
      else if(file.getName().endsWith(".ceu")) {
        JMenuItem example = new JMenuItem(file.getName().substring(0, file.getName().length() - 4));
        example.addActionListener((e) -> pluginAPI.openFile(file.getAbsolutePath()));
        rootMenu.add(example);
      }
    }

    return rootMenu;
  }
}