<%-- 
    Document   : Navigate
    Created on : Oct 18, 2016, 3:42:40 PM
    Author     : THANH
--%>

<%@page import="vn.ra.utility.Definitions"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<style>
    /* Dropdown Button */
    .dropbtn_navigate {
        background-color: #EDEDED;
        color: #000000;
        padding: 20px 16px 16px 16px;
        font-size: 13px;
        border: none;
        cursor: pointer;margin-right: 20px;
    }

    /* Dropdown button on hover & focus */
    .dropbtn_navigate:hover, .dropbtn_navigate:focus {
        background-color: #D9DEE4;
    }

    /* The container <div> - needed to position the dropdown content */
    .dropdown_navigate {
        position: relative;
        display: inline-block;
    }

    /* Dropdown Content (Hidden by Default) */
    .dropdown-content_navigate {
        display: none;
        position: absolute;
        background-color: #f1f1f1;
        min-width: 160px;
        box-shadow: 0px 8px 16px 0px rgba(0,0,0,0.2);
        z-index: 1;
    }

    /* Links inside the dropdown */
    .dropdown-content_navigate a {
        color: black;
        padding: 12px 12px;
        text-decoration: none;
        display: block;
    }

    /* Change color of dropdown links on hover */
    .dropdown-content_navigate a:hover {background-color: #ddd}

    /* Show the dropdown menu (use JS to add this class to the .dropdown-content_navigate container when the user clicks on the dropdown button) */
    .show {display:block;}
</style>
<script>
    function myFunction() {
        document.getElementById("myDropdown").classList.toggle("show");
    }
    window.onclick = function (event) {
        if (!event.target.matches('.dropbtn_navigate')) {
            var dropdowns = document.getElementsByClassName("dropdown-content_navigate");
            var i;
            for (i = 0; i < dropdowns.length; i++) {
                var openDropdown = dropdowns[i];
                if (openDropdown.classList.contains('show')) {
                    openDropdown.classList.remove('show');
                }
            }
        }
    };
    $(document).ready(function () {
        if(IsWhichCA === "15" || IsWhichCA === "17") {
            $("#idLoginBannerLanguageRight").attr("src", "../Images/Laos_flag.png");
            //document.getElementById("idDivLanguage").style.display = "none";
            //localStorage.setItem("VN", "0");
            document.getElementById("idLoginBannerLanguageRight").setAttribute("title", "Laos");
        }
    });
</script>
<div class="nav_menu">
    <nav>
        <div class="nav toggle">
            <a id="menu_toggle" title="Collapse | Expand left menu"><i id="idIMenuTopIcon" class="fa fa-exchange"></i></a>
<!--            .fa-arrow-circle-o-right .fa-arrow-circle-o-left fa fa-tags-->
            <!--fa-chevron-circle-left fa-chevron-circle-right-->
        </div>
        <div class="nav toggle" style="padding-top: 15px;width: 40%;font-size: 17px;font-weight: bold;" id="idNameURL">
        </div>
        <ul class="nav navbar-nav navbar-right" style="width: 50%;">
            <li class="">
                <%
                    String sNameLoginHeader = (String) session.getAttribute("sUserID");
                    String s1111 = (String) session.getAttribute("sesFullname");
                    if (!"null".equals(s1111)) {
                        sNameLoginHeader = s1111;
                    }
                %>
                <div class="dropdown_navigate">
                    <button onclick="myFunction()" class="dropbtn_navigate"><%= sNameLoginHeader%> <span class=" fa fa-angle-double-down"></span></button> 
                    <div id="myDropdown" class="dropdown-content_navigate">
                        <a href="../Admin/Home.jsp"><i class="fa fa-check-square-o pull-left"></i> &nbsp;<script>document.write(global_fm_title_account);</script></a>
                        <a href="../Modules/Logout.jsp"><i class="fa fa-sign-out pull-left"></i> &nbsp;<script>document.write(global_fm_logout);</script></a>
                    </div>
                </div>
<!--                <a href="javascript:;" class="user-profile dropdown-toggle" style="font-size: 13px;font-weight: bold;" data-toggle="dropdown" aria-expanded="false">
                    <= sNameLoginHeader%> <span class=" fa fa-angle-double-down"></span>
                </a>
                <ul class="dropdown-menu dropdown-usermenu pull-right">
                    <li><a href="../Admin/Home.jsp"><i class="fa fa-check-square-o pull-left"></i> &nbsp;<script>document.write(global_fm_title_account);</script></a></li>
                    <li><a href="../Modules/Logout.jsp"><i class="fa fa-sign-out pull-left"></i> &nbsp;<script>document.write(global_fm_logout);</script></a></li>
                </ul>-->
            </li>
            <li style="padding-top: 3.5%;padding-right: 1%;" class="">
                <p id="idDivLanguage"><img title="English" style="width: 18px;height: 18px;cursor: pointer;" onclick="loginEN('1');" src="../Images/en_flag.png" /> | <img onclick="loginEN('0');" style="width: 18px;height: 18px;cursor: pointer;" id="idLoginBannerLanguageRight" title="Vietnamese" src="../Images/vn_flag.png" /></p>
            </li>
<!--            Push Notification-->
            <%
                String SessRoleID_Navigate = session.getAttribute("RoleID_ID").toString().trim();
                String SessUserAgentID_Navigate = session.getAttribute("SessUserAgentID").toString().trim();
                String SessUserID_Navigate = session.getAttribute("UserID").toString().trim();
                if(SessRoleID_Navigate.equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN)
                    || SessRoleID_Navigate.equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN_CHILD)
                    || SessRoleID_Navigate.equals(Definitions.CONFIG_ROLE_ID_CA_SURVEYOR))
                {
                    String sValue_Navigate = "";
                    String sViewTable_Navigate = "none";
                    if (application.getAttribute("sessPushNotiRequestApprove") != null) {
                        sValue_Navigate = application.getAttribute("sessPushNotiRequestApprove").toString();
                        sViewTable_Navigate = "";
                    }
            %>
            <li id="idContentPushRequest" role="presentation" class="dropdown" style="padding-top: 1.1%;padding-right: 2%;display: <%= sViewTable_Navigate%>">
                <!--<div id="idContentPushRequest" style="display: <= sViewTable_Navigate%>">-->
                <a href="javascript:;" class="dropdown-toggle info-number" data-toggle="dropdown" aria-expanded="false" onclick="clearCachePush();">
                    <i class="fa fa-envelope-o"></i>
                    <span class="badge bg-green" id="idContentPushRequestCount1"><%= sValue_Navigate%></span>
                </a>
                <ul id="menu1" class="dropdown-menu list-unstyled msg_list" role="menu">
                    <li>
                        <a>
                            <span class="image"></span>
                            <!--<span>-->
                            <span><script>document.write(global_fm_title_push_approve1);</script><span id="idContentPushRequestCount2"><%= sValue_Navigate%></span><script>document.write(global_fm_title_push_approve2);</script></span>
<!--                                <span class="time" id="idContentPushRequestCount2"><= sValue_Navigate%></span>
                            </span>-->
<!--                                <span class="message">
                                Số lượng yêu cầu từ đại lý gửi lên chờ duyệt cấp phát chứng thư số
                            </span>-->
                        </a>
                    </li>
                </ul>
                <!--</div>-->
                <script>
                    var messagesWaiting = false;
                    function getMessages() {
                        if(!messagesWaiting) {
                            messagesWaiting = true;
                            var xmlhttp = new XMLHttpRequest();
                            xmlhttp.onreadystatechange=function(){
                                if (xmlhttp.readyState===4 && xmlhttp.status===200) {
                                    messagesWaiting = false;
                                    $("#idContentPushRequest").css("display", "");
                                    var idContentPushRequestCount1 = document.getElementById("idContentPushRequestCount1");
                                    var idContentPushRequestCount2 = document.getElementById("idContentPushRequestCount2");
                                    var sCountRequest = sSpace(idContentPushRequestCount1.innerHTML);
                                    if(sCountRequest !== "")
                                    {
                                        idContentPushRequestCount1.innerHTML = parseInt(sCountRequest) + 1;
                                        idContentPushRequestCount2.innerHTML = parseInt(sCountRequest) + 1;
                                    } else {
                                        idContentPushRequestCount1.innerHTML = 1;
                                        idContentPushRequestCount2.innerHTML = 1;
                                    }
                                }
                            };
                            xmlhttp.open("GET", "../PushNotiRequestApprove?t="+new Date(), true);
                            xmlhttp.send();
                        }
                    }
                    setInterval(getMessages, 1000);
                    function clearCachePush()
                    {
                        $.ajax({
                            type: "post",
                            url: "../SomeCommon",
                            data: {
                                idParam: 'deletepushnotirequestapprove'
                            },
                            cache: false,
                            success: function (html)
                            {
                                var myStrings = sSpace(html).split('#');
                                if (myStrings[0] !== "0")
                                {
                                    funErrorAlert(global_errorsql);
                                }
                            }
                        });
                        return false;
                    }
                </script>
            </li>
            <%
                } else {
                    if(SessRoleID_Navigate.equals(Definitions.CONFIG_ROLE_ID_AGENT_ADMIN)
                        || SessRoleID_Navigate.equals(Definitions.CONFIG_ROLE_ID_AGENT_SURVEYOR)
                        || SessRoleID_Navigate.equals(Definitions.CONFIG_ROLE_ID_AGENT_USER))
                    {
                        String sValue_Navigate = "";
                        String sViewTable_Navigate = "none";
                        String sNameSess = "sessPushNotiRequestDecline-" + SessUserAgentID_Navigate + "-" + SessUserID_Navigate;
                        if (application.getAttribute(sNameSess) != null) {
                            sValue_Navigate = application.getAttribute(sNameSess).toString();
                            sViewTable_Navigate = "";
                        }
            %>
            <li id="idContentPushRequest" role="presentation" class="dropdown" style="padding-top: 1.1%;padding-right: 2%;display: <%= sViewTable_Navigate%>">
                <!--<div id="idContentPushRequest" style="display: <= sViewTable_Navigate%>">-->
                <a href="javascript:;" class="dropdown-toggle info-number" data-toggle="dropdown" aria-expanded="false" onclick="clearCachePushDecline('<%= SessUserAgentID_Navigate%>', '<%= SessUserID_Navigate%>');">
                    <i class="fa fa-envelope-o"></i>
                    <span class="badge bg-green" id="idContentPushRequestCount1"><%= sValue_Navigate%></span>
                </a>
                <ul id="menu1" class="dropdown-menu list-unstyled msg_list" role="menu">
                    <li>
                        <a>
                            <span class="image"></span>
                            <!--<span>-->
                            <span><script>document.write(global_fm_title_push_approve1);</script><span id="idContentPushRequestCount2"><%= sValue_Navigate%></span><script>document.write(global_fm_title_push_decline);</script></span>
<!--                                <span class="time" id="idContentPushRequestCount2">%= sValue_Navigate%></span>
                            </span>
                            <span class="message">
                                Số lượng yêu cầu bị từ chối duyệt cấp phát chứng thư số bởi nhà cung cấp dịch vụ
                            </span>-->
                        </a>
                    </li>
                </ul>
                <!--</div>-->
                <script>
                    var messagesWaiting = false;
                    function getMessages() {
                        if(!messagesWaiting) {
                            messagesWaiting = true;
                            var vBRANCH_ID_Navigate = '<%= SessUserAgentID_Navigate%>';
                            var vUserID_Navigate = '<%= SessUserID_Navigate%>';
                            var xmlhttp = new XMLHttpRequest();
                            xmlhttp.onreadystatechange=function(){
                                if (xmlhttp.readyState===4 && xmlhttp.status===200) {
                                    messagesWaiting = false;
//                                    alert(sSpace(xmlhttp.responseText));
                                    var vResponse = sSpace(xmlhttp.responseText).split('#');
                                    if(vBRANCH_ID_Navigate === vResponse[1] && vUserID_Navigate === vResponse[2])
                                    {
                                        $("#idContentPushRequest").css("display", "");
                                        var idContentPushRequestCount1 = document.getElementById("idContentPushRequestCount1");
                                        var idContentPushRequestCount2 = document.getElementById("idContentPushRequestCount2");
                                        var sCountRequest = sSpace(idContentPushRequestCount1.innerHTML);
                                        if(sCountRequest !== "")
                                        {
                                            idContentPushRequestCount1.innerHTML = parseInt(sCountRequest) + 1;
                                            idContentPushRequestCount2.innerHTML = parseInt(sCountRequest) + 1;
                                        } else {
                                            idContentPushRequestCount1.innerHTML = 1;
                                            idContentPushRequestCount2.innerHTML = 1;
                                        }
                                    }
                                }
                            };
                            xmlhttp.open("GET", "../PushNotiRequestDecline?t="+new Date(), true);
                            xmlhttp.send();
                        }
                    }
                    setInterval(getMessages, 1000);
                    function clearCachePushDecline(idBRANCH, idUser)
                    {
                        $.ajax({
                            type: "post",
                            url: "../SomeCommon",
                            data: {
                                idParam: 'deletepushnotirequestdecline',
                                idBRANCH: idBRANCH,
                                idUser: idUser
                            },
                            cache: false,
                            success: function (html)
                            {
                                var myStrings = sSpace(html).split('#');
                                if (myStrings[0] !== "0")
                                {
                                    funErrorAlert(global_errorsql);
                                }
                            }
                        });
                        return false;
                    }
                </script>
            </li>
            <%    
                    }
                }
            %>
        </ul>
    </nav>
    <script>
        function loginEN(id)
        {
            var s = "0";
            if (id === "1")
            {
                localStorage.setItem("VN", "0");
                s = "0";
            }
            else
            {
                localStorage.setItem("VN", "1");
                s = "1";
            }
            $.ajax({
                type: "post",
                url: "../SomeCommon",
                data: {
                    idParam: 'languagepage',
                    svn: s
                },
                cache: false,
                success: function (html)
                {
                    location.reload();
                }
            });
            return false;
            location.reload();
        }
    </script>
</div>