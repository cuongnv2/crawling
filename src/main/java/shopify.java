import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.*;
//import com.gargoylesoftware.htmlunit.javascript.host.URL;
import com.gargoylesoftware.htmlunit.util.Cookie;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;


/**
 * Created by cuongnguyen on 4/28/18.
 */
public class shopify {


    public static void main(String[] args) throws Exception {

        getShopifyList(45);

    }

    public static void getShopifyList(Integer desiredCrawledNumber) throws IOException, SQLException, InterruptedException {

        WebClient webClient = new WebClient(BrowserVersion.CHROME);
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.getOptions().setCssEnabled(true);
        webClient.getOptions().setThrowExceptionOnScriptError(false);

        String proxyUrl = "https://free-proxy-list.net/";
        HtmlPage proxyPage = webClient.getPage(proxyUrl);
        List<Proxy> proxies = new ArrayList<Proxy>();

        List<HtmlAnchor> links = proxyPage.getByXPath("//div[@class='dataTables_paginate fg-buttonset ui-buttonset fg-buttonset-multi ui-buttonset-multi paging_full_numbers']//ul//li//a");
        for (HtmlAnchor link : links) {
            HtmlPage page = link.click();
            HtmlTable proxyListTable = (HtmlTable) page.getElementById("proxylisttable");
            List<HtmlTableRow> rows = proxyListTable.getRows();
            for (int i = 1; i < rows.size(); i++) {
                List<HtmlTableCell> cells = rows.get(i).getCells();
                if (cells.get(0).asText().contains(".") && cells.get(6).asText().contains("yes")) {
                    proxies.add(new Proxy(cells.get(0).asText(), cells.get(1).asText()));

                }
            }
        }
        MySQLUtils util = new MySQLUtils();
        int crawledPage = util.getCrawledMyIPPage();

        for (Proxy proxy: proxies) {
            System.out.println("Using proxy number " + proxy.ip);



            String proxyHost = proxy.ip;
            String proxyPort = proxy.port;

            if (proxyHost != null && !"".equals(proxyHost) && proxyPort != null && !"".equals(proxyPort)) {
                ProxyConfig proxyConfig = new ProxyConfig(proxyHost, Integer.parseInt(proxyPort));
                webClient.getOptions().setProxyConfig(proxyConfig);
            }

            webClient.setAjaxController(new NicelyResynchronizingAjaxController());
            Set<Cookie> cookies = new HashSet<Cookie>();
            cookies.addAll(webClient.getCookieManager().getCookies());

            String cookieHeader = "s2_theme_ui=red; gat=1; gid=GA1.2.1613492260.1525331573; s2_csrf_cookie_name=a990bc673a93b62c5cc27086a838e50d;s2_csrf_cookie_name=a990bc673a93b62c5cc27086a838e50d; disqus_unique=4uaierj12hreba;_jid=4uaierj107obmh; ga=GA1.2.1907813435.1525331573;IDE=AHWqTUnAztD7cIS2TK8sii4zlBA34ub4fdru_upcI4bOsWl9RVUS4Y6Cqxo2JsPO;s2_uLang=en; ";
            // cookieHeader.append("; _unam=737437c-1631fa6e134-71643e17-89");

            //"www.fourseasonssale.com"
            for (int i = 1 ; i < desiredCrawledNumber; i++) {
                try {
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
                    WebResponse response = redirectPage.getWebResponse();
                    if(response.getContentAsString().length() > 1000) {
                        PrintWriter out = new PrintWriter("list" + i + ".html");
                        out.println(response.getContentAsString());
                        util.updateCrawledMyIpPage(crawledPage + i);

                        Document doc = Jsoup.parse(response.getContentAsString(), "UTF-8");

                        Elements webLinks = doc.select("a[href]");

                        for (Element webLink : webLinks) {

                            if (!webLink.attr("ri").isEmpty()) {
                                if (!util.checkShopifyUrlExist(webLink.text())) {
                                    util.insertShopify(webLink.text());
                                }
                            }
                        }
                    }
                    else {
                        break;
                    }
                }
                catch (Exception e) {
                    System.out.print(e.getMessage());
                    break;
                }
            }
        }
    }

//    public static void getAllShopifyList() throws IOException, SQLException, InterruptedException {
//
//        WebClient webClient = new WebClient(BrowserVersion.CHROME);
//        webClient.getOptions().setJavaScriptEnabled(true);
//        webClient.getOptions().setCssEnabled(true);
//        webClient.getOptions().setThrowExceptionOnScriptError(false);
//
//        String proxyUrl = "https://free-proxy-list.net/";
//        HtmlPage proxyPage = webClient.getPage(proxyUrl);
//        List<Proxy> proxies = new ArrayList<Proxy>();
//
//        List<HtmlAnchor> links = proxyPage.getByXPath("//div[@class='dataTables_paginate fg-buttonset ui-buttonset fg-buttonset-multi ui-buttonset-multi paging_full_numbers']//ul//li//a");
//        for (HtmlAnchor link : links) {
//            HtmlPage page = link.click();
//            HtmlTable proxyListTable = (HtmlTable) page.getElementById("proxylisttable");
//            List<HtmlTableRow> rows = proxyListTable.getRows();
//            for (int i = 1; i < rows.size(); i++) {
//                List<HtmlTableCell> cells = rows.get(i).getCells();
//                if (cells.get(0).asText().contains(".") && cells.get(6).asText().contains("yes")) {
//                    proxies.add(new Proxy(cells.get(0).asText(), cells.get(1).asText()));
//
//                }
//            }
//        }
//
//        for (Proxy proxy: proxies) {
//            System.out.println("Using proxy number " + proxy.ip);
//            MySQLUtils util = new MySQLUtils();
//            int startPage = util.getCrawledMyIPPage();
//            if (startPage == 0)
//                return;
//
//            String proxyHost = proxy.ip;
//            String proxyPort = proxy.port;
//
//            if (proxyHost != null && !"".equals(proxyHost) && proxyPort != null && !"".equals(proxyPort)) {
//                ProxyConfig proxyConfig = new ProxyConfig(proxyHost, Integer.parseInt(proxyPort));
//                webClient.getOptions().setProxyConfig(proxyConfig);
//            }
//
//            webClient.setAjaxController(new NicelyResynchronizingAjaxController());
//            Set<Cookie> cookies = new HashSet<Cookie>();
//            cookies.addAll(webClient.getCookieManager().getCookies());
//
//            String cookieHeader = "s2_theme_ui=red; gat=1; gid=GA1.2.1613492260.1525331573; s2_csrf_cookie_name=a990bc673a93b62c5cc27086a838e50d;s2_csrf_cookie_name=a990bc673a93b62c5cc27086a838e50d; disqus_unique=4uaierj12hreba;_jid=4uaierj107obmh; ga=GA1.2.1907813435.1525331573;IDE=AHWqTUnAztD7cIS2TK8sii4zlBA34ub4fdru_upcI4bOsWl9RVUS4Y6Cqxo2JsPO;s2_uLang=en; ";
//            // cookieHeader.append("; _unam=737437c-1631fa6e134-71643e17-89");
//
//            //"www.fourseasonssale.com"
//            for (int i = startPage +1 ; i < startPage + 50; i++) {
//                try {
//                    URL url = new URL("https://myip.ms/ajax_table/sites/" + i + "/ipID/23.227.38.1/ipIDii/23.227.38.90/sort/3/asc/1");
//                    WebRequest requestSettings = new WebRequest(url); //WebRequest(url, HttpMethod.POST);
//                    requestSettings.setAdditionalHeader("Cookie", cookieHeader.toString());
//                    requestSettings.setAdditionalHeader("Accept", "text/html, */*; q=0.01");
//                    requestSettings.setAdditionalHeader("Connection", "keep-alive");
//                    requestSettings.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
//                    requestSettings.setAdditionalHeader("Referer", " https://myip.ms/browse/sites/2/ipID/23.227.38.1/ipIDii/23.227.38.90/sort/3/asc/1");
//                    requestSettings.setAdditionalHeader("Accept-Language", "en-US,en;q=0.8");
//                    requestSettings.setAdditionalHeader("Accept-Encoding", "gzip,deflate,sdch");
//                    requestSettings.setAdditionalHeader("Accept-Charset", "ISO-8859-1,utf-8;q=0.7,*;q=0.3");
//                    requestSettings.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
//                    requestSettings.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:59.0) Gecko/20100101 Firefox/59.0");
//                    requestSettings.setAdditionalHeader("Cache-Control", "no-cache");
//                    requestSettings.setAdditionalHeader("Pragma", "no-cache");
//                    Page redirectPage = webClient.getPage(requestSettings);
//                    WebResponse response = redirectPage.getWebResponse();
//                    if(response.getContentAsString().length() > 1000) {
//                        PrintWriter out = new PrintWriter("list" + i + ".html");
//                        out.println(response.getContentAsString());
//                        util.updateCrawledMyIpPage(i);
//
//                        Document doc = Jsoup.parse(response.getContentAsString(), "UTF-8");
//
//                        Elements webLinks = doc.select("a[href]");
//
//                        for (Element webLink : webLinks) {
//
//                            if (!webLink.attr("ri").isEmpty()) {
//                                if (!util.checkShopifyUrlExist(webLink.text())) {
//                                    util.insertShopify(webLink.text());
//                                }
//                            }
//                        }
//                    }
//                    else {
//                        break;
//                    }
//                }
//                catch (Exception e) {
//                    System.out.print(e.getMessage());
//                    break;
//                }
//            }
//        }
//    }

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
}

