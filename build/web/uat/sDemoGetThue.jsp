<%-- 
    Document   : sDemoGetThue.jsp
    Created on : Jan 10, 2019, 4:42:03 PM
    Author     : THANH-PC
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <script type="text/javascript" src="../js/jquery.js"></script>
        <link rel="stylesheet" href="../js/sweetalert.css"/>
        <script src="../js/sweetalert-dev.js"></script>
        <script src="../Css/GlobalAlert.js"></script>
        <script>
            function onClick()
            {
//                alert(document.getElementById("idCaptcha").value);
                $.ajax({
                    type: "post",
                    url: "http://tracuunnt.gdt.gov.vn/tcnnt/mstdn.jsp",
                    data: {
                        id: '',
                        page: '1',
                        mst: '0306555792',
                        fullname: '',
                        address:'',
                        cmt:'',
                        captcha:document.getElementById("idCaptcha").value
                    },
                    headers: {
                        "Authorization": "Bearer ",
                        "cache-control": "no-cache"
                    },
                    cache: false,
                    success: function (html)
                    {
                        alert(html);
                    }
                });
                return false;
            }
        </script>
    </head>
    <body>
        <form name="myname">
            <img height="45" src="http://tracuunnt.gdt.gov.vn/tcnnt/captcha.png?uid=55851766-737c-405a-89a8-e9dad0382820" />
            <br/>
            <input id="idCaptcha" type="text"/>
            <br/>
            <input id="btnSubmit" value="Submit" onclick="onClick()" type="button"/>
        </form>
    </body>
</html>
