/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ra.process;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CSSResolver;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.PdfWriterPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.Arrays;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import vn.ra.object.REPORT_PER_MONTH;
import vn.ra.utility.Config;
import vn.ra.utility.EscapeUtils;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.List;
import javax.xml.bind.JAXBException;
import org.apache.commons.io.FileUtils;
import org.docx4j.convert.in.xhtml.XHTMLImporterImpl;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.AltChunkType;
import org.docx4j.openpackaging.parts.WordprocessingML.NumberingDefinitionsPart;
import vn.ra.object.REPORT_RECURRING_NEAC;
import vn.ra.utility.Definitions;

/**
 *
 * @author THANH-PC
 */
public class PrintFormFunction {

    Config conf = new Config();
    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(PrintFormFunction.class);
    private static final long serialVersionUID = 6106269076155338045L;

//    public static final String[] FONTS = {
//        "src/main/webapp/fonts/arial.ttf",
//        "src/main/webapp/fonts/arialbd.ttf",
//        "src/main/webapp/fonts/arialbi.ttf",
//        "src/main/webapp/fonts/ariali.ttf",
//        "src/main/webapp/fonts/ariblk.ttf",
//        "src/main/webapp/fonts/timesbi.ttf",
//        "src/main/webapp/fonts/times.ttf",
//        "src/main/webapp/fonts/timesbd.ttf",
//        "src/main/webapp/fonts/timesbi.ttf",
//        "src/main/webapp/fonts/timesi.ttf",};
//    public static final String[] FONTS = {
//        "D:\\Project\\TMS CA\\TMS EFY\\main" + "\\" + "times.ttf",
//        "D:\\Project\\TMS CA\\TMS EFY\\main" + "\\" + "timesbd.ttf",
//        "D:\\Project\\TMS CA\\TMS EFY\\main" + "\\" + "timesbi.ttf",
//        "D:\\Project\\TMS CA\\TMS EFY\\main" + "\\" + "timesi.ttf",};

    /**
     *
     * @param strHTML
     * @param pathPdf path save pdf
     * @param properties
     * @return true/false
     */
    public static boolean convertPdfFinal(String strHTML, String pathPdf, ConverterProperties properties) {
        try {
            PdfWriter writer = null;
            writer = new PdfWriter(pathPdf);
            PdfDocument pdf = new PdfDocument(writer);
            pdf.setTagged();
            PageSize pageSize = PageSize.A4.rotate();
            pdf.setDefaultPageSize(pageSize);
            HtmlConverter.convertToPdf(strHTML, writer, properties);
        } catch (IOException ex) {
            System.out.println(ex.toString());
            return false;
        }
        return true;
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
            CommonFunction.LogExceptionServlet(log, "PrintFormFunction: " + ex.getMessage(), ex);
        } finally {
            try {
                writer.close();
            } catch (IOException e) {
                intResult[0] = 1;
                CommonFunction.LogExceptionServlet(log, "PrintFormFunction: " + e.getMessage(), e);
            }

        }
        return response;
    }
    
    public static String createStringHtmlInStringExtend(String strXslt, String strData, byte[] img,
            boolean isImg, boolean isNeedBg, int[] intResult) {
        String sLogoTemp = "";
        String sBackgroundTemp = "";
        if(strXslt.contains(Definitions.CONFIG_TEMPLATE_PROCESS_LOGO_FROM_BASE64)){
            sLogoTemp = CommonFunction.subStringBetweenWord(strXslt, Definitions.CONFIG_TEMPLATE_PROCESS_LOGO_FROM_BASE64, Definitions.CONFIG_TEMPLATE_PROCESS_LOGO_END_BASE64);
            strXslt = strXslt.replace(Definitions.CONFIG_TEMPLATE_PROCESS_LOGO_FROM_BASE64
                +sLogoTemp+Definitions.CONFIG_TEMPLATE_PROCESS_LOGO_END_BASE64, "");
        }
        if(strXslt.contains(Definitions.CONFIG_TEMPLATE_PROCESS_BACKGROUND_FROM_BASE64)){
            sBackgroundTemp = CommonFunction.subStringBetweenWord(strXslt, Definitions.CONFIG_TEMPLATE_PROCESS_BACKGROUND_FROM_BASE64, Definitions.CONFIG_TEMPLATE_PROCESS_BACKGROUND_END_BASE64);
            strXslt = strXslt.replace(Definitions.CONFIG_TEMPLATE_PROCESS_BACKGROUND_FROM_BASE64
                +sBackgroundTemp+Definitions.CONFIG_TEMPLATE_PROCESS_BACKGROUND_END_BASE64, "");
        }
        System.out.println("strXslt: " + strXslt);
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
            System.out.println("html: " + html);
//            if(html.contains("@LOGO_TEMP@")) {
//                html = html.replace("@LOGO_TEMP@", sLogoTemp);
//            }
//            if(html.contains("@BACKGROUND_TEMP@")) {
//                html = html.replace("@BACKGROUND_TEMP@", sBackgroundTemp);
//            }
//            if (isImg) {
//                html = html.replaceAll("@imgBg", "data:image/jpeg;base64," + Arrays.toString(img));
//            }
//            if (!isNeedBg) {
//                html = html.replaceAll("@display", "none");
//            }
            response = html;
            intResult[0] = 0;
        } catch (TransformerException ex) {
            intResult[0] = 1;
            CommonFunction.LogExceptionServlet(log, "PrintFormFunction: " + ex.getMessage(), ex);
        } finally {
            try {
                writer.close();
            } catch (IOException e) {
                intResult[0] = 1;
                CommonFunction.LogExceptionServlet(log, "PrintFormFunction: " + e.getMessage(), e);
            }

        }
        return response;
    }

    public static void convertWord(String html, String pathWord, String pathHTML, String[] sCode) throws Docx4JException, JAXBException, UnsupportedEncodingException 
    {
        try {
//            createFile(pathHTML, html);
            File file = new File(pathWord);
            WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.createPackage();
            NumberingDefinitionsPart ndp = new NumberingDefinitionsPart();
            wordMLPackage.getMainDocumentPart().addTargetPart(ndp);
            ndp.unmarshalDefaultNumbering();
            // Convert the XHTML, and add it into the empty docx we made
            XHTMLImporterImpl XHTMLImporter = new XHTMLImporterImpl(wordMLPackage);
            XHTMLImporter.setHyperlinkStyle("Hyperlink");
//        String baseurl = file.getPath();
//        baseurl = baseurl.substring(0, baseurl.lastIndexOf("\\"));
//        wordMLPackage.getMainDocumentPart().getContent().addAll(XHTMLImporter.convert(html, baseurl));
            //Saving the Document
            wordMLPackage.getMainDocumentPart().addAltChunk(AltChunkType.Xhtml, html.getBytes("UTF-8"));
            wordMLPackage.save(file);
            sCode[0] = "0";
        } catch (JAXBException | Docx4JException ex) {
            sCode[0] = "1";
            log.error(ex.getMessage(), ex);
        }
    }
    
    public static void convertWordHasImage(String html, String pathWord, String pathHTML, String[] sCode)
        throws Docx4JException, JAXBException, UnsupportedEncodingException {
        try {
            System.out.println("convertWordHasImage");
            File file = new File(pathWord);
            WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.createPackage();
            NumberingDefinitionsPart ndp = new NumberingDefinitionsPart();
            wordMLPackage.getMainDocumentPart().addTargetPart(ndp);
            ndp.unmarshalDefaultNumbering();
            
//            XHTMLImporterImpl XHTMLImporter = new XHTMLImporterImpl(wordMLPackage);	
            html = html.replace("class=\"imgWatermark\"", "style=\"display:none;\" class=\"imgWatermark\"");
            System.out.println(html);
            XHTMLImporterImpl XHTMLImporter = new XHTMLImporterImpl(wordMLPackage);
            XHTMLImporter.setHyperlinkStyle("Hyperlink");
            List<Object> convert = XHTMLImporter.convert(html, null);
            wordMLPackage.getMainDocumentPart().getContent().addAll(convert);
//            wordMLPackage.getMainDocumentPart().addAltChunk(AltChunkType.Xhtml, html.getBytes("UTF-8"));
            wordMLPackage.save(file);
            sCode[0] = "0";
        } catch (JAXBException | Docx4JException ex) {
            sCode[0] = "1";
            log.error(ex.getMessage(), ex);
        }
    }

    public static void convertPdf(String html, String pathHTML, String pathPDF, String[] sCode) {
        PdfWriter writer = null;
//        try {
//            if (createFile(pathHTML, html)) {
//                writer = new PdfWriter(pathPDF);
//                PdfDocument pdf = new PdfDocument(writer);
//                pdf.setTagged();
//                PageSize pageSize = PageSize.A4.rotate();
//                pdf.setDefaultPageSize(pageSize);
//                ConverterProperties properties = new ConverterProperties();
//                MediaDeviceDescription mediaDeviceDescription
//                        = new MediaDeviceDescription(MediaType.SCREEN);
//                mediaDeviceDescription.setWidth(pageSize.getWidth());
//                properties.setMediaDeviceDescription(mediaDeviceDescription);
//                FontProvider fontProvider = new DefaultFontProvider(false, false, false);
//                for (String font : FONTS) {
//                    try {
//                        FontProgram fontProgram = FontProgramFactory.createFont(font);
//                        fontProvider.addFont(fontProgram);
//                    } catch (IOException ex) {
//                        log.error(ex.getMessage(), ex);
//                    }
//                }
//                properties.setFontProvider(fontProvider);
//                HtmlConverter.convertToPdf(new File(pathHTML), new File(pathPDF), properties);
//                sCode[0] = "0";
//            } else {
//                sCode[0] = "1";
//            }
////            HtmlConverter.convertToPdf(new File(pathHTML), new File(pathPDF), properties);
//        } catch (IOException ex) {
//            sCode[0] = "2";
//            log.error(ex.getMessage(), ex);
//        } finally {
//            try {
//                if (writer != null) {
//                    writer.close();
//                }
////                if (new File(pathHTML).exists()) {
////                    new File(pathHTML).delete();
////                }
//            } catch (IOException ex) {
//                log.error(ex.getMessage(), ex);
//            }
//        }
    }
    
    public static void AddBackgroundImageToPDF(String src, String destPdfFileName, String templateImageFile, String strImg2, int[] resCode)
            throws DocumentException, IOException {
        PdfReader reader = null;
        PdfStamper stamper = null;
        try {
            reader = new PdfReader(src);
            int n = reader.getNumberOfPages();
            stamper = new PdfStamper(reader, new FileOutputStream(destPdfFileName));
            // image watermark
            Image img = Image.getInstance(templateImageFile);
            float w = img.getScaledWidth();
            float h = img.getScaledHeight();
            img.setAbsolutePosition(150, 400);
            img.scaleToFit(360, 220);
            // transparency
            
            Image imgSign = Image.getInstance(strImg2);
            imgSign.setAbsolutePosition(100, 705);
            imgSign.scaleToFit(105, 105);
            
            // transparency 2
            PdfGState gs1 = new PdfGState();
            gs1.setFillOpacity(0.2f);
            
            PdfGState gs1Sign = new PdfGState();
            gs1Sign.setFillOpacity(0.6f);
            // properties
            PdfContentByte over;
            Rectangle pagesize;
            //float x, y;
            for (int i = 1; i <= n; i++) {
                pagesize = reader.getPageSizeWithRotation(i);
                //x = (pagesize.getLeft() + pagesize.getRight()) / 2;
                //y = (pagesize.getTop() + pagesize.getBottom()) / 2;
                over = stamper.getOverContent(i);
                over.saveState();
                over.setGState(gs1);
                over.addImage(img); //, w, 100, 200, h, x - (w / 2), y - (h / 2)
                over.restoreState();
            }
            
            for (int i = 1; i <= n; i++) {
                pagesize = reader.getPageSizeWithRotation(i);
                //x = (pagesize.getLeft() + pagesize.getRight()) / 2;
                //y = (pagesize.getTop() + pagesize.getBottom()) / 2;
                over = stamper.getOverContent(i);
                over.saveState();
                over.setGState(gs1Sign);
                over.addImage(imgSign); //, w, 100, 200, h, x - (w / 2), y - (h / 2)
                over.restoreState();
            }
            resCode[0] = 0;
        } catch (IOException e) {
            resCode[0] = 1;
        } catch (DocumentException e) {
            resCode[0] = 1;
        } finally {
            if (stamper != null) {
                stamper.close();
            }
            if (reader != null) {
                reader.close();
            }
            if (new File(src).exists()) {
                new File(src).delete();
            }
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
    
    public static void CreatePDFFromHTML(String strHTML, String sPathPDF, int[] resCode)
    {
        try {
//            System.out.println("A: " + strHTML);
            //String fileFont = "D:\\Project\\Hoa Don Dien Tu\\FPT HDDT\\CoreWs\\FPTEInvoiceData\\0312671405\\0306555016\\01GTKT0_004\\AC_12E/NotoNaskhArabic-Regular.ttf";
            com.itextpdf.text.Document document = new com.itextpdf.text.Document(com.itextpdf.text.PageSize.LETTER);
            com.itextpdf.text.pdf.PdfWriter pdfWriter = com.itextpdf.text.pdf.PdfWriter.getInstance(document, new FileOutputStream(sPathPDF));
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
//            XMLParser p = new XMLParser(worker);
//            p.parse(new ByteArrayInputStream(strHTML.getBytes("UTF-8")));
            
            final Charset charset = Charset.forName("UTF-8");
            final XMLParser xmlParser = new XMLParser(true, worker, charset);
            xmlParser.parse(new ByteArrayInputStream(strHTML.getBytes("UTF-8")), charset);
            
//            File file = new File(strHTML);
//            byte[] data = FileUtils.readFileToByteArray(file);
//            p.parse(new ByteArrayInputStream(data));
//            p.parse(new ByteArrayInputStream(strHTML.getBytes("UTF-8")));
            document.close();
            resCode[0] = 0;
        } catch (DocumentException | IOException e) {
            resCode[0] = 1;
            CommonFunction.LogExceptionServlet(log, "CreatePDFFromHTML: " + e.getMessage(), e);
        }
    }

    public static String createXMLRegistrationCN(String sName, String isCMND, String isHC, String So, String NgayCap,
            String NoiCap, String DiaChi, String DienThoai, String isCapMoi, String isGiaHan, String is1Nam,
            String is2Nam, String is3Nam, String isKhac, String NoiDungKhac, String ChucVu, String PhongBan, String ToChuc,
            String CaNhanThuocToChucDiaChi, String MST, String Email, String Mobile, String ThoiGianDiaDiem) {
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
                + "        <DiaChi>" + CaNhanThuocToChucDiaChi + "</DiaChi>\n"
                + "        <MST>" + MST + "</MST>\n"
                + "        <Email>" + Email + "</Email>\n"
                + "        <Mobile>" + Mobile + "</Mobile>\n"
                + "    </CaNhanThuocToChuc>\n"
                + "    <ThoiGianDiaDiem>\n"
                + "        " + ThoiGianDiaDiem + "\n"
                + "    </ThoiGianDiaDiem>\n"
                + "    <HoSoKemTheo>\n"
                + "        <CaNhan>\n"
                + "            <isGiayDKCapCTS>0</isGiayDKCapCTS>\n"
                + "            <isBanSaoCMNDHC>0</isBanSaoCMNDHC>\n"
                + "        </CaNhan>\n"
                + "        <ToChuc>\n"
                + "            <isGiayDKCapCTS1>0</isGiayDKCapCTS1>\n"
                + "            <isBanSaoGPKD>0</isBanSaoGPKD>\n"
                + "            <isBanSaoDKThue>0</isBanSaoDKThue>\n"
                + "            <UyQuyen>\n"
                + "                <isVanBanUyQuyen>0</isVanBanUyQuyen>\n"
                + "                <isBanSaoCMNDHC1>0</isBanSaoCMNDHC1>\n"
                + "            </UyQuyen>\n"
                + "        </ToChuc>\n"
                + "    </HoSoKemTheo>\n"
                + "</CCTSCN>";

        return sXML;
    }

    public static String createXMLRegistrationCNFinal(String sName, String isCMND, String isHC, String So, String NgayCap,
            String NoiCap, String DiaChi, String DienThoai, String isCapMoi, String isGiaHan, String is1Nam,
            String is2Nam, String is3Nam, String isKhac, String NoiDungKhac, String ChucVu, String PhongBan, String ToChuc,
            String CaNhanThuocToChucDiaChi, String MST, String Email, String Mobile, String ThoiGianDiaDiem, String IsCCCD)
        throws ParserConfigurationException, TransformerException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        // ROOT elements
        Document doc = docBuilder.newDocument();
        Element elmRoot = doc.createElement("CCTSCN");
        doc.appendChild(elmRoot);

        // ThongTinCaNhan
        Element thongTinCaNhanElm = doc.createElement("ThongTinCaNhan");
        elmRoot.appendChild(thongTinCaNhanElm);

        // TenKH elements
        Element hoTenElm = doc.createElement("HoTen");
        hoTenElm.appendChild(doc.createTextNode(sName));
        thongTinCaNhanElm.appendChild(hoTenElm);

        Element isCMNDElm = doc.createElement("isCMND");
        isCMNDElm.appendChild(doc.createTextNode(isCMND));
        thongTinCaNhanElm.appendChild(isCMNDElm);

        Element isHCElm = doc.createElement("isHC");
        isHCElm.appendChild(doc.createTextNode(isHC));
        thongTinCaNhanElm.appendChild(isHCElm);
        
        Element isCCCDElm = doc.createElement("isCCCD");
        isCCCDElm.appendChild(doc.createTextNode(IsCCCD));
        thongTinCaNhanElm.appendChild(isCCCDElm);

        Element soElm = doc.createElement("So");
        soElm.appendChild(doc.createTextNode(So));
        thongTinCaNhanElm.appendChild(soElm);

        Element ngayCapElm = doc.createElement("NgayCap");
        ngayCapElm.appendChild(doc.createTextNode(NgayCap));
        thongTinCaNhanElm.appendChild(ngayCapElm);

        Element noiCapElm = doc.createElement("NoiCap");
        noiCapElm.appendChild(doc.createTextNode(NoiCap));
        thongTinCaNhanElm.appendChild(noiCapElm);

        Element diaChiElm = doc.createElement("DiaChi");
        diaChiElm.appendChild(doc.createTextNode(DiaChi));
        thongTinCaNhanElm.appendChild(diaChiElm);

        Element dienThoaiElm = doc.createElement("DienThoai");
        dienThoaiElm.appendChild(doc.createTextNode(DienThoai));
        thongTinCaNhanElm.appendChild(dienThoaiElm);

        //GoiDangKy
        Element goiDangKyElm = doc.createElement("GoiDangKy");
        elmRoot.appendChild(goiDangKyElm);

        //DoiTuong
        Element doiTuongElm = doc.createElement("DoiTuong");
        goiDangKyElm.appendChild(doiTuongElm);

        //DoiTuong
        Element isCapMoiElm = doc.createElement("isCapMoi");
        isCapMoiElm.appendChild(doc.createTextNode(isCapMoi));
        doiTuongElm.appendChild(isCapMoiElm);

        //DoiTuong
        Element isGiaHanElm = doc.createElement("isGiaHan");
        isGiaHanElm.appendChild(doc.createTextNode(isGiaHan));
        doiTuongElm.appendChild(isGiaHanElm);

        //ThoiGianSuDung
        Element thoiGianSuDungElm = doc.createElement("ThoiGianSuDung");
        goiDangKyElm.appendChild(thoiGianSuDungElm);

        //DoiTuong
        Element is1NamElm = doc.createElement("is1Nam");
        is1NamElm.appendChild(doc.createTextNode(is1Nam));
        thoiGianSuDungElm.appendChild(is1NamElm);

        //DoiTuong
        Element is2NamElm = doc.createElement("is2Nam");
        is2NamElm.appendChild(doc.createTextNode(is2Nam));
        thoiGianSuDungElm.appendChild(is2NamElm);

        Element is3NamElm = doc.createElement("is3Nam");
        is3NamElm.appendChild(doc.createTextNode(is3Nam));
        thoiGianSuDungElm.appendChild(is3NamElm);

        Element isKhacElm = doc.createElement("isKhac");
        isKhacElm.appendChild(doc.createTextNode(isKhac));
        thoiGianSuDungElm.appendChild(isKhacElm);

        Element noiDungKhacElm = doc.createElement("NoiDungKhac");
        noiDungKhacElm.appendChild(doc.createTextNode(NoiDungKhac));
        thoiGianSuDungElm.appendChild(noiDungKhacElm);

        //CaNhanThuocToChuc
        Element caNhanThuocToChucElm = doc.createElement("CaNhanThuocToChuc");
        elmRoot.appendChild(caNhanThuocToChucElm);

        Element chucVuElm = doc.createElement("ChucVu");
        chucVuElm.appendChild(doc.createTextNode(ChucVu));
        caNhanThuocToChucElm.appendChild(chucVuElm);

        Element phongBanElm = doc.createElement("PhongBan");
        phongBanElm.appendChild(doc.createTextNode(PhongBan));
        caNhanThuocToChucElm.appendChild(phongBanElm);

        Element tenToChucElm = doc.createElement("ToChuc");
        tenToChucElm.appendChild(doc.createTextNode(ToChuc));
        caNhanThuocToChucElm.appendChild(tenToChucElm);

        Element diaChiTCElm = doc.createElement("DiaChi");
        diaChiTCElm.appendChild(doc.createTextNode(CaNhanThuocToChucDiaChi));
        caNhanThuocToChucElm.appendChild(diaChiTCElm);

        Element mstElm = doc.createElement("MST");
        mstElm.appendChild(doc.createTextNode(MST));
        caNhanThuocToChucElm.appendChild(mstElm);

        Element emailElm = doc.createElement("Email");
        emailElm.appendChild(doc.createTextNode(Email));
        caNhanThuocToChucElm.appendChild(emailElm);

        Element mobileElm = doc.createElement("Mobile");
        mobileElm.appendChild(doc.createTextNode(Mobile));
        caNhanThuocToChucElm.appendChild(mobileElm);

        //ThoiGianDiaDiem
        Element thoiGianDiaDiemElm = doc.createElement("ThoiGianDiaDiem");
        thoiGianDiaDiemElm.appendChild(doc.createTextNode(ThoiGianDiaDiem));
        elmRoot.appendChild(thoiGianDiaDiemElm);

        //HoSoKemTheo
        Element hoSoKemTheoElm = doc.createElement("HoSoKemTheo");
        elmRoot.appendChild(hoSoKemTheoElm);

        //CaNhan
        Element caNhanElm = doc.createElement("CaNhan");
        hoSoKemTheoElm.appendChild(caNhanElm);

        Element isGiayDKCapCTSElm = doc.createElement("isGiayDKCapCTS");
        isGiayDKCapCTSElm.appendChild(doc.createTextNode("0"));
        caNhanElm.appendChild(isGiayDKCapCTSElm);

        Element isBanSaoCMNDHCElm = doc.createElement("isBanSaoCMNDHC");
        isBanSaoCMNDHCElm.appendChild(doc.createTextNode("0"));
        caNhanElm.appendChild(isBanSaoCMNDHCElm);

        //ToChuc
        Element toChucElm = doc.createElement("ToChuc");
        hoSoKemTheoElm.appendChild(toChucElm);

        Element isGiayDKCapCTSTCElm = doc.createElement("isGiayDKCapCTS");
        isGiayDKCapCTSTCElm.appendChild(doc.createTextNode("0"));
        toChucElm.appendChild(isGiayDKCapCTSTCElm);

        Element isBanSaoGPKDElm = doc.createElement("isBanSaoGPKD");
        isBanSaoGPKDElm.appendChild(doc.createTextNode("0"));
        toChucElm.appendChild(isBanSaoGPKDElm);

        Element isBanSaoDKThueElm = doc.createElement("isBanSaoDKThue");
        isBanSaoDKThueElm.appendChild(doc.createTextNode("0"));
        toChucElm.appendChild(isBanSaoDKThueElm);

        //UyQuyen
        Element uyQuyenElm = doc.createElement("UyQuyen");
        toChucElm.appendChild(uyQuyenElm);

        Element isVanBanUyQuyenElm = doc.createElement("isVanBanUyQuyen");
        isVanBanUyQuyenElm.appendChild(doc.createTextNode("0"));
        uyQuyenElm.appendChild(isVanBanUyQuyenElm);

        Element isBanSaoCMNDHCTCElm = doc.createElement("isBanSaoCMNDHC");
        isBanSaoCMNDHCTCElm.appendChild(doc.createTextNode("0"));
        uyQuyenElm.appendChild(isBanSaoCMNDHCTCElm);

        // write the content into xml file
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);

        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        transformer.transform(source, result);
        String xml = writer.toString();
        return xml;
    }
    
    public static String createXMLRegistrationCNFinal2(String PRINT_FULLNAME, String PRINT_TAXCODE, String PRINT_ADDRESS_BILLING,
            String PRINT_PHONE, String PRINT_EMAIL, String is1Nam, String is2Nam, String is3Nam, String isKhac,
            String NoiDungKhac, String sNameReceive, String sAddressReceive, String sPhoneReceive,
            String sEmailReceive, String ThoiGianDiaDiem, String soNam, String is6Thang, String is4Nam, String GoiSanPhamDangKy,
            String isReceiveRegister, String isReceiveEnter)
            throws ParserConfigurationException, TransformerException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        // ROOT elements
        Document doc = docBuilder.newDocument();
        Element elmRoot = doc.createElement("CCTSCN");
        doc.appendChild(elmRoot);

        // ThongTinToChuc
        Element thongTinToChucElm = doc.createElement("ThongTinCaNhan");
        elmRoot.appendChild(thongTinToChucElm);

        // TenKH elements
        Element tenGiaoDichElm = doc.createElement("HoTen");
        tenGiaoDichElm.appendChild(doc.createTextNode(PRINT_FULLNAME));
        thongTinToChucElm.appendChild(tenGiaoDichElm);

        Element maSoElm = doc.createElement("So");
        maSoElm.appendChild(doc.createTextNode(PRINT_TAXCODE));
        thongTinToChucElm.appendChild(maSoElm);

        Element capNgayElm = doc.createElement("DiaChi");
        capNgayElm.appendChild(doc.createTextNode(PRINT_ADDRESS_BILLING));
        thongTinToChucElm.appendChild(capNgayElm);

        Element toChucCapElm = doc.createElement("DienThoai");
        toChucCapElm.appendChild(doc.createTextNode(PRINT_PHONE));
        thongTinToChucElm.appendChild(toChucCapElm);

        Element mstElm = doc.createElement("Email");
        mstElm.appendChild(doc.createTextNode(PRINT_EMAIL));
        thongTinToChucElm.appendChild(mstElm);
        
        Element mstGoiDK = doc.createElement("GoiSanPhamDangKy");
        mstGoiDK.appendChild(doc.createTextNode(GoiSanPhamDangKy));
        thongTinToChucElm.appendChild(mstGoiDK);

        //GoiDangKy
        Element goiDangKyElm = doc.createElement("GoiDangKy");
        elmRoot.appendChild(goiDangKyElm);

        //ThoiGianSuDung
        Element thoiGianSuDungElm = doc.createElement("ThoiGianSuDung");
        goiDangKyElm.appendChild(thoiGianSuDungElm);

        //DoiTuong
        Element is1NamElm = doc.createElement("is1Nam");
        is1NamElm.appendChild(doc.createTextNode(is1Nam));
        thoiGianSuDungElm.appendChild(is1NamElm);

        //DoiTuong
        Element is2NamElm = doc.createElement("is2Nam");
        is2NamElm.appendChild(doc.createTextNode(is2Nam));
        thoiGianSuDungElm.appendChild(is2NamElm);

        Element is3NamElm = doc.createElement("is3Nam");
        is3NamElm.appendChild(doc.createTextNode(is3Nam));
        thoiGianSuDungElm.appendChild(is3NamElm);
        
        Element is6ThangElm = doc.createElement("is6Thang");
        is6ThangElm.appendChild(doc.createTextNode(is6Thang));
        thoiGianSuDungElm.appendChild(is6ThangElm);
        
        Element is4NamElm = doc.createElement("is4Nam");
        is4NamElm.appendChild(doc.createTextNode(is4Nam));
        thoiGianSuDungElm.appendChild(is4NamElm);

        Element isKhacElm = doc.createElement("isKhac");
        isKhacElm.appendChild(doc.createTextNode(isKhac));
        thoiGianSuDungElm.appendChild(isKhacElm);

        Element noiDungKhacElm = doc.createElement("NoiDungKhac");
        noiDungKhacElm.appendChild(doc.createTextNode(NoiDungKhac));
        thoiGianSuDungElm.appendChild(noiDungKhacElm);

        //NguoiLienHe
        Element nguoiLienHeElm = doc.createElement("ThongTinNguoiNhan");
        elmRoot.appendChild(nguoiLienHeElm);

        Element hoTenNguoiLienHeElm = doc.createElement("TenNguoiNhan");
        hoTenNguoiLienHeElm.appendChild(doc.createTextNode(sNameReceive));
        nguoiLienHeElm.appendChild(hoTenNguoiLienHeElm);

        Element chucVuNguoiLienHeElm = doc.createElement("DiaChi");
        chucVuNguoiLienHeElm.appendChild(doc.createTextNode(sAddressReceive));
        nguoiLienHeElm.appendChild(chucVuNguoiLienHeElm);

        Element emailNguoiLienHeElm = doc.createElement("DienThoai");
        emailNguoiLienHeElm.appendChild(doc.createTextNode(sPhoneReceive));
        nguoiLienHeElm.appendChild(emailNguoiLienHeElm);

        Element mobileNguoiLienHeElm = doc.createElement("Email");
        mobileNguoiLienHeElm.appendChild(doc.createTextNode(sEmailReceive));
        nguoiLienHeElm.appendChild(mobileNguoiLienHeElm);
        
        Element eReceiveRegister = doc.createElement("isReceiveRegister");
        eReceiveRegister.appendChild(doc.createTextNode(isReceiveRegister));
        nguoiLienHeElm.appendChild(eReceiveRegister);
        
        Element eReceiveEnter = doc.createElement("isReceiveEnter");
        eReceiveEnter.appendChild(doc.createTextNode(isReceiveEnter));
        nguoiLienHeElm.appendChild(eReceiveEnter);

        //ThoiGianDiaDiem
        Element thoiGianDiaDiemElm = doc.createElement("ThoiGianDiaDiem");
        thoiGianDiaDiemElm.appendChild(doc.createTextNode(ThoiGianDiaDiem));
        elmRoot.appendChild(thoiGianDiaDiemElm);
        //soNam
        Element soNamElm = doc.createElement("SoNam");
        soNamElm.appendChild(doc.createTextNode(soNam));
        elmRoot.appendChild(soNamElm);

        // write the content into xml file
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        transformer.transform(source, result);
        String xml = writer.toString();
        return xml;
    }
    
    public static String createXMLRegistrationCNDNFinal2(String PRINT_FULLNAME, String PRINT_TAXCODE, String PRINT_ADDRESS_BILLING,
            String PRINT_PHONE, String PRINT_EMAIL, String is1Nam, String is2Nam, String is3Nam, String isKhac,
            String NoiDungKhac, String sNameReceive, String sAddressReceive, String sPhoneReceive,
            String sEmailReceive, String ThoiGianDiaDiem, String soNam, String is6Thang, String is4Nam, String GoiSanPhamDangKy,
            String isReceiveRegister, String isReceiveEnter, String sDonViToChuc, String sMST)
            throws ParserConfigurationException, TransformerException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        // ROOT elements
        Document doc = docBuilder.newDocument();
        Element elmRoot = doc.createElement("CCTSCN");
        doc.appendChild(elmRoot);

        // ThongTinToChuc
        Element thongTinToChucElm = doc.createElement("ThongTinCaNhan");
        elmRoot.appendChild(thongTinToChucElm);

        // TenKH elements
        Element tenGiaoDichElm = doc.createElement("HoTen");
        tenGiaoDichElm.appendChild(doc.createTextNode(PRINT_FULLNAME));
        thongTinToChucElm.appendChild(tenGiaoDichElm);

        Element maSoElm = doc.createElement("So");
        maSoElm.appendChild(doc.createTextNode(PRINT_TAXCODE));
        thongTinToChucElm.appendChild(maSoElm);

        Element capNgayElm = doc.createElement("DiaChi");
        capNgayElm.appendChild(doc.createTextNode(PRINT_ADDRESS_BILLING));
        thongTinToChucElm.appendChild(capNgayElm);

        Element toChucCapElm = doc.createElement("DienThoai");
        toChucCapElm.appendChild(doc.createTextNode(PRINT_PHONE));
        thongTinToChucElm.appendChild(toChucCapElm);

        Element mstElm = doc.createElement("Email");
        mstElm.appendChild(doc.createTextNode(PRINT_EMAIL));
        thongTinToChucElm.appendChild(mstElm);

        Element eDonViToChuc = doc.createElement("DonViToChuc");
        eDonViToChuc.appendChild(doc.createTextNode(sDonViToChuc));
        thongTinToChucElm.appendChild(eDonViToChuc);

        Element eMST = doc.createElement("MST");
        eMST.appendChild(doc.createTextNode(sMST));
        thongTinToChucElm.appendChild(eMST);
        
        Element mstGoiDK = doc.createElement("GoiSanPhamDangKy");
        mstGoiDK.appendChild(doc.createTextNode(GoiSanPhamDangKy));
        thongTinToChucElm.appendChild(mstGoiDK);

        //GoiDangKy
        Element goiDangKyElm = doc.createElement("GoiDangKy");
        elmRoot.appendChild(goiDangKyElm);

        //ThoiGianSuDung
        Element thoiGianSuDungElm = doc.createElement("ThoiGianSuDung");
        goiDangKyElm.appendChild(thoiGianSuDungElm);

        //DoiTuong
        Element is1NamElm = doc.createElement("is1Nam");
        is1NamElm.appendChild(doc.createTextNode(is1Nam));
        thoiGianSuDungElm.appendChild(is1NamElm);

        //DoiTuong
        Element is2NamElm = doc.createElement("is2Nam");
        is2NamElm.appendChild(doc.createTextNode(is2Nam));
        thoiGianSuDungElm.appendChild(is2NamElm);

        Element is3NamElm = doc.createElement("is3Nam");
        is3NamElm.appendChild(doc.createTextNode(is3Nam));
        thoiGianSuDungElm.appendChild(is3NamElm);
        
        Element is6ThangElm = doc.createElement("is6Thang");
        is6ThangElm.appendChild(doc.createTextNode(is6Thang));
        thoiGianSuDungElm.appendChild(is6ThangElm);
        
        Element is4NamElm = doc.createElement("is4Nam");
        is4NamElm.appendChild(doc.createTextNode(is4Nam));
        thoiGianSuDungElm.appendChild(is4NamElm);

        Element isKhacElm = doc.createElement("isKhac");
        isKhacElm.appendChild(doc.createTextNode(isKhac));
        thoiGianSuDungElm.appendChild(isKhacElm);

        Element noiDungKhacElm = doc.createElement("NoiDungKhac");
        noiDungKhacElm.appendChild(doc.createTextNode(NoiDungKhac));
        thoiGianSuDungElm.appendChild(noiDungKhacElm);

        //NguoiLienHe
        Element nguoiLienHeElm = doc.createElement("ThongTinNguoiNhan");
        elmRoot.appendChild(nguoiLienHeElm);

        Element hoTenNguoiLienHeElm = doc.createElement("TenNguoiNhan");
        hoTenNguoiLienHeElm.appendChild(doc.createTextNode(sNameReceive));
        nguoiLienHeElm.appendChild(hoTenNguoiLienHeElm);

        Element chucVuNguoiLienHeElm = doc.createElement("DiaChi");
        chucVuNguoiLienHeElm.appendChild(doc.createTextNode(sAddressReceive));
        nguoiLienHeElm.appendChild(chucVuNguoiLienHeElm);

        Element emailNguoiLienHeElm = doc.createElement("DienThoai");
        emailNguoiLienHeElm.appendChild(doc.createTextNode(sPhoneReceive));
        nguoiLienHeElm.appendChild(emailNguoiLienHeElm);

        Element mobileNguoiLienHeElm = doc.createElement("Email");
        mobileNguoiLienHeElm.appendChild(doc.createTextNode(sEmailReceive));
        nguoiLienHeElm.appendChild(mobileNguoiLienHeElm);
        
        Element eReceiveRegister = doc.createElement("isReceiveRegister");
        eReceiveRegister.appendChild(doc.createTextNode(isReceiveRegister));
        nguoiLienHeElm.appendChild(eReceiveRegister);
        
        Element eReceiveEnter = doc.createElement("isReceiveEnter");
        eReceiveEnter.appendChild(doc.createTextNode(isReceiveEnter));
        nguoiLienHeElm.appendChild(eReceiveEnter);

        //ThoiGianDiaDiem
        Element thoiGianDiaDiemElm = doc.createElement("ThoiGianDiaDiem");
        thoiGianDiaDiemElm.appendChild(doc.createTextNode(ThoiGianDiaDiem));
        elmRoot.appendChild(thoiGianDiaDiemElm);
        //soNam
        Element soNamElm = doc.createElement("SoNam");
        soNamElm.appendChild(doc.createTextNode(soNam));
        elmRoot.appendChild(soNamElm);

        // write the content into xml file
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        transformer.transform(source, result);
        String xml = writer.toString();
        return xml;
    }

    public static String createXMLRegistrationDN(String sName, String isCMND, String isHC, String So, String NgayCap,
            String NoiCap, String DiaChi, String DienThoai, String isCapMoi, String isGiaHan, String is1Nam,
            String is2Nam, String is3Nam, String isKhac, String NoiDungKhac, String NguoiLienHeHoTen,
            String NguoiLienHeChucVu, String NguoiLienHeEmail, String NguoiLienHeMobile, String ThoiGianDiaDiem,
            String isGiayDKCapCTS, String isBanSaoCMNDHC, String isGiayDKCapCTS1, String isBanSaoGPKD, String isBanSaoDKThue,
            String isVanBanUyQuyen, String isBanSaoCMNDHC1) {
        String sXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<CCTSDN>\n"
                + "    <ThongTinToChuc>\n"
                + "        <TenGiaoDich>" + sName + "</TenGiaoDich>\n"
                + "        <TrucThuoc>" + isCMND + "</TrucThuoc>\n"
                + "        <isGiayChungNhanDKKD>0</isGiayChungNhanDKKD>\n"
                + "        <isGiayPhepDauTu>0</isGiayPhepDauTu>\n"
                + "        <GiayQuetDinhThanhLap>0</GiayQuetDinhThanhLap>\n"
                + "        <NoiCap>" + NoiCap + "</NoiCap>\n"
                + "        <DiaChi>" + DiaChi + "</DiaChi>\n"
                + "        <DienThoai>" + DienThoai + "</DienThoai>\n"
                + "    </ThongTinToChuc>\n"
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
                + "    <NguoiLienHe>\n"
                + "        <HoTen>" + NguoiLienHeHoTen + "</HoTen>\n"
                + "        <ChucVu>" + NguoiLienHeChucVu + "</ChucVu>\n"
                + "        <Email>" + NguoiLienHeEmail + "</Email>\n"
                + "        <Mobile>" + NguoiLienHeMobile + "</Mobile>\n"
                + "    </NguoiLienHe>\n"
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
                + "</CCTSDN>";

        return sXML;
    }

    public static String createXMLRegistrationDNFinal(String sName, String TrucThuoc, String isGiayChungNhanDKKD,
            String isGiayPhepDauTu, String isGiayQuetDinhThanhLap, String maSo, String capNgay, String toChucCap,
            String mst, String diaChi, String email, String dienThoaiFax, String hoTenNguoiDaiDien,
            String chucVuNguoiDaiDien, String cmndHCNguoiDaiDien, String isCapMoi, String isGiaHan,
            String is1Nam, String is2Nam, String is3Nam, String isKhac, String NoiDungKhac, String hoTenNguoiLienHe,
            String chucVuNguoiLienHe, String emailNguoiLienHe, String mobileNguoiLienHe, String ThoiGianDiaDiem)
            throws ParserConfigurationException, TransformerException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        // ROOT elements
        Document doc = docBuilder.newDocument();
        Element elmRoot = doc.createElement("CCTSDN");
        doc.appendChild(elmRoot);

        // ThongTinToChuc
        Element thongTinToChucElm = doc.createElement("ThongTinToChuc");
        elmRoot.appendChild(thongTinToChucElm);

        // TenKH elements
        Element tenGiaoDichElm = doc.createElement("TenGiaoDich");
        tenGiaoDichElm.appendChild(doc.createTextNode(sName));
        thongTinToChucElm.appendChild(tenGiaoDichElm);

        Element trucThuocElm = doc.createElement("TrucThuoc");
        trucThuocElm.appendChild(doc.createTextNode(TrucThuoc));
        thongTinToChucElm.appendChild(trucThuocElm);

        Element isGiayChungNhanDKKDElm = doc.createElement("isGiayChungNhanDKKD");
        isGiayChungNhanDKKDElm.appendChild(doc.createTextNode(isGiayChungNhanDKKD));
        thongTinToChucElm.appendChild(isGiayChungNhanDKKDElm);

        Element isGiayPhepDauTuElm = doc.createElement("isGiayPhepDauTu");
        isGiayPhepDauTuElm.appendChild(doc.createTextNode(isGiayPhepDauTu));
        thongTinToChucElm.appendChild(isGiayPhepDauTuElm);

        Element isGiayQuetDinhThanhLapElm = doc.createElement("GiayQuetDinhThanhLap");
        isGiayQuetDinhThanhLapElm.appendChild(doc.createTextNode(isGiayQuetDinhThanhLap));
        thongTinToChucElm.appendChild(isGiayQuetDinhThanhLapElm);

        Element maSoElm = doc.createElement("MaSo");
        maSoElm.appendChild(doc.createTextNode(maSo));
        thongTinToChucElm.appendChild(maSoElm);

        Element capNgayElm = doc.createElement("CapNgay");
        capNgayElm.appendChild(doc.createTextNode(capNgay));
        thongTinToChucElm.appendChild(capNgayElm);

        Element toChucCapElm = doc.createElement("ToChucCap");
        toChucCapElm.appendChild(doc.createTextNode(toChucCap));
        thongTinToChucElm.appendChild(toChucCapElm);

        Element mstElm = doc.createElement("MST");
        mstElm.appendChild(doc.createTextNode(mst));
        thongTinToChucElm.appendChild(mstElm);

        Element diaChiElm = doc.createElement("DiaChi");
        diaChiElm.appendChild(doc.createTextNode(diaChi));
        thongTinToChucElm.appendChild(diaChiElm);

        Element emailElm = doc.createElement("Email");
        emailElm.appendChild(doc.createTextNode(email));
        thongTinToChucElm.appendChild(emailElm);

        Element dienThoaiFaxElm = doc.createElement("DienThoaiFax");
        dienThoaiFaxElm.appendChild(doc.createTextNode(dienThoaiFax));
        thongTinToChucElm.appendChild(dienThoaiFaxElm);

        Element nguoiDaiDienElm = doc.createElement("NguoiDaiDien");
        thongTinToChucElm.appendChild(nguoiDaiDienElm);

        Element hoTenNguoiDaiDienElm = doc.createElement("HoTen");
        hoTenNguoiDaiDienElm.appendChild(doc.createTextNode(hoTenNguoiDaiDien));
        nguoiDaiDienElm.appendChild(hoTenNguoiDaiDienElm);

        Element chucVuNguoiDaiDienElm = doc.createElement("ChucVu");
        chucVuNguoiDaiDienElm.appendChild(doc.createTextNode(chucVuNguoiDaiDien));
        nguoiDaiDienElm.appendChild(chucVuNguoiDaiDienElm);

        Element cmndHCNguoiDaiDienElm = doc.createElement("CMNDHC");
        cmndHCNguoiDaiDienElm.appendChild(doc.createTextNode(cmndHCNguoiDaiDien));
        nguoiDaiDienElm.appendChild(cmndHCNguoiDaiDienElm);

        //GoiDangKy
        Element goiDangKyElm = doc.createElement("GoiDangKy");
        elmRoot.appendChild(goiDangKyElm);

        //DoiTuong
        Element doiTuongElm = doc.createElement("DoiTuong");
        goiDangKyElm.appendChild(doiTuongElm);

        //DoiTuong
        Element isCapMoiElm = doc.createElement("isCapMoi");
        isCapMoiElm.appendChild(doc.createTextNode(isCapMoi));
        doiTuongElm.appendChild(isCapMoiElm);

        //DoiTuong
        Element isGiaHanElm = doc.createElement("isGiaHan");
        isGiaHanElm.appendChild(doc.createTextNode(isGiaHan));
        doiTuongElm.appendChild(isGiaHanElm);

        //ThoiGianSuDung
        Element thoiGianSuDungElm = doc.createElement("ThoiGianSuDung");
        goiDangKyElm.appendChild(thoiGianSuDungElm);

        //DoiTuong
        Element is1NamElm = doc.createElement("is1Nam");
        is1NamElm.appendChild(doc.createTextNode(is1Nam));
        thoiGianSuDungElm.appendChild(is1NamElm);

        //DoiTuong
        Element is2NamElm = doc.createElement("is2Nam");
        is2NamElm.appendChild(doc.createTextNode(is2Nam));
        thoiGianSuDungElm.appendChild(is2NamElm);

        Element is3NamElm = doc.createElement("is3Nam");
        is3NamElm.appendChild(doc.createTextNode(is3Nam));
        thoiGianSuDungElm.appendChild(is3NamElm);

        Element isKhacElm = doc.createElement("isKhac");
        isKhacElm.appendChild(doc.createTextNode(isKhac));
        thoiGianSuDungElm.appendChild(isKhacElm);

        Element noiDungKhacElm = doc.createElement("NoiDungKhac");
        noiDungKhacElm.appendChild(doc.createTextNode(NoiDungKhac));
        thoiGianSuDungElm.appendChild(noiDungKhacElm);

        //NguoiLienHe
        Element nguoiLienHeElm = doc.createElement("NguoiLienHe");
        elmRoot.appendChild(nguoiLienHeElm);

        Element hoTenNguoiLienHeElm = doc.createElement("HoTen");
        hoTenNguoiLienHeElm.appendChild(doc.createTextNode(hoTenNguoiLienHe));
        nguoiLienHeElm.appendChild(hoTenNguoiLienHeElm);

        Element chucVuNguoiLienHeElm = doc.createElement("ChucVu");
        chucVuNguoiLienHeElm.appendChild(doc.createTextNode(chucVuNguoiLienHe));
        nguoiLienHeElm.appendChild(chucVuNguoiLienHeElm);

        Element emailNguoiLienHeElm = doc.createElement("Email");
        emailNguoiLienHeElm.appendChild(doc.createTextNode(emailNguoiLienHe));
        nguoiLienHeElm.appendChild(emailNguoiLienHeElm);

        Element mobileNguoiLienHeElm = doc.createElement("Mobile");
        mobileNguoiLienHeElm.appendChild(doc.createTextNode(mobileNguoiLienHe));
        nguoiLienHeElm.appendChild(mobileNguoiLienHeElm);

        //ThoiGianDiaDiem
        Element thoiGianDiaDiemElm = doc.createElement("ThoiGianDiaDiem");
        thoiGianDiaDiemElm.appendChild(doc.createTextNode(ThoiGianDiaDiem));
        elmRoot.appendChild(thoiGianDiaDiemElm);

        //HoSoKemTheo
        Element hoSoKemTheoElm = doc.createElement("HoSoKemTheo");
        elmRoot.appendChild(hoSoKemTheoElm);

        //CaNhan
        Element caNhanElm = doc.createElement("CaNhan");
        hoSoKemTheoElm.appendChild(caNhanElm);

        Element isGiayDKCapCTSElm = doc.createElement("isGiayDKCapCTS");
        isGiayDKCapCTSElm.appendChild(doc.createTextNode("0"));
        caNhanElm.appendChild(isGiayDKCapCTSElm);

        Element isBanSaoCMNDHCElm = doc.createElement("isBanSaoCMNDHC");
        isBanSaoCMNDHCElm.appendChild(doc.createTextNode("0"));
        caNhanElm.appendChild(isBanSaoCMNDHCElm);

        //ToChuc
        Element toChucElm = doc.createElement("ToChuc");
        hoSoKemTheoElm.appendChild(toChucElm);

        Element isGiayDKCapCTSTCElm = doc.createElement("isGiayDKCapCTS");
        isGiayDKCapCTSTCElm.appendChild(doc.createTextNode("0"));
        toChucElm.appendChild(isGiayDKCapCTSTCElm);

        Element isBanSaoGPKDElm = doc.createElement("isBanSaoGPKD");
        isBanSaoGPKDElm.appendChild(doc.createTextNode("0"));
        toChucElm.appendChild(isBanSaoGPKDElm);

        Element isBanSaoDKThueElm = doc.createElement("isBanSaoDKThue");
        isBanSaoDKThueElm.appendChild(doc.createTextNode("0"));
        toChucElm.appendChild(isBanSaoDKThueElm);

        //UyQuyen
        Element uyQuyenElm = doc.createElement("UyQuyen");
        toChucElm.appendChild(uyQuyenElm);

        Element isVanBanUyQuyenElm = doc.createElement("isVanBanUyQuyen");
        isVanBanUyQuyenElm.appendChild(doc.createTextNode("0"));
        uyQuyenElm.appendChild(isVanBanUyQuyenElm);

        Element isBanSaoCMNDHCTCElm = doc.createElement("isBanSaoCMNDHC");
        isBanSaoCMNDHCTCElm.appendChild(doc.createTextNode("0"));
        uyQuyenElm.appendChild(isBanSaoCMNDHCTCElm);

        // write the content into xml file
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        transformer.transform(source, result);
        String xml = writer.toString();
        return xml;
    }
    
    public static String createXMLChange14DNFinal(String sName, String TrucThuoc, String isGiayChungNhanDKKD,
            String isGiayPhepDauTu, String isGiayQuetDinhThanhLap, String maSo, String capNgay, String toChucCap,
            String mst, String diaChi, String hoTenNguoiDaiDien, String chucVuNguoiDaiDien,
            String cmndHCNguoiDaiDien, String ThoiGianDiaDiem, String emailNguoiLienHe, String mobileNguoiLienHe,
            String isChangeInfo, String isSuspend, String isRecovery, String isRevoke, String snSuspend,
            String snRecovery, String snRevoke, String subjectDNNew, String subjectDNOld)
            throws ParserConfigurationException, TransformerException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        // ROOT elements
        Document doc = docBuilder.newDocument();
        Element elmRoot = doc.createElement("CCTSDN");
        doc.appendChild(elmRoot);

        // ThongTinToChuc
        Element thongTinToChucElm = doc.createElement("ThongTinToChuc");
        elmRoot.appendChild(thongTinToChucElm);

        // TenKH elements
        Element tenGiaoDichElm = doc.createElement("TenGiaoDich");
        tenGiaoDichElm.appendChild(doc.createTextNode(sName));
        thongTinToChucElm.appendChild(tenGiaoDichElm);

        Element trucThuocElm = doc.createElement("TrucThuoc");
        trucThuocElm.appendChild(doc.createTextNode(TrucThuoc));
        thongTinToChucElm.appendChild(trucThuocElm);

        Element isGiayChungNhanDKKDElm = doc.createElement("isGiayChungNhanDKKD");
        isGiayChungNhanDKKDElm.appendChild(doc.createTextNode(isGiayChungNhanDKKD));
        thongTinToChucElm.appendChild(isGiayChungNhanDKKDElm);

        Element isGiayPhepDauTuElm = doc.createElement("isGiayPhepDauTu");
        isGiayPhepDauTuElm.appendChild(doc.createTextNode(isGiayPhepDauTu));
        thongTinToChucElm.appendChild(isGiayPhepDauTuElm);

        Element isGiayQuetDinhThanhLapElm = doc.createElement("GiayQuetDinhThanhLap");
        isGiayQuetDinhThanhLapElm.appendChild(doc.createTextNode(isGiayQuetDinhThanhLap));
        thongTinToChucElm.appendChild(isGiayQuetDinhThanhLapElm);

        Element maSoElm = doc.createElement("MaSo");
        maSoElm.appendChild(doc.createTextNode(maSo));
        thongTinToChucElm.appendChild(maSoElm);

        Element capNgayElm = doc.createElement("CapNgay");
        capNgayElm.appendChild(doc.createTextNode(capNgay));
        thongTinToChucElm.appendChild(capNgayElm);

        Element toChucCapElm = doc.createElement("ToChucCap");
        toChucCapElm.appendChild(doc.createTextNode(toChucCap));
        thongTinToChucElm.appendChild(toChucCapElm);

        Element mstElm = doc.createElement("MST");
        mstElm.appendChild(doc.createTextNode(mst));
        thongTinToChucElm.appendChild(mstElm);

        Element diaChiElm = doc.createElement("DiaChi");
        diaChiElm.appendChild(doc.createTextNode(diaChi));
        thongTinToChucElm.appendChild(diaChiElm);

        Element nguoiDaiDienElm = doc.createElement("NguoiDaiDien");
        thongTinToChucElm.appendChild(nguoiDaiDienElm);

        Element hoTenNguoiDaiDienElm = doc.createElement("HoTen");
        hoTenNguoiDaiDienElm.appendChild(doc.createTextNode(hoTenNguoiDaiDien));
        nguoiDaiDienElm.appendChild(hoTenNguoiDaiDienElm);

        Element chucVuNguoiDaiDienElm = doc.createElement("ChucVu");
        chucVuNguoiDaiDienElm.appendChild(doc.createTextNode(chucVuNguoiDaiDien));
        nguoiDaiDienElm.appendChild(chucVuNguoiDaiDienElm);

        Element cmndHCNguoiDaiDienElm = doc.createElement("CMNDHC");
        cmndHCNguoiDaiDienElm.appendChild(doc.createTextNode(cmndHCNguoiDaiDien));
        nguoiDaiDienElm.appendChild(cmndHCNguoiDaiDienElm);

        //NguoiLienHe
        Element nguoiLienHeElm = doc.createElement("NguoiLienHe");
        elmRoot.appendChild(nguoiLienHeElm);

        Element emailNguoiLienHeElm = doc.createElement("Email");
        emailNguoiLienHeElm.appendChild(doc.createTextNode(emailNguoiLienHe));
        nguoiLienHeElm.appendChild(emailNguoiLienHeElm);

        Element mobileNguoiLienHeElm = doc.createElement("Mobile");
        mobileNguoiLienHeElm.appendChild(doc.createTextNode(mobileNguoiLienHe));
        nguoiLienHeElm.appendChild(mobileNguoiLienHeElm);
        
        //ThongTinThayDoi
        Element thongTinThayDoiElm = doc.createElement("ThongTinThayDoi");
        elmRoot.appendChild(thongTinThayDoiElm);

        Element isChangeInfoElm = doc.createElement("isChangeInfo");
        isChangeInfoElm.appendChild(doc.createTextNode(isChangeInfo));
        thongTinThayDoiElm.appendChild(isChangeInfoElm);

        Element isSuspendElm = doc.createElement("isSuspend");
        isSuspendElm.appendChild(doc.createTextNode(isSuspend));
        thongTinThayDoiElm.appendChild(isSuspendElm);

        Element isRecoveryElm = doc.createElement("isRecovery");
        isRecoveryElm.appendChild(doc.createTextNode(isRecovery));
        thongTinThayDoiElm.appendChild(isRecoveryElm);

        Element isRevokeElm = doc.createElement("isRevoke");
        isRevokeElm.appendChild(doc.createTextNode(isRevoke));
        thongTinThayDoiElm.appendChild(isRevokeElm);
        
        Element snRevokeElm = doc.createElement("SerialNumberRevoke");
        snRevokeElm.appendChild(doc.createTextNode(snRevoke));
        thongTinThayDoiElm.appendChild(snRevokeElm);
        
        Element snSuspendElm = doc.createElement("SerialNumberSuspend");
        snSuspendElm.appendChild(doc.createTextNode(snSuspend));
        thongTinThayDoiElm.appendChild(snSuspendElm);
        
        Element snRecoveryElm = doc.createElement("SerialNumberRecovery");
        snRecoveryElm.appendChild(doc.createTextNode(snRecovery));
        thongTinThayDoiElm.appendChild(snRecoveryElm);
        
        Element subjectOldElm = doc.createElement("ThongTinDangKyCu");
        subjectOldElm.appendChild(doc.createTextNode(subjectDNOld));
        thongTinThayDoiElm.appendChild(subjectOldElm);
        
        Element subjectNewElm = doc.createElement("ThongTinDangKyMoi");
        subjectNewElm.appendChild(doc.createTextNode(subjectDNNew));
        thongTinThayDoiElm.appendChild(subjectNewElm);

        //ThoiGianDiaDiem
        Element thoiGianDiaDiemElm = doc.createElement("ThoiGianDiaDiem");
        thoiGianDiaDiemElm.appendChild(doc.createTextNode(ThoiGianDiaDiem));
        elmRoot.appendChild(thoiGianDiaDiemElm);
        
        // write the content into xml file
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        transformer.transform(source, result);
        String xml = writer.toString();
        return xml;
    }
    
    public static String createXMLRegistrationDNFinal2(String PRINT_FULLNAME, String PRINT_TAXCODE, String PRINT_ADDRESS_BILLING,
            String PRINT_PHONE, String PRINT_EMAIL, String PRINT_REPRESENTATIVE, String PRINT_ROLE, String is1Nam,
            String is2Nam, String is3Nam, String isKhac, String NoiDungKhac, String sNameReceive, String sAddressReceive, String sPhoneReceive,
            String sEmailReceive, String ThoiGianDiaDiem, String soNam, String is6Thang, String is4Nam,
            String GoiSanPhamDangKy, String isReceiveRegister, String isReceiveEnter)
            throws ParserConfigurationException, TransformerException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        // ROOT elements
        Document doc = docBuilder.newDocument();
        Element elmRoot = doc.createElement("CCTSDN");
        doc.appendChild(elmRoot);

        // ThongTinToChuc
        Element thongTinToChucElm = doc.createElement("ThongTinToChuc");
        elmRoot.appendChild(thongTinToChucElm);

        // TenKH elements
        Element tenGiaoDichElm = doc.createElement("TenGiaoDich");
        tenGiaoDichElm.appendChild(doc.createTextNode(PRINT_FULLNAME));
        thongTinToChucElm.appendChild(tenGiaoDichElm);

        Element maSoElm = doc.createElement("MST");
        maSoElm.appendChild(doc.createTextNode(PRINT_TAXCODE));
        thongTinToChucElm.appendChild(maSoElm);

        Element capNgayElm = doc.createElement("DiaChi");
        capNgayElm.appendChild(doc.createTextNode(PRINT_ADDRESS_BILLING));
        thongTinToChucElm.appendChild(capNgayElm);

        Element toChucCapElm = doc.createElement("DienThoaiFax");
        toChucCapElm.appendChild(doc.createTextNode(PRINT_PHONE));
        thongTinToChucElm.appendChild(toChucCapElm);

        Element mstElm = doc.createElement("Email");
        mstElm.appendChild(doc.createTextNode(PRINT_EMAIL));
        thongTinToChucElm.appendChild(mstElm);

        Element msNguoiDaiDienElm = doc.createElement("NguoiDaiDien");
        msNguoiDaiDienElm.appendChild(doc.createTextNode(PRINT_REPRESENTATIVE));
        thongTinToChucElm.appendChild(msNguoiDaiDienElm);

        Element mstChucVuElm = doc.createElement("ChucVu");
        mstChucVuElm.appendChild(doc.createTextNode(PRINT_ROLE));
        thongTinToChucElm.appendChild(mstChucVuElm);
        
        Element goiDichVuElm = doc.createElement("GoiSanPhamDangKy");
        goiDichVuElm.appendChild(doc.createTextNode(GoiSanPhamDangKy));
        thongTinToChucElm.appendChild(goiDichVuElm);

        //GoiDangKy
        Element goiDangKyElm = doc.createElement("GoiDangKy");
        elmRoot.appendChild(goiDangKyElm);

        //ThoiGianSuDung
        Element thoiGianSuDungElm = doc.createElement("ThoiGianSuDung");
        goiDangKyElm.appendChild(thoiGianSuDungElm);

        //DoiTuong
        Element is1NamElm = doc.createElement("is1Nam");
        is1NamElm.appendChild(doc.createTextNode(is1Nam));
        thoiGianSuDungElm.appendChild(is1NamElm);

        //DoiTuong
        Element is2NamElm = doc.createElement("is2Nam");
        is2NamElm.appendChild(doc.createTextNode(is2Nam));
        thoiGianSuDungElm.appendChild(is2NamElm);

        Element is3NamElm = doc.createElement("is3Nam");
        is3NamElm.appendChild(doc.createTextNode(is3Nam));
        thoiGianSuDungElm.appendChild(is3NamElm);

        Element isKhacElm = doc.createElement("isKhac");
        isKhacElm.appendChild(doc.createTextNode(isKhac));
        thoiGianSuDungElm.appendChild(isKhacElm);
        
        Element is6ThangElm = doc.createElement("is6Thang");
        is6ThangElm.appendChild(doc.createTextNode(is6Thang));
        thoiGianSuDungElm.appendChild(is6ThangElm);
        
        Element is4NamElm = doc.createElement("is4Nam");
        is4NamElm.appendChild(doc.createTextNode(is4Nam));
        thoiGianSuDungElm.appendChild(is4NamElm);

        Element noiDungKhacElm = doc.createElement("NoiDungKhac");
        noiDungKhacElm.appendChild(doc.createTextNode(NoiDungKhac));
        thoiGianSuDungElm.appendChild(noiDungKhacElm);

        //NguoiLienHe
        Element nguoiLienHeElm = doc.createElement("ThongTinNguoiNhan");
        elmRoot.appendChild(nguoiLienHeElm);

        Element hoTenNguoiLienHeElm = doc.createElement("TenNguoiNhan");
        hoTenNguoiLienHeElm.appendChild(doc.createTextNode(sNameReceive));
        nguoiLienHeElm.appendChild(hoTenNguoiLienHeElm);

        Element chucVuNguoiLienHeElm = doc.createElement("DiaChi");
        chucVuNguoiLienHeElm.appendChild(doc.createTextNode(sAddressReceive));
        nguoiLienHeElm.appendChild(chucVuNguoiLienHeElm);

        Element emailNguoiLienHeElm = doc.createElement("DienThoai");
        emailNguoiLienHeElm.appendChild(doc.createTextNode(sPhoneReceive));
        nguoiLienHeElm.appendChild(emailNguoiLienHeElm);

        Element mobileNguoiLienHeElm = doc.createElement("Email");
        mobileNguoiLienHeElm.appendChild(doc.createTextNode(sEmailReceive));
        nguoiLienHeElm.appendChild(mobileNguoiLienHeElm);
        
        Element receiveRegisterElm = doc.createElement("isReceiveRegister");
        receiveRegisterElm.appendChild(doc.createTextNode(isReceiveRegister));
        nguoiLienHeElm.appendChild(receiveRegisterElm);
        
        Element receiveEnterElm = doc.createElement("isReceiveEnter");
        receiveEnterElm.appendChild(doc.createTextNode(isReceiveEnter));
        nguoiLienHeElm.appendChild(receiveEnterElm);

        //ThoiGianDiaDiem
        Element thoiGianDiaDiemElm = doc.createElement("ThoiGianDiaDiem");
        thoiGianDiaDiemElm.appendChild(doc.createTextNode(ThoiGianDiaDiem));
        elmRoot.appendChild(thoiGianDiaDiemElm);
        //soNam
        Element soNamElm = doc.createElement("SoNam");
        soNamElm.appendChild(doc.createTextNode(soNam));
        elmRoot.appendChild(soNamElm);

        // write the content into xml file
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        transformer.transform(source, result);
        String xml = writer.toString();
        return xml;
    }
    
    public static String createXMLChangeInfoFinal(String PRINT_FULLNAME, String PRINT_TAXCODE, String PRINT_CMND,
        String SubjectDNOld, String SubjectDNNew, String sNameReceive, String sAddressReceive, String sPhoneReceive,
        String sEmailReceive, String ThoiGianDiaDiem)
        throws ParserConfigurationException, TransformerException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        // ROOT elements
        Document doc = docBuilder.newDocument();
        Element elmRoot = doc.createElement("CCTSDN");
        doc.appendChild(elmRoot);

        // ThongTinToChuc
        Element thongTinToChucElm = doc.createElement("ThongTinToChuc");
        elmRoot.appendChild(thongTinToChucElm);

        // TenKH elements
        Element tenGiaoDichElm = doc.createElement("TenGiaoDich");
        tenGiaoDichElm.appendChild(doc.createTextNode(PRINT_FULLNAME));
        thongTinToChucElm.appendChild(tenGiaoDichElm);

        Element maSoElm = doc.createElement("MST");
        maSoElm.appendChild(doc.createTextNode(PRINT_TAXCODE));
        thongTinToChucElm.appendChild(maSoElm);

        Element capNgayElm = doc.createElement("CMND");
        capNgayElm.appendChild(doc.createTextNode(PRINT_CMND));
        thongTinToChucElm.appendChild(capNgayElm);

        //GoiDangKy
        Element goiDangKyElm = doc.createElement("ThongTinThayDoi");
        elmRoot.appendChild(goiDangKyElm);

        //ThoiGianSuDung
        Element sSubjectDNOld = doc.createElement("SubjectDNOld");
        sSubjectDNOld.appendChild(doc.createTextNode(SubjectDNOld));
        goiDangKyElm.appendChild(sSubjectDNOld);
        
        Element sSubjectDNNew = doc.createElement("SubjectDNNew");
        sSubjectDNNew.appendChild(doc.createTextNode(SubjectDNNew));
        goiDangKyElm.appendChild(sSubjectDNNew);

        // write the content into xml file
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        transformer.transform(source, result);
        String xml = writer.toString();
        return xml;
    }
    
    public static String createXMLReissueRevokeFinal(String PRINT_FULLNAME, String PRINT_TAXCODE, String PRINT_CMND,
            String sNameReceive, String sAddressReceive, String sPhoneReceive,
            String sEmailReceive, String ThoiGianDiaDiem, String isReissueType, String isRevokeType,
            String sAddress, String sCERTIFICATION_SN, String sEFFECTIVE_DT, String sEXPIRATION_DT)
            throws ParserConfigurationException, TransformerException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        // ROOT elements
        Document doc = docBuilder.newDocument();
        Element elmRoot = doc.createElement("CCTSDN");
        doc.appendChild(elmRoot);

        // ThongTinToChuc
        Element thongTinToChucElm = doc.createElement("ThongTinToChuc");
        elmRoot.appendChild(thongTinToChucElm);

        // TenKH elements
        Element tenGiaoDichElm = doc.createElement("TenGiaoDich");
        tenGiaoDichElm.appendChild(doc.createTextNode(PRINT_FULLNAME));
        thongTinToChucElm.appendChild(tenGiaoDichElm);

        Element maSoElm = doc.createElement("MST");
        maSoElm.appendChild(doc.createTextNode(PRINT_TAXCODE));
        thongTinToChucElm.appendChild(maSoElm);

        Element capNgayElm = doc.createElement("CMND");
        capNgayElm.appendChild(doc.createTextNode(PRINT_CMND));
        thongTinToChucElm.appendChild(capNgayElm);
        
        Element eDiaChi = doc.createElement("DiaChi");
        eDiaChi.appendChild(doc.createTextNode(sAddress));
        thongTinToChucElm.appendChild(eDiaChi);

        //GoiDangKy
        Element goiDangKyElm = doc.createElement("ThongTinThuHoi");
        elmRoot.appendChild(goiDangKyElm);

        //ThoiGianSuDung
        Element eCERTIFICATION_SN = doc.createElement("CertSN");
        eCERTIFICATION_SN.appendChild(doc.createTextNode(sCERTIFICATION_SN));
        goiDangKyElm.appendChild(eCERTIFICATION_SN);
        
        Element eNgayBatDau = doc.createElement("NgayBatDau");
        eNgayBatDau.appendChild(doc.createTextNode(sEFFECTIVE_DT));
        goiDangKyElm.appendChild(eNgayBatDau);
        
        Element eNgayKetThuc = doc.createElement("NgayKetThuc");
        eNgayKetThuc.appendChild(doc.createTextNode(sEXPIRATION_DT));
        goiDangKyElm.appendChild(eNgayKetThuc);
        
        Element eisRevoke = doc.createElement("isRevoke");
        eisRevoke.appendChild(doc.createTextNode(isRevokeType));
        goiDangKyElm.appendChild(eisRevoke);
        
        Element eisReissue = doc.createElement("isReissue");
        eisReissue.appendChild(doc.createTextNode(isReissueType));
        goiDangKyElm.appendChild(eisReissue);

        //NguoiLienHe
        Element nguoiLienHeElm = doc.createElement("ThongTinNguoiNhan");
        elmRoot.appendChild(nguoiLienHeElm);

        Element hoTenNguoiLienHeElm = doc.createElement("TenNguoiNhan");
        hoTenNguoiLienHeElm.appendChild(doc.createTextNode(sNameReceive));
        nguoiLienHeElm.appendChild(hoTenNguoiLienHeElm);

        Element chucVuNguoiLienHeElm = doc.createElement("DiaChi");
        chucVuNguoiLienHeElm.appendChild(doc.createTextNode(sAddressReceive));
        nguoiLienHeElm.appendChild(chucVuNguoiLienHeElm);

        Element emailNguoiLienHeElm = doc.createElement("DienThoai");
        emailNguoiLienHeElm.appendChild(doc.createTextNode(sPhoneReceive));
        nguoiLienHeElm.appendChild(emailNguoiLienHeElm);

        Element mobileNguoiLienHeElm = doc.createElement("Email");
        mobileNguoiLienHeElm.appendChild(doc.createTextNode(sEmailReceive));
        nguoiLienHeElm.appendChild(mobileNguoiLienHeElm);

        //ThoiGianDiaDiem
        Element thoiGianDiaDiemElm = doc.createElement("ThoiGianDiaDiem");
        thoiGianDiaDiemElm.appendChild(doc.createTextNode(ThoiGianDiaDiem));
        elmRoot.appendChild(thoiGianDiaDiemElm);

        // write the content into xml file
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        transformer.transform(source, result);
        String xml = writer.toString();
        return xml;
    }

    public static String createXMLCertificate(String sName, String sIsCongTy, String CongTy, String SubjectDN, String IssuerDN, String Serial,
            String ThoiHanCTSTo, String ThoiHanCTSFrom, String ThoiHanHDTo,
            String ThoiHanHDFrom, String DoDaiKhoa, String TinhNang) {
        String sXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<GCN>\n"
                + "    <TenKH>" + sName + "</TenKH>\n"
                + "    <isCongTy>" + sIsCongTy + "</isCongTy>\n"
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

    public static String createXMLCertificateFinal(String sName, String sIsCongTy, String sShowCompanyName,
            String sCongTy, String sSubjectDN, String sIssuerDN, String sSerial,
            String ThoiHanCTSTo, String ThoiHanCTSFrom, String ThoiHanHDTo,
            String ThoiHanHDFrom, String sDoDaiKhoa, String sTinhNang, String sOrderNumber,
            String sIsMST, String sMST, String sIsCMND, String sCMND)
            throws TransformerException, ParserConfigurationException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        // GCN elements
        Document doc = docBuilder.newDocument();
        Element GCN = doc.createElement("GCN");
        doc.appendChild(GCN);

        // TenKH elements
        Element TenKH = doc.createElement("TenKH");
        TenKH.appendChild(doc.createTextNode(sName));
        GCN.appendChild(TenKH);

        // TenKH elements
        Element isShowCompanyName = doc.createElement("isShowCompanyName");
        isShowCompanyName.appendChild(doc.createTextNode(sShowCompanyName));
        GCN.appendChild(isShowCompanyName);
        
        Element isCongTy = doc.createElement("isCongTy");
        isCongTy.appendChild(doc.createTextNode(sIsCongTy));
        GCN.appendChild(isCongTy);

        // CongTy
        Element CongTy = doc.createElement("CongTy");
        CongTy.appendChild(doc.createTextNode(sCongTy));
        GCN.appendChild(CongTy);
        
        Element isTaxCode = doc.createElement("isTaxCode");
        isTaxCode.appendChild(doc.createTextNode(sIsMST));
        GCN.appendChild(isTaxCode);
        // CongTy
        Element TaxCode = doc.createElement("TaxCode");
        TaxCode.appendChild(doc.createTextNode(sMST));
        GCN.appendChild(TaxCode);
        Element isCMND = doc.createElement("isCMND");
        isCMND.appendChild(doc.createTextNode(sIsCMND));
        GCN.appendChild(isCMND);
        // CongTy
        Element CMND = doc.createElement("CMND");
        CMND.appendChild(doc.createTextNode(sCMND));
        GCN.appendChild(CMND);

        // SubjectDN
        Element SubjectDN = doc.createElement("SubjectDN");
        SubjectDN.appendChild(doc.createTextNode(sSubjectDN));
        GCN.appendChild(SubjectDN);

        // IssuerDN
        Element IssuerDN = doc.createElement("IssuerDN");
        IssuerDN.appendChild(doc.createTextNode(sIssuerDN));
        GCN.appendChild(IssuerDN);

        // Serial
        Element Serial = doc.createElement("Serial");
        Serial.appendChild(doc.createTextNode(sSerial));
        GCN.appendChild(Serial);

        // ThoiHanCTS
        Element ThoiHanCTS = doc.createElement("ThoiHanCTS");
        GCN.appendChild(ThoiHanCTS);

        // Tu CTS
        Element TuCTS = doc.createElement("Tu");
        TuCTS.appendChild(doc.createTextNode(ThoiHanCTSTo));
        ThoiHanCTS.appendChild(TuCTS);
        // Den CTS
        Element DenCTS = doc.createElement("Den");
        DenCTS.appendChild(doc.createTextNode(ThoiHanCTSFrom));
        ThoiHanCTS.appendChild(DenCTS);

        // ThoiHanHD
        Element ThoiHanHD = doc.createElement("ThoiHanHD");
        GCN.appendChild(ThoiHanHD);

        // Tu HD
        Element TuHD = doc.createElement("Tu");
        TuHD.appendChild(doc.createTextNode(ThoiHanHDTo));
        ThoiHanHD.appendChild(TuHD);
        // Den HD
        Element DenHD = doc.createElement("Den");
        DenHD.appendChild(doc.createTextNode(ThoiHanHDFrom));
        ThoiHanHD.appendChild(DenHD);

        // DoDaiKhoa
        Element DoDaiKhoa = doc.createElement("DoDaiKhoa");
        DoDaiKhoa.appendChild(doc.createTextNode(sDoDaiKhoa));
        GCN.appendChild(DoDaiKhoa);

        // TinhNang
        Element TinhNang = doc.createElement("TinhNang");
        TinhNang.appendChild(doc.createTextNode(sTinhNang));
        GCN.appendChild(TinhNang);
        
        // OrderNumber
        Element OrderNumber = doc.createElement("OrderNumber");
        OrderNumber.appendChild(doc.createTextNode(sOrderNumber));
        GCN.appendChild(OrderNumber);

        // write the content into xml file
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);

        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        transformer.transform(source, result);

        String xml = writer.toString();
        return xml;
    }

    public static String createXMLHadover(String NgayTao, String DiaDiem, String BenGiaoPhapNhan, String BenGiaoHoTen, String BenGiaoDiaChi,
            String BenGiaoDienThoai, String BenNhanPhapNhan, String BenNhanHoTen, String BenNhanCMND_HC,
            String BenNhanNgayCap, String BenNhanNoiCap, String BenNhanDiaChi, String BenNhanDienThoai,
            String TenHangHoa, String SerialNumber, String SubjectDN, String IsusuerDN, String DoDaiKhoa,
            String TinhNang, String MaXacThuc, String SoLuongToken, String SoLuongHopDong, String GCN, String SoBan) {
        String sXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<BBBG>\n"
                + "    <NgayTao>" + NgayTao + "</NgayTao>\n"
                + "    <DiaDiem>" + DiaDiem + "</DiaDiem>\n"
                + "    <BenGiao>\n"
                + "        <PhapNhan>" + BenGiaoPhapNhan + "</PhapNhan>\n"
                + "        <HoTen>" + BenGiaoHoTen + "</HoTen>\n"
                + "        <DiaChi>" + BenGiaoDiaChi + "</DiaChi>\n"
                + "        <DienThoai>" + BenGiaoDienThoai + "</DienThoai>\n"
                + "    </BenGiao>\n"
                + "    <BenNhan>\n"
                + "        <PhapNhan>" + BenNhanPhapNhan + "</PhapNhan>\n"
                + "        <HoTen>" + BenNhanHoTen + "</HoTen>\n"
                + "        <CMND_HC>" + BenNhanCMND_HC + "</CMND_HC>\n"
                + "        <NgayCap>" + BenNhanNgayCap + "</NgayCap>\n"
                + "        <NoiCap>" + BenNhanNoiCap + "</NoiCap>\n"
                + "        <DiaChi>" + BenNhanDiaChi + "</DiaChi>\n"
                + "        <DienThoai>" + BenNhanDienThoai + "</DienThoai>\n"
                + "    </BenNhan>\n"
                + "    <DanhMuc>\n"
                + "        <TenHangHoa>" + TenHangHoa + "</TenHangHoa>\n"
                + "        <MoTa>\n"
                + "            <SerialNumber>" + SerialNumber + "</SerialNumber>\n"
                + "            <SubjectDN>" + SubjectDN + "</SubjectDN>\n"
                + "            <IsusuerDN>" + IsusuerDN + "</IsusuerDN>\n"
                + "            <DoDaiKhoa>" + DoDaiKhoa + "</DoDaiKhoa>\n"
                + "            <TinhNang>" + TinhNang + "</TinhNang>\n"
                + "        </MoTa>\n"
                + "        <MaXacThuc>" + MaXacThuc + "</MaXacThuc>\n"
                + "        <SoLuongToken>" + SoLuongToken + "</SoLuongToken>\n"
                + "        <SoLuongHopDong>" + SoLuongHopDong + "</SoLuongHopDong>\n"
                + "        <GCN>" + GCN + "</GCN>\n"
                + "    </DanhMuc>\n"
                + "    <SoBan>" + SoBan + "</SoBan>\n"
                + "</BBBG>";

        return sXML;
    }

    public static String createXMLHadoverFinal(String NgayTao, String DiaDiem, String BenGiaoPhapNhan,
            String BenGiaoHoTen, String BenGiaoDiaChi,
            String BenGiaoDienThoai, String BenNhanPhapNhan, String BenNhanHoTen, String BenNhanCMND_HC,
            String BenNhanNgayCap, String BenNhanNoiCap, String BenNhanDiaChi, String BenNhanDienThoai,
            String TenHangHoa, String SerialNumber, String SubjectDN, String IsusuerDN, String DoDaiKhoa,
            String TinhNang, String MaXacThuc, String SoLuongToken, String SoLuongHopDong, String GCN)
            throws TransformerException, ParserConfigurationException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        // BBBG elements
        Document doc = docBuilder.newDocument();
        Element elmRoot = doc.createElement("BBBG");
        doc.appendChild(elmRoot);

        // TenKH elements
        Element elmNgayTao = doc.createElement("NgayTao");
        elmNgayTao.appendChild(doc.createTextNode(NgayTao));
        elmRoot.appendChild(elmNgayTao);

        // TenKH elements
        Element elmDiaDiem = doc.createElement("DiaDiem");
        elmDiaDiem.appendChild(doc.createTextNode(DiaDiem));
        elmRoot.appendChild(elmDiaDiem);

        // BenGiao
        Element elmBenGiao = doc.createElement("BenGiao");
        elmRoot.appendChild(elmBenGiao);

        // SubjectDN
        Element elmPhanNhanBenGiao = doc.createElement("PhapNhan");
        elmPhanNhanBenGiao.appendChild(doc.createTextNode(BenGiaoPhapNhan));
        elmBenGiao.appendChild(elmPhanNhanBenGiao);

        // IssuerDN
        Element elmHoTenBenGiao = doc.createElement("HoTen");
        elmHoTenBenGiao.appendChild(doc.createTextNode(BenGiaoHoTen));
        elmBenGiao.appendChild(elmHoTenBenGiao);

        // Serial
        Element elmDiaChiBenGiao = doc.createElement("DiaChi");
        elmDiaChiBenGiao.appendChild(doc.createTextNode(BenGiaoDiaChi));
        elmBenGiao.appendChild(elmDiaChiBenGiao);

        // Tu CTS
        Element elmDienThoaiBenGiao = doc.createElement("DienThoai");
        elmDienThoaiBenGiao.appendChild(doc.createTextNode(BenGiaoDienThoai));
        elmBenGiao.appendChild(elmDienThoaiBenGiao);

        // BenNhan
        Element elmBenNhan = doc.createElement("BenNhan");
        elmRoot.appendChild(elmBenNhan);

        // Den CTS
        Element elmPhapNhanBenNhan = doc.createElement("PhapNhan");
        elmPhapNhanBenNhan.appendChild(doc.createTextNode(BenNhanPhapNhan));
        elmBenNhan.appendChild(elmPhapNhanBenNhan);

        // Tu HD
        Element elmHoTenBenNhan = doc.createElement("HoTen");
        elmHoTenBenNhan.appendChild(doc.createTextNode(BenNhanHoTen));
        elmBenNhan.appendChild(elmHoTenBenNhan);

        // Tu HD
        Element emlCMND_HCBenNhan = doc.createElement("CMND_HC");
        emlCMND_HCBenNhan.appendChild(doc.createTextNode(BenNhanCMND_HC));
        elmBenNhan.appendChild(emlCMND_HCBenNhan);
        // Tu HD
        Element elmNgayCapBenNhan = doc.createElement("NgayCap");
        elmNgayCapBenNhan.appendChild(doc.createTextNode(BenNhanNgayCap));
        elmBenNhan.appendChild(elmNgayCapBenNhan);
        // Tu HD
        Element elmNoiCapBenNhan = doc.createElement("NoiCap");
        elmNoiCapBenNhan.appendChild(doc.createTextNode(BenNhanNoiCap));
        elmBenNhan.appendChild(elmNoiCapBenNhan);

        Element elmDiaChiBenNhan = doc.createElement("DiaChi");
        elmDiaChiBenNhan.appendChild(doc.createTextNode(BenNhanDiaChi));
        elmBenNhan.appendChild(elmDiaChiBenNhan);

        Element elmDienThoaiBenNhan = doc.createElement("DienThoai");
        elmDienThoaiBenNhan.appendChild(doc.createTextNode(BenNhanDienThoai));
        elmBenNhan.appendChild(elmDienThoaiBenNhan);

        //Danh Muc
        Element elmDanhMuc = doc.createElement("DanhMuc");
        elmRoot.appendChild(elmDanhMuc);

        Element elmTenHangHoa = doc.createElement("TenHangHoa");
        elmTenHangHoa.appendChild(doc.createTextNode(TenHangHoa));
        elmDanhMuc.appendChild(elmTenHangHoa);

        Element elmMaXacThuc = doc.createElement("MaXacThuc");
        elmMaXacThuc.appendChild(doc.createTextNode(MaXacThuc));
        elmDanhMuc.appendChild(elmMaXacThuc);

        Element elmSoLuongToken = doc.createElement("SoLuongToken");
        elmSoLuongToken.appendChild(doc.createTextNode(SoLuongToken));
        elmDanhMuc.appendChild(elmSoLuongToken);

        Element elmSoLuongHopDong = doc.createElement("SoLuongHopDong");
        elmSoLuongHopDong.appendChild(doc.createTextNode(SoLuongHopDong));
        elmDanhMuc.appendChild(elmSoLuongHopDong);

        Element elmGCN = doc.createElement("GCN");
        elmGCN.appendChild(doc.createTextNode(GCN));
        elmDanhMuc.appendChild(elmGCN);

        //Mo Ta
        Element elmMoTa = doc.createElement("MoTa");
        elmDanhMuc.appendChild(elmMoTa);

        Element elmSerialNumber = doc.createElement("SerialNumber");
        elmSerialNumber.appendChild(doc.createTextNode(SerialNumber));
        elmMoTa.appendChild(elmSerialNumber);

        Element elmSubjectDN = doc.createElement("SubjectDN");
        elmSubjectDN.appendChild(doc.createTextNode(SubjectDN));
        elmMoTa.appendChild(elmSubjectDN);

        Element emlIsusuerDN = doc.createElement("IsusuerDN");
        emlIsusuerDN.appendChild(doc.createTextNode(IsusuerDN));
        elmMoTa.appendChild(emlIsusuerDN);

        Element elmDoDaiKhoa = doc.createElement("DoDaiKhoa");
        elmDoDaiKhoa.appendChild(doc.createTextNode(DoDaiKhoa));
        elmMoTa.appendChild(elmDoDaiKhoa);

        Element elmTinhNang = doc.createElement("TinhNang");
        elmTinhNang.appendChild(doc.createTextNode(TinhNang));
        elmMoTa.appendChild(elmTinhNang);

        // write the content into xml file
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);

//        ByteArrayOutputStream ou = new ByteArrayOutputStream();
//        StreamResult result = new StreamResult(ou);
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        transformer.transform(source, result);

        String xml = writer.toString();// new String(ou.toByteArray());

        return xml;
    }
    
    public static String createXMLHadover2Final(String BenGiaoPhapNhan,
            String BenGiaoHoTen, String BenGiaoDiaChi,
            String BenGiaoDienThoai, String BenNhanPhapNhan, String BenNhanHoTen, String BenNhanDiaChi, String BenNhanDienThoai,
            String SerialNumber, String CompanyName, String PersonalName, String TaxCode, String CMND, String TimeValid, String DateSign)
            throws TransformerException, ParserConfigurationException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        // BBBG elements
        Document doc = docBuilder.newDocument();
        Element elmRoot = doc.createElement("BBBG");
        doc.appendChild(elmRoot);

        // BenGiao
        Element elmBenGiao = doc.createElement("BenGiao");
        elmRoot.appendChild(elmBenGiao);

        // SubjectDN
        Element elmPhanNhanBenGiao = doc.createElement("PhapNhan");
        elmPhanNhanBenGiao.appendChild(doc.createTextNode(BenGiaoPhapNhan));
        elmBenGiao.appendChild(elmPhanNhanBenGiao);

        // IssuerDN
        Element elmHoTenBenGiao = doc.createElement("HoTen");
        elmHoTenBenGiao.appendChild(doc.createTextNode(BenGiaoHoTen));
        elmBenGiao.appendChild(elmHoTenBenGiao);

        // Serial
        Element elmDiaChiBenGiao = doc.createElement("DiaChi");
        elmDiaChiBenGiao.appendChild(doc.createTextNode(BenGiaoDiaChi));
        elmBenGiao.appendChild(elmDiaChiBenGiao);

        // Tu CTS
        Element elmDienThoaiBenGiao = doc.createElement("DienThoai");
        elmDienThoaiBenGiao.appendChild(doc.createTextNode(BenGiaoDienThoai));
        elmBenGiao.appendChild(elmDienThoaiBenGiao);

        // BenNhan
        Element elmBenNhan = doc.createElement("BenNhan");
        elmRoot.appendChild(elmBenNhan);

        // Den CTS
        Element elmPhapNhanBenNhan = doc.createElement("PhapNhan");
        elmPhapNhanBenNhan.appendChild(doc.createTextNode(BenNhanPhapNhan));
        elmBenNhan.appendChild(elmPhapNhanBenNhan);

        // Tu HD
        Element elmHoTenBenNhan = doc.createElement("HoTen");
        elmHoTenBenNhan.appendChild(doc.createTextNode(BenNhanHoTen));
        elmBenNhan.appendChild(elmHoTenBenNhan);

        Element elmDiaChiBenNhan = doc.createElement("DiaChi");
        elmDiaChiBenNhan.appendChild(doc.createTextNode(BenNhanDiaChi));
        elmBenNhan.appendChild(elmDiaChiBenNhan);

        Element elmDienThoaiBenNhan = doc.createElement("DienThoai");
        elmDienThoaiBenNhan.appendChild(doc.createTextNode(BenNhanDienThoai));
        elmBenNhan.appendChild(elmDienThoaiBenNhan);

        //Danh Muc
        Element elmDanhMuc = doc.createElement("DanhMuc");
        elmRoot.appendChild(elmDanhMuc);

        Element elmSerialNumber = doc.createElement("SerialNumber");
        elmSerialNumber.appendChild(doc.createTextNode(SerialNumber));
        elmDanhMuc.appendChild(elmSerialNumber);

        Element elmCompanyName = doc.createElement("CompanyName");
        elmCompanyName.appendChild(doc.createTextNode(CompanyName));
        elmDanhMuc.appendChild(elmCompanyName);

        Element elmPersonalName = doc.createElement("PersonalName");
        elmPersonalName.appendChild(doc.createTextNode(PersonalName));
        elmDanhMuc.appendChild(elmPersonalName);

        Element elmTaxCode = doc.createElement("TaxCode");
        elmTaxCode.appendChild(doc.createTextNode(TaxCode));
        elmDanhMuc.appendChild(elmTaxCode);

        Element elmCMND = doc.createElement("CMND");
        elmCMND.appendChild(doc.createTextNode(CMND));
        elmDanhMuc.appendChild(elmCMND);

        Element elmTimeValid = doc.createElement("TimeValid");
        elmTimeValid.appendChild(doc.createTextNode(TimeValid));
        elmDanhMuc.appendChild(elmTimeValid);
        // DateSign
        Element elmDateSign = doc.createElement("ThoiGianDiaDiem");
        elmDateSign.appendChild(doc.createTextNode(DateSign));
        elmRoot.appendChild(elmDateSign);

        // write the content into xml file
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);

//        ByteArrayOutputStream ou = new ByteArrayOutputStream();
//        StreamResult result = new StreamResult(ou);
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        transformer.transform(source, result);

        String xml = writer.toString();// new String(ou.toByteArray());

        return xml;
    }

    public static String createXMLCertControlFinal(String donViTinh, String diaDiem, String ngay,
            String thang, String nam, String hopDongKinhDoanh, String thangDoiSoatThueBao,
            String namDoiSoatThueBao, String thangDoiSoatHoSo, String namDoiSoatHoSo,
            String thangDoiSoatCongNo, String namDoiSoatCongNo,
            String thangPhatTrien, String namPhatTrien, String benA, String diaChiA, String daiDienA,
            String chucVuA, String benB, String diaChiB, String daiDienB,
            String chucVuB, String nameNoiDung1, String vndNoiDung1, String strNoiDung1, String nameNoiDung2, String vndNoiDung2, String strNoiDung2,
            String nameNoiDung3, String vndNoiDung3, String strNoiDung3, String nameNoiDung4, String vndNoiDung4, String strNoiDung4, String nameNoiDung5,
            String vndNoiDung5, String strNoiDung5, String nameNoiDung6, String vndNoiDung6, String strNoiDung6, String nameNoiDung7, String vndNoiDung7,
            String strNoiDung7, String nameNoiDung8, String getVndNoiDung8, String strNoiDung8)
            throws TransformerException, ParserConfigurationException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        // ROOT elements
        Document doc = docBuilder.newDocument();
        Element elmRoot = doc.createElement("DCCN");
        doc.appendChild(elmRoot);

        Element dvtElm = doc.createElement("DVT");
        dvtElm.appendChild(doc.createTextNode(donViTinh));
        elmRoot.appendChild(dvtElm);

        // ThongTinCaNhan
        Element thoiGianDiaDiemElm = doc.createElement("ThoiGianDiaDiem");
        elmRoot.appendChild(thoiGianDiaDiemElm);

        // TenKH elements
        Element diaDiemElm = doc.createElement("DiaDiem");
        diaDiemElm.appendChild(doc.createTextNode(diaDiem));
        thoiGianDiaDiemElm.appendChild(diaDiemElm);

        Element ngayElm = doc.createElement("Ngay");
        ngayElm.appendChild(doc.createTextNode(ngay));
        thoiGianDiaDiemElm.appendChild(ngayElm);

        Element thangElm = doc.createElement("Thang");
        thangElm.appendChild(doc.createTextNode(thang));
        thoiGianDiaDiemElm.appendChild(thangElm);

        Element namElm = doc.createElement("Nam");
        namElm.appendChild(doc.createTextNode(nam));
        thoiGianDiaDiemElm.appendChild(namElm);

        Element hopDongKinhDoanhElm = doc.createElement("HopDongKinhDoanh");
        hopDongKinhDoanhElm.appendChild(doc.createTextNode(hopDongKinhDoanh));
        elmRoot.appendChild(hopDongKinhDoanhElm);

        Element dccnThueBaoElm = doc.createElement("DoiSoatThueBao");
        elmRoot.appendChild(dccnThueBaoElm);

        Element thangDoiSoatThueBaoElm = doc.createElement("Thang");
        thangDoiSoatThueBaoElm.appendChild(doc.createTextNode(thangDoiSoatThueBao));
        dccnThueBaoElm.appendChild(thangDoiSoatThueBaoElm);

        Element dnamDoiSoatThueBaoElm = doc.createElement("Nam");
        dnamDoiSoatThueBaoElm.appendChild(doc.createTextNode(namDoiSoatThueBao));
        dccnThueBaoElm.appendChild(dnamDoiSoatThueBaoElm);

        Element dccnHoSoElm = doc.createElement("DoiSoatHoSo");
        elmRoot.appendChild(dccnHoSoElm);

        Element thangDoiSoatHoSoElm = doc.createElement("Thang");
        thangDoiSoatHoSoElm.appendChild(doc.createTextNode(thangDoiSoatHoSo));
        dccnHoSoElm.appendChild(thangDoiSoatHoSoElm);

        Element namDoiSoatHoSoElm = doc.createElement("Nam");
        namDoiSoatHoSoElm.appendChild(doc.createTextNode(namDoiSoatHoSo));
        dccnHoSoElm.appendChild(namDoiSoatHoSoElm);
//
        Element dccnCongNoElm = doc.createElement("DoiSoatCongNo");
        elmRoot.appendChild(dccnCongNoElm);

        Element thangDoiSoatCongNoElm = doc.createElement("Thang");
        thangDoiSoatCongNoElm.appendChild(doc.createTextNode(thangDoiSoatCongNo));
        dccnCongNoElm.appendChild(thangDoiSoatCongNoElm);

        Element namDoiSoatCongNoElm = doc.createElement("Nam");
        namDoiSoatCongNoElm.appendChild(doc.createTextNode(namDoiSoatCongNo));
        dccnCongNoElm.appendChild(namDoiSoatCongNoElm);

        Element thoiGianPhatTrienKhachHangElm = doc.createElement("ThoiGianPhatTrienKhachHang");
        elmRoot.appendChild(thoiGianPhatTrienKhachHangElm);

        Element thangPhatTrienElm = doc.createElement("Thang");
        thangPhatTrienElm.appendChild(doc.createTextNode(thangPhatTrien));
        thoiGianPhatTrienKhachHangElm.appendChild(thangPhatTrienElm);

        Element namPhatTrienElm = doc.createElement("Nam");
        namPhatTrienElm.appendChild(doc.createTextNode(namPhatTrien));
        thoiGianPhatTrienKhachHangElm.appendChild(namPhatTrienElm);

        Element phapNhanAElm = doc.createElement("PhapNhanA");
        elmRoot.appendChild(phapNhanAElm);

        Element benAElm = doc.createElement("Ten");
        benAElm.appendChild(doc.createTextNode(benA));
        phapNhanAElm.appendChild(benAElm);

        Element diaChiAElm = doc.createElement("DiaChi");
        diaChiAElm.appendChild(doc.createTextNode(diaChiA));
        phapNhanAElm.appendChild(diaChiAElm);

        Element daiDienAElm = doc.createElement("DaiDien");
        daiDienAElm.appendChild(doc.createTextNode(daiDienA));
        phapNhanAElm.appendChild(daiDienAElm);

        Element chucVuAElm = doc.createElement("ChucVu");
        chucVuAElm.appendChild(doc.createTextNode(chucVuA));
        phapNhanAElm.appendChild(chucVuAElm);

        Element phapNhanBElm = doc.createElement("PhapNhanB");
        elmRoot.appendChild(phapNhanBElm);

        Element benBElm = doc.createElement("Ten");
        benBElm.appendChild(doc.createTextNode(benB));
        phapNhanBElm.appendChild(benBElm);

        Element diaChiBElm = doc.createElement("DiaChi");
        diaChiBElm.appendChild(doc.createTextNode(diaChiB));
        phapNhanBElm.appendChild(diaChiBElm);

        Element daiDienBElm = doc.createElement("DaiDien");
        daiDienBElm.appendChild(doc.createTextNode(daiDienB));
        phapNhanBElm.appendChild(daiDienBElm);

        Element chucVuBElm = doc.createElement("ChucVu");
        chucVuBElm.appendChild(doc.createTextNode(chucVuB));
        phapNhanBElm.appendChild(chucVuBElm);

        Element danhMucElm = doc.createElement("DanhMuc");
        elmRoot.appendChild(danhMucElm);

        Element nameNoiDung1Elm = doc.createElement("NameNoiDung1");
        nameNoiDung1Elm.appendChild(doc.createTextNode(nameNoiDung1));
        danhMucElm.appendChild(nameNoiDung1Elm);

        Element vndNoiDung1Elm = doc.createElement("VNDNoiDung1");
        vndNoiDung1Elm.appendChild(doc.createTextNode(String.valueOf(vndNoiDung1)));
        danhMucElm.appendChild(vndNoiDung1Elm);

        Element strNoiDung1Elm = doc.createElement("STRNoiDung1");
        strNoiDung1Elm.appendChild(doc.createTextNode(strNoiDung1));
        danhMucElm.appendChild(strNoiDung1Elm);

        Element nameNoiDung2Elm = doc.createElement("NameNoiDung2");
        nameNoiDung2Elm.appendChild(doc.createTextNode(nameNoiDung2));
        danhMucElm.appendChild(nameNoiDung2Elm);

        Element vndNoiDung2Elm = doc.createElement("VNDNoiDung2");
        vndNoiDung2Elm.appendChild(doc.createTextNode(String.valueOf(vndNoiDung2)));
        danhMucElm.appendChild(vndNoiDung2Elm);

        Element strNoiDung2Elm = doc.createElement("STRNoiDung2");
        strNoiDung2Elm.appendChild(doc.createTextNode(strNoiDung2));
        danhMucElm.appendChild(strNoiDung2Elm);

        Element nameNoiDung3Elm = doc.createElement("NameNoiDung3");
        nameNoiDung3Elm.appendChild(doc.createTextNode(nameNoiDung3));
        danhMucElm.appendChild(nameNoiDung3Elm);

        Element vndNoiDung3Elm = doc.createElement("VNDNoiDung3");
        vndNoiDung3Elm.appendChild(doc.createTextNode(String.valueOf(vndNoiDung3)));
        danhMucElm.appendChild(vndNoiDung3Elm);

        Element strNoiDung3Elm = doc.createElement("STRNoiDung3");
        strNoiDung3Elm.appendChild(doc.createTextNode(strNoiDung3));
        danhMucElm.appendChild(strNoiDung3Elm);

        Element nameNoiDung4Elm = doc.createElement("NameNoiDung4");
        nameNoiDung4Elm.appendChild(doc.createTextNode(nameNoiDung4));
        danhMucElm.appendChild(nameNoiDung4Elm);

        Element vndNoiDung4Elm = doc.createElement("VNDNoiDung4");
        vndNoiDung4Elm.appendChild(doc.createTextNode(String.valueOf(vndNoiDung4)));
        danhMucElm.appendChild(vndNoiDung4Elm);

        Element strNoiDung4Elm = doc.createElement("STRNoiDung4");
        strNoiDung4Elm.appendChild(doc.createTextNode(strNoiDung4));
        danhMucElm.appendChild(strNoiDung4Elm);

        Element nameNoiDung5Elm = doc.createElement("NameNoiDung5");
        nameNoiDung5Elm.appendChild(doc.createTextNode(nameNoiDung5));
        danhMucElm.appendChild(nameNoiDung5Elm);

        Element vndNoiDung5Elm = doc.createElement("VNDNoiDung5");
        vndNoiDung5Elm.appendChild(doc.createTextNode(String.valueOf(vndNoiDung5)));
        danhMucElm.appendChild(vndNoiDung5Elm);

        Element strNoiDung5Elm = doc.createElement("STRNoiDung5");
        strNoiDung5Elm.appendChild(doc.createTextNode(strNoiDung5));
        danhMucElm.appendChild(strNoiDung5Elm);

        Element nameNoiDung6Elm = doc.createElement("NameNoiDung6");
        nameNoiDung6Elm.appendChild(doc.createTextNode(nameNoiDung6));
        danhMucElm.appendChild(nameNoiDung6Elm);

        Element vndNoiDung6Elm = doc.createElement("VNDNoiDung6");
        vndNoiDung6Elm.appendChild(doc.createTextNode(String.valueOf(vndNoiDung6)));
        danhMucElm.appendChild(vndNoiDung6Elm);

        Element strNoiDung6Elm = doc.createElement("STRNoiDung6");
        strNoiDung6Elm.appendChild(doc.createTextNode(strNoiDung6));
        danhMucElm.appendChild(strNoiDung6Elm);

        Element nameNoiDung7Elm = doc.createElement("NameNoiDung7");
        nameNoiDung7Elm.appendChild(doc.createTextNode(nameNoiDung7));
        danhMucElm.appendChild(nameNoiDung7Elm);

        Element vndNoiDung7Elm = doc.createElement("VNDNoiDung7");
        vndNoiDung7Elm.appendChild(doc.createTextNode(String.valueOf(vndNoiDung7)));
        danhMucElm.appendChild(vndNoiDung7Elm);

        Element strNoiDung7Elm = doc.createElement("STRNoiDung7");
        strNoiDung7Elm.appendChild(doc.createTextNode(strNoiDung7));
        danhMucElm.appendChild(strNoiDung7Elm);

        Element nameNoiDung8Elm = doc.createElement("NameNoiDung8");
        nameNoiDung8Elm.appendChild(doc.createTextNode(nameNoiDung8));
        danhMucElm.appendChild(nameNoiDung8Elm);

        Element vndNoiDung8Elm = doc.createElement("VNDNoiDung8");
        vndNoiDung8Elm.appendChild(doc.createTextNode(String.valueOf(getVndNoiDung8)));
        danhMucElm.appendChild(vndNoiDung8Elm);

        Element strNoiDung8Elm = doc.createElement("STRNoiDung8");
        strNoiDung8Elm.appendChild(doc.createTextNode(strNoiDung8));
        danhMucElm.appendChild(strNoiDung8Elm);

        // write the content into xml file
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);

//        ByteArrayOutputStream ou = new ByteArrayOutputStream();
//        StreamResult result = new StreamResult(ou);
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        transformer.transform(source, result);

        String xml = writer.toString();// new String(ou.toByteArray());

        return xml;
    }

    public static String createXMLCertListFinal(String fromDate, String toDate, REPORT_PER_MONTH[][] rsReport,
            String Note1, String Note2)
            throws TransformerException, ParserConfigurationException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        // ROOT elements
        Document doc = docBuilder.newDocument();
        Element elmRoot = doc.createElement("LS");
        doc.appendChild(elmRoot);

        // ThongTinCaNhan
        Element thoiGianElm = doc.createElement("DateTime");
        elmRoot.appendChild(thoiGianElm);

        Element fromDateElm = doc.createElement("FromDate");
        fromDateElm.appendChild(doc.createTextNode(fromDate));
        thoiGianElm.appendChild(fromDateElm);

        Element toDateElm = doc.createElement("ToDate");
        toDateElm.appendChild(doc.createTextNode(toDate));
        thoiGianElm.appendChild(toDateElm);

        Element listElm = doc.createElement("List");
        elmRoot.appendChild(listElm);
        int i = 1;
        for (REPORT_PER_MONTH item : rsReport[0]) {
            Element itemElm = doc.createElement("Item");
            listElm.appendChild(itemElm);

            Element sttElm = doc.createElement("No");
            sttElm.appendChild(doc.createTextNode(String.valueOf(i++)));
            itemElm.appendChild(sttElm);

            Element agencyCodeElm = doc.createElement("AgencyCode");
            agencyCodeElm.appendChild(doc.createTextNode(EscapeUtils.CheckTextNull(item.BRANCH_NAME)));
            itemElm.appendChild(agencyCodeElm);

            Element userCodeElm = doc.createElement("UserCode");
            userCodeElm.appendChild(doc.createTextNode(EscapeUtils.CheckTextNull(item.USERNAME_CREATED)));
            itemElm.appendChild(userCodeElm);

            String sP_IDAndPASSPORT = EscapeUtils.CheckTextNull(item.P_ID);
            Element identityElm = doc.createElement("Identity");
            identityElm.appendChild(doc.createTextNode(sP_IDAndPASSPORT));
            itemElm.appendChild(identityElm);

            Element certicateStatusElm = doc.createElement("CerticateStatus");
            certicateStatusElm.appendChild(doc.createTextNode(EscapeUtils.CheckTextNull(item.CERTIFICATION_STATE_NAME)));
            itemElm.appendChild(certicateStatusElm);

            Element companyNameElm = doc.createElement("CompanyName");
            companyNameElm.appendChild(doc.createTextNode(EscapeUtils.CheckTextNull(item.COMPANY_NAME)));
            itemElm.appendChild(companyNameElm);

            String sMSTAndBUDGET_CODE = EscapeUtils.CheckTextNull(item.TAX_CODE);
            Element taxCodeElm = doc.createElement("TaxCode");
            taxCodeElm.appendChild(doc.createTextNode(sMSTAndBUDGET_CODE));
            itemElm.appendChild(taxCodeElm);

            Element userNameElm = doc.createElement("UserName");
            userNameElm.appendChild(doc.createTextNode(EscapeUtils.CheckTextNull(item.PERSONAL_NAME)));
            itemElm.appendChild(userNameElm);
            
            String strDeviceUUID = "";
            if(item.CERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_ID_DEVICE) {
                strDeviceUUID = EscapeUtils.CheckTextNull(item.SERVICE_UUID);
            }
            Element deviceUUIDElm = doc.createElement("DeviceUUID");
            deviceUUIDElm.appendChild(doc.createTextNode(strDeviceUUID));
            itemElm.appendChild(deviceUUIDElm);

            Element cityElm = doc.createElement("City");
            cityElm.appendChild(doc.createTextNode(EscapeUtils.CheckTextNull(item.PROVINCE_NAME)));
            itemElm.appendChild(cityElm);

            Element benefitPackageElm = doc.createElement("BenefitPackage");
            benefitPackageElm.appendChild(doc.createTextNode(EscapeUtils.CheckTextNull(item.CERTIFICATION_PROFILE_NAME)));
            itemElm.appendChild(benefitPackageElm);
            
            Element requestTypeElm = doc.createElement("RequestType");
            requestTypeElm.appendChild(doc.createTextNode(EscapeUtils.CheckTextNull(item.CERTIFICATION_ATTR_TYPE_DESC)));
            itemElm.appendChild(requestTypeElm);
            
            Element formFactorElm = doc.createElement("FormFactor");
            formFactorElm.appendChild(doc.createTextNode(EscapeUtils.CheckTextNull(item.FORM_FACTOR_DESC)));
            itemElm.appendChild(formFactorElm);

            Element createDateElm = doc.createElement("CreateDate");
            createDateElm.appendChild(doc.createTextNode(EscapeUtils.CheckTextNull(item.CREATED_DT)));
            itemElm.appendChild(createDateElm);

            Element cancelDateElm = doc.createElement("CancelDate");
            cancelDateElm.appendChild(doc.createTextNode(EscapeUtils.CheckTextNull(item.REVOKED_DT)));
            itemElm.appendChild(cancelDateElm);

            Element numberOfDaysCanceledElm = doc.createElement("NumberOfDaysCanceled");
            numberOfDaysCanceledElm.appendChild(doc.createTextNode(EscapeUtils.CheckTextNull(item.NUMBER_DELETED)));
            itemElm.appendChild(numberOfDaysCanceledElm);

            Element numberOfDaysGeneralElm = doc.createElement("NumberOfDaysGeneral");
            numberOfDaysGeneralElm.appendChild(doc.createTextNode(EscapeUtils.CheckTextNull(item.GENERATED_DT)));
            itemElm.appendChild(numberOfDaysGeneralElm);

            Element note1Elm = doc.createElement("Note1");
            note1Elm.appendChild(doc.createTextNode(Note1));
            itemElm.appendChild(note1Elm);

            Element note2Elm = doc.createElement("Note2");
            note2Elm.appendChild(doc.createTextNode(Note2));
            itemElm.appendChild(note2Elm);
        }
        // write the content into xml file
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);

        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        transformer.transform(source, result);

        String xml = writer.toString();// new String(ou.toByteArray());

        return xml;

//        TransformerFactory transformerFactory = TransformerFactory.newInstance();
//        Transformer transformer = transformerFactory.newTransformer();
//        DOMSource source = new DOMSource(doc);
//
//        StringWriter writer = new StringWriter();
//        StreamResult result = new StreamResult(writer);
//        transformer.transform(source, result);
//
//        String xml = writer.toString();// new String(ou.toByteArray());
//        return xml;
    }

    public static String createXMLReportRecurringFinal(String sNUMBER, String sQUATER, String sYEAR, String sNUMBER_ENTERPRISE1,
            String sNUMBER_STAFF1, String sNUMBER_PERSONAL1, String sSTATUS1, String sSUM1, String sNUMBER_ENTERPRISE2,
            String sNUMBER_STAFF2, String sNUMBER_PERSONAL2, String sSTATUS2, String sSUM2, String sDATE_TIME, String sNUMBER_ENTERPRISE3,
            String sNUMBER_STAFF3, String sNUMBER_PERSONAL3, String sSTATUS3, String sSUM3)
            throws TransformerException, ParserConfigurationException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        // ROOT elements
        Document doc = docBuilder.newDocument();
        Element elmRoot = doc.createElement("REPORT_RECURRING");
        doc.appendChild(elmRoot);

        Element fromDateElm = doc.createElement("NUMBER");
        fromDateElm.appendChild(doc.createTextNode(sNUMBER));
        elmRoot.appendChild(fromDateElm);

        Element toQUATER = doc.createElement("QUATER");
        toQUATER.appendChild(doc.createTextNode(sQUATER));
        elmRoot.appendChild(toQUATER);

        Element toYEAR = doc.createElement("YEAR");
        toYEAR.appendChild(doc.createTextNode(sYEAR));
        elmRoot.appendChild(toYEAR);

        Element toNUMBER_ENTERPRISE1 = doc.createElement("NUMBER_ENTERPRISE1");
        toNUMBER_ENTERPRISE1.appendChild(doc.createTextNode(sNUMBER_ENTERPRISE1));
        elmRoot.appendChild(toNUMBER_ENTERPRISE1);

        Element toNUMBER_STAFF1 = doc.createElement("NUMBER_STAFF1");
        toNUMBER_STAFF1.appendChild(doc.createTextNode(sNUMBER_STAFF1));
        elmRoot.appendChild(toNUMBER_STAFF1);

        Element toNUMBER_PERSONAL1 = doc.createElement("NUMBER_PERSONAL1");
        toNUMBER_PERSONAL1.appendChild(doc.createTextNode(sNUMBER_PERSONAL1));
        elmRoot.appendChild(toNUMBER_PERSONAL1);

        Element toSTATUS1 = doc.createElement("STATUS1");
        toSTATUS1.appendChild(doc.createTextNode(sSTATUS1));
        elmRoot.appendChild(toSTATUS1);

        Element toSUM1 = doc.createElement("SUM1");
        toSUM1.appendChild(doc.createTextNode(sSUM1));
        elmRoot.appendChild(toSUM1);

        Element toNUMBER_ENTERPRISE2 = doc.createElement("NUMBER_ENTERPRISE2");
        toNUMBER_ENTERPRISE2.appendChild(doc.createTextNode(sNUMBER_ENTERPRISE2));
        elmRoot.appendChild(toNUMBER_ENTERPRISE2);

        Element toNUMBER_STAFF2 = doc.createElement("NUMBER_STAFF2");
        toNUMBER_STAFF2.appendChild(doc.createTextNode(sNUMBER_STAFF2));
        elmRoot.appendChild(toNUMBER_STAFF2);

        Element toNUMBER_PERSONAL2 = doc.createElement("NUMBER_PERSONAL2");
        toNUMBER_PERSONAL2.appendChild(doc.createTextNode(sNUMBER_PERSONAL2));
        elmRoot.appendChild(toNUMBER_PERSONAL2);

        Element toSTATUS2 = doc.createElement("STATUS2");
        toSTATUS2.appendChild(doc.createTextNode(sSTATUS2));
        elmRoot.appendChild(toSTATUS2);

        Element toSUM2 = doc.createElement("SUM2");
        toSUM2.appendChild(doc.createTextNode(sSUM2));
        elmRoot.appendChild(toSUM2);

        Element toNUMBER_ENTERPRISE3 = doc.createElement("NUMBER_ENTERPRISE3");
        toNUMBER_ENTERPRISE3.appendChild(doc.createTextNode(sNUMBER_ENTERPRISE3));
        elmRoot.appendChild(toNUMBER_ENTERPRISE3);

        Element toNUMBER_STAFF3 = doc.createElement("NUMBER_STAFF3");
        toNUMBER_STAFF3.appendChild(doc.createTextNode(sNUMBER_STAFF3));
        elmRoot.appendChild(toNUMBER_STAFF3);

        Element toNUMBER_PERSONAL3 = doc.createElement("NUMBER_PERSONAL3");
        toNUMBER_PERSONAL3.appendChild(doc.createTextNode(sNUMBER_PERSONAL3));
        elmRoot.appendChild(toNUMBER_PERSONAL3);

        Element toSTATUS3 = doc.createElement("STATUS3");
        toSTATUS3.appendChild(doc.createTextNode(sSTATUS3));
        elmRoot.appendChild(toSTATUS3);

        Element toSUM3 = doc.createElement("SUM3");
        toSUM3.appendChild(doc.createTextNode(sSUM3));
        elmRoot.appendChild(toSUM3);

        Element tosDATE_TIME = doc.createElement("DATE_TIME");
        tosDATE_TIME.appendChild(doc.createTextNode(sDATE_TIME));
        elmRoot.appendChild(tosDATE_TIME);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);

        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        transformer.transform(source, result);

        String xml = writer.toString();

        return xml;
    }
    
    public static String createXMLReportRecurringFinal_New(String sQUATER, String sYEAR,
            REPORT_RECURRING_NEAC[][] rsRecurringIssue, REPORT_RECURRING_NEAC[][] rsRecurringRevoke,
            REPORT_RECURRING_NEAC[][] rsRecurringOperation, String sDATE_TIME)
        throws TransformerException, ParserConfigurationException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        CommonFunction com = new CommonFunction();
        // ROOT elements
        Document doc = docBuilder.newDocument();
        Element elmRoot = doc.createElement("REPORT_RECURRING");
        doc.appendChild(elmRoot);

//        Element fromDateElm = doc.createElement("NUMBER");
//        fromDateElm.appendChild(doc.createTextNode(sNUMBER));
//        elmRoot.appendChild(fromDateElm);

        Element toQUATER = doc.createElement("QUATER");
        toQUATER.appendChild(doc.createTextNode(sQUATER));
        elmRoot.appendChild(toQUATER);

        Element toYEAR = doc.createElement("YEAR");
        toYEAR.appendChild(doc.createTextNode(sYEAR));
        elmRoot.appendChild(toYEAR);

        Element listElm = doc.createElement("List");
        elmRoot.appendChild(listElm);
        int sumIssueEnterprise = 0;
        int sumRevokeEnterprise = 0;
        int sumOperationEnterprise = 0;
        int sumIssuePersonal = 0;
        int sumRevokePersonal = 0;
        int sumOperationPersonal = 0;
        int sumIssueStaff = 0;
        int sumRevokeStaff = 0;
        int sumOperationStaff = 0;
        int i = 1;
        if(rsRecurringIssue[0].length > 0) {
            for (REPORT_RECURRING_NEAC item : rsRecurringIssue[0]) {
                sumIssueEnterprise = sumIssueEnterprise + item.TOTAL_ENTERPRISE;
                sumIssuePersonal = sumIssuePersonal + item.TOTAL_PERSONAL;
                sumIssueStaff = sumIssueStaff + item.TOTAL_STAFF;
                Element itemElm = doc.createElement("Issue");
                listElm.appendChild(itemElm);
                Element sttElm = doc.createElement("No");
                sttElm.appendChild(doc.createTextNode(String.valueOf(i++)));
                itemElm.appendChild(sttElm);
                
                Element contentElm = doc.createElement("Content");
                contentElm.appendChild(doc.createTextNode(item.CONTENT));
                itemElm.appendChild(contentElm);
                
                int intEnterprise = item.TOTAL_ENTERPRISE;
                Element enterpriseElm = doc.createElement("Enterprise");
                enterpriseElm.appendChild(doc.createTextNode(com.convertMoneyZero(intEnterprise)));
                itemElm.appendChild(enterpriseElm);
                
                int intPersonal = item.TOTAL_PERSONAL;
                Element personalElm = doc.createElement("Personal");
                personalElm.appendChild(doc.createTextNode(com.convertMoneyZero(intPersonal)));
                itemElm.appendChild(personalElm);
                
                int intStaff = item.TOTAL_STAFF;
                Element staffElm = doc.createElement("Staff");
                staffElm.appendChild(doc.createTextNode(com.convertMoneyZero(intStaff)));
                itemElm.appendChild(staffElm);
                
                int intSum = item.SUM;
                Element sumElm = doc.createElement("Sum");
                sumElm.appendChild(doc.createTextNode(com.convertMoneyZero(intSum)));
                itemElm.appendChild(sumElm);
            }
        }
        Element issueElm = doc.createElement("Issue");
        elmRoot.appendChild(issueElm);
        Element sumIssueEnterElm = doc.createElement("Enterprise");
        sumIssueEnterElm.appendChild(doc.createTextNode(com.convertMoneyZero(sumIssueEnterprise)));
        issueElm.appendChild(sumIssueEnterElm);
        Element sumIssuePersonalElm = doc.createElement("Personal");
        sumIssuePersonalElm.appendChild(doc.createTextNode(com.convertMoneyZero(sumIssuePersonal)));
        issueElm.appendChild(sumIssuePersonalElm);
        Element sumIssueStaffElm = doc.createElement("Staff");
        sumIssueStaffElm.appendChild(doc.createTextNode(com.convertMoneyZero(sumIssueStaff)));
        issueElm.appendChild(sumIssueStaffElm);
        Element sumIssueSumElm = doc.createElement("Sum");
        sumIssueSumElm.appendChild(doc.createTextNode(com.convertMoneyZero(sumIssueEnterprise + sumIssuePersonal + sumIssueStaff)));
        issueElm.appendChild(sumIssueSumElm);
        
        
        i = 1;
        if(rsRecurringRevoke[0].length > 0) {
            for (REPORT_RECURRING_NEAC item : rsRecurringRevoke[0]) {
                sumRevokeEnterprise = sumRevokeEnterprise + item.TOTAL_ENTERPRISE;
                sumRevokePersonal = sumRevokePersonal + item.TOTAL_PERSONAL;
                sumRevokeStaff = sumRevokeStaff + item.TOTAL_STAFF;
                Element itemElm = doc.createElement("Revoke");
                listElm.appendChild(itemElm);
                Element sttElm = doc.createElement("No");
                sttElm.appendChild(doc.createTextNode(String.valueOf(i++)));
                itemElm.appendChild(sttElm);
                
                Element contentElm = doc.createElement("Content");
                contentElm.appendChild(doc.createTextNode(item.CONTENT));
                itemElm.appendChild(contentElm);
                
                int intEnterprise = item.TOTAL_ENTERPRISE;
                Element enterpriseElm = doc.createElement("Enterprise");
                enterpriseElm.appendChild(doc.createTextNode(com.convertMoneyZero(intEnterprise)));
                itemElm.appendChild(enterpriseElm);
                
                int intPersonal = item.TOTAL_PERSONAL;
                Element personalElm = doc.createElement("Personal");
                personalElm.appendChild(doc.createTextNode(com.convertMoneyZero(intPersonal)));
                itemElm.appendChild(personalElm);
                
                int intStaff = item.TOTAL_STAFF;
                Element staffElm = doc.createElement("Staff");
                staffElm.appendChild(doc.createTextNode(com.convertMoneyZero(intStaff)));
                itemElm.appendChild(staffElm);
                
                int intSum = item.SUM;
                Element sumElm = doc.createElement("Sum");
                sumElm.appendChild(doc.createTextNode(com.convertMoneyZero(intSum)));
                itemElm.appendChild(sumElm);
            }
        }
        Element revokeElm = doc.createElement("Revoke");
        elmRoot.appendChild(revokeElm);
        Element sumRevokeEnterElm = doc.createElement("Enterprise");
        sumRevokeEnterElm.appendChild(doc.createTextNode(com.convertMoneyZero(sumRevokeEnterprise)));
        revokeElm.appendChild(sumRevokeEnterElm);
        Element sumRevokePersonalElm = doc.createElement("Personal");
        sumRevokePersonalElm.appendChild(doc.createTextNode(com.convertMoneyZero(sumRevokePersonal)));
        revokeElm.appendChild(sumRevokePersonalElm);
        Element sumRevokeStaffElm = doc.createElement("Staff");
        sumRevokeStaffElm.appendChild(doc.createTextNode(com.convertMoneyZero(sumRevokeStaff)));
        revokeElm.appendChild(sumRevokeStaffElm);
        Element sumRevokeSumElm = doc.createElement("Sum");
        sumRevokeSumElm.appendChild(doc.createTextNode(com.convertMoneyZero(sumRevokeEnterprise + sumRevokePersonal + sumRevokeStaff)));
        revokeElm.appendChild(sumRevokeSumElm);
        
        i = 1;
        if(rsRecurringOperation[0].length > 0) {
            for (REPORT_RECURRING_NEAC item : rsRecurringOperation[0]) {
                sumOperationEnterprise = sumOperationEnterprise + item.TOTAL_ENTERPRISE;
                sumOperationPersonal = sumOperationPersonal + item.TOTAL_PERSONAL;
                sumOperationStaff = sumOperationStaff + item.TOTAL_STAFF;
                Element itemElm = doc.createElement("Operation");
                listElm.appendChild(itemElm);
                Element sttElm = doc.createElement("No");
                sttElm.appendChild(doc.createTextNode(String.valueOf(i++)));
                itemElm.appendChild(sttElm);
                
                Element contentElm = doc.createElement("Content");
                contentElm.appendChild(doc.createTextNode(item.CONTENT));
                itemElm.appendChild(contentElm);
                
                int intEnterprise = item.TOTAL_ENTERPRISE;
                Element enterpriseElm = doc.createElement("Enterprise");
                enterpriseElm.appendChild(doc.createTextNode(com.convertMoneyZero(intEnterprise)));
                itemElm.appendChild(enterpriseElm);
                
                int intPersonal = item.TOTAL_PERSONAL;
                Element personalElm = doc.createElement("Personal");
                personalElm.appendChild(doc.createTextNode(com.convertMoneyZero(intPersonal)));
                itemElm.appendChild(personalElm);
                
                int intStaff = item.TOTAL_STAFF;
                Element staffElm = doc.createElement("Staff");
                staffElm.appendChild(doc.createTextNode(com.convertMoneyZero(intStaff)));
                itemElm.appendChild(staffElm);
                
                int intSum = item.SUM;
                Element sumElm = doc.createElement("Sum");
                sumElm.appendChild(doc.createTextNode(com.convertMoneyZero(intSum)));
                itemElm.appendChild(sumElm);
            }
        }
        Element operationElm = doc.createElement("Operation");
        elmRoot.appendChild(operationElm);
        Element sumOperationEnterElm = doc.createElement("Enterprise");
        sumOperationEnterElm.appendChild(doc.createTextNode(com.convertMoneyZero(sumOperationEnterprise)));
        operationElm.appendChild(sumOperationEnterElm);
        Element sumOperationPersonalElm = doc.createElement("Personal");
        sumOperationPersonalElm.appendChild(doc.createTextNode(com.convertMoneyZero(sumOperationPersonal)));
        operationElm.appendChild(sumOperationPersonalElm);
        Element sumOperationStaffElm = doc.createElement("Staff");
        sumOperationStaffElm.appendChild(doc.createTextNode(com.convertMoneyZero(sumOperationStaff)));
        operationElm.appendChild(sumOperationStaffElm);
        Element sumOperationSumElm = doc.createElement("Sum");
        sumOperationSumElm.appendChild(doc.createTextNode(com.convertMoneyZero(sumOperationEnterprise + sumOperationPersonal + sumOperationStaff)));
        operationElm.appendChild(sumOperationSumElm);
        
//        Element toNUMBER_ENTERPRISE1 = doc.createElement("NUMBER_ENTERPRISE1");
//        toNUMBER_ENTERPRISE1.appendChild(doc.createTextNode(sNUMBER_ENTERPRISE1));
//        elmRoot.appendChild(toNUMBER_ENTERPRISE1);
//
//        Element toNUMBER_STAFF1 = doc.createElement("NUMBER_STAFF1");
//        toNUMBER_STAFF1.appendChild(doc.createTextNode(sNUMBER_STAFF1));
//        elmRoot.appendChild(toNUMBER_STAFF1);
//
//        Element toNUMBER_PERSONAL1 = doc.createElement("NUMBER_PERSONAL1");
//        toNUMBER_PERSONAL1.appendChild(doc.createTextNode(sNUMBER_PERSONAL1));
//        elmRoot.appendChild(toNUMBER_PERSONAL1);
//
//        Element toSTATUS1 = doc.createElement("STATUS1");
//        toSTATUS1.appendChild(doc.createTextNode(sSTATUS1));
//        elmRoot.appendChild(toSTATUS1);
//
//        Element toSUM1 = doc.createElement("SUM1");
//        toSUM1.appendChild(doc.createTextNode(sSUM1));
//        elmRoot.appendChild(toSUM1);
//
//        Element toNUMBER_ENTERPRISE2 = doc.createElement("NUMBER_ENTERPRISE2");
//        toNUMBER_ENTERPRISE2.appendChild(doc.createTextNode(sNUMBER_ENTERPRISE2));
//        elmRoot.appendChild(toNUMBER_ENTERPRISE2);
//
//        Element toNUMBER_STAFF2 = doc.createElement("NUMBER_STAFF2");
//        toNUMBER_STAFF2.appendChild(doc.createTextNode(sNUMBER_STAFF2));
//        elmRoot.appendChild(toNUMBER_STAFF2);
//
//        Element toNUMBER_PERSONAL2 = doc.createElement("NUMBER_PERSONAL2");
//        toNUMBER_PERSONAL2.appendChild(doc.createTextNode(sNUMBER_PERSONAL2));
//        elmRoot.appendChild(toNUMBER_PERSONAL2);
//
//        Element toSTATUS2 = doc.createElement("STATUS2");
//        toSTATUS2.appendChild(doc.createTextNode(sSTATUS2));
//        elmRoot.appendChild(toSTATUS2);
//
//        Element toSUM2 = doc.createElement("SUM2");
//        toSUM2.appendChild(doc.createTextNode(sSUM2));
//        elmRoot.appendChild(toSUM2);
//
//        Element toNUMBER_ENTERPRISE3 = doc.createElement("NUMBER_ENTERPRISE3");
//        toNUMBER_ENTERPRISE3.appendChild(doc.createTextNode(sNUMBER_ENTERPRISE3));
//        elmRoot.appendChild(toNUMBER_ENTERPRISE3);
//
//        Element toNUMBER_STAFF3 = doc.createElement("NUMBER_STAFF3");
//        toNUMBER_STAFF3.appendChild(doc.createTextNode(sNUMBER_STAFF3));
//        elmRoot.appendChild(toNUMBER_STAFF3);
//
//        Element toNUMBER_PERSONAL3 = doc.createElement("NUMBER_PERSONAL3");
//        toNUMBER_PERSONAL3.appendChild(doc.createTextNode(sNUMBER_PERSONAL3));
//        elmRoot.appendChild(toNUMBER_PERSONAL3);
//
//        Element toSTATUS3 = doc.createElement("STATUS3");
//        toSTATUS3.appendChild(doc.createTextNode(sSTATUS3));
//        elmRoot.appendChild(toSTATUS3);
//
//        Element toSUM3 = doc.createElement("SUM3");
//        toSUM3.appendChild(doc.createTextNode(sSUM3));
//        elmRoot.appendChild(toSUM3);

        Element tosDATE_TIME = doc.createElement("DATE_TIME");
        tosDATE_TIME.appendChild(doc.createTextNode(sDATE_TIME));
        elmRoot.appendChild(tosDATE_TIME);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);

        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        transformer.transform(source, result);

        String xml = writer.toString();

        return xml;
    }
    
    public static String createXMLReportRecurringIVFinal(String sNUMBER, String sQUATER, String sYEAR, String sNUMBER_ISSUED_ENTERPRISE1,
            String sNUMBER_ISSUED_ENTERPRISE2, String sNUMBER_ISSUED_ENTERPRISE3, String sNUMBER_ISSUED_ENTERPRISE4,
            String sNUMBER_ISSUED_PERSONAL1, String sNUMBER_ISSUED_PERSONAL2,
            String sNUMBER_ISSUED_PERSONAL3, String sNUMBER_ISSUED_PERSONAL4, String sNUMBER_REVOKE_ENTERPRISE1,
            String sNUMBER_REVOKE_ENTERPRISE2, String sNUMBER_REVOKE_ENTERPRISE3, String sNUMBER_REVOKE_ENTERPRISE4,
            String sNUMBER_REVOKE_PERSONAL1, String sNUMBER_REVOKE_PERSONAL2, String sNUMBER_REVOKE_PERSONAL3,
            String sNUMBER_REVOKE_PERSONAL4, String sNUMBER_CONTROL_ENTERPRISE, String sNUMBER_CONTROL_PERSONAL,
            String sNUMBER_CONTROL_SUM, String sDATE_TIME, REPORT_RECURRING_NEAC[][] rsRecurringIssue, REPORT_RECURRING_NEAC[][] rsRecurringRevoke,
            REPORT_RECURRING_NEAC[][] rsRecurringOperation)
            throws TransformerException, ParserConfigurationException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        CommonFunction com = new CommonFunction();
        
        // ROOT elements
        Document doc = docBuilder.newDocument();
        Element elmRoot = doc.createElement("REPORT_RECURRING");
        doc.appendChild(elmRoot);

        Element fromDateElm = doc.createElement("NUMBER");
        fromDateElm.appendChild(doc.createTextNode(sNUMBER));
        elmRoot.appendChild(fromDateElm);
        
        Element toQUATER = doc.createElement("QUATER");
        toQUATER.appendChild(doc.createTextNode(sQUATER));
        elmRoot.appendChild(toQUATER);

        Element toYEAR = doc.createElement("YEAR");
        toYEAR.appendChild(doc.createTextNode(sYEAR));
        elmRoot.appendChild(toYEAR);
        
        Element listElm = doc.createElement("List");
        elmRoot.appendChild(listElm);
        int sumIssueEnterprise = 0;
        int sumRevokeEnterprise = 0;
        int sumOperationEnterprise = 0;
        int sumIssuePersonal = 0;
        int sumRevokePersonal = 0;
        int sumOperationPersonal = 0;
        int sumIssueStaff = 0;
        int sumRevokeStaff = 0;
        int sumOperationStaff = 0;
        int i = 1;
        if(rsRecurringIssue[0].length > 0) {
            for (REPORT_RECURRING_NEAC item : rsRecurringIssue[0]) {
                sumIssueEnterprise = sumIssueEnterprise + item.TOTAL_ENTERPRISE;
                sumIssuePersonal = sumIssuePersonal + item.TOTAL_PERSONAL;
                sumIssueStaff = sumIssueStaff + item.TOTAL_STAFF;
                Element itemElm = doc.createElement("Issue");
                listElm.appendChild(itemElm);
                Element sttElm = doc.createElement("No");
                sttElm.appendChild(doc.createTextNode(String.valueOf(i++)));
                itemElm.appendChild(sttElm);
                
                Element contentElm = doc.createElement("Content");
                contentElm.appendChild(doc.createTextNode(item.CONTENT));
                itemElm.appendChild(contentElm);
                
                int intEnterprise = item.TOTAL_ENTERPRISE;
                Element enterpriseElm = doc.createElement("Enterprise");
                enterpriseElm.appendChild(doc.createTextNode(com.convertMoneyZero(intEnterprise)));
                itemElm.appendChild(enterpriseElm);
                
                int intPersonal = item.TOTAL_PERSONAL;
                Element personalElm = doc.createElement("Personal");
                personalElm.appendChild(doc.createTextNode(com.convertMoneyZero(intPersonal)));
                itemElm.appendChild(personalElm);
                
                int intStaff = item.TOTAL_STAFF;
                Element staffElm = doc.createElement("Staff");
                staffElm.appendChild(doc.createTextNode(com.convertMoneyZero(intStaff)));
                itemElm.appendChild(staffElm);
                
                int intSum = item.SUM;
                Element sumElm = doc.createElement("Sum");
                sumElm.appendChild(doc.createTextNode(com.convertMoneyZero(intSum)));
                itemElm.appendChild(sumElm);
            }
        }
        Element issueElm = doc.createElement("Issue");
        elmRoot.appendChild(issueElm);
        Element sumIssueEnterElm = doc.createElement("Enterprise");
        sumIssueEnterElm.appendChild(doc.createTextNode(com.convertMoneyZero(sumIssueEnterprise)));
        issueElm.appendChild(sumIssueEnterElm);
        Element sumIssuePersonalElm = doc.createElement("Personal");
        sumIssuePersonalElm.appendChild(doc.createTextNode(com.convertMoneyZero(sumIssuePersonal)));
        issueElm.appendChild(sumIssuePersonalElm);
        Element sumIssueStaffElm = doc.createElement("Staff");
        sumIssueStaffElm.appendChild(doc.createTextNode(com.convertMoneyZero(sumIssueStaff)));
        issueElm.appendChild(sumIssueStaffElm);
        Element sumIssueSumElm = doc.createElement("Sum");
        sumIssueSumElm.appendChild(doc.createTextNode(com.convertMoneyZero(sumIssueEnterprise + sumIssuePersonal + sumIssueStaff)));
        issueElm.appendChild(sumIssueSumElm);
        
        i = 1;
        if(rsRecurringRevoke[0].length > 0) {
            for (REPORT_RECURRING_NEAC item : rsRecurringRevoke[0]) {
                sumRevokeEnterprise = sumRevokeEnterprise + item.TOTAL_ENTERPRISE;
                sumRevokePersonal = sumRevokePersonal + item.TOTAL_PERSONAL;
                sumRevokeStaff = sumRevokeStaff + item.TOTAL_STAFF;
                Element itemElm = doc.createElement("Revoke");
                listElm.appendChild(itemElm);
                Element sttElm = doc.createElement("No");
                sttElm.appendChild(doc.createTextNode(String.valueOf(i++)));
                itemElm.appendChild(sttElm);
                
                Element contentElm = doc.createElement("Content");
                contentElm.appendChild(doc.createTextNode(item.CONTENT));
                itemElm.appendChild(contentElm);
                
                int intEnterprise = item.TOTAL_ENTERPRISE;
                Element enterpriseElm = doc.createElement("Enterprise");
                enterpriseElm.appendChild(doc.createTextNode(com.convertMoneyZero(intEnterprise)));
                itemElm.appendChild(enterpriseElm);
                
                int intPersonal = item.TOTAL_PERSONAL;
                Element personalElm = doc.createElement("Personal");
                personalElm.appendChild(doc.createTextNode(com.convertMoneyZero(intPersonal)));
                itemElm.appendChild(personalElm);
                
                int intStaff = item.TOTAL_STAFF;
                Element staffElm = doc.createElement("Staff");
                staffElm.appendChild(doc.createTextNode(com.convertMoneyZero(intStaff)));
                itemElm.appendChild(staffElm);
                
                int intSum = item.SUM;
                Element sumElm = doc.createElement("Sum");
                sumElm.appendChild(doc.createTextNode(com.convertMoneyZero(intSum)));
                itemElm.appendChild(sumElm);
            }
        }
        Element revokeElm = doc.createElement("Revoke");
        elmRoot.appendChild(revokeElm);
        Element sumRevokeEnterElm = doc.createElement("Enterprise");
        sumRevokeEnterElm.appendChild(doc.createTextNode(com.convertMoneyZero(sumRevokeEnterprise)));
        revokeElm.appendChild(sumRevokeEnterElm);
        Element sumRevokePersonalElm = doc.createElement("Personal");
        sumRevokePersonalElm.appendChild(doc.createTextNode(com.convertMoneyZero(sumRevokePersonal)));
        revokeElm.appendChild(sumRevokePersonalElm);
        Element sumRevokeStaffElm = doc.createElement("Staff");
        sumRevokeStaffElm.appendChild(doc.createTextNode(com.convertMoneyZero(sumRevokeStaff)));
        revokeElm.appendChild(sumRevokeStaffElm);
        Element sumRevokeSumElm = doc.createElement("Sum");
        sumRevokeSumElm.appendChild(doc.createTextNode(com.convertMoneyZero(sumRevokeEnterprise + sumRevokePersonal + sumRevokeStaff)));
        revokeElm.appendChild(sumRevokeSumElm);
        
        i = 1;
        if(rsRecurringOperation[0].length > 0) {
            for (REPORT_RECURRING_NEAC item : rsRecurringOperation[0]) {
                sumOperationEnterprise = sumOperationEnterprise + item.TOTAL_ENTERPRISE;
                sumOperationPersonal = sumOperationPersonal + item.TOTAL_PERSONAL;
                sumOperationStaff = sumOperationStaff + item.TOTAL_STAFF;
                Element itemElm = doc.createElement("Operation");
                listElm.appendChild(itemElm);
                Element sttElm = doc.createElement("No");
                sttElm.appendChild(doc.createTextNode(String.valueOf(i++)));
                itemElm.appendChild(sttElm);
                
                Element contentElm = doc.createElement("Content");
                contentElm.appendChild(doc.createTextNode(item.CONTENT));
                itemElm.appendChild(contentElm);
                
                int intEnterprise = item.TOTAL_ENTERPRISE;
                Element enterpriseElm = doc.createElement("Enterprise");
                enterpriseElm.appendChild(doc.createTextNode(com.convertMoneyZero(intEnterprise)));
                itemElm.appendChild(enterpriseElm);
                
                int intPersonal = item.TOTAL_PERSONAL;
                Element personalElm = doc.createElement("Personal");
                personalElm.appendChild(doc.createTextNode(com.convertMoneyZero(intPersonal)));
                itemElm.appendChild(personalElm);
                
                int intStaff = item.TOTAL_STAFF;
                Element staffElm = doc.createElement("Staff");
                staffElm.appendChild(doc.createTextNode(com.convertMoneyZero(intStaff)));
                itemElm.appendChild(staffElm);
                
                int intSum = item.SUM;
                Element sumElm = doc.createElement("Sum");
                sumElm.appendChild(doc.createTextNode(com.convertMoneyZero(intSum)));
                itemElm.appendChild(sumElm);
            }
        }
        Element operationElm = doc.createElement("Operation");
        elmRoot.appendChild(operationElm);
        Element sumOperationEnterElm = doc.createElement("Enterprise");
        sumOperationEnterElm.appendChild(doc.createTextNode(com.convertMoneyZero(sumOperationEnterprise)));
        operationElm.appendChild(sumOperationEnterElm);
        Element sumOperationPersonalElm = doc.createElement("Personal");
        sumOperationPersonalElm.appendChild(doc.createTextNode(com.convertMoneyZero(sumOperationPersonal)));
        operationElm.appendChild(sumOperationPersonalElm);
        Element sumOperationStaffElm = doc.createElement("Staff");
        sumOperationStaffElm.appendChild(doc.createTextNode(com.convertMoneyZero(sumOperationStaff)));
        operationElm.appendChild(sumOperationStaffElm);
        Element sumOperationSumElm = doc.createElement("Sum");
        sumOperationSumElm.appendChild(doc.createTextNode(com.convertMoneyZero(sumOperationEnterprise + sumOperationPersonal + sumOperationStaff)));
        operationElm.appendChild(sumOperationSumElm);

        Element toNUMBER_ISSUED_ENTERPRISE1 = doc.createElement("NUMBER_ISSUED_ENTERPRISE1");
        toNUMBER_ISSUED_ENTERPRISE1.appendChild(doc.createTextNode(sNUMBER_ISSUED_ENTERPRISE1));
        elmRoot.appendChild(toNUMBER_ISSUED_ENTERPRISE1);

        Element toNUMBER_ISSUED_ENTERPRISE2 = doc.createElement("NUMBER_ISSUED_ENTERPRISE2");
        toNUMBER_ISSUED_ENTERPRISE2.appendChild(doc.createTextNode(sNUMBER_ISSUED_ENTERPRISE2));
        elmRoot.appendChild(toNUMBER_ISSUED_ENTERPRISE2);

        Element toNUMBER_ISSUED_ENTERPRISE3 = doc.createElement("NUMBER_ISSUED_ENTERPRISE3");
        toNUMBER_ISSUED_ENTERPRISE3.appendChild(doc.createTextNode(sNUMBER_ISSUED_ENTERPRISE3));
        elmRoot.appendChild(toNUMBER_ISSUED_ENTERPRISE3);

        Element toNUMBER_ISSUED_ENTERPRISE4 = doc.createElement("NUMBER_ISSUED_ENTERPRISE4");
        toNUMBER_ISSUED_ENTERPRISE4.appendChild(doc.createTextNode(sNUMBER_ISSUED_ENTERPRISE4));
        elmRoot.appendChild(toNUMBER_ISSUED_ENTERPRISE4);

        Element toNUMBER_ISSUED_PERSONAL1 = doc.createElement("NUMBER_ISSUED_PERSONAL1");
        toNUMBER_ISSUED_PERSONAL1.appendChild(doc.createTextNode(sNUMBER_ISSUED_PERSONAL1));
        elmRoot.appendChild(toNUMBER_ISSUED_PERSONAL1);
        
        Element toNUMBER_ISSUED_PERSONAL2 = doc.createElement("NUMBER_ISSUED_PERSONAL2");
        toNUMBER_ISSUED_PERSONAL2.appendChild(doc.createTextNode(sNUMBER_ISSUED_PERSONAL2));
        elmRoot.appendChild(toNUMBER_ISSUED_PERSONAL2);
        
        Element toNUMBER_ISSUED_PERSONAL3 = doc.createElement("NUMBER_ISSUED_PERSONAL3");
        toNUMBER_ISSUED_PERSONAL3.appendChild(doc.createTextNode(sNUMBER_ISSUED_PERSONAL3));
        elmRoot.appendChild(toNUMBER_ISSUED_PERSONAL3);
        
        Element toNUMBER_ISSUED_PERSONAL4 = doc.createElement("NUMBER_ISSUED_PERSONAL4");
        toNUMBER_ISSUED_PERSONAL4.appendChild(doc.createTextNode(sNUMBER_ISSUED_PERSONAL4));
        elmRoot.appendChild(toNUMBER_ISSUED_PERSONAL4);

        Element toNUMBER_REVOKE_ENTERPRISE1 = doc.createElement("NUMBER_REVOKE_ENTERPRISE1");
        toNUMBER_REVOKE_ENTERPRISE1.appendChild(doc.createTextNode(sNUMBER_REVOKE_ENTERPRISE1));
        elmRoot.appendChild(toNUMBER_REVOKE_ENTERPRISE1);
        
        Element toNUMBER_REVOKE_ENTERPRISE2 = doc.createElement("NUMBER_REVOKE_ENTERPRISE2");
        toNUMBER_REVOKE_ENTERPRISE2.appendChild(doc.createTextNode(sNUMBER_REVOKE_ENTERPRISE2));
        elmRoot.appendChild(toNUMBER_REVOKE_ENTERPRISE2);
        
        Element toNUMBER_REVOKE_ENTERPRISE3 = doc.createElement("NUMBER_REVOKE_ENTERPRISE3");
        toNUMBER_REVOKE_ENTERPRISE3.appendChild(doc.createTextNode(sNUMBER_REVOKE_ENTERPRISE3));
        elmRoot.appendChild(toNUMBER_REVOKE_ENTERPRISE3);
        
        Element toNUMBER_REVOKE_ENTERPRISE4 = doc.createElement("NUMBER_REVOKE_ENTERPRISE4");
        toNUMBER_REVOKE_ENTERPRISE4.appendChild(doc.createTextNode(sNUMBER_REVOKE_ENTERPRISE4));
        elmRoot.appendChild(toNUMBER_REVOKE_ENTERPRISE4);

        Element toNUMBER_REVOKE_PERSONAL1 = doc.createElement("NUMBER_REVOKE_PERSONAL1");
        toNUMBER_REVOKE_PERSONAL1.appendChild(doc.createTextNode(sNUMBER_REVOKE_PERSONAL1));
        elmRoot.appendChild(toNUMBER_REVOKE_PERSONAL1);
        
        Element toNUMBER_REVOKE_PERSONAL2 = doc.createElement("NUMBER_REVOKE_PERSONAL2");
        toNUMBER_REVOKE_PERSONAL2.appendChild(doc.createTextNode(sNUMBER_REVOKE_PERSONAL2));
        elmRoot.appendChild(toNUMBER_REVOKE_PERSONAL2);
        
        Element toNUMBER_REVOKE_PERSONAL3 = doc.createElement("NUMBER_REVOKE_PERSONAL3");
        toNUMBER_REVOKE_PERSONAL3.appendChild(doc.createTextNode(sNUMBER_REVOKE_PERSONAL3));
        elmRoot.appendChild(toNUMBER_REVOKE_PERSONAL3);
        
        Element toNUMBER_REVOKE_PERSONAL4 = doc.createElement("NUMBER_REVOKE_PERSONAL4");
        toNUMBER_REVOKE_PERSONAL4.appendChild(doc.createTextNode(sNUMBER_REVOKE_PERSONAL4));
        elmRoot.appendChild(toNUMBER_REVOKE_PERSONAL4);

        Element toNUMBER_CONTROL_ENTERPRISE = doc.createElement("NUMBER_CONTROL_ENTERPRISE");
        toNUMBER_CONTROL_ENTERPRISE.appendChild(doc.createTextNode(sNUMBER_CONTROL_ENTERPRISE));
        elmRoot.appendChild(toNUMBER_CONTROL_ENTERPRISE);
        
        Element toNUMBER_CONTROL_PERSONAL = doc.createElement("NUMBER_CONTROL_PERSONAL");
        toNUMBER_CONTROL_PERSONAL.appendChild(doc.createTextNode(sNUMBER_CONTROL_PERSONAL));
        elmRoot.appendChild(toNUMBER_CONTROL_PERSONAL);
        
        Element toNUMBER_CONTROL_SUM = doc.createElement("NUMBER_CONTROL_SUM");
        toNUMBER_CONTROL_SUM.appendChild(doc.createTextNode(sNUMBER_CONTROL_SUM));
        elmRoot.appendChild(toNUMBER_CONTROL_SUM);

        Element tosDATE_TIME = doc.createElement("DATE_TIME");
        tosDATE_TIME.appendChild(doc.createTextNode(sDATE_TIME));
        elmRoot.appendChild(tosDATE_TIME);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);

        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        transformer.transform(source, result);

        String xml = writer.toString();

        return xml;
    }

    public static String createXMLReportControlFinal(String sNUMBER, String sQUATER, String sYEAR, String sCONTENT1,
            String sNUMBER_CERT1, String sCONTENT2, String sNUMBER_CERT2, String sCONTENT3, String sNUMBER_CERT3, String sDATE_TIME)
            throws TransformerException, ParserConfigurationException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        // ROOT elements
        Document doc = docBuilder.newDocument();
        Element elmRoot = doc.createElement("REPORT_CONTROL");
        doc.appendChild(elmRoot);

        Element fromDateElm = doc.createElement("NUMBER");
        fromDateElm.appendChild(doc.createTextNode(sNUMBER));
        elmRoot.appendChild(fromDateElm);

        Element toQUATER = doc.createElement("QUATER");
        toQUATER.appendChild(doc.createTextNode(sQUATER));
        elmRoot.appendChild(toQUATER);

        Element toYEAR = doc.createElement("YEAR");
        toYEAR.appendChild(doc.createTextNode(sYEAR));
        elmRoot.appendChild(toYEAR);

        Element toCONTENT1 = doc.createElement("CONTENT1");
        toCONTENT1.appendChild(doc.createTextNode(sCONTENT1));
        elmRoot.appendChild(toCONTENT1);

        Element toNUMBER_CERT1 = doc.createElement("NUMBER_CERT1");
        toNUMBER_CERT1.appendChild(doc.createTextNode(sNUMBER_CERT1));
        elmRoot.appendChild(toNUMBER_CERT1);

        Element toCONTENT2 = doc.createElement("CONTENT2");
        toCONTENT2.appendChild(doc.createTextNode(sCONTENT2));
        elmRoot.appendChild(toCONTENT2);

        Element toNUMBER_CERT2 = doc.createElement("NUMBER_CERT2");
        toNUMBER_CERT2.appendChild(doc.createTextNode(sNUMBER_CERT2));
        elmRoot.appendChild(toNUMBER_CERT2);

        Element toCONTENT3 = doc.createElement("CONTENT3");
        toCONTENT3.appendChild(doc.createTextNode(sCONTENT3));
        elmRoot.appendChild(toCONTENT3);

        Element toNUMBER_CERT3 = doc.createElement("NUMBER_CERT3");
        toNUMBER_CERT3.appendChild(doc.createTextNode(sNUMBER_CERT3));
        elmRoot.appendChild(toNUMBER_CERT3);

        Element tosDATE_TIME = doc.createElement("DATE_TIME");
        tosDATE_TIME.appendChild(doc.createTextNode(sDATE_TIME));
        elmRoot.appendChild(tosDATE_TIME);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);

        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        transformer.transform(source, result);

        String xml = writer.toString();

        return xml;
    }
    
    public static String createXMLConfirmInfo(String customerName, String taxCode, String representive,
            String role, String certSN, String thoiGianDiaDiem) throws ParserConfigurationException, TransformerException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        // ROOT elements
        Document doc = docBuilder.newDocument();
        Element elmRoot = doc.createElement("Confirm");
        doc.appendChild(elmRoot);

        // TenKH elements
        Element elmCusName = doc.createElement("CustomerName");
        elmCusName.appendChild(doc.createTextNode(customerName));
        elmRoot.appendChild(elmCusName);

        Element elmTaxCode = doc.createElement("TaxCode");
        elmTaxCode.appendChild(doc.createTextNode(taxCode));
        elmRoot.appendChild(elmTaxCode);

        Element elmRepresentive = doc.createElement("Representive");
        elmRepresentive.appendChild(doc.createTextNode(representive));
        elmRoot.appendChild(elmRepresentive);

        Element elmRole = doc.createElement("Role");
        elmRole.appendChild(doc.createTextNode(role));
        elmRoot.appendChild(elmRole);

        Element mstSN = doc.createElement("CertSN");
        mstSN.appendChild(doc.createTextNode(certSN));
        elmRoot.appendChild(mstSN);
        
        Element mstTime = doc.createElement("ThoiGianDiaDiem");
        mstTime.appendChild(doc.createTextNode(thoiGianDiaDiem));
        elmRoot.appendChild(mstTime);

        // write the content into xml file
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        transformer.transform(source, result);
        String xml = writer.toString();
        return xml;
    }
    
    public static String createXMLConfirmInfoIsAddress(String customerName, String taxCode, String representive,
            String role, String certSN, String thoiGianDiaDiem, String sAddress, String cmnd,
            String sPIDPlace, String sPIDDate, String sPhone, String sEmail, String sPhoneNewtel, String sDistrict, String sProvince,
            String sOrganization, String sUnit, String isCertType, String thoigiansudung, String sSubjectDN, String sIssuerDN)
        throws ParserConfigurationException, TransformerException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        // ROOT elements
        Document doc = docBuilder.newDocument();
        Element elmRoot = doc.createElement("Confirm");
        doc.appendChild(elmRoot);

        // TenKH elements
        Element elmCusName = doc.createElement("CustomerName");
        elmCusName.appendChild(doc.createTextNode(customerName));
        elmRoot.appendChild(elmCusName);
        
        Element elmCusAddress = doc.createElement("CustomerAddress");
        elmCusAddress.appendChild(doc.createTextNode(sAddress));
        elmRoot.appendChild(elmCusAddress);

        Element elmTaxCode = doc.createElement("TaxCode");
        elmTaxCode.appendChild(doc.createTextNode(taxCode));
        elmRoot.appendChild(elmTaxCode);

        Element elmCMND = doc.createElement("CMND");
        elmCMND.appendChild(doc.createTextNode(cmnd));
        elmRoot.appendChild(elmCMND);
        
        Element elmPlace = doc.createElement("IssuedBy");
        elmPlace.appendChild(doc.createTextNode(sPIDPlace));
        elmRoot.appendChild(elmPlace);
        
        Element elmPIDDate = doc.createElement("Date");
        elmPIDDate.appendChild(doc.createTextNode(sPIDDate));
        elmRoot.appendChild(elmPIDDate);

        Element elmRepresentive = doc.createElement("Representive");
        elmRepresentive.appendChild(doc.createTextNode(representive));
        elmRoot.appendChild(elmRepresentive);

        Element elmRole = doc.createElement("Role");
        elmRole.appendChild(doc.createTextNode(role));
        elmRoot.appendChild(elmRole);
        
        Element elmTetePhone = doc.createElement("TelephoneNumber");
        elmTetePhone.appendChild(doc.createTextNode(sPhone));
        elmRoot.appendChild(elmTetePhone);
        
        Element elmPhone = doc.createElement("PhoneNumber");
        elmPhone.appendChild(doc.createTextNode(sPhoneNewtel));
        elmRoot.appendChild(elmPhone);
        
        Element elmEmail = doc.createElement("Email");
        elmEmail.appendChild(doc.createTextNode(sEmail));
        elmRoot.appendChild(elmEmail);

        Element mstSN = doc.createElement("CertSN");
        mstSN.appendChild(doc.createTextNode(certSN));
        elmRoot.appendChild(mstSN);
        
        Element mstTime = doc.createElement("ThoiGianDiaDiem");
        mstTime.appendChild(doc.createTextNode(thoiGianDiaDiem));
        elmRoot.appendChild(mstTime);
        
        Element elmDistrict = doc.createElement("District");
        elmDistrict.appendChild(doc.createTextNode(sDistrict));
        elmRoot.appendChild(elmDistrict);
        
        Element elmProvince = doc.createElement("CityProvince");
        elmProvince.appendChild(doc.createTextNode(sProvince));
        elmRoot.appendChild(elmProvince);
        
        Element elmOrganization = doc.createElement("OrganizationName");
        elmOrganization.appendChild(doc.createTextNode(sOrganization));
        elmRoot.appendChild(elmOrganization);
        
        Element elmSubjectDN = doc.createElement("SubjectDN");
        elmSubjectDN.appendChild(doc.createTextNode(sSubjectDN));
        elmRoot.appendChild(elmSubjectDN);
        
        Element elmIssuerDN = doc.createElement("IssuerDN");
        elmIssuerDN .appendChild(doc.createTextNode(sIssuerDN));
        elmRoot.appendChild(elmIssuerDN);
        
        Element elmUnit = doc.createElement("Unit");
        elmUnit.appendChild(doc.createTextNode(sUnit));
        elmRoot.appendChild(elmUnit);
        
        Element elmCertType = doc.createElement("isCertificateType");
        elmCertType.appendChild(doc.createTextNode(isCertType));
        elmRoot.appendChild(elmCertType);
        
        Element elmTimeUse = doc.createElement("ThoiGianSuDung");
        elmTimeUse.appendChild(doc.createTextNode(thoigiansudung));
        elmRoot.appendChild(elmTimeUse);

        // write the content into xml file
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        transformer.transform(source, result);
        String xml = writer.toString();
        return xml;
    }
    
    public static String createXMLRegistrationRedirect(String sNameCN, String sChucVu, String sCMNDHC, String NgayCap,
        String NoiCap, String DiaChiCN, String MobileCN, String EmailCN, String sNameDN, String MST, String DiaChiDN,
        String MobileDN, String EmailDN, String isCaNhan, String isToChuc, String isCaNhanThuocToChuc, String isCapMoi,
        String isGiaHan, String isCapLai, String isTamDung, String isKhoiPhuc, String isThuHoi, String is1Nam, String is2Nam,
        String is3Nam, String is4Nam, String CertSN, String ThoiGianDiaDiem, String isHSM, String isToken,
        String sDLPL, String sChucVuDN, String sCMNDHCDN, String NgayCapDN, String NoiCapDN)
        throws ParserConfigurationException, TransformerException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        // ROOT elements
        Document doc = docBuilder.newDocument();
        Element elmRoot = doc.createElement("CCTSDN");
        doc.appendChild(elmRoot);

        // ThongTinCaNhan
        Element thongTinCaNhanElm = doc.createElement("ThongTinCaNhan");
        elmRoot.appendChild(thongTinCaNhanElm);

        // TenKH elements
        Element hoTenElm = doc.createElement("HoTen");
        hoTenElm.appendChild(doc.createTextNode(sNameCN));
        thongTinCaNhanElm.appendChild(hoTenElm);

        Element isChucVu = doc.createElement("ChucVu");
        isChucVu.appendChild(doc.createTextNode(sChucVu));
        thongTinCaNhanElm.appendChild(isChucVu);

        Element isCMNDHC = doc.createElement("CMNDHC");
        isCMNDHC.appendChild(doc.createTextNode(sCMNDHC));
        thongTinCaNhanElm.appendChild(isCMNDHC);

        Element ngayCapElm = doc.createElement("NgayCap");
        ngayCapElm.appendChild(doc.createTextNode(NgayCap));
        thongTinCaNhanElm.appendChild(ngayCapElm);

        Element noiCapElm = doc.createElement("NoiCap");
        noiCapElm.appendChild(doc.createTextNode(NoiCap));
        thongTinCaNhanElm.appendChild(noiCapElm);

        Element diaChiCNElm = doc.createElement("DiaChi");
        diaChiCNElm.appendChild(doc.createTextNode(DiaChiCN));
        thongTinCaNhanElm.appendChild(diaChiCNElm);

        Element dienThoaiElm = doc.createElement("Mobile");
        dienThoaiElm.appendChild(doc.createTextNode(MobileCN));
        thongTinCaNhanElm.appendChild(dienThoaiElm);
        Element EmailElm = doc.createElement("Email");
        EmailElm.appendChild(doc.createTextNode(EmailCN));
        thongTinCaNhanElm.appendChild(EmailElm);
        
        // ThongTinDN
        Element thongTinToChucElm = doc.createElement("ThongTinToChuc");
        elmRoot.appendChild(thongTinToChucElm);

        // TenToChuc elements
        Element TenToChucElm = doc.createElement("TenGiaoDich");
        TenToChucElm.appendChild(doc.createTextNode(sNameDN));
        thongTinToChucElm.appendChild(TenToChucElm);
        
        Element HoTenDDPLElm = doc.createElement("NguoiDaiDien");
        HoTenDDPLElm.appendChild(doc.createTextNode(sDLPL));
        thongTinToChucElm.appendChild(HoTenDDPLElm);
        
        Element isChucVuDN = doc.createElement("ChucVu");
        isChucVuDN.appendChild(doc.createTextNode(sChucVuDN));
        thongTinToChucElm.appendChild(isChucVuDN);

        Element isCMNDHCDN = doc.createElement("CMNDHC");
        isCMNDHCDN.appendChild(doc.createTextNode(sCMNDHCDN));
        thongTinToChucElm.appendChild(isCMNDHCDN);

        Element ngayCapElmDN = doc.createElement("NgayCap");
        ngayCapElmDN.appendChild(doc.createTextNode(NgayCapDN));
        thongTinToChucElm.appendChild(ngayCapElmDN);

        Element noiCapElmDN = doc.createElement("NoiCap");
        noiCapElmDN.appendChild(doc.createTextNode(NoiCapDN));
        thongTinToChucElm.appendChild(noiCapElmDN);
        
        Element TaxcodeElm = doc.createElement("MST");
        TaxcodeElm.appendChild(doc.createTextNode(MST));
        thongTinToChucElm.appendChild(TaxcodeElm);
        Element DiaChiElm = doc.createElement("DiaChi");
        DiaChiElm.appendChild(doc.createTextNode(DiaChiDN));
        thongTinToChucElm.appendChild(DiaChiElm);
        Element DienThoaiDNElm = doc.createElement("Mobile");
        DienThoaiDNElm.appendChild(doc.createTextNode(MobileDN));
        thongTinToChucElm.appendChild(DienThoaiDNElm);
        Element EmailDNElm = doc.createElement("Email");
        EmailDNElm.appendChild(doc.createTextNode(EmailDN));
        thongTinToChucElm.appendChild(EmailDNElm);
        
        // DoiTuongSuDung
        Element DoiTuongSuDungElm = doc.createElement("DoiTuongSuDung");
        elmRoot.appendChild(DoiTuongSuDungElm);
        Element isCaNhanElm = doc.createElement("isCaNhan");
        isCaNhanElm.appendChild(doc.createTextNode(isCaNhan));
        DoiTuongSuDungElm.appendChild(isCaNhanElm);
        Element isToChucElm = doc.createElement("isToChuc");
        isToChucElm.appendChild(doc.createTextNode(isToChuc));
        DoiTuongSuDungElm.appendChild(isToChucElm);
        Element isCaNhanThuocToChucElm = doc.createElement("isCaNhanThuocToChuc");
        isCaNhanThuocToChucElm.appendChild(doc.createTextNode(isCaNhanThuocToChuc));
        DoiTuongSuDungElm.appendChild(isCaNhanThuocToChucElm);
        
        // DichVuYeuCau
        Element DichVuYeuCauElm = doc.createElement("DichVuYeuCau");
        elmRoot.appendChild(DichVuYeuCauElm);
        Element isCapMoiElm = doc.createElement("isCapMoi");
        isCapMoiElm.appendChild(doc.createTextNode(isCapMoi));
        DichVuYeuCauElm.appendChild(isCapMoiElm);
        
        Element isGiaHanElm = doc.createElement("isGiaHan");
        isGiaHanElm.appendChild(doc.createTextNode(isGiaHan));
        DichVuYeuCauElm.appendChild(isGiaHanElm);
        
        Element isCapLaiElm = doc.createElement("isCapLai");
        isCapLaiElm.appendChild(doc.createTextNode(isCapLai));
        DichVuYeuCauElm.appendChild(isCapLaiElm);
        
        Element isTamDungElm = doc.createElement("isTamDung");
        isTamDungElm.appendChild(doc.createTextNode(isTamDung));
        DichVuYeuCauElm.appendChild(isTamDungElm);
        
        Element isKhoiPhucElm = doc.createElement("isKhoiPhuc");
        isKhoiPhucElm.appendChild(doc.createTextNode(isKhoiPhuc));
        DichVuYeuCauElm.appendChild(isKhoiPhucElm);
        
        Element isThuHoiElm = doc.createElement("isThuHoi");
        isThuHoiElm.appendChild(doc.createTextNode(isThuHoi));
        DichVuYeuCauElm.appendChild(isThuHoiElm);
        
        // ThoiGianSuDung
        Element ThoiGianSuDungElm = doc.createElement("ThoiGianSuDung");
        elmRoot.appendChild(ThoiGianSuDungElm);
        Element is1NamElm = doc.createElement("is1Nam");
        is1NamElm.appendChild(doc.createTextNode(is1Nam));
        ThoiGianSuDungElm.appendChild(is1NamElm);
        
        Element is2NamElm = doc.createElement("is2Nam");
        is2NamElm.appendChild(doc.createTextNode(is2Nam));
        ThoiGianSuDungElm.appendChild(is2NamElm);
        
        Element is3NamElm = doc.createElement("is3Nam");
        is3NamElm.appendChild(doc.createTextNode(is3Nam));
        ThoiGianSuDungElm.appendChild(is3NamElm);
        
        Element is4NamElm = doc.createElement("is4Nam");
        is4NamElm.appendChild(doc.createTextNode(is4Nam));
        ThoiGianSuDungElm.appendChild(is4NamElm);
        
        // formfactor
        Element formfactorElm = doc.createElement("ThietBi");
        elmRoot.appendChild(formfactorElm);
        Element isHSMElm = doc.createElement("HSM");
        isHSMElm.appendChild(doc.createTextNode(isHSM));
        formfactorElm.appendChild(isHSMElm);
        
        Element is2TokenElm = doc.createElement("usbToken");
        is2TokenElm.appendChild(doc.createTextNode(isToken));
        formfactorElm.appendChild(is2TokenElm);
        
        // CertSN
        Element CertSNElm = doc.createElement("CertSN");
        CertSNElm.appendChild(doc.createTextNode(CertSN));
        elmRoot.appendChild(CertSNElm);
        // ThoiGianDiaDiem
        Element ThoiGianDiaDiemElm = doc.createElement("ThoiGianDiaDiem");
        ThoiGianDiaDiemElm.appendChild(doc.createTextNode(ThoiGianDiaDiem));
        elmRoot.appendChild(ThoiGianDiaDiemElm);

        // write the content into xml file
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);

        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        transformer.transform(source, result);
        String xml = writer.toString();
        return xml;
    }
    
    public static String createXMLChangeInfoRedirect(String sHoTenCTS, String sMST, String sCMNDHC, String sCertSN,
        String sTenKhachHangCu, String sTenKhachHangMoi, String sCMNDHCCu, String sCMNDHCMoi, String sEmailCu, String sEmailMoi,
        String sSDTCu, String sSDTMoi,
        String sDiaChiCu, String sDiaChiMoi, String sHoTenCN, String sChucVu, String sDienThoai, String sEmail, String ThoiGianDiaDiem)
        throws ParserConfigurationException, TransformerException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        // ROOT elements
        Document doc = docBuilder.newDocument();
        Element elmRoot = doc.createElement("CCTSCN");
        doc.appendChild(elmRoot);
        // CertSN
        Element HoTenElm = doc.createElement("HoTen");
        HoTenElm.appendChild(doc.createTextNode(sHoTenCTS));
        elmRoot.appendChild(HoTenElm);
        // CertSN
        Element MSTElm = doc.createElement("MST");
        MSTElm.appendChild(doc.createTextNode(sMST));
        elmRoot.appendChild(MSTElm);
        // CertSN
        Element CMNDHCElm = doc.createElement("CMNDHC");
        CMNDHCElm.appendChild(doc.createTextNode(sCMNDHC));
        elmRoot.appendChild(CMNDHCElm);
        // CertSN
        Element CertSNElm = doc.createElement("CertSN");
        CertSNElm.appendChild(doc.createTextNode(sCertSN));
        elmRoot.appendChild(CertSNElm);

        // ThongTinCaNhan
        Element thongTinCTSElm = doc.createElement("ThongTinThayDoi");
        elmRoot.appendChild(thongTinCTSElm);

        // TenKH elements
        Element TenKHCuElm = doc.createElement("TenKhachHangCu");
        TenKHCuElm.appendChild(doc.createTextNode(sTenKhachHangCu));
        thongTinCTSElm.appendChild(TenKHCuElm);
        // TenKH elements
        Element TenKHMoiElm = doc.createElement("TenKhachHangMoi");
        TenKHMoiElm.appendChild(doc.createTextNode(sTenKhachHangMoi));
        thongTinCTSElm.appendChild(TenKHMoiElm);
        
        Element DCCuElm = doc.createElement("DiaChiCu");
        DCCuElm.appendChild(doc.createTextNode(sDiaChiCu));
        thongTinCTSElm.appendChild(DCCuElm);
        // TenKH elements
        Element DCMoiElm = doc.createElement("DiaChiMoi");
        DCMoiElm.appendChild(doc.createTextNode(sDiaChiMoi));
        thongTinCTSElm.appendChild(DCMoiElm);
        // TenKH elements
        Element CMNDHCCuElm = doc.createElement("CMNDHCCu");
        CMNDHCCuElm.appendChild(doc.createTextNode(sCMNDHCCu));
        thongTinCTSElm.appendChild(CMNDHCCuElm);
        // TenKH elements
        Element CMNDHCMoiElm = doc.createElement("CMNDHCMoi");
        CMNDHCMoiElm.appendChild(doc.createTextNode(sCMNDHCMoi));
        thongTinCTSElm.appendChild(CMNDHCMoiElm);
        // TenKH elements
        Element EmailCuElm = doc.createElement("EmailCu");
        EmailCuElm.appendChild(doc.createTextNode(sEmailCu));
        thongTinCTSElm.appendChild(EmailCuElm);
        // TenKH elements
        Element EmailMoiElm = doc.createElement("EmailMoi");
        EmailMoiElm.appendChild(doc.createTextNode(sEmailMoi));
        thongTinCTSElm.appendChild(EmailMoiElm);
        // TenKH elements
        Element SDTCuElm = doc.createElement("SDTCu");
        SDTCuElm.appendChild(doc.createTextNode(sSDTCu));
        thongTinCTSElm.appendChild(SDTCuElm);
        // TenKH elements
        Element SDTMoiElm = doc.createElement("SDTMoi");
        SDTMoiElm.appendChild(doc.createTextNode(sSDTMoi));
        thongTinCTSElm.appendChild(SDTMoiElm);
        
        // ThongTinDN
        Element ThongTinLienHeElm = doc.createElement("ThongTinNguoiNhan");
        elmRoot.appendChild(ThongTinLienHeElm);
        Element HoTenCNElm = doc.createElement("HoTen");
        HoTenCNElm.appendChild(doc.createTextNode(sHoTenCN));
        ThongTinLienHeElm.appendChild(HoTenCNElm);
        Element ChucVuElm = doc.createElement("ChucVu");
        ChucVuElm.appendChild(doc.createTextNode(sChucVu));
        ThongTinLienHeElm.appendChild(ChucVuElm);
        Element DienThoaiElm = doc.createElement("DienThoai");
        DienThoaiElm.appendChild(doc.createTextNode(sDienThoai));
        ThongTinLienHeElm.appendChild(DienThoaiElm);
        Element EmailElm = doc.createElement("Email");
        EmailElm.appendChild(doc.createTextNode(sEmail));
        ThongTinLienHeElm.appendChild(EmailElm);
        // ThoiGianDiaDiem
        Element ThoiGianDiaDiemElm = doc.createElement("ThoiGianDiaDiem");
        ThoiGianDiaDiemElm.appendChild(doc.createTextNode(ThoiGianDiaDiem));
        elmRoot.appendChild(ThoiGianDiaDiemElm);

        // write the content into xml file
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);

        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        transformer.transform(source, result);
        String xml = writer.toString();
        return xml;
    }
    
}
