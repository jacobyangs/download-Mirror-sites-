import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jacob on 2016/11/15.
 */
public class Download {
    private static BufferedWriter bfw  ;
    public static final String downloaded_file = "temp/downloaded-links.txt";
    static {
        try {
            if(!new File(downloaded_file).exists())
                new File(downloaded_file).createNewFile();
            bfw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(downloaded_file,true)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) throws Exception{
        try {

        for(String url : readLinks()){
            if(!StringUtil.atList(StringUtil.passDownloadedfile(),url)){
                downliadrequest(url);
            }else {
                continue;
            }
        }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            bfw.close();
        }
    }
    public static void downliadrequest(String url) throws Exception{
        CloseableHttpClient httpClient = Request2Index.createSSLInsecureClient();
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("user-agent", Request2Index.USERAGETN);
        httpGet.setHeader("cookie", Request2Index.COOKIE);
        HttpResponse response = httpClient.execute(httpGet);
        writeFile(response.getEntity().getContent(),url);
        bfw.write(url);
    }
    public static List<String> readLinks() throws Exception{
        BufferedReader bfr = new BufferedReader(new FileReader(Request2Index.DOWNLINKS));
        List<String> links = new ArrayList<>();
        String temp = null;
        while ((temp = bfr.readLine())!=null){
            links.add(temp);
        }
        return links;
    }
    public static void writeFile(InputStream in , String url) throws Exception{
        String filename = url.replaceAll("https://","");
        System.out.println(filename);
        File file = new File(filename);
        if(!file.exists()){
            file.getParentFile().mkdirs();
            file.createNewFile();
        }
        OutputStream out = new FileOutputStream(filename);
        byte [] buffer = new byte[1024];
        int temp = 0;
        while ((temp = in.read(buffer))!=-1){
            out.write(buffer,0,temp);
//            buffer = new byte[1024];
        }
        in.close();
        out.close();
    }
}
