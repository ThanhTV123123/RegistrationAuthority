<%-- 
    Document   : ManagePolicy
    Created on : Jan 17, 2014, 3:08:05 PM
    Author     : Thanh
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="../Admin/ConnectionParam.jsp" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="../style/bootstrap.min.css" rel="stylesheet">
        <link href="../style/font-awesome.css" rel="stylesheet">
        <link href="../style/nprogress.css" rel="stylesheet">
        <link href="../style/custom.min.css" rel="stylesheet">
        <link href="../Css/active/bootstrap-switch.css" rel="stylesheet">
        <script src="../js/Language.js"></script>
        <script src="../js/process_javajs.js"></script>
        <link href="../style/customportal.min.css" rel="stylesheet">
        <script type="text/javascript" src="../js/jquery.js"></script>
        <link rel="stylesheet" href="../js/sweetalert.css"/>
        <script src="../js/sweetalert-dev.js"></script>
        <script type="text/javascript" src="../Css/GlobalAlert.js"></script>
        <script type='text/javascript' src='../Css/jscolor.js'></script>
        <!--<link rel="stylesheet" type="text/css" media="all" href="../js/daterangepicker.css" />-->
        <title></title>
        <script type="text/javascript">
            document.title = policy_title_list;
            changeFavicon("../");
            $(document).ready(function () {
                $('.loading-gif').hide();
            });
            function ValidateForm(idCSRF) {
                $('body').append('<div id="over"></div>');
                $(".loading-gif").show();
                var vValue = ClickSaveForDN();
                if(vValue !== "" && vValue !== false)
                {
                    $.ajax({
                        type: "post",
                        url: "../PolicyCommon",
                        data: {
                            idParam: 'editpolicyconfig_edited',
                            vValue: vValue,
                            CsrfToken: idCSRF
                        },
                        cache: false,
                        success: function (html)
                        {
                            var myStrings = sSpace(html).split('#');
                            if (myStrings[0] === "0")
                            {
                                funSuccAlert(policy_succ_edit, "ManagePolicy.jsp");
                            }
                            else if (myStrings[0] === "10")
                            {
                                funErrorAlert(global_req_all);
                            }
                            else if (myStrings[0] === JS_EX_CSRF) {
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
                                funErrorAlert(global_errorsql);
                            }
                            $(".loading-gif").hide();
                            $('#over').remove();
                        }
                    });
                    return false;
                }
                $(".loading-gif").hide();
                $('#over').remove();
            }

        </script>
        <style>
            fieldset.scheduler-border {
                border: 1px solid #E6E9ED !important;
                padding: 0 1.4em 1.4em 1.4em !important;
                margin: 0 0 1.5em 0 !important;
                -webkit-box-shadow:  0px 0px 0px 0px #E6E9ED;
                box-shadow:  0px 0px 0px 0px #E6E9ED;
            }
            legend.scheduler-border {
                font-size: 14px !important;
                font-weight: bold !important;
                text-align: left !important;
                width:auto;
                padding:0 10px;
                border-bottom:none;
            }
            .form-control123[readonly]{background-color:#ffffff;opacity:1}
            /*.form-control123[disabled]{background-color:#eee;opacity:1}*/
        </style>
    </head>
    <body class="nav-md">
        <%
        if ((session.getAttribute("sUserID")) != null) {
            String anticsrf = "" + Math.random();
            request.getSession().setAttribute("anticsrf", anticsrf);
            session.setAttribute("sessPushNotificationList", null);
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
                        document.getElementById("idNameURL").innerHTML = policy_title_list;
                    </script>
                </div>
                <div class="right_col" role="main">
                    <div class="">
                        <div class="row">
                            <div class="col-md-12 col-sm-12 col-xs-12">
                                <%
                                    try {
                                %>
                                <div class="x_panel">
                                    <div class="x_title">
                                        <h2><i class="fa fa-list-ul"></i> <script>document.write(policy_title_list_client);</script></h2>
                                        <ul class="nav navbar-right panel_toolbox">
                                            <li>
                                                <input type="button" id="btnSave" data-switch-get="state" class="btn btn-info" onclick="ValidateForm('<%=anticsrf%>');" />
                                                <input type="hidden" name="CsrfToken" value="<%=anticsrf%>"/>
                                                <input type="hidden" id="tempCsrfToken" name="tempCsrfToken"/>
                                                <!--<input type="text" id="idTextParsePush"/>-->
                                                <script>
                                                    document.getElementById("btnSave").value = global_fm_button_edit;
                                                </script>
                                            </li>
                                        </ul>
                                        <div class="clearfix"></div>
                                    </div>
                                    <div class="x_content">
                                        <form name="myname" method="post" class="form-horizontal">
                                            <div id="idDivViewComponentDN"></div>
                                        </form>
                                        <script>
                                            function ClickSaveForDN()
                                            {
                                                var vDNResult = "";
                                                var sListRequire = localStorage.getItem("localStoreRequiredPersonal").split("@@@");
                                                for (var i = 1; i < sListRequire.length; i++) {
                                                    var idCheckEmpty = sListRequire[i].split('###')[0];
                                                    if(idCheckEmpty !== JS_STR_POLICY_TAG_FO_LANGUAGE) {
                                                        if (!JSCheckEmptyField($("#" + idCheckEmpty).val()))
                                                        {
                                                            $("#" + idCheckEmpty).focus();
                                                            funErrorAlert(policy_req_empty + sListRequire[i].split('###')[1]);
                                                            return false;
                                                        }
                                                    }
                                                }
                                                var sListInput = localStorage.getItem("localStoreInputPersonal").split("@@@");
                                                for (var i = 1; i < sListInput.length; i++) {
                                                    var idItemMineType = sListInput[i].split('###')[3];
                                                    var idItemValue = sListInput[i].split('###')[1];
                                                    var idItemName = sListInput[i].split('###')[0];
                                                    if(idItemName === JS_STR_TOKEN_TAG_PUSH_NOTICE_TEXT_COLOR || idItemName === JS_STR_TOKEN_TAG_PUSH_NOTICE_BGR_COLOR
                                                        || idItemName === JS_STR_TOKEN_TAG_PUSH_NOTICE_CONTENT || idItemName === JS_STR_TOKEN_TAG_PUSH_NOTICE_URL)
                                                    {
                                                        if(idItemName === JS_STR_TOKEN_TAG_PUSH_NOTICE_TEXT_COLOR) {
                                                            idItemValue = idItemValue + "_" + JS_STR_TOKEN_TAG_PUSH_NOTICE_TEXT_COLOR;
                                                        }
                                                        if(idItemName === JS_STR_TOKEN_TAG_PUSH_NOTICE_BGR_COLOR) {
                                                            idItemValue = idItemValue + "_" + JS_STR_TOKEN_TAG_PUSH_NOTICE_BGR_COLOR;
                                                        }
                                                        if(idItemName === JS_STR_TOKEN_TAG_PUSH_NOTICE_CONTENT) {
                                                            idItemValue = idItemValue + "_" + JS_STR_TOKEN_TAG_PUSH_NOTICE_CONTENT;
                                                        }
                                                        if(idItemName === JS_STR_TOKEN_TAG_PUSH_NOTICE_URL){
                                                            idItemValue = idItemValue + "_" + JS_STR_TOKEN_TAG_PUSH_NOTICE_URL;
                                                        }
                                                        var itemValue = $("#" + idItemName).val();
                                                        if (itemValue !== "" && itemValue !== "undefined") {
                                                            vDNResult += idItemValue + "###" + itemValue + "###" + idItemMineType + "###" + idItemName + "@@@";
                                                        }
                                                    } else {
                                                        var itemValue = $("#" + idItemName).val();
                                                        if(sListInput[i].split('###')[3] === JS_STR_POLICY_MIMETYPE_NUMERIC)
                                                        {
                                                            if (!JSCheckNumericField(itemValue.replace(/\,/g,""))) {
                                                                $("#" + sListInput[i].split('###')[0]).focus();
                                                                funErrorAlert(sListInput[i].split('###')[4] + policy_req_number);
                                                                return false;
                                                            }
                                                        } else if(sListInput[i].split('###')[3] === JS_STR_POLICY_MIMETYPE_BOOLEAN)
                                                        {
                                                            if(sListInput[i].split('###')[0] === JS_STR_POLICY_TAG_FO_LANGUAGE)
                                                            {
    //                                                            if($("#" + JS_STR_POLICY_TAG_FO_LANGUAGE + JS_STR_POLICY_REFIX_ID_RADIO_VN).attr('checked') === 'checked'){
                                                                if(document.getElementById(JS_STR_POLICY_TAG_FO_LANGUAGE + JS_STR_POLICY_REFIX_ID_RADIO_VN).checked) {
                                                                    itemValue = "1";
                                                                }
                                                                else
                                                                {
    //                                                                if($("#" + JS_STR_POLICY_TAG_FO_LANGUAGE + JS_STR_POLICY_REFIX_ID_RADIO_EN).attr('checked') === 'checked'){
                                                                    if(document.getElementById(JS_STR_POLICY_TAG_FO_LANGUAGE + JS_STR_POLICY_REFIX_ID_RADIO_EN).checked) {
                                                                        itemValue = "0";
                                                                    }
                                                                    else
                                                                    {
                                                                      itemValue = "1";
                                                                    }
                                                                }
                                                            } else {
                                                                var sTempActive = $("#" + sListInput[i].split('###')[0]).bootstrapSwitch("state");
                                                                if(sTempActive === true)
                                                                {
                                                                    itemValue = "1";
                                                                } else {
                                                                    itemValue = "0";
                                                                }
                                                            }
                                                        } else {}
                                                        if (itemValue !== "undefined") {
                                                            if(sListInput[i].split('###')[5] === '1') {
                                                                if(itemValue !== "") {
                                                                    vDNResult += idItemValue + "###" + itemValue + "###" + idItemMineType + "###" + idItemName + "@@@";
                                                                }
                                                            } else {
                                                                vDNResult += idItemValue + "###" + itemValue + "###" + idItemMineType + "###" + idItemName + "@@@";
                                                            }
                                                        }
                                                    }
                                                }
                                                var intSub = vDNResult.lastIndexOf('@@@');
                                                vDNResult = vDNResult.substring(0, intSub);
                                                localStorage.setItem("localStoreRequiredPersonal", null);
                                                localStorage.setItem("localStoreInputPersonal", null);
                                                return vDNResult;
                                            }
                                            function LoadBranchCombobox(cbxData, vValueST)
                                            {
                                                $.ajax({
                                                    type: "post",
                                                    url: "../JSONCommon",
                                                    data: {
                                                        idParam: 'listbranchagencycombobox',
                                                        CsrfToken: '<%= anticsrf%>'
                                                    },
                                                    cache: false,
                                                    success: function (html)
                                                    {
                                                        var cbxTemplateData = document.getElementById(cbxData);
                                                        removeOptions(cbxTemplateData);
                                                        if (html.length > 0)
                                                        {
                                                            var obj = JSON.parse(html);
                                                            if (obj[0].Code === "0")
                                                            {
                                                                for (var i = 0; i < obj.length; i++) {
                                                                    cbxTemplateData.options[cbxTemplateData.options.length] = new Option(obj[i].NAME + " - " + obj[i].REMARK, obj[i].NAME);
                                                                    if(vValueST === obj[i].NAME)
                                                                    {
                                                                        $("#" +cbxData+ " option[value='" + vValueST + "']").attr("selected", "selected");
                                                                    }
                                                                }
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
                                                    }
                                                });
                                                return false;
                                            }
                                            function LoadCACombobox(cbxData, vValueST)
                                            {
                                                $.ajax({
                                                    type: "post",
                                                    url: "../JSONCommon",
                                                    data: {
                                                        idParam: 'listcacombobox',
                                                        CsrfToken: '<%= anticsrf%>'
                                                    },
                                                    cache: false,
                                                    success: function (html)
                                                    {
                                                        var cbxTemplateData = document.getElementById(cbxData);
                                                        removeOptions(cbxTemplateData);
                                                        if (html.length > 0)
                                                        {
                                                            var obj = JSON.parse(html);
                                                            if (obj[0].Code === "0")
                                                            {
                                                                for (var i = 0; i < obj.length; i++) {
                                                                    cbxTemplateData.options[cbxTemplateData.options.length] = new Option(obj[i].NAME + " - " + obj[i].REMARK, obj[i].NAME);
                                                                    if(vValueST === obj[i].NAME)
                                                                    {
                                                                        $("#" +cbxData+ " option[value='" + vValueST + "']").attr("selected", "selected");
                                                                    }
                                                                }
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
                                                    }
                                                });
                                                return false;
                                            }
                                            function LoadJSONPushNoti(vREMARK, vLabelRequired, vJSON)
                                            {
                                                $.ajax({
                                                    type: "post",
                                                    url: "../JSONCommon",
                                                    data: {
                                                        idParam: 'loadjsonpushnoti',
                                                        vJSON: vJSON
                                                    },
                                                    cache: false,
                                                    success: function (html) {
                                                        if (html.length > 0)
                                                        {
                                                            var arr = JSON.parse(html);
                                                            if (arr[0].Code === "0")
                                                            {
                                                                var sVarJSONPush = '<div style="height: 10px;width: 100%;"></div>' +
                                                                    '<fieldset class="scheduler-border">' +
                                                                        '<legend class="scheduler-border">'+ vREMARK +'</legend>' +
                                                                        '<div class="form-group">'+
                                                                            '<div class="col-sm-6">'+
                                                                                '<label class="control-label123">'+token_fm_colortext+'</label>'+
                                                                                vLabelRequired +
                                                                                '<input type="Text" id="'+JS_STR_TOKEN_TAG_PUSH_NOTICE_TEXT_COLOR+'" class="form-control123 color" value="'+arr[0].PUSH_NOTICE_TEXT_COLOR+'"/>' +
                                                                            '</div>'+
                                                                            '<div class="col-sm-6">'+
                                                                                '<label class="control-label123">'+token_fm_colorgkgd+'</label>'+
                                                                                vLabelRequired +
                                                                                '<input type="Text" id="'+JS_STR_TOKEN_TAG_PUSH_NOTICE_BGR_COLOR+'" class="form-control123 color" value="'+arr[0].PUSH_NOTICE_BGR_COLOR+'"/>' +
                                                                            '</div>'+
                                                                        '</div>' +
                                                                        '<div class="form-group">'+
                                                                            '<div class="col-sm-6">'+
                                                                                '<label class="control-label123">'+token_fm_noticelink+'</label>'+
                                                                                vLabelRequired +
                                                                                '<input type="Text" id="'+JS_STR_TOKEN_TAG_PUSH_NOTICE_URL+'" class="form-control123" value="'+arr[0].PUSH_NOTICE_URL+'"/>' +
                                                                            '</div>'+
                                                                            '<div class="col-sm-6">'+
                                                                                '<label class="control-label123">'+token_fm_noticeinfor+'</label>'+
                                                                                vLabelRequired +
                                                                                '<input type="Text" id="'+JS_STR_TOKEN_TAG_PUSH_NOTICE_CONTENT+'" class="form-control123" value="'+arr[0].PUSH_NOTICE_CONTENT+'"/>' +
                                                                            '</div>'+
                                                                        '</div>' +
                                                                    '</fieldset>';
                                                                    $("#idTextParsePush").val(sVarJSONPush);
                                                            }
                                                            else if (arr[0].Code === JS_EX_LOGIN)
                                                            {
                                                                $("#idTextParsePush").val('');
                                                                RedirectPageLoginNoSess(global_alert_login);
                                                            }
                                                            else if (arr[0].Code === JS_EX_ANOTHERLOGIN)
                                                            {
                                                                $("#idTextParsePush").val('');
                                                                RedirectPageLoginNoSess(global_alert_another_login);
                                                            }
                                                            else
                                                            {
                                                                $("#idTextParsePush").val('');
                                                                funErrorAlert(global_errorsql);
                                                            }
                                                        }
                                                    }
                                                });
                                                return sVarJSONPush;
                                            }
                                            function LoadFormDNForPersonalCommon(idCSRF)
                                            {
                                                $('body').append('<div id="over"></div>');
                                                $(".loading-gif").show();
                                                var vLanguage = localStorage.getItem("VN");
                                                $.ajax({
                                                    type: "post",
                                                    url: "../JSONCommon",
                                                    data: {
                                                        idParam: 'listgeneralpolicyedited',
                                                        vLanguage: vLanguage,
                                                        CsrfToken: idCSRF
                                                    },
                                                    cache: false,
                                                    success: function (html)
                                                    {
                                                        if (html.length > 0)
                                                        {
                                                            var obj = JSON.parse(html);
                                                            $("#idDivViewComponentDN").empty();
                                                            if (obj[0].Code === "0")
                                                            {
                                                                $("#idDivViewComponentDN").css("display", "");
                                                                localStorage.setItem("localStoreCompSTID", null);
                                                                localStorage.setItem("localStoreDateTime", null);
                                                                localStorage.setItem("localStoreCompSTValue", null);
                                                                localStorage.setItem("localStoreJsonColor", null);
                                                                var vContentFrontOffice = "<fieldset class=\"scheduler-border\"><legend class=\"scheduler-border\">"+policy_title_list_client_fo+"</legend>";
                                                                var vContentBackOffice = "<fieldset class=\"scheduler-border\"><legend class=\"scheduler-border\">"+policy_title_list_client_bo+"</legend>";
                                                                var vContent = "";
                                                                var vInputID_DateTime = "";
                                                                var localStoreRequired = "";
                                                                var localStoreInput = "";
                                                                var vStringViewCheckbox = "";
                                                                
                                                                for (var i = 0; i < obj.length; i++) {
                                                                    var vLabelRequired = "";
                                                                    if (obj[i].IsRequired === '1') {
                                                                        var vInputID_Required = obj[i].NAME;
                                                                        vLabelRequired = '<label class="CssRequireField">' + global_fm_require_label + '</label>';
                                                                        if(obj[i].MIMETYPE === JS_STR_POLICY_MIMETYPE_JSON_PUSHNOTIFICATION) {
                                                                            for (var j = 0; j < obj[i].FO_DEFAULT_PUSH_NOTICE_JSON.length; j++) {
                                                                                vInputID_Required = obj[i].ID + "_" + obj[i].FO_DEFAULT_PUSH_NOTICE_JSON[j].NAME;
                                                                                localStoreRequired = localStoreRequired + "@@@" + vInputID_Required + '###' + obj[i].FO_DEFAULT_PUSH_NOTICE_JSON[j].REMARK;
                                                                            }
                                                                        } else if(obj[i].MIMETYPE === JS_STR_POLICY_MIMETYPE_JSON_NOTIFICATION_RECORD_COLLECTION) {
                                                                            for (var j = 0; j < obj[i].FO_PUSH_NOTIFICATION_RECORD_COLLECTION.length; j++) {
                                                                                vInputID_Required = obj[i].ID + "_" + obj[i].FO_PUSH_NOTIFICATION_RECORD_COLLECTION[j].NAME;
                                                                                localStoreRequired = localStoreRequired + "@@@" + vInputID_Required + '###' + obj[i].FO_PUSH_NOTIFICATION_RECORD_COLLECTION[j].REMARK;
                                                                            }
                                                                        } else {
                                                                            localStoreRequired = localStoreRequired + "@@@" + vInputID_Required + '###' + obj[i].REMARK;
                                                                        }
                                                                    }
                                                                    if(obj[i].FRONT_OFFICE_ENABLED === '1')
                                                                    {
                                                                        var vInputID = obj[i].NAME;
                                                                        localStoreInput = localStoreInput + "@@@" + vInputID + "###" + obj[i].ID + "###" + obj[i].VALUE + "###" + obj[i].MIMETYPE + '###' + obj[i].REMARK + '###' + obj[i].IsRequired;
                                                                        if(obj[i].MIMETYPE === JS_STR_POLICY_MIMETYPE_TEMPLATE)
                                                                        {
                                                                            vContentFrontOffice += '<div class="form-group" style="padding: 5px 0px 0 0px;margin: 0;">' +
                                                                            '<label class="control-label123">' + obj[i].REMARK + '</label> ' +
                                                                            vLabelRequired +
                                                                            '<textarea class="form-control123" type="text" style=\'height: 85px;\' id="' + vInputID + '">'+ obj[i].VALUE + '</textarea>' +
                                                                            '</div>';
                                                                        }
                                                                        else if(obj[i].MIMETYPE === JS_STR_POLICY_MIMETYPE_NUMERIC)
                                                                        {
                                                                            var idText = 'autoConvertMoney($(@#' + vInputID + '@).val(), $(@#' + vInputID + '@));';
                                                                            idText = idText.replace(/\@/g, "'");
                                                                            vContentBackOffice += '<div class="form-group" style="padding: 5px 0px 0 0px;margin: 0;">' +
                                                                                '<label class="control-label123">' + obj[i].REMARK + '</label> ' +
                                                                                vLabelRequired +
                                                                                '<input class="form-control123" oninput="'+idText+'" type="text" value="'+ obj[i].VALUE + '" id="' + vInputID + '" />' +
                                                                                '</div>';
                                                                        }
                                                                        else if(obj[i].MIMETYPE === JS_STR_POLICY_MIMETYPE_DATETIME)
                                                                        {
                                                                            var sValueDateTime = ConvertDateTime(obj[i].VALUE);
                                                                            vContentFrontOffice += '<div class="form-group" style="padding: 5px 0px 0 0px;margin: 0;">' +
                                                                            '<label class="control-label123">' + obj[i].REMARK + '</label> ' +
                                                                            vLabelRequired +
                                                                            '<input class="form-control123" type="text" readonly value="'+ sValueDateTime + '" id="' + vInputID + '" />' +
                                                                            '</div>';
                                                                        }
                                                                        else if(obj[i].MIMETYPE === JS_STR_POLICY_MIMETYPE_COMBOBOX_CA)
                                                                        {
                                                                            vContentFrontOffice += '<div class="form-group" style="padding: 5px 0px 0 0px;margin: 0;">' +
                                                                            '<label class="control-label123">' + obj[i].REMARK + '</label> ' +
                                                                            '<select class="form-control123" id="' + vInputID + '"></select>' +
                                                                            '</div>';
                                                                            LoadCACombobox(vInputID, obj[i].VALUE);
                                                                        }
                                                                        else if(obj[i].MIMETYPE === JS_STR_POLICY_MIMETYPE_COMBOBOX_BRANCH)
                                                                        {
                                                                            vContentFrontOffice += '<div class="form-group" style="padding: 5px 0px 0 0px;margin: 0;">' +
                                                                            '<label class="control-label123">' + obj[i].REMARK + '</label> ' +
                                                                            '<select class="form-control123" id="' + vInputID + '"></select>' +
                                                                            '</div>';
                                                                            LoadBranchCombobox(vInputID, obj[i].VALUE);
                                                                        }
                                                                        else if (vInputID === JS_STR_POLICY_TAG_FO_LANGUAGE) {
                                                                            var sCheckRadio1 = "checked";
                                                                            var sCheckRadio2 = "";
                                                                            if(obj[i].VALUE === "0") {
                                                                                sCheckRadio1 = "";
                                                                                sCheckRadio2 = "checked";
                                                                            }
                                                                            vContentFrontOffice += '<div class="form-group" style="padding: 10px 0px 5px 0px;margin: 0;">' +
                                                                            '<label class="control-label col-sm-3" style="color: #000000; font-weight: bold;text-align: left;padding:5px 0 0 0;">' + obj[i].REMARK + '</label>' +
                                                                            '<div class="col-sm-5">' +
                                                                            '<label class="radio-inline"><input type="radio" name="' + vInputID + '" id="' + vInputID + ''+JS_STR_POLICY_REFIX_ID_RADIO_VN+'" '+sCheckRadio1+'>'+global_fm_vn+'</label>' +
                                                                            '<label class="radio-inline"><input type="radio" name="' + vInputID + '" id="' + vInputID + ''+JS_STR_POLICY_REFIX_ID_RADIO_EN+'" '+sCheckRadio2+'>'+global_fm_en+'</label>' +
                                                                            '</div>' +
                                                                            '</div>';
                                                                        }
                                                                        else if(obj[i].MIMETYPE === JS_STR_POLICY_MIMETYPE_JSON_PUSHNOTIFICATION)
                                                                        {
                                                                            var vValueJsonColor = "";
                                                                            var vValuePUSH_JSON = '<div style="height: 10px;width: 100%;"></div><fieldset class="scheduler-border"><legend class="scheduler-border">'+ obj[i].REMARK +'</legend>\n\
                                                                                <div class="form-group" style="padding-left: 10px;">';
                                                                            for (var j = 0; j < obj[i].FO_DEFAULT_PUSH_NOTICE_JSON.length; j++) {
                                                                                var vInputID_PUSH = obj[i].ID + "_" + obj[i].FO_DEFAULT_PUSH_NOTICE_JSON[j].NAME;
                                                                                localStoreInput = localStoreInput + "@@@" + vInputID_PUSH + "###" + vInputID_PUSH + "###" + obj[i].FO_DEFAULT_PUSH_NOTICE_JSON[j].VALUE + "###" + obj[i].MIMETYPE + '###' + obj[i].FO_DEFAULT_PUSH_NOTICE_JSON[j].REMARK + '###' + obj[i].IsRequired;
                                                                                vValuePUSH_JSON += "<div class=\"col-sm-6\" style=\"padding-left: 0; padding-top: 10px;\">"+
                                                                                    "<div class='form-group' style='padding: 5px 0px 0 0px;margin: 0;'>" +
                                                                                    "<label class='control-label123'>" + obj[i].FO_DEFAULT_PUSH_NOTICE_JSON[j].REMARK + "</label> " +
                                                                                    vLabelRequired +
                                                                                    "<input class='form-control123' type='text' value='" + obj[i].FO_DEFAULT_PUSH_NOTICE_JSON[j].VALUE + "' id='" + vInputID_PUSH + "' name='" + vInputID_PUSH + "' />" +
                                                                                    "</div>"+
                                                                                    "</div>";
                                                                                if(obj[i].FO_DEFAULT_PUSH_NOTICE_JSON[j].MIMETYPE === JS_STR_POLICY_MIMETYPE_COLOR)
                                                                                {
                                                                                    vValueJsonColor += vInputID_PUSH + "###";
                                                                                }
                                                                            }
                                                                            localStorage.setItem("localStoreJsonColor", vValueJsonColor);
                                                                            vValuePUSH_JSON = vValuePUSH_JSON + "</fieldset>";
                                                                            vContentFrontOffice = vContentFrontOffice + vValuePUSH_JSON;
                                                                        }
                                                                        else if(obj[i].MIMETYPE === JS_STR_POLICY_MIMETYPE_JSON_NOTIFICATION_RECORD_COLLECTION)
                                                                        {
                                                                            var vValuePUSH_JSON = '<div style="height: 10px;width: 100%;"></div><fieldset class="scheduler-border"><legend class="scheduler-border">'+ obj[i].REMARK +'</legend>\n\
                                                                                <div class="form-group" style="padding-left: 10px;">';
                                                                            for (var j = 0; j < obj[i].FO_PUSH_NOTIFICATION_RECORD_COLLECTION.length; j++) {
                                                                                var vInputID_PUSH = obj[i].ID + "_" + obj[i].FO_PUSH_NOTIFICATION_RECORD_COLLECTION[j].NAME;
                                                                                localStoreInput = localStoreInput + "@@@" + vInputID_PUSH + "###" + vInputID_PUSH + "###" + obj[i].FO_PUSH_NOTIFICATION_RECORD_COLLECTION[j].VALUE + "###" + obj[i].MIMETYPE + '###' + obj[i].FO_PUSH_NOTIFICATION_RECORD_COLLECTION[j].REMARK + '###' + obj[i].IsRequired;
                                                                                if(obj[i].FO_PUSH_NOTIFICATION_RECORD_COLLECTION[j].NAME === 'content')
                                                                                {
                                                                                    vValuePUSH_JSON += "<div class=\"form-group\" style=\"padding: 0;\">"+
                                                                                        "<label class='control-label123'>" + obj[i].FO_PUSH_NOTIFICATION_RECORD_COLLECTION[j].REMARK + "</label> " +
                                                                                        vLabelRequired +
                                                                                        "<textarea class='form-control123' style=\"height: 85px;\" type='text' id='" + vInputID_PUSH + "' name='" + vInputID_PUSH + "'>" + obj[i].FO_PUSH_NOTIFICATION_RECORD_COLLECTION[j].VALUE + "</textarea>" +
                                                                                        "</div>";
                                                                                } else {
                                                                                    vValuePUSH_JSON += "<div class=\"form-group\" style=\"padding: 0;\">"+
                                                                                        "<label class='control-label123'>" + obj[i].FO_PUSH_NOTIFICATION_RECORD_COLLECTION[j].REMARK + "</label> " +
                                                                                        vLabelRequired +
                                                                                        "<input class='form-control123' type='text' value='" + obj[i].FO_PUSH_NOTIFICATION_RECORD_COLLECTION[j].VALUE + "' id='" + vInputID_PUSH + "' name='" + vInputID_PUSH + "' />" +
                                                                                        "</div>";
                                                                                }
                                                                            }
                                                                            vValuePUSH_JSON = vValuePUSH_JSON + "</fieldset>";
                                                                            vContentFrontOffice = vContentFrontOffice + vValuePUSH_JSON;
                                                                        }
                                                                        else
                                                                        {
                                                                            if(obj[i].MIMETYPE === JS_STR_POLICY_MIMETYPE_BOOLEAN)
                                                                            {
                                                                                vContentFrontOffice += '<div class="form-group" style="padding: 10px 0px 5px 0px;margin: 0;">' +
                                                                                '<input class="form-control123" type="checkbox" id="' + vInputID + '" '+ obj[i].VALUE + ' />&nbsp;&nbsp;' +
                                                                                '<label class="control-label123">' + obj[i].REMARK + '</label> ' +
                                                                                '</div>';
                                                                                vStringViewCheckbox = vStringViewCheckbox + "@@@" + vInputID;
                                                                            }
                                                                            else
                                                                            {
                                                                                vContentFrontOffice += '<div class="form-group" style="padding: 5px 0px 0 0px;margin: 0;">' +
                                                                                '<label class="control-label123">' + obj[i].REMARK + '</label> ' +
                                                                                vLabelRequired +
                                                                                '<input class="form-control123" type="text" value="'+ obj[i].VALUE + '" id="' + vInputID + '" />' +
                                                                                '</div>';
                                                                            }
                                                                        }
                                                                    }
                                                                    else
                                                                    {
                                                                        var vInputID = obj[i].NAME;
                                                                        localStoreInput = localStoreInput + "@@@" + vInputID + "###" + obj[i].ID + "###" + obj[i].VALUE + "###" + obj[i].MIMETYPE + '###' + obj[i].REMARK + '###' + obj[i].IsRequired;
                                                                        if(obj[i].MIMETYPE === JS_STR_POLICY_MIMETYPE_TEMPLATE)
                                                                        {
                                                                            vContentBackOffice += '<div class="form-group" style="padding: 5px 0px 0 0px;margin: 0;">' +
                                                                            '<label class="control-label123">' + obj[i].REMARK + '</label> ' +
                                                                            vLabelRequired +
                                                                            '<textarea class="form-control123" type="text" style=\'height: 85px;\' id="' + vInputID + '">'+ obj[i].VALUE + '</textarea>' +
                                                                            '</div>';
                                                                        }
                                                                        else if(obj[i].MIMETYPE === JS_STR_POLICY_MIMETYPE_NUMERIC)
                                                                        {
                                                                            var idText = 'autoConvertMoney($(@#' + vInputID + '@).val(), $(@#' + vInputID + '@));';
                                                                            idText = idText.replace(/\@/g, "'");
                                                                            vContentBackOffice += '<div class="form-group" style="padding: 5px 0px 0 0px;margin: 0;">' +
                                                                                '<label class="control-label123">' + obj[i].REMARK + '</label> ' +
                                                                                vLabelRequired +
                                                                                '<input class="form-control123" oninput="'+idText+'" type="text" value="'+ obj[i].VALUE + '" id="' + vInputID + '" />' +
                                                                                '</div>';
                                                                        }
                                                                        else if(obj[i].MIMETYPE === JS_STR_POLICY_MIMETYPE_DATETIME)
                                                                        {
                                                                            var sValueDateTime = ConvertDateTime(obj[i].VALUE);
                                                                            vContentBackOffice += '<div class="form-group" style="padding: 5px 0px 0 0px;margin: 0;">' +
                                                                            '<label class="control-label123">' + obj[i].REMARK + '</label> ' +
                                                                            vLabelRequired +
                                                                            '<input class="form-control123" readonly type="text" value="'+ sValueDateTime + '" id="' + vInputID + '" />' +
                                                                            '</div>';
                                                                            vInputID_DateTime = vInputID_DateTime + "###" + vInputID;
                                                                        }
                                                                        else if(obj[i].MIMETYPE === JS_STR_POLICY_MIMETYPE_COMBOBOX_CA)
                                                                        {
                                                                            vContentBackOffice += '<div class="form-group" style="padding: 5px 0px 0 0px;margin: 0;">' +
                                                                            '<label class="control-label123">' + obj[i].REMARK + '</label> ' +
                                                                            '<select class="form-control123" id="' + vInputID + '"></select>' +
                                                                            '</div>';
                                                                            LoadCACombobox(vInputID, obj[i].VALUE);
                                                                        }
                                                                        else
                                                                        {
                                                                            if(obj[i].MIMETYPE === JS_STR_POLICY_MIMETYPE_BOOLEAN)
                                                                            {
                                                                                vContentBackOffice += '<div class="form-group" style="padding: 10px 0px 5px 0px;margin: 0;">' +
                                                                                '<input type="checkbox" id="' + vInputID + '" '+ obj[i].VALUE + ' />&nbsp;&nbsp;' +
                                                                                '<label class="control-label123">' + obj[i].REMARK + '</label>' +
                                                                                '</div>';
                                                                                vStringViewCheckbox = vStringViewCheckbox + "@@@" + vInputID;
                                                                            }
                                                                            else
                                                                            {
                                                                                vContentBackOffice += '<div class="form-group" style="padding: 5px 0px 0 0px;margin: 0;">' +
                                                                                '<label class="control-label123">' + obj[i].REMARK + '</label> ' +
                                                                                vLabelRequired +
                                                                                '<input class="form-control123" type="text" value="'+ obj[i].VALUE + '" id="' + vInputID + '" />' +
                                                                                '</div>';
                                                                            }
                                                                        }
                                                                    }
                                                                    localStorage.setItem("localStoreDateTime", vInputID_DateTime);
                                                                }
                                                                vContentFrontOffice = vContentFrontOffice + "</fieldset>";
                                                                vContentBackOffice = vContentBackOffice + "</fieldset>";
                                                                vContent = vContentFrontOffice + vContentBackOffice;
                                                                $("#idDivViewComponentDN").append(vContent);
                                                                localStorage.setItem("localStoreRequiredPersonal", localStoreRequired);
                                                                localStorage.setItem("localStoreInputPersonal", localStoreInput);
                                                                if(vStringViewCheckbox !== "")
                                                                {
                                                                    ViewCheckBox(vStringViewCheckbox);
                                                                }
                                                                if(localStorage.getItem("localStoreDateTime") !== "")
                                                                {
                                                                    var vStoreDateTime = localStorage.getItem("localStoreDateTime").split('###');
                                                                    for(var iDate = 0; iDate<vStoreDateTime.length; iDate++)
                                                                    {
                                                                        $('#'+vStoreDateTime[iDate]).daterangepicker({
                                                                            singleDatePicker: true,
                                                                            showDropdowns: true
                                                                        }, function (start, end, label) {
                                                                            console.log(start.toISOString(), end.toISOString(), label);
                                                                        });
                                                                    }
                                                                }
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
                                                                $("#idDivViewComponentDN").css("display", "none");
                                                                funErrorAlert(global_errorsql);
                                                            }
                                                        }
                                                            $(".loading-gif").hide();
                                                            $('#over').remove();
                                                    }
                                                });
                                                return false;
                                            }
                                            function ViewCheckBox(vvViewCheckbox)
                                            {
                                                var sStringTemp = "";
                                                var vItemViewCheckbox = vvViewCheckbox.split('@@@');
                                                for (var n = 1; n < vItemViewCheckbox.length; n++) {
                                                    sStringTemp = sStringTemp + "setTimeout(function() {$('#"+vItemViewCheckbox[n]+"').bootstrapSwitch('setState', true);}, 1000);";
                                                }
                                                eval(sStringTemp);
                                            }
                                        </script>
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
                            <script>
                                $(document).ready(function () {
                                    LoadFormDNForPersonalCommon('<%= anticsrf%>');
                                    setTimeout(function(){
                                        if(localStorage.getItem("localStoreJsonColor") !== "")
                                        {
                                            var vStoreJsonColore = localStorage.getItem("localStoreJsonColor").split('###');
                                            for(var iColor = 0; iColor<vStoreJsonColore.length; iColor++)
                                            {
                                                var vID = sSpace(vStoreJsonColore[iColor]);
//                                                console.log(vID);
                                                if(vID !== "") {
                                                    $('#'+vID).addClass('jscolor');
//                                                    $('#'+vID).prop('readonly', true);
                                                    new jscolor($('#'+vID)[0]);
                                                }
                                            }
                                            localStorage.setItem("localStoreJsonColor", null);
                                        }
                                    }, 1000);
//                                        var myColor = new jscolor($('#2_textColor')[0]);
                                });
                            </script>
                        </div>
                    </div>
                </div>
                <%@ include file="../Modules/Footer.jsp" %>
            </div>
        </div>
        <script src="../style/jquery.min.js"></script>
        <script src="../style/bootstrap.min.js"></script>
        <script src="../style/custom.min.js"></script>
<!--        <script src="../js/moment.min_limit.js"></script>
        <script src="../js/daterangepicker_limit.js"></script>-->
        <script src="../js/active/highlight.js"></script>
        <script src="../js/active/bootstrap-switch.js"></script>
        <script src="../js/active/main.js"></script>
        <script src="../js/moment.min.js"></script>
        <script src="../js/daterangepicker.js"></script>
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