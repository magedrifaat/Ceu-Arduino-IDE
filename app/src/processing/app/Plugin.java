package processing.app;

public interface Plugin {
  default void onStart(PluginAPI p) {}
  default void onQuit(PluginAPI p) {}
  default void onCompile(PluginAPI p) {}
  default void onUpload(PluginAPI p) {}
  default void onRun(PluginAPI p) {}
  default void onFormat(PluginAPI p) {}
  default void onMenu(PluginAPI p) {}
  default void onSide(PluginAPI p) {}
  default void onNewLine(PluginAPI p) {}
}