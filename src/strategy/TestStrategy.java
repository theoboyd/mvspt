package strategy;

import util.Lambda;
import util.Response;

public class TestStrategy extends Strategy {

  public TestStrategy() {
    super();
    lambda = new Lambda(0.5);
  }

  @Override
  public Response respond() {
    lambda.noChange();
    return Response.C;
  }

  @Override
  public String name() {
    return "Basic Strategy";
  }

  @Override
  public String author() {
    return "In-house (Theodore Boyd)";
  }
}
