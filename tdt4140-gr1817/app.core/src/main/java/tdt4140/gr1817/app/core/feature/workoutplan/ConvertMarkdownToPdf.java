package tdt4140.gr1817.app.core.feature.workoutplan;

import com.qkyrie.markdown2pdf.Markdown2PdfConverter;
import com.qkyrie.markdown2pdf.internal.reading.Markdown2PdfReader;
import com.qkyrie.markdown2pdf.internal.writing.SimpleFileMarkdown2PdfWriter;

import java.io.File;
import java.util.concurrent.ThreadLocalRandom;

public class ConvertMarkdownToPdf {


    public String generateRandomFileName() {
        int length = 10;
        String name = "";
        for (int i = 0; i < length; i++) {
            name += Integer.toString(ThreadLocalRandom.current().nextInt(0, 10));
        }
        return name;
    }

    public void createPdfFromString(String str) {
        new File("improvifyWorkoutSessions").mkdir();
        File file = new File("improvifyWorkoutSessions\\" + generateRandomFileName() + ".pdf");

        Markdown2PdfConverter converter = Markdown2PdfConverter.newConverter();
        converter.readFrom(new Markdown2PdfReader() {
            @Override
            public String read() {
                return str;
            }
        });

        converter.writeTo(new SimpleFileMarkdown2PdfWriter(file));

        try {
            converter.doIt();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        ConvertMarkdownToPdf p = new ConvertMarkdownToPdf();
        p.createPdfFromString("### WOLOLOLO");

    }
}
