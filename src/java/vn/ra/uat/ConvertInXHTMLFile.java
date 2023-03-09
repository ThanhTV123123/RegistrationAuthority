/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ra.uat;

import java.io.File;

//import org.apache.commons.io.FileUtils;
//import org.apache.commons.lang3.StringEscapeUtils;
//import org.docx4j.XmlUtils;
//import org.docx4j.convert.in.xhtml.ImportXHTMLProperties;
//import org.docx4j.convert.in.xhtml.XHTMLImporterImpl;
//import org.docx4j.jaxb.Context;
//import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
//import org.docx4j.openpackaging.parts.WordprocessingML.NumberingDefinitionsPart;
//import org.docx4j.wml.RFonts;

/**
 *
 * @author THANH-PC
 */
public class ConvertInXHTMLFile {

    public static void main(String[] args) throws Exception {
        String dataPath = "D://Project//TMS CA//TMS EFY";
        String inputfilepath = dataPath + "//abc.html";
//        String baseURL = "file:///C:/Users/jharrop/git/docx4j-ImportXHTML/somedir/";
//        String baseURL = dataPath + "//";// "file:///C:/Users/jharrop/git/docx4j-ImportXHTML/";
//
//        String stringFromFile = FileUtils.readFileToString(new File(inputfilepath), "UTF-8");
//
//        String unescaped = stringFromFile;
////        if (stringFromFile.contains("&lt;/") ) {
////    		unescaped = StringEscapeUtils.unescapeHtml(stringFromFile);        	
////        }
//
////        XHTMLImporter.setTableFormatting(FormattingOption.IGNORE_CLASS);
////        XHTMLImporter.setParagraphFormatting(FormattingOption.IGNORE_CLASS);
//        System.out.println("Unescaped: " + unescaped);
//
//        // Setup font mapping
//        RFonts rfonts = Context.getWmlObjectFactory().createRFonts();
//        rfonts.setAscii("Century Gothic");
//        XHTMLImporterImpl.addFontMapping("Century Gothic", rfonts);
//
//        // Create an empty docx package
//        WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.createPackage();
////		WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(new File(System.getProperty("user.dir") + "/styled.docx"));
//
//        NumberingDefinitionsPart ndp = new NumberingDefinitionsPart();
//        wordMLPackage.getMainDocumentPart().addTargetPart(ndp);
//        ndp.unmarshalDefaultNumbering();
//
//        // Convert the XHTML, and add it into the empty docx we made
//        XHTMLImporterImpl XHTMLImporter = new XHTMLImporterImpl(wordMLPackage);
//
//        XHTMLImporter.setHyperlinkStyle("Hyperlink");
//        wordMLPackage.getMainDocumentPart().getContent().addAll(
//                XHTMLImporter.convert(unescaped, baseURL));
//
//        System.out.println(
//                XmlUtils.marshaltoString(wordMLPackage.getMainDocumentPart().getJaxbElement(), true, true));
//
////		System.out.println(
////				XmlUtils.marshaltoString(wordMLPackage.getMainDocumentPart().getNumberingDefinitionsPart().getJaxbElement(), true, true));
//        wordMLPackage.save(new java.io.File(dataPath + "//OUT_from_XHTML.docx"));

    }
}
