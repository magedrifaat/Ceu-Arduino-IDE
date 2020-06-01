package processing.app;

import javax.swing.*;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ChooseProjectDialog extends JDialog implements ActionListener {
  
  String projectType;
  JButton ceuBtn, legacyBtn;
  public ChooseProjectDialog(JFrame parent) {
    super(parent, "Choose project type...", true);
    GridLayout layout = new GridLayout(0, 2);
    setLayout(layout);
    ceuBtn = new JButton("Ceu-Arduino");
    ceuBtn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        projectType = "Ceu-Arduino";
      }
    });
    ceuBtn.addActionListener(this);
    
    legacyBtn = new JButton("Legacy Arduino");
    legacyBtn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        projectType = "Legacy";
      }
    });
    legacyBtn.addActionListener(this);
    
    add(ceuBtn);
    add(legacyBtn);
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
}