import common.Model;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by punksta on 28.12.15.
 * http://mobiumapps.com/
 */
public class Bayes<E extends Enum<E>, M extends Model<E>> {
    //class ->  featureNumber -> (normal distribution args)}
    private final EnumMap<E, Map<Integer, NormalDistribution>> classFeatureCharacteristics;

    //class -> {iris of class}
    private final EnumMap<E, List<M>> sample;

    //total learning sample size
    private final int totalCount;


    private final Class<E> modelClass;

    private final int featureCount;

    public Bayes(List<M> learning) {
        featureCount = learning.get(0).featureCount();
        modelClass = learning.get(0).modelClass().getDeclaringClass();
        totalCount = learning.size();

        sample = getSample(learning);
        classFeatureCharacteristics = buildTable(sample);
    }

    private EnumMap<E, List<M>> getSample(List<M> models) {
        E values = models.get(0).modelClass();

        EnumMap<E, List<M>> enumMap = new EnumMap<>(values.getDeclaringClass());

        for (E t : values.getDeclaringClass().getEnumConstants())
            enumMap.put(t, models.parallelStream().filter(m -> m.modelClass().equals(t)).collect(Collectors.toList()));
        return enumMap;
    }

    private EnumMap<E, Map<Integer, NormalDistribution>> buildTable(EnumMap<E, List<M>> sample) {

        EnumMap<E, Map<Integer, NormalDistribution>> classWithFeatures = new EnumMap<>(modelClass);

        for (Map.Entry<E, List<M>> entry : sample.entrySet()) {
            Map<Integer, NormalDistribution> featureWithEvDe = new HashMap<>(featureCount);

            for (int fNumber = 0; fNumber < featureCount; fNumber++) {
                final int finalFNumber = fNumber;
                List<Double> randomValue = entry.getValue().parallelStream().map(v -> v.getFeature(finalFNumber)).collect(Collectors.toList());
                double e = NormalDistribution.getDiscreteExpectedValue(randomValue);
                double v = NormalDistribution.getDiscreteVariance(randomValue, e);

                featureWithEvDe.put(fNumber, new NormalDistribution(e, v));
            }

            classWithFeatures.put(entry.getKey(), featureWithEvDe);
        }
        return classWithFeatures;
    }


    public E getType(double[] features) {
        double pMax = 0;
        E maxType = null;

        for (Map.Entry<E, Map<Integer, NormalDistribution>>  e : classFeatureCharacteristics.entrySet()) {
            E type = e.getKey();

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
