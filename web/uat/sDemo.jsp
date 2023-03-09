<%-- 
    Document   : sDemo
    Created on : Mar 20, 2018, 5:03:05 PM
    Author     : THANH-PC
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="../Admin/ConnectionParam.jsp" %>
<%@include file="../Admin/CommonPagingList.jsp" %>
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
        <link href="../Css/smartpaginator.css" rel="stylesheet" type="text/css"/>
        <script src="../Css/smartpaginator.js" type="text/javascript"></script>
<!--        <script type="text/javascript" src="../js/jquery-1.12.4.js"></script>
        <script type="text/javascript" src="../js/jquery-ui.js"></script>
        <link rel="stylesheet" type="text/css" media="all" href="../style/jquery-ui.css" />-->
        <link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/smoothness/jquery-ui.css">
        <script src="//code.jquery.com/jquery-1.12.4.js"></script>
        <script src="//code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
        <script>
            $(document).ready(function () {
                $('.loading-gif').hide();
            });
            function RegisterCert()
            {
                $.ajax({
                    type: "post",
                    url: "../SomeCommon",
                    data: {
                        idParam: 'democreatednsname'
                    },
                    cache: false,
                    success: function (html)
                    {
                        var myStrings = sSpace(html).split('#');
                        if (myStrings[0] === "0")
                        {
                            alert("Success");
                        } else if (myStrings[0] === "1")
                        {
                        } else {
                            alert("Failed");
                        }
                    }
                });
                return false;
            }
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
                                .attr("placeholder", "")
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
                                wasOpen = false;
                        $("<a>")
                                .attr("tabIndex", -1)
                                .attr("title", "Show All Items")
                                .tooltip()
                                .appendTo(this.wrapper)
                                .button({
                                    icons: {
                                        primary: "ui-icon-triangle-1-s"
                                    },
                                    text: false
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
                                .val("Tat ca")
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
                $("#js__apply_now").combobox();
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
                padding: 5px 10px;
            }
        </style>
    </head>
    <body class="nav-md">
        <div style="width: 100%; text-align: center; position: fixed;z-index: 1000;top: 0; padding-top: 300px;
             left: 0; height: 100%;" class="loading-gif">
            <img src="../Images/ajax-loader1.gif" alt="Please wait..." />
        </div>
        <div class="container body">
            <div class="main_container">
                <div class="col-md-3 left_col">
                    <div class="left_col scroll-view">
                        <br />
                        <div id="sidebar-menu" class="main_menu_side hidden-print main_menu">
                        </div>
                    </div>
                </div>
                <div class="top_nav">
                </div>
                <div class="right_col" role="main">
                    <div class="">
                        <div class="row">
                            <div class="col-md-12 col-sm-12 col-xs-12">
                                <div class="x_panel">
                                    <div class="x_title">
                                        <h2><i class="fa fa-list-ul"></i> sDemo.jsp</h2>
                                        <ul class="nav navbar-right panel_toolbox">
                                            <li>
                                                <input type="button" value="Register" id="btnRegister" class="btn btn-info" onclick="RegisterCert();"/>
                                            </li>
                                        </ul>
                                        <div class="clearfix"></div>
                                    </div>
                                    <div class="x_content">
                                        <div class="ui-widget">
                                            <label>Your preferred programming language: </label>
                                            <select id="js__apply_now">
                                                <%
                                                    BRANCH[][] rsBranchFrist = (BRANCH[][]) session.getAttribute("sessTreeBranchSystemAgency");
                                                    BRANCH[][] rsBranch = CommonFunction.cloneBranchAddAllOption(rsBranchFrist, "1");
                                                    if (rsBranch[0].length > 0) {
                                                        for (BRANCH temp1 : rsBranch[0]) {
                                                            if(!String.valueOf(temp1.PARENT_ID).equals(Definitions.CONFIG_AGENT_ROOT)) {
                                                %>
                                                <option value="<%=String.valueOf(temp1.ID)%>" <%= session.getAttribute("sessBranchOfficeStatusCollation") != null
                                                    && String.valueOf(temp1.ID).equals(session.getAttribute("sessBranchOfficeStatusCollation").toString().trim())
                                                    ? "selected" : ""%>><%=temp1.NAME + " - " + temp1.REMARK%></option>
                                                <%
                                                            }
                                                        }
                                                    }
                                                %>
                                            </select>
                                        </div>
                                        <br/>
                                        <input id="btnSubmit" onclick="onSubmit();" value="Submit" type="button"/>
                                        <script>
                                            function onSubmit(){
                                                alert(document.getElementById("js__apply_now").value);
                                            }
                                        </script>
                                        <br/>
                                        <form name="myForm">
                                        <select name="mySelect1" multiple onBlur='comeBack()'>
                                          <option value=A>AA</option>
                                          <option value=B>BB</option>
                                          <option value=C>CC</option>
                                          <option value=D>DD</option>
                                        </select>
                                        <br>
                                        <select name="mySelect2" multiple>
                                          <option value=E>EE</option>
                                          <option value=F>FF</option>
                                          <option value=G>GG</option>
                                          <option value=H>HH</option>
                                        </select>
                                        <input type=TEXT size=2 value="" name="counter">
                                        <script language="JavaScript">
    <!--
    var counter = 0;

    function comeBack(){
      document.myForm.mySelect1.focus();
      document.myForm.counter.value = counter++;
    }
    -->
    </script>
                                      </form>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
            <!--<script src="../style/bootstrap.min.js"></script>-->
            <script src="../style/custom.min.js"></script>
            <script src="../js/moment.min.js"></script>
            <script src="../js/daterangepicker.js"></script>
    </body>
</html>
