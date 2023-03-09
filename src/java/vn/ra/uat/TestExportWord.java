/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ra.uat;

import java.io.File;
import java.io.InputStream;
import javax.xml.bind.JAXBException;

import org.docx4j.convert.in.xhtml.XHTMLImporterImpl;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.AltChunkType;
import org.docx4j.openpackaging.parts.WordprocessingML.NumberingDefinitionsPart;
import org.docx4j.dml.wordprocessingDrawing.Inline;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.wml.*;
import org.docx4j.openpackaging.parts.WordprocessingML.BinaryPartAbstractImage;

//import java.io.File;
//import com.itextpdf.text.pdf.PdfReader;
//import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
//import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
//import com.itextpdf.text.pdf.parser.TextExtractionStrategy;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import org.apache.poi.xwpf.usermodel.BreakType;
//import org.apache.poi.xwpf.usermodel.XWPFDocument;
//import org.apache.poi.xwpf.usermodel.XWPFParagraph;
//import org.apache.poi.xwpf.usermodel.XWPFRun;
//import org.docx4j.convert.in.xhtml.FormattingOption;
//import org.docx4j.convert.in.xhtml.XHTMLImporter;
//import org.docx4j.convert.in.xhtml.XHTMLImporterImpl;
//import org.docx4j.openpackaging.exceptions.Docx4JException;
//import org.docx4j.openpackaging.exceptions.InvalidFormatException;
//import org.docx4j.openpackaging.io.SaveToZipFile;
//import org.docx4j.openpackaging.packages.WordprocessingMLPackage;

//import java.io.FileInputStream;
//import java.util.List;
//
//import javax.xml.bind.JAXBContext;
//
//import org.docx4j.XmlUtils;
//import org.docx4j.dml.CTBlip;
//import org.docx4j.model.datastorage.BindingHandler;
//import org.docx4j.model.datastorage.OpenDoPEHandler;
//import org.docx4j.model.datastorage.OpenDoPEIntegrity;
//import org.docx4j.model.datastorage.RemovalHandler;
//import org.docx4j.model.datastorage.RemovalHandler.Quantifier;
//import org.docx4j.openpackaging.exceptions.Docx4JException;
//import org.docx4j.openpackaging.io.SaveToZipFile;
//import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
//import org.docx4j.openpackaging.parts.CustomXmlDataStoragePart;
//import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
////import org.docx4j.samples.ImageConvertEmbeddedToLinked.TraversalUtilBlipVisitor;
//import org.docx4j.utils.SingleTraversalUtilVisitorCallback;
//import org.docx4j.utils.TraversalUtilVisitor;
//import org.docx4j.wml.SdtElement;
/**
 *
 * @author THANH-PC
 */
public class TestExportWord {
//public static JAXBContext context = org.docx4j.jaxb.Context.jc; 

//	private final static boolean DEBUG = true;
//	private final static boolean SAVE = true;
    public static P newImage( WordprocessingMLPackage wordMLPackage, byte[] bytes, 
            String filenameHint, String altText, int id1, int id2) throws Exception {
        BinaryPartAbstractImage imagePart = BinaryPartAbstractImage.createImagePart(wordMLPackage, bytes);
        Inline inline = imagePart.createImageInline( filenameHint, altText, id1, id2);

        ObjectFactory factory = new ObjectFactory();

        P  p = factory.createP();
        R  run = factory.createR();

        p.getParagraphContent().add(run);        
        Drawing drawing = factory.createDrawing();      
        run.getRunContent().add(drawing);       
        drawing.getAnchorOrInline().add(inline);

        return p;
    }
    private static void documentGenerator(String html, File file) throws Docx4JException, JAXBException, Exception {
        File fileSave = new File("D://Programer//Company//TMS-RA//A_CommonCA//RAPortal_EE6//web//Images//Logo_NCCA.jpg" );

        InputStream inputStream = new java.io.FileInputStream(fileSave);
        long fileLength = fileSave.length();    

        byte[] bytes = new byte[(int)fileLength];

        int offset = 0;
        int numRead = 0;

        while(offset < bytes.length
               && (numRead = inputStream.read(bytes, offset, bytes.length-offset)) >= 0) {
            offset += numRead;
        }

        inputStream.close();

        String filenameHint = null;
        String altText = null;

        int id1 = 1;
        int id2 = 2;
        
        WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.createPackage();
        NumberingDefinitionsPart ndp = new NumberingDefinitionsPart();
        wordMLPackage.getMainDocumentPart().addTargetPart(ndp);
        ndp.unmarshalDefaultNumbering();
        // Convert the XHTML, and add it into the empty docx we made
        XHTMLImporterImpl XHTMLImporter = new XHTMLImporterImpl(wordMLPackage);
        XHTMLImporter.setHyperlinkStyle("Hyperlink");

        P p = newImage(wordMLPackage, bytes, filenameHint, altText, id1, id2);
//        String baseurl = file.getPath();
//        baseurl = baseurl.substring(0, baseurl.lastIndexOf("\\"));
//        wordMLPackage.getMainDocumentPart().getContent().addAll(XHTMLImporter.convert(html, baseurl));
        //Saving the Document
        wordMLPackage.getMainDocumentPart().addAltChunk(AltChunkType.Xhtml, html.getBytes());
        wordMLPackage.save(file);
    }
    public static void main(String[] args) throws Docx4JException, JAXBException, Exception 
    {
        String sHtml = "<html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:ds=\"http://www.w3.org/2000/09/xmldsig#\"><head><meta charset=\"UTF-8\"/><meta content=\"width=device-width, initial-scale=1.0\" name=\"viewport\"/><style>\n" +
"                    *{\n" +
"                        margin: 0;\n" +
"                        padding: 0;\n" +
"                    }\n" +
"                    p{\n" +
"                        margin: 0px;\n" +
"                        padding:0px;\n" +
"                    }\n" +
"                    body {\n" +
"                        color: #000;\n" +
"                        font-size: 13pt;\n" +
"                        width: 21cm;\n" +
"                        height: 29.7cm; \n" +
"                        font-family: \"Times New Roman\", Times, serif;\n" +
"                        margin: 0 auto;\n" +
"                        line-height: 18px;\n" +
"                    }\n" +
"                    .page{\n" +
"                        padding: 25px 60px 15px 70px;\n" +
"                    }\n" +
"                    .clFix{\n" +
"                        padding-left: 15px;\n" +
"                    }\n" +
"                    .spIcon{\n" +
"                        width: 10px;\n" +
"\n" +
"                    }\n" +
"                    table{\n" +
"                        width: 100%;\n" +
"                    }\n" +
"                    .pdLeft10{\n" +
"                        padding-left: 20px;\n" +
"                    }\n" +
"                    .tblDetail td{\n" +
"                        padding:5px;\n" +
"                    }\n" +
"                    .tblDetail th, .tblDetail td{\n" +
"                        border: 0.1px solid #000;\n" +
"                    }\n" +
"                    .clLine{\n" +
"                        text-align: center;\n" +
"                        width: 100%;\n" +
"                    }\n" +
"                    .clLine span{\n" +
"                        width: 50%;\n" +
"                        border-bottom: 1px solid;\n" +
"                        height: 1px;\n" +
"                        display: inline-block;\n" +
"                    }\n" +
"                    table td{\n" +
"                        vertical-align: top\n" +
"                    }\n" +
"                </style></head><body><div class=\"page\"><div><table style=\"width:100%\"><tr><td style=\"width: 300px;\"><div style=\"text-align: center;\"><p style=\"font-weight: bold;\">\n" +
"                                            CÔNG TY CP CÔNG NGHỆ TIN HỌC EFY VIỆT NAM\n" +
"                                        </p><p style=\"font-weight: bold;\">---------------</p><p style=\"font-weight: bold;\">Số: 10/2018/BC-EFY</p></div></td><td style=\"width: 10px\"/><td style=\"width: 530px\"><div style=\"text-align: center;\"><p style=\"font-weight: bold;\">CỘNG HÒA XÃ HỘI CHỦ NGHĨA VIỆT NAM</p><p style=\"font-size: 17px;font-weight: bold;\">Độc lập - Tự do - Hạnh phúc </p><p style=\"font-weight: bold;\">---------------</p></div></td></tr></table><div class=\"content\"><div style=\"font-size: 19px;text-align: center;font-weight: bold;margin-top:10px;\"><p>BÁO CÁO ĐỊNH KỲ</p></div><div style=\"font-size: 15px;text-align: center;margin-bottom: 10px;margin-top: 10px;\"><p><i>Quý: I, Năm: 2018</i></p></div><div style=\"font-size: 16px;text-align: center;font-weight: bold;margin-bottom: 10px;\"><p>Kính gửi: Trung tâm Chứng thực điện tử quốc gia</p></div><div style=\"margin-bottom: 10px;\"><p style=\"font-weight: bold;margin-left: 25px;\">Phần 1. Tình hình hoạt động</p></div><div style=\"margin-bottom: 10px;\"><p><span style=\"margin-right: 25px;\"/>Công ty Cổ phần công nghệ tin học EFY Việt Nam xin báo cáo định kỳ về tình hình hoạt động như sau:</p></div><div style=\"margin-bottom: 7px;\"><p>- Tình hình chung: Hiện tại hệ thống đang hoạt động tốt</p></div><div style=\"margin-bottom: 7px;\"><p>- Cơ sở hạ tầng đang sử dụng: Hệ thống chính EFY-CA đặt tại FPT-DC Duy Tân, số 17 Duy Tân, Cầu Giấy và hệ thống dự phòng đặt tại Viettel-DC Láng Hòa Lạc.</p></div><div style=\"margin-bottom: 7px;\"><p>- Nội dung khác (ví dụ: sự cố liên quan đến chứng thư số, người dùng,…).</p></div><div style=\"padding-top:12px;\"/><div class=\"box\"><table style=\"width: 100%;border-collapse: collapse;\" class=\"tblDetail\"><thead><tr><td style=\"width: 22%;font-weight: bold;text-align: center;padding-top:10px;padding-bottom:10px;\"><p>CTS tổ chức</p></td><td style=\"width: 22%;font-weight: bold;text-align: center;padding-top:10px;padding-bottom:10px;\"><p>CTS cá nhân trong tổ chức</p></td><td style=\"width: 22%;font-weight: bold;text-align: center;padding-top:10px;padding-bottom:10px;padding-bottom:10px;\"><p>CTS cá nhân</p></td><td style=\"width: 20%;font-weight: bold;text-align: center;padding-top:10px;padding-bottom:10px;padding-bottom:10px;\"><p>Trạng thái</p></td><td style=\"font-weight: bold;text-align: center;padding-top:10px;padding-bottom:10px;\"><p>Tổng số</p></td></tr></thead><tbody><tr><td style=\"text-align: center;\"><p>974</p></td><td style=\"text-align: center;\"><p>7</p></td><td style=\"text-align: center;\"><p>2</p></td><td style=\"text-align: center;\"><p>Hoạt động</p></td><td style=\"text-align: center;\"><p>983</p></td></tr><tr><td style=\"text-align: center;\"><p>82</p></td><td style=\"text-align: center;\"><p>1</p></td><td style=\"text-align: center;\"><p>2</p></td><td style=\"text-align: center;\"><p>Thu hồi</p></td><td style=\"text-align: center;\"><p>85</p></td></tr></tbody></table></div><div style=\"margin-top: 10px; margin-bottom: 10px;\"><div style=\"margin-bottom:10px;\"><p>Bảng 1.2: Thống kê số lượng CTS hoạt động/thu hồi quý I/2018.</p></div><div style=\"margin-bottom:10px;\"><p style=\"font-weight: bold;margin-left:25px;\">Phần 2. Ý kiến đóng góp (nếu có)</p><p style=\"font-weight: bold;margin-left:25px;\">Phần 3. Cam kết</p></div><div><p>Công ty Cổ phần công nghệ tin học EFY Việt Nam cam kết chịu trách nhiệm trước pháp luật về tính chính xác của nội dung trong Báo cáo và các tài liệu kèm theo./.</p></div></div><table style=\"width:100%;\" class=\"tblFooter\"><tr><td style=\"width: 50%;text-align: center;\"><p style=\"margin-top: 22px;font-weight: bold;\">NGƯỜI LẬP BÁO CÁO</p><p style=\"font-size: 13px;\"><i>(Ký, ghi rõ họ tên)</i></p></td><td style=\"width: 50%;text-align: center;\"><p style=\"padding-bottom: 5px;\">........., ngày.....tháng.....năm ......</p><p style=\"font-weight: bold;\">ĐẠI DIỆN THEO PHÁP LUẬT</p><p style=\"font-size: 13px;\"><i>(Ký, ghi rõ họ tên, chức danh và đóng dấu)</i></p></td></tr></table></div></div></div></body></html>";
        //<div style="padding-top:12px;"/>
        String inputfilepath = "D:\\Programer\\Company\\TMS-RA\\EFY-CA\\Demo\\admin_mn_REPORT_PERIODIC_20181025085872.docx";
        documentGenerator(sHtml, new File(inputfilepath));
        
//        WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.createPackage();
//        String outputfilepath = "D:\\style-example-OUT.docx";
//        String sdir = "D:\\Project\\TMS CA\\TMS EFY\\pdf.docx";
//        String text = "<html><head><title></title></head><body>"
//                + "<p class=\"Title\">Testing Title</p>"
//                + "<p class=\"Subtitle\">Testing Subtitle</p>"
//                + "</body></html>";
//
//        XHTMLImporter xHTMLImporter = new XHTMLImporterImpl(wordMLPackage);
//        // it does not matter if I choose CLASS_PLUS_OTHER or CLASS_TO_STYLE_ONLY
//        xHTMLImporter.setParagraphFormatting(FormattingOption.CLASS_PLUS_OTHER);
//        xHTMLImporter.setRunFormatting(FormattingOption.CLASS_PLUS_OTHER);
//
//        wordMLPackage.getMainDocumentPart().getContent().addAll(xHTMLImporter.convert(text, null));
//
//// now we add the same lines again using method addStyledParagraphOfText instead of the xHTMLImporter
//        wordMLPackage.getMainDocumentPart().addStyledParagraphOfText("Title", "Testing Title");
//        wordMLPackage.getMainDocumentPart().addStyledParagraphOfText("Subtitle", "Testing Subtitle");
//
//        SaveToZipFile saver = new SaveToZipFile(wordMLPackage);
//        saver.save(sdir);
//        System.out.println("a");
//        
//        String sdir = "D:\\Project\\TMS CA\\TMS EFY\\";
//        XWPFDocument doc = new XWPFDocument();
//        String pdf = sdir + "admin_mn_REPORT_PERIODIC_20181022150509.pdf";
//        PdfReader reader = new PdfReader(pdf);
//        PdfReaderContentParser parser = new PdfReaderContentParser(reader);
//        for (int i = 1; i <= reader.getNumberOfPages(); i++) {
//            TextExtractionStrategy strategy = parser.processContent(i, new SimpleTextExtractionStrategy());
//            String text = strategy.getResultantText();
//            XWPFParagraph p = doc.createParagraph();
//            XWPFRun run = p.createRun();
//            run.setText(text);
////            run.addBreak(BreakType.PAGE);
//        }
//        FileOutputStream out = new FileOutputStream(sdir + "pdf.docx");
//        doc.write(out);
    }

//	public static void main(String[] args) throws Exception {
//			String sdir = "D:\\Project\\TMS CA\\TMS EFY\\";
//		// the docx 'template'
//		String input_DOCX = sdir + "TestExport_FileWord.docx";
//		
//		// the instance data
//		String input_XML = sdir+ "a.xml";
//		
//		// resulting docx
//		String OUTPUT_DOCX = sdir + "TestExport_FileWord2.docx";
//
//		// Load input_template.docx
//		WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(
//				new java.io.File(input_DOCX));
//		
//		// Find custom xml item id
//		String itemId = getCustomXmlItemId(wordMLPackage).toLowerCase();
//		System.out.println("Looking for item id: " + itemId);
//		
//		// Inject data_file.xml		
//		CustomXmlDataStoragePart customXmlDataStoragePart = (CustomXmlDataStoragePart) wordMLPackage.getCustomXmlDataStorageParts().get(itemId);
//		if (customXmlDataStoragePart==null) {
//			System.out.println("Couldn't find CustomXmlDataStoragePart! exiting..");
//			return;			
//		}
//		System.out.println("Getting " + input_XML);
//		FileInputStream fis = new FileInputStream(new File(input_XML));
//		customXmlDataStoragePart.getData().setDocument(fis);
//		
//		SaveToZipFile saver = new SaveToZipFile(wordMLPackage);
//		try {
//			// Process conditionals and repeats
//			OpenDoPEHandler odh = new OpenDoPEHandler(wordMLPackage);
//			odh.preprocess();
//			
//			OpenDoPEIntegrity odi = new OpenDoPEIntegrity();
//			odi.process(wordMLPackage);
//			
//			if (DEBUG) {
//				String save_preprocessed; 						
//				if (OUTPUT_DOCX.lastIndexOf(".")==-1) {
//					save_preprocessed = OUTPUT_DOCX + "_INT.docx"; 
//				} else {
//					save_preprocessed = OUTPUT_DOCX.substring(0, OUTPUT_DOCX.lastIndexOf(".") ) + "_INT.docx"; 
//				}
////				System.out.println(
////						XmlUtils.marshaltoString(wordMLPackage.getMainDocumentPart().getJaxbElement(), true, true)
////						);		
//				saver.save(save_preprocessed);
//				System.out.println("Saved: " + save_preprocessed);
//			}
//			
//		} catch (Docx4JException d) {
//			// Probably this docx doesn't contain OpenDoPE convention parts
//			System.out.println(d.getMessage());
//		}
//		
//		
//		// Apply the bindings
//		//BindingHandler.setHyperlinkStyle("Hyperlink");
//		BindingHandler.applyBindings(wordMLPackage);
//		// If you inspect the output, you should see your data in 2 places:
//		// 1. the custom xml part 
//		// 2. (more importantly) the main document part
////		System.out.println(
////				XmlUtils.marshaltoString(wordMLPackage.getMainDocumentPart().getJaxbElement(), true, true)
////				);
//		
//		// Strip content controls: you MUST do this 
//		// if you are processing hyperlinks
//		RemovalHandler rh = new RemovalHandler();
//		rh.removeSDTs(wordMLPackage, Quantifier.ALL);
//		
//		saver.save(OUTPUT_DOCX);
//		System.out.println("Saved: " + OUTPUT_DOCX);
//		
//	}
    /**
     * We need the item id of the custom xml part.
     *
     * There are several strategies we could use to find it, including searching
     * the docx for a bind element, but here, we simply look in the xpaths part.
     *
     * @param wordMLPackage
     * @return
     */
//	private static String getCustomXmlItemId(WordprocessingMLPackage wordMLPackage) throws Docx4JException {
//		
//		MainDocumentPart documentPart = wordMLPackage.getMainDocumentPart();			
//		if (wordMLPackage.getMainDocumentPart().getXPathsPart()==null) {
//			// Can't do it the easy way, so look up the binding on the first content control
//			TraversalUtilCCVisitor visitor = new TraversalUtilCCVisitor();
//			SingleTraversalUtilVisitorCallback ccFinder 
//			= new SingleTraversalUtilVisitorCallback(visitor);
//			ccFinder.walkJAXBElements(
//				wordMLPackage.getMainDocumentPart().getJaxbElement().getBody());
//			return visitor.storeItemID;
//			
//		} else {
//	
//			org.opendope.xpaths.Xpaths xPaths = wordMLPackage.getMainDocumentPart().getXPathsPart().getJaxbElement();
//			return xPaths.getXpath().get(0).getDataBinding().getStoreItemID();
//		}
//	}
//
//	public static class TraversalUtilCCVisitor extends TraversalUtilVisitor<SdtElement> {
//		
//		String storeItemID = null;
//		
//		@Override
//		public void apply(SdtElement element, Object parent, List<Object> siblings) {
//
//			if (element.getSdtPr()!=null
//					&& element.getSdtPr().getDataBinding()!=null) {
//				storeItemID = element.getSdtPr().getDataBinding().getStoreItemID();
//			}
//		}
//	
//	}
//    static String dir;
    // Config for non-command line version
//    static {
//
//        dir = "D:\\Project\\TMS CA\\TMS EFY\\";
////		dir = System.getProperty("user.dir") + "/";
//        inputfilepath = "TestExport_FileWord2.docx";
//    }
//    	inputfilepath = System.getProperty("user.dir") + "/sample-docs/docx/tables.docx";
//    	inputfilepath = System.getProperty("user.dir") + "/images.docx";
//    public static void xhtmlToDocx(String destinationPath, String fileName) {
//        File dir = new File(destinationPath);
//        File actualFile = new File(dir, fileName);
//        WordprocessingMLPackage wordMLPackage = null;
//        try {
//            wordMLPackage = WordprocessingMLPackage.createPackage();
//        } catch (InvalidFormatException e) {
//            e.printStackTrace();
//        }
//        XHTMLImporterImpl XHTMLImporter = new XHTMLImporterImpl(wordMLPackage);
//        OutputStream fos = null;
//        try {
//            fos = new ByteArrayOutputStream();
//            System.out.println(XmlUtils.marshaltoString(wordMLPackage.getMainDocumentPart().getJaxbElement(), true, true));
//            HTMLSettings htmlSettings = Docx4J.createHTMLSettings();
//            htmlSettings.setWmlPackage(wordMLPackage);
//            Docx4jProperties.setProperty("docx4j.Convert.Out.HTML.OutputMethodXML", true);
//            Docx4J.toHTML(htmlSettings, fos, Docx4J.FLAG_EXPORT_PREFER_XSL);
//            wordMLPackage.save(actualFile);
//        } catch (Docx4JException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } finally {
//            try {
//                fos.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//    public static void convertToDocX(File html, File docx) throws IOException, Docx4JException, JAXBException {
//
//        // Create an empty docx package
//        WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.createPackage();
//
//        NumberingDefinitionsPart ndp = new NumberingDefinitionsPart();
//        wordMLPackage.getMainDocumentPart().addTargetPart(ndp);
//        ndp.unmarshalDefaultNumbering();
//
//        XHTMLImporterImpl xHTMLImporter = new XHTMLImporterImpl(wordMLPackage);
//        xHTMLImporter.setHyperlinkStyle("Hyperlink");
//
//        // Convert the XHTML, and add it into the empty docx we made
//        wordMLPackage.getMainDocumentPart().getContent().addAll(xHTMLImporter.convert(html, null));
//
//        wordMLPackage.save(docx);
//    }
//    public static void main(String[] args) throws Exception {
//        String dataPath = "D://Project//TMS CA//TMS EFY//";
//        String html = "<html><head><title>Import me</title></head><body style=\"text-align: right;\"><table border=\"1\"><tbody><tr><td width=\"100\">מס'</td><td>נסיון:</td><td width=\"200\">לורם איפסום דולור סיט אמט, קונסקטורר אדיפיסינג אלית ושבעגט ליבם סולגק. בראיט</td></tr></tbody></table></body></html>";
//        convertToDocX(new File(dataPath+"abc.html"), new File(dataPath+"TestExport_FileWord2.docx"));
//
//        //    	String baseURL = "file:///C:/Users/jharrop/git/docx4j-ImportXHTML/images";    	
//        Docx4jProperties.setProperty("docx4j.Convert.Out.HTML.OutputMethodXML", true);
//
//        try {
//            getInputFilePath(args);
//        } catch (IllegalArgumentException e) {
//        }
//
//        System.out.println(inputfilepath);
//        WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(new java.io.File(dir + inputfilepath));
//
//        // XHTML export
//        AbstractHtmlExporter exporter = new HtmlExporterNG2();
//        HtmlSettings htmlSettings = new HtmlSettings();
//
//        htmlSettings.setWmlPackage(wordMLPackage);
//
//        htmlSettings.setImageDirPath(dir + inputfilepath + "_files");
//        htmlSettings.setImageTargetUri(dir + inputfilepath + "_files");
//
//        String htmlFilePath = dir + "abc.html";
//        OutputStream os = new java.io.FileOutputStream(htmlFilePath);
//
////		javax.xml.transform.stream.StreamResult result = new javax.xml.transform.stream.StreamResult(os);
////		exporter.html(wordMLPackage, result, htmlSettings);
////		os.flush();
////		os.close();
//        Docx4J.toHTML(htmlSettings, os, Docx4J.FLAG_NONE);
//
//        // XHTML to docx
//        String stringFromFile = FileUtils.readFileToString(new File(htmlFilePath), "UTF-8");
//
//        WordprocessingMLPackage docxOut = WordprocessingMLPackage.createPackage();
//        NumberingDefinitionsPart ndp = new NumberingDefinitionsPart();
//        docxOut.getMainDocumentPart().addTargetPart(ndp);
//        ndp.unmarshalDefaultNumbering();
//
//        XHTMLImporterImpl XHTMLImporter = new XHTMLImporterImpl(docxOut);
//        XHTMLImporter.setHyperlinkStyle("Hyperlink");
//
//        docxOut.getMainDocumentPart().getContent().addAll(
//                XHTMLImporter.convert(stringFromFile, null));
//
//        docxOut.save(new java.io.File(dir + "/TestExport_FileWord.docx"));
//        wordMLPackage.save(new java.io.File("D://Project//TMS CA//EFY//TestExport_FileWord.docx"));
//    }
//    public static void xhtmlToDocx(String destinationPath, String fileName) {
//        File dir = new File(destinationPath);
//        File actualFile = new File(dir, fileName);
//
//        WordprocessingMLPackage wordMLPackage = null;
//        try {
//            wordMLPackage = WordprocessingMLPackage.createPackage();
//        } catch (InvalidFormatException e) {
//            e.printStackTrace();
//        }
//
//        XHTMLImporterImpl XHTMLImporter = new XHTMLImporterImpl(wordMLPackage);
//
//        OutputStream fos = null;
//        try {
//            fos = new ByteArrayOutputStream();
//
//            System.out.println(XmlUtils.marshaltoString(wordMLPackage.getMainDocumentPart().getJaxbElement(), true, true));
//
//            HTMLSettings htmlSettings = Docx4J.createHTMLSettings();
//            htmlSettings.setWmlPackage(wordMLPackage);
//            Docx4jProperties.setProperty("docx4j.Convert.Out.HTML.OutputMethodXML",
//                    true);
//            Docx4J.toHTML(htmlSettings, fos, Docx4J.FLAG_EXPORT_PREFER_XSL);
//            wordMLPackage.save(actualFile);
//        } catch (Docx4JException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } finally {
//            try {
//                fos.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//    public static void main(String[] args) throws Exception {
//        String sURL = "D:\\Project\\TMS CA\\TMS EFY\\TestExport_FileWord.docx";
//        String html = "<html><head><title>Import me</title></head><body><p>Hello World!</p></body></html>";
//        WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.createPackage();
// 
//        NumberingDefinitionsPart ndp = new NumberingDefinitionsPart();
//        wordMLPackage.getMainDocumentPart().addTargetPart(ndp);
//        ndp.unmarshalDefaultNumbering();
// 
//        wordMLPackage.getMainDocumentPart().getContent().addAll(XHTMLImporter.convert(html, null, wordMLPackage));
// 
//        System.out.println(XmlUtils.marshaltoString(wordMLPackage
//                .getMainDocumentPart().getJaxbElement(), true, true));
// 
//        wordMLPackage.save(new java.io.File(sURL));
//        System.out.println("done");
//        xhtmlToDocx("D:\\Project\\TMS CA\\TMS EFY", "TestExport_FileWord.docx");
//        String sText = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:ds=\"http://www.w3.org/2000/09/xmldsig#\"><head><meta charset=\"UTF-8\"/><meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"/><style>\n"
//                + "            \n"
//                + "                    @page {\n"
//                + "                    margin: 0;\n"
//                + "                    size: A4;\n"
//                + "                    }\n"
//                + "                    \n"
//                + "                    *{\n"
//                + "                    margin: 0;\n"
//                + "                    padding: 0;\n"
//                + "                    }\n"
//                + "            \n"
//                + "                    p{\n"
//                + "                    margin: 0px;\n"
//                + "                    padding:0px;\n"
//                + "                    }\n"
//                + "            \n"
//                + "                    body {\n"
//                + "                    color: #000;\n"
//                + "                    font-size: 13pt;\n"
//                + "                    width: 21cm;\n"
//                + "                    height: 29.7cm; \n"
//                + "                    font-family: \"Times New Roman\", Times, serif;\n"
//                + "                    margin: 0 auto;\n"
//                + "                    line-height: 18px;\n"
//                + "                    }\n"
//                + "            \n"
//                + "                    .page{\n"
//                + "                    padding: 25px 60px 15px 70px;\n"
//                + "                    }\n"
//                + "                    \n"
//                + "                    .clFix{\n"
//                + "                    padding-left: 15px;\n"
//                + "                    }\n"
//                + "                    .spIcon{\n"
//                + "                    width: 10px;\n"
//                + "                    \n"
//                + "                    }\n"
//                + "                    table{\n"
//                + "                    width: 100%;\n"
//                + "                    }\n"
//                + "                    .pdLeft10{\n"
//                + "                    padding-left: 20px;\n"
//                + "                    }\n"
//                + "                    .tblDetail td{\n"
//                + "                    padding:5px;\n"
//                + "                    }\n"
//                + "                    .tblDetail th, .tblDetail td{\n"
//                + "                    border: 0.1px solid #000;\n"
//                + "                    }\n"
//                + "                    .clLine{\n"
//                + "                    text-align: center;\n"
//                + "                    width: 100%;\n"
//                + "                    }\n"
//                + "                    \n"
//                + "                    .clLine span{\n"
//                + "                    width: 50%;\n"
//                + "                    border-bottom: 1px solid;\n"
//                + "                    height: 1px;\n"
//                + "                    display: inline-block;\n"
//                + "                    }\n"
//                + "                    \n"
//                + "                    table td{\n"
//                + "                    vertical-align: top\n"
//                + "                    }\n"
//                + "                    \n"
//                + "\n"
//                + "                </style></head><body><div class=\"page pageA4\"><div class=\"wrapper\"><table class=\"tblHeader\" style=\"width:100%\"><tr><td style=\"width: 252px\"><img class=\"logo pt6pt\" alt=\"Logo\" style=\"width:194px;height:68px;\" src=\"data:image/jpeg;base64,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\"/></td><td style=\"width: 540px\"><div class=\"logoRight\" style=\"text-align: center;\"><p class=\"toUpper brandTop\" style=\"font-weight: bold;\">C?NG HÒA XÃ H?I CH? NGH?A VI?T NAM</p><p class=\"brandUnder\" style=\"font-size: 17px;font-weight: bold;\">??c l?p - T? do - H?nh phúc </p><div class=\"clLine\"><span/></div></div></td></tr></table><div class=\"content\"><div style=\"font-size: 19px;text-align: center;font-weight: bold;padding-top:12px;\">BIÊN B?N BÀN GIAO</div><p style=\"text-align: justify;padding-top:20px;\">Hôm nay, ngày ..../..../........ t?i Cong Ty ABC, chúng tôi g?m:</p><div class=\"box\"><div class=\"title2\" style=\"font-weight: bold;padding-top:4px;\">\n"
//                + "                                    BÊN GIAO: <span style=\"text-transform: uppercase;\">Cong Ty ABC</span></div><div class=\"box pdLeft10\"><table style=\"width:100%;margin-left:15px;\"><tr><td style=\"width:20px\">-</td><td style=\"\">H? và tên: Ho Ten ABC</td></tr><tr style=\"\"><td style=\"\">-</td><td style=\"\">??a ch?: 803/1 KVC, LT, TD</td></tr><tr><td style=\"\">-</td><td style=\"\">?i?n tho?i: 0978377152</td></tr></table></div></div><div class=\"box\"><div class=\"title2\" style=\"font-weight: bold;\">\n"
//                + "                                    BÊN NH?N: <span style=\"text-transform: uppercase;\"/></div><div class=\"box pdLeft10\"><table style=\"width:100%;margin-left:15px;\"><tr><td style=\"width:20px;\">-</td><td style=\"\">H? và tên: NGUYÊ?N</td></tr><tr style=\"\"><td style=\"\">-</td><td style=\"\"><table style=\"width:100%\"><tr><td style=\"width:240px;padding-left:-2px;\">S? CMND/h? chi?u: 123123123</td><td style=\"width:180px;\">Ngày c?p: 02/10/2018</td><td>N?i c?p: TP H? Chí Minh</td></tr></table></td></tr><tr><td style=\"\">-</td><td style=\"\">\n"
//                + "                                                ??a ch?: Sô? 12 nguyê?n nguyê?n</td></tr><tr><td style=\"\">-</td><td style=\"\">\n"
//                + "                                                ?i?n tho?i: 0901111111</td></tr></table></div></div><div class=\"box\"><p class=\"pt6pt\" style=\"font-weight: bold;padding-bottom: 6px;\">Danh m?c bàn giao:</p><table class=\"tblDetail\" style=\"width: 100%;border-collapse: collapse;\"><thead><tr><td class=\"pt3pt\" style=\"width: 7%;font-weight: bold;text-align: center;padding-top:10px;padding-bottom:10px;\">STT</td><td class=\"pt3pt\" style=\"width: 35%;font-weight: bold;text-align: center;padding-top:10px;padding-bottom:10px;\">Tên hàng hóa</td><td style=\"font-weight: bold;text-align: center;padding-top:10px;padding-bottom:10px;\">Các thông s?</td></tr></thead><tbody><tr><td style=\"text-align: center;\">\n"
//                + "                                                1\n"
//                + "                                            </td><td>\n"
//                + "                                                Ch?ng th? s?: OSR1Y (Gia h?n Doanh nghi?p (12 tháng)/Renew Enterprise (12 months))</td><td><p>Serial number: <strong>54 01 01 10 EE 82 00 CF 29 67 DE AA 80 02 F6 B8</strong></p><p>Subject DN: <strong>UID=MST:0313994173, CN=CÔNG TY C? PH?N CÔNG NGH? VÀ D?CH V? MOBILE-ID, L=Qu?n 1, ST=H? Chi Minh, C=VN</strong></p><p>Iusuer DN: <strong>C=VN, CN=EFY Certification Authority</strong></p><p>?? dài c?p khóa: <strong>2048 bits</strong></p><p>Các thu?c tính ch?ng th? s?: <strong>Client Authentication, Secure Email, Digital Signature, Non-Repudiation, Key Encipherment, Data Encipherment</strong></p></td></tr><tr><td style=\"text-align: center;\">\n"
//                + "                                                2\n"
//                + "                                            </td><td>\n"
//                + "                                                Mã kích ho?t (Th?i h?n hi?u l?c)\n"
//                + "                                            </td><td><p><strong/></p></td></tr><tr><td style=\"text-align: center;\">\n"
//                + "                                                3\n"
//                + "                                            </td><td>\n"
//                + "                                                Thi?t b? ph?n c?ng ch?a khóa bí m?t c?a thuê bao\n"
//                + "                                            </td><td><p>EFY-CA Token: <strong>1A86052314300913</strong></p></td></tr><tr><td style=\"text-align: center;\">\n"
//                + "                                                4\n"
//                + "                                            </td><td>\n"
//                + "                                                H?p ??ng cung c?p d?ch v? ch?ng th?c ch? ký s? công c?ng EFY-CA\n"
//                + "                                            </td><td><strong/></td></tr><tr><td style=\"text-align: center;\">\n"
//                + "                                                5\n"
//                + "                                            </td><td>\n"
//                + "                                                Gi?y ch?ng nh?n s? d?ng ch?ng th? s? \n"
//                + "                                            </td><td><strong/></td></tr></tbody></table></div><div class=\"box\" style=\"padding-bottom:18px;\"><div class=\"note\" style=\"padding-top:18px;\"><p class=\"pt3pt\"><strong><u> Ghi chú:</u></strong> Biên b?n bàn giao ???c l?p thành 02 b?n, m?i bên gi? 1 b?n có giá tr? nh? nhau. </p><p class=\"pt6pt\" style=\"padding-top:3px;\">Khách hàng c?p nh?p l?i s? <strong>serial number</strong> v?i c? quan thu? ?? khai thu? qua m?ng.</p></div></div><table class=\"tblFooter\" style=\"width:100%;\"><tr><td style=\"width: 50%;text-align: center;\"><p class=\"toUpper\" style=\"font-weight: bold;\">BÊN GIAO</p><p><i>(Ký, ghi rõ h? tên)</i></p></td><td style=\"width: 50%;text-align: center;\"><p class=\"toUpper\" style=\"font-weight: bold;\">BÊN NH?N</p><p><i>(Ký, ghi rõ h? tên)</i></p></td></tr></table></div></div></div></body></html>";
//        //Blank Document
//        XWPFDocument document = new XWPFDocument();
//        try (FileOutputStream out = new FileOutputStream(new File(sURL))) {
//            XWPFParagraph paragraph = document.createParagraph();
//            //Set Bold an Italic
//            XWPFRun paragraphOneRunOne = paragraph.createRun();
////        paragraphOneRunOne.setBold(true);
////        paragraphOneRunOne.setItalic(true);
//            paragraphOneRunOne.setText(sText);
////        paragraphOneRunOne.addBreak();
////Set text Position
////        XWPFRun paragraphOneRunTwo = paragraph.createRun();
////        paragraphOneRunTwo.setText("Font Style two");
////        paragraphOneRunTwo.setTextPosition(100);
////Set Strike through and Font Size and Subscript
////        XWPFRun paragraphOneRunThree = paragraph.createRun();
////        paragraphOneRunThree.setStrike(true);
////        paragraphOneRunThree.setFontSize(20);
////        paragraphOneRunThree.setSubscript(
////                VerticalAlign.SUBSCRIPT);
////        paragraphOneRunThree.setText(" Different Font Styles");
//            document.write(out);
//        }
//        System.out.println("fontstyle.docx written successully");
//    }
}
