/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ra.process;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import vn.ra.object.BRANCH;
import vn.ra.object.CertificateStateInfo;
import vn.ra.object.CERTIFICATION;
import vn.ra.object.PREFIX_UUID;
import vn.ra.object.RequestStateInfo;
import vn.ra.object.TOKEN;
import vn.ra.utility.EscapeUtils;
import vn.ra.utility.Definitions;
import vn.ra.utility.LoadParamSystem;

/**
 *
 * @author vanth - 300921
 */
public class ConnectDbPhaseTwo {

    private static final Logger log = Logger.getLogger(ConnectDatabase.class);
    DatabaseConfig dbConf = new DatabaseConfig();
    CallableStatement proc_stmt = null;

    public void S_BO_CERTIFICATION_LIST_REVISION_TO_REVOKE(int pLIMIT, CERTIFICATION[][] response)
            throws Exception {
        ArrayList<CERTIFICATION> tempList = new ArrayList<>();
        Connection conns = null;
        ResultSet rs = null;
        try {
            if (null != dbConf.Choise_TypeDB.trim()) {
                switch (dbConf.Choise_TypeDB.trim()) {
                    case "1":
                        conns = dbConf.OpenDatabase();
                        proc_stmt = conns.prepareCall("{ call S_BO_CERTIFICATION_LIST_REVISION_TO_REVOKE(?) }");
                        proc_stmt.setInt(1, pLIMIT);
                        CommonFunction.LogDebugString(log, "S_BO_CERTIFICATION_LIST_REVISION_TO_REVOKE", proc_stmt.toString());
                        rs = proc_stmt.executeQuery();
                        while (rs.next()) {
                            CERTIFICATION tempItem = new CERTIFICATION();
                            tempItem.ID = rs.getInt("ID");
                            tempList.add(tempItem);
                        }
                        response[0] = new CERTIFICATION[tempList.size()];
                        response[0] = tempList.toArray(response[0]);
                    case "2":
                        break;
                    default:
                        break;
                }
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (proc_stmt != null) {
                proc_stmt.close();
            }
            Connection[] temp_connection = new Connection[]{conns};
            dbConf.CloseDatabase(temp_connection);
        }
    }
    
    public void S_BO_CERTIFICATION_LIST_RENEWAL_TO_REVOKE(int pLIMIT, CERTIFICATION[][] response)
            throws Exception {
        ArrayList<CERTIFICATION> tempList = new ArrayList<>();
        Connection conns = null;
        ResultSet rs = null;
        try {
            if (null != dbConf.Choise_TypeDB.trim()) {
                switch (dbConf.Choise_TypeDB.trim()) {
                    case "1":
                        conns = dbConf.OpenDatabase();
                        proc_stmt = conns.prepareCall("{ call S_BO_CERTIFICATION_LIST_RENEWAL_TO_REVOKE(?) }");
                        proc_stmt.setInt(1, pLIMIT);
                        CommonFunction.LogDebugString(log, "S_BO_CERTIFICATION_LIST_RENEWAL_TO_REVOKE", proc_stmt.toString());
                        rs = proc_stmt.executeQuery();
                        while (rs.next()) {
                            CERTIFICATION tempItem = new CERTIFICATION();
                            tempItem.ID = rs.getInt("ID");
                            tempList.add(tempItem);
                        }
                        response[0] = new CERTIFICATION[tempList.size()];
                        response[0] = tempList.toArray(response[0]);
                    case "2":
                        break;
                    default:
                        break;
                }
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (proc_stmt != null) {
                proc_stmt.close();
            }
            Connection[] temp_connection = new Connection[]{conns};
            dbConf.CloseDatabase(temp_connection);
        }
    }
    
    public int S_BO_CERTIFICATION_ATTR_UPDATE_ACTIVATED_ENABLED(int pCERTIFICATION_ATTR_ID, int pACTIVATED_ENABLED)
        throws Exception {
        int convrtr = 0;
        Connection conns = null;
        try {
            if (null != dbConf.Choise_TypeDB.trim()) {
                switch (dbConf.Choise_TypeDB.trim()) {
                    case "1":
                        conns = dbConf.OpenDatabase();
                        proc_stmt = conns.prepareCall("{ call S_BO_CERTIFICATION_ATTR_UPDATE_ACTIVATED_ENABLED(?,?) }");
                        proc_stmt.setInt("pCERTIFICATION_ATTR_ID", pCERTIFICATION_ATTR_ID);
                        proc_stmt.setInt("pACTIVATED_ENABLED", pACTIVATED_ENABLED);
                        CommonFunction.LogDebugString(log, "S_BO_CERTIFICATION_ATTR_UPDATE_ACTIVATED_ENABLED", proc_stmt.toString());
                        proc_stmt.execute();
                        convrtr = 0;
                        break;
                    case "2":
                        break;
                    default:
                        break;
                }
            }
        } finally {
            if (proc_stmt != null) {
                proc_stmt.close();
            }
            Connection[] temp_connection = new Connection[]{conns};
            dbConf.CloseDatabase(temp_connection);
        }
        return convrtr;
    }
    
    public int S_RAC_CERTIFICATION_ATTR_UPDATE_CONFIRMATION_PROPERTIES(int pCERTIFICATION_ATTR_ID, String pUSERNAME_BY, String pCONFIRMATION_PROPERTIES)
        throws Exception {
        int convrtr = 0;
        Connection conns = null;
        try {
            if (null != dbConf.Choise_TypeDB.trim()) {
                switch (dbConf.Choise_TypeDB.trim()) {
                    case "1":
                        conns = dbConf.OpenDatabase();
                        proc_stmt = conns.prepareCall("{ call S_RAC_CERTIFICATION_ATTR_UPDATE_CONFIRMATION_PROPERTIES(?,?,?,?) }");
                        proc_stmt.setInt("pCERTIFICATION_ATTR_ID", pCERTIFICATION_ATTR_ID);
                        proc_stmt.setString("pUSERNAME_BY", pUSERNAME_BY);
                        proc_stmt.setString("pCONFIRMATION_PROPERTIES", pCONFIRMATION_PROPERTIES);
                        proc_stmt.registerOutParameter("pRESULT", java.sql.Types.INTEGER);
                        CommonFunction.LogDebugString(log, "S_RAC_CERTIFICATION_ATTR_UPDATE_CONFIRMATION_PROPERTIES", proc_stmt.toString());
                        proc_stmt.execute();
                        convrtr = proc_stmt.getInt("pRESULT");
                        break;
                    case "2":
                        break;
                    default:
                        break;
                }
            }
        } finally {
            if (proc_stmt != null) {
                proc_stmt.close();
            }
            Connection[] temp_connection = new Connection[]{conns};
            dbConf.CloseDatabase(temp_connection);
        }
        return convrtr;
    }
    
    //<editor-fold defaultstate="collapsed" desc="S_BO_PREFIX_UUID_COMBOBOX">
    public void S_BO_PREFIX_UUID_COMBOBOX(String sType, String pLanguage, PREFIX_UUID[][] response)
            throws Exception {
        ArrayList<PREFIX_UUID> tempList = new ArrayList<>();
        Connection conns = null;
        ResultSet rs = null;
        try {
            if (null != dbConf.Choise_TypeDB.trim()) {
                switch (dbConf.Choise_TypeDB.trim()) {
                    case "1":
                        conns = dbConf.OpenDatabase();
                        proc_stmt = conns.prepareCall("{ call S_BO_PREFIX_UUID_COMBOBOX(?,?) }");
                        proc_stmt.setString(1, sType);
                        proc_stmt.setInt(2, Integer.parseInt(pLanguage));
                        rs = proc_stmt.executeQuery();
                        while (rs.next()) {
                            PREFIX_UUID tempItem = new PREFIX_UUID();
                            tempItem.ID = rs.getInt("ID");
                            tempItem.REMARK_EN = EscapeUtils.CheckTextNull(rs.getString("REMARK_EN"));
                            tempItem.REMARK_VN = EscapeUtils.CheckTextNull(rs.getString("REMARK"));
                            tempItem.REMARK = EscapeUtils.CheckTextNull(rs.getString("PREFIX_DESC"));
                            tempItem.PREFIX_DB = EscapeUtils.CheckTextNull(rs.getString("PREFIX_DB"));
                            tempItem.PREFIX = EscapeUtils.CheckTextNull(rs.getString("PREFIX"));
                            tempList.add(tempItem);
                        }
                        response[0] = new PREFIX_UUID[tempList.size()];
                        response[0] = tempList.toArray(response[0]);
                        break;
                    case "2":
                        break;
                    default:
                        break;
                }
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (proc_stmt != null) {
                proc_stmt.close();
            }
            Connection[] temp_connection = new Connection[]{conns};
            dbConf.CloseDatabase(temp_connection);
        }
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="GET_PREFIX_UUID_TOOLTIP">
    public String GET_PREFIX_UUID_TOOLTIP(String sType, String pLanguage, String sPrefixCompare)
            throws Exception {
        Connection conns = null;
        ResultSet rs = null;
        String sPrefix = "";
        try {
            if (null != dbConf.Choise_TypeDB.trim()) {
                switch (dbConf.Choise_TypeDB.trim()) {
                    case "1":
                        conns = dbConf.OpenDatabase();
                        proc_stmt = conns.prepareCall("{ call S_BO_PREFIX_UUID_COMBOBOX(?,?) }");
                        proc_stmt.setString(1, sType);
                        proc_stmt.setInt(2, Integer.parseInt(pLanguage));
                        rs = proc_stmt.executeQuery();
                        while (rs.next()) {
                            String sPrefixDB = EscapeUtils.CheckTextNull(rs.getString("PREFIX_DB"));
                            if(sPrefixDB.equals(sPrefixCompare)) {
                                sPrefix = EscapeUtils.CheckTextNull(rs.getString("PREFIX_DESC"));
                            }
                        }
                        break;
                    case "2":
                        break;
                    default:
                        break;
                }
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (proc_stmt != null) {
                proc_stmt.close();
            }
            Connection[] temp_connection = new Connection[]{conns};
            dbConf.CloseDatabase(temp_connection);
        }
        return sPrefix;
    }
    //</editor-fold>
    
    public String S_BO_CERTIFICATION_UPDATE_CONTACT_REAL(int pCERTIFICATION_ID, String pPHONE_CONTRACT_REAL, String pEMAIL_CONTRACT_REAL, 
        String pUSER_BY) throws Exception {
        String convrtr = "0";
        Connection conns = null;
        try {
            if (null != dbConf.Choise_TypeDB.trim()) {
                switch (dbConf.Choise_TypeDB.trim()) {
                    case "1":
                        conns = dbConf.OpenDatabase();
                        proc_stmt = conns.prepareCall(CommonFunction.getNumericalOrderStore("S_BO_CERTIFICATION_UPDATE_CONTACT_REAL", 4));
                        proc_stmt.setInt("pCERTIFICATION_ID", pCERTIFICATION_ID);
                        proc_stmt.setString("pEMAIL_CONTRACT_REAL", pEMAIL_CONTRACT_REAL);
                        proc_stmt.setString("pPHONE_CONTRACT_REAL", pPHONE_CONTRACT_REAL);
                        proc_stmt.setInt("pUSER_BY", Integer.parseInt(pUSER_BY));
                        CommonFunction.LogDebugString(log, "S_BO_CERTIFICATION_UPDATE_CONTACT_REAL", proc_stmt.toString());
                        proc_stmt.execute();
                        break;
                    case "2":
                        break;
                    default:
                        break;
                }
            }
        } finally {
            if (proc_stmt != null) {
                proc_stmt.close();
            }
            Connection[] temp_connection = new Connection[]{conns};
            dbConf.CloseDatabase(temp_connection);
        }
        return convrtr;
    }
    
    //<editor-fold defaultstate="collapsed" desc="S_BO_CERTIFICATION_UPDATE_INFO_BRIEF">
    public int S_BO_CERTIFICATION_UPDATE_INFO_BRIEF(int pCERTIFICATION_ID, String pINFO_BRIEF, String pUSER_BY)
        throws Exception {
        int convrtr = 0;
        Connection conns = null;
        try {
            if (null != dbConf.Choise_TypeDB.trim()) {
                switch (dbConf.Choise_TypeDB.trim()) {
                    case "1":
                        conns = dbConf.OpenDatabase();
                        proc_stmt = conns.prepareCall("{ call S_BO_CERTIFICATION_UPDATE_INFO_BRIEF(?,?,?) }");
                        proc_stmt.setInt("pCERTIFICATION_ID", pCERTIFICATION_ID);
                        proc_stmt.setString("pINFO_BRIEF", pINFO_BRIEF);
                        proc_stmt.setInt("pUSER_BY", Integer.parseInt(pUSER_BY));
                        CommonFunction.LogDebugString(log, "S_BO_CERTIFICATION_UPDATE_INFO_BRIEF", proc_stmt.toString());
                        proc_stmt.execute();
                        break;
                    case "2":
                        break;
                    default:
                        break;
                }
            }
        } finally {
            if (proc_stmt != null) {
                proc_stmt.close();
            }
            Connection[] temp_connection = new Connection[]{conns};
            dbConf.CloseDatabase(temp_connection);
        }
        return convrtr;
    }
    //</editor-fold>
    
    public int S_BO_BRANCH_UPDATE_API_ISSUE_P12_ENABLED(int pBRANCH_ID, String pVALUE, String pUSERNAME_BY)
        throws Exception {
        int convrtr = 0;
        Connection conns = null;
        try {
            if (null != dbConf.Choise_TypeDB.trim()) {
                switch (dbConf.Choise_TypeDB.trim()) {
                    case "1":
                        conns = dbConf.OpenDatabase();
                        proc_stmt = conns.prepareCall(CommonFunction.getNumericalOrderStore("S_BO_BRANCH_UPDATE_API_ISSUE_P12_ENABLED", 4));
                        proc_stmt.setInt("pBRANCH_ID", pBRANCH_ID);
                        proc_stmt.setString("pVALUE", pVALUE);
                        proc_stmt.setString("pUSERNAME_BY", pUSERNAME_BY);
                        proc_stmt.registerOutParameter("pRESULT", java.sql.Types.INTEGER);
                        CommonFunction.LogDebugString(log, "S_BO_BRANCH_UPDATE_API_ISSUE_P12_ENABLED", proc_stmt.toString());
                        proc_stmt.execute();
                        convrtr = proc_stmt.getInt("pRESULT");
                        break;
                    case "2":
                        break;
                    default:
                        break;
                }
            }
        } finally {
            if (proc_stmt != null) {
                proc_stmt.close();
            }
            Connection[] temp_connection = new Connection[]{conns};
            dbConf.CloseDatabase(temp_connection);
        }
        return convrtr;
    }
    
    public int S_BO_CERTIFICATION_ATTR_UPDATE_APPROVED_CA_BY(int pCERTIFICATION_ATTR_ID, String pAPPROVED_CA_BY, String pUSER_BY)
        throws Exception {
        int convrtr = 0;
        Connection conns = null;
        try {
            if (null != dbConf.Choise_TypeDB.trim()) {
                switch (dbConf.Choise_TypeDB.trim()) {
                    case "1":
                        conns = dbConf.OpenDatabase();
                        proc_stmt = conns.prepareCall(CommonFunction.getNumericalOrderStore("S_BO_CERTIFICATION_ATTR_UPDATE_APPROVED_CA_BY", 3));
                        proc_stmt.setInt("pCERTIFICATION_ATTR_ID", pCERTIFICATION_ATTR_ID);
                        proc_stmt.setString("pAPPROVED_CA_BY", pAPPROVED_CA_BY);
                        proc_stmt.setString("pUSER_BY", pUSER_BY);
                        CommonFunction.LogDebugString(log, "S_BO_CERTIFICATION_ATTR_UPDATE_APPROVED_CA_BY", proc_stmt.toString());
                        proc_stmt.execute();
                        break;
                    case "2":
                        break;
                    default:
                        break;
                }
            }
        } finally {
            if (proc_stmt != null) {
                proc_stmt.close();
            }
            Connection[] temp_connection = new Connection[]{conns};
            dbConf.CloseDatabase(temp_connection);
        }
        return convrtr;
    }
    
    public void S_BO_BRANCH_GET_TREE_BRANCH_NOT_LOCKED(int pBRANCH_ID, int pLANGUAGE, BRANCH[][] response)
        throws Exception {
        ArrayList<BRANCH> tempList = new ArrayList<>();
        Connection conns = null;
        ResultSet rs = null;
        try {
            if (null != dbConf.Choise_TypeDB.trim()) {
                switch (dbConf.Choise_TypeDB.trim()) {
                    case "1":
                        conns = dbConf.OpenDatabase();
                        proc_stmt = conns.prepareCall("{ call S_BO_BRANCH_GET_TREE_BRANCH_NOT_LOCKED(?,?) }");
                        proc_stmt.setInt(1, pBRANCH_ID);
                        proc_stmt.setInt(2, pLANGUAGE);
                        CommonFunction.LogDebugString(log, "S_BO_BRANCH_GET_TREE_BRANCH_NOT_LOCKED", proc_stmt.toString());
                        rs = proc_stmt.executeQuery();
                        while (rs.next()) {
                            if(rs.getInt("ID") != 1) {
                                BRANCH tempItem = new BRANCH();
                                tempItem.ID = rs.getInt("ID");
                                tempItem.PARENT_ID = rs.getInt("PARENT_ID");
                                tempItem.LEVEL_ID = rs.getInt("LV");
                                tempItem.NAME = EscapeUtils.CheckTextNull(rs.getString("NAME"));
                                tempItem.REMARK = EscapeUtils.CheckTextNull(rs.getString("DESC"));
                                tempList.add(tempItem);
                            }
                        }
                        List<Integer> list = new ArrayList<>();
                        for(int i=0; i<tempList.size(); i++) {
                            list.add(tempList.get(i).LEVEL_ID);
                        }
                        int iCountParent = CommonFunction.findMaxList(list);
                        String sPrefix = "";
                        if(iCountParent > 1) {
                            int n=2;
                            if(pBRANCH_ID == 1) {
                                n=3;
                            }
                            for(int i = n; i<=iCountParent; i++)
                            {
                                sPrefix = sPrefix + "&nbsp;&nbsp;&nbsp;";
                                for(int j=0; j<tempList.size(); j++)
                                {
                                    if(tempList.get(j).LEVEL_ID == i)
                                    {
                                        tempList.get(j).NAME = sPrefix +"+ "+tempList.get(j).NAME;
                                    }
                                }
                            }
                        }
                        response[0] = new BRANCH[tempList.size()];
                        response[0] = tempList.toArray(response[0]);
                    case "2":
                        break;
                    default:
                        break;
                }
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (proc_stmt != null) {
                proc_stmt.close();
            }
            Connection[] temp_connection = new Connection[]{conns};
            dbConf.CloseDatabase(temp_connection);
        }
    }
    
    public int S_BO_API_PARTNER_CERTIFICATION_GENERATED(int pCERTFICATION_ATTR_ID, String pCSR, String pVALUE, String pUSERNAME_BY)
        throws Exception {
        int convrtr = 0;
        Connection conns = null;
        try {
            if (null != dbConf.Choise_TypeDB.trim()) {
                switch (dbConf.Choise_TypeDB.trim()) {
                    case "1":
                        conns = dbConf.OpenDatabase();
                        proc_stmt = conns.prepareCall(CommonFunction.getNumericalOrderStore("S_BO_API_PARTNER_CERTIFICATION_GENERATED", 5));
                        proc_stmt.setInt("pCERTFICATION_ATTR_ID", pCERTFICATION_ATTR_ID);
                        proc_stmt.setString("pCSR", pCSR);
                        proc_stmt.setString("pVALUE", pVALUE);
                        proc_stmt.setString("pUSERNAME_BY", pUSERNAME_BY);
                        proc_stmt.registerOutParameter("pRESULT", java.sql.Types.INTEGER);
                        CommonFunction.LogDebugString(log, "S_BO_API_PARTNER_CERTIFICATION_GENERATED", proc_stmt.toString());
                        proc_stmt.execute();
                        convrtr = proc_stmt.getInt("pRESULT");
                        break;
                    case "2":
                        break;
                    default:
                        break;
                }
            }
        } finally {
            if (proc_stmt != null) {
                proc_stmt.close();
            }
            Connection[] temp_connection = new Connection[]{conns};
            dbConf.CloseDatabase(temp_connection);
        }
        return convrtr;
    }
    
    public int S_BO_BRANCH_UPDATE_CALLBACK_URL_APPROVED(int pBRANCH_ID, String pVALUE, String pUSERNAME_BY)
        throws Exception {
        int convrtr = 0;
        Connection conns = null;
        try {
            if (null != dbConf.Choise_TypeDB.trim()) {
                switch (dbConf.Choise_TypeDB.trim()) {
                    case "1":
                        conns = dbConf.OpenDatabase();
                        proc_stmt = conns.prepareCall(CommonFunction.getNumericalOrderStore("S_BO_BRANCH_UPDATE_CALLBACK_URL_APPROVED", 4));
                        proc_stmt.setInt("pBRANCH_ID", pBRANCH_ID);
                        proc_stmt.setString("pVALUE", pVALUE);
                        proc_stmt.setString("pUSERNAME_BY", pUSERNAME_BY);
                        proc_stmt.registerOutParameter("pRESULT", java.sql.Types.INTEGER);
                        CommonFunction.LogDebugString(log, "S_BO_BRANCH_UPDATE_CALLBACK_URL_APPROVED", proc_stmt.toString());
                        proc_stmt.execute();
                        convrtr = proc_stmt.getInt("pRESULT");
                        break;
                    case "2":
                        break;
                    default:
                        break;
                }
            }
        } finally {
            if (proc_stmt != null) {
                proc_stmt.close();
            }
            Connection[] temp_connection = new Connection[]{conns};
            dbConf.CloseDatabase(temp_connection);
        }
        return convrtr;
    }
    
    public void S_BO_TOKEN_GET_LOCK_REQUEST_RECENTLY(String pTOKEN_ID, TOKEN[][] response)
            throws Exception {
        ArrayList<TOKEN> tempList = new ArrayList<>();
        Connection conns = null;
        ResultSet rs = null;
        try {
            if (null != dbConf.Choise_TypeDB.trim()) {
                switch (dbConf.Choise_TypeDB.trim()) {
                    case "1":
                        conns = dbConf.OpenDatabase();
                        proc_stmt = conns.prepareCall("{ call S_BO_TOKEN_GET_LOCK_REQUEST_RECENTLY(?) }");
                        proc_stmt.setInt(1, Integer.parseInt(pTOKEN_ID));
                        CommonFunction.LogDebugString(log, "S_BO_TOKEN_GET_LOCK_REQUEST_RECENTLY", proc_stmt.toString());
                        rs = proc_stmt.executeQuery();
                        while (rs.next()) {
                            TOKEN tempItem = new TOKEN();
                            tempItem.VALUE = EscapeUtils.CheckTextNull(rs.getString("VALUE"));
                            tempList.add(tempItem);
                        }
                        response[0] = new TOKEN[tempList.size()];
                        response[0] = tempList.toArray(response[0]);
                    case "2":
                        break;
                    default:
                        break;
                }
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (proc_stmt != null) {
                proc_stmt.close();
            }
            Connection[] temp_connection = new Connection[]{conns};
            dbConf.CloseDatabase(temp_connection);
        }
    }
    
    public int S_BO_CERTIFICATION_RSSP_TOTAL(String pCERTIFICATION_SN, String pCERTIFICATION_HASH,
            String pREMAINING_SIGNING_COUNTER, String pCOMPANY_NAME, String pPERSONAL_NAME,
            String pENTERPRISE_ID, String pPERSONAL_ID, String pEFFECTIVE_FROM_DT,
            String pEFFECTIVE_TO_DT, String pEXPIRATION_FROM_DT, String pEXPIRATION_TO_DT,
            String pBRANCH_ID, String pBRANCH_LIST_ID)
        throws Exception {
        int convrtr = 0;
        Connection conns = null;
        try {
            if (null != dbConf.Choise_TypeDB.trim()) {
                switch (dbConf.Choise_TypeDB.trim()) {
                    case "1":
                        conns = dbConf.OpenDatabase();
                        proc_stmt = conns.prepareCall(CommonFunction.getNumericalOrderStore("S_BO_CERTIFICATION_RSSP_TOTAL", 14));
                        if(!"".equals(pCERTIFICATION_SN)) {
                            proc_stmt.setString("pCERTIFICATION_SN", pCERTIFICATION_SN);
                        } else {
                            proc_stmt.setString("pCERTIFICATION_SN", null);
                        }
                        if(!"".equals(pCERTIFICATION_HASH)) {
                            proc_stmt.setString("pCERTIFICATION_HASH", pCERTIFICATION_HASH);
                        } else {
                            proc_stmt.setString("pCERTIFICATION_HASH", null);
                        }
                        if(!"".equals(pREMAINING_SIGNING_COUNTER)) {
                            proc_stmt.setString("pREMAINING_SIGNING_COUNTER", pREMAINING_SIGNING_COUNTER);
                        } else {
                            proc_stmt.setString("pREMAINING_SIGNING_COUNTER", null);
                        }
                        if(!"".equals(pCOMPANY_NAME)) {
                            proc_stmt.setString("pCOMPANY_NAME", pCOMPANY_NAME);
                        } else {
                            proc_stmt.setString("pCOMPANY_NAME", null);
                        }
                        if(!"".equals(pPERSONAL_NAME)) {
                            proc_stmt.setString("pPERSONAL_NAME", pPERSONAL_NAME);
                        } else {
                            proc_stmt.setString("pPERSONAL_NAME", null);
                        }
                        if(!"".equals(pENTERPRISE_ID)) {
                            proc_stmt.setString("pENTERPRISE_ID", pENTERPRISE_ID);
                        } else {
                            proc_stmt.setString("pENTERPRISE_ID", null);
                        }
                        if(!"".equals(pPERSONAL_ID)) {
                            proc_stmt.setString("pPERSONAL_ID", pPERSONAL_ID);
                        } else {
                            proc_stmt.setString("pPERSONAL_ID", null);
                        }
                        if(!"".equals(pEFFECTIVE_FROM_DT)) {
                            proc_stmt.setString("pEFFECTIVE_FROM_DT", pEFFECTIVE_FROM_DT);
                        } else {
                            proc_stmt.setString("pEFFECTIVE_FROM_DT", null);
                        }
                        if(!"".equals(pEFFECTIVE_TO_DT)) {
                            proc_stmt.setString("pEFFECTIVE_TO_DT", pEFFECTIVE_TO_DT);
                        } else {
                            proc_stmt.setString("pEFFECTIVE_TO_DT", null);
                        }
                        if(!"".equals(pEXPIRATION_FROM_DT)) {
                            proc_stmt.setString("pEXPIRATION_FROM_DT", pEXPIRATION_FROM_DT);
                        } else {
                            proc_stmt.setString("pEXPIRATION_FROM_DT", null);
                        }
                        if(!"".equals(pEXPIRATION_TO_DT)) {
                            proc_stmt.setString("pEXPIRATION_TO_DT", pEXPIRATION_TO_DT);
                        } else {
                            proc_stmt.setString("pEXPIRATION_TO_DT", null);
                        }
                        if(!"".equals(pBRANCH_ID)) {
                            proc_stmt.setInt("pBRANCH_ID", Integer.parseInt(pBRANCH_ID));
                        } else {
                            proc_stmt.setString("pBRANCH_ID", null);
                        }
                        if(!"".equals(pBRANCH_LIST_ID)) {
                            proc_stmt.setString("pBRANCH_LIST_ID", pBRANCH_LIST_ID);
                        } else {
                            proc_stmt.setString("pBRANCH_LIST_ID", null);
                        }
                        proc_stmt.registerOutParameter("pCOUNT", java.sql.Types.INTEGER);
                        CommonFunction.LogDebugString(log, "S_BO_CERTIFICATION_RSSP_TOTAL", proc_stmt.toString());
                        proc_stmt.execute();
                        convrtr = proc_stmt.getInt("pCOUNT");
                        break;
                    case "2":
                        break;
                    default:
                        break;
                }
            }
        } finally {
            if (proc_stmt != null) {
                proc_stmt.close();
            }
            Connection[] temp_connection = new Connection[]{conns};
            dbConf.CloseDatabase(temp_connection);
        }
        return convrtr;
    }
    
    public void S_BO_CERTIFICATION_RSSP_LIST(String pCERTIFICATION_SN, String pCERTIFICATION_HASH,
            String pREMAINING_SIGNING_COUNTER, String pCOMPANY_NAME, String pPERSONAL_NAME,
            String pENTERPRISE_ID, String pPERSONAL_ID, String pEFFECTIVE_FROM_DT,
            String pEFFECTIVE_TO_DT, String pEXPIRATION_FROM_DT, String pEXPIRATION_TO_DT,
            String pBRANCH_ID, String pBRANCH_LIST_ID, int pLANGUAGE, int pPAGE_NO, int pROW_NO, CERTIFICATION[][] response)
        throws Exception {
        ArrayList<CERTIFICATION> tempList = new ArrayList<>();
        Connection conns = null;
        ResultSet rs = null;
        try {
            if (null != dbConf.Choise_TypeDB.trim()) {
                switch (dbConf.Choise_TypeDB.trim()) {
                    case "1":
                        conns = dbConf.OpenDatabase();
                        proc_stmt = conns.prepareCall(CommonFunction.getNumericalOrderStore("S_BO_CERTIFICATION_RSSP_LIST", 16));
                        if(!"".equals(pCERTIFICATION_SN)) {
                            proc_stmt.setString("pCERTIFICATION_SN", pCERTIFICATION_SN);
                        } else {
                            proc_stmt.setString("pCERTIFICATION_SN", null);
                        }
                        if(!"".equals(pCERTIFICATION_HASH)) {
                            proc_stmt.setString("pCERTIFICATION_HASH", pCERTIFICATION_HASH);
                        } else {
                            proc_stmt.setString("pCERTIFICATION_HASH", null);
                        }
                        if(!"".equals(pREMAINING_SIGNING_COUNTER)) {
                            proc_stmt.setString("pREMAINING_SIGNING_COUNTER", pREMAINING_SIGNING_COUNTER);
                        } else {
                            proc_stmt.setString("pREMAINING_SIGNING_COUNTER", null);
                        }
                        if(!"".equals(pCOMPANY_NAME)) {
                            proc_stmt.setString("pCOMPANY_NAME", pCOMPANY_NAME);
                        } else {
                            proc_stmt.setString("pCOMPANY_NAME", null);
                        }
                        if(!"".equals(pPERSONAL_NAME)) {
                            proc_stmt.setString("pPERSONAL_NAME", pPERSONAL_NAME);
                        } else {
                            proc_stmt.setString("pPERSONAL_NAME", null);
                        }
                        if(!"".equals(pENTERPRISE_ID)) {
                            proc_stmt.setString("pENTERPRISE_ID", pENTERPRISE_ID);
                        } else {
                            proc_stmt.setString("pENTERPRISE_ID", null);
                        }
                        if(!"".equals(pPERSONAL_ID)) {
                            proc_stmt.setString("pPERSONAL_ID", pPERSONAL_ID);
                        } else {
                            proc_stmt.setString("pPERSONAL_ID", null);
                        }
                        if(!"".equals(pEFFECTIVE_FROM_DT)) {
                            proc_stmt.setString("pEFFECTIVE_FROM_DT", pEFFECTIVE_FROM_DT);
                        } else {
                            proc_stmt.setString("pEFFECTIVE_FROM_DT", null);
                        }
                        if(!"".equals(pEFFECTIVE_TO_DT)) {
                            proc_stmt.setString("pEFFECTIVE_TO_DT", pEFFECTIVE_TO_DT);
                        } else {
                            proc_stmt.setString("pEFFECTIVE_TO_DT", null);
                        }
                        if(!"".equals(pEXPIRATION_FROM_DT)) {
                            proc_stmt.setString("pEXPIRATION_FROM_DT", pEXPIRATION_FROM_DT);
                        } else {
                            proc_stmt.setString("pEXPIRATION_FROM_DT", null);
                        }
                        if(!"".equals(pEXPIRATION_TO_DT)) {
                            proc_stmt.setString("pEXPIRATION_TO_DT", pEXPIRATION_TO_DT);
                        } else {
                            proc_stmt.setString("pEXPIRATION_TO_DT", null);
                        }
                        if(!"".equals(pBRANCH_ID)) {
                            proc_stmt.setInt("pBRANCH_ID", Integer.parseInt(pBRANCH_ID));
                        } else {
                            proc_stmt.setString("pBRANCH_ID", null);
                        }
                        if(!"".equals(pBRANCH_LIST_ID)) {
                            proc_stmt.setString("pBRANCH_LIST_ID", pBRANCH_LIST_ID);
                        } else {
                            proc_stmt.setString("pBRANCH_LIST_ID", null);
                        }
                        proc_stmt.setInt("pLANGUAGE", pLANGUAGE);
                        proc_stmt.setInt("pPAGE_NO", pPAGE_NO);
                        proc_stmt.setInt("pROW_NO", pROW_NO);
                        CommonFunction.LogDebugString(log, "S_BO_CERTIFICATION_RSSP_LIST", proc_stmt.toString());
                        rs = proc_stmt.executeQuery();
                        while (rs.next()) {
                            CERTIFICATION tempItem = new CERTIFICATION();
                            tempItem.ID = rs.getInt("ID");
                            tempItem.CERTIFICATION_SN = EscapeUtils.CheckTextNull(rs.getString("CERTIFICATION_SN"));
                            tempItem.CERTIFICATION_HASH = EscapeUtils.CheckTextNull(rs.getString("CERTIFICATION_HASH"));
                            tempItem.COMPANY_NAME = EscapeUtils.CheckTextNull(rs.getString("COMPANY_NAME"));
                            tempItem.PERSONAL_NAME = EscapeUtils.CheckTextNull(rs.getString("PERSONAL_NAME"));
                            String[] prefixRemark = new String[2];
                            String sENTERPRISE_ID = EscapeUtils.CheckTextNull(rs.getString("ENTERPRISE_ID"));
                            String sPERSONAL_ID = EscapeUtils.CheckTextNull(rs.getString("PERSONAL_ID"));
                            tempItem.PERSONAL_ID = CommonReferServlet.swapPrefixPersonalLanguage(sPERSONAL_ID, String.valueOf(pLANGUAGE), prefixRemark);
                            tempItem.ENTERPRISE_ID = CommonReferServlet.swapPrefixEnterpriseLanguage(sENTERPRISE_ID, String.valueOf(pLANGUAGE), prefixRemark);
                            tempItem.EFFECTIVE_DT = EscapeUtils.CheckTextNull(rs.getString("EFFECTIVE_DT"));
                            tempItem.EXPIRATION_DT = EscapeUtils.CheckTextNull(rs.getString("EXPIRATION_DT"));
                            tempItem.CREATED_DT = EscapeUtils.CheckTextNull(rs.getString("CREATED_DT"));
                            tempItem.BRANCH_ID = rs.getInt("BRANCH_ID");
                            tempItem.BRANCH_NAME = EscapeUtils.CheckTextNull(rs.getString("BRANCH_NAME"));
                            tempItem.BRANCH_DESC = EscapeUtils.CheckTextNull(rs.getString("BRANCH_DESC"));
                            if(rs.getObject("REMAINING_SIGNING_COUNTER") == null){
                                tempItem.REMAINING_SIGNING_COUNTER = "";
                            } else {
                                int sRemaining = rs.getInt("REMAINING_SIGNING_COUNTER");
                                if(sRemaining == -1){
                                    tempItem.REMAINING_SIGNING_COUNTER = "UNLIMITED";
                                } else {
                                    tempItem.REMAINING_SIGNING_COUNTER = String.valueOf(sRemaining);
                                }
                            }
                            tempList.add(tempItem);
                        }
                        response[0] = new CERTIFICATION[tempList.size()];
                        response[0] = tempList.toArray(response[0]);
                    case "2":
                        break;
                    default:
                        break;
                }
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (proc_stmt != null) {
                proc_stmt.close();
            }
            Connection[] temp_connection = new Connection[]{conns};
            dbConf.CloseDatabase(temp_connection);
        }
    }
    
    public int S_BO_CERTIFICATION_BRIEF_UPDATE_BUSINESS_LICENSE_TYPE_ID(int pCERTIFICATION_ID, int pBUSINESS_LICESCE_TYPE_ID, int pUSER_BY)
        throws Exception {
        int convrtr = 0;
        Connection conns = null;
        try {
            if (null != dbConf.Choise_TypeDB.trim()) {
                switch (dbConf.Choise_TypeDB.trim()) {
                    case "1":
                        conns = dbConf.OpenDatabase();
                        proc_stmt = conns.prepareCall(CommonFunction.getNumericalOrderStore("S_BO_CERTIFICATION_BRIEF_UPDATE_BUSINESS_LICENSE_TYPE_ID", 3));
                        proc_stmt.setInt("pCERTIFICATION_ID", pCERTIFICATION_ID);
                        proc_stmt.setInt("pBUSINESS_LICENSE_TYPE_ID", pBUSINESS_LICESCE_TYPE_ID);
                        proc_stmt.setInt("pUSER_BY", pUSER_BY);
                        CommonFunction.LogDebugString(log, "S_BO_CERTIFICATION_BRIEF_UPDATE_BUSINESS_LICENSE_TYPE_ID", proc_stmt.toString());
                        proc_stmt.execute();
                        convrtr = 0;
                        break;
                    case "2":
                        break;
                    default:
                        break;
                }
            }
        } finally {
            if (proc_stmt != null) {
                proc_stmt.close();
            }
            Connection[] temp_connection = new Connection[]{conns};
            dbConf.CloseDatabase(temp_connection);
        }
        return convrtr;
    }
    
    //<editor-fold defaultstate="collapsed" desc="S_BO_CERTIFICATION_BRIEF_REPORT_BUSINESS_LICENSE">
    public void S_BO_CERTIFICATION_BRIEF_REPORT_BUSINESS_LICENSE(String pCOMPANY_NAME, String pPERSONAL_NAME, String pCOLLECT_ENABLED,
        String pMONTH, String pYEAR, String pBRANCH_ID, String pCOMPENSATION_BRIEF, String pBRIEF_TYPE, String pUserUID,
        String pLANGUAGE, CERTIFICATION[][] response, int sPage, int sSum, String pCOMMIT_ENABLED, String stateProfile, String strFromReceiveDate,
        String strToReceiveDate, String pConfirmResign, String pERROR_SIGNING, String pENTERPRISE_ID,
        String pPERSONAL_ID, String pBRANCH_BENEFICIARY_ID, String pFROM, String pTO)
        throws Exception {
        ResultSet rs = null;
        ArrayList<CERTIFICATION> tempList = new ArrayList<>();
        Connection conns = null;
        try {
            if (null != dbConf.Choise_TypeDB.trim()) {
                switch (dbConf.Choise_TypeDB.trim()) {
                    case "1":
                        String isCALoad = LoadParamSystem.getParamStart(Definitions.CONFIG_IS_WHICH_ABOUT_CA);
                        conns = dbConf.OpenDatabase();
                        proc_stmt = conns.prepareCall(CommonFunction.getNumericalOrderStore("S_BO_CERTIFICATION_BRIEF_REPORT_BUSINESS_LICENSE", 21));
                        if (!"".equals(pCOMPANY_NAME)) {
                            proc_stmt.setString("pCOMPANY_NAME", pCOMPANY_NAME);
                        } else {
                            proc_stmt.setString("pCOMPANY_NAME", null);
                        }
                        if (!"".equals(pPERSONAL_NAME)) {
                            proc_stmt.setString("pPERSONAL_NAME", pPERSONAL_NAME);
                        } else {
                            proc_stmt.setString("pPERSONAL_NAME", null);
                        }
                        if (!"".equals(pENTERPRISE_ID)) {
                            proc_stmt.setString("pENTERPRISE_ID", pENTERPRISE_ID);
                        } else {
                            proc_stmt.setString("pENTERPRISE_ID", null);
                        }
                        if (!"".equals(pPERSONAL_ID)) {
                            proc_stmt.setString("pPERSONAL_ID", pPERSONAL_ID);
                        } else {
                            proc_stmt.setString("pPERSONAL_ID", null);
                        }
                        if (!"".equals(pCOLLECT_ENABLED)) {
                            proc_stmt.setInt("pCOLLECT_ENABLED", Integer.parseInt(pCOLLECT_ENABLED));
                        } else {
                            proc_stmt.setString("pCOLLECT_ENABLED", null);
                        }
                        if (!"".equals(pMONTH)) {
                            proc_stmt.setInt("pMONTH", Integer.parseInt(pMONTH));
                        } else {
                            proc_stmt.setString("pMONTH", null);
                        }
                        if (!"".equals(pYEAR)) {
                            proc_stmt.setInt("pYEAR", Integer.parseInt(pYEAR));
                        } else {
                            proc_stmt.setString("pYEAR", null);
                        }
                        proc_stmt.setInt("pLANGUAGE", Integer.parseInt(pLANGUAGE));
                        if (!"".equals(pBRANCH_ID)) {
                            proc_stmt.setInt("pBRANCH_ID", Integer.parseInt(pBRANCH_ID));
                        } else {
                            proc_stmt.setString("pBRANCH_ID", null);
                        }
                        if (!"".equals(pCOMPENSATION_BRIEF)) {
                            proc_stmt.setInt("pCOMPENSATION_BRIEF", Integer.parseInt(pCOMPENSATION_BRIEF));
                        } else {
                            proc_stmt.setString("pCOMPENSATION_BRIEF", null);
                        }
                        if (!"".equals(pBRIEF_TYPE)) {
                            proc_stmt.setInt("pBRIEF_TYPE", Integer.parseInt(pBRIEF_TYPE));
                        } else {
                            proc_stmt.setString("pBRIEF_TYPE", null);
                        }
                        if (!"".equals(pUserUID)) {
                            proc_stmt.setInt("pUSER_BY", Integer.parseInt(pUserUID));
                        } else {
                            proc_stmt.setString("pUSER_BY", null);
                        }
                        if ("1".equals(pCOMMIT_ENABLED)) {
                            proc_stmt.setInt("pCOMMIT_ENABLED", Integer.parseInt(pCOMMIT_ENABLED));
                        } else {
                            proc_stmt.setString("pCOMMIT_ENABLED", null);
                        }
                        if (!"".equals(stateProfile)) {
                            proc_stmt.setInt("pFILE_MANAGER_STATE_ID", Integer.parseInt(stateProfile));
                        } else {
                            proc_stmt.setString("pFILE_MANAGER_STATE_ID", null);
                        }
                        if (!"".equals(strFromReceiveDate)) {
                            proc_stmt.setString("pDATE_RECEIVED_BRIEF_FROM", strFromReceiveDate);
                        } else {
                            proc_stmt.setString("pDATE_RECEIVED_BRIEF_FROM", null);
                        }
                        if (!"".equals(strToReceiveDate)) {
                            proc_stmt.setString("pDATE_RECEIVED_BRIEF_TO", strToReceiveDate);
                        } else {
                            proc_stmt.setString("pDATE_RECEIVED_BRIEF_TO", null);
                        }
                        if (!"".equals(pConfirmResign)) {
                            proc_stmt.setInt("pRESIGNING_CONFIRMATION_PAPER_ENABLED", Integer.parseInt(pConfirmResign));
                        } else {
                            proc_stmt.setString("pRESIGNING_CONFIRMATION_PAPER_ENABLED", null);
                        }
                        if (!"".equals(pERROR_SIGNING)) {
                            proc_stmt.setInt("pERROR_SIGNING", Integer.parseInt(pERROR_SIGNING));
                        } else {
                            proc_stmt.setString("pERROR_SIGNING", null);
                        }
                        if (!"".equals(pFROM)) {
                            proc_stmt.setObject("pFROM", CommonFunction.ConvertStringToTimeStampFormat(pFROM, Definitions.CONFIG_DATE_PATTERN_DATE_DDMMYYYY));
                        } else {
                            proc_stmt.setString("pFROM", null);
                        }
                        if (!"".equals(pTO)) {
                            proc_stmt.setObject("pTO", CommonFunction.ConvertStringToTimeStampFormat(pTO, Definitions.CONFIG_DATE_PATTERN_DATE_DDMMYYYY));
                        } else {
                            proc_stmt.setString("pTO", null);
                        }
                        if (!"".equals(pBRANCH_BENEFICIARY_ID)) {
                            proc_stmt.setInt("pBRANCH_BENEFICIARY_ID", Integer.parseInt(pBRANCH_BENEFICIARY_ID));
                        } else {
                            proc_stmt.setString("pBRANCH_BENEFICIARY_ID", null);
                        }
                        CommonFunction.LogDebugString(log, "S_BO_CERTIFICATION_BRIEF_LIST", proc_stmt.toString());
                        rs = proc_stmt.executeQuery();
                        while (rs.next()) {
                            CERTIFICATION tempItem = new CERTIFICATION();
                            tempItem.ID = rs.getInt("ID");
                            tempItem.CERTIFICATION_OWNER_ID = rs.getInt("CERTIFICATION_OWNER_ID");
                            tempItem.ISSUED_DT = rs.getString("ISSUED_DT");
                            tempItem.CREATED_BY = rs.getString("SALE_STAFF");
                            tempItem.COMPANY_NAME = rs.getString("COMPANY_NAME");
                            String sENTERPRISE_ID = EscapeUtils.CheckTextNull(rs.getString("ENTERPRISE_ID"));
                            String sPERSONAL_ID = EscapeUtils.CheckTextNull(rs.getString("PERSONAL_ID"));
//                            CommonReferServlet.separateUIDToField(sENTERPRISE_ID, sPERSONAL_ID, tempItem);
                            String[] prefixRemark = new String[2];
                            if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA)) {
                                tempItem.PERSONAL_ID = CommonReferServlet.filterPrefixCNUIDLanguage(sPERSONAL_ID, "0");
                                CommonReferServlet.swapPrefixPersonalLanguage(sPERSONAL_ID, pLANGUAGE, prefixRemark);
                                tempItem.PERSONAL_ID_REMARK = prefixRemark[0];
                                tempItem.ENTERPRISE_ID = CommonReferServlet.filterPrefixUIDLanguage(sENTERPRISE_ID, "0");
                                CommonReferServlet.swapPrefixEnterpriseLanguage(sENTERPRISE_ID, pLANGUAGE, prefixRemark);
                                tempItem.ENTERPRISE_ID_REMARK = prefixRemark[1];
                            } else {
                                tempItem.PERSONAL_ID = CommonReferServlet.swapPrefixPersonalLanguage(sPERSONAL_ID, pLANGUAGE, prefixRemark);
                                tempItem.PERSONAL_ID_REMARK = prefixRemark[0];
                                tempItem.ENTERPRISE_ID = CommonReferServlet.swapPrefixEnterpriseLanguage(sENTERPRISE_ID, pLANGUAGE, prefixRemark);
                                tempItem.ENTERPRISE_ID_REMARK = prefixRemark[1];
                            }
                            tempItem.PERSONAL_NAME = rs.getString("PERSONAL_NAME");
                            tempItem.TOKEN_SN = rs.getString("TOKEN_SN");
                            tempItem.CERTIFICATION_AUTHORITY_DESC = rs.getString("CERTIFICATION_AUTHORITY_DESC");
                            tempItem.CERTIFICATION_PROFILE_NAME = rs.getString("CERTIFICATION_PROFILE_NAME");
                            tempItem.CROSS_CHECKED_MOUNTH = rs.getString("CROSS_CHECK_MONTH");
                            tempItem.BRIEF_PROPERTIES = EscapeUtils.CheckTextNull(rs.getString("BRIEF_PROPERTIES"));
                            tempItem.CERTIFICATION_ATTR_TYPE_DESC = EscapeUtils.CheckTextNull(rs.getString("SERVICE_TYPE_DESC"));
                            tempItem.FILE_MANAGER_STATE_DESC = EscapeUtils.CheckTextNull(rs.getString("FILE_MANAGER_STATE_DESC"));
                            tempItem.COLLECT_ENABLED = rs.getBoolean("COLLECT_ENABLED");
                            tempItem.BRIEF_TYPE = rs.getBoolean("BRIEF_TYPE");
                            tempItem.CERTIFICATION_SN = EscapeUtils.CheckTextNull(rs.getString("CERTIFICATION_SN"));
                            tempItem.PROFILE_NOTE = EscapeUtils.CheckTextNull(rs.getString("PROFILE_NOTE"));
                            tempItem.REVOCATION_REASON = EscapeUtils.CheckTextNull(rs.getString("REVOCATION_REASON"));
                            tempList.add(tempItem);
                        }
                        response[0] = new CERTIFICATION[tempList.size()];
                        response[0] = tempList.toArray(response[0]);
                        break;
                    case "2":
                        break;
                    default:
                        break;
                }
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (proc_stmt != null) {
                proc_stmt.close();
            }
            Connection[] temp_connection = new Connection[]{conns};
            dbConf.CloseDatabase(temp_connection);
        }
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="S_BO_API_CERTIFICATION_ATTR_STATE_LIST">
    public void S_BO_API_CERTIFICATION_ATTR_STATE_LIST(String pCERT_STATE_CODE, int pLANGUAGE, RequestStateInfo[][] response)
            throws Exception {
        ArrayList<RequestStateInfo> tempList = new ArrayList<>();
        Connection conns = null;
        ResultSet rs = null;
        try {
            if (null != dbConf.Choise_TypeDB.trim()) {
                switch (dbConf.Choise_TypeDB.trim()) {
                    case "1":
                        conns = dbConf.OpenDatabase();
                        proc_stmt = conns.prepareCall("{ call S_BO_API_CERTIFICATION_ATTR_STATE_LIST(?,?) }");
                        if(!"".equals(pCERT_STATE_CODE)) {
                            proc_stmt.setString(1, pCERT_STATE_CODE);
                        } else {
                            proc_stmt.setString(1, null);
                        }
                        proc_stmt.setInt(2, pLANGUAGE);
                        rs = proc_stmt.executeQuery();
                        while (rs.next()) {
                            RequestStateInfo tempItem = new RequestStateInfo();
                            tempItem.requestStateCode = rs.getString("CERTIFICATION_ATTR_STATE_NAME");
                            tempItem.requestStateName = rs.getString("CERTIFICATION_ATTR_STATE_DESC");
                            tempList.add(tempItem);
                        }
                        response[0] = new RequestStateInfo[tempList.size()];
                        response[0] = tempList.toArray(response[0]);
                         break;
                    case "2":
                        break;
                    default:
                        break;
                }
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (proc_stmt != null) {
                proc_stmt.close();
            }
            Connection[] temp_connection = new Connection[]{conns};
            dbConf.CloseDatabase(temp_connection);
        }
    }
    //</editor-fold>
    
    public int S_BO_CHECK_OLD_CERTIFICATION_INFO(int pCERTIFICATION_ID)
        throws Exception {
        int convrtr = 0;
        Connection conns = null;
        try {
            if (null != dbConf.Choise_TypeDB.trim()) {
                switch (dbConf.Choise_TypeDB.trim()) {
                    case "1":
                        conns = dbConf.OpenDatabase();
                        proc_stmt = conns.prepareCall(CommonFunction.getNumericalOrderStore("S_BO_CHECK_OLD_CERTIFICATION_INFO", 2));
                        proc_stmt.setInt("pCERTIFICATION_ID", pCERTIFICATION_ID);
                        proc_stmt.registerOutParameter("pRESULT", java.sql.Types.INTEGER);
                        CommonFunction.LogDebugString(log, "S_BO_CHECK_OLD_CERTIFICATION_INFO", proc_stmt.toString());
                        proc_stmt.execute();
                        convrtr = proc_stmt.getInt("pRESULT");
                        break;
                    case "2":
                        break;
                    default:
                        break;
                }
            }
        } finally {
            if (proc_stmt != null) {
                proc_stmt.close();
            }
            Connection[] temp_connection = new Connection[]{conns};
            dbConf.CloseDatabase(temp_connection);
        }
        return convrtr;
    }
    
    
}
