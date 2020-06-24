package processing.app;

public interface Plugin {
  default void start(PluginAPI p) {}
  default void quit(PluginAPI p) {}
  default void addMenu(PluginAPI p) {}
  default void sideTab(PluginAPI p) {}
  default void format(PluginAPI p) {}
}