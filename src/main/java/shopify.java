import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.*;
//import com.gargoylesoftware.htmlunit.javascript.host.URL;
import com.gargoylesoftware.htmlunit.util.Cookie;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by cuongnguyen on 4/28/18.
 */
public class shopify {

    //static WebClient webClient = new WebClient(BrowserVersion.CHROME);

    public static void main(String[] args) throws Exception {

        getShopifyList();

    }

//    private static  void makeWebClientWaitThroughJavaScriptLoadings() {
//        webClient.setAjaxController(new AjaxController(){
//            @Override
//            public boolean processSynchron(HtmlPage page, WebRequest request, boolean async)
//            {
//                return true;
//            }
//        });
//    }

    public static void getShopifyList() throws IOException, SQLException, InterruptedException {
        String mainPage = "https://myip.ms/browse/sites/1/ipID/23.227.38.1/ipIDii/23.227.38.90/sort/3/asc/1";
        WebClient webClient = new WebClient(BrowserVersion.CHROME);
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.getOptions().setCssEnabled(true);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getPage(mainPage);
        String proxyHost = "85.115.15.2";
        String proxyPort = "8080";


//        String proxyHost = "82.222.40.107";
//        String proxyPort = "8080";
        if (proxyHost != null && !"".equals(proxyHost) && proxyPort != null && !"".equals(proxyPort))
        {
            ProxyConfig proxyConfig = new ProxyConfig(proxyHost, Integer.parseInt(proxyPort));
            webClient.getOptions().setProxyConfig(proxyConfig);
        }

        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        Set<Cookie> cookies = new HashSet<Cookie>();
        cookies.addAll(webClient.getCookieManager().getCookies());
//        StringBuilder cookieHeader = new StringBuilder();
//        for (int index = 0; index < cookies.size(); index++){
//            String cookie = cookies.toArray()[index].toString();
//            String cookieNameValue =cookie.substring(0, cookie.indexOf(";"));
//            String name = cookieNameValue.substring(0, cookieNameValue.indexOf("="));
//            String value = cookieNameValue.substring(cookieNameValue.indexOf("=") + 1);
//
//            if (index == 0){
//                cookieHeader.append(name + "=" +value);
//            } else {
//                cookieHeader.append("; "+ name + "=" +value);
//            }
//        }
        String cookieHeader = "s2_theme_ui=red; gat=1; gid=GA1.2.1613492260.1525331573; s2_csrf_cookie_name=a990bc673a93b62c5cc27086a838e50d;s2_csrf_cookie_name=a990bc673a93b62c5cc27086a838e50d; disqus_unique=4uaierj12hreba;_jid=4uaierj107obmh; ga=GA1.2.1907813435.1525331573;IDE=AHWqTUnAztD7cIS2TK8sii4zlBA34ub4fdru_upcI4bOsWl9RVUS4Y6Cqxo2JsPO;s2_uLang=en; ";
       // cookieHeader.append("; _unam=737437c-1631fa6e134-71643e17-89");

        //"www.fourseasonssale.com"
        for(int i=220; i <230; i++) {
            //URL url = new URL("https://myip.ms/browse/sites/" + i + "/ipID/23.227.38.1/ipIDii/23.227.38.90/sort/3/asc/1");
           URL url = new URL("https://myip.ms/ajax_table/sites/" + i + "/ipID/23.227.38.1/ipIDii/23.227.38.90/sort/3/asc/1");
            WebRequest requestSettings = new WebRequest(url); //WebRequest(url, HttpMethod.POST);
            requestSettings.setAdditionalHeader("Cookie", cookieHeader.toString());
            requestSettings.setAdditionalHeader("Accept", "text/html, */*; q=0.01");
            requestSettings.setAdditionalHeader("Connection", "keep-alive");
            requestSettings.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            requestSettings.setAdditionalHeader("Referer", " https://myip.ms/browse/sites/2/ipID/23.227.38.1/ipIDii/23.227.38.90/sort/3/asc/1");
            requestSettings.setAdditionalHeader("Accept-Language", "en-US,en;q=0.8");
            requestSettings.setAdditionalHeader("Accept-Encoding", "gzip,deflate,sdch");
            requestSettings.setAdditionalHeader("Accept-Charset", "ISO-8859-1,utf-8;q=0.7,*;q=0.3");
            requestSettings.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
            requestSettings.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:59.0) Gecko/20100101 Firefox/59.0");
            requestSettings.setAdditionalHeader("Cache-Control", "no-cache");
            requestSettings.setAdditionalHeader("Pragma", "no-cache");
            Page redirectPage = webClient.getPage(requestSettings);
            webClient.waitForBackgroundJavaScript(10000);
            WebResponse response = redirectPage.getWebResponse();
            PrintWriter out = new PrintWriter("list" + i + ".html");
            out.println(response.getContentAsString());
            Thread.sleep(5000);
        }


      //  HtmlTable table = (HtmlTable) page.getElementById("sites_tbl");

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
//        int i = 0;
//
//        List<HtmlAnchor> links = page.getByXPath("//div[@class='aqPsites_tbl aqPaging']//a");
//        for(HtmlAnchor link: links)
//        {
//            i+=1;
//            //makeWebClientWaitThroughJavaScriptLoadings();
//            HtmlPage page2 = link.click();
//            //waitOutLoading(page2);
//
//            String pageContent = page2.asXml();
//            PrintWriter out = new PrintWriter("list" + i + ".txt");
//            out.println(pageContent);
//            System.out.println("x");
//        }
    }
//    private static void waitOutLoading(HtmlPage page) {
//        while(page.asText().toLowerCase().contains("please wait...")){
//            webClient.waitForBackgroundJavaScript(100);
//        }
//    }
}
