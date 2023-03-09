<%-- 
    Document   : Logout
    Created on : Oct 12, 2013, 2:14:39 PM
    Author     : Thanh
--%>

<script type="text/javascript" src="../js/jquery.js"></script>
<script type="text/javascript" src="../Css/GlobalAlert.js"></script>
<!--<input id="idSessInput" type="text" value="{pageContext.session.maxInactiveInterval}"/>-->
<script>
    $(document).ready(function () {
//        alert(document.getElementById("idSessInput").value);
        //window.localStorage.clear();
        var tempUID_Logout = localStorage.getItem("localStoreUserName_RoudRB");
        var tempSessID_Logout = localStorage.getItem("localStoreSessKey_RoudRB");
//        var dataString = 'idParam=1';
        $.ajax({
            type: "post",
            url: "../LogoutCommon",
            data: {
                idParam: '1',
                sUserName: tempUID_Logout,
                sSessKey: tempSessID_Logout
            },
//            data: dataString,
            cache: false,
            success: function (html) {
                var arr = sSpace(html).split('#');
                if (arr[0] === "0")
                {
                    window.location = "../Login.jsp";
                }
                else
                {
                    window.location = "../Login.jsp";
                }
            }
        });
        return false;
    });
</script>
