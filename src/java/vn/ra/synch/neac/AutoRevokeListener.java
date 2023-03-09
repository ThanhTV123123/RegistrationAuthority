/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ra.synch.neac;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import vn.mobileid.fms.client.JCRManager;
import vn.ra.object.ATTRIBUTE_VALUES;
import vn.ra.object.CERTIFICATION;
import vn.ra.object.CERTIFICATION_AUTHORITY;
import vn.ra.object.CERTIFICATION_COMMENT;
import vn.ra.object.GENERAL_POLICY;
import vn.ra.process.CommonFunction;
import vn.ra.process.ConnectConnector;
import vn.ra.process.ConnectDatabase;
import vn.ra.process.ConnectDbPhaseTwo;
import vn.ra.process.JackRabbitCommon;
import vn.ra.utility.Config;
import vn.ra.utility.Definitions;
import vn.ra.utility.EscapeUtils;

/**
 *
 * @author vanth
 */
public class AutoRevokeListener implements ServletContextListener {
    /**
     * @param arg0
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    @Override
    public void contextInitialized(ServletContextEvent arg0) {
        System.out.println("*********AutoRevokeListener started*********");
        Config conf = new Config();
        String sEnabledNEAC = conf.GetTryPropertybyCode(Definitions.CONFIG_SCAN_REVOKE_AUTO_ENABLED);
        if("1".equals(sEnabledNEAC)) {
            String sHour = conf.GetTryPropertybyCode(Definitions.CONFIG_SCAN_REVOKE_HOUR_OF_DATE);
            String sMinute = conf.GetTryPropertybyCode(Definitions.CONFIG_SCAN_REVOKE_MINUTE_OF_DATE);
            String sSecond = conf.GetTryPropertybyCode(Definitions.CONFIG_SCAN_REVOKE_SECOND_OF_DATE);
            if(!"".equals(sHour) && !"".equals(sMinute) & !"".equals(sSecond)) {
                final int intHour = Integer.parseInt(sHour);
                final int intMinute = Integer.parseInt(sMinute);
                final int intSecond = Integer.parseInt(sSecond);
            
                final String sTYPE_OF_DATE = conf.GetPrintPropertybyCode(Definitions.CONFIG_SCAN_REVOKE_TYPE_OF_DATE, "");
                final int intAMPM = "0".equals(sTYPE_OF_DATE) ? Calendar.PM : Calendar.AM;
                ServletContext servletContext = arg0.getServletContext();
                Timer timer = new Timer();
                //<editor-fold defaultstate="collapsed" desc="Set Time In day">
                final Calendar date = Calendar.getInstance();
                date.set(
                    Calendar.HOUR_OF_DAY,
                    intAMPM
                );
                date.set(Calendar.HOUR, intHour);
                date.set(Calendar.MINUTE, intMinute);
                date.set(Calendar.SECOND, intSecond);
                date.set(Calendar.MILLISECOND, 0);
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        CommonFunction.LogDebugString(null, "RevokeScan start at Time: ", date.getTime() + " ....");
                        CERTIFICATION[][] rsScan;
                        ConnectDatabase dbPhaseOne = new ConnectDatabase();
                        ConnectDbPhaseTwo dbPhaseTwo = new ConnectDbPhaseTwo();
                        try {
                            int intLimitChange = 0;
                            int intLimitRenew = 0;
                            String sChangeEnabled = "0";
                            String sRenewEnabled = "0";
                            String sDeleteCerRenewEnabled = "0";
                            String sDeleteCerChangeInfoEnabled = "0";
                            GENERAL_POLICY[][] rsPolicy = new GENERAL_POLICY[1][];
                            dbPhaseOne.S_BO_GENERAL_POLICY_LIST("1", rsPolicy);
                            if (rsPolicy[0].length > 0) {
                                for (GENERAL_POLICY rsPolicy1 : rsPolicy[0]) {
                                    if (rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_BO_DAYS_AUTO_REVOKE_AFTER_REVISION)) {
                                        intLimitChange = Integer.parseInt(rsPolicy1.VALUE);
                                    }
                                    if (rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_BO_DAYS_AUTO_REVOKE_AFTER_RENEWAL)) {
                                        intLimitRenew = Integer.parseInt(rsPolicy1.VALUE);
                                    }
                                    if (rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_BO_AUTO_REVOKE_AFTER_REVISION_OPTION)) {
                                        sChangeEnabled = rsPolicy1.VALUE;
                                    }
                                    if (rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_BO_AUTO_REVOKE_AFTER_RENEWAL_OPTION)) {
                                        sRenewEnabled = rsPolicy1.VALUE;
                                    }
                                    if (rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_BO_DELETE_CERT_IN_TOKEN_FOR_AUTO_REVOKE_AFTER_RENEWAL)) {
                                        sDeleteCerRenewEnabled = rsPolicy1.VALUE;
                                    }
                                    if (rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_BO_DELETE_CERT_IN_TOKEN_FOR_AUTO_REVOKE_AFTER_REVISION)) {
                                        sDeleteCerChangeInfoEnabled = rsPolicy1.VALUE;
                                    }
                                }
                            }
                            CERTIFICATION[][] rsListScan;
                            //<editor-fold defaultstate="collapsed" desc="AUTO CHANGE INFO">
                            if("1".equals(sChangeEnabled)){
                                CommonFunction.LogDebugString(null, "BEGIN SCAN REVOKE OF CHANGE INFO", "....");
                                rsListScan = new CERTIFICATION[1][];
                                dbPhaseTwo.S_BO_CERTIFICATION_LIST_REVISION_TO_REVOKE(intLimitChange, rsListScan);
                                if(rsListScan[0].length > 0) {
                                    String sUserUID = "admin";
                                    String idDESC_DECLINE = "Automatic revoke of changeinfo after "+intLimitChange+" days";
                                    for(CERTIFICATION item : rsListScan[0]) {
                                        String sID = String.valueOf(item.ID);
                                        rsScan = new CERTIFICATION[1][];
                                        dbPhaseOne.S_BO_CERTIFICATION_DETAIL(sID, "1", rsScan);
                                        if(rsScan[0].length > 0) {
                                            if(rsScan[0][0].CERTIFICATION_STATE_ID == Definitions.CONFIG_CERTIFICATION_STATE_REVISED
                                                || rsScan[0][0].CERTIFICATION_STATE_ID == Definitions.CONFIG_CERTIFICATION_STATE_REVISED_KEEP_SN
                                                || rsScan[0][0].CERTIFICATION_STATE_ID == Definitions.CONFIG_CERTIFICATION_STATE_REISSUED)
                                            {
                                                String sTOKEN_SN = EscapeUtils.CheckTextNull(rsScan[0][0].TOKEN_SN);
                                                ATTRIBUTE_VALUES valueATTR;
                                                valueATTR = new ATTRIBUTE_VALUES();
                                                valueATTR.setTokenSn(sTOKEN_SN);
                                                valueATTR.setCerttificateRevokeReason(idDESC_DECLINE);
                                                valueATTR.setCertRevokeDeleteInTokenEnabled(true);
                                                valueATTR.setTypeName(Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_REVOKE);
                                                valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PENDING);
                                                valueATTR.setCreateUser("Administrator ("+sUserUID+")");
                                                valueATTR.setCreateDt(new Date());
                                                int[] pCERTIFICATE_ATTR_ID = new int[1];
                                                String sParam = dbPhaseOne.S_BO_CERTIFICATION_ATTR_INSERT(sID, String.valueOf(Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REVOKE),
                                                    CommonFunction.GenJSONTokenATTR(valueATTR), sUserUID, pCERTIFICATE_ATTR_ID);
                                                if ("0".equals(sParam)) {
                                                    valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PRE_APPROVED);
                                                    valueATTR.setApproveUser("Administrator ("+sUserUID+")");
                                                    valueATTR.setApproveDt(new Date());
                                                    dbPhaseOne.S_BO_CERTIFICATION_PRE_APPROVED(pCERTIFICATE_ATTR_ID[0], CommonFunction.GenJSONTokenATTR(valueATTR), sUserUID);
                                                    int sRevokeReason = Definitions.CONFIG_CERTIFICATION_REVOKE_REASON_UNSPECIFIED_ID;
                                                    valueATTR.setCerttificateRevokeEJBCAReason(String.valueOf(sRevokeReason));
                                                    valueATTR.setCertRevokeDeleteInTokenEnabled(false);
                                                    valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_APPROVED);
                                                    valueATTR.setApproveUser("Administrator ("+sUserUID+")");
                                                    valueATTR.setApproveCADt(new Date());
                                                    String sApprove = dbPhaseOne.S_BO_CERTIFICATION_APPROVED(pCERTIFICATE_ATTR_ID[0], CommonFunction.GenJSONTokenATTR(valueATTR), sUserUID);
                                                    if("0".equals(sApprove)) {
                                                        String pCA_NAME = "";
                                                        CERTIFICATION_AUTHORITY[][] rsCERTIFICATION_AUTHORITY = new CERTIFICATION_AUTHORITY[1][];
                                                        dbPhaseOne.S_BO_CERTIFICATION_AUTHORITY_DETAIL(String.valueOf(rsScan[0][0].CERTIFICATION_AUTHORITY_ID), rsCERTIFICATION_AUTHORITY);
                                                        if (rsCERTIFICATION_AUTHORITY[0].length > 0) {
                                                            pCA_NAME = EscapeUtils.CheckTextNull(rsCERTIFICATION_AUTHORITY[0][0].NAME);
                                                        }
                                                        int[] intRes = new int[1];
                                                        String[] sRes = new String[1];
                                                        ConnectConnector.RevokeCertificate(sTOKEN_SN, EscapeUtils.CheckTextNull(rsScan[0][0].CERTIFICATION_SN), sRevokeReason,
                                                            pCA_NAME, "", intRes, sRes, Integer.parseInt(sID), pCERTIFICATE_ATTR_ID[0]);
                                                        if (intRes[0] == 0) {
                                                            int deleteCerChangeInfoEnabled = 0;
                                                            if("1".equals(sDeleteCerChangeInfoEnabled)) {
                                                                deleteCerChangeInfoEnabled = 1;
                                                            }
                                                            dbPhaseOne.S_BO_CERTIFICATION_REVOKED(pCERTIFICATE_ATTR_ID[0], deleteCerChangeInfoEnabled, sUserUID);
                                                            ObjectMapper objectMapper = new ObjectMapper();
                                                            CERTIFICATION_COMMENT jsonCertComment = new CERTIFICATION_COMMENT();
                                                            jsonCertComment.certificateDeclineReason = "";
                                                            jsonCertComment.certificateSuspendReason = "";
                                                            jsonCertComment.certificateRevokeReason = idDESC_DECLINE;
                                                            dbPhaseOne.S_BO_CERTIFICATION_UPDATE_REVOKED_REASON(sID, objectMapper.writeValueAsString(jsonCertComment), sUserUID);
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            //</editor-fold>
                            
                            //<editor-fold defaultstate="collapsed" desc="AUTO RENEW">
                            if("1".equals(sRenewEnabled)){
                                CommonFunction.LogDebugString(null, "BEGIN SCAN REVOKE OF RENEWAL", "....");
                                rsListScan = new CERTIFICATION[1][];
                                dbPhaseTwo.S_BO_CERTIFICATION_LIST_RENEWAL_TO_REVOKE(intLimitRenew, rsListScan);
                                if(rsListScan[0].length > 0) {
                                    String sUserUID = "admin";
                                    String idDESC_DECLINE = "Automatic revoke of renewal after "+intLimitRenew+" days";
                                    for(CERTIFICATION item : rsListScan[0]) {
                                        String sID = String.valueOf(item.ID);
                                        rsScan = new CERTIFICATION[1][];
                                        dbPhaseOne.S_BO_CERTIFICATION_DETAIL(sID, "1", rsScan);
                                        if(rsScan[0].length > 0) {
                                            if(rsScan[0][0].CERTIFICATION_STATE_ID == Definitions.CONFIG_CERTIFICATION_STATE_RENEWED
                                                || rsScan[0][0].CERTIFICATION_STATE_ID == Definitions.CONFIG_CERTIFICATION_STATE_RENEWED_KEEP_SN)
                                            {
                                                String sTOKEN_SN = EscapeUtils.CheckTextNull(rsScan[0][0].TOKEN_SN);
                                                ATTRIBUTE_VALUES valueATTR;
                                                valueATTR = new ATTRIBUTE_VALUES();
                                                valueATTR.setTokenSn(sTOKEN_SN);
                                                valueATTR.setCerttificateRevokeReason(idDESC_DECLINE);
                                                valueATTR.setCertRevokeDeleteInTokenEnabled(false);
                                                valueATTR.setTypeName(Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_REVOKE);
                                                valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PENDING);
                                                valueATTR.setCreateUser("Administrator ("+sUserUID+")");
                                                valueATTR.setCreateDt(new Date());
                                                int[] pCERTIFICATE_ATTR_ID = new int[1];
                                                String sParam = dbPhaseOne.S_BO_CERTIFICATION_ATTR_INSERT(sID, String.valueOf(Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REVOKE),
                                                    CommonFunction.GenJSONTokenATTR(valueATTR), sUserUID, pCERTIFICATE_ATTR_ID);
                                                if ("0".equals(sParam)) {
                                                    valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PRE_APPROVED);
                                                    valueATTR.setApproveUser("Administrator ("+sUserUID+")");
                                                    valueATTR.setApproveDt(new Date());
                                                    dbPhaseOne.S_BO_CERTIFICATION_PRE_APPROVED(pCERTIFICATE_ATTR_ID[0], CommonFunction.GenJSONTokenATTR(valueATTR), sUserUID);
                                                    int sRevokeReason = Definitions.CONFIG_CERTIFICATION_REVOKE_REASON_UNSPECIFIED_ID;
                                                    valueATTR.setCerttificateRevokeEJBCAReason(String.valueOf(sRevokeReason));
                                                    valueATTR.setCertRevokeDeleteInTokenEnabled(false);
                                                    valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_APPROVED);
                                                    valueATTR.setApproveUser("Administrator ("+sUserUID+")");
                                                    valueATTR.setApproveCADt(new Date());
                                                    String sApprove = dbPhaseOne.S_BO_CERTIFICATION_APPROVED(pCERTIFICATE_ATTR_ID[0], CommonFunction.GenJSONTokenATTR(valueATTR), sUserUID);
                                                    if("0".equals(sApprove)) {
                                                        String pCA_NAME = "";
                                                        CERTIFICATION_AUTHORITY[][] rsCERTIFICATION_AUTHORITY = new CERTIFICATION_AUTHORITY[1][];
                                                        dbPhaseOne.S_BO_CERTIFICATION_AUTHORITY_DETAIL(String.valueOf(rsScan[0][0].CERTIFICATION_AUTHORITY_ID), rsCERTIFICATION_AUTHORITY);
                                                        if (rsCERTIFICATION_AUTHORITY[0].length > 0) {
                                                            pCA_NAME = EscapeUtils.CheckTextNull(rsCERTIFICATION_AUTHORITY[0][0].NAME);
                                                        }
                                                        int[] intRes = new int[1];
                                                        String[] sRes = new String[1];
                                                        ConnectConnector.RevokeCertificate(sTOKEN_SN, EscapeUtils.CheckTextNull(rsScan[0][0].CERTIFICATION_SN), sRevokeReason,
                                                            pCA_NAME, "", intRes, sRes, Integer.parseInt(sID), pCERTIFICATE_ATTR_ID[0]);
                                                        if (intRes[0] == 0) {
                                                            int deleteCerRenewEnabled = 0;
                                                            if("1".equals(sDeleteCerRenewEnabled)) {
                                                                deleteCerRenewEnabled = 1;
                                                            }
                                                            dbPhaseOne.S_BO_CERTIFICATION_REVOKED(pCERTIFICATE_ATTR_ID[0], deleteCerRenewEnabled, sUserUID);
                                                            ObjectMapper objectMapper = new ObjectMapper();
                                                            CERTIFICATION_COMMENT jsonCertComment = new CERTIFICATION_COMMENT();
                                                            jsonCertComment.certificateDeclineReason = "";
                                                            jsonCertComment.certificateSuspendReason = "";
                                                            jsonCertComment.certificateRevokeReason = idDESC_DECLINE;
                                                            dbPhaseOne.S_BO_CERTIFICATION_UPDATE_REVOKED_REASON(sID, objectMapper.writeValueAsString(jsonCertComment), sUserUID);
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            //</editor-fold>
                            CommonFunction.LogDebugString(null, "RevokeScan end at Time: ", date.getTime() + " ....");
                        } catch (Exception ex) {
                            CommonFunction.LogExceptionJSP("SynchNEACListener", ex.getMessage(), ex);
                        }
                    }//End of Run
                }, date.getTime(), 1000 * 60 * 60 * 24);
                servletContext.setAttribute("timerAutoRevoke", timer);
                //</editor-fold>

                //<editor-fold defaultstate="collapsed" desc="Set Time in delay">
        //final Calendar calendar = Calendar.getInstance();
        //System.out.println("Tweet at Time = " + calendar.getTime());
        //calendar.add(Calendar.SECOND, -60);
                /*int delay = 1000;
                timer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        System.out.println("Running this code every 1 minute: " + ParameterLoadRAM.prefixFor("MINUTE") + "....");
                    }//End of Run
                }, delay, 9000);
                servletContext.setAttribute("timer", timer);*/
                //</editor-fold>
            
            }
        
        }
        
    }

    /**
     * @param arg0
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
        ServletContext servletContext = arg0.getServletContext();
// get our timer from the Context
        Timer timer = (Timer) servletContext.getAttribute("timerAutoRevoke");

// cancel all pending tasks in the timers queue
        if (timer != null) {
            timer.cancel();
        }

// remove the timer from the servlet context
        servletContext.removeAttribute("timerAutoRevoke");
        System.out.println("SynchNEACListener destroyed");

    }
}
