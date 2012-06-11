package strategy;
 
import util.Lambda;
import util.Response;
 
public class RandomTitForTat extends Strategy {

  public RandomTitForTat() {
    super();
    lambda = new Lambda(rand.nextDouble());
  }
 
  @Override
  public Response respond() {
    if (rand.nextInt() % 2 == 0) {
      lambda.noChange();
      return Response.D; 
    } else {
      lambda.noChange();
      return Response.C;
    }
  }
 
  @Override
  public String name() {
    return "Random Tit For Tat";
  }
 
  @Override
  public String author() {
    return "Aga Madurska";
    // Contact email: amadurska@gmail.com
  }
}
