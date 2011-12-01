package strategy;

import java.util.LinkedList;
import java.util.List;

import util.DiscreteDouble;
import util.GameSettings;
import util.Response;
import util.StrategyException;

/**
 * All Prisoner's Dilemma strategies must extend this framework
 */
public abstract class Strategy {

  /**
   * A decimal value for the social coefficient, in the range [0, 1]
   */
  protected DiscreteDouble     lambda;
  private List<DiscreteDouble> lambdaHistory;       // A history of lambda values for observation
  private int                  lambdaIsBehindBy = 1; // The amount behind that public lambda is at

  /**
   * The history parameter is a list of [My play, Opponent's response] pairs in a map.
   */
  private List<List<Response>> history;

  public double                socialScore      = 0;
  public double                materialScore    = 0;
  private Strategy             opponent;

  public Strategy() {
    socialScore = 0;
    materialScore = 0;
    history = new LinkedList<List<Response>>();
    lambdaHistory = new LinkedList<DiscreteDouble>();
  }

  public void setOpponent(Strategy opponent) {
    this.opponent = opponent;
  }

  public Strategy getOpponent() {
    return opponent;
  }

  /**
   * Update's opponent's last move pair to include this strategy's last move
   */
  public void updateOpponentHistory(Response response) {
    opponent.setLastResponsePair(1, response);
  }

  /**
   * Update's this strategy's last move pair
   */
  public void updateMyHistory(Response response) {
    setLastResponsePair(0, response);
  }

  /**
   * Must return a cooperate or defect response optionally based on the history of previous plays; must update history
   * with the last move played
   */
  public abstract Response respond();

  /**
   * Safe wrapper for a complete play
   */
  public void play() {
    Response r = respond();
    updateMyHistory(r);
    updateOpponentHistory(r);
    calculateScore();
  }

  private void calculateScore() {
    Response lastResponse = getLastResponsePair().get(0);
    Response lastOpponentResponse = getLastResponsePair().get(1);
    if (lastResponse == Response.C) {
      // Cooperate, X
      if (lastOpponentResponse == Response.C) {
        // Cooperate, Cooperate
        socialScore += lambda.getValue() * GameSettings.M;
        materialScore += (1 - lambda.getValue()) * GameSettings.R;
      } else {
        // Cooperate, Defect
        socialScore += lambda.getValue() * GameSettings.M;
        materialScore += (1 - lambda.getValue()) * GameSettings.S;
      }
    } else {
      // Defect, X
      if (lastOpponentResponse == Response.C) {
        // Defect, Cooperate
        socialScore += lambda.getValue() * GameSettings.Mp;
        materialScore += (1 - lambda.getValue()) * GameSettings.T;
      } else {
        // Defect, Defect
        socialScore += lambda.getValue() * GameSettings.Mp;
        materialScore += (1 - lambda.getValue()) * GameSettings.P;
      }
    }
  }

  /**
   * Must return a string representing a printable strategy name
   */
  public abstract String name();

  /**
   * Must return a string representing the printable full name of the author
   */
  public abstract String author();

  /**
   * Returns the last move pair [Me, Opponent] of the strategy played
   */
  public List<Response> getLastResponsePair() {
    // Indexes into the last element in the history
    List<Response> lastResponsePair;
    lastResponsePair = history.get(history.size() - 1);
    return lastResponsePair;
  }

  /**
   * Sets the last move pair [Me, Opponent] of the strategy played for this strategy (0) or the opponent (0)
   */
  public void setLastResponsePair(int index, Response response) {
    boolean createNew = false; // Whether we need to create a new history item or update an existing one

    // Indexes into the last element in the history
    List<Response> lastResponsePair = null;
    try {
      // We have a last element
      lastResponsePair = history.get(history.size() - 1);
      if (lastResponsePair.get(0) != null && lastResponsePair.get(1) != null) {
        createNew = true;
      }
    } catch (Exception e) {
      // No history, we need to create the item
      createNew = true;
    }

    if (createNew) {
      List<Response> responsePair = new LinkedList<Response>();
      responsePair.add(null);
      responsePair.add(null);
      responsePair.set(index, response);
      history.add(responsePair);
    } else {
      if (lastResponsePair.get(index) == null) {
        lastResponsePair.set(index, response);
      } else {
        // We have been asked to overwrite a value
        System.err.println("Overwriting a response. Check your " + name() + " strategy.");
        lastResponsePair.set(index, response);
      }
    }
  }

  /**
   * Returns the last publicly available lambda. This is usually the one that was used for the last move. The current
   * active lambda of the strategy is private
   * 
   * @return
   * @throws StrategyException
   */
  public double getPublicLambda() throws StrategyException {
    double lambda = 0.5;
    try {
      lambda = lambdaHistory.get(lambdaHistory.size() - 1 - lambdaIsBehindBy).getValue();
    } catch (Exception e) {
      // Not enough items in history to return a lambda
    }
    return lambda;
  }
}
