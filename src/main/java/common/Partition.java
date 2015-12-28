package common;

import java.util.Arrays;

/**
 * Created by punksta on 27.10.15.
 * http://mobiumapps.com/
 */
public class Partition {
    public final int learningIndex[];
    public final int testIndex[];

    public Partition(int learningIndex[], int testIndex[]) {
        this.learningIndex = learningIndex;
        this.testIndex = testIndex;
    }

    @Override
    public String toString() {
        return "Partition{" +
                "learningIndex=" + Arrays.toString(learningIndex) +
                ", testIndex=" + Arrays.toString(testIndex) +
                '}';
    }
}
