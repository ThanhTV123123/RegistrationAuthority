/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ra.uat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.RandomAccessFile;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import vn.ra.process.CommonFunction;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import org.apache.log4j.Appender;
import org.apache.log4j.Layout;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.WriterAppender;
import org.json.simple.JSONObject;
import vn.ra.synch.neac.ListPdfFileBase64;
import vn.ra.synch.neac.NEREQUEST_DATA;
import vn.ra.synch.neac.RequestDataNEAC;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;
import vn.ra.object.ProfileContactInfoJson;
import vn.ra.utility.Config;
import vn.ra.utility.EscapeUtils;

/**
 *
 * @author USER
 */
public class ServerLogRead {

//    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(ServerLogRead.class);
//    private boolean debug = false;
//
//    private int crunchifyRunEveryNSeconds = 2000;
//    private long lastKnownPosition = 0;
//    private boolean shouldIRun = true;
//    private File crunchifyFile = null;
//    private static int crunchifyCounter = 0;
//    String filePath = "D:\\Programer\\Company\\TMS-RA\\TrustCA\\Common\\ra_portal.log";
    public static void main(String[] args) throws Exception {
        Config conf = new Config();
        /*String sss = conf.GetTryPropertybyCode("ABCCCCCC");
        System.out.println(sss);

        String sPrfileContact = "{\"RepresentativeName\":\"Nguyen van A\",\"RepresentativePhone\":\"0908775901\",\"RepresentativeEmail\":\"thanhtv@tomicalab.com\",\"ContactName\":\"Sale_01\",\"Position\":\"Sale\",\"Address\":\"P. An Phu, Q.2\"}";
        ObjectMapper oMapperParse = new ObjectMapper();
        ProfileContactInfoJson profileContact = oMapperParse.readValue(sPrfileContact, ProfileContactInfoJson.class);
        if (profileContact != null) {
            System.out.println(EscapeUtils.CheckTextNull(profileContact.PIDIssuedBy));
        }*/
        
        System.out.println(conf.GetTryPropertybyCode("IS_WHICH_ABOUT_CA"));

        /*String filePath = "D:\\Common Test\\convert_word_pdf2.pdf";
        File pdfFile = new File(filePath);
        byte[] encoded = Files.readAllBytes(Paths.get(pdfFile.getAbsolutePath()));
        Base64.Encoder enc = Base64.getEncoder();
        byte[] strenc = enc.encode(encoded);
        String encode = new String(strenc, "UTF-8");
        System.out.println(encode);*/
//        String[] aaa = new String[2];
//        aaa[0] = "112";
//        aaa[1] = "1122";
//        for(int i=0;i<aaa.length; i++){
//            System.out.println(aaa[i]);
//        }
//        String json = "{\"id\": 1001, \"firstName\": \"Lokesh\",\"lastName\": \"Gupta\", \"email\": \"howtodoinjava@gmail.com\"}";
//        JsonElement jelement = new JsonParser().parse(json);
//        JsonObject jsonObj = new JsonObject();
//        jsonObj.add("mac1", new Gson().toJsonTree(json));
        /*RequestDataNEAC res = new RequestDataNEAC();
        NEREQUEST_DATA itemReqNEAC = new NEREQUEST_DATA();
        itemReqNEAC.UserID = "1";
        itemReqNEAC.VERSION = "AAA";
        itemReqNEAC.SERIAL_NUMBER = "aaassasa";
        res.certForCAModel=itemReqNEAC;
        ListPdfFileBase64[] arrayList;
        ArrayList<ListPdfFileBase64> tempList = new ArrayList<>();
        ListPdfFileBase64 item =new ListPdfFileBase64();
        item.fileName = "ABC.pdf";
        item.fileBase64Content = "AAOSO92kjkKJD";
        tempList.add(item);
        arrayList = new ListPdfFileBase64[tempList.size()];
        arrayList = tempList.toArray(arrayList);
        res.listPdfFileBase64 = arrayList;
        System.out.println(new Gson().toJson(res));*/
//        Gson gson = new GsonBuilder().setPrettyPrinting().create();
//        JsonElement jsonElement = gson.toJsonTree(ssss);
//        jsonObj.add("mac2", new Gson().toJson(jsonElement));
////        JsonArray jarray = jobject.getAsJsonArray("translations");
////        jobject = jarray.get(0).getAsJsonObject();
////        String result = jobject.get("translatedText").toString();
//        System.out.println(new Gson().toJson(jsonObj));
    }

//    private static void appendData(String filePath, boolean shouldIRun, int crunchifyRunEveryNSeconds) {
//        FileWriter fileWritter;
//
//        try {
//            while (shouldIRun) {
//                Thread.sleep(crunchifyRunEveryNSeconds);
//                fileWritter = new FileWriter(filePath, true);
//                BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
//
//                String data = "\nCrunchify.log file content: " + Math.random();
//                bufferWritter.write(data);
//                bufferWritter.close();
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
}
