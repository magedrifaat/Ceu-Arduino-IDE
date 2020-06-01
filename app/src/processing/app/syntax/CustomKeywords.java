package processing.app.syntax;

import processing.app.Base;
import processing.app.BaseNoGui;

import java.io.File;

public class CustomKeywords extends PdeKeywords {
  private File keywordsFile;
  
  public CustomKeywords(File file) {
    super();
    this.keywordsFile = file;
  }
  
  /**
   * Handles loading of Ceu keywords file.
   *
   */
  @Override
  public void reload() {
    try {
      parseKeywordsTxt(this.keywordsFile);
    } catch (Exception e) {
      Base.showError("Problem loading keywords", "Could not load keywords file,\nplease re-install Arduino.", e);
      System.exit(1);
    }
  }
}