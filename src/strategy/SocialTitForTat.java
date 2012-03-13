package strategy;

import util.Lambda;
import util.Response;

public class SocialTitForTat extends Strategy {

  public SocialTitForTat() {
    super();
    lambda = new Lambda(rand.nextDouble());
  }

  @Override
  public Response respond() {
    if (getRoundsPlayed() == 0) {
      // First round
      lambda.noChange();
      return Response.C;
    } else {
      // Not the first round
      if (getLastResponsePair().get(1) == Response.C) {
        // If the opponent last cooperated
        lambda.incrementValue();
        return Response.C;
      } else {
        // The opponent last defected
        lambda.decrementValue();
        return Response.D;
      }
    }
  }

  @Override
  public String name() {
    return "Social Tit For Tat";
  }

  @Override
  public String author() {
    return "In-house (Theodore Boyd)";
  }

}
