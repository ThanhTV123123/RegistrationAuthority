<%-- 
    Document   : captcha
    Created on : Dec 9, 2015, 2:56:49 PM
    Author     : Tran
--%>

<%@page import="vn.ra.process.CommonFunction"%>
<%@page import="vn.ra.process.ConnectDatabase"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="javax.imageio.ImageIO"%>
<%@page import="java.awt.image.BufferedImage"%>
<%@page import="java.awt.*"%>
<%@ page import="java.io.*" %>
<%@ page import="java.util.*"%>
<%
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
    response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
    response.setDateHeader("Expires", 0); // Proxies.
    String strResult = "";
    CommonFunction com = new CommonFunction();
    String sImageCode = "";
    int iTotalChars = com.randInt(3, 6);
    int iHeight = 35;
    int iWidth = 155;
    try {
        Font fntStyle1 = new Font("Arial", Font.BOLD, 25) {
        };
        Font fntStyle2 = new Font("Verdana", Font.BOLD, 20);
        Random randChars = new Random();
        sImageCode = (Long.toString(Math.abs(randChars.nextLong()), 36)).substring(0, iTotalChars);
        session.setAttribute("sessCaptchaCode", sImageCode);
        BufferedImage biImage = new BufferedImage(iWidth, iHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2dImage = (Graphics2D) biImage.getGraphics();
        int iCircle = 15;
        for (int i = 0; i < iCircle; i++) {
            //g2dImage.setColor(new Color(randChars.nextInt(255), randChars.nextInt(255), randChars.nextInt(255)));
            int iRadius = (int) (Math.random() * iHeight / 2.0);
            int iX = (int) (Math.random() * iWidth - iRadius);
            int iY = (int) (Math.random() * iHeight - iRadius);
            //g2dImage.fillRoundRect(iX, iY, iRadius * 2, iRadius * 2,100,100);
        }
        g2dImage.setFont(fntStyle1);
        for (int i = 0; i < iTotalChars; i++) {
            //g2dImage.setColor(new Color(randChars.nextInt(255), randChars.nextInt(255), randChars.nextInt(255)));
            if (i % 2 == 0) {
                g2dImage.drawString(sImageCode.substring(i, i + 1), 25 * i, 29);
            } else {
                g2dImage.drawString(sImageCode.substring(i, i + 1), 25 * i, 29);
            }
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(biImage, "jpg", baos);
        baos.flush();
        byte[] imageInByteArray = baos.toByteArray();
        baos.close();
        String b64 = javax.xml.bind.DatatypeConverter.printBase64Binary(imageInByteArray);
        strResult = sImageCode + "##" + b64;
        g2dImage.dispose();
    } catch (Exception e) {
        CommonFunction.LogExceptionServlet(null,"Captcha_jsp: "+ e.getMessage(), e);
        strResult = e.getMessage() + "##";
    }
%>
<%= strResult%>