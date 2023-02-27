package com.trejhara;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.Security;
import java.util.ArrayList;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.security.PdfPKCS7;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SignatureIntegrity {

    private static final Logger logger = LogManager.getLogger(SignatureInfo.class);
    public static final String EXAMPLEO = "1088145.pdf";

    public PdfPKCS7 verifySignature(AcroFields fields, String name) throws GeneralSecurityException, IOException {
        logger.info("Signature covers whole document: " + fields.signatureCoversWholeDocument(name));
        logger.info("Document revision: " + fields.getRevision(name) + " of " + fields.getTotalRevisions());
        PdfPKCS7 pkcs7 = fields.verifySignature(name);
        logger.info("Integrity check OK? " + pkcs7.verify());
        return pkcs7;
    }

    public void verifySignatures(String path) throws IOException, GeneralSecurityException {
        logger.info(path);
        PdfReader reader = new PdfReader(path);
        AcroFields fields = reader.getAcroFields();
        ArrayList<String> names = fields.getSignatureNames();
        for (String name : names) {
            logger.info("===== " + name + " =====");
            verifySignature(fields, name);
        }
        logger.info("================================================================================================");
    }

    public static void main(String[] args) throws IOException, GeneralSecurityException {
        BouncyCastleProvider provider = new BouncyCastleProvider();
        Security.addProvider(provider);
        SignatureIntegrity app = new SignatureIntegrity();

        app.verifySignatures(EXAMPLEO);
    }
}
