import common.*;

import java.util.Collections;
import java.util.List;

/**
 * Created by punksta on 28.12.15.
 * http://mobiumapps.com/
 */
public class Main {
    public static void main(String[] args) {
        List<IrisModel> iris = new IrisProvider().getRecords();

        for (int i = 3; i < 99; i++) {
            float learingProcent = (float) i / 100;
            int learningSize = (int) (iris.size() * learingProcent);
            System.out.println(learningSize + " " + getAverageTest(iris, learningSize, 350));
        }
    }


    public static double getAverageTest(List<IrisModel> models, int learning, int times) {
        double sum = 0;
        for (int i = 0; i < times; i++) {
            sum += test(models, learning);
        }
        return sum / times;
    }

    public static double test(List<IrisModel> models, int learningSize) {
        Collections.shuffle(models);
        Partition partition = LBUtil.randomSplit(models.size(), learningSize);

        List<IrisModel> learning = LBUtil.getElemsByIndex(partition.learningIndex, models);
        List<IrisModel> testing = LBUtil.getElemsByIndex(partition.testIndex, models);

        Bayes<IrisModel.Type, IrisModel> bayes = new Bayes<>(learning);

        int currentAnswers = 0;

        for (IrisModel m : testing)
            if (bayes.getType(m.getFeatures()).equals(m.type))
                currentAnswers++;

        return ((double) currentAnswers) / testing.size();
    }
}
