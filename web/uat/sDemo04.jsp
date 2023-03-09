<%-- 
    Document   : sDemo04
    Created on : Jun 8, 2018, 4:03:57 PM
    Author     : THANH-PC
--%>

<%@page import="java.io.ByteArrayInputStream"%>
<%@page import="org.apache.commons.io.IOUtils"%>
<%@page import="java.io.InputStream"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>File Manager</title>
    </head>
    <body>
        <div class="form-group" style="padding: 10px 10px 0 10px;margin: 0;">
            <%
                byte[] imageBytes;
                String sCert = "MIICiDCCAi6gAwIBAgIQQ6Ue3Pq1D35NWg/TrKXiUjAKBggqhkjOPQQDAjBpMQswCQYDVQQGEwJWTjE7MDkGA1UECgwyVmlldG5hbSBHb3Zlcm5tZW50IEluZm9ybWF0aW9uIFNlY3VyaXR5IENvbW1pc3Npb24xHTAbBgNVBAMMFENTQ0EgZUhlYWx0aCBWaWV0bmFtMB4XDTIxMDgwNDA2NDQwNFoXDTIzMDgwNDA2NDQwNFowXTELMAkGA1UEBhMCVk4xGzAZBgNVBAoMEk1pbmlzdHJ5IG9mIEhlYWx0aDExMC8GA1UEAwwoRG9jdW1lbnQgU2lnbmVyIFZhY3hpbiBDZXJ0aWZpY2F0ZSAwMDAwMTBZMBMGByqGSM49AgEGCCqGSM49AwEHA0IABGwYjQi8LtXaj/nelqYLiFIRMO2Z/0H6lc3gnhamzhz8+07K+7FuJpUUeDV2qu4W87WfJGWo/U9tJFtYa0mLMESjgcMwgcAwDAYDVR0TAQH/BAIwADAfBgNVHSMEGDAWgBSfCkZ/QM0B8Q/SUZ3mQ4eKKJljNTAzBgNVHSUELDAqBgwrBgEEAQCON49lAQMGDCsGAQQBAI43j2UBAgYMKwYBBAEAjjePZQEBMB0GA1UdDgQWBBTmDfYKIwF2RKu+b6jg5SHgjcNrbTArBgNVHRAEJDAigA8yMDIxMDgwNDA2NDQwNFqBDzIwMjIwMTMxMDY0NDA0WjAOBgNVHQ8BAf8EBAMCB4AwCgYIKoZIzj0EAwIDSAAwRQIgLwrYStbO5JPSSzxM7NGZIWEiysBOl9ePSpv8G4xxBJcCIQDfrkLrPG0OgwfjtRmep3RtGUY/N8sqzESD/PzvLi43sg==";
                InputStream data = new ByteArrayInputStream(sCert.getBytes());
                if (data != null) {
                    imageBytes = IOUtils.toByteArray(data);
                    if(imageBytes != null)
                    {
                        response.setContentType("application/pkix-cert");
                        response.setContentLength(imageBytes.length);
                        response.getOutputStream().write(imageBytes);
                        response.getOutputStream().flush();
                        response.getOutputStream().close();
                        out.clear();
                        out = pageContext.pushBody();
                    }
                }
            %>
    </body>
</html>
