package gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.TextArea;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;
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

public class AppletGUI extends JFrame implements Runnable {

  private static final long  serialVersionUID = 4794145998591358565L;
  private int                width            = 1440;
  private int                height           = 900;
  JLabel                     mainLabel        = new JLabel("", SwingConstants.CENTER);
  JLabel                     secondaryLabel   = new JLabel("", SwingConstants.CENTER);
  TextArea                   mainTextArea     = new TextArea();
  public boolean             running          = false;
  private StrategyCollection sc;
  private List<Strategy>     strategies;
  private List<String>       output           = new LinkedList<String>();             // Displayable output
  private int                speed            = 200;                                  // Speed of iteration in ms

  public AppletGUI() {
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

    JButton stop = new JButton();
    stop.setText("Stop");
    stop.addMouseListener(bc);

    JButton edit = new JButton();
    edit.setText("Edit");
    edit.addMouseListener(bc);

    // Containers
    Container controlContainer = new JPanel();
    controlContainer.add(step);
    controlContainer.add(run);
    controlContainer.add(stop);

    Container mainContainer = new JPanel(new GridLayout(1, 1));
    mainContainer.add(mainTextArea, BorderLayout.CENTER);

    Container statusContainer = new JPanel();
    statusContainer.add(mainLabel, BorderLayout.CENTER);
    statusContainer.add(secondaryLabel, BorderLayout.CENTER);
    statusContainer.add(edit, BorderLayout.EAST);

    // Labels
    sc = new StrategyCollection();
    strategies = sc.getStrategies();
    mainLabel.setText("Strategies loaded: " + strategies.size() + ".");
    updateSecondaryLabel();
    mainTextArea.setEditable(false);

    // Display and add containers
    add(controlContainer, BorderLayout.NORTH);
    add(mainContainer, BorderLayout.CENTER);
    add(statusContainer, BorderLayout.SOUTH);
    // pack();
    setVisible(true);
  }

  private void updateSecondaryLabel() {
    secondaryLabel.setText(JavaExtensions.join(JavaExtensions.fieldsIn(GameSettings.class), " "));
  }

  public void run() {
    setVisible(true);
  }

  /**
   * Executes one step of the tournament
   */
  public void step() {
    for (int i = 0; i < strategies.size(); i++) {
      for (int j = i; j < strategies.size(); j++) {
        // Play strategy i and strategy j against each other
        Strategy strategy = strategies.get(i);
        Strategy opponent = strategies.get(j);
        strategy.setOpponent(opponent);
        strategy.play();
        opponent.setOpponent(strategy);
        opponent.play();

        // TODO remove console output
        System.out.println(strategy.name() + " vs " + opponent.name());
        // System.out.println(strategy.getLastResponsePair());
        System.out.println(strategy.getLastResponsePair());
        System.out.println(opponent.getLastResponsePair());

        try {
          addOutput(strategy.name() + " [Author: " + strategy.author() + ", Social: " + strategy.getSocialScore()
              + " points (public coeff: " + strategy.getPublicLambda() + "), Material: " + strategy.getMaterialScore()
              + " points] played " + strategy.getLastResponsePair().get(0) + " against " + opponent.name()
              + " (who played " + strategy.getLastResponsePair().get(1) + ")");
        } catch (StrategyException e) {
          addOutput("Invalid strategy: " + strategy.name() + " by " + strategy.author()
              + ". Please check. Error message was: " + e.getMessage());
        }

        System.out.println("");

      }
    }
    updateWinnerDisplay();
  }

  private void updateWinnerDisplay() {
    Strategy socialWinner = sc.getSocialWinner();
    Strategy materialWinner = sc.getMaterialWinner();
    mainLabel.setText("Social winner: " + socialWinner.name() + "(" + socialWinner.getSocialScore()
        + "points), material winner: " + materialWinner.name() + "(" + materialWinner.getMaterialScore() + "points)");
  }

  public void runSteps() {
    Timer t = new Timer();
    t.schedule(new TimerTask() {
      // Schedule the timer to run the step() function
      public void run() {
        if (running) {
          step();
          runSteps(); // Continue to run
        }
      }
    }, speed);
  }

  public void stop() {
    int result = JOptionPane.showConfirmDialog(this, "Are you sure you want to quit?", "MVSPT",
        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
    if (result == JOptionPane.YES_OPTION) {
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
    updateSecondaryLabel();
    if (invalidValue) {
      JOptionPane.showMessageDialog(this, "Invalid value encountered. Please remember these rules:\n"
          + "M > M'\nM > (R+P)/2\nT > R > P > S\nR > (T+S)/2)\n"
          + "0 <= MIN_LAMBDA < MAX_LAMBDA <= 1\n0 < INCR_LAMBDA < MAX_LAMBDA", "MVSPT", JOptionPane.WARNING_MESSAGE);
      edit();
    }
  }

  private void addOutput(String line) {
    output.add(line);
    mainTextArea.setText(JavaExtensions.join(output, "\n"));
  }
}
