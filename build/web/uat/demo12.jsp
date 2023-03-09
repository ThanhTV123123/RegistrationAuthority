<%-- 
    Document   : demo12
    Created on : Nov 1, 2022, 5:31:54 PM
    Author     : DELL
--%>

<%@page import="vn.ra.process.ConnectDatabase"%>
<%@page import="vn.ra.utility.Definitions"%>
<%@page import="vn.ra.process.CommonFunction"%>
<%@page import="vn.ra.object.BRANCH"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%--<%@include file="../Admin/ConnectionParam.jsp" %>--%>
<%--<%@include file="../Admin/CommonPagingList.jsp" %>--%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
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
<!--        <link href="../Css/smartpaginator.css" rel="stylesheet" type="text/css"/>
        <script src="../Css/smartpaginator.js" type="text/javascript"></script>-->
        <link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
        <!--<script src="//code.jquery.com/jquery-1.11.1.min.js"></script>
        <link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
        <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>-->
        <!--<link href="//netdna.bootstrapcdn.com/bootstrap/3.0.0/css/bootstrap.min.css" rel="stylesheet" id="bootstrap-css">-->
        <!--<script src="//netdna.bootstrapcdn.com/bootstrap/3.0.0/js/bootstrap.min.js"></script>-->
        <!------ Include the above in your HEAD tag ---------->

        <script>
            $(document).ready(function () {
                $('#myModalRegisterPrint').modal({
                    backdrop: 'static',
                    keyboard: true,
                    show: false
                });
                
            });
            $(function () {
                $.widget("custom.combobox", {
                    _create: function () {
                        this.wrapper = $("<span>")
                                .addClass("custom-combobox")
                                .insertAfter(this.element);
                        this.element.hide();
                        this._createAutocomplete();
                        this._createShowAllButton();
                    },
                    _createAutocomplete: function () {
                        var selected = this.element.children(":selected"),
                                value = selected.val() ? selected.text() : "";
                        this.input = $("<input>")
                                .appendTo(this.wrapper)
                                .val(value)
                                .attr("title", "")
                                .addClass("custom-combobox-input ui-widget ui-widget-content ui-state-default ui-corner-left")
                                .autocomplete({
                                    delay: 0,
                                    minLength: 0,
                                    source: $.proxy(this, "_source")
                                })
                                .tooltip({
                                    classes: {
                                        "ui-tooltip": "ui-state-highlight"
                                    }
                                });
                        this._on(this.input, {
                            autocompleteselect: function (event, ui) {
                                ui.item.option.selected = true;
                                this._trigger("select", event, {
                                    item: ui.item.option
                                });
                            },

                            autocompletechange: "_removeIfInvalid"
                        });
                    },
                    _createShowAllButton: function () {
                        var input = this.input,
                                wasOpen = false
                        $("<a>")
                                .attr("tabIndex", -1)
                                .attr("title", "Show All Items")
                                .attr("height", "")
                                .tooltip()
                                .appendTo(this.wrapper)
                                .button({
                                    icons: {
                                        primary: "ui-icon-triangle-1-s"
                                    },
                                    text: "false"
                                })
                                .removeClass("ui-corner-all")
                                .addClass("custom-combobox-toggle ui-corner-right")
                                .on("mousedown", function () {
                                    wasOpen = input.autocomplete("widget").is(":visible");
                                })
                                .on("click", function () {
                                    input.trigger("focus");

                                    // Close if already visible
                                    if (wasOpen) {
                                        return;
                                    }
                                    // Pass empty string as value to search for, displaying all results
                                    input.autocomplete("search", "");
                                });
                    },
                    _source: function (request, response) {
                        var matcher = new RegExp($.ui.autocomplete.escapeRegex(request.term), "i");
                        response(this.element.children("option").map(function () {
                            var text = $(this).text();
                            if (this.value && (!request.term || matcher.test(text)))
                                return {
                                    label: text,
                                    value: text,
                                    option: this
                                };
                        }));
                    },
                    _removeIfInvalid: function (event, ui) {
                        // Selected an item, nothing to do
                        if (ui.item) {
                            return;
                        }
                        // Search for a match (case-insensitive)
                        var value = this.input.val(),
                                valueLowerCase = value.toLowerCase(),
                                valid = false;
                        this.element.children("option").each(function () {
                            if ($(this).text().toLowerCase() === valueLowerCase) {
                                this.selected = valid = true;
                                return false;
                            }
                        });
                        // Found a match, nothing to do
                        if (valid) {
                            return;
                        }
                        // Remove invalid value
                        this.input
                                .val("All - Tất cả")
                                .attr("title", value + " didn't match any item")
                                .tooltip("open");
                        this.element.val("");
                        this._delay(function () {
                            this.input.tooltip("close").attr("title", "");
                        }, 2500);
                        this.input.autocomplete("instance").term = "";
                    },
                    _destroy: function () {
                        this.wrapper.remove();
                        this.element.show();
                    }
                });
                $("#combobox").combobox();
                $("#toggle").on("click", function () {
                    $("#combobox").toggle();
                });
            });
        </script>
        <style>
            .custom-combobox {
                position: relative;
                display: inline-block;
              }
              .custom-combobox-toggle {
                position: absolute;
                top: 0;
                bottom: 0;
                margin-left: -1px;
                padding: 0;
              }
              .custom-combobox-input {
                margin: 0;
                padding-top: 2px;
                padding-bottom: 5px;
                padding-right: 5px;
              }
        </style>
    </head>
    <body>
        <%            
//            String anticsrf = "" + Math.random();
//            request.getSession().setAttribute("anticsrf", anticsrf);
//            CERTIFICATION_POLICY_DATA[][] sessPolicyFormFactor_Data = (CERTIFICATION_POLICY_DATA[][]) session.getAttribute("SessPolicyFormFactor_Data");
//            String SessAgentID = session.getAttribute("SessAgentID").toString().trim();
//            String SessUserAgentID = session.getAttribute("SessUserAgentID").toString().trim();
//            String SessUserAgentName = session.getAttribute("SessAgentName").toString().trim();
//            String SessUserID = session.getAttribute("UserID").toString().trim();
//            String SessRoleID = session.getAttribute("RoleID_ID").toString().trim();
//            String sessTreeArrayBranchID = session.getAttribute("sessTreeArrayBranchIDSystem").toString().trim();
        %>
        <h1>Hello World!</h1>
        <div class="ui-widget">
            <label>Procedures: </label>
            <select id="combobox">
                <%
                    ConnectDatabase db = new ConnectDatabase();
                    BRANCH[][] rsBranchFrist = new BRANCH[1][];
                    db.S_BO_BRANCH_GET_TREE_BRANCH_AGENCY(1, 1, rsBranchFrist, "2");
                    BRANCH[][] rsBranch = CommonFunction.cloneBranchAddAllOption(rsBranchFrist, "1");
                    if (rsBranch[0].length > 0) {
                        for (BRANCH temp1 : rsBranch[0]) {
                            if (!String.valueOf(temp1.PARENT_ID).equals(Definitions.CONFIG_AGENT_ROOT)) {
                %>
                <option value="<%=String.valueOf(temp1.ID)%>"><%=temp1.NAME + " - " + temp1.REMARK%></option>
                <%
                            }
                        }
                    }
                %>
            </select>
        </div>
<!--        <button id="toggle">Show underlying select</button>-->

<!--        <script src="../style/jquery.min.js"></script>  -->
            <!--<script src="../style/bootstrap.min.js"></script>-->
        <script src="../style/jquery.min.js"></script>
        <script src="../style/bootstrap.min.js"></script>
        <!--<script src="//code.jquery.com/jquery-1.11.1.min.js"></script>-->
        <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
        <script src="../style/custom.min.js"></script>
        <link href="../js/checkphone/intlTelInput.css" rel="stylesheet" type="text/css"/>
        <script src="../js/checkphone/intlTelInput.js" type="text/javascript"></script>
        <script src="../js/moment.min.js"></script>
        <script src="../js/daterangepicker.js"></script>
    </body>
</html>
