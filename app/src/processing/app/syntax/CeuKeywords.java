package processing.app.syntax;

import processing.app.Base;
import processing.app.BaseNoGui;

import java.io.File;

public class CeuKeywords extends PdeKeywords {
  
  public CeuKeywords() {
    super();
  }
  
  /**
   * Handles loading of Ceu keywords file.
   *
   */
  @Override
  public void reload() {
    try {
      parseKeywordsTxt(new File(BaseNoGui.getContentFile("lib"), "keywords_ceu.txt"));
    } catch (Exception e) {
      Base.showError("Problem loading keywords", "Could not load keywords_ceu.txt,\nplease re-install Arduino.", e);
      System.exit(1);
    }
  }
}