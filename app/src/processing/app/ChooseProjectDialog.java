package processing.app;

import javax.swing.*;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;


public class ChooseProjectDialog extends JDialog implements ActionListener {
  
  String projectType;
  public ChooseProjectDialog(JFrame parent) {
    
    super(parent, "Choose project type...", true);
    
    JScrollPane mainPane = new JScrollPane();
    JPanel mainPanel = new JPanel();
    
    GridLayout layout = new GridLayout(1, 0);
    mainPanel.setLayout(layout);
    
    ArrayList<String> projects = getProjectTypes();
    for (String project : projects) {
      String title = PreferencesData.get(project + "-title");
      
      JButton projectBtn = new JButton(title);
      projectBtn.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          projectType = project;
        }
      });
      projectBtn.addActionListener(this);
      
      mainPanel.add(projectBtn);
    }
    
    mainPane.setViewportView(mainPanel);
    this.add(mainPane);
    
    setSize(300, 200);
    
    setLocationRelativeTo(null);
    projectType = "exit";
  }
  
  public String getProjectType() {
    return projectType;
  }
  
  @Override
  public void actionPerformed(ActionEvent e) {
    setVisible(false);
  }
  
  private ArrayList<String> getProjectTypes() {
    return new ArrayList<String> (Arrays.asList(PreferencesData.get("project-types").replaceAll(" ", "").split(",")));
  }
}
