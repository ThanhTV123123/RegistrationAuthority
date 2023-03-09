<%-- 
    Document   : sDemo02
    Created on : Apr 23, 2018, 3:33:18 PM
    Author     : THANH-PC
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
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
        <link href="../style/customportal.min.css" rel="stylesheet">
        <script src="../js/sweetalert-dev.js"></script>
        <script type="text/javascript" src="../Css/GlobalAlert.js"></script>
        <link rel="stylesheet" href="https://netdna.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css">
        <link href="../Css/active/bootstrap-switch.css" rel="stylesheet">
        <script>
//            $(document).ready(function () {
//                JSLoadPhoneNew($("#idPhone"));
//            });
//            function CheckPhone()
//            {
//                if (!JSCheckFormatPhoneNew($("#idPhone")))
//                {
//                    $("#idPhone").focus();
//                    funErrorAlert("Format Phone");
//                    return false;
//                }
//            }
//            function GetValue()
//            {
            $(document).ready(function () {
//                $("input[type=\"checkbox\"], input[type=\"radio\"]").not("[data-switch-no-init]").bootstrapSwitch();
                $("[data-switch-get]").on("click", function () {
                    var type;
                    type = $(this).data("switch-get");
                    alert(type);
                    return alert($("#ActiveFlag").bootstrapSwitch("state"));
                });
            });
//                alert($("#switch-state1").bootstrapSwitch("state1"));
//            }
        </script>
    </head>
    <body>
        <main id="content" role="main">
            <%
                boolean sFalse = true;
            %>
            <div class="container">
                <div class="row">
                    <div class="col-sm-6 col-lg-4">
                        <h2 class="h4">A demo of Bootstrap switch</h2>
                        <p>
                            <input id="ActiveFlag" type="checkbox" <%=sFalse ? "checked='checked'" : ""%>>
                        </p>
                        <p>
                            <input id="switch-state2" type="checkbox" checked>
                        </p>
                        <input type="button" data-switch-get="state" value="Get"/>
                        <div class="btn-group">
                            <!--<button type="button" data-switch-toggle="state" class="btn btn-danger">Toggle</button>-->
                            <button type="button" data-switch-set="state" data-switch-value="true" class="btn btn-default">Set true</button>
                            <button type="button" data-switch-set="state" data-switch-value="false" class="btn btn-default">Set false</button>
                            <button type="button" data-switch-get="state" class="btn btn-success">Get</button>
                        </div>
                    </div>
                </div>
            </div>
        </main>
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script> 
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
        <script src="../js/active/highlight.js"></script>
        <script src="../js/active/bootstrap-switch.js"></script>
        <script src="../js/active/main.js"></script>
    </body>
</html>
