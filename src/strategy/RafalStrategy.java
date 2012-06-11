package strategy;

import util.Lambda;
import util.NashEquilibrium;
import util.Response;
import java.util.List;
import java.util.LinkedList;

public class RafalStrategy extends Strategy {

  private List<List<Response>> history;

  public RafalStrategy() {
    super();
    lambda = new Lambda(0);
    history = new LinkedList<List<Response>>();
  }

  @Override
  public Response respond() {
    if (getRoundsPlayed() != 0) {
      history.add(getLastResponsePair());

      int cc = 0;
      int cd = 0;
      int dd = 0;
      int dc = 0;

      for (List<Response> elem : history) {
        Response myMove = elem.get(0);
        Response otherMove = elem.get(1);
        if (myMove == Response.C && otherMove == Response.C) cc++;
        else if (myMove == Response.C && otherMove == Response.D) cd++;
        else if (myMove == Response.D && otherMove == Response.D) dd++;
        else if (myMove == Response.D && otherMove == Response.C) dc++;
      }

      String todo = "cc";
      int max = cc;
      if (cd >= max) {
        todo = "cd";
        max = cd;
      }
      if (dd >= max) {
        todo = "dd";
        max = dd;
      }
      if (dc >= max) {
        todo = "dc";
        max = dc;
      }

      if (todo == "dc") lambda.decrementValue();
      else lambda.incrementValue();

    } else {
      lambda.noChange();
    }
    return NashEquilibrium.getEquilibrium(lambda);
  }

  @Override
  public String name() {
    return "Rafal Strategy";
  }

  @Override
  public String author() {
    return "Rafal Szymanski";
    // Contact email: rs2909@doc.ic.ac.uk
  }

}
