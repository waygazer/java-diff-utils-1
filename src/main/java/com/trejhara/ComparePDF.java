package com.trejhara;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.LocationTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

public abstract class ComparePDF {

    /**
     * File separator.
     */
    protected static final String FS = File.separator;
    /**
     * The base resource path.
     */
    protected static String BASE_PATH = "src" + FS + "test" + FS + "resources";

    /**
     * Tries to read the file and split it into a list of lines.
     *
     * @param filename The filename as path.
     * @return A list of lines.
     */
    public static List<String> pdfToLines(String filename) throws Exception {
        List<String> lines = new LinkedList<String>();
        String[] pagelines;
        PdfReader in = null;
        try {
            in = new PdfReader(filename);
            int totalPageNo = in.getNumberOfPages();
            for (int i = 1; i <= totalPageNo; i++) {
                pagelines = getPageLines(in, i);
                for (String line : pagelines) {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                in.close(); // ignore ... any errors should already have been
                // reported via an IOException from the final flush.
            }
        }
        return lines;
    }

    public static String getPageText(PdfReader reader, int pageNo) throws Exception {
        String str = PdfTextExtractor.getTextFromPage(reader, pageNo, new LocationTextExtractionStrategy()).trim();
        return str.replace(" ", "").replace("\n", "");
    }

    public static String[] getPageLines(PdfReader reader, int pageNo) throws Exception {
        String[] str = PdfTextExtractor.getTextFromPage(reader, pageNo, new LocationTextExtractionStrategy()).trim().split("\n");
        return str;
    }

}
