<%-- 
    Document   : BranchNew
    Created on : Jun 16, 2014, 11:02:43 AM
    Author     : Thanh
--%>

<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="vn.ra.utility.PropertiesContent"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DateFormat"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="../Admin/ConnectionParam.jsp" %>
<!DOCTYPE html>
<%
    String isCALoad = LoadParamSystem.getParamStart(Definitions.CONFIG_IS_WHICH_ABOUT_CA);
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
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
        <script type="text/javascript" src="../js/jquery.js"></script>
        <link rel="stylesheet" href="../js/sweetalert.css"/>
        <link href="../style/customportal.min.css" rel="stylesheet">
        <script src="../js/sweetalert-dev.js"></script>
        <script type="text/javascript" src="../Css/GlobalAlert.js"></script>
        <title></title>
        <link href="../js/checkphone/intlTelInput.css" rel="stylesheet" type="text/css"/>
        <script src="../js/checkphone/intlTelInput.js" type="text/javascript"></script>
        <script type="text/javascript">
            document.title = branch_title_add;
            changeFavicon("../");
            $(document).ready(function () {
                $('.loading-gif').hide();
                $("#BASE64_IMG").val('');
            });
            function ValidateForm(idCSRF) {
                var varCALoad = '<%=isCALoad%>';
                if (!JSCheckEmptyField(document.myname.BranchCode.value))
                {
                    document.myname.BranchCode.focus();
                    funErrorAlert(branch_req_code);
                    return false;
                } else {
                    if (JSCheckSpaceField(document.myname.BranchCode.value))
                    {
                        document.myname.BranchCode.focus();
                        funErrorAlert(global_fm_Branch + global_req_no_space);
                        return false;
                    }
                }
                if (!JSCheckEmptyField(document.myname.Remark.value))
                {
                    document.myname.Remark.focus();
                    if(varCALoad !== JS_IS_WHICH_ABOUT_CA_ICA) {
                        funErrorAlert(policy_req_empty + global_fm_remark_vn);
                    } else {funErrorAlert(policy_req_empty + global_fm_remark_agency_vn);}
                    return false;
                }
                if (!JSCheckEmptyField(document.myname.Remark_EN.value))
                {
                    document.myname.Remark_EN.focus();
                    if(varCALoad !== JS_IS_WHICH_ABOUT_CA_ICA){
                        funErrorAlert(policy_req_empty + global_fm_remark_en);
                    } else {funErrorAlert(policy_req_empty + global_fm_remark_agency_en);}
                    return false;
                }
                if(varCALoad !== JS_IS_WHICH_ABOUT_CA_ICA){
                    if (!JSCheckEmptyField(document.myname.Address.value))
                    {
                        document.myname.Address.focus();
                        funErrorAlert(global_req_address);
                        return false;
                    }
                }
                
                if (!JSCheckEmptyField(document.myname.WorkPhone.value))
                {
                    if(varCALoad !== JS_IS_WHICH_ABOUT_CA_ICA){
                        document.myname.WorkPhone.focus();
                        funErrorAlert(policy_req_empty + global_fm_phone);
                        return false;
                    }
                }
                else {
                    if (!FormCheckPhoneHand(localStorage.getItem("sessLocal_REGEX_PHONE"), $("#WorkPhone")))
                    {
                        $("#WorkPhone").focus();
                        funErrorAlert(global_req_phone_format);
                        return false;
                    }
                }
                if (!JSCheckEmptyField(document.myname.EMAIL.value))
                {
                    if(varCALoad !== JS_IS_WHICH_ABOUT_CA_ICA){
                        document.myname.EMAIL.focus();
                        funErrorAlert(policy_req_empty + global_fm_email);
                        return false;
                    }
                } else {
                    if (!FormCheckEmailSearchHand(localStorage.getItem("sessLocal_REGEX_EMAIL"), document.myname.EMAIL.value))
                    {
                        document.myname.EMAIL.focus();
                        funErrorAlert(global_req_mail_format);
                        return false;
                    }
                }
                if(varCALoad !== JS_IS_WHICH_ABOUT_CA_ICA) {
                    if (!JSCheckEmptyField(document.myname.REPRESENTATIVE.value))
                    {
                        document.myname.REPRESENTATIVE.focus();
                        funErrorAlert(policy_req_empty + branch_fm_representative);
                        return false;
                    }
                }
                if (!JSCheckEmptyField(document.myname.User_Username.value))
                {
                    document.myname.User_Username.focus();
                    funErrorAlert(policy_req_empty + global_fm_Username);
                    return false;
                }
                if (!JSCheckEmptyField(document.myname.User_Fullname.value))
                {
                    document.myname.User_Fullname.focus();
                    funErrorAlert(policy_req_empty + global_fm_fullname);
                    return false;
                }
                
                if (!JSCheckEmptyField(document.myname.User_WorkPhone.value))
                {
                    document.myname.User_WorkPhone.focus();
                    funErrorAlert(policy_req_empty + global_fm_phone);
                    return false;
                }
                else {
                    if (!FormCheckPhoneHand(localStorage.getItem("sessLocal_REGEX_PHONE"), $("#User_WorkPhone")))
                    {
                        $("#User_WorkPhone").focus();
                        funErrorAlert(global_req_phone_format);
                        return false;
                    }
                }
                if (!JSCheckEmptyField(document.myname.User_EMAIL.value))
                {
                    document.myname.User_EMAIL.focus();
                    funErrorAlert(policy_req_empty + global_fm_email);
                    return false;
                } else {
                    if (!FormCheckEmailSearchHand(localStorage.getItem("sessLocal_REGEX_EMAIL"), document.myname.EMAIL.value))
                    {
                        document.myname.User_EMAIL.focus();
                        funErrorAlert(global_req_mail_format);
                        return false;
                    }
                }
                var isShareModeEnabled = "0";
                if ($("#shareModeEnabled").is(':checked'))
                {
                    isShareModeEnabled = "1";
                }
                var isIssueP12Enabled = "0";
                if ($("#issueP12Enabled").is(':checked'))
                {
                    isIssueP12Enabled = "1";
                }
                $('body').append('<div id="over"></div>');
                $(".loading-gif").show();
                $.ajax({
                    type: "post",
                    url: "../BranchCommon",
                    data: {
                        idParam: 'addbranch',
                        BranchCode: document.myname.BranchCode.value,
                        Remark: document.myname.Remark.value,
                        issueP12Enabled: isIssueP12Enabled,
                        Remark_EN: document.myname.Remark_EN.value,
                        BranchParent: document.myname.BranchParent.value,
                        CityProvince: $("#CityProvince").val(),
                        DISCOUNT_RATE_PROFILE: $("#DISCOUNT_RATE_PROFILE").val(),
                        EMAIL: document.myname.EMAIL.value,
                        REPRESENTATIVE: document.myname.REPRESENTATIVE.value,
                        REPRESENTATIVE_POSITION: document.myname.REPRESENTATIVE_POSITION.value,
                        TAX_CODE: document.myname.TAX_CODE.value,
                        Logo: $("#BASE64_IMG").val(),
                        WorkPhone: document.myname.WorkPhone.value,
                        Address: document.myname.Address.value,
                        User_Username: document.myname.User_Username.value,
                        User_Fullname: document.myname.User_Fullname.value,
                        User_WorkPhone: document.myname.User_WorkPhone.value,
                        User_EMAIL: document.myname.User_EMAIL.value,
                        BRANCH_ROLE: $("#BRANCH_ROLE").val(),
                        CALLBACK_URL_NOTICE: $("#CALLBACK_URL_NOTICE").val(),
                        CALLBACK_WHEN_APPROVE: $("#CALLBACK_WHEN_APPROVE").val(),
                        idShareModeEnabled: isShareModeEnabled,
                        CsrfToken: idCSRF
                    },
                    cache: false,
                    success: function (html)
                    {
                        var myStrings = sSpace(html).split('#');
                        if (myStrings[0] === "0")
                        {
                            $("#BASE64_IMG").val('');
                            if(myStrings[1] === "USER_EXISTS") {
                                funSuccAlert(branch_warning_add, "BranchList.jsp");
                            } else {
                                funSuccAlert(branch_succ_add, "BranchList.jsp");
                            }
                        }
                        else if (myStrings[0] === "10")
                        {
                            funErrorAlert(global_req_all);
                        }
                        else if (myStrings[0] === "11")
                        {
                            funErrorAlert(global_req_length);
                        }
                        else if (myStrings[0] === "12")
                        {
                            funErrorAlert(branch_fm_code + policy_req_unicode);
                        }
                        else if (myStrings[0] === JS_EX_CSRF)
                        {
                            funCsrfAlert();
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
                            if (myStrings[1] === "1") {
                                funErrorAlert(branch_exists_code);
                            } else if (myStrings[1] === JS_STR_ERROR_CODE_99) {
                                funErrorAlert(global_error_login_info);
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
            function closeForm()
            {
                window.location = "BranchList.jsp";
            }
            function downloadSampleLogo()
            {
                var f = document.myname;
                f.method = "post";
                f.action = '../DownFromSaveFile?idParam=downfilesamplelogo';
                f.submit();
            }
            function UploadImageBase64(input1)
            {
                if (input1.value !== '')
                {
                    var checkFileName = input1.value.substring(input1.value.lastIndexOf('.') + 1);
                    if (checkFileName === "jpg" || checkFileName === "png" || checkFileName === "JPG"
                        || checkFileName === "PNG" || checkFileName === "gif" || checkFileName === "GIF")
                    {
                        $("#BASE64_IMG").val('');
                        $('body').append('<div id="over"></div>');
                        $(".loading-gif").show();
                        file1 = input1.files[0];
                        var data1 = new FormData();
                        data1.append('file', file1);
                        $.ajax({
                            url: "../ReadFileBase64",
                            data: data1,
                            cache: false,
                            contentType: false,
                            processData: false,
                            type: 'POST',
                            enctype: "multipart/form-data",
                            success: function (html) {
                                var myStrings = sSpace(html).split('###');
                                if (myStrings[0] === "0")
                                {
                                    $("#BASE64_IMG").val(myStrings[1]);
                                }
                                else if (myStrings[0] === JS_EX_LOGIN)
                                {
                                    RedirectPageLoginNoSess(global_alert_login);
                                }
                                else if (myStrings[0] === JS_EX_ANOTHERLOGIN)
                                {
                                    RedirectPageLoginNoSess(global_alert_another_login);
                                }
                                else if (myStrings[0] === JS_EX_GREAT_SIZE)
                                {
                                    funErrorAlert(branch_error_logo_great_size);
                                }
                                else
                                {
                                    funErrorAlert(global_errorsql);
                                }
                                $(".loading-gif").hide();
                                $('#over').remove();
                            }
                        });
                    }
                    else
                    {
                        funErrorAlert(global_req_image_format);
                    }
                }
                else
                {
                    funErrorAlert(global_req_file);
                }
            }
        </script>
    </head>
    <body class="nav-md">
        <%
        if ((session.getAttribute("sUserID")) != null) {
                String anticsrf = "";
                anticsrf = "" + Math.random();
                request.getSession().setAttribute("anticsrf", anticsrf);
                String sessLanguageGlobal = session.getAttribute("sessVN").toString();
                String sessUserAgentID = session.getAttribute("SessUserAgentID").toString().trim();
                String sRegexPolicy = "";
                String sSYS_DISCOUNT_RATE = "";
                String sSHARE_CERTIFICATE_MODE = "";
                String sCallBackEnabled = "1";
                String sCallBackApproveEnabled = "1";
                String sCallBackApproveValue = "";
                String sIssueP12Enabled = "";
                boolean approveCAProfileEnabled = false;
                GENERAL_POLICY[][] sessGeneralPolicy = (GENERAL_POLICY[][]) session.getAttribute("sessGeneralPolicy_System");
                if (sessGeneralPolicy[0].length > 0) {
                    for(GENERAL_POLICY rsPolicy1: sessGeneralPolicy[0])
                    {
                        if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_SYS_REGEX_FOR_PHONE_EMAIL))
                        {
                            sRegexPolicy = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                        }
                        if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_SYS_DISCOUNT_RATE_PROFILE_OPTION))
                        {
                            sSYS_DISCOUNT_RATE = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                        }
                        if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_SYS_DEFAULT_SHARE_CERTIFICATE_MODE_FOR_BRANCH))
                        {
                            sSHARE_CERTIFICATE_MODE = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                        }
                        if (rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_SYS_AUTO_APPROVED_FOR_EACH_CERTIFICATION_PROFILE_OPTION)) {
                            if("1".equals(EscapeUtils.CheckTextNull(rsPolicy1.VALUE))) {
                                approveCAProfileEnabled = true;
                            }
                        }
                        if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_FO_PUSH_NOTIFICATION_CALLBACK_URL_ENABLED)) {
                            sCallBackEnabled = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                        }
                        if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_BO_OPTION_ALLOW_ISSUING_P12)) {
                            sIssueP12Enabled = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                        }
                        if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_CALLBACK_URL_APPROVED_ENABLED)) {
                            sCallBackApproveEnabled = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                        }
                        if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_CALLBACK_URL_APPROVED_VALUE)) {
                            sCallBackApproveValue = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                        }
                    }
                }
                String sREGEX_PHONE = Definitions.CONFIG_DEFAULT_VALUE_REGEX_PHONE;
                String sREGEX_EMAIL = Definitions.CONFIG_DEFAULT_VALUE_REGEX_EMAIL;
                if(!"".equals(sRegexPolicy))
                {
                    sREGEX_PHONE = PropertiesContent.getPropertiesContentKey(sRegexPolicy, Definitions.CONFIG_REGEX_PHONE);
                    sREGEX_EMAIL = PropertiesContent.getPropertiesContentKey(sRegexPolicy, Definitions.CONFIG_REGEX_EMAIL);
                }
                String sViewRose = "none";
                if("1".equals(sSYS_DISCOUNT_RATE))
                {
                    sViewRose = "";
                }
                String sessAgentID = session.getAttribute("SessAgentID").toString().trim();
        %> 
        <script>
            $(document).ready(function () {
                localStorage.setItem("sessLocal_REGEX_PHONE", '<%=sREGEX_PHONE%>');
                localStorage.setItem("sessLocal_REGEX_EMAIL", '<%=sREGEX_EMAIL%>');
            });
        </script>
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
                        document.getElementById("idNameURL").innerHTML = branch_title_list;
                    </script>
                </div>
                <div class="right_col" role="main">
                    <div class="">
                        <div class="row">
                            <div class="col-md-12 col-sm-12 col-xs-12">
                                <div class="x_panel">
                                    <div class="x_title">
                                        <h2><i class="fa fa-list-ul"></i> <span id="idLblTitleEdits" style="color: #36526D;"></span></h2>
                                        <script>$("#idLblTitleEdits").text(branch_title_add);</script>
                                        <ul class="nav navbar-right panel_toolbox">
                                            <li style="padding-right: 10px;">
                                                <%
                                                    boolean isAccessBranch = false;
                                                    String sAgentAccessBranch = conf.GetPropertybyCode(Definitions.CONFIG_BRANCH_TREE_ENABLED);
                                                    if("1".equals(sAgentAccessBranch)) {
                                                        isAccessBranch = true;
                                                    } else {
                                                        if(sessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                                            isAccessBranch = true;
                                                        }
                                                    }
                                                    if(isAccessBranch == true) {
                                                %>
                                                <input id="btnSave" type="button" class="btn btn-info" onclick="return ValidateForm('<%=anticsrf%>');"/>
                                                <script>document.getElementById("btnSave").value = global_fm_button_add;</script>
                                                <%
                                                    }
                                                %>
                                                <input id="btnClose" class="btn btn-info" type="button" onclick="closeForm();" />
                                                <input type="hidden" name="CsrfToken" value="<%=anticsrf%>"/>
                                                <script>document.getElementById("btnClose").value = global_fm_button_back;</script>
                                            </li>
                                        </ul>
                                        <div class="clearfix"></div>
                                    </div>
                                    <div class="x_content">
                                        <form name="myname" method="post" class="form-horizontal">
                                            <%
                                                try {
                                            %>
                                            <fieldset class="scheduler-border" style="clear: both;">
                                                <legend class="scheduler-border"><script>document.write(branch_title_info);</script></legend>
                                            
                                                <div class="form-group" style="display: none;">
                                                    <label class="control-label col-sm-2" style="color: #000000; padding-left: 0; font-weight: bold;text-align: left;"><script>document.write(branch_fm_choise_new);</script></label>
                                                    <div class="col-sm-10">
                                                        <label class="radio-inline"><input type="radio" name="nameCheck" id="nameCheck1" checked></label>
                                                        <!--<script>document.write(branch_fm_choise_CN);</script>-->
                                                        <label class="radio-inline"><input type="radio" name="nameCheck" id="nameCheck2"></label>
                                                        <!--<script>document.write(branch_fm_choise_PGD);</script>-->
                                                        <input type="text" style="display: none;" id="idSessIsChoise" name="idSessIsChoise"/>
                                                    </div>
                                                    <script>
                                                        $("#idSessIsChoise").val('1');
                                                        $('.radio-inline').on('click', function () {
                                                            var s = $(this).find('input').attr('id');
                                                            if (s === 'nameCheck1')
                                                            {
                                                                //$("select#Regions")[0].selectedIndex = 0;
                                                                $("#idSessIsChoise").val('1');
                                                                $('#divBranchParent').css('display', 'none');
                                                            }
                                                            if (s === 'nameCheck2')
                                                            {
                                                                //$("select#BranchParent")[0].selectedIndex = 0;
                                                                $("#idSessIsChoise").val('0');
                                                                $('#divBranchParent').css('display', '');
                                                            }
                                                        });
                                                        function ValueExistsOptions(selectbox, text)
                                                        {
                                                            var s = 0;
                                                            for (var i = 0, l = selectbox.length; i < l; i++)
                                                            {
                                                                if (selectbox.options[i].value.split("###")[0] === text) {
                                                                    s = i;
                                                                }
                                                            }
                                                            selectbox.selectedIndex = s;
                                                        }
                                                        function changeSimbol(obj) {
                                                            var cbxCity = document.getElementById('CityProvince');
                                                            ValueExistsOptions(cbxCity, obj.value.split("###")[1]);
                                                            changeCity(cbxCity.options[cbxCity.selectedIndex]);
                                                        }
                                                        function changeCity(obj) {
                                                            document.getElementById('Regions').value = obj.value.split("###")[1];
                                                            document.getElementById('RegionsDes').value = obj.value.split("###")[2];
                                                        }
                                                    </script>
                                                </div>
                                                <div class="col-sm-6" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                                            <label id="idLblTitleCode"></label>
                                                            <label id="idLblNoteCode"class="CssRequireField"></label>
                                                        </label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <input class="form-control123" maxlength="64" id="BranchCode"
                                                                name="BranchCode" oninput="OnBlurBranch('User_Username', this.value, this);"/>
                                                        </div>
                                                    </div>
                                                    <script>
                                                        $("#idLblTitleCode").text(branch_fm_code);
                                                        $("#idLblNoteCode").text(global_fm_require_label);
                                                    </script>
                                                </div>
                                                <div class="col-sm-6" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                                            <label id="idLblTitleParent"></label>
                                                            <label id="idLblNoteParent"class="CssRequireField"></label>
                                                        </label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <select name="BranchParent" id="BranchParent" class="form-control123">
                                                                <%
                                                                    BRANCH[][] rsNoNull;
                                                                    try {
                                                                        if("1".equals(sAgentAccessBranch)) {
                                                                            String sSessTreeName = "sessTreeBranchSystem";
                                                                            if(sessUserAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                                                                sSessTreeName = "sessTreeBranchSystemRoot";
                                                                            }
                                                                            rsNoNull = (BRANCH[][]) session.getAttribute(sSessTreeName);
                                                                        } else {
                                                                            rsNoNull = new BRANCH[1][];
                                                                            db.S_BO_PARENT_BRANCH_COMBOBOX(sessLanguageGlobal, rsNoNull);
                                                                        }
                                                                        if (rsNoNull[0].length > 0) {
                                                                            for (int i = 0; i < rsNoNull[0].length; i++) {
                                                                                String sValueParent = String.valueOf(rsNoNull[0][i].ID);
                                                                                String sBranchName = rsNoNull[0][i].NAME + " - " +rsNoNull[0][i].REMARK;
                                                                %>
                                                                <option value="<%=sValueParent%>"><%=sBranchName%></option>
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
                                                    <script>
                                                        $("#idLblTitleParent").text(branch_fm_parent);
                                                        $("#idLblNoteParent").text(global_fm_require_label);
                                                    </script>
                                                </div>
                                                <div class="col-sm-6" style="padding-left: 0;display: <%=sViewRose%>">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                                            <label id="idLblTitleRose"></label>
                                                        </label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <select name="DISCOUNT_RATE_PROFILE" id="DISCOUNT_RATE_PROFILE" class="form-control123">
                                                                <option value="<%= Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL%>"><script>document.write(global_fm_combox_empty);</script></option>
                                                                <%
                                                                    DISCOUNT_RATE_PROFILE[][] rsRose = new DISCOUNT_RATE_PROFILE[1][];
                                                                    try {
                                                                        db.S_BO_DISCOUNT_RATE_PROFILE_COMBOBOX(sessLanguageGlobal, rsRose);
                                                                        if (rsRose[0].length > 0) {
                                                                            for (int i = 0; i < rsRose[0].length; i++) {
                                                                                String sValueParent = String.valueOf(rsRose[0][i].ID);
                                                                %>
                                                                <option value="<%=sValueParent%>"><%=rsRose[0][i].REMARK%></option>
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
                                                    <script>
                                                        $("#idLblTitleRose").text(rose_fm_rose);
                                                    </script>
                                                </div>
                                                <div class="col-sm-6" style="padding-left: 0;display: <%= approveCAProfileEnabled ? "" : "none" %>">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                                            <label id="idLblTitleProfileAccess"></label>
                                                        </label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <select name="BRANCH_ROLE" id="BRANCH_ROLE" class="form-control123">
                                                                <option value="<%= Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL%>"><script>document.write(global_fm_combox_empty);</script></option>
                                                                <%
                                                                    String sChoiseRoleOption = "";
                                                                    if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA)) {
                                                                        if(!sessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                                                            sChoiseRoleOption = "2";
                                                                        }
                                                                    }
                                                                    BRANCH_ROLE[][] rsBranchRole = new BRANCH_ROLE[1][];
                                                                    try {
                                                                        db.S_BO_BRANCH_ROLE_COMBOBOX(sessLanguageGlobal, rsBranchRole);
                                                                        if (rsBranchRole[0].length > 0) {
                                                                            for (int i = 0; i < rsBranchRole[0].length; i++) {
                                                                                String sValueParent = String.valueOf(rsBranchRole[0][i].ID);
                                                                %>
                                                            <option <%= !"".equals(sChoiseRoleOption) ? !sValueParent.equals(sChoiseRoleOption) ? "style='display:none;'" : "" : "" %> value="<%=sValueParent%>"><%=rsBranchRole[0][i].REMARK%></option>
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
                                                            <script>
                                                                $(document).ready(function () {
                                                                    if(IsWhichCA === "18") {
                                                                        document.getElementById("BRANCH_ROLE").selectedIndex = "1";
                                                                    }
                                                                });
                                                            </script>
                                                        </div>
                                                    </div>
                                                    <script>
                                                        $("#idLblTitleProfileAccess").text(profileaccss_fm_rose);
                                                    </script>
                                                </div>
                                                <div class="col-sm-6" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                                            <label id="idLblTitleRemark"></label>
                                                            <label id="idLblNoteRemark"class="CssRequireField"></label>
                                                        </label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <input class="form-control123" maxlength="256" id="Remark" name="Remark"
                                                                oninput="OnBlurBranch('User_Fullname', this.value, this);"/>
                                                        </div>
                                                    </div>
                                                    <script>
                                                        $(document).ready(function () {
                                                            if('<%=isCALoad%>' === JS_IS_WHICH_ABOUT_CA_ICA){
                                                                $("#idLblTitleRemark").text(global_fm_remark_agency_vn);
                                                            } else {$("#idLblTitleRemark").text(global_fm_remark_vn);}
                                                            $("#idLblNoteRemark").text(global_fm_require_label);
                                                        });
                                                    </script>
                                                </div>
                                                <div class="col-sm-6" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                                            <label id="idLblTitleRemark_EN"></label>
                                                            <label id="idLblNoteRemark_EN"class="CssRequireField"></label>
                                                        </label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <input class="form-control123" maxlength="256" id="Remark_EN" name="Remark_EN"/>
                                                        </div>
                                                    </div>
                                                    <script>
                                                        $(document).ready(function () {
                                                            if('<%=isCALoad%>' === JS_IS_WHICH_ABOUT_CA_ICA){
                                                                $("#idLblTitleRemark_EN").text(global_fm_remark_agency_en);
                                                            } else {$("#idLblTitleRemark_EN").text(global_fm_remark_en);}
                                                            $("#idLblNoteRemark_EN").text(global_fm_require_label);
                                                        });
                                                    </script>
                                                </div>
                                                <div class="col-sm-6" style="padding-left: 0;" id="IsCityProvincCN">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                                            <label id="idLblTitleCity"></label>
                                                            <label id="idLblNoteCity"class="CssRequireField"></label>
                                                        </label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <select name="CityProvince" id="CityProvince" class="form-control123" onchange="changeCity(this);">
                                                                <%
                                                                    CITY_PROVINCE[][] rsCity = new CITY_PROVINCE[1][];
                                                                    try {
                                                                        db.S_BO_PROVINCE_COMBOBOX(sessLanguageGlobal, rsCity);
                                                                        if (rsCity[0].length > 0) {
                                                                            for (int i = 0; i < rsCity[0].length; i++) {
                                                                %>
                                                                <option value="<%=String.valueOf(rsCity[0][i].ID)%>"><%=rsCity[0][i].REMARK %></option>
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
                                                    <script>
                                                        $("#idLblTitleCity").text(global_fm_city);
                                                        $("#idLblNoteCity").text(global_fm_require_label);
                                                    </script>
                                                </div>
                                                <div class="col-sm-6" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                                            <label id="idLblTitleAddress"></label>
                                                            <label id="idLblNoteAddress"class="CssRequireField" style="display: <%= isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA) ? "none" : ""%>"></label>
                                                        </label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <input class="form-control123" name="Address" maxlength="256">
                                                        </div>
                                                    </div>
                                                    <script>
                                                        $("#idLblTitleAddress").text(global_fm_address);
                                                        $("#idLblNoteAddress").text(global_fm_require_label);
                                                    </script>
                                                </div>
                                                <div class="col-sm-6" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                                            <label id="idLblTitlePhone"></label>
                                                            <label id="idLblNotePhone"class="CssRequireField" style="display: <%= isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA) ? "none" : ""%>"></label>
                                                        </label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <input class="form-control123" id="WorkPhone" name="WorkPhone" maxlength="16"
                                                                onblur="autoTrimTextField('WorkPhone', this.value);" oninput="OnBlurBranch('User_WorkPhone', this.value, this);">
                                                        </div>
                                                    </div>
                                                    <script>
                                                        $("#idLblTitlePhone").text(global_fm_phone);
                                                        $("#idLblNotePhone").text(global_fm_require_label);
                                                    </script>
                                                </div>
                                                <div class="col-sm-6" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                                            <label id="idLblTitleEmail"></label>
                                                            <label id="idLblNoteEmail"class="CssRequireField" style="display: <%= isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA) ? "none" : ""%>"></label>
                                                        </label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <input class="form-control123" maxlength="128" id="EMAIL" name="EMAIL" oninput="OnBlurBranch('User_EMAIL', this.value, this);">
                                                        </div>
                                                    </div>
                                                    <script>
                                                        $("#idLblTitleEmail").text(global_fm_email);
                                                        $("#idLblNoteEmail").text(global_fm_require_label);
                                                    </script>
                                                </div>
                                                <div class="col-sm-6" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                                            <label id="idLblTitleRepresentative"></label>
                                                            <label id="idLblNoteRepresentative"class="CssRequireField" style="display: <%= isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA) ? "none" : ""%>"></label>
                                                        </label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <input class="form-control123" maxlength="64" id="REPRESENTATIVE" name="REPRESENTATIVE">
                                                        </div>
                                                    </div>
                                                    <script>
                                                        $("#idLblTitleRepresentative").text(branch_fm_representative);
                                                        $("#idLblNoteRepresentative").text(global_fm_require_label);
                                                    </script>
                                                </div>
                                                <div class="col-sm-6" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                                            <label id="idLblTitlePosition"></label>
                                                        </label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <input class="form-control123" maxlength="64" id="REPRESENTATIVE_POSITION" name="REPRESENTATIVE_POSITION">
                                                        </div>
                                                    </div>
                                                    <script>
                                                        $("#idLblTitlePosition").text(branch_fm_representative_position);
                                                    </script>
                                                </div>
                                                <div class="col-sm-6" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                                            <label id="idLblTitleMST"></label>
                                                        </label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <input class="form-control123" maxlength="32" id="TAX_CODE" name="TAX_CODE">
                                                        </div>
                                                    </div>
                                                    <script>
                                                        $("#idLblTitleMST").text(global_fm_MST);
                                                    </script>
                                                </div>
                                                <%
                                                    String certAccessEnabled = conf.GetPropertybyCode(Definitions.CONFIG_CERT_MODULES_ACCESS_ENABLED);
                                                %>
                                                <div class="col-sm-6" style="padding-left: 0; display: <%= "1".equals(certAccessEnabled) ? "" : "none" %>">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                                            <label id="idLblTitleShareCert"></label>
                                                        </label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <label class="switch" for="shareModeEnabled" style="margin-bottom: 0;">
                                                                <input type="checkbox" name="shareModeEnabled" id="shareModeEnabled" <%= "1".equals(sSHARE_CERTIFICATE_MODE) ? "checked" : ""%>/>
                                                                <div id="shareModeEnabledClass" class="slider round"></div>
                                                            </label>
                                                        </div>
                                                    </div>
                                                    <script>
                                                        $("#idLblTitleShareCert").text(global_fm_share_mode_cert);
                                                    </script>
                                                </div>
                                                <%
                                                    if(!sessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)){
                                                        sIssueP12Enabled = "0";
                                                    }
                                                %>
                                                <div class="col-sm-6" style="padding-left: 0; <%= "1".equals(certAccessEnabled) ? "clear: both;" : "" %> <%= sessAgentID.equals(Definitions.CONFIG_AGENT_ROOT) ? "" : "display:none" %>">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                                            <label id="idLblTitleIssueP12Enabled"></label>
                                                        </label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <label class="switch" for="issueP12Enabled" style="margin-bottom: 0;">
                                                                <input type="checkbox" name="issueP12Enabled" id="issueP12Enabled" <%= "1".equals(sIssueP12Enabled) ? "checked" : ""%>/>
                                                                <div id="issueP12EnabledClass" class="slider round"></div>
                                                            </label>
                                                        </div>
                                                    </div>
                                                    <script>
                                                        $("#idLblTitleIssueP12Enabled").text(global_fm_issue_p12_enabled);
                                                    </script>
                                                </div>
                                                <div class="col-sm-6" style="padding-left: 0;display: <%= "1".equals(sCallBackApproveEnabled) ? "" : "none" %>;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                                            <label id="idLblTitleCallbackWhenApprove"></label>
                                                        </label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <input class="form-control123" maxlength="256" value="<%= sCallBackApproveValue%>" id="CALLBACK_WHEN_APPROVE" name="CALLBACK_WHEN_APPROVE">
                                                        </div>
                                                    </div>
                                                    <script>
                                                        $("#idLblTitleCallbackWhenApprove").text(global_fm_callback_when_approve);
                                                    </script>
                                                </div>
                                                                
                                                <div class="col-sm-6" style="padding-left: 0;display: <%= "1".equals(sCallBackEnabled) ? "" : "none" %>">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                                            <label id="idLblTitleCallbackUrl"></label>
                                                        </label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <input class="form-control123" maxlength="256" id="CALLBACK_URL_NOTICE" name="CALLBACK_URL_NOTICE">
                                                        </div>
                                                    </div>
                                                    <script>
                                                        $("#idLblTitleCallbackUrl").text(global_fm_callback_url);
                                                    </script>
                                                </div>
                                                                
                                                <div class="col-sm-13" style="padding: 0px 0px 0 0px;margin: 0;clear: both;">
                                                    <label class="control-label123" id="idLblTitleLogo"></label>
                                                    <INPUT id="uploadImg" class="form-control123" NAME="uploadImg" TYPE="file"
                                                        accept=".png,.jpg,.gif" onchange="UploadImageBase64(this);" />
                                                    <input type="hidden" id="BASE64_IMG" name="BASE64_IMG">
                                                    <div style="width: 100%; height: 6px;"></div>
                                                    <label class="control-label123" style="color: red; font-weight: 300;" id="idLblTitleBranchNote"></label>
                                                    <br/>
                                                    <label class="control-label123" id="idLblTitleLogoDown"></label>
                                                    <a style="cursor: pointer;" onclick="return downloadSampleLogo();" id="idLblTitleDownHere"></a>
                                                    <script>
                                                        $("#idLblTitleLogo").text(branch_fm_logo);
                                                        $("#idLblTitleBranchNote").text(branch_fm_logo_note);
                                                        $("#idLblTitleLogoDown").text(branch_fm_logo_down);
                                                        $("#idLblTitleDownHere").text(error_content_link_out);
                                                    </script>
                                                </div>
                                            </fieldset>
                                            <fieldset class="scheduler-border" style="clear: both;">
                                                <legend class="scheduler-border"><script>document.write(user_title_info);</script></legend>
                                                <div class="col-sm-6" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                                            <label id="idLblTitleUser_Username"></label>
                                                            <label id="idLblNoteUser_Username"class="CssRequireField"></label>
                                                        </label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <input class="form-control123" maxlength="64" id="User_Username" name="User_Username"/>
                                                        </div>
                                                    </div>
                                                    <script>
                                                        $("#idLblTitleUser_Username").text(global_fm_Username);
                                                        $("#idLblNoteUser_Username").text(global_fm_require_label);
                                                    </script>
                                                </div>
                                                <div class="col-sm-6" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                                            <label id="idLblTitleUser_Fullname"></label>
                                                            <label id="idLblNoteUser_Fullname"class="CssRequireField"></label>
                                                        </label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <input class="form-control123" maxlength="64" id="User_Fullname" name="User_Fullname">
                                                        </div>
                                                    </div>
                                                    <script>
                                                        $("#idLblTitleUser_Fullname").text(global_fm_fullname);
                                                        $("#idLblNoteUser_Fullname").text(global_fm_require_label);
                                                    </script>
                                                </div>
                                                <div class="col-sm-6" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                                            <label id="idLblTitleUser_Phone"></label>
                                                            <label id="idLblNoteUser_Phone"class="CssRequireField"></label>
                                                        </label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <input class="form-control123" id="User_WorkPhone" name="User_WorkPhone" maxlength="16"
                                                                onblur="autoTrimTextField('WorkPhone', this.value);">
                                                        </div>
                                                    </div>
                                                    <script>
                                                        $("#idLblTitleUser_Phone").text(global_fm_phone);
                                                        $("#idLblNoteUser_Phone").text(global_fm_require_label);
                                                    </script>
                                                </div>
                                                <div class="col-sm-6" style="padding-left: 0;">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                                            <label id="idLblTitleUser_Email"></label>
                                                            <label id="idLblNoteUser_Email"class="CssRequireField"></label>
                                                        </label>
                                                        <div class="col-sm-7" style="padding-right: 0px;">
                                                            <input class="form-control123" maxlength="128" id="User_EMAIL" name="User_EMAIL">
                                                        </div>
                                                    </div>
                                                    <script>
                                                        $("#idLblTitleUser_Email").text(global_fm_email);
                                                        $("#idLblNoteUser_Email").text(global_fm_require_label);
                                                    </script>
                                                </div>
                                            </fieldset>
                                            <script>
                                                function OnBlurBranch(objInput, objValue, objCode)
                                                {
                                                    if(objInput === 'User_Username')
                                                    {
                                                        $("#"+objInput).val(objValue.toLowerCase() + "_admin");
                                                        objCode.value = objCode.value.toUpperCase();
                                                    } else {
                                                        $("#"+objInput).val(objValue);
                                                    }
                                                }
                                            </script>                
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
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <%@ include file="../Modules/Footer.jsp" %>
            </div>
        </div>
        <script src="../style/jquery.min.js"></script>
        <script src="../style/bootstrap.min.js"></script>
        <script src="../style/custom.min.js"></script>
        <link href="../js/checkphone/intlTelInput.css" rel="stylesheet" type="text/css"/>
        <script src="../js/checkphone/intlTelInput.js" type="text/javascript"></script>
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
