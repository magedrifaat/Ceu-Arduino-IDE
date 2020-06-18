package processing.app;

import javax.swing.JMenu;

public class PluginAPI {
  private Editor editor;
  
  public PluginAPI(Editor editor) {
    this.editor = editor;
  }
  
  public void addMenu(JMenu newMenu) {
    editor.addMenu(newMenu);
  }
}
  