package util;

public abstract class DiscreteDouble {
  private double value;
  private double minimum;
  private double maximum;
  private double increment;

  /**
   * Wrapper for the special discrete double that lambda should be. Allows for the one-time initialisation of lambda to
   * a strategic value. Also locks setting to only incrementing and decrementing by the increment value.
   * 
   * @param minimum
   * @param maximum
   * @param increment
   * @param startValue
   */
  public DiscreteDouble(double minimum, double maximum, double increment, double startValue) {
    this.minimum = minimum;
    this.maximum = maximum;
    this.increment = increment;
    this.value = fixToDiscrete(startValue, minimum, maximum, increment);
  }

  public double getValue() {
    return value;
  }

  public void incrementValue() {
    value = fixToDiscrete(value + increment, value, value + increment, increment);
  }

  public void decrementValue() {
    value = fixToDiscrete(value - increment, value - increment, value, increment);
  }

  /**
   * Fix input to the closed interval [min, max]
   * 
   * @param input
   * @param min
   * @param max
   * @return
   */
  private double fixToRange(double input, double min, double max) {
    double output = 0;
    if (input > max) {
      output = max;
    } else
      if (input < min) {
        output = min;
      } else {
        output = input;
      }
    return output;
  }

  /**
   * Fix input to the closer of min and max
   * 
   * @param input
   * @param min
   * @param max
   * @return
   */
  private double fixToDiscrete(double input, double min, double max, double step) {
    double output = 0;

    // First cap the value
    input = fixToRange(input, min, max);

    for (double i = min; i < max; i += step) {
      double currentMin = i;
      double currentMax = i + step;

      if (input >= currentMin && input <= currentMax) {
        // If the input is within the current limit values

        if (input == currentMin) {
          // If the input is exactly the limit value
          output = currentMin;
        } else
          if (input == currentMax) {
            // Or the other limit
            output = currentMax;
          } else {
            // Else the input is neither of the limit values
            if (input <= ((currentMin + currentMax) / 2)) {
              // If it's <= the midpoint of currentMin and currentMax
              // Set it to the minimum
              output = currentMin;
            } else {
              // Set it to the maximum
              output = currentMax;
            }
          }
      }
    }
    return output;
  }
}
