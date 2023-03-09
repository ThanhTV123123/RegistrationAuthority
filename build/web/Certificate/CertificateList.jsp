<%-- 
    Document   : CertificateList
    Created on : Jun 4, 2018, 10:03:12 AM
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
            Config conf = new Config();
            String sNewRefreshJS = conf.GetPropertybyCode(Definitions.CONFIG_JS_REFRESH_STRING_RANDOM);
        %>
        <script src="../js/Language.js?t=<%=sNewRefreshJS%>"></script>
        <script src="../js/process_javajs.js?t=<%=sNewRefreshJS%>"></script>
        <link href="../style/customportal.min.css" rel="stylesheet">
        <script type="text/javascript" src="../js/jquery.js"></script>
        <link rel="stylesheet" href="../js/sweetalert.css"/>
        <script src="../js/sweetalert-dev.js"></script>
        <link href="../style/customportal.min.css" rel="stylesheet">
        <script type="text/javascript" src="../Css/GlobalAlert.js"></script>
        <title></title>
        <script language="javascript">
            changeFavicon("../");
            document.title = cert_title_list;
            $(document).ready(function () {
                localStorage.setItem("LOCAL_PARAM_CERTLIST", null);
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
                $('#myModalCertDecline').modal({
                    backdrop: 'static',
                    keyboard: true,
                    show: false
                });
                $('[data-toggle="tooltipPrefix"]').tooltip();
            });
            function reload_js(src) {
                $('script[src="' + src + '"]').remove();
            }
            function popupDetail(id)
            {
                $('body').append('<div id="over"></div>');
                $(".loading-gif").show();
                $('#contentEdit').load('CertificateApprove.jsp', {id:id}, function () {
                    $(".loading-gif").hide();
                    $('#over').remove();
                    reload_js("../style/bootstrap.min.js");
                    $('#idX_Panel_Edit').css("display", "");
                    goToByScroll("contentEdit");
                    reloadApproveFunction(id);
                });
            }
            function FormDecline(idATTR, idCSRF, idBranch, idUser) {
                swal({
                    title: "",
                    text: request_conform_delete,
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
                    $('body').append('<div id="over"></div>');
                    $(".loading-gif").show();
                    setTimeout(function () {
                        $.ajax({
                            type: "post",
                            url: "../ReqApproveDeclineCommon",
                            data: {
                                idParam: 'declinecert',
                                idATTR: idATTR,
                                CsrfToken: idCSRF
                            },
                            cache: false,
                            success: function (html) {
                                var arr = sSpace(html).split('#');
                                if (arr[0] === "0")
                                {
                                    if (arr[2] === "1")
                                    {
                                        pushNotificationDecline(idBranch, idUser);
                                    }
                                    funSuccLocalAlert(request_succ_delete);
                                }
                                else if (arr[0] === JS_EX_CSRF) {
                                    funCsrfAlert();
                                }
                                else if (arr[0] === JS_EX_LOGIN)
                                {
                                    RedirectPageLoginNoSess(global_alert_login);
                                }
                                else if (arr[0] === JS_EX_ANOTHERLOGIN)
                                {
                                    RedirectPageLoginNoSess(global_alert_another_login);
                                }
                                else if (arr[0] === JS_EX_WRONG_AGENCY) {
                                    RedirectPageLoginNoSess(global_error_wrong_agency);
                                }
                                else if (arr[0] === JS_EX_ERROR_DB) {
                                    funErrorAlert(arr[1]);
                                }
                                else if (arr[0] === JS_EX_STATUS)
                                {
                                    funErrorAlert(global_error_appove_status);
                                }
                                else if (arr[0] === JS_EX_INVALID_EXTERNAL_SYSTEM)
                                {
                                    funErrorAlert(global_error_credential_external_invalid);
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
                    }, JS_STR_ACTION_TIMEOUT);
                });
            }
            function pushNotificationDecline(idBRANCH, user) {
                var xmlhttp = new XMLHttpRequest();
                xmlhttp.open("POST", "../PushNotiRequestDecline?t="+new Date(), false);
                xmlhttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
                xmlhttp.send("name="+idBRANCH+"&user="+user);
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
                    localStorage.setItem("CountCheckDeclineMilti", "");
                    localStorage.setItem("CountCheckAllDeclineMilti", "");
                    var f = document.form;
                    f.method = "post";
                    f.action = '';
                    f.submit();
                }
            }
            function popupHideCTSList()
            {
                document.getElementById('idViewCSRList').style.display = 'none';
                document.getElementById('idAHideList').style.display = 'none';
                document.getElementById('idAShowList').style.display = '';
            }
            function popupViewCSRSList()
            {
                document.getElementById('idViewCSRList').style.display = '';
                document.getElementById('idAHideList').style.display = '';
                document.getElementById('idAShowList').style.display = 'none';
            }
            function popupDialogCertDecline(attrId, branchId, userId)
            {
                $('#myModalCertDecline').modal('show');
                $('#contentCertDecline').empty();
                $('#contentCertDecline').load('IncludeCertDecline.jsp', {attrId: attrId, branchId: branchId, userId: userId}, function () {
                });
                $(".loading-gifCertDecline").hide();
                $(".loading-gif").hide();
                $('#over').remove();
            }
            function closeDialogCertDecline()
            {
                $('#myModalCertDecline').modal('hide');
                $(".loading-gifCertDecline").hide();
                $(".loading-gif").hide();
                $('#over').remove();
            }
            function ApproveFormCAList(idATTR, idCSRF)
            {
                swal({
                    title: "",
                    text: request_conform_approve,
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
                    $('body').append('<div id="over"></div>');
                    $(".loading-gif").show();
                    setTimeout(function () {
                        $.ajax({
                            type: "post",
                            url: "../ReqApproveGridCommon",
                            data: {
                                idParam: 'approvecertca',
                                sID: idATTR,
                                CsrfToken: idCSRF
                            },
                            cache: false,
                            success: function (html) {
                                var arr = sSpace(html).split('#');
                                if (arr[0] === "0")
                                {
                                    funSuccAlert(cert_succ_approve, "CertificateList.jsp");
                                }
                                else if (arr[0] === JS_EX_CSR_NULL) {
                                    $("#input-file-csr").focus();
                                    funErrorAlert(global_req_file);
                                }
                                else if (arr[0] === JS_EX_CSR_KEYSIZE) {
                                    $("#input-file-csr").focus();
                                    funErrorAlert(global_error_keysize_csr);
                                }
                                else if (arr[0] === JS_EX_DNS_SSL_NULL) {
                                    $("#idDNSName").focus();
                                    funErrorAlert(policy_req_empty + global_fm_dns_name);
                                }
                                else if (arr[0] === "1") {
                                    funErrorAlert(cert_error_approve);
                                }
                                else if (arr[0] === JS_EX_CSRF) {
                                    funCsrfAlert();
                                }
                                else if (arr[0] === JS_EX_LOGIN)
                                {
                                    RedirectPageLoginNoSess(global_alert_login);
                                }
                                else if (arr[0] === JS_EX_ANOTHERLOGIN)
                                {
                                    RedirectPageLoginNoSess(global_alert_another_login);
                                }
                                else if (arr[0] === JS_EX_WRONG_AGENCY) {
                                    RedirectPageLoginNoSess(global_error_wrong_agency);
                                }
                                else if (arr[0] === JS_EX_STATUS || arr[0] === JS_EX_NO_APPROVE_AGENCY)
                                {
                                    funErrorAlert(global_error_appove_status);
                                }
                                else if (arr[0] === JS_EX_INVALID_EXTERNAL_SYSTEM)
                                {
                                    funErrorAlert(global_error_credential_external_invalid);
                                }
                                else if (arr[0] === JS_EX_ERROR_DB)
                                {
                                    funErrorAlert(arr[1]);
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
                    }, JS_STR_ACTION_TIMEOUT);
                });
            }
            function ApproveFormAgency(idATTR, idCSRF)
            {
                swal({
                    title: "",
                    text: request_conform_approve,
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
                    $('body').append('<div id="over"></div>');
                    $(".loading-gif").show();
                    setTimeout(function () {
                        $.ajax({
                            type: "post",
                            url: "../ReqApproveGridCommon",
                            data: {
                                idParam: 'approvecertagency',
                                sID: idATTR,
                                CsrfToken: idCSRF
                            },
                            cache: false,
                            success: function (html) {
                                var arr = sSpace(html).split('#');
                                if (arr[0] === "0")
                                {
        //                            pushNotificationApprove(idATTR, $("#idHiddenCerDurationOrProfileID").val());
                                    funSuccAlert(cert_succ_approve, "CertificateList.jsp");
                                }
                                else if (arr[0] === JS_EX_CSR_NULL) {
                                    $("#input-file-csr").focus();
                                    funErrorAlert(global_req_file);
                                }
                                else if (arr[0] === JS_EX_CSR_KEYSIZE) {
                                    $("#input-file-csr").focus();
                                    funErrorAlert(global_error_keysize_csr);
                                }
                                else if (arr[0] === JS_EX_DNS_SSL_NULL) {
                                    $("#idDNSName").focus();
                                    funErrorAlert(policy_req_empty + global_fm_dns_name);
                                }
                                else if (arr[0] === JS_EX_CSRF) {
                                    funCsrfAlert();
                                }
                                else if (arr[0] === JS_EX_LOGIN)
                                {
                                    RedirectPageLoginNoSess(global_alert_login);
                                }
                                else if (arr[0] === JS_EX_ANOTHERLOGIN)
                                {
                                    RedirectPageLoginNoSess(global_alert_another_login);
                                }
                                else if (arr[0] === JS_EX_WRONG_AGENCY) {
                                    RedirectPageLoginNoSess(global_error_wrong_agency);
                                }
                                else if (arr[0] === JS_EX_DENIED_FUNCTION)
                                {
                                    funErrorAlert(global_alert_another_menu);
                                }
                                else if (arr[0] === JS_EX_STATUS)
                                {
                                    funErrorAlert(global_error_appove_status);
                                }
                                else if (arr[0] === JS_EX_INVALID_EXTERNAL_SYSTEM)
                                {
                                    funErrorAlert(global_error_credential_external_invalid);
                                }
                                else if (arr[0] === JS_EX_ERROR_DB)
                                {
                                    funErrorAlert(arr[1]);
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
                    }, JS_STR_ACTION_TIMEOUT);
                });
            }
            function ApproveFormChildrenAgency(idATTR, idCSRF)
            {
                swal({
                    title: "",
                    text: request_conform_approve,
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
                    $('body').append('<div id="over"></div>');
                    $(".loading-gif").show();
                    setTimeout(function () {
                        $.ajax({
                            type: "post",
                            url: "../ReqApproveGridCommon",
                            data: {
                                idParam: 'approvecertchildrenagency',
                                sID: idATTR,
                                CsrfToken: idCSRF
                            },
                            cache: false,
                            success: function (html) {
                                var arr = sSpace(html).split('#');
                                if (arr[0] === "0")
                                {
        //                            pushNotificationApprove(idATTR, $("#idHiddenCerDurationOrProfileID").val());
                                    funSuccAlert(cert_succ_approve, "CertificateList.jsp");
                                }
                                else if (arr[0] === JS_EX_CSR_NULL) {
                                    $("#input-file-csr").focus();
                                    funErrorAlert(global_req_file);
                                }
                                else if (arr[0] === JS_EX_CSR_KEYSIZE) {
                                    $("#input-file-csr").focus();
                                    funErrorAlert(global_error_keysize_csr);
                                }
                                else if (arr[0] === JS_EX_DNS_SSL_NULL) {
                                    $("#idDNSName").focus();
                                    funErrorAlert(policy_req_empty + global_fm_dns_name);
                                }
                                else if (arr[0] === JS_EX_CSRF) {
                                    funCsrfAlert();
                                }
                                else if (arr[0] === JS_EX_LOGIN)
                                {
                                    RedirectPageLoginNoSess(global_alert_login);
                                }
                                else if (arr[0] === JS_EX_ANOTHERLOGIN)
                                {
                                    RedirectPageLoginNoSess(global_alert_another_login);
                                }
                                else if (arr[0] === JS_EX_WRONG_AGENCY) {
                                    RedirectPageLoginNoSess(global_error_wrong_agency);
                                }
                                else if (arr[0] === JS_EX_DENIED_FUNCTION)
                                {
                                    funErrorAlert(global_alert_another_menu);
                                }
                                else if (arr[0] === JS_EX_STATUS)
                                {
                                    funErrorAlert(global_error_appove_status);
                                }
                                else if (arr[0] === JS_EX_INVALID_EXTERNAL_SYSTEM)
                                {
                                    funErrorAlert(global_error_credential_external_invalid);
                                }
                                else if (arr[0] === JS_EX_ERROR_DB)
                                {
                                    funErrorAlert(arr[1]);
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
                    }, JS_STR_ACTION_TIMEOUT);
                });
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
            .x_panel{padding: 10px 10px}
            .col-sm-4{padding-right: 5px;}
        </style>
    </head>
    <body class="nav-md">
        <%
        if (request.getSession(false).getAttribute("sUserID") != null) {
            String anticsrf = "" + Math.random();
            request.getSession().setAttribute("anticsrf", anticsrf);
            String SessAgentID = session.getAttribute("SessAgentID").toString().trim();
            String SessUserAgentID = session.getAttribute("SessUserAgentID").toString().trim();
            ROLE_DATA[][] sessFunctionCert = (ROLE_DATA[][]) session.getAttribute("SessRoleSet_Cert");
            String sessTreeArrayBranchID = session.getAttribute("sessTreeArrayBranchIDSystem").toString().trim();
            String SessLevelBranch = session.getAttribute("sessLevelBranch").toString().trim();
            String SessRoleID = session.getAttribute("RoleID_ID").toString().trim();
            String sBranchTreeEnable = conf.GetPropertybyCode(Definitions.CONFIG_BRANCH_TREE_ENABLED);
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
                        document.getElementById("idNameURL").innerHTML = cert_title_list;
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
                                String sDefaulCheckAll = "";
                                String isActiveSignServer = "0";
                                GENERAL_POLICY[][] sessGeneralPolicy1 = (GENERAL_POLICY[][]) request.getSession(false).getAttribute("sessGeneralPolicy_System");
                                if (sessGeneralPolicy1[0].length > 0) {
                                    for(GENERAL_POLICY rsPolicy1: sessGeneralPolicy1[0]) {
                                        if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_ACTIVATED_SIGNSERVER_ENABLED)) {
                                            isActiveSignServer = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                            break;
                                        }
                                    }
                                }
                                try {
                                    String isLoadPeronalPrefix = "";
                                    String isLoadEnterprisePrefix = "";
                                    String sEnterpriseCert = "";
                                    String sPersonalCert = "";
                                    CERTIFICATION[][] rsPgin = new CERTIFICATION[1][];
                                    String sessLanguageGlobal = session.getAttribute("sessVN").toString();
                                    if (session.getAttribute("RefreshApproveReqSess") != null && session.getAttribute("sessFromCreateDateApproveReq") != null
                                        && session.getAttribute("sessToCreateDateApproveReq") != null) {
                                        try{
                                            session.setAttribute("RefreshApproveReqSessPaging", "1");
                                            session.setAttribute("SearchSharePagingRegisToken", "0");
                                            statusLoad = 1;
                                            String ToCreateDate = (String) session.getAttribute("sessToCreateDateApproveReq");
                                            String FromCreateDate = (String) session.getAttribute("sessFromCreateDateApproveReq");
                                            String TOKEN_ID = (String) session.getAttribute("sessTOKEN_IDApproveReq");
                                            String CERTIFICATION_SN = (String) session.getAttribute("sessCERTIFICATION_SNApproveReq");
                                            String BranchOffice = (String) session.getAttribute("sessBranchOfficeApproveReq");
                                            String PERSONAL_NAME = (String) session.getAttribute("sessPERSONAL_NAMEApproveReq");
                                            String COMPANY_NAME = (String) session.getAttribute("sessCOMPANY_NAMEApproveReq");
                                            String FORM_FACTOR = (String) session.getAttribute("sessFormFactorApproveReq");
                                            String DOMAIN_NAME = (String) session.getAttribute("sessDOMAIN_NAMEApproveReq");

                                            String TAX_CODE = (String) session.getAttribute("sessTAX_CODEApproveReq");
                                            String BUDGET_CODE = (String) session.getAttribute("sessBUDGET_CODEApproveReq");
                                            String DECISION = (String) session.getAttribute("sessDECISIONApproveReq");
                                            String P_ID = (String) session.getAttribute("sessP_IDApproveReq");
                                            String CCCD = (String) session.getAttribute("sessCCCDApproveReq");
                                            String PASSPORT = (String) session.getAttribute("sessPASSPORTApproveReq");
                                            String CERTIFICATION_STATE = (String) session.getAttribute("sessCERTIFICATION_STATEApproveReq");
                                            String CERTIFICATION_ATTR_TYPE = (String) session.getAttribute("sessCERTIFICATION_ATTR_TYPEApproveReq");
                                            String CERTIFICATION_PURPOSE = (String) session.getAttribute("sessCERTIFICATION_PURPOSEApproveReq");
                                            String DEVICE_UUID_SEARCH = (String) session.getAttribute("sessDEVICE_UUIDApproveReq");
                                            String HSM_CONFIRM = (String) session.getAttribute("sessActiveEnabledApproveReq");

                                            strAlertAllTimes = (String) session.getAttribute("AlertAllTimeSApproveReq");
                                            session.setAttribute("RefreshApproveReqSess", null);
                                            session.setAttribute("sessFromCreateDateApproveReq", FromCreateDate);
                                            session.setAttribute("sessToCreateDateApproveReq", ToCreateDate);
                                            session.setAttribute("sessTOKEN_IDApproveReq", TOKEN_ID);
                                            session.setAttribute("sessCERTIFICATION_SNApproveReq", CERTIFICATION_SN);

                                            session.setAttribute("sessPERSONAL_NAMEApproveReq", PERSONAL_NAME);
                                            session.setAttribute("sessCOMPANY_NAMEApproveReq", COMPANY_NAME);
                                            session.setAttribute("sessFormFactorApproveReq", FORM_FACTOR);
                                            session.setAttribute("sessDOMAIN_NAMEApproveReq", DOMAIN_NAME);
                                            session.setAttribute("sessTAX_CODEApproveReq", TAX_CODE);
                                            session.setAttribute("sessBUDGET_CODEApproveReq", BUDGET_CODE);
                                            session.setAttribute("sessDECISIONApproveReq", DECISION);
                                            session.setAttribute("sessP_IDApproveReq", P_ID);
                                            session.setAttribute("sessCCCDApproveReq", CCCD);
                                            session.setAttribute("sessPASSPORTApproveReq", PASSPORT);
                                            session.setAttribute("sessCERTIFICATION_STATEApproveReq", CERTIFICATION_STATE);
                                            session.setAttribute("sessCERTIFICATION_ATTR_TYPEApproveReq", CERTIFICATION_ATTR_TYPE);
                                            session.setAttribute("sessCERTIFICATION_PURPOSEApproveReq", CERTIFICATION_PURPOSE);
                                            session.setAttribute("sessDEVICE_UUIDApproveReq", DEVICE_UUID_SEARCH);
                                            session.setAttribute("sessActiveEnabledApproveReq", HSM_CONFIRM);

                                            session.setAttribute("sessBranchOfficeApproveReq", BranchOffice);
                                            session.setAttribute("AlertAllTimeSApproveReq", strAlertAllTimes);
                                            if(isUIDCollection.equals("1")) {
                                                String sENTERPRISE_ID = (String) session.getAttribute("sessENTERPRISE_IDApproveReq");
                                                String sPERSONAL_ID = (String) session.getAttribute("sessPERSONAL_IDApproveReq");
                                                String sENTERPRISE_PREFIX = (String) session.getAttribute("sessENTERPRISE_PREFIXApproveReq");
                                                String sPERSONAL_PREFIX = (String) session.getAttribute("sessPERSONAL_PREFIXApproveReq");
                                                session.setAttribute("sessENTERPRISE_IDApproveReq", sENTERPRISE_ID);
                                                session.setAttribute("sessPERSONAL_IDApproveReq", sPERSONAL_ID);
                                                session.setAttribute("sessENTERPRISE_PREFIXApproveReq", sENTERPRISE_PREFIX);
                                                session.setAttribute("sessPERSONAL_PREFIXApproveReq", sPERSONAL_PREFIX);
                                                isLoadEnterprisePrefix = sENTERPRISE_PREFIX;
                                                isLoadPeronalPrefix = sPERSONAL_PREFIX;
                                                if(!"".equals(sENTERPRISE_ID)){
                                                    sEnterpriseCert = sENTERPRISE_PREFIX + "%"+sENTERPRISE_ID+"%";
                                                }
                                                if(!"".equals(sPERSONAL_ID)){
                                                    sPersonalCert = sPERSONAL_PREFIX + "%"+sPERSONAL_ID+"%";
                                                }
                                            }
                                            if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(CERTIFICATION_STATE)) {
                                                CERTIFICATION_STATE = "";
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
                                            if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(BranchOffice)) {
                                                BranchOffice = "";
                                            }
                                            if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(HSM_CONFIRM)) {
                                                HSM_CONFIRM = "";
                                            }
                                            if(!FORM_FACTOR.equals(String.valueOf(Definitions.CONFIG_PKI_FORMFACTOR_ID_SOFT_TOKEN))) {
                                                HSM_CONFIRM = "";
                                            } else {
                                                if(!"1".equals(isActiveSignServer)) {
                                                    HSM_CONFIRM = "";
                                                }
                                            }
                                            if("1".equals(strAlertAllTimes)) {
                                                FromCreateDate = "";
                                                ToCreateDate = "";
                                            }
                                            if(!"".equals(TOKEN_ID) || !"".equals(PERSONAL_NAME) || !"".equals(HSM_CONFIRM)
                                                || !"".equals(COMPANY_NAME) || !"".equals(FORM_FACTOR) || !"".equals(DOMAIN_NAME) || !"".equals(TAX_CODE)
                                                || !"".equals(BUDGET_CODE) || !"".equals(P_ID) || !"".equals(PASSPORT) || !"".equals(CCCD) || !"".equals(DECISION)
                                                || !"".equals(CERTIFICATION_PURPOSE) || !"".equals(DEVICE_UUID_SEARCH) || !"".equals(CERTIFICATION_SN)
                                                || (!"".equals(BranchOffice) && !BranchOffice.equals(SessUserAgentID))
                                                || !"".equals(sEnterpriseCert) || !"".equals(sPersonalCert))
                                            {
                                                strExpandFilter = "1";
                                            }
                                            String pBRANCH_BY = EscapeUtils.escapeHtmlSearch(SessUserAgentID);
                                            if("1".equals(sBranchTreeEnable)) {
                                                pBRANCH_BY = "1";
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
                                            ss = db.S_BO_CERTIFICATION_APPROVED_TOTAL(EscapeUtils.escapeHtmlSearch(FromCreateDate),
                                                EscapeUtils.escapeHtmlSearch(ToCreateDate), EscapeUtils.escapeHtmlSearch(CERTIFICATION_STATE), EscapeUtils.escapeHtmlSearch(TOKEN_ID),
                                                EscapeUtils.escapeHtmlSearch(CERTIFICATION_PURPOSE), EscapeUtils.escapeHtmlSearch(CERTIFICATION_ATTR_TYPE),
                                                EscapeUtils.escapeHtmlSearch(PERSONAL_NAME), EscapeUtils.escapeHtmlSearch(COMPANY_NAME),
                                                EscapeUtils.escapeHtmlSearch(DOMAIN_NAME), EscapeUtils.escapeHtmlSearch(BranchOffice),
                                                pBRANCH_BY, EscapeUtils.escapeHtmlSearch(FORM_FACTOR),
                                                EscapeUtils.escapeHtmlSearch(DEVICE_UUID_SEARCH), EscapeUtils.escapeHtmlSearch(CERTIFICATION_SN),
                                                "", sessTreeArrayBranchID, sEnterpriseCert, sPersonalCert, HSM_CONFIRM);
                                            session.setAttribute("CountListApproveReq", String.valueOf(ss));
                                            if (session.getAttribute("SearchIPageNoPagingApproveReq") != null) {
                                                String sPage = (String) session.getAttribute("SearchIPageNoPagingApproveReq");
                                                iPagNo = Integer.parseInt(sPage);
                                            }
                                            if (session.getAttribute("SearchISwRwsPagingApproveReq") != null) {
                                                String sSumPage = (String) session.getAttribute("SearchISwRwsPagingApproveReq");
                                                iSwRws = Integer.parseInt(sSumPage);
                                            }
                                            if (session.getAttribute("RefreshApproveReqSessNumberPaging") != null) {
                                                String sNoPage = (String) session.getAttribute("RefreshApproveReqSessNumberPaging");
                                                iPaNoSS = Integer.parseInt(sNoPage);
                                            }
                                            session.setAttribute("SearchIPageNoPagingApproveReq", String.valueOf(iPagNo));
                                            session.setAttribute("SearchISwRwsPagingApproveReq", String.valueOf(iSwRws));
                                            if (ss > 0) {
                                                db.S_BO_CERTIFICATION_APPROVED_LIST(EscapeUtils.escapeHtmlSearch(FromCreateDate),
                                                    EscapeUtils.escapeHtmlSearch(ToCreateDate),
                                                    EscapeUtils.escapeHtmlSearch(CERTIFICATION_STATE), EscapeUtils.escapeHtmlSearch(TOKEN_ID),
                                                    EscapeUtils.escapeHtmlSearch(CERTIFICATION_PURPOSE), EscapeUtils.escapeHtmlSearch(CERTIFICATION_ATTR_TYPE),
                                                    EscapeUtils.escapeHtmlSearch(PERSONAL_NAME), EscapeUtils.escapeHtmlSearch(COMPANY_NAME),
                                                    EscapeUtils.escapeHtmlSearch(DOMAIN_NAME), EscapeUtils.escapeHtmlSearch(BranchOffice),
                                                    pBRANCH_BY, EscapeUtils.escapeHtmlSearch(FORM_FACTOR),
                                                    EscapeUtils.escapeHtmlSearch(DEVICE_UUID_SEARCH), EscapeUtils.escapeHtmlSearch(CERTIFICATION_SN),
                                                    sessLanguageGlobal, rsPgin, iPagNo, iSwRws, "", sessTreeArrayBranchID, sEnterpriseCert, sPersonalCert, HSM_CONFIRM);
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
                                        } catch(Exception e){CommonFunction.LogExceptionServlet(null, "RequestList reload: " + e.getMessage(), e);}
                                    } else if (request.getMethod().equals("POST") || "1".equals(hasPaging)) {
                                        session.setAttribute("RefreshApproveReqSessPaging", null);
                                        if (request.getMethod().equals("POST")) {
                                            session.setAttribute("pIaApproveReq", null);
                                            session.setAttribute("pIbApproveReq", null);
                                            session.setAttribute("SearchShareStoreRegisToken", null);
                                            session.setAttribute("SearchIPageNoPagingApproveReq", null);
                                            session.setAttribute("SearchISwRwsPagingApproveReq", null);
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
                                        String FromCreateDate = request.getParameter("FromCreateDate");
                                        String ToCreateDate = request.getParameter("ToCreateDate");
                                        String TOKEN_ID = request.getParameter("TOKEN_ID");
                                        String CERTIFICATION_SN = EscapeUtils.ConvertStringToUnicode(request.getParameter("CERTIFICATION_SN"));
                                        String PERSONAL_NAME = EscapeUtils.ConvertStringToUnicode(request.getParameter("PERSONAL_NAME"));
                                        String COMPANY_NAME = EscapeUtils.ConvertStringToUnicode(request.getParameter("COMPANY_NAME"));
                                        String FORM_FACTOR = request.getParameter("FORM_FACTOR_LIST");
                                        String DOMAIN_NAME = EscapeUtils.ConvertStringToUnicode(request.getParameter("DOMAIN_NAME"));
                                        String TAX_CODE =EscapeUtils.ConvertStringToUnicode(request.getParameter("TAX_CODE"));
                                        String BUDGET_CODE =EscapeUtils.ConvertStringToUnicode(request.getParameter("BUDGET_CODE"));
                                        String DECISION =EscapeUtils.ConvertStringToUnicode(request.getParameter("DECISION"));
                                        String P_ID = EscapeUtils.ConvertStringToUnicode(request.getParameter("P_ID"));
                                        String CCCD = EscapeUtils.ConvertStringToUnicode(request.getParameter("CCCD"));
                                        String PASSPORT = EscapeUtils.ConvertStringToUnicode(request.getParameter("PASSPORT"));
                                        String CERTIFICATION_STATE =request.getParameter("CERTIFICATION_STATE");
                                        String CERTIFICATION_ATTR_TYPE = request.getParameter("CERTIFICATION_ATTR_TYPE");
                                        String CERTIFICATION_PURPOSE = request.getParameter("CERTIFICATION_PURPOSE_LIST");
                                        String DEVICE_UUID_SEARCH = EscapeUtils.ConvertStringToUnicode(request.getParameter("DEVICE_UUID_SEARCH"));
                                        String BranchOffice = request.getParameter("BranchOffice");
                                        String HSM_CONFIRM = request.getParameter("HSM_CONFIRM");
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
                                            String sENTERPRISE_ID = request.getParameter("ENTERPRISE_ID");
                                            String sPERSONAL_ID = request.getParameter("PERSONAL_ID");
                                            String sENTERPRISE_PREFIX = EscapeUtils.ConvertStringToUnicode(request.getParameter("ENTERPRISE_PREFIX"));
                                            String sPERSONAL_PREFIX = EscapeUtils.ConvertStringToUnicode(request.getParameter("PERSONAL_PREFIX"));
                                            if ("1".equals(hasPaging)) {
                                                sENTERPRISE_PREFIX = (String) session.getAttribute("sessENTERPRISE_PREFIXApproveReq");
                                                sPERSONAL_PREFIX = (String) session.getAttribute("sessPERSONAL_PREFIXApproveReq");
                                                sENTERPRISE_ID = (String) session.getAttribute("sessENTERPRISE_IDApproveReq");
                                                sPERSONAL_ID = (String) session.getAttribute("sessPERSONAL_IDApproveReq");
                                            }
                                            session.setAttribute("sessENTERPRISE_IDApproveReq", sENTERPRISE_ID);
                                            session.setAttribute("sessPERSONAL_IDApproveReq", sPERSONAL_ID);
                                            session.setAttribute("sessENTERPRISE_PREFIXApproveReq", sENTERPRISE_PREFIX);
                                            session.setAttribute("sessPERSONAL_PREFIXApproveReq", sPERSONAL_PREFIX);
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
                                            session.setAttribute("SearchSharePagingRegisToken", "0");
                                            ToCreateDate = (String) session.getAttribute("sessToCreateDateApproveReq");
                                            FromCreateDate = (String) session.getAttribute("sessFromCreateDateApproveReq");
                                            TOKEN_ID = (String) session.getAttribute("sessTOKEN_IDApproveReq");
                                            CERTIFICATION_SN = (String) session.getAttribute("sessCERTIFICATION_SNApproveReq");
                                            PERSONAL_NAME = (String) session.getAttribute("sessPERSONAL_NAMEApproveReq");
                                            COMPANY_NAME = (String) session.getAttribute("sessCOMPANY_NAMEApproveReq");
                                            FORM_FACTOR = (String) session.getAttribute("sessFormFactorApproveReq");
                                            DOMAIN_NAME = (String) session.getAttribute("sessDOMAIN_NAMEApproveReq");
                                            TAX_CODE = (String) session.getAttribute("sessTAX_CODEApproveReq");
                                            BUDGET_CODE = (String) session.getAttribute("sessBUDGET_CODEApproveReq");
                                            DECISION = (String) session.getAttribute("sessDECISIONApproveReq");
                                            P_ID = (String) session.getAttribute("sessP_IDApproveReq");
                                            CCCD = (String) session.getAttribute("sessCCCDApproveReq");
                                            PASSPORT = (String) session.getAttribute("sessPASSPORTApproveReq");
                                            CERTIFICATION_STATE = (String) session.getAttribute("sessCERTIFICATION_STATEApproveReq");
                                            CERTIFICATION_ATTR_TYPE = (String) session.getAttribute("sessCERTIFICATION_ATTR_TYPEApproveReq");
                                            CERTIFICATION_PURPOSE = (String) session.getAttribute("sessCERTIFICATION_PURPOSEApproveReq");
                                            DEVICE_UUID_SEARCH = (String) session.getAttribute("sessDEVICE_UUIDApproveReq");
                                            BranchOffice = (String) session.getAttribute("sessBranchOfficeApproveReq");
                                            HSM_CONFIRM = (String) session.getAttribute("sessActiveEnabledApproveReq");
                                            strAlertAllTimes = (String) session.getAttribute("AlertAllTimeSApproveReq");
                                            session.setAttribute("SessParamOnPagingCertList", null);
                                        } else {
                                            session.setAttribute("SearchSharePagingRegisToken", "1");
                                            session.setAttribute("CountListApproveReq", null);
                                        }
                                        session.setAttribute("sessFromCreateDateApproveReq", FromCreateDate);
                                        session.setAttribute("sessToCreateDateApproveReq", ToCreateDate);
                                        session.setAttribute("sessTOKEN_IDApproveReq", TOKEN_ID);
                                        session.setAttribute("sessCERTIFICATION_SNApproveReq", CERTIFICATION_SN);
                                        session.setAttribute("sessPERSONAL_NAMEApproveReq", PERSONAL_NAME);
                                        session.setAttribute("sessCOMPANY_NAMEApproveReq", COMPANY_NAME);
                                        session.setAttribute("sessFormFactorApproveReq", FORM_FACTOR);
                                        session.setAttribute("sessDOMAIN_NAMEApproveReq", DOMAIN_NAME);
                                        session.setAttribute("sessTAX_CODEApproveReq", TAX_CODE);
                                        session.setAttribute("sessBUDGET_CODEApproveReq", BUDGET_CODE);
                                        session.setAttribute("sessDECISIONApproveReq", DECISION);
                                        session.setAttribute("sessP_IDApproveReq", P_ID);
                                        session.setAttribute("sessCCCDApproveReq", CCCD);
                                        session.setAttribute("sessPASSPORTApproveReq", PASSPORT);
                                        session.setAttribute("sessCERTIFICATION_STATEApproveReq", CERTIFICATION_STATE);
                                        session.setAttribute("sessCERTIFICATION_ATTR_TYPEApproveReq", CERTIFICATION_ATTR_TYPE);
                                        session.setAttribute("sessCERTIFICATION_PURPOSEApproveReq", CERTIFICATION_PURPOSE);
                                        session.setAttribute("sessDEVICE_UUIDApproveReq", DEVICE_UUID_SEARCH);
                                        session.setAttribute("sessBranchOfficeApproveReq", BranchOffice);
                                        session.setAttribute("sessActiveEnabledApproveReq", HSM_CONFIRM);
                                        session.setAttribute("AlertAllTimeSApproveReq", strAlertAllTimes);
                                        if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(CERTIFICATION_STATE)) {
                                            CERTIFICATION_STATE = "";
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
                                        if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(BranchOffice)) {
                                            BranchOffice = "";
                                        }
                                        if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(HSM_CONFIRM)) {
                                            HSM_CONFIRM = "";
                                        }
                                        if(!FORM_FACTOR.equals(String.valueOf(Definitions.CONFIG_PKI_FORMFACTOR_ID_SOFT_TOKEN))) {
                                            HSM_CONFIRM = "";
                                        } else {
                                            if(!"1".equals(isActiveSignServer)) {
                                                HSM_CONFIRM = "";
                                            }
                                        }
                                        if(!"".equals(TOKEN_ID) || !"".equals(PERSONAL_NAME) || !"".equals(HSM_CONFIRM)
                                            || !"".equals(COMPANY_NAME) || !"".equals(FORM_FACTOR) || !"".equals(DOMAIN_NAME) || !"".equals(TAX_CODE)
                                            || !"".equals(BUDGET_CODE) || !"".equals(P_ID) || !"".equals(PASSPORT) || !"".equals(CCCD) || !"".equals(DECISION)
                                            || !"".equals(CERTIFICATION_PURPOSE) || !"".equals(DEVICE_UUID_SEARCH) || !"".equals(CERTIFICATION_SN)
                                            || (!"".equals(BranchOffice) && !BranchOffice.equals(SessUserAgentID))
                                            || !"".equals(sEnterpriseCert) || !"".equals(sPersonalCert)) {
                                            strExpandFilter = "1";
                                        }
                                        String pBRANCH_BY = EscapeUtils.escapeHtmlSearch(SessUserAgentID);
                                        if("1".equals(sBranchTreeEnable)) {
                                            pBRANCH_BY = "1";
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
                                        if ((session.getAttribute("CountListApproveReq")) == null) {
                                            ss = db.S_BO_CERTIFICATION_APPROVED_TOTAL(EscapeUtils.escapeHtmlSearch(FromCreateDate),
                                                EscapeUtils.escapeHtmlSearch(ToCreateDate),EscapeUtils.escapeHtmlSearch(CERTIFICATION_STATE), EscapeUtils.escapeHtmlSearch(TOKEN_ID),
                                                EscapeUtils.escapeHtmlSearch(CERTIFICATION_PURPOSE), EscapeUtils.escapeHtmlSearch(CERTIFICATION_ATTR_TYPE),
                                                EscapeUtils.escapeHtmlSearch(PERSONAL_NAME), EscapeUtils.escapeHtmlSearch(COMPANY_NAME),
                                                EscapeUtils.escapeHtmlSearch(DOMAIN_NAME), EscapeUtils.escapeHtmlSearch(BranchOffice),
                                                pBRANCH_BY, EscapeUtils.escapeHtmlSearch(FORM_FACTOR),
                                                EscapeUtils.escapeHtmlSearch(DEVICE_UUID_SEARCH), EscapeUtils.escapeHtmlSearch(CERTIFICATION_SN),
                                                "", sessTreeArrayBranchID, sEnterpriseCert, sPersonalCert, HSM_CONFIRM);
                                            session.setAttribute("CountListApproveReq", String.valueOf(ss));
                                        } else {
                                            String sCount = (String) session.getAttribute("CountListApproveReq");
                                            ss = Integer.parseInt(sCount);
                                            session.setAttribute("CountListApproveReq", String.valueOf(ss));
                                        }
                                        iTotRslts = ss;
                                        if (iTotRslts > 0) {
                                            db.S_BO_CERTIFICATION_APPROVED_LIST(EscapeUtils.escapeHtmlSearch(FromCreateDate),
                                                EscapeUtils.escapeHtmlSearch(ToCreateDate),
                                                EscapeUtils.escapeHtmlSearch(CERTIFICATION_STATE), EscapeUtils.escapeHtmlSearch(TOKEN_ID),
                                                EscapeUtils.escapeHtmlSearch(CERTIFICATION_PURPOSE), EscapeUtils.escapeHtmlSearch(CERTIFICATION_ATTR_TYPE),
                                                EscapeUtils.escapeHtmlSearch(PERSONAL_NAME), EscapeUtils.escapeHtmlSearch(COMPANY_NAME),
                                                EscapeUtils.escapeHtmlSearch(DOMAIN_NAME), EscapeUtils.escapeHtmlSearch(BranchOffice),
                                                pBRANCH_BY, EscapeUtils.escapeHtmlSearch(FORM_FACTOR),
                                                EscapeUtils.escapeHtmlSearch(DEVICE_UUID_SEARCH), EscapeUtils.escapeHtmlSearch(CERTIFICATION_SN),
                                                sessLanguageGlobal, rsPgin, iPagNo, iSwRws,
                                                "", sessTreeArrayBranchID, sEnterpriseCert, sPersonalCert, HSM_CONFIRM);
                                            session.setAttribute("SearchIPageNoPagingApproveReq", String.valueOf(iPagNo));
                                            session.setAttribute("SearchISwRwsPagingApproveReq", String.valueOf(iSwRws));
                                            strMess = com.convertMoney(ss);
                                            if (rsPgin[0].length > 0) {
                                                status = 1;
                                            } else {
                                                status = 1000;
                                            }
                                        } else {
                                            status = 1000;
                                        }
                                    } catch(Exception e){CommonFunction.LogExceptionServlet(null, "RequestList search new: " + e.getMessage(), e);}
                                } else {
                                    session.setAttribute("RefreshApproveReqSessPaging", null);
                                    session.setAttribute("SearchShareStoreRegisToken", null);
                                    session.setAttribute("SearchIPageNoPagingApproveReq", null);
                                    session.setAttribute("SearchISwRwsPagingApproveReq", null);
                                    session.setAttribute("sessFromCreateDateApproveReq", null);
                                    session.setAttribute("sessToCreateDateApproveReq", null);
                                    session.setAttribute("sessTOKEN_IDApproveReq", null);
                                    session.setAttribute("sessPERSONAL_NAMEApproveReq", null);
                                    session.setAttribute("sessCOMPANY_NAMEApproveReq", null);
                                    session.setAttribute("sessFormFactorApproveReq", null);
                                    session.setAttribute("sessDOMAIN_NAMEApproveReq", null);
                                    session.setAttribute("sessTAX_CODEApproveReq", null);
                                    session.setAttribute("sessBUDGET_CODEApproveReq", null);
                                    session.setAttribute("sessDECISIONApproveReq", null);
                                    session.setAttribute("sessP_IDApproveReq", null);
                                    session.setAttribute("sessCCCDApproveReq", null);
                                    session.setAttribute("sessPASSPORTApproveReq", null);
                                    session.setAttribute("sessBranchOfficeApproveReq", null);
                                    session.setAttribute("sessDEVICE_UUIDApproveReq", null);
                                    session.setAttribute("sessCERTIFICATION_SNApproveReq", null);
                                    session.setAttribute("AlertAllTimeSApproveReq", null);
                                    session.setAttribute("sessCERTIFICATION_STATEApproveReq", "All");
                                    if(SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT))
                                    {
                                        session.setAttribute("sessCERTIFICATION_STATEApproveReq", "2");
                                    } else if(!SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT))
                                    {
                                        session.setAttribute("sessCERTIFICATION_STATEApproveReq", "1");
                                    }
                                    session.setAttribute("sessCERTIFICATION_ATTR_TYPEApproveReq", "All");
                                    session.setAttribute("sessCERTIFICATION_PURPOSEApproveReq", "All");
                                    session.setAttribute("sessBranchOfficeApproveReq", "All");
                                    if(isUIDCollection.equals("1")) {
                                        session.setAttribute("sessENTERPRISE_IDApproveReq", null);
                                        session.setAttribute("sessPERSONAL_IDApproveReq", null);
                                        session.setAttribute("sessENTERPRISE_PREFIXApproveReq", null);
                                        session.setAttribute("sessPERSONAL_PREFIXApproveReq", null);
                                    }
                                }
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
                                                            <input type="Text" id="demo1" name="FromCreateDate" <%= session.getAttribute("AlertAllTimeSApproveReq") != null && "1".equals(session.getAttribute("AlertAllTimeSApproveReq").toString()) ? "disabled" : ""%>
                                                                value="<%= session.getAttribute("sessFromCreateDateApproveReq") != null && !"1".equals(session.getAttribute("AlertAllTimeSApproveReq").toString()) ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessFromCreateDateApproveReq").toString()) : com.ConvertMonthSub(30)%>"
                                                                maxlength="25" class="form-control123"/>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;padding-right: 5px;"><script>document.write(global_fm_ToDate);</script></label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <input type="Text" id="demo2" name="ToCreateDate" <%= session.getAttribute("AlertAllTimeSApproveReq") != null && "1".equals(session.getAttribute("AlertAllTimeSApproveReq").toString()) ? "disabled" : ""%>
                                                                value="<%= session.getAttribute("sessToCreateDateApproveReq") != null && !"1".equals(session.getAttribute("AlertAllTimeSApproveReq").toString()) ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessToCreateDateApproveReq").toString()) : com.ConvertMonthSub(0)%>"
                                                                maxlength="25" class="form-control123"/>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;padding-right: 5px;"><script>document.write(global_fm_check_date);</script></label>
                                                        <div class="col-sm-7" style="padding-right: 0px;padding-top: 7px; text-align: left;">
                                                            <label class="switch" for="idCheck">
                                                                <input type="checkbox" name="nameCheck" id="idCheck" onchange="checkboxChange();" <%= session.getAttribute("AlertAllTimeSApproveReq") != null && "1".equals(session.getAttribute("AlertAllTimeSApproveReq").toString()) ? "" : "checked" %>/>
                                                                <div class="slider round"></div>
                                                            </label>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="form-group" style="padding: 0px 0px 0px 0px;margin: 0;">
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;padding-right: 5px;"><script>document.write(cert_fm_type_request);</script></label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <select name="CERTIFICATION_ATTR_TYPE" id="CERTIFICATION_ATTR_TYPE" class="form-control123">
                                                                <option value="<%= Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL%>" <%= session.getAttribute("sessCERTIFICATION_ATTR_TYPEApproveReq") != null && Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(session.getAttribute("sessCERTIFICATION_ATTR_TYPEApproveReq").toString()) ? "selected" : ""%>><script>document.write(global_fm_combox_all);</script></option>
                                                                <%
                                                                    CERTIFICATION_ATTR_TYPE[][] rsType = new CERTIFICATION_ATTR_TYPE[1][];
                                                                    db.S_BO_CERTIFICATION_ATTR_TYPE_COMBOBOX(sessLanguageGlobal, rsType);
                                                                    if (rsType[0].length > 0) {
                                                                        for (CERTIFICATION_ATTR_TYPE temp1 : rsType[0]) {
                                                                %>
                                                                <option value="<%=String.valueOf(temp1.ID)%>" <%= session.getAttribute("sessCERTIFICATION_ATTR_TYPEApproveReq") != null && String.valueOf(temp1.ID).equals(session.getAttribute("sessCERTIFICATION_ATTR_TYPEApproveReq").toString()) ? "selected" : ""%>><%=temp1.REMARK%></option>
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
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;padding-right: 5px;"><script>document.write(global_fm_Status);</script></label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <select name="CERTIFICATION_STATE" id="CERTIFICATION_STATE" class="form-control123">
                                                                <%
                                                                    if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_ALL,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_SEARCH, sessFunctionCert) == true) {
                                                                %>
                                                                <option value="<%= Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL%>" <%= session.getAttribute("sessCERTIFICATION_STATEApproveReq") != null && Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(session.getAttribute("sessCERTIFICATION_STATEApproveReq").toString()) ? "selected" : ""%>><script>document.write(global_fm_combox_all);</script></option>
                                                                <%
                                                                    }
                                                                %>
                                                                <%
                                                                    CERTIFICATION_ATTR_STATE[][] rsState = new CERTIFICATION_ATTR_STATE[1][];
                                                                    db.S_BO_CERTIFICATION_ATTR_STATE_COMBOBOX(sessLanguageGlobal, rsState);
                                                                    if (rsState[0].length > 0) {
                                                                        for (CERTIFICATION_ATTR_STATE temp1 : rsState[0]) {
                                                                            if(CommonFunction.CheckRoleFuncValid(EscapeUtils.CheckTextNull(temp1.NAME),Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_SEARCH, sessFunctionCert) == true) {
                                                                 %>
                                                                 <option value="<%=String.valueOf(temp1.ID)%>" <%= session.getAttribute("sessCERTIFICATION_STATEApproveReq") != null && String.valueOf(temp1.ID).equals(session.getAttribute("sessCERTIFICATION_STATEApproveReq").toString()) ? "selected" : ""%>><%=temp1.REMARK%></option>
                                                                 <%
                                                                            }
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
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="form-group" style="clear: both;">
                                                <label class="control-label col-sm-2" style="color: #000000; font-weight: bold;padding-top: 0;">
                                                    <a id="idAShowList" style="cursor: pointer; color: blue; text-decoration: underline;" onclick="popupViewCSRSList();"><script>document.write(global_fm_search_expand);</script></a>
                                                    <a id="idAHideList" style="cursor: pointer; color: blue; text-decoration: underline;display: none;" onclick="popupHideCTSList();"><script>document.write(global_fm_search_hide);</script></a>
                                                </label>
                                                <div class="col-sm-10" style="padding-right: 0px;"></div>
                                            </div>
                                            <div id="idViewCSRList" style="clear: both;padding-top: 0px;display: none;">
                                                <div class="form-group" style="padding: 0px 0px 0px 0px;margin: 0;">
                                                    <div class="col-sm-4" style="padding-left: 0;">
                                                        <div class="form-group">
                                                            <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;padding-right: 5px;">
                                                                <script>document.write(global_fm_Method);</script>
                                                            </label>
                                                            <div class="col-sm-7" style="padding-right: 0px;">
                                                                <select name="FORM_FACTOR_LIST" id="FORM_FACTOR_LIST" class="form-control123" onchange="onChangeHSMConfirm(this.value);">
                                                                    <option value="<%= Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL%>" <%= session.getAttribute("sessFormFactorApproveReq") != null && Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(session.getAttribute("sessFormFactorApproveReq").toString()) ? "selected" : ""%>><script>document.write(global_fm_combox_all);</script></option>
                                                                    <%
                                                                        PKI_FORMFACTOR[][] rsFromFactore = new PKI_FORMFACTOR[1][];
                                                                        db.S_BO_PKI_FORMFACTOR_COMBOBOX(sessLanguageGlobal, rsFromFactore);
                                                                        if (rsFromFactore[0].length > 0) {
                                                                            for (PKI_FORMFACTOR temp1 : rsFromFactore[0]) {
                                                                    %>
                                                                    <option value="<%=String.valueOf(temp1.ID)%>" <%= session.getAttribute("sessFormFactorApproveReq") != null && String.valueOf(temp1.ID).equals(session.getAttribute("sessFormFactorApproveReq").toString()) ? "selected" : ""%>><%=temp1.REMARK%></option>
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
                                                                    <option value="<%= Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL%>" <%= session.getAttribute("sessCERTIFICATION_PURPOSEApproveReq") != null && Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(session.getAttribute("sessCERTIFICATION_PURPOSEApproveReq").toString()) ? "selected" : ""%>><script>document.write(global_fm_combox_all);</script></option>
                                                                    <%
                                                                        CERTIFICATION_PURPOSE[][] rsPurpose = new CERTIFICATION_PURPOSE[1][];
                                                                        db.S_BO_CERTIFICATION_PURPOSE_COMBOBOX(sessLanguageGlobal, rsPurpose);
                                                                        if (rsPurpose[0].length > 0) {
                                                                            for (CERTIFICATION_PURPOSE temp1 : rsPurpose[0]) {
                                                                    %>
                                                                    <option value="<%=String.valueOf(temp1.ID)%>" <%= session.getAttribute("sessCERTIFICATION_PURPOSEApproveReq") != null && String.valueOf(temp1.ID).equals(session.getAttribute("sessCERTIFICATION_PURPOSEApproveReq").toString()) ? "selected" : ""%>><%=temp1.REMARK%></option>
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
                                                                <script>document.write(global_fm_serial);</script>
                                                            </label>
                                                            <div class="col-sm-7" style="padding-right: 0px;">
                                                                <input type="text" name="CERTIFICATION_SN" maxlength="45" value="<%= session.getAttribute("sessCERTIFICATION_SNApproveReq") != null ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessCERTIFICATION_SNApproveReq").toString()) : ""%>"
                                                                    class="form-control123">
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
                                                            <input type="text" name="COMPANY_NAME" maxlength="150" value="<%= session.getAttribute("sessCOMPANY_NAMEApproveReq") != null ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessCOMPANY_NAMEApproveReq").toString()) : ""%>"
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
                                                            <option value="<%=temp1.PREFIX_DB%>" <%= session.getAttribute("sessENTERPRISE_PREFIXApproveReq") != null && temp1.PREFIX_DB.equals(session.getAttribute("sessENTERPRISE_PREFIXApproveReq").toString()) ? "selected" : ""%>><%=temp1.REMARK%></option>
                                                            <%
                                                                    }
                                                                }
                                                            %>
                                                        </select>
<!--                                                        <select name="ENTERPRISE_PREFIX" id="ENTERPRISE_PREFIX" onclick="changeEnterprise(this.value);" class="form-control123" style="text-align: left;">
                                                            <option value="MST" <= session.getAttribute("sessENTERPRISE_PREFIXApproveReq") != null && "MST".equals(session.getAttribute("sessENTERPRISE_PREFIXApproveReq").toString()) ? "selected" : ""%>><script>document.write(global_fm_MST);</script></option>
                                                            <option value="MNS" <= session.getAttribute("sessENTERPRISE_PREFIXApproveReq") != null && "MNS".equals(session.getAttribute("sessENTERPRISE_PREFIXApproveReq").toString()) ? "selected" : ""%>><script>document.write(global_fm_MNS);</script></option>
                                                            <option value="QĐ" <= session.getAttribute("sessENTERPRISE_PREFIXApproveReq") != null && "QĐ".equals(session.getAttribute("sessENTERPRISE_PREFIXApproveReq").toString()) ? "selected" : ""%>><script>document.write(global_fm_decision);</script></option>
                                                        </select>-->
                                                    </div>
                                                </div>
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" id="idLblTooltipEnterprise" style="color: #000000; font-weight: bold;text-align: right;padding-right: 5px;">
                                                            <script>document.write(global_fm_enter);</script> <%= dbTwo.GET_PREFIX_UUID_TOOLTIP("ENTERPRISE", sessLanguageGlobal, isLoadEnterprisePrefix) %>
                                                        </label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <input type="text" name="ENTERPRISE_ID" maxlength="22" value="<%= session.getAttribute("sessENTERPRISE_IDApproveReq") != null ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessENTERPRISE_IDApproveReq").toString()) : ""%>"
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
                                                            <input type="text" name="PERSONAL_NAME" maxlength="150" value="<%= session.getAttribute("sessPERSONAL_NAMEApproveReq") != null ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessPERSONAL_NAMEApproveReq").toString()) : ""%>"
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
                                                            <option value="<%=temp1.PREFIX_DB%>" <%= session.getAttribute("sessPERSONAL_PREFIXApproveReq") != null && temp1.PREFIX_DB.equals(session.getAttribute("sessPERSONAL_PREFIXApproveReq").toString()) ? "selected" : ""%>><%=temp1.REMARK%></option>
                                                            <%
                                                                    }
                                                                }
                                                            %>
                                                        </select>
<!--                                                        <select name="PERSONAL_PREFIX" id="PERSONAL_PREFIX" onclick="changePersonal(this.value);" class="form-control123" style="text-align: left;">
                                                            <option value="CCCD" <= session.getAttribute("sessPERSONAL_PREFIXApproveReq") != null && "CCCD".equals(session.getAttribute("sessPERSONAL_PREFIXApproveReq").toString()) ? "selected" : ""%>><script>document.write(global_fm_CitizenId);</script></option>
                                                            <option value="CMND" <= session.getAttribute("sessPERSONAL_PREFIXApproveReq") != null && "CMND".equals(session.getAttribute("sessPERSONAL_PREFIXApproveReq").toString()) ? "selected" : ""%>><script>document.write(global_fm_CMND);</script></option>
                                                            <option value="HC" <= session.getAttribute("sessPERSONAL_PREFIXApproveReq") != null && "HC".equals(session.getAttribute("sessPERSONAL_PREFIXApproveReq").toString()) ? "selected" : ""%>><script>document.write(global_fm_HC);</script></option>
                                                        </select>-->
                                                    </div>
                                                </div>
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" id="idLblTooltipPersonal" style="color: #000000; font-weight: bold;text-align: right;padding-right: 5px;">
                                                            <script>document.write(global_fm_enter);</script> <%= dbTwo.GET_PREFIX_UUID_TOOLTIP("PERSONAL", sessLanguageGlobal, isLoadPeronalPrefix) %>
                                                        </label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <input type="text" name="PERSONAL_ID" maxlength="22" value="<%= session.getAttribute("sessPERSONAL_IDApproveReq") != null ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessPERSONAL_IDApproveReq").toString()) : ""%>"
                                                                class="form-control123">
                                                        </div>
                                                    </div>
                                                    <script>
                                                        function changePersonal() {
                                                            $("#idLblTooltipPersonal").text(global_fm_enter + $("#PERSONAL_PREFIX option:selected").text());
                                                        }
                                                    </script>
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
                                                            <input type="text" name="TAX_CODE" maxlength="25" value="<%= session.getAttribute("sessTAX_CODEApproveReq") != null ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessTAX_CODEApproveReq").toString()) : ""%>"
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
                                                            <input type="text" name="BUDGET_CODE" maxlength="25" value="<%= session.getAttribute("sessBUDGET_CODEApproveReq") != null ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessBUDGET_CODEApproveReq").toString()) : ""%>"
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
                                                            <input type="text" name="DECISION" maxlength="25" value="<%= session.getAttribute("sessDECISIONApproveReq") != null ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessDECISIONApproveReq").toString()) : ""%>"
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
                                                            <input type="text" name="PERSONAL_NAME" maxlength="150" value="<%= session.getAttribute("sessPERSONAL_NAMEApproveReq") != null ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessPERSONAL_NAMEApproveReq").toString()) : ""%>"
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
                                                            <input type="text" name="P_ID" maxlength="25" value="<%= session.getAttribute("sessP_IDApproveReq") != null ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessP_IDApproveReq").toString()) : ""%>"
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
                                                            <input type="text" name="CCCD" maxlength="25" value="<%= session.getAttribute("sessCCCDApproveReq") != null ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessCCCDApproveReq").toString()) : ""%>"
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
                                                            <input type="text" name="PASSPORT" maxlength="25" value="<%= session.getAttribute("sessPASSPORTApproveReq") != null ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessPASSPORTApproveReq").toString()) : ""%>"
                                                                   class="form-control123">
                                                        </div>
                                                    </div>
                                                </div>
                                                <%
                                                    }
                                                %>
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;padding-right: 5px;">
                                                            <script>document.write(token_fm_tokenid);</script>
                                                        </label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <input type="text" name="TOKEN_ID" maxlength="45" value="<%= session.getAttribute("sessTOKEN_IDApproveReq") != null ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessTOKEN_IDApproveReq").toString()) : ""%>"
                                                                class="form-control123">
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-sm-4" style="padding-left: 0; display: <%=isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA) ? "none" : ""%>">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;padding-right: 5px;">
                                                            <script>document.write(certlist_fm_device_uuid);</script>
                                                        </label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <input type="text" name="DEVICE_UUID_SEARCH" maxlength="150" value="<%= session.getAttribute("sessDEVICE_UUIDApproveReq") != null ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessDEVICE_UUIDApproveReq").toString()) : ""%>"
                                                                class="form-control123">
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-sm-4" style="padding-left: 0; display: <%=isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA) ? "none" : ""%>">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;padding-right: 5px;">
                                                            <script>document.write(global_fm_grid_domain);</script>
                                                        </label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <input type="text" name="DOMAIN_NAME" maxlength="150" value="<%= session.getAttribute("sessDOMAIN_NAMEApproveReq") != null ? EscapeUtils.escapeHtmlSearch(session.getAttribute("sessDOMAIN_NAMEApproveReq").toString()) : ""%>"
                                                                class="form-control123">
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-sm-4" style="padding-left: 0;<%= isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA) && SessRoleID.equals(Definitions.CONFIG_ROLE_ID_CA_SURVEYOR) ? "display:none;" : "" %>">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;padding-right: 5px;">
                                                            <script>document.write(global_fm_Branch);</script>
                                                        </label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <select name="BranchOffice" id="idBranchOffice" class="form-control123">
                                                                <option value="<%= Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL%>" <%= session.getAttribute("sessBranchOfficeApproveReq") != null && Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(session.getAttribute("sessBranchOfficeApproveReq").toString()) ? "selected" : ""%>><script>document.write(global_fm_combox_all);</script></option>
                                                                <%
                                                                    BRANCH[][] rsBranch = (BRANCH[][]) session.getAttribute("sessTreeBranchSystem");
                                                                    if (rsBranch[0].length > 0) {
                                                                        for (BRANCH temp1 : rsBranch[0]) {
                                                                            if(!String.valueOf(temp1.PARENT_ID).equals(Definitions.CONFIG_AGENT_ROOT))
                                                                            {
                                                                %>
                                                                <option value="<%=String.valueOf(temp1.ID)%>" <%= session.getAttribute("sessBranchOfficeApproveReq") != null && String.valueOf(temp1.ID).equals(session.getAttribute("sessBranchOfficeApproveReq").toString()) ? "selected" : ""%>><%= temp1.NAME + " - " + temp1.REMARK%></option>
                                                                <%
                                                                            }
                                                                        }
                                                                    }
                                                                %>
                                                            </select>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-sm-4" style="padding-left: 0; <%= !"1".equals(isActiveSignServer) ? "display: none;" : ""%>" id="idViewHSMDeclineConfirm">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;padding-right: 5px;">
                                                            <script>document.write(global_fm_confirm);</script>
                                                        </label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <select name="HSM_CONFIRM" id="HSM_CONFIRM" class="form-control123">
                                                                <option value="<%= Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL%>" <%= session.getAttribute("sessActiveEnabledApproveReq") != null && Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(session.getAttribute("sessActiveEnabledApproveReq").toString()) ? "selected" : ""%>><script>document.write(global_fm_combox_all);</script></option>
                                                                <option value="0" <%= session.getAttribute("sessActiveEnabledApproveReq") != null && "0".equals(session.getAttribute("sessActiveEnabledApproveReq").toString()) ? "selected" : ""%>><script>document.write(hsm_confirm_not_confirm);</script></option>
                                                                <option value="2" <%= session.getAttribute("sessActiveEnabledApproveReq") != null && "2".equals(session.getAttribute("sessActiveEnabledApproveReq").toString()) ? "selected" : ""%>><script>document.write(global_fm_button_decline);</script></option>
                                                            </select>
                                                        </div>
                                                    </div>
                                                </div>
                                                <script>
                                                    $(document).ready(function () {
                                                        $("#idViewHSMDeclineConfirm").css("display","none");
                                                        if($("#FORM_FACTOR_LIST").val() === JS_STR_PKI_FORMFACTOR_ID_SOFT_TOKEN){
                                                            if('<%=isActiveSignServer%>' === '1') {
                                                                $("#idViewHSMDeclineConfirm").css("display","");
                                                            }
                                                        }
                                                    });
                                                    function onChangeHSMConfirm(obj){
                                                        if(obj === JS_STR_PKI_FORMFACTOR_ID_SOFT_TOKEN) {
                                                            if('<%=isActiveSignServer%>' === '1') {
                                                                $("#idViewHSMDeclineConfirm").css("display","");
                                                            }
                                                        } else {
                                                            $("#idViewHSMDeclineConfirm").css("display","none");
                                                        }
                                                    }
                                                </script>
                                                <div class="col-sm-4" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: right;padding-right: 5px;">

                                                        </label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">

                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                            <input type="hidden" name="CsrfToken" value="<%=anticsrf%>"/>
                                            <input type="hidden" id="tempCsrfToken" name="tempCsrfToken"/>
                                            <input id="idHiddenLoad" name="nameHiddenLoad" value="" type="hidden"/>
                                        </form>
                                    </div>
                                </div>
                                <%
                                    if (status == 1 && statusLoad == 1) {
                                        String sDeviceDomainShow = conf.GetPropertybyCode(Definitions.CONFIG_DEVICE_DOMAIN_GRID_SHOW_ENABLED);
                                %>
                                <script type="text/javascript">
                                    $(document).ready(function () {
                                        goToByScroll("idShowResultSearch");
                                    });
                                    $(document).ready(function () {
                                        if(localStorage.getItem("CountCheckAllDeclineMilti") === "1")
                                        {
                                            $('tbody tr td input[type="checkbox"]').prop('checked', true);
                                            $('tbody tr').css('background', '#d8d8d8');
                                            localStorage.setItem("CountCheckAllDeclineMilti", "1");
                                            $('#checkAll').prop('checked', true);
                                            CheckAllTableValue();
                                            $("#divEditTokenMulti").css("display", "");
                                        }
                                        else
                                        {
                                            $('tbody tr').css('background', '');
                                            localStorage.setItem("CountCheckAllDeclineMilti", "");
                                            $('#checkAll').prop('checked', false);
                                        }
                                        if(localStorage.getItem("CountCheckDeclineMilti") !== "")
                                        {
                                            ForCheckTable(localStorage.getItem("CountCheckDeclineMilti"));
                                            $("#divEditTokenMulti").css("display", "");
                                        }
                                        $('tr').click(function () {
                                            if (this.style.background === "" || this.style.background === "white") {
                                                $(this).css('background', '');
                                            }
                                            else {
                                                $(this).css('background', 'white');
                                            }
                                        });
                                        $(".uncheck").change(function () {
                                            var sValueCheck = localStorage.getItem("CountCheckDeclineMilti");
                                            var ischecked = $(this).is(':checked');
                                            if (!ischecked) {
                                                $(this).closest('tr').css('background', '');
                                                var sss = sSpace($(this).closest('tr').find("td:eq(1)").text()) + ",";
                                                sValueCheck = sValueCheck.replace(sss, "");
                                                $('#checkAll').prop('checked', false);
                                                localStorage.setItem("CountCheckAllDeclineMilti", "");
                                            }
                                            else
                                            {
                                                $(this).closest('tr').css('background', '#d8d8d8');
                                                var sCheck = sSpace($(this).closest('tr').find("td:eq(1)").text()) + ",";
                                                if(sValueCheck.indexOf(sCheck) === -1)
                                                {
                                                    sValueCheck = sValueCheck + sCheck;
                                                }
                                                $("#divEditTokenMulti").css("display", "");
                                            }
                                            localStorage.setItem("CountCheckDeclineMilti", sValueCheck);
                                        });
                                    });
                                    function ForCheckTable(sValueCheck)
                                    {
                                        $('#mtToken').find('tr').each(function () {
                                            var sCheck = sSpace($(this).find('td:eq(1)').text());
                                            if(sCheck !== "")
                                            {
                                                if (sValueCheck.indexOf(sCheck + ",") !== -1)
                                                {
                                                    $(this).closest('tr').css('background', '#d8d8d8');
                                                    $('#checkChilren' + sCheck).prop('checked', true);
                                                }
                                            }
                                        });
                                    }
                                    function CheckAllTableValue()
                                    {
                                        var currentCheck = localStorage.getItem("CountCheckDeclineMilti");
                                        $('#mtToken').find('tr').each(function () {
                                            var sCheck = sSpace($(this).find('td:eq(1)').text());
                                            if(sCheck !== "" && currentCheck.indexOf(sCheck + ',') === -1)
                                            {
                                                currentCheck = currentCheck + sCheck + ",";
                                            }
                                        });
                                        localStorage.setItem("CountCheckDeclineMilti", currentCheck);
                                    }
                                    function checkAllTable()
                                    {
                                        $('#checkAll').change(function () {
                                            $('tbody tr td input[type="checkbox"]').prop('checked', $(this).prop('checked'));
                                            $('tbody tr').css('background', '#d8d8d8');
                                            if ($("#checkAll").is(':checked')) {
                                                CheckAllTableValue();
                                                localStorage.setItem("CountCheckAllDeclineMilti", "1");
                                                $("#divEditTokenMulti").css("display", "");
                                            }
                                            else
                                            {
                                                localStorage.setItem("CountCheckDeclineMilti", "");
                                                localStorage.setItem("CountCheckAllDeclineMilti", "");
                                                $('tbody tr').css('background', '');
                                            }
                                        });
                                    }
                                    function stopRKey(evt) {
                                        var evt = (evt) ? evt : ((event) ? event : null);
                                        var node = (evt.target) ? evt.target : ((evt.srcElement) ? evt.srcElement : null);
                                        if ((evt.keyCode === 13) && (node.type === "text")) {
                                            return false;
                                        }
                                    }
                                    document.onkeypress = stopRKey;
                                </script>
                                
                                <div class="x_panel" id="idShowResultSearch">
                                    <div class="x_title" style="border-bottom: 0 solid #E6E9ED;margin-bottom: 0px;">
                                        <h2><i class="fa fa-list-ul"></i> <script>document.write(cert_title_table);</script></h2>
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
                                                <button type="button" class="btn btn-info" onClick="declineMultiple('<%=anticsrf%>');"><script>document.write(global_fm_button_decline);</script></button>
                                                <script>
                                                    function declineMultiple(idCSRF)
                                                    {
                                                        var sCheckCheckAll = "0";
                                                        if ($("#checkAll").is(':checked')) {
                                                            sCheckCheckAll = "1";
                                                        }
                                                        if(sCheckCheckAll === "0")
                                                        {
                                                            if(localStorage.getItem("CountCheckDeclineMilti") === "")
                                                            {
                                                                funErrorAlert(global_succ_NoCheck);
                                                                return false;
                                                            }
                                                        }
                                                        //console.log(localStorage.getItem("CountCheckDeclineMilti"));
                                                        swal({
                                                            title: "",
                                                            text: synchneac_conform_decline_multi,
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
                                                            $('body').append('<div id="over"></div>');
                                                            $(".loading-gif").show();
                                                            setTimeout(function () {
                                                                $.ajax({
                                                                    type: "post",
                                                                    url: "../ReqApproveDeclineCommon",
                                                                    data: {
                                                                        idParam: "declinecertmulti",
                                                                        idValue: localStorage.getItem("CountCheckDeclineMilti"),
                                                                        isCheckAll: sCheckCheckAll,
                                                                        CsrfToken: idCSRF
                                                                    },
                                                                    catche: false,
                                                                    success: function (html) {
                                                                        var arr = sSpace(html).split('#');
                                                                        if (arr[0] === "0")
                                                                        {
                                                                            localStorage.setItem("CountCheckDeclineMilti", "");
                                                                            localStorage.setItem("CountCheckAllDeclineMilti", "");
                                                                            funSuccAlert(token_succ_edit, "CertificateList.jsp");
                                                                        }
                                                                        else if (arr[0] === "10")
                                                                        {
                                                                            funErrorAlert(global_req_all);
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
                                                                        else if (arr[0] === JS_EX_WRONG_AGENCY) {
                                                                            RedirectPageLoginNoSess(global_error_wrong_agency);
                                                                        }
                                                                        else if (arr[0] === "1") {
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
                                                            }, JS_STR_ACTION_TIMEOUT);
                                                        });
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
                                            <th><input id="checkAll" name="checkAll" onclick="checkAllTable();" type="checkbox"/></th>
                                            <th><script>document.write(global_fm_action);</script></th>
                                            <th><script>document.write(global_fm_STT);</script></th>
                                            <th style="width: 135px;"><script>document.write(global_fm_grid_company);</script></th>
                                            <th style="width: 100px;"><script>document.write(global_fm_enterprise_id);</script></th>
                                            <th style="width: 120px;"><script>document.write(global_fm_grid_personal);</script></th>
                                            <th style="width: 100px;"><script>document.write(global_fm_personal_id);</script></th>
                                            <%
                                                if("1".equals(sDeviceDomainShow)) {
                                            %>
                                            <th style="width: 120px;"><script>document.write(global_fm_grid_domain);</script></th>
                                            <%
                                                } else {
                                            %>
                                            <th style="width: 120px;"><script>document.write(global_fm_duration_cts);</script></th>
                                            <%
                                                }
                                            %>
                                            <th><script>document.write(global_fm_requesttype);</script></th>
                                            <th><script>document.write(global_fm_certtype);</script></th>
                                            <th><script>document.write(global_fm_Status_request);</script></th>
                                            <th><script>document.write(global_fm_certstatus);</script></th>
                                            <th><script>document.write(global_fm_status_profile);</script></th>
                                            <%
                                                if(!isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_NC)) {
                                            %>
                                            <th><script>document.write(token_fm_agent_level_one);</script></th>
                                            <% } %>
                                            <th><script>document.write(token_fm_agent);</script></th>
                                            <th><script>document.write(global_fm_date_create);</script></th>
                                            <th><script>document.write(global_fm_date_gencert);</script></th>
                                            <%
                                                if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_NC)) {
                                            %>
                                            <th><script>document.write(global_fm_Expire_cert);</script></th>
                                            <th><script>document.write(global_fm_Status_token);</script></th>
                                            <th><script>document.write(global_fm_user_lock_unlock_token);</script></th>
                                            <% } %>
                                            </thead>
                                            <tbody>
                                                <%
                                                    if (iPaNoSS > 1) {
                                                        j = ((iPaNoSS - 1) * iSwRws) + 1;
                                                    }
                                                    session.setAttribute("RefreshApproveReqSessNumberPaging", String.valueOf(iPaNoSS));
                                                    if (rsPgin[0].length > 0) {
                                                        for (CERTIFICATION temp1 : rsPgin[0]) {
                                                            String sCERTIFICATION_ATTR_ID = String.valueOf(temp1.CERTIFICATION_ATTR_ID);
                                                            String sDisabled = "1";
                                                            String sColor = "EMPTY";
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
                                                                if(isDeclineAgent == true) {
                                                                    sDisabled = "";
                                                                }
                                                            } else if(temp1.CERTIFICATION_ATTR_STATE_ID != Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_COMMITED
                                                                && temp1.CERTIFICATION_ATTR_STATE_ID != Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_DECLINED
                                                                && temp1.CERTIFICATION_ATTR_STATE_ID != Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_ISSUED) {
                                                                if(SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                                                    if(temp1.CERTIFICATION_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_APPROVED
                                                                        && temp1.CERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REVOKE
                                                                        && temp1.CERTIFICATION_STATE_ID == Definitions.CONFIG_CERTIFICATION_STATE_REVOKED)
                                                                    { } else {
                                                                        if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_DECLINED,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true)
                                                                        {
                                                                            sDisabled = "";
                                                                        }
                                                                    }
                                                                }
                                                            } else if(temp1.CERTIFICATION_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_ISSUED) {
                                                                if(SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                                                    if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_DECLINED,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true)
                                                                    {
                                                                        sDisabled = "";
                                                                    }
                                                                }
                                                            }
                                                            if(sDisabled == "") {
                                                                sDefaulCheckAll = "1";
                                                            }
                                                %>
                                                <tr>
                                                    <td>
                                                        <%
                                                            if("".equals(sDisabled)) {
                                                        %>
                                                        <input id="checkChilren<%= Definitions.CONFIG_GRID_TAG_VALUE_CHECKBOX + sCERTIFICATION_ATTR_ID%>" value="value-<%= sCERTIFICATION_ATTR_ID%>" name="checkChilren" class='uncheck' type="checkbox"/>
                                                        <%
                                                            } else {
                                                        %>
                                                        <img src="../Images/icon_checkboxdisable.png" id="idImgCheckBox"/>
                                                        <%
                                                            }
                                                        %>
                                                    </td>
                                                    <td style="display: none;">
                                                        <%
                                                            if("".equals(sDisabled)) {
                                                        %>
                                                        <%= Definitions.CONFIG_GRID_TAG_VALUE_CHECKBOX + sCERTIFICATION_ATTR_ID%>
                                                        <%
                                                            } else {
                                                        %>
                                                        <%=sColor%>
                                                        <%
                                                            }
                                                        %>
                                                    </td>
                                                    <td>
                                                        <a style="cursor: pointer;margin-bottom: 2px;" onclick="popupDetail('<%=sCERTIFICATION_ATTR_ID%>');" class="btn btn-info btn-xs"><i class="fa fa-pencil"></i> <script>document.write(global_fm_button_detail);</script> </a>
                                                        <%
                                                            if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA)) {
                                                                if (temp1.CERTIFICATION_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_PENDING
                                                                    || temp1.CERTIFICATION_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_INIT) {
                                                                    if(!SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                                        %>
                                                        <%
                                                            if(SessRoleID.equals(Definitions.CONFIG_ROLE_ID_AGENT_USER) || SessRoleID.equals(Definitions.CONFIG_ROLE_ID_AGENT_ACCOUNTANT)) {
                                                                if(SessLevelBranch.equals(Definitions.CONFIG_BRANCH_LEVEL_CHILREN_ONE)) {
                                                                    if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_PRE_APPROVED,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true)
                                                                    {
                                                        %>
                                                        <span><a style="cursor: pointer;" onclick="ApproveFormAgency('<%= sCERTIFICATION_ATTR_ID%>', '<%=anticsrf%>');" class="btn btn-info btn-xs"><i class="fa fa-pencil"></i> <script>document.write(global_fm_approve);</script> </a></span>
<!--                                                        <input type="button" id="btnApprove" class="btn btn-info" onclick="" />
                                                        <script>document.getElementById("btnApprove").value = global_fm_approve;</script>-->
                                                        <%
                                                                }
                                                            } else {
                                                                int intApprove = db.S_BO_CHECK_BRANCH_APPROVED(Integer.parseInt(sCERTIFICATION_ATTR_ID), Integer.parseInt(SessUserAgentID), sessTreeArrayBranchID);
                                                                if(intApprove == 1) {
                                                                    if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_IN_AGENCY_APPROVED,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true)
                                                                    {
                                                        %>
                                                                    <a style="cursor: pointer;" onclick="ApproveFormChildrenAgency('<%= sCERTIFICATION_ATTR_ID%>', '<%=anticsrf%>');" class="btn btn-info btn-xs"><i class="fa fa-pencil"></i> <script>document.write(global_fm_approve);</script> </a>
<!--                                                                    <input type="button" id="btnApprove" class="btn btn-info" onclick="" />
                                                                    <script>document.getElementById("btnApprove").value = global_fm_approve;</script>-->
                                                        <%
                                                                        }
                                                                    }
                                                                }
                                                            } else {
                                                                if(SessLevelBranch.equals(Definitions.CONFIG_BRANCH_LEVEL_CHILREN_ONE)) {
                                                        %>
                                                        <span><a style="cursor: pointer;" onclick="ApproveFormAgency('<%= sCERTIFICATION_ATTR_ID%>', '<%=anticsrf%>');" class="btn btn-info btn-xs"><i class="fa fa-pencil"></i> <script>document.write(global_fm_approve);</script> </a></span>
<!--                                                        <input type="button" id="btnApprove" class="btn btn-info" onclick="" />
                                                        <script>document.getElementById("btnApprove").value = global_fm_approve;</script>-->
                                                        <%
                                                                } else {
                                                                    int intApprove = db.S_BO_CHECK_BRANCH_APPROVED(Integer.parseInt(sCERTIFICATION_ATTR_ID), Integer.parseInt(SessUserAgentID), sessTreeArrayBranchID);
                                                                    if(intApprove == 1) {
                                                        %>
                                                                        <a style="cursor: pointer;" onclick="ApproveFormChildrenAgency('<%= sCERTIFICATION_ATTR_ID%>', '<%=anticsrf%>');" class="btn btn-info btn-xs"><i class="fa fa-pencil"></i> <script>document.write(global_fm_approve);</script> </a>
<!--                                                                        <input type="button" id="btnApprove" class="btn btn-info" onclick="" />
                                                                        <script>document.getElementById("btnApprove").value = global_fm_approve;</script>-->
                                                        <%
                                                                    }
                                                                }
                                                            }
                                                        %>
                                                        <%
                                                            } else {
                                                                if(SessRoleID.equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN)
                                                                    || SessRoleID.equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN_CHILD)
                                                                    || SessRoleID.equals(Definitions.CONFIG_ROLE_ID_CA_SURVEYOR))
                                                                {
                                                        %>
                                                        <span><a style="cursor: pointer;" id="idApproveDirectCA" onclick="ApproveFormCAList('<%= sCERTIFICATION_ATTR_ID%>', '<%=anticsrf%>');" class="btn btn-info btn-xs"><i class="fa fa-pencil"></i> <script>document.write(global_fm_approve);</script> </a></span>
<!--                                                        <input type="button" id="btnApprove" class="btn btn-info" onclick="" />
                                                        <script>document.getElementById("btnApprove").value = global_fm_approve;</script>-->
                                                        <%
                                                            } else {
                                                                if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_APPROVED_CERT,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true) {
                                                        %>
                                                        <span><a style="cursor: pointer;" id="idApproveDirectCA" onclick="ApproveFormCAList('<%= sCERTIFICATION_ATTR_ID%>', '<%=anticsrf%>');" class="btn btn-info btn-xs"><i class="fa fa-pencil"></i> <script>document.write(global_fm_approve);</script> </a></span>
<!--                                                        <input type="button" id="btnApprove" class="btn btn-info" onclick="" />
                                                        <script>document.getElementById("btnApprove").value = global_fm_approve;</script>-->
                                                        <%
                                                                        }
                                                                    }
                                                                }
                                                            } else if(temp1.CERTIFICATION_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_PRE_APPROVED) {
                                                                if(SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                                        %>
                                                        <%
                                                            if(SessRoleID.equals(Definitions.CONFIG_ROLE_ID_CA_USER) || SessRoleID.equals(Definitions.CONFIG_ROLE_ID_CA_ACCOUNTANT)) {
                                                                if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_APPROVED_CERT,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true) {
                                                        %>
                                                        <span><a style="cursor: pointer;" id="idApproveDirectCA" onclick="ApproveFormCAList('<%= sCERTIFICATION_ATTR_ID%>', '<%=anticsrf%>');" class="btn btn-info btn-xs"><i class="fa fa-pencil"></i> <script>document.write(global_fm_approve);</script> </a></span>
<!--                                                        <input type="button" id="btnApprove" class="btn btn-info" onclick="ApproveFormCAList('<= sCERTIFICATION_ATTR_ID%>', '<%=anticsrf%>');" />
                                                        <script>document.getElementById("btnApprove").value = global_fm_approve;</script>-->
                                                        <%
                                                                }
                                                            } else {
                                                        %>
                                                        <span><a style="cursor: pointer;" id="idApproveDirectCA" onclick="ApproveFormCAList('<%= sCERTIFICATION_ATTR_ID%>', '<%=anticsrf%>');" class="btn btn-info btn-xs"><i class="fa fa-pencil"></i> <script>document.write(global_fm_approve);</script> </a></span>
<!--                                                        <input type="button" id="btnApprove" class="btn btn-info" onclick="ApproveFormCAList('<= sCERTIFICATION_ATTR_ID%>', '<%=anticsrf%>');" />
                                                        <script>document.getElementById("btnApprove").value = global_fm_approve;</script>-->
                                                        <%
                                                            }
                                                        %>
                                                        <%
                                                                    }
                                                                } else {}
                                                            }
                                                        %>                                                        
                                                        <%
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
                                                        <span><a style="cursor: pointer;" onclick="popupDialogCertDecline('<%=sCERTIFICATION_ATTR_ID%>', '<%= String.valueOf(temp1.BRANCH_ID)%>', '<%= String.valueOf(temp1.CREATED_BY_ID)%>');" class="btn btn-info btn-xs"><i class="fa fa-pencil"></i> <script>document.write(global_fm_button_decline);</script> </a></span>
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
                                                        <span><a style="cursor: pointer;" onclick="popupDialogCertDecline('<%=sCERTIFICATION_ATTR_ID%>', '<%= String.valueOf(temp1.BRANCH_ID)%>', '<%= String.valueOf(temp1.CREATED_BY_ID)%>');" class="btn btn-info btn-xs"><i class="fa fa-pencil"></i> <script>document.write(global_fm_button_decline);</script> </a></span>
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
                                                        <span><a style="cursor: pointer;" onclick="popupDialogCertDecline('<%=sCERTIFICATION_ATTR_ID%>', '<%= String.valueOf(temp1.BRANCH_ID)%>', '<%= String.valueOf(temp1.CREATED_BY_ID)%>');" class="btn btn-info btn-xs"><i class="fa fa-pencil"></i> <script>document.write(global_fm_button_decline);</script> </a></span>
                                                        <%
                                                                    }
                                                                }
                                                            }
                                                        %>
                                                    </td>
                                                    <td style="text-align: center;"><%= com.convertMoney(j)%></td>
                                                    <td><%= EscapeUtils.CheckTextNull(temp1.COMPANY_NAME)%></td>
                                                    <td><a style="color: blue;" data-toggle="tooltipPrefix" title="<%= temp1.ENTERPRISE_ID_REMARK%>"><%= temp1.ENTERPRISE_ID%></a></td>
                                                    <td><%= EscapeUtils.CheckTextNull(temp1.PERSONAL_NAME)%></td>
                                                    <td><a style="color: blue;" data-toggle="tooltipPrefix" title="<%= temp1.PERSONAL_ID_REMARK%>"><%= temp1.PERSONAL_ID%></a></td>
                                                    <%
                                                        if("1".equals(sDeviceDomainShow)) {
                                                    %>
                                                    <td><%= EscapeUtils.CheckTextNull(temp1.DOMAIN_NAME)%></td>
                                                    <%
                                                        } else {
                                                    %>
                                                    <td><%= EscapeUtils.CheckTextNull(temp1.CERTIFICATION_PROFILE_NAME)%></td>
                                                    <%
                                                        }
                                                    %>
                                                    <td><%= EscapeUtils.CheckTextNull(temp1.CERTIFICATION_ATTR_TYPE_DESC)%></td>
                                                    <td><%= EscapeUtils.CheckTextNull(temp1.CERTIFICATION_PURPOSE_DESC)%></td>
                                                    <td><%= EscapeUtils.CheckTextNull(temp1.CERTIFICATION_ATTR_STATE_DESC)%></td>
                                                    <td><%= EscapeUtils.CheckTextNull(temp1.CERTIFICATION_STATE_DESC)%></td>
                                                    <td><%= EscapeUtils.CheckTextNull(temp1.FILE_MANAGER_STATE_DESC)%></td>
                                                    <%
                                                        if(!isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_NC)) {
                                                    %>
                                                    <td><%= temp1.BRANCH_LEVEL_1_NAME%></td>
                                                    <% } %>
                                                    <td><%= EscapeUtils.CheckTextNull(temp1.BRANCH_DESC)%></td>
                                                    <td><%= EscapeUtils.CheckTextNull(temp1.CREATED_DT)%></td>
                                                    <td><%= EscapeUtils.CheckTextNull(temp1.OPERATED_DT)%></td>
                                                    <%
                                                        if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_NC)) {
                                                    %>
                                                    <td><%= EscapeUtils.CheckTextNull(temp1.EXPIRATION_DT)%></td>
                                                    <td><%= EscapeUtils.CheckTextNull(temp1.TOKEN_STATE_DESC)%></td>
                                                    <td><%= EscapeUtils.CheckTextNull(temp1.USER_CREATE_UNLOCK_LOCK)%></td>
                                                    <% } %>
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
                                                        <td id="idTDPaging" style="text-align: right;">
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
                    <!-- Modal Cert Decline -->
                    <div id="myModalCertDecline" class="modal fade" role="dialog">
                        <div style="width: 100%; text-align: center; position: fixed;z-index: 1000;top: 0; padding-top: 90px;
                             left: 0; height: 100%;" class="loading-gifCertDecline">
                            <img src="../Images/ajax-loader1.gif" alt="Please wait..." />
                        </div>
                        <div class="modal-dialog modal-800" id="myDialogCertDecline">
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
                        $(document).ready(function () {
                            if('<%= strExpandFilter%>' === "1")
                            {
                                popupViewCSRSList();
                            }
                            if('<%=sDefaulCheckAll%>' === "") {
                                $("#checkAll").attr('disabled','disabled');
                            }
                        });
                        function reloadApproveFunction(id) {
                            document.getElementById("idApproveDirectCA").onclick = function() {
                                ApproveFormCAList(id, document.getElementById("CsrfTokenDetail").value);
                            };
                            console.log("anticsrf_CALL: " + '<%=anticsrf%>');
                        }
                    </script>
                </div>
                <%@ include file="../Modules/Footer.jsp" %>
            </div>
            <script src="../style/jquery.min.js"></script>
            <script src="../style/bootstrap.min.js"></script>
            <script src="../style/custom.min.js"></script>
            <script src="../js/moment.min.js"></script>
            <!--<script src="../style/icheck/icheck.min.js"></script>-->
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