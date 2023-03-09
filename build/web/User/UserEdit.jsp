<%-- 
    Document   : UserEdit
    Created on : Oct 11, 2013, 5:47:47 PM
    Author     : Thanh
--%>

<%@page import="vn.ra.utility.PropertiesContent"%>
<%@page import="vn.ra.process.SessionRoleFunctions"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="../Admin/ConnectionParam.jsp" %>
<!DOCTYPE html>
<%  response.setHeader("Cache-Control", "no-cache");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", -1);
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <META HTTP-EQUIV="PRAGMA" CONTENT="NO-CACHE">
        <META HTTP-EQUIV="Expires" CONTENT="-1">
        <link href="../style/bootstrap.min.css" rel="stylesheet">
        <link href="../style/font-awesome.css" rel="stylesheet">
        <link href="../style/nprogress.css" rel="stylesheet">
        <link href="../style/custom.min.css" rel="stylesheet">
        <!--<link href="../style/switchery/dist/switchery.min.css" rel="stylesheet">-->
        <!--<link href="../Css/active/bootstrap-switch.css" rel="stylesheet">-->
        <script src="../js/Language.js"></script>
        <script src="../js/process_javajs.js"></script>
        <link href="../style/customportal.min.css" rel="stylesheet">
        <script type="text/javascript" src="../js/jquery.js"></script>
        <link rel="stylesheet" href="../js/sweetalert.css"/>
        <script src="../js/sweetalert-dev.js"></script>
        <script type="text/javascript" src="../Css/GlobalAlert.js"></script>
        <script type="text/javascript">
            $(document).ready(function () {
                $('.loading-gif').hide();
                $("#idLblTitleEdits").text(user_title_edit);
                $("#idLblTitleUsername").text(global_fm_Username);
                $("#idLblTitleFullname").text(global_fm_fullname);
                $("#idLblTitleRole").text(global_fm_role);
                $("#idLblTitleBranch").text(global_fm_Branch);
                $("#idLblTitleStatus").text(global_fm_Status);
                $("#idLblTitleEmail").text(global_fm_email);
                $("#idLblTitlePhone").text(global_fm_phone);
                $("#idLblTitleSSL").text(global_fm_login_ssl);
                $("#idLblTitleChooseCert").text(global_fm_choose_cert);
                $("#idLblTitleUnchooseCert").text(global_fm_unchoose_cert);
                $("#idLblTitleGroupCert").text(global_group_cert);
                $("#idLblTitleCompany").text(global_fm_company);
                $("#idLblTitleIssue").text(global_fm_issue);
                $("#idLblTitleValid").text(global_fm_valid);
                $("#idLblTitleExpire").text(global_fm_Expire);
                $("#idLblTitleActive").text(global_fm_active);
                $("#idLblTitleRoleSet").text(user_title_roleset);
                $("#idLblTitleRoleSetToken").text(user_title_roleset_token);
                $("#idLblTitleTableTokenSST").text(global_fm_STT);
                $("#idLblTitleTableTokenName").text(role_fm_function_name);
                $("#idLblTitleTableTokenDes").text(global_fm_Description);
                $("#idLblTitleTableTokenActive").text(global_fm_active);
                $("#idLblTitleRoleSetCert").text(user_title_roleset_cert);
                $("#idLblTitleTableCertSST").text(global_fm_STT);
                $("#idLblTitleTableCertName").text(role_fm_function_name);
                $("#idLblTitleTableCertDes").text(global_fm_Description);
                $("#idLblTitleTableCertActive").text(global_fm_active);
                $("#idLblTitleRoleSetOther").text(user_title_roleset_another);
                $("#idLblTitleTableOtherSST").text(global_fm_STT);
                $("#idLblTitleTableOtherName").text(role_fm_function_name);
                $("#idLblTitleTableOtherDes").text(global_fm_Description);
                $("#idLblTitleTableOtherActive").text(global_fm_active);
                $("#idLblTitleCreateUser").text(global_fm_user_create);
                $("#idLblTitleCreateDate").text(global_fm_date_create);
                $("#idLblTitleUpdateUser").text(global_fm_user_endupdate);
                $("#idLblTitleUpdateDate").text(global_fm_date_endupdate);
                $("#idLblTitleDelete").text(user_title_delete);
                $("#idLblTitleDeleteNote").text(user_title_delete_note);
                $("#idLblTitleDeleteBranch").text(global_fm_Branch);
                $("#idLblTitleUserReceive").text(global_fm_user_receive);
                $("#idLblNoteFullname").text(global_fm_require_label);
                $("#idLblNoteEmail").text(global_fm_require_label);
                $("#idLblNotePhone").text(global_fm_require_label);
//                $('#myModalOTPHardware').modal({
//                    backdrop: 'static',
//                    keyboard: true,
//                    show: false
//                });
//                if(localStorage.getItem("LOCAL_PARAM_USERLIST") !== null && localStorage.getItem("LOCAL_PARAM_USERLIST") !== "null")
//                {
//                    var vParamUrl = getUrlParam("id", "");
//                    if(vParamUrl !== localStorage.getItem("LOCAL_PARAM_USERLIST"))
//                    {
//                        window.location = "../Admin/Home.jsp";
//                    }
//                } else {
//                    window.location = "UserList.jsp";
//                }
            });
            function ValidateForm(idCSRF) {
                if (!JSCheckEmptyField(document.myname.Username.value))
                {
                    document.myname.Username.focus();
                    funErrorAlert(policy_req_empty + global_fm_Username);
                    return false;
                }
                if (!JSCheckEmptyField(document.myname.FullName.value))
                {
                    document.myname.FullName.focus();
                    funErrorAlert(policy_req_empty + global_fm_fullname);
                    return false;
                }
                var email = document.myname.Email;
                if (!JSCheckEmptyField(email.value))
                {
                    email.focus();
                    funErrorAlert(policy_req_empty + global_fm_email);
                    return false;
                }
                else
                {
//                    var vValuePhone = sSpace($("#Email").val());
//                    var cChatStatus = getCheckStatusEmail(vValuePhone);
//                    cChatStatus.done(function(msg) {
//                        var s = sSpace(msg).split('#');
//                        if(s[0] !== "0")
//                        {
//                            $("#Email").focus();
//                            funErrorAlert(global_req_mail_format);
//                            return false;
//                        }
//                    });
                    if (!FormCheckEmailSearchHand(localStorage.getItem("sessLocal_REGEX_EMAIL"), email.value)) {
                        email.focus();
                        funErrorAlert(global_req_mail_format);
                        return false;
                    }
                }
                var x = document.myname.MobileNumber;
                if (!JSCheckEmptyField(x.value))
                {
                    x.focus();
                    funErrorAlert(policy_req_empty + global_fm_phone);
                    return false;
                }
                else
                {
//                    var vValuePhoneNum = trimAllString($("#MobileNumber").val());
//                    var cChatStatusNum = getCheckStatusPhone(vValuePhoneNum);
//                    cChatStatusNum.done(function(msg) {
//                        var s = sSpace(msg).split('#');
//                        if(s[0] !== "0")
//                        {
//                            $("#MobileNumber").focus();
//                            funErrorAlert(global_req_phone_format);
//                            return false;
//                        }
//                    });
                    if (!FormCheckPhoneHand(localStorage.getItem("sessLocal_REGEX_PHONE"), $("#MobileNumber")))
                    {
                        x.focus();
                        funErrorAlert(global_req_phone_format);
                        return false;
                    }
                }
                var sCheckActiveFlag = "0";
//                var sTempActive = $("#ActiveFlag").bootstrapSwitch("state");
//                if(sTempActive === true)
                if ($("#ActiveFlag").is(':checked'))
                {
                    sCheckActiveFlag = "1";
                }
                if(document.myname.GroupName.value !== JS_STR_ROLE_ADMIN_CA && document.myname.GroupName.value !== JS_STR_ROLE_ADMIN_SUPER_CA)
                {
                    document.myname.idCertCompany.value = "";
                }
                if(sSpace(document.myname.USER_STATE.value) === JS_STR_USER_STATE_CANCEL_ID)
                {
                    swal({
                        title: "",
                        text: user_conform_cancel,
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
                                url: "../UserCommon",
                                data: {
                                    idParam: 'edituser',
                                    id: document.myname.TMSUserID.value,
                                    Username: document.myname.Username.value,
                                    sAdmin: document.myname.sAdmin.value,
                                    FullName: document.myname.FullName.value,
                                    Username1: document.myname.Username1.value,
                                    GroupName1: document.myname.GroupName1.value,
                                    GroupName: document.myname.GroupName.value,
                                    idSSL: document.myname.idCertCompany.value,
                                    USER_STATE: document.myname.USER_STATE.value,
                                    BranchName: document.myname.BranchName.value,
//                                    USER_STATE: "",
                                    Email: email.value,
                                    MobileNumber: x.value,
                                    ActiveFlag: sCheckActiveFlag,
                                    CsrfToken: idCSRF
                                },
                                cache: false,
                                success: function (html) {
                                    console.log(sSpace(html));
                                    var arr = sSpace(html).split('#');
                                    if (arr[0] === "0")
                                    {
                                        localStorage.setItem("EDIT_USER", document.myname.TMSUserID.value);
                                        funSuccAlert(user_succ_edit, "UserList.jsp");
                                    }
                                    else if (arr[0] === "10")
                                    {
                                        funErrorAlert(global_req_all);
                                    }
                                    else if (arr[0] === "11")
                                    {
                                        funErrorAlert(global_req_length);
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
                                    } else if (arr[0] === "2") {
                                        funErrorAlert(user_exists_email);
                                    } else if (arr[0] === "3") {
                                        funErrorAlert(user_exists_user_role_admin);
                                    } else if (arr[0] === "4") {
                                        funErrorAlert(user_exists_cert_hash);
                                    } else if (arr[1] === JS_STR_ERROR_CODE_99) {
                                        funErrorAlert(global_error_login_info);
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
                else
                {
                    $('body').append('<div id="over"></div>');
                    $(".loading-gif").show();
                    $.ajax({
                        type: "post",
                        url: "../UserCommon",
                        data: {
                            idParam: 'edituser',
                            id: document.myname.TMSUserID.value,
                            Username: document.myname.Username.value,
                            sAdmin: document.myname.sAdmin.value,
                            FullName: document.myname.FullName.value,
                            Username1: document.myname.Username1.value,
                            GroupName1: document.myname.GroupName1.value,
                            GroupName: document.myname.GroupName.value,
                            idSSL: document.myname.idCertCompany.value,
                            USER_STATE: document.myname.USER_STATE.value,
                            BranchName: document.myname.BranchName.value,
//                            USER_STATE: "",
                            Email: email.value,
                            MobileNumber: x.value,
                            ActiveFlag: sCheckActiveFlag,
                            CsrfToken: idCSRF
                        },
                        cache: false,
                        success: function (html) {
                            console.log(sSpace(html));
                            var arr = sSpace(html).split('#');
                            if (arr[0] === "0")
                            {
                                localStorage.setItem("EDIT_USER", document.myname.TMSUserID.value);
                                funSuccAlert(user_succ_edit, "UserList.jsp");
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
                            else if (arr[0] === "2") {
                                funErrorAlert(user_exists_email);
                            } else if (arr[0] === "3") {
                                funErrorAlert(user_exists_user_role_admin);
                            } else if (arr[0] === "4") {
                                funErrorAlert(user_exists_cert_hash);
                            } else if (arr[1] === JS_STR_ERROR_CODE_99) {
                                funErrorAlert(global_error_login_info);
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
            }
            function changePassword(idCSRF) {
                swal({
                    title: "",
                    text: user_conform_reset_pass,
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
                            url: "../UserCommon",
                            data: {
                                idParam: 'changepassdefault',
                                id: document.myname.TMSUserID.value,
                                Username: document.myname.Username.value,
                                CsrfToken: idCSRF
                            },
                            cache: false,
                            success: function (html) {
                                var arr = sSpace(html).split('#');
                                if (arr[0] === "0")
                                {
//                                    var s = pass_succ_change + pass_succ_change_show + arr[2];
                                    localStorage.setItem("EDIT_USER", document.myname.TMSUserID.value);
                                    funSuccAlert(pass_succ_change, "UserList.jsp");
                                }
                                else if (arr[0] === "1")
                                {
                                    funErrorLoginAlert(sendmail_error);
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
            
            function BeforeDeleteUser(idRole, idCSRF) {
                swal({
                    title: "",
                    text: user_conform_delete,
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
                            url: "../UserCommon",
                            data: {
                                idParam: 'checkcerttouser',
                                id: document.myname.TMSUserID.value,
                                idRole: idRole,
                                CsrfToken: idCSRF
                            },
                            cache: false,
                            success: function (html) {
                                var arr = sSpace(html).split('#');
                                if (arr[0] === "0")
                                {
                                    if(arr[1] === "0")
                                    {
                                        AfterDeleteUser("0", idCSRF);
                                    } else if(arr[1] === "1")
                                    {
                                        ShowDialog();
                                    }
                                    else
                                    {
                                        funErrorAlert(global_errorsql);
                                        $(".loading-gif").hide();
                                        $('#over').remove();
                                    }
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
            function AfterDeleteUser(isHasCert, idCSRF) {
                if(isHasCert === "1") {
                    if ($("#USER").val() === "null" || $("#USER").val() === null)
                    {
                        funErrorAlert(policy_req_empty_choose + global_fm_user_receive);
                        return false;
                    } else
                    {
                        if(!JSCheckEmptyField($("#USER").val()))
                        {
                            funErrorAlert(policy_req_empty_choose + global_fm_user_receive);
                            return false;
                        }
                    }
                }
                $('body').append('<div id="over"></div>');
                $(".loading-gif").show();
                $.ajax({
                    type: "post",
                    url: "../UserCommon",
                    data: {
                        idParam: 'deleteuser',
                        id: document.myname.TMSUserID.value,
                        idToUSER: $("#USER").val(),
                        idToAGENT_NAME: $("#AGENT_NAME").val(),
                        isHasCert: isHasCert,
                        CsrfToken: idCSRF
                    },
                    cache: false,
                    success: function (html) {
                        var arr = sSpace(html).split('#');
                        if (arr[0] === "0")
                        {
                            funSuccAlert(user_succ_delete, "UserList.jsp");
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
            }
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
            function closeForm()
            {
               $.ajax({
                    type: "post",
                    url: "../SomeCommon",
                    data: {
                        idParam: 'backformpage',
                        idSession: 'RefreshUserSess'
                    },
                    cache: false,
                    success: function (html) {
                        var arr = sSpace(html);
                        if (arr === "0")
                        {
                            window.location = "UserList.jsp";
                        }
                        else
                        {
                            window.location = "UserList.jsp";
                        }
                    }
                });
                return false;
            }
        </script>
<!--        <style>
            fieldset.scheduler-border {
                border: 1px solid #E6E9ED !important;
                padding: 0 1.2em 5px 1.2em !important;
                margin: 0 0 1.1em 0 !important;
                -webkit-box-shadow:  0px 0px 0px 0px #E6E9ED;
                box-shadow:  0px 0px 0px 0px #E6E9ED;
            }
            .switch {
                display: inline-block;
                height: 34px;
                position: relative;
                width: 60px;
            }

            .switch input {
                display:none;
            }

            .slider {
                background-color: #ccc;
                bottom: 0;
                cursor: pointer;
                left: 0;
                position: absolute;
                right: 0;
                top: 0;
                transition: .4s;
            }

            .slider:before {
                background-color: #fff;
                bottom: 4px;
                content: "";
                height: 26px;
                left: 4px;
                position: absolute;
                transition: .4s;
                width: 26px;
            }

            input:checked + .slider {
                background-color: #26B99A;
            }

            input:checked + .slider:before {
                transform: translateX(26px);
            }

            .slider.round {
                border-radius: 34px;
            }

            .slider.round:before {
                border-radius: 50%;
            }
        </style>-->
    </head>
    <body>
        <%
            session.setAttribute("sessRoleFunctionsToken",null);
            session.setAttribute("sessRoleFunctionsCert",null);
            session.setAttribute("sessRoleFunctionsAnother",null);
            String anticsrf = "" + Math.random();
            request.getSession().setAttribute("anticsrf", anticsrf);
            String sessLanguageGlobal = session.getAttribute("sessVN").toString();
            String sessUserIDGlobal = session.getAttribute("UserID").toString();
            String SessRoleID_ID = session.getAttribute("RoleID_ID").toString().trim();
            String SessLevelBranch = session.getAttribute("sessLevelBranch").toString().trim();
        %>
        <div style="width: 100%; text-align: center; position: fixed;z-index: 1000;top: 0; padding-top: 300px;
             left: 0; height: 100%;" class="loading-gif">
            <img src="../Images/ajax-loader1.gif" alt="Please wait..." />
        </div>
        <%
            BACKOFFICE_USER[][] rs = new BACKOFFICE_USER[1][];
            String ids = EscapeUtils.CheckTextNull(request.getParameter("id"));
            int sBranchID_User = 0;
            try {
                String sAdmin = "";
                String SessAgentID = session.getAttribute("SessAgentID").toString().trim();
                String sessUserAgentID = session.getAttribute("SessUserAgentID").toString().trim();
                if (EscapeUtils.IsInteger(ids) == true) {
                    session.setAttribute("sessCertSSLToken", null);
                    session.setAttribute("sessHashSSLToken", null);
                    db.S_BO_USER_DETAIL(EscapeUtils.escapeHtml(ids), sessLanguageGlobal, rs);
                    String sTemStateCancel = "";
                    String sDisActiveSuperCA = "";
                    String sIsRoleAdmin = "";
                    String sIsRoleAdminFunc = "";
                    String roleChilrentAgent = "0";
                    if (rs[0].length > 0) {
                        String sUser = EscapeUtils.CheckTextNull(rs[0][0].USERNAME);
                        if(String.valueOf(rs[0][0].ROLE_ID).equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN)
                            || String.valueOf(rs[0][0].ROLE_ID).equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN_CHILD)
                            || String.valueOf(rs[0][0].ROLE_ID).equals(Definitions.CONFIG_ROLE_ID_AGENT_ADMIN))
                        {
                            sAdmin = "disabled";
                        }
                        if(SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT))
                        {
                            if(SessRoleID_ID.equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN))
                            {
                                if(String.valueOf(rs[0][0].ROLE_ID).equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN_CHILD))
                                {
                                    sAdmin = "";
                                }
                                if(String.valueOf(rs[0][0].ROLE_ID).equals(Definitions.CONFIG_ROLE_ID_AGENT_ADMIN))
                                {
                                    sAdmin = "";
                                }
                            }
                            if(SessRoleID_ID.equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN_CHILD))
                            {
                                if(String.valueOf(rs[0][0].ROLE_ID).equals(Definitions.CONFIG_ROLE_ID_AGENT_ADMIN))
                                {
                                    sAdmin = "";
                                }
                            }
                        }
                        if(rs[0][0].USER_STATE_ID == Definitions.CONFIG_USER_STATE_CANCEL_ID)
                        {
                            sTemStateCancel = "disabled";
                            sAdmin = "disabled";
                        }
                        String sDisableAgent = "";
                        if(SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                            if(SessRoleID_ID.equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN))
                            {
                                if(!sessUserAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                    if(String.valueOf(rs[0][0].ROLE_ID).equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN))
                                    {
                                        sIsRoleAdmin = "disabled";
                                        sIsRoleAdminFunc = "disabled";
                                        if(session.getAttribute("sUserID").toString().trim().equals(EscapeUtils.CheckTextNull(rs[0][0].USERNAME)))
                                        {
                                            sIsRoleAdmin = "";
                                            sIsRoleAdminFunc = "";
                                        }
                                    }
                                } else {
                                    if(String.valueOf(rs[0][0].ROLE_ID).equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN)
                                        && rs[0][0].BRANCH_ID == Integer.parseInt(Definitions.CONFIG_AGENT_ROOT))
                                    {
                                        sIsRoleAdmin = "disabled";
                                        sIsRoleAdminFunc = "disabled";
                                        if(session.getAttribute("sUserID").toString().trim().equals(EscapeUtils.CheckTextNull(rs[0][0].USERNAME)))
                                        {
                                            sIsRoleAdmin = "";
                                            sIsRoleAdminFunc = "";
                                        }
                                    }
                                }
                            }
                            if(SessRoleID_ID.equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN_CHILD)) {
                                if(String.valueOf(rs[0][0].ROLE_ID).equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN)
                                    || String.valueOf(rs[0][0].ROLE_ID).equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN_CHILD))
                                {
                                    sIsRoleAdmin = "disabled";
                                    sIsRoleAdminFunc = "disabled";
                                    if(session.getAttribute("sUserID").toString().trim().equals(EscapeUtils.CheckTextNull(rs[0][0].USERNAME)))
                                    {
                                        sIsRoleAdmin = "";
                                        sIsRoleAdminFunc = "";
                                    }
                                }
                            }
                        } else {
                            if(SessRoleID_ID.equals(Definitions.CONFIG_ROLE_ID_AGENT_ADMIN)) {
                                if(String.valueOf(rs[0][0].ROLE_ID).equals(Definitions.CONFIG_ROLE_ID_AGENT_ADMIN)) {
                                    if(SessLevelBranch.equals(rs[0][0].BRANCH_LEVEL_ID)) {
                                        sIsRoleAdmin = "disabled";
                                        sIsRoleAdminFunc = "disabled";
                                    }
                                    if(session.getAttribute("sUserID").toString().trim().equals(EscapeUtils.CheckTextNull(rs[0][0].USERNAME))) {
                                        sIsRoleAdmin = "";
                                    }
                                }
                                if(SessLevelBranch.equals(rs[0][0].BRANCH_LEVEL_ID)) {
                                    if(String.valueOf(rs[0][0].ROLE_ID).equals(Definitions.CONFIG_ROLE_ID_AGENT_ADMIN)) {
                                        sDisableAgent = "disabled";
                                    }
                                }
                            }
                        }
                        sBranchID_User = rs[0][0].BRANCH_ID;
                        String sJSON_FUNCTION = EscapeUtils.CheckTextNull(rs[0][0].PROPERTIES);
                        SessionRoleFunctions cartToken = (SessionRoleFunctions) session.getAttribute("sessRoleFunctionsToken");
                        if (cartToken == null) {
                            cartToken = new SessionRoleFunctions();
                        }
                        SessionRoleFunctions cartCert = (SessionRoleFunctions) session.getAttribute("sessRoleFunctionsCert");
                        if (cartCert == null) {
                            cartCert = new SessionRoleFunctions();
                        }
                        SessionRoleFunctions cartAnother = (SessionRoleFunctions) session.getAttribute("sessRoleFunctionsAnother");
                        if (cartAnother == null) {
                            cartAnother = new SessionRoleFunctions();
                        }
                        if(!"".equals(sIsRoleAdmin))
                        {
                            sTemStateCancel = "disabled";
                            sAdmin = "disabled";
                        }
                        String sPARENT_ID = "";
                        BRANCH[][] rsBRANCH = new BRANCH[1][];
                        db.S_BO_BRANCH_DETAIL(String.valueOf(rs[0][0].BRANCH_ID), rsBRANCH);
                        if(rsBRANCH[0].length > 0)
                        {
                            sPARENT_ID = String.valueOf(rsBRANCH[0][0].PARENT_ID);
                        }
                        String sCERTIFICATION = EscapeUtils.CheckTextNull(rs[0][0].CERTIFICATION);
                        String sCERTIFICATION_HASH = EscapeUtils.CheckTextNull(rs[0][0].CERTIFICATION_HASH);
                        if(!"".equals(sCERTIFICATION_HASH))
                        {
                            session.setAttribute("sessCertSSLToken", sCERTIFICATION);
                            session.setAttribute("sessHashSSLToken", sCERTIFICATION_HASH);
                        }
                        String sRegexPolicy = "";
                        GENERAL_POLICY[][] sessGeneralPolicy = (GENERAL_POLICY[][]) session.getAttribute("sessGeneralPolicy_System");
                        if (sessGeneralPolicy[0].length > 0) {
                            for(GENERAL_POLICY rsPolicy1: sessGeneralPolicy[0])
                            {
                                if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_SYS_REGEX_FOR_PHONE_EMAIL))
                                {
                                    sRegexPolicy = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                    break;
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
                        Config conf = new Config();
                        String certAccessEnabled = conf.GetPropertybyCode(Definitions.CONFIG_CERT_MODULES_ACCESS_ENABLED);
                        if(!SessLevelBranch.equals(Definitions.CONFIG_BRANCH_LEVEL_CA)
                            && !SessLevelBranch.equals(Definitions.CONFIG_BRANCH_LEVEL_CHILREN_ONE)
                            && !SessLevelBranch.equals(""))
                        {
                            roleChilrentAgent = "1";
                        }
                        String sUserPermissionEnable = conf.GetPropertybyCode(Definitions.CONFIG_USER_PERMISSION_EDIT_ENABLED);
                        String permissionDisbled = "";
                        if("0".equals(sUserPermissionEnable)){
                            permissionDisbled = "disabled";
                        }
                        if(SessRoleID_ID.equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN)) {
                            if(String.valueOf(rs[0][0].ROLE_ID).equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN)) {
                                sDisActiveSuperCA = "disabled";
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
            <h2><i class="fa fa-list-ul"></i> <span id="idLblTitleEdits" style="color: #36526D;"></span></h2>
            <ul class="nav navbar-right panel_toolbox">
                <li>
                    <%
                        if("".equals(sTemStateCancel)) {
                    %>
                    <input type="button" id="btnSave" data-switch-get="state" class="btn btn-info" onclick="ValidateForm('<%=anticsrf%>');" />
                    <script>
                        document.getElementById("btnSave").value = global_fm_button_edit;
                    </script>
                    <%
                        String sSesRoleID = session.getAttribute("RoleID_ID").toString().trim();
                        if (sSesRoleID.equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN)
                            || sSesRoleID.equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN_CHILD)
                            || sSesRoleID.equals(Definitions.CONFIG_ROLE_ID_AGENT_ADMIN)) {
                    %>
                    <input type="button" id="btnPass" class="btn btn-info" onclick="changePassword('<%=anticsrf%>');"/>
                    <script>document.getElementById("btnPass").value = global_fm_button_reset_pass;</script>
                    <%
                        if(!sessUserIDGlobal.equals(ids))
                        {
                    %>
                    <input type="button" id="btnDelete" class="btn btn-info" onclick="BeforeDeleteUser('<%= String.valueOf(rs[0][0].ROLE_ID)%>', '<%=anticsrf%>');"/>
                    <script>document.getElementById("btnDelete").value = global_fm_button_delete;</script>
                    <%
                                }
                            }
                        }
                    %>
                    <%
                        if(session.getAttribute("RoleID").toString().trim().equals("HOTROKENH")) {
                            if(!String.valueOf(rs[0][0].ROLE_ID).equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN)
                                && !String.valueOf(rs[0][0].ROLE_ID).equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN_CHILD)) {
                    %>
                    <input type="button" id="btnPass" class="btn btn-info" onclick="changePassword('<%=anticsrf%>');"/>
                    <script>document.getElementById("btnPass").value = global_fm_button_reset_pass;</script>
                    <%
                            }
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
                <input type="hidden" name="TMSUserID" hidden="true" readonly="true" value="<%= rs[0][0].ID %>">
                <input type="hidden" name="GroupName1" hidden="true" readonly="true" value="<%= rs[0][0].ROLE_ID %>"/>
                <input type="hidden" name="Username1" hidden="true" readonly="true" value="<%= sUser%>"/>
                <input type="hidden" name="sAdmin" hidden="true" readonly="true" value="<%= sAdmin%>" />
                <input type="hidden" name="CsrfToken" value="<%=anticsrf%>"/>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitleUsername" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input type="text" name="Username" readonly maxlength="64" value="<%= sUser%>" class="form-control123" <%= sAdmin%>>
                        </div>
                    </div>
                </div>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                            <label id="idLblTitleFullname"></label>
                            <label id="idLblNoteFullname"class="CssRequireField"></label>
                        </label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input type="text" <%= sTemStateCancel%> name="FullName" maxlength="64" value="<%= rs[0][0].FULL_NAME%>" class="form-control123">
                        </div>
                    </div>
                </div>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitleBranch" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <select name="BranchName" id="BranchName" onchange="LOAD_ROLE_FOR_BRANCH(this.value);" class="form-control123" <%= sTemStateCancel%> <%= sDisableAgent%>>
                                <%
                                    try {
                                        String sSessTreeName = "sessTreeBranchSystem";
                                        if(sessUserAgentID.equals(Definitions.CONFIG_AGENT_ROOT)){
                                            sSessTreeName = "sessTreeBranchSystemRoot";
                                        }
                                        BRANCH[][] rsBranch = (BRANCH[][]) session.getAttribute(sSessTreeName);
                                        if (rsBranch[0].length > 0) {
                                            for (BRANCH temp1 : rsBranch[0]) {
                                %>
                                <option value="<%=String.valueOf(temp1.ID) + "#" + String.valueOf(temp1.PARENT_ID)%>" <%= rs[0][0].BRANCH_ID == temp1.ID ? "selected" : "" %>><%=temp1.NAME + " - " + temp1.REMARK %></option>
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
                            <!--<input type="text" readonly name="BranchName" value="<= EscapeUtils.CheckTextNull(rs[0][0].BRANCH_REMARK)%>" class="form-control123"/>-->
                        </div>
                        <script>
                            function LOAD_ROLE_FOR_BRANCH(objc)
                            {
                                if(objc.split('#')[0] === JS_STR_AGENCY_ROOT_CA)
                                {
                                    $("#UserRole").attr("disabled", true);
                                } else{$("#UserRole").attr("disabled", false);}
                                $.ajax({
                                    type: "post",
                                    url: "../JSONCommon",
                                    data: {
                                        idParam: 'loadrole_forbranch',
                                        idParent: objc.split('#')[1]
                                    },
                                    cache: false,
                                    success: function (html)
                                    {
                                        if (html.length > 0)
                                        {
                                            var sessRoleAdminCA = '<%=SessRoleID_ID%>';
                                            var cbxUSER = document.getElementById("UserRole");
                                            removeOptions(cbxUSER);
                                            var obj = JSON.parse(html);
                                            if (obj[0].Code === "0")
                                            {
                                                var vRoleID_Frist = "";
                                                if(objc.split('#')[0] === JS_STR_AGENCY_ROOT_CA) {
                                                    for (var i = 0; i < obj.length; i++) {
                                                        if(vRoleID_Frist === "") {
                                                            vRoleID_Frist = obj[i].ID;
                                                        }
                                                        cbxUSER.options[cbxUSER.options.length] = new Option(obj[i].REMARK, obj[i].ID);
                                                        if(obj[i].ID === objc.split('#')[1])
                                                        {
                                                            $("#UserRole" + " option[value='" + JS_STR_ROLE_ADMIN_SUPER_CA + "']").attr("selected", "selected");
                                                        }
                                                    }
                                                    $("#UserRole").attr("disabled", true);
                                                } else {
                                                    for (var i = 0; i < obj.length; i++) {
                                                        if(vRoleID_Frist === "")
                                                        {
                                                            vRoleID_Frist = obj[i].ID;
                                                        }
                                                        cbxUSER.options[cbxUSER.options.length] = new Option(obj[i].REMARK, obj[i].ID);
                                                    }
                                                    $("#UserRole").attr("disabled", false);
                                                }
                                                onChangeFunction(vRoleID_Frist, '<%= sessLanguageGlobal%>', '<%= anticsrf%>', "0");
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
                        </script>
                    </div>
                </div>
                <div class="col-sm-6" style="padding-left: 0;display: none;">
                    <div class="form-group">
                        <label id="idLblTitleBranch" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input type="text" readonly name="BranchName123" value="<%= EscapeUtils.CheckTextNull(rs[0][0].BRANCH_REMARK)%>" class="form-control123"/>
                        </div>
                    </div>
                </div>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitleRole" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <select name="GroupName" id="UserRole" class="form-control123" onchange="onChangeFunction(this.value, '<%= sessLanguageGlobal%>', '<%= anticsrf%>');" <%= sAdmin%>>
                                <%
                                    ROLE[][] rssGroup = new ROLE[1][];
                                    db.S_BO_ROLE_COMBOBOX(sessLanguageGlobal, rssGroup);
                                    if (rssGroup[0].length > 0) {
                                        if (SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                            if(SessRoleID_ID.equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN)) {
                                                if(!"1".equals(sPARENT_ID))
                                                {
                                                    for (int i = 0; i < rssGroup[0].length; i++) {
                                                        if(rssGroup[0][i].CA_ENABLED == false)
                                                        {
                                        %>
                                        <option value="<%=String.valueOf(rssGroup[0][i].ID)%>" <%= rssGroup[0][i].ID == rs[0][0].ROLE_ID ? "selected" : ""%>><%=rssGroup[0][i].REMARK %></option>
                                        <%
                                                        }
                                                    }
                                                }
                                                else
                                                {
                                                    for (int i = 0; i < rssGroup[0].length; i++) {
                                                        if(rssGroup[0][i].CA_ENABLED == true)
                                                        {
                                        %>
                                        <option value="<%=String.valueOf(rssGroup[0][i].ID)%>" <%= rssGroup[0][i].ID == rs[0][0].ROLE_ID ? "selected" : ""%>><%=rssGroup[0][i].REMARK %></option>
                                        <% 
                                                        }
                                                    }
                                                }
                                            }
                                            else
                                            {
                                                if(!"1".equals(sPARENT_ID))
                                                {
                                                    for (int i = 0; i < rssGroup[0].length; i++) {
                                                        if(rssGroup[0][i].CA_ENABLED == false) {
                                                            if(!String.valueOf(rssGroup[0][i].ID).equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN)) {
                                %>
                                <option value="<%=String.valueOf(rssGroup[0][i].ID)%>" <%= rssGroup[0][i].ID == rs[0][0].ROLE_ID ? "selected" : ""%>><%=rssGroup[0][i].REMARK %></option>
                                <%
                                                            }
                                                        }
                                                    }
                                                }
                                                else 
                                                {
                                                    for (int i = 0; i < rssGroup[0].length; i++) {
                                                        if(rssGroup[0][i].CA_ENABLED == true) {
                                                            if(!String.valueOf(rssGroup[0][i].ID).equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN)) {
                                %>
                                <option value="<%=String.valueOf(rssGroup[0][i].ID)%>" <%= rssGroup[0][i].ID == rs[0][0].ROLE_ID ? "selected" : ""%>><%=rssGroup[0][i].REMARK %></option>
                                <%
                                                    }
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    if (!String.valueOf(rs[0][0].ROLE_ID).equals(Definitions.CONFIG_ROLE_ID_AGENT_ADMIN)) {
                                        for (int i = 0; i < rssGroup[0].length; i++) {
                                            if(rssGroup[0][i].CA_ENABLED == false)
                                            {
                                                if (!String.valueOf(rssGroup[0][i].ID).equals(Definitions.CONFIG_ROLE_ID_AGENT_ADMIN))
                                                {
                                %>
                                <option value="<%=String.valueOf(rssGroup[0][i].ID)%>" <%= rssGroup[0][i].ID == rs[0][0].ROLE_ID ? "selected" : ""%>><%=rssGroup[0][i].REMARK%></option>
                                <%
                                            }
                                        }
                                    }
                                } else {
                                    for (int i = 0; i < rssGroup[0].length; i++) {
                                        if(rssGroup[0][i].CA_ENABLED == false)
                                        {
                                            if (String.valueOf(rssGroup[0][i].ID).equals(Definitions.CONFIG_ROLE_ID_AGENT_ADMIN))
                                            {
                                %>
                                <option value="<%=String.valueOf(rssGroup[0][i].ID)%>" <%= rssGroup[0][i].ID == rs[0][0].ROLE_ID ? "selected" : ""%>><%=rssGroup[0][i].REMARK%></option>
                                <%
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                %>
                            </select>
                            <script>
                                function onChangeFunction(idRole, idLanguage, idCSRF)
                                {
                                    if(idRole === JS_STR_ROLE_ADMIN_CA || idRole === JS_STR_ROLE_ADMIN_SUPER_CA)
                                    {
                                        $("#idViewTokenSSL").css("display", "none");
                                    } else {
                                        $("#idViewTokenSSL").css("display", "none");
                                    }
                                    $('body').append('<div id="over"></div>');
                                    $(".loading-gif").show();
                                    $.ajax({
                                        type: "post",
                                        url: "../JSONCommon",
                                        data: {
                                            idParam: 'loadfunctions_forrole',
                                            idRole: idRole,
                                            idLanguage: idLanguage,
                                            CsrfToken: idCSRF
                                        },
                                        cache: false,
                                        success: function (html)
                                        {
                                            if (html.length > 0)
                                            {
                                                var obj = JSON.parse(html);
                                                if (obj[0].Code === "0")
                                                {
                                                    $("#idTBodyRoleToken").empty();
                                                    $("#idTBodyRoleCert").empty();
                                                    $("#idTBodyRoleAnother").empty();
                                                    var vStringViewCheckbox = "";
                                                    var contentToken = "";
                                                    var contentCert = "";
                                                    var contentAnother = "";
                                                    var indexToken = 1;
                                                    var indexCert = 1;
                                                    var indexAnother = 1;
                                                    var certAccessEnabled = '<%= certAccessEnabled%>';
                                                    var userPermissionEnable = "";
                                                    if('<%=sUserPermissionEnable%>' === "0"){
                                                        userPermissionEnable = "disabled";
                                                    }
                                                    for (var i = 0; i < obj.length; i++) {
                                                        if(obj[i].attributeType === JS_STR_ROLE_ATTRIBUTE_TYPE_TOKEN)
                                                        {
                                                            var idCheckBox = obj[i].attributeTypeChilrent.replace('/','') + '_' + obj[i].name;
                                                            var sActiveHTML = "<label class='switch' for='"+idCheckBox+"'><input TYPE='checkbox' class='js-switch' "+userPermissionEnable+" data-switchery='true' checked id='"+idCheckBox+"' onchange=\"SetActiveFunction('"+obj[i].name+"', '"+obj[i].attributeTypeChilrent+"', '"+obj[i].attributeType+"', '<%= anticsrf%>');\" /><div class='slider round "+userPermissionEnable+"'></div></label>";
                                                            if(obj[i].enabled === "0")
                                                            {
                                                                sActiveHTML = "<label class='switch' for='"+idCheckBox+"'><input TYPE='checkbox' class='js-switch' "+userPermissionEnable+" data-switchery='true' id='"+idCheckBox+"' onchange=\"SetActiveFunction('"+obj[i].name+"', '"+obj[i].attributeTypeChilrent+"', '"+obj[i].attributeType+"', '<%= anticsrf%>');\" /><div class='slider round "+userPermissionEnable+"'></div></label>";
                                                            }
                                                            contentToken += "<tr>" +
                                                                    "<td>" + indexToken + "</td>" +
                                                                    "<td>" + obj[i].name + "</td>" +
                                                                    "<td>" + obj[i].remark + "</td>" +
                                                                    "<td>" + sActiveHTML + "</td>" +
                                                                    "</tr>";
                                                            indexToken++;
                                                            vStringViewCheckbox = vStringViewCheckbox + "@@@" + idCheckBox ;
                                                        }
                                                        if(obj[i].attributeType === JS_STR_ROLE_ATTRIBUTE_TYPE_CERT)
                                                        {
                                                            var idCheckBox = obj[i].attributeTypeChilrent.replace('/','') + '_' + obj[i].name;
                                                            var sActiveHTML = "<label class='switch' for='"+idCheckBox+"'><input TYPE='checkbox' "+userPermissionEnable+" class='js-switch' data-switchery='true' checked id='"+idCheckBox+"' onchange=\"SetActiveFunction('"+obj[i].name+"', '"+obj[i].attributeTypeChilrent+"', '"+obj[i].attributeType+"', '<%= anticsrf%>');\" /><div class='slider round "+userPermissionEnable+"'></div></label>";
                                                            if(obj[i].enabled === "0")
                                                            {
                                                                sActiveHTML = "<label class='switch' for='"+idCheckBox+"'><input TYPE='checkbox' "+userPermissionEnable+" class='js-switch' data-switchery='true' id='"+idCheckBox+"' onchange=\"SetActiveFunction('"+obj[i].name+"', '"+obj[i].attributeTypeChilrent+"', '"+obj[i].attributeType+"', '<%= anticsrf%>');\" /><div class='slider round "+userPermissionEnable+"'></div></label>";
                                                            }
                                                            contentCert += "<tr>" +
                                                                    "<td>" + indexToken + "</td>" +
                                                                    "<td>" + obj[i].name + "</td>" +
                                                                    "<td>" + obj[i].remark + "</td>" +
                                                                    "<td>" + sActiveHTML + "</td>" +
                                                                    "</tr>";
                                                            indexCert++;
                                                            vStringViewCheckbox = vStringViewCheckbox + "@@@" + idCheckBox;
                                                        }
                                                        if(obj[i].attributeType === JS_STR_ROLE_ATTRIBUTE_TYPE_ANOTHER)
                                                        {
                                                            var idCheckBox = obj[i].attributeTypeChilrent.replace('/','') + '_' + obj[i].name;
                                                            var sActiveHTML = "<label class='switch' for='"+idCheckBox+"'><input TYPE='checkbox' "+userPermissionEnable+" class='js-switch' data-switchery='true' checked id='"+idCheckBox+"' onchange=\"SetActiveFunction('"+obj[i].name+"', '"+obj[i].attributeTypeChilrent+"', '"+obj[i].attributeType+"', '<%= anticsrf%>');\" /><div class='slider round "+userPermissionEnable+"'></div></label>";
                                                            if(obj[i].enabled === "0")
                                                            {
                                                                sActiveHTML = "<label class='switch' for='"+idCheckBox+"'><input TYPE='checkbox' "+userPermissionEnable+" class='js-switch' data-switchery='true' id='"+idCheckBox+"' onchange=\"SetActiveFunction('"+obj[i].name+"', '"+obj[i].attributeTypeChilrent+"', '"+obj[i].attributeType+"', '<%= anticsrf%>');\" /><div class='slider round "+userPermissionEnable+"'></div></label>";
                                                            }
                                                            contentAnother += "<tr>" +
                                                                    "<td>" + indexToken + "</td>" +
                                                                    "<td>" + obj[i].name + "</td>" +
                                                                    "<td>" + obj[i].remark + "</td>" +
                                                                    "<td>" + sActiveHTML + "</td>" +
                                                                    "</tr>";
                                                            indexAnother++;
                                                            vStringViewCheckbox = vStringViewCheckbox + "@@@" + idCheckBox;
                                                        }
                                                    }
                                                    if(contentToken !== "")
                                                    {
                                                        $("#idTBodyRoleToken").append(contentToken);
                                                        $("#idViewRoleToken").css("display", "");
                                                    } else{
                                                        $("#idViewRoleToken").css("display", "none");
                                                    }
                                                    if(contentCert !== "")
                                                    {
                                                        $("#idTBodyRoleCert").append(contentCert);
                                                        $("#idViewRoleCert").css("display", "");
                                                        if(certAccessEnabled === "0") {
                                                            $("#idViewRoleCert").css("display", "none");
                                                        } else {
                                                            $("#idViewRoleCert").css("display", "");
                                                        }
                                                    } else{
                                                        $("#idViewRoleCert").css("display", "none");
                                                    }
                                                    if(contentAnother !== "")
                                                    {
                                                        $("#idTBodyRoleAnother").append(contentAnother);
                                                        $("#idViewRoleAnother").css("display", "");
                                                    } else{
                                                        $("#idViewRoleAnother").css("display", "none");
                                                    }
                                                    if(contentToken === "" && contentCert === "" && contentAnother === "")
                                                    {
                                                        $("#idViewRoleFunction").css("display", "none");
                                                    } else
                                                    {
                                                        $("#idViewRoleFunction").css("display", "");
                                                    }
                                                    if(vStringViewCheckbox !== "")
                                                    {
                                                        //ViewCheckBox(vStringViewCheckbox);
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
                                                else if (obj[0].Code === "1")
                                                {
                                                    $("#idTBodyRoleToken").empty();
                                                    $("#idViewRoleToken").css("display", "none");
                                                    $("#idTBodyRoleCert").empty();
                                                    $("#idViewRoleCert").css("display", "none");
                                                    $("#idTBodyRoleAnother").empty();
                                                    $("#idViewRoleAnother").css("display", "none");
                                                    $("#idViewRoleFunction").css("display", "none");
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
                                }
                                function ViewCheckBox(vvViewCheckbox)
                                {
                                    var sStringTemp = "";
                                    var vItemViewCheckbox = vvViewCheckbox.split('@@@');
                                    for (var n = 1; n < vItemViewCheckbox.length; n++) {
                                        sStringTemp = sStringTemp + "setTimeout(function() {$('#"+vItemViewCheckbox[n]+"').bootstrapSwitch('setState', true);}, 500);";
                                    }
                                    eval(sStringTemp);
                                }
                                function SetActiveFunction(name, type_chilrent, type, idCSRF)
                                {
                                    var strActive = "0";
                                    if ($("#"+type_chilrent.replace('/','') +'_'+name).is(':checked')) {
                                        strActive = "1";
                                    }
                                    $.ajax({
                                        type: "post",
                                        url: "../SomeCommon",
                                        data: {
                                            idParam: 'setactiverolefunction',
                                            name: name,
                                            attributeType: type_chilrent,
                                            enabled: strActive,
                                            type: type,
                                            CsrfToken: idCSRF
                                        },
                                        cache: false,
                                        success: function (html)
                                        {
                                            var myStrings = sSpace(html).split('#');
                                            if (myStrings[0] === "0")
                                            {
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
                                        }
                                    });
                                    return false;
                                }
                            </script>
                        </div>
                    </div>
                </div>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitleStatus" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <select name="USER_STATE" class="form-control123" <%= sTemStateCancel%> <%= sDisableAgent%> <%=sDisActiveSuperCA%>>
                                <%
                                    USER_STATE[][] rsState = new USER_STATE[1][];
                                    db.S_BO_USER_STATE_COMBOBOX(sessLanguageGlobal, rsState);
                                    if (rsState[0].length > 0) {
                                        for (USER_STATE temp1 : rsState[0]) {
                                            if(temp1.ID != Definitions.CONFIG_USER_STATE_CANCEL_ID)
                                            {
                                %>
                                <option value="<%=String.valueOf(temp1.ID)%>" <%= temp1.ID == rs[0][0].USER_STATE_ID ? "selected" : ""%>><%=temp1.REMARK %></option>
                                <%
                                            }
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
                            <label id="idLblTitleEmail"></label>
                            <label class="CssRequireField" id="idLblNoteEmail"></label>
                        </label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input type="text" id="Email" name="Email" <%= sTemStateCancel%> maxlength="128" value="<%= EscapeUtils.CheckTextNull(rs[0][0].EMAIL)%>" class="form-control123"/>
                        </div>
                    </div>
                </div>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                            <label id="idLblTitlePhone"></label>
                            <label class="CssRequireField" id="idLblNotePhone"></label>
                        </label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input type="text" <%= sTemStateCancel%> id="MobileNumber" name="MobileNumber" maxlength="16" value="<%= rs[0][0].MSISDN %>"
                                class="form-control123" onblur="autoTrimTextField('MobileNumber', this.value);">
                        </div>
                    </div>
                </div>
                <%
                    String sViewTokenSSL = "none";
                    String sViewTokenCert = "none";
                    String sViewAShow = "";
                    String sViewAHide = "none";
                    Object strSubjectDN = "";
                    Object strIssuerDN="";
                    String strNotBefore="";
                    String strNotAfter="";
                    if(String.valueOf(rs[0][0].ROLE_ID).equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN)
                        || String.valueOf(rs[0][0].ROLE_ID).equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN_CHILD))
                    {
                        // open none -> "" make to SSL show
                        sViewTokenSSL = "none";
                        if(!"".equals(sCERTIFICATION_HASH) && !"".equals(sCERTIFICATION))
                        {
                            sViewAShow = "none";
                            sViewAHide = "";
                            int[] intRes = new int[1];
                            Object[] sss = new Object[2];
                            String[] tmp = new String[3];
                            com.VoidCertificateComponents(sCERTIFICATION, sss, tmp, intRes);
                            if (intRes[0] == 0 && sss.length > 0) {
                                sViewTokenCert = "";
                                strSubjectDN = sss[0].toString().replace(", ", "\n");
                                strIssuerDN = sss[1].toString().replace(", ", "\n");
                                strNotBefore = EscapeUtils.CheckTextNull(tmp[1]);
                                strNotAfter = EscapeUtils.CheckTextNull(tmp[2]);
                            }
                        }
                    }
                %>
                <div id="idViewTokenSSL" class="form-group" style="display: <%= sViewTokenSSL%>; padding-top: 10px;">
                    <label class="control-label123" id="idLblTitleSSL"></label>
                    <br/>
                    <a id="idAShow" style="cursor: pointer; color: blue; text-decoration: underline;display: <%= sViewAShow%>" onclick="addTokenSSL();" id="idLblTitleChooseCert"></a>
                    <a id="idAHide" style="cursor: pointer; color: blue; text-decoration: underline;display: <%= sViewAHide%>;" onclick="deleteTokenSSL('<%= anticsrf%>');" id="idLblTitleUnchooseCert"></a>
                    <div id="idViewTokenCert" class="form-group" style="display: <%= sViewTokenCert%>;padding: 10px 0px 0 0px;margin: 0;">
                        <fieldset class="scheduler-border">
                            <legend class="scheduler-border" id="idLblTitleGroupCert"></legend>
                            <div class="form-group" style="padding: 0px 0px 0 0px;margin: 0;">
                                <label class="control-label123" id="idLblTitleCompany"></label>
                                <textarea id="idCertCompany" class="form-control123" readonly="true" name="idCertCompany" style="height: 120px;"><%= strSubjectDN%></textarea>
                            </div>
                            <div class="form-group" style="padding: 10px 0px 0 0px;margin: 0;">
                                <label class="control-label123" id="idLblTitleIssue"></label>
                                <textarea id="idCertIssuer" class="form-control123" readonly="true" name="idCertIssuer" style="height: 120px;"><%= strIssuerDN%></textarea>
                            </div>
                            <div class="form-group" style="padding: 10px 0px 0 0px;margin: 0;">
                                <label class="control-label123" id="idLblTitleValid"></label>
                                <input id="idCertValid" name="idCertValid" value="<%= strNotBefore%>" class="form-control123" type="text" readonly="true"/>
                            </div>
                            <div class="form-group" style="padding: 10px 0px 0 0px;margin: 0;">
                                <label class="control-label123" id="idLblTitleExpire"></label>
                                <input id="idCertExpire" name="idCertExpire" value="<%= strNotAfter%>" class="form-control123" type="text" readonly="true" />
                            </div>
                        </fieldset>
                    </div>
                    <!-- Action SSL Script -->
                    <script>
                        function addTokenSSL()
                        {
                            $.ajax({
                                type: "GET",
                                url: "../GetCertSSL",
                                cache: false,
                                success: function (htmlSSL) 
                                {
                                   var vResult = sSpace(htmlSSL).split('#');
                                   if(vResult[0] === "0")
                                   {
                                       $('body').append('<div id="over"></div>');
                                       $(".loading-gif").show();
                                       $.ajax({
                                            type: "post",
                                            url: "../JSONCommon",
                                            data: {
                                                idParam: 'sslparsecert'
                                            },
                                            cache: false,
                                            success: function (html)
                                            {
                                                if (html.length > 0)
                                                {
                                                    $("#idViewTokenCert").css("display", "none");
                                                    $("#idAHide").css("display", "none");
                                                    $("#idAShow").css("display", "");
                                                    $("#idCertCompany").val('');
                                                    $("#idCertIssuer").val('');
                                                    $("#idCertValid").val('');
                                                    $("#idCertExpire").val('');
                                                    var obj = JSON.parse(html);
                                                    if (obj[0].Code === "0")
                                                    {
                                                        $("#idViewTokenCert").css("display", "");
                                                        $("#idAHide").css("display", "");
                                                        $("#idAShow").css("display", "none");
                                                        $("#idCertCompany").val(obj[0].SUBJECT);
                                                        $("#idCertIssuer").val(obj[0].ISSUER);
                                                        $("#idCertValid").val(obj[0].DATE_VALID);
                                                        $("#idCertExpire").val(obj[0].DATE_EXPIRE);
                                                    }
                                                    else if (obj[0].Code === "1")
                                                    {
                                                        funErrorAlert(ca_req_info_cert);
                                                    }
                                                    else if (obj[0].Code === JS_EX_LOGIN)
                                                    {
                                                        RedirectPageLoginNoSess(global_alert_login);
                                                    }
                                                    else if (obj[0].Code === JS_EX_ANOTHERLOGIN)
                                                    {
                                                        RedirectPageLoginNoSess(global_alert_another_login);
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
                                        return false;
                                    } else if(vResult[0] === JS_EX_NO_CERTCHAN) {
                                        funErrorAlert(global_error_chain_cert);
                                    } else if(vResult[0] === JS_EX_ERRORCTS) {
                                        funErrorAlert(global_error_cert_compare_ca);
                                    } else {
                                       funErrorAlert(global_errorsql);
                                    }
                                },
                                timeout: 100000
                            });
                            return false;
                        }
                        function deleteTokenSSL(idCSRF)
                        {
                            swal({
                                title: "",
                                text: global_ssl_conform_delete,
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
                                        url: "../UserCommon",
                                        data: {
                                            idParam: 'deletetokenssl',
                                            CsrfToken: idCSRF
                                        },
                                        cache: false,
                                        success: function (html) {
                                            var arr = sSpace(html).split('#');
                                            if (arr[0] === "0")
                                            {
                                                $("#idViewTokenCert").css("display", "none");
                                                $("#idAHide").css("display", "none");
                                                $("#idAShow").css("display", "");
                                                $("#idCertCompany").val('');
                                                $("#idCertIssuer").val('');
                                                $("#idCertValid").val('');
                                                $("#idCertExpire").val('');
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
                </div>

                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitleActive" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <label class="switch" for="ActiveFlag">
                                <input TYPE="checkbox" id="ActiveFlag" name="ActiveFlag"
                                    <%=rs[0][0].ENABLED ? "checked='checked'" : ""%> <%= sTemStateCancel%> <%= sDisableAgent%> <%=sDisActiveSuperCA%>/>
                                <div class="slider round"></div>
                            </label>
                        </div>
                    </div>
                </div>
                <%
                    String sViewGroupRole = "";
                    if("".equals(EscapeUtils.CheckTextNull(rs[0][0].PROPERTIES)))
                    {
                        sViewGroupRole = "none;";
                    }
                %>
                <!--<div style="width: 100%; height: 10px; display: <= sViewGroupRole%>"></div>-->
                <div class="col-sm-13" style="padding-left: 0;clear: both;">
                    <fieldset class="scheduler-border" id="idViewRoleFunction" style="display: <%= sViewGroupRole%>">
                        <legend class="scheduler-border" id="idLblTitleRoleSet"></legend>
                        <style type="text/css">
                            .table > thead > tr > th, .table > tbody > tr > td{vertical-align: middle;}
                            .table > thead > tr > th{border-bottom: none;}
                            .btn{margin-bottom: 0px;}
                            .panel_toolbox { min-width: 0;}
                        </style>
                        <%
                            ROLE_DATA[][] rsToken = new ROLE_DATA[1][];
                            CommonFunction.LoadRoleToken(sJSON_FUNCTION, rsToken);
                            String sDisableToken = "none";
                            if(rsToken[0] != null)
                            {
                                if (rsToken[0].length > 0) {
                                    sDisableToken = "";
                                }
                            }
                        %>
                        <div class="table-responsive" id="idViewRoleToken" style="display: <%= sDisableToken%>">
                            <div class="x_title" style="border-bottom: 0 solid #E6E9ED;margin-bottom: 0px;">
                                <h2><i class="fa fa-list-ul"></i> <span id="idLblTitleRoleSetToken" style="color: #36526D;"></span></h2>
                                <ul class="nav navbar-right panel_toolbox"></ul>
                                <div class="clearfix"></div>
                            </div>
                            <div class="x_content">
                                <div class="table-responsive">
                                    <table id="idTableListToken" class="table table-bordered table-striped projects">
                                        <thead>
                                        <th style="width: 50px;" id="idLblTitleTableTokenSST"></th>
                                        <th style="width: 350px;" id="idLblTitleTableTokenName"></th>
                                        <th style="width: 350px;" id="idLblTitleTableTokenDes"></th>
                                        <th id="idLblTitleTableTokenActive"></th>
                                        </thead>
                                        <tbody id="idTBodyRoleToken">
                                            <%
                                                int j=1;
                                                if(rsToken[0] != null)
                                                {
                                                    if (rsToken[0].length > 0) {
                                                for (ROLE_DATA rsRole1 : rsToken[0]) {
                                                    String sEnabled = "1";
                                                    if(rsRole1.enabled == false)
                                                    {
                                                        sEnabled = "0";
                                                    }
                                                    cartToken.AddRoleFunctionsList(rsRole1);
                                            %>
                                            <tr>
                                                <td><%= com.convertMoney(j)%></td>
                                                <td><%= rsRole1.name%></td>
                                                <%
                                                    if("1".equals(sessLanguageGlobal))
                                                    {
                                                %>
                                                <td><%= rsRole1.remark%></td>
                                                <td style="display: none;"><%= rsRole1.remarkEn%></td>
                                                <%
                                                    } else {
                                                %>
                                                <td style="display: none;"><%= rsRole1.remark%></td>
                                                <td><%= rsRole1.remarkEn %></td>
                                                <%
                                                    }
                                                %>
                                                <td>
                                                    <div id="idCheckTableTokenCert<%=j%>"></div>
                                                    <script>
                                                        var sRequiredHTML = "<label class='switch' for='<%= rsRole1.attributeType.replace("/", "") + '_' + rsRole1.name%>'><input TYPE='checkbox' <%= sIsRoleAdminFunc%> <%= permissionDisbled%> checked id='<%= rsRole1.attributeType.replace("/", "") + '_' + rsRole1.name%>' onchange=\"SetActiveFunction('<%= rsRole1.name%>', '<%= rsRole1.attributeType%>', '<%= Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_TOKEN%>', '<%= anticsrf%>');\" /><div class='slider round <%= permissionDisbled%>'></div></label>";
                                                        if('<%= sEnabled%>' === "0")
                                                        {
                                                            sRequiredHTML = "<label class='switch' for='<%= rsRole1.attributeType.replace("/", "") + '_' + rsRole1.name%>'><input TYPE='checkbox' <%= sIsRoleAdminFunc%> <%= permissionDisbled%> id='<%= rsRole1.attributeType.replace("/", "") + '_' + rsRole1.name%>' onchange=\"SetActiveFunction('<%= rsRole1.name%>', '<%= rsRole1.attributeType%>', '<%= Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_TOKEN %>', '<%= anticsrf%>');\" /><div class='slider round <%= permissionDisbled%>'></div></label>";
                                                        }
                                                        $("#idCheckTableTokenCert"+'<%=j%>').append(sRequiredHTML);
                                                    </script>
                                                </td>
                                            </tr>
                                            <%
                                                j++;
                                                }
                                                session.setAttribute("sessRoleFunctionsToken", cartToken);
                                                    }
                                                }
                                            %>
                                        </tbody>
                                    </table>
                                    </div>
                            </div>
                        </div>
                        <%
                            ROLE_DATA[][] rsCert = new ROLE_DATA[1][];
                            CommonFunction.LoadRoleCertificate(sJSON_FUNCTION, rsCert);
                            String sDisableCert = "none";
                            if("1".equals(certAccessEnabled)) {
                                if(rsCert[0] != null) {
                                    if (rsCert[0].length > 0) {
                                        sDisableCert = "";
                                    }
                                }
                            }
                        %>
                        <div class="table-responsive" id="idViewRoleCert" style="display: <%= sDisableCert%>">
                            <div class="x_title" style="border-bottom: 0 solid #E6E9ED;margin-bottom: 0px;">
                                <h2><i class="fa fa-list-ul"></i> <span id="idLblTitleRoleSetCert" style="color: #36526D;"></span></h2>
                                <ul class="nav navbar-right panel_toolbox"></ul>
                                <div class="clearfix"></div>
                            </div>
                            <div class="x_content">
                                <div class="table-responsive">
                                <table id="idTableListCert" class="table table-bordered table-striped projects">
                                    <thead>
                                    <th style="width: 50px;" id="idLblTitleTableCertSST"></th>
                                    <th style="width: 350px;" id="idLblTitleTableCertName"></th>
                                    <th style="width: 350px;" id="idLblTitleTableCertDes"></th>
                                    <th id="idLblTitleTableCertActive"></th>
                                    </thead>
                                    <tbody id="idTBodyRoleCert">
                                        <%
                                            int n=1;
                                            if(rsCert[0] != null)
                                            {
                                                if (rsCert[0].length > 0) {
                                                    ROLE_DATA[][] rsCertLast = new ROLE_DATA[1][];
                                                    CommonFunction.checkAddPermissionUser(Definitions.CONFIG_ROLE_PROPERTIES_CERT_BUY_MORE,
                                                        Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT,
                                                        rsCert, rsCertLast);
                                                    for (ROLE_DATA rsRole1 : rsCertLast[0]) {
                                                        String disableRolePreApprove = "";
                                                        String sEnabled = "1";
                                                        if(rsRole1.enabled == false)
                                                        {
                                                            sEnabled = "0";
                                                        }
                                                        if("1".equals(roleChilrentAgent)) {
                                                            String sName = rsRole1.name;
                                                            String sType = rsRole1.attributeType;
                                                            if(sName.equals(Definitions.CONFIG_ROLE_PROPERTIES_CERT_PRE_APPROVED)
                                                                && sType.equals(Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST))
                                                            {
                                                                disableRolePreApprove = "disabled";
                                                            }
                                                        }
                                                        cartCert.AddRoleFunctionsList(rsRole1);
                                        %>
                                        <tr>
                                            <td><%= com.convertMoney(n)%></td>
                                            <td><%= rsRole1.name%></td>
                                            <%
                                                if("1".equals(sessLanguageGlobal))
                                                {
                                            %>
                                            <td><%= rsRole1.remark%></td>
                                            <td style="display: none;"><%= rsRole1.remarkEn%></td>
                                            <%
                                                } else {
                                            %>
                                            <td style="display: none;"><%= rsRole1.remark%></td>
                                            <td><%= rsRole1.remarkEn %></td>
                                            <%
                                                }
                                            %>
                                            <td>
                                                <div id="idCheckTableCert<%=n%>"></div>
                                                <script>
                                                    var sRequiredHTML = "<label class='switch' for='<%= rsRole1.attributeType.replace("/", "") + '_' + rsRole1.name%>'><input TYPE='checkbox' class='js-switch' <%= sIsRoleAdminFunc%> <%= permissionDisbled%> <%= disableRolePreApprove%> checked id='<%= rsRole1.attributeType.replace("/", "") + '_' + rsRole1.name%>' onchange=\"SetActiveFunction('<%= rsRole1.name%>', '<%= rsRole1.attributeType%>', '<%= Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT %>', '<%= anticsrf%>');\" /><div class='slider round <%= permissionDisbled%>'></div></label>";
                                                    if('<%= sEnabled%>' === "0")
                                                    {
                                                        sRequiredHTML = "<label class='switch' for='<%= rsRole1.attributeType.replace("/", "") + '_' + rsRole1.name%>'><input TYPE='checkbox' class='js-switch' <%= sIsRoleAdminFunc%> <%= permissionDisbled%> <%= disableRolePreApprove%> id='<%= rsRole1.attributeType.replace("/", "") + '_' + rsRole1.name%>' onchange=\"SetActiveFunction('<%= rsRole1.name%>', '<%= rsRole1.attributeType%>', '<%= Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT %>', '<%= anticsrf%>');\" /><div class='slider round <%= permissionDisbled%>'></div></label>";
                                                    }
                                                    $("#idCheckTableCert"+'<%=n%>').append(sRequiredHTML);
                                                </script>
                                            </td>
                                        </tr>
                                        <%
                                                n++;
                                                }
                                                session.setAttribute("sessRoleFunctionsCert", cartCert);
                                                }
                                            }
                                        %>
                                    </tbody>
                                </table>
                                </div>
                            </div>
                        </div>
                        <%
                            ROLE_DATA[][] rsAnother = new ROLE_DATA[1][];
                            CommonFunction.LoadRoleAnother(sJSON_FUNCTION, rsAnother);
                            String sDisableAnother = "none";
                            if(rsAnother[0] != null)
                            {
                                if (rsAnother[0].length > 0) {
                                    sDisableAnother = "";
                                }
                            }
                        %>
                        <div class="table-responsive" id="idViewRoleAnother" style="display: <%= sDisableAnother%>">
                            <div class="x_title" style="border-bottom: 0 solid #E6E9ED;margin-bottom: 0px;">
                                <h2><i class="fa fa-list-ul"></i> <span id="idLblTitleRoleSetOther" style="color: #36526D;"></span></h2>
                                <ul class="nav navbar-right panel_toolbox"></ul>
                                <div class="clearfix"></div>
                            </div>
                            <div class="x_content">
                                <div class="table-responsive">
                                <table id="idTableListAnother" class="table table-bordered table-striped projects">
                                    <thead>
                                    <th style="width: 50px;" id="idLblTitleTableOtherSST"></th>
                                    <th style="width: 350px;" id="idLblTitleTableOtherName"></th>
                                    <th style="width: 350px;" id="idLblTitleTableOtherDes"></th>
                                    <th id="idLblTitleTableOtherActive"></th>
                                    </thead>
                                    <tbody id="idTBodyRoleAnother">
                                        <%
                                            int m=1;
                                            if(rsAnother[0] != null)
                                            {
                                                if (rsAnother[0].length > 0) {
                                                    for (ROLE_DATA rsRole1 : rsAnother[0]) {
                                                        String sEnabled = "1";
                                                        if(rsRole1.enabled == false)
                                                        {
                                                            sEnabled = "0";
                                                        }
                                                        cartAnother.AddRoleFunctionsList(rsRole1);
                                        %>
                                        <tr>
                                            <td><%= com.convertMoney(m)%></td>
                                            <td><%= rsRole1.name%></td>
                                            <%
                                                if("1".equals(sessLanguageGlobal))
                                                {
                                            %>
                                            <td><%= rsRole1.remark%></td>
                                            <td style="display: none;"><%= rsRole1.remarkEn%></td>
                                            <%
                                                } else {
                                            %>
                                            <td style="display: none;"><%= rsRole1.remark%></td>
                                            <td><%= rsRole1.remarkEn %></td>
                                            <%
                                                }
                                            %>
                                            <td>
                                                <div id="idCheckTableOtherCert<%=m%>"></div>
                                                <script>
                                                    var sRequiredHTML = "<label class='switch' for='<%= rsRole1.attributeType.replace("/", "") + '_' + rsRole1.name%>'><input TYPE='checkbox' class='js-switch' <%= sIsRoleAdminFunc%> <%= permissionDisbled%> checked id='<%= rsRole1.attributeType.replace("/", "") + '_' + rsRole1.name%>' onchange=\"SetActiveFunction('<%= rsRole1.name%>', '<%= rsRole1.attributeType%>', '<%= Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_ANOTHER %>', '<%= anticsrf%>');\" /><div class='slider round <%= permissionDisbled%>'></div></label>";
                                                    if('<%= sEnabled%>' === "0")
                                                    {
                                                        sRequiredHTML = "<label class='switch' for='<%= rsRole1.attributeType.replace("/", "") + '_' + rsRole1.name%>'><input TYPE='checkbox' class='js-switch' <%= sIsRoleAdminFunc%> <%= permissionDisbled%> id='<%= rsRole1.attributeType.replace("/", "") + '_' + rsRole1.name%>' onchange=\"SetActiveFunction('<%= rsRole1.name%>', '<%= rsRole1.attributeType%>', '<%= Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_ANOTHER %>', '<%= anticsrf%>');\" /><div class='slider round <%= permissionDisbled%>'></div></label>";
                                                    }
//                                                        document.write(sRequiredHTML);
//                                                        $("#idCheckTableCert").text(sRequiredHTML);
                                                    $("#idCheckTableOtherCert"+'<%=m%>').append(sRequiredHTML);
                                                </script>
                                            </td>
                                        </tr>
                                        <%
                                                    m++;
                                                    }
                                                    session.setAttribute("sessRoleFunctionsAnother", cartAnother);
                                                }
                                            }
                                        %>
                                    </tbody>
                                </table>
                                </div>
                            </div>
                        </div>
                    </fieldset>
                </div>
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
                            <input class="form-control123" type="text" name="doDate" readonly value="<%= rs[0][0].CREATED_DT%>"/>
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
                            <input class="form-control123" type="text" name="doDate" readonly value="<%= EscapeUtils.CheckTextNull(rs[0][0].MODIFIED_DT)%>"/>
                        </div>
                    </div>
                </div>                                        
            </form>
        </div>
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
                            <h3 class="modal-title" style="font-size: 18px;"> <span id="idLblTitleDelete"></span></h3>
                        </div>
                        <div style="width: 29%; float: right;text-align: right;">
                            <input type="button" id="btnOK" data-switch-get="state" class="btn btn-info" onclick="AfterDeleteUser('1', '<%=anticsrf%>');" />
                            
                            <!--<button type="button" id="idCloseDialog" onclick="CloseDialog();" class="btn btn-info"></button>-->
                            <input type="button" id="idCloseDialog" onclick="CloseDialog();" class="btn btn-info" />
                            <script>
                                document.getElementById("btnOK").value = login_fm_buton_OK;
                                document.getElementById("idCloseDialog").value = global_fm_button_close;
                            </script>
                        </div>
                    </div>
                    <div class="modal-body">
                        <form role="formAddOTPHardware" id="formOTPHardware">
                            <div class="form-group" style="padding: 10px 0px 0 0px;margin: 0;">
                                <label class="control-label123" style="color: blue;" id="idLblTitleDeleteNote">
                                </label>
                            </div>
                            <%
                                String sBranchFirst = "";
                                String SessAgentID = session.getAttribute("SessAgentID").toString().trim();
                                String SessUserAgentID = session.getAttribute("SessUserAgentID").toString().trim();
                            %>
                            <%
                                if(SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                            %>
                            <div class="form-group" style="padding: 10px 0px 0 0px;margin: 0;">
                                <label class="control-label123" id="idLblTitleDeleteBranch"></label>
                                <select name="AGENT_NAME" id="AGENT_NAME" class="form-control123"
                                    onchange="LOAD_BACKOFFICE_USER(this.value, '<%= ids%>', '<%= anticsrf%>');">
                                    <%
                                        BRANCH[][] rst = new BRANCH[1][];
                                        try {
                                            db.S_BO_BRANCH_COMBOBOX(sessLanguageGlobal, rst);
                                            if (rst[0].length > 0) {
                                                for (BRANCH temp1 : rst[0]) {
                                                    if(!String.valueOf(temp1.PARENT_ID).equals(Definitions.CONFIG_AGENT_ROOT))
                                                    {
                                                        String sSelected = "";
                                                        if("".equals(sBranchFirst)) {
                                                            sBranchFirst = String.valueOf(temp1.ID);
                                                        }
                                                        if(temp1.ID == sBranchID_User)
                                                        {
                                                            sSelected = "selected";
                                                            sBranchFirst = String.valueOf(sBranchID_User);
                                                        }
                                    %>
                                    <option value="<%=String.valueOf(temp1.ID)%>" <%= sSelected%>><%=temp1.NAME + " - " + temp1.REMARK%></option>
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
                            <script>
                                function LOAD_BACKOFFICE_USER(objAgency, objUserLogin, idCSRF)
                                {
                                    $.ajax({
                                        type: "post",
                                        url: "../JSONCommon",
                                        data: {
                                            idParam: 'loaduser_ofagency',
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
                                                    for (var i = 0; i < obj.length; i++) {
                                                        if(obj[i].ID !== objUserLogin) {
                                                            cbxUSER.options[cbxUSER.options.length] = new Option(obj[i].FULL_NAME + " (" + obj[i].USERNAME + ")", obj[i].ID);
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
                                                    cbxUSER.options[cbxUSER.options.length] = new Option("", "");
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
                                } else {
                                    sBranchFirst = SessUserAgentID;
                            %>
                            <input type="text" name="AGENT_NAME" id="AGENT_NAME" disabled style="display: none;" value="<%= EscapeUtils.CheckTextNull(SessUserAgentID) %>">
                            <%
                                }
                            %>
                            <div class="form-group" style="padding: 10px 0px 0 0px;margin: 0;">
                                <label class="control-label123" id="idLblTitleUserReceive"></label>
                                <select name="USER" id="USER" class="form-control123">
                                    <%
                                        BACKOFFICE_USER[][] rssUser = new BACKOFFICE_USER[1][];
                                        db.S_BO_GET_USER_BRANCH(sBranchFirst, rssUser);
                                        if (rssUser[0].length > 0) {
                                            for (int i = 0; i < rssUser[0].length; i++) {
                                                if(!String.valueOf(rssUser[0][i].ID).equals(ids))
                                                {
                                    %>
                                    <option value="<%=String.valueOf(rssUser[0][i].ID)%>"><%=rssUser[0][i].FULL_NAME + " (" + rssUser[0][i].USERNAME + ")" %></option>
                                    <%
                                                }
                                            }
                                        }
                                    %>
                                </select>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
        <script src="../style/jquery.min.js"></script>
        <script src="../style/bootstrap.min.js"></script>
        <!--<script src="../style/custom.min.js"></script>-->
        <link href="../js/checkphone/intlTelInput.css" rel="stylesheet" type="text/css"/>
        <script src="../js/checkphone/intlTelInput.js" type="text/javascript"></script>
        <!--<script src="../style/switchery/dist/switchery.min.js"></script>-->
<!--        <script src="../js/active/highlight.js"></script>
        <script src="../js/active/bootstrap-switch.js"></script>
        <script src="../js/active/main.js"></script>-->
    </body>
</html>