/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ra.uat;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.css.media.MediaDeviceDescription;
import com.itextpdf.html2pdf.css.media.MediaType;
import com.itextpdf.html2pdf.resolver.font.DefaultFontProvider;
import com.itextpdf.io.font.FontProgram;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.font.FontProvider;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import vn.ra.object.CERTIFICATION_POLICY_DATA;
import vn.ra.process.CommonFunction;
import vn.ra.process.PrintFormFunction;
import vn.ra.utility.Config;
import vn.ra.utility.EscapeUtils;

/**
 *
 * @author THANH-PC
 */
public class PrintReport {

    Config conf = new Config();
    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(PrintReport.class);
    private static final long serialVersionUID = 6106269076155338045L;
    public static final String[] FONTS = {
        "D:\\Project\\TMS CA\\TMS EFY\\main" + "\\" + "times.ttf",
        "D:\\Project\\TMS CA\\TMS EFY\\main" + "\\" + "timesbd.ttf",
        "D:\\Project\\TMS CA\\TMS EFY\\main" + "\\" + "timesbi.ttf",
        "D:\\Project\\TMS CA\\TMS EFY\\main" + "\\" + "timesi.ttf",};
    public static final String fileHTML = "D:\\Project\\TMS CA\\TMS EFY" + "\\" + "capCTSDN.html";
    public static final String filePDF = "D:\\Project\\TMS CA\\TMS EFY" + "\\" + "capCTSDN.pdf";
    public static final String pathXSLT = "D:\\Project\\TMS CA\\TMS EFY\\Deploy Temp\\GiayChungNhan.xslt"; //"src/main/java/vn/mobileid/createfilepdf/main/capCTSDN.xslt";
    public static final String pathXML = "D:\\Project\\TMS CA\\TMS EFY\\Deploy Temp\\GCN.xml";

    public void API_REQUEST()
    {
        // 1. core - generateKeystore, bo sung input send email (p12, pass), input password
        // 
    }
    public static void main(String[] args) throws Exception {
        
        // boolean autoApproveCA = false;
//        boolean autoApproveCAServer = false;
//        boolean autoApproveCAClient = raServiceReq.approveEnabled;
//        //<editor-fold defaultstate="collapsed" desc="### LEVEL APPROVE">
//        // intLevelApprove - new : 0, agency approve : 1, CA approve : 2
//        int intLevelApprove = 0;
//        if(autoApproveCAServer == false && autoApproveCAClient == false) {
//            intLevelApprove = 0;
//        } else if(autoApproveCAServer == false && autoApproveCAClient == true)
//        {
//            intLevelApprove = 1;
//        } else if(autoApproveCAServer == true && autoApproveCAClient == false)
//        {
//            intLevelApprove = 1;
//        } else if(autoApproveCAServer == true && autoApproveCAClient == true)
//        {
//            intLevelApprove = 2;
//        }
//        //</editor-fold>
//        if(intLevelApprove == 1 || intLevelApprove == 2)
//        if(intLevelApprove == 2)
//        autoApproveCAServer = CommonFunction.getApproveEnabledCert(sCERT_POLICY_PROPERTIES);
//        
//        valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PRE_APPROVED);
//        valueATTR.setApproveUser(raFullname + " (" + raServiceReq.beneficiaryUser + ")");
//        valueATTR.setApproveDt(new Date());
//        strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
//        db.S_BO_CERTIFICATION_PRE_APPROVED(pCERTIFICATE_ATTR_ID[0], strReqValueATTR, EscapeUtils.CheckTextNull(raServiceReq.beneficiaryUser));
        
        /*String sCERT_POLICY_PROPERTIES = "{ \"attributes\": [ { \"attributeType\": \"PROFILE_LIST\", \"attributes\": [ {\"name\":\"OSN1Y_G1\",\"certificateAuthority\":\"TRUSTCA_G1\",\"certificatePurpose\":\"ENTERPRISE\",\"remarkEn\":\"Enterprise (12 months)\",\"remark\":\"Doanh nghiệp (12 tháng)\",\"enabled\":true,\"attributeType\":\"PROFILE_LIST/ITEM\"},{\"name\":\"OSN2Y_G1\",\"certificateAuthority\":\"TRUSTCA_G1\",\"certificatePurpose\":\"ENTERPRISE\",\"remarkEn\":\"Enterprise (24 months)\",\"remark\":\"Doanh nghiệp (24 tháng)\",\"enabled\":true,\"attributeType\":\"PROFILE_LIST/ITEM\"},{\"name\":\"SSN1Y_G1\",\"certificateAuthority\":\"TRUSTCA_G1\",\"certificatePurpose\":\"STAFF\",\"remarkEn\":\"Staff (12 months)\",\"remark\":\"Cá nhân trong doanh nghiệp (12 tháng)\",\"enabled\":true,\"attributeType\":\"PROFILE_LIST/ITEM\"},{\"name\":\"SSN2Y_G1\",\"certificateAuthority\":\"TRUSTCA_G1\",\"certificatePurpose\":\"STAFF\",\"remarkEn\":\"Staff (24 months)\",\"remark\":\"Cá nhân trong doanh nghiệp (24 tháng)\",\"enabled\":true,\"attributeType\":\"PROFILE_LIST/ITEM\"},{\"name\":\"PSN1Y_G1\",\"certificateAuthority\":\"TRUSTCA_G1\",\"certificatePurpose\":\"PERSONAL\",\"remarkEn\":\"Personal (12 months)\",\"remark\":\"Cá nhân (12 tháng)\",\"enabled\":true,\"attributeType\":\"PROFILE_LIST/ITEM\"},{\"name\":\"OSR1Y_G1\",\"certificateAuthority\":\"TRUSTCA_G1\",\"certificatePurpose\":\"ENTERPRISE\",\"remarkEn\":\"Renew Enterprise (12 months)\",\"remark\":\"Gia hạn Doanh nghiệp (12 tháng)\",\"enabled\":true,\"attributeType\":\"PROFILE_LIST/ITEM\"},{\"name\":\"SSR1Y_G1\",\"certificateAuthority\":\"TRUSTCA_G1\",\"certificatePurpose\":\"STAFF\",\"remarkEn\":\"Renew Staff (12 months)\",\"remark\":\"Gia hạn Cá nhân trong doanh nghiệp (12 tháng)\",\"enabled\":true,\"attributeType\":\"PROFILE_LIST/ITEM\"},{\"name\":\"PSR2Y_G1\",\"certificateAuthority\":\"TRUSTCA_G1\",\"certificatePurpose\":\"PERSONAL\",\"remarkEn\":\"Renew Personal (24 months)\",\"remark\":\"Gia hạn Cá nhân (24 tháng)\",\"enabled\":true,\"attributeType\":\"PROFILE_LIST/ITEM\"},{ \"name\": \"SS_OSN1Y_G1\", \"certificateAuthority\": \"TRUSTCA_G1\", \"certificatePurpose\": \"SIGNSERVER_ENTERPRISE\", \"remarkEn\": \"Enterprise (12 months)\", \"remark\": \"Doanh nghiệp (12 tháng)\", \"enabled\": true, \"attributeType\": \"PROFILE_LIST/ITEM\" },{ \"name\": \"SS_OSN2Y_G1\", \"certificateAuthority\": \"TRUSTCA_G1\", \"certificatePurpose\": \"SIGNSERVER_ENTERPRISE\", \"remarkEn\": \"Enterprise (24 months)\", \"remark\": \"Doanh nghiệp (24 tháng)\", \"enabled\": true, \"attributeType\": \"PROFILE_LIST/ITEM\" },{ \"name\": \"SS_OSN3Y_G1\", \"certificateAuthority\": \"TRUSTCA_G1\", \"certificatePurpose\": \"SIGNSERVER_ENTERPRISE\", \"remarkEn\": \"Enterprise (36 months)\", \"remark\": \"Doanh nghiệp (36 tháng)\", \"enabled\": true, \"attributeType\": \"PROFILE_LIST/ITEM\" },{ \"name\": \"SS_OSR1Y_G1\", \"certificateAuthority\": \"TRUSTCA_G1\", \"certificatePurpose\": \"SIGNSERVER_ENTERPRISE\", \"remarkEn\": \"Renew Enterprise (12 months)\", \"remark\": \"Gia hạn Doanh nghiệp (12 tháng)\", \"enabled\": true, \"attributeType\": \"PROFILE_LIST/ITEM\" },{ \"name\": \"SS_OSR2Y_G1\", \"certificateAuthority\": \"TRUSTCA_G1\", \"certificatePurpose\": \"SIGNSERVER_ENTERPRISE\", \"remarkEn\": \"Renew Enterprise (24 months)\", \"remark\": \"Gia hạn Doanh nghiệp (24 tháng)\", \"enabled\": true, \"attributeType\": \"PROFILE_LIST/ITEM\" },{ \"name\": \"SS_OSR3Y_G1\", \"certificateAuthority\": \"TRUSTCA_G1\", \"certificatePurpose\": \"SIGNSERVER_ENTERPRISE\", \"remarkEn\": \"Renew Enterprise (36 months)\", \"remark\": \"Gia hạn Doanh nghiệp (36 tháng)\", \"enabled\": true, \"attributeType\": \"PROFILE_LIST/ITEM\" },{ \"name\": \"SS_SSN1Y_G1\", \"certificateAuthority\": \"TRUSTCA_G1\", \"certificatePurpose\": \"SIGNSERVER_STAFF\", \"remarkEn\": \"Staff (12 months)\", \"remark\": \"Cá nhân trong doanh nghiệp (12 tháng)\", \"enabled\": true, \"attributeType\": \"PROFILE_LIST/ITEM\" },{ \"name\": \"SS_SSN2Y_G1\", \"certificateAuthority\": \"TRUSTCA_G1\", \"certificatePurpose\": \"SIGNSERVER_STAFF\", \"remarkEn\": \"Staff (24 months)\", \"remark\": \"Cá nhân trong doanh nghiệp (24 tháng)\", \"enabled\": true, \"attributeType\": \"PROFILE_LIST/ITEM\" },{ \"name\": \"SS_SSN3Y_G1\", \"certificateAuthority\": \"TRUSTCA_G1\", \"certificatePurpose\": \"SIGNSERVER_STAFF\", \"remarkEn\": \"Staff (36 months)\", \"remark\": \"Cá nhân trong doanh nghiệp (36 tháng)\", \"enabled\": true, \"attributeType\": \"PROFILE_LIST/ITEM\" },{ \"name\": \"SS_SSR1Y_G1\", \"certificateAuthority\": \"TRUSTCA_G1\", \"certificatePurpose\": \"SIGNSERVER_STAFF\", \"remarkEn\": \"Renew Staff (12 months)\", \"remark\": \"Gia hạn Cá nhân trong doanh nghiệp (12 tháng)\", \"enabled\": true, \"attributeType\": \"PROFILE_LIST/ITEM\" },{ \"name\": \"SS_SSR2Y_G1\", \"certificateAuthority\": \"TRUSTCA_G1\", \"certificatePurpose\": \"SIGNSERVER_STAFF\", \"remarkEn\": \"Renew Staff (24 months)\", \"remark\": \"Gia hạn Cá nhân trong doanh nghiệp (24 tháng)\", \"enabled\": true, \"attributeType\": \"PROFILE_LIST/ITEM\" },{ \"name\": \"SS_SSR3Y_G1\", \"certificateAuthority\": \"TRUSTCA_G1\", \"certificatePurpose\": \"SIGNSERVER_STAFF\", \"remarkEn\": \"Renew Staff (36 months)\", \"remark\": \"Gia hạn Cá nhân trong doanh nghiệp (36 tháng)\", \"enabled\": true, \"attributeType\": \"PROFILE_LIST/ITEM\" }\n" +
",{ \"name\": \"SS_PSR1Y_G1\", \"certificateAuthority\": \"TRUSTCA_G1\", \"certificatePurpose\": \"SIGNSERVER_PERSONAL\", \"remarkEn\": \"Renew Personal (12 months)\", \"remark\": \"Gia hạn Cá nhân (12 tháng)\", \"enabled\": true, \"attributeType\": \"PROFILE_LIST/ITEM\" }\n" +
" ] },{ \"name\": \"false\", \"remarkEn\": \"Profile All Access\", \"remark\": \"Cho phép truy cập tất cả gói CTS\", \"enabled\": true, \"attributeType\": \"PROFILE_ALL_ACCESS\" },{ \"name\": \"true\", \"remarkEn\": \"Approved enabled\", \"remark\": \"Cho phép duyệt cấp CTS\", \"enabled\": true, \"attributeType\": \"APPROVED_ENABLED\" },{ \"name\": \"admin_mn\", \"remarkEn\": \"Beneficiary User\", \"remark\": \"Nhân viên thụ hưởng CTS\", \"enabled\": true, \"attributeType\": \"BENEFICIACY_USER\" } ] }";
        
//        CERTIFICATION_POLICY_DATA[][] resPolicyCertData = new CERTIFICATION_POLICY_DATA[1][];
//        CommonFunction.getProfileCertList(sCERT_POLICY_PROPERTIES, resPolicyCertData);
        
        boolean accessProfileAll = CommonFunction.checkAPIAccessProfileAll(sCERT_POLICY_PROPERTIES);
        if(accessProfileAll == true)
        {
            System.out.println("true");
        } else {
            System.out.println("false");
        }*/
        
        
        
        /*String strCertificate = "MIIFDzCCA/egAwIBAgIQVAEBEJZNoE/k2LwCfml9hjANBgkqhkiG9w0BAQUFADBw\n" +
"MQswCQYDVQQGEwJWTjE/MD0GA1UEChM2VmlldG5hbSBFRlkgSW5mb3JtYXRpY3Mg\n" +
"VGVjaG5vbG9neSBKb2ludCBTdG9jayBDb21wYW55MQ8wDQYDVQQLEwZFRlktQ0Ex\n" +
"DzANBgNVBAMTBkVGWS1DQTAeFw0xODA3MzAwOTI3MDBaFw0yMTA3MjkxNzE2NTNa\n" +
"MIHoMQswCQYDVQQGEwJWTjEWMBQGA1UECAwNUXXhuqNuZyBCw6xuaDFaMFgGA1UE\n" +
"BwxRxJDGsOG7nW5nIFF1w6FjaCBYdcOibiBL4buzIC0gUGjGsOG7nW5nIEjhuqNp\n" +
"IMSQw6xuaCAtIFRow6BuaCBwaOG7kSDEkOG7k25nIEjhu5tpMUEwPwYDVQQDDDhD\n" +
"SEkgTkjDgU5IIFFV4bqiTkcgQsOMTkggLSBDw5RORyBUWSBD4buUIFBI4bqmTiBW\n" +
"SU5QRUFSTDEiMCAGCgmSJomT8ixkAQEMEk1TVDo0MjAwNDU2ODQ4LTAyMjCCASIw\n" +
"DQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBAL/jmfiCGLvvUrIDaz5p0tqCoY7z\n" +
"uMcS5KVUeJZtVyxGIf74xZj7+8LZwe4nOd+CLAc6af/qO4hYs8YbNHcTrypkDquf\n" +
"GQBO2YMlwZYlx7wZJnSNKjI4MwVXSPA12o0Pd3DVosgrAaXhB3Zx3qtTK2H8bKwe\n" +
"fA8oW6R+QhpEcDbhn6SS7uE9fCkXqEpPrPyjaEPumkHFSn0in8NbT+jl1ybDJj3k\n" +
"1QnMTlk/etihd41woDbWelLCNbAWP4ZQg35ttoVqL0cC376Ksbi3OHfWhtjrIYld\n" +
"wS3ScLDd3OLfsIjyRD8GMEeq8UxJuytVkT0enlGXMnRcm2kVQpH5U4VGmRECAwEA\n" +
"AaOCASowggEmMAwGA1UdEwEB/wQCMAAwHwYDVR0jBBgwFoAUpRvUKkDl4DftlFbJ\n" +
"opP8SDXzudMwYAYIKwYBBQUHAQEEVDBSMC4GCCsGAQUFBzAChiJodHRwOi8vdmEu\n" +
"ZWZ5Y2Eudm4vY2VydHMvZWZ5Y2EuY3J0MCAGCCsGAQUFBzABhhRodHRwOi8vb2Nz\n" +
"cC5lZnljYS52bjAYBgNVHSAEETAPMA0GCysGAQQBge0DAQoBMB0GA1UdJQQWMBQG\n" +
"CCsGAQUFBwMCBggrBgEFBQcDBDArBgNVHR8EJDAiMCCgHqAchhpodHRwOi8vdmEu\n" +
"ZWZ5Y2Eudm4vcHViL0NSTDAdBgNVHQ4EFgQU6gOrdTpsvIiP5vpDHlvQPQ6dYZ4w\n" +
"DgYDVR0PAQH/BAQDAgXgMA0GCSqGSIb3DQEBBQUAA4IBAQACRJc8FWUUxP0WyCYW\n" +
"nroHpLIpH7qAhYX7yrC/LNC5Zn8QHg9SGV+sfcXhyxk13l0AHe3KjC0NY9b/+fgC\n" +
"hvcZ4qswVftqMrpm2zQEcSVoi0v+dnxypzXntlYkICjA+TR11As/qODaQOrh/UhG\n" +
"Z0iH3bocbhAcet+QAOFC1S+GTO/jgr6ODhQnnCIR5/u+0NCjSJ5MZMuElUaqS0cH\n" +
"Etbvr0ys0o5JAwLmoroKv158QP4irJIokp/7knklKxKpsxJznJMyJu2XbzfVPoTg\n" +
"dSECjg21vWqfTwswaumLfecufUzX99QJieSxEhImrXSsITAWMfG9xPsFWZ5ZWOWC\n" +
"hWSf";
        CommonFunction com = new CommonFunction();
        int[] intRes = new int[1];
        Object[] sss = new Object[2];
        String[] tmp = new String[3];
        String strNotBefore = "";
        com.VoidCertificateComponents(strCertificate, sss, tmp, intRes);
        if (intRes[0] == 0 && sss.length > 0) {
            Object strSubjectDN = sss[0].toString().replace(", ", "\n");
            Object strIssuerDN = sss[1].toString().replace(", ", "\n");
            strNotBefore = EscapeUtils.CheckTextNull(tmp[1]);
            String strNotAfter = EscapeUtils.CheckTextNull(tmp[2]);
        }
        System.out.println(strNotBefore);

*/

//        PdfWriter writer = null;
//        try {
//            String html = PrintReport.createStringHtml(pathXSLT, pathXML);
//            String[] sResult = new String[1];
////            PrintFormFunction.convertPdf(html, fileHTML, filePDF, sResult);
////            if (PrintReport.createFile(fileHTML, html)) {
////                writer = new PdfWriter(filePDF);
////                PdfDocument pdf = new PdfDocument(writer);
////                pdf.setTagged();
////                PageSize pageSize = PageSize.A4.rotate();
////                pdf.setDefaultPageSize(pageSize);
////                ConverterProperties properties = new ConverterProperties();
////                MediaDeviceDescription mediaDeviceDescription
////                        = new MediaDeviceDescription(MediaType.SCREEN);
////                mediaDeviceDescription.setWidth(pageSize.getWidth());
////                properties.setMediaDeviceDescription(mediaDeviceDescription);
////                FontProvider fontProvider = new DefaultFontProvider(false, false, false);
////                for (String font : FONTS) {
////                    try {
////                        FontProgram fontProgram = FontProgramFactory.createFont(font);
////                        fontProvider.addFont(fontProgram);
////                    } catch (IOException ex) {
////                        Logger.getLogger(PrintReport.class.getName()).log(Level.SEVERE, null, ex);
////                    }
////                }
////                properties.setFontProvider(fontProvider);
////
////                PrintReport.convertPdf(fileHTML, filePDF, properties);
////            };
//
//        } catch (Exception ex) {
//            log.error(ex.getMessage(), ex);
//        }
//        finally {
//            try {
//                writer.close();
//            } catch (IOException ex) {
//                log.error(ex.getMessage(), ex);
//            }
//        }
    }
    
    public static String createStringHtmlInString(String strXslt, String strData, byte[] img,
        boolean isImg, boolean isNeedBg, int[] intResult) {
        String response = null;
        StringWriter writer = new StringWriter();
        try {

            InputStream inputXslt = new ByteArrayInputStream(strXslt.getBytes(Charset.forName("UTF-8")));
            InputStream inputData = new ByteArrayInputStream(strData.getBytes(Charset.forName("UTF-8")));
            TransformerFactory tFactory = TransformerFactory.newInstance();
            Transformer transformer = tFactory.newTransformer(
                    new javax.xml.transform.stream.StreamSource(inputXslt));
            transformer.transform(
                    new javax.xml.transform.stream.StreamSource(inputData),
                    new javax.xml.transform.stream.StreamResult(writer));
            String html = writer.toString();
            if (isImg) {
                html = html.replaceAll("@imgBg", "data:image/jpeg;base64," + Arrays.toString(img));
            }
            if (!isNeedBg) {
                html = html.replaceAll("@display", "none");
            }
            response = html;
            intResult[0] = 0;
        } catch (TransformerException ex) {
            intResult[0] = 1;
            CommonFunction.LogExceptionServlet(log, "CreateStringReport: " + ex.getMessage(), ex);
        } finally {
            try {
                writer.close();
            } catch (IOException e) {
                intResult[0] = 1;
                CommonFunction.LogExceptionServlet(log, "CreateStringReport: " + e.getMessage(), e);
            }

        }
        return response;
    }

//    public static byte[] createFile(String pathFile, String content) {
//        boolean result = true;
//        File files = new File(pathFile);
//        if (!files.exists()) {
//            try {
//                if (files.createNewFile()) {
//                    files.setReadable(true);
//                    files.setWritable(true);
//                    FileOutputStream fos = new FileOutputStream(files, false);
//                    BufferedOutputStream outputStream = new BufferedOutputStream(fos);
//                    outputStream.write(content.getBytes("UTF-8"));
//                    outputStream.close();
//                    fos.close();
//                }
//            } catch (IOException ex) {
//                result = false;
//                log.error(ex.getMessage(), ex);
//            }
//        } else {
//            files.delete();
//            try {
//                if (files.createNewFile()) {
//                    files.setReadable(true);
//                    files.setWritable(true);
//                    FileOutputStream fos = new FileOutputStream(files, false);
//                    BufferedOutputStream outputStream = new BufferedOutputStream(fos);
//                    outputStream.write(content.getBytes("UTF-8"));
//                    outputStream.close();
//                    fos.close();
//                }
//            } catch (IOException ex) {
//                result = false;
//                log.error(ex.getMessage(), ex);
//            }
//        }
//        return result;
//
//    }
//    public static void
    public static void convertPdf(String pathHTML, String pathPDF, ConverterProperties properties) {
        try {
            HtmlConverter.convertToPdf(new File(pathHTML), new File(pathPDF), properties);
        } catch (IOException ex) {
            log.error(ex.getMessage(), ex);
        }
    }

    public static boolean createFile(String pathFile, String content) {
        boolean result = true;
        File files = new File(pathFile);
        if (!files.exists()) {
            try {
                if (files.createNewFile()) {
                    files.setReadable(true);
                    files.setWritable(true);
                    FileOutputStream fos = new FileOutputStream(files, false);
                    BufferedOutputStream outputStream = new BufferedOutputStream(fos);
                    outputStream.write(content.getBytes("UTF-8"));
                    outputStream.close();
                    fos.close();
                }
            } catch (IOException ex) {
                result = false;
                log.error(ex.getMessage(), ex);
            }
        } else {
            files.delete();
            try {
                if (files.createNewFile()) {
                    files.setReadable(true);
                    files.setWritable(true);
                    FileOutputStream fos = new FileOutputStream(files, false);
                    BufferedOutputStream outputStream = new BufferedOutputStream(fos);
                    outputStream.write(content.getBytes("UTF-8"));
                    outputStream.close();
                    fos.close();
                }
            } catch (IOException ex) {
                result = false;
                log.error(ex.getMessage(), ex);
            }
        }
        return result;

    }

    public static String createStringHtml(String templetPath, String xmlPath) {
//        byte[] response = null;
        String response = null;
        byte[] byteSignedXML;
        StringWriter writer = new StringWriter();
        try {
            File file = new File(xmlPath);
            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream inputStream = new BufferedInputStream(fis);
            byteSignedXML = new byte[(int) file.length()];
            inputStream.read(byteSignedXML);
            inputStream.close();
            String strTextSign;
            String strImageSign;

            TransformerFactory tFactory = TransformerFactory.newInstance();
            Transformer transformer = tFactory.newTransformer(
                    new javax.xml.transform.stream.StreamSource(new File(templetPath)));
            transformer.transform(
                    new javax.xml.transform.stream.StreamSource(new File(xmlPath)),
                    new javax.xml.transform.stream.StreamResult(writer));
            String html = writer.toString();
            System.out.println("STRING:" + html);
//            response = html.getBytes("UTF-8");
            response = html;
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        } finally {
            try {
                writer.close();
            } catch (Exception ex) {
                log.error(ex.getMessage(), ex);
            }

        }
        return response;
    }

    public static String createStringHtmlInString_BK(String templetPath, String xmlPath) {
        String response = null;
        byte[] byteSignedXML;
        StringWriter writer = new StringWriter();
        try {
            File file = new File(xmlPath);
            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream inputStream = new BufferedInputStream(fis);
            byteSignedXML = new byte[(int) file.length()];
            inputStream.read(byteSignedXML);
            inputStream.close();
            String strTextSign;
            String strImageSign;

            TransformerFactory tFactory = TransformerFactory.newInstance();
            Transformer transformer = tFactory.newTransformer(
                    new javax.xml.transform.stream.StreamSource(new File(templetPath)));
            transformer.transform(
                    new javax.xml.transform.stream.StreamSource(new File(xmlPath)),
                    new javax.xml.transform.stream.StreamResult(writer));
            String html = writer.toString();
            System.out.println("STRING:" + html);
//            response = html.getBytes("UTF-8");
            response = html;
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        } finally {
            try {
                writer.close();
            } catch (Exception ex) {
                log.error(ex.getMessage(), ex);
            }

        }
        return response;
    }

    public static String createXMLRegistration(String sName, String isCMND, String isHC, String So, String NgayCap,
            String NoiCap, String DiaChi, String DienThoai, String isCapMoi, String isGiaHan, String is1Nam,
            String is2Nam, String is3Nam, String isKhac, String NoiDungKhac, String ChucVu, String PhongBan, String ToChuc,
            String DiaChiToChuc, String MST, String Email, String Mobile, String ThoiGianDiaDiem, String isGiayDKCapCTS,
            String isBanSaoCMNDHC, String isGiayDKCapCTS1, String isBanSaoGPKD, String isBanSaoDKThue,
            String isVanBanUyQuyen, String isBanSaoCMNDHC1) {
        String sXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<CCTSCN>\n"
                + "    <ThongTinCaNhan>\n"
                + "        <HoTen>" + sName + "</HoTen>\n"
                + "        <isCMND>" + isCMND + "</isCMND>\n"
                + "        <isHC>" + isHC + "</isHC>\n"
                + "        <So>" + So + "</So>\n"
                + "        <NgayCap>" + NgayCap + "</NgayCap>\n"
                + "        <NoiCap>" + NoiCap + "</NoiCap>\n"
                + "        <DiaChi>" + DiaChi + "</DiaChi>\n"
                + "        <DienThoai>" + DienThoai + "</DienThoai>\n"
                + "    </ThongTinCaNhan>\n"
                + "    <GoiDangKy>\n"
                + "        <DoiTuong>\n"
                + "            <isCapMoi>" + isCapMoi + "</isCapMoi>\n"
                + "            <isGiaHan>" + isGiaHan + "</isGiaHan>\n"
                + "        </DoiTuong>\n"
                + "        <ThoiGianSuDung>\n"
                + "            <is1Nam>" + is1Nam + "</is1Nam>\n"
                + "            <is2Nam>" + is2Nam + "</is2Nam>\n"
                + "            <is3Nam>" + is3Nam + "</is3Nam>\n"
                + "            <isKhac>" + isKhac + "</isKhac>\n"
                + "            <NoiDungKhac>" + NoiDungKhac + "</NoiDungKhac>\n"
                + "        </ThoiGianSuDung>\n"
                + "    </GoiDangKy>\n"
                + "    <CaNhanThuocToChuc>\n"
                + "        <ChucVu>" + ChucVu + "</ChucVu>\n"
                + "        <PhongBan>" + PhongBan + "</PhongBan>\n"
                + "        <ToChuc>" + ToChuc + "</ToChuc>\n"
                + "        <DiaChi>" + DiaChi + "</DiaChi>\n"
                + "        <MST>" + MST + "</MST>\n"
                + "        <Email>" + Email + "</Email>\n"
                + "        <Mobile>" + Mobile + "</Mobile>\n"
                + "    </CaNhanThuocToChuc>\n"
                + "    <ThoiGianDiaDiem>\n"
                + "        " + ThoiGianDiaDiem + "\n"
                + "    </ThoiGianDiaDiem>\n"
                + "    <HoSoKemTheo>\n"
                + "        <CaNhan>\n"
                + "            <isGiayDKCapCTS>" + isGiayDKCapCTS + "</isGiayDKCapCTS>\n"
                + "            <isBanSaoCMNDHC>" + isBanSaoCMNDHC + "</isBanSaoCMNDHC>\n"
                + "        </CaNhan>\n"
                + "        <ToChuc>\n"
                + "            <isGiayDKCapCTS1>" + isGiayDKCapCTS1 + "</isGiayDKCapCTS1>\n"
                + "            <isBanSaoGPKD>" + isBanSaoGPKD + "</isBanSaoGPKD>\n"
                + "            <isBanSaoDKThue>" + isBanSaoDKThue + "</isBanSaoDKThue>\n"
                + "            <UyQuyen>\n"
                + "                <isVanBanUyQuyen>" + isVanBanUyQuyen + "</isVanBanUyQuyen>\n"
                + "                <isBanSaoCMNDHC1>" + isBanSaoCMNDHC1 + "</isBanSaoCMNDHC1>\n"
                + "            </UyQuyen>\n"
                + "        </ToChuc>\n"
                + "    </HoSoKemTheo>\n"
                + "</CCTSCN>";

        return sXML;
    }

    public static String createXMLCertificate(String sName, String CongTy, String SubjectDN, String IssuerDN, String Serial,
            String ThoiHanCTSTo, String ThoiHanCTSFrom, String ThoiHanHDTo,
            String ThoiHanHDFrom, String DoDaiKhoa, String TinhNang) {
        String sXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<GCN>\n"
                + "    <TenKH>" + sName + "</TenKH>\n"
                + "    <CongTy>" + CongTy + "</CongTy>\n"
                + "    <SubjectDN>" + SubjectDN + "</SubjectDN>\n"
                + "    <IssuerDN>" + IssuerDN + "</IssuerDN>\n"
                + "    <Serial>" + Serial + "</Serial>\n"
                + "    <ThoiHanCTS>\n"
                + "        <Tu>" + ThoiHanCTSTo + "</Tu>\n"
                + "        <Den>" + ThoiHanCTSFrom + "</Den>\n"
                + "    </ThoiHanCTS>\n"
                + "    <ThoiHanHD>\n"
                + "        <Tu>" + ThoiHanHDTo + "</Tu>\n"
                + "        <Den>" + ThoiHanHDFrom + "</Den>\n"
                + "    </ThoiHanHD>\n"
                + "    <DoDaiKhoa>" + DoDaiKhoa + "</DoDaiKhoa>\n"
                + "    <TinhNang>" + TinhNang + "</TinhNang>\n"
                + "</GCN>";

        return sXML;
    }
}
