package gui;

public class AppletLauncher {

  public static void main(String[] args) {
    AppletGUI appletGui = new AppletGUI();
    javax.swing.SwingUtilities.invokeLater(appletGui);
  }
}