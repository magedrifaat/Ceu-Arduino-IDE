package processing.app;

import java.util.ArrayList;
import java.io.File;
import processing.app.helpers.FileUtils;
import java.net.URL;
import java.net.URLClassLoader;

import javax.swing.JMenu;

public class PluginManager {
  public enum Hooks {MENU, SIDE, FORMAT, COMPILE, UPLOAD, 
                    RUN, ENTER, START, QUIT}
  
  ArrayList<Plugin> plugins;
  Editor editor;
  PluginAPI pluginAPI;
  
  public PluginManager(Editor editor) {
    
    this.editor = editor;
    pluginAPI = new PluginAPI(editor);
    
    plugins = new ArrayList<> ();
    
    // TODO: support .jar files as well
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
    try {
      switch (hook) {
        case START:
          for (Plugin p : plugins) {
            p.onStart(pluginAPI);
          }
          break;
        case QUIT:
          for (Plugin p : plugins) {
            p.onQuit(pluginAPI);
          }
          break;
          
        case MENU:
          for (Plugin p : plugins) {
            p.onMenu(pluginAPI);
          }
          break;
        case SIDE:
          for (Plugin p : plugins) {
            p.onSide(pluginAPI);
          }
          break;
        case COMPILE:
          for (Plugin p : plugins) {
            p.onCompile(pluginAPI);
          }
          break;
        case UPLOAD:
          for (Plugin p : plugins) {
            p.onUpload(pluginAPI);
          }
          break;
        case RUN:
          for (Plugin p : plugins) {
            p.onRun(pluginAPI);
          }
          break;
        case FORMAT:
          for (Plugin p : plugins) {
            p.onFormat(pluginAPI);
          }
          break;
        case ENTER:
          for (Plugin p : plugins) {
            p.onNewLine(pluginAPI);
          }
          break;
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }
}