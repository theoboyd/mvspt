package util;

import java.text.DecimalFormat;

public class ResultRow {
  public String name;
  public String author;
  public double latestLambda;
  public double aggregateLambda;
  public double aggregateMaterial;
  public double aggregateSocial;
  public double aggregateOverall;
  public int counter;
  public int lambdaCounter;

  public ResultRow() {
    name = "";
    author = "";
    latestLambda = 0.0;
    aggregateLambda = 0.0;
    aggregateMaterial = 0.0;
    aggregateSocial = 0.0;
    aggregateOverall = 0.0;
    counter = 0;
    lambdaCounter = 0;
  }

  @Override
  public String toString() {
    return toString(false);
  }

  public String toString(boolean latexMode) {
    DecimalFormat df = new DecimalFormat("0.000");

    String startTag = (latexMode ? "" : "<tr>\n<td>\n");
    String sepTag = (latexMode ? " & " : "\n</td>\n<td>\n");
    String endTag = (latexMode ? " \\\\ \\hline" : "\n</td>\n</tr>");

    String output = startTag + name + sepTag + author + sepTag + df.format(latestLambda) + sepTag
        + df.format((double) aggregateLambda / (double) lambdaCounter) + sepTag
        + df.format((double) aggregateMaterial / (double) counter) + sepTag
        + df.format((double) aggregateSocial / (double) counter) + sepTag
        + df.format((double) aggregateOverall / (double) counter) + endTag;

    return output;
  }
}
