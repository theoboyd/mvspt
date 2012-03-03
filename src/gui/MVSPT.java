package gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.TextArea;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import strategy.Strategy;
import util.GameSettings;
import util.JavaExtensions;
import util.StrategyCollection;
import util.StrategyException;

public class MVSPT extends JFrame implements Runnable {

  private static final long               serialVersionUID = 4794145998591358565L;
  private int                             width            = 1440;
  private int                             height           = 90;
  public boolean                          running          = false;
  private StrategyCollection              sc;
  private List<Class<? extends Strategy>> strategies;
  private List<Score>                     scores;
  private List<String>                    output           = new LinkedList<String>(); // Displayable
  private int                             speed            = 200;                     // Iter. speed in ms
  private int                             allRunsMade      = 0;                       // Full running tot.
  private boolean                         guiMode          = false;
  public boolean                          verboseMode      = false;                   // Print debug

  public MVSPT(boolean guiMode, boolean verboseMode) {
    this.guiMode = guiMode;
    this.verboseMode = verboseMode;
    initialise();
    if (guiMode) {
      startGUI();
    }
  }

  private void initialise() {
    sc = new StrategyCollection();
    strategies = sc.getStrategies();
    scores = new LinkedList<Score>();
  }

  /**
   * Executes one complete tournament
   */
  public void tournament() {
    scores.clear();
    for (int i = 0; i < strategies.size(); i++) {
      for (int j = 0; j < strategies.size(); j++) {
        // Play strategy i and strategy j against each other
        Strategy strategy = null;
        Strategy opponent = null;
        try {
          strategy = (Strategy) strategies.get(i).newInstance();
          opponent = (Strategy) strategies.get(j).newInstance();
        } catch (InstantiationException e2) {
          e2.printStackTrace();
        } catch (IllegalAccessException e2) {
          e2.printStackTrace();
        }
        strategy.setOpponent(opponent);
        opponent.setOpponent(strategy);
        Score si = new Score(strategy.name(), strategy.author(), opponent.name());
        // Score sj = new Score(opponent.name(), opponent.author());
        while (si.runsMade < GameSettings.N) {
          try {
            strategy.play();
          } catch (StrategyException e1) {
            e1.printStackTrace();
            stop(true); // Forced stop
          }
          try {
            opponent.play();
          } catch (StrategyException e1) {
            e1.printStackTrace();
            stop(true); // Forced stop
          }
          strategy.calculateScore();
          opponent.calculateScore();
          si.runsMade++;
          allRunsMade++;
          // sj.runsMade++;

          if (verboseMode) {
            addOutput(strategy.name() + " vs " + opponent.name());
            // addOutput(strategy.getLastResponsePair());
            addOutput(strategy.getLastResponsePair().toString());
            addOutput(opponent.getLastResponsePair().toString());
            try {
              addOutput(strategy.name() + " [Author: " + strategy.author() + ", Social: " + strategy.getSocialScore()
                  + " points (public coeff: " + strategy.getPublicLambda() + "), Material: "
                  + strategy.getMaterialScore() + " points] played " + strategy.getLastResponsePair().get(0)
                  + " against " + opponent.name() + " (who played " + strategy.getLastResponsePair().get(1) + ")");
            } catch (Exception e) {
              addOutput("Invalid strategy: " + strategy.name() + " by " + strategy.author()
                  + ". Please check. Error message was: " + e.getMessage());
            }
            addOutput("");
          }

        }
        si.lambda = strategy.getPublicLambda();
        si.materialScore = strategy.getMaterialScore();
        si.socialScore = strategy.getSocialScore();
        // sj.lambda = opponent.getPublicLambda();
        // sj.materialScore = opponent.getMaterialScore();
        // sj.socialScore = opponent.getSocialScore();
        scores.add(si);
        // scores.add(sj);
      }
    }
  }

  private void updateWinnerDisplay() {
    Score socialWinner = sc.getSocialWinner(scores);
    Score materialWinner = sc.getMaterialWinner(scores);
    Score overallWinner = sc.getOverallWinner(scores);
    String winners = "Social winner: " + socialWinner.name + "(" + socialWinner.socialScore
        + "points), material winner: " + materialWinner.name + "(" + materialWinner.materialScore
        + "points), overall winner: " + overallWinner.name + "(" + StrategyCollection.overallScore(overallWinner)
        + "points)";
    if (guiMode) ((JLabel) JavaExtensions.getComponentById(this, "mainLabel")).setText(winners);
    addOutput(winners);
  }

  public void runTournament() {
    tournament();
    printSummary();
    allRunsMade = 0;
  }

  private void printSummary() {
    addOutput(allRunsMade + " runs made, with each round consisting of " + scores.get(0).runsMade + " games.");
    DecimalFormat df = new DecimalFormat("0.00");
    List<Score> readScores = new LinkedList<Score>();
    readScores.addAll(scores);
    List<Score> uniqueScores = new LinkedList<Score>();
    Map<String, Score> uniqueScoreMap = new HashMap<String, Score>();

    for (Class<? extends Strategy> c : strategies) {
      Strategy s = null;
      try {
        s = c.newInstance();
      } catch (InstantiationException e) {
        e.printStackTrace();
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      }
      uniqueScoreMap.put(s.name() + ":" + s.author(), new Score(s.name(), s.author(), ""));
    }

    for (Entry<String, Score> item : uniqueScoreMap.entrySet()) {
      for (Score s : scores) {
        if (item.getKey().toString().equals(s.name + ":" + s.author)) {
          // Got a matching strategy run, merging the two scores
          item.getValue().materialScore += s.materialScore;
          item.getValue().socialScore += s.socialScore;
          item.getValue().lambda += s.lambda;
          item.getValue().aggregates++;
        }
      }
    }

    for (Entry<String, Score> item : uniqueScoreMap.entrySet()) {
      double newLambda = item.getValue().lambda / item.getValue().aggregates; // Lambda as average
      uniqueScores.add(new Score(item.getValue().name, item.getValue().author, item.getValue().opponentName, newLambda,
          item.getValue().materialScore, item.getValue().socialScore));
    }

    addOutput("=== Name, Author, (Avg.) Ending Lambda, Material Score, Social Score, Overall Score ===");
    if (verboseMode) {
      addOutput("=== Individual runs ===");
      for (Score s : scores) {
        addOutput(s.name + " (vs. " + s.opponentName + ") & " + s.author + " & " + df.format(s.lambda) + " & "
            + df.format(s.materialScore) + " & " + df.format(s.socialScore) + " & "
            + df.format(StrategyCollection.overallScore(s)) + " \\\\ \\hline");
      }
    }
    addOutput("=== Totals: ===");
    for (Score s : uniqueScores) {
      addOutput(s.name + " & " + s.author + " & " + df.format(s.lambda) + " & " + df.format(s.materialScore) + " & "
          + df.format(s.socialScore) + " & " + df.format(StrategyCollection.overallScore(s)) + " \\\\ \\hline");
    }
    addOutput("=== Winners: ===");
    updateWinnerDisplay();
  }

  private void addOutput(String line) {
    if (guiMode) {
      output.add(line);
      ((JLabel) JavaExtensions.getComponentById(this, "mainTextArea")).setText(JavaExtensions.join(output, "\n"));
    } else {
      System.out.println(line);
    }
  }

  /*
   * GUI-specific code follows only
   */

  public void startGUI() {
    // Frame
    setName("Life");
    setSize(width, height);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // To terminate

    // Buttons (other than cells)
    ButtonClick bc = new ButtonClick(this);

    JButton step = new JButton();
    step.setText("Step");
    step.addMouseListener(bc);

    JButton run = new JButton();
    run.setText("Run");
    run.addMouseListener(bc);

    JButton runTournament = new JButton();
    runTournament.setText("Run Tournament");
    runTournament.addMouseListener(bc);

    JButton edit = new JButton();
    edit.setText("Edit Variables");
    edit.addMouseListener(bc);

    JButton quit = new JButton();
    quit.setText("Quit");
    quit.addMouseListener(bc);

    // Containers
    Container controlContainer = new JPanel();
    controlContainer.add(step);
    controlContainer.add(run);
    controlContainer.add(runTournament);
    controlContainer.add(edit);
    controlContainer.add(quit);

    JLabel mainLabel = new JLabel("", SwingConstants.CENTER);
    mainLabel.setName("mainLabel");
    JLabel secondaryLabel = new JLabel("", SwingConstants.CENTER);
    secondaryLabel.setName("secondaryLabel");
    TextArea mainTextArea = new TextArea();
    mainTextArea.setName("mainTextArea");

    Container mainContainer = new JPanel(new GridLayout(1, 1));
    mainContainer.add(mainTextArea, BorderLayout.CENTER);

    Container statusContainer = new JPanel();
    statusContainer.add(mainLabel, BorderLayout.CENTER);
    statusContainer.add(secondaryLabel, BorderLayout.CENTER);

    // Labels
    mainLabel.setText("Strategies loaded: " + strategies.size() + ".");
    refreshGameSettingsDisplay();
    mainTextArea.setEditable(false);

    // Display and add containers
    add(controlContainer, BorderLayout.NORTH);
    add(mainContainer, BorderLayout.CENTER);
    add(statusContainer, BorderLayout.SOUTH);
    // pack();
    setVisible(true);
  }

  private void refreshGameSettingsDisplay() {
    ((JLabel) JavaExtensions.getComponentById(this, "secondaryLabel")).setText(JavaExtensions.join(
        JavaExtensions.fieldsIn(GameSettings.class), " "));
  }

  public void run() {
    setVisible(true);
  }

  public void runSteps() {
    Timer t = new Timer();
    t.schedule(new TimerTask() {
      // Schedule the timer to run the step() function
      public void run() {
        // if (runsMade >= runLimit) {
        // runsMade = 0;
        // running = false;
        // }
        if (running) {
          // step();
          runSteps(); // Continue to run
        }
      }
    }, speed);
  }

  public void quit() {
    stop(false);
  }

  public void stop(boolean force) {
    int result = JOptionPane.showConfirmDialog(this, "Are you sure you want to quit?", "MVSPT",
        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
    if (result == JOptionPane.YES_OPTION || force) {
      dispose(); // Destroy window
      System.exit(0); // Exit (no error)
    }
  }

  public void edit() {
    boolean invalidValue = false;
    for (Field f : GameSettings.class.getDeclaredFields()) {
      try {
        double x = Double.parseDouble(JOptionPane.showInputDialog(
            "Please enter the new value for " + f.getName() + ":", f.get(null)));
        f.set(null, x);
      } catch (IllegalArgumentException e) {
        invalidValue = true;
        break;
      } catch (IllegalAccessException e) {
        invalidValue = true;
        break;
      } catch (Exception e) {
        invalidValue = true;
        System.err.println("Unexpected input: " + e.getMessage());
        break;
      }
      if (!((GameSettings.M > GameSettings.Mp) && (GameSettings.M > (GameSettings.R + GameSettings.P) / 2)
          && (GameSettings.T > GameSettings.R) && (GameSettings.R > GameSettings.P)
          && (GameSettings.P > GameSettings.S) && (GameSettings.R > (GameSettings.T + GameSettings.S) / 2))
          && (GameSettings.MIN_LAMBDA >= 0)
          && (GameSettings.MIN_LAMBDA < GameSettings.MAX_LAMBDA)
          && (GameSettings.MAX_LAMBDA > GameSettings.MIN_LAMBDA)
          && (GameSettings.MAX_LAMBDA <= 1)
          && (GameSettings.INCR_LAMBDA > 0) && (GameSettings.INCR_LAMBDA < GameSettings.MAX_LAMBDA)) {
        invalidValue = true;
      }
    }
    refreshGameSettingsDisplay();
    if (invalidValue) {
      JOptionPane.showMessageDialog(this, "Invalid value encountered. Please remember these rules:\n"
          + "M > M'\nM > (R+P)/2\nT > R > P > S\nR > (T+S)/2\n"
          + "0 <= MIN_LAMBDA < MAX_LAMBDA <= 1\n0 < INCR_LAMBDA < MAX_LAMBDA", "MVSPT", JOptionPane.WARNING_MESSAGE);
      edit();
    }
  }
}
