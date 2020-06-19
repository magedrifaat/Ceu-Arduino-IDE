package processing.app;

import javax.swing.JMenu;
import javax.swing.AbstractAction;
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
  
  public AbstractAction getIncludeAction(String folderName) {
    File folder = new File(folderName);
    
    if (folder.exists()) {
      return editor.getIncludeAction(folder);
    }
    else {
      editor.statusError("Couldn't find the file specified: " + folderName);
      return null;
    }
  }
}
