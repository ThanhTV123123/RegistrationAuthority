/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ra.process;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import vn.ra.utility.Definitions;

/**
 *
 * @author THANH
 */
public class SendMail {

    /**
     * SendMail
     */
    public SendMail() {
    }

    /**
     *
     * @param d_email_server
     * @param d_password
     * @param d_host
     * @param d_port
     * @param m_to
     * @param m_subject
     * @param m_text
     * @throws MessagingException
     */
    public void sendEamilGmail(final String d_email_server, final String d_password, String d_host, String d_port,
            String m_to, String m_subject, String m_text) throws MessagingException {
        Properties props = new Properties();
        props.put("mail.smtp.user", d_email_server);
        props.put("mail.smtp.host", d_host);
        props.put("mail.smtp.port", d_port);
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.socketFactory.port", d_port);
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");
        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(d_email_server, d_password);
                    }
                });
        session.setDebug(true);
        MimeMessage msg = new MimeMessage(session);
        msg.setText(m_text, Definitions.CONFIG_UNICODE_UTF_8);
        msg.setSubject(m_subject, Definitions.CONFIG_UNICODE_UTF_8);
        msg.setFrom(new InternetAddress(d_email_server));
        msg.addRecipient(Message.RecipientType.TO, new InternetAddress(m_to));
        Transport.send(msg);
    }

    ////////// Send Mail Domain /////

    /**
     *
     * @param s_MailServer
     * @param s_host
     * @param s_Port
     * @param s_MailCustomer
     * @param s_Pass
     * @param strSubject
     * @param strContent
     * @throws MessagingException
     */
    public void SendMailDomain(String s_MailServer, String s_host, String s_Port, String s_MailCustomer,
            String s_Pass, String strSubject, String strContent) throws MessagingException {
        try {
            MimeMessage message = new MimeMessage(getSession(s_MailServer, s_host, s_Port, s_Pass));
            message.addRecipient(RecipientType.TO, new InternetAddress(s_MailCustomer));
            message.addFrom(new InternetAddress[]{new InternetAddress(s_MailServer)});
            message.setSubject(strSubject, Definitions.CONFIG_UNICODE_UTF_8);
            message.setContent(strContent, "text/plain; charset=UTF-8");
            Transport.send(message);
        } catch (RuntimeException e) {
            CommonFunction.LogExceptionServlet(null, "SendMailDomain: " + e.getMessage(), e);
        } catch (Exception e) {
            CommonFunction.LogExceptionServlet(null, "SendMailDomain: " + e.getMessage(), e);
        }
    }

    private Session getSession(String s_MailServer, String s_host, String s_Port, String s_Pass) {
        Authenticator authenticator = new Authenticator(s_MailServer, s_Pass);
        Properties properties = new Properties();
        properties.setProperty("mail.smtp.submitter", authenticator.getPasswordAuthentication().getUserName());
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.host", s_host);
        properties.setProperty("mail.smtp.port", s_Port);
        return Session.getInstance(properties, authenticator);
    }

    private class Authenticator extends javax.mail.Authenticator {

        private final PasswordAuthentication authentication;

        public Authenticator(String s_MailServer, String s_Pass) {
            String username = s_MailServer;
            String password = s_Pass;
            authentication = new PasswordAuthentication(username, password);
        }

        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
            return authentication;
        }
    }
}
