package knn;

/**
 * Created by punksta on 27.10.15.
 * http://mobiumapps.com/
 */
public interface KnnStrategy<T>  {
    double distance(T a, T b);

    int getK();

    boolean equals(T a, T b);


    class Result {
        public final int correctAnswers;
        public final double percent;

        public Result(int correctAnswers, double percent) {
            this.correctAnswers = correctAnswers;
            this.percent = percent;
        }

        public Result(int correctAnswers, int total) {
            this(correctAnswers, correctAnswers / (double) total);
        }

        @Override
        public String toString() {
            return "Result{" +
                    "correctAnswers=" + correctAnswers +
                    ", percent=" + percent +
                    '}';
        }
    }



}
