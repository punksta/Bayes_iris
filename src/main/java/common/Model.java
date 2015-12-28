package common;

/**
 * Created by punksta on 29.12.15.
 * http://mobiumapps.com/
 */
public interface Model<C extends Enum<C>> {
    double[] getFeatures();
    double getFeature(int number);
    int featureCount();
    C modelClass();
}
