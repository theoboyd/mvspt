package strategy;

import util.Lambda;
import util.NashEquilibrium;
import util.Response;

public class NonsensePeople extends Strategy {

  public NonsensePeople() {
    super();
    lambda = new Lambda(rand.nextDouble());
  }

  @Override
  public Response respond() {
    lambda.noChange();
    return NashEquilibrium.getEquilibrium(lambda);
  }

  @Override
  public String name() {
    return "Nonsense People";
  }

  @Override
  public String author() {
    return "In-house (Ali Ghoroghi)";
  }
}