package strategy;

import util.Lambda;
import util.NashEquilibrium;
import util.Response;

public class NashDefect extends Strategy {

  public NashDefect() {
    super();
    lambda = new Lambda(0);
  }

  @Override
  public Response respond() {
    lambda.noChange();
    return NashEquilibrium.getEquilibrium(lambda);
  }

  @Override
  public String name() {
    return "Nash Defect";
  }

  @Override
  public String author() {
    return "Xin Yan";
    // Contact email: kx108@ic.ac.uk
  }

}
