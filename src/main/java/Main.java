import common.IrisModel;
import common.IrisProvider;
import common.LBUtil;
import common.Partition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by punksta on 28.12.15.
 * http://mobiumapps.com/
 */
public class Main {
    public static void main(String[] args) {
        List<IrisModel> iris = new IrisProvider().getRecords();

        Long l = System.currentTimeMillis();

        String pointsArray =
                IntStream.range(1, 120)
                        .parallel()
                        .mapToObj(lerningSize-> {
                            double result = getAverageTest(iris, lerningSize, 1500, true);
                            return "(" + lerningSize + "," +result + "),";
                        })
                        .collect(Collectors.joining());

        System.out.println("line( [" + pointsArray + "], color= 'red')");
        System.out.print(System.currentTimeMillis() - l);
    }


    public static double getAverageTest(List<IrisModel> models, int learning, int times, boolean parralel) {
        return (parralel ? IntStream.range(0, times).parallel() : IntStream.range(0, times))
                .mapToDouble(i -> test(parralel ? new ArrayList<>(models) : models, learning)).sum() / times;
    }

    public static double test(List<IrisModel> models, int learningSize) {
        Collections.shuffle(models);
        Partition partition = LBUtil.randomSplit(models.size(), learningSize);

        List<IrisModel> learning = LBUtil.getElemsByIndex(partition.learningIndex, models);
        List<IrisModel> testing = LBUtil.getElemsByIndex(partition.testIndex, models);

        Bayes<IrisModel.Type, IrisModel> bayes = new Bayes<>(learning);

        int currentAnswers = 0;

        for (IrisModel m : testing)
            if (bayes.getModelType(m.getFeatures()).equals(m.type))
                currentAnswers++;

        return ((double) currentAnswers) / testing.size();
    }
}
