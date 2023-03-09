/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ra.process;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import org.apache.log4j.Logger;
import vn.ra.object.CERTIFICATION;
import vn.ra.utility.Config;
import vn.ra.utility.Definitions;
import vn.ra.utility.EscapeUtils;

/**
 *
 * @author vanth
 */
public class ConnectDatabaseReport {

    private static int COUNT_CONNECTIONS = 0;
    CallableStatement proc_stmt = null;
    ObjectMapper oMapperParse;
    String Driver_Sql;
    String Url_Sql;
    String UserName_SQL;
    String Password_SQL;
    String DBConnect_Timeout = "120";
    private static final Logger log = Logger.getLogger(ConnectDatabaseReport.class);

    public ConnectDatabaseReport() {
        Config conf = new Config();
        Driver_Sql = conf.GetPropertybyCode(Definitions.CONFIG_DBDRIVER_SQL);
        Url_Sql = conf.GetPropertybyCode("REPORT_DBConnectionSQL");
        UserName_SQL = conf.GetPropertybyCode("REPORT_DBUser");
        Password_SQL = conf.GetPropertybyCode("REPORT_DBPass");
        DBConnect_Timeout = conf.GetPropertybyCode(Definitions.CONFIG_DATABASE_CONNECT_TIMEOUT_SECOND);
    }

    public Connection OpenDatabase() throws Exception {
        Connection connInner = null;
        Class.forName(Driver_Sql);
        DriverManager.setLoginTimeout(Integer.parseInt(DBConnect_Timeout));
        connInner = DriverManager.getConnection(Url_Sql, UserName_SQL, Password_SQL);
        COUNT_CONNECTIONS++;
        return connInner;
    }

    public void CloseDatabase(Connection[] temp) throws Exception {
        if (temp[0] != null) {
            temp[0].close();
            COUNT_CONNECTIONS--;
        }
    }
    
    public void SP_BO_TMS_REPORT_CERTIFICATE_LIST(int sPage, int sSum, CERTIFICATION[][] response)
            throws Exception {
        ArrayList<CERTIFICATION> tempList = new ArrayList<>();
        Connection conns = null;
        ResultSet rs = null;
        try {
            conns = OpenDatabase();
            proc_stmt = conns.prepareCall("{ call SP_BO_TMS_REPORT_CERTIFICATE_LIST(?,?) }");
            proc_stmt.setInt(1, sPage);
            proc_stmt.setInt(2, sSum);
            rs = proc_stmt.executeQuery();
            while (rs.next()) {
                CERTIFICATION tempItem = new CERTIFICATION();
                tempItem.ID = rs.getInt("CERTIFICATE_ID");
                tempItem.CERTIFICATION_SN = EscapeUtils.CheckTextNull(rs.getString("CERTIFICATE_SN"));
                tempItem.CERTIFICATE_THUMBPRINT = rs.getString("CERTIFICATE_THUMBPRINT");
                int sRemaining = rs.getInt("REMAINING_SIGNING_COUNTER");
                if(sRemaining == -1){
                    tempItem.REMAINING_SIGNING_COUNTER = "UNLIMITED";
                } else {
                    tempItem.REMAINING_SIGNING_COUNTER = String.valueOf(sRemaining);
                }
                tempList.add(tempItem);
            }
            response[0] = new CERTIFICATION[tempList.size()];
            response[0] = tempList.toArray(response[0]);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (proc_stmt != null) {
                proc_stmt.close();
            }
            Connection[] temp_connection = new Connection[]{conns};
            CloseDatabase(temp_connection);
        }
    }
    
    //<editor-fold defaultstate="collapsed" desc="SP_BO_TMS_REPORT_CERTIFICATE_TOTAL">
    public int SP_BO_TMS_REPORT_CERTIFICATE_TOTAL() throws Exception {
        Connection conns = null;
        int ss = 0;
        try {
            conns = OpenDatabase();
            proc_stmt = conns.prepareCall("{ call SP_BO_TMS_REPORT_CERTIFICATE_TOTAL(?) }");
            proc_stmt.registerOutParameter(1, java.sql.Types.INTEGER);
            CommonFunction.LogDebugString(log, "SP_BO_TMS_REPORT_CERTIFICATE_TOTAL", proc_stmt.toString());
            proc_stmt.execute();
            ss = proc_stmt.getInt(1);
        } finally {
            if (proc_stmt != null) {
                proc_stmt.close();
            }
            Connection[] temp_connection = new Connection[]{conns};
            CloseDatabase(temp_connection);
        }
        return ss;
    }
    //</editor-fold>
    
}
