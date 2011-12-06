package strategy;

import util.Lambda;
import util.Response;

public class NonsensePeople extends Strategy {

  public NonsensePeople() {
    super();
    lambda = new Lambda(rand.nextDouble());
  }

  @Override
  public Response respond() {
    if (getRoundsPlayed() == 0) {
      // First round
      return Response.C;
    } else {
      return (rand.nextDouble() < 0.5 ? Response.C : Response.D);
    }
  }

  @Override
  public String name() {
    return "Nonsense People";
  }

  @Override
  public String author() {
    return "Ali Ghoroghi";
  }
}