package bayes;

/**
 * Created by punksta on 29.12.15.
 * http://mobiumapps.com/
 */
public interface BayesModel<C extends Enum<C>> {
    double[] getFeatures();
    double getFeature(int number);
    int featureCount();
    C modelClass();
}
