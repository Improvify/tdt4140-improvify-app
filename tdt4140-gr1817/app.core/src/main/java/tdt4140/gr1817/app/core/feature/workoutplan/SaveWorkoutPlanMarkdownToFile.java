package tdt4140.gr1817.app.core.feature.workoutplan;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

public class SaveWorkoutPlanMarkdownToFile {

    String convertedWorkoutPlan;


    public void writeToFile(File file, String contents) throws IOException {
        try (
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true),
                        StandardCharsets.UTF_8))) {

            bw.write(contents);
        } catch (Exception ex) {
            throw new RuntimeException("Failed to write to file", ex);
        }
    }
}
