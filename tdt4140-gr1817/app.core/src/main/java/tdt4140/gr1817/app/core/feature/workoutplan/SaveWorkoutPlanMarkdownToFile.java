package tdt4140.gr1817.app.core.feature.workoutplan;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class SaveWorkoutPlanMarkdownToFile {

    String convertedWorkoutPlan;


    public void writeToFile(File file, String contents) throws IOException {
        FileWriter fileWriter = new FileWriter(file);
        PrintWriter printWriter = new PrintWriter(fileWriter);
        printWriter.print(contents);
        printWriter.flush();
        printWriter.close();
    }
}
