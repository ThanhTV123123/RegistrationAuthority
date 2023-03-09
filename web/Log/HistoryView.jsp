<%-- 
    Document   : HistoryView
    Created on : Oct 17, 2018, 2:34:45 PM
    Author     : THANH-PC
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
                        idSession: 'RefreshSestemLogSess'
                    },
                    cache: false,
                    success: function (html) {
                        var arr = sSpace(html);
                        if (arr === "0")
                        {
                            window.location = "HistoryList.jsp";
                        }
                        else
                        {
                            window.location = "HistoryList.jsp";
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
        <%                                        SYSTEM_LOG[][] rs = new SYSTEM_LOG[1][];
            try {
                String ids = EscapeUtils.CheckTextNull(request.getParameter("id"));
//                ids = seEncript.decrypt(ids);
                String sessLanguageGlobal = session.getAttribute("sessVN").toString();
                if (EscapeUtils.IsInteger(ids) == true) {
                    db.S_BO_SYSTEM_LOG_DETAIL(EscapeUtils.escapeHtml(ids), sessLanguageGlobal, rs);
                    if (rs[0].length > 0) {
                        String sTOKEN_SN = EscapeUtils.CheckTextNull(rs[0][0].TOKEN_SN);
                        String sCERTIFICATION_SN = EscapeUtils.CheckTextNull(rs[0][0].CERTIFICATION_SN);
                        String sSOURCE_ENTITY_DESC = EscapeUtils.CheckTextNull(rs[0][0].SOURCE_ENTITY_DESC);
                        String sREQUEST_IP = EscapeUtils.CheckTextNull(rs[0][0].REQUEST_IP);
                        String sREQUEST_DATA = EscapeUtils.CheckTextNull(rs[0][0].REQUEST_DATA);
                        String sRESPONSE_DATA = EscapeUtils.CheckTextNull(rs[0][0].RESPONSE_DATA);
                        String sBILLCODE = EscapeUtils.CheckTextNull(rs[0][0].BILLCODE);
                        String sRESPONSE_CODE_DESC = EscapeUtils.CheckTextNull(rs[0][0].RESPONSE_CODE_DESC);
                        String sFUNCTIONALITY_DESC = EscapeUtils.CheckTextNull(rs[0][0].FUNCTIONALITY_DESC);
                        String strCREATED_DT = EscapeUtils.CheckTextNull(rs[0][0].CREATED_DT);
                        String strCREATED_BY = EscapeUtils.CheckTextNull(rs[0][0].CREATED_BY);
                        String sBRANCH_DESC = EscapeUtils.CheckTextNull(rs[0][0].BRANCH_DESC);
        %>
        <div class="x_title">
            <h2><i class="fa fa-list-ul"></i> <span id="idLblTitleEdits" style="color: #36526D;"></span></h2>
            <script>$("#idLblTitleEdits").text(history_title_detail);</script>
            <ul class="nav navbar-right panel_toolbox">
                <li>
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
                                <label id="idLblTitleRESPONSE_CODE_DESC"></label>
                            </label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input type="text" readonly id="sRESPONSE_CODE_DESC" value="<%= sRESPONSE_CODE_DESC %>" class="form-control123">
                            </div>
                        </div>
                        <script>
                            $("#idLblTitleRESPONSE_CODE_DESC").text(history_fm_response);
                        </script>
                    </div>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                <label id="idLblTitleFUNCTIONALITY_DESC"></label>
                            </label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input type="text" name="sFUNCTIONALITY_DESC" value="<%= sFUNCTIONALITY_DESC%>" readonly class="form-control123"/>
                            </div>
                        </div>
                        <script>
                            $("#idLblTitleFUNCTIONALITY_DESC").text(history_fm_function);
                        </script>
                    </div>
                    <%
                        if(!"".equals(sTOKEN_SN) && CommonFunction.checkViewTokenValid(sTOKEN_SN) == true)
                        {
                    %>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                <label id="idLblTitleTOKEN_SN"></label>
                            </label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input type="text" name="sTOKEN_SN" readonly value="<%= sTOKEN_SN%>" class="form-control123">
                            </div>
                        </div>
                        <script>
                            $("#idLblTitleTOKEN_SN").text(token_fm_tokenid);
                        </script>
                    </div>
                    <%
                        }
                    %>
                    <%
                        if(!"".equals(sCERTIFICATION_SN))
                        {
                    %>
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
                    <%
                        }
                    %>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                <label id="idLblTitleSOURCE_ENTITY"></label>
                            </label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input type="text" readonly name="sSOURCE_ENTITY_DESC" value="<%= sSOURCE_ENTITY_DESC%>" class="form-control123">
                            </div>
                        </div>
                        <script>
                            $("#idLblTitleSOURCE_ENTITY").text(history_fm_source_entity);
                        </script>
                    </div>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                <label id="idLblTitleREQUEST_IP"></label>
                            </label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input type="text" readonly name="sREQUEST_IP" value="<%= sREQUEST_IP%>" class="form-control123">
                            </div>
                        </div>
                        <script>
                            $("#idLblTitleREQUEST_IP").text(history_fm_request_ip);
                        </script>
                    </div>
                    <div class="col-sm-6" style="padding-left: 0;">
                        <div class="form-group">
                            <label class="control-label col-sm-5" style="color: #000000; font-weight: bold;text-align: left;padding-left: 0;">
                                <label id="idLblTitleBILLCODE"></label>
                            </label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input type="text" readonly id="sBILLCODE" value="<%= sBILLCODE  %>" class="form-control123">
                            </div>
                        </div>
                        <script>
                            $("#idLblTitleBILLCODE").text(global_fm_billcode);
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
                                <label id="idLblTitleBRANCH_DESC"></label>
                            </label>
                            <div class="col-sm-7" style="padding-right: 0px;">
                                <input type="text" name="sBRANCH_DESC" id="sBRANCH_DESC" readonly 
                                    value="<%= sBRANCH_DESC%>" class="form-control123"/>
                            </div>
                        </div>
                        <script>
                            $("#idLblTitleBRANCH_DESC").text(token_fm_agent);
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
