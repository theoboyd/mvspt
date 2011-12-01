package strategy;

import util.Lambda;
import util.Response;

/**
 * Always returns a cooperate response
 * 
 * @author Theo
 */
public class AlwaysCooperates extends Strategy {

  public AlwaysCooperates() {
    super();
    lambda = new Lambda(1.0);
  }
  
  @Override
  public Response respond() {
    return Response.C;
  }

  @Override
  public String name() {
    return "Always Cooperates";
  }

  @Override
  public String author() {
    return "Theodore Boyd";
  }
}
