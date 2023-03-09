<%-- 
    Document   : CertViewPrintCert
    Created on : Aug 3, 2018, 11:57:58 AM
    Author     : THANH-PC
--%>

<%@page import="vn.ra.uat.PrintReport"%>
<%@page import="vn.ra.object.CERTIFICATION"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="../Admin/ConnectionParam.jsp" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script src="../js/Language.js"></script>
        <script src="../js/process_javajs.js"></script>
        <link href="../style/customportal.min.css" rel="stylesheet">
        <script type="text/javascript" src="../js/jquery.js"></script>
        <link rel="stylesheet" href="../js/sweetalert.css"/>
        <script src="../js/sweetalert-dev.js"></script>
        <script type="text/javascript" src="../Css/GlobalAlert.js"></script>
        <script src="../js/jquery.PrintArea.js"></script>
        <title>In giay dk</title>
        <script>
            function load_home() {
//                document.getElementById("content").innerHTML = '<object type="text/html" data="D:\\Project\\TMS CA\\TMS EFY\\capCTSDN.html" ></object>';
                $("#content").innerHTML("D:\\Project\\TMS CA\\TMS EFY\\capCTSDN.html");
            }
            function printex() {
//                load_home();
                $('#content').printArea({
                    popWd: 1200,
                    popHt: 900,
                    mode: "popup",
                    popClose: false
                });
            }
        </script>
    </head>
    <body>
        <input id="btnSave" value="Submit" type="button" onclick="printex();" />
        <br/>
        <%        String sID = request.getParameter("id");
            String sessLanguage = request.getSession(false).getAttribute("sessVN").toString().trim();
            String AGENT_ID_LOG = EscapeUtils.CheckTextNull(request.getSession(false).getAttribute("SessAgentID").toString().trim());
            String SessUserAgentID = EscapeUtils.CheckTextNull(request.getSession(false).getAttribute("SessUserAgentID").toString().trim());
            // check agency
            boolean isAccessAgency = true;
            String sResultHTML = "";
            int pCERTIFICATION_AUTHORITY_ID = 0;
            String pCERTIFICATION_SN = "";
            String pPERSONAL_NAME = "";
            String pCOMPANY_NAME = "";
            String pISSUER_SUBJECT = "";
            String pSUBJECT = "";
            String pKEY_SIZE = "";
            String pEXPIRATION_CERT = "";
            String pEXPIRATION_CONTRACT = "";
            String pEXFFECTIVE_CERT = "";
            String pEXFFECTIVE_CONTRACT = "";
            String sAGENT_ID;
            CERTIFICATION[][] rsReq = new CERTIFICATION[1][];
            db.S_BO_CERTIFICATION_DETAIL(sID, sessLanguage, rsReq);
            if (rsReq[0].length > 0) {
                pEXFFECTIVE_CERT = EscapeUtils.CheckTextNull(rsReq[0][0].EFFECTIVE_DT);
                pEXPIRATION_CERT = EscapeUtils.CheckTextNull(rsReq[0][0].EXPIRATION_DT);
                pEXFFECTIVE_CONTRACT = EscapeUtils.CheckTextNull(rsReq[0][0].EFFECTIVE_DT);
                pEXPIRATION_CONTRACT = EscapeUtils.CheckTextNull(rsReq[0][0].EXPIRATION_CONTRACT_DT);
                pISSUER_SUBJECT = EscapeUtils.CheckTextNull(rsReq[0][0].ISSUER_SUBJECT);
                pPERSONAL_NAME = EscapeUtils.CheckTextNull(rsReq[0][0].PERSONAL_NAME);
                pCOMPANY_NAME = EscapeUtils.CheckTextNull(rsReq[0][0].COMPANY_NAME);
                pSUBJECT = EscapeUtils.CheckTextNull(rsReq[0][0].SUBJECT);
                pKEY_SIZE = EscapeUtils.CheckTextNull(rsReq[0][0].KEY_SIZE);
                pCERTIFICATION_AUTHORITY_ID = rsReq[0][0].CERTIFICATION_AUTHORITY_ID;
                sAGENT_ID = String.valueOf(rsReq[0][0].BRANCH_ID);
                if (!AGENT_ID_LOG.equals(Definitions.CONFIG_AGENT_ROOT)) {
                    if (!sAGENT_ID.equals(SessUserAgentID)) {
                        isAccessAgency = false;
                    }
                }
            } else {
                isAccessAgency = false;
            }
            if (isAccessAgency == true) {
                int[] intResult = new int[1];
                String TinhNang = "Client Authentication, Secure Email, Digital Signature, Non-Repudiation, Key Encipherment";
                String pathXSLT = "";
                CERTIFICATION_AUTHORITY[][] rsCA = new CERTIFICATION_AUTHORITY[1][];
                db.S_BO_CERTIFICATION_AUTHORITY_DETAIL(String.valueOf(pCERTIFICATION_AUTHORITY_ID), rsCA);
                if (rsCA[0].length > 0) {
                    pathXSLT = EscapeUtils.CheckTextNull(rsCA[0][0].TEMPLATE_LICENSE_CERTIFICATION);
                }
                String pathXML = PrintReport.createXMLCertificate(pPERSONAL_NAME, pCOMPANY_NAME, pSUBJECT,
                        pISSUER_SUBJECT, pCERTIFICATION_SN, pEXFFECTIVE_CERT, pEXPIRATION_CERT, pEXFFECTIVE_CONTRACT,
                        pEXPIRATION_CONTRACT, pKEY_SIZE, TinhNang);
                sResultHTML = PrintReport.createStringHtmlInString(pathXSLT, pathXML, null, false, false, intResult);
            }
        %>
        <div id="content"><%= sResultHTML%></div>
    </body>
</html>
