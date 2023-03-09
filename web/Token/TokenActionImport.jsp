<%-- 
    Document   : TokenActionImport
    Created on : Mar 26, 2020, 10:29:05 AM
    Author     : USER
--%>

<%@page import="com.fasterxml.jackson.databind.ObjectMapper"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="../Admin/ConnectionParam.jsp" %>
<%@include file="../Admin/CommonPagingList.jsp" %>
<%  response.setHeader("Cache-Control", "no-cache");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", -1);
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
        <script type='text/javascript' src='../Css/jscolor.js'></script>
        <script src="../js/sweetalert-dev.js"></script>
        <link href="../style/customportal.min.css" rel="stylesheet">
        <script type="text/javascript" src="../Css/GlobalAlert.js"></script>
        <link href="../js/checkphone/intlTelInput.css" rel="stylesheet" type="text/css"/>
        <script src="../js/checkphone/intlTelInput.js" type="text/javascript"></script>
        <title></title>
        <script language="javascript">
            changeFavicon("../");
            document.title = actionimport_title_list;
            $(document).ready(function () {
                $('.loading-gif').hide();
            });
            function downloadErrorFile()
            {
                $.ajax({
                    type: "post",
                    url: "../DownloadFileCSR",
                    data: {
                        idParam: "savefileimporttokenaction"
                    },
                    catche: false,
                    success: function (html) {
                        var arr = sSpace(html).split('#');
                        if (arr[0] === "0")
                        {
                            var f = document.formimport;
                            f.method = "post";
                            f.action = '../DownFromSaveFile?idParam=downfileimportdisallowance&name=' + arr[1];
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
            function calUpload(idCSRF)
            {
                var isAllApplyEnabled = "0";
                if ($("#allApplyEnabled").is(':checked')) {
                    isAllApplyEnabled = "1";
                }
                if(isAllApplyEnabled === "0")
                {
                    var input1 = document.getElementById('input-file');
                    if (input1.value !== '')
                    {
                        var checkFileName = input1.value.substring(input1.value.lastIndexOf('.') + 1);
                        if (checkFileName === "xls" || checkFileName === "xlsx" || checkFileName === "csv")
                        {

                        } else {
                            funErrorAlert(token_error_import_format);
                            return false;
                        }
                    }
                    else
                    {
                        funErrorAlert(global_req_file);
                        return false;
                    }
                }
                var vCOLOR_TEXT = $("#strCOLOR_TEXT_Edit").val();
                var vCOLOR_BKGR = $("#strCOLOR_BKGR_Edit").val();
                var vLINK_NOTICE = $("#strLINK_NOTICE_Edit").val();
                var vNOTICE_INFO = $("#strNOTICE_INFO_Edit").val();
                var vNAME_LINK = $("#strNAME_LINK_Edit").val();
                var vLINK_VALUE = $("#strLINK_VALUE_Edit").val();
                var vACTION_TOKEN = $("#ACTION_TOKEN").val();
                if(vACTION_TOKEN === "PUSH_NOTFICATION")
                {
                    if (!JSCheckEmptyField(vCOLOR_TEXT))
                    {
                        document.getElementById("strCOLOR_TEXT_Edit").focus();
                        funErrorAlert(policy_req_empty + token_fm_colortext);
                        return false;
                    }
                    if (!JSCheckEmptyField(vCOLOR_BKGR))
                    {
                        document.getElementById("strCOLOR_BKGR_Edit").focus();
                        funErrorAlert(policy_req_empty + token_fm_colorgkgd);
                        return false;
                    }
                    if (!JSCheckEmptyField(vLINK_NOTICE))
                    {
                        document.getElementById("strLINK_NOTICE_Edit").focus();
                        funErrorAlert(policy_req_empty + token_fm_noticelink);
                        return false;
                    }
                    if (!JSCheckEmptyField(vNOTICE_INFO))
                    {
                        document.getElementById("strNOTICE_INFO_Edit").focus();
                        funErrorAlert(policy_req_empty + token_fm_noticeinfor);
                        return false;
                    }
                }
                var vIS_PUSH_NOTICE_SET_NO = "0";
                if ($("#cancelPushEnabled").is(':checked'))
                {
                    vIS_PUSH_NOTICE_SET_NO = "1";
                }
                var vIS_MENU_LINK_SET_NO = "0";
                if ($("#cancelMenuEnabled").is(':checked'))
                {
                    vIS_MENU_LINK_SET_NO = "1";
                }
                $.ajax({
                    type: "post",
                    url: '../TokenCommon',
                    data: {
                        idParam: 'addreasonlocktoken',
                        idLockReason: $("#idLockReason").val(),
                        idActionToken: $("#ACTION_TOKEN").val(),
                        vCOLOR_TEXT: vCOLOR_TEXT,
                        vCOLOR_BKGR: vCOLOR_BKGR,
                        vLINK_NOTICE: vLINK_NOTICE,
                        vNOTICE_INFO: vNOTICE_INFO,
                        vNAME_LINK: vNAME_LINK,
                        vLINK_VALUE: vLINK_VALUE,
                        vIS_PUSH_NOTICE_SET_NO: vIS_PUSH_NOTICE_SET_NO,
                        vIS_MENU_LINK_SET_NO: vIS_MENU_LINK_SET_NO,
                        vAllApplyEnabled: isAllApplyEnabled,
                        CsrfToken: idCSRF
                    },
                    cache: false,
                    success: function (check) {
                        var arr = sSpace(check).split('#');
                        if (arr[0] === "0")
                        {
                            $('body').append('<div id="over"></div>');
                            $(".loading-gif").show();
                            var data1 = new FormData();
                            $.each($('#input-file')[0].files, function(k, value)
                            {
                                data1.append(k, value);
                            });
//                            data1.append('pIS_ACTION', $("#ACTION_TOKEN").val());
//                                    data1.append('pLOCK_REASON', $("#idLockReason").val());
                            $.ajax({
                                type: 'POST',
                                url: '../TokenActionImport',
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
                                                text: actionimport_succ_edit + "\n"
                                                    + global_fm_combox_success + ": " + arr[1] + token_succ_import_error + arr[2] + "\n"
                                                    + disallowanceimport_succ_conform_down,
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
                                            var sAlert = actionimport_succ_edit + "\n" + global_fm_combox_success + ": " + arr[1];
                                            funSuccAlert(sAlert, "TokenActionImport.jsp");
                                        }
                                    }
                                    else if (arr[0] === "00")
                                    {
                                        funSuccAlert(actionimport_succ_edit, "TokenActionImport.jsp");
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
                                        else if (arr[1] === "NO_FORMAT")
                                        {
                                            funErrorAlert(token_error_import_format);
                                        }
                                        else if (arr[1] === JS_EX_CSV_NO_TOKENSN)
                                        {
                                            funErrorAlert(certimport_file_format_invalid);
                                        }
                                        else if (arr[1] === JS_EX_CSV_NO_MST)
                                        {
                                            funErrorAlert(certimport_file_format_invalid);
                                        }
                                        else if (arr[1] === JS_EX_CSV_NO_STT)
                                        {
                                            funErrorAlert(certimport_file_format_invalid);
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
                        else if (arr[0] === "10")
                        {
                            funErrorAlert(global_req_all);
                        }
                        else if (arr[0] === "11")
                        {
                            funErrorAlert(global_req_length);
                        }
                        else
                        {
                            funCsrfAlert();
                        }
                    }
                });
            }
        </script>
        <style>.projects th{font-weight: bold;}</style>
    </head>
    <body class="nav-md">
        <%
            if ((session.getAttribute("sUserID")) != null) {
                String anticsrf = "" + Math.random();
                request.getSession().setAttribute("anticsrf", anticsrf);
                GENERAL_POLICY[][] sessGeneralPolicy = (GENERAL_POLICY[][]) session.getAttribute("sessGeneralPolicy_System");
                String strNOTICE_INFO = "";
                String strLINK_NOTICE = "";
                String strCOLOR_BKGR = "";
                String strCOLOR_TEXT = "";
                if (sessGeneralPolicy[0].length > 0) {
                    for(GENERAL_POLICY rsPolicy1: sessGeneralPolicy[0])
                    {
                        if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_FO_DEFAULT_PUSH_NOTICE_JSON))
                        {
                            String sValueDefaultJSON = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                            if(!"".equals(sValueDefaultJSON))
                            {
                                ObjectMapper oMapperParse = new ObjectMapper();
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
                        document.getElementById("idNameURL").innerHTML = actionimport_title_list;
                    </script>
                </div>
                <div class="right_col" role="main">
                    <div class="">
                        <div class="row">
                            <%
                                try {
                            %>
                            <div class="col-md-12 col-sm-12 col-xs-12">
                                <div class="x_panel">
                                    <div class="x_title">
                                        <h2><i class="fa fa-list-ul"></i> <script>document.write(actionimport_title_import);</script></h2>
                                        <ul class="nav navbar-right panel_toolbox">
                                            <li>
                                                <input id="btnImport" class="btn btn-info" onclick="calUpload('<%= anticsrf%>')" type="button"/>
                                                <script>$("#btnImport").val(global_fm_button_import);</script>
                                                <input type="hidden" name="CsrfToken" value="<%=anticsrf%>"/>
                                            </li>
                                        </ul>
                                        <div class="clearfix"></div>
                                    </div>
                                    <div class="x_content" style="margin-top: 0px;">
                                        <form name="formimport" method="post" accept-charset="utf-8" class="form-horizontal">
                                            <fieldset class="scheduler-border">
                                                <legend class="scheduler-border"><script>document.write(actionimport_title_import);</script></legend>
                                                <div class="x_content" style="margin-top: 0px;">
                                                    <div class="form-group" style="padding: 0px;margin: 0;">
                                                        <label class="control-label123"><script>document.write(global_fm_browse_file);</script></label>
                                                        <input type="file" id="input-file" accept=".xlsx,.xls,.csv" style="width: 100%;"
                                                            class="btn btn-default btn-file select_file">
                                                    </div>
                                                    <div class="form-group" style="padding: 0px 0px 0px 0px;">
                                                        <label class="control-label"><script>document.write(token_fm_import_sample);</script></label> <a style="cursor: pointer;" onclick="return downloadSamplePush();"><script>document.write(global_fm_down);</script></a>
                                                    </div>
                                                    <script>
                                                        function downloadSamplePush()
                                                        {
                                                            var f = document.formimport;
                                                            f.method = "post";
                                                            f.action = '../DownFromSaveFile?idParam=downfilesampletokenaction';
                                                            f.submit();
                                                        }
                                                    </script>
                                                </div>
                                            </fieldset>
                                            <fieldset class="scheduler-border">
                                                <legend class="scheduler-border"><script>document.write(global_button_grid_config);</script></legend>
                                                <div class="form-group" style="padding-left: 10px;">
                                                    <div class="col-sm-6" style="padding-left: 10px;padding-right: 0;padding-top: 10px;">
                                                        <div class="form-group">
                                                            <label class="control-label col-sm-4" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                                                <script>document.write(global_fm_action);</script>
                                                            </label>
                                                            <div class="col-sm-8" style="padding-right: 0px;">
                                                                <select name="ACTION_TOKEN" id="ACTION_TOKEN" class="form-control123" onchange="onClickAction(this.value);">
                                                                    <option value="LOCK"><script>document.write(token_fm_block);</script></option>
                                                                    <option value="UNLOCK"><script>document.write(token_fm_unblock);</script></option>
                                                                    <option value="PUSH_NOTFICATION"><script>document.write(token_fm_noticepush);</script></option>
                                                                    <option value="MENU_LINK"><script>document.write(token_group_dynamic);</script></option>
                                                                </select>
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div class="col-sm-6" style="padding-left: 10px;padding-right: 0;padding-top: 10px;display: none;" id="idLockReasonView">
                                                        <div class="form-group">
                                                            <label class="control-label col-sm-4" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                                                <script>document.write(token_fm_reason_block);</script>
                                                            </label>
                                                            <div class="col-sm-8" style="padding-right: 0px;">
                                                                <input type="text" id="idLockReason" maxlength="256" name="idLockReason" class="form-control123"/>
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div class="col-sm-13" style="padding-left: 10px;padding-right: 0;padding-top: 10px;clear: both;" id="idAllApplyView">
                                                        <div class="form-group">
                                                            <div class="col-sm-1" style="padding-right: 0px;padding-left: 0;padding-top: 6px;">
                                                                <label class="switch" for="allApplyEnabled" style="margin-bottom: 0;">
                                                                    <input type="checkbox" name="allApplyEnabled" id="allApplyEnabled" onclick="onCheckAllApply();" />
                                                                    <div id="allApplyEnabledClass" class="slider round"></div>
                                                                </label>
                                                            </div>
                                                            <label class="control-label col-sm-11" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                                                <script>document.write(token_fm_all_apply);</script>
                                                            </label>
                                                        </div>
                                                    </div>
                                                    <script>
                                                        $(document).ready(function () {
                                                            $("#idAllApplyView").css("display","none");
                                                            onClickAction($("#ACTION_TOKEN").val());
                                                        });
                                                        function onCheckAllApply()
                                                        {
                                                            if ($("#allApplyEnabled").is(':checked')) {
                                                                document.getElementById('input-file').value = "";
                                                                document.getElementById("input-file").disabled = true;
                                                            } else {
                                                                document.getElementById("input-file").disabled = false;
                                                            }
                                                        }
                                                        function onClickAction(vValue)
                                                        {
                                                            if (vValue === "LOCK")
                                                            {
                                                                $("#idAllApplyView").css("display","none");
                                                                $("#idLockReasonView").css("display","");
                                                                $("#idLockReason").val("");
                                                                $("#idMenuDynamicView").css("display","none");
                                                                $("#idPushNoticeView").css("display","none");
                                                            } else if (vValue === "UNLOCK")
                                                            {
                                                                $("#idAllApplyView").css("display","none");
                                                                $("#idLockReasonView").css("display","none");
                                                                $("#idLockReason").val("");
                                                                $("#idMenuDynamicView").css("display","none");
                                                                $("#idPushNoticeView").css("display","none");
                                                            } else if (vValue === "MENU_LINK")
                                                            {
                                                                $("#idLockReasonView").css("display","none");
                                                                $("#idLockReason").val("");
                                                                $("#idMenuDynamicView").css("display","");
                                                                $("#idPushNoticeView").css("display","none");
                                                                $("#idAllApplyView").css("display","");
                                                            }
                                                            else
                                                            {
                                                                $("#idLockReasonView").css("display","none");
                                                                $("#idLockReason").val("");
                                                                $("#idMenuDynamicView").css("display","none");
                                                                $("#idPushNoticeView").css("display","");
                                                                $("#idAllApplyView").css("display","");
                                                            }
                                                        }
                                                    </script>
                                                    <div id="idPushNoticeView" style="display: none;">
                                                        <div class="form-group" style="padding: 0px 0px 0px 0px;margin: 0;clear: both;">
                                                            <div class="col-sm-6" style="padding-left: 10px;padding-right: 0;padding-top: 10px;">
                                                                <div class="form-group">
                                                                    <label class="control-label col-sm-4" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                                                        <script>document.write(token_fm_colortext);</script>
                                                                        <label class="CssRequireField"><script>document.write(global_fm_require_label);</script></label>
                                                                    </label>
                                                                    <div class="col-sm-8" style="padding-right: 0px;">
                                                                        <input type="text" id="strCOLOR_TEXT_Edit" maxlength="6" name="strCOLOR_TEXT_Edit" class="form-control123 jscolor"/>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                            <div class="col-sm-6" style="padding-left: 10px;padding-right: 0;padding-top: 10px;">
                                                                <div class="form-group">
                                                                    <label class="control-label col-sm-4" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                                                        <script>document.write(token_fm_colorgkgd);</script>
                                                                        <label class="CssRequireField"><script>document.write(global_fm_require_label);</script></label>
                                                                    </label>
                                                                    <div class="col-sm-8" style="padding-right: 0px;">
                                                                        <input type="text" id="strCOLOR_BKGR_Edit" maxlength="6" name="strCOLOR_BKGR_Edit" class="form-control123 jscolor"/>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                        <div class="form-group" style="padding: 0px 0px 0px 0px;margin: 0;">
                                                            <div class="col-sm-6" style="padding-left: 10px;padding-right: 0;padding-top: 10px;">
                                                                <div class="form-group">
                                                                    <label class="control-label col-sm-4" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                                                        <script>document.write(token_fm_noticelink);</script>
                                                                        <label class="CssRequireField"><script>document.write(global_fm_require_label);</script></label>
                                                                    </label>
                                                                    <div class="col-sm-8" style="padding-right: 0px;">
                                                                        <textarea type="Text" id="strLINK_NOTICE_Edit" name="strLINK_NOTICE_Edit" maxlength="512" class="form-control123" style="height: 85px;"></textarea>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                            <div class="col-sm-6" style="padding-left: 10px;padding-right: 0;padding-top: 10px;">
                                                                <div class="form-group">
                                                                    <label class="control-label col-sm-4" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                                                        <script>document.write(token_fm_noticeinfor);</script>
                                                                        <label class="CssRequireField"><script>document.write(global_fm_require_label);</script></label>
                                                                    </label>
                                                                    <div class="col-sm-8" style="padding-right: 0px;">
                                                                        <textarea type="Text" id="strNOTICE_INFO_Edit" name="strNOTICE_INFO_Edit" maxlength="1024" class="form-control123" style="height: 85px;"></textarea>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                        <div class="form-group" style="padding: 0px 0px 0px 0px;margin: 0; clear: both;">
                                                            <div class="col-sm-13" style="padding-left: 0px;padding-right: 0;padding-top: 10px;">
                                                                <div class="form-group">
                                                                    <div class="col-sm-1" style="padding-right: 0px;padding-top: 6px;">
                                                                        <label class="switch" for="cancelPushEnabled" style="margin-bottom: 0;">
                                                                            <input type="checkbox" name="cancelPushEnabled" id="cancelPushEnabled" onclick="onCancelPushNotice();" />
                                                                            <div id="cancelPushEnabledClass" class="slider round"></div>
                                                                        </label>
                                                                    </div>
                                                                    <label class="control-label col-sm-11" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;padding-bottom: 7px;">
                                                                        <script>document.write(token_fm_set_no_noticepush);</script>
                                                                    </label>
                                                                </div>
                                                                <script>
                                                                    $(document).ready(function () {
                                                                        //document.getElementById("strCOLOR_TEXT_Edit").disabled = false;
                                                                        //document.getElementById("strCOLOR_BKGR_Edit").disabled = false;
                                                                        document.getElementById("strLINK_NOTICE_Edit").disabled = false;
                                                                        document.getElementById("strNOTICE_INFO_Edit").disabled = false;
                                                                    });
                                                                    function onCancelPushNotice()
                                                                    {
                                                                        if ($("#cancelPushEnabled").is(':checked')) {
//                                                                            $("#strCOLOR_TEXT_Edit").prop("disabled", true);
//                                                                            $("#strCOLOR_TEXT_Edit").removeClass('jscolor');
//                                                                            $("#strCOLOR_BKGR_Edit").prop("disabled", true);
//                                                                            $("#strCOLOR_BKGR_Edit").removeClass('color');
                                                                            document.getElementById('strCOLOR_TEXT_Edit').value = '<%= strCOLOR_TEXT%>';
                                                                            $("#strCOLOR_TEXT_Edit").focus();
                                                                            $("#strCOLOR_TEXT_Edit").blur();
                                                                            document.getElementById("strCOLOR_TEXT_Edit").disabled = true;
                                                                            document.getElementById('strCOLOR_BKGR_Edit').value ='<%= strCOLOR_BKGR%>';
                                                                            $("#strCOLOR_BKGR_Edit").focus();
                                                                            $("#strCOLOR_BKGR_Edit").blur();
                                                                            document.getElementById("strCOLOR_BKGR_Edit").disabled = true;
                                                                            document.getElementById('strLINK_NOTICE_Edit').value ='<%= strLINK_NOTICE%>';
                                                                            document.getElementById("strLINK_NOTICE_Edit").disabled = true;
                                                                            document.getElementById('strNOTICE_INFO_Edit').value = '<%= strNOTICE_INFO%>';
                                                                            document.getElementById("strNOTICE_INFO_Edit").disabled = true;
                                                                        } else {
                                                                            document.getElementById("strCOLOR_TEXT_Edit").disabled = false;
                                                                            document.getElementById("strCOLOR_BKGR_Edit").disabled = false;
                                                                            document.getElementById("strLINK_NOTICE_Edit").disabled = false;
                                                                            document.getElementById("strNOTICE_INFO_Edit").disabled = false;
//                                                                            $("#strCOLOR_TEXT_Edit").prop("disabled", false);
                                                                            $("#strCOLOR_TEXT_Edit").addClass('jscolor');
//                                                                            $("#strCOLOR_BKGR_Edit").prop("disabled", false);
                                                                            $("#strCOLOR_BKGR_Edit").addClass('color');
                                                                        }
                                                                    }
                                                                </script>
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div id="idMenuDynamicView" style="display: none;">
                                                        <div class="form-group" style="padding: 0px 0px 0px 0px;margin: 0; clear: both;">
                                                            <div class="col-sm-6" style="padding-left: 10px;padding-right: 0;padding-top: 10px;">
                                                                <div class="form-group">
                                                                    <label class="control-label col-sm-4" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                                                        <script>document.write(token_fm_linkname);</script>
                                                                    </label>
                                                                    <div class="col-sm-8" style="padding-right: 0px;">
                                                                        <input type="Text" id="strNAME_LINK_Edit" maxlength="256" name="strNAME_LINK_Edit" class="form-control123"/>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                            <div class="col-sm-6" style="padding-left: 10px;padding-right: 0;padding-top: 10px;">
                                                                <div class="form-group">
                                                                    <label class="control-label col-sm-4" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                                                        <script>document.write(token_fm_linkvalue);</script>
                                                                    </label>
                                                                    <div class="col-sm-8" style="padding-right: 0px;">
                                                                        <input type="Text" id="strLINK_VALUE_Edit" maxlength="512" name="strLINK_VALUE_Edit" class="form-control123"/>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                        <div class="form-group" style="padding: 0px 0px 0px 0px;margin: 0; clear: both;">
                                                            <div class="col-sm-13" style="padding-left: 0px;padding-right: 0;padding-top: 10px;">
                                                                <div class="form-group">
                                                                    <div class="col-sm-1" style="padding-right: 0px;padding-top: 6px;">
                                                                        <label class="switch" for="cancelMenuEnabled" style="margin-bottom: 0;">
                                                                            <input type="checkbox" name="cancelMenuEnabled" id="cancelMenuEnabled" onclick="onCancelMenu();" />
                                                                            <div id="cancelMenuEnabledClass" class="slider round"></div>
                                                                        </label>
                                                                    </div>
                                                                    <label class="control-label col-sm-11" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;padding-bottom: 7px;">
                                                                        <script>document.write(token_fm_set_no_dynamic);</script>
                                                                    </label>
                                                                </div>
                                                                <script>
                                                                    $(document).ready(function () {
                                                                        document.getElementById("strNAME_LINK_Edit").disabled = false;
                                                                        document.getElementById("strLINK_VALUE_Edit").disabled = false;
                                                                    });
                                                                    function onCancelMenu()
                                                                    {
                                                                        if ($("#cancelMenuEnabled").is(':checked')) {
                                                                            document.getElementById('strNAME_LINK_Edit').value = "";
                                                                            document.getElementById("strNAME_LINK_Edit").disabled = true;
                                                                            document.getElementById('strLINK_VALUE_Edit').value = "";
                                                                            document.getElementById("strLINK_VALUE_Edit").disabled = true;
                                                                        } else {
                                                                            document.getElementById("strNAME_LINK_Edit").disabled = false;
                                                                            document.getElementById("strLINK_VALUE_Edit").disabled = false;
                                                                        }
                                                                    }
                                                                </script>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </fieldset>
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
                <%@ include file="../Modules/Footer.jsp" %>
            </div>
            <script src="../style/jquery.min.js"></script>
            <script src="../style/bootstrap.min.js"></script>
            <script src="../style/custom.min.js"></script>
            <script src="../js/moment.min.js"></script>
            <script src="../js/active/highlight.js"></script>
<!--            <script src="../js/active/bootstrap-switch.js"></script>
            <script src="../js/active/main.js"></script>
            <script src="../js/daterangepicker.js"></script>-->
        </div>
        <%        } else {
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
