<%-- 
    Document   : TokenEdit
    Created on : Jun 26, 2018, 5:16:23 PM
    Author     : THANH-PC
--%>

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
        <!--<script type="text/javascript" src="../js/jquery.js"></script>-->
        <link rel="stylesheet" href="../js/sweetalert.css"/>
        <script src="../js/sweetalert-dev.js"></script>
        <script type="text/javascript" src="../Css/GlobalAlert.js"></script>
        <script type='text/javascript' src='../Css/jscolor.js'></script>
        <link href="../Css/smartpaginator.css" rel="stylesheet" type="text/css"/>
        <script src="../Css/smartpaginator.js" type="text/javascript"></script>
        <!--<link rel="stylesheet" type="text/css" media="all" href="../js/daterangepicker.css" />-->
        <title></title>
        <script type="text/javascript">
            changeFavicon("../");
            document.title = token_title_edit;
            $(document).ready(function () {
                $('.loading-gif').hide();
                localStorage.setItem("storeAnticsrfTokenList", null);
                $("#strIS_PUSH_NOTICE_SET_NO").prop("disabled", true);
                $("#strIS_MENU_LINK_SET_NO").prop("disabled", true);
                $("#strIS_PUSH_NOTICEEdit").prop('checked', false);
                $("#strIS_PUSH_NOTICE_SET_NO").prop('checked', false);
                $("#strIS_MENU_LINKEdit").prop('checked', false);
                $("#strIS_MENU_LINK_SET_NO").prop('checked', false);
                $("#strIS_UNLOCKEdit").prop('checked', false);
                $("#strIS_LOCKEdit").prop('checked', false);
                $("#strACTIVE_FLAGEdit").prop('checked', false);
                $("#strCOLOR_TEXT_EditForm").prop("disabled", true);
                $("#strCOLOR_BKGR_EditForm").prop("disabled", true);
                $('[data-toggle="tooltipPrefix"]').tooltip();
            });
            function ValidateForm(idCSRF) {
                var vChooseFunction= "0"; 
                var sValueLINK_NOTICE = "";
                var sValueCOLOR_TEXT = "";
                var sValueCOLOR_BKGR = "";
                var sValueNOTICE_INFO = "";
                var vIS_PUSH_NOTICE_SET_NO = "0";
                if ($("#strIS_PUSH_NOTICE_SET_NO").is(':checked'))
                {
                    vIS_PUSH_NOTICE_SET_NO = "1";
                    vChooseFunction= "1";
                }
                if ($("#strIS_PUSH_NOTICEEdit").is(':checked')) {
                    if (!JSCheckEmptyField(document.getElementById("strLINK_NOTICE_EditForm").value))
                    {
                        document.getElementById("strLINK_NOTICE_EditForm").focus();
                        funErrorAlert(policy_req_empty + token_fm_noticelink);
                        return false;
                    }
                    if (!JSCheckEmptyField(document.getElementById("strNOTICE_INFO_EditForm").value))
                    {
                        document.getElementById("strNOTICE_INFO_EditForm").focus();
                        funErrorAlert(policy_req_empty + token_fm_noticeinfor);
                        return false;
                    }
                    if(sSpace(document.getElementById("strLINK_NOTICE_EditForm").value) !== sSpace($("#strLINK_NOTICEEdit").val()))
                    {
                        vChooseFunction= "1";
                    }
                    if(sSpace(document.getElementById("strNOTICE_INFO_EditForm").value) !== sSpace($("#strNOTICE_INFOEdit").val()))
                    {
                        vChooseFunction= "1";
                    }
                    if(sSpace(document.getElementById("strCOLOR_TEXT_EditForm").value) !== sSpace($("#strCOLOR_TEXTEdit").val()))
                    {
                        vChooseFunction= "1";
                    }
                    if(sSpace($("#strCOLOR_BKGR_EditForm").val()) !== sSpace($("#strCOLOR_BKGREdit").val()))
                    {
                        vChooseFunction= "1";
                    }
                    sValueLINK_NOTICE = document.getElementById("strLINK_NOTICE_EditForm").value;
                    sValueNOTICE_INFO = document.getElementById("strNOTICE_INFO_EditForm").value;
                    sValueCOLOR_TEXT = document.getElementById("strCOLOR_TEXT_EditForm").value;
                    sValueCOLOR_BKGR = $("#strCOLOR_BKGR_EditForm").val();
                }
                var sValueNAME_LINK = "";
                var sValueLINK_VALUE = "";
                var vIS_MENU_LINK_SET_NO = "0";
                if ($("#strIS_MENU_LINK_SET_NO").is(':checked'))
                {
                    vIS_MENU_LINK_SET_NO = "1";
                    vChooseFunction= "1";
                }
                if ($("#strIS_MENU_LINKEdit").is(':checked')) {
                    if (!JSCheckEmptyField(document.getElementById("strNAME_LINK_EditForm").value))
                    {
                        document.getElementById("strNAME_LINK_EditForm").focus();
                        funErrorAlert(policy_req_empty + token_fm_linkname);
                        return false;
                    }
                    if (!JSCheckEmptyField(document.getElementById("strLINK_VALUE_EditForm").value))
                    {
                        document.getElementById("strLINK_VALUE_EditForm").focus();
                        funErrorAlert(policy_req_empty + token_fm_linkvalue);
                        return false;
                    } else {
                        if(!JSCheckLinkURLFormat(document.getElementById("strLINK_VALUE_EditForm").value))
                        {
                            document.getElementById("strLINK_VALUE_EditForm").focus();
                            funErrorAlert(global_req_format_url + token_fm_linkvalue);
                            return false;
                        }
                    }
                    if(sSpace($("#strNAME_LINK_EditForm").val()) !== sSpace($("#sstrNAME_LINKEdit").val()))
                    {
                        vChooseFunction= "1";
                    }
                    if(sSpace($("#strLINK_VALUE_EditForm").val()) !== sSpace($("#sstrLINK_VALUEEdit").val()))
                    {
                        vChooseFunction= "1"; 
                    }
                    sValueNAME_LINK = sSpace($("#strNAME_LINK_EditForm").val());
                    sValueLINK_VALUE = sSpace($("#strLINK_VALUE_EditForm").val());
                }
                var sCheckIS_LOCK = "0";
                if ($("#strIS_LOCKEdit").is(':checked')) {
                    sCheckIS_LOCK = "1";
                    vChooseFunction= "1";
                }
                var sCheckIS_UNLOCK = "0";
                if ($("#strIS_UNLOCKEdit").is(':checked')) {
                    sCheckIS_UNLOCK = "1";
                    vChooseFunction= "1";
                }
                var sCheckActiveFlag = "0";
                if ($("#strACTIVE_FLAGEdit").is(':checked')) {
                    sCheckActiveFlag = "1";
                    vChooseFunction= "1";
                }
                
                var sCheckRE_OPERATION = "0";
                if ($("#strRE_OPERATIONEdit").is(':checked')) {
                    sCheckRE_OPERATION = "1";
                    vChooseFunction= "1";
                }
                
                var sValueAGENT_ID = "";
                if(sSpace($("#strAGENT_IDEdit").val()) !== sSpace($("#strAGENT_ID").val()))
                {
                    sValueAGENT_ID = sSpace($("#strAGENT_ID").val());
                    vChooseFunction= "1";
                }
                if(vChooseFunction === "1")
                {
                    $('body').append('<div id="over"></div>');
                    $(".loading-gif").show();
                    $.ajax({
                        type: "post",
                        url: "../TokenCommon",
                        data: {
                            idParam: 'edittoken',
                            ID: $("#strID").val(),
                            TOKEN_ID: $("#TOKEN_SN").val(),//document.myname.TOKEN_SN.value,
                            AGENT_ID_LOG: $("#strstrAGENT_ID_LOGID").val(),//document.myname.strAGENT_ID_LOG.value,
                            LINK_NOTICE: sValueLINK_NOTICE,
                            COLOR_TEXT: sValueCOLOR_TEXT,
                            COLOR_BKGR: sValueCOLOR_BKGR,
                            NOTICE_INFO: sValueNOTICE_INFO,
                            NAME_LINK: sValueNAME_LINK,
                            LINK_VALUE: sValueLINK_VALUE,
                            AGENT_ID: sValueAGENT_ID,
                            IS_UNLOCK: sCheckIS_UNLOCK,
                            IS_LOCK: sCheckIS_LOCK,
                            LOCK_REASON: $("#idLockReason").val(),
                            vIS_PUSH_NOTICE_SET_NO: vIS_PUSH_NOTICE_SET_NO,
                            vIS_MENU_LINK_SET_NO: vIS_MENU_LINK_SET_NO,
                            IS_LOST: sCheckActiveFlag,
                            IS_RE_OPERATION: sCheckRE_OPERATION,
                            CsrfToken: idCSRF
                        },
                        cache: false,
                        success: function (html) {
                            var arr = sSpace(html).split('#');
                            if (arr[0] === "0")
                            {
                                localStorage.setItem("EDIT_TOKEN", $("#strID").val());
                                funSuccAlert(token_succ_edit, "TokenList.jsp");
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
                            else
                            {
                                funErrorAlert(global_errorsql);
                            }
                            $(".loading-gif").hide();
                            $('#over').remove();
                        }
                    });
                    return false;
                } else {
                    funErrorAlert(global_req_enter_info_change);
                }
            }
            function popupPushNotice(idCSRF)
            {
                $('body').append('<div id="over"></div>');
                $(".loading-gif").show();
                $.ajax({
                    type: "post",
                    url: "../TokenCommon",
                    data: {
                        idParam: 'editpushnotice',
                        ID: $("#strID").val(),
                        CsrfToken: idCSRF
                    },
                    cache: false,
                    success: function (html) {
                        var arr = sSpace(html).split('#');
                        if (arr[0] === "0")
                        {
                            localStorage.setItem("EDIT_TOKEN_PUSH", $("#strID").val());
                            funSuccLocalAlert(token_succ_edit);
                        }
                        else if (arr[0] === '1') {
                            funErrorAlert(global_alert_another_menu);
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
            function popupDelete(idCSRF)
            {
                swal({
                    title: "",
                    text: token_confirm_delete,
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
                            url: "../TokenCommon",
                            data: {
                                idParam: 'deletetoken',
                                ID: $("#strID").val(),
                                CsrfToken: idCSRF
                            },
                            cache: false,
                            success: function (html) {
                                var arr = sSpace(html).split('#');
                                if (arr[0] === "0") {
                                    funSuccAlert(token_succ_delete, "TokenList.jsp");
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
                                else if (arr[0] === JS_EX_WRONG_ROLE) {
                                    RedirectPageLoginNoSess(global_error_wrong_role);
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
            function closeForm()
            {
                $.ajax({
                    type: "post",
                    url: "../UserCommon",
                    data: {
                        idParam: 'backformpage',
                        idSession: 'RefreshTokenSess'
                    },
                    cache: false,
                    success: function (html) {
                        var arr = sSpace(html);
                        if (arr === "0")
                        {
                            window.location = "TokenList.jsp";
                        }
                        else
                        {
                            window.location = "TokenList.jsp";
                        }
                    }
                });
                return false;
            }
            $(document).ready(function () {
                $('.loading-gifHardware').hide();
//                $('#myModalOTPHardware').modal({
//                    backdrop: 'static',
//                    keyboard: true,
//                    show: false
//                });
            });
        </script>
        <style type="text/css">
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
            .checkbox-add {
                padding-bottom:5px;
            }
            .checkbox-button {
                width: 17px;
                height: 14px;
            }
            .checkbox label {
                color: #0145b6;font-size: 13px;font-weight: bold;
            }
        </style>
    </head>
    <body class="nav-md">
        <%
        if ((session.getAttribute("sUserID")) != null) {
            ROLE_DATA[][] sessFunctionToken = (ROLE_DATA[][]) session.getAttribute("SessRoleSet_Token");
            try {
                String anticsrf = "" + Math.random();
                request.getSession().setAttribute("anticsrf", anticsrf);
                String SessAgentID = session.getAttribute("SessAgentID").toString().trim();
                String SessUserAgentID = session.getAttribute("SessUserAgentID").toString().trim();
                String SessAgentName = (String) session.getAttribute("SessAgentName");
                String isCALoad = LoadParamSystem.getParamStart(Definitions.CONFIG_IS_WHICH_ABOUT_CA);
        %>
        <script>
            $(document).ready(function () {
                localStorage.setItem("storeAnticsrfTokenList", '<%= anticsrf%>');
            });
        </script>
        <div style="width: 100%; text-align: center; position: fixed;z-index: 1000;top: 0; padding-top: 300px;
             left: 0; height: 100%;" class="loading-gif">
            <img src="../Images/ajax-loader1.gif" alt="Please wait..." />
        </div>
        <%                                        TOKEN[][] rs = new TOKEN[1][];
                String ids = EscapeUtils.CheckTextNull(request.getParameter("id"));
                if (EscapeUtils.IsInteger(ids) == true) {
                    String sessLanguageGlobal = session.getAttribute("sessVN").toString();
                    db.S_BO_TOKEN_DETAIL(EscapeUtils.escapeHtml(ids), rs);
                    if (rs[0].length > 0) {
                        boolean isAccessAgencyPage = true;
                        if (!SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                            BRANCH[][] branchAccess = (BRANCH[][]) session.getAttribute("sessTreeBranchSystem");
                            isAccessAgencyPage = CommonFunction.checkBranchTreeInvalidCert(rs[0][0].BRANCH_ID, branchAccess);
                        }
                        if (isAccessAgencyPage == true) {
                            String strNAME_LINK = EscapeUtils.CheckTextNull(rs[0][0].MENU_LINK_NAME);
                            String strLINK_VALUE = EscapeUtils.CheckTextNull(rs[0][0].MENU_LINK_URL);
                            String strLINK_NOTICE = EscapeUtils.CheckTextNull(rs[0][0].PUSH_NOTICE_URL);
                            String strNOTICE_INFO = EscapeUtils.CheckTextNull(rs[0][0].PUSH_NOTICE_CONTENT);
                            String strCOLOR_TEXT = EscapeUtils.CheckTextNull(rs[0][0].PUSH_NOTICE_TEXT_COLOR);
                            String strCOLOR_BKGR = EscapeUtils.CheckTextNull(rs[0][0].PUSH_NOTICE_BGR_COLOR);
                            if("".equals(strLINK_NOTICE) && "".equals(strNOTICE_INFO) &&
                                "".equals(strCOLOR_TEXT) && "".equals(strCOLOR_BKGR))
                            {
                                GENERAL_POLICY[][] sessGeneralPolicy = (GENERAL_POLICY[][]) session.getAttribute("sessGeneralPolicy_System");
                                ObjectMapper oMapperParse = new ObjectMapper();
                                if (sessGeneralPolicy[0].length > 0) {
                                    for(GENERAL_POLICY rsPolicy1: sessGeneralPolicy[0])
                                    {
                                        if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_FO_DEFAULT_PUSH_NOTICE_JSON))
                                        {
                                            String sValueDefaultJSON = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                            if(!"".equals(sValueDefaultJSON))
                                            {
                                                PUSH_TOKEN_EDITED itemParsePush = oMapperParse.readValue(sValueDefaultJSON, PUSH_TOKEN_EDITED.class);
                                                for (PUSH_TOKEN_EDITED.Attribute attribute : itemParsePush.getAttributes()) {
                                                    if(EscapeUtils.CheckTextNull(attribute.getName()).equals(Definitions.CONFIG_POLICY_FO_PUSH_NOTICE_CONTENT))
                                                    {
                                                        strNOTICE_INFO = EscapeUtils.CheckTextNull(attribute.getValue());
                                                    }
                                                    if(EscapeUtils.CheckTextNull(attribute.getName()).equals(Definitions.CONFIG_POLICY_FO_PUSH_NOTICE_URL))
                                                    {
                                                        strLINK_NOTICE = EscapeUtils.CheckTextNull(attribute.getValue());
                                                    }
                                                    if(EscapeUtils.CheckTextNull(attribute.getName()).equals(Definitions.CONFIG_POLICY_FO_PUSH_NOTICE_BGR_COLOR))
                                                    {
                                                        strCOLOR_BKGR = EscapeUtils.CheckTextNull(attribute.getValue());
                                                    }
                                                    if(EscapeUtils.CheckTextNull(attribute.getName()).equals(Definitions.CONFIG_POLICY_FO_PUSH_NOTICE_TEXT_COLOR))
                                                    {
                                                        strCOLOR_TEXT = EscapeUtils.CheckTextNull(attribute.getValue());
                                                    }
                                                }
                                            }
                                            break;
                                        }
                                    }
                                }
                            }
                            int sTOKEN_STATE_ID = rs[0][0].TOKEN_STATE_ID;
                            int strACTIVATION_REMAINING_COUNTER = rs[0][0].ACTIVATION_REMAINING_COUNTER;
                            String sRole = session.getAttribute("RoleID_ID").toString().trim();
                            String sPUSH_NOTICE_TEXT = EscapeUtils.CheckTextNull(rs[0][0].PUSH_NOTICE_TEXT);
                            String hasPush = "0";
                            if(!"".equals(sPUSH_NOTICE_TEXT)){
                                hasPush = "1";
                            }
        %>
        <div class="x_title">
            <h2><i class="fa fa-list-ul"></i> <span id="idLblTitleEdits" style="color: #36526D;"></span></h2>
            <script>$("#idLblTitleEdits").text(token_title_edit);</script>
            <ul class="nav navbar-right panel_toolbox">
                <li>
                    <%
                        if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_TOKEN_LOCK,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_TOKEN_REQUUEST, sessFunctionToken) == true
                            || CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_TOKEN_UNLOCK,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_TOKEN_REQUUEST, sessFunctionToken) == true
                            || CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_TOKEN_LOST,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_TOKEN_REQUUEST, sessFunctionToken) == true
                            || CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_TOKEN_MENU_LINK,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_TOKEN_REQUUEST, sessFunctionToken) == true
                            || CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_TOKEN_PUSH_NOTFICATION,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_TOKEN_REQUUEST, sessFunctionToken) == true) {
                    %>
                    <input type="button" id="btnSave" class="btn btn-info" onclick="ValidateForm('<%=anticsrf%>');" />
                    <script>document.getElementById("btnSave").value = global_fm_button_edit;</script>
                    <%
                        }
                    %>
                    <%
                        if(sRole.equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN)) {
                    %>
                    <input id="btnDelete" onclick="popupDelete('<%=anticsrf%>');" class="btn btn-info" type="button" />
                    <script>document.getElementById("btnDelete").value = global_fm_button_delete;</script>
                    <%
                        }
                    %>
                    <input id="btnClose" class="btn btn-info" type="button" onclick="closeForm();" />
                    <script>document.getElementById("btnClose").value = global_fm_button_close;</script>
                </li>
            </ul>
            <div class="clearfix"></div>
        </div>
        <div class="x_content">
            <form name="myname" method="post" class="form-horizontal">
                <input type="hidden" name="strID" id="strID" hidden="true" readonly value="<%= rs[0][0].ID%>">
                <input type="hidden" name="strAGENT_ID_LOG" id="strstrAGENT_ID_LOGID" hidden="true" readonly value="<%= SessUserAgentID%>">
                <input type="hidden" name="CsrfToken" value="<%=anticsrf%>"/>
                <%
                    if(!"".equals(EscapeUtils.CheckTextNull(rs[0][0].TOKEN_SN))
                        && CommonFunction.checkViewTokenValid(EscapeUtils.CheckTextNull(rs[0][0].TOKEN_SN)) == true){
                %>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitleTokenSN" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input type="Text" name="TOKEN_SN" id="TOKEN_SN" readonly class="form-control123" value="<%= EscapeUtils.CheckTextNull(rs[0][0].TOKEN_SN) %>"/>
                        </div>
                    </div>
                    <script>$("#idLblTitleTokenSN").text(token_fm_tokenid);</script>
                </div>
                <%
                    }
                %>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitleTokenVersion" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <select name="TOKEN_VERSION" id="TOKEN_VERSION" class="form-control123" disabled>
                                <%
                                    TOKEN_VERSION[][] rstTOKEN_VERSION = new TOKEN_VERSION[1][];
                                    db.S_BO_TOKEN_VERSION_COMBOBOX(sessLanguageGlobal, rstTOKEN_VERSION);
                                    if (rstTOKEN_VERSION[0].length > 0) {
                                        for (TOKEN_VERSION temp1 : rstTOKEN_VERSION[0]) {
                                %>
                                <option value="<%=String.valueOf(temp1.ID)%>" <%= temp1.ID==rs[0][0].TOKEN_VERSION_ID ? "selected" : ""%>><%=temp1.REMARK%></option>
                                <%
                                        }
                                    }
                                %>
                            </select>
                        </div>
                    </div>
                    <script>$("#idLblTitleTokenVersion").text(token_fm_version);</script>
                </div>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitleTokenStatus" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <select name="TOKEN_STATE" id="TOKEN_STATE" class="form-control123" disabled>
                                <%
                                    TOKEN_STATE[][] rstTOKEN_STATE = new TOKEN_STATE[1][];
                                    db.S_BO_TOKEN_STATE_COMBOBOX(sessLanguageGlobal, rstTOKEN_STATE);
                                    if (rstTOKEN_STATE[0].length > 0) {
                                        for (TOKEN_STATE temp1 : rstTOKEN_STATE[0]) {
                                %>
                                <option value="<%=String.valueOf(temp1.ID)%>" <%= temp1.ID== sTOKEN_STATE_ID? "selected" : ""%>><%=temp1.REMARK%></option>
                                <%
                                        }
                                    }
                                %>
                            </select>
                        </div>
                    </div>
                    <script>$("#idLblTitleTokenStatus").text(global_fm_Status);</script>
                </div>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitleAgentName" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <%
                                if (Definitions.CONFIG_AGENT_ROOT.equals(SessAgentID)) {
                                    if(rs[0][0].BRANCH_ID == Integer.parseInt(Definitions.CONFIG_AGENT_ROOT)) {
                            %>
                            <input type="text" readonly name="BranchOfficeName" id="BranchOfficeName" value="<%= SessAgentName%>" class="form-control123"/>
                            <input type="text" style="display: none;" name="strAGENT_ID" id="strAGENT_ID" value="<%= SessUserAgentID%>" class="form-control123"/>
                            <input TYPE="text" id="strAGENT_IDEdit" style="display: none;" value="<%= SessUserAgentID%>"/>
                            <%
                                    } else {
                            %>
                            <select name="strAGENT_ID" id="strAGENT_ID" class="form-control123" disabled>
                                <%
                                    BRANCH[][] rst = new BRANCH[1][];
                                    db.S_BO_BRANCH_COMBOBOX(sessLanguageGlobal, rst);
                                    if (rst[0].length > 0) {
                                        for (BRANCH temp1 : rst[0]) {
                                %>
                                <option value="<%=String.valueOf(temp1.ID)%>" <%= temp1.ID == rs[0][0].BRANCH_ID ? "selected" : ""%>><%=temp1.NAME + " - " + temp1.REMARK%></option>
                                <%
                                        }
                                    }
                                %>
                            </select>
                            <input TYPE="text" id="strAGENT_IDEdit" style="display: none;" value="<%= String.valueOf(rs[0][0].BRANCH_ID) %>"/>
                            <%
                                }
                            } else {
                            %>
                            <input type="text" readonly name="BranchOfficeName" id="BranchOfficeName" value="<%= SessAgentName%>" class="form-control123"/>
                            <input type="text" style="display: none;" name="strAGENT_ID" id="strAGENT_ID" value="<%= SessUserAgentID%>" class="form-control123"/>
                            <input TYPE="text" id="strAGENT_IDEdit" style="display: none;" value="<%= SessUserAgentID%>"/>
                            <%
                                }
                            %>
                        </div>
                    </div>
                    <script>$("#idLblTitleAgentName").text(global_fm_Branch);</script>
                </div>
                <%
                    if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA)) {
                        if(sTOKEN_STATE_ID == Definitions.CONFIG_TOKEN_STATE_ID_LOCKED) {
                            TOKEN[][] rsTokenAttr = new TOKEN[1][];
                            dbTwo.S_BO_TOKEN_GET_LOCK_REQUEST_RECENTLY(EscapeUtils.escapeHtml(ids), rsTokenAttr);
                            String sLockReason = "";
                            if(rsTokenAttr != null && rsTokenAttr[0].length > 0){
                                if(!"".equals(rsTokenAttr[0][0].VALUE)){
                                    ObjectMapper objectMapper = new ObjectMapper();
                                    ATTRIBUTE_VALUES valueATTR_Frist = objectMapper.readValue(rsTokenAttr[0][0].VALUE, ATTRIBUTE_VALUES.class);
                                    sLockReason = valueATTR_Frist.getActionReason();
                                }
                            }
                %>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitleDeclineReason" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input type="Text" id="idLOCK_REASON" readonly class="form-control123" value="<%= sLockReason%>"/>
                        </div>
                    </div>
                    <script>$("#idLblTitleDeclineReason").text(token_fm_reason_block);</script>
                </div>
                <%
                        }
                    }
                %>
                <%
                    String sViewUnlockOTP = "";
                    Config conf = new Config();
                    String sSignedEnabled = conf.GetPropertybyCode(Definitions.CONFIG_SIGNED_OF_TOKEN_ENABLED);
                    if("1".equals(sSignedEnabled)) {
                        sViewUnlockOTP = "none";
                %>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitleSigningState" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <label class="switch" for="SIGNED_ENABLED">
                                <input type="checkbox" name="SIGNED_ENABLED" id="SIGNED_ENABLED" disabled
                                    <%= rs[0][0].SIGNED_ENABLED ? "checked" : "" %>/>
                                <div id="SIGNED_ENABLEDClass" class="slider round disabled"></div>
                            </label>
                        </div>
                    </div>
                    <script>$("#idLblTitleSigningState").text(global_fm_Status_signed);</script>
                </div>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitleSigningCounter" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input type="Text" name="SIGNING_COUNTER" id="SIGNING_COUNTER" readonly class="form-control123" value="<%= rs[0][0].SIGNING_COUNTER %>"/>
                        </div>
                    </div>
                    <script>$("#idLblTitleSigningCounter").text(token_fm_signing_number);</script>
                </div>
                <%
                    }
                %>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitlePushText" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                        <div class="col-sm-2" style="padding-right: 0px;padding-top: 5px;">
                            <label class="switch" for="PUSH_NOTICE_TEXT">
                                <input type="checkbox" name="PUSH_NOTICE_TEXT" id="PUSH_NOTICE_TEXT" disabled
                                       <%= !"".equals(sPUSH_NOTICE_TEXT) ? "checked" : "" %>/>
                                <div id="PUSH_NOTICE_TEXTClass" class="slider round disabled"></div>
                            </label>
                        </div>
                        <div class="col-sm-5" style="padding-right: 0px;">
                            <input id="btnPushNotice" onclick="popupPushNotice('<%=anticsrf%>');" class="btn btn-info" style="padding-top: 5px;padding-bottom: 5px;" type="button" />
                            <script>document.getElementById("btnPushNotice").value = global_fm_button_off_notice;</script>
                        </div>
                        <script>
                            $(document).ready(function () {
                                if('<%=hasPush%>' === '1') {
                                    $("#btnPushNotice").css("display", "");
                                } else {
                                    $("#btnPushNotice").css("display", "none");
                                }
                            });
                        </script>
                    </div>
                    <script>$("#idLblTitlePushText").text(global_fm_Status_notice);</script>
                </div>
                                    
<!--                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitlePushText" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                        <div class="col-sm-2" style="padding-right: 0px;padding-top: 5px;">
                            <label class="switch" for="PUSH_NOTICE_TEXT">
                                <input type="checkbox" name="PUSH_NOTICE_TEXT" id="PUSH_NOTICE_TEXT" disabled
                                       <= !"".equals(sPUSH_NOTICE_TEXT) ? "checked" : "" %>/>
                                <div id="PUSH_NOTICE_TEXTClass" class="slider round disabled"></div>
                            </label>
                        </div>
                        <div class="col-sm-5" style="padding-right: 0px;">
                            <input id="btnPushNotice" onclick="popupPushNotice('<=anticsrf%>');" class="btn btn-info" style="padding-top: 5px;padding-bottom: 5px;" type="button" />
                            <script>document.getElementById("btnPushNotice").value = global_fm_button_off_notice;</script>
                        </div>
                        <script>
                            $(document).ready(function () {
                                if('<=hasPush%>' === '1') {
                                    $("#btnPushNotice").css("display", "");
                                } else {
                                    $("#btnPushNotice").css("display", "none");
                                }
                            });
                        </script>
                    </div>
                    <script>$("#idLblTitlePushText").text(global_fm_Status_notice);</script>
                </div>-->
                <%
                    String strLabelOTP = "";
                    String strColorOTP = "";
                    String strDisabledActivation = "";
                    if (strACTIVATION_REMAINING_COUNTER == Definitions.CONFIG_TOKEN_ACTIVATION_CODE_LOCK) {
                        strLabelOTP = "lock";
                        strColorOTP = "red";
                    }
                    if (strACTIVATION_REMAINING_COUNTER > Definitions.CONFIG_TOKEN_ACTIVATION_CODE_LOCK) {
                        strLabelOTP = "unlock";
                        strColorOTP = "blue";
                        strDisabledActivation = "disabled";
                    }
                %>
                <div class="col-sm-13" style="padding-left: 0;clear: both;display: <%=sViewUnlockOTP%>">
                    <div class="form-group">
                        <label id="idLblTitleActiCode" class="control-label col-sm-4" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                        <div class="col-sm-8" style="padding-right: 0px;"></div>
                    </div>
                    <script>$("#idLblTitleActiCode").text(global_fm_Status_OTP);</script>
                </div>
                <div class="col-sm-13" style="padding-left: 0;clear: both;display: <%=sViewUnlockOTP%>">
                    <div class="form-group">
                        <%
                            if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_TOKEN_RESET_ACTIVATION_REMAINING_COUNTER,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_TOKEN_REQUUEST, sessFunctionToken) == true) {
                        %>
                        <div style="width: 100%;padding-top: 0px;clear: both;">
                            <div style="float: left;width: 90%;">
                                <input class="form-control123" readonly id="strACTIVATION_REMAINING_COUNTER"
                                       name="strACTIVATION_REMAINING_COUNTER" type="text" value="<%= String.valueOf(strACTIVATION_REMAINING_COUNTER)%>"/>
                            </div>
                            <div style="float: right;text-align: right;">
                                <%
                                    if (strACTIVATION_REMAINING_COUNTER == Definitions.CONFIG_TOKEN_ACTIVATION_CODE_LOCK) {
                                %>
                                <input type="button" class="btn btn-info" style="margin-right: 0px;width: 80px;" onclick="UnLockOTP('<%= anticsrf%>');"
                                    id="idbtnResetOTP" <%= strDisabledActivation %> />
                                <script>
                                    document.getElementById("idbtnResetOTP").value = global_button_grid_unlock;
                                </script>
                                <%
                                } else {
                                %>
                                <input type="button" class="btn btn-info" style="margin-right: 0px;width: 80px;" id="idbtnResetOTP" <%= strDisabledActivation%> />
                                <script>
                                    document.getElementById("idbtnResetOTP").value = global_button_grid_unlock;
                                </script>
                                <%
                                    }
                                %>
                            </div>
                        </div>
                        <%
                            } else {
                        %>
                        <input class="form-control123" readonly id="strACTIVATION_REMAINING_COUNTER"
                            name="strACTIVATION_REMAINING_COUNTER" type="text" value="<%= String.valueOf(strACTIVATION_REMAINING_COUNTER)%>"/>
                        <%
                            }
                        %>
                    </div>
                    
                    <div class="form-group">
                        <span style="font-weight: bold; color: <%= strColorOTP%>" id="idLblTitleNoteActiCode">
                        </span>
                        <script>
                            var jsabelPKI = '<%= strLabelOTP%>';
                            if (jsabelPKI !== "")
                            {
                                if (jsabelPKI === "lock")
                                {
                                    $("#idLblTitleNoteActiCode").text(token_fm_lock_opt);
                                }
                                else if (jsabelPKI === "unlock")
                                {
                                    $("#idLblTitleNoteActiCode").text(token_fm_unlock_opt);
                                }
                                else
                                {
                                    $("#idLblTitleNoteActiCode").text(agree_fm_lock_opt);
                                }
                            }
                        </script>
                    </div>
                </div>
                <%
                    String sReadOnlyLost = "";
                    if(sTOKEN_STATE_ID == Definitions.CONFIG_TOKEN_STATE_ID_LOST) {
                        sReadOnlyLost = "disabled";
                    }
                    if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_TOKEN_PUSH_NOTFICATION,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_TOKEN_REQUUEST, sessFunctionToken) == true) {
                %>
                <fieldset class="scheduler-border" style="clear: both;">
                    <legend class="scheduler-border" id="idLblTitleGroupNotifi"></legend>
                    <script>$("#idLblTitleGroupNotifi").text(token_group_notification);</script>
                    <div class="form-group" style="padding-left: 10px;">
                        <div class="col-sm-1" style="padding: 0;">
                            <label class="switch" for="strIS_PUSH_NOTICEEdit">
                            <input TYPE="checkbox" id="strIS_PUSH_NOTICEEdit" <%=sReadOnlyLost%> name="strIS_PUSH_NOTICEEdit" onchange="clickCheckIS_PUSH_NOTICE();"/>
                            <div class="slider round"></div>
                        </label>
                        </div>
                        <div class="col-sm-11" style="text-align: left;padding-top: 5px;">
                            <label class="control-labelcheckbox" style="padding-right: 15%;" id="idLblTitleChooseNotifi"></label>
                        </div>
                    </div>
                    <script>$("#idLblTitleChooseNotifi").text(token_fm_choose_noticepush);</script>
                    <script>
                        function UnLockOTP(idCSRF)
                        {
                            swal({
                                title: "",
                                text: token_confirm_unlock_temp,
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
                                        url: "../TokenCommon",
                                        data: {
                                            idParam: 'resetauthenotp',
                                            ID: $("#strID").val(),
                                            CsrfToken: idCSRF
                                        },
                                        cache: false,
                                        success: function (html) {
                                            var arr = sSpace(html).split('#');
                                            if (arr[0] === "0")
                                            {
//                                                                            funSuccAlert(token_succ_reset_opt, "TokenEdit.jsp?id=" + $("#strID").val());
                                                funSuccLocalAlert(token_succ_reset_opt);
                                            }
                                            else if (arr[0] === JS_EX_CSRF)
                                            {
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
                        function clickCheckIS_PUSH_NOTICE()
                        {
                            if ($("#strIS_PUSH_NOTICEEdit").is(':checked'))
                            {
                                $("#strCOLOR_TEXT_EditForm").prop("disabled", false);
                                $("#strCOLOR_TEXT_EditForm").addClass('jscolor');
                                $("#strCOLOR_BKGR_EditForm").prop("disabled", false);
                                $("#strCOLOR_BKGR_EditForm").addClass('color');
                                document.getElementById("strLINK_NOTICE_EditForm").readOnly = false;
                                document.getElementById("strNOTICE_INFO_EditForm").readOnly = false;
                                $("#strIS_PUSH_NOTICE_SET_NO").prop("disabled", false);
                            }
                            else
                            {
                                $("#strCOLOR_TEXT_EditForm").prop("disabled", true);
                                $("#strCOLOR_TEXT_EditForm").removeClass('jscolor');
                                $("#strCOLOR_TEXT_EditForm").removeClass('jscolor');
                                $("#strCOLOR_BKGR_EditForm").prop("disabled", true);
                                $("#strCOLOR_BKGR_EditForm").removeClass('color');
                                $("#strCOLOR_BKGR_EditForm").removeClass('jscolor');
                                document.getElementById("strLINK_NOTICE_EditForm").readOnly = true;
                                document.getElementById("strNOTICE_INFO_EditForm").readOnly = true;
                                $("#strIS_PUSH_NOTICE_SET_NO").prop("disabled", true);
                            }
                        }
                        function clickCheckIS_PUSH_NOTICE_SET_NO()
                        {
                            if ($("#strIS_PUSH_NOTICE_SET_NO").is(':checked'))
                            {
                                $("#strCOLOR_TEXT_EditForm").prop("disabled", true);
                                $("#strCOLOR_TEXT_EditForm").removeClass('jscolor');
                                $("#strCOLOR_BKGR_EditForm").prop("disabled", true);
                                $("#strCOLOR_BKGR_EditForm").removeClass('color');
                                document.getElementById("strLINK_NOTICE_EditForm").readOnly = true;
                                document.getElementById("strNOTICE_INFO_EditForm").readOnly = true;
                            }
                            else
                            {
                                $("#strCOLOR_TEXT_EditForm").prop("disabled", false);
                                $("#strCOLOR_TEXT_EditForm").addClass('jscolor');
                                $("#strCOLOR_BKGR_EditForm").prop("disabled", false);
                                $("#strCOLOR_BKGR_EditForm").addClass('color');
                                document.getElementById("strLINK_NOTICE_EditForm").readOnly = false;
                                document.getElementById("strNOTICE_INFO_EditForm").readOnly = false;
                            }
                        }
                    </script>
                    <div class="form-group">
                        <div class="col-sm-6">
                            <label class="control-label123" id="idLblTitleColorText"></label>
                            <input type="text" id="strCOLOR_TEXT_EditForm" name="strCOLOR_TEXT_EditForm" class="form-control123 jscolor" value="<%= strCOLOR_TEXT%>"/>
                            <input type="text" id="strCOLOR_TEXTEdit" style="display: none;" class="color" value="<%= strCOLOR_TEXT%>"/>
                        </div>
                        <script>$("#idLblTitleColorText").text(token_fm_colortext);</script>
                        <div class="col-sm-6">
                            <label class="control-label123" id="idLblTitleColorBkgr"></label>
                            <input type="text" id="strCOLOR_BKGR_EditForm" name="strCOLOR_BKGR_EditForm" class="form-control123 jscolor" value="<%= strCOLOR_BKGR%>"/>
                            <input type="text" id="strCOLOR_BKGREdit" style="display: none;" class="color" value="<%= strCOLOR_BKGR%>"/>
                        </div>
                        <script>
                            $("#idLblTitleColorBkgr").text(token_fm_colorgkgd);
                        </script>
                    </div>
                    <div class="form-group">
                        <div class="col-sm-6">
                            <label class="control-label123" id="idLblTitleLinkNote"></label>
                            <textarea type="Text" id="strLINK_NOTICE_EditForm" readOnly name="strLINK_NOTICE_EditForm" style="height: 50px;" class="form-control123"><%= strLINK_NOTICE%></textarea>
                            <input TYPE="text" id="strLINK_NOTICEEdit" style="display: none;" value="<%= strLINK_NOTICE%>"/>
                        </div>
                        <script>$("#idLblTitleLinkNote").text(token_fm_noticelink);</script>
                        <div class="col-sm-6">
                            <label class="control-label123" id="idLblTitleInfoNote"></label>
                            <textarea type="Text" id="strNOTICE_INFO_EditForm" readOnly name="strNOTICE_INFO_EditForm" style="height: 50px;" class="form-control123"><%= strNOTICE_INFO%></textarea>
                            <input TYPE="text" id="strNOTICE_INFOEdit" style="display: none;" value="<%= strNOTICE_INFO%>"/>
                        </div>
                        <script>$("#idLblTitleInfoNote").text(token_fm_noticeinfor);</script>
                    </div>
                    <div class="form-group" style="padding-left: 10px;">
                        <span style="font-weight: bold; color: blue;" id="idLblTitleStatusConfig">
                        </span>
                        <script>$("#idLblTitleStatusConfig").text(global_fm_token_status_configed);</script>
                    </div>
                    <div class="form-group" style="padding-left: 10px;">
                        <div class="col-sm-1" style="padding: 0;">
                            <label class="switch" for="strIS_PUSH_NOTICE_SET_NO">
                                <input TYPE="checkbox" id="strIS_PUSH_NOTICE_SET_NO" name="strIS_PUSH_NOTICE_SET_NO" onchange="clickCheckIS_PUSH_NOTICE_SET_NO();"/>
                                <div class="slider round"></div>
                            </label>
                        </div>
                        <div class="col-sm-11" style="text-align: left;padding-top: 5px;">
                            <label class="control-labelcheckbox" style="padding-right: 15%;" id="idLblTitleNoPush"></label>
                        </div>
                        <script>$("#idLblTitleNoPush").text(token_fm_set_no_noticepush);</script>
                    </div>
                </fieldset>
                <%
                    }
                %>
                <%
                    if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_TOKEN_MENU_LINK,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_TOKEN_REQUUEST, sessFunctionToken) == true) {
                %>
                <fieldset class="scheduler-border" style="clear: both;">
                    <legend class="scheduler-border" id="idLblTitleGroupDynamic"></legend>
                    <script>$("#idLblTitleGroupDynamic").text(token_group_dynamic);</script>
                    <div class="form-group" style="padding-left: 10px;">
                        <div class="col-sm-1" style="padding: 0;">
                            <label class="switch" for="strIS_MENU_LINKEdit">
                                <input TYPE="checkbox" id="strIS_MENU_LINKEdit" <%=sReadOnlyLost%> name="strIS_MENU_LINKEdit" onchange="clickCheckIS_MENU_LINK();"/>
                                <div class="slider round"></div>
                            </label>
                        </div>
                        <div class="col-sm-11" style="text-align: left;padding-top: 5px;">
                            <label class="control-labelcheckbox" style="padding-right: 15%;" id="idLblTitleChooseDynamic"></label>
                        </div>
                        <script>$("#idLblTitleChooseDynamic").text(token_group_choose_dynamic);</script>
                    </div>
                    <script>
                        function clickCheckIS_MENU_LINK()
                        {
                            if ($("#strIS_MENU_LINKEdit").is(':checked'))
                            {
                                document.getElementById("strNAME_LINK_EditForm").readOnly = false;
                                document.getElementById("strLINK_VALUE_EditForm").readOnly = false;
                                $("#strIS_MENU_LINK_SET_NO").prop("disabled", false);
//                                $("#strIS_MENU_LINK_SET_NO").bootstrapSwitch('disabled',false);
                            }
                            else
                            {
                                document.getElementById("strNAME_LINK_EditForm").readOnly = true;
                                document.getElementById("strLINK_VALUE_EditForm").readOnly = true;
                                $("#strIS_MENU_LINK_SET_NO").prop("disabled", true);
//                                $("#strIS_MENU_LINK_SET_NO").bootstrapSwitch('disabled',true);
                            }
                        }
                        function clickCheckIS_MENU_LINK_SET_NO()
                        {
                            if ($("#strIS_MENU_LINK_SET_NO").is(':checked'))
                            {
                                document.getElementById("strNAME_LINK_EditForm").readOnly = true;
                                document.getElementById("strLINK_VALUE_EditForm").readOnly = true;
                            }
                            else
                            {
                                document.getElementById("strNAME_LINK_EditForm").readOnly = false;
                                document.getElementById("strLINK_VALUE_EditForm").readOnly = false;
                            }
                        }
                    </script>
                    <div class="form-group">
                        <div class="col-sm-6">
                            <label class="control-label123" id="idLblTitleLinkName"></label>
                            <textarea type="Text" id="strNAME_LINK_EditForm" readOnly name="strNAME_LINK_EditForm" class="form-control123"><%= strNAME_LINK%></textarea>
                            <input TYPE="text" id="sstrNAME_LINKEdit" style="display: none;" value="<%=strNAME_LINK%>"/>
                        </div>
                        <script>$("#idLblTitleLinkName").text(token_fm_linkname);</script>
                        <div class="col-sm-6">
                            <label class="control-label123" id="idLblTitleLinkValue"></label>
                            <textarea type="Text" id="strLINK_VALUE_EditForm" readOnly name="strLINK_VALUE_EditForm" class="form-control123"><%= strLINK_VALUE%></textarea>
                            <input TYPE="text" id="sstrLINK_VALUEEdit" style="display: none;" value="<%=strLINK_VALUE%>"/>
                        </div>
                        <script>$("#idLblTitleLinkValue").text(token_fm_linkvalue);</script>
                    </div>
                    <div class="form-group" style="padding-left: 10px;">
                        <span style="font-weight: bold; color: blue;" id="idLblTitleStatusConfig1">
                        </span>
                        <script>$("#idLblTitleStatusConfig1").text(global_fm_token_status_configed);</script>
                    </div>
                    <div class="form-group" style="padding-left: 10px;">
                        <div class="col-sm-1" style="padding: 0;">
                            <label class="switch" for="strIS_MENU_LINK_SET_NO">
                                <input TYPE="checkbox" id="strIS_MENU_LINK_SET_NO" name="strIS_MENU_LINK_SET_NO" onchange="clickCheckIS_MENU_LINK_SET_NO();" />
                                <div class="slider round"></div>
                            </label>
                        </div>
                        <div class="col-sm-11" style="text-align: left;padding-top: 5px;">
                            <label class="control-labelcheckbox" id="idLblTitleNoDynamic" style="padding-right: 15%;"></label>
                        </div>
                    </div>
                    <script>$("#idLblTitleNoDynamic").text(token_fm_set_no_dynamic);</script>
                </fieldset>
                <%
                    }
                %>
                <%
                    if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_TOKEN_LOCK,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_TOKEN_REQUUEST, sessFunctionToken) == true
                        || CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_TOKEN_UNLOCK,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_TOKEN_REQUUEST, sessFunctionToken) == true
                        || CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_TOKEN_LOST,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_TOKEN_REQUUEST, sessFunctionToken) == true
                        || CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_TOKEN_RE_OPERATION,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_TOKEN_REQUUEST, sessFunctionToken) == true) {
                %>
                <fieldset class="scheduler-border" style="clear: both;">
                    <legend class="scheduler-border" id="idLblTitleGroupOther"></legend>
                    <script>$("#idLblTitleGroupOther").text(token_group_other);</script>
                        <%
                            if(sTOKEN_STATE_ID != Definitions.CONFIG_TOKEN_STATE_ID_LOST) {
                                if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_TOKEN_LOCK,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_TOKEN_REQUUEST, sessFunctionToken) == true) {
                        %>
                        <div class="col-sm-4" style="padding-left: 0; padding-top: 10px;">
                            <div class="col-sm-3" style="padding: 0;">
                                <label class="switch" for="strIS_LOCKEdit">
                                    <input TYPE="checkbox" id="strIS_LOCKEdit" name="strIS_LOCKEdit" onchange="onClickLock();"/>
                                    <div class="slider round"></div>
                                </label>
                            </div>
                            <div class="col-sm-9" style="text-align: left;padding-top: 5px;">
                                <label class="control-labelcheckbox" style="padding-right: 15%;" id="idLblTitleOtherBlock"></label>
                            </div>
                        </div>
                        <div class="col-sm-4" style="padding-left: 10px;padding-right: 0;padding-top: 5px;display: none;" id="idLockReasonView">
                            <div class="form-group">
                                <label id="idLblTitleLocReasonk" class="control-label col-sm-4" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                </label>
                                <div class="col-sm-8" style="padding-right: 0px;">
                                    <input type="text" id="idLockReason" class="form-control123"/>
                                </div>
                                <script>$("#idLblTitleLocReasonk").text(token_fm_reason_block);</script>
                            </div>
                        </div>
                        <script>
                            $("#idLblTitleOtherBlock").text(token_fm_block);
                            function onClickLock()
                            {
                                if ($("#strIS_LOCKEdit").is(':checked'))
                                {
                                    $("#idLockReasonView").css("display","");
                                    $("#idLockReason").val("");
                                    $("#idDivACTIVE_FLAGEdit").css("clear", "both");
                                }
                                else
                                {
                                    $("#idLockReasonView").css("display","none");
                                    $("#idLockReason").val("");
                                    $("#idDivACTIVE_FLAGEdit").css("clear", "");

                                }
                            }
                        </script>
                        <%
                                }
                            }
                        %>
                        <%
                            if(sTOKEN_STATE_ID != Definitions.CONFIG_TOKEN_STATE_ID_LOST) {
                                if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_TOKEN_UNLOCK,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_TOKEN_REQUUEST, sessFunctionToken) == true) {
                        %>
                        <div class="col-sm-4" style="padding-left: 20px;padding-top: 10px;">
                            <div class="col-sm-3" style="padding: 0;">
                                <label class="switch" for="strIS_UNLOCKEdit">
                                    <input TYPE="checkbox" id="strIS_UNLOCKEdit" name="strIS_UNLOCKEdit"/>
                                    <div class="slider round"></div>
                                </label>
                            </div>
                            <div class="col-sm-9" style="text-align: left;padding-top: 5px;">
                                <label class="control-labelcheckbox" style="padding-right: 15%;" id="idLblTitleOtherUnBlock"></label>
                            </div>
                        </div>
                        <script>$("#idLblTitleOtherUnBlock").text(token_fm_unblock);</script>
                        <%
                                }
                            }
                        %>
                        <%
                            if(sTOKEN_STATE_ID != Definitions.CONFIG_TOKEN_STATE_ID_LOST) {
                                if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_TOKEN_LOST,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_TOKEN_REQUUEST, sessFunctionToken) == true) {
                        %>
                        <div class="col-sm-4" style="padding-left: 0; padding-top: 10px;" id="idDivACTIVE_FLAGEdit">
                            <div class="col-sm-3" style="padding: 0;">
                                <label class="switch" for="strACTIVE_FLAGEdit">
                                    <input TYPE="checkbox" id="strACTIVE_FLAGEdit" name="strACTIVE_FLAGEdit"/>
                                    <div class="slider round"></div>
                                </label>
                            </div>
                            <div class="col-sm-9" style="text-align: left;padding-top: 5px;">
                                <label class="control-labelcheckbox" style="padding-right: 15%;" id="idLblTitleOtherLost"></label>
                            </div>
                        </div>
                        <script>$("#idLblTitleOtherLost").text(global_fm_lost);</script>
                        <%
                                }
                            }
                        %>
                        <%
                            if(sTOKEN_STATE_ID == Definitions.CONFIG_TOKEN_STATE_ID_LOST) {
                                if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_TOKEN_RE_OPERATION,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_TOKEN_REQUUEST, sessFunctionToken) == true) {
                        %>
                        <div class="col-sm-4" style="padding-left: 0; padding-top: 10px;">
                            <div class="col-sm-3" style="padding: 0;">
                                <label class="switch" for="strRE_OPERATIONEdit">
                                    <input TYPE="checkbox" id="strRE_OPERATIONEdit" name="strRE_OPERATIONEdit"/>
                                    <div class="slider round"></div>
                                </label>
                            </div>
                            <div class="col-sm-9" style="text-align: left;padding-top: 5px;">
                                <label class="control-labelcheckbox" style="padding-right: 15%;" id="idLblTitleOtherReLost"></label>
                            </div>
<!--                            <input TYPE="checkbox" id="strRE_OPERATIONEdit" name="strRE_OPERATIONEdit"/>
                            &nbsp;&nbsp;<label class="control-labelcheckbox" style="padding-right: 15%;" id="idLblTitleOtherReLost"></label>-->
                        </div>
                        <script>$("#idLblTitleOtherReLost").text(global_fm_relost);</script>
                        <%
                                }
                            }
                        %>
                </fieldset>
                <%
                    }
                %>
                <%
                    boolean IS_LOCK = false;
                    boolean IS_UNLOCK = false;
                    boolean IS_PUSH_NOTFICATION = false;
                    boolean IS_MENU_LINK = false;
                    boolean IS_LOST = false;
                    boolean IS_RESET_ACTIVATION = false;
                    boolean IS_RE_OPERATION = false;
                    String dtIS_LOCK = "";
                    String dtIS_UNLOCK = "";
                    String dtIS_PUSH_NOTFICATION = "";
                    String dtIS_MENU_LINK = "";
                    String dtIS_LOST = "";
                    String dtIS_RESET_ACTIVATION = "";
                    String dtIS_RE_OPERATION = "";
                    String userIS_LOCK = "";
                    String userIS_UNLOCK = "";
                    String userIS_PUSH_NOTFICATION = "";
                    String userIS_MENU_LINK = "";
                    String userIS_LOST = "";
                    String userIS_RESET_ACTIVATION = "";
                    String userIS_RE_OPERATION = "";
                    String desIS_LOCK = "";
                    String desIS_UNLOCK = "";
                    String desIS_PUSH_NOTFICATION = "";
                    String desIS_MENU_LINK = "";
                    String desIS_LOST = "";
                    String desIS_RESET_ACTIVATION = "";
                    String desIS_RE_OPERATION = "";
                    String attrIS_LOCK = "";
                    String attrIS_UNLOCK = "";
                    String attrIS_PUSH_NOTFICATION = "";
                    String attrIS_MENU_LINK = "";
                    String attrIS_LOST = "";
                    String attrIS_RESET_ACTIVATION = "";
                    String attrIS_RE_OPERATION = "";
                    String stateIS_LOCK = "";
                    String stateIS_UNLOCK = "";
                    String stateIS_PUSH_NOTFICATION = "";
                    String stateIS_MENU_LINK = "";
                    String stateIS_LOST = "";
                    String stateIS_RESET_ACTIVATION = "";
                    String stateIS_RE_OPERATION = "";
                    int itateIS_LOCK = 0;
                    int itateIS_UNLOCK = 0;
                    int itateIS_PUSH_NOTFICATION = 0;
                    int itateIS_MENU_LINK = 0;
                    int itateIS_LOST = 0;
                    int itateIS_RESET_ACTIVATION = 0;
                    int itateIS_RE_OPERATION = 0;
                    TOKEN[][] rsFunction = new TOKEN[1][];
                    db.S_BO_TOKEN_GET_ATTR(EscapeUtils.escapeHtml(ids), sessLanguageGlobal, rsFunction);
                    if (rsFunction[0].length > 0) {
                        for(int j = 0; j< rsFunction[0].length; j++)
                        {
                            if(rsFunction[0][j].TOKEN_ATTR_TYPE_ID == Definitions.CONFIG_TOKEN_ATTR_TYPE_ID_LOCK)
                            {
                                IS_LOCK = true;
                                dtIS_LOCK = rsFunction[0][j].CREATED_DT;
                                userIS_LOCK = rsFunction[0][j].CREATED_BY;
                                desIS_LOCK = rsFunction[0][j].TOKEN_ATTR_TYPE_DESC;
                                attrIS_LOCK = String.valueOf(rsFunction[0][j].TOKEN_ATTR_ID);
                                itateIS_LOCK = rsFunction[0][j].TOKEN_ATTR_STATE;
                                stateIS_LOCK = EscapeUtils.CheckTextNull(rsFunction[0][j].TOKEN_ATTR_STATE_DESC);
                            }
                            if(rsFunction[0][j].TOKEN_ATTR_TYPE_ID == Definitions.CONFIG_TOKEN_ATTR_TYPE_ID_UNLOCK)
                            {
                                IS_UNLOCK = true;
                                dtIS_UNLOCK = rsFunction[0][j].CREATED_DT;
                                userIS_UNLOCK = rsFunction[0][j].CREATED_BY;
                                desIS_UNLOCK = rsFunction[0][j].TOKEN_ATTR_TYPE_DESC;
                                attrIS_UNLOCK = String.valueOf(rsFunction[0][j].TOKEN_ATTR_ID);
                                itateIS_UNLOCK = rsFunction[0][j].TOKEN_ATTR_STATE;
                                stateIS_UNLOCK = EscapeUtils.CheckTextNull(rsFunction[0][j].TOKEN_ATTR_STATE_DESC);
                            }
                            if(rsFunction[0][j].TOKEN_ATTR_TYPE_ID == Definitions.CONFIG_TOKEN_ATTR_TYPE_ID_PUSH_NOTFICATION)
                            {
                                IS_PUSH_NOTFICATION = true;
                                dtIS_PUSH_NOTFICATION = rsFunction[0][j].CREATED_DT;
                                userIS_PUSH_NOTFICATION = rsFunction[0][j].CREATED_BY;
                                desIS_PUSH_NOTFICATION = rsFunction[0][j].TOKEN_ATTR_TYPE_DESC;
                                attrIS_PUSH_NOTFICATION = String.valueOf(rsFunction[0][j].TOKEN_ATTR_ID);
                                itateIS_PUSH_NOTFICATION = rsFunction[0][j].TOKEN_ATTR_STATE;
                                stateIS_PUSH_NOTFICATION = EscapeUtils.CheckTextNull(rsFunction[0][j].TOKEN_ATTR_STATE_DESC);
                            }
                            if(rsFunction[0][j].TOKEN_ATTR_TYPE_ID == Definitions.CONFIG_TOKEN_ATTR_TYPE_ID_MENU_LINK)
                            {
                                IS_MENU_LINK = true;
                                dtIS_MENU_LINK = rsFunction[0][j].CREATED_DT;
                                userIS_MENU_LINK = rsFunction[0][j].CREATED_BY;
                                desIS_MENU_LINK = rsFunction[0][j].TOKEN_ATTR_TYPE_DESC;
                                attrIS_MENU_LINK = String.valueOf(rsFunction[0][j].TOKEN_ATTR_ID);
                                itateIS_MENU_LINK = rsFunction[0][j].TOKEN_ATTR_STATE;
                                stateIS_MENU_LINK = EscapeUtils.CheckTextNull(rsFunction[0][j].TOKEN_ATTR_STATE_DESC);
                            }
                            if(rsFunction[0][j].TOKEN_ATTR_TYPE_ID == Definitions.CONFIG_TOKEN_ATTR_TYPE_ID_LOST)
                            {
                                IS_LOST = true;
                                dtIS_LOST = rsFunction[0][j].CREATED_DT;
                                userIS_LOST = rsFunction[0][j].CREATED_BY;
                                desIS_LOST = rsFunction[0][j].TOKEN_ATTR_TYPE_DESC;
                                attrIS_LOST = String.valueOf(rsFunction[0][j].TOKEN_ATTR_ID);
                                itateIS_LOST = rsFunction[0][j].TOKEN_ATTR_STATE;
                                stateIS_LOST = EscapeUtils.CheckTextNull(rsFunction[0][j].TOKEN_ATTR_STATE_DESC);
                            }
                            if(rsFunction[0][j].TOKEN_ATTR_TYPE_ID == Definitions.CONFIG_TOKEN_ATTR_TYPE_ID_RESET_ACTIVATION_REMAINING_COUNTER)
                            {
                                IS_RESET_ACTIVATION= true;
                                dtIS_RESET_ACTIVATION = rsFunction[0][j].CREATED_DT;
                                userIS_RESET_ACTIVATION = rsFunction[0][j].CREATED_BY;
                                desIS_RESET_ACTIVATION = rsFunction[0][j].TOKEN_ATTR_TYPE_DESC;
                                attrIS_RESET_ACTIVATION = String.valueOf(rsFunction[0][j].TOKEN_ATTR_ID);
                                itateIS_RESET_ACTIVATION = rsFunction[0][j].TOKEN_ATTR_STATE;
                                stateIS_RESET_ACTIVATION = EscapeUtils.CheckTextNull(rsFunction[0][j].TOKEN_ATTR_STATE_DESC);
                            }
                            if(rsFunction[0][j].TOKEN_ATTR_TYPE_ID == Definitions.CONFIG_TOKEN_ATTR_TYPE_ID_RE_OPERATION)
                            {
                                IS_RE_OPERATION= true;
                                dtIS_RE_OPERATION = rsFunction[0][j].CREATED_DT;
                                userIS_RE_OPERATION = rsFunction[0][j].CREATED_BY;
                                desIS_RE_OPERATION = rsFunction[0][j].TOKEN_ATTR_TYPE_DESC;
                                attrIS_RE_OPERATION = String.valueOf(rsFunction[0][j].TOKEN_ATTR_ID);
                                itateIS_RE_OPERATION = rsFunction[0][j].TOKEN_ATTR_STATE;
                                stateIS_RE_OPERATION = EscapeUtils.CheckTextNull(rsFunction[0][j].TOKEN_ATTR_STATE_DESC);
                            }
                        }
                    }
                    int j=0;
                %>
                <%
                    if(IS_LOCK == true || IS_PUSH_NOTFICATION == true || IS_UNLOCK == true || IS_MENU_LINK == true
                        || IS_LOST == true || IS_RESET_ACTIVATION == true || IS_RE_OPERATION == true)
                    {
                %>
                <fieldset class="scheduler-border" style="clear: both;">
                    <legend class="scheduler-border" id="idLblTitleRequestEdit"></legend>
                    <script>$("#idLblTitleRequestEdit").text(token_group_request_edit);</script>
                    <div class="table-responsive">
                    <table class="table table-bordered table-striped projects" id="mtToken">
                        <thead>
                        <th id="idLblTitleTableSST"></th>
                        <th id="idLblTitleTableRequestType"></th>
                        <th id="idLblTitleTableStatus"></th>
                        <th id="idLblTitleTableCreateDate"></th>
                        <th id="idLblTitleTableCreateUser"></th>
                        <%
                            if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_TOKEN_DECLINED, Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_TOKEN_REQUUEST, sessFunctionToken) == true) {
                        %>
                        <th id="idLblTitleTableAction"></th>
                        <%
                            }
                        %>
                        </thead>
                        <tbody>
                            <%
                                if(IS_LOCK == true) {
                                j=j+1;
                            %>
                            <tr>
                                <td><%= j%></td>
                                <td><%= desIS_LOCK%></td>
                                <td>
                                    <%= stateIS_LOCK%>
                                </td>
                                <td><%= dtIS_LOCK%></td>
                                <td><%= userIS_LOCK%></td>
                                <%
                                    if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_TOKEN_DECLINED, Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_TOKEN_REQUUEST, sessFunctionToken) == true) {
                                %>
                                <td>
                                    <%
                                        if(itateIS_LOCK == Definitions.CONFIG_TOKEN_STATE_ID_PENDDING) {
                                    %>
                                    <a id="idLblTitleDecline01" style="cursor: pointer;" onclick="popupCancel('<%=attrIS_LOCK%>', '<%= anticsrf%>');" class="btn btn-info btn-xs"> </a>
                                    <script>
                                        $("#idLblTitleDecline01").append('<i class="fa fa-pencil"></i>' + global_fm_button_decline);
                                    </script>
                                    <%
                                        } else if(itateIS_LOCK == Definitions.CONFIG_TOKEN_STATE_ID_APPROVED) {
                                            if(sRole.equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN) || sRole.equals(Definitions.CONFIG_ROLE_ID_CA_SURVEYOR)
                                                || sRole.equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN_CHILD)
                                                || sRole.equals(Definitions.CONFIG_ROLE_ID_AGENT_ADMIN) || sRole.equals(Definitions.CONFIG_ROLE_ID_AGENT_SURVEYOR)) {
                                    %>
                                    <a id="idLblTitleDecline01" style="cursor: pointer;" onclick="popupCancel('<%=attrIS_LOCK%>', '<%= anticsrf%>');" class="btn btn-info btn-xs"> </a>
                                    <script>
                                        $("#idLblTitleDecline01").append('<i class="fa fa-pencil"></i>' + global_fm_button_decline);
                                    </script>
                                    <%
                                            }
                                        }
                                    %>
                                </td>
                                <%
                                    }
                                %>
                            </tr>
                            <%
                                }
                            %>
                            <%
                                if(IS_UNLOCK == true) {
                                    j=j+1;
                            %>
                            <tr>
                                <td><%= j%></td>
                                <td><%= desIS_UNLOCK%></td>
                                <td><%= stateIS_UNLOCK%>
                                </td>
                                <td><%= dtIS_UNLOCK%></td>
                                <td><%= userIS_UNLOCK%></td>
                                <%
                                    if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_TOKEN_DECLINED, Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_TOKEN_REQUUEST, sessFunctionToken) == true) {
                                %>
                                <td>
                                    <%
                                        if(itateIS_UNLOCK == Definitions.CONFIG_TOKEN_STATE_ID_PENDDING) {
                                    %>
                                    <a id="idLblTitleDecline02" style="cursor: pointer;" onclick="popupCancel('<%=attrIS_UNLOCK%>', '<%= anticsrf%>');" class="btn btn-info btn-xs"></a>
                                    <script>
                                        $("#idLblTitleDecline02").append('<i class="fa fa-pencil"></i>' + global_fm_button_decline);
                                    </script>
                                    <%
                                        } else if(itateIS_UNLOCK == Definitions.CONFIG_TOKEN_STATE_ID_APPROVED) {
                                            if(sRole.equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN) || sRole.equals(Definitions.CONFIG_ROLE_ID_CA_SURVEYOR)
                                                || sRole.equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN_CHILD)
                                                || sRole.equals(Definitions.CONFIG_ROLE_ID_AGENT_ADMIN) || sRole.equals(Definitions.CONFIG_ROLE_ID_AGENT_SURVEYOR)) {
                                    %>
                                    <a id="idLblTitleDecline02" style="cursor: pointer;" onclick="popupCancel('<%=attrIS_UNLOCK%>', '<%= anticsrf%>');" class="btn btn-info btn-xs"> </a>
                                    <script>
                                        $("#idLblTitleDecline02").append('<i class="fa fa-pencil"></i>' + global_fm_button_decline);
                                    </script>
                                    <%
                                        }
                                    }
                                    %>
                                </td>
                                <%
                                    }
                                %>
                            </tr>
                            <%
                                }
                            %>
                            <%
                                if(IS_PUSH_NOTFICATION == true) {
                                j=j+1;
                            %>
                            <tr>
                                <td><%= j%></td>
                                <td><%= desIS_PUSH_NOTFICATION%></td>
                                <td><%= stateIS_PUSH_NOTFICATION%>
                                </td>
                                <td><%= dtIS_PUSH_NOTFICATION%></td>
                                <td><%= userIS_PUSH_NOTFICATION%></td>
                                <%
                                    if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_TOKEN_DECLINED, Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_TOKEN_REQUUEST, sessFunctionToken) == true) {
                                %>
                                <td>
                                    <%
                                        if(itateIS_PUSH_NOTFICATION == Definitions.CONFIG_TOKEN_STATE_ID_PENDDING) {
                                    %>
                                    <a id="idLblTitleDecline03" style="cursor: pointer;" onclick="popupCancel('<%=attrIS_PUSH_NOTFICATION%>', '<%= anticsrf%>');" class="btn btn-info btn-xs"></a>
                                    <script>
                                        $("#idLblTitleDecline03").append('<i class="fa fa-pencil"></i>' + global_fm_button_decline);
                                    </script>
                                    <%
                                        } else if(itateIS_PUSH_NOTFICATION == Definitions.CONFIG_TOKEN_STATE_ID_APPROVED) {
                                            if(sRole.equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN) || sRole.equals(Definitions.CONFIG_ROLE_ID_CA_SURVEYOR)
                                                || sRole.equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN_CHILD)
                                                || sRole.equals(Definitions.CONFIG_ROLE_ID_AGENT_ADMIN) || sRole.equals(Definitions.CONFIG_ROLE_ID_AGENT_SURVEYOR)) {
                                    %>
                                    <a id="idLblTitleDecline03" style="cursor: pointer;" onclick="popupCancel('<%=attrIS_PUSH_NOTFICATION%>', '<%= anticsrf%>');" class="btn btn-info btn-xs"></a>
                                    <script>
                                        $("#idLblTitleDecline03").append('<i class="fa fa-pencil"></i>' + global_fm_button_decline);
                                    </script>
                                    <%
                                        }
                                    }
                                    %>
                                </td>
                                <%
                                    }
                                %>
                            </tr>
                            <%
                                }
                            %>
                            <%
                                if(IS_MENU_LINK == true) {
                                j=j+1;
                            %>
                            <tr>
                                <td><%= j%></td>
                                <td><%= desIS_MENU_LINK%></td>
                                <td><%= stateIS_MENU_LINK%>
                                </td>
                                <td><%= dtIS_MENU_LINK%></td>
                                <td><%= userIS_MENU_LINK%></td>
                                <%
                                    if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_TOKEN_DECLINED, Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_TOKEN_REQUUEST, sessFunctionToken) == true) {
                                %>
                                <td>
                                    <%
                                        if(itateIS_MENU_LINK == Definitions.CONFIG_TOKEN_STATE_ID_PENDDING) {
                                    %>
                                    <a id="idLblTitleDecline04" style="cursor: pointer;" onclick="popupCancel('<%=attrIS_MENU_LINK%>', '<%= anticsrf%>');" class="btn btn-info btn-xs"></a>
                                    <script>
                                        $("#idLblTitleDecline04").append('<i class="fa fa-pencil"></i>' + global_fm_button_decline);
                                    </script>
                                    <%
                                        } else if(itateIS_MENU_LINK == Definitions.CONFIG_TOKEN_STATE_ID_APPROVED) {
                                            if(sRole.equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN) || sRole.equals(Definitions.CONFIG_ROLE_ID_CA_SURVEYOR)
                                                || sRole.equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN_CHILD)
                                                || sRole.equals(Definitions.CONFIG_ROLE_ID_AGENT_ADMIN) || sRole.equals(Definitions.CONFIG_ROLE_ID_AGENT_SURVEYOR)) {
                                    %>
                                    <a id="idLblTitleDecline04" style="cursor: pointer;" onclick="popupCancel('<%=attrIS_MENU_LINK%>', '<%= anticsrf%>');" class="btn btn-info btn-xs"></a>
                                    <script>
                                        $("#idLblTitleDecline04").append('<i class="fa fa-pencil"></i>' + global_fm_button_decline);
                                    </script>
                                    <%
                                            }
                                        }
                                    %>
                                </td>
                                <%
                                    }
                                %>
                            </tr>
                            <%
                                }
                            %>
                            <%
                                if(IS_LOST == true) {
                                j=j+1;
                            %>
                            <tr>
                                <td><%= j%></td>
                                <td><%= desIS_LOST%></td>
                                <td><%= stateIS_LOST%>
                                </td>
                                <td><%= dtIS_LOST%></td>
                                <td><%= userIS_LOST%></td>
                                <%
                                    if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_TOKEN_DECLINED, Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_TOKEN_REQUUEST, sessFunctionToken) == true) {
                                %>
                                <td>
                                    <%
                                        if(itateIS_LOST == Definitions.CONFIG_TOKEN_STATE_ID_PENDDING) {
                                    %>
                                    <a id="idLblTitleDecline05" style="cursor: pointer;" onclick="popupCancel('<%=attrIS_LOST%>', '<%= anticsrf%>');" class="btn btn-info btn-xs"></a>
                                    <script>
                                        $("#idLblTitleDecline05").append('<i class="fa fa-pencil"></i>' + global_fm_button_decline);
                                    </script>
                                    <%
                                        } else if(itateIS_LOST == Definitions.CONFIG_TOKEN_STATE_ID_APPROVED) {
                                            if(sRole.equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN) || sRole.equals(Definitions.CONFIG_ROLE_ID_CA_SURVEYOR)
                                                || sRole.equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN_CHILD)
                                                || sRole.equals(Definitions.CONFIG_ROLE_ID_AGENT_ADMIN) || sRole.equals(Definitions.CONFIG_ROLE_ID_AGENT_SURVEYOR)) {
                                    %>
                                    <a id="idLblTitleDecline05" style="cursor: pointer;" onclick="popupCancel('<%=attrIS_LOST%>', '<%= anticsrf%>');" class="btn btn-info btn-xs"></a>
                                    <script>
                                        $("#idLblTitleDecline05").append('<i class="fa fa-pencil"></i>' + global_fm_button_decline);
                                    </script>
                                    <%
                                            }
                                        }
                                    %>
                                </td>
                                <%
                                    }
                                %>
                            </tr>
                            <%
                                }
                            %>
                            <%
                                if(IS_RESET_ACTIVATION == true) {
                                j=j+1;
                            %>
                            <tr>
                                <td><%= j%></td>
                                <td><%= desIS_RESET_ACTIVATION%></td>
                                <td><%= stateIS_RESET_ACTIVATION%></td>
                                <td><%= dtIS_RESET_ACTIVATION%></td>
                                <td><%= userIS_RESET_ACTIVATION%></td>
                                <%
                                    if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_TOKEN_DECLINED, Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_TOKEN_REQUUEST, sessFunctionToken) == true) {
                                %>
                                <td>
                                    <%
                                        if(itateIS_RESET_ACTIVATION == Definitions.CONFIG_TOKEN_STATE_ID_PENDDING) {
                                    %>
                                    <a id="idLblTitleDecline06" style="cursor: pointer;" onclick="popupCancel('<%=attrIS_RESET_ACTIVATION%>', '<%= anticsrf%>');" class="btn btn-info btn-xs"></a>
                                    <script>
                                        $("#idLblTitleDecline06").append('<i class="fa fa-pencil"></i>' + global_fm_button_decline);
                                    </script>
                                    <%
                                        } else if(itateIS_RESET_ACTIVATION == Definitions.CONFIG_TOKEN_STATE_ID_APPROVED) {
                                            if(sRole.equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN) || sRole.equals(Definitions.CONFIG_ROLE_ID_CA_SURVEYOR)
                                                || sRole.equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN_CHILD)
                                                || sRole.equals(Definitions.CONFIG_ROLE_ID_AGENT_ADMIN) || sRole.equals(Definitions.CONFIG_ROLE_ID_AGENT_SURVEYOR)) {
                                    %>
                                    <a id="idLblTitleDecline06" style="cursor: pointer;" onclick="popupCancel('<%=attrIS_RESET_ACTIVATION%>', '<%= anticsrf%>');" class="btn btn-info btn-xs"></a>
                                    <script>
                                        $("#idLblTitleDecline06").append('<i class="fa fa-pencil"></i>' + global_fm_button_decline);
                                    </script>
                                    <%
                                            }
                                        }
                                    %>
                                </td>
                                <%
                                    }
                                %>
                            </tr>
                            <%
                                }
                            %>
                            <%
                                if(IS_RE_OPERATION == true) {
                                j=j+1;
                            %>
                            <tr>
                                <td><%= j%></td>
                                <td><%= desIS_RE_OPERATION%></td>
                                <td><%= stateIS_RE_OPERATION%></td>
                                <td><%= dtIS_RE_OPERATION%></td>
                                <td><%= userIS_RE_OPERATION%></td>
                                <%
                                    if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_TOKEN_DECLINED, Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_TOKEN_REQUUEST, sessFunctionToken) == true) {
                                %>
                                <td>
                                    <%
                                        if(itateIS_RE_OPERATION == Definitions.CONFIG_TOKEN_STATE_ID_PENDDING) {
                                    %>
                                    <a id="idLblTitleDecline06" style="cursor: pointer;" onclick="popupCancel('<%=attrIS_RE_OPERATION%>', '<%= anticsrf%>');" class="btn btn-info btn-xs"></a>
                                    <script>
                                        $("#idLblTitleDecline06").append('<i class="fa fa-pencil"></i>' + global_fm_button_decline);
                                    </script>
                                    <%
                                        } else if(itateIS_RE_OPERATION == Definitions.CONFIG_TOKEN_STATE_ID_APPROVED) {
                                            if(sRole.equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN) || sRole.equals(Definitions.CONFIG_ROLE_ID_CA_SURVEYOR)
                                                || sRole.equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN_CHILD)
                                                || sRole.equals(Definitions.CONFIG_ROLE_ID_AGENT_ADMIN) || sRole.equals(Definitions.CONFIG_ROLE_ID_AGENT_SURVEYOR)) {
                                    %>
                                    <a id="idLblTitleDecline06" style="cursor: pointer;" onclick="popupCancel('<%=attrIS_RE_OPERATION%>', '<%= anticsrf%>');" class="btn btn-info btn-xs"></a>
                                    <script>
                                        $("#idLblTitleDecline06").append('<i class="fa fa-pencil"></i>' + global_fm_button_decline);
                                    </script>
                                    <%
                                            }
                                        }
                                    %>
                                </td>
                                <%
                                    }
                                %>
                            </tr>
                            <%
                                }
                            %>
                            <script>
                                function popupCancel(idID, idCSRF)
                                {
                                    swal({
                                        title: "",
                                        text: token_confirm_cancel_request,
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
                                                url: "../TokenCommon",
                                                data: {
                                                    idParam: 'cancelrequest',
                                                    TokenID: $("#strID").val(),
                                                    id: idID,
                                                    CsrfToken: idCSRF
                                                },
                                                cache: false,
                                                success: function (html) {
                                                    var myStrings = sSpace(html);
                                                    var arr = myStrings.split('#');
                                                    if (arr[0] === "0")
                                                    {
//                                                                                funSuccAlert(token_succ_cancel_request, "TokenEdit.jsp?id=" + $("#strID").val());
//                                                        funSuccLocalAlert(token_succ_cancel_request);
                                                        localStorage.setItem("EDIT_TOKEN", $("#strID").val());
                                                        funSuccLocalAlert(token_succ_cancel_request);
                                                    }
                                                    else if (arr[0] === JS_EX_CSRF)
                                                    {
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
                        </tbody>
                    </table>
                    </div>
                    <script>
                        $("#idLblTitleTableSST").text(global_fm_STT);
                        $("#idLblTitleTableRequestType").text(global_fm_requesttype);
                        $("#idLblTitleTableStatus").text(global_fm_Status);
                        $("#idLblTitleTableCreateDate").text(global_fm_date_create);
                        $("#idLblTitleTableCreateUser").text(global_fm_user_create);
                        $("#idLblTitleTableAction").text(global_fm_action);
                    </script>
                </fieldset>
                <%
                    }
                %>
                <%
                    //CERTIFICATION[][] rsPgin = new CERTIFICATION[1][];
                %>
<!--                <fieldset class="scheduler-border">
                    <legend class="scheduler-border" id="idLblTitleGroupCert"></legend>
                    <script>$("#idLblTitleGroupCert").text(global_fm_cert_list);</script>
                    <table id="idTableList" class="table table-bordered table-striped projects">
                        <thead>
                        <th id="idLblTitleTableGroupCertSTT"></th>
                        <th id="idLblTitleTableGroupCertCompany" style="width: 120px;"></th>
                        <th id="idLblTitleTableGroupCertMST" style="width: 100px;"></th>
                        <th id="idLblTitleTableGroupCertPersonal" style="width: 120px;"></th>
                        <th id="idLblTitleTableGroupCertCMND" style="width: 100px;"></th>
                        <th id="idLblTitleTableGroupCertDuration"></th>
                        <th id="idLblTitleTableGroupCertType"></th>
                        <th id="idLblTitleTableGroupCertStatusCert" style="width: 80px;"></th>
                        <th id="idLblTitleTableGroupCertCreateDate"></th>
                        <th id="idLblTitleTableGroupCertAction" style="width: 220px;"></th>
                        </thead>
                        <tbody>
                        <
                            if (rsPgin[0].length > 0) {
                                for (CERTIFICATION temp1 : rsPgin[0]) {
                                    String strID = String.valueOf(temp1.ID);
                                    String sSMTOrMSN = EscapeUtils.CheckTextNull(temp1.TAX_CODE);
                                    if("".equals(sSMTOrMSN))
                                    {
                                        sSMTOrMSN = EscapeUtils.CheckTextNull(temp1.BUDGET_CODE);
                                    }
                                    String sCMNDOrHC = EscapeUtils.CheckTextNull(temp1.P_ID);
                                    if("".equals(sCMNDOrHC))
                                    {
                                        sCMNDOrHC = EscapeUtils.CheckTextNull(temp1.PASSPORT);
                                    }
                        %>
                        <tr>
                            <td><= com.convertMoney(j)%></td>
                            <td><= EscapeUtils.CheckTextNull(temp1.COMPANY_NAME)%></td>
                            <td><= sSMTOrMSN%></td>
                            <td><= EscapeUtils.CheckTextNull(temp1.PERSONAL_NAME)%></td>
                            <td><= sCMNDOrHC%></td>
                            <td><= EscapeUtils.CheckTextNull(temp1.CERTIFICATION_PROFILE_NAME)%></td>
                            <td><= EscapeUtils.CheckTextNull(temp1.CERTIFICATION_PURPOSE_DESC)%></td>
                            <td><= EscapeUtils.CheckTextNull(temp1.CERTIFICATION_STATE_DESC)%></td>
                            <td><= EscapeUtils.CheckTextNull(temp1.CREATED_DT)%></td>
                        </tr>
                        <
                                    j++;
                                }
                            }
                        %>
                        </tbody>
                    </table>
                    <script>
                        $("#idLblTitleTableGroupCertSTT").text(global_fm_STT);
                        $("#idLblTitleTableGroupCertCompany").text(global_fm_grid_company);
                        $("#idLblTitleTableGroupCertMST").text(global_fm_MST + '/' + global_fm_MNS);
                        $("#idLblTitleTableGroupCertPersonal").text(global_fm_grid_personal);
                        $("#idLblTitleTableGroupCertCMND").text(global_fm_CMND + '/' + global_fm_HC);
                        $("#idLblTitleTableGroupCertDuration").text(global_fm_duration_cts);
                        $("#idLblTitleTableGroupCertType").text(global_fm_certtype);
                        $("#idLblTitleTableGroupCertStatusCert").text(global_fm_Status_cert);
                        $("#idLblTitleTableGroupCertCreateDate").text(global_fm_date_create);
                        $("#idLblTitleTableGroupCertAction").text(global_fm_action);
                    </script>
                </fieldset>-->
                
                <!-- CERTÌICATION HISTORY -->
                <%
                    CERTIFICATION[][] rsCert = new CERTIFICATION[1][];
                    db.S_BO_TOKEN_DETAIL_HISTORY_CERTIFICATION(ids, sessLanguageGlobal, rsCert);
                    if(rsCert != null && rsCert[0].length > 0) {
                        int p=0;
                %>
                <fieldset class="scheduler-border" style="clear: both;">
                    <legend class="scheduler-border" id="idLblTitleGroupCertHis"></legend>
                    <script>$("#idLblTitleGroupCertHis").text(token_group_cert_history);</script>
                    <div class="table-responsive">
                    <table class="table table-bordered table-striped projects" id="mtTokenHisCert">
                        <thead>
                        <th id="idLblTitleTableHisSST"></th>
                        <th id="idLblTitleTableHisMST"></th>
                        <th id="idLblTitleTableHisCompany"></th>
                        <th id="idLblTitleTableHisCMND"></th>
                        <th id="idLblTitleTableHisPersonal"></th>
                        <th id="idLblTitleTableHisService"></th>
                        <th id="idLblTitleTableHisMinDate"></th>
                        <th id="idLblTitleTableHisMaxDate"></th>
                        <th id="idLblTitleTableHisAction"></th>
                        <script>
                            $("#idLblTitleTableHisSST").text(global_fm_STT);
                            $("#idLblTitleTableHisMST").text(global_fm_enterprise_id);
                            $("#idLblTitleTableHisCompany").text(global_fm_grid_company);
                            $("#idLblTitleTableHisCMND").text(global_fm_personal_id);
                            $("#idLblTitleTableHisPersonal").text(global_fm_grid_personal);
                            $("#idLblTitleTableHisService").text(certprofile_fm_service_type);
                            $("#idLblTitleTableHisMinDate").text(global_fm_date_create);
                            $("#idLblTitleTableHisMaxDate").text(global_fm_date_cancel);
                            $("#idLblTitleTableHisAction").text(global_fm_action);
                        </script>
                        </thead>
                        <tbody>
                            <%
                                for(CERTIFICATION rsCert1 : rsCert[0]) {
                                    String strID = String.valueOf(rsCert1.ID);
//                                    String sMST = rsCert1.TAX_CODE;
//                                    if("".equals(sMST)){
//                                        sMST = rsCert1.BUDGET_CODE;
//                                    }
//                                    if("".equals(sMST)){
//                                        sMST = rsCert1.DECISION;
//                                    }
//                                    String sCMND = rsCert1.P_ID;
//                                    if("".equals(sCMND)){
//                                        sCMND = rsCert1.P_EID;
//                                    }
//                                    if("".equals(sCMND)){
//                                        sCMND = rsCert1.PASSPORT;
//                                    }
                                    p=p+1;
                            %>
                            <tr>
                                <td><%= p%></td>
                                <td><a style="color: blue;" data-toggle="tooltipPrefix" title="<%= rsCert1.ENTERPRISE_ID_REMARK%>"><%= rsCert1.ENTERPRISE_ID%></a></td>
                                <td><%= rsCert1.COMPANY_NAME %></td>
                                <td><a style="color: blue;" data-toggle="tooltipPrefix" title="<%= rsCert1.PERSONAL_ID_REMARK%>"><%= rsCert1.PERSONAL_ID%></a></td>
                                <td><%= rsCert1.PERSONAL_NAME %></td>
                                <td><%= rsCert1.CERTIFICATION_ATTR_TYPE_DESC %></td>
                                <td><%= rsCert1.CREATED_DT %></td>
                                <td><%= rsCert1.REVOKED_DT %></td>
                                <td>
                                    <a id="idLblTitleTableHisDetail<%=strID%>" onclick="ShowDialog('<%= strID%>');" style="cursor: pointer;" class="btn btn-info btn-xs" data-toggle="modal"></a>
                                </td>
                                <script>
                                    $("#idLblTitleTableHisDetail" + '<%=strID%>').append('<i class="fa fa-pencil"></i> ' + global_fm_detail);
                                </script>
                            </tr>
                            <% 
                                }
                            %>
                        </tbody>
                    </table>
                    <div id="greenIP" style="margin: 5px 0 5px 0;"> </div>
                    </div>
                    <script>
                        if (parseInt('<%=p%>') > 0)
                        {
                            $('#greenIP').smartpaginator({totalrecords: parseInt('<%=p%>'), recordsperpage: 10, datacontainer: 'mtTokenHisCert', dataelement: 'tr', initval: 0, next: global_paging_last, prev: global_paging_Before, first: global_paging_first, last: global_paging_next, theme: 'green'});
                        }
                        function LoadCert(Tag_ID)
                        {
                            $.ajax({
                                type: "post",
                                url: "../JSONCommon",
                                data: {
                                    idParam: 'loadcertificedetail',
                                    Tag_ID: Tag_ID
                                },
                                cache: false,
                                success: function (html)
                                {
                                    if (html.length > 0)
                                    {
                                        var obj = JSON.parse(html);
                                        if (obj[0].Code === "0")
                                        {
                                            $("#DetailCompany").val(obj[0].COMPANY_NAME);
                                            $("#DetailTaxcode").val(obj[0].MST_MNS);
                                            $("#DetailPersonal").val(obj[0].PERSONAL_NAME);
                                            $("#DetailCMND").val(obj[0].CMND);
                                            $("#DetailPhone").val(obj[0].PHONE_CONTRACT);
                                            $("#DetailEmail").val(obj[0].EMAIL_CONTRACT);
                                            $("#DetailPupore").val(obj[0].CERTIFICATION_PURPOSE_DESC);
                                            $("#DetailStatus").val(obj[0].CERTIFICATION_STATE_DESC);
                                            $("#DetailEffective").val(obj[0].EFFECTIVE_DT);
                                            $("#DetailExpiration").val(obj[0].EXPIRATION_DT);
                                            $("#DetailCertDN").val(obj[0].CERTIFICATION_SN);
                                        }
                                        else if (obj[0].Code === "1") {
                                            funErrorAlert(global_no_data);
                                        }
                                        else if (obj[0].Code === JS_EX_CSRF)
                                        {
                                            funCsrfAlert();
                                        } else if (obj[0].Code === JS_EX_LOGIN)
                                        {
                                            RedirectPageLoginNoSess(global_alert_login);
                                        }
                                        else if (obj[0].Code === JS_EX_ANOTHERLOGIN)
                                        {
                                            RedirectPageLoginNoSess(global_alert_another_login);
                                        }
                                        else {
                                            funErrorAlert(global_errorsql);
                                        }
                                    }
                                    else
                                    {
                                        funErrorAlert(global_no_data);
                                    }
                                }
                            });
                        }
                        $(document).ready(function () {
                            $('#myModalOTPHardware').on('show.bs.modal', function (e) {
//                                $('#myModalOTPHardware').modal('hide');
                                $(".loading-gifHardware").hide();
                                $(".loading-gif").hide();
                                $('#over').remove();
                                var Tag_ID = $(e.relatedTarget).data('book-id');
                                LoadCert(Tag_ID);
                            });
                        });
                        function ShowDialog(Tag_ID)
                        {
                            LoadCert(Tag_ID);
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
                </fieldset>
                <%}%>
                <!-- CERTÌICATION HISTORY -->
                <div style="clear: both;"></div>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitleCreateUser" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input type="text" readonly name="strCreateUser" class="form-control123" value="<%= EscapeUtils.CheckTextNull(rs[0][0].CREATED_BY)%>" />
                        </div>
                    </div>
                    <script>
                        $("#idLblTitleCreateUser").text(global_fm_user_create);
                    </script>
                </div>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitleCreateDate" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input class="form-control123" type="text" name="doDate" readonly value="<%= EscapeUtils.CheckTextNull(rs[0][0].CREATED_DT)%>"/>
                        </div>
                    </div>
                    <script>
                        $("#idLblTitleCreateDate").text(global_fm_date_create);
                    </script>
                </div>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitleUpdateUser" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input type="text" readonly name="strUpdateUser" class="form-control123" value="<%= EscapeUtils.CheckTextNull(rs[0][0].MODIFIED_BY)%>" />
                        </div>
                    </div>
                    <script>
                        $("#idLblTitleUpdateUser").text(global_fm_user_endupdate);
                    </script>
                </div>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitleUpdateDate" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input class="form-control123" type="text" name="doDate" readonly value="<%= EscapeUtils.CheckTextNull(rs[0][0].MODIFIED_DT)%>"/>
                        </div>
                    </div>
                    <script>
                        $("#idLblTitleUpdateDate").text(global_fm_date_endupdate);
                    </script>
                </div>
            </form>
        </div>
        <%
                    }
                }
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
        <style>
            @media (min-width: 768px) {
                .modal-dialog {
                    width: 900px;
                    margin: 30px auto;
                }
            }
        </style>
        <div id="myModalOTPHardware" class="modal fade" role="dialog">
            <div style="width: 100%; text-align: center; position: fixed;z-index: 1000;top: 0; padding-top: 90px;
                left: 0; height: 100%;" class="loading-gifHardware">
               <img src="../Images/ajax-loader1.gif" alt="Please wait..." />
           </div>
            <div class="modal-dialog modal-800" id="myDialogOTPHardware">
                <div class="modal-content">
                    <div class="modal-header">
                        <div style="width: 70%; float: left;">
                            <h3 id="idLblTitleDetailModalGroup" class="modal-title" style="font-size: 18px;"></h3>
                        </div>
                        <div style="width: 29%; float: right;text-align: right;">
                            <button id="idLblTitleDetailModalClose" type="button" onclick="CloseDialog();" class="btn btn-info"></button>
                        </div>
                        <script>
                            $("#idLblTitleDetailModalGroup").text(ca_group_cert);
                            $("#idLblTitleDetailModalClose").text(global_fm_button_close);
                        </script>
                    </div>
                    <div class="modal-body">
                        <form role="formAddOTPHardware" id="formOTPHardware">
                            <div class="col-sm-6" style="padding-left: 0;padding-bottom: 10px;">
                                <div class="form-group">
                                    <label id="idLblTitleDetailCompany" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                    <div class="col-sm-7" style="padding-right: 0px;">
                                        <input type="text" readonly id="DetailCompany" class="form-control123" />
                                    </div>
                                </div>
                                <script>
                                    $("#idLblTitleDetailCompany").text(global_fm_grid_company);
                                </script>
                            </div>
                            <div class="col-sm-6" style="padding-left: 0;padding-bottom: 10px;">
                                <div class="form-group">
                                    <label id="idLblTitleDetailTaxcode" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                    <div class="col-sm-7" style="padding-right: 0px;">
                                        <input class="form-control123" type="text" id="DetailTaxcode" readonly/>
                                    </div>
                                </div>
                                <script>
                                    $("#idLblTitleDetailTaxcode").text(global_fm_enterprise_id);
                                </script>
                            </div>
                            <div class="col-sm-6" style="padding-left: 0;padding-bottom: 10px;">
                                <div class="form-group">
                                    <label id="idLblTitleDetailPersonal" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                    <div class="col-sm-7" style="padding-right: 0px;">
                                        <input type="text" readonly id="DetailPersonal" class="form-control123" />
                                    </div>
                                </div>
                                <script>
                                    $("#idLblTitleDetailPersonal").text(global_fm_grid_personal);
                                </script>
                            </div>
                            <div class="col-sm-6" style="padding-left: 0;padding-bottom: 10px;">
                                <div class="form-group">
                                    <label id="idLblTitleDetailCMND" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                    <div class="col-sm-7" style="padding-right: 0px;">
                                        <input class="form-control123" type="text" id="DetailCMND" readonly/>
                                    </div>
                                </div>
                                <script>
                                    $("#idLblTitleDetailCMND").text(global_fm_personal_id);
                                </script>
                            </div>
                            <div class="col-sm-6" style="padding-left: 0;padding-bottom: 10px;">
                                <div class="form-group">
                                    <label id="idLblTitleDetailPhone" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                    <div class="col-sm-7" style="padding-right: 0px;">
                                        <input type="text" readonly id="DetailPhone" class="form-control123" />
                                    </div>
                                </div>
                                <script>
                                    $("#idLblTitleDetailPhone").text(token_fm_mobile);
                                </script>
                            </div>
                            <div class="col-sm-6" style="padding-left: 0;padding-bottom: 10px;">
                                <div class="form-group">
                                    <label id="idLblTitleDetailEmail" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                    <div class="col-sm-7" style="padding-right: 0px;">
                                        <input class="form-control123" type="text" id="DetailEmail" readonly/>
                                    </div>
                                </div>
                                <script>
                                    $("#idLblTitleDetailEmail").text(token_fm_email);
                                </script>
                            </div>
                            <div class="col-sm-6" style="padding-left: 0;padding-bottom: 10px;">
                                <div class="form-group">
                                    <label id="idLblTitleDetailPupore" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                    <div class="col-sm-7" style="padding-right: 0px;">
                                        <input type="text" readonly id="DetailPupore" class="form-control123" />
                                    </div>
                                </div>
                                <script>
                                    $("#idLblTitleDetailPupore").text(global_fm_certpurpose);
                                </script>
                            </div>
                            <div class="col-sm-6" style="padding-left: 0;padding-bottom: 10px;">
                                <div class="form-group">
                                    <label id="idLblTitleDetailStatus" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                    <div class="col-sm-7" style="padding-right: 0px;">
                                        <input class="form-control123" type="text" id="DetailStatus" readonly/>
                                    </div>
                                </div>
                                <script>
                                    $("#idLblTitleDetailStatus").text(global_fm_Status);
                                </script>
                            </div>
                            <div class="col-sm-6" style="padding-left: 0;padding-bottom: 10px;">
                                <div class="form-group">
                                    <label id="idLblTitleDetailEffective" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                    <div class="col-sm-7" style="padding-right: 0px;">
                                        <input type="text" readonly id="DetailEffective" class="form-control123" />
                                    </div>
                                </div>
                                <script>
                                    $("#idLblTitleDetailEffective").text(global_fm_valid_cert);
                                </script>
                            </div>
                            <div class="col-sm-6" style="padding-left: 0;padding-bottom: 10px;">
                                <div class="form-group">
                                    <label id="idLblTitleDetailExpiration" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                    <div class="col-sm-7" style="padding-right: 0px;">
                                        <input class="form-control123" type="text" id="DetailExpiration" readonly/>
                                    </div>
                                </div>
                                <script>
                                    $("#idLblTitleDetailExpiration").text(global_fm_Expire_cert);
                                </script>
                            </div>
                            <div class="col-sm-6" style="padding-left: 0;padding-bottom: 10px;">
                                <div class="form-group">
                                    <label id="idLblTitleDetailCertDN" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                    <div class="col-sm-7" style="padding-right: 0px;">
                                        <input class="form-control123" type="text" id="DetailCertDN" readonly/>
                                    </div>
                                </div>
                                <script>
                                    $("#idLblTitleDetailCertDN").text(global_fm_serial);
                                </script>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
        <script src="../style/jquery.min.js"></script>
        <script src="../style/bootstrap.min.js"></script>
<!--        <script src="../style/custom.min.js"></script>
            <script src="../js/moment.min.js"></script>-->
<!--        <script src="../js/moment.min_limit.js"></script>
        <script src="../js/daterangepicker_limit.js"></script>-->
       <!--<script src="../js/active/highlight.js"></script>-->
        <!-- <script src="../js/active/bootstrap-switch.js"></script>-->
        <!--<script src="../js/active/main.js"></script>-->
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
