<%-- 
    Document   : ProfileList
    Created on : Dec 10, 2019, 5:37:36 PM
    Author     : USER
--%>

<%@page import="vn.ra.process.CommonReferServlet"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="../Admin/ConnectionParam.jsp" %>
<%@include file="../Admin/CommonPagingList.jsp" %>
<!DOCTYPE html>
<%
    String strAlertAllTimes = "0";
    String strAlertAllControl = "0";
    String isUIDCollection = LoadParamSystem.getParamStart(Definitions.CONFIG_IS_UID_COLLECTION_DISPLAY_ENABLED);
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="../style/bootstrap.min.css" rel="stylesheet">
        <link href="../style/font-awesome.css" rel="stylesheet">
        <link href="../style/nprogress.css" rel="stylesheet">
        <link href="../style/custom.min.css" rel="stylesheet">
        <%
            String sNewRefreshJS = cogCommon.GetPropertybyCode(Definitions.CONFIG_JS_REFRESH_STRING_RANDOM);
        %>
        <script src="../js/Language.js?t=<%=sNewRefreshJS%>"></script>
        <script src="../js/process_javajs.js?t=<%=sNewRefreshJS%>"></script>
        <script type="text/javascript" src="../js/jquery.js"></script>
        <link rel="stylesheet" href="../js/sweetalert.css"/>
        <script src="../js/sweetalert-dev.js"></script>
        <link href="../style/customportal.min.css" rel="stylesheet">
        <script type="text/javascript" src="../Css/GlobalAlert.js"></script>
        <link href="../Css/smartpaginator.css" rel="stylesheet" type="text/css"/>
        <script src="../Css/smartpaginator.js" type="text/javascript"></script>
        <title></title>
        <script language="javascript">
            document.title = profile_title_list;
            changeFavicon("../");
            $(document).ready(function () {
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
                $('#fromControl').daterangepicker({
                    singleDatePicker: true,
                    showDropdowns: true
                }, function (start, end, label) {
                    console.log(start.toISOString(), end.toISOString(), label);
                });
                $('#toControl').daterangepicker({
                    singleDatePicker: true,
                    showDropdowns: true
                }, function (start, end, label) {
                    console.log(start.toISOString(), end.toISOString(), label);
                });
                $('.loading-gif').hide();
                $('[data-toggle="tooltipPrefix"]').tooltip();
            });
            $(document).ready(function () {
                $('#COMPANY_NAME').keydown(function (event) {
                    if (event.keyCode === 13) {
                        searchForm(document.getElementById("CsrfToken").value);
                    }
                });
                $('#ENTERPRISE_ID').keydown(function (event) {
                    if (event.keyCode === 13) {
                        searchForm(document.getElementById("CsrfToken").value);
                    }
                });
                $('#PERSONAL_NAME').keydown(function (event) {
                    if (event.keyCode === 13) {
                        searchForm(document.getElementById("CsrfToken").value);
                    }
                });
                $('#PERSONAL_ID').keydown(function (event) {
                    if (event.keyCode === 13) {
                        searchForm(document.getElementById("CsrfToken").value);
                    }
                });
            });
            function searchForm(id)
            {
                document.getElementById("idHiddenLoad").value = JS_STR_GRID_SEARCH_RESET;
                document.getElementById("tempCsrfToken").value = id;
                var f = document.form;
                f.method = "post";
                f.action = '';
                f.submit();
            }
            function popupDetailCert(id)
            {
                $('body').append('<div id="over"></div>');
                $(".loading-gif").show();
                $("#contentEdit").empty();
                $('#contentEdit').load('ProfileEdit.jsp', {id:id}, function () {
                    $(".loading-gif").hide();
                    $('#over').remove();
                    $('#idX_Panel_Edit').css("display", "");
                    goToByScroll("contentEdit");
                });
            }
        </script>
        <style>
            .projects th{font-weight: bold;}
            .navbar-right{margin-right: 0;padding-right:10px;}
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
            .x_panel {
                padding:10px 17px 10px 17px;
            }
            .x_content {
                padding: 0 5px 0 5px;
            }
        </style>
    </head>
    <body class="nav-md">
        <%
            if ((session.getAttribute("UserID")) != null) {
                String anticsrf = "" + Math.random();
                request.getSession().setAttribute("anticsrf", anticsrf);
                String SessAgentID = session.getAttribute("SessAgentID").toString().trim();
                String SessUserID = session.getAttribute("UserID").toString().trim();
                String SessRoleID = session.getAttribute("RoleID_ID").toString().trim();
                String SessUserAgentID = session.getAttribute("SessUserAgentID").toString().trim();
                String isCALoad = LoadParamSystem.getParamStart(Definitions.CONFIG_IS_WHICH_ABOUT_CA);
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
                        document.getElementById("idNameURL").innerHTML = profile_title_list;
                    </script>
                </div>
                <div class="right_col" role="main">
                    <div class="">
                        <div class="row">
                            <div class="col-md-12 col-sm-12 col-xs-12">
                            <%
                                int status = 1000;
                                int statusLoad = 0;
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
                                    String sEnterpriseCert = "";
                                    String sPersonalCert = "";
                                    CERTIFICATION[][] rsPginToken = new CERTIFICATION[1][];
                                    String sessLanguageGlobal = session.getAttribute("sessVN").toString();
                                    String loginUID = session.getAttribute("sUserID").toString();
                                    if (session.getAttribute("RefreshProfileCertSess") != null && session.getAttribute("sessMonthProfileCert") != null
                                         && session.getAttribute("sessYearProfileCert") != null) {
                                        try {
                                            statusLoad = 1;
                                            String ToReceivedDate = (String) session.getAttribute("sessToReceiveDateProfileCert");
                                            String FromReceivedDate = (String) session.getAttribute("sessFromReceiveDateProfileCert");
                                            String ToControlDate = (String) session.getAttribute("sessToControlProfileCert");
                                            String FromControlDate = (String) session.getAttribute("sessFromControlProfileCert");
                                            String ToCreateDate = (String) session.getAttribute("sessMonthProfileCert");
                                            String FromCreateDate = (String) session.getAttribute("sessYearProfileCert");
                                            String PERSONAL_NAME = (String) session.getAttribute("sessPERSONAL_NAMEProfileCert");
                                            String COMPANY_NAME = (String) session.getAttribute("sessCOMPANY_NAMEProfileCert");
                                            String TAX_CODE = (String) session.getAttribute("sessTAX_CODEProfileCert");
                                            String BUDGET_CODE = (String) session.getAttribute("sessBUDGET_CODEProfileCert");
                                            String DECISION = (String) session.getAttribute("sessDECISIONProfileCert");
                                            String P_ID = (String) session.getAttribute("sessP_IDProfileCert");
                                            String CCCD = (String) session.getAttribute("sessCCCDProfileCert");
                                            String PASSPORT = (String) session.getAttribute("sessPASSPORTProfileCert");
                                            String idCollectEnabled = (String) session.getAttribute("sessCollectEnabledProfileCert");
                                            String BranchOffice = (String) session.getAttribute("sessBranchOfficeProfileCert");
                                            String idCheckCompensation = (String) session.getAttribute("sessCompensationProfileCert");
                                            String idCheckOverdue = (String) session.getAttribute("sessOverdueProfileCert");
                                            String idCheckCommitEnabled = (String) session.getAttribute("sessCommitEnabledCert");
                                            String stateProfile = (String) session.getAttribute("sessStateProfileProfileCert");
                                            String sConfirmResign = (String) session.getAttribute("sessResignProfileCert");
                                            String sSignedEnabled = (String) session.getAttribute("sessSignedEnabledProfileCert");
                                            strAlertAllTimes = (String) session.getAttribute("AlertAllTimeSProfileCert");
                                            strAlertAllControl = (String) session.getAttribute("AlertAllControlProfileCert");
                                            session.setAttribute("RefreshProfileCertSess", null);
                                            session.setAttribute("sessFromReceiveDateProfileCert", FromReceivedDate);
                                            session.setAttribute("sessToReceiveDateProfileCert", ToReceivedDate);
                                            session.setAttribute("sessFromControlProfileCert", FromControlDate);
                                            session.setAttribute("sessToControlProfileCert", ToControlDate);
                                            session.setAttribute("sessMonthProfileCert", ToCreateDate);
                                            session.setAttribute("sessYearProfileCert", FromCreateDate);
                                            session.setAttribute("sessPERSONAL_NAMEProfileCert", PERSONAL_NAME);
                                            session.setAttribute("sessCOMPANY_NAMEProfileCert", COMPANY_NAME);
                                            session.setAttribute("sessTAX_CODEProfileCert", TAX_CODE);
                                            session.setAttribute("sessBUDGET_CODEProfileCert", BUDGET_CODE);
                                            session.setAttribute("sessDECISIONProfileCert", DECISION);
                                            session.setAttribute("sessP_IDProfileCert", P_ID);
                                            session.setAttribute("sessCCCDProfileCert", CCCD);
                                            session.setAttribute("sessPASSPORTProfileCert", PASSPORT);
                                            session.setAttribute("sessCollectEnabledProfileCert", idCollectEnabled);
                                            session.setAttribute("sessBranchOfficeProfileCert", BranchOffice);
                                            session.setAttribute("sessCompensationProfileCert", idCheckCompensation);
                                            session.setAttribute("sessOverdueProfileCert", idCheckOverdue);
                                            session.setAttribute("sessCommitEnabledCert", idCheckCommitEnabled);
                                            session.setAttribute("sessStateProfileProfileCert", stateProfile);
                                            session.setAttribute("sessResignProfileCert", sConfirmResign);
                                            session.setAttribute("sessSignedEnabledProfileCert", sSignedEnabled);
                                            session.setAttribute("AlertAllTimeSProfileCert", strAlertAllTimes);
                                            session.setAttribute("AlertAllControlProfileCert", strAlertAllControl);
                                            if(isUIDCollection.equals("1")) {
                                                String sENTERPRISE_ID = (String) session.getAttribute("sessENTERPRISE_IDProfileCert");
                                                String sPERSONAL_ID = (String) session.getAttribute("sessPERSONAL_IDProfileCert");
                                                String sENTERPRISE_PREFIX = (String) session.getAttribute("sessENTERPRISE_PREFIXProfileCert");
                                                String sPERSONAL_PREFIX = (String) session.getAttribute("sessPERSONAL_PREFIXProfileCert");
                                                session.setAttribute("sessENTERPRISE_IDProfileCert", sENTERPRISE_ID);
                                                session.setAttribute("sessPERSONAL_IDProfileCert", sPERSONAL_ID);
                                                session.setAttribute("sessENTERPRISE_PREFIXProfileCert", sENTERPRISE_PREFIX);
                                                session.setAttribute("sessPERSONAL_PREFIXProfileCert", sPERSONAL_PREFIX);
                                                isLoadEnterprisePrefix = sENTERPRISE_PREFIX;
                                                isLoadPeronalPrefix = sPERSONAL_PREFIX;
                                                if(!"".equals(sENTERPRISE_ID)){
                                                    sEnterpriseCert = sENTERPRISE_PREFIX + "%"+sENTERPRISE_ID+"%";
                                                }
                                                if(!"".equals(sPERSONAL_ID)){
                                                    sPersonalCert = sPERSONAL_PREFIX + "%"+sPERSONAL_ID+"%";
                                                }
                                            }
                                            if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(idCollectEnabled)) {
                                                idCollectEnabled = "";
                                            }
                                            if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(BranchOffice)) {
                                                BranchOffice = "";
                                            }
                                            if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(stateProfile)) {
                                                stateProfile = "";
                                            }
                                            if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(sConfirmResign)) {
                                                sConfirmResign = "";
                                            }
                                            if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(sSignedEnabled)) {
                                                sSignedEnabled = "";
                                            }
                                            if(SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                                SessUserID = "";
                                            } else {
                                                if(!SessRoleID.equals(Definitions.CONFIG_ROLE_ID_AGENT_USER)) {
                                                    SessUserID = "";
                                                }
                                            }
                                            if("1".equals(strAlertAllTimes)) {
                                                FromReceivedDate = "";
                                                ToReceivedDate = "";
                                            }
                                            if("1".equals(strAlertAllControl)) {
                                                FromControlDate = "";
                                                ToControlDate = "";
                                            } else {
                                                ToCreateDate = "";
                                                FromCreateDate = "";
                                            }
                                            if("0".equals(ToCreateDate)){ToCreateDate = "";}
                                            String pBRANCH_BENEFICIARY_ID = SessUserAgentID;
                                            if(SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT) && !"".equals(BranchOffice)) {
                                                pBRANCH_BENEFICIARY_ID=EscapeUtils.escapeHtmlSearch(BranchOffice);
                                            }
                                            if(!isUIDCollection.equals("1")) {
                                                String[] sUIDResult = new String[2];
                                                CommonReferServlet.collectFieldToUID(TAX_CODE, BUDGET_CODE, DECISION, P_ID, PASSPORT, CCCD, sUIDResult);
                                                sEnterpriseCert = sUIDResult[0];
                                                sPersonalCert = sUIDResult[1];
                                            }
                                            int ssToken = 0;
                                            if ((session.getAttribute("CountListProfileCert")) == null) {
                                                ssToken = db.S_BO_CERTIFICATION_BRIEF_TOTAL(COMPANY_NAME, PERSONAL_NAME,
                                                    idCollectEnabled, EscapeUtils.escapeHtmlSearch(ToCreateDate),
                                                    EscapeUtils.escapeHtmlSearch(FromCreateDate), BranchOffice, idCheckCompensation, idCheckOverdue,
                                                    SessUserID, idCheckCommitEnabled, stateProfile,
                                                    EscapeUtils.escapeHtmlSearch(FromReceivedDate),EscapeUtils.escapeHtmlSearch(ToReceivedDate),
                                                    sConfirmResign, sSignedEnabled, sEnterpriseCert, sPersonalCert,
                                                    pBRANCH_BENEFICIARY_ID, FromControlDate, ToControlDate);
                                                session.setAttribute("CountListProfileCert", String.valueOf(ssToken));
                                            } else {
                                                String sCount = (String) session.getAttribute("CountListProfileCert");
                                                ssToken = Integer.parseInt(sCount);
                                                session.setAttribute("CountListProfileCert", String.valueOf(ssToken));
                                            }
                                            if (session.getAttribute("SearchIPageNoPagingProfileCert") != null) {
                                                String sPage = (String) session.getAttribute("SearchIPageNoPagingProfileCert");
                                                iPagNo = Integer.parseInt(sPage);
                                            }
                                            if (session.getAttribute("SearchISwRwsPagingProfileCert") != null) {
                                                String sSumPage = (String) session.getAttribute("SearchISwRwsPagingProfileCert");
                                                iSwRws = Integer.parseInt(sSumPage);
                                            }
                                            if (session.getAttribute("RefreshCertSessNumberPaging") != null) {
                                                String sNoPage = (String) session.getAttribute("RefreshCertSessNumberPaging");
                                                iPaNoSS = Integer.parseInt(sNoPage);
                                            }
                                            session.setAttribute("SearchIPageNoPagingProfileCert", String.valueOf(iPagNo));
                                            session.setAttribute("SearchISwRwsPagingProfileCert", String.valueOf(iSwRws));
                                            if (ssToken > 0) {
                                                db.S_BO_CERTIFICATION_BRIEF_LIST(COMPANY_NAME, PERSONAL_NAME, idCollectEnabled,
                                                    EscapeUtils.escapeHtmlSearch(ToCreateDate), EscapeUtils.escapeHtmlSearch(FromCreateDate),
                                                    BranchOffice, idCheckCompensation, idCheckOverdue, SessUserID, sessLanguageGlobal, rsPginToken,
                                                    iPagNo, iSwRws, idCheckCommitEnabled, stateProfile,
                                                    EscapeUtils.escapeHtmlSearch(FromReceivedDate),EscapeUtils.escapeHtmlSearch(ToReceivedDate),
                                                    sConfirmResign, sSignedEnabled, sEnterpriseCert, sPersonalCert,
                                                    pBRANCH_BENEFICIARY_ID, FromControlDate, ToControlDate);
                                            }
                                            iTotRslts = ssToken;
                                            if (ssToken > 0) {
                                                strMess = com.convertMoney(ssToken);
                                            }
                                            if (iTotRslts > 0 && rsPginToken[0].length > 0) {
                                                status = 1;
                                            } else {
                                                status = 1000;
                                            }
                                        } catch(Exception e){CommonFunction.LogExceptionServlet(null, "ProfileList reload: " + e.getMessage(), e);}
                                    } else if (request.getMethod().equals("POST") || "1".equals(hasPaging)) {
                                        if (request.getMethod().equals("POST")) {
                                            session.setAttribute("SearchIPageNoPagingProfileCert", null);
                                            session.setAttribute("SearchISwRwsPagingProfileCert", null);
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
                                        String FromReceivedDate = request.getParameter("FromCreateDate");
                                        String ToControlDate = request.getParameter("toControl");
                                        String FromControlDate = request.getParameter("fromControl");
                                        String ToReceivedDate = request.getParameter("ToCreateDate");
                                        String FromCreateDate = request.getParameter("idYear");
                                        String ToCreateDate = request.getParameter("idMounth");
                                        String PERSONAL_NAME = EscapeUtils.ConvertStringToUnicode(request.getParameter("PERSONAL_NAME"));
                                        String COMPANY_NAME = EscapeUtils.ConvertStringToUnicode(request.getParameter("COMPANY_NAME"));
                                        String TAX_CODE ="";
                                        String BUDGET_CODE ="";
                                        String DECISION ="";
                                        String P_ID = "";
                                        String CCCD = "";
                                        String PASSPORT = "";
                                        String idCollectEnabled = request.getParameter("idCollectEnabled");
                                        String BranchOffice = request.getParameter("BranchOffice");
                                        String stateProfile = request.getParameter("STATE_PROFILE_SEARCH");
                                        String sConfirmResign = request.getParameter("CONFIRM_RESIGN_SEARCH");
                                        String sSignedEnabled = request.getParameter("idSignedEnabled");
                                        String idCheckCompensation = "0";
                                        String idCheckOverdue = "0";
                                        String idCheckCommitEnabled = "0";
                                        Boolean nameCheckDate = Boolean.valueOf(request.getParameter("nameCheck") != null);
                                        if (nameCheckDate == false) {
                                            FromReceivedDate = "";
                                            ToReceivedDate = "";
                                            strAlertAllTimes = "1";
                                        }
                                        Boolean nameControlCheckDate = Boolean.valueOf(request.getParameter("nameControlCheck") != null);
                                        if (nameControlCheckDate == false) {
                                            FromControlDate = "";
                                            ToControlDate = "";
                                            strAlertAllControl = "1";
                                        } else {
                                            ToCreateDate = "";
                                            FromCreateDate = "";
                                        }
                                        if(!isUIDCollection.equals("1")) {
                                            TAX_CODE = EscapeUtils.ConvertStringToUnicode(request.getParameter("TAX_CODE"));
                                            BUDGET_CODE = EscapeUtils.ConvertStringToUnicode(request.getParameter("BUDGET_CODE"));
                                            P_ID = EscapeUtils.ConvertStringToUnicode(request.getParameter("P_ID"));
                                            CCCD = EscapeUtils.ConvertStringToUnicode(request.getParameter("CCCD"));
                                            PASSPORT = EscapeUtils.ConvertStringToUnicode(request.getParameter("v"));
                                            DECISION = EscapeUtils.ConvertStringToUnicode(request.getParameter("DECISION"));
                                        } else {
                                            String sENTERPRISE_PREFIX = EscapeUtils.ConvertStringToUnicode(request.getParameter("ENTERPRISE_PREFIX"));
                                            String sPERSONAL_PREFIX = EscapeUtils.ConvertStringToUnicode(request.getParameter("PERSONAL_PREFIX"));
                                            String sENTERPRISE_ID = request.getParameter("ENTERPRISE_ID");
                                            String sPERSONAL_ID = request.getParameter("PERSONAL_ID");
                                            if ("1".equals(hasPaging)) {
                                                sENTERPRISE_PREFIX = (String) session.getAttribute("sessENTERPRISE_PREFIXProfileCert");
                                                sPERSONAL_PREFIX = (String) session.getAttribute("sessPERSONAL_PREFIXProfileCert");
                                                sENTERPRISE_ID = (String) session.getAttribute("sessENTERPRISE_IDProfileCert");
                                                sPERSONAL_ID = (String) session.getAttribute("sessPERSONAL_IDProfileCert");
                                            }
                                            session.setAttribute("sessENTERPRISE_PREFIXProfileCert", sENTERPRISE_PREFIX);
                                            session.setAttribute("sessPERSONAL_PREFIXProfileCert", sPERSONAL_PREFIX);
                                            session.setAttribute("sessENTERPRISE_IDProfileCert", sENTERPRISE_ID);
                                            session.setAttribute("sessPERSONAL_IDProfileCert", sPERSONAL_ID);
                                            isLoadEnterprisePrefix = sENTERPRISE_PREFIX;
                                            isLoadPeronalPrefix = sPERSONAL_PREFIX;
                                            if(!"".equals(sENTERPRISE_ID)){
                                                sEnterpriseCert = sENTERPRISE_PREFIX + "%"+sENTERPRISE_ID+"%";
                                            }
                                            if(!"".equals(sPERSONAL_ID)){
                                                sPersonalCert = sPERSONAL_PREFIX + "%"+sPERSONAL_ID+"%";
                                            }
                                        }
                                        Boolean nameCheck = Boolean.valueOf(request.getParameter("idCheckCompensation") != null);
                                        if (nameCheck == true) {
                                            idCheckCompensation = "1";
                                        }
                                        Boolean checkOverdue = Boolean.valueOf(request.getParameter("idCheckOverdue") != null);
                                        if (checkOverdue == true) {
                                            idCheckOverdue = "1";
                                        }
                                        Boolean checkCommitEnabled = Boolean.valueOf(request.getParameter("idCheckCommitEnabled") != null);
                                        if (checkCommitEnabled == true) {
                                            idCheckCommitEnabled = "1";
                                        }
                                        if(SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                            SessUserID = "";
                                        } else {
                                            if(!SessRoleID.equals(Definitions.CONFIG_ROLE_ID_AGENT_USER)) {
                                                SessUserID = "";
                                            }
                                        }
                                        if ("1".equals(hasPaging)) {
                                            ToReceivedDate = (String) session.getAttribute("sessToReceiveDateProfileCert");
                                            FromReceivedDate = (String) session.getAttribute("sessFromReceiveDateProfileCert");
                                            ToControlDate = (String) session.getAttribute("sessToControlProfileCert");
                                            FromControlDate = (String) session.getAttribute("sessFromControlProfileCert");
                                            ToCreateDate = (String) session.getAttribute("sessMonthProfileCert");
                                            FromCreateDate = (String) session.getAttribute("sessYearProfileCert");
                                            PERSONAL_NAME = (String) session.getAttribute("sessPERSONAL_NAMEProfileCert");
                                            COMPANY_NAME = (String) session.getAttribute("sessCOMPANY_NAMEProfileCert");
                                            TAX_CODE = (String) session.getAttribute("sessTAX_CODEProfileCert");
                                            BUDGET_CODE = (String) session.getAttribute("sessBUDGET_CODEProfileCert");
                                            DECISION = (String) session.getAttribute("sessDECISIONProfileCert");
                                            P_ID = (String) session.getAttribute("sessP_IDProfileCert");
                                            CCCD = (String) session.getAttribute("sessCCCDProfileCert");
                                            PASSPORT = (String) session.getAttribute("sessPASSPORTProfileCert");
                                            idCollectEnabled = (String) session.getAttribute("sessCollectEnabledProfileCert");
                                            BranchOffice = (String) session.getAttribute("sessBranchOfficeProfileCert");
                                            idCheckCompensation = (String) session.getAttribute("sessCompensationProfileCert");
                                            idCheckOverdue = (String) session.getAttribute("sessOverdueProfileCert");
                                            idCheckCommitEnabled = (String) session.getAttribute("sessCommitEnabledCert");
                                            stateProfile = (String) session.getAttribute("sessStateProfileProfileCert");
                                            sConfirmResign = (String) session.getAttribute("sessResignProfileCert");
                                            sSignedEnabled = (String) session.getAttribute("sessSignedEnabledProfileCert");
                                            strAlertAllTimes = (String) session.getAttribute("AlertAllTimeSProfileCert");
                                            strAlertAllControl = (String) session.getAttribute("AlertAllControlProfileCert");
                                            session.setAttribute("SessParamOnPagingCertList", null);
                                        } else {
                                            session.setAttribute("CountListProfileCert", null);
                                        }
                                        session.setAttribute("sessFromReceiveDateProfileCert", FromReceivedDate);
                                        session.setAttribute("sessToReceiveDateProfileCert", ToReceivedDate);
                                        session.setAttribute("sessFromControlProfileCert", FromControlDate);
                                        session.setAttribute("sessToControlProfileCert", ToControlDate);
                                        session.setAttribute("sessYearProfileCert", FromCreateDate);
                                        session.setAttribute("sessMonthProfileCert", ToCreateDate);
                                        session.setAttribute("sessPERSONAL_NAMEProfileCert", PERSONAL_NAME);
                                        session.setAttribute("sessCOMPANY_NAMEProfileCert", COMPANY_NAME);
                                        session.setAttribute("sessTAX_CODEProfileCert", TAX_CODE);
                                        session.setAttribute("sessBUDGET_CODEProfileCert", BUDGET_CODE);
                                        session.setAttribute("sessDECISIONProfileCert", DECISION);
                                        session.setAttribute("sessP_IDProfileCert", P_ID);
                                        session.setAttribute("sessCCCDProfileCert", CCCD);
                                        session.setAttribute("sessPASSPORTProfileCert", PASSPORT);
                                        session.setAttribute("sessCollectEnabledProfileCert", idCollectEnabled);
                                        session.setAttribute("sessBranchOfficeProfileCert", BranchOffice);
                                        session.setAttribute("sessCompensationProfileCert", idCheckCompensation);
                                        session.setAttribute("sessOverdueProfileCert", idCheckOverdue);
                                        session.setAttribute("sessCommitEnabledCert", idCheckCommitEnabled);
                                        session.setAttribute("sessStateProfileProfileCert", stateProfile);
                                        session.setAttribute("sessResignProfileCert", sConfirmResign);
                                        session.setAttribute("sessSignedEnabledProfileCert", sSignedEnabled);
                                        session.setAttribute("AlertAllTimeSProfileCert", strAlertAllTimes);
                                        session.setAttribute("AlertAllControlProfileCert", strAlertAllControl);
                                        if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(idCollectEnabled)) {
                                            idCollectEnabled = "";
                                        }
                                        if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(BranchOffice)) {
                                            BranchOffice = "";
                                        }
                                        if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(stateProfile)) {
                                            stateProfile = "";
                                        }
                                        if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(sConfirmResign)) {
                                            sConfirmResign = "";
                                        }
                                        if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(sSignedEnabled)) {
                                            sSignedEnabled = "";
                                        }
                                        if("0".equals(ToCreateDate)){ToCreateDate = "";}
                                        String pBRANCH_BENEFICIARY_ID = SessUserAgentID;
                                        if(SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT) && !"".equals(BranchOffice)) {
                                            pBRANCH_BENEFICIARY_ID=EscapeUtils.escapeHtmlSearch(BranchOffice);
                                        }
                                        if(!isUIDCollection.equals("1")) {
                                            String[] sUIDResult = new String[2];
                                            CommonReferServlet.collectFieldToUID(TAX_CODE, BUDGET_CODE, DECISION, P_ID, PASSPORT, CCCD, sUIDResult);
                                            sEnterpriseCert = sUIDResult[0];
                                            sPersonalCert = sUIDResult[1];
                                        }
                                        int ssToken = 0;
                                        if ((session.getAttribute("CountListProfileCert")) == null) {
                                            ssToken = db.S_BO_CERTIFICATION_BRIEF_TOTAL(COMPANY_NAME, PERSONAL_NAME,
                                                idCollectEnabled, EscapeUtils.escapeHtmlSearch(ToCreateDate),
                                                EscapeUtils.escapeHtmlSearch(FromCreateDate), BranchOffice, idCheckCompensation,
                                                idCheckOverdue, SessUserID, idCheckCommitEnabled, stateProfile,
                                                EscapeUtils.escapeHtmlSearch(FromReceivedDate),EscapeUtils.escapeHtmlSearch(ToReceivedDate),
                                                sConfirmResign, sSignedEnabled, sEnterpriseCert, sPersonalCert, 
                                                    pBRANCH_BENEFICIARY_ID, FromControlDate, ToControlDate);
                                            session.setAttribute("CountListProfileCert", String.valueOf(ssToken));
                                        } else {
                                            String sCount = (String) session.getAttribute("CountListProfileCert");
                                            ssToken = Integer.parseInt(sCount);
                                            session.setAttribute("CountListProfileCert", String.valueOf(ssToken));
                                        }
                                        iTotRslts = ssToken;
                                        if (iTotRslts > 0) {
                                            db.S_BO_CERTIFICATION_BRIEF_LIST(COMPANY_NAME, PERSONAL_NAME, idCollectEnabled,
                                                EscapeUtils.escapeHtmlSearch(ToCreateDate), EscapeUtils.escapeHtmlSearch(FromCreateDate),
                                                BranchOffice, idCheckCompensation, idCheckOverdue, SessUserID, sessLanguageGlobal, rsPginToken,
                                                iPagNo, iSwRws, idCheckCommitEnabled, stateProfile,
                                                EscapeUtils.escapeHtmlSearch(FromReceivedDate),EscapeUtils.escapeHtmlSearch(ToReceivedDate),
                                                sConfirmResign, sSignedEnabled, sEnterpriseCert, sPersonalCert,
                                                pBRANCH_BENEFICIARY_ID, FromControlDate, ToControlDate);
                                            session.setAttribute("SearchIPageNoPagingProfileCert", String.valueOf(iPagNo));
                                            session.setAttribute("SearchISwRwsPagingProfileCert", String.valueOf(iSwRws));
                                            strMess = com.convertMoney(ssToken);
                                            if (rsPginToken[0].length > 0) {
                                                status = 1;
                                                db.S_BO_CERTIFICATION_BRIEF_UPDATE_BRIEF_TYPE(COMPANY_NAME, TAX_CODE, BUDGET_CODE, PERSONAL_NAME,
                                                    P_ID, PASSPORT, idCollectEnabled, EscapeUtils.escapeHtmlSearch(ToCreateDate),
                                                    EscapeUtils.escapeHtmlSearch(FromCreateDate), BranchOffice, loginUID,
                                                    idCheckCommitEnabled, DECISION, EscapeUtils.escapeHtmlSearch(CCCD), sConfirmResign, sEnterpriseCert, sPersonalCert);
                                            } else {
                                                status = 1000;
                                            }
                                        } else {
                                            status = 1000;
                                        }
                                    } catch(Exception e){CommonFunction.LogExceptionServlet(null, "ProfileList search new: " + e.getMessage(), e);}
                                } else {
                                    session.setAttribute("SearchIPageNoPagingProfileCert", null);
                                    session.setAttribute("SearchISwRwsPagingProfileCert", null);
                                    session.setAttribute("sessYearProfileCert", null);
                                    session.setAttribute("sessMonthProfileCert", null);
                                    session.setAttribute("sessPERSONAL_NAMEProfileCert", null);
                                    session.setAttribute("sessCOMPANY_NAMEProfileCert", null);
                                    session.setAttribute("sessTAX_CODEProfileCert", null);
                                    session.setAttribute("sessDEVICE_UUIDRenewCert", null);
                                    session.setAttribute("sessBUDGET_CODEProfileCert", null);
                                    session.setAttribute("sessDECISIONProfileCert", null);
                                    session.setAttribute("sessP_IDProfileCert", null);
                                    session.setAttribute("sessCCCDProfileCert", null);
                                    session.setAttribute("sessCollectEnabledProfileCert", null);
                                    session.setAttribute("sessBranchOfficeProfileCert", "All");
                                    session.setAttribute("sessCompensationProfileCert", null);
                                    session.setAttribute("sessOverdueProfileCert", null);
                                    session.setAttribute("sessCommitEnabledCert", null);
                                    session.setAttribute("sessStateProfileProfileCert", "All");
                                    session.setAttribute("sessSignedEnabledProfileCert", "All");
                                    session.setAttribute("AlertAllTimeSProfileCert", "1");
                                    session.setAttribute("AlertAllControlProfileCert", "1");
                                    session.setAttribute("sessFromReceiveDateProfileCert", null);
                                    session.setAttribute("sessToReceiveDateProfileCert", null);
                                    session.setAttribute("sessFromControlProfileCert", null);
                                    session.setAttribute("sessToControlProfileCert", null);
                                    if(isUIDCollection.equals("1")) {
                                        session.setAttribute("sessENTERPRISE_PREFIXProfileCert", null);
                                        session.setAttribute("sessPERSONAL_PREFIXProfileCert", null);
                                        session.setAttribute("sessENTERPRISE_IDProfileCert", null);
                                        session.setAttribute("sessPERSONAL_IDProfileCert", null);
                                    }
                                }
                                %>
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
                                                function checkboxControlChange() {
                                                    if ($("#idControlCheck").is(':checked')) {
                                                        document.getElementById("fromControl").disabled = false;
                                                        document.getElementById("toControl").disabled = false;
                                                    }
                                                    else
                                                    {
                                                        document.getElementById("fromControl").disabled = true;
                                                        document.getElementById("toControl").disabled = true;
                                                    }
                                                }
                                            </script>
                                            <div class="form-group" style="padding: 0px 0px 0px 0px;margin: 0;">
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;"><script>document.write(global_fm_From_Control);</script></label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <input type="Text" id="fromControl" name="fromControl" <%= session.getAttribute("AlertAllControlProfileCert") != null && "1".equals(session.getAttribute("AlertAllControlProfileCert").toString()) ? "disabled" : ""%>
                                                                value="<%= session.getAttribute("sessFromControlProfileCert") != null && !"1".equals(session.getAttribute("AlertAllControlProfileCert").toString()) ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessFromControlProfileCert").toString()) : com.ConvertMonthSub(30)%>"
                                                                maxlength="25" class="form-control123"/>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;"><script>document.write(global_fm_To_Control);</script></label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <input type="Text" id="toControl" name="toControl" <%= session.getAttribute("AlertAllControlProfileCert") != null && "1".equals(session.getAttribute("AlertAllControlProfileCert").toString()) ? "disabled" : ""%>
                                                                value="<%= session.getAttribute("sessToControlProfileCert") != null && !"1".equals(session.getAttribute("AlertAllControlProfileCert").toString()) ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessToControlProfileCert").toString()) : com.ConvertMonthSub(0)%>"
                                                                maxlength="25" class="form-control123"/>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;"><script>document.write(global_fm_check_date_control);</script></label>
                                                        <div class="col-sm-7" style="padding-right: 0px;padding-top: 7px; text-align: left;">
                                                            <label class="switch" for="idControlCheck">
                                                                <input type="checkbox" name="nameControlCheck" id="idControlCheck" onchange="checkboxControlChange();" <%= session.getAttribute("AlertAllControlProfileCert") != null && "1".equals(session.getAttribute("AlertAllControlProfileCert").toString()) ? "" : "checked" %>/>
                                                                <div class="slider round"></div>
                                                            </label>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-sm-4" style="padding-left: 0;">
                                                <div class="form-group">
                                                    <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;">
                                                        <script>document.write(global_fm_mounth);</script>
                                                    </label>
                                                    <div class="col-sm-7" style="padding-right: 0px;">
                                                        <select name="idMounth" id="idMounth" class="form-control123">
                                                            <option value="0" <%= session.getAttribute("sessMonthProfileCert") != null
                                                                && session.getAttribute("sessMonthProfileCert").toString().trim().equals("0") ? "selected" : "" %>><script>document.write(global_fm_combox_all);</script></option>
                                                            <option value="01" <%= session.getAttribute("sessMonthProfileCert") != null
                                                                && session.getAttribute("sessMonthProfileCert").toString().trim().equals("01") ? "selected" : "" %>>01</option>
                                                            <option value="02" <%= session.getAttribute("sessMonthProfileCert") != null
                                                                && session.getAttribute("sessMonthProfileCert").toString().trim().equals("02") ? "selected" : "" %>>02</option>
                                                            <option value="03" <%= session.getAttribute("sessMonthProfileCert") != null
                                                                && session.getAttribute("sessMonthProfileCert").toString().trim().equals("03") ? "selected" : "" %>>03</option>
                                                            <option value="04" <%= session.getAttribute("sessMonthProfileCert") != null
                                                                && session.getAttribute("sessMonthProfileCert").toString().trim().equals("04") ? "selected" : "" %>>04</option>
                                                            <option value="05" <%= session.getAttribute("sessMonthProfileCert") != null
                                                                && session.getAttribute("sessMonthProfileCert").toString().trim().equals("05") ? "selected" : "" %>>05</option>
                                                            <option value="06" <%= session.getAttribute("sessMonthProfileCert") != null
                                                                && session.getAttribute("sessMonthProfileCert").toString().trim().equals("06") ? "selected" : "" %>>06</option>
                                                            <option value="07" <%= session.getAttribute("sessMonthProfileCert") != null
                                                                && session.getAttribute("sessMonthProfileCert").toString().trim().equals("07") ? "selected" : "" %>>07</option>
                                                            <option value="08" <%= session.getAttribute("sessMonthProfileCert") != null
                                                                && session.getAttribute("sessMonthProfileCert").toString().trim().equals("08") ? "selected" : "" %>>08</option>
                                                            <option value="09" <%= session.getAttribute("sessMonthProfileCert") != null
                                                                && session.getAttribute("sessMonthProfileCert").toString().trim().equals("09") ? "selected" : "" %>>09</option>
                                                            <option value="10" <%= session.getAttribute("sessMonthProfileCert") != null
                                                                && session.getAttribute("sessMonthProfileCert").toString().trim().equals("10") ? "selected" : "" %>>10</option>
                                                            <option value="11" <%= session.getAttribute("sessMonthProfileCert") != null
                                                                && session.getAttribute("sessMonthProfileCert").toString().trim().equals("11") ? "selected" : "" %>>11</option>
                                                            <option value="12" <%= session.getAttribute("sessMonthProfileCert") != null
                                                                && session.getAttribute("sessMonthProfileCert").toString().trim().equals("12") ? "selected" : "" %>>12</option>
                                                        </select>
                                                        <%
                                                            if(session.getAttribute("sessMonthProfileCert") == null)
                                                            {
                                                        %>
                                                            <script type="text/javascript">
                                                                    $("#idMounth").val('<%=CommonFunction.getMonthCurrentForSearch()%>');
                                                            </script>
                                                            <%
                                                            }
                                                        %>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-sm-4" style="padding-left: 0;">
                                                <div class="form-group">
                                                    <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;">
                                                        <script>document.write(global_fm_year);</script>
                                                    </label>
                                                    <div class="col-sm-7" style="padding-right: 0px;">
                                                        <select name="idYear" id="idYear" class="form-control123">
                                                            <%
                                                                for(int i=18; i<68; i++) {
                                                                    String sYearAfter = String.valueOf(i);
                                                                    if(sYearAfter.length() == 1)
                                                                    {
                                                                        sYearAfter = "0" + sYearAfter;
                                                                    }
                                                                    String sYearAfterID = "20" + sYearAfter;
                                                            %>
                                                            <option value="<%=sYearAfterID%>" <%= session.getAttribute("sessYearProfileCert") != null
                                                                && session.getAttribute("sessYearProfileCert").toString().trim().equals(sYearAfterID) ? "selected" : "" %>><%=sYearAfterID%></option>
                                                            <%
                                                                }
                                                            %>
                                                        </select>
                                                        <%
                                                            if(session.getAttribute("sessYearProfileCert") == null)
                                                            {
                                                        %>
                                                            <script type="text/javascript">
                                                                $("#idYear").val('<%=CommonFunction.getYearCurrentForSearch()%>');
                                                            </script>
                                                            <%
                                                            }
                                                        %>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-sm-4" style="padding-left: 0;">
                                                <div class="form-group">
                                                    <label id="idLblTitleStateProfile" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;padding-left: 0;"></label>
                                                    <div class="col-sm-7" style="padding-right: 0px;">
                                                        <select name="STATE_PROFILE_SEARCH" id="STATE_PROFILE_SEARCH" class="form-control123">
                                                            <option value="<%= Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL%>" <%= session.getAttribute("sessStateProfileProfileCert") != null && Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(session.getAttribute("sessStateProfileProfileCert").toString()) ? "selected" : ""%>><script>document.write(global_fm_combox_all);</script></option>
                                                            <%
                                                                FILE_MANAGER_STATE[][] rsStatus = new FILE_MANAGER_STATE[1][];
                                                                db.S_BO_FILE_MANAGER_STATE_COMBOBOX(sessLanguageGlobal, rsStatus);
                                                                if (rsStatus[0].length > 0) {
                                                                    for (FILE_MANAGER_STATE temp1 : rsStatus[0]) {
                                                            %>
                                                            <option value="<%=String.valueOf(temp1.ID)%>" <%= session.getAttribute("sessStateProfileProfileCert") != null
                                                                && session.getAttribute("sessStateProfileProfileCert").toString().trim().equals(String.valueOf(temp1.ID)) ? "selected" : "" %>><%=temp1.REMARK%></option>
                                                            <%
                                                                    }
                                                                }
                                                            %>
                                                        </select>
                                                    </div>
                                                </div>
                                                <script>$("#idLblTitleStateProfile").text(global_fm_status_profile);</script>
                                            </div>
                                            
                                            <div class="form-group" style="padding: 0px 0px 0px 0px;margin: 0;">
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;"><script>document.write(global_fm_FromDate_profile);</script></label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <input type="Text" id="demo1" name="FromCreateDate" <%= session.getAttribute("AlertAllTimeSProfileCert") != null && "1".equals(session.getAttribute("AlertAllTimeSProfileCert").toString()) ? "disabled" : ""%>
                                                                value="<%= session.getAttribute("sessFromReceiveDateProfileCert") != null && !"1".equals(session.getAttribute("AlertAllTimeSProfileCert").toString()) ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessFromReceiveDateProfileCert").toString()) : com.ConvertMonthSub(30)%>"
                                                                maxlength="25" class="form-control123"/>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;"><script>document.write(global_fm_ToDate_profile);</script></label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <input type="Text" id="demo2" name="ToCreateDate" <%= session.getAttribute("AlertAllTimeSProfileCert") != null && "1".equals(session.getAttribute("AlertAllTimeSProfileCert").toString()) ? "disabled" : ""%>
                                                                value="<%= session.getAttribute("sessToReceiveDateProfileCert") != null && !"1".equals(session.getAttribute("AlertAllTimeSProfileCert").toString()) ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessToReceiveDateProfileCert").toString()) : com.ConvertMonthSub(0)%>"
                                                                maxlength="25" class="form-control123"/>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;"><script>document.write(global_fm_check_date_profile);</script></label>
                                                        <div class="col-sm-7" style="padding-right: 0px;padding-top: 7px; text-align: left;">
                                                            <label class="switch" for="idCheck">
                                                                <input type="checkbox" name="nameCheck" id="idCheck" onchange="checkboxChange();" <%= session.getAttribute("AlertAllTimeSProfileCert") != null && "1".equals(session.getAttribute("AlertAllTimeSProfileCert").toString()) ? "" : "checked" %>/>
                                                                <div class="slider round"></div>
                                                            </label>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-sm-4" style="padding-left: 0;">
                                                <div class="form-group">
                                                    <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;">
                                                        <script>document.write(global_fm_grid_company);</script>
                                                    </label>
                                                    <div class="col-sm-7" style="padding-right: 0px;">
                                                        <input type="text" id="COMPANY_NAME" name="COMPANY_NAME" maxlength="150" value="<%= session.getAttribute("sessCOMPANY_NAMEProfileCert") != null ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessCOMPANY_NAMEProfileCert").toString()) : ""%>"
                                                            class="form-control123">
                                                    </div>
                                                </div>
                                            </div>
                                            <%
                                                PREFIX_UUID[][] rsPrefix;
                                                if(isUIDCollection.equals("1")) {
                                            %>
                                            <div class="col-sm-4" style="padding-left: 0;">
                                                <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;padding-right: 5px;">
                                                    <script>document.write(global_fm_identifier_type);</script>
                                                </label>
                                                <div class="col-sm-7" style="padding-right: 0px;">
                                                    <select name="ENTERPRISE_PREFIX" id="ENTERPRISE_PREFIX" onclick="changeEnterprise(this.value);" class="form-control123" style="text-align: left;">
                                                        <%
                                                            rsPrefix = new PREFIX_UUID[1][];
                                                            dbTwo.S_BO_PREFIX_UUID_COMBOBOX("ENTERPRISE", sessLanguageGlobal, rsPrefix);
                                                            if (rsPrefix[0].length > 0) {
                                                                for (PREFIX_UUID temp1 : rsPrefix[0]) {
                                                                    if("".equals(isLoadEnterprisePrefix)){
                                                                        isLoadEnterprisePrefix = temp1.PREFIX_DB;
                                                                    }
                                                        %>
                                                        <option value="<%=temp1.PREFIX_DB%>" <%= session.getAttribute("sessENTERPRISE_PREFIXProfileCert") != null && temp1.PREFIX_DB.equals(session.getAttribute("sessENTERPRISE_PREFIXProfileCert").toString()) ? "selected" : ""%>><%=temp1.REMARK%></option>
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
                                                        <input type="text" id="ENTERPRISE_ID" name="ENTERPRISE_ID" maxlength="22" value="<%= session.getAttribute("sessENTERPRISE_IDProfileCert") != null ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessENTERPRISE_IDProfileCert").toString()) : ""%>"
                                                            class="form-control123">
                                                    </div>
                                                </div>
                                                <script>
                                                    function changeEnterprise() {
                                                        $("#idLblTooltipEnterprise").text(global_fm_enter + $("#ENTERPRISE_PREFIX option:selected").text());
                                                    }
                                                </script>
                                            </div>
                                            <%
                                                } else {
                                            %>
                                            <div class="col-sm-4" style="padding-left: 0;">
                                                <div class="form-group">
                                                    <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;">
                                                        <script>document.write(global_fm_MST);</script>
                                                    </label>
                                                    <div class="col-sm-7" style="padding-right: 0px;">
                                                        <input type="text" name="TAX_CODE" maxlength="25" value="<%= session.getAttribute("sessTAX_CODEProfileCert") != null ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessTAX_CODEProfileCert").toString()) : ""%>"
                                                            class="form-control123">
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-sm-4" style="padding-left: 0;">
                                                <div class="form-group">
                                                    <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;">
                                                        <script>document.write(global_fm_MNS);</script>
                                                    </label>
                                                    <div class="col-sm-7" style="padding-right: 0px;">
                                                        <input type="text" name="BUDGET_CODE" maxlength="25" value="<%= session.getAttribute("sessBUDGET_CODEProfileCert") != null ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessBUDGET_CODEProfileCert").toString()) : ""%>"
                                                            class="form-control123">
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-sm-4" style="padding-left: 0;">
                                                <div class="form-group">
                                                    <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;">
                                                        <script>document.write(global_fm_decision);</script>
                                                    </label>
                                                    <div class="col-sm-7" style="padding-right: 0px;">
                                                        <input type="text" name="DECISION" maxlength="25" value="<%= session.getAttribute("sessDECISIONProfileCert") != null ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessDECISIONProfileCert").toString()) : ""%>"
                                                            class="form-control123">
                                                    </div>
                                                </div>
                                            </div>
                                            <%
                                                }
                                            %>
                                            <div class="col-sm-4" style="padding-left: 0;">
                                                <div class="form-group">
                                                    <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;">
                                                        <script>document.write(global_fm_grid_personal);</script>
                                                    </label>
                                                    <div class="col-sm-7" style="padding-right: 0px;">
                                                        <input type="text" id="PERSONAL_NAME" name="PERSONAL_NAME" maxlength="150" value="<%= session.getAttribute("sessPERSONAL_NAMEProfileCert") != null ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessPERSONAL_NAMEProfileCert").toString()) : ""%>"
                                                            class="form-control123">
                                                    </div>
                                                </div>
                                            </div>
                                            <%
                                                if(isUIDCollection.equals("1")) {
                                            %>
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
                                                        <option value="<%=temp1.PREFIX_DB%>" <%= session.getAttribute("sessPERSONAL_PREFIXProfileCert") != null && temp1.PREFIX_DB.equals(session.getAttribute("sessPERSONAL_PREFIXProfileCert").toString()) ? "selected" : ""%>><%=temp1.REMARK%></option>
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
                                                        <input type="text" id="PERSONAL_ID" name="PERSONAL_ID" maxlength="22" value="<%= session.getAttribute("sessPERSONAL_IDProfileCert") != null ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessPERSONAL_IDProfileCert").toString()) : ""%>"
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
                                            <%
                                                } else {
                                            %>
                                            <div class="col-sm-4" style="padding-left: 0;">
                                                <div class="form-group">
                                                    <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;">
                                                        <script>document.write(global_fm_CMND);</script>
                                                    </label>
                                                    <div class="col-sm-7" style="padding-right: 0px;">
                                                        <input type="text" name="P_ID" maxlength="25" value="<%= session.getAttribute("sessP_IDProfileCert") != null ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessP_IDProfileCert").toString()) : ""%>"
                                                            class="form-control123">
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-sm-4" style="padding-left: 0;">
                                                <div class="form-group">
                                                    <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;">
                                                        <script>document.write(global_fm_CitizenId);</script>
                                                    </label>
                                                    <div class="col-sm-7" style="padding-right: 0px;">
                                                        <input type="text" name="CCCD" maxlength="25" value="<%= session.getAttribute("sessCCCDProfileCert") != null ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessCCCDProfileCert").toString()) : ""%>"
                                                            class="form-control123">
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-sm-4" style="padding-left: 0;">
                                                <div class="form-group">
                                                    <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;">
                                                        <script>document.write(global_fm_HC);</script>
                                                    </label>
                                                    <div class="col-sm-7" style="padding-right: 0px;">
                                                        <input type="text" name="PASSPORT" maxlength="25" value="<%= session.getAttribute("sessPASSPORTProfileCert") != null ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessPASSPORTProfileCert").toString()) : ""%>"
                                                            class="form-control123">
                                                    </div>
                                                </div>
                                            </div>
                                            <%
                                                }
                                            %>
                                            <div class="col-sm-4" style="padding-left: 0;">
                                                <div class="form-group">
                                                    <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;">
                                                        <script>document.write(global_fm_status_control);</script>
                                                    </label>
                                                    <div class="col-sm-7" style="padding-right: 0px;">
                                                        <select name="idCollectEnabled" id="idCollectEnabled" class="form-control123">
                                                            <option value="<%= Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL%>" <%= session.getAttribute("sessCollectEnabledProfileCert") != null && Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(session.getAttribute("sessCollectEnabledProfileCert").toString()) ? "selected" : ""%>><script>document.write(global_fm_combox_all);</script></option>
                                                            <option value="0" <%= session.getAttribute("sessCollectEnabledProfileCert") != null
                                                                && session.getAttribute("sessCollectEnabledProfileCert").toString().trim().equals("0") ? "selected" : "" %>><script>document.write(profile_fm_unenoughed);</script></option>
                                                            <option value="1" <%= session.getAttribute("sessCollectEnabledProfileCert") != null
                                                                && session.getAttribute("sessCollectEnabledProfileCert").toString().trim().equals("1") ? "selected" : "" %>><script>document.write(profile_fm_enoughed);</script></option>
                                                        </select>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-sm-4" style="padding-left: 0;">
                                                <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;padding-left: 0;">
                                                    <script>document.write(collation_fm_unapproved_profile);</script>
                                                </label>
                                                <div class="col-sm-7" style="padding-right: 0px;">
                                                    <label class="switch" for="idCheckCommitEnabled" style="margin-bottom: 0;">
                                                        <input type="checkbox" name="idCheckCommitEnabled" id="idCheckCommitEnabled"
                                                            <%= session.getAttribute("sessCommitEnabledCert") != null && "1".equals(session.getAttribute("sessCommitEnabledCert").toString()) ? "checked" : ""%> />
                                                        <div id="idCheckCollectEnabledClass" class="slider round"></div>
                                                    </label>
                                                </div>
                                            </div>
                                            <div class="col-sm-4" style="padding-left: 0;">
                                                <div class="form-group">
                                                    <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;">
                                                        <script>document.write(global_fm_sign_confirmation);</script>
                                                    </label>
                                                    <div class="col-sm-7" style="padding-right: 0px;">
                                                        <select name="CONFIRM_RESIGN_SEARCH" id="CONFIRM_RESIGN_SEARCH" class="form-control123">
                                                            <option value="<%= Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL%>" <%= session.getAttribute("sessResignProfileCert") != null && Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(session.getAttribute("sessResignProfileCert").toString()) ? "selected" : ""%>><script>document.write(global_fm_combox_all);</script></option>
                                                            <option value="1" <%= session.getAttribute("sessResignProfileCert") != null
                                                                && session.getAttribute("sessResignProfileCert").toString().trim().equals("1") ? "selected" : "" %>><script>document.write(global_cbx_wait_sign_confirmation);</script></option>
                                                            <option value="0" <%= session.getAttribute("sessResignProfileCert") != null
                                                                && session.getAttribute("sessResignProfileCert").toString().trim().equals("0") ? "selected" : "" %>><script>document.write(global_cbx_sign_confirmation);</script></option>
                                                        </select>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-sm-4" style="padding-left: 0;display: <%= !isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_NC) ? "none" : "" %>;">
                                                <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;">
                                                    <script>document.write(global_fm_Status_signed);</script>
                                                </label>
                                                <div class="col-sm-7" style="padding-right: 0px;">
                                                    <select name="idSignedEnabled" id="idSignedEnabled" class="form-control123">
                                                        <option value="<%= Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL%>" <%= session.getAttribute("sessSignedEnabledProfileCert") != null
                                                            && Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(session.getAttribute("sessSignedEnabledProfileCert").toString()) ? "selected" : ""%>><script>document.write(global_fm_combox_all);</script></option>
                                                        <option value="0" <%= session.getAttribute("sessSignedEnabledProfileCert") != null
                                                            && session.getAttribute("sessSignedEnabledProfileCert").toString().trim().equals("0") ? "selected" : "" %>><script>document.write(global_fm_unapply_signed);</script></option>
                                                        <option value="1" <%= session.getAttribute("sessSignedEnabledProfileCert") != null
                                                            && session.getAttribute("sessSignedEnabledProfileCert").toString().trim().equals("1") ? "selected" : "" %>><script>document.write(global_fm_apply_signed);</script></option>
                                                    </select>
                                                </div>
                                            </div>
                                            <div class="col-sm-4" style="padding-left: 0;<%= isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA) && SessRoleID.equals(Definitions.CONFIG_ROLE_ID_CA_SURVEYOR) ? "display:none;" : "" %>">
                                                <div class="form-group">
                                                    <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;">
                                                        <script>document.write(global_fm_Branch);</script>
                                                    </label>
                                                    <div class="col-sm-7" style="padding-right: 0px;">
                                                        <select name="BranchOffice" id="idBranchOffice" class="form-control123">
                                                            <option value="<%= Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL%>" <%= session.getAttribute("sessBranchOfficeProfileCert") != null && Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(session.getAttribute("sessBranchOfficeProfileCert").toString()) ? "selected" : ""%>><script>document.write(global_fm_combox_all);</script></option>
                                                            <%
                                                                try {
                                                                    BRANCH[][] rst = (BRANCH[][]) session.getAttribute("sessTreeBranchSystem");
                                                                    if (rst[0].length > 0) {
                                                                        for (BRANCH temp1 : rst[0]) {
                                                            %>
                                                            <option value="<%=String.valueOf(temp1.ID)%>"  <%= session.getAttribute("sessBranchOfficeProfileCert") != null && String.valueOf(temp1.ID).equals(session.getAttribute("sessBranchOfficeProfileCert").toString()) ? "selected" : ""%>><%=temp1.NAME + " - " + temp1.REMARK%></option>
                                                            <%
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
                                                </div>
                                            </div>
                                            <%
                                                String sViewAgencySearch = "";
                                                String sViewDivButton = "clear: both;";
                                                String sViewDivHidden = "";
                                                if (!Definitions.CONFIG_AGENT_ROOT.equals(SessAgentID)) {
                                                    sViewAgencySearch = "none";sViewDivButton = "";sViewDivHidden = "display: none;";
                                                }
                                            %>
                                            <div class="col-sm-4" style="padding-left: 0; display: <%= sViewAgencySearch%>">
                                                <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;padding-left: 0;">
                                                    <script>document.write(collation_alert_type_compensation);</script>
                                                </label>
                                                <div class="col-sm-7" style="padding-right: 0px;">
                                                    <label class="switch" for="idCheckCompensation" style="margin-bottom: 0;">
                                                        <input type="checkbox" name="idCheckCompensation" onclick="onCheckCompensation();" id="idCheckCompensation"
                                                            <%= session.getAttribute("sessCompensationProfileCert") != null && "1".equals(session.getAttribute("sessCompensationProfileCert").toString()) ? "checked" : ""%> />
                                                        <div id="idCheckCompensationClass" class="slider round"></div>
                                                    </label>
                                                </div>
                                                <script>
                                                    function onCheckCompensation()
                                                    {
                                                        if ($("#idCheckCompensation").is(':checked'))
                                                        {
                                                            $("#idSearchViewBriefType").css("display", "");
//                                                            $("#idDivCheckCompensation").css("display", "none");
                                                        } else {
                                                            $('#idCheckOverdue').prop('checked', false);
                                                            $("#idSearchViewBriefType").css("display", "none");
//                                                            $("#idDivCheckCompensation").css("display", "");
                                                        }
                                                    }
                                                </script>
                                            </div>
                                            <%
                                                String sViewBriefType = "none";
                                                if(session.getAttribute("sessCompensationProfileCert") != null)
                                                {
                                                    if(session.getAttribute("sessCompensationProfileCert").toString().trim().equals("1"))
                                                    {
                                                        sViewBriefType = "";
                                                    }
                                                }
                                            %>
                                            <div class="col-sm-4" style="padding-left: 0;display: <%= sViewBriefType%>;" id="idSearchViewBriefType">
                                                <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;padding-left: 0;">
                                                    <script>document.write(collation_fm_profile_overdue);</script>
                                                </label>
                                                <div class="col-sm-7" style="padding-right: 0px;">
                                                    <label class="switch" for="idCheckOverdue" style="margin-bottom: 0;">
                                                        <input type="checkbox" name="idCheckOverdue" id="idCheckOverdue"
                                                            <%= session.getAttribute("sessOverdueProfileCert") != null && "1".equals(session.getAttribute("sessOverdueProfileCert").toString()) ? "checked" : ""%> />
                                                        <div id="idCheckOverdueClass" class="slider round"></div>
                                                    </label>
                                                </div>
                                            </div>
                                            <div class="col-sm-4" id="idDivHiden1" style="<%=sViewDivButton%> <%=sViewDivHidden%>"><div class="form-group"></div></div>
                                            <div class="col-sm-4" id="idDivButtonSearch" style="padding-left: 0;">
                                                <div class="form-group">
                                                <label class="control-label col-sm-5">
                                                </label>
                                                <div class="col-sm-7" style="padding-right: 0px;">
                                                    <button type="button" class="btn btn-info" onClick="searchForm('<%=anticsrf%>');"><script>document.write(global_fm_button_search);</script></button>
                                                </div>
                                                </div>
                                            </div>
                                            <%
                                                if (Definitions.CONFIG_AGENT_ROOT.equals(SessAgentID)) {
                                            %>
                                            <div class="form-group" style="text-align: right;padding-right: 12px;">
                                                <label id="idLblTitleLoadProfileContact" class="control-label" style="color: #000000; font-weight: bold;text-align: right;"></label>
                                                <a id="idAShowList" style="cursor: pointer; color: blue; text-decoration: underline;" onclick="ShowDialog();"><script>document.write(error_content_link_out);</script></a>
                                                <script>
                                                    $("#idLblTitleLoadProfileContact").text(profile_title_import_list);
                                                </script>
                                            </div>
                                            <%
                                                }
                                            %>
                                            <input type="hidden" name="CsrfToken" id="CsrfToken" value="<%=anticsrf%>"/>
                                            <input type="hidden" id="tempCsrfToken" name="tempCsrfToken"/>
                                            <input id="idHiddenLoad" name="nameHiddenLoad" value="" type="hidden"/>
                                        </form>
                                    </div>
                                </div>
                                <%
                                    if (status == 1 && statusLoad == 1) {
                                %>
                                <div class="x_panel" style="margin-top: 0px;">
                                    <div class="x_title" style="border-bottom: 0 solid #E6E9ED;margin-bottom: 0px;">
                                        <h2>
                                            <i class="fa fa-list-ul"></i> <script>document.write(reportneac_title_table);</script>
                                        </h2>
                                        <ul class="nav navbar-right panel_toolbox">
                                            <li style="color: red;font-weight: bold;">
                                                <%
                                                    if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA) && SessRoleID.equals(Definitions.CONFIG_ROLE_ID_CA_SURVEYOR))
                                                    {} else {
                                                %>
                                                <script>document.write(global_label_grid_sum);</script><%= strMess%>
                                                <%
                                                    }
                                                %>
                                                <%
                                                    boolean isExportEnabled = false;
                                                    if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA))
                                                    {
                                                        isExportEnabled = true;
                                                    } else {
                                                        if (Definitions.CONFIG_AGENT_ROOT.equals(SessAgentID)) {
                                                            isExportEnabled = true;
                                                        }
                                                    }
                                                    if (isExportEnabled == true) {
                                                %>
                                                <%
                                                    if(!isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA)){
                                                        if(SessRoleID.equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN) || SessRoleID.equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN_CHILD))
                                                        {
                                                %>
                                                &nbsp;&nbsp;
                                                <button type="button" class="btn btn-info" onClick="exportBriefProfile('<%= iTotRslts%>');"><script>document.write(global_fm_button_export_csv);</script></button>
                                                <%
                                                        }
                                                    } else {
                                                        if(!SessRoleID.equals(Definitions.CONFIG_ROLE_ID_CA_SURVEYOR)) {
                                                %>
                                                &nbsp;&nbsp;
                                                <button type="button" class="btn btn-info" onClick="exportBriefProfile('<%= iTotRslts%>');"><script>document.write(global_fm_button_export_csv);</script></button>
                                                <%
                                                        }
                                                    }
                                                %>
                                                <%
                                                    if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA) && SessRoleID.equals(Definitions.CONFIG_ROLE_ID_CA_SURVEYOR))
                                                    {} else {
                                                %>
                                                &nbsp;&nbsp;
                                                <button type="button" class="btn btn-info" onClick="updateBriefProfile('<%= iTotRslts%>');"><script>document.write(global_fm_button_profile_pettlement);</script></button>
                                                <%
                                                    }
                                                %>
                                                <script>
                                                    function exportBriefProfile(vSum)
                                                    {
                                                       $('body').append('<div id="over"></div>');
                                                        $(".loading-gif").show();
                                                        $.ajax({
                                                            type: "post",
                                                            url: "../ExportCSVParam",
                                                            data: {
                                                                idParam: "exportprofilemanagerlist",
                                                                vSum: vSum
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
                                                    function updateBriefProfile(vSum)
                                                    {
                                                        $('body').append('<div id="over"></div>');
                                                        $(".loading-gif").show();
                                                        var servletParam = "exportprofilecontrollist";
                                                        if(IsWhichCA === JS_IS_WHICH_ABOUT_CA_HILO) {
                                                            servletParam = "exportprofilecontrollist_20";
                                                        }
                                                        $.ajax({
                                                            type: "post",
                                                            url: "../ExportCSVPhaseTwo",
                                                            data: {
                                                                idParam: servletParam,
                                                                vSum: vSum
                                                            },
                                                            cache: false,
                                                            success: function (html)
                                                            {
                                                                var myStrings = sSpace(html).split('#');
                                                                if (myStrings[0] === "0")
                                                                {
                                                                    var f = document.form;
                                                                    f.method = "post";
                                                                    f.action = '../DownFromSaveFile?idParam=downfileexportquick&name=' + myStrings[2];
                                                                    f.submit();
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
                                                    function updateBriefProfile_bk(vSum)
                                                    {
                                                        $('body').append('<div id="over"></div>');
                                                        $(".loading-gif").show();
                                                        $.ajax({
                                                            type: "post",
                                                            url: "../ProfileCommon",
                                                            data: {
                                                                idParam: 'edittypeprofile',
                                                                vSum: vSum
                                                            },
                                                            cache: false,
                                                            success: function (html)
                                                            {
                                                                var myStrings = sSpace(html).split('#');
                                                                if (myStrings[0] === "0")
                                                                {
                                                                    var vMessage = inputcertlist_succ_edit + ".\n"
                                                                        + collation_alert_type_inmounth + ": " + myStrings[1] + ".\n"
                                                                        + collation_alert_type_compensation + ": " + myStrings[2] + ".\n"
                                                                        + collation_fm_profile_overdue + ": " + myStrings[3];
                                                                    funSuccLocalAlert(vMessage);
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
                                        <%
                                            if(rsPginToken != null && rsPginToken[0].length > 0) {
                                        %>
                                        <div class="table-responsive">
                                            <table id="idTableList" class="table table-bordered table-striped projects">
                                                <thead>
                                                <th><script>document.write(global_fm_action);</script></th>
                                                <th><script>document.write(global_fm_STT);</script></th>
                                                <th><script>document.write(global_fm_user_create);</script></th>
                                                <%
                                                    if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_NC)) {
                                                %>
                                                <th><script>document.write(token_fm_agent);</script></th>
                                                <%
                                                    }
                                                %>
                                                <th><script>document.write(global_fm_enterprise_id);</script></th>
                                                <th><script>document.write(global_fm_grid_company);</script></th>
                                                <th><script>document.write(global_fm_personal_id);</script></th>
                                                <th><script>document.write(global_fm_grid_personal);</script></th>
                                                <th><script>document.write(global_fm_duration_cts);</script></th>
                                                <th><script>document.write(cert_fm_type_request);</script></th>
                                                <%
                                                    if(!isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_NC)) {
                                                %>
                                                <th><script>document.write(global_fm_date_gencert);</script></th>
                                                <% } %>
                                                <%
                                                    if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_NC)) {
                                                %>
                                                <th><script>document.write(global_fm_date_create);</script></th>
                                                <th><script>document.write(global_fm_valid_cert);</script></th>
                                                <% } %>
                                                <th><script>document.write(collation_fm_mounth);</script></th>
                                                <%
                                                    if(!isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_NC)) {
                                                %>
                                                <th><script>document.write(global_fm_Status);</script></th>
                                                <% } %>
                                                <!--<th><script>document.write(collation_fm_date_receipt);</script></th>-->
                                                <th><script>document.write(global_fm_status_profile);</script></th>
                                                <th><script>document.write(collation_fm_print_DK);</script></th>
                                                <th><script>document.write(collation_fm_print_Confirm);</script></th>
                                                <th><script>document.write(collation_fm_print_GPKD);</script></th>
                                                <th><script>document.write(collation_fm_print_CMND);</script></th>
                                                <th><script>document.write(global_fm_serial);</script></th>
                                                <th><script>document.write(token_fm_tokenid);</script></th>
                                                <th><script>document.write(global_fm_Note_offset);</script></th>
                                                </thead>
                                                <tbody>
                                                    <%
                                                        int j = 1;
                                                        if (iPaNoSS > 1) {
                                                            j = ((iPaNoSS - 1) * iSwRws) + 1;
                                                        }
                                                        if (rsPginToken[0].length > 0) {
                                                            for (CERTIFICATION temp1 : rsPginToken[0]) {
                                                                String strID = String.valueOf(temp1.ID);
                                                                String sCOLLECT_ENABLED = temp1.COLLECT_ENABLED ? "1" : "0";
                                                                String sRegister = "0";
                                                                String sConfirm = "0";
                                                                String sDKKD = "0";
                                                                String sCMND = "0";
                                                                String sBRIEF_PROPERTIES = temp1.BRIEF_PROPERTIES;
                                                                if(!"".equals(sBRIEF_PROPERTIES))
                                                                {
                                                                    CERTIFICATION_POLICY_DATA[][] resIPData = new CERTIFICATION_POLICY_DATA[1][];
                                                                    CommonFunction.getCollectedBriefProperties(sBRIEF_PROPERTIES, resIPData);
                                                                    if(resIPData[0].length > 0) {
                                                                        boolean bRegister = CommonFunction.checkBriefFileType(Definitions.CONFIG_FILE_PROFILE_SERVICE_REGISTRATION_DOCUMENT, resIPData);
                                                                        if(bRegister == true){sRegister = "1";}
                                                                        boolean bConfirm = CommonFunction.checkBriefFileType(Definitions.CONFIG_FILE_PROFILE_MINUTES_OF_HANDOVER, resIPData);
                                                                        if(bConfirm == true){sConfirm = "1";}
                                                                        boolean bDKKD = CommonFunction.checkBriefFileType(Definitions.CONFIG_FILE_PROFILE_PHOTO_ACTIVITY_DECLARATION, resIPData);
                                                                        if(bDKKD == true){sDKKD = "1";}
                                                                        boolean bCMND = CommonFunction.checkBriefFileType(Definitions.CONFIG_FILE_PROFILE_PHOTO_ID_CARD, resIPData);
                                                                        if(bCMND == true){sCMND = "1";}
                                                                    }
                                                                }
                                                    %>
                                                    <tr>
                                                        <td>
                                                            <a style="cursor: pointer;" onclick="popupDetailCert('<%=strID%>');" class="btn btn-info btn-xs"><i class="fa fa-pencil"></i> <script>document.write(global_fm_button_detail);</script></a>
                                                        </td>
                                                        <td style="text-align: center;"><%= com.convertMoney(j)%></td>
                                                        <td><%= EscapeUtils.CheckTextNull(temp1.CREATED_BY)%></td>
                                                        <%
                                                            if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_NC)) {
                                                        %>
                                                        <td><%= "[" + temp1.BRANCH_NAME + "] " + temp1.BRANCH_DESC%></td>
                                                        <%
                                                            }
                                                        %>
                                                        <td><a style="color: blue;" data-toggle="tooltipPrefix" title="<%= temp1.ENTERPRISE_ID_REMARK%>"><%= temp1.ENTERPRISE_ID%></a></td>
                                                        <td><%= EscapeUtils.CheckTextNull(temp1.COMPANY_NAME)%></td>
                                                        <td><a style="color: blue;" data-toggle="tooltipPrefix" title="<%= temp1.PERSONAL_ID_REMARK%>"><%= temp1.PERSONAL_ID%></a></td>
                                                        <td><%= EscapeUtils.CheckTextNull(temp1.PERSONAL_NAME)%></td>
                                                        <td><%= EscapeUtils.CheckTextNull(temp1.CERTIFICATION_PROFILE_NAME)%></td>
                                                        <td><%= temp1.CERTIFICATION_ATTR_TYPE_DESC%></td>
                                                        <%
                                                            if(!isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_NC)) {
                                                        %>
                                                        <td><%= EscapeUtils.CheckTextNull(temp1.ISSUED_DT)%></td>
                                                        <% } %>
                                                        <%
                                                            if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_NC)) {
                                                        %>
                                                        <td><%= EscapeUtils.CheckTextNull(temp1.CREATED_DT)%></td>
                                                        <td><%= EscapeUtils.CheckTextNull(temp1.EFFECTIVE_DT)%></td>
                                                        <% } %>
                                                        <td><%= EscapeUtils.CheckTextNull(temp1.CROSS_CHECKED_MOUNTH)%></td>
                                                        <%
                                                            if(!isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_NC)) {
                                                        %>
                                                        <td>
                                                            <script>
                                                                var vCOLLECT_ENABLED = '<%=sCOLLECT_ENABLED%>';
                                                                if(vCOLLECT_ENABLED === '1') {
                                                                    document.write(profile_fm_enoughed);
                                                                } else {
                                                                    document.write(profile_fm_unenoughed);
                                                                }
                                                            </script>
                                                        </td>
                                                        <% } %>
                                                        <!--<td><= EscapeUtils.CheckTextNull(temp1.COLLECTED_BRIEF_DT)%></td>-->
                                                        <td><%= EscapeUtils.CheckTextNull(temp1.FILE_MANAGER_STATE_DESC)%>
<!--                                                            <script>
                                                                var vBRIEF_TYPE = '<=sBRIEF_TYPE%>';
                                                                if(vBRIEF_TYPE === '1') {
                                                                    document.write(collation_fm_profile_overdue);
                                                                } else {
                                                                    document.write(collation_fm_type_inmounth);
                                                                }
                                                            </script>-->
                                                        </td>
                                                        <td>
                                                            <script>
                                                                var vRegister = '<%=sRegister%>';
                                                                if(vRegister === '1') {
                                                                    document.write(global_fm_active_true);
                                                                } else {
                                                                    document.write(global_fm_active_false);
                                                                }
                                                            </script>
                                                        </td>
                                                        <td>
                                                            <script>
                                                                var vConfirm = '<%=sConfirm%>';
                                                                if(vConfirm === '1') {
                                                                    document.write(global_fm_active_true);
                                                                } else {
                                                                    document.write(global_fm_active_false);
                                                                }
                                                            </script>
                                                        </td>
                                                        <td>
                                                            <script>
                                                                var vDKKD = '<%=sDKKD%>';
                                                                if(vDKKD === '1') {
                                                                    document.write(global_fm_active_true);
                                                                } else {
                                                                    document.write(global_fm_active_false);
                                                                }
                                                            </script>
                                                        </td>
                                                        <td>
                                                            <script>
                                                                var vCMND = '<%=sCMND%>';
                                                                if(vCMND === '1') {
                                                                    document.write(global_fm_active_true);
                                                                } else {
                                                                    document.write(global_fm_active_false);
                                                                }
                                                            </script>
                                                        </td>
                                                        <td><%= temp1.CERTIFICATION_SN%></td>
                                                        <td><%= EscapeUtils.CheckTextNull(temp1.TOKEN_SN)%></td>
                                                        <td><%= temp1.PROFILE_NOTE%></td>
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
                                                    <tr id="idPagingTabel">
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
                                                                        cPge = ((int) (Math.ceil((double) iEnRsNo / (iTotSrhRcrds * iSwRws))));
                                                                        int prePageNo = (cPge * iTotSrhRcrds) - ((iTotSrhRcrds - 1) + iTotSrhRcrds);
                                                                        if ((cPge * iTotSrhRcrds) - (iTotSrhRcrds) > 0) {
                                                                %>
                                                                <a href="?iPagNo=<%=prePageNo%>&cPagNo=<%=prePageNo%>"><< <script>document.write(global_paging_Before);</script></a>
                                                                &nbsp;
                                                                <%
                                                                    }
                                                                    for (i = ((cPge * iTotSrhRcrds) - (iTotSrhRcrds - 1)); i <= (cPge * iTotSrhRcrds); i++) {
                                                                        if (i == ((iPagNo / iSwRws) + 1)) {
                                                                %>
                                                                <a href="?iPagNo=<%=i%>" style="cursor:pointer;color:red;"><b><%=i%></b></a>
                                                                        <%
                                                                        } else if (i <= iTotPags) {
                                                                        %>
                                                                &nbsp;<a href="?iPagNo=<%=i%>"><%=i%></a>
                                                                <%
                                                                        }
                                                                    }
                                                                    if (iTotPags > iTotSrhRcrds && i <= iTotPags) {
                                                                %>
                                                                &nbsp;
                                                                <a href="?iPagNo=<%=i%>&cPagNo=<%=i%>">>> <script>document.write(global_paging_last);</script></a>
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
                                        <%
                                            } else {
                                        %>
                                        <div class="x_content" style="text-align: center;">
                                            <span style="color: red; font-size: 15px;"><script>document.write(global_succ_NoResult);</script></span>
                                            <div class="clearfix"></div>
                                        </div>
                                        <%
                                            }
                                        %>
                                    </div>
                                    <!--end new-->
                                </div>
                                <div class="x_panel" id="idX_Panel_Edit" style="display: none;">
                                    <div class="x_content">
                                        <div id="contentEdit"></div>
                                    </div>
                                </div>
                                <%
                                    }
                                %>
                                <%
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
                                %>
                                <%
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
                </div>
                <%@ include file="../Modules/Footer.jsp" %>
            </div>
            <script>
                function ShowDialog()
                {
                    $('#myModalOTPHardware').modal('show');
                    $(".loading-gifHardware").hide();
                    $(".loading-gif").hide();
                    $('#over').remove();
                }
                function CloseDialog()
                {
                    $('#myModalOTPHardware').modal('hide');
                    $(".loading-gifHardware").hide();
                    $(".loading-gif").hide();
                    $('#over').remove();
                }
            </script>
            <!-- HTML Model Confirm Delete -->
            <div id="myModalOTPHardware" class="modal fade" role="dialog">
                <div style="width: 100%; text-align: center; position: fixed;z-index: 1000;top: 0; padding-top: 90px;
                     left: 0; height: 100%;" class="loading-gifHardware">
                    <img src="../Images/ajax-loader1.gif" alt="Please wait..." />
                </div>
                <div class="modal-dialog modal-800" id="myDialogOTPHardware">
                    <div class="modal-content">
                        <div class="modal-header">
                            <div style="width: 70%; float: left;">
                                <h3 class="modal-title" style="font-size: 18px;"> <span id="idLblTitleImportProfile"></span></h3>
                            </div>
                            <div style="width: 29%; float: right;text-align: right;">
                                <input type="button" id="btnUploadOK" data-switch-get="state" class="btn btn-info" onclick="calUpload();" />
                                <input type="button" id="idCloseDialog" onclick="CloseDialog();" class="btn btn-info" />
                                <script>
                                    document.getElementById("btnUploadOK").value = global_fm_button_import;
                                    document.getElementById("idCloseDialog").value = global_fm_button_close;
                                    $("#idLblTitleImportProfile").text(profile_title_import_list);
                                </script>
                            </div>
                            <script>
                                function downloadSamplePush()
                                {
                                    var f = document.formOTPHardware;
                                    f.method = "post";
                                    f.action = '../DownFromSaveFile?idParam=downfilesamplecontactprofile';
                                    f.submit();
                                }
                                function downloadErrorFile()
                                {
                                    $.ajax({
                                        type: "post",
                                        url: "../DownloadFileCSR",
                                        data: {
                                            idParam: "savefileimportcontactprofile"
                                        },
                                        catche: false,
                                        success: function (html) {
                                            var arr = sSpace(html).split('#');
                                            if (arr[0] === "0")
                                            {
                                                var f = document.formOTPHardware;
                                                f.method = "post";
                                                f.action = '../DownFromSaveFile?idParam=downfileimportpush&name=' + arr[1];
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
                                function calUpload()
                                {
                                    var input1 = document.getElementById('input-file');
                                    if (input1.value !== '')
                                    {
                                        var checkFileName = input1.value.substring(input1.value.lastIndexOf('.') + 1);
                                        if (checkFileName === "xls" || checkFileName === "xlsx" || checkFileName === "csv")
                                        {
                                            $('body').append('<div id="over"></div>');
                                            $(".loading-gif").show();
                                            file1 = input1.files[0];
                                            var data1 = new FormData();
                                            data1.append('file', file1);
                                            $.ajax({
                                                type: 'POST',
                                                url: '../ContactProfileImport',
                                                data: data1,
                                                cache: false,
                                                contentType: false,
                                                processData: false,
                                                enctype: "multipart/form-data",
                                                success: function (html) {
                                                    var arr = sSpace(html).split('###');
                                                    if (arr[0] === "0")
                                                    {
                                                        if(arr[3] !== "")
                                                        {
                                                            swal({
                                                                title: "",
                                                                text: pushimport_succ_edit + "\n"
                                                                    + global_fm_combox_success + ": " + arr[1] + token_succ_import_error + arr[2] + "\n"
                                                                    + pushimport_succ_conform_down,
                                                                imageUrl: "../Images/icon_warning.png",
                                                                imageSize: "45x45",
                                                                showCancelButton: true,
                                                                closeOnConfirm: true,
                                                                allowOutsideClick: false,
                                                                confirmButtonText: login_fm_buton_OK,
                                                                cancelButtonText: global_button_grid_cancel,
                                                                cancelButtonColor: "#199DC0"
                                                            },
                                                            function () {
                                                                downloadErrorFile();
                                                            });
                                                        } else {
                                                            var sAlert = token_succ_import + "\n" + global_fm_combox_success + ": " + arr[1];
                                                            funSuccLocalAlert(sAlert);
                                                            CloseDialog();
                                                        }
                                                    }
                                                    else if (arr[0] === JS_EX_LOGIN)
                                                    {
                                                        RedirectPageLoginNoSess(global_alert_login);
                                                    }
                                                    else if (arr[0] === JS_EX_ANOTHERLOGIN)
                                                    {
                                                        RedirectPageLoginNoSess(global_alert_another_login);
                                                    }
                                                    else
                                                    {
                                                        if (arr[1] === JS_EX_SPECIAL)
                                                        {
                                                            funErrorAlert(global_error_file_special);
                                                        }
                                                        else if (arr[1] === JS_EX_CSV_NO_TOKENID)
                                                        {
                                                            funErrorAlert(token_error_no_tokenid);
                                                        }
                                                        else if (arr[1] === JS_EX_CSV_NO_SOPIN)
                                                        {
                                                            funErrorAlert(token_error_no_sopin);
                                                        }
                                                        else
                                                        {
                                                            funErrorAlert(global_errorsql);
                                                        }
                                                    }
                                                    $(".loading-gif").hide();
                                                    $('#over').remove();
                                                }
                                            });
                                        }
                                        else
                                        {
                                            funErrorAlert(token_error_import_format);
                                        }
                                    }
                                    else
                                    {
                                        funErrorAlert(global_req_file);
                                    }
                                }
                            </script>
                        </div>
                        <div class="modal-body">
                            <form role="formAddOTPHardware" name="formOTPHardware" id="formOTPHardware">
                                <div class="form-group" style="padding: 10px 0px 0 0px;margin: 0;">
                                    <label class="control-label123" style="color: blue;" id="idLblTitleDeleteNote">
                                    </label>
                                </div>
                                <div class="form-group" style="padding: 0px;margin: 0;">
                                    <label class="control-label123"><script>document.write(global_fm_browse_file);</script></label>
                                    <input type="file" id="input-file" accept=".xlsx,.xls,.csv" style="width: 100%;"
                                        class="btn btn-default btn-file select_file">
                                </div>
                                <div class="form-group" style="padding: 0px 0px 0px 0px;">
                                    <label class="control-label"><script>document.write(token_fm_import_sample);</script></label> <a style="cursor: pointer;" onclick="return downloadSamplePush();"><script>document.write(global_fm_down);</script></a>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
            <script src="../style/bootstrap.min.js"></script>
            <script src="../style/custom.min.js"></script>
            <script src="../js/moment.min.js"></script>
            <script src="../js/daterangepicker.js"></script>
        </div>
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