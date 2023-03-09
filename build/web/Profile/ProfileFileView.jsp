<%-- 
    Document   : ProfileFileView
    Created on : Oct 8, 2021, 3:51:28 PM
    Author     : vanth
--%>

<%@ page trimDirectiveWhitespaces="true" %>
<%@page import="vn.ra.process.DocxDocumentMergerAndConverter"%>
<%@page import="org.apache.commons.io.IOUtils"%>
<%@page import="vn.mobileid.fms.client.JCRFile"%>
<%@page import="vn.mobileid.fms.client.JCRConfig"%>
<%@page import="vn.ra.process.JackRabbitCommon"%>
<%@page import="org.apache.http.Header"%>
<%@page import="org.apache.http.client.methods.CloseableHttpResponse"%>
<%@page import="vn.ra.process.ConnectFileToPartner"%>
<%@page import="vn.ra.utility.PropertiesContent"%>
<%@page import="java.util.ArrayList"%>
<%@page import="vn.ra.process.SessionUploadFileCert"%>
<%@page import="com.fasterxml.jackson.databind.ObjectMapper"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="../Admin/ConnectionParam.jsp" %>
<%@include file="../Admin/CommonPagingList.jsp" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>File View Manager</title>
    </head>
    <body>
        <%
            try {
            byte[] imageBytes = null;
            String strView="";
            String sFileName = "";
            String sMimeType = "";
            String pFILE_MANAGER_ID = EscapeUtils.CheckTextNull(request.getParameter("idFile"));
            if("0".equals(pFILE_MANAGER_ID)){
                SessionUploadFileCert cartToken = (SessionUploadFileCert) request.getSession(false).getAttribute("sessUploadFileCertProfile");
                if (cartToken != null) {
                    String pFILE_NAME = EscapeUtils.CheckTextNull(request.getParameter("idName"));
                    if(!"".equals(pFILE_NAME)) {
                        ArrayList<FILE_PROFILE_DATA> ds = cartToken.getGH();
                        if(ds.size() > 0)
                        {
                            for (FILE_PROFILE_DATA mhIP : ds) {
                                if(mhIP.FILE_NAME.equals(pFILE_NAME))
                                {
                                    imageBytes = mhIP.FILE_STREAM;
                                    sMimeType = mhIP.FILE_MIMETYPE;
                                    break;
                                }
                            }
                        }
                    }
                }
            } else {
                FILE_MANAGER[][] rsFILE_MANAGER = new FILE_MANAGER[1][];
                db.S_BO_FILE_MANAGER_DETAIL(pFILE_MANAGER_ID, "1", rsFILE_MANAGER);
                if (rsFILE_MANAGER[0].length > 0) {
                    String sUUID = EscapeUtils.CheckTextNull(rsFILE_MANAGER[0][0].UUID);
                    sFileName = EscapeUtils.CheckTextNull(rsFILE_MANAGER[0][0].FILE_NAME);
                    sMimeType = EscapeUtils.CheckTextNull(rsFILE_MANAGER[0][0].MIME_TYPE_NAME);
                    String sJRBConfig = EscapeUtils.CheckTextNull(rsFILE_MANAGER[0][0].DMS_PROPERTIES);
                    String sJRB_Source =  PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_SOURCE);
                    if(sJRB_Source.equals(Definitions.CONFIG_JACK_RABBIT_SOURCE_EFY))
                    {
                        String sIP_CONNECT = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_IP);
                        String sHTTP_CONNECT = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_PROTOCOL);
                        String sCONTEXT_CONNECT = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_CONTEXT);
                        String sPORT_CONNECT = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_PORT);
                        String sDEFAULT_USER = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_USERNAME);
                        String sDEFAULT_PASS = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_PASSWORD);
                        String sOWNERCODE_CONNECT = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_OWNERCODE);
                        String sAPPCODE_CONNECT = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_APPCODE);
                        String sFUNCTION_CONNECT = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_FUNCTION_DOWN);
                        CloseableHttpResponse pHttpRes = ConnectFileToPartner.loadFileParner(sIP_CONNECT, sHTTP_CONNECT,
                            sCONTEXT_CONNECT, Integer.parseInt(sPORT_CONNECT), sDEFAULT_USER,
                            sDEFAULT_PASS, sOWNERCODE_CONNECT, sAPPCODE_CONNECT, sFUNCTION_CONNECT, sUUID);
                        InputStream data = pHttpRes.getEntity().getContent();
                        if (data != null) {
                            imageBytes = IOUtils.toByteArray(data);
                        }
                    } else if(sJRB_Source.equals(Definitions.CONFIG_JACK_RABBIT_SOURCE_JRB)) {
                        String sJRB_Host = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_HOST);
                        String sJRB_UserID = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_USERID);
                        String sJRB_UserPass = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_USER_PASSWORD);
                        String sJRB_MaxSession = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_MAX_SESSION);
                        String sJRB_MaxFileFolder = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_MAXFILE_INFOLDER);
                        String sJRB_PrefixFolder = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_PREFIX_FOLDER);
                        String sJRB_WorkSpace = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_WORKSPACE);
                        JCRConfig jcrConfig = JackRabbitCommon.getJCRConfig(sJRB_Host, sJRB_UserID, sJRB_UserPass, Integer.parseInt(sJRB_MaxSession),
                            Integer.parseInt(sJRB_MaxFileFolder), sJRB_WorkSpace, sJRB_PrefixFolder);
                        JCRFile jrbFile = JackRabbitCommon.getInstance(jcrConfig).downloadFile(sUUID);
                        if (jrbFile.getStream() != null) {
                            imageBytes = IOUtils.toByteArray(jrbFile.getStream());
                        }
                    } else {
                    }
                }
            }
        %>
        <%
                if(imageBytes != null)
                {
                    String[] sMimeTypeOut = new String[1];
                    DocxDocumentMergerAndConverter rs = new DocxDocumentMergerAndConverter();
                    byte[] imageBytesNew = rs.byteWordToPDF(imageBytes, sMimeType, sMimeTypeOut);
                    response.setContentType(sMimeTypeOut[0]);
                    response.setContentLength(imageBytesNew.length);
                    response.getOutputStream().write(imageBytesNew);
                    response.getOutputStream().flush();
                    response.getOutputStream().close();
                    out.clear();
                    out = pageContext.pushBody();
                }
            } catch(Exception e)
            {
                CommonFunction.LogExceptionJSP(null, e.getMessage(), e);
            }
        %>
    </body>
</html>