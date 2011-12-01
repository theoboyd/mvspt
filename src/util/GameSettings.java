package util;

public class GameSettings {
  /**
   * Social payoffs
   * M > M' and M > (R+P)/2.
   */
  public static final double M = 2.5; // "M" moral reward
  public static final double Mp = 0; // "M prime" moral punishment
  
  
  /**
   * Material payoffs
   * T > R > P > S and R > (T+S)/2
   */
  public static final double P = 1; // "Punishment" for mutual defection
  public static final double R = 3; // "Reward" for mutual cooperation
  public static final double S = 0; // "Sucker"'s payoff
  public static final double T = 5; // "Temptation" to defect payoff
  
  /**
   * Default constants
   */
  public static final String SOCIAL_CURRENCY = "points";
  public static final String MATERIAL_CURRENCY = "points";
  public static final double MINIMUM_LAMBDA = 0.0;
  public static final double MAXIMUM_LAMBDA = 1.0;
  public static final double INCREMENT_LAMBDA = 0.2;
}
