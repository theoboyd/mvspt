package strategy;

import util.Lambda;
import util.Response;

public class TitForTat extends Strategy {

  public TitForTat() {
    super();
    lambda = new Lambda(0);
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
        lambda.noChange();
        return Response.C;
      } else {
        // The opponent last defected
        lambda.noChange();
        return Response.D;
      }
    }
  }

  @Override
  public String name() {
    return "Tit For Tat";
  }

  @Override
  public String author() {
    return "Theodore Boyd";
  }

}
