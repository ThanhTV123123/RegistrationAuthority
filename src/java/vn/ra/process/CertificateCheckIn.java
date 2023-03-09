/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ra.process;

import vn.ra.utility.Definitions;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Arrays;

import javax.xml.bind.DatatypeConverter;

import org.bouncycastle.jce.PKCS10CertificationRequest;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.apache.log4j.Logger;

/**
 *
 * @author THANH-PC
 */
public class CertificateCheckIn {

    private static final Logger log = Logger.getLogger(CertificateCheckIn.class);

    private static X509Certificate getCertificate(String base64)
            throws Exception {
        CertificateFactory certFactory = CertificateFactory
                .getInstance("X.509");
        InputStream in = new ByteArrayInputStream(
                processBinaryCertificate(base64));
        X509Certificate cert = (X509Certificate) certFactory
                .generateCertificate(in);
        return cert;
    }

    private static byte[] processBinaryCertificate(String base64Str)
            throws Exception {
        byte[] binary;
        if (base64Str.contains(Definitions.CONFIG_WORKER_TAG_CERTIFICATE_BEGIN)) {
            base64Str = base64Str.replace(Definitions.CONFIG_WORKER_TAG_CERTIFICATE_BEGIN, "");
            base64Str = base64Str.replace(Definitions.CONFIG_WORKER_TAG_CERTIFICATE_END, "");
            binary = DatatypeConverter.parseBase64Binary(base64Str);
        } else {
            binary = DatatypeConverter.parseBase64Binary(base64Str);
        }
        return binary;
    }

    private static byte[] processBinaryCsr(String base64Str)
            throws Exception {
        byte[] binary;
        if (base64Str.contains(Definitions.CONFIG_WORKER_TAG_CSR_BEGIN)
                || base64Str.contains(Definitions.CONFIG_WORKER_TAG_CSR_BEGIN_NEW)) {
            base64Str = base64Str.replace(Definitions.CONFIG_WORKER_TAG_CSR_BEGIN, "");
            base64Str = base64Str.replace(Definitions.CONFIG_WORKER_TAG_CSR_END, "");
            base64Str = base64Str.replace(Definitions.CONFIG_WORKER_TAG_CSR_BEGIN_NEW, "");
            base64Str = base64Str.replace(Definitions.CONFIG_WORKER_TAG_CSR_END_NEW, "");

            binary = DatatypeConverter.parseBase64Binary(base64Str);
        } else {
            binary = DatatypeConverter.parseBase64Binary(base64Str);
        }
        return binary;
    }

    /**
     *
     * @param childCert
     * @param parentCert
     * @return
     * @throws Exception
     */
    public static boolean checkCertificateRelation(String childCert, String parentCert) throws Exception {
        if (!childCert.toUpperCase().contains(Definitions.CONFIG_WORKER_TAG_CERTIFICATE_BEGIN_CONTAINS)) {
            childCert = Definitions.CONFIG_WORKER_TAG_CERTIFICATE_BEGIN + "\n" + childCert;
        }
        if (!childCert.toUpperCase().contains(Definitions.CONFIG_WORKER_TAG_CERTIFICATE_END_CONTAINS)) {
            childCert = childCert + "\n" + Definitions.CONFIG_WORKER_TAG_CERTIFICATE_END;
        }
        boolean isOk = false;
        Security.addProvider(new BouncyCastleProvider());
        try {
            X509Certificate certChild = getCertificate(childCert);
            X509Certificate certParent = getCertificate(parentCert);
            certChild.verify(certParent.getPublicKey());
            isOk = true;
        } catch (SignatureException e) {
            CommonFunction.LogExceptionServlet(log, "CheckCertificateRelation: " + e.getMessage(), e);
        } catch (CertificateException e) {
            CommonFunction.LogExceptionServlet(log, "CheckCertificateRelation: " + e.getMessage(), e);
        } catch (Exception e) {
            CommonFunction.LogExceptionServlet(log, "CheckCertificateRelation: " + e.getMessage(), e);
        }
        return isOk;
    }

    /**
     *
     * @param certificate
     * @param csr
     * @return
     * @throws Exception
     */
    public static boolean checkCertificateAndCsr(String certificate, String csr) throws Exception {
        if (!certificate.toUpperCase().contains(Definitions.CONFIG_WORKER_TAG_CERTIFICATE_BEGIN_CONTAINS)) {
            certificate = Definitions.CONFIG_WORKER_TAG_CERTIFICATE_BEGIN + "\n" + certificate;
        }
        if (!certificate.toUpperCase().contains(Definitions.CONFIG_WORKER_TAG_CERTIFICATE_END_CONTAINS)) {
            certificate = certificate + "\n" + Definitions.CONFIG_WORKER_TAG_CERTIFICATE_END;
        }
        boolean isOk = false;
        Security.addProvider(new BouncyCastleProvider());
        try {
            X509Certificate cert = getCertificate(certificate);
            byte[] pubKey = hashSHA1(cert.getPublicKey().getEncoded());
            byte[] pubKeyCsr = hashSHA1(getEncodedPublicKeyCsr(csr));
            if (Arrays.equals(pubKey, pubKeyCsr)) {
                isOk = true;
            }
        } catch (Exception e) {
            CommonFunction.LogExceptionServlet(log, "CheckCertificateAndCsr: " + e.getMessage(), e);
        }
        return isOk;
    }

    private static byte[] getEncodedPublicKeyCsr(String base64csr) throws Exception {
        byte[] pubKey = null;
        try {
            PKCS10CertificationRequest csr
                    = new PKCS10CertificationRequest(processBinaryCsr(base64csr));
            pubKey = csr.getPublicKey().getEncoded();
        } catch (Exception e) {
            CommonFunction.LogExceptionServlet(log, "getEncodedPublicKeyCsr: " + e.getMessage(), e);
        }
        return pubKey;
    }

    private static byte[] hashSHA1(byte[] data) {
        byte[] hash = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(data);
            hash = md.digest();
        } catch (NoSuchAlgorithmException e) {
            CommonFunction.LogExceptionServlet(log, "hashSHA1: " + e.getMessage(), e);
        }
        return hash;
    }
}
