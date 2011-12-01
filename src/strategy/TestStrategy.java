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
    return Response.C;
  }

  @Override
  public String name() {
    return "Test Strategy";
  }

  @Override
  public String author() {
    return "Theodore Boyd";
  }
}
