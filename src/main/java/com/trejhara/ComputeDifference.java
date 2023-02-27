package com.trejhara;

import java.util.List;

import difflib.Delta;
import difflib.DiffUtils;
import difflib.Patch;
import difflib.PatchFailedException;
import java.io.File;
import com.itextpdf.license.*;

// Import log4j classes.
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class ComputeDifference extends ComparePDF {

    private static final Logger logger = LogManager.getLogger(ComputeDifference.class);
    //static final String ORIGINAL = "D:\\usr\\Cryptography\\revised.txt";
    //static final String REVISED = "D:\\usr\\Cryptography\\original.txt";
    static final String OriginalPath = "D:\\Projects\\IDFC\\V1\\";
    static final String ActualPath = "D:\\Projects\\IDFC\\V2\\";

    public static void main(String[] args) throws PatchFailedException, Exception {
        LicenseKey.loadLicenseFile("src/main/resources/itextkey.xml");
        File folder;
        File listfiles[] = null;
        logger.info("Modified files : " + ActualPath);
        logger.info("Original files : " + OriginalPath);
        if (new File(ActualPath).exists()) {
            folder = new File(ActualPath);
            listfiles = folder.listFiles();
        } else {
            logger.error("Input files not found at : " + ActualPath);
            System.exit(0);
        }
        if (!new File(OriginalPath).exists()) {
            logger.error("Original files not found at : " + OriginalPath);
            System.exit(0);
        }

        for (File listfile : listfiles) {
            try {
                String ORIGINAL = OriginalPath + listfile.getName();
                String REVISED = ActualPath + listfile.getName();

                List<String> original = pdfToLines(ORIGINAL);
                List<String> revised = pdfToLines(REVISED);

                // Compute diff. Get the Patch object. Patch is the container for computed deltas.
                Patch<String> patch = DiffUtils.diff(original, revised);
                logger.info(listfile.getName() + "-----------------------------------------------------------------------------------");
                for (Delta<String> delta : patch.getDeltas()) {
                    logger.info(delta);
                }
                /*
                List<String> patched = patch.applyTo(original);
                // At first, parse the unified diff file and get the patch
                Patch<String> patchu = DiffUtils.parseUnifiedDiff(patched);

                // Then apply the computed patch to the given text
                List<String> resulto = DiffUtils.patch(original, patchu);
                System.out.println("Applying to Original");
                System.out.println(resulto);
                // / Or we can call patch.applyTo(original). There is no difference.

                List<String> resultr = DiffUtils.patch(revised, patchu);
                System.out.println("Applying to Revised");
                System.out.println(resultr);
                 */
            } catch (Exception e) {
                logger.error(e.getLocalizedMessage());
            }
        }
    }
}
