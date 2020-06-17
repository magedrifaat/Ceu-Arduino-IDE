package processing.app;

public interface Plugin {
  default void start() {}
  default void quit() {}
  default void addmenu() {}
  default void addtool() {}
  default void format() {}
}