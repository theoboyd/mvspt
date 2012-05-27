package strategy;

import util.Lambda;
import util.NashEquilibrium;
import util.Response;

public class ExtendedTitForTat extends Strategy {

  public ExtendedTitForTat() {
    super();
    lambda = new Lambda(0.5);
  }

  @Override
  public Response respond() {
    if (getRoundsPlayed() == 0 || getRoundsPlayed() == 1) {
      // First or second round
      lambda.noChange();
      return Response.C;
    } else if (getRoundsPlayed() % 10 < 4) {
      lambda.incrementValue();
      return Response.C;
    } else if (getRoundsPlayed() % 2 == 0) {
      lambda.decrementValue();
      return Response.D;
    } else {
      // Not the first or second round
      if (getLastResponsePair().get(1) == Response.C) {
        // If the opponent last cooperated
        lambda.incrementValue();
      } else {
        // The opponent last defected
        lambda.decrementValue();
      }
      return NashEquilibrium.getEquilibrium(lambda);
    }
  }

  @Override
  public String name() {
    return "Extended Tit For Tat";
  }

  @Override
  public String author() {
    return "In-house (Theodore Boyd)";
  }

}
