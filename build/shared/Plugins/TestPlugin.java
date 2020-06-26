import processing.app.Plugin;
import processing.app.PluginAPI;
import java.awt.BorderLayout;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import javax.swing.JButton;

public class TestPlugin implements Plugin {
  
  public TestPlugin() {
    
  }
  
  @Override
  public void onStart(PluginAPI pluginAPI) {
    System.out.println("Plugin Started!");
  }
  
  @Override
  public void onQuit(PluginAPI pluginAPI) {
    JOptionPane pane = new JOptionPane("Plugin is quitting!", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION);
    
    pluginAPI.showDialog(pane, "Goodbye");
  }
  
  @Override
  public void onCompile(PluginAPI pluginAPI) {
    System.out.println("Plugin listened to onCompile!");
  }
  
  @Override
  public void onUpload(PluginAPI pluginAPI) {
    System.out.println("Plugin listened to onUpload!");
  }
  
  @Override
  public void onFormat(PluginAPI pluginAPI) {
    System.out.println("Plugin listened to onFormat!");
  }
  
  @Override
  public void onNewLine(PluginAPI pluginAPI) {
    System.out.println("Plugin listened to onNewLine!");
  }
  
  @Override
  public void onMenu(PluginAPI pluginAPI) {
    JMenu testmenu = new JMenu("Test");
    testmenu.add(new JMenuItem("Item 1"));
    pluginAPI.addMenu(testmenu);
    System.out.println("Menu added!");
  }
  
  @Override
  public void onSide(PluginAPI pluginAPI) {
    JPanel tabPanel = new JPanel(new BorderLayout());
    tabPanel.add(new JButton("my button"), BorderLayout.CENTER);
    
    pluginAPI.addSideTab("Test tab", tabPanel);
  }
}