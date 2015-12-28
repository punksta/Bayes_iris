package knn;

import common.*;
import javafx.util.Pair;

import java.util.*;
import java.util.stream.IntStream;


/**
 * Created by punksta on 15.10.15.
 * http://mobiumapps.com/
 */
public class KNN {



    private HashMap<Integer, Pair<Double, Double>> minMaxTable = new HashMap<>();



    public static void main(String[] args) {
        KNN KNN = new KNN();
        List<IrisModel> iris = new IrisProvider().getRecords();
        System.out.println("   p     percent");
        for (int k = 1; k <= 5; k++) {

            System.out.print("line" + k + " = line([");
            for (float p = 1; p < 90; p +=2) {
                double result = KNN.test(iris, k, p/100);
                System.out.print("(" + p/100 + ", " + result+"),");
            }
            System.out.println("], " +
                    "legend_label='k=" + k + "'," +
                    "color ='"+ color(k) +"')");
        }
        System.out.printf("lines = line1 + line2 + line3 + line4 + line5" +
                "plot( lines,  ymin=0.1, ymax=1.1) ");
    }

    public static String color(int k) {
        switch (k) {
            case 1: return "red";
            case 2: return "yellow";
            case 3: return "blue";
            case 4: return "green";
            default: return "black";
        }
    }

    public double test(List<IrisModel> iris, int k, float learningPart)  {
        int c = 350;

        return IntStream.range(0, c)
                .mapToDouble(i -> accept( new ArrayList<>(iris), k, learningPart).percent)
                .sum() / c;
    }


    public KnnStrategy.Result accept(List<IrisModel> iris, int k, float learningSize) {
        Collections.shuffle(iris);
        new Normalisation(iris);

        Partition partition = LBUtil.randomSplit(iris.size(), (int) (learningSize * iris.size()));

        List<IrisModel> learning = LBUtil.getElemsByIndex(partition.learningIndex, iris);
        List<IrisModel> testing = LBUtil.getElemsByIndex(partition.testIndex, iris);

        SimpleStrategy simpleStrategy = new SimpleStrategy(k);

        return testProcedure(simpleStrategy, learning, testing);
    }

    private <T> KnnStrategy.Result testProcedure(KnnStrategy<T> strategy, List<T> learing, List<T> tesing) {
        int goodAnswers = 0;

        for (T test : tesing) {
            HashMap<T, Integer> equivalences = new HashMap<>(); // equivalence class of neighbor - power

            learing
                    .stream()
                    .sorted((o1, o2) -> (int) (1000 * strategy.distance(test, o1)) -
                            (int) (1000 * strategy.distance(test, o2))
                    )
                    .limit(strategy.getK())
                    .forEach(t -> {
                        boolean find = false;
                        for (T key : equivalences.keySet())
                            if (strategy.equals(key, t)) {
                                find = true;
                                int count = equivalences.get(key);
                                equivalences.put(key, count + 1);
                            }
                        if (!find)
                            equivalences.put(t, 1);

                    });


            T bestNeighor = new HashSet<>(equivalences.entrySet())
                    .stream()
                    .sorted((o1, o2) -> o2.getValue() - o1.getValue())
                    .findFirst()
                    .get()
                    .getKey();

                if (strategy.equals(bestNeighor, test))
                    goodAnswers++;
        }

        return new KnnStrategy.Result(goodAnswers, tesing.size());
    }
}
