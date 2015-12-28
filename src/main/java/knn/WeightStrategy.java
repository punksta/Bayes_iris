package knn;

import common.IrisModel;

/**
 * Created by punksta on 27.10.15.
 * http://mobiumapps.com/
 */
public class WeightStrategy extends SimpleStrategy {
    public final double w0;
    public final double w1;
    public final double w2;
    public final double w3;


    public WeightStrategy(double w0, double w1, double w2, double w3, int k) {
        super(k);
        this.w0 = w0;
        this.w1 = w1;
        this.w2 = w2;
        this.w3 = w3;
    }


    @Override
    public double distance(IrisModel a, IrisModel b) {
        return Math.sqrt(
                        Math.pow(w0 * a.petalLength - w0 * b.petalLength, 2) +
                        Math.pow(w1 * a.petalWidth - w1 * b.petalWidth, 2) +
                        Math.pow(w2 * a.sepalLength - w2 * b.sepalLength, 2) +
                        Math.pow(w3 * a.sepalWidth - w3 * b.sepalWidth, 2)
        );
    }


    @Override
    public String toString() {
        return "knn.WeightStrategy{" +
                "w0=" + w0 +
                ", w1=" + w1 +
                ", w2=" + w2 +
                ", w3=" + w3 +
                ", k=" + k +
                '}';
    }
}
