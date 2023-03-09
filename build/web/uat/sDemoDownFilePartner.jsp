<%-- 
    Document   : sDemoDownFilePartner
    Created on : Feb 27, 2019, 4:15:39 PM
    Author     : THANH-PC
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Demo download file from partner</title>
        <script type="text/javascript" src="../js/jquery.js"></script>
        <script type="text/javascript" src="../Css/GlobalAlert.js"></script>
        <link rel="stylesheet" href="../js/sweetalert.css"/>
        <script src="../js/sweetalert-dev.js"></script>
        <script src="../js/Language.js"></script>
        <script>
            function ValidateForm() {
                if (!JSCheckEmptyField(document.getElementById("idValue").value))
                {
                    document.getElementById("idValue").focus();
                    funErrorAlert("Please Enter Value");
                    return false;
                }
                $.ajax({
                    type: "post",
                    url: "../SomeCommon",
                    data: {
                        idParam: 'demodownfilefrompartner',
                        idValue: document.getElementById("idValue").value
                    },
                    cache: false,
                    success: function (html)
                    {
                        console.log(html);
                        var myStrings = sSpace(html).split('#');
                        if (myStrings[0] === "0")
                        {
                            var f = document.myname;
                            f.method = "post";
                            f.action = '../DownFromSaveFile?idParam=downfilepdfcert&filename=' + myStrings[1];
                            f.submit();
                        }
                        else
                        {
                            funErrorAlert("Error: " + myStrings[1]);
                        }
                    }
                });
                return false;
            }
        </script>
    </head>
    <body>
        <form name="myname" method="post">
            <div style="padding: 10px 0 10px 0; text-align: left;width: 500px">
                UUID Value: <input id="idValue" name="idValue" type="text" style="width: 300px;"/>
            </div>
            <div style="padding: 10px 0 10px 0; text-align: left;width: 500px;">
                <input id="btnSubmit" name="btnSubmit" type="button" onclick="ValidateForm();" value=" Download "/>
            </div>
        </form>
    </body>
</html>
