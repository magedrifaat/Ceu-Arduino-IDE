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

    JMenu examples = new JMenu("Examples");
    
    // TODO: implement support for recursive search for ceu-arduino examples
    String examplesPath = pluginPath + "/CeuPluginData/" + folderName + "/Examples/";
    File examplesDir = new File(examplesPath);
    for (File file : examplesDir.listFiles()) {
      if (file.isDirectory()) {
        File exampleFile = new File(file, file.getName() + ".ceu");
        if (exampleFile.exists()) {
          JMenuItem example = new JMenuItem(file.getName());
          example.addActionListener((e) -> pluginAPI.openFile(exampleFile.getAbsolutePath()));
          examples.add(example);
        }
      }
      else if (file.getName().endsWith(".ceu")) {
        JMenuItem example = new JMenuItem(file.getName().substring(0, file.getName().length() - 4));
        example.addActionListener((e) -> pluginAPI.openFile(file.getAbsolutePath()));
        examples.add(example);
      }
    }
    
    
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
}