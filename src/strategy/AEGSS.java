package strategy;

import util.Lambda;
import util.NashEquilibrium;
import util.Response;

public class AEGSS extends Strategy {

  public AEGSS() {
    super();
    lambda = new Lambda(0);
  }

  @Override
  public Response respond() {
    if (getRoundsPlayed() != 0) {
      Response myHistoryRound_1 = getLastResponsePair().get(0);
      Response oppHistoryRound_1 = getLastResponsePair().get(1);

      if (myHistoryRound_1 == Response.C && oppHistoryRound_1 == Response.C) lambda.noChange();
      else if (myHistoryRound_1 == Response.C && oppHistoryRound_1 == Response.D) lambda.incrementValue();
      else if (myHistoryRound_1 == Response.D && oppHistoryRound_1 == Response.C) lambda.decrementValue();
      else if (myHistoryRound_1 == Response.D && oppHistoryRound_1 == Response.D) lambda.incrementValue();

    } else {
      lambda.noChange();
    }
    return NashEquilibrium.getEquilibrium(lambda);
  }

  @Override
  public String name() {
    return "AE-GS-S";
  }

  @Override
  public String author() {
    return "Georgios Sakellariou";
  }

}
