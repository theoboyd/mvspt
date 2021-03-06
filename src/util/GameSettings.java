package util;

public class GameSettings {
  /**
   * Default reinforcement learning parameters
   */
  public static double RL_coopRatio = 0.3;
  public static double RL_learningRate = 0.1;
  public static double RL_discountFactor = 0.9;
  public static double RL_ccLambdaA = 3;
  public static double RL_ccLambdaB = 2.5;
  public static double RL_cdLambda = 2.5;
  public static double RL_dcLambda = 5;
  public static double RL_ddLambda = 1;

  /**
   * Social payoffs M > M' and M > (R+P)/2.
   */
  public static double M = 2.5; // "M" moral reward
  public static double Mp = 0; // "M prime" moral punishment

  /**
   * Material payoffs T > R > P > S and R > (T+S)/2
   */
  public static double P = 1; // "Punishment" for mutual defection
  public static double R = 3; // "Reward" for mutual cooperation
  public static double S = 0; // "Sucker"'s payoff
  public static double T = 5; // "Temptation" to defect payoff

  /**
   * Default constants
   */
  public static double MIN_LAMBDA = 0.0;
  public static double MAX_LAMBDA = 1.0;
  public static double INCR_LAMBDA = 0.2;
  public static double GAMMA = 0.5; // Global social coefficient, societal social weighting
  public static int N = 200; // Number of rounds in a game
}
