package util;

import java.util.LinkedList;
import java.util.List;

import strategy.AlwaysCooperates;
import strategy.AlwaysDefects;
import strategy.AlwaysRandom;
import strategy.Strategy;
import strategy.TestStrategy;

/**
 * Wrapper collection for all valid, supported strategies
 * 
 * @author Theo
 */
public class StrategyCollection {
  List<Strategy> strategies;

  public StrategyCollection() {
    strategies = new LinkedList<Strategy>();
    strategies.add(new AlwaysCooperates());
    strategies.add(new AlwaysDefects());
    strategies.add(new AlwaysRandom());
    strategies.add(new TestStrategy());
  }

  public List<Strategy> getStrategies() {
    return strategies;
  }
  
  public Strategy getSocialWinner() {
    Strategy winner = strategies.get(0);
    for (Strategy s : strategies) {
      if (s.socialScore > winner.socialScore) {
        winner = s;
      }
    }
    return winner;
  }
  
  public Strategy getMaterialWinner() {
    Strategy winner = strategies.get(0);
    for (Strategy s : strategies) {
      if (s.materialScore > winner.materialScore) {
        winner = s;
      }
    }
    return winner;
  }
}