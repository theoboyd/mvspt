package strategy;

import util.Lambda;
import util.Response;
import util.NashEquilibrium;

public class BayesianTitForTat extends Strategy {

  public BayesianTitForTat() {
    super();
    lambda = new Lambda(rand.nextGaussian() + 0.3);
  }

  @Override
  public Response respond() {
    if (getRoundsPlayed() == 0) {
      // First round
      lambda.noChange();
    } else {
      // Not the first round
      Response myHistoryRound_1 = getLastResponsePair().get(0);
      Response oppHistoryRound_1 = getLastResponsePair().get(1);
      double predictedOppLambda = rand.nextGaussian();

      if (myHistoryRound_1 == Response.C && oppHistoryRound_1 == Response.C) lambda.noChange();
      else if ((myHistoryRound_1 == Response.C && oppHistoryRound_1 == Response.D)
          || (myHistoryRound_1 == Response.D && oppHistoryRound_1 == Response.C)) {
        if (predictedOppLambda < lambda.getValue()) {
          lambda.decrementValue();
        } else {
          lambda.incrementValue();
        }
      } else if (myHistoryRound_1 == Response.D && oppHistoryRound_1 == Response.D) lambda.incrementValue();
    }
    return NashEquilibrium.getEquilibrium(lambda);
  }

  @Override
  public String name() {
    return "Bayesian Tit For Tat";
  }

  @Override
  public String author() {
    return "Ingrid Funie";
    // Contact email: aif109@doc.ic.ac.uk
  }
}
