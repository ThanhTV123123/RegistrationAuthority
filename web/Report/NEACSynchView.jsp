<%-- 
    Document   : NEACSynchView
    Created on : Mar 4, 2021, 5:29:46 PM
    Author     : USER
--%>

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
        <script type="text/javascript" src="../js/jquery.js"></script>
        <link rel="stylesheet" href="../js/sweetalert.css"/>
        <script src="../js/sweetalert-dev.js"></script>
        <script type="text/javascript" src="../Css/GlobalAlert.js"></script>
        <script src="../js/jquery.PrintArea.js"></script>
        <title></title>
        <script type="text/javascript">
//            changeFavicon("../");
//            document.title = history_title_detail;
            $(document).ready(function () {
                $('.loading-gif').hide();
//                if(localStorage.getItem("LOCAL_PARAM_HISTORYLIST") !== null && localStorage.getItem("LOCAL_PARAM_HISTORYLIST") !== "null")
//                {
//                    var vParamUrl = getUrlParam("id", "");
//                    if(vParamUrl !== localStorage.getItem("LOCAL_PARAM_HISTORYLIST"))
//                    {
//                        window.location = "../Admin/Home.jsp";
//                    }
//                } else {
//                    window.location = "HistoryList.jsp";
//                }
            });
            function closeForm()
            {
                $.ajax({
                    type: "post",
                    url: "../UserCommon",
                    data: {
                        idParam: 'backformpage',
                        idSession: 'SessRefreshNEACLog'
                    },
                    cache: false,
                    success: function (html) {
                        var arr = sSpace(html);
                        if (arr === "0")
                        {
                            window.location = "NEACSynchList.jsp";
                        }
                        else
                        {
                            window.location = "NEACSynchList.jsp";
                        }
                    }
                });
                return false;
            }
        </script>
    </head>
    <body>
        <%         
        if (session.getAttribute("sUserID") != null) {
            String anticsrf = "" + Math.random();
            request.getSession().setAttribute("anticsrf", anticsrf);
        %>
        <div style="width: 100%; text-align: center; position: fixed;z-index: 1000;top: 0; padding-top: 300px;
             left: 0; height: 100%;" class="loading-gif">
            <img src="../Images/ajax-loader1.gif" alt="Please wait..." />
        </div>
        <%                                        NEAC_LOG[][] rs = new NEAC_LOG[1][];
            try {
                String ids = EscapeUtils.CheckTextNull(request.getParameter("id"));
                String sessLanguageGlobal = session.getAttribute("sessVN").toString();
                if (EscapeUtils.IsInteger(ids) == true) {
                    db.S_BO_NEAC_LOG_DETAIL(EscapeUtils.escapeHtml(ids), sessLanguageGlobal, rs);
                    if (rs[0].length > 0) {
                        String sCERTIFICATION_SN = EscapeUtils.CheckTextNull(rs[0][0].CERTIFICATION_SN);
                        String CERTIFICATION_ATTR_TYPE_REMARK = rs[0][0].CERTIFICATION_ATTR_TYPE_REMARK;
                        String REMAINING_COUNTER = String.valueOf(rs[0][0].REMAINING_COUNTER);
                        String NEAC_SYNC_STATE_REMARK = rs[0][0].NEAC_SYNC_STATE_REMARK;
                        String sREQUEST_DATA = rs[0][0].REQUEST_DATA;
                        String sRESPONSE_DATA = rs[0][0].RESPONSE_DATA;
                        String strCREATED_DT = rs[0][0].CREATED_DT;
                        String strCREATED_BY = rs[0][0].CREATED_BY;
                        String strMODIFIED_BY = rs[0][0].MODIFIED_BY;
                        String strMODIFIED_DT = rs[0][0].MODIFIED_DT;
        %>
        <div class="x_title">
            <h2><i class="fa fa-list-ul"></i> <span id="idLblTitleEdits" style="color: #36526D;"></span></h2>
            <script>$("#idLblTitleEdits").text(history_title_detail);</script>
            <ul class="nav navbar-right panel_toolbox">
                <li>
                    <%
                        if(rs[0][0].NEAC_SYNC_STATE_ID == Definitions.CONFIG_SYNCH_NEAC_STATE_PROCESSING
                            || rs[0][0].NEAC_SYNC_STATE_ID == Definitions.CONFIG_SYNCH_NEAC_STATE_PROCESSING_MANUALLY
                            || rs[0][0].NEAC_SYNC_STATE_ID == Definitions.CONFIG_SYNCH_NEAC_STATE_ERROR_ASYNCHRONOUS)
                        {
                    %>
                    <input id="btnSave" type="button" data-switch-get="state" class="btn btn-info" onclick="ValidateForm('<%=rs[0][0].ID%>', '<%= anticsrf%>');"/>
                    <script>
                        document.getElementById("btnSave").value = global_button_grid_synch;
                        function ValidateForm(neacLogID, idCSRF)
                        {
                            $('body').append('<div id="over"></div>');
                            $(".loading-gif").show();
                            $.ajax({
                                type: "post",
                                url: "../SomeCommon",
                                data: {
                                    idParam: 'synchneacsimple',
                                    neacLogID: neacLogID,
                                    CsrfToken: idCSRF
                                },
                                cache: false,
                                success: function (html)
                                {
                                    var myStrings = sSpace(html).split('#');
                                    if (myStrings[0] === "0")
                                    {
                                        funSuccAlert(synchneac_succ_edit, "NEACSynchList.jsp");
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
                                        if (myStrings[0] === "1") {
                                            funErrorAlertLocal(myStrings[1]);
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
                    </script>
                    <%
                        }
                    %>
                    <%
                        String sApproveEnabled = "";
                        GENERAL_POLICY[][] sessGeneralPolicy = (GENERAL_POLICY[][]) session.getAttribute("sessGeneralPolicy_System");
                        if (sessGeneralPolicy[0].length > 0) {
                            for(GENERAL_POLICY rsPolicy1: sessGeneralPolicy[0]){
                                if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_BO_NEAC_ASYNC_NEED_APPROVE_BY_USER)) {
                                    sApproveEnabled = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                    break;
                                }
                            }
                        }
                        if("1".equals(sApproveEnabled)){
                            if(rs[0][0].NEAC_SYNC_STATE_ID == Definitions.CONFIG_SYNCH_NEAC_STATE_INITIALIZE){
                    %>
                    <input id="btnApprove" type="button" data-switch-get="state" class="btn btn-info" onclick="approveSimple('<%=rs[0][0].ID%>', '<%= anticsrf%>');"/>
                    <script>
                        document.getElementById("btnApprove").value = global_fm_approve;
                        function approveSimple(neacLogID, idCSRF)
                        {
                            $('body').append('<div id="over"></div>');
                            $(".loading-gif").show();
                            $.ajax({
                                type: "post",
                                url: "../SomeCommon",
                                data: {
                                    idParam: "approveneacsimple",
                                    neacLogID: neacLogID,
                                    CsrfToken: idCSRF
                                },
                                catche: false,
                                success: function (html) {
                                    var myStrings = sSpace(html).split('#');
                                    if (myStrings[0] === "0")
                                    {
                                        funSuccAlert(inputcertlist_succ_edit, "NEACSynchList.jsp");
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
                                    $(".loading-gif").hide();
                                    $('#over').remove();
                                }
                            });
                            return false;
                        }
                    </script>
                    <%
                            }
                        }
                    %>
                    <%
                        if(rs[0][0].NEAC_SYNC_STATE_ID != Definitions.CONFIG_SYNCH_NEAC_STATE_CANCEL
                            && rs[0][0].NEAC_SYNC_STATE_ID != Definitions.CONFIG_SYNCH_NEAC_STATE_SUCCESS
                            && rs[0][0].NEAC_SYNC_STATE_ID != Definitions.CONFIG_SYNCH_NEAC_STATE_ERROR_RESYNCHRONIZE) {
                    %>
                    <input id="btnDecline" type="button" data-switch-get="state" class="btn btn-info" onclick="DeclineForm('<%=rs[0][0].ID%>', '<%= anticsrf%>');"/>
                    <script>
                        document.getElementById("btnDecline").value = global_fm_button_decline;
                        function DeclineForm(neacLogID, idCSRF)
                        {
                            $('body').append('<div id="over"></div>');
                            $(".loading-gif").show();
                            $.ajax({
                                type: "post",
                                url: "../SomeCommon",
                                data: {
                                    idParam: 'declineneacsimple',
                                    neacLogID: neacLogID,
                                    CsrfToken: idCSRF
                                },
                                cache: false,
                                success: function (html)
                                {
                                    var myStrings = sSpace(html).split('#');
                                    if (myStrings[0] === "0")
                                    {
                                        funSuccAlert(request_succ_delete, "NEACSynchList.jsp");
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
                                        if (myStrings[0] === "1") {
                                            funErrorAlert(myStrings[1]);
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
                    </script>
                    <%
                        }
                    %>
                    <input id="btnClose" class="btn btn-info" type="button" onclick="closeForm();" />
                </li>
                <script>
                    document.getElementById("btnClose").value = global_fm_button_close;
                </script>
            </ul>
            <div class="clearfix"></div>
        </div>
        <div class="x_content">
            <form name="myname" method="post" class="form-horizontal">
                <input type="hidden" id="sID" name="sID" hidden="true" readonly="true" value="<%= rs[0][0].ID%>">
                <input type="hidden" name="CsrfToken" value="<%=anticsrf%>"/>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                            <label id="idLblTitleCERTIFICATION_SN"></label>
                        </label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input type="text" name="CERTIFICATION_SN" readonly value="<%= sCERTIFICATION_SN%>" class="form-control123">
                        </div>
                    </div>
                    <script>
                        $("#idLblTitleCERTIFICATION_SN").text(global_fm_serial);
                    </script>
                </div>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                            <label id="idLblTitleStatus"></label>
                        </label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input type="text" readonly name="STATUS" value="<%= NEAC_SYNC_STATE_REMARK%>" class="form-control123">
                        </div>
                    </div>
                    <script>
                        $("#idLblTitleStatus").text(global_fm_Status);
                    </script>
                </div>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                            <label id="idLblTitleREQUEST_TYPE"></label>
                        </label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input type="text" readonly name="REQUEST_TYPE" value="<%= CERTIFICATION_ATTR_TYPE_REMARK%>" class="form-control123">
                        </div>
                    </div>
                    <script>
                        $("#idLblTitleREQUEST_TYPE").text(cert_fm_type_request);
                    </script>
                </div>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                            <label id="idLblTitleREMAINING_COUNTER"></label>
                        </label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input type="text" readonly name="REMAINING_COUNTER" value="<%= REMAINING_COUNTER%>" class="form-control123">
                        </div>
                    </div>
                    <script>
                        $("#idLblTitleREMAINING_COUNTER").text(synchneac_fm_remaining);
                    </script>
                </div>
                <div class="col-sm-13" style="padding-left: 0;clear: both;">
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                <label id="idLblTitleREQUEST_DATA"></label>
                            </label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <textarea style="height: 85px;" name="sREQUEST_DATA" id="sREQUEST_DATA" readonly class="form-control123"><%= sREQUEST_DATA%></textarea>
                            </div>
                        </div>
                        <script>
                            $("#idLblTitleREQUEST_DATA").text(history_fm_request_data);
                        </script>
                    </div>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                <label id="idLblTitleRESPONSE_DATA"></label>
                            </label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <textarea style="height: 85px;" name="sRESPONSE_DATA" id="sRESPONSE_DATA" readonly class="form-control123"><%= sRESPONSE_DATA%></textarea>
                            </div>
                        </div>
                        <script>
                            $("#idLblTitleRESPONSE_DATA").text(history_fm_response_data);
                        </script>
                    </div>
                </div>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                            <label id="idLblTitleCREATED_BY"></label>
                        </label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input class="form-control123" type="text" name="CREATED_BY" readonly value="<%= strCREATED_BY%>"/>
                        </div>
                    </div>
                    <script>
                        $("#idLblTitleCREATED_BY").text(global_fm_user_create);
                    </script>
                </div>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                            <label id="idLblTitleCreateDate"></label>
                        </label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input class="form-control123" type="text" name="REQUEST_TIME" readonly value="<%= strCREATED_DT%>"/>
                        </div>
                    </div>
                    <script>
                        $("#idLblTitleCreateDate").text(global_fm_date_create);
                    </script>
                </div>
                <%
                    if(!"".equals(strMODIFIED_BY)){
                %>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                            <label id="idLblTitleMODIFIED_BY"></label>
                        </label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input class="form-control123" type="text" name="MODIFIED_BY" readonly value="<%= strMODIFIED_BY%>"/>
                        </div>
                    </div>
                    <script>
                        $("#idLblTitleMODIFIED_BY").text(global_fm_user_endupdate);
                    </script>
                </div>
                <%
                    }
                %>
                <%
                    if(!"".equals(strMODIFIED_DT)){
                %>
                <div class="col-sm-6" style="padding-left: 0;">
                    <div class="form-group">
                        <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                            <label id="idLblTitleMODIFIED_DT"></label>
                        </label>
                        <div class="col-sm-7" style="padding-right: 0px;">
                            <input class="form-control123" type="text" name="MODIFIED_DT" readonly value="<%= strMODIFIED_DT%>"/>
                        </div>
                    </div>
                    <script>
                        $("#idLblTitleMODIFIED_DT").text(global_fm_date_endupdate);
                    </script>
                </div>
                <%
                    }
                %>
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
        <script src="../style/jquery.min.js"></script>
        <script src="../style/bootstrap.min.js"></script>
        <!--<script src="../style/custom.min.js"></script>-->
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