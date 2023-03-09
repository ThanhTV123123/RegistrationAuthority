/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ra.uat;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfAnnotation;
import com.itextpdf.text.pdf.PdfBorderArray;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfDestination;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.codec.Base64;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.css.StyleAttrCSSResolver;
import com.itextpdf.tool.xml.html.CssAppliers;
import com.itextpdf.tool.xml.html.CssAppliersImpl;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CSSResolver;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.PdfWriterPipeline;
import com.itextpdf.tool.xml.pipeline.html.AbstractImageProvider;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;
import java.io.BufferedInputStream;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipOutputStream;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import vn.ra.object.CERTIFICATION;
import vn.ra.object.CERTIFICATION_AUTHORITY;
import vn.ra.process.CommonFunction;
import vn.ra.process.ConnectDatabase;
import vn.ra.process.PrintFormFunction;
import vn.ra.utility.Definitions;
import vn.ra.utility.EscapeUtils;

/**
 *
 * @author USER
 */
public class TestPDFConvert {

    public static void main(String[] args) throws IOException, DocumentException, Docx4JException, JAXBException, ParserConfigurationException, TransformerException, Exception {
//        String sHtmlPDF = "<html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:ds=\"http://www.w3.org/2000/09/xmldsig#\">\n" +
//"    <head>\n" +
//"        <style>\n" +
//"            body {\n" +
//"                color: #000;\n" +
//"                font-size: 16px;\n" +
//"                width: 21cm;\n" +
//"                height: 29.7cm; \n" +
//"                font-family: \"Times New Roman\", Times, serif;\n" +
//"                margin: 0 auto;\n" +
//"                line-height: 19px;\n" +
//"            }\n" +
//"            .toUpper{\n" +
//"                font-weight: bold;\n" +
//"            }\n" +
//"            .wrapper{\n" +
//"                height: 28px;\n" +
//"            }\n" +
//"        </style>\n" +
//"    </head>\n" +
//"    <body>\n" +
//"            <div>\n" +
//"                    <div>GIẤY ĐĂNG KÝ CẤP CHỨNG THƯ SỐ TRUSTCA</div>\n" +
//"                    <p>III. Trường hợp cá nhân thuộc Tổ chức/Doanh nghiệp</p>\n" +
//"                </div>\n" +
//"    </body>\n" +
//"</html>";

        String sHtmlPDF = "<html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:ds=\"http://www.w3.org/2000/09/xmldsig#\"><head><meta charset=\"UTF-8\"/><meta content=\"width=device-width, initial-scale=1.0\" name=\"viewport\"/><style>\n"
                + "            \n"
                + "                    @page{\n"
                + "                    margin: 0;\n"
                + "                     size: A4;\n"
                + "                    }\n"
                + "                        *{\n"
                + "                    margin: 0;\n"
                + "                    padding: 0;\n"
                + "                    }\n"
                + "            \n"
                + "                    p{\n"
                + "                    margin: 0px;\n"
                + "                    padding:0px;\n"
                + "                    }\n"
                + "            \n"
                + "                    body {\n"
                + "                    color: #000;\n"
                + "                    font-size: 16px;\n"
                + "                    width: 21cm;\n"
                + "                    height: 29.7cm; \n"
                + "                    font-family: \"Times New Roman\", Times, serif;\n"
                + "                    margin: 0 auto;\n"
                + "                    line-height: 19px;\n"
                + "                    }\n"
                + "            \n"
                + "                    .page{\n"
                + "                    padding: 25px 60px 15px 70px;\n"
                + "                    }\n"
                + "                    \n"
                + "                    .clFix{\n"
                + "                    padding-left: 15px;\n"
                + "                    }\n"
                + "                    .spIcon{\n"
                + "                    width: 10px;\n"
                + "                    \n"
                + "                    }\n"
                + "                    table{\n"
                + "                    width: 100%;\n"
                + "                    }\n"
                + "                    .pdLeft10{\n"
                + "                    padding-left: 20px;\n"
                + "                    }\n"
                + "                    .tblDetail td{\n"
                + "                    padding:5px;\n"
                + "                    }\n"
                + "                    .tblDetail th, .tblDetail td{\n"
                + "                    border: 0.1px solid #000;\n"
                + "                    }\n"
                + "                    \n"
                + "                    \n"
                + "                    .toUpper{\n"
                + "                    font-weight: bold;\n"
                + "                    }\n"
                + "                   \n"
                + "                    \n"
                + "                    td.fill { float: left; overflow: hidden; white-space: nowrap }\n"
                + "                    \n"
                + "                    .divfooter {\n"
                + "                        clear: both;\n"
                + "                        bottom:10px;\n"
                + "                    }\n"
                + "                   \n"
                + "                    table.tblFooterPage{\n"
                + "                    font-size: 10pt;\n"
                + "                    width: 100%;\n"
                + "                    border-collapse: collapse;\n"
                + "                    }\n"
                + "                    table.tblFooterPage, table.tblFooterPage th, table.tblFooterPage td {\n"
                + "                    border: 0.1px solid black;\n"
                + "                    }\n"
                + "                    \n"
                + "                    table.tblFooterPage td{\n"
                + "                    padding: 10px;\n"
                + "                    \n"
                + "                    }\n"
                + "                    \n"
                + "                    .imgWatermark{\n"
                + "                    opacity: 0.2;\n"
                + "                    margin-top: 146px;\n"
                + "                    position: absolute;\n"
                + "                    margin-left: 100px;\n"
                + "                    width: 420px;\n"
                + "                    z-index: -1;\n"
                + "                    }\n"
                + "                    .clLine{\n"
                + "                    text-align: center;\n"
                + "                    width: 100%;\n"
                + "                    }\n"
                + "                    \n"
                + "                    .clLine span{\n"
                + "                    width: 50%;\n"
                + "                    border-bottom: 1px solid;\n"
                + "                    height: 1px;\n"
                + "                    display: inline-block;\n"
                + "                    }\n"
                + "\n"
                + "                </style></head><body><div class=\"page pageA4\"><table style=\"width:100%\" class=\"tblHeader\"><tr><td style=\"width: 252px\"><img src=\"data:image/jpeg;base64,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\" style=\"width:194px;height:68px;\" alt=\"Logo\" class=\"logo pt6pt\"/></td><td style=\"width: 540px\"><div style=\"text-align: center;\" class=\"logoRight\"><p style=\"font-weight: bold;\" class=\"toUpper brandTop\">CỘNG HÒA XÃ HỘI CHỦ NGHĨA VIỆT NAM</p><p style=\"font-size: 17px;font-weight: bold;padding-top:6px;\" class=\"brandUnder\">Độc lập - Tự do - Hạnh phúc </p><div class=\"clLine\"><span/></div></div></td></tr></table><div style=\"position: relative;\" class=\"content\"><img src=\"data:image/png;base64,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\" class=\"imgWatermark\"/><div style=\"font-size: 19px;text-align: center;font-weight: bold;padding-top:12px;\">GIẤY ĐĂNG KÝ CẤP CHỨNG THƯ SỐ TRUSTCA</div><p class=\"toUpper\" style=\"padding-top:12px;\">I. Thông tin cá nhân đăng ký</p><div class=\"box pdLeft10\"><table style=\"width: 100%\"><tr><td style=\"width:20px;padding-top:6px;\">1.</td><td colspan=\"3\" style=\"padding-top:6px;\" class=\"fill\">\n"
                + "                                            Họ tên cá nhân: <span>Nguyễn Văn A</span></td></tr><tr style=\"padding-top:6px;\"><td style=\"padding-top:6px;\">2.</td><td style=\"padding-top:6px;width:20%;\"><input disabled=\"disabled\" type=\"checkbox\"/> Giấy CMND\n"
                + "                                        </td><td colspan=\"2\" style=\"padding-top:6px;\"><input disabled=\"disabled\" type=\"checkbox\"/> Hộ chiếu\n"
                + "                                        </td></tr><tr style=\"padding-top:6px;\"><td style=\"padding-top:6px;\"> </td><td style=\"padding-top:6px;width:20%;\">\n"
                + "                                            Số: <span>20200203</span></td><td style=\"padding-top:6px;width:180px;\">\n"
                + "                                            Ngày cấp: <span/></td><td style=\"padding-top:6px;\">\n"
                + "                                            Nơi cấp: <span/></td></tr><tr><td style=\"padding-top:6px;\">3.</td><td colspan=\"3\" style=\"padding-top:6px;\">\n"
                + "                                            Địa chỉ thường trú: Q2, TP Hồ Chí Minh</td></tr><tr><td style=\"padding-top:6px;\">4.</td><td colspan=\"3\" style=\"padding-top:6px;\">\n"
                + "                                            Điện thoại: 0906332271</td></tr></table></div><p class=\"toUpper\" style=\"padding-top:12px;\">II. Đăng ký sử dụng Gói chứng thư số</p><div class=\"box pdLeft10\"><table style=\"width: 780px\"><tr><td style=\"width:20px;padding-top:6px;\">1.</td><td style=\"padding-top:6px;width:150px\">Đối tượng:</td><td style=\"padding-top:6px;width:90px\"><input disabled=\"disabled\" type=\"checkbox\"/> Cấp mới\n"
                + "                                        </td><td colspan=\"3\" style=\"padding-top:6px;\"><input disabled=\"disabled\" type=\"checkbox\"/> Gia hạn\n"
                + "                                        </td></tr><tr style=\"padding-top:6px;\"><td style=\"padding-top:6px;\">2.</td><td style=\"padding-top:6px;width:150px\">\n"
                + "                                            Thời gian sử dụng:\n"
                + "                                        </td><td style=\"padding-top:6px;width:90px\"><input disabled=\"disabled\" type=\"checkbox\"/> 1 năm\n"
                + "                                        </td><td style=\"padding-top:6px;width:90px\"><input disabled=\"disabled\" type=\"checkbox\" checked=\"checked\"/> 2 năm\n"
                + "                                        </td><td style=\"padding-top:6px;width:90px\"><input disabled=\"disabled\" type=\"checkbox\"/> 3 năm\n"
                + "                                        </td><td style=\"padding-top:6px;\"><input disabled=\"disabled\" type=\"checkbox\"/> Khác: </td></tr></table></div><p class=\"toUpper\" style=\"padding-top:12px;\">III. Trường hợp cá nhân thuộc Tổ chức/Doanh nghiệp</p><div class=\"box pdLeft10\"><table style=\"width: 780px\"><tr><td style=\"width:20px;padding-top:6px;\">1.</td><td colspan=\"2\" style=\"padding-top:6px;\">\n"
                + "                                            Chức vụ: </td></tr><tr style=\"padding-top:6px;\"><td style=\"padding-top:6px;\">2.</td><td colspan=\"2\" style=\"padding-top:6px;\">\n"
                + "                                            Tên phòng ban: </td></tr><tr style=\"padding-top:6px;\"><td style=\"padding-top:6px;\">3.</td><td colspan=\"2\" style=\"padding-top:6px;\">\n"
                + "                                            Tên Tổ chức: </td></tr><tr style=\"padding-top:6px;\"><td style=\"padding-top:6px;\">4.</td><td colspan=\"2\" style=\"padding-top:6px;\">\n"
                + "                                            Địa chỉ: </td></tr><tr style=\"padding-top:6px;\"><td style=\"padding-top:6px;\">5.</td><td colspan=\"2\" style=\"padding-top:6px;\">\n"
                + "                                            Mã số thuế/mã ngân sách của Tổ chức/Doanh nghiệp: </td></tr><tr style=\"padding-top:6px;\"><td style=\"padding-top:6px;\">6.</td><td style=\"padding-top:6px;width:330px;\">\n"
                + "                                            Email: </td><td style=\"padding-top:6px;\">\n"
                + "                                            Mobile: </td></tr></table></div><div style=\"text-align:right\" class=\"box\"><table style=\"width: 100%\" class=\"tblFooter\"><tr><td style=\"width:40%\">\n"
                + "                                         \n"
                + "                                        </td><td style=\"text-align: center;padding-top:20px;\"><p class=\"\"><i>........., ngày.....tháng.....năm ......</i></p><p class=\"toUpper\">\n"
                + "                                                KHÁCH HÀNG\n"
                + "                                            </p><p><i>(ký, ghi rõ họ tên)</i></p></td></tr></table></div></div>\n"
                + "                        <div style=\"width: 100%;height: 90px;\"></div>\n"
                + "                        <div class=\"divfooter\"><div class=\"box\"><p><strong>HỒ SƠ KÈM THEO:</strong></p><table class=\"tblFooterPage\"><tr><td style=\"width:35%;vertical-align: top; \"><p><strong>\n"
                + "                                                    Đối với cá nhân:</strong></p><p><input disabled=\"disabled\" type=\"checkbox\"/> 01 Giấy đăng ký cấp chứng thư số\n"
                + "                                            </p><p><input disabled=\"disabled\" type=\"checkbox\"/> 01 Bản sao CMND/Hộ chiếu \n"
                + "                                            </p></td><td><p><strong>Đối với tổ chức:</strong></p><p><input disabled=\"disabled\" type=\"checkbox\"/> 01 Giấy đăng ký cấp chứng thư số\n"
                + "                                            </p><p><input disabled=\"disabled\" type=\"checkbox\"/> 01 Bản sao quyết định thành lập hoặc giấy chứng nhận đăng ký doanh nghiệp hoặc tài liệu tương đương\n"
                + "                                            </p><p><input disabled=\"disabled\" type=\"checkbox\"/> 01 Bản sao quyết định thành lập hoặc giấy chứng nhận đăng ký doanh nghiệp hoặc tài liệu tương đương\n"
                + "                                            </p><p><input disabled=\"disabled\" type=\"checkbox\"/> 01 Bản sao Giấy chứng nhận đăng ký thuế (Nếu có)\n"
                + "                                            </p><p style=\"font-style: italic;\"><i>Trường hợp ủy quyền cần bổ sung:</i></p><p><input disabled=\"disabled\" type=\"checkbox\"/> 01 Văn bản ủy quyền \n"
                + "                                            </p><p><input disabled=\"disabled\" type=\"checkbox\"/> 01 Bản sao CMND/Hộ chiếu đại diện pháp luật của người đại diện theo ủy quyền\n"
                + "                                            </p></td></tr></table></div></div></div></body></html>";

        String[] sCode = new String[1];
        String sPathRegisPDF = "D:\\Programer\\Company\\TMS-RA\\TrustCA\\Common\\" + CommonFunction.getDateFormat() + Definitions.CONFIG_EXPORT_FILENAME_EXTENTION_PDF;
//        PrintFormFunction.convertWord(sHtmlPDF.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", ""), sPathRegisPDF, "", sCode);

        int[] tempResCode = new int[1];
//        String sPathTemplatePDF = "D:\\Project\\Hoa Don Dien Tu\\FPT HDDT\\CoreWs\\FPTEInvoiceData\\Templet/ViNaVAT_PDF.xslt";
//        String sPathImagePDF = "D:\\Project\\Hoa Don Dien Tu\\FPT HDDT\\CoreWs\\FPTEInvoiceData\\Logo\\0312671405\\01GTKT0/004.png";
//        String sFileHTML = "D:\\Programer\\Company\\TMS-RA\\TrustCA\\Common\\";
        String sPDFOut = "D:\\Programer\\Company\\TMS-RA\\TrustCA\\Common\\" + CommonFunction.getDateFormat() + ".pdf";
//        createFile();
//        String sHtmlPDF = Utils.CreateStringHTMLFinal(sPathTemplatePDF, IMAGE, sFileSign, tempResCode);
        String sHtmlPDF1 = "<HTML><HEAD></HEAD><BODY><H1>Testing</H1><FORM>"
                + "check : <INPUT TYPE='checkbox' checked=checked/><br/>"
                + "</FORM></BODY></HTML>";
//        CreatePDFFromHTML(sHtmlPDF1, sPDFOut, tempResCode);
        WriteFileRar01();
//        int[] intCode = new int[1];
//        PrintFormFunction.CreatePDFFromHTML("","D:\\Programer\\Company\\TMS-RA\\TrustCA\\Common\\REGIS_" + CommonFunction.getDateFormat() + Definitions.CONFIG_EXPORT_FILENAME_EXTENTION_PDF,
//            intCode);
    }

    public static void WriteFileRar01() throws ParserConfigurationException, TransformerException, FileNotFoundException, IOException, Exception {
//        Config conf = new Config();
//        String sNameFile = conf.GetPropertybyCode(Definitions.CONFIG_NAMEFILE_LOGO);
//        String queryString = getServletContext().getRealPath("/");
//        String outputDirectory = queryString;
//        String absoluteDiskPath = outputDirectory + "/Images/" + sNameFile;
        String pathImage = "D:\\Programer\\Company\\TMS-RA\\TrustCA\\Primary\\RAPortal_EE6\\web\\Images\\Logo_NCCA.jpg";
        String sPathConfirmPDF = "D:\\Programer\\Company\\TMS-RA\\TrustCA\\Common\\CONFIRM_" + CommonFunction.getDateFormat() + Definitions.CONFIG_EXPORT_FILENAME_EXTENTION_PDF;
        String sPathRegisPDF = "D:\\Programer\\Company\\TMS-RA\\TrustCA\\Common\\REGIS_" + CommonFunction.getDateFormat() + Definitions.CONFIG_EXPORT_FILENAME_EXTENTION_PDF;
        String sPathRegisHTML = "D:\\Programer\\Company\\TMS-RA\\TrustCA\\Common\\REGIS_" + CommonFunction.getDateFormat() + Definitions.CONFIG_EXPORT_FILENAME_EXTENTION_HTML;
        String sFileNameZip = "D:\\Programer\\Company\\TMS-RA\\TrustCA\\Common\\" + CommonFunction.getDateFormat() + Definitions.CONFIG_EXPORT_FILENAME_EXTENTION_RAR;
        String pathConfirmXSLT = "";
        ConnectDatabase db = new ConnectDatabase();
        String PRINT_FULLNAME = "aa";
        String PRINT_TAXCODE = "1212121";
        String PRINT_REPRESENTATIVE = "ss";
        String PRINT_ROLE = "2s";
        String ThoiGianDiaDiem = "......, Ngay.....thang....nam......";
        String pathConfirmXML = PrintFormFunction.createXMLConfirmInfo(PRINT_FULLNAME, PRINT_TAXCODE, PRINT_REPRESENTATIVE,
                PRINT_ROLE, "", ThoiGianDiaDiem);
        int[] intResult = new int[1];
        int pCERTIFICATION_AUTHORITY_ID = 0;
        String pathRegisXSLT = "";
        CERTIFICATION[][] rsReq = new CERTIFICATION[1][];
        db.S_BO_CERTIFICATION_GET_INFO("1145", "1", rsReq);
        if (rsReq[0].length > 0) {
            pCERTIFICATION_AUTHORITY_ID = rsReq[0][0].CERTIFICATION_AUTHORITY_ID;
        }
        CERTIFICATION_AUTHORITY[][] rsCA = new CERTIFICATION_AUTHORITY[1][];
        db.S_BO_CERTIFICATION_AUTHORITY_DETAIL(String.valueOf(pCERTIFICATION_AUTHORITY_ID), rsCA);
        if (rsCA[0].length > 0) {
            pathRegisXSLT = EscapeUtils.CheckTextNull(rsCA[0][0].TEMPLATE_PERSONAL_REGISTRATION_PAPER);
            pathConfirmXSLT = EscapeUtils.CheckTextNull(rsCA[0][0].TEMPLATE_CONFIRMATION_PAPER);
        }
        String sResultConfirmHTML = PrintFormFunction.createStringHtmlInStringExtend(pathConfirmXSLT, pathConfirmXML, null, false, false, intResult);
        if (intResult[0] == 0) {
            String pathRegisXML = PrintFormFunction.createXMLRegistrationCNFinal2(PRINT_FULLNAME, PRINT_TAXCODE, "abc 123",
                "0908111111", "vanthanh@gmail.com", "1", "0", "0", "0",
                "", "an", "aan1 1", "0911111111", "aaa@gmail.com", ThoiGianDiaDiem, "1 năm", "0", "0", "6", "0", "0");
            String sResultRegisHTML = PrintFormFunction.createStringHtmlInStringExtend(pathRegisXSLT, pathRegisXML, null, false, false, intResult);
            PrintFormFunction.createFile(sPathRegisHTML, sResultRegisHTML);
            int[] tempResCode;
            tempResCode = new int[1];
            PrintFormFunction.CreatePDFFromHTML(sResultConfirmHTML, sPathConfirmPDF, tempResCode);
            String sPathConfirmPDF_temp = "D:\\Programer\\Company\\TMS-RA\\TrustCA\\Common\\CONFIRM_" + CommonFunction.getDateFormat() + "_temp" + Definitions.CONFIG_EXPORT_FILENAME_EXTENTION_PDF;
            PrintFormFunction.AddBackgroundImageToPDF(sPathConfirmPDF, sPathConfirmPDF_temp, pathImage, pathImage, tempResCode);   
            tempResCode = new int[1];
            PrintFormFunction.CreatePDFFromHTML(sResultRegisHTML, sPathRegisPDF, tempResCode);
            String sPathRegisPDF_temp = "D:\\Programer\\Company\\TMS-RA\\TrustCA\\Common\\REGIS_" + CommonFunction.getDateFormat() + "_temp" + Definitions.CONFIG_EXPORT_FILENAME_EXTENTION_PDF;
            PrintFormFunction.AddBackgroundImageToPDF(sPathRegisPDF, sPathRegisPDF_temp, pathImage, pathImage, tempResCode);   
            try (FileOutputStream fos = new FileOutputStream(sFileNameZip)) {
                ZipOutputStream zos = new ZipOutputStream(fos);
                CommonFunction.addToZipFile(sPathRegisPDF_temp, zos);
                CommonFunction.addToZipFile(sPathConfirmPDF_temp, zos);
                zos.close();
            }
            System.out.println("path file: " + sFileNameZip);
        } else {
            System.out.println("Error");
        }
    }
    
    public static void writeFileRarPersonal(int pCERTIFICATION_AUTHORITY_ID, String pathImage, String sPathConfirmPDF, String sPathConfirmPDF_temp,
        String sPathRegisPDF, String sPathRegisPDF_temp, String sFileNameZip,
        String PRINT_FULLNAME, String PRINT_TAXCODE, String PRINT_REPRESENTATIVE, String PRINT_ROLE, String ThoiGianDiaDiem,
        String PRINT_ADDRESS_BILLING, String PRINT_PHONE, String PRINT_EMAIL, String is1Nam, String is2Nam, String is3Nam, String isKhac,
        String NoiDungKhac, String sNameReceive, String sAddressReceive, String sPhoneReceive, String sEmailReceive, String soNam,
        String is6Thang, String is4Nam, int[] sReturn)
        throws ParserConfigurationException, TransformerException, FileNotFoundException, IOException, Exception
    {
        sReturn[0] = 0;
//        String sPathConfirmPDF = "D:\\Programer\\Company\\TMS-RA\\TrustCA\\Common\\CONFIRM_" + CommonFunction.getDateFormat() + Definitions.CONFIG_EXPORT_FILENAME_EXTENTION_PDF;
//        String sPathConfirmPDF_temp = "D:\\Programer\\Company\\TMS-RA\\TrustCA\\Common\\CONFIRM_" + CommonFunction.getDateFormat() + "_temp" + Definitions.CONFIG_EXPORT_FILENAME_EXTENTION_PDF;
//        String sPathRegisPDF = "D:\\Programer\\Company\\TMS-RA\\TrustCA\\Common\\REGIS_" + CommonFunction.getDateFormat() + Definitions.CONFIG_EXPORT_FILENAME_EXTENTION_PDF;
//        String sPathRegisPDF_temp = "D:\\Programer\\Company\\TMS-RA\\TrustCA\\Common\\REGIS_" + CommonFunction.getDateFormat() + "_temp" + Definitions.CONFIG_EXPORT_FILENAME_EXTENTION_PDF;
//        String sPathRegisHTML = "D:\\Programer\\Company\\TMS-RA\\TrustCA\\Common\\REGIS_" + CommonFunction.getDateFormat() + Definitions.CONFIG_EXPORT_FILENAME_EXTENTION_HTML;
//        String sFileNameZip = "D:\\Programer\\Company\\TMS-RA\\TrustCA\\Common\\" + CommonFunction.getDateFormat() + Definitions.CONFIG_EXPORT_FILENAME_EXTENTION_RAR;
        String pathConfirmXSLT = "";
        ConnectDatabase db = new ConnectDatabase();
        String pathConfirmXML = PrintFormFunction.createXMLConfirmInfo(PRINT_FULLNAME, PRINT_TAXCODE, PRINT_REPRESENTATIVE,
                PRINT_ROLE, "", ThoiGianDiaDiem);
        int[] intResult = new int[1];
        String pathRegisXSLT = "";
        CERTIFICATION_AUTHORITY[][] rsCA = new CERTIFICATION_AUTHORITY[1][];
        db.S_BO_CERTIFICATION_AUTHORITY_DETAIL(String.valueOf(pCERTIFICATION_AUTHORITY_ID), rsCA);
        if (rsCA[0].length > 0) {
            pathRegisXSLT = EscapeUtils.CheckTextNull(rsCA[0][0].TEMPLATE_PERSONAL_REGISTRATION_PAPER);
            pathConfirmXSLT = EscapeUtils.CheckTextNull(rsCA[0][0].TEMPLATE_CONFIRMATION_PAPER);
        }
        String sResultConfirmHTML = PrintFormFunction.createStringHtmlInStringExtend(pathConfirmXSLT, pathConfirmXML, null, false, false, intResult);
        System.out.println(sResultConfirmHTML);
        if (intResult[0] == 0) {
            String pathRegisXML = PrintFormFunction.createXMLRegistrationCNFinal2(PRINT_FULLNAME, PRINT_TAXCODE, PRINT_ADDRESS_BILLING,
                PRINT_PHONE, PRINT_EMAIL, is1Nam, is2Nam, is3Nam, isKhac,
                NoiDungKhac, sNameReceive, sAddressReceive, sPhoneReceive, sEmailReceive, ThoiGianDiaDiem,
                soNam, is6Thang, is4Nam, "7", "0", "0");
            String sResultRegisHTML = PrintFormFunction.createStringHtmlInStringExtend(pathRegisXSLT, pathRegisXML, null, false, false, intResult);
//            PrintFormFunction.createFile(sPathRegisHTML, sResultRegisHTML);
            int[] tempResCode;
            tempResCode = new int[1];
            PrintFormFunction.CreatePDFFromHTML(sResultConfirmHTML, sPathConfirmPDF, tempResCode);
            PrintFormFunction.AddBackgroundImageToPDF(sPathConfirmPDF, sPathConfirmPDF_temp, pathImage, pathImage, tempResCode);   
            tempResCode = new int[1];
            PrintFormFunction.CreatePDFFromHTML(sResultRegisHTML, sPathRegisPDF, tempResCode);
            PrintFormFunction.AddBackgroundImageToPDF(sPathRegisPDF, sPathRegisPDF_temp, pathImage, pathImage, tempResCode);
            try (FileOutputStream fos = new FileOutputStream(sFileNameZip)) {
                ZipOutputStream zos = new ZipOutputStream(fos, java.nio.charset.StandardCharsets.UTF_8);
                CommonFunction.addToZipFile(sPathRegisPDF_temp, zos);
                CommonFunction.addToZipFile(sPathConfirmPDF_temp, zos);
                zos.close();
            }
        } else {
            sReturn[0] = 1;
        }
    }

    public static void CreatePDFFromHTML(String HTML, String sPDFOut, int[] resCode) {
        try {
            System.out.println("URL: " + sPDFOut);
            //String fileFont = "D:\\Project\\Hoa Don Dien Tu\\FPT HDDT\\CoreWs\\FPTEInvoiceData\\0312671405\\0306555016\\01GTKT0_004\\AC_12E/NotoNaskhArabic-Regular.ttf";
            Document document = new Document(PageSize.LETTER);
            PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(sPDFOut));
            document.open();
            //String str = ViewStringFile(HTML);
            // CSS - access style css
            CSSResolver cssResolver = XMLWorkerHelper.getInstance().getDefaultCssResolver(true);
            // HTML
            //CssAppliers cssAppliers = new CssAppliersImpl(null);
            // Styles
//            CSSResolver cssResolver = new StyleAttrCSSResolver();
//            XMLWorkerFontProvider fontProvider = new XMLWorkerFontProvider(XMLWorkerFontProvider.DONTLOOKFORFONTS);
//            fontProvider.register("D:\\Project\\Hoa Don Dien Tu\\FPT HDDT\\CoreWs\\FPTEInvoiceData\\0312671405\\0306555016\\01GTKT0_004\\AC_12E/NotoNaskhArabic-Regular.ttf");
//            CssAppliers cssAppliers = new CssAppliersImpl(fontProvider);
            //
            HtmlPipelineContext htmlContext = new HtmlPipelineContext(null);
            htmlContext.setTagFactory(Tags.getHtmlTagProcessorFactory());
            //CSSResolver cssResolver = XMLWorkerHelper.getInstance().getDefaultCssResolver(true);
            // Pipelines
            PdfWriterPipeline pdf = new PdfWriterPipeline(document, pdfWriter);
            HtmlPipeline html = new HtmlPipeline(htmlContext, pdf);
            CssResolverPipeline css = new CssResolverPipeline(cssResolver, html);
            // XML Worker
            XMLWorker worker = new XMLWorker(css, true);
            XMLParser p = new XMLParser(worker);
            p.parse(new ByteArrayInputStream(HTML.getBytes("UTF-8")));
            document.close();
            resCode[0] = 0;
        } catch (DocumentException e) {
            resCode[0] = 1001;
            //Log.errorLog("CreatePDFFromHTML", e.getMessage());
        } catch (IOException e) {
            resCode[0] = 1001;
            //Log.errorLog("CreatePDFFromHTML", e.getMessage());
        }
    }
}
