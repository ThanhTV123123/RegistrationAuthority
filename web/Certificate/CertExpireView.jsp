<%-- 
    Document   : CertExpireView
    Created on : Feb 21, 2019, 9:53:22 AM
    Author     : THANH-PC
--%>

<%@page import="java.net.URLEncoder"%>
<%@page import="java.util.ArrayList"%>
<%@page import="vn.ra.process.SessionUploadFileCert"%>
<%@page import="vn.ra.process.EncodeSOPIN"%>
<%@page import="com.fasterxml.jackson.databind.ObjectMapper"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="../Admin/ConnectionParam.jsp" %>
<%@include file="../Admin/CommonPagingList.jsp" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <META HTTP-EQUIV="PRAGMA" CONTENT="NO-CACHE"> 
        <META HTTP-EQUIV="Expires" CONTENT="-1">
        <link href="../style/bootstrap.min.css" rel="stylesheet">
        <link href="../style/font-awesome.css" rel="stylesheet">
        <link href="../style/nprogress.css" rel="stylesheet">
        <link href="../style/custom.min.css" rel="stylesheet">
        <script src="../js/Language.js"></script>
        <script src="../js/process_javajs.js"></script>
        <link href="../style/customportal.min.css" rel="stylesheet">
        <script type="text/javascript" src="../js/jquery.js"></script>
        <link rel="stylesheet" href="../js/sweetalert.css"/>
        <script src="../js/sweetalert-dev.js"></script>
        <script type="text/javascript" src="../Css/GlobalAlert.js"></script>
        <script src="../js/jquery.PrintArea.js"></script>
        <title></title>
        <script type="text/javascript">
            changeFavicon("../");
            document.title = certlist_title_detail;
            $(document).ready(function () {
                $('.loading-gif').hide();
//                if(localStorage.getItem("LOCAL_PARAM_CERTEXPIRELIST") !== null && localStorage.getItem("LOCAL_PARAM_CERTEXPIRELIST") !== "null")
//                {
//                    var vParamUrl = getUrlParam("id", "");
//                    if(vParamUrl !== localStorage.getItem("LOCAL_PARAM_CERTEXPIRELIST"))
//                    {
//                        window.location = "../Admin/Home.jsp";
//                    }
//                } else {
//                    window.location = "CertExpireList.jsp";
//                }
            });
            function closeForm()
            {
                $.ajax({
                    type: "post",
                    url: "../UserCommon",
                    data: {
                        idParam: 'backformpage',
                        idSession: 'RefreshCertExpireSess'
                    },
                    cache: false,
                    success: function (html) {
                        var arr = sSpace(html);
                        if (arr === "0")
                        {
                            window.location = "CertExpireList.jsp";
                        }
                        else
                        {
                            window.location = "CertExpireList.jsp";
                        }
                    }
                });
                return false;
            }
        </script>
        <style>
            fieldset.scheduler-border {
                border: 1px solid #E6E9ED !important;
                padding: 0 1.2em 10px 1.2em !important;
                margin: 0 0 12px 0 !important;
                -webkit-box-shadow:  0px 0px 0px 0px #E6E9ED;
                box-shadow:  0px 0px 0px 0px #E6E9ED;
            }
            .table > thead > tr > th, .table > tbody > tr > td{vertical-align: middle;}
            .table > thead > tr > th{border-bottom: none;}
            .btn{margin-bottom: 0px;}
        </style>
    </head>
    <body>
        <%         
        if (session.getAttribute("sUserID") != null) {
            String anticsrf = "" + Math.random();
            request.getSession().setAttribute("anticsrf", anticsrf);
            String SessAgentID = session.getAttribute("SessAgentID").toString().trim();
            String SessUserAgentID = session.getAttribute("SessUserAgentID").toString().trim();
            String SessRoleID = session.getAttribute("RoleID_ID").toString().trim();
        %>
        <div style="width: 100%; text-align: center; position: fixed;z-index: 1000;top: 0; padding-top: 300px;
             left: 0; height: 100%;" class="loading-gif">
            <img src="../Images/ajax-loader1.gif" alt="Please wait..." />
        </div>
        <%                                        CERTIFICATION[][] rs = new CERTIFICATION[1][];
        String strCertificate = "";
        String strIsCert = "0";
        String sMaxLengthFile = cogCommon.GetPropertybyCode(Definitions.CONFIG_JACK_RABBIT_MAX_LENGTH_FILE).trim();
        session.setAttribute("sessUploadFileCert", null);
            try {
                String ids = EscapeUtils.CheckTextNull(request.getParameter("id"));
                String sessLanguageGlobal = session.getAttribute("sessVN").toString();
                if (EscapeUtils.IsInteger(ids) == true) {
                    db.S_BO_CERTIFICATION_DETAIL(EscapeUtils.escapeHtml(ids), sessLanguageGlobal, rs);
                    if (rs[0].length > 0) {
                        String sOWNER_ID = String.valueOf(rs[0][0].CERTIFICATION_OWNER_ID);
                        boolean isAccessAgencyPage = true;
                        if (!SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                            BRANCH[][] branchAccess = (BRANCH[][]) session.getAttribute("sessTreeBranchSystem");
                            isAccessAgencyPage = CommonFunction.checkBranchTreeInvalidCert(rs[0][0].BRANCH_ID, branchAccess);
//                            if (!String.valueOf(rs[0][0].BRANCH_ID).equals(SessUserAgentID)) {
//                                isAccessAgencyPage = false;
//                            }
                        }
                        if (isAccessAgencyPage == true) {
                            int intPKI_FORMFACTOR_ID = rs[0][0].PKI_FORMFACTOR_ID;
                            String sTOKEN_SN = EscapeUtils.CheckTextNull(rs[0][0].TOKEN_SN);
                            String sCERTIFICATION_SN = EscapeUtils.CheckTextNull(rs[0][0].CERTIFICATION_SN);
                            String strEMAIL_CONTRACT = EscapeUtils.CheckTextNull(rs[0][0].EMAIL_CONTRACT);
                            String strPHONE_CONTRACT = EscapeUtils.CheckTextNull(rs[0][0].PHONE_CONTRACT);
                            String strCERTIFICATION_STATE_DESC = EscapeUtils.CheckTextNull(rs[0][0].CERTIFICATION_STATE_DESC);
                            String strCERTIFICATION_ATTR_STATE_DESC = EscapeUtils.CheckTextNull(rs[0][0].CERTIFICATION_ATTR_STATE_DESC);
                            strCertificate = EscapeUtils.CheckTextNull(rs[0][0].CERTIFICATION);
                            if(!"".equals(strCertificate))
                            {
                                strIsCert = "1";
                            }
                            boolean isP12 = false;
//                            if(rs[0][0].CERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_CODE_ENTERPRISE_SIGNSERVER
//                                || rs[0][0].CERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_CODE_STAFF_SIGNSERVER
//                                || rs[0][0].CERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_CODE_PERSONAL_SIGNSERVER
//                                || rs[0][0].CERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_CODE_ENTERPRISE_SSL
//                                || rs[0][0].CERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_CODE_STAFF_SSL
//                                || rs[0][0].CERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_CODE_PERSONAL_SSL
//                                || rs[0][0].CERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_CODE_ENTERPRISE_CODESIGNING
//                                || rs[0][0].CERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_CODE_STAFF_CODESIGNING
//                                || rs[0][0].CERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_CODE_PERSONAL_CODESIGNING)
//                            {
//                                if("".equals(EscapeUtils.CheckTextNull(rs[0][0].CSR)))
//                                {
//                                    isP12 = true;
//                                    strIsCert = "0";
//                                }
//                            }
                            if(intPKI_FORMFACTOR_ID == Definitions.CONFIG_PKI_FORMFACTOR_ID_SOFT_TOKEN)
                            {
                                if(rs[0][0].PRIVATE_KEY_ENABLED == true)
                                {
                                    isP12 = true;
                                    strIsCert = "0";
                                }
                            }
                            String strDN = EscapeUtils.CheckTextNull(rs[0][0].SUBJECT);
                            if(!"".equals(strDN))
                            {
                                strDN = strDN.replace("\\,", "###");
                            }
                            String strFEE_AMOUNT = "0";
                            if(rs[0][0].FEE_AMOUNT != 0)
                            {
                                strFEE_AMOUNT = com.convertMoneyAnotherZero(rs[0][0].FEE_AMOUNT);
                            }
                            String strCREATED_DT = EscapeUtils.CheckTextNull(rs[0][0].CREATED_DT);
                            String strCREATED_BY = EscapeUtils.CheckTextNull(rs[0][0].CREATED_BY);
                            String strMODIFIED_DT = EscapeUtils.CheckTextNull(rs[0][0].MODIFIED_DT);
                            String strMODIFIED_BY = EscapeUtils.CheckTextNull(rs[0][0].MODIFIED_BY);
        %>
        <div class="x_title">
            <h2><i class="fa fa-list-ul"></i> <span id="idLblTitleEdits" style="color: #36526D;"></span></h2>
            <script>$("#idLblTitleEdits").text(certlist_title_detail);</script>
            <ul class="nav navbar-right panel_toolbox">
                <li>
                    <input id="btnClose" class="btn btn-info" type="button" onclick="closeForm();" />
                </li>
                <script>
                    document.getElementById("btnClose").value = global_fm_button_close;
                </script>
            </ul>
            <div class="clearfix"></div>
        </div>
        <div class="x_content">
            <form name="myname" method="post" class="form-horizontal">
                <input type="hidden" id="sID" name="sID" hidden="true" readonly="true" value="<%= rs[0][0].ID%>">
                <input type="hidden" name="CsrfToken" value="<%=anticsrf%>"/>
                <input type="hidden" name="idHiddenDN" id="idHiddenDN" value="<%=EscapeUtils.escapeHtmlDN(strDN)%>"/>
                    <%
                        if(!"".equals(sCERTIFICATION_SN))
                        {
                    %>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                <label id="idLblTitleCertSN"></label>
                            </label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input type="text" name="CERTIFICATION_SN" readonly value="<%= sCERTIFICATION_SN%>" class="form-control123">
                            </div>
                        </div>
                        <script>
                            $("#idLblTitleCertSN").text(global_fm_serial);
                        </script>
                    </div>
                    <%
                        }
                    %>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                <label id="idLblTitleCertState"></label>
                            </label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input type="text" readonly name="CERTIFICATION_STATE_DESC" value="<%= strCERTIFICATION_STATE_DESC%>" class="form-control123">
                            </div>
                        </div>
                        <script>
                            $("#idLblTitleCertState").text(global_fm_Status_cert);
                        </script>
                    </div>
<!--                                                <div class="form-group" style="padding: 0px 0px 10px 0px;margin: 0;">
                        <label class="control-label123"><script>document.write(global_fm_Status_request);</script></label>
                        <input type="text" readonly name="CERTIFICATION_ATTR_STATE_DESC" value="<= strCERTIFICATION_ATTR_STATE_DESC%>" class="form-control123">
                    </div>-->
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                <label id="idLblTitleCertAttrType"></label>
                            </label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input type="text" name="CERTIFICATION_ATTR_TYPE" readonly value="<%= EscapeUtils.CheckTextNull(rs[0][0].CERTIFICATION_ATTR_TYPE_DESC) %>" class="form-control123">
                            </div>
                        </div>
                        <script>
                            $("#idLblTitleCertAttrType").text(global_fm_requesttype);
                        </script>
                    </div>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                <label id="idLblTitleCA"></label>
                            </label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input type="text" readonly id="CERTIFICATION_AUTHORITY" value="<%= EscapeUtils.CheckTextNull(rs[0][0].CERTIFICATION_AUTHORITY_DESC) %>" class="form-control123">
                            </div>
                        </div>
                        <script>
                            $("#idLblTitleCA").text(global_fm_ca);
                        </script>
                    </div>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                <label id="idLblTitleCertPurpose"></label>
                            </label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input type="text" readonly id="CERTIFICATION_PURPOSE" value="<%= EscapeUtils.CheckTextNull(rs[0][0].CERTIFICATION_PURPOSE_DESC) %>" class="form-control123">
                            </div>
                        </div>
                        <script>
                            $("#idLblTitleCertPurpose").text(global_fm_certpurpose);
                        </script>
                    </div>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                <label id="idLblTitleDuration"></label>
                            </label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input type="text" readonly id="DURATION" value="<%= EscapeUtils.CheckTextNull(rs[0][0].CERTIFICATION_PROFILE_DESC) %>" class="form-control123">
                            </div>
                        </div>
                        <script>
                            $("#idLblTitleDuration").text(global_fm_duration_cts);
                        </script>
                    </div>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                <label id="idLblTitleFeeAmount"></label>
                            </label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input type="text" name="strFEE_AMOUNT" value="<%= strFEE_AMOUNT%>" readonly class="form-control123"/>
                            </div>
                        </div>
                        <script>
                            $("#idLblTitleFeeAmount").text(global_fm_amount_fee);
                        </script>
                    </div>
                    <%
                        boolean isHasFileOfUser = true;
                        FILE_MANAGER[][] rsFileMana = new FILE_MANAGER[1][];
                        db.S_BO_FILE_MANAGER_GET_BY_CERTIFICATION_AND_OWNER(EscapeUtils.escapeHtml(ids), sOWNER_ID, sessLanguageGlobal, rsFileMana);
                        if (!SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                            if(rsFileMana[0].length <= 0)
                            {
                                isHasFileOfUser = false;
                            } else {
                                for(FILE_MANAGER rsFile : rsFileMana[0])
                                {
                                    isHasFileOfUser = false;
                                    if(!rsFile.FILE_PROFILE_NAME.equals(Definitions.CONFIG_FILE_PROFILE_CODE_E_CONTRACT))
                                    {
                                        isHasFileOfUser = true;
                                        break;
                                    }
                                }
                            }
                        }
                        if(rs[0][0].CERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_OPERATED)
                        {
                            if(rsFileMana[0].length <= 0)
                            {
                                isHasFileOfUser = false;
                            } else {
                                for(FILE_MANAGER rsFile : rsFileMana[0])
                                {
                                    isHasFileOfUser = false;
                                    if(!rsFile.FILE_PROFILE_NAME.equals(Definitions.CONFIG_FILE_PROFILE_CODE_E_CONTRACT))
                                    {
                                        isHasFileOfUser = true;
                                        break;
                                    }
                                }
                            }
                        }
                    %>
                    <%
                        if(isHasFileOfUser == true) {
                    %>

                    <div id="idDivShowFileMana" style="clear: both;">
                    <%
                        String sJSON = "";
                        CERTIFICATION_PURPOSE[][] rsPURPOSE = new CERTIFICATION_PURPOSE[1][];
                        db.S_BO_CERTIFICATION_PURPOSE_DETAIL_BY_CERTIFICATION_ATTR_TYPE(String.valueOf(rs[0][0].CERTIFICATION_PURPOSE_ID), "", rsPURPOSE);
                        if(rsPURPOSE[0].length > 0)
                        {
                            sJSON = EscapeUtils.CheckTextNull(rsPURPOSE[0][0].FILE_PROPERTIES);
                        }
                        if(!"".equals(sJSON)) {
                            SessionUploadFileCert cartIP = (SessionUploadFileCert) session.getAttribute("sessUploadFileCert");
                            cartIP = new SessionUploadFileCert();
                            if(rsFileMana[0].length > 0)
                            {
                                for(FILE_MANAGER rsFile : rsFileMana[0])
                                {
                                    FILE_PROFILE_DATA item = new FILE_PROFILE_DATA();
                                    item.FILE_MANAGER_ID = rsFile.ID;
                                    item.FILE_NAME = rsFile.FILE_NAME;
                                    item.FILE_PROFILE = rsFile.FILE_PROFILE_NAME;
                                    item.FILE_SIZE = (double) rsFile.FILE_SIZE;
                                    item.FILE_MIMETYPE = rsFile.MIME_TYPE_NAME;
                                    item.FILE_STREAM = null;
                                    cartIP.AddRoleFunctionsList(item);
                                }
                                session.setAttribute("sessUploadFileCert", cartIP);
                            }
                            ObjectMapper oMapperParse = new ObjectMapper();
                            FILE_PROFILE_JSON itemParsePush = oMapperParse.readValue(sJSON, FILE_PROFILE_JSON.class);
                            int jFile = 1;
                            for (FILE_PROFILE_JSON.Attribute attribute : itemParsePush.getAttributes()) {
                                String sRemark = attribute.getRemark().trim();
                                String sName = attribute.getName().trim();
                                if(!sName.equals(Definitions.CONFIG_FILE_PROFILE_CODE_E_CONTRACT)) {
                                    String sNameInID = attribute.getName().trim() + String.valueOf(jFile);
                                    if(attribute.getEnabled() == true) {
                    %>
                    <fieldset class="scheduler-border">
                        <legend class="scheduler-border"><%= sRemark%></legend>
                        <div style="padding: 10px 0 10px 0;" id="idDiv<%= sName%>" class="table-responsive">
                             <table id="idTable<%= sName%>" class="table table-bordered table-striped projects">
                                 <thead>
                                    <th id="idLblTitleTableSST<%= sNameInID%>"></th>
                                    <th id="idLblTitleTableFileName<%= sNameInID%>"></th>
                                    <th id="idLblTitleTableSize<%= sNameInID%>"></th>
                                    <th id="idLblTitleTableAction<%= sNameInID%>"></th>
                                    <script>
                                        $("#idLblTitleTableSST"+'<%= sNameInID%>').text(global_fm_STT);
                                        $("#idLblTitleTableFileName"+'<%= sNameInID%>').text(global_fm_file_name);
                                        $("#idLblTitleTableSize"+'<%= sNameInID%>').text(global_fm_size);
                                        $("#idLblTitleTableAction"+'<%= sNameInID%>').text(global_fm_action);
                                    </script>
                                </thead>
                                <tbody id="idTBody<%= sName%>">
                                    <%
                                        boolean isHasFile = false;
                                        if (cartIP != null) {
                                            int j = 1;
                                            ArrayList<FILE_PROFILE_DATA> ds = cartIP.getGH();
                                            for (FILE_PROFILE_DATA mhIP : ds) {
                                                String sNameInID_inner = sNameInID + String.valueOf(j);
                                                if(mhIP.FILE_PROFILE.equals(sName)) {
                                                    isHasFile = true;
                                    %>
                                    <tr>
                                        <td><%= String.valueOf(j)%></td>
                                        <td><%= EscapeUtils.CheckTextNull(mhIP.FILE_NAME)%></td>
                                        <td><%= com.convertMoneyFromDouble(mhIP.FILE_SIZE / 1024)%></td>
                                        <td>
                                            <a id="idLblTitleTableLinkDown<%= sNameInID_inner%>" style="cursor: pointer;" class="btn btn-info btn-xs" onclick="DownloadTempFile('<%= mhIP.FILE_MANAGER_ID%>', '<%= anticsrf%>');"></a>
                                            <!--<i class="fa fa-pencil"></i><script>document.write(global_fm_down);</script></a>-->
                                            <script>
                                                $("#idLblTitleTableLinkDown<%= sNameInID_inner%>").append('<i class="fa fa-pencil"></i>' + global_fm_down);
                                            </script>
                                        </td>
                                    </tr>
                                    <%
                                                j++;
                                            }
                                        }
                                        if(isHasFile == false)
                                        {
                                    %>
                                    <tr><td colspan="4" id="idLblTitleTableNoFile<%= sName%>"></td></tr>
                                    <script>
                                        $("#idLblTitleTableNoFile"+'<%= sName%>').text(global_no_file_list);
                                    </script>
                                    <%
                                            }
                                        } else {
                                    %>
                                    <tr><td colspan="4" id="idLblTitleTableNoFile<%= sName%>"></td></tr>
                                    <script>
                                        $("#idLblTitleTableNoFile"+'<%= sName%>').text(global_no_file_list);
                                    </script>
                                    <%
                                        }
                                    %>
                                </tbody>
                             </table>
                        </div>
                    </fieldset>
                    <%
                                    }
                                }
                            }
                        }
                    %>
                    </div>
                    <script>
                        function DownloadTempFile(vFILE_MANAGER_ID, idCSRF)
                        {
                            $('body').append('<div id="over"></div>');
                            $(".loading-gif").show();
                            $.ajax({
                                type: "post",
                                url: "../DownloadFileCSR",
                                data: {
                                    idParam: 'downloadfilemanager',
                                    pFILE_MANAGER_ID: vFILE_MANAGER_ID,
                                    CsrfToken: idCSRF
                                },
                                cache: false,
                                success: function (html)
                                {
                                    var myStrings = sSpace(html).split('#');
                                    if (myStrings[0] === "0")
                                    {
                                        $(".loading-gif").hide();
                                        $('#over').remove();
                                        var f = document.myname;
                                        f.method = "post";
                                        f.action = '../DowloadFile?filename=' + myStrings[1];
                                        f.submit();
                                    }
                                    else if (myStrings[0] === JS_EX_CSRF)
                                    {
                                        funCsrfAlert();
                                    }
                                    else if (myStrings[0] === JS_EX_NO_DATA_WRITE)
                                    {
                                        funErrorAlert(global_no_data);
                                    }
                                    else if (myStrings[0] === JS_EX_LOGIN)
                                    {
                                        RedirectPageLoginNoSess(global_alert_login);
                                    }
                                    else if (myStrings[0] === JS_EX_ANOTHERLOGIN)
                                    {
                                        RedirectPageLoginNoSess(global_alert_another_login);
                                    }
                                    else
                                    {
                                        funErrorAlert(global_errorsql);
                                    }
                                    $(".loading-gif").hide();
                                    $('#over').remove();
                                }
                            });
                            return false;
                        }
                        function LoadFileManage(idPurpose)
                        {
                            $.ajax({
                                type: 'POST',
                                url: '../JSONCommon',
                                data: {
                                    idParam: 'loadfilemanageofpurpose',
                                    idPurpose: idPurpose
                                },
                                cache: false,
                                success: function (html) {
                                    if (html.length > 0)
                                    {
                                        var obj = JSON.parse(html);
                                        if (obj[0].Code === "0")
                                        {
                                            $("#idDivShowFileMana").empty();
                                            var content = "";
                                            for (var i = 0; i < obj.length; i++) {
                                                content += '<fieldset class="scheduler-border">'+
                                                    '<legend class="scheduler-border">'+obj[i].REMARK+'</legend>'+
                                                    '<div class="form-group" style="padding: 0px;margin: 0;">'+
                                                        '<label class="control-label123">'+global_fm_browse_file+'</label>'+
                                                        '<input type="file" id="input-file'+obj[i].NAME+'" style="width: 100%;"'+
                                                            'onchange="calUploadFile(this, \'' + obj[i].NAME + '\');" class="btn btn-default btn-file select_file" multiple>'+
                                                        '<div style="height:10px;"></div><label class="control-label123" style="color:red;font-weight: 200;">' + global_fm_browse_cert_note + '<%= Integer.parseInt(sMaxLengthFile) / 1024 %>' + ' MB. ' + global_fm_browse_cert_addnote + '</label>'+
                                                    '</div>'+
                                                    '<div style="padding: 10px 0 10px 0;display:none;" id="idDiv'+obj[i].NAME+'">'+
                                                         '<table id="idTable'+obj[i].NAME+'" class="table table-striped projects">'+
                                                             '<thead>'+
                                                                '<th>'+global_fm_STT+'</th>'+
                                                                '<th>'+global_fm_file_name+'</th>'+
                                                                '<th>'+global_fm_size+'</th>'+
                                                                '<th>'+global_fm_action+'</th>'+
                                                            '</thead>'+
                                                            '<tbody id="idTBody'+obj[i].NAME+'">'+
                                                            '</tbody>'+
                                                         '</table>'+
                                                    '</div>'+
                                                '</fieldset>';
                                            }
                                            $("#idDivShowFileMana").append(content);
                                            $("#idDivShowFileMana").css("display", "");
                                        } else if(obj[0].Code === "1")
                                        {
                                            $("#idDivShowFileMana").css("display", "none");
                                        }
                                        else {
                                            funErrorAlert(global_errorsql);
                                        }
                                    }
                                }
                            });
                            return false;
                        }
                    </script>
                    <%
                        }
                    %>
                    <%
                        if(!"".equals(EscapeUtils.CheckTextNull(rs[0][0].COMMENT))) {
                            ObjectMapper oMapperParse = new ObjectMapper();
                            CERTIFICATION_COMMENT itemParsePush = oMapperParse.readValue(EscapeUtils.CheckTextNull(rs[0][0].COMMENT), CERTIFICATION_COMMENT.class);
                            String sDeclineReason = itemParsePush.certificateDeclineReason;
                    %>
                    <div class="form-group" style="padding: 0px 0px 10px 0px;margin: 0;">
                        <label id="idLblTitleDeclineReason" class="control-label123"></label>
                        <input type="text" name="DESC_DECLINE_VIEW" id="DESC_DECLINE_VIEW" readonly 
                            value="<%= sDeclineReason%>" class="form-control123"/>
                        <script>$("#idLblTitleDeclineReason").text(global_fm_decline_desc);</script>
                    </div>
                    <%
                        }
                    %>
                    <%
                        if (!"".equals(strCertificate)) {
                    %>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label id="idLblTitleCert" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                            <div class="col-sm-7" style="padding-right: 0px;padding-top: 6px;">
                                <textarea style="height: 85px;display: none;" readonly name="CERTIFICATE" id="CERTIFICATE" class="form-control123"><%= strCertificate%></textarea>
                            </div>
                            <script>$("#idLblTitleCert").text(global_fm_Certificate);</script>
                        </div>
                    </div>
                    <%
                        if(isP12 == false) {
                    %>
                    <div id="idViewCSR" class="form-group" style="display: none;padding: 10px 0px 0 0px;margin: 0;clear: both;">
                        <fieldset class="scheduler-border">
                            <legend class="scheduler-border" id="idLblTitleGroupCert"></legend>
                            <script>$("#idLblTitleGroupCert").text(global_group_cert);</script>
                            <%
                                if (!"".equals(strCertificate)) {
                                    int[] intRes = new int[1];
                                    Object[] sss = new Object[2];
                                    String[] tmp = new String[3];
                                    com.VoidCertificateComponents(strCertificate, sss, tmp, intRes);
                                    if (intRes[0] == 0 && sss.length > 0) {
                                        Object strSubjectDN = sss[0].toString().replace(", ", "\n");
                                        Object strIssuerDN = sss[1].toString().replace(", ", "\n");
                                        String strNotBefore = EscapeUtils.CheckTextNull(tmp[1]);
                                        String strNotAfter = EscapeUtils.CheckTextNull(tmp[2]);
                            %>
                            <div class="col-sm-6" style="padding-left: 0;">
                                <div class="form-group" style="vertical-align:middle;">
                                    <label id="idLblTitleCompanyPrin" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                    <div class="col-sm-7" style="padding-right: 0px;">
                                        <textarea id="idCompanyPrin" class="form-control123" readonly="true" name="idCompanyPrin" style="height: 75px;"><%= strSubjectDN%></textarea>
                                    </div>
                                </div>
                            </div>
                            <div class="col-sm-6" style="padding-left: 0;">
                                <div class="form-group">
                                    <label id="idLblTitleIssuerPrin" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                    <div class="col-sm-7" style="padding-right: 0px;">
                                        <textarea id="idIssuerPrin" class="form-control123" readonly="true" name="idIssuerPrin" style="height: 75px;"><%= strIssuerDN%></textarea>
                                    </div>
                                </div>
                            </div>
                            <div class="col-sm-6" style="padding-left: 0;">
                                <div class="form-group">
                                    <label id="idLblTitleCertValid" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                    <div class="col-sm-7" style="padding-right: 0px;">
                                        <input id="idCertValid" class="form-control123" type="text" readonly="true" value="<%= strNotBefore%>"/>
                                    </div>
                                </div>
                            </div>
                            <div class="col-sm-6" style="padding-left: 0;">
                                <div class="form-group">
                                    <label id="idLblTitleCertExpire" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                    <div class="col-sm-7" style="padding-right: 0px;">
                                        <input id="idCertExpire" class="form-control123" type="text" readonly="true" value="<%= strNotAfter%>"/>
                                    </div>
                                </div>
                            </div>
                            <script>
                                $("#idLblTitleCompanyPrin").text(global_fm_company);
                                $("#idLblTitleIssuerPrin").text(global_fm_issue);
                                $("#idLblTitleCertValid").text(global_fm_valid_cert);
                                $("#idLblTitleCertExpire").text(global_fm_Expire_cert);
                            </script>
                            <% } else {
                            %>
                            <div class="form-group" style="padding: 10px 0px 0 0px;margin: 0;">
                                <label class="control-label123" id="idLblTitleNoCert"></label>
                                <script>$("#idLblTitleNoCert").text(global_req_info_cert);</script>
                            </div>
                            <%
                                    }
                                }
                            %>
                        </fieldset>
                    </div>
                    <%
                        }
                    %>
                    <script>
                        function popupViewCSRS()
                        {
                            document.getElementById('idViewCSR').style.display = '';
//                            document.getElementById('idAHide').style.display = '';
//                            document.getElementById('idAShow').style.display = 'none';
                        }
                    </script>
                    <%
                        }
                    %>
                    <%
                        if(isP12 == true) {
                            if(SessRoleID.equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN) || SessRoleID.equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN_CHILD))
                            {
                                String sPROPERTIES = EscapeUtils.CheckTextNull(rs[0][0].PROPERTIES);
                                if(!"".equals(sPROPERTIES)) {
                                    ObjectMapper objectMapper = new ObjectMapper();
                                    String strPassword = "";
                                    CERTIFICATION_PROPERTIES_JSON itemParsePush = objectMapper.readValue(sPROPERTIES, CERTIFICATION_PROPERTIES_JSON.class);
                                    for (int i = 0; i < itemParsePush.getAttributes().size(); i++) {
                                        if(itemParsePush.getAttributes().get(i).getKey().equals(CERTIFICATION_PROPERTIES_JSON.Attribute.KEY_KEYSTORE_PASSWORD))
                                        {
                                            EncodeSOPIN encript = new EncodeSOPIN();
                                            strPassword = encript.decode(itemParsePush.getAttributes().get(i).getValue());
                                            break;
                                        }
                                    }
                    %>
                    <style>
                        .field-icon {
                            float: right;
                            margin-left: -25px;
                            margin-top: -25px;
                            position: relative;
                            z-index: 2;
                          }
                    </style>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                <label id="idLblTitlePassP12"></label>
                            </label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <div style="width: 100%;padding-top: 0px;clear: both;">
                                    <div style="float: left;width: 90%;">
                                        <input id="idPW" class="form-control123" readonly type="password" value="<%= strPassword%>"/>
                                    </div>
                                    <div style="float: right;text-align: right;">
                                        <span toggle="#idPW" class="fa fa-fw fa-eye field_icon toggle-password"></span>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <script>$("#idLblTitlePassP12").text(global_fm_pass_p12);</script>
                    </div>
                    <script>
                        $(document).ready(function () {
                            $(".toggle-password").click(function() {
                                $(this).toggleClass("fa-eye fa-eye-slash");
                                var input = $($(this).attr("toggle"));
                                if (input.attr("type") === "password") {
                                  input.attr("type", "text");
                                } else {
                                  input.attr("type", "password");
                                }
                            });
                        });
                    </script>
                    <%
                            }
                        }
                    %>
                    <fieldset class="scheduler-border" style="clear: both;">
                        <legend class="scheduler-border" id="idLblTitleGroupCert"></legend>
                        <script>$("#idLblTitleGroupCert").text(global_group_cert);</script>
                        <div class="col-sm-6" style="padding-left: 0;">
                            <div class="form-group" style="vertical-align:middle;">
                                <label id="idLblTitleCompanyPrin" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                <div class="col-sm-7" style="padding-right: 0px;">
                                    <textarea id="idCompanyPrin" class="form-control123" readonly="true" name="idCompanyPrin" style="height: 75px;"><%= EscapeUtils.CheckTextNull(rs[0][0].SUBJECT)%></textarea>
                                </div>
                            </div>
                        </div>
                        <div class="col-sm-6" style="padding-left: 0;">
                            <div class="form-group">
                                <label id="idLblTitleIssuerPrin" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                <div class="col-sm-7" style="padding-right: 0px;">
                                    <textarea id="idIssuerPrin" class="form-control123" style="height: 75px;" readonly="true" name="idIssuerPrin"><%= EscapeUtils.CheckTextNull(rs[0][0].ISSUER_SUBJECT)%></textarea>
                                </div>
                            </div>
                        </div>
                        <div class="col-sm-6" style="padding-left: 0;">
                            <div class="form-group">
                                <label id="idLblTitleCertValid" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                <div class="col-sm-7" style="padding-right: 0px;">
                                    <input id="idEFFECTIVE_DT" class="form-control123" type="text" readonly="true" value="<%= EscapeUtils.CheckTextNull(rs[0][0].EFFECTIVE_DT) %>"/>
                                </div>
                            </div>
                        </div>
                        <div class="col-sm-6" style="padding-left: 0;">
                            <div class="form-group">
                                <label id="idLblTitleCertExpire" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                <div class="col-sm-7" style="padding-right: 0px;">
                                    <input id="idEXPIRATION_DT" class="form-control123" type="text" readonly="true" value="<%= EscapeUtils.CheckTextNull(rs[0][0].EXPIRATION_DT)%>"/>
                                </div>
                            </div>
                        </div>
                        <script>
                            $("#idLblTitleCompanyPrin").text(global_fm_company);
                            $("#idLblTitleIssuerPrin").text(global_fm_issue);
                            $("#idLblTitleCertValid").text(global_fm_valid_cert);
                            $("#idLblTitleCertExpire").text(global_fm_Expire_cert);
                        </script>
                    </fieldset>
                    <%
                        }
                    %>
                    <%
                        if(!"".equals(sTOKEN_SN) && CommonFunction.checkViewTokenValid(sTOKEN_SN) == true)
                        {
                    %>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label id="idLblTitleTokenSN" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input type="text" name="TOKEN_SN" readonly value="<%= sTOKEN_SN%>" class="form-control123">
                            </div>
                        </div>
                        <script>
                            $("#idLblTitleTokenSN").text(token_fm_tokenid);
                        </script>
                    </div>
                    <%
                        }
                    %>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                <label id="idLblTitlePhoneContact"></label>
                            </label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input type="text" readonly name="PHONE_CONTRACT" value="<%= strPHONE_CONTRACT%>" class="form-control123">
                            </div>
                        </div>
                        <script>$("#idLblTitlePhoneContact").text(global_fm_phone_contact);</script>
                    </div>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                <label id="idLblTitleEmailContact"></label>
                            </label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input type="text" readonly name="EMAIL_CONTRACT" value="<%= strEMAIL_CONTRACT%>" class="form-control123">
                            </div>
                        </div>
                        <script>$("#idLblTitleEmailContact").text(global_fm_email_contact);</script>
                    </div>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label id="idLblTitleAgentName" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input type="text" readonly name="BranchOfficeName" id="BranchOfficeName" value="<%= EscapeUtils.CheckTextNull(rs[0][0].BRANCH_DESC)%>" class="form-control123"/>
                            </div>
                        </div>
                        <script>
                            $("#idLblTitleAgentName").text(global_fm_Branch);
                        </script>
                    </div>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label id="idLblTitleCreatedBy" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input class="form-control123" type="text" name="CREATED_BY" readonly value="<%= strCREATED_BY%>"/>
                            </div>
                        </div>
                        <script>
                            $("#idLblTitleCreatedBy").text(global_fm_user_create);
                        </script>
                    </div>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label id="idLblTitleModifiedBy" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input class="form-control123" type="text" name="MODIFIED_BY" readonly value="<%= strMODIFIED_BY%>"/>
                            </div>
                        </div>
                        <script>
                            $("#idLblTitleModifiedBy").text(global_fm_user_endupdate);
                        </script>
                    </div>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label id="idLblTitleCreatedDate" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input class="form-control123" type="text" name="REQUEST_TIME" readonly value="<%= strCREATED_DT%>"/>
                            </div>
                        </div>
                        <script>
                            $("#idLblTitleCreatedDate").text(global_fm_date_create);
                        </script>
                    </div>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label id="idLblTitleModifiedDate" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input class="form-control123" type="text" name="MODIFIED_DT" readonly value="<%= strMODIFIED_DT%>"/>
                            </div>
                        </div>
                        <script>
                            $("#idLblTitleModifiedDate").text(global_fm_date_endupdate);
                        </script>
                    </div>
            </form>
        </div>
        <%
                    }
                }
            }
        } catch (Exception e) {
            checkExFinnaly = 1;
            CommonFunction.LogExceptionJSP(request.getRequestURI(), e.getMessage(), e);
        } finally {
            if (checkExFinnaly == 1) {
                checkExFinnaly = 0;
        %>
        <script type="text/javascript">
            window.onload = function () {
                RedirectPageError();
            }();
        </script>
        <%
                }
            }
        %>
        <script>
            $(document).ready(function () {
                if('<%= strIsCert%>' === "1")
                {
                    popupViewCSRS();
                }
            });
        </script>
        <!--<script src="../style/bootstrap.min.js"></script>-->
        <!--<script src="../style/custom.min.js"></script>-->
        <%
        } else {
        %>
        <script type="text/javascript">
            window.onload = function () {
                RedirectPageLoginNoSess(global_alert_login);
            }();
        </script>
        <%
            }
        %>
    </body>
</html>