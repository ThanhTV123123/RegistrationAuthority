<%-- 
    Document   : OwnerRegister
    Created on : Nov 1, 2019, 5:10:14 PM
    Author     : USER
--%>

<%@page import="vn.ra.utility.PropertiesContent"%>
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
        <script src="../js/Language.js"></script>
        <script src="../js/process_javajs.js"></script>
        <script type="text/javascript" src="../js/jquery.js"></script>
        <link rel="stylesheet" href="../js/sweetalert.css"/>
        <script src="../js/sweetalert-dev.js"></script>
        <link href="../style/customportal.min.css" rel="stylesheet">
        <script type="text/javascript" src="../Css/GlobalAlert.js"></script>
        <title></title>
        <script type="text/javascript">
            document.title = owner_title_add;
            changeFavicon("../");
            $(document).ready(function () {
                $('.loading-gif').hide();
            });
            function onBlurToUppercase(obj)
            {
                obj.value = obj.value.toUpperCase();
            }
            function ValidateForm(idCSRF) {
                var isChooseUID = $("#idSessIsChoise").val();
                var isValueDNUID = "";
                var isValueDNName = "";
                var isValueCNUID = "";
                var isValueCNName = "";
                if(isChooseUID === "1") {
                    if (!JSCheckEmptyField(document.myname.INPUT_MST_MNS.value))
                    {
                        document.myname.INPUT_MST_MNS.focus();
                        funErrorAlert(policy_req_empty + owner_fm_private_uid);
                        return false;
                    }
                    if (!JSCheckEmptyField(document.myname.COMPANY_NAME.value))
                    {
                        document.myname.COMPANY_NAME.focus();
                        funErrorAlert(policy_req_empty + global_fm_grid_company);
                        return false;
                    }
                    isValueDNUID = document.myname.INPUT_MST_MNS.value;
                    isValueDNName = document.myname.COMPANY_NAME.value;
                    isValueCNUID = "";
                    isValueCNName = "";
                } else if(isChooseUID === "0") {
                    if (!JSCheckEmptyField(document.myname.INPUT_CMND_HC.value))
                    {
                        document.myname.INPUT_CMND_HC.focus();
                        funErrorAlert(policy_req_empty + owner_fm_private_uid);
                        return false;
                    }
                    if (!JSCheckEmptyField(document.myname.PERSONAL_NAME.value))
                    {
                        document.myname.PERSONAL_NAME.focus();
                        funErrorAlert(policy_req_empty + global_fm_grid_personal);
                        return false;
                    }
                    isValueDNUID = "";
                    isValueDNName = "";
                    isValueCNUID = document.myname.INPUT_CMND_HC.value;
                    isValueCNName = document.myname.PERSONAL_NAME.value;
                } else {
                    return false;
                }
//                if (!JSCheckEmptyField(document.myname.INPUT_MST_MNS.value) && !JSCheckEmptyField(document.myname.INPUT_CMND_HC.value))
//                {
//                    funErrorAlert(policy_req_empty + owner_fm_private_uid);
//                    return false;
//                }
//                if (!JSCheckEmptyField(document.myname.PERSONAL_NAME.value) && !JSCheckEmptyField(document.myname.COMPANY_NAME.value))
//                {
//                    funErrorAlert(policy_req_empty + global_fm_grid_company + '/' + global_fm_grid_personal);
//                    return false;
//                }
                if (!JSCheckEmptyField($("#PHONE_CONTRACT").val()))
                {
                    $("#PHONE_CONTRACT").focus();
                    funErrorAlert(policy_req_empty + global_fm_phone);
                    return false;
                } else {
                    if (!FormCheckPhoneHand(localStorage.getItem("sessLocal_REGEX_PHONE"), $("#PHONE_CONTRACT")))
                    {
                        $("#PHONE_CONTRACT").focus();
                        funErrorAlert(global_req_phone_format);
                        return false;
                    }
                }
                if (!JSCheckEmptyField($("#EMAIL_CONTRACT").val()))
                {
                    $("#EMAIL_CONTRACT").focus();
                    funErrorAlert(policy_req_empty + global_fm_email);
                    return false;
                } else {
                    if (!FormCheckEmailSearchHand(localStorage.getItem("sessLocal_REGEX_EMAIL"), $("#EMAIL_CONTRACT").val()))
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
                    url: "../OwnerCommon",
                    data: {
                        idParam: 'addowner',
                        pPERSONAL_NAME: isValueCNName,
                        pCOMPANY_NAME: isValueDNName,
                        pCBX_MST_MNS: document.myname.CBX_MST_MNS.value,
                        pINPUT_MST_MNS: isValueDNUID,
                        pCBX_CMND_HC: document.myname.CBX_CMND_HC.value,
                        pINPUT_CMND_HC: isValueCNUID,
                        pPHONE_CONTRACT: document.myname.PHONE_CONTRACT.value,
                        pEMAIL_CONTRACT: document.myname.EMAIL_CONTRACT.value,
                        pADDRESS: document.myname.ADDRESS.value,
                        pREPRESENTATIVE: document.myname.REPRESENTATIVE.value,
                        pREPRESENTATIVE_POSITION: document.myname.REPRESENTATIVE_POSITION.value,
                        CsrfToken: idCSRF
                    },
                    cache: false,
                    success: function (html)
                    {
                        var myStrings = sSpace(html).split('#');
                        if (myStrings[0] === "0")
                        {
                            funSuccAlert(owner_succ_add, "OwnerRegister.jsp");
                        }
                        else if (myStrings[0] === JS_EX_ERROR_DB) {
                            funErrorAlert(myStrings[1]);
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
                            } else {
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
                        idSession: 'RefreshOwnerListSess'
                    },
                    cache: false,
                    success: function (html) {
                        var arr = sSpace(html);
                        if (arr === "0")
                        {
                            window.location = "OwnerList.jsp";
                        }
                        else
                        {
                            window.location = "OwnerList.jsp";
                        }
                    }
                });
                return false;
            }
        </script>
    </head>
    <body class="nav-md">
        <%
        if ((session.getAttribute("sUserID")) != null) {
                String anticsrf = "" + Math.random();
                request.getSession().setAttribute("anticsrf", anticsrf);
                String sRegexPolicy = "";
                GENERAL_POLICY[][] sessGeneralPolicy = (GENERAL_POLICY[][]) session.getAttribute("sessGeneralPolicy_System");
                if (sessGeneralPolicy[0].length > 0) {
                    for(GENERAL_POLICY rsPolicy1: sessGeneralPolicy[0])
                    {
                        if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_SYS_REGEX_FOR_PHONE_EMAIL))
                        {
                            sRegexPolicy = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
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
                String sessLanguageGlobal = session.getAttribute("sessVN").toString();
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
                        document.getElementById("idNameURL").innerHTML = owner_title_list;
                    </script>
                </div>
                <div class="right_col" role="main">
                    <div class="">
                        <div class="row">
                            <div class="col-md-12 col-sm-12 col-xs-12">
                                <div class="x_panel">
                                    <div class="x_title">
                                        <h2><i class="fa fa-list-ul"></i> <script>document.write(owner_title_add);</script></h2>
                                        <ul class="nav navbar-right panel_toolbox">
                                            <li>
                                                <input type="button" id="btnSave" class="btn btn-info" onclick="ValidateForm('<%=anticsrf%>');"/>
                                                <input id="btnClose" class="btn btn-info" onclick="closeForm();" type="button" />
                                                <script>
                                                    document.getElementById("btnSave").value = global_fm_button_add;
                                                    document.getElementById("btnClose").value = global_fm_button_back;
                                                </script>
                                            </li>
                                        </ul>
                                        <div class="clearfix"></div>
                                    </div>
                                    <div class="x_content">
                                        <form name="myname" method="post" class="form-horizontal">
                                            <input type="hidden" name="CsrfToken" value="<%=anticsrf%>"/>
<!--                                            <div class='col-sm-12' style='margin-bottom:0px;padding-left: 0px;'>
                                                <div class='form-group'>
                                                    <div class='col-sm-2' style='padding-left: 0px;margin-left: 0px;'>
                                                        <select name="CBX_MST_MNS" id="CBX_MST_MNS" class="form-control123">
                                                            <option id="CBX_MST_MNS_TIN"><script>document.write(global_fm_MST);</script></option>
                                                            <option id="CBX_MST_MNS_BGC"><script>document.write(global_fm_MNS);</script></option>
                                                        </select>
                                                    </div>
                                                    <div class='col-sm-10' style='padding-right: 0px;'>
                                                        <input class="form-control123" type="text" id="INPUT_MST_MNS" name="INPUT_MST_MNS" />
                                                    </div>
                                                </div>
                                                <script>
                                                    $("#CBX_MST_MNS_TIN").val(JS_STR_COMPONENT_DN_VALUE_TIN);
                                                    $("#CBX_MST_MNS_BGC").val(JS_STR_COMPONENT_DN_VALUE_BGC);
                                                </script>
                                            </div>-->
                                            <div class="form-group" style="padding: 0px 0px 15px 0px;margin: 0;">
                                                <label class="radio-inline" style="font-size: 14px;color: #000000;font-weight: bold;">
                                                    <input type="radio" name="nameCheck" id="nameCheckDN" checked>
                                                    <script>document.write(certlist_group_add_bussiness_info);</script>
                                                </label>
                                                <label class="radio-inline" style="font-size: 14px;color: #000000;font-weight: bold;">
                                                    <input type="radio" name="nameCheck" id="nameCheckCN">
                                                    <script>document.write(certlist_group_add_personal_info);</script>
                                                </label>
                                                <input type="text" style="display: none;" id="idSessIsChoise" name="idSessIsChoise"/>
                                                <script>
                                                    $(document).ready(function () {
                                                        $("#idSessIsChoise").val('1');
                                                        $("#idViewOwnerDNUID").css("display", "");
                                                        $("#idViewOwnerDNName").css("display", "");
                                                        $("#idViewOwnerCNUID").css("display", "none");
                                                        $("#idViewOwnerCNName").css("display", "none");
                                                        $('.radio-inline').on('click', function () {
                                                            var s = $(this).find('input').attr('id');
                                                            if (s === 'nameCheckDN')
                                                            {
                                                                $("#idSessIsChoise").val('1');
                                                                $("#idViewOwnerDNUID").css("display", "");
                                                                $("#idViewOwnerDNName").css("display", "");
                                                                $("#idViewOwnerCNUID").css("display", "none");
                                                                $("#idViewOwnerCNName").css("display", "none");
                                                            }
                                                            if (s === 'nameCheckCN')
                                                            {
                                                                $("#idSessIsChoise").val('0');
                                                                $("#idViewOwnerDNUID").css("display", "none");
                                                                $("#idViewOwnerDNName").css("display", "none");
                                                                $("#idViewOwnerCNUID").css("display", "");
                                                                $("#idViewOwnerCNName").css("display", "");
                                                            }
                                                        });
                                                    });
                                                </script>
                                            </div>
                                            <div class="col-sm-6" style="padding-left: 0;" id="idViewOwnerDNUID">
                                                <div class="form-group">
                                                    <div class='col-sm-5' style='padding-left: 0px;margin-left: 0px;'>
                                                        <select name="CBX_MST_MNS" id="CBX_MST_MNS" class="form-control123">
                                                            <%
                                                                PREFIX_UUID[][] rsPrefix;
                                                                rsPrefix = new PREFIX_UUID[1][];
                                                                dbTwo.S_BO_PREFIX_UUID_COMBOBOX("ENTERPRISE", sessLanguageGlobal, rsPrefix);
                                                                if (rsPrefix[0].length > 0) {
                                                                    for (PREFIX_UUID temp1 : rsPrefix[0]) {
                                                            %>
                                                            <option value="<%=temp1.PREFIX_DB%>"><%=temp1.REMARK%></option>
                                                            <%
                                                                    }
                                                                }
                                                            %>
<!--                                                            <option id="CBX_MST_MNS_TIN"><script>document.write(global_fm_MST);</script></option>
                                                            <option id="CBX_MST_MNS_BGC"><script>document.write(global_fm_MNS);</script></option>
                                                            <option id="CBX_MST_MNS_DEC"><script>document.write(global_fm_decision);</script></option>-->
                                                        </select>
                                                    </div>
                                                    <div class='col-sm-7' style='padding-right: 0px;'>
                                                        <input class="form-control123" type="text" id="INPUT_MST_MNS" name="INPUT_MST_MNS" />
                                                    </div>
                                                </div>
<!--                                                <script>
                                                    $("#CBX_MST_MNS_TIN").val(JS_STR_COMPONENT_DN_VALUE_TIN);
                                                    $("#CBX_MST_MNS_BGC").val(JS_STR_COMPONENT_DN_VALUE_BGC);
                                                    $("#CBX_MST_MNS_DEC").val(JS_STR_COMPONENT_DN_VALUE_DEC);
                                                </script>-->
                                            </div>
                                            <div class="col-sm-6" style="padding-left: 0;" id="idViewOwnerDNName">
                                                <div class="form-group">
                                                    <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                                        <label id="idLblTitleCOMPANY_NAME"></label>
                                                        <label id="idLblTitleNoteCOMPANY_NAME" class="CssRequireField"></label>
                                                    </label>
                                                    <div class="col-sm-7" style="padding-right: 0px;">
                                                        <input class="form-control123" maxlength="150" id="COMPANY_NAME" name="COMPANY_NAME">
                                                    </div>
                                                </div>
                                                <script>
                                                    $("#idLblTitleCOMPANY_NAME").text(global_fm_grid_company);
                                                    $("#idLblTitleNoteCOMPANY_NAME").text(global_fm_require_label);
                                                </script>
                                            </div>
                                            <div class="col-sm-6" style="padding-left: 0;display: none;" id="idViewOwnerCNUID">
                                                <div class="form-group">
                                                    <div class='col-sm-5' style='padding-left: 0px;margin-left: 0px;'>
                                                        <select name="CBX_CMND_HC" id="CBX_CMND_HC" class="form-control123">
                                                            <%
                                                                rsPrefix = new PREFIX_UUID[1][];
                                                                dbTwo.S_BO_PREFIX_UUID_COMBOBOX("PERSONAL", sessLanguageGlobal, rsPrefix);
                                                                if (rsPrefix[0].length > 0) {
                                                                    for (PREFIX_UUID temp1 : rsPrefix[0]) {
                                                            %>
                                                            <option value="<%=temp1.PREFIX_DB%>"><%=temp1.REMARK%></option>
                                                            <%
                                                                    }
                                                                }
                                                            %>
<!--                                                            <option id="CBX_CMND_HC_PID"><script>document.write(global_fm_CMND);</script></option>
                                                            <option id="CBX_CMND_HC_PPID"><script>document.write(global_fm_HC);</script></option>
                                                            <option id="CBX_CMND_HC_PEID"><script>document.write(global_fm_CitizenId);</script></option>-->
                                                        </select>
                                                    </div>
                                                    <div class='col-sm-7' style='padding-right: 0px;'>
                                                        <input class="form-control123" type="text" id="INPUT_CMND_HC" name="INPUT_CMND_HC" />
                                                    </div>
                                                </div>
<!--                                                <script>
                                                    $("#CBX_CMND_HC_PID").val(JS_STR_COMPONENT_DN_VALUE_PID);
                                                    $("#CBX_CMND_HC_PPID").val(JS_STR_COMPONENT_DN_VALUE_PPID);
                                                    $("#CBX_CMND_HC_PEID").val(JS_STR_COMPONENT_DN_VALUE_PEID);
                                                </script>-->
                                            </div>
<!--                                            <div class='col-sm-12' style='margin-bottom:0px;padding-left: 0px;'>
                                                <div class='form-group'>
                                                    <div class='col-sm-2' style='padding-left: 0px;margin-left: 0px;'>
                                                        <select name="CBX_CMND_HC" id="CBX_CMND_HC" class="form-control123">
                                                            <option id="CBX_CMND_HC_PID"><script>document.write(global_fm_CMND);</script></option>
                                                            <option id="CBX_CMND_HC_PPID"><script>document.write(global_fm_HC);</script></option>
                                                        </select>
                                                    </div>
                                                    <div class='col-sm-10' style='padding-right: 0px;'>
                                                        <input class="form-control123" type="text" id="INPUT_CMND_HC" name="INPUT_CMND_HC" />
                                                    </div>
                                                </div>
                                                <script>
                                                    $("#CBX_CMND_HC_PID").val(JS_STR_COMPONENT_DN_VALUE_PID);
                                                    $("#CBX_CMND_HC_PPID").val(JS_STR_COMPONENT_DN_VALUE_PPID);
                                                </script>
                                            </div>-->
                                            
                                            <div class="col-sm-6" style="padding-left: 0;display: none;" id="idViewOwnerCNName">
                                                <div class="form-group">
                                                    <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                                        <label id="idLblTitlePERSONAL_NAME"></label>
                                                        <label id="idLblTitleNotePERSONAL_NAME" class="CssRequireField"></label>
                                                    </label>
                                                    <div class="col-sm-7" style="padding-right: 0px;">
                                                        <input class="form-control123" maxlength="150" id="PERSONAL_NAME" name="PERSONAL_NAME">
                                                    </div>
                                                </div>
                                                <script>
                                                    $("#idLblTitlePERSONAL_NAME").text(global_fm_grid_personal);
                                                    $("#idLblTitleNotePERSONAL_NAME").text(global_fm_require_label);
                                                </script>
                                            </div>
                                            <div class="col-sm-6" style="padding-left: 0;">
                                                <div class="form-group">
                                                    <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                                        <label id="idLblTitlePHONE_CONTRACT"></label>
                                                        <label id="idLblTitleNotePHONE_CONTRACT" class="CssRequireField"></label>
                                                    </label>
                                                    <div class="col-sm-7" style="padding-right: 0px;">
                                                        <input class="form-control123" maxlength="150" id="PHONE_CONTRACT" name="PHONE_CONTRACT">
                                                    </div>
                                                </div>
                                                <script>
                                                    $("#idLblTitlePHONE_CONTRACT").text(global_fm_phone);
                                                    $("#idLblTitleNotePHONE_CONTRACT").text(global_fm_require_label);
                                                </script>
                                            </div>
                                            <div class="col-sm-6" style="padding-left: 0;">
                                                <div class="form-group">
                                                    <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                                        <label id="idLblTitleEMAIL_CONTRACT"></label>
                                                        <label id="idLblTitleNoteEMAIL_CONTRACT" class="CssRequireField"></label>
                                                    </label>
                                                    <div class="col-sm-7" style="padding-right: 0px;">
                                                        <input class="form-control123" maxlength="150" id="EMAIL_CONTRACT" name="EMAIL_CONTRACT">
                                                    </div>
                                                </div>
                                                <script>
                                                    $("#idLblTitleEMAIL_CONTRACT").text(global_fm_email);
                                                    $("#idLblTitleNoteEMAIL_CONTRACT").text(global_fm_require_label);
                                                </script>
                                            </div>
                                            <div class="col-sm-6" style="padding-left: 0;">
                                                <div class="form-group">
                                                    <label id="idLblTitleREPRESENTATIVE" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                                    <div class="col-sm-7" style="padding-right: 0px;">
                                                        <input class="form-control123" maxlength="150" id="REPRESENTATIVE" name="REPRESENTATIVE">
                                                    </div>
                                                </div>
                                                <script>
                                                    $("#idLblTitleREPRESENTATIVE").text(branch_fm_representative);
                                                </script>
                                            </div>
                                            <div class="col-sm-6" style="padding-left: 0;">
                                                <div class="form-group">
                                                    <label id="idLblTitlepREPRESENTATIVE_POSITION" class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;"></label>
                                                    <div class="col-sm-7" style="padding-right: 0px;">
                                                        <input class="form-control123" maxlength="150" id="REPRESENTATIVE_POSITION" name="REPRESENTATIVE_POSITION">
                                                    </div>
                                                </div>
                                                <script>
                                                    $("#idLblTitlepREPRESENTATIVE_POSITION").text(branch_fm_representative_position);
                                                </script>
                                            </div>
                                            <div class="form-group" style="padding: 0px 0px 10px 0px;margin: 0;">
                                                <label id="idLblTitleADDRESS" class="control-label123"></label>
                                                <input class="form-control123" maxlength="500" id="ADDRESS" name="ADDRESS">
                                                <script>
                                                    $("#idLblTitleADDRESS").text(global_fm_address);
                                                </script>
                                            </div>
<!--                                            <div class="col-sm-13" style="padding-left: 0;clear: both;">
                                                <div class="form-group">
                                                    <label id="idLblTitleADDRESS" class="control-label col-sm-2" style="color: #000000; font-weight: bold;text-align: left;"></label>
                                                    <div class="col-sm-10" style="padding-right: 0px;">
                                                        <input class="form-control123" maxlength="150" id="ADDRESS" name="ADDRESS">
                                                    </div>
                                                </div>
                                                <script>
                                                    $("#idLblTitleADDRESS").text(global_fm_address);
                                                </script>
                                            </div>-->
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