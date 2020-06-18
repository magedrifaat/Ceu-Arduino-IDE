package processing.app;

public interface Plugin {
  default void start(PluginAPI p) {}
  default void quit(PluginAPI p) {}
  default void addmenu(PluginAPI p) {}
  default void addtool(PluginAPI p) {}
  default void format(PluginAPI p) {}
}