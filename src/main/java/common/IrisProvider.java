package common;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * Created by punksta on 28.12.15.
 * http://mobiumapps.com/
 */
public class IrisProvider {
    private final static String fileName = "LearningBase";

    public List<IrisModel> getRecords() {
        Scanner scanner = null;
        try {
            //Get file from resources folder
            ClassLoader classLoader = getClass().getClassLoader();
            File file = new File(classLoader.getResource(fileName).getFile());

            List<IrisModel> result = new ArrayList<>();
            scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] record = line.split(",");
                result.add(new IrisModel(
                                getDoubleFromRecord(record, 0),
                                getDoubleFromRecord(record, 1),
                                getDoubleFromRecord(record, 2),
                                getDoubleFromRecord(record, 3),
                                IrisModel.Type.find(record[4])
                        )
                );
            }

            return result;
        } catch (Exception e) {
            return Collections.emptyList();
        } finally {
            if (scanner != null)
                scanner.close();
        }
    }

    public static Double getDoubleFromRecord(String[] record, int num) {
        return Double.parseDouble(record[num]);
    }

}
