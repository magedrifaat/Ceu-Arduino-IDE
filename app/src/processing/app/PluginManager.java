package processing.app;

import java.util.ArrayList;
import java.io.File;
import processing.app.helpers.FileUtils;
import java.net.URL;
import java.net.URLClassLoader;

import javax.swing.JMenu;

public class PluginManager {
  public enum Hooks {MENU, TOOL, SIDE, FORMAT, COMPILE, UPLOAD, 
                    RUN, ENTER, START, QUIT}
  
  ArrayList<Plugin> plugins;
  Editor editor;
  PluginAPI pluginAPI;
  
  public PluginManager(Editor editor) {
    
    this.editor = editor;
    pluginAPI = new PluginAPI(editor);
    
    plugins = new ArrayList<> ();
    
    File pluginFolder = BaseNoGui.getContentFile("Plugins");
    for (File file : FileUtils.listFiles(pluginFolder, false, new ArrayList<String> ())) {
      if (file.getName().endsWith(".class")) {
        try {
          URLClassLoader classLoader = new URLClassLoader(new URL[]{pluginFolder.toURI().toURL()});
          Class<?> cl = classLoader.loadClass(file.getName().substring(0, file.getName().length() - 6));
          plugins.add((Plugin)(cl.newInstance()));
        }
        catch (Exception ex) {
          ex.printStackTrace();
        }
      }
    }
    
  }
  
  public void fire(Hooks hook) {
    switch (hook) {
      case START:
        for (Plugin p : plugins) {
          p.start(pluginAPI);
        }
        break;
        
      case MENU:
        for (Plugin p : plugins) {
          p.addmenu(pluginAPI);
        }
        break;
    }
  }
}