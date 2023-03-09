/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ra.utility;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.HashMap;

/**
 *
 * @author Tran
 */
public class EscapeUtils {

    /**
     *
     */
    public static final HashMap m = new HashMap();

    static {
        //m.put(34, "&quot;"); // < - "
        m.put(60, "&lt;");   // < - <
        m.put(62, "&gt;");   // > - >
        //m.put(38, "&amp;");   // > - &
        // m.put(61, "&equals;");   // - =
        m.put(92, "&bsol;");   // - \
        // m.put(47, "&sol;");   // - /
        m.put(169, "&copy;");   // > - greater-than
        m.put(174, "&reg;");   // > - greater-than
        //m.put(39, "&apos;"); // '
        //m.put(59, "&#59;"); // '
        //m.put(8482, "&trade;");   // > - greater-than
        //m.put(162, "&cent;");   // > - greater-than
        //m.put(163, "&pound;");   // > - greater-than
        //m.put(165, "&yen;");   // > - greater-than
        //m.put(8364, "&euro;");   // > - greater-than
        //m.put(167, "&sect;");   // > - greater-than
    }
    static CharsetEncoder asciiEncoder = Charset.forName("US-ASCII").newEncoder();

    /**
     *
     * @param v
     * @return
     */
    public static boolean isPureAscii(String v) {
        return asciiEncoder.canEncode(v);
    }

//    public static String escapeHtml(String str) {
//        //String str1 = "<script>alert</script>";
//        String sResult = "";
//        if (!"".equals(str)) {
//            try {
//                StringWriter writer = new StringWriter((int) (str.length() * 1.5));
//                escape(writer, str);
//                //System.out.println("encoded string is " + writer.toString());
//                sResult = writer.toString();
//            } catch (IOException ex) {
//                CommonFunction.LogExceptionJSP("escapeHtml: " + ex.getMessage() + Definitions.CONFIG_LOG_WRITE_DOWNLINE, ex);
//                return null;
//            }
//        }
//        return sResult;
//    }
    /**
     *
     * @param text
     * @return
     */
    public static String escapeHtml(String text) {
        String sResult;
        if (!"".equals(text) && text != null) {
            text = text.replace("'", "&apos;");
            //text = text.replace("\"", "&quot;");
            text = text.replace(">", "&gt;");
            text = text.replace("<", "&lt;");
            //text = text.replace("=", "&equals;");
            text = text.replace("\\", "&bsol;");
            //text = text.replace("/", "&sol;");
            //text = text.replace("&", "&amp;");
            //text = text.replace("*", "&ast;");
            //text = text.replace("@", "&commat;");
        } else {
            text = "";
        }
        sResult = text;
        return sResult.trim();
    }

    /**
     *
     * @param text
     * @return
     */
    public static String escapeHtmlSearch(String text) {
        String sResult;
        if (!"".equals(text) && text != null) {
            text = text.replace("'", "&apos;");
            text = text.replace("\"", "&quot;");
            text = text.replace(">", "&gt;");
            text = text.replace("<", "&lt;");
            text = text.replace("=", "&equals;");
            text = text.replace("\\", "&bsol;");
        } else {
            text = "";
        }
        sResult = text;
        return sResult.trim();
    }

    /**
     *
     * @param text
     * @return
     * @throws java.io.UnsupportedEncodingException
     */
    public static String escapeHtmlSearch_UTF8(String text) throws UnsupportedEncodingException {
        String sResult;
        if (!"".equals(text) && text != null) {
            text = new String(text.getBytes("ISO-8859-1"), Definitions.CONFIG_UNICODE_UTF_8);
            text = text.replace("'", "&apos;");
            text = text.replace("\"", "&quot;");
            text = text.replace(">", "&gt;");
            text = text.replace("<", "&lt;");
            text = text.replace("=", "&equals;");
            text = text.replace("\\", "&bsol;");
        } else {
            text = "";
        }
        sResult = text;
        return sResult.trim();
    }

    /**
     *
     * @param writer
     * @param str
     * @throws IOException
     */
    public static void escape(Writer writer, String str) throws IOException {
        int len = str.length();
        for (int i = 0; i < len; i++) {
            char c = str.charAt(i);
            int ascii = (int) c;
            String entityName = (String) m.get(ascii);
            if (entityName == null) {
                if (c > 0x7F) {
                    writer.write("&#");
                    writer.write(Integer.toString(c, 10));
                    writer.write(';');
                } else {
                    writer.write(c);
                }
            } else {
                writer.write(entityName);
            }
        }
    }

    /**
     *
     * @param sValue
     * @return
     */
    public static String CheckTextNull(String sValue) {
        if (sValue == null) {
            sValue = "";
        } else {
            if (Definitions.CONFIG_EXCEPTION_STRING_ERROR_NULL.equals(sValue.trim().toUpperCase())) {
                sValue = "";
            }
        }
        return sValue.trim();
    }
    
    /**
     *
     * @param sValue
     * @return
     */
    public static String CheckTextNullDecoder(String sValue) {
        if (sValue == null) {
            sValue = "";
        } else {
//            sValue = URLDecoder.decode(sValue);
            if (Definitions.CONFIG_EXCEPTION_STRING_ERROR_NULL.equals(sValue.trim().toUpperCase())) {
                sValue = "";
            }
        }
        return sValue.trim();
    }

    /**
     *
     * @param sValue
     * @return
     */
    public static String CheckTextNullExport(String sValue) {
        if (sValue == null) {
            sValue = "";
        } else {
            if (Definitions.CONFIG_EXCEPTION_STRING_ERROR_NULL.equals(sValue.trim().toUpperCase())) {
                sValue = "";
            }
        }
        return sValue.trim();
    }

    /**
     *
     * @param sText
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String ConvertStringToUnicode(String sText) throws UnsupportedEncodingException {
        String sTextReturn;
        if (sText == null) {
            sTextReturn = null;
        } else {
            String sA = new String(sText.getBytes("ISO-8859-1"), Definitions.CONFIG_UNICODE_UTF_8);
            sTextReturn = sA;
        }
        return sTextReturn;
    }

    /**
     *
     * @param strValue
     * @return
     */
    public static boolean IsInteger(String strValue) {
        if (strValue == null || strValue.trim().isEmpty()) {
            return false;
        }
        for (int i = 0; i < strValue.trim().length(); i++) {
            if (!Character.isDigit(strValue.trim().charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     *
     * @param text
     * @return
     */
    public static String escapeDashDN(String text) {
        String sResult;
        if (!"".equals(text) && text != null) {
            text = text.replace("\\,", "###");
            text = text.replace("\\\\", "!!!");
            text = text.replace("\\+", "@@@");
            text = text.replace("\"", "$$$");
        } else {
            text = "";
        }
        sResult = text;
        return sResult.trim();
    }
    
    /**
     *
     * @param text
     * @return
     */
    public static String escapeHtml_Print(String text) {
        String sResult;
        if (!"".equals(text) && text != null) {
//            text = text.replace("'", "&apos;");
            //text = text.replace("\"", "&quot;");
//            text = text.replace(">", "&gt;");
//            text = text.replace("<", "&lt;");
            //text = text.replace("=", "&equals;");
//            text = text.replace("\\", "&bsol;");
//            text = text.replace("/", "&sol;");
            text = text.replace("&", "&amp;");
            //text = text.replace("*", "&ast;");
            //text = text.replace("@", "&commat;");
        } else {
            text = "";
        }
        sResult = text;
        return sResult.trim();
    }
    
    /**
     *
     * @param text
     * @return
     */
    public static String escapeHtmlDN(String text) {
        String sResult;
        if (!"".equals(text) && text != null) {
            text = text.replace("'", "&apos;");
            text = text.replace("\"", "&quot;");
            text = text.replace(">", "&gt;");
            text = text.replace("<", "&lt;");
            text = text.replace("\\", "&bsol;");
        } else {
            text = "";
        }
        sResult = text;
        return sResult.trim();
    }
    
    public static String checkTextNullAndXSS(String sValue) {
        if (sValue == null) {
            sValue = "";
        } else {
            if (Definitions.CONFIG_EXCEPTION_STRING_ERROR_NULL.equals(sValue.trim().toUpperCase())) {
                sValue = "";
            } else {
                //sValue = sValue.replace(">", "&gt;");
                //sValue = sValue.replace("<", "&lt;");
            }
        }
        return sValue.trim();
    }
}
