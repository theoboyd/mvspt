package strategy;

import java.util.Random;

import util.Lambda;
import util.Response;

/**
 * Randomly chooses between cooperate and defect, ignoring the history
 */
public class AlwaysRandom extends Strategy {

  public AlwaysRandom() {
    super();
    lambda = new Lambda(rand.nextDouble());
  }
  
  @Override
  public Response respond() {
    Random rand = new Random();
    double random = rand.nextDouble();
    lambda.noChange();
    return (random < 0.5 ? Response.C : Response.D);
  }
  
  @Override
  public String name() {
    return "Always Random";
  }

  @Override
  public String author() {
    return "In-house (Theodore Boyd)";
  }
}