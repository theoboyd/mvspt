package gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.TextArea;
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
  private List<String>       output           = new LinkedList<String>();              // Displayable output
  private int                speed            = 200;                                   // Speed of iteration in ms

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

    // Containers
    Container topContainer = new JPanel();
    topContainer.add(mainLabel, BorderLayout.CENTER);
    topContainer.add(secondaryLabel, BorderLayout.CENTER);

    Container controlContainer = new JPanel();

    controlContainer.add(step);
    controlContainer.add(run);
    controlContainer.add(stop);

    Container bottomContainer = new JPanel(new GridLayout(1, 1));
    bottomContainer.add(mainTextArea, BorderLayout.CENTER);

    // Labels
    sc = new StrategyCollection();
    strategies = sc.getStrategies();
    mainLabel.setText("Strategies loaded: " + strategies.size() + ".");
    secondaryLabel.setText(JavaExtensions.join(JavaExtensions.itemsIn(GameSettings.class), " "));
    mainTextArea.setEditable(false);

    // Display and add containers
    add(controlContainer, BorderLayout.NORTH);
    add(bottomContainer, BorderLayout.CENTER);
    add(topContainer, BorderLayout.SOUTH);
    pack();
    setVisible(true);
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
          addOutput(strategy.name() + " [Author: " + strategy.author() + ", Social: " + strategy.socialScore + " "
              + GameSettings.SOCIAL_CURRENCY + " (public coeff: " + strategy.getPublicLambda() + "), Material: "
              + strategy.materialScore + " " + GameSettings.MATERIAL_CURRENCY + "] played "
              + strategy.getLastResponsePair().get(0) + " against " + opponent.name() + " (who played "
              + strategy.getLastResponsePair().get(1) + ")");
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
    mainLabel.setText("Social winner: " + socialWinner.name() + "(" + socialWinner.materialScore
        + "points), material winner: " + materialWinner.name() + "(" + materialWinner.materialScore + "points)");
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

  private void addOutput(String line) {
    output.add(line);
    mainTextArea.setText(JavaExtensions.join(output, "\n"));
  }
}
