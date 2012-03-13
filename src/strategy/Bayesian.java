package strategy;

import util.Lambda;
import util.NashEquilibrium;
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
      lambda.noChange();
    } else {
      double predictedOppLambda = rand.nextGaussian() + 0.5;
      if (predictedOppLambda < lambda.getValue()) {
        lambda.decrementValue();
      } else if (predictedOppLambda > lambda.getValue()) {
        lambda.incrementValue();
      }
    }
    return NashEquilibrium.getEquilibrium(lambda);
  }

  @Override
  public String name() {
    return "Bayesian";
  }

  @Override
  public String author() {
    return "In-house (Ali Ghoroghi)";
  }

}
