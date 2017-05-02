package umlparser.umlparser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DrawDiagram {

    public static Boolean drawImage(String yumlCode, String outPath) {

        try {
            String url = "https://yuml.me/diagram/plain/class/" + yumlCode;
            URL yumlUrl = new URL(url);
            HttpURLConnection httpConnection = (HttpURLConnection) yumlUrl.openConnection();
            httpConnection.setRequestMethod("GET");
            httpConnection.setRequestProperty("Accept", "application/json");

            if (httpConnection.getResponseCode() != 200) {
                throw new RuntimeException("Runtime Exception Occured: HTTP" + httpConnection.getResponseCode());
            }
            OutputStream outputStream = new FileOutputStream(new File(outPath));
            int r = 0;
            byte[] byteStream = new byte[32768];

            while ((r = httpConnection.getInputStream().read(byteStream)) != -1) {
                outputStream.write(byteStream, 0, r);
            }
            outputStream.close();
            httpConnection.disconnect();
        } 
        catch (RuntimeException e) {
        	
            e.printStackTrace();
        } 
        catch (IOException e) {
           
        	e.printStackTrace();
        }
        return null;
    }
}
