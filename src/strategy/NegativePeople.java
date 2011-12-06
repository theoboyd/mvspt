package strategy;

import util.Lambda;
import util.Response;

public class NegativePeople extends Strategy {

  public NegativePeople() {
    super();
    lambda = new Lambda(rand.nextDouble());
  }

  @Override
  public Response respond() {
    if (getRoundsPlayed() == 0) {
      // First round
      return Response.C;
    } else {
      if (getLastResponsePair().get(1) == Response.D) {
        // Opponent last defected
        lambda.decrementValue();
      }
      return Response.C;
    }
  }

  @Override
  public String name() {
    return "Negative People";
  }

  @Override
  public String author() {
    return "Ali Ghoroghi";
  }
}