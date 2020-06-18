package processing.app;

import javax.swing.JMenu;
import java.io.File;

public class PluginAPI {
  private Editor editor;
  
  public PluginAPI(Editor editor) {
    this.editor = editor;
  }
  
  public void addMenu(JMenu newMenu) {
    editor.addMenu(newMenu);
  }
  
  public void openFile(String fileName) {
    File file = new File(fileName);
    if (file.exists()) {
      try {
        editor.base.handleOpen(file);
      }
      catch (Exception ex) {
        editor.statusError("Error while trying to open the file");
      }
    }
    else {
      editor.statusError("Couldn't find the file specified");
    }
  }
}
  