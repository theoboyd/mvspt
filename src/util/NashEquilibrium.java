package util;

import java.util.Random;

public class NashEquilibrium {

  protected static Random rand = new Random();

  public static Response getEquilibrium(DiscreteHistoricDouble lambda) {
    if (lambda.getValue() == 0) return Response.D;
    else if (lambda.getValue() <= 0.1) return Response.D;
    else if (lambda.getValue() <= 0.2) return Response.D;
    else if (lambda.getValue() <= 0.3) {
      if (getRandomNumber(1, 11) > 3) return Response.D;
      else return Response.C;
    } else if (lambda.getValue() <= 0.4) {
      if (getRandomNumber(1, 11) > 3) return Response.D;
      else return Response.C;
    } else if (lambda.getValue() <= 0.5) return Response.C;
    else if (lambda.getValue() <= 0.6) return Response.C;
    else if (lambda.getValue() <= 0.7) return Response.C;
    else if (lambda.getValue() <= 0.8) return Response.C;
    else if (lambda.getValue() <= 0.9) return Response.C;
    else if (lambda.getValue() >= 1) return Response.C;
    return null;
  }

  public static int getRandomNumber(int min, int max) {
    return min + rand.nextInt(max - min); // nextInt itself will return a value between 0 and max-min
  }
}
