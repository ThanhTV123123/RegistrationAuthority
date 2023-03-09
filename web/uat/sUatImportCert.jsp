<%-- 
    Document   : sUatImportCert
    Created on : Mar 25, 2019, 3:23:17 PM
    Author     : THANH-PC
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="../Admin/ConnectionParam.jsp" %>
<%@include file="../Admin/CommonPagingList.jsp" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Certificate Import</title>
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
        <script language="javascript">
            changeFavicon("../");
            document.title = tokenimport_title_list;
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
                $('.loading-gif').hide();
            });
            function calUpload(idCSRF)
            {
                var input1 = document.getElementById('input-file');
                if (input1.value !== '')
                {
                    var checkFileName = input1.value.substring(input1.value.lastIndexOf('.') + 1);
                    if (checkFileName === "xls" || checkFileName === "xlsx" || checkFileName === "csv")
                    {
//                        $('body').append('<div id="over"></div>');
//                        $(".loading-gif").show();
                        $.ajax({
                            type: "post",
                            url: '../TokenCommon',
                            data: {
                                idParam: 'checkcsrf',
                                TOKEN_VERSION: document.formimport.TOKEN_VERSION.value,
                                BranchID: document.formimport.BranchID.value,
                                CsrfToken: idCSRF
                            },
                            cache: false,
                            success: function (html) {
                                var arr = sSpace(html).split('#');
                                if (arr[0] === "0")
                                {
                                    $('body').append('<div id="over"></div>');
                                    $(".loading-gif").show();
                                    file1 = input1.files[0];
                                    var data1 = new FormData();
                                    data1.append('file', file1);
                                    $.ajax({
                                        type: 'POST',
                                        url: '../TokenImport',
                                        data: data1,
                                        cache: false,
                                        contentType: false,
                                        processData: false,
                                        enctype: "multipart/form-data",
                                        success: function (html) {
                                            var arr = sSpace(html).split('###');
                                            if (arr[0] === "0")
                                            {
                                                var sAlert = token_succ_import + token_succ_import_insert + arr[1]
                                                        + token_succ_import_update + arr[3]
                                                        + token_succ_import_error + arr[2];
                                                funSuccAlert(sAlert, "TokenImport.jsp");
                                            } else if (arr[0] === JS_EX_LOGIN)
                                            {
                                                RedirectPageLoginNoSess(global_alert_login);
                                            } else if (arr[0] === JS_EX_ANOTHERLOGIN)
                                            {
                                                RedirectPageLoginNoSess(global_alert_another_login);
                                            } else
                                            {
                                                if (arr[1] === JS_EX_SPECIAL)
                                                {
                                                    funErrorAlert(global_error_file_special);
                                                } else if (arr[1] === JS_EX_CSV_NO_TOKENID)
                                                {
                                                    funErrorAlert(token_error_no_tokenid);
                                                } else if (arr[1] === JS_EX_CSV_NO_SOPIN)
                                                {
                                                    funErrorAlert(token_error_no_sopin);
                                                } else
                                                {
                                                    funErrorAlert(global_errorsql);
                                                }
                                            }
                                            $(".loading-gif").hide();
                                            $('#over').remove();
                                        }
                                    });
                                } else
                                {
                                    funCsrfAlert();
                                }
//                                $(".loading-gif").hide();
//                                $('#over').remove();
                            }
                        });
                    } else
                    {
                        funErrorAlert(token_error_import_format);
                    }
                } else
                {
                    funErrorAlert(global_req_file);
                }
            }
            
            function UploadCertificate(input1)
            {
                if (input1.value !== '')
                {
                    var checkFileName = input1.value.substring(input1.value.lastIndexOf('.') + 1);
                    if(checkFileName === "cer" || checkFileName === "txt" || checkFileName === "CER"
                        || checkFileName === "TXT" || checkFileName === "pem" || checkFileName === "PEM")
                    {
                        file1 = input1.files[0];
                        var data1 = new FormData();
                        data1.append('file', file1);
                        $.ajax({
                            url: "../UploadFile",
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
                                    $("textarea#idCer").val(myStrings[1]);
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
                    }
                    else
                    {
                        funErrorAlert(global_req_cer_format);
                    }
                }
                else
                {
                    funErrorAlert(global_req_file);
                }
            }
            
            function ValidateForm(idCSRF) {
                if (!JSCheckEmptyField($("#USER").val()))
                {
                    funErrorAlert(global_error_not_user_create);
                    return false;
                }
                if (!JSCheckEmptyField($("#PHONE_CONTRACT").val()))
                {
                    $("#PHONE_CONTRACT").focus();
                    funErrorAlert(policy_req_empty + global_fm_phone_contact);
                    return false;
                } else {
                    if (!JSCheckFormatPhoneNew_Edit($("#PHONE_CONTRACT")))
                    {
                        $("#PHONE_CONTRACT").focus();
                        funErrorAlert(global_req_phone_format);
                        return false;
                    }
                }
                if (!JSCheckEmptyField($("#EMAIL_CONTRACT").val()))
                {
                    $("#EMAIL_CONTRACT").focus();
                    funErrorAlert(policy_req_empty + global_fm_email_contact);
                    return false;
                } else {
                    if (!FormCheckEmailSearch($("#EMAIL_CONTRACT").val()))
                    {
                        $("#EMAIL_CONTRACT").focus();
                        funErrorAlert(global_req_mail_format);
                        return false;
                    }
                }
                $('body').append('<div id="over"></div>');
                $(".loading-gif").show();
                $.ajax({
                    type: "post",
                    url: "../RequestCommon",
                    data: {
                        idParam: 'importcertcoreca',
                        pCERTIFICATION_PURPOSE: $("#CERTIFICATION_PURPOSE").val(),
                        BRANCH_ID: $("#AGENT_NAME").val(),
                        sTypeRegister: JS_STR_CERTIFICATION_PURPOSE_CODE_TOKEN,
                        CertProfileID: $("#idHiddenCerDurationOrProfileID").val(),
                        PHONE_CONTRACT: $("#PHONE_CONTRACT").val(),
                        EMAIL_CONTRACT: $("#EMAIL_CONTRACT").val(),
                        CREATE_USER: $("#USER").val(),
                        TOKEN_SN: $("#TOKEN_SN").val(),
                        idCer: $("textarea#idCer").val()
                    },
                    cache: false,
                    success: function (html_first) {
                        var arr_first = sSpace(html_first).split('#');
                        if (arr_first[0] === "0")
                        {
                            
                        } else {
                            
                        }
                        $(".loading-gif").hide();
                        $('#over').remove();
                    }
                });
                return false;
            }
        </script>
        <style>.projects th{font-weight: bold;}</style>
    </head>
    <body class="nav-md">
        <%            //if ((session.getAttribute("sUserID")) != null) {
                String anticsrf = "" + Math.random();
                request.getSession().setAttribute("anticsrf", anticsrf);
//                String SessAgentID = session.getAttribute("SessAgentID").toString().trim();
        %>
<!--        <div style="width: 100%; text-align: center; position: fixed;z-index: 1000;top: 0; padding-top: 300px;
             left: 0; height: 100%;" class="loading-gif">
            <img src="../Images/ajax-loader1.gif" alt="Please wait..." />
        </div>-->
        <div class="container body">
            <div class="main_container">
                <div class="col-md-3 left_col">
<!--                    <div class="left_col scroll-view">
                        <@ include file="../Modules/Header.jsp" %>
                        <br />
                        <div id="sidebar-menu" class="main_menu_side hidden-print main_menu">
                            <@ include file="../Modules/MenuLeft.jsp" %>
                        </div>
                    </div>-->
                </div>
                <div class="top_nav">
                    <!--<@ include file="../Modules/Navigate.jsp" %>-->
                    <script>
//                        document.getElementById("idNameURL").innerHTML = tokenimport_title_list;
                    </script>
                </div>
                <div class="right_col" role="main">
                    <div class="">
                        <div class="row">
                            <%                                String sessLanguageGlobal = "1";// session.getAttribute("sessVN").toString();
                                String pISSUE_ENABLED = "1";
                                try {
                            %>
                            <div class="col-md-12 col-sm-12 col-xs-12">
                                <div class="x_panel">
                                    <div class="x_title">
                                        <h2><i class="fa fa-list-ul"></i> <script>document.write(tokenimport_title_import);</script></h2>
                                        <ul class="nav navbar-right panel_toolbox">
                                            <li>
                                                <input type="button" id="btnSave" class="btn btn-info" onclick="ValidateForm('<%=anticsrf%>');" />
                                                <script>document.getElementById("btnSave").value = global_fm_button_add;</script>
                                                <input type="hidden" name="CsrfToken" value="<%=anticsrf%>"/>
                                            </li>
                                        </ul>
                                        <div class="clearfix"></div>
                                    </div>
                                    <div class="x_content" style="margin-top: 0px;">
                                        <form name="formimport" method="post" class="form-horizontal">
                                            <div class="form-group" style="padding: 10px 0px 0 0px;margin: 0;">
                                                <label class="control-label123"><script>document.write(global_fm_Branch);</script></label>
                                                <select name="AGENT_NAME" id="AGENT_NAME" class="form-control123"
                                                    onchange="LOAD_BACKOFFICE_USER(this.value, '<%= anticsrf%>');">
                                                    <%
                                                        BRANCH[][] rst = new BRANCH[1][];
                                                        String sBranchFirst = "";
                                                        try {
                                                            db.S_BO_BRANCH_COMBOBOX(sessLanguageGlobal, rst);
                                                            if (rst[0].length > 0) {
                                                                for (BRANCH temp1 : rst[0]) {
                                                                    if(!String.valueOf(temp1.PARENT_ID).equals(Definitions.CONFIG_AGENT_ROOT))
                                                                    {
                                                                        if("".equals(sBranchFirst)) {
                                                                            sBranchFirst = String.valueOf(temp1.ID);
                                                                        }
                                                    %>
                                                    <option value="<%=String.valueOf(temp1.ID)%>"><%=temp1.NAME + " - " + temp1.REMARK%></option>
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
                                            <div class="form-group" style="padding: 10px 0px 0 0px;margin: 0;">
                                                <label class="control-label123"><script>document.write(global_fm_user_create);</script></label>
                                                <select name="USER" id="USER" class="form-control123">
                                                    <%
                                                        BACKOFFICE_USER[][] rssUser = new BACKOFFICE_USER[1][];
                                                        db.S_BO_GET_USER_BRANCH_ALL(sBranchFirst, rssUser);
                                                        if (rssUser[0].length > 0) {
                                                            for (int i = 0; i < rssUser[0].length; i++) {
                                                    %>
                                                    <option value="<%=String.valueOf(rssUser[0][i].ID)%>"><%=rssUser[0][i].FULL_NAME + " (" + rssUser[0][i].USERNAME + ")" %></option>
                                                    <%
                                                            }
                                                        }
                                                    %>
                                                </select>
                                            </div>
                                            <script>
                                                function LOAD_BACKOFFICE_USER(objAgency, idCSRF)
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
                                                                    for (var i = 0; i < obj.length; i++) {
                                                                        cbxUSER.options[cbxUSER.options.length] = new Option(obj[i].FULL_NAME + " (" + obj[i].USERNAME + ")", obj[i].ID);
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
                                                                    cbxUSER.options[cbxUSER.options.length] = new Option("---", "");
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
                                            <div class="form-group" style="padding: 10px 0px 0 0px;margin: 0;">
                                                <label class="control-label123"><script>document.write(global_fm_phone_contact);</script></label>
                                                <label class="CssRequireField"><script>document.write(global_fm_require_label);</script></label>
                                                <input type="text" id="PHONE_CONTRACT" maxlength="<%= Definitions.CONFIG_MAXLENGTH_FORM_PHONE %>" class="form-control123">
                                            </div>
                                            <div class="form-group" style="padding: 10px 0px 0 0px;margin: 0;">
                                                <label class="control-label123"><script>document.write(global_fm_email_contact);</script></label>
                                                <label class="CssRequireField"><script>document.write(global_fm_require_label);</script></label>
                                                <input type="text" id="EMAIL_CONTRACT" class="form-control123" maxlength="<%= Definitions.CONFIG_MAXLENGTH_FORM_EMAIL%>">
                                            </div>
                                            <div class="form-group" style="padding: 10px 0px 0 0px;margin: 0;">
                                                <label class="control-label123"><script>document.write(global_fm_ca);</script></label>
                                                <select name="CERTIFICATION_AUTHORITY" id="CERTIFICATION_AUTHORITY" class="form-control123"
                                                        onchange="LOAD_CERTIFICATION_AUTHORITY(this.value, '<%= anticsrf%>');" disabled>
                                                    <%
                                                        String sFristCA = "";
                                                        String sCACoreSubject = "";
                                                        CERTIFICATION_AUTHORITY[][] rssProfile = new CERTIFICATION_AUTHORITY[1][];
                                                        db.S_BO_CERTIFICATION_AUTHORITY_COMBOBOX(sessLanguageGlobal, rssProfile);
                                                        if (rssProfile[0].length > 0) {
                                                            for (int i = 0; i < rssProfile[0].length; i++) {
                                                                sFristCA = String.valueOf(rssProfile[0][0].ID);
                                                                sCACoreSubject = EscapeUtils.CheckTextNull(rssProfile[0][0].CERTIFICATION_AUTHORITY_CORECA_SUBJECT);
                                                    %>
                                                    <option value="<%=String.valueOf(rssProfile[0][i].ID)
                                                        + "###" + EscapeUtils.CheckTextNull(rssProfile[0][i].CERTIFICATION_AUTHORITY_CORECA_SUBJECT)%>"><%=rssProfile[0][i].REMARK%></option>
                                                    <%
                                                            }
                                                        }
                                                    %>
                                                </select>
                                            </div>
                                            <input id="idSessProfileID" style="display: none;"/>
                                            <div class="form-group" style="padding: 10px 0px 0 0px;margin: 0;">
                                                <label class="control-label123"><script>document.write(global_fm_certpurpose);</script></label>
                                                <select id="CERTIFICATION_PURPOSE" name="CERTIFICATION_PURPOSE" class="form-control123"
                                                    onchange="LOAD_CERTIFICATION_PURPOSE($('#idHiddenCerCA').val().split('###')[0], this.value, '<%= anticsrf%>');">
                                                    <%
                                                        String sFristCerPurpose="7";
                                                        CERTIFICATION_PURPOSE[][] rsCertPro = new CERTIFICATION_PURPOSE[1][];
                                                        db.S_BO_CA_GET_CERTIFICATION_PURPOSE_COMBOBOX(sFristCA, sessLanguageGlobal, rsCertPro);
                                                        if (rsCertPro.length > 0) {
                                                            for (int i = 0; i < rsCertPro[0].length; i++) {
//                                                                sFristCerPurpose = String.valueOf(rsCertPro[0][0].ID);
                                                    %>
                                                    <option value="<%= String.valueOf(rsCertPro[0][i].ID)%>" <%= String.valueOf(rsCertPro[0][i].ID).equals(sFristCerPurpose) ? "selected" : ""%>><%= rsCertPro[0][i].REMARK%></option>
                                                    <%
                                                            }
                                                        }
                                                    %>
                                                </select>
                                            </div>
                                            <div class="form-group" style="padding: 10px 0px 0 0px;margin: 0;">
                                                <label class="control-label123"><script>document.write(global_fm_duration_cts);</script></label>
                                                <select id="CERTIFICATION_DURATION" name="CERTIFICATION_DURATION" class="form-control123"
                                                    onchange="LOAD_CERTIFICATION_DURATION(this.value, '<%= anticsrf%>');">
                                                    <%
                                                        String sFristCerDurationOrProfileID = "";
                                                        CERTIFICATION_PROFILE[][] rsDuration = new CERTIFICATION_PROFILE[1][];
                                                        db.S_BO_CA_GET_DURATION_COMBOBOX(sFristCA, sFristCerPurpose, pISSUE_ENABLED, sessLanguageGlobal, rsDuration);
                                                        if (rsDuration[0].length > 0) {
                                                            for (int i = 0; i < rsDuration[0].length; i++) {
                                                                sFristCerDurationOrProfileID = String.valueOf(rsDuration[0][0].ID);
                                                    %>
                                                    <option value="<%= String.valueOf(rsDuration[0][i].ID)%>"><%= rsDuration[0][i].REMARK %></option>
                                                    <%
                                                            }
                                                        }
                                                    %>
                                                </select>
                                            </div>
                                            <input id="idHiddenCerCA" value="<%= sFristCA%>" style="display: none;"/>
                                            <input id="idHiddenCerCoreSubject" value="<%= sCACoreSubject%>" style="display: none;"/>
                                            <input id="idHiddenCerPurpose" value="<%= sFristCerPurpose%>" style="display: none;"/>
                                            <input id="idHiddenCerDurationOrProfileID" value="<%= sFristCerDurationOrProfileID%>" style="display: none;"/>
                                            <script>
                                                $(document).ready(function () {
                                                    if('<%= sFristCA%>' !== "" && '<%= sFristCerPurpose%>' !== "" && '<%= sFristCerDurationOrProfileID%>' !== "")
                                                    {
                                                        LOAD_CERTIFICATION_PROFILE('<%= sFristCerDurationOrProfileID%>');
                                                        LoadEnableTokenSN('<%= sFristCerPurpose%>');
                                                    }
                                                });
                                            </script>
                                            <div class="form-group" style="padding: 10px 0px 0 0px;margin: 0;">
                                                <label class="control-label123"><script>document.write(global_fm_amount_fee);</script></label>
                                                <input type="text" name="FEE_AMOUNT" disabled id="FEE_AMOUNT" class="form-control123">
                                            </div>
                                            <div class="form-group" style="padding: 10px 0px 0px 0px;margin: 0;display: none;" id="idViewDURATION_FREE">
                                                <label class="control-label123"><script>document.write(global_fm_date_free);</script></label>
                                                <input type="text" name="DURATION_FREE" disabled id="DURATION_FREE" class="form-control123">
                                            </div>
                                            <div class="form-group" style="padding: 10px 0px 0 0px;margin: 0;">
                                                <label class="control-label123"><script>document.write(token_fm_tokenid);</script></label>
                                                <label class="CssRequireField"><script>document.write(global_fm_require_label);</script></label>
                                                <input type="text" name="TOKEN_SN" id="TOKEN_SN" class="form-control123">
                                            </div>
                                            <div class="form-group" style="padding: 10px 0px 0px 0px;margin: 0;">
                                                <label class="control-label123"><script>document.write(global_fm_Certificate);</script></label>
                                                <input type="file" id="input-file" accept=".cer,.txt,.pem" style="width: 100%;" name="xls_filename"
                                                    onchange="UploadCertificate(this);" class="btn btn-default btn-file select_file">
                                            </div>
                                            <textarea id="idCer" class="form-control123" readonly="true" name="idCer" style="height: 85px;display: none;"></textarea>
                                            <script>
                                                function LOAD_CERTIFICATION_PROFILE(vCertDurationOrProfileID)
                                                {
                                                    $("#idHiddenCerDurationOrProfileID").val(vCertDurationOrProfileID);
                                                    $.ajax({
                                                        type: "post",
                                                        url: "../JSONCommon",
                                                        data: {
                                                            idParam: 'loadcert_profile_frist',
                                                            vCertDurationOrProfileID: vCertDurationOrProfileID
                                                        },
                                                        cache: false,
                                                        success: function (html)
                                                        {
                                                            if (html.length > 0)
                                                            {
                                                                var obj = JSON.parse(html);
                                                                if (obj[0].Code === "0")
                                                                {
                                                                    $("#FEE_AMOUNT").val(obj[0].AMOUNT);
                                                                    $("#DURATION_FREE").val(obj[0].DURATION_FREE);
//                                                                    LoadFormSubjectDN(vCertDurationOrProfileID);
                                                                    if(obj[0].DURATION_FREE === "0" || obj[0].DURATION_FREE === 0)
                                                                    {
                                                                        $("#idViewDURATION_FREE").css("display", "none");
                                                                    } else {
                                                                        $("#idViewDURATION_FREE").css("display", "");
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
                                                function LOAD_CERTIFICATION_AUTHORITY(objCA, idCSRF)
                                                {
                                                    $("#idHiddenCerCA").val(objCA.split('###')[0]);
                                                    $("#idHiddenCerCoreSubject").val(objCA.split('###')[1]);
                                                    $.ajax({
                                                        type: "post",
                                                        url: "../JSONCommon",
                                                        data: {
                                                            idParam: 'loadcert_authority_oftoken',
                                                            idCA: objCA.split('###')[0],
                                                            CsrfToken: idCSRF
                                                        },
                                                        cache: false,
                                                        success: function (html)
                                                        {
                                                            if (html.length > 0)
                                                            {
                                                                var cbxCERTIFICATION_PURPOSE = document.getElementById("CERTIFICATION_PURPOSE");
                                                                removeOptions(cbxCERTIFICATION_PURPOSE);
                                                                var obj = JSON.parse(html);
                                                                if (obj[0].Code === "0")
                                                                {
                                                                    for (var i = 0; i < obj.length; i++) {
                                                                        cbxCERTIFICATION_PURPOSE.options[cbxCERTIFICATION_PURPOSE.options.length] = new Option(obj[i].REMARK, obj[i].ID);
                                                                    }
                                                                    $("#idHiddenCerPurpose").val(obj[0].ID);
                                                                    LOAD_CERTIFICATION_PURPOSE(objCA.split('###')[0], obj[0].ID, idCSRF);
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
                                                                    cbxCERTIFICATION_PURPOSE.options[cbxCERTIFICATION_PURPOSE.options.length] = new Option("---", "");
                                                                }
                                                                else {
                                                                    funErrorAlert(global_errorsql);
                                                                }
                                                            }
                                                        }
                                                    });
                                                    return false;
                                                }
                                                function LOAD_CERTIFICATION_PURPOSE(objCA, objPurpose, idCSRF)
                                                {
                                                    $("#idHiddenCerPurpose").val(objPurpose);
                                                    LoadEnableTokenSN(objPurpose);
//                                                    LoadFileManage(objPurpose);
                                                    $.ajax({
                                                        type: "post",
                                                        url: "../JSONCommon",
                                                        data: {
                                                            idParam: 'loadcert_purpose',
                                                            idCA: objCA,
                                                            idPurpose: objPurpose,
                                                            pISSUE_ENABLED: '<%= pISSUE_ENABLED%>',
                                                            CsrfToken: idCSRF
                                                        },
                                                        cache: false,
                                                        success: function (html)
                                                        {
                                                            if (html.length > 0)
                                                            {
                                                                var cbxCERTIFICATION_DURATION = document.getElementById("CERTIFICATION_DURATION");
                                                                removeOptions(cbxCERTIFICATION_DURATION);
                                                                var obj = JSON.parse(html);
                                                                if (obj[0].Code === "0")
                                                                {
                                                                    $("#idHiddenCerDurationOrProfileID").val(obj[0].ID);
                                                                    for (var i = 0; i < obj.length; i++) {
                                                                        cbxCERTIFICATION_DURATION.options[cbxCERTIFICATION_DURATION.options.length] = new Option(obj[i].REMARK, obj[i].ID);
                                                                    }
                                                                    LOAD_CERTIFICATION_DURATION(obj[0].ID, idCSRF);
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
                                                                    cbxCERTIFICATION_DURATION.options[cbxCERTIFICATION_DURATION.options.length] = new Option("---", "");
                                                                }
                                                                else {
                                                                    funErrorAlert(global_errorsql);
                                                                }
                                                            }
                                                        }
                                                    });
                                                    return false;
                                                }
                                                function LOAD_CERTIFICATION_DURATION(objProfile, idCSRF)
                                                {
                                                    LOAD_CERTIFICATION_PROFILE(objProfile);
                                                }
                                                function LoadEnableTokenSN(objProfile)
                                                {
                                                    if(objProfile === JS_STR_CERTIFICATION_PURPOSE_ID_ENTERPRISE
                                                        || objProfile === JS_STR_CERTIFICATION_PURPOSE_ID_STAFF
                                                        || objProfile === JS_STR_CERTIFICATION_PURPOSE_ID_PERSONAL)
                                                    {
                                                        $("#TOKEN_SN").attr('disabled', false);
                                                    } else {
                                                        $("#TOKEN_SN").attr('disabled', true);
                                                    }
                                                }
                                            </script>
                                        </form>
                                    </div>
                                </div>
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
                <!--<@ include file="../Modules/Footer.jsp" %>-->
            </div>
            <script src="../style/jquery.min.js"></script>
            <script src="../style/bootstrap.min.js"></script>
            <script src="../style/custom.min.js"></script>
            <script src="../js/moment.min.js"></script>
            <script src="../js/daterangepicker.js"></script>
        </div>
        <%        //} else {
        %>
<!--        <script type="text/javascript">
                                    window.onload = function () {
                                        RedirectPageLoginNoSess(global_alert_login);
                                    }();
        </script>-->
        <%
         //   }
        %>
    </body>
</html>