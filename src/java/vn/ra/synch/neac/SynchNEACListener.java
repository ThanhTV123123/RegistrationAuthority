/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ra.synch.neac;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import vn.ra.object.GENERAL_POLICY;
import vn.ra.object.NEAC_LOG;
import vn.ra.process.CommonFunction;
import vn.ra.process.ConnectDatabase;
import vn.ra.process.SynchNEACFunction;
import vn.ra.utility.Config;
import vn.ra.utility.Definitions;
import vn.ra.utility.LoadParamSystem;

/**
 *
 * @author USER
 */
public class SynchNEACListener implements ServletContextListener {
    /**
     * @param arg0
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    @Override
    public void contextInitialized(ServletContextEvent arg0) {
        System.out.println("*********SynchNEACListener started*********");
        Config conf = new Config();
        String sSourceNEAC = conf.GetPrintPropertybyCode(Definitions.CONFIG_SYNCH_NEAC_WS_SOURCE, "");
        String sUrlNEAC = conf.GetPrintPropertybyCode(Definitions.CONFIG_SYNCH_NEAC_WS_URL, "");
        String sUserIDNEAC = conf.GetPrintPropertybyCode(Definitions.CONFIG_SYNCH_NEAC_WS_USERID, "");
        String sUserKeyNEAC = conf.GetPrintPropertybyCode(Definitions.CONFIG_SYNCH_NEAC_WS_USERKEY, "");
        String sEnabledNEAC = conf.GetPrintPropertybyCode(Definitions.CONFIG_SYNCH_NEAC_AUTO_ENABLED, "");
        LoadParamSystem.updateParamSystem(Definitions.CONFIG_SYNCH_NEAC_WS_SOURCE, sSourceNEAC);
        LoadParamSystem.updateParamSystem(Definitions.CONFIG_SYNCH_NEAC_WS_URL, sUrlNEAC);
        LoadParamSystem.updateParamSystem(Definitions.CONFIG_SYNCH_NEAC_WS_USERID, sUserIDNEAC);
        LoadParamSystem.updateParamSystem(Definitions.CONFIG_SYNCH_NEAC_WS_USERKEY, sUserKeyNEAC);
        if("1".equals(sEnabledNEAC)) {
            final int intHour = Integer.parseInt(conf.GetPrintPropertybyCode(Definitions.CONFIG_SYNCH_NEAC_HOUR_OF_DATE, ""));
            final int intMinute = Integer.parseInt(conf.GetPrintPropertybyCode(Definitions.CONFIG_SYNCH_NEAC_MINUTE_OF_DATE, ""));
            final int intSecond = Integer.parseInt(conf.GetPrintPropertybyCode(Definitions.CONFIG_SYNCH_NEAC_SECOND_OF_DATE, ""));
            
            final String sTYPE_OF_DATE = conf.GetPrintPropertybyCode(Definitions.CONFIG_SYNCH_NEAC_TYPE_OF_DATE, "");
            final int intAMPM = "0".equals(sTYPE_OF_DATE) ? Calendar.PM : Calendar.AM;
            ServletContext servletContext = arg0.getServletContext();
            //S_BO_NEAC_LOG_SCAN
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
                    CommonFunction.LogDebugString(null, "SynchNEACListener start at Time: ", date.getTime() + " ....");
                    NEAC_LOG[][] rsScan = new NEAC_LOG[1][];
                    ConnectDatabase db = new ConnectDatabase();
                    Calendar calendar;
                    calendar = Calendar.getInstance();
                    calendar.add(Calendar.DAY_OF_YEAR, -1);
                    calendar.set(Calendar.HOUR, intHour);
                    calendar.set(Calendar.MINUTE, intMinute);
                    calendar.set(Calendar.SECOND, intSecond);
                    calendar.set(Calendar.MILLISECOND, 0);
                    calendar.set(Calendar.AM_PM, intAMPM);
                    java.sql.Timestamp pFROM_DT = new Timestamp(calendar.getTimeInMillis());
                    
                    calendar = Calendar.getInstance();
                    calendar.add(Calendar.DAY_OF_YEAR, 0);
                    calendar.set(Calendar.HOUR, intHour);
                    calendar.set(Calendar.MINUTE, intMinute);
                    calendar.set(Calendar.SECOND, intSecond);
                    calendar.set(Calendar.MILLISECOND, 0);
                    calendar.set(Calendar.AM_PM, intAMPM);
                    java.sql.Timestamp pTO_DT = new Timestamp(calendar.getTimeInMillis());
                    try {
                        db.S_BO_NEAC_LOG_SCAN(pFROM_DT, pTO_DT, rsScan);
                        if(rsScan[0].length > 0) {
                            int intRemainingSystem = 0;
                            GENERAL_POLICY[][] rsPolicy = new GENERAL_POLICY[1][];
                            db.S_BO_GENERAL_POLICY_LIST("1", rsPolicy);
                            if (rsPolicy[0].length > 0) {
                                for (GENERAL_POLICY rsPolicy1 : rsPolicy[0]) {
                                    if (rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_BO_MAX_RETRY_NEAC_SYNCHRONIZATION)) {
                                        intRemainingSystem = Integer.parseInt(rsPolicy1.VALUE);
                                        break;
                                    }
                                }
                            }
                            for(NEAC_LOG item : rsScan[0]) {
                                int[] intRes = new int[1]; String[] strRes = new String[1];
                                SynchNEACFunction.synchNEACCertificate("admin", item.ID, item.CERTIFICATION_ID, item.CERTIFICATION_ATTR_TYPE_ID,
                                    item.REMAINING_COUNTER, intRemainingSystem, intRes, strRes);
                            }
                            CommonFunction.LogDebugString(null, "SynchNEACListener end at Time: ", date.getTime() + " ....");
                        } else {
                            CommonFunction.LogDebugString(null, "DATA NOT FOUND", "");
                        }
                    } catch (Exception ex) {
                        CommonFunction.LogExceptionJSP("SynchNEACListener", ex.getMessage(), ex);
                    }
                }//End of Run
            }, date.getTime(), 1000 * 60 * 60 * 24);
            servletContext.setAttribute("timerSynchNEAC", timer);
            //timer.schedule(new YourTask(), today.getTime(), TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS)); // period: 1 day
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

    /**
     * @param arg0
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
        ServletContext servletContext = arg0.getServletContext();
// get our timer from the Context
        Timer timer = (Timer) servletContext.getAttribute("timerSynchNEAC");

// cancel all pending tasks in the timers queue
        if (timer != null) {
            timer.cancel();
        }

// remove the timer from the servlet context
        servletContext.removeAttribute("timerSynchNEAC");
        System.out.println("SynchNEACListener destroyed");

    }
}
