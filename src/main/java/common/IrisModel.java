package common;

import bayes.BayesModel;

import java.util.stream.Stream;

/**
 * Created by punksta on 15.10.15.
 * http://mobiumapps.com/
 */
public class IrisModel implements Cloneable, BayesModel<IrisModel.Type> {
    public double sepalLength;
    public double sepalWidth;
    public double petalLength;
    public double petalWidth;
    public final Type type;

    public IrisModel(double sepalLength,
                     double sepalWidth,
                     double petalLength,
                     double petalWidth,
                     Type type)
    {
        this.sepalLength = sepalLength;
        this.sepalWidth = sepalWidth;
        this.petalLength = petalLength;
        this.petalWidth = petalWidth;
        this.type = type;
    }

    public double[] getFeatures() {
        return new double[]{
                getFeature(0),
                getFeature(1),
                getFeature(2),
                getFeature(3)
        };
    }


    public enum Type {
        setosa("Iris-setosa"),
        versicolour("Iris-versicolor"),
        virginica("Iris-virginica");

        public final String name;

        public static Type find (String type) {
            return Stream.of(values())
                    .filter(s -> type.equals(s.name))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("unknown argument"));
        }

        Type(String name) {
            this.name = name;
        }
    }

    @Override
    public String toString() {
        return type.name();
    }

    public static int featureSize() {
        return new IrisModel(0,0,0,0, Type.setosa).featureCount();
    }

    public double getFeature(int number) {
        switch (number) {
            case 0: return petalLength;
            case 1: return petalWidth;
            case 2: return sepalLength;
            case 3: return sepalWidth;
            default: throw new IllegalArgumentException("AHTUGN!!!");
        }
    }

    @Override
    public int featureCount() {
        return 4;
    }

    @Override
    public Type modelClass() {
        return type;
    }
}
