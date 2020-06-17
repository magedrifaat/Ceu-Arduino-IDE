package processing.app;

import java.util.ArrayList;
import java.util.Arrays;
import java.io.File;


public class ProjectConfig {
  
  static ArrayList<ProjectConfig> configs = new ArrayList<>();
  static ProjectConfig LEGACY_CONFIG;
  
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
  
  public static void loadConfigs() {
    String types = PreferencesData.get("project-types");
    for (String type : types.replaceAll(" ", "").split(",")) {
      ProjectConfig newConfig = new ProjectConfig(type);
      configs.add(newConfig);
      if (newConfig.isLegacy()) {
        LEGACY_CONFIG = newConfig;
      }
    }
  }
  
  public static ProjectConfig inferConfig(String extension) {
    if (extension == null) {
      return LEGACY_CONFIG;
    }
    
    for (ProjectConfig conf : configs) {
      if (conf.getDefaultExtension().equals(extension)) {
        return conf;
      }
    }
    
    return LEGACY_CONFIG;
  }
  
  /**
  *  Checks to see if this file's extension is available in any
  *  project type.
  */
  public static boolean acceptName(String fileName) {
    for (ProjectConfig config : configs) {
      if (config.hasAcceptableExtension(fileName)) {
        return true;
      }
    }
    
    return false;
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