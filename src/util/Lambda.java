package util;

public class Lambda extends DiscreteHistoricDouble {
  public Lambda(double startValue) {
    super(GameSettings.MIN_LAMBDA, GameSettings.MAX_LAMBDA, GameSettings.INCR_LAMBDA, startValue);
  }
}