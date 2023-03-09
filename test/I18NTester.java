/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.text.ParseException;

/**
 *
 * @author vanth
 */
public class I18NTester {

    public static void main(String[] args) throws ParseException, IOException {
        String input = "Nguyễn văn bỉnh";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        //get the UTF-8 data
        Reader reader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
        //convert UTF-8 to Unicode
        int data = reader.read();
        while (data != -1) {
            char theChar = (char) data;
            System.out.print(theChar);
            data = reader.read();
        }
        reader.close();

//        System.out.println();

        //Convert Unicode to UTF-8 Bytes
       /* ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Writer writer = new OutputStreamWriter(outputStream, Charset.forName("UTF-8"));

        writer.write(input);
        writer.close();

        String out = new String(outputStream.toByteArray());

        System.out.println(out);*/
    }
}
