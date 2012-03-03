package gui;

public class Launcher {

  public static void main(String[] args) {
    boolean guiMode = false;
    boolean verboseMode = false;
    
    for (String arg : args) {
      if (arg == "-gui") {
        guiMode = true;
      } else if (arg == "-verbose") {
        verboseMode = true;
      }
    }
    
    System.out.println("Launching MVSPT. GUI mode: (-gui) " + guiMode + ", verbose mode: (-verbose) " + verboseMode + ".");
    MVSPT mvspt = new MVSPT(guiMode, verboseMode);
    
    if (guiMode) {
      javax.swing.SwingUtilities.invokeLater(mvspt);
    } else {
      mvspt.runTournament();
    }
  }
}