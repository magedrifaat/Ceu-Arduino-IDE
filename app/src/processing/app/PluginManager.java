package processing.app;

import java.util.ArrayList;
import java.io.File;
import processing.app.helpers.FileUtils;
import java.net.URL;
import java.net.URLClassLoader;

public class PluginManager {
  public enum Hook {MENU, TOOL, SIDE, FORMAT, COMPILE, UPLOAD, 
                    RUN, ENTER, START, QUIT}
  
  ArrayList<Plugin> plugins;
  
  public PluginManager() {
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
  
  public void fire(Hook hook) {
    switch (hook) {
      case START:
        for (Plugin p : plugins) {
          p.start();
        }
        break;
    }
  }
}