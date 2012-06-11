package strategy;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import util.GameSettings;
import util.Lambda;
import util.Response;

public class RLQTableIPlus extends Strategy {

  private static double[][] Qtable = new double[105][2];
  private int LastState;
  private int LastAction;
  private int CurrentState;
  private int CurrentAction;
  private double ExplorePercentage;
  private static int Counter;
  private static double LastLambda;
  private static double CoopRatio = 0.0; // 0.3
  private static double learningRate = 0.1; // 0.1
  private static double discountFactor = 0.9; // 0.9
  private static double dcLambda = 3; // 5
  private static double ccLambdaB = 2; // 2.5
  private static double ccLambdaA = 2; // 3
  private static double ddLambda = 10; // 1
  private static double cdLambda = 3; // 2.5
  private double gamma = 0;

  public RLQTableIPlus() {
    super();
    lambda = new Lambda(CoopRatio);
    initialQ();
    Counter = 0;
    ExplorePercentage = 0;
    LastLambda = CoopRatio;
    
    // Set optimal constants
    gamma = GameSettings.GAMMA;
    ccLambdaA = gamma;
    learningRate = 0.5;
    discountFactor = 1 - learningRate;
    System.out.println("gamma: " + gamma + ", learningRate: " + learningRate + ", discountFactor: " + discountFactor + ".");
  }

  @Override
  public Response respond() {

    Response FinalAnswer;
    Response OppLast, MyLast;

    Counter += 1;

    // First round
    if (getRoundsPlayed() == 0) {
      lambda.noChange();
      LastState = (int) (lambda.getValue() * 100);
      LastAction = 0;
      LastLambda = lambda.getValue();
      return Response.C;
    } else {
      OppLast = getLastResponsePair().get(1);
      MyLast = getLastResponsePair().get(0);

      if (MyLast == Response.C && OppLast == Response.C) lambda.decrementValue();
      else if (MyLast == Response.C && OppLast == Response.D) lambda.incrementValue();
      else if (MyLast == Response.D && OppLast == Response.C) lambda.decrementValue();
      else if (MyLast == Response.D && OppLast == Response.D) lambda.incrementValue();

      CurrentState = (int) (lambda.getValue() * 100);
      FinalAnswer = learningResult(OppLast);
      LastLambda = lambda.getValue();

      return FinalAnswer;
    }

  }

  public Response learningResult(Response OppLastState) {
    double Reward;
    int OppLast;

    // Get opponent's last action
    if (OppLastState == Response.C) OppLast = 0;
    else OppLast = 1;

    Reward = getReward(OppLast);
    CurrentAction = getBestAction(CurrentState);
    Qtable[LastState][LastAction] += learningRate * (Reward + discountFactor * (Qtable[CurrentState][CurrentAction] - Qtable[LastState][LastAction]));

    LastState = CurrentState;
    LastAction = CurrentAction;

    if (CurrentAction == 0) return Response.C;
    else return Response.D;

  }

  public int getBestAction(int state) {
    ExplorePercentage = -(5.0 / GameSettings.N) * Counter + 5;

    if (rand.nextInt(100) < ExplorePercentage) {
      return rand.nextInt(2);
    } else {
      if (Qtable[state][0] >= Qtable[state][1]) return 0;
      else return 1;
    }
  }

  public double getReward(int OppLast) {
    double Vlambda = LastLambda;

    // Both cooperate
    if (LastAction == 0 && OppLast == 0) {
      return ccLambdaA * (1 - Vlambda) + ccLambdaB * Vlambda;
    } else if (LastAction == 0 && OppLast == 1) {
      return cdLambda * Vlambda;
    } else if (LastAction == 1 && OppLast == 0) {
      return dcLambda * (1 - Vlambda);
    } else {
      return ddLambda * 1 - Vlambda;
    }
  }

  public static void initialQ() {

    for (int i = 0; i <= 100; ++i) {
      double lambda = i / 100.0;

      Qtable[i][0] = CoopRatio * (ccLambdaA * (1 - lambda) + ccLambdaB * lambda) + (1 - CoopRatio) * (ccLambdaB * lambda);
      Qtable[i][1] = CoopRatio * (dcLambda * (1 - lambda)) + (1 - CoopRatio) * (lambda);
    }
  }

  public void printQtable() {
    try {
      FileWriter file = new FileWriter("Qtable.txt");
      BufferedWriter writer = new BufferedWriter(file);
      for (int i = 0; i <= 100; i++) {
        writer.append(Qtable[i][0] + "\n" + Qtable[i][1] + "\n");
      }
      writer.close();
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }

  }

  @Override
  public String name() {
    return "RL QTable I+";
  }

  @Override
  public String author() {
    return "Theodore Boyd";
  }

}
