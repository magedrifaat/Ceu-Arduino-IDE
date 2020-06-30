import processing.app.Plugin;
import processing.app.PluginAPI;

import java.io.File;

import javax.swing.JMenu;
import javax.swing.JMenuItem;


public class PicoPlugin implements Plugin {
  public PicoPlugin() {
    
  }
  
  @Override
  public void onMenu(PluginAPI pluginAPI) {
    JMenu picomenu = new JMenu("pico-Ceu");
    
    JMenu examples = new JMenu("Exmaples");
    
    // Get the path that this plugin is in
    // stackoverflow.com/questions/11747833
    String pluginPath = PicoPlugin.class.getProtectionDomain().getCodeSource().getLocation().getPath();
    
    String examplesPath = pluginPath + "/picoExamples/";
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
    
    String libsPath = pluginPath + "/picoLibraries/";
    File libsDir = new File(libsPath);
    for (File file : libsDir.listFiles()) {
      if (file.isDirectory()) {
        JMenuItem libItem = new JMenuItem(file.getName());
        libItem.addActionListener(pluginAPI.getIncludeAction(file.getAbsolutePath()));
        libraries.add(libItem);
      }
    }
    
    picomenu.add(examples);
    picomenu.add(libraries);
    
    pluginAPI.addMenu(picomenu);
  }
}