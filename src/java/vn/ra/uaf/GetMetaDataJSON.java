/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ra.uaf;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.log4j.Logger;
import vn.ra.uaf.DisplayPNGCharacteristicsDescriptor;
import vn.ra.uaf.MetadataStatement;
import vn.ra.uaf.VerificationMethodDescriptor;
import vn.ra.uaf.Version;
//import vn.smartid.utility.DatabaseUAFConnection;
import vn.ra.utility.DatabaseUAFConstants;

/**
 *
 * @author THANH-PC
 */
public class GetMetaDataJSON {

    final static Logger logger = org.apache.log4j.Logger.getLogger(GetMetaDataJSON.class);
    private static String aaid = "0057#0001";
    public static Path folderJSON = Paths.get("metadataStatements");

    public static void main(String[] args) throws SQLException, JsonGenerationException, JsonMappingException, IOException {
        MetadataStatement metadata = getMetaData(aaid);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(new File(folderJSON + File.separator + aaid + ".json"), metadata);
    }

    public static MetadataStatement getMetaData(String aaid) throws SQLException {
        // TODO Auto-generated method stub
        CallableStatement proc_stmt = null;
//        DatabaseUAFConnection db_connection = null;
//        ResultSet rs = null;
        MetadataStatement metadata = null;
//        try {
//            db_connection = new DatabaseUAFConnection();
//            proc_stmt = db_connection.getConn().prepareCall("{ call MetaDataGet(?,?) }");
//            proc_stmt.setString(DatabaseUAFConstants.pAaid, aaid);
//            proc_stmt.registerOutParameter(DatabaseUAFConstants.pCount, java.sql.Types.BIGINT);
//            rs = proc_stmt.executeQuery();
//            if (proc_stmt.getInt(DatabaseUAFConstants.pCount) == 1 && rs.first()) {
//                metadata = new MetadataStatement();
//                ObjectMapper objectMapper = new ObjectMapper();
//                metadata.setAaid(aaid);
//                //metadata.setAttestationRootCertificates(new String[]{rs.getString(DatabaseUAFConstants.ATTESTATIONROOTCERTIFICATELIST)});
//                metadata.setAttestationRootCertificates(objectMapper.readValue(rs.getString(DatabaseUAFConstants.ATTESTATIONROOTCERTIFICATELIST), String[].class));
//                metadata.setDescription(rs.getString(DatabaseUAFConstants.METADATA_DESC));
//                metadata.setAuthenticatorVersion(rs.getShort(DatabaseUAFConstants.AUTHENTICATORVERSION));
//                VerificationMethodDescriptor[][] userVerificationDetails = objectMapper.readValue(rs.getString(DatabaseUAFConstants.USERVERIFICATIONDETAILLIST), VerificationMethodDescriptor[][].class);
//                metadata.setUserVerificationDetails(userVerificationDetails);
//                metadata.setAttachmentHintTypes(rs.getInt(DatabaseUAFConstants.ATTACHMENTHINT));
//                metadata.setKeyProtection(rs.getShort(DatabaseUAFConstants.KEYPROTECTION));
//                metadata.setMatcherProtection(rs.getShort(DatabaseUAFConstants.MATCHERPROTECTION));
//                metadata.setTcDisplay(rs.getShort(DatabaseUAFConstants.TCDISPLAY));
//                metadata.setTcDisplayContentType(rs.getString(DatabaseUAFConstants.TCDISPLAYCONTENTTYPE));
//                metadata.setSecondFactorOnly(rs.getBoolean(DatabaseUAFConstants.ISSECONDFACTORONLY));
//                metadata.setIcon(rs.getString(DatabaseUAFConstants.ICON));
//                metadata.setAssertionScheme(rs.getString(DatabaseUAFConstants.ASSERTIONSCHEME));
//                metadata.setAuthenticationAlgorithm(rs.getShort(DatabaseUAFConstants.AUTHENTICATIONALGORITHM));
//                metadata.setPublicKeyAlgAndEncoding(rs.getShort(DatabaseUAFConstants.PUBLICKEYALGANDENCODING));
//                try {
//                    /*String[] attestationTypesString = rs.getString(DatabaseUAFConstants.ATTESTATIONTYPELIST).trim().split(",");
//                     short[] attestationTypes = new short[attestationTypesString.length];
//                     for(int idx = 0; idx < attestationTypes.length; idx++){
//                     attestationTypes[idx] = Short.valueOf(attestationTypesString[idx]).shortValue();
//                     }*/
//                    short[] attestationTypes = objectMapper.readValue(rs.getString(DatabaseUAFConstants.ATTESTATIONTYPELIST).trim(), short[].class);
//
//                    metadata.setAttestationType(attestationTypes);
//                } catch (Exception e) {
//                    // TODO: handle exception
//                    e.printStackTrace();
//                    logger.error(e.getMessage());
//                }
//                Version[] upv = objectMapper.readValue(rs.getString(DatabaseUAFConstants.UPV), Version[].class);
//                metadata.setUpv(upv);
//                DisplayPNGCharacteristicsDescriptor[] tcDisplayPNGCharacteristics = objectMapper.readValue(rs.getString(DatabaseUAFConstants.TCDISPLAYPNGCHARACTERISTICLIST), DisplayPNGCharacteristicsDescriptor[].class);
//                metadata.setTcDisplayPNGCharacteristics(tcDisplayPNGCharacteristics);
//                metadata.setKeyRestricted(rs.getBoolean(DatabaseUAFConstants.ISKEYRESTRICTED));
//
//            }
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            logger.error(ex.getMessage());
//        } finally {
//            if (!rs.isClosed()) {
//                rs.close();
//            }
//            if (proc_stmt != null) {
//                proc_stmt.close();
//            }
//            if (db_connection != null) {
//                db_connection.getConn().close();
//            }
//        }
        return metadata;
    }
}
