package strategy;

import util.Lambda;
import util.NashEquilibrium;
import util.Response;

public class ABitNicer extends Strategy {
  int myCtr, oppCtr;

  public ABitNicer() {
    super();
    lambda = new Lambda(0.1);
    myCtr = 0;
    oppCtr = 0;
  }

  @Override
  public Response respond() {
    if (getRoundsPlayed() != 0) {
      Response myHistoryRound_1 = getLastResponsePair().get(0);
      Response oppHistoryRound_1 = getLastResponsePair().get(1);

      if (myHistoryRound_1 == Response.C) myCtr++;

      if (oppHistoryRound_1 == Response.C) oppCtr++;

      if (myCtr < oppCtr + 10) lambda.incrementValue();
      else if (myCtr > oppCtr + 20) lambda.decrementValue();
      else lambda.noChange();
    } else {
      lambda.noChange();
    }

    Response r = NashEquilibrium.getEquilibrium(lambda);
    return r;
  }

  @Override
  public String name() {
    return "A Bit Nicer";
  }

  @Override
  public String author() {
    return "Xiuyi Fan";
    // Contact email: x.fan09@imperial.ac.uk
  }
}