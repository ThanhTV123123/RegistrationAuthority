/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ra.process;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import vn.ra.utility.Config;
import vn.ra.utility.Definitions;
import vn.ra.utility.MimeTypeUtils;
/**
 *
 * @author USER
 */
public class DocxDocumentMergerAndConverter {
    private static final Logger log = Logger.getLogger(DocxDocumentMergerAndConverter.class);
    //    /**
//     * Takes file path as input and returns the stream opened on it
//     *
//     * @param filePath
//     * @return
//     * @throws IOException
//     */
    public static String readDocFilePath(String fileName) {
        String output = "";
        try {
            File file = new File(fileName);
            FileInputStream fis = new FileInputStream(file.getAbsolutePath());
            HWPFDocument doc = new HWPFDocument(fis);
            WordExtractor we = new WordExtractor(doc);
            String[] paragraphs = we.getParagraphText();
            for (String para : paragraphs) {
                output = output + "\n" + para.toString() + "\n";
            }
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }

    public static String readDocFileBytes(byte[] byteWord) {
        String output = "";
        try {
            if (byteWord != null) {
                HWPFDocument doc = new HWPFDocument(new ByteArrayInputStream(byteWord));
                WordExtractor we = new WordExtractor(doc);
                String[] paragraphs = we.getParagraphText();
                for (String para : paragraphs) {
                    output = output + "\n" + para.toString() + "\n";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }

    public static String readDocxFilePath(String fileName) {
        String output = "";
        try {
            File file = new File(fileName);
            FileInputStream fis = new FileInputStream(file.getAbsolutePath());
            XWPFDocument document = new XWPFDocument(fis);
            List<XWPFParagraph> paragraphs = document.getParagraphs();
            for (XWPFParagraph para : paragraphs) {
                output = output + "\n" + para.getText() + "\n";
            }
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }

    public static String readDocxFileBytes(byte[] byteWord) {
        String output = "";
        try {
            if (byteWord != null) {
                XWPFDocument document = new XWPFDocument(new ByteArrayInputStream(byteWord));
                List<XWPFParagraph> paragraphs = document.getParagraphs();
                for (XWPFParagraph para : paragraphs) {
                    output = output + "\n" + para.getText() + "\n";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }
    
    public static void writePdfFilePath(String output, String[] sViewPath) throws FileNotFoundException, DocumentException {
        Config conf = new Config();
        String pPathURL = conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER);
        String sFileName = CommonFunction.getDateFormat() + Definitions.CONFIG_EXPORT_FILENAME_EXTENTION_PDF;
        String strURLPath = pPathURL + sFileName;        
        File file = new File(strURLPath);
        FileOutputStream fileout = new FileOutputStream(file);
        Document document = new Document();
        PdfWriter.getInstance(document, fileout);
        document.addAuthor("TMS-RA");
        document.addTitle("TMS-RA System");
        document.open();
        String[] splitter = output.split("\\n");
        for (int i = 0; i < splitter.length; i++) {
            Chunk chunk = new Chunk(splitter[i]);
            Font font = new Font();
            font.setStyle(Font.UNDERLINE);
            font.setStyle(Font.ITALIC);
            chunk.setFont(font);
            document.add(chunk);
            Paragraph paragraph = new Paragraph();
            paragraph.add("");
            document.add(paragraph);
        }
        sViewPath[0] = strURLPath;
        document.close();
    }

    public static byte[] writePdfFileBytes(String output) throws FileNotFoundException, DocumentException {
//        byte[] pdfBytes;
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        File file = new File("D:\\Common Test\\convert_word_pdf2.pdf");
//        FileOutputStream fileout = new FileOutputStream(file);
        Document document = new Document();
//        PdfWriter.getInstance(document, fileout);
//        PdfWriter.getInstance(document, byteArrayOutputStream);
        PdfWriter writer = PdfWriter.getInstance(document, byteArrayOutputStream);
        document.addAuthor("TMS-RA");
        document.addTitle("TMS-RA System");
        document.open();
        String[] splitter = output.split("\\n");
        for (String splitter1 : splitter) {
            Chunk chunk = new Chunk(splitter1);
            Font font = new Font();
            font.setStyle(Font.UNDERLINE);
            font.setStyle(Font.ITALIC);
            chunk.setFont(font);
            document.add(chunk);
            Paragraph paragraph = new Paragraph();
            paragraph.add("");
            document.add(paragraph);
        }
//        pdfBytes = byteArrayOutputStream.toByteArray();
        document.close();
        return byteArrayOutputStream.toByteArray();
    }
    
    public static byte[] byteWordToPDF(byte[] byteInput, String sMimeType, String[] sMimeTypeOut)
    {
        byte[] byteOutput = null;
//        String[] sViewPath = new String[1];
        sMimeTypeOut[0] = sMimeType;
        try
        {
            if(byteInput != null)
            {
                String sExtFile = MimeTypeUtils.getDefaultExt(sMimeType);
                if(sExtFile.toUpperCase().equals("DOC") || sExtFile.toUpperCase().equals("DOCX"))
                {
                    if ("DOCX".equals(sExtFile.toUpperCase())) {
                        String output = readDocxFileBytes(byteInput);
                        byteOutput = writePdfFileBytes(output);
//                        writePdfFilePath(output, sViewPath);
                    } else {
                        String output = readDocFileBytes(byteInput);
                        byteOutput = writePdfFileBytes(output);
//                        writePdfFilePath(output, sViewPath);
                    }
                    sMimeTypeOut[0] = MimeTypeUtils.MIME_APPLICATION_PDF;
//                    if(!"".equals(sViewPath[0])) {
//                        byteOutput = readBytesFromFile(sViewPath[0].trim());
//                    }
                } else {
                    byteOutput = byteInput;
                }
            }
            return byteOutput;
        } catch(DocumentException | FileNotFoundException ex)
        {
            CommonFunction.LogExceptionServlet(log, ex.getMessage(), ex);
            return null;
        } finally {
//            if(new File(sViewPath[0].trim()).exists()) {
//                CommonFunction.LogDebugString(log, "File Path", sViewPath[0].trim());
//                //new File(sViewPath[0].trim()).delete();
//            }
        }
    }
    
    private static byte[] readBytesFromFile(String filePath)
    {
        FileInputStream fileInputStream = null;
        byte[] bytesArray = null;
        try {
            File file = new File(filePath);
            bytesArray = new byte[(int) file.length()];
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(bytesArray);
        } catch (IOException ex) {
            CommonFunction.LogExceptionServlet(log, ex.getMessage(), ex);
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException ex) {
                    CommonFunction.LogExceptionServlet(log, ex.getMessage(), ex);
                }
            }

        }
        return bytesArray;
    }
    
//    public static void main(String[] args) throws FileNotFoundException, DocumentException {
//        String ext = FilenameUtils.getExtension("D:\\Common Test\\convert_word.docx");
//        String output = "";
//        if ("docx".equalsIgnoreCase(ext)) {
//            output = readDocxFilePath("D:\\Common Test\\convert_word.docx");
//        } else if ("doc".equalsIgnoreCase(ext)) {
//            output = readDocFilePath("D:\\Common Test\\convert_word.doc");
//        } else {
//            System.out.println("INVALID FILE TYPE. ONLY .doc and .docx are permitted.");
//        }
//        writePdfFilePath(output);
//    }

//    public InputStream loadDocumentAsStream(String filePath) throws IOException {
//        //URL url =new File(filePath).toURL();
//        URL url = new File(filePath).toURI().toURL();
//        InputStream documentTemplateAsStream = null;
//        documentTemplateAsStream = url.openStream();
//        return documentTemplateAsStream;
//    }
//
//    /**
//     * Loads the docx report
//     *
//     * @param documentTemplateAsStream
//     * @param freemarkerOrVelocityTemplateKind
//     * @return
//     * @throws IOException
//     * @throws XDocReportException
//     */
//    public IXDocReport loadDocumentAsIDocxReport(InputStream documentTemplateAsStream, TemplateEngineKind freemarkerOrVelocityTemplateKind) throws IOException, XDocReportException {
//        IXDocReport xdocReport = XDocReportRegistry.getRegistry().loadReport(documentTemplateAsStream, freemarkerOrVelocityTemplateKind);
//        return xdocReport;
//    }
//
//    /**
//     * Takes the IXDocReport instance, creates IContext instance out of it and
//     * puts variables in the context
//     *
//     * @param report
//     * @param variablesToBeReplaced
//     * @return
//     * @throws XDocReportException
//     */
//    public IContext replaceVariabalesInTemplateOtherThanImages(IXDocReport report, Map<String, Object> variablesToBeReplaced) throws XDocReportException {
//        IContext context = report.createContext();
//        for (Map.Entry<String, Object> variable : variablesToBeReplaced.entrySet()) {
//            context.put(variable.getKey(), variable.getValue());
//        }
//        return context;
//    }
//
//    /**
//     * Takes Map of image variable name and fileptah of the image to be
//     * replaced. Creates IImageprovides and adds the variable in context
//     *
//     * @param report
//     * @param variablesToBeReplaced
//     * @param context
//     */
//    public void replaceImagesVariabalesInTemplate(IXDocReport report, Map<String, String> variablesToBeReplaced, IContext context) {
//
//        FieldsMetadata metadata = new FieldsMetadata();
//        for (Map.Entry<String, String> variable : variablesToBeReplaced.entrySet()) {
//            metadata.addFieldAsImage(variable.getKey());
//            context.put(variable.getKey(), new FileImageProvider(new File(variable.getValue()), true));
//        }
//        report.setFieldsMetadata(metadata);
//
//    }
//
//    /**
//     * Generates byte array as output from merged template
//     *
//     * @param report
//     * @param context
//     * @return
//     * @throws XDocReportException
//     * @throws IOException
//     */
//    public byte[] generateMergedOutput(IXDocReport report, IContext context) throws XDocReportException, IOException {
//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//        report.process(context, outputStream);
//        return outputStream.toByteArray();
//    }
//
//    /**
//     * Takes inputs and returns merged output as byte[]
//     *
//     * @param templatePath
//     * @param templateEngineKind
//     * @param nonImageVariableMap
//     * @param imageVariablesWithPathMap
//     * @return
//     * @throws IOException
//     * @throws XDocReportException
//     */
//    public byte[] mergeAndGenerateOutput(String templatePath, TemplateEngineKind templateEngineKind, Map<String, Object> nonImageVariableMap, Map<String, String> imageVariablesWithPathMap) throws IOException, XDocReportException {
//        InputStream inputStream = loadDocumentAsStream(templatePath);
//        IXDocReport xdocReport = loadDocumentAsIDocxReport(inputStream, templateEngineKind);
//        IContext context = replaceVariabalesInTemplateOtherThanImages(xdocReport, nonImageVariableMap);
//        replaceImagesVariabalesInTemplate(xdocReport, imageVariablesWithPathMap, context);
//        byte[] mergedOutput = generateMergedOutput(xdocReport, context);
//        return mergedOutput;
//    }
//
//    /**
//     * Generates byte array as pdf output from merged template
//     *
//     * @param docxBytes
//     * @return
//     * @throws XDocReportException
//     * @throws IOException
//     * @throws Docx4JException
//     */
//    public byte[] generatePDFOutputFromDocx(byte[] docxBytes) throws XDocReportException, IOException, Docx4JException {
//
//        ByteArrayOutputStream pdfByteOutputStream = new ByteArrayOutputStream();
//        WordprocessingMLPackage wordprocessingMLPackage = null;
//
//        wordprocessingMLPackage = WordprocessingMLPackage.load(new ByteArrayInputStream(docxBytes));
//        PdfSettings pdfSettings = new PdfSettings();
//        PdfConversion docx4jViaXSLFOconverter = new org.docx4j.convert.out.pdf.viaXSLFO.Conversion(wordprocessingMLPackage);
//        docx4jViaXSLFOconverter.output(pdfByteOutputStream, pdfSettings);
//        return pdfByteOutputStream.toByteArray();
//    }
//
//    /**
//     * Takes inputs and returns merged output as pdf byte[]
//     *
//     * @param templatePath
//     * @param templateEngineKind
//     * @param nonImageVariableMap
//     * @param imageVariablesWithPathMap
//     * @return
//     * @throws IOException
//     * @throws XDocReportException
//     * @throws Docx4JException
//     */
//    public byte[] mergeAndGeneratePDFOutput(String templatePath, TemplateEngineKind templateEngineKind, Map<String, Object> nonImageVariableMap, Map<String, String> imageVariablesWithPathMap) throws IOException, XDocReportException, Docx4JException {
//        InputStream inputStream = loadDocumentAsStream(templatePath);
//        IXDocReport xdocReport = loadDocumentAsIDocxReport(inputStream, templateEngineKind);
//        IContext context = replaceVariabalesInTemplateOtherThanImages(xdocReport, nonImageVariableMap);
//        replaceImagesVariabalesInTemplate(xdocReport, imageVariablesWithPathMap, context);
//        byte[] mergedOutput = generateMergedOutput(xdocReport, context);
//        byte[] pdfBytes = generatePDFOutputFromDocx(mergedOutput);
//        return pdfBytes;
//    }
}
