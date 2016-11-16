import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jacob on 2016/11/15.
 */
public class StringUtil {
    public static String getLastchar(String str){
        return String.valueOf(str.toCharArray()[str.length()-1]);
    }


    public static void main(String[] args) throws Exception {
        //递归显示C盘下所有文件夹及其中文件
        File root = new File("c:");
        showAllFiles(root);
    }

    final static void showAllFiles(File dir) throws Exception{
        File[] fs = dir.listFiles();
        for(int i=0; i<fs.length; i++){
            System.out.println(fs[i].getAbsolutePath());
            if(fs[i].isDirectory()){
                try{
                    showAllFiles(fs[i]);
                }catch(Exception e){}
            }
        }
    }
    public static List<String> passDownloadedfile() throws Exception{
        List<String> result = new ArrayList<>();
        BufferedReader bfr = new BufferedReader(new InputStreamReader( new FileInputStream(Download.downloaded_file)));
        String temp = null;
        while ((temp = bfr.readLine()) != null){
            result.add(temp);
        }
        bfr.close();
        return result;
    }
    public static boolean atList(List<String> list,String index){
        for (String s : list){
            if (s.equals(index)){
                return true;
            }
        }
        return false;
    }
}
