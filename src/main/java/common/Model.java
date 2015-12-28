package common;

/**
 * Created by punksta on 29.12.15.
 * http://mobiumapps.com/
 */
public interface Model<E extends Enum<E>> {
    double[] getFeatures();
    double getFeature(int number);
    int featureCount();
    E modelClass();
}
