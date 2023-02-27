package com.trejhara;

import com.itextpdf.license.LicenseKey;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.tsp.TimeStampToken;

import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.AcroFields.FieldPosition;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfString;
import com.itextpdf.text.pdf.security.CertificateInfo;
import com.itextpdf.text.pdf.security.PdfPKCS7;
import com.itextpdf.text.pdf.security.SignaturePermissions;
import com.itextpdf.text.pdf.security.SignaturePermissions.FieldLock;

// Import log4j classes.
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class SignatureInfo extends SignatureIntegrity {

    private static final Logger logger = LogManager.getLogger(SignatureInfo.class);
    public static final String EXAMPLEO = "UPOS19477942_D1326244.pdf";

    public SignaturePermissions inspectSignature(AcroFields fields, String name, SignaturePermissions perms) throws GeneralSecurityException, IOException {
        List<FieldPosition> fps = fields.getFieldPositions(name);
        if (fps != null && fps.size() > 0) {
            FieldPosition fp = fps.get(0);
            Rectangle pos = fp.position;
            if (pos.getWidth() == 0 || pos.getHeight() == 0) {
                logger.info("Invisible signature");
            } else {
                logger.info(String.format("Field on page %s; llx: %s, lly: %s, urx: %s; ury: %s",
                        fp.page, pos.getLeft(), pos.getBottom(), pos.getRight(), pos.getTop()));
            }
        }

        PdfPKCS7 pkcs7 = super.verifySignature(fields, name);
        logger.info("Digest algorithm: " + pkcs7.getHashAlgorithm());
        logger.info("Encryption algorithm: " + pkcs7.getEncryptionAlgorithm());
        logger.info("Filter subtype: " + pkcs7.getFilterSubtype());
        X509Certificate cert = (X509Certificate) pkcs7.getSigningCertificate();
        logger.info("Name of the signer: " + CertificateInfo.getSubjectFields(cert).getField("CN"));
        if (pkcs7.getSignName() != null) {
            logger.info("Alternative name of the signer: " + pkcs7.getSignName());
        }
        SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SS");
        logger.info("Signed on: " + date_format.format(pkcs7.getSignDate().getTime()));
        if (pkcs7.getTimeStampDate() != null) {
            logger.info("TimeStamp: " + date_format.format(pkcs7.getTimeStampDate().getTime()));
            TimeStampToken ts = pkcs7.getTimeStampToken();
            logger.info("TimeStamp service: " + ts.getTimeStampInfo().getTsa());
            logger.info("Timestamp verified? " + pkcs7.verifyTimestampImprint());
        }
        logger.info("Location: " + pkcs7.getLocation());
        logger.info("Reason: " + pkcs7.getReason());
        PdfDictionary sigDict = fields.getSignatureDictionary(name);
        PdfString contact = sigDict.getAsString(PdfName.CONTACTINFO);
        if (contact != null) {
            logger.info("Contact info: " + contact);
        }
        perms = new SignaturePermissions(sigDict, perms);
        logger.info("Signature type: " + (perms.isCertification() ? "certification" : "approval"));
        logger.info("Filling out fields allowed: " + perms.isFillInAllowed());
        logger.info("Adding annotations allowed: " + perms.isAnnotationsAllowed());
        for (FieldLock lock : perms.getFieldLocks()) {
            logger.info("Lock: " + lock.toString());
        }
        return perms;
    }

    public void inspectSignatures(String path) throws IOException, GeneralSecurityException {
        logger.info(path);
        PdfReader reader = new PdfReader(path);
        AcroFields fields = reader.getAcroFields();
        ArrayList<String> names = fields.getSignatureNames();
        SignaturePermissions perms = null;
        for (String name : names) {
            logger.info("===== " + name + " =====");
            perms = inspectSignature(fields, name, perms);
        }
        logger.info("================================================================================================");
    }

    public static void main(String[] args) throws IOException, GeneralSecurityException {
        LicenseKey.loadLicenseFile("src/main/resources/itextkey.xml");
        //LoggerFactory.getInstance().setLogger(new SysoLogger());
        BouncyCastleProvider provider = new BouncyCastleProvider();
        Security.addProvider(provider);
        SignatureInfo app = new SignatureInfo();

        app.inspectSignatures(EXAMPLEO);
    }
}