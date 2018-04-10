package tdt4140.gr1817.app.core.feature.workoutplan;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.*;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

/**
 * Created by Ask Sommervoll and Kristian Rekstad.
 *
 * @author Ask Sommervoll and Kristian Rekstad
 */

public class SaveWorkoutPlanMarkdownToFileTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    public void shouldWriteContentsToFile() throws Exception{
        //Given
        File file = temporaryFolder.newFile("testfile.txt");
        SaveWorkoutPlanMarkdownToFile tested = new SaveWorkoutPlanMarkdownToFile();
        String markdown = "#squadgoals, #æøå";

        //When
        tested.writeToFile(file, markdown);
        String contents = readFile(file);

        // Then
        assertThat(contents, is(markdown));


    }

    private static String readFile(File file) {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf8"))) {
            List<String> lines = bufferedReader.lines()
                    .collect(Collectors.toList());

            String contents = String.join("\n", lines);
            return contents;
        } catch (Exception ex) {
            throw new RuntimeException("Failed to read file", ex);
        }
    }

}