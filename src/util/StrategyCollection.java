package util;

import gui.Score;

import java.util.LinkedList;
import java.util.List;

import strategy.AEGS;
import strategy.AEGSS;
import strategy.AlwaysCooperates;
import strategy.AlwaysDefects;
import strategy.AlwaysRandom;
import strategy.Bayesian;
import strategy.ExtendedTitForTat;
import strategy.NashTitForTat;
import strategy.NegativePeople;
import strategy.NonsensePeople;
import strategy.PositivePeople;
import strategy.SocialTitForTat;
import strategy.Strategy;
import strategy.TitForTat;

/**
 * Wrapper collection for all valid, supported strategies
 */
public class StrategyCollection {
  List<Class<? extends Strategy>> strategies;

  public StrategyCollection() {
    strategies = new LinkedList<Class<? extends Strategy>>();
    strategies.add(AEGS.class);
    strategies.add(AEGSS.class);
    strategies.add(AlwaysCooperates.class);
    strategies.add(AlwaysDefects.class);
    strategies.add(AlwaysRandom.class);
    strategies.add(Bayesian.class);
    strategies.add(ExtendedTitForTat.class);
    strategies.add(NashTitForTat.class);
    strategies.add(NegativePeople.class);
    strategies.add(NonsensePeople.class);
    strategies.add(PositivePeople.class);
    strategies.add(SocialTitForTat.class);
    strategies.add(TitForTat.class);
    // strategies.add(TestStrategy.class);
  }

  public List<Class<? extends Strategy>> getStrategies() {
    return strategies;
  }

  public Score getSocialWinner(List<Score> scores) {
    Score winner = scores.get(0);
    for (Score s : scores) {
      if (s.socialScore > winner.socialScore) {
        winner = s;
      }
    }
    return winner;
  }

  public Score getMaterialWinner(List<Score> scores) {
    Score winner = scores.get(0);
    for (Score s : scores) {
      if (s.materialScore > winner.materialScore) {
        winner = s;
      }
    }
    return winner;
  }

  public Score getOverallWinner(List<Score> scores) {
    Score winner = scores.get(0);
    for (Score s : scores) {
      if (overallScore(s) > overallScore(winner)) {
        winner = s;
      }
    }
    return winner;
  }

  public static double overallScore(Score s) {
    return (((1 - GameSettings.GAMMA) * s.materialScore) + (GameSettings.GAMMA * s.socialScore));
  }
}