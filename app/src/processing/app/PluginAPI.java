package processing.app;

import java.awt.Color;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.JDialog;
import java.io.File;

import org.fife.ui.rsyntaxtextarea.folding.FoldParser;
import org.fife.ui.rsyntaxtextarea.folding.FoldParserManager;
import org.fife.ui.rsyntaxtextarea.RSyntaxDocument;

public class PluginAPI {
  private Editor editor;
  
  public PluginAPI(Editor editor) {
    this.editor = editor;
  }
  
  // --------------- GUI relevant functions --------------- //
  public void addMenu(JMenu newMenu) {
    editor.addMenu(newMenu);
  }
  
  public void addSideTab(String tabName, JPanel tabPanel) {
    editor.addSideTab(tabName, tabPanel);
  }
  
  public Object showDialog(JOptionPane optionPane, String title) {
    JDialog dialog = optionPane.createDialog(editor, title);
    dialog.setVisible(true);
    
    return optionPane.getValue();
  }
  
  
  // --------------- IDE Actions functions ---------------- //
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
  
  public void addLineHighlight(int line) {
    try {
      editor.addLineHighlight(line);
    }
    catch (Exception e) {
      System.out.println("Faield to add line highlight at line " +line);
    }
  }
  
  public void addLineHighlight(int line, Color color) {
    try {
      editor.addLineHighlight(line, color);
    }
    catch (Exception e) {
      System.out.println("Faield to add line highlight at line " +line);
    }
  }
  
  
  public void removeAllLineHighlights() {
    editor.removeAllLineHighlights();
  }
  
  public boolean saveProject() {
    return editor.handleSave(false);
  }
  
  public boolean saveProjectAs() {
    return editor.handleSaveAs();
  }
  
  public String getMainFilePath() {
    return editor.getMainFilePath();
  }
  
  public String getCurrentTabText() {
    return editor.getCurrentTab().getText();
  }
  
  public void setCurrentTabText(String text) {
    editor.getCurrentTab().setText(text);
  }

  public String getProjectType() {
    return editor.getProjectConfig().getType();
  }

  public int getCaretPosition() {
    return editor.getCurrentTab().getTextArea().getCaretPosition();
  }

  public void indentNextLine() {
    editor.getCurrentTab().getTextArea().indentNext();
  }

  public void addFoldParser(FoldParser customParser) {
    FoldParserManager.get().addFoldParserMapping(RSyntaxDocument.SYNTAX_STYLE_CPLUSPLUS, customParser);
  }
}
