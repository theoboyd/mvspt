package gui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.LinkedList;

import util.ResultRow;

public class Launcher {

  public static void main(String[] args) throws IOException {
    boolean guiMode = false;
    boolean verboseMode = false;
    boolean statsMode = false;
    boolean matrixMode = true;
    boolean latexMode = true; // Print results to LaTeX format (defaults to HTML table otherwise)

    int statsSample = 1;
    String statsFilePath = "MVSPT-results.txt";

    for (String arg : args) {
      if (arg.equals("-g")) {
        guiMode = true;
      } else if (arg.equals("-v")) {
        verboseMode = true;
      } else if (arg.equals("-s")) {
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
    System.out.println("GUI mode (-g): " + (guiMode ? "yes" : "no") + ", verbose mode (-v): "
        + (verboseMode ? "yes" : "no") + ", stats mode (-s [sample-size] [output-file]): "
        + (statsMode ? ("yes [" + statsSample + "] [" + statsFilePath + "].") : "no"));
    MVSPT mvspt = new MVSPT(guiMode, verboseMode, statsMode, latexMode, statsFilePath);

    if (guiMode) {
      javax.swing.SwingUtilities.invokeLater(mvspt);
    } else if (statsMode) {
      if (matrixMode) {
        // Save results in an aggregated matrix
        String startTag = (latexMode ? "" : "<tr>\n<td>\n");
        String sepTag = (latexMode ? " & " : "\n</td>\n<td>\n");
        String endTag = (latexMode ? " \\\\ \\hline" : "\n</td>\n</tr>");

        LinkedList<ResultRow> dataMatrix = new LinkedList<ResultRow>();
        mvspt.initResultRowMatrix(dataMatrix, mvspt.strategies);
        for (int i = 0; i < statsSample; i++) {
          dataMatrix = updateMatrix(mvspt.runTournament(), dataMatrix);
          if ((i + 2) % 10 == 1) {
            System.out.println(i + 1);
          }
        }
        
        // Print out aggregates
        String output = startTag + "Name" + sepTag + "Author" + sepTag + "Latest Lamda" + sepTag + "Aggregate Lamda"
            + sepTag + "Agg. Material Score" + sepTag + "Agg. Social Score" + sepTag + "Agg. Overall Score" + endTag;
        System.out.println(output);
        for (ResultRow rr : dataMatrix) {
          System.out.println(rr.toString(true));
        }
      } else {
        // Write to file for statistical analysis
        ByteBuffer bbuf = null;
        String dataString = "";
        File file = new File(statsFilePath);
        // Writable file channel
        FileChannel wChannel = new FileOutputStream(file, true).getChannel();
        for (int i = 0; i < statsSample; i++) {
          dataString = mvspt.runTournament().toString();
          bbuf = ByteBuffer.wrap(dataString.getBytes());
          wChannel.write(bbuf);
          if ((i + 2) % 10 == 1) {
            System.out.println(i + 1);
          }
        }
        wChannel.close();
      }
    } else {
      mvspt.runTournament();
    }
  }

  private static LinkedList<ResultRow> updateMatrix(LinkedList<ResultRow> newMatrixData,
      LinkedList<ResultRow> existingMatrixData) {
    // Update each result row, adding the new results
    for (ResultRow r1 : newMatrixData) {
      for (ResultRow r2 : existingMatrixData) {
        if (r1.name.equals(r2.name)) {
          r2.latestLambda = r1.latestLambda; // Only value that is not an aggregation
          r2.aggregateLambda += r1.aggregateLambda;
          r2.aggregateMaterial += r1.aggregateMaterial;
          r2.aggregateSocial += r1.aggregateSocial;
          r2.aggregateOverall += r1.aggregateOverall;
          r2.counter += r1.counter;
          r2.lambdaCounter += r1.lambdaCounter;
        }
      }
    }

    return existingMatrixData;
  }
}