import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by punksta on 28.12.15.
 * http://mobiumapps.com/
 */
class NormalDistribution {
    public final double expectedValue;
    public final double variance;
    public final double standardDeviation;

    private final double k;

    NormalDistribution(double expectedValue, double variance) {
        this.expectedValue = expectedValue;
        this.variance = variance;

        standardDeviation = Math.sqrt(variance);
        k = 1 / (standardDeviation * Math.sqrt(2 * Math.PI));

    }


    /**
     * @return  P {X = x}: X ~ N(variance, expectedValue)
     */
    public double getProbability(double x) {
        return k * Math.exp(
                    -Math.pow(x - expectedValue, 2) /
                            (2 * variance));
    }


    /**
     * @param radonValueValues values of random value X
     * @param expectedValue of X
     * @return E[X]
     */
    public static double getDiscreteVariance(List<Double> radonValueValues, double expectedValue) {
        return getDiscreteExpectedValue(radonValueValues.stream().map(v -> v*v).collect(Collectors.toList()))
                - expectedValue * expectedValue;
    }


    /**
     * @param radonValueValues values of random value X
     * @return D[X]
     */
    public static double getDiscreteExpectedValue(List<Double> radonValueValues) {
        Map<Double, Integer> count = new HashMap<>();
        for (double v : radonValueValues) {
            if (count.containsKey(v))
                count.put(v, count.get(v) + 1);
            else
                count.put(v, 1);
        }
        double sum = 0.;

        for (Map.Entry<Double, Integer> entry : count.entrySet())
            sum += entry.getKey() * entry.getValue() / radonValueValues.size();

        return sum;
    }




}
