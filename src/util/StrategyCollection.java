package util;

import gui.Score;

import java.util.LinkedList;
import java.util.List;

import strategy.ABitNicer;
import strategy.ABitRandom;
import strategy.AEGS;
import strategy.AEGSS;
import strategy.AlwaysCooperates;
import strategy.AlwaysDefects;
import strategy.AlwaysRandom;
import strategy.Bayesian;
import strategy.BayesianTitForTat;
import strategy.DoubleIncrement;
import strategy.EightyPercentNice;
import strategy.ExtendedTitForTat;
import strategy.NashDefect;
import strategy.NashTitForTat;
import strategy.NegativePeople;
import strategy.NonsensePeople;
import strategy.PositivePeople;
import strategy.RLQTableI;
import strategy.RLQTableIPlus;
import strategy.RafalStrategy;
import strategy.RandomTitForTat;
import strategy.SocialTitForTat;
import strategy.Strategy;
import strategy.TitForTat;

/**
 * Wrapper collection for all valid, supported strategies
 */
public class StrategyCollection {
  List<Class<? extends Strategy>> strategies;

  public StrategyCollection() {
    // Note: all strategy names must be unique (both classes and string representations)
    strategies = new LinkedList<Class<? extends Strategy>>();
    strategies.add(ABitNicer.class);
    strategies.add(ABitRandom.class);
    strategies.add(AEGS.class);
    strategies.add(AEGSS.class);
    strategies.add(AlwaysCooperates.class);
    strategies.add(AlwaysDefects.class);
    strategies.add(AlwaysRandom.class);
    strategies.add(Bayesian.class);
    strategies.add(BayesianTitForTat.class);
    strategies.add(DoubleIncrement.class);
    strategies.add(EightyPercentNice.class);
    strategies.add(ExtendedTitForTat.class);
    strategies.add(NashDefect.class);
    strategies.add(NashTitForTat.class);
    strategies.add(NegativePeople.class);
    strategies.add(NonsensePeople.class);
    strategies.add(PositivePeople.class);
    strategies.add(RafalStrategy.class);
    strategies.add(RandomTitForTat.class);
    strategies.add(RLQTableI.class);
    strategies.add(RLQTableIPlus.class);
    // strategies.add(RLQTableII.class); // Is the same as I
    strategies.add(SocialTitForTat.class);
    // 
    // strategies.add(TestStrategy.class);
    strategies.add(TitForTat.class);
  }

  public List<Class<? extends Strategy>> getStrategies() {
    return strategies;
  }

  public static double overallScore(Score s) {
    return (((1 - GameSettings.GAMMA) * s.materialScore) + (GameSettings.GAMMA * s.socialScore));
  }
}