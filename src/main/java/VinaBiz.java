import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by cuongnguyen on 4/23/18.
 */
public class VinaBiz {
    public static void main(String[] args) throws Exception {
        //getIndustryName();

        handleSpecifyIndustry("https://vinabiz.org/categories/business/nong-nghiep-lam-nghiep-thuy-san/300031003000300030003000");
        //WebClient webClient = new WebClient();
        //getComanyDetailInfo(webClient,"http://trangvangvietnam.com/listings/1187759913/chi_nhanh_cong_ty_tnhh_tm_dv_hoa_binh_phat.html");
    }

    public static void getIndustryName() throws IOException {
        WebClient webClient = new WebClient();
        HtmlPage containerIndustryPage = webClient.getPage("https://vinabiz.org/categories/business/");
        List<HtmlAnchor> links = containerIndustryPage.getByXPath("//div[@class='widget-body no-padding padding-left-5 padding-bottom-5 padding-right-5 padding-top-5']//a");
        for(HtmlAnchor link: links) {
            System.out.println(link);
        }
    }
    private static void handleSpecifyIndustry(String url) {
        WebClient webClient = new WebClient();
        //MySQLUtils util = new MySQLUtils();
        try {
            System.out.println(url);
            Integer maxPageNumber = getPaging(webClient, url);

            System.out.println(maxPageNumber);
            for (int i = 1; i <= maxPageNumber; i++) {
               // handleEachPageInIndustry(webClient, url, i, category);

            }
        }
        catch (IOException ex) {System.out.println(ex.getMessage());}
        catch (SQLException ex) {System.out.println(ex.getMessage());}
        finally {
            webClient.close();
        }
        System.out.println("x");
    }

    public static Integer getPaging(WebClient webClient, String url) throws IOException, SQLException {
        HtmlPage containerPage = webClient.getPage(url);
        List<HtmlAnchor> links = containerPage.getByXPath("//li[@class='PagedList-skipToLast']//a");
        Integer pageNumber = Integer.parseInt(extractPagingNumberFromString(links.get(0).getHrefAttribute().toString()));

        webClient.close();
        return pageNumber;
    }

    public static String extractPagingNumberFromString(String url) {
        return url.substring(url.lastIndexOf("/") + 1, url.length());
    }
}