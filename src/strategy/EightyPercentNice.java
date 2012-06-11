package strategy;

import util.Lambda;
import util.NashEquilibrium;
import util.Response;

public class EightyPercentNice extends Strategy {
  public EightyPercentNice() {
    super();
    lambda = new Lambda(0.8);
  }

  @Override
  public Response respond() {
    lambda.noChange();
    return NashEquilibrium.getEquilibrium(lambda);
  }

  @Override
  public String name() {
    return "Eighty Percent Nice";
  }

  @Override
  public String author() {
    return "Xiuyi Fan";
    // Contact email: x.fan09@imperial.ac.uk
  }
}
