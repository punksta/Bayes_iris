package bayes;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by punksta on 28.12.15.
 * http://mobiumapps.com/
 */

/**
 * bayes.Bayes naive algorithm
 * @param <C> type of model class
 * @param <M> type of model
 */
public class Bayes<C extends Enum<C>, M extends BayesModel<C>> {
    //some class ->  featureNumber -> (normal distribution of feature in the class)}
    private final EnumMap<C, Map<Integer, NormalDistribution>> classFeatureCharacteristics;

    //some class -> {iris of the class}
    private final EnumMap<C, List<M>> sample;

    //total learning sample size
    private final int totalCount;


    private final Class<C> modelClassType;
    private final C[] classValueSet;

    private final int featureCount;

    public Bayes(List<M> learning) {
        featureCount = learning.get(0).featureCount();
        modelClassType = learning.get(0).modelClass().getDeclaringClass();
        classValueSet = modelClassType.getEnumConstants();

        totalCount = learning.size();

        sample = getSample(learning);
        classFeatureCharacteristics = buildNormalDistributionTable(sample);

    }

    private EnumMap<C, List<M>> getSample(List<M> models) {
        EnumMap<C, List<M>> enumMap = new EnumMap<>(modelClassType);
        for (C t : classValueSet)
            enumMap.put(t, models.stream().filter(m -> m.modelClass().equals(t)).collect(Collectors.toList()));
        return enumMap;
    }

    private EnumMap<C, Map<Integer, NormalDistribution>> buildNormalDistributionTable(EnumMap<C, List<M>> sample) {

        EnumMap<C, Map<Integer, NormalDistribution>> classWithFeatures = new EnumMap<>(modelClassType);

        for (Map.Entry<C, List<M>> entry : sample.entrySet()) {
            Map<Integer, NormalDistribution> featureWithEvDe = new HashMap<>(featureCount);

            for (int fNumber = 0; fNumber < featureCount; fNumber++) {
                final int finalFNumber = fNumber;
                List<Double> randomValue = entry.getValue().stream().map(v -> v.getFeature(finalFNumber)).collect(Collectors.toList());
                double e = NormalDistribution.getDiscreteExpectedValue(randomValue);
                double v = NormalDistribution.getDiscreteVariance(randomValue, e);

                featureWithEvDe.put(fNumber, new NormalDistribution(e, v));
            }

            classWithFeatures.put(entry.getKey(), featureWithEvDe);
        }
        return classWithFeatures;
    }


    public C getModelType(double[] features) {
        double pMax = 0;
        C maxType = null;

        for (Map.Entry<C, Map<Integer, NormalDistribution>>  e : classFeatureCharacteristics.entrySet()) {
            C type = e.getKey();

            double pC = ((double) sample.get(type).size()) / totalCount;

            double pCurrent = pC;
            for (int i = 0; i < featureCount; i++) {
                pCurrent *= e.getValue().get(i).getProbability(features[i]);
            }
            if (maxType == null || pMax < pCurrent) {
                pMax = pCurrent;
                maxType = type;
            }
        }
        return maxType;
    }
}
