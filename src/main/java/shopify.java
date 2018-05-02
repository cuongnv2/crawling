import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by cuongnguyen on 4/28/18.
 */
public class shopify {

    static WebClient webClient = new WebClient(BrowserVersion.CHROME);

    public static void main(String[] args) throws Exception {

        getShopifyList();

    }

    private static  void makeWebClientWaitThroughJavaScriptLoadings() {
        webClient.setAjaxController(new AjaxController(){
            @Override
            public boolean processSynchron(HtmlPage page, WebRequest request, boolean async)
            {
                return true;
            }
        });
    }

    public static void getShopifyList() throws IOException, SQLException, InterruptedException {
        //"www.fourseasonssale.com"

        String url = "https://myip.ms/browse/sites/1/ipID/23.227.38.1/ipIDii/23.227.38.90/sort/3/asc/1#sites_tbl_top";

        //webClient.getOptions().setJavaScriptEnabled(false);
        //webClient.getOptions().setCssEnabled(false);

        webClient.waitForBackgroundJavaScript(15000); //Wait for document.ready Auto search to fire and fetch page results via AJAX
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        HtmlPage page = webClient.getPage(url);
        HtmlTable table = (HtmlTable) page.getElementById("sites_tbl");

//        for (HtmlTableRow row : table.getRows()) {
//            if (row.getCells().size() >= 8) {
//                System.out.println(row.getCells().get(1).asText());
//            }
//
//        }
////        String pageContent =page.asXml();
////                PrintWriter out = new PrintWriter("list.txt");
//
//
//        out.println(pageContent);
        int i = 0;

        List<HtmlAnchor> links = page.getByXPath("//div[@class='aqPsites_tbl aqPaging']//a");
        for(HtmlAnchor link: links)
        {
            i+=1;
            makeWebClientWaitThroughJavaScriptLoadings();
            HtmlPage page2 = link.click();
            waitOutLoading(page2);

            String pageContent = page2.asXml();
            PrintWriter out = new PrintWriter("list" + i + ".txt");
            out.println(pageContent);
            System.out.println("x");
        }
    }
    private static void waitOutLoading(HtmlPage page) {
        while(page.asText().toLowerCase().contains("please wait...")){
            webClient.waitForBackgroundJavaScript(100);
        }
    }
}
