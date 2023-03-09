<%-- 
    Document   : CAEdit
    Created on : May 2, 2018, 9:56:53 AM
    Author     : THANH-PC
--%>

<%@page import="vn.ra.utility.PropertiesContent"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DateFormat"%>
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
        <script type="text/javascript" src="../js/jquery.js"></script>
        <link rel="stylesheet" href="../js/sweetalert.css"/>
        <link href="../style/customportal.min.css" rel="stylesheet">
        <script src="../js/sweetalert-dev.js"></script>
        <script type="text/javascript" src="../Css/GlobalAlert.js"></script>
        <title></title>
        <script type="text/javascript">
            $(document).ready(function () {
                $('#btnClose').click(function () {
                    parent.history.back();
                    return false;
                });
                $('.loading-gif').hide();
                $("#idLblTitleEdits").text(ca_title_edit);
                $("#idLblTitleCode").text(ca_fm_code);
                $("#idLblTitleRemark").text(global_fm_remark_vn);
                $("#idLblNoteRemark").text(global_fm_require_label);
                $("#idLblTitleRemark_EN").text(global_fm_remark_en);
                $("#idLblNoteRemark_EN").text(global_fm_require_label);
                $("#idLblTitleOCSP").text(ca_fm_OCSP);
                $("#idLblNoteOCSP").text(global_fm_require_label);
                $("#idLblTitleProperties").text(global_fm_soap);
                $("#idLblTitleTableSTT").text(global_fm_STT);
                $("#idLblTitleTableType").text(global_fm_type);
                $("#idLblTitleTableValue").text(global_fm_value);
                $("#idLblTitleTableAction").text(global_fm_action);
                $("#idLblTitleGroupCRL").text(ca_group_CRLFile_1);
                $("#idLblTitleCRL").text(ca_fm_CRL);
                $("#idLblNoteCRL").text(global_fm_require_label);
                $("#idLblTitleCRLPath").text(ca_fm_CRLPath);
                $("#idLblNoteCRLPath").text(global_fm_require_label);
                $("#idLblTitleURI").text(ca_fm_URI);
                $("#idLblNoteURI").text(global_fm_require_label);
                $("#idLblTitleTableCRLIssue").text(global_fm_issue);
                $("#idLblTitleTableCRLSize").text(global_fm_size);
                $("#idLblTitleTableCRLDateUpdate").text(global_fm_dateUpdate);
                $("#idLblTitleTableCRLDateNext").text(global_fm_dateUpdate_next);
                $("#idLblTitleCert1").text(ca_fm_Cert_01);
                $("#idLblNoteCert1").text(global_fm_require_label);
                $("#idAShow1").text(global_fm_detail);
                $("#idAHide1").text(global_fm_hide);
                $("#idLblTitleCopy").text(global_fm_copy_all);
                $("#idLblTitleCert1Group").text(ca_group_cert);
                $("#idLblTitleCert1Company").text(global_fm_company);
                $("#idLblTitleCert1Issue").text(global_fm_issue);
                $("#idLblTitleCert1Valid").text(global_fm_valid);
                $("#idLblTitleCert1DateEnd").text(global_fm_dateend);
                $("#idLblTitleCert1Group1").text(ca_group_cert);
                $("#idLblTitleCert1NoCert").text(ca_req_info_cert);
                $("#idLblTitleCheckOCSP").text(ca_fm_CheckOCSP);
                $("#idLblTitleActiveFlag").text(global_fm_active);
                $("#idLblTitleCreateUser").text(global_fm_user_create);
                $("#idLblTitleCreateDate").text(global_fm_date_create);
                $("#idLblTitleUpdateUser").text(global_fm_user_endupdate);
                $("#idLblTitleUpdateDate").text(global_fm_date_endupdate);
                $("#idLblTitleNoData").text(global_no_data);
                $("#idLblTitleTableCRLAction").text(global_fm_action);
            });
            function ValidateForm(idCSRF) {
                if (!JSCheckEmptyField(document.myname.citycode.value))
                {
                    document.myname.citycode.focus();
                    funErrorAlert(policy_req_empty + ca_fm_code);
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
                if (!JSCheckEmptyField(document.myname.OCSP.value))
                {
                    document.myname.OCSP.focus();
                    funErrorAlert(policy_req_empty + ca_fm_OCSP);
                    return false;
                }
                else
                {
                    if (!JSCheckFormatURL(document.myname.OCSP.value)) {
                        document.myname.OCSP.focus();
                        funErrorAlert(global_req_format_http + ca_fm_OCSP);
                        return false;
                    }
                }
                if (!JSCheckEmptyField(document.myname.CRL.value))
                {
                    document.myname.CRL.focus();
                    funErrorAlert(policy_req_empty + ca_fm_CRL);
                    return false;
                }
                else
                {
                    if (!JSCheckFormatURL(document.myname.CRL.value)) {
                        document.myname.CRL.focus();
                        funErrorAlert(global_req_format_http + ca_fm_CRL);
                        return false;
                    }
                }
                if (!JSCheckEmptyField(document.myname.URI.value))
                {
                    document.myname.URI.focus();
                    funErrorAlert(policy_req_empty + ca_fm_URI);
                    return false;
                }
                if (!JSCheckEmptyField(document.myname.CRLPath.value))
                {
                    document.myname.CRLPath.focus();
                    funErrorAlert(policy_req_empty + ca_fm_CRLPath);
                    return false;
                }
                if (!JSCheckEmptyField(document.myname.certificate.value))
                {
                    document.myname.certificate.focus();
                    funErrorAlert(policy_req_empty + ca_fm_Cert_01);
                    return false;
                }
                var snameCheckOCSP = "0";
                if ($("#nameCheckOCSP").is(':checked')) {
                    snameCheckOCSP = "1";
                }
                var sCheckActiveFlag = "0";
                if ($("#ActiveFlag").is(':checked')) {
                    sCheckActiveFlag = "1";
                }
                var sNameUniqueDN = "0";
                if ($("#nameUniqueDN").is(':checked')) {
                    sNameUniqueDN = "1";
                }
                $('body').append('<div id="over"></div>');
                $(".loading-gif").show();
                $.ajax({
                    type: "post",
                    url: "../CACommon",
                    data: {
                        idParam: 'editca',
                        id: document.myname.doID.value,
                        citycode: document.myname.citycode.value,
                        Remark: document.myname.Remark.value,
                        Remark_EN: document.myname.Remark_EN.value,
                        OCSP: document.myname.OCSP.value,
                        CRL: document.myname.CRL.value,
                        URI: document.myname.URI.value,
                        CRLPath: document.myname.CRLPath.value,
                        strCertificate: document.myname.certificate.value,
                        nameCheckOCSP: snameCheckOCSP,
                        nameUniqueDN: sNameUniqueDN,
                        ActiveFlag: sCheckActiveFlag,
                        CsrfToken: idCSRF
                    },
                    cache: false,
                    success: function (html)
                    {
                        var myStrings = sSpace(html).split('#');
                        if (myStrings[0] === "0")
                        {
                            localStorage.setItem("EDIT_CA", document.myname.doID.value);
                            funSuccAlert(ca_succ_edit, "CAList.jsp");
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
                                funErrorAlert(ca_exists_code);
                            } else if (myStrings[1] === "2") {
                                funErrorAlert(ca_exists_name);
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
                $.ajax({
                    type: "post",
                    url: "../SomeCommon",
                    data: {
                        idParam: 'backformpage',
                        idSession: 'SessRefreshCA'
                    },
                    cache: false,
                    success: function (html) {
                        var arr = sSpace(html);
                        if (arr === "0")
                        {
                            window.location = "CAList.jsp";
                        }
                        else
                        {
                            window.location = "CAList.jsp";
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
            .table > thead > tr > th, .table > tbody > tr > td{vertical-align: middle;}
            .table > thead > tr > th{border-bottom: none;}
        </style>
    </head>
    <body>
        <%        
                String anticsrf = "";
                anticsrf = "" + Math.random();
                request.getSession().setAttribute("anticsrf", anticsrf);
        %> 
        <div style="width: 100%; text-align: center; position: fixed;z-index: 1000;top: 0; padding-top: 300px;
             left: 0; height: 100%;" class="loading-gif">
            <img src="../Images/ajax-loader1.gif" alt="Please wait..." />
        </div>
        <div class="x_title">
            <h2><i class="fa fa-list-ul"></i> <span style="color: #36526D;" id="idLblTitleEdits"></span> </h2>
            <ul class="nav navbar-right panel_toolbox">
                <li style="padding-right: 10px;">
                    <input id="btnSave" type="button" class="btn btn-info" data-switch-get="state" onclick="ValidateForm('<%=anticsrf%>');"/>
                    <input id="btnClose" class="btn btn-info" type="button" onclick="closeForm();" />
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
                CERTIFICATION_AUTHORITY[][] rs = new CERTIFICATION_AUTHORITY[1][];
                String ids = "";
                try {
                    ids = EscapeUtils.CheckTextNull(request.getParameter("id"));
                    if (EscapeUtils.IsInteger(ids) == true) {
                        db.S_BO_CERTIFICATION_AUTHORITY_DETAIL(ids, rs);
                        if (rs[0].length > 0) {
                            String sSizeCRL = Definitions.CONFIG_GRID_STRING_NA;
                            String sDateCRL = Definitions.CONFIG_GRID_STRING_NA;
                            String sNextDate = Definitions.CONFIG_GRID_STRING_NA;
                            String sIssuerName = Definitions.CONFIG_GRID_STRING_NA;
                            boolean booHasCRLData = false;
                            if(rs[0][0].CRL_BLOB != null)
                            {
                                booHasCRLData = true;
                                sDateCRL = rs[0][0].LAST_UPDATED_DT;
                                sNextDate = rs[0][0].NEXT_UPDATED_DT;
                                sIssuerName = rs[0][0].ISSUER_SUBJECT;
                                sSizeCRL = String.valueOf(com.convertMoney(rs[0][0].CRL_BLOB.length / 1024));
                            }
                            String sCERTIFICATE = EscapeUtils.CheckTextNull(rs[0][0].CERTIFICATE);
                            String sHasCertificate = "0";
                            if(!"".equals(sCERTIFICATE))
                            {
                                sHasCertificate = "1";
                            }
            %>
            <form name="myname" method="post" class="form-horizontal">
                <input type="hidden" name="CsrfToken" value="<%=anticsrf%>"/>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitleCode" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input type="hidden" name="doID" id="doID" value="<%= ids %>" />
                            <input class="form-control123" readonly maxlength="<%= Definitions.CONFIG_LENGTH_INPUT_NAME%>" id="citycode" name="citycode" value="<%=rs[0][0].NAME%>">
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
                            <input class="form-control123" value="<%=rs[0][0].REMARK%>" maxlength="<%= Definitions.CONFIG_LENGTH_INPUT_REMARK%>" id="Remark" name="Remark"/>
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
                            <input class="form-control123" value="<%=rs[0][0].REMARK_EN%>" maxlength="<%= Definitions.CONFIG_LENGTH_INPUT_REMARK%>" id="Remark_EN" name="Remark_EN"/>
                        </div>
                    </div>
                </div>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;">
                            <label id="idLblTitleOCSP"></label>
                            <label class="CssRequireField" id="idLblNoteOCSP"></label>
                        </label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input type="text" class="form-control123" maxlength="200" id="OCSP" value="<%= EscapeUtils.CheckTextNull(rs[0][0].OCSP_URI)%>" name="OCSP">
                        </div>
                    </div>
                </div>
                <div class="form-group" style="padding: 10px 0px 0 0px;margin: 0;">
                    <fieldset class="scheduler-border">
                        <legend class="scheduler-border"><span id="idLblTitleProperties"></span></legend>
                        <div class="form-group" style="text-align: right; padding: 0px 0px 0 0px;margin: 0;">
                            <input type="button" class="btn btn-info" id="bntNewSoap" name="bntNewSoap" onclick="ShowDialogNewProps();"/>
                            <script>
                                document.getElementById("bntNewSoap").value = global_fm_button_New;
                            </script>
                        </div>
                        <div class="table-responsive">
                            <table class="table table-bordered table-striped projects">
                                <thead>
                                <th id="idLblTitleTableSTT"></th>
                                <th id="idLblTitleTableType"></th>
                                <th id="idLblTitleTableValue"></th>
                                <th id="idLblTitleTableAction"></th>
                                </thead>
                                <tbody id="idTbodyProps">
                                    <%
                                        PropertiesContent ss = new PropertiesContent();
                                        ArrayList<ProObj> list;
                                        int s = 1;
                                        list = ss.getPropertiesContent(EscapeUtils.CheckTextNull(rs[0][0].PROPERTIES));
                                        for (int i = 0; i < list.size(); i++) {
                                            String strID = list.get(i).getKey().trim();
                                            String sValueSub = list.get(i).getValue();
                                            if (sValueSub.length() > 58) {
                                                sValueSub = sValueSub.substring(0, 58) + "...";
                                            }
                                    %>
                                    <tr>
                                        <td><%= com.convertMoney(s)%></td>
                                        <td><%=strID%></td>
                                        <td><%=sValueSub%></td>
                                        <td>
                                            <a style="cursor: pointer;" onclick="ShowDialogEditProps('<%= ids%>', '<%= strID%>');" class="btn btn-info btn-xs" id="idLblTitleTableEdit<%=s%>">
                                            </a>
                                            <a style="cursor: pointer;" onclick="deleteProps('<%= ids%>', '<%= strID%>', '<%=anticsrf%>');" class="btn btn-info btn-xs" id="idLblTitleTableDelete<%=s%>">
                                            </a>
                                            <script>
                                                $("#idLblTitleTableEdit"+'<%=s%>').append("<i class='fa fa-pencil'></i> " + global_button_grid_edit);
                                                $("#idLblTitleTableDelete"+'<%=s%>').append("<i class='fa fa-pencil'></i> "+global_button_grid_delete);
                                            </script>
                                        </td>
                                    </tr>
                                    <%
                                            s++;
                                        }
                                    %>
                                </tbody>
                            </table>
                            <script>
                                function deleteProps(id, value)
                                {
                                    swal({
                                        title: "",
                                        text: global_conform_delete_soap,
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
                                                url: "../CACommon",
                                                data: {
                                                    idParam: 'deleteproperties',
                                                    id: id,
                                                    value: value,
                                                    CsrfToken: '<%=anticsrf%>'
                                                },
                                                cache: false,
                                                success: function (html) {
                                                    var myStrings = sSpace(html).split('#');
                                                    if (myStrings[0] === "0")
                                                    {
                                                        LOAD_PROPERTIES_LIST(id, '');
                                                        funSuccNoLoad(global_succ_delete_soap);
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
                                                        funErrorAlert(global_errorsql);
                                                    }
                                                    $(".loading-gif").hide();
                                                    $(".loading-gifHardwareEdit").hide();
                                                    $(".loading-gifHardwareNew").hide();
                                                    $('#over').remove();
                                                }
                                            });
                                            return false;
                                        }, JS_STR_ACTION_TIMEOUT);
                                                    $('#over').remove();
                                    });
                                }
                            </script>
                        </div>
                    </fieldset>
                </div>
                <fieldset class="scheduler-border">
                    <legend class="scheduler-border"><label id="idLblTitleGroupCRL"></label></legend>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;">
                                <label id="idLblTitleCRL"></label>
                                <label class="CssRequireField" id="idLblNoteCRL"></label>
                            </label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input type="text" class="form-control123" value="<%= EscapeUtils.CheckTextNull(rs[0][0].CRL_URI)%>" maxlength="200" id="CRL" name="CRL"/>
                            </div>
                        </div>
                    </div>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;">
                                <label id="idLblTitleCRLPath"></label>
                                <label class="CssRequireField" id="idLblNoteCRLPath"></label>
                            </label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input type="text" class="form-control123" value="<%= EscapeUtils.CheckTextNull(rs[0][0].CA_URI)%>" maxlength="200" id="CRLPath" name="CRLPath"/>
                            </div>
                        </div>
                    </div>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;">
                                <label id="idLblTitleURI"></label>
                                <label class="CssRequireField" id="idLblNoteURI"></label>
                            </label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input type="text" class="form-control123" value="<%= EscapeUtils.CheckTextNull(rs[0][0].URI)%>" maxlength="200" id="URI" name="URI"/>
                                <% if (booHasCRLData == true) { %>
                                <div style="width: 100%; height: 5px;"></div>
                                <a id="idAShowCRL" style="cursor: pointer; color: blue; text-decoration: underline;display: none;" onclick="popupViewCRL();"><script>document.write(global_fm_detail);</script></a>&nbsp;
                                <a id="idAHideCRL" style="cursor: pointer; color: blue; text-decoration: underline;" onclick="popupHideCRL();"><script>document.write(global_fm_hide);</script></a>
                                <% } %>
                            </div>
                        </div>
                    </div>
                    <script>
                        function popupViewCRL()
                        {
                            document.getElementById('idAShowCRL').style.display = 'none';
                            document.getElementById('idAHideCRL').style.display = '';
                            document.getElementById('idShowCRLFile').style.display = '';
                        }
                        function popupHideCRL()
                        {
                            document.getElementById('idAShowCRL').style.display = '';
                            document.getElementById('idAHideCRL').style.display = 'none';
                            document.getElementById('idShowCRLFile').style.display = 'none';
                        }
                    </script>
                    <div id="idShowCRLFile" class="form-group" style="padding: 10px 0px 0 0px;margin: 0; display: none;">
                        <div class="table-responsive">
                        <table class="table table-bordered table-striped projects">
                            <thead>
                            <th id="idLblTitleTableCRLIssue"></th>
                            <th id="idLblTitleTableCRLSize"></th>
                            <th id="idLblTitleTableCRLDateUpdate"></th>
                            <th id="idLblTitleTableCRLDateNext"></th>
                            <th id="idLblTitleTableCRLAction"></th>
                            </thead>
                            <tbody>
                                <tr>
                                    <td id="idTdIssuerCRL"><%= sIssuerName%></td>
                                    <td id="idTdSizeCRL"><%= sSizeCRL%></td>
                                    <td id="idTdDateCRL"><%= sDateCRL%></td>
                                    <td id="idTdNextCRL"><%= sNextDate%></td>
                                    <td id="idTdActionCRL">
                                        <%
                                            if(booHasCRLData == true) {
                                        %>
                                        <a style="cursor: pointer;" class="btn btn-info btn-xs" onclick="downRecord('<%= anticsrf%>');">
                                            <i class="fa fa-pencil"></i> <span id="idLblTitleTableCRLDown"></span> 
                                        </a>
                                        <script>
                                            $("#idLblTitleTableCRLDown").text(global_fm_down);
                                        </script>
                                        <%
                                            }
                                        %>
                                        <a style="cursor: pointer;" class="btn btn-info btn-xs" onclick="SubmitReload('<%=anticsrf%>');">
                                            <i class="fa fa-pencil"></i> <span id="idLblTitleTableCRLReload"></span> 
                                        </a>
                                        <script>
                                            $("#idLblTitleTableCRLReload").text(global_fm_button_reload);
                                        </script>
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                        </div>
                    </div>
                    <div class="form-group" style="padding: 10px 0px 0 0px;margin: 0;display: none;">
                        <input type="file" id="fileUploadCRL1" style="width: 100%;" accept=".crl,.CRL"
                           class="btn btn-default btn-file select_file" onchange="calUploadCRL(this, '<%=anticsrf%>');">
                    </div>
                </fieldset>
                <div class="col-sm-13" style="padding-left: 0;">
                    <div class="form-group">
                        <label class="control-label col-sm-2" style="color: #000000; font-weight: bold;text-align: left;">
                            <label id="idLblTitleCert1"></label>
                            <label class="CssRequireField" id="idLblNoteCert1"></label>
                        </label>
                        <div class="col-sm-10" style="padding-right: 0px;">
                            <textarea name="certificate" style="height: 85px;display: none;" readonly id="certificate" class="form-control123">
                                <%= sCERTIFICATE%>
                            </textarea>
                            <INPUT class="form-control123" id="input-file" NAME="xls_filename" accept=".cer,.txt,.pem"
                                TYPE="file" onchange="UploadCertificate(this);" />
                        </div>
                    </div>
                    <div class="form-group">
                        <a id="idAShow1" style="cursor: pointer; color: blue; text-decoration: underline;" onclick="popupViewCTS('<%=anticsrf%>');"></a>&nbsp;
                        <a id="idAHide1" style="cursor: pointer; color: blue; text-decoration: underline;display: none;" onclick="popupHideCTS1();"></a>&nbsp;
                        <a style="cursor: pointer; color: blue; text-decoration: underline;" onclick="copyToClipboard($('#certificate').val());" id="idLblTitleCopy"></a>&nbsp;
                    </div>
                </div>
                <div id="idViewCSR1" class="form-group" style="display: none;padding: 0px 0px 0 0px;margin: 0;">
                    <fieldset class="scheduler-border">
                        <legend class="scheduler-border" id="idLblTitleCert1Group"></legend>
                        <div class="form-group" style="padding: 0px;margin: 0;">
                            <label class="control-label123" id="idLblTitleCert1Company"></label>
                            <textarea id="idCompany1" class="form-control123" readonly="true" name="idCompany1" style="height: 120px;"></textarea>
                        </div>
                        <div class="form-group" style="padding: 10px 0px 0 0px;margin: 0;">
                            <label class="control-label123" id="idLblTitleCert1Issue"></label>
                            <textarea id="idIssuer1" class="form-control123" readonly="true" name="idIssuer1" style="height: 120px;"></textarea>
                        </div>
                        <div class="form-group" style="padding: 10px 0px 0 0px;margin: 0;">
                            <label class="control-label123" id="idLblTitleCert1Valid"></label>
                            <input id="idValid1" class="form-control123" type="text" readonly="true" />
                        </div>
                        <div class="form-group" style="padding: 10px 0px 0 0px;margin: 0;">
                            <label class="control-label123" id="idLblTitleCert1DateEnd"></label>
                            <input id="idExpired1" class="form-control123" type="text" readonly="true" />
                        </div>
                    </fieldset>
                </div>
                <div id="idViewCSRNoData1" class="form-group" style="display: none;padding: 10px 0px 0 0px;margin: 0;">
                    <fieldset class="scheduler-border">
                        <legend class="scheduler-border" id="idLblTitleCert1Group1"></legend>
                        <div class="form-group" style="padding: 0px;margin: 0;">
                            <label class="control-label123" id="idLblTitleCert1NoCert"></label>
                        </div>
                    </fieldset>
                </div>
                <script>
                    $(document).ready(function () {
                        if('<%= sHasCertificate%>' === "1")
                        {
                            popupViewCTS('<%=anticsrf%>', '1');
                        }
                    });
                    function calUploadCRL(input1, idCSRF)
                    {
                        if (input1.value !== '')
                        {
                            var checkFileName = input1.value.substring(input1.value.lastIndexOf('.') + 1);
                            if(checkFileName === "crl" || checkFileName === "CRL")
                            {
                                $('body').append('<div id="over"></div>');
                                $(".loading-gif").show();
                                file1 = input1.files[0];
                                var data1 = new FormData();
                                data1.append('file', file1);
                                $.ajax({
                                    url: '../UploadFileCRL',
                                    data: data1,
                                    cache: false,
                                    contentType: false,
                                    processData: false,
                                    type: 'POST',
                                    enctype: "multipart/form-data",
                                    success: function (html) {
                                        var mess = sSpace(html).split('###');
                                        if (mess[0] === "0")
                                        {
                                            $.ajax({
                                                type: "post",
                                                url: "../CACommon",
                                                data: {
                                                    idParam: 'uploadcrl',
                                                    CAID: $("#doID").val(),
                                                    UrlCRL: mess[1],
                                                    CsrfToken: idCSRF
                                                },
                                                cache: false,
                                                success: function (htmlLast)
                                                {
                                                    var messLast = sSpace(htmlLast).split('#');
                                                    if (messLast[0] === "0")
                                                    {
                                                        $('#idTdIssuerCRL').html(messLast[3]);
                                                        $('#idTdSizeCRL').html(messLast[4]);
                                                        $('#idTdDateCRL').html(messLast[1]);
                                                        $('#idTdNextCRL').html(messLast[2]);
                                                        funSuccNoLoad(ca_succ_import_crl1);
                                                    }
                                                    else if (messLast[0] === "1")
                                                    {
                                                        funErrorAlert(ca_error_import_crl1);
                                                    }
                                                    else if (messLast[0] === JS_EX_CSRF) {
                                                        funCsrfAlert();
                                                    } else if (messLast[0] === JS_EX_LOGIN) {
                                                        RedirectPageLoginNoSess(global_alert_login);
                                                    }
                                                    else if (messLast[0] === JS_EX_ANOTHERLOGIN)
                                                    {
                                                        RedirectPageLoginNoSess(global_alert_another_login);
                                                    } else {
                                                        funErrorAlert(global_errorsql);
                                                    }
                                                    $(".loading-gif").hide();
                                                    $('#over').remove();
                                                }
                                            });
                                            return false;
                                        }
                                        else if (mess[0] === JS_EX_LOGIN) {
                                            RedirectPageLoginNoSess(global_alert_login);
                                        }
                                        else if (mess[0] === JS_EX_ANOTHERLOGIN)
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
                                funErrorAlert(global_req_crl_format);
                            }
                        }
                        else
                        {
                            funErrorAlert(global_req_file);
                        }
                    }
                    function SubmitReload(idCSRF)
                    {
                        $('body').append('<div id="over"></div>');
                        $(".loading-gif").show();
                        $.ajax({
                            type: "post",
                            url: "../CACommon",
                            CAID: $("#doID").val(),
                            data: {
                                idParam: 'reloadcrl',
                                CAID: $("#doID").val(),
                                CsrfToken: idCSRF
                            },
                            cache: false,
                            success: function (html)
                            {
                                var messLast = sSpace(html).split('#');
                                if (messLast[0] === "0")
                                {
                                    var sCSRF = '<%= anticsrf%>';
                                    popupViewCRL();
                                    $('#idTdIssuerCRL').html(messLast[3]);
                                    $('#idTdSizeCRL').html(messLast[4]);
                                    $('#idTdDateCRL').html(messLast[1]);
                                    $('#idTdNextCRL').html(messLast[2]);
                                    var sActionCRL = '<a style=\'cursor: pointer;\' class=\'btn btn-info btn-xs\' \n\
                                        onclick="downRecord(\''+ sCSRF +'\');"><i class="fa fa-pencil">\n\
                                        </i> ' + global_fm_down + '</a>&nbsp;<a style="cursor: pointer;" class="btn btn-info\n\
                                        btn-xs" onclick="SubmitReload(\''+ sCSRF +'\');">\n\
                                        <i class="fa fa-pencil"></i> ' + global_fm_button_reload + '</a>';
                                    $('#idTdActionCRL').html(sActionCRL);
                                    funSuccNoLoad(ca_succ_reload);
                                }
                                else if (messLast[0] === JS_STR_GRID_STRING_NA)
                                {
                                    funCsrfAlert();
                                }
                                else if (messLast[0] === JS_EX_CSRF)
                                {
                                    funCsrfAlert();
                                } else if (messLast[0] === JS_EX_LOGIN)
                                {
                                    RedirectPageLoginNoSess(global_alert_login);
                                }
                                else if (messLast[0] === JS_EX_ANOTHERLOGIN)
                                {
                                    RedirectPageLoginNoSess(global_alert_another_login);
                                }
                                else {
                                    document.getElementById('idAShowCRL').style.display = 'none';
                                    document.getElementById('idAHideCRL').style.display = 'none';
                                    document.getElementById('idShowCRLFile').style.display = 'none';
                                    funErrorAlert(global_errorsql);
                                }
                                $(".loading-gif").hide();
                                $('#over').remove();
                            }
                        });
                        return false;
                    }
                    function downRecord(idCSRF) {
                        $('body').append('<div id="over"></div>');
                        $(".loading-gif").show();
                        $.ajax({
                            type: "post",
                            url: "../DownloadFileCSR",
                            data: {
                                idParam: 'crlhasid',
                                CAID: $("#doID").val(),
                                CsrfToken: idCSRF
                            },
                            cache: false,
                            success: function (html)
                            {
                                var myStrings = sSpace(html).split('#');
                                if (myStrings[0] === "0")
                                {
                                    $(".loading-gif").hide();
                                    $('#over').remove();
                                    var f = document.myname;
                                    f.method = "post";
                                    f.action = '../DowloadFile?filename=' + myStrings[1];
                                    f.submit();
                                }
                                else if (myStrings[0] === JS_EX_CSRF)
                                {
                                    funCsrfAlert();
                                }
                                else if (myStrings[0] === JS_EX_NO_DATA_WRITE)
                                {
                                    funErrorAlert(global_no_data);
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
                    function UploadCertificate(input1)
                    {
                        if (input1.value !== '')
                        {
                            var checkFileName = input1.value.substring(input1.value.lastIndexOf('.') + 1);
                            if(checkFileName === "cer" || checkFileName === "txt" || checkFileName === "CER"
                                || checkFileName === "TXT" || checkFileName === "pem" || checkFileName === "PEM")
                            {
                                $('body').append('<div id="over"></div>');
                                $(".loading-gif").show();
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
                                            $("textarea#certificate").val(myStrings[1]);
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
                    function popupViewCTS(idCSRF)
                    {
                        if (document.myname.certificate.value === "")
                        {
                            document.myname.certificate.focus();
                            funErrorAlert(policy_req_empty + ca_fm_Cert_01);
                            return false;
                        }
                        $.ajax({
                            type: "post",
                            url: "../CertificateParse",
                            data: {
                                sCert: $("#certificate").val(),
                                CsrfToken: idCSRF
                            },
                            cache: false,
                            success: function (html)
                            {
                                var myStrings = sSpace(html).split('###');
                                if (myStrings[0] === "0")
                                {
                                    $("#idCompany1").val(myStrings[1].replace(/, /g, "\n"));
                                    $("#idIssuer1").val(myStrings[2].replace(/, /g, "\n"));
                                    $("#idValid1").val(myStrings[3]);
                                    $("#idExpired1").val(myStrings[4]);
                                    document.getElementById('idViewCSR1').style.display = '';
                                    document.getElementById('idViewCSRNoData1').style.display = 'none';
                                    document.getElementById('idAHide1').style.display = '';
                                    document.getElementById('idAShow1').style.display = 'none';
                                }
                                else if (myStrings[0] === "1") {
                                    $("#idCompany1").val('');
                                    $("#idIssuer1").val('');
                                    $("#idValid1").val('');
                                    $("#idExpired1").val('');
                                    document.getElementById('idViewCSR1').style.display = 'none';
                                    document.getElementById('idViewCSRNoData1').style.display = '';
                                    document.getElementById('idAHide1').style.display = '';
                                    document.getElementById('idAShow1').style.display = 'none';
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
                                    funErrorAlert(global_errorsql);
                                }
                            }
                        });
                        return false;
                    }
                    function popupHideCTS1()
                    {
                        $("#idCompany1").val('');
                        $("#idIssuer1").val('');
                        $("#idValid1").val('');
                        $("#idExpired1").val('');
                        document.getElementById('idViewCSR1').style.display = 'none';
                        document.getElementById('idViewCSRNoData1').style.display = 'none';
                        document.getElementById('idAHide1').style.display = 'none';
                        document.getElementById('idAShow1').style.display = '';
                    }
                </script>
                <div class="form-group" style="padding: 10px 0px 0 0px;margin: 0;"></div>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitleCheckOCSP" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input TYPE="checkbox" <%=rs[0][0].OCSP_PRIORITY_ENABLED ? "checked" : ""%> id="nameCheckOCSP" name="nameCheckOCSP" />
                        </div>
                    </div>
                </div>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitleUniqueDN" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input TYPE="checkbox" <%= "1".equals(rs[0][0].ENFORCE_UNIQUE_DN) ? "checked" : ""%> id="nameUniqueDN" name="nameUniqueDN" />
                        </div>
                    </div>
                    <script>
                        $("#idLblTitleUniqueDN").text(ca_fm_unique_DN);
                    </script>
                </div>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label id="idLblTitleActiveFlag" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;"></label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input TYPE="checkbox" id="ActiveFlag" name="ActiveFlag" <%=rs[0][0].ENABLED ? "checked" : ""%> />
                        </div>
                    </div>
                </div>
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
                            <input type="text" readonly="true" name="dates" class="form-control123" value="<%= rs[0][0].CREATED_DT%>" />
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
                            <input type="text" readonly="true" name="UpdateDate" class="form-control123" value="<%= rs[0][0].MODIFIED_DT%>" />
                        </div>
                    </div>
                </div>
            </form>
            <%
                } else
                {
            %>
            <div class="form-group" style="padding: 0px 0px 0 0px;margin: 0;text-align: center;">
                <label style="color: red;" id="idLblTitleNoData"></label>
            </div>
            <%
                    }
                } else
                    {
            %>
            <div class="form-group" style="padding: 0px 0px 0 0px;margin: 0;text-align: center;">
                <label style="color: red;" id="idLblTitleNoData"></label>
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
        <script>
            $(document).ready(function () {
                $('#myModalPropsEdit').modal({
                    backdrop: 'static',
                    keyboard: true,
                    show: false
                });
                $('#myModalPropsNew').modal({
                    backdrop: 'static',
                    keyboard: true,
                    show: false
                });
            });
            function ShowDialogEditProps(id, type)
            {
                $('#myModalPropsEdit').modal('show');
                LOAD_PROPERTIES_LIST(id, type);
                $(".loading-gifHardwareEdit").hide();
                $(".loading-gif").hide();
                $('#over').remove();
            }
            function ShowDialogNewProps()
            {
                $('#myModalPropsNew').modal('show');
                $(".loading-gifHardwareNew").hide();
                $(".loading-gif").hide();
                $('#over').remove();
            }
            function CloseDialogEditProps()
            {
                $('#idPropsEditType').val('');
                $('textarea#idPropsEditValue').val('');
                $('#myModalPropsEdit').modal('hide');
                $(".loading-gifHardwareEdit").hide();
                $(".loading-gif").hide();
                $('#over').remove();
            }
            function CloseDialogNewProps()
            {
                $('#idPropsNewType').val('');
                $('textarea#idPropsNewValue').val('');
                $('#myModalPropsNew').modal('hide');
                $(".loading-gifHardwareNew").hide();
                $(".loading-gif").hide();
                $('#over').remove();
            }
            function LOAD_PROPERTIES_LIST(id, type)
            {
                $('body').append('<div id="over"></div>');
                $(".loading-gifHardwareEdit").show();
                $.ajax({
                    type: "post",
                    url: "../JSONCommon",
                    data: {
                        idParam: 'getpropertiesofca',
                        id: id,
                        type: type
                    },
                    cache: false,
                    success: function (html)
                    {
                        if (html.length > 0)
                        {
                            console.log(html);
                            var obj = JSON.parse(html);
                            if (obj[0].Code === "0")
                            {
                                if(type === "")
                                {
                                    $("#idTbodyProps").empty();
                                    var contentProps = "";
                                    for (var i = 0; i < obj.length; i++) {
                                        var sActionHTML = '<a style="cursor: pointer;" onclick="ShowDialogEditProps(\''+id+'\', \''+obj[i].PROPS_KEY+'\');"\n\
                                            class="btn btn-info btn-xs"><i class="fa fa-pencil"></i> '+ global_button_grid_edit +' </a>\n\
                                            <a style="cursor: pointer;" onclick="deleteProps(\''+id+'\', \''+obj[i].PROPS_KEY+'\');"\n\
                                            class="btn btn-info btn-xs"><i class="fa fa-pencil"></i> '+global_button_grid_delete+' </a>';
                                        contentProps += "<tr>" +
                                            "<td>" + obj[i].NO + "</td>" +
                                            "<td>" + obj[i].PROPS_KEY + "</td>" +
                                            "<td>" + obj[i].PROPS_VALUE + "</td>" +
                                            "<td>" + sActionHTML + "</td>" +
                                            "</tr>";
                                    }
                                    $("#idTbodyProps").append(contentProps);
                                }
                                else
                                {
                                    $("#idPropsEditType").val(obj[0].PROPS_KEY);
                                    $("textarea#idPropsEditValue").val(obj[0].PROPS_VALUE);
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
        <!-- Modal Edit Properties -->
        <div id="myModalPropsEdit" class="modal fade" role="dialog">
            <div style="width: 100%; text-align: center; position: fixed;z-index: 1000;top: 0; padding-top: 90px;
                 left: 0; height: 100%;" class="loading-gifHardwareEdit">
                <img src="../Images/ajax-loader1.gif" alt="Please wait..." />
            </div>
            <div class="modal-dialog modal-800" id="myModalPropsEdit">
                <div class="modal-content">
                    <div class="modal-header">
                        <div style="width: 70%; float: left;">
                            <h3 class="modal-title" style="font-size: 18px;" id="idLblTitleModalEditProps"></h3>
                            <script>
                                $("#idLblTitleModalEditProps").text(global_title_soap_edit);
                            </script>
                        </div>
                        <div style="width: 29%; float: right;text-align: right;">
                            <input type="button" id="btnOKEdit" data-switch-get="state" class="btn btn-info" onclick="dialogPropsUpdate('<%= ids%>', '<%= anticsrf%>');" />
                            <input type="button" id="btnClosePropsEdit" class="btn btn-info" onclick="CloseDialogEditProps();" />
                            <script>
                                document.getElementById("btnOKEdit").value = login_fm_buton_OK;
                                document.getElementById("btnClosePropsEdit").value = global_fm_button_close;
                            </script>
                        </div>
                    </div>
                    <div class="modal-body">
                        <form role="formPropsEdit" id="formPropsEdit">
                            <div class="form-group" style="padding: 0px 0px 0 0px;margin: 0;">
                                <label class="control-label123" id="idLblTitlePropsEditType"></label>
                                <label class="CssRequireField" id="idLblNotePropsEditType"></label>
                                <input name="idPropsEditType" readonly="true" id="idPropsEditType" class="form-control123" type="text"/>
                            </div>
                            <div class="form-group" style="padding: 10px 0px 0 0px;margin: 0;">
                                <label class="control-label123" id="idLblTitlePropsEditValue"></label>
                                <label class="CssRequireField" id="idLblNotePropsEditValue"></label>
                                <!--<input class="form-control123" id="idPropsEditValue" name="idPropsEditValue" type="text"/>-->
                                <textarea name="idPropsEditValue" style="height: 85px;" id="idPropsEditValue" class="form-control123">
                                </textarea>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
             <script>
                $(document).ready(function () {
                    $("#idLblTitlePropsEditType").text(global_fm_type);
                    $("#idLblNotePropsEditType").text(global_fm_require_label);
                    $("#idLblTitlePropsEditValue").text(global_fm_value);
                    $("#idLblNotePropsEditValue").text(global_fm_require_label);
                });
                function dialogPropsUpdate(id, idCSRF)
                {
                    var sType = $("#idPropsEditType").val();
                    var sValue = $("textarea#idPropsEditValue").val();
                    if (!JSCheckEmptyField(sType) || !JSCheckEmptyField(sValue))
                    {
                        funErrorAlert(global_req_all);
                        return false;
                    }
                    $('body').append('<div id="over"></div>');
                    $(".loading-gifHardwareEdit").show();
                    $.ajax({
                        type: "post",
                        url: "../CACommon",
                        data: {
                            idParam: 'editproperties',
                            id: id,
                            nameType: sType,
                            nameValue: sValue,
                            CsrfToken: idCSRF
                        },
                        cache: false,
                        success: function (html)
                        {
                            var myStrings = sSpace(html).split('#');
                            if (myStrings[0] === "0")
                            {
                                LOAD_PROPERTIES_LIST(id, '');
                                funSuccNoLoad(global_succ_edit_soap);
                                CloseDialogEditProps();
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
                            $(".loading-gifHardwareEdit").hide();
                            $('#over').remove();
                        }
                    });
                    return false;
                }
            </script>
        </div>
        <!-- Modal Edit Properties -->
        <!-- Modal New Properties -->
        <div id="myModalPropsNew" class="modal fade" role="dialog">
            <div style="width: 100%; text-align: center; position: fixed;z-index: 1000;top: 0; padding-top: 90px;
                 left: 0; height: 100%;" class="loading-gifHardwareNew">
                <img src="../Images/ajax-loader1.gif" alt="Please wait..." />
            </div>
            <div class="modal-dialog modal-800" id="myModalPropsNew">
                <input type="hidden" id="idPropsNewId" name="idPropsNewId" value="<%=ids%>" />
                <div class="modal-content">
                    <div class="modal-header">
                        <div style="width: 70%; float: left;">
                            <h3 class="modal-title" style="font-size: 18px;" id="idLblTitleModalNewProps"></h3>
                            <script>
                                $("#idLblTitleModalNewProps").text(global_title_propeties_ca_add);
                            </script>
                        </div>
                        <div style="width: 29%; float: right;text-align: right;">
                            <input type="button" id="btnOKNew" data-switch-get="state" class="btn btn-info" onclick="dialogPropsAdd('<%= ids%>', '<%= anticsrf%>');" />
                            <input type="button" id="btnClosePropsNew" class="btn btn-info" onclick="CloseDialogNewProps();" />
                            <script>
                                document.getElementById("btnOKNew").value = login_fm_buton_OK;
                                document.getElementById("btnClosePropsNew").value = global_fm_button_close;
                            </script>
                        </div>
                    </div>
                    <div class="modal-body">
                        <form role="formPropsNew" id="formPropsNew">
                            <div class="form-group" style="padding: 0px 0px 0 0px;margin: 0;">
                                <label class="control-label123" id="idLblTitlePropsNewType"></label>
                                <label class="CssRequireField" id="idLblNotePropsNewType"></label>
                                <input name="idPropsNewType" id="idPropsNewType" class="form-control123" type="text"/>
                            </div>
                            <div class="form-group" style="padding: 10px 0px 0 0px;margin: 0;">
                                <label class="control-label123" id="idLblTitlePropsNewValue"></label>
                                <label class="CssRequireField" id="idLblNotePropsNewValue"></label>
                                <!--<input class="form-control123" id="idPropsNewValue" name="idPropsNewValue" type="text"/>-->
                                <textarea name="idPropsNewValue" style="height: 85px;" id="idPropsNewValue" class="form-control123">
                                </textarea>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
            <script>
                $(document).ready(function () {
                    $("#idLblTitlePropsNewType").text(global_fm_type);
                    $("#idLblNotePropsNewType").text(global_fm_require_label);
                    $("#idLblTitlePropsNewValue").text(global_fm_value);
                    $("#idLblNotePropsNewValue").text(global_fm_require_label);
                });
                function dialogPropsAdd(id, idCSRF)
                {
                    var sType = $("#idPropsNewType").val();
                    var sValue = $("textarea#idPropsNewValue").val();
                    if (!JSCheckEmptyField(sType) || !JSCheckEmptyField(sValue))
                    {
                        funErrorAlert(global_req_all);
                        return false;
                    }
                    $('body').append('<div id="over"></div>');
                    $(".loading-gifHardwareNew").show();
                    $.ajax({
                        type: "post",
                        url: "../CACommon",
                        data: {
                            idParam: 'addproperties',
                            id: id,
                            nameType: sType,
                            nameValue: sValue,
                            CsrfToken: idCSRF
                        },
                        cache: false,
                        success: function (html)
                        {
                            var myStrings = sSpace(html).split('#');
                            if (myStrings[0] === "0")
                            {
                                LOAD_PROPERTIES_LIST(id, '');
                                funSuccNoLoad(global_succ_add_soap);
                                CloseDialogNewProps();
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
                            $(".loading-gifHardwareNew").hide();
                            $('#over').remove();
                        }
                    });
                    return false;
                }
            </script>
        </div>
        <!-- Modal New Properties -->
        <script src="../style/jquery.min.js"></script>
        <script src="../style/bootstrap.min.js"></script>
        <!--<script src="../style/custom.min.js"></script>-->
        <link href="../js/checkphone/intlTelInput.css" rel="stylesheet" type="text/css"/>
        <script src="../js/checkphone/intlTelInput.js" type="text/javascript"></script>
        <script src="../js/active/highlight.js"></script>
        <script src="../js/active/bootstrap-switch.js"></script>
        <script src="../js/active/main.js"></script>
    </body>
</html>