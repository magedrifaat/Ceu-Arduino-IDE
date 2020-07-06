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
  
  /**
   * Try to infer project type from extesnion.
   * First check if it matches the default extesion of any Config,
   *  then see if it matches any sub extension of any config.
   */
  public static ProjectConfig inferConfig(String extension) {
    if (extension == null) {
      return LEGACY_CONFIG;
    }
    
    for (ProjectConfig conf : configs) {
      if (conf.getDefaultExtension().equals(extension)) {
        return conf;
      }
    }
    
    for (ProjectConfig conf : configs) {
      if (conf.hasAcceptableExtension("test." + extension)) {
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
  
  /** 
  *   Return a list of titles for all the project types.
  */
  public static ArrayList<String> getTitles() {
    ArrayList<String> titles = new ArrayList<> ();
    for (ProjectConfig config : configs) {
      titles.add(config.getTitle());
    }
    
    return titles;
  }
  
  /**
  *   Return the project config of the given title.
  */
  public static ProjectConfig fromTitle(String title) {
    for (ProjectConfig config : configs) {
      if (config.getTitle().equals(title)) {
        return config;
      }
    }
    
    return LEGACY_CONFIG;
  }
  
  public String getTitle() {
    return projectTitle;
  }
  
  public String getCompileCommand() {
    return compileCommand;
  }
  
  public String getRunCommand() {
    if (runCommand == null || runCommand.isEmpty()) {
      return "";
    }
    
    return runCommand;
  }
  
  public String getUploadCommand() {
    if (uploadCommand == null || uploadCommand.isEmpty()) {
      return "";
    }
    
    return uploadCommand;
  }
  
  public boolean isRunnable() {
    return !getRunCommand().isEmpty();
  }
  
  public boolean isUploadable() {
    return !getUploadCommand().isEmpty() || isLegacy();
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
    String csvExtensions = PreferencesData.getDefault(projectType + "-extensions");
    if (csvExtensions == null) {
      csvExtensions = PreferencesData.get(projectType + "-extensions");
    }
    
    if (csvExtensions == null) {
      return new ArrayList<String> ();
    }
    
    return new ArrayList<String> (Arrays.asList(csvExtensions.replaceAll(" ", "").split(",")));
  }
  
  /**
   *  Returns the preference [projectType]-[command].
   *  If the preference doesn't exist, returns an empty string
   */
  private String getPrefCommand(String command) {
    String res = PreferencesData.getDefault(projectType + "-" + command);
    if (res == null) {
      res = PreferencesData.get(projectType + "-" + command);
    }
    
    return res != null? res: "";
  }
  
  public File getKeywordsFile() {
    String fileName;
    if (isLegacy() || getPrefCommand("keywords") == "") {
      fileName = "keywords.txt";
    }
    else {
      fileName = getPrefCommand("keywords");
    }
    
    return new File(BaseNoGui.getContentFile("lib"), fileName);
  }
}