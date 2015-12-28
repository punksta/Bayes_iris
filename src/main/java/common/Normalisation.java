package common;

import javafx.util.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

/**
 * Created by punksta on 29.10.15.
 * http://mobiumapps.com/
 */
public class Normalisation {
    private HashMap<Integer, Pair<Double, Double>> minMaxTable = new HashMap<>();

    public Normalisation(List<IrisModel> models) {
        put(0, models, ir -> ir.petalLength);
        put(1, models, ir -> ir.petalWidth);
        put(2, models, ir -> ir.sepalLength);
        put(3, models, ir -> ir.sepalWidth);

        for (IrisModel model : models) {
            model.petalLength = normalize(model.petalLength, 0);
            model.petalWidth = normalize(model.petalWidth, 1);
            model.sepalLength = normalize(model.sepalLength, 2);
            model.sepalWidth = normalize(model.sepalWidth, 3);
        }
    }

    private void put(int i, List<IrisModel> models, Function<IrisModel, Double> mapper){
        put(i, findMin(models, mapper), findMax(models, mapper));
    }

    private void put(int i, double min, double max) {
        minMaxTable.put(i, new Pair<>(min, max));
    }

    private Double getMax(int i) {
        return minMaxTable.get(i).getValue();
    }

    private Double getMin(int i) {
        return minMaxTable.get(i).getKey();
    }


    double normalize(double currentValue, int number) {
        double num = currentValue - getMin(number);
        double denom = getMax(number) - getMin(number);
        return num / denom;
    }

    private static double findMax(List<IrisModel> models, Function<IrisModel, Double> mapper) {
        return models.stream().mapToDouble(mapper::apply).max().getAsDouble();
    }

    private static double findMin(List<IrisModel> models, Function<IrisModel, Double> mapper) {
        return models.stream().mapToDouble(mapper::apply).min().getAsDouble();
    }


}
