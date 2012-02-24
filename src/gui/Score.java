package gui;

public class Score {

  public String name;
  public String author;
  public String opponentName;
  public int    runsMade;
  public double lambda;
  public double materialScore;
  public double socialScore;
  public double aggregates;   // Number of individual scores this was aggregated from

  public Score(String name, String author, String opponentName) {
    this.name = name;
    this.author = author;
    this.opponentName = opponentName;
    this.runsMade = 0;
    this.lambda = 0;
    this.materialScore = 0;
    this.socialScore = 0;
    this.aggregates = 0;
  }

  public Score(String name, String author, String opponentName, double lambda, double materialScore, double socialScore) {
    this.name = name;
    this.author = author;
    this.opponentName = opponentName;
    this.runsMade = 0;
    this.lambda = lambda;
    this.materialScore = materialScore;
    this.socialScore = socialScore;
    this.aggregates = 0;
  }
}
