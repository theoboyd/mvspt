package util;

public class Lambda extends DiscreteDouble {
  public Lambda(double startValue) {
    super(GameSettings.MINIMUM_LAMBDA, GameSettings.MAXIMUM_LAMBDA, GameSettings.INCREMENT_LAMBDA, startValue);
  }
}
