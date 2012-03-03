package gui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JPanel;

class ButtonClick implements MouseListener {

  private MVSPT gui;

  ButtonClick(MVSPT gui) {
    this.gui = gui;
  }

  public void mouseClicked(MouseEvent e) {

    if (e.getButton() == MouseEvent.BUTTON1) { // Left click
      JButton b = (JButton) e.getSource();
      JPanel j = (JPanel) b.getParent();
      if (b.getText() == "Step") {
        // gui.step();
      } else if (b.getText() == "Run") {
        b.setText("Pause");
        j.getComponent(0).setEnabled(false);
        j.getComponent(1).setEnabled(true);
        j.getComponent(2).setEnabled(false);
        gui.running = true;
        gui.runSteps();
      } else if (b.getText() == "Pause") {
        b.setText("Run");
        j.getComponent(0).setEnabled(true);
        j.getComponent(1).setEnabled(true);
        j.getComponent(2).setEnabled(true);
        gui.running = false;
      } else if (b.getText() == "Quit") {
        gui.quit();
      } else if (b.getText() == "Edit Variables") {
        gui.edit();
      } else if (b.getText() == "Run Tournament") {
        gui.runTournament();
      }
    }
  }

  public void mouseEntered(MouseEvent e) {}

  public void mouseExited(MouseEvent e) {}

  public void mousePressed(MouseEvent e) {}

  public void mouseReleased(MouseEvent e) {}
}