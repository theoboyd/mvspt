package strategy;

import util.Lambda;
import util.Response;

/**
 * Always returns a defect response
 */
public class AlwaysDefects extends Strategy {

  public AlwaysDefects() {
    super();
    lambda = new Lambda(0.0);
  }

  @Override
  public Response respond() {
    lambda.noChange();
    return Response.D;
  }

  @Override
  public String name() {
    return "Always Defects";
  }

  @Override
  public String author() {
    return "Theodore Boyd";
  }
}
