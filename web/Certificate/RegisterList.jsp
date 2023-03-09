<%-- 
    Document   : RegisterList
    Created on : Sep 28, 2021, 9:02:52 AM
    Author     : vanth
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
        <script src="../js/Language.js"></script>
        <script src="../js/process_javajs.js"></script>
        <link href="../style/customportal.min.css" rel="stylesheet">
        <script type="text/javascript" src="../js/jquery.js"></script>
        <link rel="stylesheet" href="../js/sweetalert.css"/>
        <script src="../js/sweetalert-dev.js"></script>
        <link href="../style/customportal.min.css" rel="stylesheet">
        <script type="text/javascript" src="../Css/GlobalAlert.js"></script>
        <title></title>
        <script language="javascript">
            changeFavicon("../");
            document.title = regiscert_title_list;
            $(document).ready(function () {
                localStorage.setItem("isLoadSearchFrist", null);
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
            });
            
            function popupDetailCert(id)
            {
                $('body').append('<div id="over"></div>');
                $(".loading-gif").show();
                $("#contentEdit").empty();
                $('#contentEdit').load('CertInitView.jsp', {id:id}, function () {
                    $(".loading-gif").hide();
                    $('#over').remove();
                    $('#idX_Panel_Edit').css("display", "");
                    goToByScroll("contentEdit");
                });
            }
            function popupListCertDecline(attrId, branchId, userId)
            {
                $('#myModalListCertDecline').modal('show');
                $('#contentCertDecline').empty();
                $('#contentCertDecline').load('IncludeCertDecline.jsp', {attrId: attrId, branchId: branchId, userId: userId}, function () {
                });
                $(".loading-gifCertDecline").hide();
                $(".loading-gif").hide();
                $('#over').remove();
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
            function popupPrintRegis(id, isEnterprise, idRegis)
            {
                ShowDialogList(id, isEnterprise, idRegis);
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
            @media (min-width: 768px){.modal-dialog{width: 900px;}}
            .modal-header{
                padding: 10px 10px 10px 10px;border-bottom:0px;
            }
            .x_panel{padding: 10px 10px}
            .col-sm-4{padding-right: 5px;}
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
                String sessTreeArrayBranchID = session.getAttribute("sessTreeArrayBranchIDSystem").toString().trim();
                String SessRoleID = session.getAttribute("RoleID_ID").toString().trim();
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
                        document.getElementById("idNameURL").innerHTML = regiscert_title_list;
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
                                    if (session.getAttribute("RefreshInitCertSess") != null && session.getAttribute("sessFromCreateDateInitCert") != null
                                            && session.getAttribute("sessToCreateDateInitCert") != null) {
                                        try {
                                            session.setAttribute("RefreshInitCertSessPaging", "1");
                                            session.setAttribute("SearchSharePagingInitCert", "0");
                                            statusLoad = 1;
                                            String ToCreateDate = (String) session.getAttribute("sessToCreateDateInitCert");
                                            String FromCreateDate = (String) session.getAttribute("sessFromCreateDateInitCert");
                                            String CERT_SN = "";
                                            String FORM_FACTOR = "";
                                            String TOKEN_SN = "";
                                            String BranchOffice = "";
                                            String USER = "";

                                            String PERSONAL_NAME = (String) session.getAttribute("sessPERSONAL_NAMEInitCert");
                                            String COMPANY_NAME = (String) session.getAttribute("sessCOMPANY_NAMEInitCert");
                                            String DOMAIN_NAME = "";
                                            String TAX_CODE = (String) session.getAttribute("sessTAX_CODEInitCert");
                                            String BUDGET_CODE = (String) session.getAttribute("sessBUDGET_CODEInitCert");
                                            String DECISION = (String) session.getAttribute("sessDECISIONInitCert");
                                            String P_ID = (String) session.getAttribute("sessP_IDInitCert");
                                            String CCCD = (String) session.getAttribute("sessCCCDInitCert");
                                            String PASSPORT = (String) session.getAttribute("sessPASSPORTInitCert");
                                            String CERTIFICATION_ATTR_TYPE = "1";
                                            String CERTIFICATION_PURPOSE = "";
                                            String DEVICE_UUID_SEARCH = "";
                                            String IsTokenLost = "";
                                            String IsByOwner = "";
                                            String CERTIFICATION_AUTHORITY_SEARCH = "";
                                            String SERVICE_TYPE_SEARCH = "";

                                            strAlertAllTimes = (String) session.getAttribute("AlertAllTimeSInitCert");
                                            session.setAttribute("RefreshInitCertSess", null);
                                            session.setAttribute("sessFromCreateDateInitCert", FromCreateDate);
                                            session.setAttribute("sessToCreateDateInitCert", ToCreateDate);
                                            session.setAttribute("sessPERSONAL_NAMEInitCert", PERSONAL_NAME);
                                            session.setAttribute("sessCOMPANY_NAMEInitCert", COMPANY_NAME);
                                            session.setAttribute("sessTAX_CODEInitCert", TAX_CODE);
                                            session.setAttribute("sessBUDGET_CODEInitCert", BUDGET_CODE);
                                            session.setAttribute("sessDECISIONInitCert", DECISION);
                                            session.setAttribute("sessP_IDInitCert", P_ID);
                                            session.setAttribute("sessCCCDInitCert", CCCD);
                                            session.setAttribute("sessPASSPORTInitCert", PASSPORT);
                                            session.setAttribute("AlertAllTimeSInitCert", strAlertAllTimes);
                                            if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA)) {
                                                String sENTERPRISE_ID = (String) session.getAttribute("sessENTERPRISE_IDInitCert");
                                                String sPERSONAL_ID = (String) session.getAttribute("sessPERSONAL_IDInitCert");
                                                String sENTERPRISE_PREFIX = (String) session.getAttribute("sessENTERPRISE_PREFIXInitCert");
                                                String sPERSONAL_PREFIX = (String) session.getAttribute("sessPERSONAL_PREFIXInitCert");
                                                session.setAttribute("sessENTERPRISE_IDInitCert", sENTERPRISE_ID);
                                                session.setAttribute("sessPERSONAL_IDInitCert", sPERSONAL_ID);
                                                session.setAttribute("sessENTERPRISE_PREFIXInitCert", sENTERPRISE_PREFIX);
                                                session.setAttribute("sessPERSONAL_PREFIXInitCert", sPERSONAL_PREFIX);
                                                isLoadEnterprisePrefix = sENTERPRISE_PREFIX;
                                                isLoadPeronalPrefix = sPERSONAL_PREFIX;
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
                                            if(SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                                if(!"".equals(BranchOffice) && !"".equals(USER))
                                                {
                                                    SessUserAgentID = BranchOffice;
                                                }
                                            }
                                            //else {BranchOffice = SessUserAgentID;}
                                            if("".equals(USER)) {
                                                if(!SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                                    if(SessRoleID.equals(Definitions.CONFIG_ROLE_ID_AGENT_USER) || SessRoleID.equals(Definitions.CONFIG_ROLE_ID_AGENT_ACCOUNTANT)) {
                                                        USER = SessUserID;
                                                    }
                                                }
                                            }
                                            String[] sUIDResult = new String[2];
                                            CommonReferServlet.collectFieldToUID(EscapeUtils.escapeHtmlSearch(TAX_CODE), EscapeUtils.escapeHtmlSearch(BUDGET_CODE),
                                                EscapeUtils.escapeHtmlSearch(DECISION), EscapeUtils.escapeHtmlSearch(P_ID), EscapeUtils.escapeHtmlSearch(PASSPORT),
                                                EscapeUtils.escapeHtmlSearch(CCCD), sUIDResult);
                                            String sEnterpriseCert = sUIDResult[0];
                                            String sPersonalCert = sUIDResult[1];
                                            int ss = 0;
                                            if ((session.getAttribute("CountListInitCert")) == null) {
                                                ss = db.S_BO_CERTIFICATION_TOTAL(EscapeUtils.escapeHtmlSearch(FromCreateDate),
                                                    EscapeUtils.escapeHtmlSearch(ToCreateDate), EscapeUtils.escapeHtmlSearch(CERTIFICATION_ATTR_TYPE), EscapeUtils.escapeHtmlSearch(CERT_SN),
                                                    EscapeUtils.escapeHtmlSearch(CERTIFICATION_PURPOSE), EscapeUtils.escapeHtmlSearch(PERSONAL_NAME), EscapeUtils.escapeHtmlSearch(COMPANY_NAME),
                                                    EscapeUtils.escapeHtmlSearch(DOMAIN_NAME), EscapeUtils.escapeHtmlSearch(BranchOffice),
                                                    USER, SessRoleID_ID, SessUserAgentID, IsTokenLost, EscapeUtils.escapeHtmlSearch(TOKEN_SN),
                                                    EscapeUtils.escapeHtmlSearch(FORM_FACTOR), EscapeUtils.escapeHtmlSearch(DEVICE_UUID_SEARCH), IsByOwner,
                                                    EscapeUtils.escapeHtmlSearch(CERTIFICATION_AUTHORITY_SEARCH), EscapeUtils.escapeHtmlSearch(SERVICE_TYPE_SEARCH),
                                                    sessTreeArrayBranchID, sEnterpriseCert, sPersonalCert);
                                                session.setAttribute("CountListInitCert", String.valueOf(ss));
                                            } else {
                                                String sCount = (String) session.getAttribute("CountListInitCert");
                                                ss = Integer.parseInt(sCount);
                                                session.setAttribute("CountListInitCert", String.valueOf(ss));
                                            }
                                            if (session.getAttribute("SearchIPageNoPagingInitCert") != null) {
                                                String sPage = (String) session.getAttribute("SearchIPageNoPagingInitCert");
                                                iPagNo = Integer.parseInt(sPage);
                                            }
                                            if (session.getAttribute("SearchISwRwsPagingInitCert") != null) {
                                                String sSumPage = (String) session.getAttribute("SearchISwRwsPagingInitCert");
                                                iSwRws = Integer.parseInt(sSumPage);
                                            }
                                            if (session.getAttribute("RefreshInitCertSessNumberPaging") != null) {
                                                String sNoPage = (String) session.getAttribute("RefreshInitCertSessNumberPaging");
                                                iPaNoSS = Integer.parseInt(sNoPage);
                                            }
                                            session.setAttribute("SearchIPageNoPagingInitCert", String.valueOf(iPagNo));
                                            session.setAttribute("SearchISwRwsPagingInitCert", String.valueOf(iSwRws));
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
                                        } catch(Exception e){CommonFunction.LogExceptionServlet(null, "RegisterList reload: " + e.getMessage(), e);}
                                    } else {// if (request.getMethod().equals("POST") || "1".equals(hasPaging))
                                        session.setAttribute("RefreshInitCertSessPaging", null);
                                        String FromCreateDate = request.getParameter("FromCreateDate");
                                        String ToCreateDate = request.getParameter("ToCreateDate");
                                        String CERT_SN = "";
                                        String FORM_FACTOR = "";
                                        String TOKEN_SN = "";
                                        String PERSONAL_NAME = EscapeUtils.ConvertStringToUnicode(request.getParameter("PERSONAL_NAME"));
                                        String COMPANY_NAME = EscapeUtils.ConvertStringToUnicode(request.getParameter("COMPANY_NAME"));
                                        String DOMAIN_NAME = "";
                                        String TAX_CODE = "";
                                        String BUDGET_CODE ="";
                                        String P_ID = "";
                                        String CCCD = "";
                                        String PASSPORT = "";
                                        String DECISION = "";
                                        String CERTIFICATION_STATE_ID = "1";
                                        String CERTIFICATION_PURPOSE = "";
                                        String DEVICE_UUID_SEARCH = "";
                                        String IsTokenLost = "";
                                        String IsByOwner = "";
                                        String BranchOffice = "";
                                        String USER = "";
                                        String CERTIFICATION_AUTHORITY_SEARCH = "";
                                        String SERVICE_TYPE_SEARCH = "";
                                        try{
                                            if (request.getMethod().equals("POST")) {
                                                System.out.println("POST: POST");
                                                session.setAttribute("SearchShareStoreInitCert", null);
                                                session.setAttribute("SearchIPageNoPagingInitCert", null);
                                                session.setAttribute("SearchISwRwsPagingInitCert", null);
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
                                            if(!isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA)) {
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
                                                    sENTERPRISE_PREFIX = (String) session.getAttribute("sessENTERPRISE_PREFIXInitCert");
                                                    sPERSONAL_PREFIX = (String) session.getAttribute("sessPERSONAL_PREFIXInitCert");
                                                    sENTERPRISE_ID = (String) session.getAttribute("sessENTERPRISE_IDInitCert");
                                                    sPERSONAL_ID = (String) session.getAttribute("sessPERSONAL_IDInitCert");
                                                }
                                                session.setAttribute("sessENTERPRISE_PREFIXInitCert", sENTERPRISE_PREFIX);
                                                session.setAttribute("sessPERSONAL_PREFIXInitCert", sPERSONAL_PREFIX);
                                                session.setAttribute("sessENTERPRISE_IDInitCert", sENTERPRISE_ID);
                                                session.setAttribute("sessPERSONAL_IDInitCert", sPERSONAL_ID);
                                                if(sENTERPRISE_PREFIX.equals("MST")){
                                                    TAX_CODE = sENTERPRISE_ID;
                                                }
                                                if(sENTERPRISE_PREFIX.equals("MNS")){
                                                    BUDGET_CODE = sENTERPRISE_ID;
                                                }
                                                if(sENTERPRISE_PREFIX.equals("QĐ")){
                                                    DECISION = sENTERPRISE_ID;
                                                }
                                                if(sPERSONAL_PREFIX.equals("CMND")){
                                                    P_ID = sPERSONAL_ID;
                                                }
                                                if(sPERSONAL_PREFIX.equals("CCCD")){
                                                    CCCD = sPERSONAL_ID;
                                                }
                                                if(sPERSONAL_PREFIX.equals("HC")){
                                                    PASSPORT = sPERSONAL_ID;
                                                }
                                                isLoadEnterprisePrefix = sENTERPRISE_PREFIX;
                                                isLoadPeronalPrefix = sPERSONAL_PREFIX;
                                            }

                                            Boolean nameCheck = Boolean.valueOf(request.getParameter("nameCheck") != null);
                                            if (nameCheck == false) {
                                                FromCreateDate = "";
                                                ToCreateDate = "";
                                                strAlertAllTimes = "1";
                                            }
                                        } else {
                                            FromCreateDate = com.ConvertMonthSub(30);
                                            ToCreateDate = com.ConvertMonthSub(0);
                                            PERSONAL_NAME = "";
                                            COMPANY_NAME = "";
                                        }
                                        statusLoad = 1;
                                        request.setCharacterEncoding("UTF-8");
                                        if ("1".equals(hasPaging)) {
                                            session.setAttribute("SearchSharePagingInitCert", "0");
                                            ToCreateDate = (String) session.getAttribute("sessToCreateDateInitCert");
                                            FromCreateDate = (String) session.getAttribute("sessFromCreateDateInitCert");
                                            PERSONAL_NAME = (String) session.getAttribute("sessPERSONAL_NAMEInitCert");
                                            COMPANY_NAME = (String) session.getAttribute("sessCOMPANY_NAMEInitCert");
                                            TAX_CODE = (String) session.getAttribute("sessTAX_CODEInitCert");
                                            BUDGET_CODE = (String) session.getAttribute("sessBUDGET_CODEInitCert");
                                            DECISION = (String) session.getAttribute("sessDECISIONInitCert");
                                            P_ID = (String) session.getAttribute("sessP_IDInitCert");
                                            CCCD = (String) session.getAttribute("sessCCCDInitCert");
                                            PASSPORT = (String) session.getAttribute("sessPASSPORTInitCert");
                                            strAlertAllTimes = (String) session.getAttribute("AlertAllTimeSInitCert");
                                            session.setAttribute("SessParamOnPagingCertList", null);
                                        } else {
                                            session.setAttribute("SearchSharePagingInitCert", "1");
                                            session.setAttribute("CountListInitCert", null);
                                        }
                                        session.setAttribute("sessFromCreateDateInitCert", FromCreateDate);
                                        session.setAttribute("sessToCreateDateInitCert", ToCreateDate);
                                        session.setAttribute("sessPERSONAL_NAMEInitCert", PERSONAL_NAME);
                                        session.setAttribute("sessCOMPANY_NAMEInitCert", COMPANY_NAME);
                                        session.setAttribute("sessTAX_CODEInitCert", TAX_CODE);
                                        session.setAttribute("sessBUDGET_CODEInitCert", BUDGET_CODE);
                                        session.setAttribute("sessDECISIONInitCert", DECISION);
                                        session.setAttribute("sessP_IDInitCert", P_ID);
                                        session.setAttribute("sessCCCDInitCert", CCCD);
                                        session.setAttribute("sessPASSPORTInitCert", PASSPORT);
                                        session.setAttribute("AlertAllTimeSInitCert", strAlertAllTimes);
    //                                    if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(CERTIFICATION_ATTR_TYPE)) {
    //                                        CERTIFICATION_ATTR_TYPE = "";
    //                                    }
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
                                        if(SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                            if(!"".equals(BranchOffice) && !"".equals(USER))
                                            {
                                                SessUserAgentID = BranchOffice;
                                            }
                                        }// else {BranchOffice = SessUserAgentID;}
                                        if("".equals(USER)) {
                                            if(!SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                                if(SessRoleID.equals(Definitions.CONFIG_ROLE_ID_AGENT_USER) || SessRoleID.equals(Definitions.CONFIG_ROLE_ID_AGENT_ACCOUNTANT)) {
                                                    USER = SessUserID;
                                                }
                                            }
                                        }
                                        String[] sUIDResult = new String[2];
                                        CommonReferServlet.collectFieldToUID(EscapeUtils.escapeHtmlSearch(TAX_CODE), EscapeUtils.escapeHtmlSearch(BUDGET_CODE),
                                            EscapeUtils.escapeHtmlSearch(DECISION), EscapeUtils.escapeHtmlSearch(P_ID), EscapeUtils.escapeHtmlSearch(PASSPORT),
                                            EscapeUtils.escapeHtmlSearch(CCCD), sUIDResult);
                                        String sEnterpriseCert = sUIDResult[0];
                                        String sPersonalCert = sUIDResult[1];
                                        int ss = 0;
                                        if ((session.getAttribute("CountListInitCert")) == null) {
                                            ss = db.S_BO_CERTIFICATION_TOTAL(EscapeUtils.escapeHtmlSearch(FromCreateDate),
                                                EscapeUtils.escapeHtmlSearch(ToCreateDate),EscapeUtils.escapeHtmlSearch(CERTIFICATION_STATE_ID),
                                                EscapeUtils.escapeHtmlSearch(CERT_SN), EscapeUtils.escapeHtmlSearch(CERTIFICATION_PURPOSE),
                                                EscapeUtils.escapeHtmlSearch(PERSONAL_NAME), EscapeUtils.escapeHtmlSearch(COMPANY_NAME),
                                                EscapeUtils.escapeHtmlSearch(DOMAIN_NAME), EscapeUtils.escapeHtmlSearch(BranchOffice),
                                                USER, SessRoleID_ID, SessUserAgentID, IsTokenLost, EscapeUtils.escapeHtmlSearch(TOKEN_SN),
                                                EscapeUtils.escapeHtmlSearch(FORM_FACTOR), EscapeUtils.escapeHtmlSearch(DEVICE_UUID_SEARCH),IsByOwner,
                                                EscapeUtils.escapeHtmlSearch(CERTIFICATION_AUTHORITY_SEARCH), EscapeUtils.escapeHtmlSearch(SERVICE_TYPE_SEARCH),
                                                sessTreeArrayBranchID, sEnterpriseCert, sPersonalCert);
                                            session.setAttribute("CountListInitCert", String.valueOf(ss));
                                        } else {
                                            String sCount = (String) session.getAttribute("CountListInitCert");
                                            ss = Integer.parseInt(sCount);
                                            session.setAttribute("CountListInitCert", String.valueOf(ss));
                                        }
                                        iTotRslts = ss;
                                        if (iTotRslts > 0) {
                                            db.S_BO_CERTIFICATION_LIST(EscapeUtils.escapeHtmlSearch(FromCreateDate), EscapeUtils.escapeHtmlSearch(ToCreateDate),
                                                EscapeUtils.escapeHtmlSearch(CERTIFICATION_STATE_ID), EscapeUtils.escapeHtmlSearch(CERT_SN),
                                                EscapeUtils.escapeHtmlSearch(CERTIFICATION_PURPOSE),
                                                EscapeUtils.escapeHtmlSearch(PERSONAL_NAME), EscapeUtils.escapeHtmlSearch(COMPANY_NAME),
                                                EscapeUtils.escapeHtmlSearch(DOMAIN_NAME), EscapeUtils.escapeHtmlSearch(BranchOffice),
                                                USER, SessRoleID_ID, SessUserAgentID, IsTokenLost, EscapeUtils.escapeHtmlSearch(TOKEN_SN),
                                                EscapeUtils.escapeHtmlSearch(FORM_FACTOR), EscapeUtils.escapeHtmlSearch(DEVICE_UUID_SEARCH),
                                                IsByOwner, EscapeUtils.escapeHtmlSearch(CERTIFICATION_AUTHORITY_SEARCH), EscapeUtils.escapeHtmlSearch(SERVICE_TYPE_SEARCH),
                                                sessLanguageGlobal, rsPgin, iPagNo, iSwRws,
                                                sessTreeArrayBranchID, sEnterpriseCert, sPersonalCert);
                                            session.setAttribute("SearchIPageNoPagingInitCert", String.valueOf(iPagNo));
                                            session.setAttribute("SearchISwRwsPagingInitCert", String.valueOf(iSwRws));
                                            strMess = com.convertMoney(ss);
                                            if (rsPgin[0].length > 0) {
                                                status = 1;
                                            } else {
                                                status = 1000;
                                            }
                                        } else {
                                            status = 1000;
                                        }
                                    } catch(Exception e){CommonFunction.LogExceptionServlet(null, "RegisterList search new: " + e.getMessage(), e);}
                                }
//                            else {
//                                    session.setAttribute("RefreshInitCertSessPaging", null);
//                                    session.setAttribute("SearchShareStoreInitCert", null);
//                                    session.setAttribute("SearchIPageNoPagingInitCert", null);
//                                    session.setAttribute("SearchISwRwsPagingInitCert", null);
//                                    session.setAttribute("sessFromCreateDateInitCert", null);
//                                    session.setAttribute("sessToCreateDateInitCert", null);
//                                    session.setAttribute("sessPERSONAL_NAMEInitCert", null);
//                                    session.setAttribute("sessCOMPANY_NAMEInitCert", null);
//                                    session.setAttribute("sessTAX_CODEInitCert", null);
//                                    session.setAttribute("sessBUDGET_CODEInitCert", null);
//                                    session.setAttribute("sessDECISIONInitCert", null);
//                                    session.setAttribute("sessP_IDInitCert", null);
//                                    session.setAttribute("sessCCCDInitCert", null);
//                                    session.setAttribute("sessPASSPORTInitCert", null);
//                                    session.setAttribute("AlertAllTimeSInitCert", null);
//                                    session.setAttribute("sessENTERPRISE_PREFIXInitCert", null);
//                                    session.setAttribute("sessPERSONAL_PREFIXInitCert", null);
//                                    session.setAttribute("sessENTERPRISE_IDInitCert", null);
//                                    session.setAttribute("sessPERSONAL_IDInitCert", null);
//                                }
                            %>
                            <div class="col-md-12 col-sm-12 col-xs-12">
                                <div class="x_panel">
                                    <div class="x_content" style="margin-top: 0px;">
                                        <form name="form" method="post" class="form-horizontal">
                                            <input id="idHiddenLoad" name="nameHiddenLoad" value="" type="hidden"/>
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
                                                            <input type="Text" id="demo1" name="FromCreateDate" <%= session.getAttribute("AlertAllTimeSInitCert") != null && "1".equals(session.getAttribute("AlertAllTimeSInitCert").toString()) ? "disabled" : ""%>
                                                                value="<%= session.getAttribute("sessFromCreateDateInitCert") != null && !"1".equals(session.getAttribute("AlertAllTimeSInitCert").toString()) ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessFromCreateDateInitCert").toString()) : com.ConvertMonthSub(30)%>"
                                                                maxlength="25" class="form-control123"/>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;padding-right: 5px;"><script>document.write(global_fm_ToDate);</script></label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <input type="Text" id="demo2" name="ToCreateDate" <%= session.getAttribute("AlertAllTimeSInitCert") != null && "1".equals(session.getAttribute("AlertAllTimeSInitCert").toString()) ? "disabled" : ""%>
                                                                value="<%= session.getAttribute("sessToCreateDateInitCert") != null && !"1".equals(session.getAttribute("AlertAllTimeSInitCert").toString()) ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessToCreateDateInitCert").toString()) : com.ConvertMonthSub(0)%>"
                                                                maxlength="25" class="form-control123"/>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;padding-right: 5px;"><script>document.write(global_fm_check_date);</script></label>
                                                        <div class="col-sm-7" style="padding-right: 0px;text-align: left;">
                                                            <label class="switch" for="idCheck">
                                                                <input type="checkbox" name="nameCheck" id="idCheck" onchange="checkboxChange();" <%= session.getAttribute("AlertAllTimeSInitCert") != null && "1".equals(session.getAttribute("AlertAllTimeSInitCert").toString()) ? "" : "checked" %>/>
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
                                                        <input type="text" name="COMPANY_NAME" maxlength="150" value="<%= session.getAttribute("sessCOMPANY_NAMEInitCert") != null ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessCOMPANY_NAMEInitCert").toString()) : ""%>"
                                                            class="form-control123">
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-sm-4" style="padding-left: 0;">
                                                <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;padding-right: 5px;">
                                                    <script>document.write(global_fm_identifier_type);</script>
                                                </label>
                                                <div class="col-sm-7" style="padding-right: 0px;">
                                                    <select name="ENTERPRISE_PREFIX" onclick="changeEnterprise(this.value);" id="ENTERPRISE_PREFIX" class="form-control123" style="text-align: left;">
                                                        <option value="MST" <%= session.getAttribute("sessENTERPRISE_PREFIXInitCert") != null && "MST".equals(session.getAttribute("sessENTERPRISE_PREFIXInitCert").toString()) ? "selected" : ""%>><script>document.write(global_fm_MST);</script></option>
                                                        <option value="MNS" <%= session.getAttribute("sessENTERPRISE_PREFIXInitCert") != null && "MNS".equals(session.getAttribute("sessENTERPRISE_PREFIXInitCert").toString()) ? "selected" : ""%>><script>document.write(global_fm_MNS);</script></option>
                                                        <option value="QĐ" <%= session.getAttribute("sessENTERPRISE_PREFIXInitCert") != null && "QĐ".equals(session.getAttribute("sessENTERPRISE_PREFIXInitCert").toString()) ? "selected" : ""%>><script>document.write(global_fm_decision);</script></option>
                                                    </select>
                                                </div>
                                            </div>
                                            <div class="col-sm-4" style="padding-left: 0;">
                                                <div class="form-group">
                                                    <label class="control-label col-sm-5" id="idLblTooltipEnterprise" style="color: #000000; font-weight: bold;text-align: right;padding-right: 5px;">
                                                    </label>
                                                    <div class="col-sm-7" style="padding-right: 0px;">
                                                        <input type="text" name="ENTERPRISE_ID" maxlength="22" value="<%= session.getAttribute("sessENTERPRISE_IDInitCert") != null ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessENTERPRISE_IDInitCert").toString()) : ""%>"
                                                            class="form-control123">
                                                    </div>
                                                </div>
                                                <script>
                                                    $(document).ready(function () {
                                                        $("#idLblTooltipEnterprise").text(global_fm_enter + global_fm_MST);
                                                        var sessEnter = '<%=isLoadEnterprisePrefix%>';
                                                        if(sessEnter !== '') {
                                                            if(sessEnter === 'MNS'){
                                                                $("#idLblTooltipEnterprise").text(global_fm_enter + global_fm_MNS);
                                                            } else if(sessEnter === 'QĐ') {
                                                                $("#idLblTooltipEnterprise").text(global_fm_enter + global_fm_decision);
                                                            } else {
                                                                $("#idLblTooltipEnterprise").text(global_fm_enter + global_fm_MST);
                                                            }
                                                        }
                                                    });
                                                    function changeEnterprise(obj)
                                                    {
                                                        if(obj === 'MNS'){
                                                            $("#idLblTooltipEnterprise").text(global_fm_enter + global_fm_MNS);
                                                        } else if(obj === 'QĐ') {
                                                            $("#idLblTooltipEnterprise").text(global_fm_enter + global_fm_decision);
                                                        } else {
                                                            $("#idLblTooltipEnterprise").text(global_fm_enter + global_fm_MST);
                                                        }
                                                    }
                                                </script>
                                            </div>
                                            <div class="col-sm-4" style="padding-left: 0;">
                                                <div class="form-group">
                                                    <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;padding-right: 5px;">
                                                        <script>document.write(global_fm_grid_personal);</script>
                                                    </label>
                                                    <div class="col-sm-7" style="padding-right: 0px;">
                                                        <input type="text" name="PERSONAL_NAME" maxlength="150" value="<%= session.getAttribute("sessPERSONAL_NAMEInitCert") != null ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessPERSONAL_NAMEInitCert").toString()) : ""%>"
                                                            class="form-control123">
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-sm-4" style="padding-left: 0;">
                                                <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;padding-right: 5px;">
                                                    <script>document.write(global_fm_document_type);</script>
                                                </label>
                                                <div class="col-sm-7" style="padding-right: 0px;">
                                                    <select name="PERSONAL_PREFIX" onclick="changePersonal(this.value);" id="PERSONAL_PREFIX" class="form-control123" style="text-align: left;">
                                                        <option value="CCCD" <%= session.getAttribute("sessPERSONAL_PREFIXInitCert") != null && "CCCD".equals(session.getAttribute("sessPERSONAL_PREFIXInitCert").toString()) ? "selected" : ""%>><script>document.write(global_fm_CitizenId);</script></option>
                                                        <option value="CMND" <%= session.getAttribute("sessPERSONAL_PREFIXInitCert") != null && "CMND".equals(session.getAttribute("sessPERSONAL_PREFIXInitCert").toString()) ? "selected" : ""%>><script>document.write(global_fm_CMND);</script></option>
                                                        <option value="HC" <%= session.getAttribute("sessPERSONAL_PREFIXInitCert") != null && "HC".equals(session.getAttribute("sessPERSONAL_PREFIXInitCert").toString()) ? "selected" : ""%>><script>document.write(global_fm_HC);</script></option>
                                                    </select>
                                                </div>
                                            </div>
                                            <div class="col-sm-4" style="padding-left: 0;">
                                                <div class="form-group">
                                                    <label class="control-label col-sm-5" id="idLblTooltipPersonal" style="color: #000000; font-weight: bold;text-align: right;padding-right: 5px;">
                                                        <script>document.write(global_fm_CMND);</script>
                                                    </label>
                                                    <div class="col-sm-7" style="padding-right: 0px;">
                                                        <input type="text" name="PERSONAL_ID" maxlength="22" value="<%= session.getAttribute("sessPERSONAL_IDInitCert") != null ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessPERSONAL_IDInitCert").toString()) : ""%>"
                                                            class="form-control123">
                                                    </div>
                                                </div>
                                                <script>
                                                    $(document).ready(function () {
                                                        $("#idLblTooltipPersonal").text(global_fm_enter_number + global_fm_CitizenId_I);
                                                        var sessEnter = '<%=isLoadPeronalPrefix%>';
                                                        if(sessEnter !== '') {
                                                            if(sessEnter === 'HC'){
                                                                $("#idLblTooltipPersonal").text(global_fm_enter + global_fm_HC);
                                                            } else if(sessEnter === 'CMND') {
                                                                $("#idLblTooltipPersonal").text(global_fm_enter_number + global_fm_CMND);
                                                            } else {
                                                                $("#idLblTooltipPersonal").text(global_fm_enter_number + global_fm_CitizenId_I);
                                                            }
                                                        }
                                                    });
                                                    function changePersonal(obj)
                                                    {
                                                        if(obj === 'HC') {
                                                            $("#idLblTooltipPersonal").text(global_fm_enter + global_fm_HC);
                                                        } else if(obj === 'CMND') {
                                                            $("#idLblTooltipPersonal").text(global_fm_enter_number + global_fm_CMND);
                                                        } else {
                                                            $("#idLblTooltipPersonal").text(global_fm_enter_number + global_fm_CitizenId_I);
                                                        }
                                                    }
                                                </script>
                                            </div>
                                            <div class="col-sm-4" style="padding-left: 0;"></div>
                                            <div class="col-sm-4" style="padding-left: 0;">
                                                <div class="form-group">
                                                    <div class="col-sm-5" style="font-weight: bold; text-align: right; padding-left: 2px;font-size: 12px;"></div>
                                                    <div class="col-sm-7" style="padding-right: 0px;text-align: left;">
                                                        <button type="button" class="btn btn-info" onClick="searchForm('<%=anticsrf%>');"><script>document.write(global_fm_button_search);</script></button>
<!--                                                        &nbsp;
                                                        <button type="button" class="btn btn-info" onClick="PupopRegister();"><script>document.write(global_fm_button_add_action);</script></button>
                                                        <script>
                                                            function PupopRegister() {
                                                                window.location = "RegisterCertificate.jsp";
                                                            }
                                                        </script>-->
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-sm-4" style="padding-left: 0;"></div>
                                            <input type="hidden" name="CsrfToken" value="<%=anticsrf%>"/>
                                            <input type="hidden" id="tempCsrfToken" name="tempCsrfToken"/>
                                            <input id="idHiddenLoad" name="nameHiddenLoad" value="" type="hidden"/>
                                        </form>
                                    </div>
                                </div>
                                <%
                                    if (status == 1 && statusLoad == 1) {
                                %>
<!--                                <script type="text/javascript">
                                    $(document).ready(function () {
                                        goToByScroll("idShowResultSearch");
                                    });
                                </script>-->
                                <div class="x_panel" id="idShowResultSearch">
                                    <div class="x_title" style="border-bottom: 0 solid #E6E9ED;margin-bottom: 0px;">
                                        <h2><i class="fa fa-list-ul"></i> <script>document.write(certlist_title_table);</script></h2>
                                        <ul class="nav navbar-right panel_toolbox">
                                            <li style="color: red;font-weight: bold;">
                                                <script>document.write(global_label_grid_sum);</script><%= strMess%>&nbsp;
                                                &nbsp;
                                                <button type="button" class="btn btn-info" onClick="PupopRegister();"><script>document.write(global_fm_button_add_action);</script></button>
                                                <script>
                                                    function PupopRegister() {
                                                        window.location = "RegisterCertificate.jsp";
                                                    }
                                                </script>
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
//                                                        var cccd = IsWhichCA === '18' ? ' ' + global_fm_CitizenId_I + ' ': ' ' + global_fm_CitizenId + ' ';
//                                                        document.write(global_fm_CMND + '/' + cccd + '/' + global_fm_HC);
                                                        document.write(global_fm_personal_id);
                                                    </script>
                                                </th>
                                                <th><script>document.write(global_fm_Method);</script></th>
                                                <th><script>document.write(global_fm_duration_cts);</script></th>
                                                <th><script>document.write(global_fm_certtype);</script></th>
                                                <th style="width: 80px;"><script>document.write(IsWhichCA === '18' ? global_fm_Status : global_fm_Status_cert);</script></th>
                                                <th><script>document.write(global_fm_activation_code);</script></th>
                                                <%
                                                    if(SessRoleID_ID.equals(Definitions.CONFIG_ROLE_ID_CA_USER) || SessRoleID_ID.equals(Definitions.CONFIG_ROLE_ID_AGENT_USER)) {
                                                %>
                                                <th><script>document.write(global_fm_Status_request);</script></th>
                                                <th><script>document.write(cert_fm_type_request);</script></th>
                                                <%
                                                    }
                                                %>
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
                                                <th><script>document.write(global_fm_serial);</script></th>
                                                <th><script>document.write(token_fm_tokenid);</script></th>
                                                </thead>
                                                <tbody>
                                                    <%
                                                        if (iPaNoSS > 1) {
                                                            j = ((iPaNoSS - 1) * iSwRws) + 1;
                                                        }
                                                        session.setAttribute("RefreshInitCertSessNumberPaging", String.valueOf(iPaNoSS));
                                                        if (rsPgin[0].length > 0) {
                                                            for (CERTIFICATION temp1 : rsPgin[0]) {
                                                                String strID = String.valueOf(temp1.ID);
//                                                                String sSMTOrMSN = EscapeUtils.CheckTextNull(temp1.TAX_CODE);
//                                                                if("".equals(sSMTOrMSN)) {
//                                                                    sSMTOrMSN = EscapeUtils.CheckTextNull(temp1.BUDGET_CODE);
//                                                                }
//                                                                if("".equals(sSMTOrMSN)) {
//                                                                    sSMTOrMSN = EscapeUtils.CheckTextNull(temp1.DECISION);
//                                                                }
//                                                                String sCMNDOrHC = EscapeUtils.CheckTextNull(temp1.P_ID);
//                                                                if("".equals(sCMNDOrHC)) {
//                                                                    sCMNDOrHC = EscapeUtils.CheckTextNull(temp1.P_EID);
//                                                                }
//                                                                if("".equals(sCMNDOrHC)) {
//                                                                    sCMNDOrHC = EscapeUtils.CheckTextNull(temp1.PASSPORT);
//                                                                }
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
//                                                                String sAC = temp1.ACTIVATION_CODE;
//                                                                if(!SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
//                                                                    if(temp1.CERTIFICATION_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_PENDING
//                                                                        || temp1.CERTIFICATION_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_PRE_APPROVED
//                                                                        || temp1.CERTIFICATION_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_INIT)
//                                                                    {
//                                                                        sAC = "";
//                                                                    }
//                                                                }
                                                                String sAC = temp1.ACTIVATION_CODE;
                                                                if(temp1.CERTIFICATION_ATTR_STATE_ID != Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_APPROVED) {
                                                                    sAC = "";
                                                                }
                                                    %>
                                                    <tr>
                                                        <td>
                                                            <div style="width: 105px;">
                                                                <span>
                                                                    <a style="cursor: pointer;" onclick="popupDetailCert('<%=strID%>');" class="btn btn-info btn-xs"><i class="fa fa-eye"></i> <script>document.write(global_fm_button_detail.toUpperCase());</script></a>
                                                                </span>
                                                                
                                                                <%
                                                                    String SessLevelBranch = session.getAttribute("sessLevelBranch").toString().trim();
                                                                    ROLE_DATA[][] sessFunctionCert = (ROLE_DATA[][]) session.getAttribute("SessRoleSet_Cert");
                                                                    String sCERTIFICATION_ATTR_ID = String.valueOf(temp1.CERTIFICATION_ATTR_ID);
                                                                    if(temp1.CERTIFICATION_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_PENDING
                                                                        || temp1.CERTIFICATION_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_INIT)
                                                                    {
                                                                        boolean isDeclineAgent = false;
                                                                        if(SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                                                            isDeclineAgent = true;
                                                                        } else {
                                                                            if(SessRoleID.equals(Definitions.CONFIG_ROLE_ID_AGENT_ADMIN) || SessRoleID.equals(Definitions.CONFIG_ROLE_ID_AGENT_SURVEYOR)) {
                                                                                if(SessLevelBranch.equals(Definitions.CONFIG_BRANCH_LEVEL_CHILREN_ONE)) {
                                                                                    isDeclineAgent = true;
                                                                                } else {
                                                                                    int intApprove = db.S_BO_CHECK_BRANCH_APPROVED(Integer.parseInt(sCERTIFICATION_ATTR_ID), Integer.parseInt(SessUserAgentID), sessTreeArrayBranchID);
                                                                                    if(intApprove == 1) {
                                                                                        isDeclineAgent = true;
                                                                                    }
                                                                                }
                                                                            } else {
                                                                                if(SessLevelBranch.equals(Definitions.CONFIG_BRANCH_LEVEL_CHILREN_ONE)) {
                                                                                    if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_DECLINED,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true) {
                                                                                        isDeclineAgent = true;
                                                                                    }
                                                                                } else {
                                                                                    int intApprove = db.S_BO_CHECK_BRANCH_APPROVED(Integer.parseInt(sCERTIFICATION_ATTR_ID), Integer.parseInt(SessUserAgentID), sessTreeArrayBranchID);
                                                                                    if(intApprove == 1) {
                                                                                        if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_DECLINED,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true) {
                                                                                            isDeclineAgent = true;
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                %>
                                                                <%
                                                                    if(isDeclineAgent == true) {
                                                                %>
                                                                <span><a style="cursor: pointer;" onclick="popupListCertDecline('<%=sCERTIFICATION_ATTR_ID%>', '<%= String.valueOf(temp1.BRANCH_ID)%>', '<%= String.valueOf(temp1.CREATED_BY_ID)%>');" class="btn btn-info btn-xs"><i class="fa fa-remove"></i> <script>document.write(global_fm_button_decline.toUpperCase());</script> </a></span>
                                                                <%
                                                                    }
                                                                %>
                                                                <%
                                                                    } else if(temp1.CERTIFICATION_ATTR_STATE_ID != Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_COMMITED
                                                                        && temp1.CERTIFICATION_ATTR_STATE_ID != Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_DECLINED
                                                                        && temp1.CERTIFICATION_ATTR_STATE_ID != Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_ISSUED) {
                                                                        if(SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                                                            if(temp1.CERTIFICATION_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_APPROVED
                                                                                && temp1.CERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REVOKE
                                                                                && temp1.CERTIFICATION_STATE_ID == Definitions.CONFIG_CERTIFICATION_STATE_REVOKED)
                                                                            { } else {
                                                                %>
                                                                <%
                                                                    if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_DECLINED,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true)
                                                                    {
                                                                %>
                                                                <span><a style="cursor: pointer;" onclick="popupListCertDecline('<%=sCERTIFICATION_ATTR_ID%>', '<%= String.valueOf(temp1.BRANCH_ID)%>', '<%= String.valueOf(temp1.CREATED_BY_ID)%>');" class="btn btn-info btn-xs"><i class="fa fa-remove"></i> <script>document.write(global_fm_button_decline.toUpperCase());</script> </a></span>
                                                                <%
                                                                    }
                                                                %>
                                                                <%
                                                                            }
                                                                        }
                                                                    } else if(temp1.CERTIFICATION_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_ISSUED)
                                                                    {
                                                                        if(SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                                                            if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_DECLINED,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true)
                                                                            {
                                                                %>
                                                                <span><a style="cursor: pointer;" onclick="popupListCertDecline('<%=sCERTIFICATION_ATTR_ID%>', '<%= String.valueOf(temp1.BRANCH_ID)%>', '<%= String.valueOf(temp1.CREATED_BY_ID)%>');" class="btn btn-info btn-xs"><i class="fa fa-remove"></i> <script>document.write(global_fm_button_decline.toUpperCase());</script> </a></span>
                                                                <%
                                                                            }
                                                                        }
                                                                    }
                                                                %>
                                                                
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
                                                                    if(temp1.PKI_FORMFACTOR_ID != Definitions.CONFIG_PKI_FORMFACTOR_ID_PKI_USIM) {
                                                                %>
                                                                <%
                                                                    if(isViewButtons == true) {
                                                                %>
                                                                <span>
                                                                    <a id="idLinkButtonPrint<%=strID%>" class="btn btn-info btn-xs" style="cursor: pointer; color: #ffffff;"
                                                                        onclick="pupopButtonPrint('<%=strID%>');"><i class="fa fa-print"></i> <script>document.write(global_fm_button_print_profile);</script>
                                                                    </a>
                                                                </span>
                                                                <script>
                                                                    function pupopButtonPrint(id) {
                                                                        if(localStorage.getItem("expandViewPrint" + id) === "1") {
                                                                            localStorage.setItem("expandViewPrint" + id, "0");
                                                                            $("#idLinkViewPrint"+id).css("display","none");
                                                                        } else {
                                                                            localStorage.setItem("expandViewPrint" + id, "1");
                                                                            $("#idLinkViewPrint"+id).css("display","");
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
                                                                    <a style="cursor: pointer;" onclick="ShowDialogList('<%=strID%>', '<%= isEnterprise%>', '<%= isRgister%>', '<%= printRegisterCAOption%>', '<%=isCALoad%>', '<%= anticsrf%>');" class="btn btn-info btn-xs"><script>document.write(global_fm_button_print_regis);</script> (DK01)</a>
                                                                    <a style="cursor: pointer;" onclick="ShowDialogConfirm('<%=strID%>', '<%= isEnterprise%>', '<%=isCALoad%>', '<%= anticsrf%>');" class="btn btn-info btn-xs"><script>document.write(global_fm_button_print_confirm);</script> (DK02)</a>
                                                                    <%
                                                                            } else {
                                                                                if(temp1.PKI_FORMFACTOR_ID != Definitions.CONFIG_PKI_FORMFACTOR_ID_PKI_USIM) {
                                                                                if(temp1.CERTIFICATION_STATE_ID == Definitions.CONFIG_CERTIFICATION_STATE_EXPIRED)
                                                                                {
                                                                    %>
                                                                    <a style="cursor: pointer;" onclick="ShowDialogList('<%=strID%>', '<%= isEnterprise%>', '<%= isRgister%>', '<%= printRegisterCAOption%>', '<%=isCALoad%>', '<%= anticsrf%>');" class="btn btn-info btn-xs"><script>document.write(global_fm_button_print_regis);</script> (DK01)</a>
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
                                                                    <a style="cursor: pointer;" onclick="ShowDialogList('<%=strID%>', '<%= isEnterprise%>', '<%= isRgister%>', '<%= printRegisterCAOption%>', '<%=isCALoad%>', '<%= anticsrf%>');" class="btn btn-info btn-xs"><script>document.write(global_fm_button_print_regis);</script> (DK01)</a>
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
                                                            </div>
                                                        </td>
                                                        <td style="text-align: center;"><%= com.convertMoney(j)%></td>
                                                        <td>
                                                            <div style="min-width: 140px;">
                                                                <%= EscapeUtils.CheckTextNull(temp1.COMPANY_NAME)%>
                                                            </div>
                                                        </td>
                                                        <td><%= temp1.ENTERPRISE_ID%></td>
                                                        <td><%= EscapeUtils.CheckTextNull(temp1.PERSONAL_NAME)%></td>
                                                        <td><%= temp1.PERSONAL_ID%></td>
                                                        <td><%= temp1.PKI_FORMFACTOR_DESC%></td>
                                                        <td><%= "[" + EscapeUtils.CheckTextNull(temp1.CERTIFICATION_PROFILE_NAME) + "] " + temp1.CERTIFICATION_PROFILE_DESC %></td>
                                                        <td><%= EscapeUtils.CheckTextNull(temp1.CERTIFICATION_PURPOSE_DESC)%></td>
                                                        <td><%= EscapeUtils.CheckTextNull(temp1.CERTIFICATION_STATE_DESC)%></td>
                                                        <td><%= sAC%></td>
                                                        <%
                                                            if(SessRoleID_ID.equals(Definitions.CONFIG_ROLE_ID_CA_USER) || SessRoleID_ID.equals(Definitions.CONFIG_ROLE_ID_AGENT_USER)) {
                                                        %>
                                                        <td><%= EscapeUtils.CheckTextNull(temp1.CERTIFICATION_ATTR_STATE_DESC)%></td>
                                                        <td><%= EscapeUtils.CheckTextNull(temp1.SERVICE_TYPE_DESC)%></td>
                                                        <%
                                                            }
                                                        %>
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
                                                        <td><%= EscapeUtils.CheckTextNull(temp1.CERTIFICATION_SN)%></td>
                                                        <td><%= CommonFunction.checkViewTokenValid(EscapeUtils.CheckTextNull(temp1.TOKEN_SN)) ? EscapeUtils.CheckTextNull(temp1.TOKEN_SN) : "" %></td>
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
<!--                                <script type="text/javascript">
                                    $(document).ready(function () {
                                        goToByScroll("idShowResultSearch");
                                    });
                                </script>-->
                                <div class="x_panel" id="idShowResultSearch">
                                    <div class="x_title" style="border-bottom: 0 solid #E6E9ED;margin-bottom: 0px;">
                                        <h2></h2>
                                        <ul class="nav navbar-right panel_toolbox">
                                            <li style="color: red;font-weight: bold;">
                                                <button type="button" class="btn btn-info" onClick="PupopRegister();"><script>document.write(global_fm_button_add_action);</script></button>
                                                <script>
                                                    function PupopRegister() {
                                                        window.location = "RegisterCertificate.jsp";
                                                    }
                                                </script>
                                            </li>
                                        </ul>
                                        <div class="clearfix"></div>
                                    </div>
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
            <script src="../style/jquery.min.js"></script>
            <script src="../style/bootstrap.min.js"></script>
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
        <!-- Modal Cert Decline -->
        <div id="myModalListCertDecline" class="modal fade" role="dialog">
            <div style="width: 100%; text-align: center; position: fixed;z-index: 1000;top: 0; padding-top: 90px;
                 left: 0; height: 100%;" class="loading-gifCertDecline">
                <img src="../Images/ajax-loader1.gif" alt="Please wait..." />
            </div>
            <div class="modal-dialog modal-800" id="myDialogListCertDecline">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <div id="contentCertDecline"></div>
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
                $.ajax({
                    type: "post",
                    url: "../PrintFormCommon",
                    data: {
                        idParam: 'printconfirminforedrect',
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
                if(isCA === JS_IS_WHICH_ABOUT_CA_ICA) {
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
                        }
                        else {
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