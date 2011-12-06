package strategy;

import util.Lambda;
import util.Response;

public class Bayesian extends Strategy {

  public Bayesian() {
    super();
    // Use a Gaussian (mean 0) + 0.5 to offset it
    lambda = new Lambda(rand.nextGaussian() + 0.5);
  }

  @Override
  public Response respond() {
    if (getRoundsPlayed() == 0) {
      // First round
      return Response.C;
    } else {
      return Response.D;
    }
  }

  @Override
  public String name() {
    return "Bayesian";
  }

  @Override
  public String author() {
    return "Ali Ghoroghi";
  }

}
