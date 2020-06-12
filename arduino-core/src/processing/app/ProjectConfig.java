package processing.app;

import java.util.ArrayList;
import java.util.Arrays;
import java.io.File;


public class ProjectConfig {
  private String projectType;
  private String projectTitle;
  private ArrayList<String> extensions;
  private String compileCommand;
  private String runCommand;
  private String uploadCommand;
  
  public ProjectConfig(String projectType) {
    // TODO: handle all null cases
    
    // TODO: add all data as preferences instead of config files
    this.projectType = projectType;
    projectTitle = getPrefCommand("title");
    extensions = getPrefExtensions();
    compileCommand = getPrefCommand("compile");
    runCommand = getPrefCommand("run");
    uploadCommand = getPrefCommand("upload");
    
  }
  
  public String getTitle() {
    return projectTitle;
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
  
  public String getDefaultExtension() {
    return extensions.get(0);
  }
  
  public boolean isLegacy() {
    return projectType.equals("legacy");
  }
  
  public boolean hasAcceptableExtension(String name) {
    boolean acceptable = false;
    for (String extension : extensions) {
      if (name.endsWith(extension)) {
        acceptable = true;
        break;
      } 
    }
    
    return acceptable;
  }
  
  private ArrayList<String> getPrefExtensions() {
    String csvExtensions = PreferencesData.get(projectType + "-extensions");
    return new ArrayList<String> (Arrays.asList(csvExtensions.replaceAll(" ", "").split(",")));
  }
  
  private String getPrefCommand(String command) {
    return PreferencesData.get(projectType + "-" + command);
  }
  
  public File getKeywordsFile() {
    String fileName;
    if (isLegacy()) {
      fileName = "keywords.txt";
    }
    else {
      fileName = getPrefCommand("keywords");
    }
    
    return new File(BaseNoGui.getContentFile("lib"), fileName);
  }
}