package strategy;

import util.Lambda;
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
      return Response.C;
    } else {
      if (getLastResponsePair().get(1) == Response.C) {
        // Opponent last cooperated
        lambda.incrementValue();
      }
      return Response.C;
    }
  }

  @Override
  public String name() {
    return "Positive People";
  }

  @Override
  public String author() {
    return "Ali Ghoroghi";
  }
}