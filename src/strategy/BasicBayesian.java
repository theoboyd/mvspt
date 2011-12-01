package strategy;

import util.Lambda;
import util.Response;

public class BasicBayesian extends Strategy {

  public BasicBayesian() {
    super();
    lambda = new Lambda(0.5);
  }

  @Override
  public Response respond() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String name() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String author() {
    // TODO Auto-generated method stub
    return null;
  }

}
