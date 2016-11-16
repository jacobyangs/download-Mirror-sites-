import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jacob on 2016/11/15.
 */
public class ParseHtml {
    public List<String> geturls(String dir, String file) throws Exception {
        List<String> urls = null;
        File input = new File(file);
        Document doc = Jsoup.parse(input, "utf-8");
        String shangji = doc.getElementsByTag("h1").text().replace("Index of /", Request2Index.STARTURL);
        Elements elements = doc.getElementsByTag("a");
        if (elements != null) {
            urls = new ArrayList<>();
            for (Element e : elements) {
                if (!e.text().equals("../")) {
                    urls.add(shangji + e.attr("href"));
                }
            }
        }
        return urls;
    }
}
