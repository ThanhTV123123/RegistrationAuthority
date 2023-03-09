/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ra.configuration;

import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author THANH-PC
 */
public class XSSRequestWrapper extends HttpServletRequestWrapper {
    private static final Logger log = LoggerFactory.getLogger(XSSRequestWrapper.class);

//    private String writer;

    public XSSRequestWrapper(HttpServletRequest servletRequest) {
        super(servletRequest);
//        writer = new String();
    }
    
//    public String getWriter() {
//        return writer;
//    }
//      
//    public String toString() {
//        return writer;
//    }

    @Override
    public String[] getParameterValues(String parameter) {
        String[] values = super.getParameterValues(parameter);

        if (values == null) {
            return null;
        }

        int count = values.length;
        String[] encodedValues = new String[count];
        for (int i = 0; i < count; i++) {
            encodedValues[i] = stripXSS(values[i]);
        }

        return encodedValues;
    }
    
    @Override
    public String getParameter(String parameter) {
        String value = super.getParameter(parameter);

        return stripXSS(value);
    }

    @Override
    public String getHeader(String name) {
        String value = super.getHeader(name);
        return stripXSS(value);
    }

    private String stripXSS(String value) {
        if (value != null) {
            Pattern scriptPattern = Pattern.compile("<script>(\\s*.*?)</script>",
                    Pattern.CASE_INSENSITIVE);
            value = scriptPattern.matcher(value).replaceAll("-");
            scriptPattern = Pattern.compile("</script(\\s*.*?)>",
                    Pattern.CASE_INSENSITIVE);
            value = scriptPattern.matcher(value).replaceAll("-");
            scriptPattern = Pattern.compile("<script(\\s*.*?)>",
                    Pattern.CASE_INSENSITIVE | Pattern.MULTILINE
                    | Pattern.DOTALL);
            value = scriptPattern.matcher(value).replaceAll("-");
            scriptPattern = Pattern.compile("eval\\((.*?)\\)",
                    Pattern.CASE_INSENSITIVE | Pattern.MULTILINE
                    | Pattern.DOTALL);
            value = scriptPattern.matcher(value).replaceAll("-");
            scriptPattern = Pattern.compile("e­xpression\\((.*?)\\)",
                    Pattern.CASE_INSENSITIVE | Pattern.MULTILINE
                    | Pattern.DOTALL);
            value = scriptPattern.matcher(value).replaceAll("-");
            scriptPattern = Pattern.compile("javascript:",
                    Pattern.CASE_INSENSITIVE);
            value = scriptPattern.matcher(value).replaceAll("-");
            scriptPattern = Pattern.compile("vbscript:",
                    Pattern.CASE_INSENSITIVE);
            value = scriptPattern.matcher(value).replaceAll("-");
            scriptPattern = Pattern.compile("onload(.*?)=",
                    Pattern.CASE_INSENSITIVE | Pattern.MULTILINE
                    | Pattern.DOTALL);
            value = scriptPattern.matcher(value).replaceAll("-");

            scriptPattern = Pattern.compile("<+.*(oncontrolselect|oncopy|oncut|ondataavailable|ondatasetchanged|ondatasetcomplete|ondblclick|ondeactivate|ondrag|ondragend|ondragenter|ondragleave|ondragover|ondragstart|ondrop|onerror|onerroupdate|onfilterchange|onfinish|onfocus|onfocusin|onfocusout|onhelp|onkeydown|onkeypress|onkeyup|onlayoutcomplete|onload|onlosecapture|onmousedown|onmouseenter|onmouseleave|onmousemove|onmousout|onmouseover|onmouseup|onmousewheel|onmove|onmoveend|onmovestart|onabort|onactivate|onafterprint|onafterupdate|onbefore|onbeforeactivate|onbeforecopy|onbeforecut|onbeforedeactivate|onbeforeeditocus|onbeforepaste|onbeforeprint|onbeforeunload|onbeforeupdate|onblur|onbounce|oncellchange|onchange|onclick|oncontextmenu|onpaste|onpropertychange|onreadystatechange|onreset|onresize|onresizend|onresizestart|onrowenter|onrowexit|onrowsdelete|onrowsinserted|onscroll|onselect|onselectionchange|onselectstart|onstart|onstop|onsubmit|onunload)+.*=+",
                    Pattern.CASE_INSENSITIVE | Pattern.MULTILINE
                    | Pattern.DOTALL);
            value = scriptPattern.matcher(value).replaceAll("-");

            // Filter emoji expressions
            scriptPattern = Pattern
                    .compile(
                            "[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
                            Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
            value = scriptPattern.matcher(value).replaceAll("-");
            
            // NOTE: It's highly recommended to use the ESAPI library and uncomment the following line to
            // avoid encoded attacks.
            // value = ESAPI.encoder().canonicalize(value);

            // Avoid null characters
            /* OLD value = value.replaceAll("", "");

            // Avoid anything between script tags
            Pattern scriptPattern = Pattern.compile("<script>(\\s*.*?)</script>", Pattern.CASE_INSENSITIVE);
            if (scriptPattern.matcher(value).matches()) {
                log.info("<script>(.*?)</script>");
                value = value.replace("<script>", "");
                value = value.replace("</script>", "");
            }

            // Avoid anything in a src='...' type of expression
            scriptPattern = Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            if (scriptPattern.matcher(value).matches()) {
                value = value.replace("src=", "-");
                value = value.replace("src", "-");
                //value = scriptPattern.matcher(value).replaceAll("");
            }

            scriptPattern = Pattern.compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            if (scriptPattern.matcher(value).matches()) {
                value = value.replace("src=", "-");
                value = value.replace("src", "-");
                //value = scriptPattern.matcher(value).replaceAll("");
            }

            // Remove any lonesome </script> tag
            scriptPattern = Pattern.compile("</script(\\s*.*?)>", Pattern.CASE_INSENSITIVE);
            if (scriptPattern.matcher(value).matches()) {
                value = value.replace("</script", "");
                value = value.replace("<script>", "");
                //value = scriptPattern.matcher(value).replaceAll("");
            }

            // Remove any lonesome <script ...> tag
            scriptPattern = Pattern.compile("<script(\\s*.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            if (scriptPattern.matcher(value).matches()) {
                value = value.replace("<script", "");
                value = value.replace("</script>", "");
                //value = scriptPattern.matcher(value).replaceAll("");
            }

            // Avoid eval(...) expressions
            scriptPattern = Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            if (scriptPattern.matcher(value).matches()) {
                value = value.replace("eval", "");
                //value = scriptPattern.matcher(value).replaceAll("");
            }

            // Avoid expression(...) expressions
            scriptPattern = Pattern.compile("expression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            if (scriptPattern.matcher(value).matches()) {
                value = value.replace("expression", "");
                //value = scriptPattern.matcher(value).replaceAll("");
            }

            // Avoid javascript:... expressions
            scriptPattern = Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE);
            if (scriptPattern.matcher(value).matches()) {
                value = value.replace("javascript:", "");
                //value = scriptPattern.matcher(value).replaceAll("");
            }

            // Avoid vbscript:... expressions
            scriptPattern = Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE);
            if (scriptPattern.matcher(value).matches()) {
                value = value.replace("vbscript:", "");
                //value = scriptPattern.matcher(value).replaceAll("");
            }

            // Avoid onload= expressions
            scriptPattern = Pattern.compile("onload(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            if (scriptPattern.matcher(value).matches()) {
                value = value.replace("onload =", "");
                value = value.replace("onload=", "");
                //value = scriptPattern.matcher(value).replaceAll("");
            }*/
        }
        return value;
    }
}
