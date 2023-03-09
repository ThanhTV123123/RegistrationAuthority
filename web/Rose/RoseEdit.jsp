<%-- 
    Document   : RoseEdit
    Created on : Nov 18, 2019, 1:36:58 PM
    Author     : USER
--%>

<%@page import="vn.ra.process.SessionRoleFunctions"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DateFormat"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="../Admin/ConnectionParam.jsp" %>
<%@include file="../Admin/CommonPagingList.jsp" %>
<!DOCTYPE html>
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
        <link rel="stylesheet" href="../js/sweetalert.css"/>
        <script src="../js/sweetalert-dev.js"></script>
        <link href="../style/customportal.min.css" rel="stylesheet">
        <script type="text/javascript" src="../js/jquery.js"></script>
        <script type="text/javascript" src="../Css/GlobalAlert.js"></script>
        <link href="../Css/smartpaginator.css" rel="stylesheet" type="text/css"/>
        <script src="../Css/smartpaginator.js" type="text/javascript"></script>
        <title></title>
        <script type="text/javascript">
            changeFavicon("../");
            document.title = role_title_edit;
            $(document).ready(function () {
                $('.loading-gif').hide();
                $("#idLblTitleEdits").text(rose_title_edit);
                $("#idLblTitleCode").text(rose_fm_code);
                $("#idLblTitleRemark").text(global_fm_remark_vn);
                $("#idLblNoteRemark").text(global_fm_require_label);
                $("#idLblTitleRemark_EN").text(global_fm_remark_en);
                $("#idLblNoteRemark_EN").text(global_fm_require_label);
                $("#idLblTitleRoleSet").text(rose_permission_profile_list);
                $("#idLblTitleActiveFlag").text(global_fm_active);
                $("#idLblTitleCreateUser").text(global_fm_user_create);
                $("#idLblTitleCreateDate").text(global_fm_date_create);
                $("#idLblTitleUpdateUser").text(global_fm_user_endupdate);
                $("#idLblTitleUpdateDate").text(global_fm_date_endupdate);
            });
            function ValidateForm(idCSRF) {
                if (!JSCheckEmptyField(document.myname.GroupCode.value))
                {
                    funErrorAlert(policy_req_empty + role_fm_code);
                    return false;
                }
                if (!JSCheckEmptyField(document.myname.Remark.value))
                {
                    document.myname.Remark.focus();
                    funErrorAlert(policy_req_empty + global_fm_remark_vn);
                    return false;
                }
                if (!JSCheckEmptyField(document.myname.Remark_EN.value))
                {
                    document.myname.Remark_EN.focus();
                    funErrorAlert(policy_req_empty + global_fm_remark_en);
                    return false;
                }
                var sCheckActiveFlag = "0";
                if ($("#ActiveFlag").is(':checked')) { sCheckActiveFlag = "1"; }
                $('body').append('<div id="over"></div>');
                $(".loading-gif").show();
                $.ajax({
                    type: "post",
                    url: "../RoseCommon",
                    data: {
                        idParam: 'editrose',
                        id: document.myname.GroupID.value,
                        GroupCode: document.myname.GroupCode.value,
                        Remark: document.myname.Remark.value,
                        Remark_EN: document.myname.Remark_EN.value,
                        ActiveFlag: sCheckActiveFlag,
                        CsrfToken: idCSRF
                    },
                    cache: false,
                    success: function (html)
                    {
                        var myStrings = sSpace(html).split('#');
                        if (myStrings[0] === "0")
                        {
                            localStorage.setItem("EDIT_USERROSE", document.myname.GroupID.value);
                            funSuccAlert(inputcertlist_succ_edit, "RoseList.jsp");
                        }
                        else if (myStrings[0] === JS_EX_CSRF) {
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
                                funErrorAlert(role_exists_code);
                            } else if (myStrings[1] === JS_STR_ERROR_CODE_99) {
                                funErrorAlert(global_error_login_info);
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
            }
            function closeForm()
            {
                $.ajax({
                    type: "post",
                    url: "../SomeCommon",
                    data: {
                        idParam: 'backformpage',
                        idSession: 'SessRefreshDiscountRate'
                    },
                    cache: false,
                    success: function (html) {
                        var arr = sSpace(html);
                        if (arr === "0")
                        {
                            window.location = "RoseList.jsp";
                        }
                        else
                        {
                            window.location = "RoseList.jsp";
                        }
                    }
                });
                return false;
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
                        enabled: strActive,
                        attributeType: type_chilrent,
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
        <style>
            .projects th{font-weight: bold;}
            .navbar-right{margin-right: 0;padding-right:10px;}
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
        String anticsrf = "" + Math.random();
        request.getSession().setAttribute("anticsrf", anticsrf);
        session.setAttribute("SessDiscountRateAccess",null);
    %>
    <div style="width: 100%; text-align: center; position: fixed;z-index: 1000;top: 0; padding-top: 300px;
         left: 0; height: 100%;" class="loading-gif">
        <img src="../Images/ajax-loader1.gif" alt="Please wait..." />
    </div>
        <div class="x_title">
            <h2><i class="fa fa-list-ul"></i> <span style="color: #36526D;" id="idLblTitleEdits"></span></h2>
            <ul class="nav navbar-right panel_toolbox">
                <li>
                    <input type="button" data-switch-get="state" id="btnSave" class="btn btn-info" onclick="return ValidateForm('<%=anticsrf%>');"/>
                    <input id="btnClose" class="btn btn-info" type="button" onclick="closeForm();" />
                    <input type="hidden" name="CsrfToken" value="<%=anticsrf%>"/>
                    <script>
                        document.getElementById("btnSave").value = global_fm_button_edit;
                        document.getElementById("btnClose").value = global_fm_button_close;
                    </script>
                </li>
            </ul>
            <div class="clearfix"></div>
        </div>
        <div class="x_content">
            <%
                DISCOUNT_RATE_PROFILE[][] rs = new DISCOUNT_RATE_PROFILE[1][];
                try {
                    String ids = request.getParameter("id");
                    String sessLanguageGlobal = session.getAttribute("sessVN").toString().trim();
                    request.getSession(false).setAttribute("SessDiscountRateAccess", null);
                    String strCADefault = "";
                    if (EscapeUtils.IsInteger(ids) == true) {
                        db.S_BO_DISCOUNT_RATE_PROFILE_DETAIL(EscapeUtils.escapeHtml(ids), rs);
                        if(rs[0].length > 0) {
                            String strRemark = EscapeUtils.CheckTextNull(rs[0][0].REMARK);
                            String strRemark_EN = EscapeUtils.CheckTextNull(rs[0][0].REMARK_EN);
                            String strCode = EscapeUtils.CheckTextNull(rs[0][0].NAME);
                            String sJSON_FUNCTION = EscapeUtils.CheckTextNull(rs[0][0].PROPERTIES);
                            if(!"".equals(sJSON_FUNCTION))
                            {
                                PROFILE_DISCOUNT_RATE_DATA[][] resIPData = new PROFILE_DISCOUNT_RATE_DATA[1][];
                                CommonFunction.getAllProfileDiscountRate(sJSON_FUNCTION, resIPData);
                                if(resIPData[0].length > 0)
                                {
                                    request.getSession(false).setAttribute("SessDiscountRateAccess", resIPData);
                                }
                            }
                            GENERAL_POLICY[][] sessGeneralPolicy = (GENERAL_POLICY[][]) session.getAttribute("sessGeneralPolicy_System");
                            if (sessGeneralPolicy[0].length > 0) {
                                for(GENERAL_POLICY rsPolicy1: sessGeneralPolicy[0])
                                {
                                    if (rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_CA_DEFAULT_FOR_EXPORT))
                                    {
                                        strCADefault = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                        break;
                                    }
                                }
                            }
            %>
            <form name="myname" method="post" class="form-horizontal">
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitleCode" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input type="text" readonly="true" name="GroupID" style="display: none;" value="<%= rs[0][0].ID %>" />
                            <input type="text" readonly="true" name="GroupCode" class="form-control123" value="<%= strCode%>" />
                        </div>
                    </div>
                </div>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;">
                            <label id="idLblTitleRemark"></label>
                            <label class="CssRequireField" id="idLblNoteRemark"></label>
                        </label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input type="text" maxlength="<%=Definitions.CONFIG_LENGTH_INPUT_REMARK%>" name="Remark" class="form-control123" value="<%= strRemark%>"/>
                        </div>
                    </div>
                </div>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;">
                            <label id="idLblTitleRemark_EN"></label>
                            <label class="CssRequireField" id="idLblNoteRemark_EN"></label>
                        </label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input type="text" maxlength="<%=Definitions.CONFIG_LENGTH_INPUT_REMARK%>" name="Remark_EN" class="form-control123" value="<%= strRemark_EN%>"/>
                        </div>
                    </div>
                </div>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitleActiveFlag" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <label class="switch" for="ActiveFlag">
                                <input TYPE="checkbox" id="ActiveFlag" name="ActiveFlag" <%=rs[0][0].ENABLED ? "checked" : ""%> />
                                <div class="slider round"></div>
                            </label>
                        </div>
                    </div>
                </div>
                <div class="form-group" style="padding: 10px 0px 0px 0px;margin: 0;"></div>
                <fieldset class="scheduler-border">
                    <legend class="scheduler-border" id="idLblTitleRoleSet"></legend>
                    <style type="text/css">
                        .table > thead > tr > th, .table > tbody > tr > td{vertical-align: middle;}
                        .table > thead > tr > th{border-bottom: none;}
                        .btn{margin-bottom: 0px;}
                        .panel_toolbox { min-width: 0;}
                    </style>
                    <div class="table-responsive">
                        <div class="x_title" style="border-bottom: 0 solid #E6E9ED;margin-bottom: 0px;">
                        </div>
                        <div class="x_content">
                            <div class="col-sm-5" style="padding-left: 0;">
                                <div class="form-group">
                                    <label id="idLblTitleProfileCA" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                    <div class="col-sm-7" style="padding-right: 0px;">
                                        <select name="CERTIFICATION_AUTHORITY" id="CERTIFICATION_AUTHORITY" class="form-control123"
                                            onchange="LOAD_CERTIFICATION_AUTHORITY(this.value, '<%= anticsrf%>');">
                                            <%
                                                String sFristCA = "";
                                                CERTIFICATION_AUTHORITY[][] rssProfile = new CERTIFICATION_AUTHORITY[1][];
                                                db.S_BO_CERTIFICATION_AUTHORITY_COMBOBOX(sessLanguageGlobal, rssProfile);
                                                if (rssProfile[0].length > 0) {
                                                    int intCAIDDefault = CommonFunction.getCAIDDefault(strCADefault, rssProfile[0]);
                                                    for (int i = 0; i < rssProfile[0].length; i++) {
                                                        if(rssProfile[0][i].ID == intCAIDDefault)
                                                        {
                                                            if("".equals(sFristCA)) {
                                                                sFristCA = String.valueOf(rssProfile[0][i].ID);
                                                            }
                                                        }
                                            %>
                                            <option value="<%=String.valueOf(rssProfile[0][i].ID)%>" <%= rssProfile[0][i].ID == intCAIDDefault ? "selected" : "" %>><%=rssProfile[0][i].REMARK%></option>
                                            <%
                                                    }
                                                }
                                            %>
                                        </select>
                                    </div>
                                </div>
                                <script>$("#idLblTitleProfileCA").text(global_fm_ca);</script>
                            </div>
                            <div class="col-sm-5" style="padding-left: 0;">
                                <div class="form-group">
                                    <label id="idLblTitleProfileCertPurpose" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                    <div class="col-sm-7" style="padding-right: 0px;">
                                        <select id="CERTIFICATION_PURPOSE" name="CERTIFICATION_PURPOSE" class="form-control123"
                                            onchange="LOAD_CERTIFICATION_PURPOSE($('#CERTIFICATION_AUTHORITY').val(), this.value, '<%= anticsrf%>');">
                                            <%
                                                String sFristCerPurpose="";
                                                CERTIFICATION_PURPOSE[][] rsCertPro = new CERTIFICATION_PURPOSE[1][];
                                                db.S_BO_CA_GET_CERTIFICATION_PURPOSE_COMBOBOX(sFristCA, sessLanguageGlobal, rsCertPro);
                                                if (rsCertPro.length > 0) {
                                                    for (int i = 0; i < rsCertPro[0].length; i++) {
                                                        sFristCerPurpose = String.valueOf(rsCertPro[0][0].ID);
                                            %>
                                            <option value="<%= String.valueOf(rsCertPro[0][i].ID)%>"><%= rsCertPro[0][i].REMARK%></option>
                                            <%
                                                    }
                                                }
                                            %>
                                        </select>
                                    </div>
                                </div>
                                <script>$("#idLblTitleProfileCertPurpose").text(global_fm_certpurpose);</script>
                            </div>
                            <div class="col-sm-2" style="padding-left: 0;"></div>
                            <div class="col-sm-5" style="padding-left: 0;">
                                <div class="form-group">
                                    <label id="idLblTitlePKIFormfactor" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                    <div class="col-sm-7" style="padding-right: 0px;">
                                        <select id="PKI_FORMFACTOR" name="PKI_FORMFACTOR" class="form-control123"
                                            onchange="LOAD_PROFILE_BY_FORMFACTOR('<%= sFristCA%>', '<%= sFristCerPurpose%>', this.value, '<%= anticsrf%>');">
                                            <%
                                                String sFirstPKI_FormFactor = "";
                                                PKI_FORMFACTOR[][] rsPKIFormFactor = new PKI_FORMFACTOR[1][];
                                                db.S_BO_CA_GET_PKI_FORMFACTOR_COMBOBOX_FOR_CERTIFICATION_PURPOSE(Integer.parseInt(sFristCerPurpose),
                                                    sessLanguageGlobal, rsPKIFormFactor);
                                                if (rsPKIFormFactor.length > 0) {
                                                    for (int i = 0; i < rsPKIFormFactor[0].length; i++) {
                                                        sFirstPKI_FormFactor = String.valueOf(rsPKIFormFactor[0][0].ID);
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
                            <div class="col-sm-5" style="padding-left: 0;">
                                <div class="form-group">
                                    <label id="idLblTitleProfileCertDuration" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                    <div class="col-sm-7" style="padding-right: 0px;">
                                        <select id="CERTIFICATION_DURATION" name="CERTIFICATION_DURATION" class="form-control123">
                                            <%
                                                CERTIFICATION_PROFILE[][] rsDuration = new CERTIFICATION_PROFILE[1][];
                                                db.S_BO_CA_GET_DURATION_COMBOBOX(sFristCA, sFristCerPurpose, sFirstPKI_FormFactor, sessLanguageGlobal, rsDuration);
                                                if (rsDuration[0].length > 0) {
                                                    for (int i = 0; i < rsDuration[0].length; i++) {
                                            %>
                                            <option value="<%= String.valueOf(rsDuration[0][i].ID)%>"><%= rsDuration[0][i].REMARK %></option>
                                            <%
                                                    }
                                                }
                                            %>
                                        </select>
                                    </div>
                                </div>
                                <script>$("#idLblTitleProfileCertDuration").text(global_fm_duration_cts);</script>
                            </div>
                            <div class="col-sm-2" style="padding-left: 0;"></div>
                            <div class="col-sm-5" style="padding-left: 0;">
                                <div class="form-group">
                                    <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                        <label id="idLblTitleProfilePercent"></label>
                                        <label class="CssRequireField" id="idLblNotePercent"></label>
                                    </label>
                                    <div class="col-sm-7" style="padding-right: 0px;">
                                        <input id="PERCENT_ADD" name="PERCENT_ADD" class="form-control123"/>
                                    </div>
                                </div>
                                <script>
                                    $("#idLblTitleProfilePercent").text(global_fm_percent_cts);
                                    $("#idLblNotePercent").text(global_fm_require_label);
                                </script>
                            </div>
                            <div class="col-sm-5" style="padding-left: 0;">
                                <div class="form-group">
                                    <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                        <label id="idLblTitleProfileTooglePercent"></label>
                                    </label>
                                    <div class="col-sm-7" style="padding-right: 0px;">
                                        <label class="radio-inline" style="font-weight: bold;">
                                            <input type="radio" name="nameCheck" id="nameCheck1" checked>
                                            <label id="idRadioMoney"></label>
                                            <script>$("#idRadioMoney").text(global_fm_rose_type_money);</script>
                                        </label>&nbsp;&nbsp;
                                        <label class="radio-inline" style="font-weight: bold;"><input type="radio" name="nameCheck" id="nameCheck2">
                                            <label id="idRadioPercen"></label>
                                            <script>$("#idRadioPercen").text(global_fm_rose_type_percen);</script>
                                        </label>
                                        <input type="text" id="idSessIsChoise" style="display: none;" name="idSessIsChoise"/>
                                    </div>
                                </div>
                                <script>
                                    $("#idLblTitleProfileTooglePercent").text(global_fm_rose_type);
                                    $("#idLblNotePercent").text(global_fm_require_label);
                                </script>
                            </div>
                            <div class="col-sm-2" style="padding-left: 0;">
                                <div class="form-group">
                                    <input id="btnAPIRoseAdd" class="btn btn-info" type="button" onclick="onFormRoseAdd('<%= anticsrf%>');" />
                                    <script>
                                        document.getElementById("btnAPIRoseAdd").value = global_fm_button_New;
                                        $("#idSessIsChoise").val('1');
                                        $('.radio-inline').on('click', function () {
                                            var s = $(this).find('input').attr('id');
                                            if (s === 'nameCheck1')
                                            {
                                                $("#idSessIsChoise").val('1');
                                            }
                                            if (s === 'nameCheck2')
                                            {
                                                $("#idSessIsChoise").val('0');
                                            }
                                        });
                                    </script>
                                </div>
                            </div>
                            <%
                                PROFILE_DISCOUNT_RATE_DATA[][] resIPData = (PROFILE_DISCOUNT_RATE_DATA[][]) session.getAttribute("SessDiscountRateAccess");
                                int j=1;
                            %>
                            <div class="table-responsive" style="clear: both;">
                            <table id="tblCertUseIP" class="table table-bordered table-striped projects">
                                <thead>
                                <th id="idLblTitleTableIPSTT"></th>
                                <th id="idLblTitleTableIPName"></th>
                                <th id="idLblTitleTableIPProfileRemark"></th>
                                <th id="idLblTitleTableIPPercent"></th>
                                <th id="idLblTitleTableRoseType"></th>
                                <th id="idLblTitleTableIPActive"></th>
                                <script>
                                    $("#idLblTitleTableIPSTT").text(global_fm_STT);
                                    $("#idLblTitleTableIPName").text(global_fm_duration_cts);
                                    $("#idLblTitleTableIPProfileRemark").text(global_fm_Description);
                                    $("#idLblTitleTableIPPercent").text(global_fm_percent_cts);
                                    $("#idLblTitleTableRoseType").text(global_fm_rose_type);
                                    $("#idLblTitleTableIPActive").text(global_fm_action);
                                </script>
                                </thead>
                                <tbody id="idTemplateAssignIP">
                                    <%
                                        if(resIPData != null && resIPData[0].length > 0) {
                                            for(PROFILE_DISCOUNT_RATE_DATA resIPData1 : resIPData[0]) {
                                                String sDeleteID = resIPData1.profileName + j;
                                    %>
                                    <tr>
                                        <td><%= j%></td>
                                        <td><%= resIPData1.profileName %></td>
                                        <td><%= "1".equals(sessLanguageGlobal) ? resIPData1.profileRemark : resIPData1.profileRemarkEN %></td>
                                        <td><%= resIPData1.rosePercent %></td>
                                        <%
                                            if(resIPData1.isMoneyType == true){
                                        %>
                                        <td>
                                            <label id="idRoseType<%=j%>"></label>
                                            <script>$("#idRoseType"+<%=j%>).text(global_fm_rose_type_money);</script>
                                        </td>
                                        <%
                                            } else {
                                        %>
                                        <td>
                                            <label id="idRoseType<%=j%>"></label>
                                            <script>$("#idRoseType"+<%=j%>).text(global_fm_rose_type_percen);</script>
                                        </td>
                                        <%
                                            }
                                        %>
                                        <td>
                                            <a style="cursor: pointer;" class="btn btn-info btn-xs" id="<%=sDeleteID%>"
                                               onclick="onFormRoseDelete('<%=resIPData1.profileName%>');"><i class="fa fa-pencil"></i></a>
                                            <script>
                                                $("#<%=sDeleteID%>").text(global_fm_button_delete);
                                            </script>
                                        </td>
                                    </tr>
                                    <%
                                                j++;
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
                            <div id="greenIP" style="margin: 5px 0 5px 0;"> </div>
                            </div>
                            <script>
                                if (parseInt('<%=j%>')-1 > 0)
                                {
                                    $('#greenIP').smartpaginator({totalrecords: parseInt('<%=j%>')-1, recordsperpage: 10, datacontainer: 'tblCertUseIP', dataelement: 'tr', initval: 0, next: global_paging_last, prev: global_paging_Before, first: global_paging_first, last: global_paging_next, theme: 'green'});
                                }
                                function onFormRoseDelete(activeKey)
                                {
                                    $('body').append('<div id="over"></div>');
                                    $(".loading-gif").show();
                                    $.ajax({
                                        type: "post",
                                        url: "../RoseCommon",
                                        data: {
                                            idParam: 'deleteroseproperties',
                                            activeKey: activeKey
                                        },
                                        cache: false,
                                        success: function (html)
                                        {
                                            var myStrings = sSpace(html).split('#');
                                            if (myStrings[0] === "0")
                                            {
                                                onFormRoseLoad();
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
                                function onFormRoseAdd()
                                {
                                    if (!JSCheckEmptyField($("#PERCENT_ADD").val()))
                                    {
                                        funErrorAlert(policy_req_empty + global_fm_percent_cts);
                                        return false;
                                    }
                                    $('body').append('<div id="over"></div>');
                                    $(".loading-gif").show();
                                    $.ajax({
                                        type: "post",
                                        url: "../RoseCommon",
                                        data: {
                                            idParam: 'addroseproperties',
                                            PERCENT_ADD: $("#PERCENT_ADD").val(),
                                            CERTIFICATION_DURATION: $("#CERTIFICATION_DURATION").val(),
                                            idSessIsChoise: $("#idSessIsChoise").val()
                                        },
                                        cache: false,
                                        success: function (html)
                                        {
                                            var myStrings = sSpace(html).split('#');
                                            if (myStrings[0] === "0")
                                            {
                                                onFormRoseLoad();
                                                funSuccNoLoad(inputcertlist_succ_add);
                                            }
                                            else if (myStrings[0] === JS_EX_CSRF)
                                            {
                                                funCsrfAlert();
                                            } else if (myStrings[0] === 'ROSEPROPERTIES_CODE_EXISTS')
                                            {
                                                funErrorAlert(rose_exists_profile_properties);
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
                                function onFormRoseLoad()
                                {
                                    $.ajax({
                                        type: "post",
                                        url: "../JSONCommon",
                                        data: {
                                            idParam: 'loadpropertiesrose'
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
                                                    var contentProps = "";
                                                    var sCount = 1;
                                                    for (var i = 0; i < obj.length; i++) {
                                                        var idID = obj[i].PROFILE_NAME + '' + obj[i].NO;
                                                        var sRoseType = "";
                                                        if(obj[i].ROSE_TYPE === "1")
                                                        {
                                                            sRoseType = global_fm_rose_type_money;
                                                        } else {
                                                            sRoseType = global_fm_rose_type_percen;
                                                        }
//                                                        var sActiveHTML = "<label class='switch' for='"+idCheckBox+"'><input TYPE='checkbox' class='js-switch' data-switchery='true' "+isChecked+" id='"+idCheckBox+"' onchange=\"onFactorActive('"+obj[i].name+"', '"+idCheckBox+"');\" /><div class='slider round'></div></label>";
                                                        var sActiveHTML = "<a style='cursor: pointer;' class='btn btn-info btn-xs' id='"+idID+"' onclick=\"onFormRoseDelete('"+obj[i].PROFILE_NAME+"');\"><i class='fa fa-pencil'> "+global_fm_button_delete+"</i></a>";
                                                        contentProps += "<tr>" +
                                                            "<td>" + obj[i].NO + "</td>" +
                                                            "<td>" + obj[i].PROFILE_NAME + "</td>" +
                                                            "<td>" + obj[i].PROFILE_REMARK + "</td>" +
                                                            "<td>" + obj[i].PERCENT + "</td>" +
                                                            "<td>" + sRoseType + "</td>" +
                                                            "<td>" + sActiveHTML + "</td>" +
                                                            "</tr>";
                                                        sCount++;
                                                    }
                                                    $("#idTemplateAssignIP").append(contentProps);
                                                    if (sCount > 0)
                                                    {
                                                        $('#greenIP').smartpaginator({totalrecords: sCount, recordsperpage: 10, datacontainer: 'tblCertUseIP', dataelement: 'tr', initval: 0, next: global_paging_last, prev: global_paging_Before, first: global_paging_first, last: global_paging_next, theme: 'green'});
                                                    }
                                                } else if (obj[0].Code === "1") {
                                                    $("#idTemplateAssignIP").empty();
                                                    var contentProps = "<tr><td colspan='6' id='idLblTitleTableNoFile'>"+global_no_data+"</td></tr>";
                                                    $("#idTemplateAssignIP").append(contentProps);
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
                                    $.ajax({
                                        type: "post",
                                        url: "../JSONCommon",
                                        data: {
                                            idParam: 'loadcert_authority_ofsofttoken',
                                            idCA: objCA,
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
                                                    LOAD_CERTIFICATION_PURPOSE(objCA, obj[0].ID, idCSRF);
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
                                    $.ajax({
                                        type: "post",
                                        url: "../JSONCommon",
                                        data: {
                                            idParam: 'loadpki_formfactorby_purpose_nocert',
                                            idPurpose: objPurpose,
                                            CsrfToken: idCSRF
                                        },
                                        cache: false,
                                        success: function (html)
                                        {
                                            if (html.length > 0)
                                            {
                                                var cbxPKI_FORMFACTOR = document.getElementById("PKI_FORMFACTOR");
                                                removeOptions(cbxPKI_FORMFACTOR);
                                                var obj = JSON.parse(html);
                                                if (obj[0].Code === "0") {
                                                    var vFristPKIFormFactor = "";
                                                    for (var i = 0; i < obj.length; i++) {
                                                        if(vFristPKIFormFactor === "") {
                                                            vFristPKIFormFactor = sSpace(obj[i].ID);
                                                        }
                                                        cbxPKI_FORMFACTOR.options[cbxPKI_FORMFACTOR.options.length] = new Option(obj[i].REMARK, obj[i].ID);
                                                    }
                                                    LOAD_PROFILE_BY_FORMFACTOR(objCA, objPurpose, vFristPKIFormFactor, idCSRF);
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
                                                    cbxPKI_FORMFACTOR.options[cbxPKI_FORMFACTOR.options.length] = new Option("---", "");
                                                }
                                                else {
                                                    funErrorAlert(global_errorsql);
                                                }
                                            }
                                        }
                                    });
                                    return false;
                                }
                                function LOAD_PROFILE_BY_FORMFACTOR(objCA, objPurpose, objFactor, idCSRF)
                                {
                                    $.ajax({
                                        type: "post",
                                        url: "../JSONCommon",
                                        data: {
                                            idParam: 'loadcert_purpose',
                                            idCA: objCA,
                                            idPurpose: objPurpose,
                                            idFactor: objFactor,
                                            idAttrType: "",
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
                                                    for (var i = 0; i < obj.length; i++) {
                                                        cbxCERTIFICATION_DURATION.options[cbxCERTIFICATION_DURATION.options.length] = new Option(obj[i].REMARK, obj[i].ID);
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
                            </script>
                        </div>
                    </div>
                </fieldset>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitleCreateUser" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input type="text" readonly name="strCreateUser" class="form-control123" value="<%= rs[0][0].CREATED_BY%>" />
                        </div>
                    </div>
                </div>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitleCreateDate" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input type="text" readonly name="strDateLimit" class="form-control123" value="<%= rs[0][0].CREATED_DT%>" />
                        </div>
                    </div>
                </div>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitleUpdateUser" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input type="text" readonly name="strUpdateUser" class="form-control123" value="<%= EscapeUtils.CheckTextNull(rs[0][0].MODIFIED_BY)%>" />
                        </div>
                    </div>
                </div>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitleUpdateDate" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input type="text" readonly name="strUpdateDate" class="form-control123" value="<%= EscapeUtils.CheckTextNull(rs[0][0].MODIFIED_DT)%>" />
                        </div>
                    </div>
                </div>
            </form>
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
        </div>
        <script src="../style/jquery.min.js"></script>
       <script src="../style/bootstrap.min.js"></script>
    </body> 
</html>