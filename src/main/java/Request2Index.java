import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import javax.net.ssl.SSLContext;
import javax.security.cert.CertificateException;
import javax.security.cert.X509Certificate;
import java.io.*;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jacob on 2016/11/14.
 */
public class Request2Index {
    //---------------------------------------------------------------------------
    // if you wanna to download other website, just change this place.
    //---------------------------------------------------------------------------
    public static final String USERAGETN = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.71 Safari/537.36";
    public static final String COOKIE = "__cfduid=d32b70628f490f0114c56f42d054ef2231478855488; cf_clearance=4f2ad8f5c1724f3064e7a904ad1a8657fee02b51-1479258684-28800";
    public static final String STARTURL = "https://ht.transparencytoolkit.org/";
    public static final String DOWNLINKS = "temp/downloadlinks.txt";
    private static List<String> links = new ArrayList<>();
    private static BufferedWriter bfw;

    private static int index = 1;
    static {
        File file = new File(DOWNLINKS);
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            bfw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(DOWNLINKS),"utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) throws Exception{

        request(STARTURL,"https://ht.transparencytoolkit.org/");
        System.out.println("--------------split line--------------");
        System.out.println(links.size()+"need download");
    }
    public static void request(String url,String dir) throws Exception{

//            url = url.replaceAll(" ","%20");
//            System.out.println("request url:" + url);
            CloseableHttpClient httpClient = createSSLInsecureClient();
            HttpGet httpGet = new HttpGet(url);
            httpGet.setHeader("user-agent", USERAGETN);
            httpGet.setHeader("cookie", COOKIE);
            httpGet.setHeader(":authority","ht.transparencytoolkit.org");
            httpGet.setHeader(":method","GET");
            httpGet.setHeader(":path","/");
            httpGet.setHeader(":scheme","https");
            httpGet.setHeader("accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
            httpGet.setHeader("accept-encoding","gzip, deflate, sdch, br");
            httpGet.setHeader("accept-language","zh-CN,zh;q=0.8");
            httpGet.setHeader("cache-control","max-age=0");
            httpGet.setHeader("upgrade-insecure-requests","1");
            HttpResponse response = httpClient.execute(httpGet);
            System.out.println(response.getStatusLine().getStatusCode());
            writeFile(response.getEntity().getContent(), "OUTPUT/"+String.valueOf(index)+".html");
            ParseHtml parse = new ParseHtml();
            List<String> alllinks = parse.geturls(dir,"OUTPUT/"+String.valueOf(index)+".html");
            for (String urls : alllinks) {
                index++;
                if (!StringUtil.getLastchar(urls).equals("/")) {
                    System.out.println(urls);
                    bfw.write(urls);
                    bfw.newLine();
//                    links.add(urls);
                } else {
                    request(urls, urls.replace(dir,""));
                }
            }
    }
    public static CloseableHttpClient createSSLInsecureClient()
    {
        try
        {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy()
            {
                @Override
                public boolean isTrusted(java.security.cert.X509Certificate[] x509Certificates, String s) throws java.security.cert.CertificateException {
                    return false;
                }
                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException
                {
                    return true;
                }
            }).build();
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);
            return HttpClients.custom().setSSLSocketFactory(sslsf).build();
        } catch (KeyManagementException e)
        {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        } catch (KeyStoreException e)
        {
            e.printStackTrace();
        }
        return HttpClients.createDefault();
    }
    public static void writeFile(InputStream in ,String filename) throws Exception{
        File file = new File(filename);
        if(!file.exists()){
            file.getParentFile().mkdir();
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
    public static void writeDownloadFileUrls(List<String> in , String downloadlinks)throws Exception{
        File file = new File(downloadlinks);
        if(!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
        }
        BufferedWriter bfw =  new BufferedWriter(new OutputStreamWriter(new FileOutputStream(downloadlinks),"utf-8"));
        for (String oneline : in){
            bfw.write(oneline);
            bfw.newLine();
        }
        bfw.close();
    }

}
