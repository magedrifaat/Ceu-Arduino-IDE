import processing.app.Plugin;
import processing.app.PluginAPI;
import java.awt.BorderLayout;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JButton;

public class TestPluginTwo implements Plugin {
  
  public TestPluginTwo() {
    
  }
  
  @Override
  public void onSide(PluginAPI pluginAPI) {
    JPanel tabPanel = new JPanel(new BorderLayout());
    tabPanel.add(new JButton("my second button"), BorderLayout.CENTER);
    
    pluginAPI.addSideTab("Test tab", tabPanel);
  }
}