package strategy;

import util.Lambda;
import util.Response;

/**
 * Always returns a cooperate response
 */
public class AlwaysCooperates extends Strategy {

  public AlwaysCooperates() {
    super();
    lambda = new Lambda(1.0);
  }

  @Override
  public Response respond() {
    lambda.noChange();
    return Response.C;
  }

  @Override
  public String name() {
    return "Always Cooperates";
  }

  @Override
  public String author() {
    return "In-house (Theodore Boyd)";
  }
}
