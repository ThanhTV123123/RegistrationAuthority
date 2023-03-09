<%-- 
    Document   : sDemoParseUrlID
    Created on : Feb 22, 2019, 2:35:16 PM
    Author     : THANH-PC
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="../Admin/ConnectionParam.jsp" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script type="text/javascript" src="../js/jquery.js"></script>
        <script type="text/javascript" src="../Css/GlobalAlert.js"></script>
        <link rel="stylesheet" href="../js/sweetalert.css"/>
        <script src="../js/sweetalert-dev.js"></script>
        <script src="../js/Language.js"></script>
        <title>Decode ID on URL</title>
        <script>
            function ValidateForm() {
                if (!JSCheckEmptyField(document.getElementById("idValue").value))
                {
                    document.getElementById("idValue").focus();
                    funErrorAlert("Please Enter Value");
                    return false;
                }
                document.getElementById("idResult").value = "";
                $.ajax({
                    type: "post",
                    url: "../SomeCommon",
                    data: {
                        idParam: 'decodeidurl',
                        idValue: document.getElementById("idValue").value
                    },
                    cache: false,
                    success: function (html)
                    {
                        document.getElementById("idViewResult").style.display = "";
                        var myStrings = sSpace(html).split('#');
                        if (myStrings[0] === "0")
                        {
                            document.getElementById("idResult").value = myStrings[1];
                        }
                        else
                        {
                            document.getElementById("idResult").value = "Error: " + myStrings[1];
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
        %>
        <%
            String SessRoleID = session.getAttribute("RoleID_ID").toString().trim();
            if(SessRoleID.equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN))
            {
        %>
        <div style="padding: 10px 0 10px 0; text-align: left;width: 500px">
            Value: <input id="idValue" name="idValue" type="text" style="width: 300px;"/>
        </div>
        <div style="padding: 10px 0 10px 0; text-align: left;width: 500px;">
            <input id="btnSubmit" name="btnSubmit" type="button" onclick="ValidateForm();" value=" Decode "/>
        </div>
        <div style="padding: 10px 0 10px 0; text-align: left;width: 500px; display: none;" id="idViewResult">
            Result: <input id="idResult" name="idResult" type="text" readonly />
        </div>
        <%
            } else {
        %>
        <script type="text/javascript">
            window.onload = function () {
                window.location = "../Admin/Home.jsp";
            }();
        </script>
        <%
            }
        %>
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
