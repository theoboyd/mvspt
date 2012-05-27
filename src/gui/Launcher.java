package gui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class Launcher {

  public static void main(String[] args) throws IOException {
    boolean guiMode = false;
    boolean verboseMode = false;
    boolean statsMode = false;
    int statsSample = 1;
    String statsFilePath = "MVSPT-results.txt";

    for (String arg : args) {
      if (arg.equals("g")) {
        guiMode = true;
      } else if (arg.equals("v")) {
        verboseMode = true;
      } else if (arg.equals("s")) {
        statsMode = true;
      } else {
        try {
          statsSample = Integer.parseInt(arg);
        } catch (NumberFormatException e) {
          statsFilePath = arg.trim();
        }
      }
    }

    System.out.println("Launching MVSPT.");
    System.out.println("GUI mode (g): " + (guiMode ? "yes" : "no") + ", verbose mode (v): "
        + (verboseMode ? "yes" : "no") + ", stats mode (s [sample-size output-file]): " + (statsMode ? "yes" : "no") + " ["
        + statsSample + " " + statsFilePath + "].");
    MVSPT mvspt = new MVSPT(guiMode, verboseMode, statsMode, statsFilePath);

    if (guiMode) {
      javax.swing.SwingUtilities.invokeLater(mvspt);
    } else if (statsMode) {
      
      // Write to file for statistical analysis
      ByteBuffer bbuf = null;
      String data = "";
      File file = new File(statsFilePath);
      // Writable file channel
      FileChannel wChannel = new FileOutputStream(file, true).getChannel();
      
      for (int i = 0; i < statsSample; i++) {
        //System.out.print((i + 1) + " ");
        data = mvspt.runTournament();
        bbuf = ByteBuffer.wrap(data.getBytes());
        wChannel.write(bbuf);
        
        if ((i + 2) % 10 == 1) {
          System.out.println(i + 1);
        }
      }
      
      wChannel.close();
      
    } else {
      mvspt.runTournament();
    }
  }
}