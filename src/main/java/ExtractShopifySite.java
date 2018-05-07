import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by cuongnguyen on 5/5/18.
 */
public class ExtractShopifySite {
    public static void main(String[] args) throws Exception {

        //ExtractDownloaded();
        Helper.setupLogging();
        GetPixels();
        //getPageContent(1000, "https://dark-jewel.com", "");

    }

    private static void GetPixels() throws SQLException, IOException {
        MySQLUtils util = new MySQLUtils();

        ArrayList<ShopifyPixel> siteNeedToCrawls = util.getShopifySites();

        for (ShopifyPixel site: siteNeedToCrawls) {
            System.out.println("Processing " + site.Id);
            getPageContent(site.Id, "https://" + site.Url, site.Pixels);

        }
    }

    private static void ExtractDownloaded() throws IOException, SQLException {
        MySQLUtils util = new MySQLUtils();
        for (int i = 1; i <= 1398; i++ ) {
            System.out.println("list" +i + ".html");
            File input = new File("/Users/cuongnguyen/Documents/working-project/crawling/cophieu-copy/list" + i + ".html");
            Document doc = Jsoup.parse(input, "UTF-8");

            Elements links = doc.select("a[href]");

            for (Element link : links) {

                if (!link.attr("ri").isEmpty()) {
                    if (!util.checkShopifyUrlExist(link.text())) {
                        util.insertShopify(link.text());
                    }
                }
            }
        }

    }
    public static void getPageContent(Integer id, String url, String pixels) throws IOException, SQLException {
        MySQLUtils util = new MySQLUtils();
        try {

            WebClient webClient = new WebClient(BrowserVersion.CHROME);
            webClient.getOptions().setJavaScriptEnabled(true);
            webClient.getOptions().setCssEnabled(false);
            webClient.getOptions().setThrowExceptionOnScriptError(false);
            webClient.getOptions().setTimeout(30000);
            HtmlPage page = webClient.getPage(url);
            WebResponse response = page.getWebResponse();
            String content = response.getContentAsString();
            String pageContent = page.asXml();
            //System.out.println(pageContent);
            int index = pageContent.lastIndexOf("pixelIds");
            if (index > 0) {
                int lastIndex = pageContent.indexOf("]", index);
                System.out.print(pageContent.substring(index + 11, lastIndex));
                String newPixels = pageContent.substring(index + 11, lastIndex);
                if (pixels != null && !pixels.isEmpty())
                    newPixels = pixels + "," + newPixels;
                util.updateShopifyPixel(id, newPixels, 0, "");
            }
            else {
                util.updateShopifyPixel(id, pixels, 0, "");
            }
        } catch (Exception e) {
//            System.out.println("error " + id);
//            if (e.getMessage().contains("Certificate for ")) {
//                url = "http://" + url.substring(8, url.length());
//                getHttpPageContent(id, url, pixels);
//            }
//            else
                util.updateShopifyPixel(id, pixels, 1, e.getMessage());

        }
    }

    public static void getHttpPageContent(Integer id, String url, String pixels) throws IOException, SQLException {
        MySQLUtils util = new MySQLUtils();
        try {

            WebClient webClient = new WebClient(BrowserVersion.CHROME);
            webClient.getOptions().setJavaScriptEnabled(true);
            webClient.getOptions().setCssEnabled(false);
            webClient.getOptions().setThrowExceptionOnScriptError(false);
            webClient.getOptions().setTimeout(30000);
            HtmlPage page = webClient.getPage(url);
            WebResponse response = page.getWebResponse();
            String content = response.getContentAsString();
            String pageContent = page.asXml();
            //System.out.println(pageContent);
            int index = pageContent.lastIndexOf("pixelIds");
            if (index > 0) {
                int lastIndex = pageContent.indexOf("]", index);
                System.out.print(pageContent.substring(index + 11, lastIndex));
                String newPixels = pageContent.substring(index + 11, lastIndex);
                if (pixels != null && !pixels.isEmpty())
                    newPixels = pixels + "," + newPixels;
                util.updateShopifyPixel(id, newPixels, 0, "");
            }
            else {
                util.updateShopifyPixel(id, pixels, 0, "");
            }
        } catch (Exception e) {
            System.out.println("error " + id);
            util.updateShopifyPixel(id, pixels, 1, e.getMessage());

        }

    }

}
