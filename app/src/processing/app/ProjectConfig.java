package processing.app;

import java.util.ArrayList;
import java.util.Arrays;
import processing.app.syntax.CustomKeywords;
import java.io.File;


public class ProjectConfig {
  private String projectType;
  private ArrayList<String> extensions;
  private CustomKeywords keywords;
  private String compileCommand;
  private String runCommand;
  private String uploadCommand;
  
  public ProjectConfig(String projectType) {
    // TODO: handle all null cases
    
    // TODO: add all data as preferences instead of config files
    this.projectType = projectType;
    extensions = getPrefExtensions();
    keywords = new CustomKeywords(getKeywordsFile());
    compileCommand = getPrefCommand("compile");
    runCommand = getPrefCommand("run");
    uploadCommand = getPrefCommand("upload");
    
  }
  
  public String getCompileCommand() {
    return compileCommand;
  }
  
  public String getRunCommand() {
    return runCommand;
  }
  
  public String getUploadCommand() {
    return uploadCommand;
  }
  
  public boolean isRunnable() {
    return runCommand == null;
  }
  
  public boolean isUploadable() {
    return uploadCommand == null;
  }
  
  public boolean isCorrectExtension(String extension) {
    return extensions.contains(extension);
  }
  
  public CustomKeywords getKeywords() {
    return keywords;
  }
  
  private ArrayList<String> getPrefExtensions() {
    String csvExtensions = PreferencesData.get(projectType + "-" + "extensions");
    return new ArrayList<String> (Arrays.asList(csvExtensions.replaceAll(" ", "").split(",")));
  }
  
  private String getPrefCommand(String command) {
    return PreferencesData.get(projectType + "-" + command);
  }
  
  private File getKeywordsFile() {
    return new File(BaseNoGui.getContentFile("lib"), "keywords_" + projectType + ".txt");
  }
}