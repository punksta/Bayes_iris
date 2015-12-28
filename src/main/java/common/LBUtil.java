package common;

import java.util.*;

/**
 * Created by punksta on 27.10.15.
 * http://mobiumapps.com/
 */
public class LBUtil {
    public static Random random = new Random();

    public static Partition randomSplit(int size, int learningPartSize) {
        Set<Integer> used = new HashSet<>();

        int testing[] = new int[size - learningPartSize];
        int learning[] = new int[learningPartSize];
        int k, n;
        k = n = 0;
        for (int i = 0; i < size; i++) {
            int index = Math.abs(random.nextInt() % size);
            while (used.contains(index))
                index = Math.abs(random.nextInt() % size);
            used.add(index);
            if (k < learningPartSize)
                learning[k++] = index;
            else
                testing[n++] = index;
        }

        used.clear();
        return new Partition(learning, testing);
    }

    public static <T> List<T> getElemsByIndex(int indexes[], List<T> list) {
        ArrayList<T> result = new ArrayList<>(indexes.length);
        for (int index : indexes)
            result.add(list.get(index));
        return result;
    }


}
