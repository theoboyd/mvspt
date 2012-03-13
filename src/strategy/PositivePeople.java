package strategy;

import util.Lambda;
import util.NashEquilibrium;
import util.Response;

public class PositivePeople extends Strategy {

  public PositivePeople() {
    super();
    lambda = new Lambda(rand.nextDouble());
  }

  @Override
  public Response respond() {
    if (getRoundsPlayed() == 0) {
      // First round
      lambda.noChange();
    } else {
      if (getLastResponsePair().get(1) == Response.C) {
        // Opponent last cooperated
        lambda.incrementValue();
      } else {
        lambda.noChange();
      }
    }
    return NashEquilibrium.getEquilibrium(lambda);
  }

  @Override
  public String name() {
    return "Positive People";
  }

  @Override
  public String author() {
    return "In-house (Ali Ghoroghi)";
  }
}