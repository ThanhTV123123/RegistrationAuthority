/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ra.uat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import vn.ra.object.ATTRIBUTE_DATA;
import vn.ra.object.ATTRIBUTE_VALUES;
import vn.ra.object.CERTIFICATE_ATTRIBUTES;
import vn.ra.object.CERTIFICATE_ATTRIBUTES_RADIO;
import vn.ra.object.CERTIFICATE_ATTRIBUTES_SESSION;
import vn.ra.object.CERTIFICATION_DATA_ATTR;
import vn.ra.object.CERTIFICATION_PROFILE;
import vn.ra.object.OS_TYPE;
import vn.ra.object.PUSH_TOKEN;
import vn.ra.object.TOKEN_CHANGE_LOG;
import vn.ra.process.AddIPRelying;
import vn.ra.process.CommonFunction;
import vn.ra.process.ConnectDatabase;
import vn.ra.utility.Definitions;
import vn.ra.utility.EscapeUtils;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONObject;
import vn.ra.object.BRANCH;
import vn.ra.object.CERTIFICATION;
import vn.ra.object.CERTIFICATION_CONTACT;
import vn.ra.object.CERTIFICATION_POLICY_DATA;
import vn.ra.object.CERTIFICATION_REPORT_SUMMARY;
import vn.ra.object.GENERAL_POLICY;
import vn.ra.object.PAYMENT;
import vn.ra.object.REPORT_PER_MONTH;
import vn.ra.object.REPORT_QUICK_BRANCH;
import vn.ra.object.TOKEN;
import vn.ra.process.CommonFunction;
import vn.ra.process.ConnectDatabase;
import vn.ra.process.EncodeSOPIN;
import vn.ra.utility.Config;
import vn.ra.utility.Definitions;
import vn.ra.utility.EscapeUtils;

/**
 *
 * @author THANH-PC
 */
public class TestJSON {

    public static void main(String[] args) throws Exception {
//        String sBRIEF_PROPERTIES = "{\"attributes\":[{\"enabled\":true,\"approveCAEnabled\":false,\"attributeType\":\"FILE_TYPE_LIST\",\"attributes\":[{\"name\":\"PHOTO_ID_CARD\",\"enabled\":false,\"approveCAEnabled\":false,\"attributeType\":\"FILE_TYPE/ITEM\"}]},{\"enabled\":true,\"approveCAEnabled\":false,\"attributeType\":\"FILE_SCAN_LIST\",\"attributes\":[{\"name\":\"PHOTO_ID_CARD\",\"enabled\":true,\"approveCAEnabled\":false,\"attributeType\":\"FILE_SCAN/ITEM\"}]},{\"enabled\":false,\"approveCAEnabled\":false}]}";
//        CERTIFICATION_POLICY_DATA[][] resIPData = new CERTIFICATION_POLICY_DATA[1][];
//        CommonFunction.getCollectedBriefProperties(sBRIEF_PROPERTIES, resIPData);
//        if(resIPData[0].length > 0) {
//            System.out.println("OK");
//        }
        
        String sChecked = "1";
        String sName = "PHOTO_ID_CARD";
        boolean booChecked = false;
        if("1".equals(sChecked)) {
            booChecked = true;
        }
        boolean isHasFileType = false;
        CERTIFICATION_POLICY_DATA[][] reProfileDataLast = new CERTIFICATION_POLICY_DATA[1][];
        CERTIFICATION_POLICY_DATA[][] resProfileData = null;
        if(resProfileData != null && resProfileData[0].length > 0) {
            for(CERTIFICATION_POLICY_DATA resProfileData1 : resProfileData[0])
            {
                if(resProfileData1.attributeType.equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_COLLECTED_ITEM_FILE_TYPE))
                {
                    if(resProfileData1.name.equals(sName))
                    {
                        resProfileData1.enabled = booChecked;
                        isHasFileType = true;
                    }
                }
            }
            if(isHasFileType == false)
            {
                if(resProfileData[0].length > 0)
                {
                    ArrayList<CERTIFICATION_POLICY_DATA> tempList;
                    tempList = new ArrayList<>();
                    tempList.addAll(Arrays.asList(resProfileData[0]));
                    CERTIFICATION_POLICY_DATA rsItem = new CERTIFICATION_POLICY_DATA();
                    rsItem.name = sName;
                    rsItem.enabled = true;
                    rsItem.attributeType = Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_COLLECTED_ITEM_FILE_TYPE;
                    tempList.add(rsItem);
                    reProfileDataLast[0] = new CERTIFICATION_POLICY_DATA[tempList.size()];
                    reProfileDataLast[0] = tempList.toArray(reProfileDataLast[0]);
                }
            } else {
                reProfileDataLast = resProfileData;
            }
        } else {
            ArrayList<CERTIFICATION_POLICY_DATA> tempList = new ArrayList<>();
            CERTIFICATION_POLICY_DATA rsItem = new CERTIFICATION_POLICY_DATA();
            rsItem.name = sName;
            rsItem.enabled = true;
            rsItem.attributeType = Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_COLLECTED_ITEM_FILE_TYPE;
            tempList.add(rsItem);
            reProfileDataLast[0] = new CERTIFICATION_POLICY_DATA[tempList.size()];
            reProfileDataLast[0] = tempList.toArray(reProfileDataLast[0]);                                    
        }
        String pBRIEF_PROPERTIES = CommonFunction.renderBriefFileType(reProfileDataLast[0], "", "", "");
        System.out.println("pBRIEF_PROPERTIES: " + pBRIEF_PROPERTIES);
        //        String pBRIEF_PROPERTIES = "{\"attributes\":[{\"enabled\":true,\"approveCAEnabled\":false,\"attributeType\":\"FILE_TYPE_LIST\",\"attributes\":[{\"name\":\"PHOTO_ID_CARD\",\"enabled\":false,\"approveCAEnabled\":false,\"attributeType\":\"FILE_TYPE/ITEM\"}]},{\"enabled\":true,\"approveCAEnabled\":false,\"attributeType\":\"FILE_SCAN_LIST\",\"attributes\":[{\"name\":\"PHOTO_ID_CARD\",\"enabled\":true,\"approveCAEnabled\":false,\"attributeType\":\"FILE_SCAN/ITEM\"}]},{\"enabled\":false,\"approveCAEnabled\":false}]}";
        CERTIFICATION_POLICY_DATA[][] resIPData = new CERTIFICATION_POLICY_DATA[1][];
        CommonFunction.getCollectedBriefProperties(pBRIEF_PROPERTIES, resIPData);
        if(resIPData[0].length > 0) {
            boolean bRegister = CommonFunction.checkBriefFileType(Definitions.CONFIG_FILE_PROFILE_PHOTO_ID_CARD, resIPData);
            System.out.println("OK: " + bRegister);
        }
        /*JSONObject json = new JSONObject();
        json.put("functionType", "2");
        json.put("tokenSN", "");
        json.put("certificateID", "1801");

        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        try {
            HttpPost request = new HttpPost("http://localhost:8081/RADemoJava/TestReceivePush");
            StringEntity params = new StringEntity(json.toString());
            request.addHeader("content-type", "application/json");
            request.setEntity(params);
            httpClient.execute(request);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        } finally {
            httpClient.close();
        }*/

        /*String strURLPath = "D:\\Programer\\Company\\TMS-RA\\FPT\\Demo\\mytemplate123.xlsx";
        FileInputStream inputStream = new FileInputStream("D:\\Programer\\Company\\TMS-RA\\FPT\\Demo\\mytemplate.xlsx");
        XSSFWorkbook wb_template = new XSSFWorkbook(inputStream);
        inputStream.close();
        SXSSFWorkbook wb = new SXSSFWorkbook(wb_template);
        wb.setCompressTempFiles(true);
        SXSSFSheet sheet = (SXSSFSheet) wb.getSheetAt(0);
        sheet.setRandomAccessWindowSize(100);// keep 100 rows in memory, exceeding rows will be flushed to disk
        sheet.setColumnWidth(0, 32 * 255);
        sheet.setColumnWidth(1, 32 * 255);
        Row row1 = sheet.createRow(0);
        CellStyle my_style = wb.createCellStyle();
        Font font = wb.createFont();
        font.setBoldweight((short) 700);
        font.setFontName("Arial");
        my_style.setFont(font);
        my_style.setFillBackgroundColor((short) 9);
        my_style.setAlignment((short) 2);
        Cell cel0l = row1.createCell((short) 0);
        cel0l.setCellValue("COLUMN 01");
        cel0l.setCellStyle(my_style);

        Cell cel02 = row1.createCell((short) 1);
        cel02.setCellValue("COLUMN 02");
        cel02.setCellStyle(my_style);
        
        for (int rownum = 1; rownum < 100000; rownum++) {
            Row row = sheet.createRow(rownum);
            CellStyle my_style1 = wb.createCellStyle();
            Font font1 = wb.createFont();
            font1.setBoldweight((short) 700);
            font1.setFontName("Arial");
            my_style1.setFont(font1);
            Cell cellBranch = row.createCell((short) 0);
            cellBranch.setCellValue(rownum);
            cellBranch.setCellStyle(my_style1);

            Cell cellBranch1 = row.createCell((short) 1);
            cellBranch1.setCellValue("Value - " + rownum);
            cellBranch1.setCellStyle(my_style1);
        }
        ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
        wb.write(outByteStream);
        byte[] outArray = outByteStream.toByteArray();
        File someFile = new File(strURLPath);
        FileOutputStream fos = new FileOutputStream(someFile);
        fos.write(outArray);
        fos.flush();*/
    }

    public static String LoadOSTYPE() throws JsonProcessingException {
        ArrayList<OS_TYPE> tempList = new ArrayList<>();
        OS_TYPE certificationAttributes = new OS_TYPE();
        certificationAttributes.NAME = "NAME";
        certificationAttributes.REMARK = "DES";
        tempList.add(certificationAttributes);
        ObjectMapper objectMapper = new ObjectMapper();
        String strRes = objectMapper.writeValueAsString(tempList);
        return strRes;
    }
}
