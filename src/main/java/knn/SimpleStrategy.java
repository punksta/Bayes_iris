package knn;

import common.IrisModel;

/**
 * Created by punksta on 29.10.15.
 * http://mobiumapps.com/
 */
public class SimpleStrategy implements KnnStrategy<IrisModel> {

    public final int k;

    public SimpleStrategy(int k) {
        this.k = k;
    }


    @Override
    public double distance(IrisModel a, IrisModel b) {
        return Math.sqrt(
                Math.pow(a.petalLength - b.petalLength, 2) +
                        Math.pow(a.petalWidth - b.petalWidth, 2) +
                        Math.pow(a.sepalLength - b.sepalLength, 2) +
                        Math.pow(a.sepalWidth - b.sepalWidth, 2)
        );
    }

    @Override
    public int getK() {
        return k;
    }

    @Override
    public boolean equals(IrisModel a, IrisModel b) {
        return a.type.equals(b.type);
    }
}
