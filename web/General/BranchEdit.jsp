<%-- 
    Document   : BranchEdit
    Created on : Jun 16, 2014, 11:02:33 AM
    Author     : Thanh
--%>

<%@page import="vn.ra.utility.LoadParamSystem"%>
<%@page import="vn.ra.utility.PropertiesContent"%>
<%@page import="javax.imageio.ImageIO"%>
<%@page import="java.awt.image.BufferedImage"%>
<%@page import="org.apache.commons.codec.binary.Base64"%>
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
        <%
            Config conf = new Config();
            String sNewRefreshJS = conf.GetPropertybyCode(Definitions.CONFIG_JS_REFRESH_STRING_RANDOM);
        %>
        <script src="../js/Language.js?t=<%=sNewRefreshJS%>"></script>
        <script src="../js/process_javajs.js?t=<%=sNewRefreshJS%>"></script>
        <script type="text/javascript" src="../js/jquery.js"></script>
        <link rel="stylesheet" href="../js/sweetalert.css"/>
        <script src="../js/sweetalert-dev.js"></script>
        <script type="text/javascript" src="../Css/GlobalAlert.js"></script>
        <link href="../Css/smartpaginator.css" rel="stylesheet" type="text/css"/>
        <script src="../Css/smartpaginator.js" type="text/javascript"></script>
        <style>
            input[type="file"] {
                position: absolute;
                left: 0;
                opacity: 0;
                top: 0;
                bottom: 0;
                width: 100%;
            }
            .cssFileImport {
                /*position: absolute;*/
                left: 0;
                top: 0;
                bottom: 0;
                width: 100%;
                height: 33px;
                color: blue;
                display: flex;
                align-items: center;
                justify-content: center;
                /*background: #5bc0de;*/color: blue;
                border-color: #1b6d85;
                border-radius: 3px;
                font-size: 14px; font-weight: 400;
                cursor: pointer;
            }
            .labelFile {
                position: relative;
                /*height: 50px;*/
                /*width: 130px;*/
                margin-top: 5px;
                padding-right: 10px;
            }
            .labelFile a {
                text-decoration: none;
            }
            .labelFile a:hover {
                text-decoration: none;
            }
            cssFileImport.dragover {
                background-color: #aaa;
            }
            .cssButtonRa{
                margin-bottom: 7px;
            }
        </style>
        <script>
            $(document).ready(function () {
                $("#BASE64_IMG").val('');
                $('.loading-gif').hide();
                $("#idLblTitleEdits").text(branch_title_edit);
                $("#idLblTitleCode").text(branch_fm_code);
//                $("#idLblNoteCode").text(global_fm_require_label);
                if('<%=isCALoad%>' === JS_IS_WHICH_ABOUT_CA_ICA){
                    $("#idLblTitleRemark").text(global_fm_remark_agency_vn);
                } else {
                    $("#idLblTitleRemark").text(global_fm_remark_vn);
                }
                $("#idLblNoteRemark").text(global_fm_require_label);
                if('<%=isCALoad%>' === JS_IS_WHICH_ABOUT_CA_ICA){
                    $("#idLblTitleRemark_EN").text(global_fm_remark_agency_en);
                } else {
                    $("#idLblTitleRemark_EN").text(global_fm_remark_en);
                }
                $("#idLblNoteRemark_EN").text(global_fm_require_label);
                
                $("#idLblTitleParent").text(branch_fm_parent);
                $("#idLblTitleCity").text(global_fm_city);
                
                $("#idLblTitleAddress").text(global_fm_address);
                $("#idLblNoteAddress").text(global_fm_require_label);
                
                $("#idLblTitleWorkPhone").text(global_fm_phone);
                $("#idLblNoteWorkPhone").text(global_fm_require_label);
                
                $("#idLblTitleEmail").text(global_fm_email);
                $("#idLblNoteEmail").text(global_fm_require_label);
                
                $("#idLblTitleRepresentative").text(branch_fm_representative);
                $("#idLblNoteRepresentative").text(global_fm_require_label);
                
                $("#idLblTitlePosition").text(branch_fm_representative_position);
                $("#idLblTitleTaxcode").text(global_fm_MST);
                
                $("#idLblTitleLogo").text(branch_fm_logo);
                $("#idLblTitleLogoChange").text(branch_fm_logo_change);
                $("#idLblTitleLogoDefault").text(branch_fm_logo_default);
                $("#idLblTitleLogoNote").text(branch_fm_logo_note);
                $("#idLblTitleBranchNote").text(branch_fm_logo_note);
                $("#idLblTitleLogoDown").text(branch_fm_logo_down);
                $("#idLblTitleDownHere").text(error_content_link_out);
                $("#idLblTitleCreateUser").text(global_fm_user_create);
                $("#idLblTitleCreateDate").text(global_fm_date_create);
                $("#idLblTitleUpdateUser").text(global_fm_user_endupdate);
                $("#idLblTitleUpdateDate").text(global_fm_date_endupdate);
            });
            function downloadSampleLogo()
            {
                var f = document.myname;
                f.method = "post";
                f.action = '../DownFromSaveFile?idParam=downfilesamplelogo';
                f.submit();
            }
            function UploadImageBase64(input1, idCSRF)
            {
                if (input1.value !== '')
                {
                    var checkFileName = input1.value.substring(input1.value.lastIndexOf('.') + 1);
                    if (checkFileName === "jpg" || checkFileName === "png" || checkFileName === "JPG"
                        || checkFileName === "PNG" || checkFileName === "gif" || checkFileName === "GIF")
                    {
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
                                    ValidateLogoForm(myStrings[1], idCSRF);
//                                    $("#BASE64_IMG").val(myStrings[1]);
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
            function ValidateLogoForm(Logo, idCSRF)
            {
                $('body').append('<div id="over"></div>');
                $(".loading-gif").show();
                $.ajax({
                    type: "post",
                    url: "../BranchCommon",
                    data: {
                        idParam: 'editlogobranch',
                        id: document.myname.ID.value,
                        Logo: Logo,
                        CsrfToken: idCSRF
                    },
                    cache: false,
                    success: function (html)
                    {
                        var myStrings = sSpace(html).split('#');
                        if (myStrings[0] === "0")
                        {
                            localStorage.setItem("EDIT_BRANCH", document.myname.ID.value);
                            funSuccLocalAlert(branch_succ_edit);
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
                            if (myStrings[1] === JS_STR_ERROR_CODE_99) {
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
            
            function setLogoDefault(idCSRF)
            {
                swal({
                    title: "",
                    text: branch_conform_default,
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
                            url: "../BranchCommon",
                            data: {
                                idParam: 'editlogobranch',
                                id: document.myname.ID.value,
                                Logo: '',
                                CsrfToken: idCSRF
                            },
                            cache: false,
                            success: function (html)
                            {
                                var myStrings = sSpace(html).split('#');
                                if (myStrings[0] === "0")
                                {
                                    localStorage.setItem("EDIT_BRANCH", document.myname.ID.value);
                                    funSuccLocalAlert(branch_succ_edit);
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
                                    if (myStrings[1] === JS_STR_ERROR_CODE_99) {
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
                    }, JS_STR_ACTION_TIMEOUT);
                });
            }
            function ValidateForm(idCSRF)
            {
                var varCALoad = '<%=isCALoad%>';
                if (!JSCheckEmptyField(document.myname.BranchCode.value))
                {
                    document.myname.BranchCode.focus();
                    funErrorAlert(branch_req_code);
                    return false;
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
                if(varCALoad !== JS_IS_WHICH_ABOUT_CA_ICA) {
                    if (!JSCheckEmptyField(document.myname.Address.value))
                    {
                        document.myname.Address.focus();
                        funErrorAlert(global_req_address);
                        return false;
                    }
                }
                if (!JSCheckEmptyField(document.myname.WorkPhone.value))
                {
                    if(varCALoad !== JS_IS_WHICH_ABOUT_CA_ICA) {
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
                    if(varCALoad !== JS_IS_WHICH_ABOUT_CA_ICA) {
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
                var isShareModeEnabled = "0";
                if ($("#shareModeEnabled").is(':checked'))
                {
                    isShareModeEnabled = "1";
                }
                var isIssueP12Enabled = "0";
                if ($("#issueP12Enabled").is(':checked')) {
                    isIssueP12Enabled = "1";
                }
                $('body').append('<div id="over"></div>');
                $(".loading-gif").show();
                $.ajax({
                    type: "post",
                    url: "../BranchCommon",
                    data: {
                        idParam: 'editbranch',
                        id: document.myname.ID.value,
                        BranchCode: document.myname.BranchCode.value,
                        issueP12Enabled: isIssueP12Enabled,
                        Remark: document.myname.Remark.value,
                        Remark_EN: document.myname.Remark_EN.value,
                        BranchParent: document.myname.BranchParent.value,
                        CityProvince: $("#CityProvince").val(),
                        DISCOUNT_RATE_PROFILE: $("#DISCOUNT_RATE_PROFILE").val(),
                        BRANCH_ROLE: $("#BRANCH_ROLE").val(),
                        BRANCH_STATE: $("#BRANCH_STATE").val(),
                        WorkPhone: document.myname.WorkPhone.value,
                        Address: document.myname.Address.value,
                        EMAIL: document.myname.EMAIL.value,
                        REPRESENTATIVE: document.myname.REPRESENTATIVE.value,
                        REPRESENTATIVE_POSITION: document.myname.REPRESENTATIVE_POSITION.value,
                        TAX_CODE: document.myname.TAX_CODE.value,
                        CALLBACK_URL_NOTICE: $("#CALLBACK_URL_NOTICE").val(),
                        pLIMIT_REVOKE1: $("#pLIMIT_REVOKE1").val(),
                        pLIMIT_REVOKE2: $("#pLIMIT_REVOKE2").val(),
                        pLIMIT_REVOKE3: $("#pLIMIT_REVOKE3").val(),
                        pRP_ACCESS_ESIGNCLOUD: $("#RP_ACCESSED_FOR_ESIGNCLOUD").val(),
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
                            localStorage.setItem("EDIT_BRANCH", document.myname.ID.value);
                            funSuccAlert(branch_succ_edit, "BranchList.jsp");
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
            function DeleteBranch(id, idCSRF)
            {
                swal({
                    title: "",
                    text: branch_conform_delete,
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
                            url: "../BranchCommon",
                            data: {
                                idParam: 'deletebranch',
                                id: id,
                                CsrfToken: idCSRF
                            },
                            cache: false,
                            success: function (html) {
                                var arr = sSpace(html).split('#');
                                if (arr[0] === "0")
                                {
                                    funSuccAlert(branch_succ_delete, "BranchList.jsp");
                                } else if (arr[0] === "1")
                                {
                                    funErrorAlert(branch_exists_user_delete);
                                }
                                else if (arr[0] === JS_EX_NO_DATA)
                                {
                                    funErrorAlert(user_error_no_data);
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
            function closeForm()
            {
                $.ajax({
                    type: "post",
                    url: "../SomeCommon",
                    data: {
                        idParam: 'backformpage',
                        idSession: 'SessRefreshBranch'
                    },
                    cache: false,
                    success: function (html) {
                        var arr = sSpace(html);
                        if (arr === "0")
                        {
                            window.location = "BranchList.jsp";
                        }
                        else
                        {
                            window.location = "BranchList.jsp";
                        }
                    }
                });
                return false;
            }
        </script>
        <style>
            fieldset.scheduler-border {
                border: 1px solid #E6E9ED !important;
                padding: 0 1.2em 5px 1.2em !important;
                margin: 0 0 1.1em 0 !important;
                -webkit-box-shadow:  0px 0px 0px 0px #E6E9ED;
                box-shadow:  0px 0px 0px 0px #E6E9ED;
            }
        </style>
    </head>
    <body>
        <%
            if ((session.getAttribute("sUserID")) != null) {
                session.setAttribute("SessProfileBranchAccess", null);
                String anticsrf = "";
                anticsrf = "" + Math.random();
                request.getSession().setAttribute("anticsrf", anticsrf);
        %>
        <div style="width: 100%; text-align: center; position: fixed;z-index: 1000;top: 0; padding-top: 300px;
             left: 0; height: 100%;" class="loading-gif">
            <img src="../Images/ajax-loader1.gif" alt="Please wait..." />
        </div>
        <div class="x_panel">
            <%                BRANCH[][] rs = new BRANCH[1][];
                try {
                    String sessAgentID = session.getAttribute("SessAgentID").toString().trim();
                    String sessLanguageGlobal = session.getAttribute("sessVN").toString();
                    String ids = EscapeUtils.CheckTextNull(request.getParameter("id"));
                    if (EscapeUtils.IsInteger(ids) == true) {
                        db.S_BO_BRANCH_DETAIL(ids, rs);
                        if (rs[0].length > 0) {
                            String strID = String.valueOf(rs[0][0].ID);
                            int sBRANCH_ROLE_ID = rs[0][0].BRANCH_ROLE_ID;
                            String strAddress = EscapeUtils.CheckTextNull(rs[0][0].ADDRESS);
                            String strWorkPhone = EscapeUtils.CheckTextNull(rs[0][0].MSISDN);
                            String strBranchCode = EscapeUtils.CheckTextNull(rs[0][0].NAME);
                            String strDateLimit = EscapeUtils.CheckTextNull(rs[0][0].CREATED_DT);
                            String pCERTIFICATION_PROFILE_PROPERTIES = EscapeUtils.CheckTextNull(rs[0][0].CERTIFICATION_PROFILE_PROPERTIES);
                            int sCityCode = rs[0][0].PROVINCE_ID;
                            int sIsBranch = rs[0][0].PARENT_ID;
                            int sIsBranchRoot = 1;
                            String imgLogo = EscapeUtils.CheckTextNull(rs[0][0].LOGO);
                            String sRegexPolicy = "";
                            String sSYS_DISCOUNT_RATE = "";
                            String sCallBackEnabled = "1";
                            String sCallBackApproveEnabled = "1";
//                            String sIssueP12Enabled = "";
                            boolean approveCAProfileEnabled = false;
                            GENERAL_POLICY[][] sessGeneralPolicy = (GENERAL_POLICY[][]) session.getAttribute("sessGeneralPolicy_System");
                            if (sessGeneralPolicy[0].length > 0) {
                                for(GENERAL_POLICY rsPolicy1: sessGeneralPolicy[0])
                                {
                                    if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_SYS_REGEX_FOR_PHONE_EMAIL)) {
                                        sRegexPolicy = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                    }
                                    if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_SYS_DISCOUNT_RATE_PROFILE_OPTION)) {
                                        sSYS_DISCOUNT_RATE = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                    }
                                    if (rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_SYS_AUTO_APPROVED_FOR_EACH_CERTIFICATION_PROFILE_OPTION)) {
                                        if("1".equals(EscapeUtils.CheckTextNull(rsPolicy1.VALUE))) {
                                            approveCAProfileEnabled = true;
                                        }
                                    }
                                    if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_FO_PUSH_NOTIFICATION_CALLBACK_URL_ENABLED)) {
                                        sCallBackEnabled = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                    }
                                    if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_CALLBACK_URL_APPROVED_ENABLED)) {
                                        sCallBackApproveEnabled = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
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
                            String sAgentAccessBranch = conf.GetPropertybyCode(Definitions.CONFIG_BRANCH_TREE_ENABLED);
                            boolean isAccessBranch = false;
                            boolean isEditBranch = false;
                            if("1".equals(sAgentAccessBranch)) {
                                isAccessBranch = true;
                                if(sessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                    isEditBranch = true;
                                }
                            } else {
                                if(sessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                    isAccessBranch = true;
                                    isEditBranch = true;
                                }
                            }
            %>
            <script>
            $(document).ready(function () {
                localStorage.setItem("sessLocal_REGEX_PHONE", '<%=sREGEX_PHONE%>');
                localStorage.setItem("sessLocal_REGEX_EMAIL", '<%=sREGEX_EMAIL%>');
            });
        </script>
            <div class="x_title">
                <h2><i class="fa fa-list-ul"></i> <label id="idLblTitleEdits"></label></h2>
                <ul class="nav navbar-right panel_toolbox">
                    <li style="padding-right: 10px;">
                        <%
                            if(isAccessBranch == true) {
                        %>
                        <input id="btnSave" type="button" data-switch-get="state" class="btn btn-info" onclick="ValidateForm('<%= anticsrf%>');"/>
                        <script>document.getElementById("btnSave").value = global_fm_button_edit;</script>
                        <input type="button" id="btnDelete" class="btn btn-info" onclick="DeleteBranch('<%= strID%>', '<%=anticsrf%>');"/>
                        <script>document.getElementById("btnDelete").value = global_fm_button_delete;</script>
                        <%
                            }
                        %>
                        <input id="btnClose" class="btn btn-info" type="button" onclick="closeForm();" />
                        <script>
                            document.getElementById("btnClose").value = global_fm_button_close;
                        </script>
                    </li>
                </ul>
                <div class="clearfix"></div>
            </div>
            <div class="x_content">
                <form name="myname" method="post" class="form-horizontal">
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label id="idLblTitleCode" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input value="<%= strID%>" class="form-control123" readonly style="display: none;" id="ID" name="ID">
                                <input value="<%= strBranchCode%>" class="form-control123" readonly maxlength="64" id="BranchCode" name="BranchCode">
                            </div>
                        </div>
                    </div>
                    <%
                        if(sIsBranch != sIsBranchRoot) {
                            String sessUserAgentID = session.getAttribute("SessUserAgentID").toString().trim();
                            if(!strID.equals(sessUserAgentID) && sessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                    %>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label id="idLblTitleParent" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <select name="BranchParent" id="BranchParent" class="form-control123">
                                    <%
                                        BRANCH[][] rsNoNull;
                                        try {
                                            if("1".equals(sAgentAccessBranch)) {
                                                rsNoNull = new BRANCH[1][];
                                                db.S_BO_GET_BRANCH_COMBOBOX_BY_BRANCH_ID(strID, sessLanguageGlobal, rsNoNull);
                                            } else {
                                                rsNoNull = new BRANCH[1][];
                                                db.S_BO_PARENT_BRANCH_COMBOBOX(sessLanguageGlobal, rsNoNull);
                                            }
                                            
//                                            BRANCH[][] rsNoNull = (BRANCH[][]) session.getAttribute("sessTreeBranchSystem");
                                            if (rsNoNull[0].length > 0) {
                                                for (int i = 0; i < rsNoNull[0].length; i++) {
                                                    if(rsNoNull[0][i].ID != 1 && rsNoNull[0][i].ID != Integer.parseInt(strID)) {
                                                        String sValueParent = String.valueOf(rsNoNull[0][i].ID);
                                                        String sBranchName = rsNoNull[0][i].NAME + " - " +rsNoNull[0][i].REMARK;
                                    %>
                                    <option value="<%=sValueParent%>" <%= sValueParent.equals(String.valueOf(sIsBranch)) ? "selected" : ""%>>
                                        <%=sBranchName%>
                                    </option>
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
                        </div>
                    </div>
                    <%
                            } else {
                                String sParentDesc = "";
                                BRANCH[][] rsParentBranch = new BRANCH[1][];
                                db.S_BO_BRANCH_DETAIL(String.valueOf(sIsBranch), rsParentBranch);
                                if(rsParentBranch[0].length > 0) {
                                    if("1".equals(sessLanguageGlobal)){
                                        sParentDesc = EscapeUtils.CheckTextNull(rsParentBranch[0][0].REMARK);
                                    } else {
                                        sParentDesc = EscapeUtils.CheckTextNull(rsParentBranch[0][0].REMARK_EN);
                                    }
                                }
                    %>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label id="idLblTitleParent" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input value="<%= sParentDesc%>" class="form-control123" readonly id="BranchParentDesc" name="BranchParentDesc">
                                <input type="hidden" value="<%= String.valueOf(sIsBranch)%>" id="BranchParent" name="BranchParent"/>
                            </div>
                        </div>
                    </div>
                    <%
                            }
                        }
                        else {
                            String sParentDesc = "";
                            BRANCH[][] rsParentBranch = new BRANCH[1][];
                            db.S_BO_BRANCH_DETAIL(String.valueOf(sIsBranch), rsParentBranch);
                            if(rsParentBranch[0].length > 0) {
                                if("1".equals(sessLanguageGlobal)) {
                                    sParentDesc = EscapeUtils.CheckTextNull(rsParentBranch[0][0].REMARK);
                                } else {
                                    sParentDesc = EscapeUtils.CheckTextNull(rsParentBranch[0][0].REMARK_EN);
                                }
                            }
                    %>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label id="idLblTitleParent" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input value="<%= sParentDesc%>" class="form-control123" readonly id="BranchParentDesc" name="BranchParentDesc">
                                <input type="hidden" value="<%= String.valueOf(sIsBranch)%>" id="BranchParent" name="BranchParent"/>
                            </div>
                        </div>
                    </div>
                    <%
                        }
                    %>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                <label id="idLblTitleBranchState"></label>
                            </label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <select name="BRANCH_STATE" id="BRANCH_STATE" class="form-control123">
                                    <%
                                        BRANCH_STATE[][] rsState = new BRANCH_STATE[1][];
                                        try {
                                            db.S_BO_BRANCH_STATE_COMBOBOX(sessLanguageGlobal, rsState);
                                            if (rsState[0].length > 0) {
                                                for (int i = 0; i < rsState[0].length; i++) {
                                    %>
                                    <option value="<%=String.valueOf(rsState[0][i].ID)%>" <%=rsState[0][i].ID == rs[0][0].BRANCH_STATE_ID ? "selected" : "" %>><%=rsState[0][i].REMARK%></option>
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
                            $("#idLblTitleBranchState").text(global_fm_Status);
                        </script>
                    </div>
                    <div class="col-sm-6" style="padding-left: 0;display: <%=sViewRose%>">
                        <div class="form-group">
                            <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                <label id="idLblTitleRose"></label>
                            </label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <select name="DISCOUNT_RATE_PROFILE" id="DISCOUNT_RATE_PROFILE" class="form-control123">
                                    <option id="idAllDISCOUNT_RATE" value="<%= Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL%>"></option>
                                    <%
                                        DISCOUNT_RATE_PROFILE[][] rsRose = new DISCOUNT_RATE_PROFILE[1][];
                                        try {
                                            db.S_BO_DISCOUNT_RATE_PROFILE_COMBOBOX(sessLanguageGlobal, rsRose);
                                            if (rsRose[0].length > 0) {
                                                for (int i = 0; i < rsRose[0].length; i++) {
                                                    String sValueParent = String.valueOf(rsRose[0][i].ID);
                                    %>
                                    <option value="<%=sValueParent%>" <%=rsRose[0][i].ID == rs[0][0].DISCOUNT_RATE_PROFILE_ID ? "selected" : "" %>><%=rsRose[0][i].REMARK%></option>
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
                            $("#idAllDISCOUNT_RATE").text(global_fm_combox_empty);
                        </script>
                    </div>
                    <div class="col-sm-6" style="padding-left: 0;display: <%= approveCAProfileEnabled ? "" : "none" %>">
                        <div class="form-group">
                            <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                <label id="idLblTitleProfileAccess"></label>
                            </label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <%
                                    String disableRoleOption = "";
                                    if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA)) {
                                        if(!sessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                            disableRoleOption = "disabled";
                                        }
                                    }
                                %>
                                <select name="BRANCH_ROLE" id="BRANCH_ROLE" <%=disableRoleOption%> class="form-control123" onchange="LoadProfileAll('0', '1');">
                                    <option id="idAllBRANCH_ROLE" value="<%= Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL%>"></option>
                                    <%
                                        BRANCH_ROLE[][] rsBranchRole = new BRANCH_ROLE[1][];
                                        try {
                                            db.S_BO_BRANCH_ROLE_COMBOBOX(sessLanguageGlobal, rsBranchRole);
                                            if (rsBranchRole[0].length > 0) {
                                                for (int i = 0; i < rsBranchRole[0].length; i++) {
                                    %>
                                    <option value="<%=String.valueOf(rsBranchRole[0][i].ID)%>" <%=rsBranchRole[0][i].ID == sBRANCH_ROLE_ID ? "selected" : "" %>><%=rsBranchRole[0][i].REMARK%></option>
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
                            $("#idLblTitleProfileAccess").text(profileaccss_fm_rose);
                            $("#idAllBRANCH_ROLE").text(global_fm_combox_empty);
                        </script>
                    </div>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                <label id="idLblTitleRemark"></label>
                                <label class="CssRequireField" id="idLblNoteRemark"></label>
                            </label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input class="form-control123" value="<%= rs[0][0].REMARK%>" maxlength="256" id="Remark" name="Remark"/>
                            </div>
                        </div>
                    </div>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                <label id="idLblTitleRemark_EN"></label>
                                <label class="CssRequireField" id="idLblNoteRemark_EN"></label>
                            </label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input class="form-control123" id="Remark_EN" name="Remark_EN" value="<%= rs[0][0].REMARK_EN %>" maxlength="256"/>
                            </div>
                        </div>
                    </div>
                    
                    <script>
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
                            var CityProvincePGD = obj.value.split("###")[1];
                            var cbxCity = document.getElementById('CityProvince');
                            ValueExistsOptions(cbxCity, CityProvincePGD);
                        }
                    </script>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label id="idLblTitleCity" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <select name="CityProvince" id="CityProvince" class="form-control123" onchange="changeCity(this);">
                                    <%
                                        CITY_PROVINCE[][] rsCity = new CITY_PROVINCE[1][];
                                        try {
                                            db.S_BO_PROVINCE_COMBOBOX(sessLanguageGlobal, rsCity);
                                            if (rsCity[0].length > 0) {
                                                for (int i = 0; i < rsCity[0].length; i++) {
                                    %>
                                    <option value="<%=String.valueOf(rsCity[0][i].ID)%>" <%= rsCity[0][i].ID == sCityCode
                                        ? "selected" : ""%>><%=rsCity[0][i].REMARK %></option>
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
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                <label id="idLblTitleAddress"></label>
                                <label class="CssRequireField" id="idLblNoteAddress" style="display: <%= isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA) ? "none" : ""%>"></label>
                            </label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input class="form-control123" name="Address" maxlength="256" value="<%= strAddress%>">
                            </div>
                        </div>
                    </div>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                <label id="idLblTitleWorkPhone"></label>
                                <label class="CssRequireField" id="idLblNoteWorkPhone" style="display: <%= isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA) ? "none" : ""%>"></label>
                            </label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input class="form-control123" id="WorkPhone" name="WorkPhone" maxlength="16"
                                    onblur="autoTrimTextField('WorkPhone', this.value);" value="<%= strWorkPhone%>">
                            </div>
                        </div>
                    </div>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                <label id="idLblTitleEmail"></label>
                                <label class="CssRequireField" id="idLblNoteEmail" style="display: <%= isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA) ? "none" : ""%>"></label>
                            </label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input class="form-control123" maxlength="128" id="EMAIL" name="EMAIL" value="<%= EscapeUtils.CheckTextNull(rs[0][0].EMAIL)%>">
                            </div>
                        </div>
                    </div>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                <label id="idLblTitleRepresentative"></label>
                                <label class="CssRequireField" id="idLblNoteRepresentative" style="display: <%= isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA) ? "none" : ""%>"></label>
                            </label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input class="form-control123" maxlength="64" id="REPRESENTATIVE" name="REPRESENTATIVE" value="<%= EscapeUtils.CheckTextNull(rs[0][0].REPRESENTATIVE)%>">
                            </div>
                        </div>
                    </div>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label id="idLblTitlePosition" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input class="form-control123" maxlength="64" id="REPRESENTATIVE_POSITION" name="REPRESENTATIVE_POSITION" value="<%= EscapeUtils.CheckTextNull(rs[0][0].REPRESENTATIVE_POSITION)%>">
                            </div>
                        </div>
                    </div>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label id="idLblTitleTaxcode" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input class="form-control123" maxlength="32" id="TAX_CODE" name="TAX_CODE" value="<%= EscapeUtils.CheckTextNull(rs[0][0].TAX_CODE)%>">
                            </div>
                        </div>
                    </div>
                    <%
                        String isCheckRevoke = conf.GetTryPropertybyCode(Definitions.CONFIG_FORBIDEN_REVOKE_CONTINUOU_DOUBLE_ENABLED);
                        String sRevoke = rs[0][0].LIMIT_REVOKE;
                        String sRevoke1 = "";
                        String sRevoke2 = "";
                        String sRevoke3 = "";
                        if("1".equals(isCheckRevoke)){
                            if(!"".equals(sRevoke)){
                                String[] sRevokeArray = sRevoke.split(";");
                                if(sRevokeArray.length > 0){
                                    sRevoke1 = sRevokeArray[0].toString();
                                    if(sRevokeArray.length > 1){
                                        sRevoke2 = sRevokeArray[1].toString();
                                    }
                                    if(sRevokeArray.length > 2){
                                        sRevoke3 = sRevokeArray[2].toString();
                                    }
                                }
                            }
                        }
                    %>
                    <div class="col-sm-6" style="padding-left: 0; display: <%= "1".equals(isCheckRevoke) ? "" : "none" %>">
                        <div class="form-group">
                            <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                <label id="idLblTitleLimitRevoke"></label>
                            </label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input class="form-control123" style="width: 55px;" maxlength="6" id="pLIMIT_REVOKE1" name="pLIMIT_REVOKE1" value="<%= sRevoke1%>"> ;
                                <input class="form-control123" style="width: 55px;" maxlength="6" id="pLIMIT_REVOKE2" name="pLIMIT_REVOKE2" value="<%= sRevoke2%>"> ;
                                <input class="form-control123" style="width: 55px;" maxlength="6" id="pLIMIT_REVOKE3" name="pLIMIT_REVOKE3" value="<%= sRevoke3%>">
                            </div>
                        </div>
                        <script>
                            $("#idLblTitleLimitRevoke").text(global_fm_limit_revoke);
                        </script>
                    </div>
                    <%
                        String sRSSP_ACCESS_ENABLED = conf.GetPropertybyCode(Definitions.CONFIG_RSSP_ACCESS_ENABLED);
                    %>
                    <div class="col-sm-6" style="padding-left: 0; <%=sessAgentID.equals(Definitions.CONFIG_AGENT_ROOT) && "1".equals(sRSSP_ACCESS_ENABLED) ? "" : "display:none;" %>">
                        <div class="form-group">
                            <label id="idLblTitleRPAccess" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input class="form-control123" maxlength="32" id="RP_ACCESSED_FOR_ESIGNCLOUD" name="RP_ACCESSED_FOR_ESIGNCLOUD" value="<%= EscapeUtils.CheckTextNull(rs[0][0].RP_ACCESSED_FOR_ESIGNCLOUD)%>">
                            </div>
                        </div>
                        <script>
                            $("#idLblTitleRPAccess").text(global_fm_RP_access_esign);
                        </script>
                    </div>
                    <%
                        String certAccessEnabled = conf.GetPropertybyCode(Definitions.CONFIG_CERT_MODULES_ACCESS_ENABLED);
                        String sJSON_PROFILE = EscapeUtils.CheckTextNull(rs[0][0].CERTIFICATION_POLICY_PROPERTIES);
                        CERTIFICATION_POLICY_DATA[][] resProfileData = new CERTIFICATION_POLICY_DATA[1][];
                        if(!"".equals(sJSON_PROFILE))
                        {
                            CommonFunction.getAllProfilePolicyForBranch(sJSON_PROFILE, resProfileData);
                            if(resProfileData[0].length > 0)
                            {
                                request.getSession(false).setAttribute("SessProfilePolicyAll", resProfileData);
                            }
                        }
                        CERTIFICATION_POLICY_DATA[][] resFormFactorData;
                        boolean booShareCertEnabled = false;
                        if("1".equals(certAccessEnabled)) {
                            resFormFactorData = (CERTIFICATION_POLICY_DATA[][]) session.getAttribute("SessProfilePolicyAll");
                            if(resFormFactorData != null) {
                                if(resFormFactorData[0].length > 0)
                                {
                                    for(CERTIFICATION_POLICY_DATA resProfileData1 : resFormFactorData[0])
                                    {
                                        if(resProfileData1.attributeType.equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_CERTIFICATION_SHARE_MODE))
                                        {
                                            if(resProfileData1.name.equals("true"))
                                            {
                                                booShareCertEnabled = true;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    %>
                    <div class="col-sm-6" style="padding-left: 0; display: <%= "1".equals(certAccessEnabled) ? "" : "none" %>">
                        <div class="form-group">
                            <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                <label id="idLblTitleShareCert"></label>
                            </label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <label class="switch" for="shareModeEnabled" style="margin-bottom: 0;">
                                    <input type="checkbox" name="shareModeEnabled" id="shareModeEnabled" <%= booShareCertEnabled == true ? "checked" : ""%>/>
                                    <div id="shareModeEnabledClass" class="slider round"></div>
                                </label>
                            </div>
                        </div>
                        <script>
                            $("#idLblTitleShareCert").text(global_fm_share_mode_cert);
                        </script>
                    </div>
                    <div class="col-sm-6" style="padding-left: 0;<%= sessAgentID.equals(Definitions.CONFIG_AGENT_ROOT) ? "" : "display:none" %>">
                        <div class="form-group">
                            <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                <label id="idLblTitleIssueP12Enabled"></label>
                            </label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <label class="switch" for="issueP12Enabled" style="margin-bottom: 0;">
                                    <input type="checkbox" name="issueP12Enabled" id="issueP12Enabled" <%= "1".equals(rs[0][0].ISSUE_P12_ENABLED) ? "checked" : ""%>/>
                                    <div id="issueP12EnabledClass" class="slider round"></div>
                                </label>
                            </div>
                        </div>
                        <script>
                            $("#idLblTitleIssueP12Enabled").text(global_fm_issue_p12_enabled);
                        </script>
                    </div>
                    <div class="col-sm-6" style="padding-left: 0;display: <%= "1".equals(sCallBackApproveEnabled) ? "" : "none" %>">
                        <div class="form-group">
                            <label id="idLblTitleCallbackWhenApprove" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input class="form-control123" maxlength="256" id="CALLBACK_WHEN_APPROVE" name="CALLBACK_WHEN_APPROVE" value="<%= rs[0][0].CALLBACK_URL_APPROVED%>">
                            </div>
                        </div>
                        <script>
                            $("#idLblTitleCallbackWhenApprove").text(global_fm_callback_when_approve);
                        </script>
                    </div>
                    <div class="col-sm-6" style="padding-left: 0;display: <%= "1".equals(sCallBackEnabled) ? "" : "none" %>">
                        <div class="form-group">
                            <label id="idLblTitleCallbackUrl" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input class="form-control123" maxlength="256" id="CALLBACK_URL_NOTICE" name="CALLBACK_URL_NOTICE" value="<%= rs[0][0].CALLBACK_URL_NOTICE%>">
                            </div>
                        </div>
                        <script>
                            $("#idLblTitleCallbackUrl").text(global_fm_callback_url);
                        </script>
                    </div>
                                    
                    <fieldset class="scheduler-border" style="clear: both;">
                        <legend class="scheduler-border" id="idLblProfileGroupFactor"></legend>
                        <script>$("#idLblProfileGroupFactor").text(branch_fm_profile_group_formfactor);</script>
                        <div class="col-sm-6" style="padding-left: 0;">
                            <div class="form-group">
                                <label id="idLblTitlePKIFormfactor" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                <div class="col-sm-7" style="padding-right: 0px;">
                                    <select id="PKI_FORMFACTOR" name="PKI_FORMFACTOR" class="form-control123">
                                        <%
                                            PKI_FORMFACTOR[][] rsPKIFormFactor = new PKI_FORMFACTOR[1][];
                                            String sFristCerPurpose = "0";
                                            db.S_BO_CA_GET_PKI_FORMFACTOR_COMBOBOX_FOR_CERTIFICATION_PURPOSE(Integer.parseInt(sFristCerPurpose),
                                                sessLanguageGlobal, rsPKIFormFactor);
                                            if (rsPKIFormFactor.length > 0) {
                                                for (int i = 0; i < rsPKIFormFactor[0].length; i++) {
                                        %>
                                        <option value="<%= String.valueOf(rsPKIFormFactor[0][i].ID)%>"><%= rsPKIFormFactor[0][i].REMARK%></option>
                                        <%
                                                }
                                            }
                                        %>
                                    </select>
                                </div>
                            </div>
                            <script>$("#idLblTitlePKIFormfactor").text(global_fm_Method);</script>
                        </div>
                        <div class="col-sm-6" style="padding-left: 0;">
                            <div class="form-group">
                                <div class="col-sm-5" style="padding-right: 0px;">
                                    
                                </div>
                                <div class="col-sm-7" style="padding-right: 0px;text-align: left;">
                                    <input id="btnFactorAdd" class="btn btn-info" type="button" onclick="onFormFactorAdd('<%=anticsrf%>');" />
                                    <script>
                                        document.getElementById("btnFactorAdd").value = global_fm_button_New;
                                    </script>
                                </div>
                            </div>
                            <script>
                                function onFormFactorAdd(idCSRF)
                                {
                                    $('body').append('<div id="over"></div>');
                                    $(".loading-gif").show();
                                    $.ajax({
                                        type: "post",
                                        url: "../BranchCommon",
                                        data: {
                                            idParam: 'addfactorpolicybranch',
                                            sID: $("#ID").val(),
                                            sFactorCert: $("#PKI_FORMFACTOR").val(),
                                            CsrfToken: idCSRF
                                        },
                                        cache: false,
                                        success: function (html)
                                        {
                                            var myStrings = sSpace(html).split('#');
                                            if (myStrings[0] === "0")
                                            {
                                                onLoadFactor();
                                                funSuccNoLoad(inputcertlist_succ_add);
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
                                                if (myStrings[1] === JS_STR_ERROR_CODE_99) {
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
                                function onLoadFactor()
                                {
                                    $.ajax({
                                        type: "post",
                                        url: "../JSONCommon",
                                        data: {
                                            idParam: 'loadfactorpolicybranch'
                                        },
                                        cache: false,
                                        success: function (html)
                                        {
                                            if (html.length > 0)
                                            {
                                                var obj = JSON.parse(html);
                                                if (obj[0].Code === "0")
                                                {
                                                    $("#idTemplateAssignFactor").empty();
                                                    var contentProps = "";
                                                    var sCount = 1;
                                                    for (var i = 0; i < obj.length; i++) {
                                                        var idCheckBox = obj[i].NAME + '' + obj[i].NO;
                                                        var isChecked = "";
                                                        if(obj[i].ACTIVE === "1")
                                                        {
                                                            isChecked = "checked";
                                                        }
                                                        var sActiveHTML = "<label class='switch' for='"+idCheckBox+"'><input TYPE='checkbox' class='js-switch' data-switchery='true' "+isChecked+" id='"+idCheckBox+"' onchange=\"onFactorActive('"+obj[i].name+"', '"+idCheckBox+"');\" /><div class='slider round'></div></label>";
                                                        contentProps += "<tr>" +
                                                            "<td>" + obj[i].NO + "</td>" +
                                                            "<td>" + obj[i].NAME + "</td>" +
                                                            "<td>" + obj[i].REMARK + "</td>" +
                                                            "<td>" + sActiveHTML + "</td>" +
                                                            "</tr>";
                                                        sCount++;
                                                    }
                                                    $("#idTemplateAssignFactor").append(contentProps);
                                                    if (sCount > 0)
                                                    {
                                                        $('#greenFactor').smartpaginator({totalrecords: sCount, recordsperpage: 10, datacontainer: 'tblCertUseFactor', dataelement: 'tr', initval: 0, next: global_paging_last, prev: global_paging_Before, first: global_paging_first, last: global_paging_next, theme: 'green'});
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
                                                else {
                                                    funErrorAlert(global_errorsql);
                                                }
                                            }
                                        }
                                    });
                                    return false;
                                }
                            </script>
                        </div>&nbsp;
                        <%
                            resFormFactorData = (CERTIFICATION_POLICY_DATA[][]) session.getAttribute("SessProfilePolicyAll");
                            if(resFormFactorData[0].length > 0) {
                            int j=1;
                        %>
                        <div class="table-responsive">
                            <table id="tblCertUseFactor" class="table table-bordered table-striped projects">
                                <thead>
                                <th id="idLblTitleTableFactorSTT"></th>
                                <th id="idLblTitleTableFactorName"></th>
                                <th id="idLblTitleTableFactorRemark"></th>
                                <th id="idLblTitleTableFactorActive"></th>
                                <script>
                                    $("#idLblTitleTableFactorSTT").text(global_fm_STT);
                                    $("#idLblTitleTableFactorName").text(global_fm_Method);
                                    $("#idLblTitleTableFactorRemark").text(global_fm_Description);
                                    $("#idLblTitleTableFactorActive").text(global_fm_active);
                                </script>
                                </thead>
                                <tbody id="idTemplateAssignFactor">
                                    <%
                                        for(CERTIFICATION_POLICY_DATA resProfileData1 : resFormFactorData[0]) {
                                            if(resProfileData1.attributeType.equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_ITEM_PKI_FORMFACTOR_LIST))
                                            {
                                    %>
                                    <tr>
                                        <td><%= j%></td>
                                        <td><%= resProfileData1.name%></td>
                                        <td><%= "1".equals(sessLanguageGlobal) ? resProfileData1.remark : resProfileData1.remarkEn%></td>
                                        <td>
                                            <label class="switch" for="idCheckFactor<%=j%>" style="margin-bottom: 0;">
                                                <input onclick="onFactorActive('<%=resProfileData1.name%>', 'idCheckFactor<%=j%>');" type="checkbox" name="idCheckFactor<%=j%>" id="idCheckFactor<%=j%>" <%= resProfileData1.enabled == true ? "checked" : ""%>/>
                                                <div id="idCheckFactorClass<%=j%>" class="slider round"></div>
                                            </label>
                                        </td>
                                    </tr>
                                    <%
                                                j++;
                                            }
                                        }
                                    %>
                                </tbody>
                            </table>
                            <div id="greenFactor" style="margin: 5px 0 5px 0;"> </div>
                            <script>
                                if (parseInt('<%=j%>')-1 > 0)
                                {
                                    $('#greenFactor').smartpaginator({totalrecords: parseInt('<%=j%>')-1, recordsperpage: 10, datacontainer: 'tblCertUseFactor', dataelement: 'tr', initval: 0, next: global_paging_last, prev: global_paging_Before, first: global_paging_first, last: global_paging_next, theme: 'green'});
                                }
                                function onFactorActive(factorCode, activeKey)
                                {
                                    $('body').append('<div id="over"></div>');
                                        $(".loading-gif").show();
                                    var isFactorActive = "0";
                                    if ($("#"+activeKey).is(':checked'))
                                    {
                                        isFactorActive = "1";
                                    }
                                    $.ajax({
                                        type: "post",
                                        url: "../BranchCommon",
                                        data: {
                                            idParam: 'activefactorpolicybranch',
                                            sID: $("#ID").val(),
                                            sFactorActive: isFactorActive,
                                            sFactorCode: factorCode
                                        },
                                        cache: false,
                                        success: function (html)
                                        {
                                            var myStrings = sSpace(html).split('#');
                                            if (myStrings[0] === "0")
                                            {
                                                funSuccNoLoad(inputcertlist_succ_edit);
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
                                                if (myStrings[1] === JS_STR_ERROR_CODE_99) {
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
                            </script>
                        </div>
                        <%
                            }
                        %>
                    </fieldset>
                                
                    <fieldset class="scheduler-border" style="clear: both;">
                        <legend class="scheduler-border" id="idLblTitleLogo"></legend>
                        <div class="form-group" style="padding: 0px 0px 0 0px;margin: 0;">
                            <%
                                if(!"".equals(imgLogo)) {
                            %>
                            <div class="form-group">
                                <img src="data:image/jpg;base64, <%=imgLogo%>" width="210px" height="70px" />
                            </div>
                            <%
                                }
                            %>
                            <input type="hidden" id="BASE64_IMG_NOCHANGE" name="BASE64_IMG_NOCHANGE" value="<%=imgLogo%>">
                            <input type="hidden" id="BASE64_IMG" name="BASE64_IMG" value="">
                            <input type="hidden" id="valueChangeLogo" name="valueChangeLogo" value="0" >
                            <div>
                                <label class="labelFile" for="input-file" style="cursor: pointer;">
                                    <div class="cssFileImport"><a id="idLblTitleLogoChange" style="cursor: pointer;"></a></div>
                                    <input type="file" id="input-file" <%= isAccessBranch==false ? "disabled" : "" %> onchange="UploadImageBase64(this, '<%=anticsrf%>');">
                                </label>
                                <%
                                    if(!"".equals(imgLogo) && isAccessBranch==true) {
                                %>
                                <label class="labelFile" style="font-weight: 200;font-size: 14px;cursor: pointer;">
                                    <a onclick="setLogoDefault('<%=anticsrf%>');" id="idLblTitleLogoDefault"></a>
                                </label>
                                <%
                                    }
                                %>
                            </div>
                            <div style="width: 100%; height: 0px;"></div>
                            <label class="control-label123" style="color: red; font-weight: 300;" id="idLblTitleLogoNote"></label>
                            <div class="form-group" style="display: none;" id="idViewChangeLogo">
                                <INPUT id="uploadImg" class="form-control123" NAME="uploadImg" TYPE="file" <%= isAccessBranch==false ? "disabled" : "" %>
                                     accept=".png,.jpg,.gif" onchange="UploadImageBase64(this);" />
                                <div style="width: 100%; height: 6px;"></div>
                                <label class="control-label123" style="color: red; font-weight: 300;color: blue;cursor: pointer;" id="idLblTitleBranchNote"></label>
                                <br/>
                                <label class="control-label123" id="idLblTitleLogoDown"></label>
                                <a style="cursor: pointer;" <%= isAccessBranch==false ? "disabled" : "" %> onclick="return downloadSampleLogo();" id="idLblTitleDownHere">
                                </a>
                            </div>
                            <script>
                                function OnChangeLogo()
                                {
                                    var sTempChangeLogo = $("#ChangeLogo").bootstrapSwitch("state");
                                    if(sTempChangeLogo === true)
                                    {
                                        $("#valueChangeLogo").val("1");
                                        $("#idViewChangeLogo").css("display", "");
                                    } else {
                                        $("#uploadImg").val("");
                                        $("#BASE64_IMG").val("");
                                        $("#valueChangeLogo").val("0");
                                        $("#idViewChangeLogo").css("display", "none");
                                    }
                                }
                            </script>
                        </div>
                    </fieldset>
                    <%
                            if(!"".equals(pCERTIFICATION_PROFILE_PROPERTIES))
                            {
                                CERTIFICATION_POLICY_DATA[][] resIPData = new CERTIFICATION_POLICY_DATA[1][];
                                CommonFunction.getProfileCertNewListForAdmin(pCERTIFICATION_PROFILE_PROPERTIES, resIPData);
                                if(resIPData[0].length > 0)
                                {
                                    request.getSession(false).setAttribute("SessProfileBranchAccess", resIPData);
                                }
                            }
                    %>
                    <%
                        CERTIFICATION_POLICY_DATA[][] resIPData = (CERTIFICATION_POLICY_DATA[][]) session.getAttribute("SessProfileBranchAccess");
                        int m=1;
                        String strMessProfile=" 0";
                        String strMessService=" 0";
                        String strMessMajor=" 0";
                        if(resIPData != null && resIPData[0].length > 0) {
                            int sSumProfile = 0;
                            int sSumService = 0;
                            int sSumMajor = 0;
                            for(CERTIFICATION_POLICY_DATA resIPData1 : resIPData[0]) {
                                if(resIPData1.attributeType.equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_ITEM_PROFILE_LIST)) {
                                    sSumProfile = sSumProfile+ 1;
                                }
                                if(resIPData1.attributeType.equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_ITEM_SERVICE_TYPE)) {
                                    sSumService = sSumService+ 1;
                                }
                                if(resIPData1.attributeType.equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_ITEM_MAJOR_TYPE)) {
                                    sSumMajor = sSumMajor+ 1;
                                }
                            }
                            strMessProfile = " " + String.valueOf(sSumProfile);
                            strMessService = " " + String.valueOf(sSumService);
                            strMessMajor = " " + String.valueOf(sSumMajor);
                        }
                    %>
                    
                    <div class="form-group" style="text-align: left; padding: 0px 0px 10px 0px;margin: 0;display: <%= approveCAProfileEnabled ? "" : "none" %>">
                        <div class="col-sm-7" style="padding-left: 0;">
                            <div class="form-group">
                                <label id="idLblTitleLoadProfile" class="control-label" style="color: #000000; font-weight: bold;text-align: left;"></label>
                                <a style="cursor: pointer;color: blue; font-weight: bold; text-decoration: underline;" onclick="LoadProfileAll('0', '1');" id="idLoadProfile"></a>
                                <br/>
                                <div class="col-sm-8" style="padding: 5px 0 0 0;text-align: left;">
                                    <label class="control-labelcheckbox" style="padding-right: 15%;" id="idTitleCheckAdditional"></label>
                                </div>
                                <div class="col-sm-3" style="padding: 0;text-align: left;">
                                    <label class="switch" for="idCheckAdditional" style="margin-bottom: 0;padding-top: 5px;">
                                        <input type="checkbox" name="idCheckAdditional" id="idCheckAdditional"/>
                                        <div id="idCheckAdditionalClass" class="slider round"></div>
                                    </label>
                                </div>
                                <script>
                                    $("#idLoadProfile").text(error_content_link_out);
                                    $("#idLblTitleLoadProfile").text(global_fm_button_reload_of_profileaccess);
                                    $("#idTitleCheckAdditional").text(branch_fm_check_reload_cert);
                                </script>
                            </div>
                        </div>
                        <div class="col-sm-5" style="padding-left: 0;text-align: left;">
                        </div>
                    </div>
                    <div class="clearfix"></div>
                    <div class="" role="tabpanel" data-example-id="togglable-tabs" style="display: <%= approveCAProfileEnabled ? "" : "none" %>">
                        <ul id="myTabTypeKey" class="nav nav-tabs bar_tabs" role="tablist">
                            <li role="presentation" class="active" id="idLi_contentAPI">
                                <a href="#tab_contentAPI" role="tab" id="profile-tab2" data-toggle="tab" aria-expanded="true">
                                    <span id="idTagCredentialAPI"></span>
                                </a>
                            </li>
                            <%
                                if(sessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                            %>
                            <li role="presentation" class="" id="idLi_contentIP" id="idTagIP">
                                <a href="#tab_contentIP" id="home-tab" role="tab" data-toggle="tab" aria-expanded="false">
                                    <span id="idTagIP"></span>
                                </a>
                            </li>
                            <li role="presentation" class="" id="idLi_contentFunction" id="idTagFunction">
                                <a href="#tab_contentFunction" id="home-tab" role="tab" data-toggle="tab" aria-expanded="false">
                                    <span id="idTagFunction"></span>
                                </a>
                            </li>
                            <%
                                }
                            %>
                        </ul>
                        <script>
                            $("#idTagCredentialAPI").text(profileaccss_fm_rose);
                            $("#idTagIP").text(profileaccss_fm_service_type);
                            $("#idTagFunction").text(profileaccss_fm_major_cert);
                        </script>
                        <div id="myTabContentTypeKey" class="tab-content">
                            <div role="tabpanel" class="tab-pane fade active in" id="tab_contentAPI" aria-labelledby="home-tab1">
                                <div class="x_title" style="border-bottom: 0 solid #E6E9ED;margin-bottom: 0px;">
                                    <ul class="nav navbar-right panel_toolbox">
                                        <li style="color: red;font-weight: bold;">
                                            <label id="idViewSumProfile"></label><%= strMessProfile%>
                                            <script>$("#idViewSumProfile").text(global_label_grid_sum);</script>
                                        </li>
                                    </ul>
                                    <div class="clearfix"></div>
                                </div>
                                <div class="x_content">
                                    <style type="text/css">
                                        .table > thead > tr > th, .table > tbody > tr > td{vertical-align: middle;}
                                        .table > thead > tr > th{border-bottom: none;}
                                        .btn{margin-bottom: 0px;}
                                        .panel_toolbox { min-width: 0;}
                                    </style>
                                    <div class="table-responsive">
                                        <table id="tblCertUseIP" class="table table-bordered table-striped projects">
                                            <thead>
                                            <th id="idLblTitleTableIPSTT"></th>
                                            <th id="idLblTitleTableIPName"></th>
                                            <th id="idLblTitleTableIPProfileRemark"></th>
                                            <th id="idLblTitleTableIPPercent"></th>
                                            <th id="idLblTitleTableIPActive"></th>
                                            <script>
                                                $("#idLblTitleTableIPSTT").text(global_fm_STT);
                                                $("#idLblTitleTableIPName").text(certprofile_fm_code);
                                                $("#idLblTitleTableIPProfileRemark").text(global_fm_Description);
                                                $("#idLblTitleTableIPPercent").text(global_fm_active);
                                                $("#idLblTitleTableIPActive").text(global_fm_approve_ca);
                                            </script>
                                            </thead>
                                            <tbody id="idTemplateAssignIP">
                                                <%
                                                    if(resIPData != null && resIPData[0].length > 0) {
                                                        for(CERTIFICATION_POLICY_DATA resIPData1 : resIPData[0]) {
                                                            if(resIPData1.attributeType.equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_ITEM_PROFILE_LIST))
                                                            {
                                                %>
                                                <tr>
                                                    <td><%= m%></td>
                                                    <td><%= resIPData1.name %></td>
                                                    <td><%= "1".equals(sessLanguageGlobal) ? resIPData1.remark : resIPData1.remarkEn %></td>
                                                    <td>
                                                        <label class="switch" for="idCheckProfile<%=m%>" style="margin-bottom: 0;">
                                                            <input onclick="onProfileActive('<%=resIPData1.name%>', 'idCheckProfile<%=m%>');" type="checkbox"
                                                                name="idCheckProfile<%=m%>" id="idCheckProfile<%=m%>" <%= resIPData1.enabled == true ? "checked" : ""%>/>
                                                            <div id="idCheckProfileClass<%=m%>" class="slider round"></div>
                                                        </label>
                                                    </td>
                                                    <td>
                                                        <label class="switch" for="idCheckApproveCA<%=m%>" style="margin-bottom: 0;">
                                                            <input onclick="onApproveCAActive('<%=resIPData1.name%>', 'idCheckApproveCA<%=m%>');" type="checkbox"
                                                                name="idCheckApproveCA<%=m%>" id="idCheckApproveCA<%=m%>" <%= resIPData1.approveCAEnabled == true ? "checked" : ""%>/>
                                                            <div id="idCheckApproveCAClass<%=m%>" class="slider round"></div>
                                                        </label>
                                                    </td>
                                                </tr>
                                                <%
                                                            m++;
                                                            }
                                                        }
                                                    } else {
                                                %>
                                                <tr><td colspan="5" id="idLblTitleTableNoFile"></td></tr>
                                                <script>
                                                    $("#idLblTitleTableNoFile").text(global_no_data);
                                                </script>
                                                <%
                                                    }
                                                %>
                                            </tbody>
                                        </table>
                                        <script>
                                            function LoadProfileAll(sAllEnabled, hasBranchRole)
                                            {
                                                if(hasBranchRole !== '0')
                                                {
                                                    hasBranchRole = $('#BRANCH_ROLE').val();
                                                }
                                                var idBranch = '<%= ids%>';
                                                var idCheckAdditionalEnabled = "0";
                                                if ($("#idCheckAdditional").is(':checked'))
                                                {
                                                    idCheckAdditionalEnabled = "1";
                                                }
                                                $('body').append('<div id="over"></div>');
                                                $(".loading-gif").show();
                                                setTimeout(function () {
                                                    $.ajax({
                                                        type: "post",
                                                        url: "../JSONCommon",
                                                        data: {
                                                            idParam: 'loadprofileallproperties',
                                                            sAllEnabled: sAllEnabled,
                                                            idBranch: idBranch,
                                                            idCheckAdditionalEnabled: idCheckAdditionalEnabled,
                                                            hasBranchRole: hasBranchRole
                                                        },
                                                        cache: false,
                                                        success: function (html)
                                                        {
                                                            if (html.length > 0)
                                                            {
                                                                var obj = JSON.parse(html);
                                                                if (obj[0].Code === "0")
                                                                {
                                                                    $("#idTemplateAssignIP").empty();
                                                                    $("#idTemplateAssignRequest").empty();
                                                                    $("#idTemplateAssignMajor").empty();
                                                                    var contentProps = "";
                                                                    var contentRequest = "";
                                                                    var contentMajor = "";
                                                                    var sCount = 1;
                                                                    var sCountRequest = 1;
                                                                    var sCountMajor = 1;
                                                                    for (var i = 0; i < obj.length; i++) {
                                                                        if(obj[i].ATTRIBUTE_TYPE === JS_STR_JSON_PROFILE_LIST_ITEM)
                                                                        {
                                                                            var idCheckBoxProfile = 'idCheckProfile' + obj[i].NO;
                                                                            var idCheckBoxApprove = 'idCheckApproveCA' + obj[i].NO;
                                                                            var isCheckedProfile = "";
                                                                            var isCheckedApprove = "";
                                                                            if(obj[i].ACTIVE === "1")
                                                                            {
                                                                                isCheckedProfile = "checked";
                                                                            }
                                                                            if(obj[i].APPROVE_CA === "1")
                                                                            {
                                                                                isCheckedApprove = "checked";
                                                                            }
                                                                            var sActiveProfile = "<label class='switch' for='"+idCheckBoxProfile+"'><input TYPE='checkbox' class='js-switch' data-switchery='true' "+isCheckedProfile+" id='"+idCheckBoxProfile+"' onchange=\"onProfileActive('"+obj[i].NAME+"', '"+idCheckBoxProfile+"');\" /><div class='slider round'></div></label>";
                                                                            var sActiveApproveCA = "<label class='switch' for='"+idCheckBoxApprove+"'><input TYPE='checkbox' class='js-switch' data-switchery='true' "+isCheckedApprove+" id='"+idCheckBoxApprove+"' onchange=\"onApproveCAActive('"+obj[i].NAME+"', '"+idCheckBoxApprove+"');\" /><div class='slider round'></div></label>";
                                                                            contentProps += "<tr>" +
                                                                                "<td>" + sCount + "</td>" +
                                                                                "<td>" + obj[i].NAME + "</td>" +
                                                                                "<td>" + obj[i].REMARK + "</td>" +
                                                                                "<td>" + sActiveProfile + "</td>" +
                                                                                "<td>" + sActiveApproveCA + "</td>" +
                                                                                "</tr>";
                                                                            sCount++;
                                                                        } else if(obj[i].ATTRIBUTE_TYPE === JS_STR_JSON_SERVICE_TYPE_ITEM)
                                                                        {
                                                                            var idCheckBoxProfile = 'idCheckProfileRequest' + obj[i].NO;
                                                                            var idCheckBoxApprove = 'idCheckApproveCARequest' + obj[i].NO;
                                                                            var isCheckedProfile = "";
                                                                            var isCheckedApprove = "";
                                                                            if(obj[i].ACTIVE === "1")
                                                                            {
                                                                                isCheckedProfile = "checked";
                                                                            }
                                                                            if(obj[i].APPROVE_CA === "1")
                                                                            {
                                                                                isCheckedApprove = "checked";
                                                                            }
                                                                            var sActiveProfile = "<label class='switch' for='"+idCheckBoxProfile+"'><input disabled TYPE='checkbox' class='js-switch' data-switchery='true' "+isCheckedProfile+" id='"+idCheckBoxProfile+"' /><div class='slider round'></div></label>";
                                                                            var sActiveApproveCA = "<label class='switch' for='"+idCheckBoxApprove+"'><input TYPE='checkbox' class='js-switch' data-switchery='true' "+isCheckedApprove+" id='"+idCheckBoxApprove+"' onchange=\"onApproveCARequestActive('"+obj[i].NAME+"', '"+idCheckBoxApprove+"');\" /><div class='slider round'></div></label>";
                                                                            contentRequest += "<tr>" +
                                                                                "<td>" + sCountRequest + "</td>" +
                                                                                "<td>" + obj[i].NAME + "</td>" +
                                                                                "<td>" + obj[i].REMARK + "</td>" +
                                                                                "<td style='display:none;'>" + sActiveProfile + "</td>" +
                                                                                "<td>" + sActiveApproveCA + "</td>" +
                                                                                "</tr>";
                                                                            sCountRequest++;
                                                                        } else if(obj[i].ATTRIBUTE_TYPE === JS_STR_JSON_MAJOR_TYPE_ITEM)
                                                                        {
                                                                            var idCheckBoxProfile = 'idCheckProfileMajor' + obj[i].NO;
                                                                            var isCheckedProfile = "";
                                                                            var isCheckedApprove = "";
                                                                            if(obj[i].ACTIVE === "1")
                                                                            {
                                                                                isCheckedProfile = "checked";
                                                                            }
                                                                            var sActiveProfile = "<label class='switch' for='"+idCheckBoxProfile+"'><input TYPE='checkbox' class='js-switch' data-switchery='true' "+isCheckedProfile+" id='"+idCheckBoxProfile+"' onchange=\"onMajorActive('"+obj[i].NAME+"', '"+idCheckBoxProfile+"');\" /><div class='slider round'></div></label>";
                                                                            contentMajor += "<tr>" +
                                                                                "<td>" + sCountMajor + "</td>" +
                                                                                "<td>" + obj[i].NAME + "</td>" +
                                                                                "<td>" + obj[i].REMARK + "</td>" +
                                                                                "<td>" + sActiveProfile + "</td>" +
                                                                                "</tr>";
                                                                            sCountMajor++;
                                                                        }
                                                                    }
                                                                    $("#idTemplateAssignIP").append(contentProps);
                                                                    $("#idTemplateAssignRequest").append(contentRequest);
                                                                    $("#idTemplateAssignMajor").append(contentMajor);
            //                                                        if (sCount > 0)
            //                                                        {
            //                                                            $('#greenIP').smartpaginator({totalrecords: sCount, recordsperpage: 10, datacontainer: 'tblCertUseIP', dataelement: 'tr', initval: 0, next: global_paging_last, prev: global_paging_Before, first: global_paging_first, last: global_paging_next, theme: 'green'});
            //                                                        }
                                                                } else if (obj[0].Code === "11") {
                                                                    $("#idTemplateAssignIP").empty();
                                                                    $("#idTemplateAssignRequest").empty();
                                                                    var contentProps = '<tr><td colspan="5" id="idLblTitleTableNoFile">'+global_no_data+'</td></tr>';
                                                                    var contentRequest = '<tr><td colspan="5" id="idLblTitleTableRequestNoFile">'+global_no_data+'</td></tr>';
                                                                    var contentMajor = '<tr><td colspan="5" id="idLblTitleTableMajorNoFile">'+global_no_data+'</td></tr>';
                                                                    $("#idTemplateAssignIP").append(contentProps);
                                                                    $("#idTemplateAssignRequest").append(contentRequest);
                                                                    $("#idTemplateAssignMajor").append(contentMajor);
                                                                } else if (obj[0].Code === "1") { }
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
                                                                else {
                                                                    funErrorAlert(global_errorsql);
                                                                }
                                                                $(".loading-gif").hide();
                                                                $('#over').remove();
                                                            }
                                                        }
                                                    });
                                                    return false;
                                                }, 1000);
                                            }
                                            function onProfileActive(idName, idCheckProfie)
                                            {
                                                var sProfileActive = "0";
                                                if ($("#"+idCheckProfie).is(':checked')) {
                                                    sProfileActive = "1";
                                                }
                                                $.ajax({
                                                    type: "post",
                                                    url: "../ProfileAccessCommon",
                                                    data: {
                                                        idParam: 'activeprofileaccess',
                                                        idName: idName,
                                                        sProfileActive: sProfileActive
                                                    },
                                                    cache: false,
                                                    success: function (html)
                                                    {
                                                        var myStrings = sSpace(html).split('#');
                                                        if (myStrings[0] === "0")
                                                        {
            //                                                LoadProfileAll('0','0');
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
                                                            if (myStrings[1] === JS_STR_ERROR_CODE_99) {
                                                                funErrorAlert(global_error_login_info);
                                                            }
                                                            else {
                                                                funErrorAlert(global_errorsql);
                                                            }
                                                        }
                                                    }
                                                });
                                                return false;
                                            }
                                            function onApproveCAActive(idName, idCheckApproveCA)
                                            {
                                                var sProfileActive = "0";
                                                if ($("#"+idCheckApproveCA).is(':checked'))
                                                {
                                                    sProfileActive = "1";
                                                }
                                                $.ajax({
                                                    type: "post",
                                                    url: "../ProfileAccessCommon",
                                                    data: {
                                                        idParam: 'activeapproveaccess',
                                                        idName: idName,
                                                        sProfileActive: sProfileActive
                                                    },
                                                    cache: false,
                                                    success: function (html)
                                                    {
                                                        var myStrings = sSpace(html).split('#');
                                                        if (myStrings[0] === "0")
                                                        {
            //                                                LoadProfileAll('0','0');
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
                                                            if (myStrings[1] === JS_STR_ERROR_CODE_99) {
                                                                funErrorAlert(global_error_login_info);
                                                            }
                                                            else {
                                                                funErrorAlert(global_errorsql);
                                                            }
                                                        }
                                                    }
                                                });
                                                return false;
                                            }
                                            function onApproveCARequestActive(idName, idCheckApproveCA)
                                            {
                                                var sProfileActive = "0";
                                                if ($("#"+idCheckApproveCA).is(':checked'))
                                                {
                                                    sProfileActive = "1";
                                                }
                                                $.ajax({
                                                    type: "post",
                                                    url: "../ProfileAccessCommon",
                                                    data: {
                                                        idParam: 'activeapproveaccess',
                                                        idName: idName,
                                                        sProfileActive: sProfileActive
                                                    },
                                                    cache: false,
                                                    success: function (html)
                                                    {
                                                        var myStrings = sSpace(html).split('#');
                                                        if (myStrings[0] === "0")
                                                        {
            //                                                                    LoadProfileAll('0','0');
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
                                                            if (myStrings[1] === JS_STR_ERROR_CODE_99) {
                                                                funErrorAlert(global_error_login_info);
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
                                    </div>
                                </div>
                            </div>
                            <%
                                if(sessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                            %>
                            <div role="tabpanel" class="tab-pane fade" id="tab_contentIP" aria-labelledby="home-tab">
                                <div class="x_title" style="border-bottom: 0 solid #E6E9ED;margin-bottom: 0px;">
                                    <ul class="nav navbar-right panel_toolbox">
                                        <li style="color: red;font-weight: bold;">
                                            <label id="idViewSumService"></label><%= strMessService%>
                                            <script>$("#idViewSumService").text(global_label_grid_sum);</script>
                                        </li>
                                    </ul>
                                    <div class="clearfix"></div>
                                </div>
                                <div class="x_content">
                                    <div class="table-responsive">
                                        <table id="tblCertUseRequest" class="table table-bordered table-striped projects">
                                            <thead>
                                            <th id="idLblTitleTableRequestSTT"></th>
                                            <th id="idLblTitleTableRequestName"></th>
                                            <th id="idLblTitleTableRequestProfileRemark"></th>
                                            <th id="idLblTitleTableRequestPercent" style="display: none;"></th>
                                            <th id="idLblTitleTableRequestActive"></th>
                                            <script>
                                                $("#idLblTitleTableRequestSTT").text(global_fm_STT);
                                                $("#idLblTitleTableRequestName").text(cert_fm_type_request);
                                                $("#idLblTitleTableRequestProfileRemark").text(global_fm_Description);
                                                $("#idLblTitleTableRequestPercent").text(global_fm_active);
                                                $("#idLblTitleTableRequestActive").text(global_fm_approve_ca);
                                            </script>
                                            </thead>
                                            <tbody id="idTemplateAssignRequest">
                                                <%
                                                    int n=1;
                                                    boolean hasRequestRecord = false;
                                                    if(resIPData != null && resIPData[0].length > 0) {
                                                        for(CERTIFICATION_POLICY_DATA resIPData1 : resIPData[0]) {
                                                            if(resIPData1.attributeType.equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_ITEM_SERVICE_TYPE))
                                                            {
                                                                hasRequestRecord = true;
                                                %>
                                                <tr>
                                                    <td><%= n%></td>
                                                    <td><%= resIPData1.name %></td>
                                                    <td><%= "1".equals(sessLanguageGlobal) ? resIPData1.remark : resIPData1.remarkEn %></td>
                                                    <td style="display: none;">
                                                        <label class="switch" for="idCheckProfileRequest<%=n%>" style="margin-bottom: 0;">
                                                            <input type="checkbox" name="idCheckProfileRequest<%=n%>" id="idCheckProfileRequest<%=n%>" <%= resIPData1.enabled == true ? "checked" : ""%>/>
                                                            <div id="idCheckProfileClassRequest<%=n%>" class="slider round"></div>
                                                        </label>
                                                    </td>
                                                    <td>
                                                        <label class="switch" for="idCheckApproveCARequest<%=n%>" style="margin-bottom: 0;">
                                                            <input onclick="onApproveCARequestActive('<%=resIPData1.name%>', 'idCheckApproveCARequest<%=n%>');" type="checkbox"
                                                                name="idCheckApproveCARequest<%=n%>" id="idCheckApproveCARequest<%=n%>" <%= resIPData1.approveCAEnabled == true ? "checked" : ""%>/>
                                                            <div id="idCheckApproveCARequestClass<%=n%>" class="slider round"></div>
                                                        </label>
                                                    </td>
                                                </tr>
                                                <%
                                                            n++;
                                                            }
                                                        }
                                                    }
                                                %>
                                                <%
                                                    if(hasRequestRecord == false)
                                                    {
                                                %>
                                                <tr><td colspan="5" id="idLblTitleTableRequestNoFile"></td></tr>
                                                    <script>
                                                        $("#idLblTitleTableRequestNoFile").text(global_no_data);
                                                    </script>
                                                <%
                                                    }
                                                %>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </div>
                            <div role="tabpanel" class="tab-pane fade" id="tab_contentFunction" aria-labelledby="home-tab">
                                <div class="x_title" style="border-bottom: 0 solid #E6E9ED;margin-bottom: 0px;">
                                    <ul class="nav navbar-right panel_toolbox">
                                        <li style="color: red;font-weight: bold;">
                                            <label id="idViewSumMajor"></label><%= strMessMajor%>
                                            <script>$("#idViewSumMajor").text(global_label_grid_sum);</script>
                                        </li>
                                    </ul>
                                    <div class="clearfix"></div>
                                </div>
                                <div class="x_content">
                                    <div class="table-responsive">
                                        <table id="tblCertUseRequest" class="table table-bordered table-striped projects">
                                            <thead>
                                            <th id="idLblTitleTableMajorSTT"></th>
                                            <th id="idLblTitleTableMajorName"></th>
                                            <th id="idLblTitleTableMajorProfileRemark"></th>
                                            <th id="idLblTitleTableMajorActive"></th>
                                            <script>
                                                $("#idLblTitleTableMajorSTT").text(global_fm_STT);
                                                $("#idLblTitleTableMajorName").text(cert_fm_major_code);
                                                $("#idLblTitleTableMajorProfileRemark").text(cert_fm_major_name);
                                                $("#idLblTitleTableMajorActive").text(global_fm_active);
                                            </script>
                                            </thead>
                                            <tbody id="idTemplateAssignMajor">
                                                <%
                                                    int k=1;
                                                    boolean hasMajorRecord = false;
                                                    if(resIPData != null && resIPData[0].length > 0) {
                                                        for(CERTIFICATION_POLICY_DATA resIPData1 : resIPData[0]) {
                                                            if(resIPData1.attributeType.equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_ITEM_MAJOR_TYPE))
                                                            {
                                                                hasMajorRecord = true;
                                                %>
                                                <tr>
                                                    <td><%= k%></td>
                                                    <td><%= resIPData1.name %></td>
                                                    <td><%= "1".equals(sessLanguageGlobal) ? resIPData1.remark : resIPData1.remarkEn %></td>
                                                    <td>
                                                        <label class="switch" for="idCheckProfileMajor<%=k%>" style="margin-bottom: 0;">
                                                            <input onclick="onMajorActive('<%=resIPData1.name%>', 'idCheckProfileMajor<%=k%>');" type="checkbox"
                                                                name="idCheckProfileMajor<%=k%>" id="idCheckProfileMajor<%=k%>" <%= resIPData1.enabled == true ? "checked" : ""%>/>
                                                            <div id="idCheckProfileClassMajor<%=k%>" class="slider round"></div>
                                                        </label>
                                                    </td>
                                                </tr>
                                                <%
                                                            k++;
                                                            }
                                                        }
                                                    }
                                                %>
                                                <%
                                                    if(hasMajorRecord == false)
                                                    {
                                                %>
                                                <tr><td colspan="5" id="idLblTitleTableMajorNoFile"></td></tr>
                                                    <script>
                                                        $("#idLblTitleTableMajorNoFile").text(global_no_data);
                                                    </script>
                                                <%
                                                    }
                                                %>
                                            </tbody>
                                        </table>
                                        <script>
                                            function onMajorActive(idName, idCheckProfie)
                                            {
                                                var sProfileActive = "0";
                                                if ($("#"+idCheckProfie).is(':checked')) {
                                                    sProfileActive = "1";
                                                }
                                                $.ajax({
                                                    type: "post",
                                                    url: "../ProfileAccessCommon",
                                                    data: {
                                                        idParam: 'activemajoraccess',
                                                        idName: idName,
                                                        sProfileActive: sProfileActive
                                                    },
                                                    cache: false,
                                                    success: function (html)
                                                    {
                                                        var myStrings = sSpace(html).split('#');
                                                        if (myStrings[0] === "0")
                                                        {
                                                            //LoadProfileAll('0','0');
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
                                                            if (myStrings[1] === JS_STR_ERROR_CODE_99) {
                                                                funErrorAlert(global_error_login_info);
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
                                    </div>
                                </div>
                            </div>
                            <%}%>
                        </div>
                    </div>
                    
<!--                    <fieldset class="scheduler-border">
                        <legend class="scheduler-border" id="idLblTitleRoleSet"></legend>
                        <script>$("#idLblTitleRoleSet").text(profileaccss_fm_rose);</script>
                        
                    </fieldset>-->
                    
<!--                    <fieldset class="scheduler-border" style="clear: both;display: <= approveCAProfileEnabled ? "" : "none" %>">
                        <legend class="scheduler-border" id="idLblTitleRequestType"></legend>
                        <script>$("#idLblTitleRequestType").text(profileaccss_fm_service_type);</script>
                        <style type="text/css">
                            .table > thead > tr > th, .table > tbody > tr > td{vertical-align: middle;}
                            .table > thead > tr > th{border-bottom: none;}
                            .btn{margin-bottom: 0px;}
                            .panel_toolbox { min-width: 0;}
                        </style>
                        
                    </fieldset>-->
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label id="idLblTitleCreateUser" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input type="text" readonly name="strCreateUser" class="form-control123" value="<%= rs[0][0].CREATED_BY%>" />
                            </div>
                        </div>
                    </div>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label id="idLblTitleCreateDate" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input class="form-control123" name="Date" readonly value="<%= strDateLimit%>">
                            </div>
                        </div>
                    </div>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label id="idLblTitleUpdateUser" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input type="text" readonly name="strUpdateUser" class="form-control123" value="<%= EscapeUtils.CheckTextNull(rs[0][0].MODIFIED_BY)%>" />
                            </div>
                        </div>
                    </div>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label id="idLblTitleUpdateDate" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input class="form-control123" name="UpdateDate" readonly value="<%= EscapeUtils.CheckTextNull(rs[0][0].MODIFIED_DT) %>">
                            </div>
                        </div>
                    </div>
                </form>
            </div>
            <%
            } else {
            %>
            <div class="form-group" style="padding: 0px 0px 0 0px;margin: 0;text-align: center;">
                <label style="color: red;">error</label>
            </div>
            <%
                }
            } else {
            %>
            <div class="form-group" style="padding: 0px 0px 0 0px;margin: 0;text-align: center;">
                <label style="color: red;">error</label>
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
        <script src="../style/jquery.min.js"></script>
        <!--<script src="../style/bootstrap.min.js"></script>-->
        <!--<script src="../style/custom.min.js"></script>-->
        <script src="../js/moment.min.js"></script>
        <script src="../js/daterangepicker.js"></script>
<!--        <link href="../Css/smartpaginator.css" rel="stylesheet" type="text/css"/>
        <script src="../Css/smartpaginator.js" type="text/javascript"></script>-->
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