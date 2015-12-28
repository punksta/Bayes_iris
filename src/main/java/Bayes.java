import common.IrisModel;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by punksta on 28.12.15.
 * http://mobiumapps.com/
 */
public class Bayes {
    //class ->  featureNumber -> (normal distribution args)}
    private final EnumMap<IrisModel.Type, Map<Integer, NormalDistribution>> classFeatureCharacteristics;

    //class -> {iris of class}
    private final EnumMap<IrisModel.Type, List<IrisModel>> sample;

    //total learning sample size
    private final int totalCount;

    public Bayes(List<IrisModel> learning) {
        sample = getSample(learning);
        classFeatureCharacteristics = buildTable(sample);
        totalCount = learning.size();
    }

    private EnumMap<IrisModel.Type, List<IrisModel>> getSample(List<IrisModel> models) {
        EnumMap<IrisModel.Type, List<IrisModel>> enumMap = new EnumMap<>(IrisModel.Type.class);
        for (IrisModel.Type t : IrisModel.Type.values())
            enumMap.put(t, models.parallelStream().filter(m -> m.type.equals(t)).collect(Collectors.toList()));
        return enumMap;
    }

    private EnumMap<IrisModel.Type, Map<Integer, NormalDistribution>> buildTable(EnumMap<IrisModel.Type, List<IrisModel>> sample) {
        EnumMap<IrisModel.Type, Map<Integer, NormalDistribution>> classWithFeatures = new EnumMap<>(IrisModel.Type.class);

        for (Map.Entry<IrisModel.Type, List<IrisModel>> e :sample.entrySet()) {
            Map<Integer, NormalDistribution> featureWithEvDe = new HashMap<>(IrisModel.featureSize());

            for (int fNumber = 0; fNumber < IrisModel.featureSize(); fNumber++) {
                final int finalFNumber = fNumber;
                List<Double> randomValue = e.getValue().parallelStream().map(v -> v.getFeature(finalFNumber)).collect(Collectors.toList());
                double expectedValue = NormalDistribution.getDiscreetExpectedValue(randomValue);
                double dispersion = NormalDistribution.getDiscreetVariance(randomValue, expectedValue);

                featureWithEvDe.put(fNumber, new NormalDistribution(expectedValue, dispersion));
            }

            classWithFeatures.put(e.getKey(), featureWithEvDe);
        }
        return classWithFeatures;
    }


    public IrisModel.Type getType(double[] features) {
        double pMax = 0;
        IrisModel.Type maxType = null;

        for (Map.Entry<IrisModel.Type, Map<Integer, NormalDistribution>>  e : classFeatureCharacteristics.entrySet()) {
            IrisModel.Type type = e.getKey();

            double pC = ((double) sample.get(type).size()) / totalCount;

            double pCurrent = pC;
            for (int i = 0; i < IrisModel.featureSize(); i++) {
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
