/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ra.uat;

import com.itextpdf.html2pdf.jsoup.Jsoup;
import com.itextpdf.html2pdf.jsoup.nodes.Document;
import com.itextpdf.html2pdf.jsoup.nodes.Entities;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.docx4j.convert.in.xhtml.XHTMLImporterImpl;
import org.docx4j.dml.wordprocessingDrawing.Inline;
import org.docx4j.model.structure.PageSizePaper;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.exceptions.InvalidFormatException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.BinaryPartAbstractImage;
import org.docx4j.wml.Drawing;
import org.docx4j.wml.ObjectFactory;
import org.docx4j.wml.P;
import org.docx4j.wml.R;
import org.docx4j.w14.CTSdtCheckbox;

/**
 *
 * @author USER
 */
public class TestExportWord01 {
    public static void testHeightWidthInPx(WordprocessingMLPackage wordMLPackage) throws Exception {
//        String PNG_IMAGE_DATA = "data:image/png;base64,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";		
        String PNG_IMAGE_DATA = "";		
        String html= "<html><head></head><body><form name=\"uploadForm\"><div>" +
                "<p><img src='" + PNG_IMAGE_DATA + "' width='40px' height='20px' /></p>" +
                "<p><img src='" + PNG_IMAGE_DATA + "' style='width:40px; height:20px' /></p>" +
                "<p><input type=\"checkbox\" name=\"processTocField\"  value=\"true\">process ToC field</input></p>" +
        "</div></form></body></html>";
        XHTMLImporterImpl XHTMLImporter = new XHTMLImporterImpl(wordMLPackage);
        WordprocessingMLPackage.FilterSettings filterSettings = new WordprocessingMLPackage.FilterSettings();
        filterSettings.setRemoveProofErrors(true);
        filterSettings.setRemoveContentControls(true);
        filterSettings.setRemoveRsids(true);
        wordMLPackage.filter(filterSettings);
        List<Object> convert = XHTMLImporter.convert(html, null);
        wordMLPackage.getMainDocumentPart().getContent().addAll(convert);
        wordMLPackage.save(new java.io.File("D:\\Programer\\Company\\TMS-RA\\EFY-CA\\Demo\\201810250858212.docx"));
    }
    
    private WordprocessingMLPackage html2word(String html,String baseUrl) {
        Document doc = Jsoup.parse(html);
        doc.outputSettings().syntax(Document.OutputSettings.Syntax.xml).escapeMode(Entities.EscapeMode.xhtml);
        WordprocessingMLPackage wordMLPackage = null;
        try {
            wordMLPackage = WordprocessingMLPackage.createPackage(PageSizePaper.valueOf("A4"), true);
            XHTMLImporterImpl xhtmlImporter = new XHTMLImporterImpl(wordMLPackage);
            wordMLPackage.getMainDocumentPart().getContent().addAll(xhtmlImporter.convert(doc.html(), baseUrl));
        } catch (Docx4JException e) {
            e.printStackTrace();
        }
        return wordMLPackage;
    }

    
    public static void main(String[] args) throws InvalidFormatException, IOException, Exception {
        
        
        
        WordprocessingMLPackage wordMLPackage
                = WordprocessingMLPackage.createPackage();
        testHeightWidthInPx(wordMLPackage);
        /*File file = new File("D://Programer//Company//TMS-RA//A_CommonCA//RAPortal_EE6//web//Images//Logo_NCCA.jpg");
        byte[] bytes = convertImageToByteArray(file);
        addImageToPackage(wordMLPackage, bytes);

        wordMLPackage.save(new java.io.File("D:\\Programer\\Company\\TMS-RA\\EFY-CA\\Demo\\20181025085822.docx"));*/
    }

    private static void addImageToPackage(WordprocessingMLPackage wordMLPackage,
            byte[] bytes) throws Exception {
        BinaryPartAbstractImage imagePart = BinaryPartAbstractImage.createImagePart(wordMLPackage, bytes);

        int docPrId = 3;
        int cNvPrId = 5;
        Inline inline = imagePart.createImageInline("Filename hint",
                "Alternative text", docPrId, cNvPrId, false);

        P paragraph = addInlineImageToParagraph(inline);

        wordMLPackage.getMainDocumentPart().addObject(paragraph);
    }

    private static P addInlineImageToParagraph(Inline inline) {
        // Now add the in-line image to a paragraph
        ObjectFactory factory = new ObjectFactory();
        P paragraph = factory.createP();
        R run = factory.createR();
        paragraph.getContent().add(run);
        Drawing drawing = factory.createDrawing();
        run.getContent().add(drawing);
        drawing.getAnchorOrInline().add(inline);
        return paragraph;
    }

    private static byte[] convertImageToByteArray(File file)
            throws FileNotFoundException, IOException {
        InputStream is = new FileInputStream(file);
        long length = file.length();
        // You cannot create an array using a long, it needs to be an int.
        if (length > Integer.MAX_VALUE) {
            System.out.println("File too large!!");
        }
        byte[] bytes = new byte[(int) length];
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += numRead;
        }
        // Ensure all the bytes have been read
        if (offset < bytes.length) {
            System.out.println("Could not completely read file "
                    + file.getName());
        }
        is.close();
        return bytes;
    }
}
