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
import org.apache.log4j.Logger;
import vn.ra.utility.Config;
import vn.ra.utility.Definitions;

/**
 *
 * @author vanth
 */
public class DatabaseConfig {

    private static int COUNT_CONNECTIONS = 0;
    ObjectMapper oMapperParse;
    String Choise_TypeDB;
    String Driver_Sql;
    String Driver_Oracle;
    String Url_Sql;
    String Url_Oracle;
    String UserName_SQL;
    String Password_SQL;
    String Password_ORACLE;
    String UserName_ORACLE;
    String DBConnect_Timeout = "120";
    private static final Logger log = Logger.getLogger(ConnectDatabase.class);

    public DatabaseConfig() {
        Config conf = new Config();
        Choise_TypeDB = conf.GetPropertybyCode(Definitions.CONFIG_CHOISEDB);
        Driver_Sql = conf.GetPropertybyCode(Definitions.CONFIG_DBDRIVER_SQL);
        Driver_Oracle = conf.GetPropertybyCode(Definitions.CONFIG_DBDRIVER_ORACLE);
        Url_Sql = conf.GetPropertybyCode(Definitions.CONFIG_DBCONNECTION_SQL);
        Url_Oracle = conf.GetPropertybyCode(Definitions.CONFIG_DBCONNECTION_ORACLE);
        UserName_SQL = conf.GetPropertybyCode(Definitions.CONFIG_DBUSER_SQL);
        Password_SQL = conf.GetPropertybyCode(Definitions.CONFIG_DBPASS_SQL);
        UserName_ORACLE = conf.GetPropertybyCode(Definitions.CONFIG_DBUSER_ORACLE);
        Password_ORACLE = conf.GetPropertybyCode(Definitions.CONFIG_DBPASS_ORACLE);
        DBConnect_Timeout = conf.GetPropertybyCode(Definitions.CONFIG_DATABASE_CONNECT_TIMEOUT_SECOND);
    }

    public Connection OpenDatabase() throws Exception {
        Connection connInner = null;
        if (null != Choise_TypeDB.trim()) {
            switch (Choise_TypeDB.trim()) {
                case "1":
                    Class.forName(Driver_Sql);
                    DriverManager.setLoginTimeout(Integer.parseInt(DBConnect_Timeout));
                    connInner = DriverManager.getConnection(Url_Sql, UserName_SQL, Password_SQL);
                    break;
                case "2":
                    Class.forName(Driver_Oracle);
                    DriverManager.setLoginTimeout(Integer.parseInt(DBConnect_Timeout));
                    connInner = DriverManager.getConnection(Url_Oracle, UserName_ORACLE, Password_ORACLE);
                    break;
                default:
                    break;
            }
        }
        COUNT_CONNECTIONS++;
        return connInner;
    }

    public void CloseDatabase(Connection[] temp) throws Exception {
        if (temp[0] != null) {
            temp[0].close();
            COUNT_CONNECTIONS--;
        }
    }

    public static int getCount() {
        return COUNT_CONNECTIONS;
    }
}
