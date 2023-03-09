<%-- 
    Document   : RenewCertList
    Created on : Jul 11, 2018, 2:07:28 PM
    Author     : THANH-PC
--%>

<%@page import="vn.ra.process.CommonReferServlet"%>
<%@page import="java.net.URLEncoder"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="../Admin/ConnectionParam.jsp" %>
<%@include file="../Admin/CommonPagingList.jsp" %>
<%  response.setHeader("Cache-Control", "no-cache");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", -1);
    String strAlertAllTimes = "0";
    String sRepresentEnabled = LoadParamSystem.getParamStart(Definitions.CONFIG_REGISTER_REPRESENT_FORM_ENABLED);
    String isCALoad = LoadParamSystem.getParamStart(Definitions.CONFIG_IS_WHICH_ABOUT_CA);
    String isUIDCollection = LoadParamSystem.getParamStart(Definitions.CONFIG_IS_UID_COLLECTION_DISPLAY_ENABLED);
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <META HTTP-EQUIV="PRAGMA" CONTENT="NO-CACHE">
        <META HTTP-EQUIV="Expires" CONTENT="-1">
        <link href="../style/bootstrap.min.css" rel="stylesheet">
        <link href="../style/font-awesome.css" rel="stylesheet">
        <link href="../style/nprogress.css" rel="stylesheet">
        <link href="../style/custom.min.css" rel="stylesheet">
        <%
            String sNewRefreshJS = cogCommon.GetPropertybyCode(Definitions.CONFIG_JS_REFRESH_STRING_RANDOM);
        %>
        <script src="../js/Language.js?t=<%=sNewRefreshJS%>"></script>
        <script src="../js/process_javajs.js?t=<%=sNewRefreshJS%>"></script>
        <link href="../style/customportal.min.css" rel="stylesheet">
        <script type="text/javascript" src="../js/jquery.js"></script>
        <link rel="stylesheet" href="../js/sweetalert.css"/>
        <script src="../js/sweetalert-dev.js"></script>
        <link href="../style/customportal.min.css" rel="stylesheet">
        <script type="text/javascript" src="../Css/GlobalAlert.js?t=<%=sNewRefreshJS%>"></script>
        <!--<script type="text/javascript" src="../js/jquery-1.12.4.js"></script>-->
        <!--<script type="text/javascript" src="../js/jquery-ui.js"></script>-->
        <link rel="stylesheet" type="text/css" media="all" href="../style/jquery-ui.css" />
        <title></title>
        <script language="javascript">
            changeFavicon("../");
            document.title = certlist_title_list;
            $(document).ready(function () {
                localStorage.setItem("expandViewPrint", null);
                localStorage.setItem("expandViewAction", null);
                localStorage.setItem("LOCAL_PARAM_RENEWCERTLIST", null);
                $('#demo1').daterangepicker({
                    singleDatePicker: true,
                    showDropdowns: true
                }, function (start, end, label) {
                    console.log(start.toISOString(), end.toISOString(), label);
                });
                $('#demo2').daterangepicker({
                    singleDatePicker: true,
                    showDropdowns: true
                }, function (start, end, label) {
                    console.log(start.toISOString(), end.toISOString(), label);
                });
                $('.loading-gif').hide();
                localStorage.setItem("TransferPage", null);
                $('#myModalRegisterPrint').modal({
                    backdrop: 'static',
                    keyboard: true,
                    show: false
                });
                $('#idCheck').removeClass("js-switch");
                $('#idCheck').addClass("js-switch");
                $('[data-toggle="tooltipPrefix"]').tooltip();
                
            });
            function popupChangeCert(id)
            {
                $('body').append('<div id="over"></div>');
                $(".loading-gif").show();
                $("#contentEdit").empty();
                $('#contentEdit').load('ChangeCertView.jsp', {id:id}, function () {
                    $(".loading-gif").hide();
                    $('#over').remove();
                    $('#idX_Panel_Edit').css("display", "");
                    goToByScroll("contentEdit");
                });
            }
            function popupRevokeCert(id)
            {
                $('body').append('<div id="over"></div>');
                $(".loading-gif").show();
                $("#contentEdit").empty();
                $('#contentEdit').load('RevokeCertView.jsp', {id:id}, function () {
                    $(".loading-gif").hide();
                    $('#over').remove();
                    $('#idX_Panel_Edit').css("display", "");
                    goToByScroll("contentEdit");
                });
            }
            
            function popupDetailCert(id)
            {
                $('body').append('<div id="over"></div>');
                $(".loading-gif").show();
                $("#contentEdit").empty();
                $('#contentEdit').load('CertView.jsp', {id:id}, function () {
                    $(".loading-gif").hide();
                    $('#over').remove();
                    $('#idX_Panel_Edit').css("display", "");
                    goToByScroll("contentEdit");
                });
            }
            function popupEditCert(id)
            {
                $('body').append('<div id="over"></div>');
                $(".loading-gif").show();
                $("#contentEdit").empty();
                $('#contentEdit').load('CertificateEdit.jsp', {id:id}, function () {
                    $(".loading-gif").hide();
                    $('#over').remove();
                    $('#idX_Panel_Edit').css("display", "");
                    goToByScroll("contentEdit");
                });
            }
            function PrintPreview(vText) {
                var popupWin = window.open('', '_blank', 'width=850,height=900,location=no,left=200px');
                popupWin.document.open();
                popupWin.document.write('<html><title></title><link rel="stylesheet" type="text/css" href="Print.css" media="screen"/></head><body onload="window.print()">');
                popupWin.document.write(vText);
                popupWin.document.write('</html>');
                popupWin.document.close();
            }
            function popupPrintCertList(id, idCSRF)
            {
                $('body').append('<div id="over"></div>');
                $(".loading-gif").show();
                $.ajax({
                    type: "post",
                    url: "../PrintFormCommon",
                    data: {
                        idParam: 'printcert',
                        id: id,
                        CsrfToken: idCSRF
                    },
                    cache: false,
                    success: function (html)
                    {
                        var myStrings = sSpace(html).split('###');
                        if (myStrings[0] === "0")
                        {
                            $(".loading-gif").hide();
                            $('#over').remove();
                            PrintPreview(myStrings[1]);
                        }
                        else if (myStrings[0] === JS_EX_CSRF)
                        {
                            funCsrfAlert();
                        } else if (myStrings[0] === JS_EX_LOGIN)
                        {
                            RedirectPageLoginNoSess(global_alert_login);
                        }
                        else if (myStrings[0] === JS_EX_ANOTHERLOGIN)
                        {
                            RedirectPageLoginNoSess(global_alert_another_login);
                        }
                        else
                        {
                            if (myStrings[1] === "1") {
                                funErrorAlert(certprofile_exists_code);
                            }
                            else {
                                funErrorAlert(global_errorsql);
                            }
                        }
                        $(".loading-gif").hide();
                        $('#over').remove();
                    }
                });
                return false;
            }
            function popupPrintHandoverList_bk(id)
            {
                localStorage.setItem("LOCAL_PARAM_RENEWCERTLIST", id);
                localStorage.setItem("TransferPage", "list");
                window.location = "PrintHandover.jsp?id="+ id;
            }
            function popupPrintRegis(id, isEnterprise, idRegis)
            {
                ShowDialogList(id, isEnterprise, idRegis);
            }
            function popupReIssueCert(id)
            {
                $('body').append('<div id="over"></div>');
                $(".loading-gif").show();
                $("#contentEdit").empty();
                $('#contentEdit').load('ReIssueCertView.jsp', {id:id}, function () {
                    $(".loading-gif").hide();
                    $('#over').remove();
                    $('#idX_Panel_Edit').css("display", "");
                    goToByScroll("contentEdit");
                });
            }
            function popupRenewCert(id)
            {
                $('body').append('<div id="over"></div>');
                $(".loading-gif").show();
                $("#contentEdit").empty();
                $('#contentEdit').load('RenewCertView.jsp', {id:id}, function () {
                    $(".loading-gif").hide();
                    $('#over').remove();
                    $('#idX_Panel_Edit').css("display", "");
                    goToByScroll("contentEdit");
                });
            }
            function popupBuyCertMore(id)
            {
                $('body').append('<div id="over"></div>');
                $(".loading-gif").show();
                $("#contentEdit").empty();
                $('#contentEdit').load('BuyCertMore.jsp', {id:id}, function () {
                    $(".loading-gif").hide();
                    $('#over').remove();
                    $('#idX_Panel_Edit').css("display", "");
                    goToByScroll("contentEdit");
                });
            }
            
            function popupSuspendCert(id)
            {
                $('body').append('<div id="over"></div>');
                $(".loading-gif").show();
                $("#contentEdit").empty();
                $('#contentEdit').load('SuspendCertView.jsp', {id:id}, function () {
                    $(".loading-gif").hide();
                    $('#over').remove();
                    $('#idX_Panel_Edit').css("display", "");
                    goToByScroll("contentEdit");
                });
            }
            
            function popupRecoveryCert(id)
            {
                $('body').append('<div id="over"></div>');
                $(".loading-gif").show();
                $("#contentEdit").empty();
                $('#contentEdit').load('RecoveryCertView.jsp', {id:id}, function () {
                    $(".loading-gif").hide();
                    $('#over').remove();
                    $('#idX_Panel_Edit').css("display", "");
                    goToByScroll("contentEdit");
                });
            }
            function searchForm(id)
            {
                var idFromCreateDate = document.form.FromCreateDate.value;
                var idToCreateDate = document.form.ToCreateDate.value;
                if (!JSCheckDateDDMMYYYYSlash(idFromCreateDate))
                {
                    funErrorAlert(global_fm_FromDate + global_error_invalid);
                    return false;
                }
                if (!JSCheckDateDDMMYYYYSlash(idToCreateDate))
                {
                    funErrorAlert(global_fm_ToDate + global_error_invalid);
                    return false;
                }
                if (parseDate(idToCreateDate).getTime() < parseDate(idFromCreateDate).getTime())
                {
                    funErrorAlert(global_check_datesearch);
                    return false;
                }
                else
                {
                    document.getElementById("idHiddenLoad").value = JS_STR_GRID_SEARCH_RESET;
                    document.getElementById("tempCsrfToken").value = id;
                    var f = document.form;
                    f.method = "post";
                    f.action = '';
                    f.submit();
                }
            }
            function popupDownloadCert(id, idCSRF)
            {
                $('body').append('<div id="over"></div>');
                $(".loading-gif").show();
                $.ajax({
                    type: "post",
                    url: "../DownloadFileCSR",
                    data: {
                        idParam: 'certhasid',
                        id: id,
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
            function popupHideCTSList()
            {
                document.getElementById('idViewCSRSList').style.display = 'none';
                document.getElementById('idAHideList').style.display = 'none';
                document.getElementById('idAShowList').style.display = '';
            }
            function popupViewCSRSList()
            {
                document.getElementById('idViewCSRSList').style.display = '';
                document.getElementById('idAHideList').style.display = '';
                document.getElementById('idAShowList').style.display = 'none';
            }
        </script>
        <style>
            .projects th{font-weight: bold;}.navbar-right{margin-right: 0;padding-right:10px;}
            fieldset.scheduler-border {
                border: 1px solid #E6E9ED !important;
                padding: 0 1.2em 10px 1.2em !important;
                margin: 0 0 12px 0 !important;
                -webkit-box-shadow:  0px 0px 0px 0px #E6E9ED;
                box-shadow:  0px 0px 0px 0px #E6E9ED;
            }
            @media (min-width: 768px){.modal-dialog{width: 900px;}}
            .modal-header{
                padding: 10px 10px 10px 10px;border-bottom:0px;
            }
            .x_panel{padding: 10px 10px}
            .col-sm-4{padding-right: 5px;}
            legend.scheduler-border {
                color: <%="1".equals(sRepresentEnabled) ? "#C43131" : ""%>;
                <%="1".equals(sRepresentEnabled) ? "padding:0 10px 0 0;" : ""%>;
            }
            .btn-info{
                <%= isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA) ? "background-color:#31B910;border-color:#31B910" : ""%>
                /*color:#fff;background-color:#C43131;border-color:#C43131*/
            }
            .btn-info:hover,.btn-info:focus,.btn-info:active,.btn-info.active {
                <%= isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA) ? "background-color:#31B910;border-color:#31B910" : ""%>
                /*color:#fff;background-color:#C43131;border-color:#C43131*/
            }
        </style>
    </head>
    <body class="nav-md">
        <%
            if (session.getAttribute("sUserID") != null) {
                String anticsrf = "" + Math.random();
                request.getSession().setAttribute("anticsrf", anticsrf);
                String SessAgentID = session.getAttribute("SessAgentID").toString().trim();
                String SessUserAgentID = session.getAttribute("SessUserAgentID").toString().trim();
                String SessUserID = session.getAttribute("UserID").toString().trim();
                String SessRoleID_ID = session.getAttribute("RoleID_ID").toString().trim();
                ROLE_DATA[][] sessFunctionCert = (ROLE_DATA[][]) session.getAttribute("SessRoleSet_Cert");
                String sessTreeArrayBranchID = session.getAttribute("sessTreeArrayBranchIDSystem").toString().trim();
                String SessRoleID = session.getAttribute("RoleID_ID").toString().trim();
                String SessLevelBranch = session.getAttribute("sessLevelBranch").toString().trim();
                Config conf = new Config();
                String printRegisterCAOption = conf.GetPropertybyCode(Definitions.CONFIG_FORM_REGISTRATION_CERT_PRINT);
                String printDeliveryCAOption = conf.GetPropertybyCode(Definitions.CONFIG_FORM_DELIVERY_CERT_PRINT);
                String printChangeReissueEnabled = conf.GetPropertybyCode(Definitions.CONFIG_FORM_CHANGEINFO_REISSUE_CERT_PRINT_ENABLED);
        %>
        <div style="width: 100%; text-align: center; position: fixed;z-index: 1000;top: 0; padding-top: 300px;
             left: 0; height: 100%;" class="loading-gif">
            <img src="../Images/ajax-loader1.gif" alt="Please wait..." />
        </div>
        <div class="container body">
            <div class="main_container">
                <div class="col-md-3 left_col">
                    <div class="left_col scroll-view">
                        <%@ include file="../Modules/Header.jsp" %>
                        <br />
                        <div id="sidebar-menu" class="main_menu_side hidden-print main_menu">
                            <%@ include file="../Modules/MenuLeft.jsp" %>
                        </div>
                    </div>
                </div>
                <div class="top_nav">
                    <%@ include file="../Modules/Navigate.jsp" %>
                    <script>
                        document.getElementById("idNameURL").innerHTML = certlist_title_list;
                    </script>
                </div>
                <div class="right_col" role="main">
                    <div class="">
                        <div class="row">
                            <%                            int status = 1000;
                                int statusLoad = 0;
                                int j = 1;
                                String nameHiddenLoad = request.getParameter("nameHiddenLoad");
                                String tempIPagNo = request.getParameter("iPagNo");
                                int iTotRslts = com.Converter(request.getParameter("iTotRslts"));
                                int iTotPags = com.Converter(request.getParameter("iTotPags"));
                                int iPagNo = com.Converter(tempIPagNo);
                                int iPaNoSS = iPagNo;
                                int cPagNo = com.Converter(request.getParameter("cPagNo"));
                                if (Definitions.CONFIG_GRID_SEARCH_RESET.equals(nameHiddenLoad)) {
                                    tempIPagNo = "null";
                                    iPagNo = 0;
                                    cPagNo = 0;
                                    iPaNoSS = 0;
                                }
                                int iEnRsNo = 0;
                                if (iPagNo == 0) {
                                    iPagNo = 0;
                                } else {
                                    iPagNo = Math.abs((iPagNo - 1) * iSwRws);
                                }
                                String hasPaging = "0";
                                if (tempIPagNo != null && !"null".equals(tempIPagNo) && !"".equals(tempIPagNo)) {
                                    hasPaging = "1";
                                }
                                String strMess = "";
                                String strExpandFilter = "0";
                                try {
                                    String isLoadPeronalPrefix = "";
                                    String isLoadEnterprisePrefix = "";
                                    CERTIFICATION[][] rsPgin = new CERTIFICATION[1][];
                                    String sessLanguageGlobal = session.getAttribute("sessVN").toString();
                                    String sRevokeEnabledDefault = "0";
                                    GENERAL_POLICY[][] sessGeneralPolicy1 = (GENERAL_POLICY[][]) session.getAttribute("sessGeneralPolicy_System");
                                    if (sessGeneralPolicy1[0].length > 0) {
                                        for(GENERAL_POLICY rsPolicy1: sessGeneralPolicy1[0])
                                        {
                                            if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_BO_DISALLOW_REVOKING_AFTER_ENABLED_CROSS_CHECK_STATE))
                                            {
                                                sRevokeEnabledDefault = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                                break;
                                            }
                                        }
                                    }
                                    if (session.getAttribute("RefreshRenewCertSess") != null && session.getAttribute("sessFromCreateDateRenewCert") != null
                                            && session.getAttribute("sessToCreateDateRenewCert") != null) {
                                        try {
                                            session.setAttribute("RefreshRenewCertSessPaging", "1");
                                            session.setAttribute("SearchSharePagingRenewCert", "0");
                                            statusLoad = 1;
                                            String sEnterpriseCert = "";
                                            String sPersonalCert = "";
                                            String ToCreateDate = (String) session.getAttribute("sessToCreateDateRenewCert");
                                            String FromCreateDate = (String) session.getAttribute("sessFromCreateDateRenewCert");
                                            String CERT_SN = (String) session.getAttribute("sessCERT_SNRenewCert");
                                            String FORM_FACTOR = (String) session.getAttribute("sessFormFactorRenewCert");
                                            String TOKEN_SN = (String) session.getAttribute("sessTOKEN_SNRenewCert");
                                            String BranchOffice = (String) session.getAttribute("sessBranchOfficeRenewCert");
                                            String USER = (String) session.getAttribute("sessUserRenewCert");

                                            String PERSONAL_NAME = (String) session.getAttribute("sessPERSONAL_NAMERenewCert");
                                            String COMPANY_NAME = (String) session.getAttribute("sessCOMPANY_NAMERenewCert");
                                            String DOMAIN_NAME = (String) session.getAttribute("sessDOMAIN_NAMERenewCert");
                                            String TAX_CODE = (String) session.getAttribute("sessTAX_CODERenewCert");
                                            String BUDGET_CODE = (String) session.getAttribute("sessBUDGET_CODERenewCert");
                                            String DECISION = (String) session.getAttribute("sessDECISIONRenewCert");
                                            String P_ID = (String) session.getAttribute("sessP_IDRenewCert");
                                            String CCCD = (String) session.getAttribute("sessCCCDRenewCert");
                                            String PASSPORT = (String) session.getAttribute("sessPASSPORTRenewCert");
                                            String CERTIFICATION_ATTR_TYPE = (String) session.getAttribute("sessCERTIFICATION_ATTR_TYPERenewCert");
                                            String CERTIFICATION_PURPOSE = (String) session.getAttribute("sessCERTIFICATION_PURPOSERenewCert");
                                            String DEVICE_UUID_SEARCH = (String) session.getAttribute("sessDEVICE_UUIDRenewCert");
                                            String IsTokenLost = (String) session.getAttribute("sessTokenLostRenewCert");
                                            String IsByOwner = (String) session.getAttribute("sessIsByOwnerRenewCert");
                                            String CERTIFICATION_AUTHORITY_SEARCH = (String) session.getAttribute("sessCARenewCert");
                                            String SERVICE_TYPE_SEARCH = (String) session.getAttribute("sessSERVICE_TYPERenewCert");

                                            strAlertAllTimes = (String) session.getAttribute("AlertAllTimeSRenewCert");
                                            session.setAttribute("RefreshRenewCertSess", null);
                                            session.setAttribute("sessFromCreateDateRenewCert", FromCreateDate);
                                            session.setAttribute("sessToCreateDateRenewCert", ToCreateDate);
                                            session.setAttribute("sessCERT_SNRenewCert", CERT_SN);
                                            session.setAttribute("sessFormFactorRenewCert", FORM_FACTOR);
                                            session.setAttribute("sessTOKEN_SNRenewCert", TOKEN_SN);
                                            session.setAttribute("sessPERSONAL_NAMERenewCert", PERSONAL_NAME);
                                            session.setAttribute("sessCOMPANY_NAMERenewCert", COMPANY_NAME);
                                            session.setAttribute("sessDOMAIN_NAMERenewCert", DOMAIN_NAME);
                                            session.setAttribute("sessTAX_CODERenewCert", TAX_CODE);
                                            session.setAttribute("sessBUDGET_CODERenewCert", BUDGET_CODE);
                                            session.setAttribute("sessDECISIONRenewCert", DECISION);
                                            session.setAttribute("sessP_IDRenewCert", P_ID);
                                            session.setAttribute("sessCCCDRenewCert", CCCD);
                                            session.setAttribute("sessPASSPORTRenewCert", PASSPORT);
                                            session.setAttribute("sessCERTIFICATION_ATTR_TYPERenewCert", CERTIFICATION_ATTR_TYPE);
                                            session.setAttribute("sessCERTIFICATION_PURPOSERenewCert", CERTIFICATION_PURPOSE);
                                            session.setAttribute("sessDEVICE_UUIDRenewCert", DEVICE_UUID_SEARCH);
                                            session.setAttribute("sessTokenLostRenewCert", IsTokenLost);
                                            session.setAttribute("sessIsByOwnerRenewCert", IsByOwner);
                                            session.setAttribute("sessCARenewCert", CERTIFICATION_AUTHORITY_SEARCH);
                                            session.setAttribute("sessSERVICE_TYPERenewCert", SERVICE_TYPE_SEARCH);
                                            session.setAttribute("sessBranchOfficeRenewCert", BranchOffice);
                                            session.setAttribute("sessUserRenewCert", USER);
                                            session.setAttribute("AlertAllTimeSRenewCert", strAlertAllTimes);
                                            if(isUIDCollection.equals("1")) {
                                                String sENTERPRISE_ID = (String) session.getAttribute("sessENTERPRISE_IDRenewCert");
                                                String sPERSONAL_ID = (String) session.getAttribute("sessPERSONAL_IDRenewCert");
                                                String sENTERPRISE_PREFIX = (String) session.getAttribute("sessENTERPRISE_PREFIXRenewCert");
                                                String sPERSONAL_PREFIX = (String) session.getAttribute("sessPERSONAL_PREFIXRenewCert");
                                                session.setAttribute("sessENTERPRISE_IDRenewCert", sENTERPRISE_ID);
                                                session.setAttribute("sessPERSONAL_IDRenewCert", sPERSONAL_ID);
                                                session.setAttribute("sessENTERPRISE_PREFIXRenewCert", sENTERPRISE_PREFIX);
                                                session.setAttribute("sessPERSONAL_PREFIXRenewCert", sPERSONAL_PREFIX);
                                                isLoadEnterprisePrefix = sENTERPRISE_PREFIX;
                                                isLoadPeronalPrefix = sPERSONAL_PREFIX;
                                                if(!"".equals(sENTERPRISE_ID)){
                                                    sEnterpriseCert = sENTERPRISE_PREFIX + "%"+ sENTERPRISE_ID+"%";
                                                }
                                                if(!"".equals(sPERSONAL_ID)){
                                                    sPersonalCert = sPERSONAL_PREFIX + "%" +sPERSONAL_ID+"%";
                                                }
                                            }
                                            if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(CERTIFICATION_ATTR_TYPE)) {
                                                CERTIFICATION_ATTR_TYPE = "";
                                            }
                                            if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(CERTIFICATION_PURPOSE)) {
                                                CERTIFICATION_PURPOSE = "";
                                            }
                                            if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(FORM_FACTOR)) {
                                                FORM_FACTOR = "";
                                            }
                                            if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(IsTokenLost)) {
                                                IsTokenLost = "";
                                            }
                                            if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(USER)) {
                                                USER = "";
                                            }
                                            if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(BranchOffice)) {
                                                BranchOffice = "";
                                            }
                                            if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(CERTIFICATION_AUTHORITY_SEARCH)) {
                                                CERTIFICATION_AUTHORITY_SEARCH = "";
                                            }
                                            if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(SERVICE_TYPE_SEARCH)) {
                                                SERVICE_TYPE_SEARCH = "";
                                            }
                                            if("1".equals(strAlertAllTimes))
                                            {
                                                FromCreateDate = "";
                                                ToCreateDate = "";
                                            }
                                            if(!"".equals(CERT_SN) || !"".equals(TOKEN_SN) || !"".equals(DOMAIN_NAME) || !"".equals(IsTokenLost)
                                                || !"".equals(CERTIFICATION_AUTHORITY_SEARCH) || !"".equals(SERVICE_TYPE_SEARCH)
                                                || !"".equals(DEVICE_UUID_SEARCH) || !"0".equals(IsByOwner)
                                                || (!"".equals(BranchOffice) && !BranchOffice.equals(SessUserAgentID))
                                                || (!"".equals(USER) && !USER.equals(SessUserID)) || !"".equals(sEnterpriseCert) || !"".equals(sPersonalCert))
                                            {
                                                strExpandFilter = "1";
                                            }
                                            if(SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                                if(!"".equals(BranchOffice) && !"".equals(USER))
                                                {
                                                    SessUserAgentID = BranchOffice;
                                                }
                                            }
                                            if("".equals(USER)) {
                                                if(!SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                                    if(SessRoleID.equals(Definitions.CONFIG_ROLE_ID_AGENT_USER) || SessRoleID.equals(Definitions.CONFIG_ROLE_ID_AGENT_ACCOUNTANT)) {
                                                        USER = SessUserID;
                                                    }
                                                }
                                            }
                                            if(!isUIDCollection.equals("1")) {
                                                String[] sUIDResult = new String[2];
                                                CommonReferServlet.collectFieldToUID(EscapeUtils.escapeHtmlSearch(TAX_CODE), EscapeUtils.escapeHtmlSearch(BUDGET_CODE),
                                                    EscapeUtils.escapeHtmlSearch(DECISION), EscapeUtils.escapeHtmlSearch(P_ID), EscapeUtils.escapeHtmlSearch(PASSPORT),
                                                    EscapeUtils.escapeHtmlSearch(CCCD), sUIDResult);
                                                sEnterpriseCert = sUIDResult[0];
                                                sPersonalCert = sUIDResult[1];
                                            }
                                            int ss = 0;
                                            if ((session.getAttribute("CountListRenewCert")) == null) {
                                                ss = db.S_BO_CERTIFICATION_TOTAL(EscapeUtils.escapeHtmlSearch(FromCreateDate),
                                                    EscapeUtils.escapeHtmlSearch(ToCreateDate), EscapeUtils.escapeHtmlSearch(CERTIFICATION_ATTR_TYPE), EscapeUtils.escapeHtmlSearch(CERT_SN),
                                                    EscapeUtils.escapeHtmlSearch(CERTIFICATION_PURPOSE), EscapeUtils.escapeHtmlSearch(PERSONAL_NAME), EscapeUtils.escapeHtmlSearch(COMPANY_NAME),
                                                    EscapeUtils.escapeHtmlSearch(DOMAIN_NAME), EscapeUtils.escapeHtmlSearch(BranchOffice),
                                                    USER, SessRoleID_ID, SessUserAgentID, IsTokenLost, EscapeUtils.escapeHtmlSearch(TOKEN_SN),
                                                    EscapeUtils.escapeHtmlSearch(FORM_FACTOR), EscapeUtils.escapeHtmlSearch(DEVICE_UUID_SEARCH), IsByOwner,
                                                    EscapeUtils.escapeHtmlSearch(CERTIFICATION_AUTHORITY_SEARCH), EscapeUtils.escapeHtmlSearch(SERVICE_TYPE_SEARCH),
                                                    sessTreeArrayBranchID, sEnterpriseCert, sPersonalCert);
                                                session.setAttribute("CountListRenewCert", String.valueOf(ss));
                                            } else {
                                                String sCount = (String) session.getAttribute("CountListRenewCert");
                                                ss = Integer.parseInt(sCount);
                                                session.setAttribute("CountListRenewCert", String.valueOf(ss));
                                            }
                                            if (session.getAttribute("SearchIPageNoPagingRenewCert") != null) {
                                                String sPage = (String) session.getAttribute("SearchIPageNoPagingRenewCert");
                                                iPagNo = Integer.parseInt(sPage);
                                            }
                                            if (session.getAttribute("SearchISwRwsPagingRenewCert") != null) {
                                                String sSumPage = (String) session.getAttribute("SearchISwRwsPagingRenewCert");
                                                iSwRws = Integer.parseInt(sSumPage);
                                            }
                                            if (session.getAttribute("RefreshRenewCertSessNumberPaging") != null) {
                                                String sNoPage = (String) session.getAttribute("RefreshRenewCertSessNumberPaging");
                                                iPaNoSS = Integer.parseInt(sNoPage);
                                            }
                                            session.setAttribute("SearchIPageNoPagingRenewCert", String.valueOf(iPagNo));
                                            session.setAttribute("SearchISwRwsPagingRenewCert", String.valueOf(iSwRws));
                                            if (ss > 0) {
                                                db.S_BO_CERTIFICATION_LIST(EscapeUtils.escapeHtmlSearch(FromCreateDate), EscapeUtils.escapeHtmlSearch(ToCreateDate),
                                                    EscapeUtils.escapeHtmlSearch(CERTIFICATION_ATTR_TYPE), EscapeUtils.escapeHtmlSearch(CERT_SN),
                                                    EscapeUtils.escapeHtmlSearch(CERTIFICATION_PURPOSE),
                                                    EscapeUtils.escapeHtmlSearch(PERSONAL_NAME), EscapeUtils.escapeHtmlSearch(COMPANY_NAME),
                                                    EscapeUtils.escapeHtmlSearch(DOMAIN_NAME), EscapeUtils.escapeHtmlSearch(BranchOffice),
                                                    USER, SessRoleID_ID, SessUserAgentID, IsTokenLost, EscapeUtils.escapeHtmlSearch(TOKEN_SN),
                                                    EscapeUtils.escapeHtmlSearch(FORM_FACTOR), EscapeUtils.escapeHtmlSearch(DEVICE_UUID_SEARCH),
                                                    IsByOwner, EscapeUtils.escapeHtmlSearch(CERTIFICATION_AUTHORITY_SEARCH), EscapeUtils.escapeHtmlSearch(SERVICE_TYPE_SEARCH),
                                                    sessLanguageGlobal, rsPgin, iPagNo, iSwRws,
                                                    sessTreeArrayBranchID, sEnterpriseCert, sPersonalCert);
                                            }
                                            iTotRslts = ss;
                                            if (ss > 0) {
                                                strMess = com.convertMoney(ss);
                                            }
                                            if (iTotRslts > 0 && rsPgin[0].length > 0) {
                                                status = 1;
                                            } else {
                                                status = 1000;
                                            }
                                        } catch(Exception e){CommonFunction.LogExceptionServlet(null, "CertList reload: " + e.getMessage(), e);}
                                    } else if (request.getMethod().equals("POST") || "1".equals(hasPaging)) {
                                        session.setAttribute("RefreshRenewCertSessPaging", null);
                                        if (request.getMethod().equals("POST")) {
                                            session.setAttribute("SearchShareStoreRenewCert", null);
                                            session.setAttribute("SearchIPageNoPagingRenewCert", null);
                                            session.setAttribute("SearchISwRwsPagingRenewCert", null);
                                        }
                                        String sCsrfToken = request.getParameter("CsrfToken");
                                        String stempCsrfToken = request.getParameter("tempCsrfToken");
                                        if (!"1".equals(hasPaging)) {
                                            if (sCsrfToken != null && sCsrfToken.equals(stempCsrfToken)) {
                                            } else {
                            %>
                            <script type="text/javascript">
                                window.onload = function () {
                                    funCsrfAlert();
                                }();
                            </script>
                            <%
                                        }
                                    }
                                    try{
                                        statusLoad = 1;
                                        request.setCharacterEncoding("UTF-8");
                                        String sEnterpriseCert = "";
                                        String sPersonalCert = "";
                                        String FromCreateDate = request.getParameter("FromCreateDate");
                                        String ToCreateDate = request.getParameter("ToCreateDate");
                                        String CERT_SN = EscapeUtils.ConvertStringToUnicode(request.getParameter("CERT_SN"));
                                        String FORM_FACTOR = request.getParameter("FORM_FACTOR_LIST");
                                        String TOKEN_SN = EscapeUtils.ConvertStringToUnicode(request.getParameter("TOKEN_SN"));
                                        String PERSONAL_NAME = EscapeUtils.ConvertStringToUnicode(request.getParameter("PERSONAL_NAME"));
                                        String COMPANY_NAME = EscapeUtils.ConvertStringToUnicode(request.getParameter("COMPANY_NAME"));
                                        String DOMAIN_NAME = EscapeUtils.ConvertStringToUnicode(request.getParameter("DOMAIN_NAME"));
                                        String TAX_CODE = "";
                                        String BUDGET_CODE ="";
                                        String P_ID = "";
                                        String CCCD = "";
                                        String PASSPORT = "";
                                        String DECISION = "";
                                        String CERTIFICATION_ATTR_TYPE = request.getParameter("CERTIFICATION_ATTR_TYPE_LIST");
                                        String CERTIFICATION_PURPOSE = request.getParameter("CERTIFICATION_PURPOSE_LIST");
                                        String DEVICE_UUID_SEARCH = EscapeUtils.ConvertStringToUnicode(request.getParameter("DEVICE_UUID_SEARCH"));
                                        String IsTokenLost = request.getParameter("IsTokenLost");
                                        String IsByOwner = request.getParameter("IsByOwner");
                                        String BranchOffice = request.getParameter("BranchOffice");
                                        String USER = request.getParameter("USER");
                                        String CERTIFICATION_AUTHORITY_SEARCH = request.getParameter("CERTIFICATION_AUTHORITY_SEARCH");
                                        String SERVICE_TYPE_SEARCH = EscapeUtils.ConvertStringToUnicode(request.getParameter("SERVICE_TYPE_SEARCH"));
                                        Boolean nameCheck = Boolean.valueOf(request.getParameter("nameCheck") != null);
                                        if (nameCheck == false) {
                                            FromCreateDate = "";
                                            ToCreateDate = "";
                                            strAlertAllTimes = "1";
                                        }

                                        if(!isUIDCollection.equals("1")) {
                                            TAX_CODE = EscapeUtils.ConvertStringToUnicode(request.getParameter("TAX_CODE"));
                                            BUDGET_CODE = EscapeUtils.ConvertStringToUnicode(request.getParameter("BUDGET_CODE"));
                                            P_ID = EscapeUtils.ConvertStringToUnicode(request.getParameter("P_ID"));
                                            CCCD = EscapeUtils.ConvertStringToUnicode(request.getParameter("CCCD_SEARCH"));
                                            PASSPORT = EscapeUtils.ConvertStringToUnicode(request.getParameter("PASSPORT"));
                                            DECISION = EscapeUtils.ConvertStringToUnicode(request.getParameter("DECISION"));
                                        } else {
                                            String sENTERPRISE_PREFIX = EscapeUtils.ConvertStringToUnicode(request.getParameter("ENTERPRISE_PREFIX"));
                                            String sPERSONAL_PREFIX = EscapeUtils.ConvertStringToUnicode(request.getParameter("PERSONAL_PREFIX"));
                                            String sENTERPRISE_ID = request.getParameter("ENTERPRISE_ID");
                                            String sPERSONAL_ID = request.getParameter("PERSONAL_ID");
                                            if ("1".equals(hasPaging)) {
                                                sENTERPRISE_PREFIX = (String) session.getAttribute("sessENTERPRISE_PREFIXRenewCert");
                                                sPERSONAL_PREFIX = (String) session.getAttribute("sessPERSONAL_PREFIXRenewCert");
                                                sENTERPRISE_ID = (String) session.getAttribute("sessENTERPRISE_IDRenewCert");
                                                sPERSONAL_ID = (String) session.getAttribute("sessPERSONAL_IDRenewCert");
                                            }
                                            session.setAttribute("sessENTERPRISE_PREFIXRenewCert", sENTERPRISE_PREFIX);
                                            session.setAttribute("sessPERSONAL_PREFIXRenewCert", sPERSONAL_PREFIX);
                                            session.setAttribute("sessENTERPRISE_IDRenewCert", sENTERPRISE_ID);
                                            session.setAttribute("sessPERSONAL_IDRenewCert", sPERSONAL_ID);
                                            isLoadEnterprisePrefix = sENTERPRISE_PREFIX;
                                            isLoadPeronalPrefix = sPERSONAL_PREFIX;
                                            if(!"".equals(sENTERPRISE_ID)){
                                                sEnterpriseCert = sENTERPRISE_PREFIX + "%"+sENTERPRISE_ID+"%";
                                            }
                                            if(!"".equals(sPERSONAL_ID)){
                                                sPersonalCert = sPERSONAL_PREFIX + "%"+sPERSONAL_ID+"%";
                                            }
                                        }
                                        if ("1".equals(hasPaging)) {
                                            session.setAttribute("SearchSharePagingRenewCert", "0");
                                            ToCreateDate = (String) session.getAttribute("sessToCreateDateRenewCert");
                                            FromCreateDate = (String) session.getAttribute("sessFromCreateDateRenewCert");
                                            CERT_SN = (String) session.getAttribute("sessCERT_SNRenewCert");
                                            FORM_FACTOR = (String) session.getAttribute("sessFormFactorRenewCert");
                                            TOKEN_SN = (String) session.getAttribute("sessTOKEN_SNRenewCert");
                                            PERSONAL_NAME = (String) session.getAttribute("sessPERSONAL_NAMERenewCert");
                                            COMPANY_NAME = (String) session.getAttribute("sessCOMPANY_NAMERenewCert");
                                            DOMAIN_NAME = (String) session.getAttribute("sessDOMAIN_NAMERenewCert");
                                            TAX_CODE = (String) session.getAttribute("sessTAX_CODERenewCert");
                                            BUDGET_CODE = (String) session.getAttribute("sessBUDGET_CODERenewCert");
                                            DECISION = (String) session.getAttribute("sessDECISIONRenewCert");
                                            P_ID = (String) session.getAttribute("sessP_IDRenewCert");
                                            CCCD = (String) session.getAttribute("sessCCCDRenewCert");
                                            PASSPORT = (String) session.getAttribute("sessPASSPORTRenewCert");
                                            CERTIFICATION_ATTR_TYPE = (String) session.getAttribute("sessCERTIFICATION_ATTR_TYPERenewCert");
                                            CERTIFICATION_PURPOSE = (String) session.getAttribute("sessCERTIFICATION_PURPOSERenewCert");
                                            DEVICE_UUID_SEARCH = (String) session.getAttribute("sessDEVICE_UUIDRenewCert");
                                            IsTokenLost = (String) session.getAttribute("sessTokenLostRenewCert");
                                            IsByOwner = (String) session.getAttribute("sessIsByOwnerRenewCert");
                                            BranchOffice = (String) session.getAttribute("sessBranchOfficeRenewCert");
                                            USER = (String) session.getAttribute("sessUserRenewCert");
                                            CERTIFICATION_AUTHORITY_SEARCH = (String) session.getAttribute("sessCARenewCert");
                                            SERVICE_TYPE_SEARCH = (String) session.getAttribute("sessSERVICE_TYPERenewCert");
                                            strAlertAllTimes = (String) session.getAttribute("AlertAllTimeSRenewCert");
                                            session.setAttribute("SessParamOnPagingCertList", null);
                                        } else {
                                            session.setAttribute("SearchSharePagingRenewCert", "1");
                                            session.setAttribute("CountListRenewCert", null);
                                        }
                                        if("".equals(BranchOffice) || "0".equals(BranchOffice)) {
                                            System.out.println("BranchOffice 1: " + BranchOffice);
                                            BranchOffice = Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL;
                                        }
                                        session.setAttribute("sessFromCreateDateRenewCert", FromCreateDate);
                                        session.setAttribute("sessToCreateDateRenewCert", ToCreateDate);
                                        session.setAttribute("sessCERT_SNRenewCert", CERT_SN);
                                        session.setAttribute("sessFormFactorRenewCert", FORM_FACTOR);
                                        session.setAttribute("sessTOKEN_SNRenewCert", TOKEN_SN);
                                        session.setAttribute("sessPERSONAL_NAMERenewCert", PERSONAL_NAME);
                                        session.setAttribute("sessCOMPANY_NAMERenewCert", COMPANY_NAME);
                                        session.setAttribute("sessDOMAIN_NAMERenewCert", DOMAIN_NAME);
                                        session.setAttribute("sessTAX_CODERenewCert", TAX_CODE);
                                        session.setAttribute("sessBUDGET_CODERenewCert", BUDGET_CODE);
                                        session.setAttribute("sessDECISIONRenewCert", DECISION);
                                        session.setAttribute("sessP_IDRenewCert", P_ID);
                                        session.setAttribute("sessCCCDRenewCert", CCCD);
                                        session.setAttribute("sessPASSPORTRenewCert", PASSPORT);
                                        session.setAttribute("sessCERTIFICATION_ATTR_TYPERenewCert", CERTIFICATION_ATTR_TYPE);
                                        session.setAttribute("sessCERTIFICATION_PURPOSERenewCert", CERTIFICATION_PURPOSE);
                                        session.setAttribute("sessDEVICE_UUIDRenewCert", DEVICE_UUID_SEARCH);
                                        session.setAttribute("sessTokenLostRenewCert", IsTokenLost);
                                        session.setAttribute("sessIsByOwnerRenewCert", IsByOwner);
                                        session.setAttribute("sessCARenewCert", CERTIFICATION_AUTHORITY_SEARCH);
                                        session.setAttribute("sessSERVICE_TYPERenewCert", SERVICE_TYPE_SEARCH);
                                        session.setAttribute("sessBranchOfficeRenewCert", BranchOffice);
                                        session.setAttribute("sessUserRenewCert", USER);
                                        session.setAttribute("AlertAllTimeSRenewCert", strAlertAllTimes);
                                        if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(CERTIFICATION_ATTR_TYPE)) {
                                            CERTIFICATION_ATTR_TYPE = "";
                                        }
                                        if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(CERTIFICATION_PURPOSE)) {
                                            CERTIFICATION_PURPOSE = "";
                                        }
                                        if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(FORM_FACTOR)) {
                                            FORM_FACTOR = "";
                                        }
                                        if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(IsTokenLost)) {
                                            IsTokenLost = "";
                                        }
                                        if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(BranchOffice)) {
                                            BranchOffice = "";
                                        }
                                        if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(USER)) {
                                            USER = "";
                                        }
                                        if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(CERTIFICATION_AUTHORITY_SEARCH)) {
                                            CERTIFICATION_AUTHORITY_SEARCH = "";
                                        }
                                        if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(SERVICE_TYPE_SEARCH)) {
                                            SERVICE_TYPE_SEARCH = "";
                                        }
                                        if(!"".equals(CERT_SN) || !"".equals(TOKEN_SN) || !"".equals(DOMAIN_NAME)
                                            || !"".equals(CERTIFICATION_AUTHORITY_SEARCH) || !"".equals(SERVICE_TYPE_SEARCH)
                                             || !"".equals(DEVICE_UUID_SEARCH) || !"0".equals(IsByOwner)
                                            || !"".equals(IsTokenLost) || (!"".equals(BranchOffice) && !BranchOffice.equals(SessUserAgentID))
                                            || (!"".equals(USER) && !USER.equals(SessUserID)) || !"".equals(sEnterpriseCert) || !"".equals(sPersonalCert))
                                        {
                                            strExpandFilter = "1";
                                        }
                                        if(SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                            if(!"".equals(BranchOffice) && !"".equals(USER))
                                            {
                                                SessUserAgentID = BranchOffice;
                                            }
                                        }
                                        if("".equals(USER)) {
                                            if(!SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                                if(SessRoleID.equals(Definitions.CONFIG_ROLE_ID_AGENT_USER) || SessRoleID.equals(Definitions.CONFIG_ROLE_ID_AGENT_ACCOUNTANT)) {
                                                    USER = SessUserID;
                                                }
                                            }
                                        }
                                        if(!isUIDCollection.equals("1")) {
                                            String[] sUIDResult = new String[2];
                                            CommonReferServlet.collectFieldToUID(EscapeUtils.escapeHtmlSearch(TAX_CODE), EscapeUtils.escapeHtmlSearch(BUDGET_CODE),
                                                EscapeUtils.escapeHtmlSearch(DECISION), EscapeUtils.escapeHtmlSearch(P_ID), EscapeUtils.escapeHtmlSearch(PASSPORT),
                                                EscapeUtils.escapeHtmlSearch(CCCD), sUIDResult);
                                            sEnterpriseCert = sUIDResult[0];
                                            sPersonalCert = sUIDResult[1];
                                        }
                                        int ss = 0;
                                        if ((session.getAttribute("CountListRenewCert")) == null) {
                                            ss = db.S_BO_CERTIFICATION_TOTAL(EscapeUtils.escapeHtmlSearch(FromCreateDate),
                                                EscapeUtils.escapeHtmlSearch(ToCreateDate),EscapeUtils.escapeHtmlSearch(CERTIFICATION_ATTR_TYPE),
                                                EscapeUtils.escapeHtmlSearch(CERT_SN), EscapeUtils.escapeHtmlSearch(CERTIFICATION_PURPOSE),
                                                EscapeUtils.escapeHtmlSearch(PERSONAL_NAME), EscapeUtils.escapeHtmlSearch(COMPANY_NAME),
                                                EscapeUtils.escapeHtmlSearch(DOMAIN_NAME), EscapeUtils.escapeHtmlSearch(BranchOffice),
                                                USER, SessRoleID_ID, SessUserAgentID, IsTokenLost, EscapeUtils.escapeHtmlSearch(TOKEN_SN),
                                                EscapeUtils.escapeHtmlSearch(FORM_FACTOR), EscapeUtils.escapeHtmlSearch(DEVICE_UUID_SEARCH),IsByOwner,
                                                EscapeUtils.escapeHtmlSearch(CERTIFICATION_AUTHORITY_SEARCH), EscapeUtils.escapeHtmlSearch(SERVICE_TYPE_SEARCH),
                                                sessTreeArrayBranchID, sEnterpriseCert, sPersonalCert);
                                            session.setAttribute("CountListRenewCert", String.valueOf(ss));
                                        } else {
                                            String sCount = (String) session.getAttribute("CountListRenewCert");
                                            ss = Integer.parseInt(sCount);
                                            session.setAttribute("CountListRenewCert", String.valueOf(ss));
                                        }
                                        iTotRslts = ss;
                                        if (iTotRslts > 0) {
                                            db.S_BO_CERTIFICATION_LIST(EscapeUtils.escapeHtmlSearch(FromCreateDate), EscapeUtils.escapeHtmlSearch(ToCreateDate),
                                                EscapeUtils.escapeHtmlSearch(CERTIFICATION_ATTR_TYPE), EscapeUtils.escapeHtmlSearch(CERT_SN),
                                                EscapeUtils.escapeHtmlSearch(CERTIFICATION_PURPOSE),
                                                EscapeUtils.escapeHtmlSearch(PERSONAL_NAME), EscapeUtils.escapeHtmlSearch(COMPANY_NAME),
                                                EscapeUtils.escapeHtmlSearch(DOMAIN_NAME), EscapeUtils.escapeHtmlSearch(BranchOffice),
                                                USER, SessRoleID_ID, SessUserAgentID, IsTokenLost, EscapeUtils.escapeHtmlSearch(TOKEN_SN),
                                                EscapeUtils.escapeHtmlSearch(FORM_FACTOR), EscapeUtils.escapeHtmlSearch(DEVICE_UUID_SEARCH),
                                                IsByOwner, EscapeUtils.escapeHtmlSearch(CERTIFICATION_AUTHORITY_SEARCH), EscapeUtils.escapeHtmlSearch(SERVICE_TYPE_SEARCH),
                                                sessLanguageGlobal, rsPgin, iPagNo, iSwRws,
                                                sessTreeArrayBranchID, sEnterpriseCert, sPersonalCert);
                                            session.setAttribute("SearchIPageNoPagingRenewCert", String.valueOf(iPagNo));
                                            session.setAttribute("SearchISwRwsPagingRenewCert", String.valueOf(iSwRws));
                                            strMess = com.convertMoney(ss);
                                            if (rsPgin[0].length > 0) {
                                                status = 1;
                                            } else {
                                                status = 1000;
                                            }
                                        } else {
                                            status = 1000;
                                        }
                                    } catch(Exception e){CommonFunction.LogExceptionServlet(null, "CertList search new: " + e.getMessage(), e);}
                                } else {
                                    session.setAttribute("RefreshRenewCertSessPaging", null);
                                    session.setAttribute("SearchShareStoreRenewCert", null);
                                    session.setAttribute("SearchIPageNoPagingRenewCert", null);
                                    session.setAttribute("SearchISwRwsPagingRenewCert", null);
                                    session.setAttribute("sessFromCreateDateRenewCert", null);
                                    session.setAttribute("sessToCreateDateRenewCert", null);
                                    session.setAttribute("sessCERT_SNRenewCert", null);
                                    session.setAttribute("sessFormFactorRenewCert", null);
                                    session.setAttribute("sessTOKEN_SNRenewCert", null);
                                    session.setAttribute("sessBranchOfficeRenewCert", "All");
                                    session.setAttribute("sessUserRenewCert", "All");
                                    session.setAttribute("sessPERSONAL_NAMERenewCert", null);
                                    session.setAttribute("sessCOMPANY_NAMERenewCert", null);
                                    session.setAttribute("sessDOMAIN_NAMERenewCert", null);
                                    session.setAttribute("sessTAX_CODERenewCert", null);
                                    session.setAttribute("sessDEVICE_UUIDRenewCert", null);
                                    session.setAttribute("sessBUDGET_CODERenewCert", null);
                                    session.setAttribute("sessDECISIONRenewCert", null);
                                    session.setAttribute("sessP_IDRenewCert", null);
                                    session.setAttribute("sessCCCDRenewCert", null);
                                    session.setAttribute("sessIsByOwnerRenewCert", null);
                                    session.setAttribute("sessPASSPORTRenewCert", null);
                                    if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA) || isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_NC)) {
                                        session.setAttribute("sessCERTIFICATION_ATTR_TYPERenewCert", "All");
                                    } else {session.setAttribute("sessCERTIFICATION_ATTR_TYPERenewCert", "2");}
                                    session.setAttribute("sessCERTIFICATION_PURPOSERenewCert", "All");
                                    session.setAttribute("sessCARenewCert", "All");
                                    session.setAttribute("sessSERVICE_TYPERenewCert", "All");
                                    session.setAttribute("sessTokenLostRenewCert", "All");
                                    session.setAttribute("AlertAllTimeSRenewCert", null);
                                    if(isUIDCollection.equals("1")) {
                                        session.setAttribute("sessENTERPRISE_PREFIXRenewCert", null);
                                        session.setAttribute("sessPERSONAL_PREFIXRenewCert", null);
                                        session.setAttribute("sessENTERPRISE_IDRenewCert", null);
                                        session.setAttribute("sessPERSONAL_IDRenewCert", null);
                                    }
                                }
                            %>
                            <div class="col-md-12 col-sm-12 col-xs-12">
                                <div class="x_panel">
                                    <div class="x_content" style="margin-top: 0px;">
                                        <form name="form" method="post" class="form-horizontal">
                                            <script>
                                                function checkboxChange() {
                                                    if ($("#idCheck").is(':checked')) {
                                                        document.getElementById("demo1").disabled = false;
                                                        document.getElementById("demo2").disabled = false;
                                                    }
                                                    else
                                                    {
                                                        document.getElementById("demo1").disabled = true;
                                                        document.getElementById("demo2").disabled = true;
                                                    }
                                                }
                                            </script>
                                            <div class="form-group" style="padding: 0px 0px 0px 0px;margin: 0;">
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;padding-right: 5px;"><script>document.write(global_fm_FromDate);</script></label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <input type="Text" id="demo1" name="FromCreateDate" <%= session.getAttribute("AlertAllTimeSRenewCert") != null && "1".equals(session.getAttribute("AlertAllTimeSRenewCert").toString()) ? "disabled" : ""%>
                                                                value="<%= session.getAttribute("sessFromCreateDateRenewCert") != null && !"1".equals(session.getAttribute("AlertAllTimeSRenewCert").toString()) ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessFromCreateDateRenewCert").toString()) : com.ConvertMonthSub(30)%>"
                                                                maxlength="25" class="form-control123"/>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;padding-right: 5px;"><script>document.write(global_fm_ToDate);</script></label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <input type="Text" id="demo2" name="ToCreateDate" <%= session.getAttribute("AlertAllTimeSRenewCert") != null && "1".equals(session.getAttribute("AlertAllTimeSRenewCert").toString()) ? "disabled" : ""%>
                                                                value="<%= session.getAttribute("sessToCreateDateRenewCert") != null && !"1".equals(session.getAttribute("AlertAllTimeSRenewCert").toString()) ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessToCreateDateRenewCert").toString()) : com.ConvertMonthSub(0)%>"
                                                                maxlength="25" class="form-control123"/>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;padding-right: 5px;"><script>document.write(global_fm_check_date);</script></label>
                                                        <div class="col-sm-7" style="padding-right: 0px;text-align: left;">
                                                            <label class="switch" for="idCheck">
                                                                <input type="checkbox" name="nameCheck" id="idCheck" onchange="checkboxChange();" <%= session.getAttribute("AlertAllTimeSRenewCert") != null && "1".equals(session.getAttribute("AlertAllTimeSRenewCert").toString()) ? "" : "checked" %>/>
                                                                <div class="slider round"></div>
                                                            </label>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-sm-4" style="padding-left: 0;">
                                                <div class="form-group">
                                                    <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;padding-right: 5px;">
                                                        <script>document.write(global_fm_grid_company);</script>
                                                    </label>
                                                    <div class="col-sm-7" style="padding-right: 0px;">
                                                        <input type="text" name="COMPANY_NAME" maxlength="150" value="<%= session.getAttribute("sessCOMPANY_NAMERenewCert") != null ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessCOMPANY_NAMERenewCert").toString()) : ""%>"
                                                            class="form-control123">
                                                    </div>
                                                </div>
                                            </div>
                                            <%
                                                if(isUIDCollection.equals("1")) {
                                            %>
                                            <div class="col-sm-4" style="padding-left: 0;">
                                                <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;padding-right: 5px;">
                                                    <script>document.write(global_fm_identifier_type);</script>
                                                </label>
                                                <div class="col-sm-7" style="padding-right: 0px;">
                                                    <select name="ENTERPRISE_PREFIX" id="ENTERPRISE_PREFIX" onclick="changeEnterprise(this.value);" class="form-control123" style="text-align: left;">
                                                        <%
                                                            PREFIX_UUID[][] rsPrefix;
                                                            rsPrefix = new PREFIX_UUID[1][];
                                                            dbTwo.S_BO_PREFIX_UUID_COMBOBOX("ENTERPRISE", sessLanguageGlobal, rsPrefix);
                                                            if (rsPrefix[0].length > 0) {
                                                                for (PREFIX_UUID temp1 : rsPrefix[0]) {
                                                                    if("".equals(isLoadEnterprisePrefix)){
                                                                        isLoadEnterprisePrefix = temp1.PREFIX_DB;
                                                                    }
                                                        %>
                                                        <option value="<%=temp1.PREFIX_DB%>" <%= session.getAttribute("sessENTERPRISE_PREFIXRenewCert") != null && temp1.PREFIX_DB.equals(session.getAttribute("sessENTERPRISE_PREFIXRenewCert").toString()) ? "selected" : ""%>><%=temp1.REMARK%></option>
                                                        <%
                                                                }
                                                            }
                                                        %>
                                                    </select>
                                                </div>
                                            </div>
                                            <div class="col-sm-4" style="padding-left: 0;">
                                                <div class="form-group">
                                                    <label class="control-label col-sm-5" id="idLblTooltipEnterprise" style="color: #000000; font-weight: bold;text-align: right;padding-right: 5px;">
                                                        <script>document.write(global_fm_enter);</script> <%= dbTwo.GET_PREFIX_UUID_TOOLTIP("ENTERPRISE", sessLanguageGlobal, isLoadEnterprisePrefix) %>
                                                    </label>
                                                    <div class="col-sm-7" style="padding-right: 0px;">
                                                        <input type="text" name="ENTERPRISE_ID" maxlength="22" value="<%= session.getAttribute("sessENTERPRISE_IDRenewCert") != null ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessENTERPRISE_IDRenewCert").toString()) : ""%>"
                                                            class="form-control123">
                                                    </div>
                                                </div>
                                                <script>
                                                    function changeEnterprise() {
                                                        $("#idLblTooltipEnterprise").text(global_fm_enter + $("#ENTERPRISE_PREFIX option:selected").text());
                                                    }
                                                </script>
                                            </div>
                                            <div class="col-sm-4" style="padding-left: 0;">
                                                <div class="form-group">
                                                    <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;padding-right: 5px;">
                                                        <script>document.write(global_fm_grid_personal);</script>
                                                    </label>
                                                    <div class="col-sm-7" style="padding-right: 0px;">
                                                        <input type="text" name="PERSONAL_NAME" maxlength="150" value="<%= session.getAttribute("sessPERSONAL_NAMERenewCert") != null ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessPERSONAL_NAMERenewCert").toString()) : ""%>"
                                                            class="form-control123">
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-sm-4" style="padding-left: 0;">
                                                <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;padding-right: 5px;">
                                                    <script>document.write(global_fm_document_type);</script>
                                                </label>
                                                <div class="col-sm-7" style="padding-right: 0px;">
                                                    <select name="PERSONAL_PREFIX" id="PERSONAL_PREFIX" class="form-control123" onclick="changePersonal();" style="text-align: left;">
                                                        <%
                                                            rsPrefix = new PREFIX_UUID[1][];
                                                            dbTwo.S_BO_PREFIX_UUID_COMBOBOX("PERSONAL", sessLanguageGlobal, rsPrefix);
                                                            if (rsPrefix[0].length > 0) {
                                                                for (PREFIX_UUID temp1 : rsPrefix[0]) {
                                                                    if("".equals(isLoadPeronalPrefix)){
                                                                        isLoadPeronalPrefix = temp1.PREFIX_DB;
                                                                    }
                                                        %>
                                                        <option value="<%=temp1.PREFIX_DB%>" <%= session.getAttribute("sessPERSONAL_PREFIXRenewCert") != null && temp1.PREFIX_DB.equals(session.getAttribute("sessPERSONAL_PREFIXRenewCert").toString()) ? "selected" : ""%>><%=temp1.REMARK%></option>
                                                        <%
                                                                }
                                                            }
                                                        %>
                                                    </select>
                                                </div>
                                            </div>
                                            <div class="col-sm-4" style="padding-left: 0;">
                                                <div class="form-group">
                                                    <label class="control-label col-sm-5" id="idLblTooltipPersonal" style="color: #000000; font-weight: bold;text-align: right;padding-right: 5px;">
                                                        <script>document.write(global_fm_enter);</script> <%= dbTwo.GET_PREFIX_UUID_TOOLTIP("PERSONAL", sessLanguageGlobal, isLoadPeronalPrefix) %>
                                                    </label>
                                                    <div class="col-sm-7" style="padding-right: 0px;">
                                                        <input type="text" name="PERSONAL_ID" maxlength="22" value="<%= session.getAttribute("sessPERSONAL_IDRenewCert") != null ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessPERSONAL_IDRenewCert").toString()) : ""%>"
                                                            class="form-control123">
                                                    </div>
                                                </div>
                                                <script>
                                                    function changePersonal()
                                                    {
                                                        $("#idLblTooltipPersonal").text(global_fm_enter + $("#PERSONAL_PREFIX option:selected").text());
                                                    }
                                                </script>
                                            </div>
                                            <div class="col-sm-4" style="padding-left: 0;">
                                                <div class="form-group">
                                                    <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;padding-right: 5px;">
                                                        <script>document.write(global_fm_certpurpose);</script>
                                                    </label>
                                                    <div class="col-sm-7" style="padding-right: 0px;">
                                                        <select name="CERTIFICATION_PURPOSE_LIST" id="CERTIFICATION_PURPOSE_LIST" class="form-control123">
                                                            <option value="<%= Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL%>" <%= session.getAttribute("sessCERTIFICATION_PURPOSERenewCert") != null && Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(session.getAttribute("sessCERTIFICATION_PURPOSERenewCert").toString()) ? "selected" : ""%>><script>document.write(global_fm_combox_all);</script></option>
                                                            <%
                                                                CERTIFICATION_PURPOSE[][] rsPurpose = new CERTIFICATION_PURPOSE[1][];
                                                                db.S_BO_CERTIFICATION_PURPOSE_COMBOBOX(sessLanguageGlobal, rsPurpose);
                                                                if (rsPurpose[0].length > 0) {
                                                                    for (CERTIFICATION_PURPOSE temp1 : rsPurpose[0]) {
                                                            %>
                                                            <option value="<%=String.valueOf(temp1.ID)%>" <%= session.getAttribute("sessCERTIFICATION_PURPOSERenewCert") != null && String.valueOf(temp1.ID).equals(session.getAttribute("sessCERTIFICATION_PURPOSERenewCert").toString()) ? "selected" : ""%>><%=temp1.REMARK%></option>
                                                            <%
                                                                    }
                                                                }
                                                            %>
                                                        </select>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-sm-4" style="padding-left: 0;">
                                                <div class="form-group">
                                                    <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;padding-right: 5px;">
                                                        <script>document.write(global_fm_Method);</script>
                                                    </label>
                                                    <div class="col-sm-7" style="padding-right: 0px;">
                                                        <select name="FORM_FACTOR_LIST" id="FORM_FACTOR_LIST" class="form-control123">
                                                            <option value="<%= Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL%>" <%= session.getAttribute("sessFormFactorRenewCert") != null && Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(session.getAttribute("sessFormFactorRenewCert").toString()) ? "selected" : ""%>><script>document.write(global_fm_combox_all);</script></option>
                                                            <%
                                                                PKI_FORMFACTOR[][] rsFromFactore = new PKI_FORMFACTOR[1][];
                                                                db.S_BO_PKI_FORMFACTOR_COMBOBOX(sessLanguageGlobal, rsFromFactore);
                                                                if (rsFromFactore[0].length > 0) {
                                                                    for (PKI_FORMFACTOR temp1 : rsFromFactore[0]) {
                                                            %>
                                                            <option value="<%=String.valueOf(temp1.ID)%>" <%= session.getAttribute("sessFormFactorRenewCert") != null && String.valueOf(temp1.ID).equals(session.getAttribute("sessFormFactorRenewCert").toString()) ? "selected" : ""%>><%=temp1.REMARK%></option>
                                                            <%
                                                                    }
                                                                }
                                                            %>
                                                        </select>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-sm-4" style="padding-left: 0;">
                                                <div class="form-group">
                                                    <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;padding-right: 5px;">
                                                        <script>document.write(global_fm_Status);</script>
                                                    </label>
                                                    <div class="col-sm-7" style="padding-right: 0px;">
                                                        <select name="CERTIFICATION_ATTR_TYPE_LIST" id="CERTIFICATION_ATTR_TYPE_LIST" class="form-control123">
                                                            <option value="<%= Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL%>" <%= session.getAttribute("sessCERTIFICATION_ATTR_TYPERenewCert") != null && Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(session.getAttribute("sessCERTIFICATION_ATTR_TYPERenewCert").toString()) ? "selected" : ""%>><script>document.write(global_fm_combox_all);</script></option>
                                                            <%
                                                                CERTIFICATION_STATE[][] rsType = new CERTIFICATION_STATE[1][];
                                                                db.S_BO_CERTIFICATION_STATE_COMBOBOX(sessLanguageGlobal, rsType);
                                                                if (rsType[0].length > 0) {
                                                                    for (CERTIFICATION_STATE temp1 : rsType[0]) {
                                                            %>
                                                            <option value="<%=String.valueOf(temp1.ID)%>" <%= session.getAttribute("sessCERTIFICATION_ATTR_TYPERenewCert") != null && String.valueOf(temp1.ID).equals(session.getAttribute("sessCERTIFICATION_ATTR_TYPERenewCert").toString()) ? "selected" : ""%>><%=temp1.REMARK%></option>
                                                            <%
                                                                    }
                                                                }
                                                            %>
                                                        </select>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="form-group" style="clear: both;padding-bottom: 0;">
                                                <div class="col-sm-4" style="padding-left: 0;padding-top: 10px;">
                                                    <label class="control-label col-sm-6" style="color: #000000; font-weight: bold;padding-top: 0;padding-right: 0px;text-align: right;">
                                                        <a id="idAShowList" style="cursor: pointer; color: blue; text-decoration: underline;" onclick="popupViewCSRSList();"><script>document.write(global_fm_search_expand);</script></a>
                                                        <a id="idAHideList" style="cursor: pointer; color: blue; text-decoration: underline;display: none;" onclick="popupHideCTSList();"><script>document.write(global_fm_search_hide);</script></a>
                                                    </label>
                                                    <div class="col-sm-6" style="padding-right: 0px;"></div>
                                                </div>
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="col-sm-5" style="padding-right: 0px;text-align: right;">
                                                        <button type="button" class="btn btn-info" onclick="searchForm('<%=anticsrf%>');"><script>document.write(global_fm_button_search);</script></button>
                                                    </div>
                                                    <div class="col-sm-7" style="padding-right: 0px;text-align: right;"></div>
                                                </div>
                                                <div class="col-sm-4">
                                                    <div class="form-group"> </div>
                                                </div>
                                            </div>
                                            <%
                                                } else {
                                            %>
                                            <div class="col-sm-4" style="padding-left: 0;">
                                                <div class="form-group">
                                                    <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;padding-right: 5px;">
                                                        <script>document.write(global_fm_MST);</script>
                                                    </label>
                                                    <div class="col-sm-7" style="padding-right: 0px;">
                                                        <input type="text" name="TAX_CODE" maxlength="25" value="<%= session.getAttribute("sessTAX_CODERenewCert") != null ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessTAX_CODERenewCert").toString()) : ""%>"
                                                            class="form-control123">
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-sm-4" style="padding-left: 0;">
                                                <div class="form-group">
                                                    <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;padding-right: 5px;">
                                                        <script>document.write(global_fm_MNS);</script>
                                                    </label>
                                                    <div class="col-sm-7" style="padding-right: 0px;">
                                                        <input type="text" name="BUDGET_CODE" maxlength="25" value="<%= session.getAttribute("sessBUDGET_CODERenewCert") != null ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessBUDGET_CODERenewCert").toString()) : ""%>"
                                                            class="form-control123">
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-sm-4" style="padding-left: 0;">
                                                <div class="form-group">
                                                    <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;padding-right: 5px;">
                                                        <script>document.write(global_fm_decision);</script>
                                                    </label>
                                                    <div class="col-sm-7" style="padding-right: 0px;">
                                                        <input type="text" name="DECISION" maxlength="25" value="<%= session.getAttribute("sessDECISIONRenewCert") != null ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessDECISIONRenewCert").toString()) : ""%>"
                                                            class="form-control123">
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-sm-4" style="padding-left: 0;">
                                                <div class="form-group">
                                                    <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;padding-right: 5px;">
                                                        <script>document.write(global_fm_grid_personal);</script>
                                                    </label>
                                                    <div class="col-sm-7" style="padding-right: 0px;">
                                                        <input type="text" name="PERSONAL_NAME" maxlength="150" value="<%= session.getAttribute("sessPERSONAL_NAMERenewCert") != null ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessPERSONAL_NAMERenewCert").toString()) : ""%>"
                                                            class="form-control123">
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-sm-4" style="padding-left: 0;">
                                                <div class="form-group">
                                                    <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;padding-right: 5px;">
                                                        <script>document.write(global_fm_CMND);</script>
                                                    </label>
                                                    <div class="col-sm-7" style="padding-right: 0px;">
                                                        <input type="text" name="P_ID" maxlength="25" value="<%= session.getAttribute("sessP_IDRenewCert") != null ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessP_IDRenewCert").toString()) : ""%>"
                                                            class="form-control123">
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-sm-4" style="padding-left: 0;">
                                                <div class="form-group">
                                                    <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;padding-right: 5px;">
                                                        <script>document.write(global_fm_HC);</script>
                                                    </label>
                                                    <div class="col-sm-7" style="padding-right: 0px;">
                                                        <input type="text" name="PASSPORT" maxlength="25" value="<%= session.getAttribute("sessPASSPORTRenewCert") != null ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessPASSPORTRenewCert").toString()) : ""%>"
                                                            class="form-control123">
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-sm-4" style="padding-left: 0;">
                                                <div class="form-group">
                                                    <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;padding-right: 5px;">
                                                        <script>document.write(global_fm_CitizenId);</script>
                                                    </label>
                                                    <div class="col-sm-7" style="padding-right: 0px;">
                                                        <input type="text" name="CCCD_SEARCH" maxlength="25" value="<%= session.getAttribute("sessCCCDRenewCert") != null ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessCCCDRenewCert").toString()) : ""%>"
                                                            class="form-control123">
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-sm-4" style="padding-left: 0;">
                                                <div class="form-group">
                                                    <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;padding-right: 5px;">
                                                        <script>document.write(global_fm_Method);</script>
                                                    </label>
                                                    <div class="col-sm-7" style="padding-right: 0px;">
                                                        <select name="FORM_FACTOR_LIST" id="FORM_FACTOR_LIST" class="form-control123">
                                                            <option value="<%= Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL%>" <%= session.getAttribute("sessFormFactorRenewCert") != null && Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(session.getAttribute("sessFormFactorRenewCert").toString()) ? "selected" : ""%>><script>document.write(global_fm_combox_all);</script></option>
                                                            <%
                                                                PKI_FORMFACTOR[][] rsFromFactore = new PKI_FORMFACTOR[1][];
                                                                db.S_BO_PKI_FORMFACTOR_COMBOBOX(sessLanguageGlobal, rsFromFactore);
                                                                if (rsFromFactore[0].length > 0) {
                                                                    for (PKI_FORMFACTOR temp1 : rsFromFactore[0]) {
                                                            %>
                                                            <option value="<%=String.valueOf(temp1.ID)%>" <%= session.getAttribute("sessFormFactorRenewCert") != null && String.valueOf(temp1.ID).equals(session.getAttribute("sessFormFactorRenewCert").toString()) ? "selected" : ""%>><%=temp1.REMARK%></option>
                                                            <%
                                                                    }
                                                                }
                                                            %>
                                                        </select>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-sm-4" style="padding-left: 0;">
                                                <div class="form-group">
                                                    <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;padding-right: 5px;">
                                                        <script>document.write(global_fm_Status);</script>
                                                    </label>
                                                    <div class="col-sm-7" style="padding-right: 0px;">
                                                        <select name="CERTIFICATION_ATTR_TYPE_LIST" id="CERTIFICATION_ATTR_TYPE_LIST" class="form-control123">
                                                            <option value="<%= Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL%>" <%= session.getAttribute("sessCERTIFICATION_ATTR_TYPERenewCert") != null && Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(session.getAttribute("sessCERTIFICATION_ATTR_TYPERenewCert").toString()) ? "selected" : ""%>><script>document.write(global_fm_combox_all);</script></option>
                                                            <%
                                                                CERTIFICATION_STATE[][] rsType = new CERTIFICATION_STATE[1][];
                                                                db.S_BO_CERTIFICATION_STATE_COMBOBOX(sessLanguageGlobal, rsType);
                                                                if (rsType[0].length > 0) {
                                                                    for (CERTIFICATION_STATE temp1 : rsType[0]) {
                                                            %>
                                                            <option value="<%=String.valueOf(temp1.ID)%>" <%= session.getAttribute("sessCERTIFICATION_ATTR_TYPERenewCert") != null && String.valueOf(temp1.ID).equals(session.getAttribute("sessCERTIFICATION_ATTR_TYPERenewCert").toString()) ? "selected" : ""%>><%=temp1.REMARK%></option>
                                                            <%
                                                                    }
                                                                }
                                                            %>
                                                        </select>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-sm-4" style="padding-left: 0;">
                                                <div class="form-group">
                                                    <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;padding-right: 5px;">
                                                        <script>document.write(global_fm_certpurpose);</script>
                                                    </label>
                                                    <div class="col-sm-7" style="padding-right: 0px;">
                                                        <select name="CERTIFICATION_PURPOSE_LIST" id="CERTIFICATION_PURPOSE_LIST" class="form-control123">
                                                            <option value="<%= Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL%>" <%= session.getAttribute("sessCERTIFICATION_PURPOSERenewCert") != null && Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(session.getAttribute("sessCERTIFICATION_PURPOSERenewCert").toString()) ? "selected" : ""%>><script>document.write(global_fm_combox_all);</script></option>
                                                            <%
                                                                CERTIFICATION_PURPOSE[][] rsPurpose = new CERTIFICATION_PURPOSE[1][];
                                                                db.S_BO_CERTIFICATION_PURPOSE_COMBOBOX(sessLanguageGlobal, rsPurpose);
                                                                if (rsPurpose[0].length > 0) {
                                                                    for (CERTIFICATION_PURPOSE temp1 : rsPurpose[0]) {
                                                            %>
                                                            <option value="<%=String.valueOf(temp1.ID)%>" <%= session.getAttribute("sessCERTIFICATION_PURPOSERenewCert") != null && String.valueOf(temp1.ID).equals(session.getAttribute("sessCERTIFICATION_PURPOSERenewCert").toString()) ? "selected" : ""%>><%=temp1.REMARK%></option>
                                                            <%
                                                                    }
                                                                }
                                                            %>
                                                        </select>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-sm-4" style="padding-left: 0;">
                                                <div class="form-group">
                                                    <div class="col-sm-5" style="padding-right: 0px;text-align: right;">
                                                        <button type="button" class="btn btn-info" onClick="searchForm('<%=anticsrf%>');"><script>document.write(global_fm_button_search);</script></button>
                                                    </div>
                                                    <div class="col-sm-7" style="padding-right: 0px;text-align: right;">

                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-sm-13" style="clear: both;">
                                                <label class="control-label col-sm-2" style="color: #000000; font-weight: bold;padding-top: 0;">
                                                    <a id="idAShowList" style="cursor: pointer; color: blue; text-decoration: underline;" onclick="popupViewCSRSList();"><script>document.write(global_fm_search_expand);</script></a>
                                                    <a id="idAHideList" style="cursor: pointer; color: blue; text-decoration: underline;display: none;" onclick="popupHideCTSList();"><script>document.write(global_fm_search_hide);</script></a>
                                                </label>
                                                <div class="col-sm-10" style="padding-right: 0px;"></div>
                                            </div>
                                            <%
                                                }
                                            %>
                                            <div id="idViewCSRSList" style="clear: both;padding-top: 10px;display: none;">
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;"><script>document.write(global_fm_ca);</script></label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <select name="CERTIFICATION_AUTHORITY_SEARCH" id="CERTIFICATION_AUTHORITY_SEARCH" class="form-control123">
                                                                <option value="<%= Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL%>" <%= session.getAttribute("sessCARenewCert") != null && Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(session.getAttribute("sessCARenewCert").toString()) ? "selected" : ""%>><script>document.write(global_fm_combox_all);</script></option>
                                                                <%
                                                                    CERTIFICATION_AUTHORITY[][] rsBranch = new CERTIFICATION_AUTHORITY[1][];
                                                                    db.S_BO_CERTIFICATION_AUTHORITY_COMBOBOX(sessLanguageGlobal, rsBranch);
                                                                    if (rsBranch[0].length > 0) {
                                                                        for (CERTIFICATION_AUTHORITY temp1 : rsBranch[0]) {
                                                                %>
                                                                <option value="<%=String.valueOf(temp1.ID)%>" <%= session.getAttribute("sessCARenewCert") != null && String.valueOf(temp1.ID).equals(session.getAttribute("sessCARenewCert").toString().trim()) ? "selected" : ""%>><%=temp1.REMARK%></option>
                                                                <%
                                                                        }
                                                                    }
                                                                %>
                                                            </select>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;"><script>document.write(cert_fm_type_request);</script></label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <select name="SERVICE_TYPE_SEARCH" id="SERVICE_TYPE_SEARCH" class="form-control123">
                                                                <option value="<%= Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL%>" <%= session.getAttribute("sessSERVICE_TYPERenewCert") != null && Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(session.getAttribute("sessSERVICE_TYPERenewCert").toString()) ? "selected" : ""%>><script>document.write(global_fm_combox_all);</script></option>
                                                                <%
                                                                    SERVICE_TYPE[][] rsService = new SERVICE_TYPE[1][];
                                                                    db.S_BO_SERVICE_TYPE_COMBOBOX(sessLanguageGlobal, rsService);
                                                                    if (rsService[0].length > 0) {
                                                                        for (SERVICE_TYPE temp1 : rsService[0]) {
                                                                %>
                                                                <option value="<%=String.valueOf(temp1.ID)%>" <%= session.getAttribute("sessSERVICE_TYPERenewCert") != null && String.valueOf(temp1.ID).equals(session.getAttribute("sessSERVICE_TYPERenewCert").toString()) ? "selected" : ""%>><%=temp1.REMARK%></option>
                                                                <%
                                                                        }
                                                                    }
                                                                %>
                                                            </select>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;">
                                                            <script>document.write(global_fm_choose_owner_cert);</script>
                                                        </label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <select name="IsByOwner" id="IsByOwner" class="form-control123">
                                                                <option value="0" <%= session.getAttribute("sessIsByOwnerRenewCert") != null && "0".equals(session.getAttribute("sessIsByOwnerRenewCert").toString()) ? "selected" : ""%>>
                                                                    <script>document.write(cert_title_register_cert);</script>
                                                                </option>
                                                                <option value="1" <%= session.getAttribute("sessIsByOwnerRenewCert") != null && "1".equals(session.getAttribute("sessIsByOwnerRenewCert").toString()) ? "selected" : ""%>>
                                                                    <script>document.write(cert_title_register_owner);</script>
                                                                </option>
                                                            </select>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-sm-4" style="padding-left: 0; display: <%=isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA) ? "none" : ""%>">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;">
                                                            <script>document.write(certlist_fm_device_uuid);</script>
                                                        </label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <input type="text" name="DEVICE_UUID_SEARCH" maxlength="150" value="<%= session.getAttribute("sessDEVICE_UUIDRenewCert") != null ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessDEVICE_UUIDRenewCert").toString()) : ""%>"
                                                                class="form-control123">
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-sm-4" style="padding-left: 0; display: <%=isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA) ? "none" : ""%>">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;">
                                                            <script>document.write(global_fm_grid_domain);</script>
                                                        </label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <input type="text" name="DOMAIN_NAME" maxlength="150" value="<%= session.getAttribute("sessDOMAIN_NAMERenewCert") != null ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessDOMAIN_NAMERenewCert").toString()) : ""%>"
                                                                class="form-control123">
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;">
                                                            <script>document.write(global_fm_serial);</script>
                                                        </label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <input type="text" name="CERT_SN" maxlength="45" value="<%= session.getAttribute("sessCERT_SNRenewCert") != null ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessCERT_SNRenewCert").toString()) : ""%>"
                                                                class="form-control123">
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;">
                                                            <script>document.write(token_fm_tokenid);</script>
                                                        </label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <input type="text" name="TOKEN_SN" maxlength="32" value="<%= session.getAttribute("sessTOKEN_SNRenewCert") != null ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessTOKEN_SNRenewCert").toString()) : ""%>"
                                                                class="form-control123">
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;">
                                                            <script>document.write(certlist_fm_unnamed);</script>
                                                        </label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <select name="IsTokenLost" id="IsTokenLost" class="form-control123">
                                                                <option value="<%= Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL%>" <%= session.getAttribute("sessTokenLostRenewCert") != null && Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(session.getAttribute("sessTokenLostRenewCert").toString()) ? "selected" : ""%>><script>document.write(global_fm_combox_all);</script></option>
                                                                <option value="1" <%= session.getAttribute("sessTokenLostRenewCert") != null && "1".equals(session.getAttribute("sessTokenLostRenewCert").toString()) ? "selected" : ""%>><script>document.write(global_fm_combox_true);</script></option>
                                                                <option value="0" <%= session.getAttribute("sessTokenLostRenewCert") != null && "0".equals(session.getAttribute("sessTokenLostRenewCert").toString()) ? "selected" : ""%>><script>document.write(global_fm_combox_false);</script></option>
                                                            </select>
                                                        </div>
                                                    </div>
                                                </div>
                                                <%
                                                    String sDisableViewBranch = "";
                                                    if(!SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                                        if(SessRoleID.equals(Definitions.CONFIG_ROLE_ID_AGENT_USER) || SessRoleID.equals(Definitions.CONFIG_ROLE_ID_AGENT_ACCOUNTANT)) {
                                                            sDisableViewBranch = "disabled";
                                                        }
                                                    }
                                                    if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA) && SessRoleID.equals(Definitions.CONFIG_ROLE_ID_CA_SURVEYOR)){
                                                        sDisableViewBranch = "none;";
                                                    }
                                                %>
                                                <div class="col-sm-4" style="padding-left: 0;display: <%=sDisableViewBranch%>">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;">
                                                            <script>document.write(global_fm_Branch);</script>
                                                        </label>
                                                        <%
                                                            if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA)
                                                                || isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_MID)) {
                                                        %>
                                                        <div class="ui-widget">
                                                            <div class="col-sm-7" style="padding-right: 0px;">
                                                                <select name="BranchOffice" id="BranchOffice" onchange="LOAD_BACKOFFICE_USER(this.value, '<%= anticsrf%>');">
                                                                    <%
                                                                        String sBranchFirst = "";
                                                                        try {
                                                                            if(SessUserAgentID.equals(Definitions.CONFIG_AGENT_ROOT) && SessRoleID.equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN)) {
                                                                                BRANCH[][] rsBranchFrist = (BRANCH[][]) session.getAttribute("sessTreeBranchSystemAgency");
                                                                                BRANCH[][] rst = CommonFunction.cloneBranchAddAllOption(rsBranchFrist, "1");
                                                                                if (rst[0].length > 0) {
                                                                                    for (BRANCH temp1 : rst[0]) {
                                                                                        if(!String.valueOf(temp1.PARENT_ID).equals(Definitions.CONFIG_AGENT_ROOT)) {
                                                                                            if("".equals(sBranchFirst)) {
                                                                                                sBranchFirst = String.valueOf(temp1.ID);
                                                                                            }
                                                                    %>
                                                                    <option value="<%=String.valueOf(temp1.ID)%>"  <%= session.getAttribute("sessBranchOfficeRenewCert") != null && String.valueOf(temp1.ID).equals(session.getAttribute("sessBranchOfficeRenewCert").toString()) ? "selected" : ""%>><%=temp1.NAME + " - " + temp1.REMARK%></option>
                                                                    <%
                                                                                    }
                                                                                }
                                                                            }
                                                                        } else {
                                                                            BRANCH[][] rst = (BRANCH[][]) session.getAttribute("sessTreeBranchSystemAgency");
                                                                            if (rst[0].length > 0) {
                                                                                for (BRANCH temp1 : rst[0]) {
                                                                                    if(!String.valueOf(temp1.PARENT_ID).equals(Definitions.CONFIG_AGENT_ROOT)) {
                                                                                        if("".equals(sBranchFirst)) {
                                                                                            sBranchFirst = String.valueOf(temp1.ID);
                                                                                        }
                                                                    %>
                                                                    <option value="<%=String.valueOf(temp1.ID)%>"  <%= session.getAttribute("sessBranchOfficeRenewCert") != null && String.valueOf(temp1.ID).equals(session.getAttribute("sessBranchOfficeRenewCert").toString()) ? "selected" : ""%>><%=temp1.NAME + " - " + temp1.REMARK%></option>
                                                                    <%
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    } catch (Exception e) {
                                                                        checkExFinnaly = 1;
                                                                        CommonFunction.LogExceptionJSP(request.getRequestURI(), "BranchOffice: " + e.getMessage(), e);
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
                                                                </select>
                                                            </div>
                                                            <script>
                                                                $(function () {
                                                                    $.widget("custom.combobox", {
                                                                        _create: function () {
                                                                            this.wrapper = $("<span>")
                                                                                    .addClass("custom-combobox")
                                                                                    .insertAfter(this.element);
                                                                            this.element.hide();
                                                                            this._createAutocomplete();
                                                                            this._createShowAllButton();
                                                                        },
                                                                        _createAutocomplete: function () {
                                                                            var selected = this.element.children(":selected"),
                                                                                    value = selected.val() ? selected.text() : "";
                                                                            this.input = $("<input>")
                                                                                    .appendTo(this.wrapper)
                                                                                    .val(value)
                                                                                    .attr("title", "")
                                                                                    .addClass("custom-combobox-input ui-widget ui-widget-content ui-state-default ui-corner-left")
                                                                                    .autocomplete({
                                                                                        delay: 0,
                                                                                        minLength: 0,
                                                                                        source: $.proxy(this, "_source")
                                                                                    })
                                                                                    .tooltip({
                                                                                        classes: {
                                                                                            "ui-tooltip": "ui-state-highlight"
                                                                                        }
                                                                                    });
                                                                            this._on(this.input, {
                                                                                autocompleteselect: function (event, ui) {
                                                                                    ui.item.option.selected = true;
                                                                                    this._trigger("select", event, {
                                                                                        item: ui.item.option
                                                                                    });
                                                                                },

                                                                                autocompletechange: "_removeIfInvalid"
                                                                            });
                                                                        },
                                                                        _createShowAllButton: function () {
                                                                            var input = this.input,
                                                                                    wasOpen = false
                                                                            $("<a>")
                                                                                    .attr("tabIndex", -1)
                                                                                    .attr("title", "Show All Items")
                                                                                    .attr("height", "")
                                                                                    .tooltip()
                                                                                    .appendTo(this.wrapper)
                                                                                    .button({
                                                                                        icons: {
                                                                                            primary: "ui-icon-triangle-1-s"
                                                                                        },
                                                                                        text: "false"
                                                                                    })
                                                                                    .removeClass("ui-corner-all")
                                                                                    .addClass("custom-combobox-toggle ui-corner-right")
                                                                                    .on("mousedown", function () {
                                                                                        wasOpen = input.autocomplete("widget").is(":visible");
                                                                                    })
                                                                                    .on("click", function () {
                                                                                        input.trigger("focus");

                                                                                        // Close if already visible
                                                                                        if (wasOpen) {
                                                                                            return;
                                                                                        }
                                                                                        // Pass empty string as value to search for, displaying all results
                                                                                        input.autocomplete("search", "");
                                                                                    });
                                                                        },
                                                                        _source: function (request, response) {
                                                                            var matcher = new RegExp($.ui.autocomplete.escapeRegex(request.term), "i");
                                                                            response(this.element.children("option").map(function () {
                                                                                var text = $(this).text();
                                                                                if (this.value && (!request.term || matcher.test(text)))
                                                                                    return {
                                                                                        label: text,
                                                                                        value: text,
                                                                                        option: this
                                                                                    };
                                                                            }));
                                                                        },
                                                                        _removeIfInvalid: function (event, ui) {
                                                                            // Selected an item, nothing to do
                                                                            if (ui.item) {
                                                                                return;
                                                                            }
                                                                            // Search for a match (case-insensitive)
                                                                            var value = this.input.val(),
                                                                                    valueLowerCase = value.toLowerCase(),
                                                                                    valid = false;
                                                                            this.element.children("option").each(function () {
                                                                                if ($(this).text().toLowerCase() === valueLowerCase) {
                                                                                    this.selected = valid = true;
                                                                                    return false;
                                                                                }
                                                                            });
                                                                            // Found a match, nothing to do
                                                                            if (valid) {
                                                                                return;
                                                                            }
                                                                            // Remove invalid value
                                                                            this.input
                                                                                    .val("All - Tất cả")
                                                                                    .attr("title", "")
                                                                                    .tooltip("open");
                                                                            this.element.val("");
                                                                            this._delay(function () {
                                                                                this.input.tooltip("close").attr("title", "");
                                                                            }, 2500);
                                                                            this.input.autocomplete("instance").term = "";
                                                                        },
                                                                        _destroy: function () {
                                                                            this.wrapper.remove();
                                                                            this.element.show();
                                                                        }
                                                                    });
                                                                    $("#BranchOffice").combobox();
                                                                    $("#toggle").on("click", function () {
                                                                        $("#BranchOffice").toggle();
                                                                    });
                                                                });
                                                            </script>
                                                            <style>
                                                                .custom-combobox {
                                                                    position: relative;
                                                                    display: inline-block;
                                                                }
                                                                .custom-combobox-toggle {
                                                                    position: absolute;
                                                                    top: 0;
                                                                    bottom: 0;
                                                                    margin-left: -1px;
                                                                    padding: 0;
                                                                }
                                                                .custom-combobox-input {
                                                                    margin: 0;
                                                                    padding: 5px 10px;
                                                                }
                                                                .ui-state-default, .ui-widget-content .ui-state-default, .ui-widget-header .ui-state-default, .ui-button, html .ui-button.ui-state-disabled:hover, html .ui-button.ui-state-disabled:active{
                                                                    background:none;
                                                                }
                                                            </style>
                                                        </div>
                                                        <%
                                                            } else {
                                                        %>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <select name="BranchOffice" id="BranchOffice" class="form-control123" onchange="LOAD_BACKOFFICE_USER(this.value, '<%= anticsrf%>');">
                                                                <option value="<%= Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL%>" <%= session.getAttribute("sessBranchOfficeRenewCert") != null && Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(session.getAttribute("sessBranchOfficeRenewCert").toString()) ? "selected" : ""%>><script>document.write(global_fm_combox_all);</script></option>
                                                                <%
                                                                    String sBranchFirst = "";
                                                                    try {
                                                                        BRANCH[][] rst = (BRANCH[][]) session.getAttribute("sessTreeBranchSystem");
                                                                        if (rst[0].length > 0) {
                                                                            for (BRANCH temp1 : rst[0]) {
                                                                                if(!String.valueOf(temp1.PARENT_ID).equals(Definitions.CONFIG_AGENT_ROOT))
                                                                                {
                                                                                    if("".equals(sBranchFirst)) {
                                                                                        sBranchFirst = String.valueOf(temp1.ID);
                                                                                    }
                                                                %>
                                                                <option value="<%=String.valueOf(temp1.ID)%>"  <%= session.getAttribute("sessBranchOfficeRenewCert") != null && String.valueOf(temp1.ID).equals(session.getAttribute("sessBranchOfficeRenewCert").toString()) ? "selected" : ""%>><%=temp1.NAME + " - " + temp1.REMARK%></option>
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
                                                            </select>
                                                        </div>
                                                        <%
                                                            }
                                                        %>
                                                    </div>
                                                    <input type="text" style="display: none;" name="RoleID_ID" id="RoleID_ID" value="<%= SessRoleID_ID%>">
                                                </div>
                                                <div class="col-sm-4" style="padding-left: 0;display: <%=sDisableViewBranch%>; <%=isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA) || isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_MID) ? "clear:both;":"" %>">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;">
                                                            <script>document.write(global_fm_user_create);</script>
                                                        </label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <select name="USER" id="USER" class="form-control123">
                                                                <option value="<%= Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL%>" <%= session.getAttribute("sessUserRenewCert") != null && Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(session.getAttribute("sessUserRenewCert").toString()) ? "selected" : ""%>><script>document.write(global_fm_combox_all);</script></option>
                                                            </select>
                                                        </div>
                                                    </div>
                                                </div>
                                                <script>
                                                    function LOAD_BACKOFFICE_USER(objAgency, idCSRF) {
                                                        alert("agencty: " + document.getElementById("BranchOffice").value);
                                                        if(objAgency === JS_STR_GRID_COMBOBOX_VALUE_ALL)
                                                        {
                                                            var cbxUSER = document.getElementById("USER");
                                                            removeOptions(cbxUSER);
                                                            cbxUSER.options[cbxUSER.options.length] = new Option(global_fm_combox_all, JS_STR_GRID_COMBOBOX_VALUE_ALL);
                                                        }
                                                        else
                                                        {
                                                            $.ajax({
                                                                type: "post",
                                                                url: "../JSONCommon",
                                                                data: {
                                                                    idParam: 'loadadminuser_ofagency',
                                                                    BRANCH_ID: objAgency,
                                                                    CsrfToken: idCSRF
                                                                },
                                                                cache: false,
                                                                success: function (html)
                                                                {
                                                                    if (html.length > 0)
                                                                    {
                                                                        var cbxUSER = document.getElementById("USER");
                                                                        removeOptions(cbxUSER);
                                                                        var obj = JSON.parse(html);
                                                                        if (obj[0].Code === "0")
                                                                        {
                                                                            var idSessUSER = '<%= session.getAttribute("sessUserRenewCert").toString().trim() %>';
                                                                            cbxUSER.options[cbxUSER.options.length] = new Option(global_fm_combox_all, JS_STR_GRID_COMBOBOX_VALUE_ALL);
                                                                            for (var i = 0; i < obj.length; i++) {
                                                                                cbxUSER.options[cbxUSER.options.length] = new Option(obj[i].FULL_NAME + " (" + obj[i].USERNAME + ")", obj[i].ID);
                                                                                if(obj[i].ID === idSessUSER)
                                                                                {
                                                                                    $("#USER option[value='" + obj[i].ID + "']").attr("selected", "selected");
                                                                                }
                                                                            }
                                                                        }
                                                                        else if (obj[0].Code === JS_EX_CSRF)
                                                                        {
                                                                            funCsrfAlert();
                                                                        }
                                                                        else if (obj[0].Code === JS_EX_LOGIN)
                                                                        {
                                                                            RedirectPageLoginNoSess(global_alert_login);
                                                                        }
                                                                        else if (obj[0].Code === JS_EX_ANOTHERLOGIN)
                                                                        {
                                                                            RedirectPageLoginNoSess(global_alert_another_login);
                                                                        }
                                                                        else if (obj[0].Code === "1")
                                                                        {
                                                                            cbxUSER.options[cbxUSER.options.length] = new Option(global_fm_combox_all, JS_STR_GRID_COMBOBOX_VALUE_ALL);
                                                                        }
                                                                        else {
                                                                            funErrorAlert(global_errorsql);
                                                                        }
                                                                    }
                                                                }
                                                            });
                                                            return false;
                                                        }
                                                    }
                                                    $(document).ready(function () {
                                                        if(document.getElementById("BranchOffice").value !== JS_STR_GRID_COMBOBOX_VALUE_ALL)
                                                        {
                                                            LOAD_BACKOFFICE_USER(document.getElementById("BranchOffice").value, '<%= anticsrf%>');
                                                        }
                                                    });
                                                </script>
                                            </div>
                                            <input type="hidden" name="CsrfToken" value="<%=anticsrf%>"/>
                                            <input type="hidden" id="tempCsrfToken" name="tempCsrfToken"/>
                                            <input id="idHiddenLoad" name="nameHiddenLoad" value="" type="hidden"/>
                                        </form>
                                    </div>
                                </div>
                                <%
                                    if (status == 1 && statusLoad == 1) {
                                %>
                                <script type="text/javascript">
                                    $(document).ready(function () {
                                        goToByScroll("idShowResultSearch");
                                    });
                                </script>
                                <div class="x_panel" id="idShowResultSearch">
                                    <div class="x_title" style="border-bottom: 0 solid #E6E9ED;margin-bottom: 0px;">
                                        <h2><i class="fa fa-list-ul"></i> <script>document.write(certlist_title_table);</script></h2>
                                        <ul class="nav navbar-right panel_toolbox">
                                            <li style="color: red;font-weight: bold;">
                                                <%
                                                    if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA) && SessRoleID.equals(Definitions.CONFIG_ROLE_ID_CA_SURVEYOR))
                                                    {} else {
                                                %>
                                                <script>document.write(global_label_grid_sum);</script><%= strMess%>&nbsp;&nbsp;
                                                <%
                                                    }
                                                %>
                                                <%
                                                    boolean isExportEnable = false;
                                                    if(rsPgin[0].length > 0) {
                                                        if (!Definitions.CONFIG_AGENT_ROOT.equals(SessAgentID)) {
                                                            String exportCertEnabled = conf.GetPropertybyCode(Definitions.CONFIG_EXPORT_CERTLIST_AGENCY_ENABLED);
                                                            if("1".equals(exportCertEnabled)) {
                                                                isExportEnable = true;
                                                            }
                                                        } else {
                                                            isExportEnable = true;
                                                        }
                                                        if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA)
                                                            && (SessRoleID_ID.equals(Definitions.CONFIG_ROLE_ID_CA_SURVEYOR) || SessRoleID_ID.equals(Definitions.CONFIG_ROLE_ID_CA_USER))) {
                                                            isExportEnable = false;
                                                        }
                                                    }
                                                    if(isExportEnable == true) {
                                                %>
                                                <button type="button" class="btn btn-info" onClick="ExportCertList('<%= String.valueOf(iTotRslts)%>', '<%= anticsrf%>');"><script>document.write(global_fm_button_export_csv);</script></button>
                                                <script>
                                                    function ExportCertList(countList, idCSRF)
                                                    {
                                                        $('body').append('<div id="over"></div>');
                                                        $(".loading-gif").show();
                                                        $.ajax({
                                                            type: "post",
                                                            url: "../ExportCSVParam",
                                                            data: {
                                                                idParam: "exportcertlist",
                                                                countList: countList,
                                                                CsrfToken: idCSRF
                                                            },
                                                            catche: false,
                                                            success: function (html) {
                                                                var arr = sSpace(html).split('#');
                                                                if (arr[0] === "0")
                                                                {
                                                                    var f = document.form;
                                                                    f.method = "post";
                                                                    f.action = '../DownFromSaveFile?idParam=downfileexportquick&name=' + arr[2];
                                                                    f.submit();
                                                                }
                                                                else if (arr[0] === JS_EX_CSRF) {
                                                                    funCsrfAlert();
                                                                }
                                                                else if (arr[0] === JS_EX_LOGIN) {
                                                                    RedirectPageLoginNoSess(global_alert_login);
                                                                }
                                                                else if (arr[0] === JS_EX_ANOTHERLOGIN)
                                                                {
                                                                    RedirectPageLoginNoSess(global_alert_another_login);
                                                                }
                                                                else if (arr[0] === "1") {
                                                                    funErrorAlert(global_error_export_excel);
                                                                }
                                                                else if (arr[0] === "2") {
                                                                    funErrorAlert(global_succ_NoResult);
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
                                                </script>
                                                <%
                                                    }
                                                %>
                                            </li>
                                        </ul>
                                        <div class="clearfix"></div>
                                    </div>
                                    <div class="x_content">
                                        <input type="hidden" name="iPagNo" value="<%=iPagNo%>">
                                        <input type="hidden" name="cPagNo" value="<%=cPagNo%>">
                                        <input type="hidden" name="iSwRws" value="<%=iSwRws%>">
                                        <style type="text/css">
                                            .table > thead > tr > th, .table > tbody > tr > td{vertical-align: middle;}
                                            .table > thead > tr > th{border-bottom: none;}
                                            .btn{margin-bottom: 0px;}
                                            .table{font-size: 12px;}
                                        </style>
                                        <div class="table-responsive">
                                            <table id="idTableList" class="table table-bordered table-striped projects">
                                                <thead>
                                                <th><script>document.write(global_fm_action);</script></th>
                                                <th><script>document.write(global_fm_STT);</script></th>
                                                <th><script>document.write(global_fm_grid_company);</script></th>
                                                <th style="width: 100px;"><script>document.write(global_fm_enterprise_id);</script></th>
                                                <th style="width: 120px;"><script>document.write(global_fm_grid_personal);</script></th>
                                                <th style="width: 100px;">
                                                    <script>
                                                        document.write(global_fm_personal_id);
                                                    </script>
                                                </th>
                                                <th><script>document.write(global_fm_Method);</script></th>
                                                <th><script>document.write(global_fm_duration_cts);</script></th>
                                                <th style="display: <%= isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_NC) ? "none" : ""%>"><script>document.write(global_fm_certtype);</script></th>
                                                <th style="width: 80px;"><script>document.write(IsWhichCA === '18' ? global_fm_Status : global_fm_Status_cert);</script></th>
                                                <%
                                                    if(!isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_NC)) {
                                                        if(SessRoleID_ID.equals(Definitions.CONFIG_ROLE_ID_CA_USER) || SessRoleID_ID.equals(Definitions.CONFIG_ROLE_ID_AGENT_USER)) {
                                                %>
                                                <th><script>document.write(global_fm_Status_request);</script></th>
                                                <th><script>document.write(cert_fm_type_request);</script></th>
                                                <%
                                                        }
                                                    } else {
                                                %>
                                                <th><script>document.write(global_fm_Status_request);</script></th>
                                                <th><script>document.write(cert_fm_type_request);</script></th>
                                                <%
                                                    }
                                                %>
                                                <th><script>document.write(global_fm_activation_code);</script></th>
                                                <%
                                                    if(!SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                                        if(!SessRoleID_ID.equals(Definitions.CONFIG_ROLE_ID_AGENT_USER)) {
                                                %>
                                                <th><script>document.write(global_fm_user_create);</script></th>
                                                <%
                                                        }
                                                    } else{
                                                %>
                                                <th><script>document.write(global_fm_user_create);</script></th>
                                                <%    
                                                    }
                                                %>
                                                <%
                                                    if(SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                                %>
                                                <th><script>document.write(token_fm_agent);</script></th>
                                                <%
                                                    }
                                                %>
                                                <th><script>document.write(global_fm_date_create);</script></th>
                                                <th><script>document.write(global_fm_valid_cert);</script></th>
                                                <th><script>document.write(token_fm_TimeOffset);</script></th>
                                                <%
                                                    if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA) && SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                                        if(!SessRoleID.equals(Definitions.CONFIG_ROLE_ID_CA_SURVEYOR)) {
                                                %>
                                                <th><script>document.write(token_fm_expire_mmyy);</script></th>
                                                <%
                                                        }
                                                    }
                                                %>
                                                <th><script>document.write(global_fm_serial);</script></th>
                                                <th><script>document.write(token_fm_tokenid);</script></th>
                                                <%
                                                    if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA) && SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                                        if(!SessRoleID.equals(Definitions.CONFIG_ROLE_ID_CA_SURVEYOR)) {
                                                %>
                                                <th><script>document.write(global_fm_phone_contact);</script></th>
                                                <th><script>document.write(global_fm_email_contact);</script></th>
                                                <th><script>document.write(global_fm_phone_contact_real);</script></th>
                                                <th><script>document.write(global_fm_email_contact_real);</script></th>
                                                <%
                                                        }
                                                    }
                                                %>
                                                </thead>
                                                <tbody>
                                                    <%
                                                        if (iPaNoSS > 1) {
                                                            j = ((iPaNoSS - 1) * iSwRws) + 1;
                                                        }
                                                        session.setAttribute("RefreshRenewCertSessNumberPaging", String.valueOf(iPaNoSS));
                                                        if (rsPgin[0].length > 0) {
                                                            for (CERTIFICATION temp1 : rsPgin[0]) {
                                                                String strID = String.valueOf(temp1.ID);
                                                                String isEnterprise = "0";
                                                                if (temp1.CERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_CODE_ENTERPRISE
                                                                    || temp1.CERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_ID_SSL
                                                                    || temp1.CERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_ID_CODE_SIGNING
                                                                    || temp1.CERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_ID_DEVICE)
                                                                {
                                                                    isEnterprise = "1";
                                                                }
                                                                String isRgister = "1";
                                                                if("1".equals(printChangeReissueEnabled))
                                                                {
                                                                    if (temp1.CERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_RENEWAL) {
                                                                        isRgister = "0";
                                                                    } else if(temp1.CERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_CHANGEINFO) {
                                                                        isRgister = "2";
                                                                    } else if(temp1.CERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REVOKE
                                                                        || temp1.CERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REISSUE) {
                                                                        isRgister = "3";
                                                                    }
                                                                } else {
                                                                    if(temp1.CERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_RENEWAL) {
                                                                        isRgister = "0";
                                                                    }
                                                                }
                                                                boolean isRevokeEnabled = true;
                                                                if (!Definitions.CONFIG_AGENT_ROOT.equals(SessAgentID)) {
                                                                    if(temp1.CROSS_CHECK_ENABLED == true) {
                                                                        if("1".equals(sRevokeEnabledDefault)) {
                                                                            isRevokeEnabled = false;
                                                                        }
                                                                    }
                                                                }
                                                                boolean isRevokeNCEnabled = true;
                                                                if("1".equals(temp1.NO_CANCEL_COMMITMENT) && !SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT) && isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_NC)) {
                                                                    isRevokeNCEnabled = false;
                                                                }
                                                                String sAC = temp1.ACTIVATION_CODE;
                                                                if(temp1.CERTIFICATION_ATTR_STATE_ID != Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_APPROVED) {
                                                                    sAC = "";
                                                                }
                                                    %>
                                                    <tr>
                                                        <td>
                                                            <div style="<%=isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA) ? "width: 117px;" : "min-width: 95px;"%>">
                                                                <span>
                                                                <a style="cursor: pointer;" onclick="popupDetailCert('<%=strID%>');" class="btn btn-info btn-xs"><i class="<%=isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA) ? "fa fa-eye" : "fa fa-pencil"%>"></i> <script>document.write(IsWhichCA === "18" ? global_fm_button_detail.toUpperCase() : global_fm_button_detail);</script></a>
                                                                </span>
                                                                <%
                                                                    boolean isViewButtons = false;
                                                                    if(temp1.CERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_DECLINED
                                                                        && temp1.CERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_RENEWED_EXPIRED
                                                                        && temp1.CERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_STOPPED_OPERATION
                                                                        && temp1.CERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_AUTO_REVOKED)
                                                                    {
                                                                        isViewButtons = true;
                                                                        if(temp1.CERTIFICATION_STATE_ID == Definitions.CONFIG_CERTIFICATION_STATE_REVOKED) {
                                                                            if(temp1.PKI_FORMFACTOR_ID == Definitions.CONFIG_PKI_FORMFACTOR_ID_ESIGNCLOUD) {
                                                                                isViewButtons = false;
                                                                            }
                                                                        }
                                                                    }
                                                                %>
                                                                <%
                                                                    if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA)) {
                                                                        if(temp1.PKI_FORMFACTOR_ID != Definitions.CONFIG_PKI_FORMFACTOR_ID_PKI_USIM) {
                                                                            if(isViewButtons == true) {
                                                                %>
                                                                <span>
                                                                <a id="idLinkButtonAction<%=strID%>" class="btn btn-info btn-xs" style="cursor: pointer; color: #ffffff;"
                                                                    onclick="pupopButtonAction('<%=strID%>');"><i class="fa fa-plus"></i> <script>document.write(global_fm_button_add_action);</script>
                                                                </a>
                                                                </span>
                                                                <script>
                                                                    function pupopButtonAction(id) {
                                                                        if(localStorage.getItem("expandViewAction") !== null) {
                                                                            var idOld = localStorage.getItem("expandViewAction");
                                                                            $("#idLinkViewAction"+idOld).css("display","none");
                                                                            localStorage.removeItem("expandViewAction");
                                                                            if(idOld !== id) {
                                                                                $("#idLinkViewAction"+id).css("display","");
                                                                                localStorage.setItem("expandViewAction", id);
                                                                            }
                                                                        } else {
                                                                            $("#idLinkViewAction"+id).css("display","");
                                                                            localStorage.setItem("expandViewAction", id);
                                                                        }
                                                                        
                                                                        if(localStorage.getItem("expandViewPrint") !== null) {
                                                                            var idPrintOld = localStorage.getItem("expandViewPrint");
                                                                            $("#idLinkViewPrint"+idPrintOld).css("display","none");
                                                                            localStorage.removeItem("expandViewPrint");
                                                                        }
                                                                    }
                                                                </script>
                                                                <%
                                                                    }
                                                                %>
                                                                <span id="idLinkViewAction<%=strID%>" style="display: none;">
                                                                    <%
                                                                        if(temp1.PKI_FORMFACTOR_ID != Definitions.CONFIG_PKI_FORMFACTOR_ID_ESIGNCLOUD) {
                                                                            if(temp1.CERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_DECLINED)
                                                                            {
                                                                                if(CommonFunction.CheckRoleFuncValidOrNot(Definitions.CONFIG_ROLE_PROPERTIES_CERT_BUY_MORE,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true) {
                                                                    %>
                                                                    <a style="cursor: pointer;" onclick="popupBuyCertMore('<%=strID%>');" class="btn btn-info btn-xs"><script>document.write(global_fm_button_buymore);</script></a>
                                                                    <%
                                                                                }
                                                                            }
                                                                    %>
                                                                    <%
                                                                        if (temp1.CERTIFICATION_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_PENDING
                                                                            || temp1.CERTIFICATION_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_INIT) {
                                                                            boolean isEditAgent = false;
                                                                            if(!SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                                                                if(!SessRoleID.equals(Definitions.CONFIG_ROLE_ID_AGENT_ADMIN) && !SessRoleID.equals(Definitions.CONFIG_ROLE_ID_AGENT_SURVEYOR)) {
                                                                                    if(SessLevelBranch.equals(Definitions.CONFIG_BRANCH_LEVEL_CHILREN_ONE)) {
                                                                                        isEditAgent = true;
                                                                                    } else {
                                                                                        int intApprove = db.S_BO_CHECK_BRANCH_APPROVED(temp1.CERTIFICATION_ATTR_ID, Integer.parseInt(SessUserAgentID), sessTreeArrayBranchID);
                                                                                        if(intApprove == 1) {
                                                                                            isEditAgent = true;
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }
                                                                    %>
                                                                    <%
                                                                        if(isEditAgent == true) {
                                                                    %>
                                                                    <a style="cursor: pointer;" onclick="popupEditCert('<%=strID%>');" class="btn btn-info btn-xs"><script>document.write(global_button_grid_edit);</script></a>
                                                                    <%
                                                                            }
                                                                        }
                                                                    %>
                                                                    <%
                                                                        }
                                                                    %>
                                                                    <%
                                                                        if(temp1.CERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_DECLINED
                                                                            && temp1.CERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_RENEWED_EXPIRED
                                                                            && temp1.CERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_REVOKED
                                                                            && temp1.CERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_STOPPED_OPERATION
                                                                            && temp1.CERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_AUTO_REVOKED
                                                                            && temp1.CERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_REVISED_KEEP_SN
                                                                            && temp1.CERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_RENEWED_KEEP_SN)
                                                                        {
                                                                            if(temp1.CERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_RENEWED
                                                                                && temp1.CERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_REVISED
                                                                                && temp1.CERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_REISSUED
                                                                                && temp1.CERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_EXPIRED
                                                                                && temp1.CERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_REVOKED
                                                                                && temp1.CERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_NEW
                                                                                && temp1.CERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_OPERATED_PERMANENT_DISABLE
                                                                                && temp1.CERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_OPERATED_TEMPORARY_DISABLE
                                                                                && temp1.CERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_RENEWAL_PERMANENT_DISABLE
                                                                                && temp1.CERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_RENEWAL_TEMPORARY_DISABLE)
                                                                            {
                                                                                if(temp1.PKI_FORMFACTOR_ID != Definitions.CONFIG_PKI_FORMFACTOR_ID_PKI_USIM) {
                                                                    %>
                                                                    <%
                                                                        if(!EscapeUtils.CheckTextNull(temp1.TOKEN_SN).equals(Definitions.CONFIG_TOKEN_SN_LOST))
                                                                        {
                                                                            if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_RENEWAL,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true)
                                                                            {
                                                                    %>
                                                                    <a style="cursor: pointer;" onclick="popupRenewCert('<%=strID%>');" class="btn btn-info btn-xs"><script>document.write(global_fm_button_renew);</script></a><br/>
                                                                    <%
                                                                            }
                                                                        }
                                                                    %>
                                                                    <%
                                                                        if(!EscapeUtils.CheckTextNull(temp1.TOKEN_SN).equals(Definitions.CONFIG_TOKEN_SN_LOST))
                                                                        {
                                                                            if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_CHANGE_INFO,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true)
                                                                            {
                                                                    %>
                                                                    <a style="cursor: pointer;" onclick="popupChangeCert('<%=strID%>');" class="btn btn-info btn-xs"><script>document.write(global_fm_button_changeinfo);</script></a><br/>
                                                                    <%
                                                                            }
                                                                        }
                                                                    %>
                                                                    <%
                                                                        if(CommonFunction.checkHardTokenIDEnabled(temp1.PKI_FORMFACTOR_ID) == true)
                                                                        {
                                                                            if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_REISSUE,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true) {
                                                                    %>
                                                                    <a style="cursor: pointer;" onclick="popupReIssueCert('<%=strID%>');" class="btn btn-info btn-xs"><script>document.write(global_fm_button_reissue);</script></a><br/>
                                                                    <%
                                                                            }
                                                                        }
                                                                    %>
                                                                    <%
                                                                        if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_REVOKE,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true)
                                                                        {
                                                                            if(isRevokeEnabled == true) {
                                                                    %>
                                                                    <a style="cursor: pointer;" onclick="popupRevokeCert('<%=strID%>');" class="btn btn-info btn-xs"><script>document.write(global_fm_button_revoke);</script></a><br/>
                                                                    <%
                                                                            }
                                                                        }
                                                                    %>
                                                                    <%
                                                                        if(temp1.PKI_FORMFACTOR_ID != Definitions.CONFIG_PKI_FORMFACTOR_ID_ESIGNCLOUD) {
                                                                            if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_PERMANENT_DISABLE,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true
                                                                                || CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_TEMPORARY_DISABLE,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true)
                                                                            {
                                                                    %>
                                                                    <a style="cursor: pointer;" onclick="popupSuspendCert('<%=strID%>');" class="btn btn-info btn-xs"><script>document.write(global_fm_button_suspend);</script></a><br/>
                                                                    <%
                                                                                    }
                                                                                }
                                                                            }
                                                                            } else {
                                                                                if(temp1.PKI_FORMFACTOR_ID != Definitions.CONFIG_PKI_FORMFACTOR_ID_PKI_USIM) {
                                                                                    if(temp1.CERTIFICATION_STATE_ID == Definitions.CONFIG_CERTIFICATION_STATE_RENEWED
                                                                                        || temp1.CERTIFICATION_STATE_ID == Definitions.CONFIG_CERTIFICATION_STATE_REVISED
                                                                                        || temp1.CERTIFICATION_STATE_ID == Definitions.CONFIG_CERTIFICATION_STATE_REISSUED)
                                                                                    {
                                                                                        if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_REVOKE,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true)
                                                                                        {
                                                                                            if(isRevokeEnabled == true) {
                                                                    %>
                                                                    <a style="cursor: pointer;" onclick="popupRevokeCert('<%=strID%>');" class="btn btn-info btn-xs"><script>document.write(global_fm_button_revoke);</script></a><br/>
                                                                    <%
                                                                                        }
                                                                                    }
                                                                                    if(temp1.PKI_FORMFACTOR_ID != Definitions.CONFIG_PKI_FORMFACTOR_ID_ESIGNCLOUD)
                                                                                    {
                                                                                        if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_PERMANENT_DISABLE,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true
                                                                                            || CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_TEMPORARY_DISABLE,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true)
                                                                                        {
                                                                    %>
                                                                    <!--<a style="cursor: pointer;" onclick="popupSuspendCert('<=strID%>');" class="btn btn-info btn-xs"><i class="fa fa-pencil"></i> <script>document.write(global_fm_button_suspend);</script></a>-->
                                                                    <%
                                                                                        }
                                                                                    }
                                                                                }
                                                                                if(temp1.CERTIFICATION_STATE_ID == Definitions.CONFIG_CERTIFICATION_STATE_EXPIRED)
                                                                                {
                                                                                    if(!EscapeUtils.CheckTextNull(temp1.TOKEN_SN).equals(Definitions.CONFIG_TOKEN_SN_LOST))
                                                                                    {
                                                                                        if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_RENEWAL,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true)
                                                                                        {
                                                                    %>
                                                                    <a style="cursor: pointer;" onclick="popupRenewCert('<%=strID%>');" class="btn btn-info btn-xs"><script>document.write(global_fm_button_renew);</script></a><br/>
                                                                    <%
                                                                                        }
                                                                                    }
                                                                                }
                                                                                if(temp1.PKI_FORMFACTOR_ID != Definitions.CONFIG_PKI_FORMFACTOR_ID_ESIGNCLOUD) {
                                                                                    if(temp1.CERTIFICATION_STATE_ID == Definitions.CONFIG_CERTIFICATION_STATE_OPERATED_PERMANENT_DISABLE
                                                                                        || temp1.CERTIFICATION_STATE_ID == Definitions.CONFIG_CERTIFICATION_STATE_OPERATED_TEMPORARY_DISABLE
                                                                                        || temp1.CERTIFICATION_STATE_ID == Definitions.CONFIG_CERTIFICATION_STATE_RENEWAL_PERMANENT_DISABLE
                                                                                        || temp1.CERTIFICATION_STATE_ID == Definitions.CONFIG_CERTIFICATION_STATE_RENEWAL_TEMPORARY_DISABLE)
                                                                                    {
                                                                                        if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_RECOVERED,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true)
                                                                                        {
                                                                    %>
                                                                    <a style="cursor: pointer;" onclick="popupRecoveryCert('<%=strID%>');" class="btn btn-info btn-xs"><script>document.write(global_fm_button_recovery);</script></a><br/>
                                                                    <%
                                                                                        }
                                                                                    }
                                                                                }
                                                                                }
                                                                            }
                                                                        }
                                                                    %>
                                                                </span>
                                                                <%
                                                                    if(temp1.CERTIFICATION_STATE_ID == Definitions.CONFIG_CERTIFICATION_STATE_REVOKED
                                                                        || temp1.CERTIFICATION_STATE_ID == Definitions.CONFIG_CERTIFICATION_STATE_REVISED_KEEP_SN
                                                                        || temp1.CERTIFICATION_STATE_ID == Definitions.CONFIG_CERTIFICATION_STATE_RENEWED_KEEP_SN
                                                                        || temp1.CERTIFICATION_STATE_ID == Definitions.CONFIG_CERTIFICATION_STATE_RENEWED
                                                                        || temp1.CERTIFICATION_STATE_ID == Definitions.CONFIG_CERTIFICATION_STATE_REVISED
                                                                        || temp1.CERTIFICATION_STATE_ID == Definitions.CONFIG_CERTIFICATION_STATE_REISSUED
                                                                        || temp1.CERTIFICATION_STATE_ID == Definitions.CONFIG_CERTIFICATION_STATE_OPERATED_PERMANENT_DISABLE
                                                                        || temp1.CERTIFICATION_STATE_ID == Definitions.CONFIG_CERTIFICATION_STATE_OPERATED_TEMPORARY_DISABLE
                                                                        || temp1.CERTIFICATION_STATE_ID == Definitions.CONFIG_CERTIFICATION_STATE_RENEWAL_PERMANENT_DISABLE
                                                                        || temp1.CERTIFICATION_STATE_ID == Definitions.CONFIG_CERTIFICATION_STATE_RENEWAL_TEMPORARY_DISABLE){
                                                                        isViewButtons = false;
                                                                    }
                                                                    if(isViewButtons == true) {
                                                                %>
                                                                <span>
                                                                <a id="idLinkButtonPrint<%=strID%>" class="btn btn-info btn-xs" style="cursor: pointer; color: #ffffff;"
                                                                    onclick="pupopButtonPrint('<%=strID%>');"><i class="fa fa-print"></i> <script>document.write(global_fm_button_print_profile);</script>
                                                                </a>
                                                                </span>
                                                                <script>
                                                                    function pupopButtonPrint(id) {
                                                                        if(localStorage.getItem("expandViewPrint") !== null) {
                                                                            var idOld = localStorage.getItem("expandViewPrint");
                                                                            $("#idLinkViewPrint"+idOld).css("display","none");
                                                                            localStorage.removeItem("expandViewPrint");
                                                                            if(idOld !== id) {
                                                                                $("#idLinkViewPrint"+id).css("display","");
                                                                                localStorage.setItem("expandViewPrint", id);
                                                                            }
                                                                        } else {
                                                                            $("#idLinkViewPrint"+id).css("display","");
                                                                            localStorage.setItem("expandViewPrint", id);
                                                                        }
                                                                        
                                                                        if(localStorage.getItem("expandViewAction") !== null) {
                                                                            var idActionOld = localStorage.getItem("expandViewAction");
                                                                            $("#idLinkViewAction"+idActionOld).css("display","none");
                                                                            localStorage.removeItem("expandViewAction");
                                                                        }
                                                                    }
                                                                </script>
                                                                <%
                                                                    }
                                                                %>
                                                                <span id="idLinkViewPrint<%=strID%>" style="display: none;">
                                                                    <%
                                                                        if(temp1.CERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_DECLINED
                                                                            && temp1.CERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_RENEWED_EXPIRED
                                                                            && temp1.CERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_REVOKED
                                                                            && temp1.CERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_STOPPED_OPERATION
                                                                            && temp1.CERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_AUTO_REVOKED
                                                                            && temp1.CERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_REVISED_KEEP_SN
                                                                            && temp1.CERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_RENEWED_KEEP_SN)
                                                                        {
                                                                            if(temp1.CERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_RENEWED
                                                                                && temp1.CERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_REVISED
                                                                                && temp1.CERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_REISSUED
                                                                                && temp1.CERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_EXPIRED
                                                                                && temp1.CERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_REVOKED
                                                                                && temp1.CERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_NEW
                                                                                && temp1.CERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_OPERATED_PERMANENT_DISABLE
                                                                                && temp1.CERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_OPERATED_TEMPORARY_DISABLE
                                                                                && temp1.CERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_RENEWAL_PERMANENT_DISABLE
                                                                                && temp1.CERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_RENEWAL_TEMPORARY_DISABLE)
                                                                            {
                                                                    %>
                                                                    <a style="cursor: pointer;" onclick="popupPrintCertList('<%=strID%>', '<%= anticsrf%>');" class="btn btn-info btn-xs"><script>document.write(global_fm_button_print_certificate);</script></a>
                                                                    <a style="cursor: pointer;" onclick="popupPrintHandoverList('<%=strID%>', '<%=printDeliveryCAOption%>');" class="btn btn-info btn-xs"><script>document.write(global_fm_button_print_handover);</script> </a>
                                                                    <a style="cursor: pointer;" onclick="ShowDialogList('<%=strID%>', '<%= isEnterprise%>', '<%= isRgister%>', '<%= printRegisterCAOption%>', '<%=isCALoad%>', '<%= anticsrf%>');" class="btn btn-info btn-xs"><script>document.write(global_fm_button_print_regis);</script></a>
                                                                    <a style="cursor: pointer;" onclick="ShowDialogConfirm('<%=strID%>', '<%= isEnterprise%>', '<%=isCALoad%>', '<%= anticsrf%>');" class="btn btn-info btn-xs"><script>document.write(global_fm_button_print_confirm);</script> (DK02)</a>
                                                                    <%
                                                                            } else {
                                                                                if(temp1.PKI_FORMFACTOR_ID != Definitions.CONFIG_PKI_FORMFACTOR_ID_PKI_USIM) {
                                                                                if(temp1.CERTIFICATION_STATE_ID == Definitions.CONFIG_CERTIFICATION_STATE_EXPIRED)
                                                                                {
                                                                    %>
                                                                    <a style="cursor: pointer;" onclick="ShowDialogList('<%=strID%>', '<%= isEnterprise%>', '<%= isRgister%>', '<%= printRegisterCAOption%>', '<%=isCALoad%>', '<%= anticsrf%>');" class="btn btn-info btn-xs"><script>document.write(global_fm_button_print_regis);</script></a>
                                                                    <a style="cursor: pointer;" onclick="ShowDialogConfirm('<%=strID%>', '<%= isEnterprise%>', '<%=isCALoad%>', '<%= anticsrf%>');" class="btn btn-info btn-xs"><script>document.write(global_fm_button_print_confirm);</script> (DK02)</a>
                                                                    <%
                                                                                }
                                                                                if(temp1.CERTIFICATION_STATE_ID == Definitions.CONFIG_CERTIFICATION_STATE_NEW)
                                                                                {
                                                                    %>
                                                                    <%
                                                                        if(temp1.CERTIFICATION_ATTR_STATE_ID != Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_PENDING
                                                                            && temp1.CERTIFICATION_ATTR_STATE_ID != Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_INIT) {
                                                                    %>
                                                                    <a style="cursor: pointer;" onclick="popupPrintHandoverList('<%=strID%>', '<%= printDeliveryCAOption%>');" class="btn btn-info btn-xs"><script>document.write(global_fm_button_print_handover);</script></a>
                                                                    <%
                                                                        }
                                                                    %>
                                                                    <a style="cursor: pointer;" onclick="ShowDialogList('<%=strID%>', '<%= isEnterprise%>', '<%= isRgister%>', '<%= printRegisterCAOption%>', '<%=isCALoad%>', '<%= anticsrf%>');" class="btn btn-info btn-xs"><script>document.write(global_fm_button_print_regis);</script></a>
                                                                    <a style="cursor: pointer;" onclick="ShowDialogConfirm('<%=strID%>', '<%= isEnterprise%>', '<%=isCALoad%>', '<%= anticsrf%>');" class="btn btn-info btn-xs"><script>document.write(global_fm_button_print_confirm);</script> (DK02)</a>
                                                                    <%
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    %>
                                                                </span>
                                                                <%
                                                                    }
                                                                %>
                                                                <%
                                                                    } else {
                                                                        if(temp1.PKI_FORMFACTOR_ID != Definitions.CONFIG_PKI_FORMFACTOR_ID_PKI_USIM) {
                                                                            if(isViewButtons == true) {
                                                                %>
                                                                &nbsp;<a id="idLinkButtonExpand<%=strID%>" class="btn btn-info btn-xs" style="cursor: pointer; color: #ffffff; text-decoration: underline;" onclick="popupButtonExpand('<%=strID%>');"> <i class="fa fa-angle-double-down"></i> </a>
                                                                <a id="idLinkButtonHide<%=strID%>" class="btn btn-info btn-xs" style="cursor: pointer; color: #ffffff; text-decoration: underline;display: none;" onclick="popupButtonHide('<%=strID%>');"> <i class="fa fa-angle-double-up"></i> </a>
                                                                <script>
                                                                    $('#idLinkButtonExpand'+'<%=strID%>').prop('title', global_fm_expand);
                                                                    $('#idLinkButtonHide'+'<%=strID%>').prop('title', global_fm_collapse);
                                                                    function popupButtonExpand(id)
                                                                    {
                                                                        $("#idViewButtonExpand"+id).css("display","");
                                                                        $("#idLinkButtonExpand"+id).css("display","none");
                                                                        $("#idLinkButtonHide"+id).css("display","");
                                                                    }
                                                                    function popupButtonHide(id)
                                                                    {
                                                                        $("#idViewButtonExpand"+id).css("display","none");
                                                                        $("#idLinkButtonExpand"+id).css("display","");
                                                                        $("#idLinkButtonHide"+id).css("display","none");
                                                                    }
                                                                </script>
                                                                <%
                                                                    }
                                                                %>
                                                                <span id="idViewButtonExpand<%=strID%>" style="display: none;">
                                                                    <%
                                                                        if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_HILO)) {
                                                                            if (temp1.CERTIFICATION_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_PENDING
                                                                                || temp1.CERTIFICATION_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_INIT) {
                                                                                boolean isEditAgent = false;
                                                                                if(!SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                                                                    if(!SessRoleID.equals(Definitions.CONFIG_ROLE_ID_AGENT_ADMIN) && !SessRoleID.equals(Definitions.CONFIG_ROLE_ID_AGENT_SURVEYOR)) {
                                                                                        if(SessLevelBranch.equals(Definitions.CONFIG_BRANCH_LEVEL_CHILREN_ONE)) {
                                                                                            isEditAgent = true;
                                                                                        } else {
                                                                                            int intApprove = db.S_BO_CHECK_BRANCH_APPROVED(temp1.CERTIFICATION_ATTR_ID, Integer.parseInt(SessUserAgentID), sessTreeArrayBranchID);
                                                                                            if(intApprove == 1) {
                                                                                                isEditAgent = true;
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }
                                                                    %>
                                                                    <%
                                                                        if(isEditAgent == true) {
                                                                    %>
                                                                    <a style="cursor: pointer;" onclick="popupEditCert('<%=strID%>');" class="btn btn-info btn-xs"><i class="<%=isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA) ? "fa fa-eye" : "fa fa-pencil"%>"></i> <script>document.write(IsWhichCA === "18" ? global_button_grid_edit.toUpperCase() : global_button_grid_edit);</script></a>
                                                                    <%
                                                                                }
                                                                            }
                                                                        }
                                                                    %>
                                                                    <%
                                                                        if(temp1.PKI_FORMFACTOR_ID != Definitions.CONFIG_PKI_FORMFACTOR_ID_ESIGNCLOUD) {
                                                                            if(temp1.CERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_DECLINED)
                                                                            {
                                                                                if(CommonFunction.CheckRoleFuncValidOrNot(Definitions.CONFIG_ROLE_PROPERTIES_CERT_BUY_MORE,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true) {
                                                                    %>
                                                                    <a style="cursor: pointer;" onclick="popupBuyCertMore('<%=strID%>');" class="btn btn-info btn-xs"><i class="fa fa-pencil"></i> <script>document.write(global_fm_button_buymore);</script></a>
                                                                    <%
                                                                                }
                                                                            }
                                                                        }
                                                                    %>
                                                                    <%
                                                                        if(temp1.CERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_DECLINED
                                                                            && temp1.CERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_RENEWED_EXPIRED
                                                                            && temp1.CERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_REVOKED
                                                                            && temp1.CERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_STOPPED_OPERATION
                                                                            && temp1.CERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_AUTO_REVOKED
                                                                            && temp1.CERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_REVISED_KEEP_SN
                                                                            && temp1.CERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_RENEWED_KEEP_SN)
                                                                        {
                                                                            if(temp1.CERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_RENEWED
                                                                                && temp1.CERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_REVISED
                                                                                && temp1.CERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_REISSUED
                                                                                && temp1.CERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_EXPIRED
                                                                                && temp1.CERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_REVOKED
                                                                                && temp1.CERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_NEW
                                                                                && temp1.CERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_OPERATED_PERMANENT_DISABLE
                                                                                && temp1.CERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_OPERATED_TEMPORARY_DISABLE
                                                                                && temp1.CERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_RENEWAL_PERMANENT_DISABLE
                                                                                && temp1.CERTIFICATION_STATE_ID != Definitions.CONFIG_CERTIFICATION_STATE_RENEWAL_TEMPORARY_DISABLE)
                                                                            {
                                                                                if(temp1.PKI_FORMFACTOR_ID != Definitions.CONFIG_PKI_FORMFACTOR_ID_PKI_USIM) {
                                                                    %>
                                                                    <%
                                                                        if(!EscapeUtils.CheckTextNull(temp1.TOKEN_SN).equals(Definitions.CONFIG_TOKEN_SN_LOST))
                                                                        {
                                                                            if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_RENEWAL,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true)
                                                                            {
                                                                    %>
                                                                    <a style="cursor: pointer;" onclick="popupRenewCert('<%=strID%>');" class="btn btn-info btn-xs"><i class="fa fa-pencil"></i> <script>document.write(global_fm_button_renew);</script></a>
                                                                    <%
                                                                            }
                                                                        }
                                                                    %>
                                                                    <%
                                                                        if(!EscapeUtils.CheckTextNull(temp1.TOKEN_SN).equals(Definitions.CONFIG_TOKEN_SN_LOST))
                                                                        {
                                                                            if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_CHANGE_INFO,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true)
                                                                            {
                                                                    %>
                                                                    <a style="cursor: pointer;" onclick="popupChangeCert('<%=strID%>');" class="btn btn-info btn-xs"><i class="fa fa-pencil"></i> <script>document.write(global_fm_button_changeinfo);</script></a>
                                                                    <%
                                                                            }
                                                                        }
                                                                    %>
                                                                    <%
                                                                        if(CommonFunction.checkHardTokenIDEnabled(temp1.PKI_FORMFACTOR_ID) == true)
                                                                        {
                                                                            if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_REISSUE,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true) {
                                                                    %>
                                                                    <a style="cursor: pointer;" onclick="popupReIssueCert('<%=strID%>');" class="btn btn-info btn-xs"><i class="fa fa-pencil"></i> <script>document.write(global_fm_button_reissue);</script></a>
                                                                    <%
                                                                            }
                                                                        }
                                                                    %>
                                                                    <%
                                                                        if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_REVOKE,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true)
                                                                        {
                                                                            if(isRevokeEnabled == true && isRevokeNCEnabled == true) {
                                                                    %>
                                                                    <a style="cursor: pointer;" onclick="popupRevokeCert('<%=strID%>');" class="btn btn-info btn-xs"><i class="fa fa-pencil"></i> <script>document.write(global_fm_button_revoke);</script></a>
                                                                    <%
                                                                            }
                                                                        }
                                                                    %>
                                                                    <%
                                                                        if(temp1.PKI_FORMFACTOR_ID != Definitions.CONFIG_PKI_FORMFACTOR_ID_ESIGNCLOUD) {
                                                                            if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_PERMANENT_DISABLE,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true
                                                                                || CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_TEMPORARY_DISABLE,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true)
                                                                            {
                                                                    %>
                                                                    <a style="cursor: pointer;" onclick="popupSuspendCert('<%=strID%>');" class="btn btn-info btn-xs"><i class="fa fa-pencil"></i> <script>document.write(global_fm_button_suspend);</script></a>
                                                                    <%
                                                                                }
                                                                            }
                                                                        }
                                                                    %>
                                                                    <a style="cursor: pointer;" onclick="popupPrintCertList('<%=strID%>', '<%= anticsrf%>');" class="btn btn-info btn-xs"><i class="fa fa-pencil"></i> <script>document.write(global_fm_button_print_certificate);</script></a>
                                                                    <a style="cursor: pointer;" onclick="popupPrintHandoverList('<%=strID%>', '<%=printDeliveryCAOption%>');" class="btn btn-info btn-xs"><i class="fa fa-pencil"></i> <script>document.write(global_fm_button_print_handover);</script> </a>
                                                                    <a style="cursor: pointer;" onclick="ShowDialogList('<%=strID%>', '<%= isEnterprise%>', '<%= isRgister%>', '<%= printRegisterCAOption%>', '<%=isCALoad%>', '<%= anticsrf%>');" class="btn btn-info btn-xs"><i class="fa fa-pencil"></i> <script>document.write(global_fm_button_print_regis);</script></a>
                                                                    <a style="cursor: pointer;" onclick="ShowDialogConfirm('<%=strID%>', '<%= isEnterprise%>', '<%=isCALoad%>', '<%= anticsrf%>');" class="btn btn-info btn-xs"><i class="fa fa-pencil"></i> <script>document.write(global_fm_button_print_confirm);</script></a>
                                                                    <%
                                                                            } else {
                                                                                if(temp1.PKI_FORMFACTOR_ID != Definitions.CONFIG_PKI_FORMFACTOR_ID_PKI_USIM) {
                                                                                    if(temp1.CERTIFICATION_STATE_ID == Definitions.CONFIG_CERTIFICATION_STATE_RENEWED
                                                                                        || temp1.CERTIFICATION_STATE_ID == Definitions.CONFIG_CERTIFICATION_STATE_REVISED
                                                                                        || temp1.CERTIFICATION_STATE_ID == Definitions.CONFIG_CERTIFICATION_STATE_REISSUED)
                                                                                    {
                                                                                        if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_REVOKE,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true)
                                                                                        {
                                                                                            if(isRevokeEnabled == true && isRevokeNCEnabled == true) {
                                                                    %>
                                                                    <a style="cursor: pointer;" onclick="popupRevokeCert('<%=strID%>');" class="btn btn-info btn-xs"><i class="fa fa-pencil"></i> <script>document.write(global_fm_button_revoke);</script></a>
                                                                    <%
                                                                                        }
                                                                                    }
                                                                                    if(temp1.PKI_FORMFACTOR_ID != Definitions.CONFIG_PKI_FORMFACTOR_ID_ESIGNCLOUD)
                                                                                    {
                                                                                        if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_PERMANENT_DISABLE,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true
                                                                                            || CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_TEMPORARY_DISABLE,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true)
                                                                                        {
                                                                    %>
                                                                    <!--<a style="cursor: pointer;" onclick="popupSuspendCert('<=strID%>');" class="btn btn-info btn-xs"><i class="fa fa-pencil"></i> <script>document.write(global_fm_button_suspend);</script></a>-->
                                                                    <%
                                                                                        }
                                                                                    }
                                                                                }
                                                                                if(temp1.CERTIFICATION_STATE_ID == Definitions.CONFIG_CERTIFICATION_STATE_EXPIRED)
                                                                                {
                                                                                    if(!EscapeUtils.CheckTextNull(temp1.TOKEN_SN).equals(Definitions.CONFIG_TOKEN_SN_LOST))
                                                                                    {
                                                                                        if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_RENEWAL,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true)
                                                                                        {
                                                                    %>
                                                                    <a style="cursor: pointer;" onclick="popupRenewCert('<%=strID%>');" class="btn btn-info btn-xs"><i class="fa fa-pencil"></i> <script>document.write(global_fm_button_renew);</script></a>
                                                                    <%
                                                                                        }
                                                                                    }
                                                                                    %>
                                                                    <a style="cursor: pointer;" onclick="ShowDialogList('<%=strID%>', '<%= isEnterprise%>', '<%= isRgister%>', '<%= printRegisterCAOption%>', '<%=isCALoad%>', '<%= anticsrf%>');" class="btn btn-info btn-xs"><i class="fa fa-pencil"></i> <script>document.write(global_fm_button_print_regis);</script> </a>
                                                                    <a style="cursor: pointer;" onclick="ShowDialogConfirm('<%=strID%>', '<%= isEnterprise%>', '<%=isCALoad%>', '<%= anticsrf%>');" class="btn btn-info btn-xs"><i class="fa fa-pencil"></i> <script>document.write(global_fm_button_print_confirm);</script></a>
                                                                    <%
                                                                                }
                                                                                if(temp1.PKI_FORMFACTOR_ID != Definitions.CONFIG_PKI_FORMFACTOR_ID_ESIGNCLOUD) {
                                                                                    if(temp1.CERTIFICATION_STATE_ID == Definitions.CONFIG_CERTIFICATION_STATE_OPERATED_PERMANENT_DISABLE
                                                                                        || temp1.CERTIFICATION_STATE_ID == Definitions.CONFIG_CERTIFICATION_STATE_OPERATED_TEMPORARY_DISABLE
                                                                                        || temp1.CERTIFICATION_STATE_ID == Definitions.CONFIG_CERTIFICATION_STATE_RENEWAL_PERMANENT_DISABLE
                                                                                        || temp1.CERTIFICATION_STATE_ID == Definitions.CONFIG_CERTIFICATION_STATE_RENEWAL_TEMPORARY_DISABLE)
                                                                                    {
                                                                                        if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_RECOVERED,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true)
                                                                                        {
                                                                    %>
                                                                    <a style="cursor: pointer;" onclick="popupRecoveryCert('<%=strID%>');" class="btn btn-info btn-xs"><i class="fa fa-pencil"></i> <script>document.write(global_fm_button_recovery);</script></a>
                                                                    <%
                                                                                        }
                                                                                    }
                                                                                }
                                                                                if(temp1.CERTIFICATION_STATE_ID == Definitions.CONFIG_CERTIFICATION_STATE_NEW)
                                                                                {
                                                                    %>
                                                                    <%
                                                                        if(temp1.CERTIFICATION_ATTR_STATE_ID != Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_PENDING
                                                                            && temp1.CERTIFICATION_ATTR_STATE_ID != Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_INIT) {
                                                                    %>
                                                                    <a style="cursor: pointer;" onclick="popupPrintHandoverList('<%=strID%>', '<%= printDeliveryCAOption%>');" class="btn btn-info btn-xs"><i class="fa fa-pencil"></i> <script>document.write(global_fm_button_print_handover);</script></a>
                                                                    <%
                                                                        }
                                                                    %>
                                                                    <a style="cursor: pointer;" onclick="ShowDialogList('<%=strID%>', '<%= isEnterprise%>', '<%= isRgister%>', '<%= printRegisterCAOption%>', '<%=isCALoad%>', '<%= anticsrf%>');" class="btn btn-info btn-xs"><i class="fa fa-pencil"></i> <script>document.write(global_fm_button_print_regis);</script> </a>
                                                                    <a style="cursor: pointer;" onclick="ShowDialogConfirm('<%=strID%>', '<%= isEnterprise%>', '<%=isCALoad%>', '<%= anticsrf%>');" class="btn btn-info btn-xs"><i class="fa fa-pencil"></i> <script>document.write(global_fm_button_print_confirm);</script></a>
                                                                    <%
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    %>
                                                                </span>
                                                                <%
                                                                        }
                                                                    }
                                                                %>
                                                            </div>
                                                        </td>
                                                        <td style="text-align: center;"><%= com.convertMoney(j)%></td>
                                                        <td>
                                                            <div style="min-width: 140px;">
                                                                <%= EscapeUtils.CheckTextNull(temp1.COMPANY_NAME)%>
                                                            </div>
                                                        </td>
                                                        <td><a style="color: blue;" data-toggle="tooltipPrefix" title="<%= temp1.ENTERPRISE_ID_REMARK%>"><%= temp1.ENTERPRISE_ID%></a></td>
                                                        <td><%= EscapeUtils.CheckTextNull(temp1.PERSONAL_NAME)%></td>
                                                        <td><a style="color: blue;" data-toggle="tooltipPrefix" title="<%= temp1.PERSONAL_ID_REMARK%>"><%= temp1.PERSONAL_ID%></a></td>
                                                        <td><%= temp1.PKI_FORMFACTOR_DESC%></td>
                                                        <td><%= !isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_NC) ? "[" + EscapeUtils.CheckTextNull(temp1.CERTIFICATION_PROFILE_NAME) + "] " + temp1.CERTIFICATION_PROFILE_DESC : temp1.CERTIFICATION_PROFILE_NAME %></td>
                                                        <td style="display: <%= isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_NC) ? "none" : ""%>"><%= EscapeUtils.CheckTextNull(temp1.CERTIFICATION_PURPOSE_DESC)%></td>
                                                        <td><%= EscapeUtils.CheckTextNull(temp1.CERTIFICATION_STATE_DESC)%></td>
                                                        <%
                                                            if(!isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_NC)) {
                                                                if(SessRoleID_ID.equals(Definitions.CONFIG_ROLE_ID_CA_USER) || SessRoleID_ID.equals(Definitions.CONFIG_ROLE_ID_AGENT_USER)) {
                                                        %>
                                                        <td><%= EscapeUtils.CheckTextNull(temp1.CERTIFICATION_ATTR_STATE_DESC)%></td>
                                                        <td><%= EscapeUtils.CheckTextNull(temp1.SERVICE_TYPE_DESC)%></td>
                                                        <%
                                                                }
                                                            } else {
                                                        %>
                                                        <td><%= EscapeUtils.CheckTextNull(temp1.CERTIFICATION_ATTR_STATE_DESC)%></td>
                                                        <td><%= EscapeUtils.CheckTextNull(temp1.SERVICE_TYPE_DESC)%></td>
                                                        <%
                                                        }
                                                        %>
                                                        <td><%= sAC%></td>
                                                        <%
                                                            if(!SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT))
                                                            {
                                                                if(!SessRoleID_ID.equals(Definitions.CONFIG_ROLE_ID_AGENT_USER)) {
                                                        %>
                                                        <td><%= EscapeUtils.CheckTextNull(temp1.CREATED_BY)%></td>
                                                        <%
                                                                }
                                                            }
                                                            else
                                                            {
                                                        %>
                                                        <td><%= EscapeUtils.CheckTextNull(temp1.CREATED_BY)%></td>
                                                        <%
                                                            }
                                                        %>
                                                        <%
                                                            if(SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                                        %>
                                                        <td><%= EscapeUtils.CheckTextNull(temp1.BRANCH_DESC)%></td>
                                                        <%
                                                            }
                                                        %>
                                                        <td><%= EscapeUtils.CheckTextNull(temp1.CREATED_DT)%></td>
                                                        <td><%= EscapeUtils.CheckTextNull(temp1.EFFECTIVE_DT)%></td>
                                                        <td><%= temp1.EXPIRATION_CONTRACT_DT%></td>
                                                        <%
                                                            if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA) && SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                                                if(!SessRoleID.equals(Definitions.CONFIG_ROLE_ID_CA_SURVEYOR)) {
                                                        %>
                                                        <td><%= EscapeUtils.CheckTextNull(temp1.EXPIRATION_DT_DESC)%></td>
                                                        <%      }
                                                            }
                                                        %>
                                                        <td><%= EscapeUtils.CheckTextNull(temp1.CERTIFICATION_SN)%></td>
                                                        <td><%= CommonFunction.checkViewTokenValid(EscapeUtils.CheckTextNull(temp1.TOKEN_SN)) ? EscapeUtils.CheckTextNull(temp1.TOKEN_SN) : "" %></td>
                                                        <%
                                                            if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA) && SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                                                if(!SessRoleID.equals(Definitions.CONFIG_ROLE_ID_CA_SURVEYOR)) {
                                                        %>
                                                        <td><%= EscapeUtils.CheckTextNull(temp1.PHONE_CONTRACT)%></td>
                                                        <td><%= EscapeUtils.CheckTextNull(temp1.EMAIL_CONTRACT)%></td>
                                                        <td><%= EscapeUtils.CheckTextNull(temp1.PHONE_CONTRACT_REAL)%></td>
                                                        <td><%= EscapeUtils.CheckTextNull(temp1.EMAIL_CONTRACT_REAL)%></td>
                                                        <%
                                                                }
                                                            }
                                                        %>
                                                    </tr>
                                                    <%
                                                                j++;
                                                            }
                                                        }
                                                    %>
                                                    <%
                                                        if (iTotRslts < (iPagNo + iSwRws)) {
                                                            iEnRsNo = iTotRslts;
                                                        } else {
                                                            iEnRsNo = (iPagNo + iSwRws);
                                                        }
                                                        iTotPags = ((int) (Math.ceil((double) iTotRslts / iSwRws)));
                                                    %>
                                                    <tr>
                                                        <script>
                                                            $(document).ready(function () {
                                                                document.getElementById("idTDPaging").colSpan = document.getElementById('idTableList').rows[0].cells.length;
                                                            });
                                                        </script>
                                                        <td id="idTDPaging" style="text-align: left;">
                                                            <div class="paging_table">
                                                                <%
                                                                    int i = 0;
                                                                    int cPge = 0;
                                                                    if (iTotRslts > iSwRws) {
                                                                        String uriPage = request.getRequestURI();
                                                                        String namePage = uriPage.substring(uriPage.lastIndexOf("/")+1);
                                                                        cPge = ((int) (Math.ceil((double) iEnRsNo / (iTotSrhRcrds * iSwRws))));
                                                                        int prePageNo = (cPge * iTotSrhRcrds) - ((iTotSrhRcrds - 1) + iTotSrhRcrds);
                                                                        if ((cPge * iTotSrhRcrds) - (iTotSrhRcrds) > 0) {
                                                                %>
                                                                <a href="<%=namePage%>?iPagNo=<%=prePageNo%>&cPagNo=<%=prePageNo%>"><< <script>document.write(global_paging_Before);</script></a>
                                                                &nbsp;
                                                                <%
                                                                    }
                                                                    for (i = ((cPge * iTotSrhRcrds) - (iTotSrhRcrds - 1)); i <= (cPge * iTotSrhRcrds); i++) {
                                                                        if (i == ((iPagNo / iSwRws) + 1)) {
                                                                %>
                                                                <a href="<%=namePage%>?iPagNo=<%=i%>" style="cursor:pointer;color:red;"><b><%=i%></b></a>
                                                                        <%                   } else if (i <= iTotPags) {
                                                                        %>
                                                                &nbsp;<a href="<%=namePage%>?iPagNo=<%=i%>"><%=i%></a>
                                                                <%
                                                                        }
                                                                    }

                                                                    if (iTotPags > iTotSrhRcrds && i <= iTotPags) {
                                                                %>
                                                                &nbsp;
                                                                <a href="<%=namePage%>?iPagNo=<%=i%>&cPagNo=<%=i%>">>> <script>document.write(global_paging_last);</script></a>
                                                                <%
                                                                    }
                                                                } else {
                                                                %>
                                                                &nbsp;<a style="cursor: pointer;">1</a>
                                                                <%
                                                                    }
                                                                %>
                                                            </div>
                                                        </td>
                                                    </tr>
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                </div>
                                <div class="x_panel" id="idX_Panel_Edit" style="display: none;">
                                    <div class="x_content">
                                        <div id="contentEdit"></div>
                                    </div>
                                </div>
                                <%
                                    }
                                    if (status == 1000 && statusLoad == 1) {
                                %>
                                <script type="text/javascript">
                                    $(document).ready(function () {
                                        goToByScroll("idShowResultSearch");
                                    });
                                </script>
                                <div class="x_panel" id="idShowResultSearch">
                                    <div class="x_content" style="text-align: center;">
                                        <span style="color: red; font-size: 15px;"><script>document.write(global_succ_NoResult);</script></span>
                                        <div class="clearfix"></div>
                                    </div>
                                </div>
                                <%
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
                            </div>
                        </div>
                    </div>
                    <script>
                        $(document).ready(function () {
                            if('<%= strExpandFilter%>' === "1")
                            {
                                popupViewCSRSList();
                            }
                            if(localStorage.getItem("EDIT_RENEWCERT_DETAIL") !== null && localStorage.getItem("EDIT_RENEWCERT_DETAIL") !== "null")
                            {
                                var vIDEDIT_DETAIL = localStorage.getItem("EDIT_RENEWCERT_DETAIL");
                                localStorage.setItem("EDIT_RENEWCERT_DETAIL", null);
                                popupDetailCert(vIDEDIT_DETAIL);
                            }
                        });
                    </script>
                </div>
                <%@ include file="../Modules/Footer.jsp" %>
            </div>
            <!--<script src="../style/jquery.min.js"></script>-->
            <script src="../style/bootstrap.min.js"></script>
            <!--<script src="//code.jquery.com/jquery-1.11.1.min.js"></script>-->
            <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
            <script src="../style/custom.min.js"></script>
            <link href="../js/checkphone/intlTelInput.css" rel="stylesheet" type="text/css"/>
            <script src="../js/checkphone/intlTelInput.js" type="text/javascript"></script>
            <script src="../js/moment.min.js"></script>
            <script src="../js/daterangepicker.js"></script>
        </div>
        <!-- Modal Registration Print -->
        <div id="myModalRegisterPrint" class="modal fade" role="dialog">
            <div style="width: 100%; text-align: center; position: fixed;z-index: 1000;top: 0; padding-top: 90px;
                 left: 0; height: 100%;" class="loading-gifRegisterPrint">
                <img src="../Images/ajax-loader1.gif" alt="Please wait..." />
            </div>
            <div class="modal-dialog modal-800" id="myDialogRegisterPrint">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <div id="contentRegisterPrint"></div>
                    </div>
                </div>
            </div>
        </div>
        <div id="myModalConfirmPrint" class="modal fade" role="dialog">
            <div style="width: 100%; text-align: center; position: fixed;z-index: 1000;top: 0; padding-top: 90px;
                 left: 0; height: 100%;" class="loading-gifConfirmPrint">
                <img src="../Images/ajax-loader1.gif" alt="Please wait..." />
            </div>
            <div class="modal-dialog modal-800" id="myDialogRegisterPrint">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <div id="contentConfirmPrint"></div>
                    </div>
                </div>
            </div>
        </div>
        <script>
            function popupPrintHandoverList(id, optionCA)
            {
                $('#myModalRegisterPrint').modal('show');
                $('#contentRegisterPrint').empty();
                if(optionCA === "2")
                {
                    $('#contentRegisterPrint').load('PrintHandover2.jsp', {id:id}, function () {
                    });
                } else {
                    $('#contentRegisterPrint').load('PrintHandover.jsp', {id:id}, function () {
                    });
                }
                $(".loading-gifRegisterPrint").hide();
                $(".loading-gif").hide();
                $('#over').remove();
            }
            function ShowDialogConfirm(id, isEnterprise, isCA, idCSRF)
            {
                if(isCA === JS_IS_WHICH_ABOUT_CA_ICA) {
                    popupPrintConfirm(id, genCSRFPrint());
                } else {
                    $('#myModalConfirmPrint').modal('show');
                    $('#contentConfirmPrint').empty();
                    localStorage.setItem("sessCertToPrint", "1");
                    localStorage.setItem("PrintConfirmPersonal", null);
                    localStorage.setItem("PrintConfirmBusiness", id);
                    $('#contentConfirmPrint').load('PrintConfirmBusiness.jsp', {id:id}, function () {
                    });
                    $(".loading-gifConfirmPrint").hide();
                    $(".loading-gif").hide();
                    $('#over').remove();
                }
            }
            
            function popupPrintConfirm(id, idCSRF)
            {
                $('body').append('<div id="over"></div>');
                $(".loading-gif").show();
                var sDayPrint = global_fm_report_print_only_date;
                $.ajax({
                    type: "post",
                    url: "../PrintFormCommon",
                    data: {
                        idParam: 'printconfirminforedrect',
                        id: id,
                        thoigiandiadiem: sDayPrint,
                        CsrfToken: idCSRF
                    },
                    cache: false,
                    success: function (html)
                    {
                        var myStrings = sSpace(html).split('###');
                        if (myStrings[0] === "0")
                        {
                            $(".loading-gif").hide();
                            $('#over').remove();
                            PrintPreview(myStrings[1]);
                        }
                        else if (myStrings[0] === JS_EX_CSRF)
                        {
                            funCsrfAlert();
                        } else if (myStrings[0] === JS_EX_LOGIN)
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
            
            function popupPrintRegister(id, idCSRF)
            {
                $('body').append('<div id="over"></div>');
                $(".loading-gif").show();
                $.ajax({
                    type: "post",
                    url: "../PrintFormCommon",
                    data: {
                        idParam: 'printregisterredrect',
                        id: id,
                        CsrfToken: idCSRF
                    },
                    cache: false,
                    success: function (html)
                    {
                        var myStrings = sSpace(html).split('###');
                        if (myStrings[0] === "0")
                        {
                            $(".loading-gif").hide();
                            $('#over').remove();
                            PrintPreview(myStrings[1]);
                        }
                        else if (myStrings[0] === JS_EX_CSRF)
                        {
                            funCsrfAlert();
//                            funErrorAlert(CSRF_Mess);
                        } else if (myStrings[0] === JS_EX_LOGIN)
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
            
            function ShowDialogList(id, isEnterprise, idRegis, printRegCAOption, isCA, idCSRF)
            {
                if(isCA === JS_IS_WHICH_ABOUT_CA_ICA || isCA === JS_IS_WHICH_ABOUT_CA_MATBAO) {
                    popupPrintRegister(id, genCSRFPrint());
                } else {
                    $('#myModalRegisterPrint').modal('show');
                    $('#contentRegisterPrint').empty();
                    localStorage.setItem("sessCertToPrint", "1");
                    if(idRegis === "1")
                    {
                        if(isEnterprise === "1")
                        {
                            localStorage.setItem("PrintRegisterPersonal", null);
                            localStorage.setItem("PrintRegisterBusiness", id);
                            if(printRegCAOption === '1')
                            {
                                $('#contentRegisterPrint').load('PrintRegisterBusiness.jsp', {id:id}, function () {
                                });
                            } else if(printRegCAOption === '2')
                            {
                                $('#contentRegisterPrint').load('PrintRegisterBusiness2.jsp', {id:id}, function () {
                                });
                            }
                        } else {
                            localStorage.setItem("PrintRegisterPersonal", id);
                            localStorage.setItem("PrintRegisterBusiness", null);
                            if(printRegCAOption === '1')
                            {
                                $('#contentRegisterPrint').load('PrintRegisterPersonal.jsp', {id:id}, function () {
                                });
                            } else if(printRegCAOption === '2')
                            {
                                $('#contentRegisterPrint').load('PrintRegisterPersonal2.jsp', {id:id}, function () {
                                });
                            }
                        }
                    } else if(idRegis === "2") {
                        localStorage.setItem("PrintRegisterPersonal", null);
                        localStorage.setItem("PrintRegisterBusiness", id);
                        $('#contentRegisterPrint').load('PrintChangeInfoCert.jsp', {id:id}, function () {
                        });
                    } else if(idRegis === "3") {
                        localStorage.setItem("PrintRegisterPersonal", null);
                        localStorage.setItem("PrintRegisterBusiness", id);
                        $('#contentRegisterPrint').load('PrintReissueCert.jsp', {id:id}, function () {
                        });
                    } else {
                        if(isEnterprise === "1")
                        {
                            localStorage.setItem("PrintRenewPersonal", null);
                            localStorage.setItem("PrintRenewBusiness", id);
                            if(printRegCAOption === '1')
                            {
                                $('#contentRegisterPrint').load('PrintRenewBusiness.jsp', {id:id}, function () {
                                });
                            } else if(printRegCAOption === '2') {
                                $('#contentRegisterPrint').load('PrintRegisterBusiness2.jsp', {id:id}, function () {
                                });
                            }
                        }
                        else {
                            localStorage.setItem("PrintRenewPersonal", id);
                            localStorage.setItem("PrintRenewBusiness", null);
                            if(printRegCAOption === '1')
                            {
                                $('#contentRegisterPrint').load('PrintRenewPersonal.jsp', {id:id}, function () {
                                });
                            } else if(printRegCAOption === '2') {
                                $('#contentRegisterPrint').load('PrintRegisterPersonal2.jsp', {id:id}, function () {
                                });
                            }
                        }
                    }
                    $(".loading-gifRegisterPrint").hide();
                    $(".loading-gif").hide();
                    $('#over').remove();
                }
            }
            function genCSRFPrint()
            {
                var vDNResult = "";
                $.ajax({
                    type: "post",
                    url: "../SomeCommon",
                    data: {
                        idParam: 'refreshCSRF'
                    },
                    cache: false,
                    async: false,
                    success: function (html)
                    {
                        var myStrings = sSpace(html).split('#');
                        if (myStrings[0] === "0")
                        {
                            vDNResult = myStrings[1];
                        }
                        else
                        {
                            vDNResult = "";
                        }
                    }
                });
                return vDNResult;
            }
            function CloseDialog()
            {
                $('#myModalRegisterPrint').modal('hide');
                $(".loading-gifRegisterPrint").hide();
                $(".loading-gif").hide();
                $('#over').remove();
            }
        </script>
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