package strategy;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import util.DiscreteHistoricDouble;
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
  protected DiscreteHistoricDouble lambda;
  private int                      lambdaIsBehindBy = 1; // The amount behind that public lambda is at

  /**
   * A random number generator for use with probabilistic strategies
   */
  protected static Random          rand;

  /**
   * A running count of the number of rounds played by this strategy
   */
  private int                      roundsPlayed;
  /**
   * The history parameter is a list of [My play, Opponent's response] pairs in a map.
   */
  private List<List<Response>>     history;

  private double                   socialScore      = 0;
  private double                   materialScore    = 0;
  private Strategy                 opponent;

  public Strategy() {
    rand = new Random();
    roundsPlayed = 0;
    setSocialScore(0);
    setMaterialScore(0);
    history = new LinkedList<List<Response>>();
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
   * with the last move played.
   */
  public abstract Response respond();

  /**
   * Safe wrapper for a complete play
   * 
   * @throws StrategyException
   */
  public void play() throws StrategyException {
    Response r = respond();
    if (r == Response.C && cooperationDisallowed()) {
      System.err.println("Cooperation was not allowed and the strategy was forced to defect.");
      throw new StrategyException(
          "Invalid strategy: attempted to cooperate after having defected and then reduced lambda.");
    } else if (r == Response.D && defectionDisallowed()) {
      System.err.println("Defection was not allowed and the strategy was forced to cooperate.");
      throw new StrategyException(
          "Invalid strategy: attempted to defect after having cooprated and then increased lambda.");
    }
    updateMyHistory(r);
    updateOpponentHistory(r);
    roundsPlayed++;
  }

  /**
   * To prevent undermining the concept of morality, defection is not permitted if they last cooperated and then
   * increased their social coefficient.
   * 
   * @return
   */
  private boolean defectionDisallowed() {
    boolean disallowed = false;
    try {
      disallowed = (getLastResponsePair().get(0) == Response.C && lambda.getValue() > lambda.getHistory().get(
          lambda.getHistory().size() - 1));
    } catch (Exception e) {
      // Could not get history item, assuming benefit of doubt
    }
    return disallowed;
  }

  /**
   * To prevent undermining the concept of morality, cooperation is not permitted if they last defected and then
   * decreased their social coefficient.
   * 
   * @return
   */
  private boolean cooperationDisallowed() {
    boolean disallowed = false;
    try {
      disallowed = (getLastResponsePair().get(0) == Response.D && lambda.getValue() < lambda.getHistory().get(
          lambda.getHistory().size() - 1));
    } catch (Exception e) {
      // Could not get history item, assuming benefit of doubt
    }
    return disallowed;
  }

  public void calculateScore() {
    Response lastResponse = getLastResponsePair().get(0);
    Response lastOpponentResponse = getLastResponsePair().get(1);
    if (lastResponse == Response.C) {
      // Cooperate, X
      if (lastOpponentResponse == Response.C) {
        // Cooperate, Cooperate
        setSocialScore(getSocialScore() + lambda.getValue() * GameSettings.M);
        setMaterialScore(getMaterialScore() + (1 - lambda.getValue()) * GameSettings.R);
      } else {
        // Cooperate, Defect
        setSocialScore(getSocialScore() + lambda.getValue() * GameSettings.M);
        setMaterialScore(getMaterialScore() + (1 - lambda.getValue()) * GameSettings.S);
      }
    } else {
      // Defect, X
      if (lastOpponentResponse == Response.C) {
        // Defect, Cooperate
        setSocialScore(getSocialScore() + lambda.getValue() * GameSettings.Mp);
        setMaterialScore(getMaterialScore() + (1 - lambda.getValue()) * GameSettings.T);
      } else {
        // Defect, Defect
        setSocialScore(getSocialScore() + lambda.getValue() * GameSettings.Mp);
        setMaterialScore(getMaterialScore() + (1 - lambda.getValue()) * GameSettings.P);
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
    List<Response> lastResponsePair = new LinkedList<Response>();
    try {
      lastResponsePair = history.get(history.size() - 1);
    } catch (Exception e) {
      if (roundsPlayed == 0) {
        //System.out.println("(Couldn't get last response pair history element as this is the first round.)");
      } else {
        System.err.println("Failed to get last response pair history element.");
      }
    }
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
   */
  public double getPublicLambda() {
    double publicLambda = -999; // Undefined
    try {
      publicLambda = lambda.getHistory().get(lambda.getHistory().size() - 1 - lambdaIsBehindBy);
    } catch (Exception e) {
      if (roundsPlayed <= lambdaIsBehindBy) {
        System.out.println("(Couldn't retrieve lambda as this is the first (+" + lambdaIsBehindBy + ") round.)");
      } else {
        System.err.println("Not enough items in history to return a lambda.");
      }
    }
    return publicLambda;
  }

  public double getMaterialScore() {
    return materialScore;
  }

  private void setMaterialScore(double materialScore) {
    this.materialScore = materialScore;
  }

  public double getSocialScore() {
    return socialScore;
  }

  private void setSocialScore(double socialScore) {
    this.socialScore = socialScore;
  }

  public int getRoundsPlayed() {
    return roundsPlayed;
  }
}
