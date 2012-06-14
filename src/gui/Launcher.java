package gui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.LinkedList;

import util.GameSettings;
import util.ResultRow;

public class Launcher {

  public static void main(String[] args) throws IOException {
    boolean guiMode = false;
    boolean verboseMode = false;
    boolean statsMode = false;
    boolean matrixMode = true;
    boolean latexMode = true; // Print results to LaTeX format (defaults to HTML table otherwise)
    boolean optimalTesterMode = false; // Run through all possibilities to find an optimal set of values for RL

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
      // Save results in an aggregated matrix
      String startTag = (latexMode ? "" : "<tr>\n<td>\n");
      String sepTag = (latexMode ? " & " : "\n</td>\n<td>\n");
      String endTag = (latexMode ? " \\\\ \\hline" : "\n</td>\n</tr>");

      LinkedList<ResultRow> dataMatrix = new LinkedList<ResultRow>();
      mvspt.initResultRowMatrix(dataMatrix, mvspt.strategies);

      if (optimalTesterMode) {
        ByteBuffer bbuf = null;
        String dataString = "";
        

        for (double gamma = 0.0; gamma <= 1.0; gamma += 0.2) {
          // For each possible global social coefficient...
          File file = new File(String.valueOf(gamma) + "-" + statsFilePath);
          // Writable file channel
          FileChannel wChannel = new FileOutputStream(file, true).getChannel();
          
          GameSettings.GAMMA = gamma;
          for (double coopRatio = 0.0; coopRatio <= 1.0; coopRatio += 0.5) {
            for (double learningRate = 0.0; learningRate <= 1.0; learningRate += 0.5) {
              for (double discountFactor = 0.0; discountFactor <= 1.0; discountFactor += 0.5) {
                for (double ccLambdaA = 0.0; ccLambdaA <= 10.0; ccLambdaA += 2) {
                  for (double ccLambdaB = 0.0; ccLambdaB <= 10.0; ccLambdaB += 2) {
                    for (double cdLambda = 0.0; cdLambda <= 10.0; cdLambda += 2) {
                      for (double dcLambda = 0.0; dcLambda <= 10.0; dcLambda += 2) {
                        for (double ddLambda = 0.0; ddLambda <= 10.0; ddLambda += 2) {
                          GameSettings.RL_coopRatio = coopRatio;
                          GameSettings.RL_learningRate = learningRate;
                          GameSettings.RL_discountFactor = discountFactor;
                          GameSettings.RL_ccLambdaA = ccLambdaA;
                          GameSettings.RL_ccLambdaB = ccLambdaB;
                          GameSettings.RL_cdLambda = cdLambda;
                          GameSettings.RL_dcLambda = dcLambda;
                          GameSettings.RL_ddLambda = ddLambda;

                          String toPrint = "\n==============\ngamma: " + gamma + ", cR: " + coopRatio + ", lR: "
                              + learningRate + ", dF: " + discountFactor + ", ccA: " + ccLambdaA + ", ccB: "
                              + ccLambdaB + ", cd: " + cdLambda + ", dc: " + dcLambda + ", dd: " + ddLambda + "\n";
                          bbuf = ByteBuffer.wrap(toPrint.getBytes());
                          wChannel.write(bbuf);
                          
                          for (int i = 0; i < statsSample; i++) {
                            LinkedList<Object> results = mvspt.runTournament();
                            LinkedList<ResultRow> resultsMatrix = (LinkedList<ResultRow>) results.getFirst();
                            String winners = (String) results.getLast() + "\n";
                            bbuf = ByteBuffer.wrap(winners.getBytes());
                            wChannel.write(bbuf);

                            dataMatrix = updateMatrix(resultsMatrix, dataMatrix);
                            if ((i + 2) % 10 == 1) {
                              System.out.println(i + 1);
                            }
                          }

                          // Print out aggregates
                          for (ResultRow rr : dataMatrix) {
                            dataString = rr.toString(true) + "\n";
                            bbuf = ByteBuffer.wrap(dataString.getBytes());
                            wChannel.write(bbuf);
                          }
                        }
                      }
                    }
                  }
                }
              }
            }
          }
          wChannel.close();
        }
      } else if (matrixMode) {
        for (int i = 0; i < statsSample; i++) {
          dataMatrix = updateMatrix((LinkedList<ResultRow>) mvspt.runTournament().getFirst(), dataMatrix);
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