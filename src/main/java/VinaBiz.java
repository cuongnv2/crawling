import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;

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
        getCityName();
        Helper.setupLogging();

        //handleSpecifyIndustry("https://vinabiz.org/categories/business/nong-nghiep-lam-nghiep-thuy-san/300031003000300030003000", "thuy san");
        //WebClient webClient = new WebClient();
        //getComanyDetailInfo(webClient,"http://trangvangvietnam.com/listings/1187759913/chi_nhanh_cong_ty_tnhh_tm_dv_hoa_binh_phat.html");

    }

    public static void getCityName() throws IOException, SQLException {
        WebClient webClient = new WebClient(BrowserVersion.CHROME);
        webClient.getOptions().setJavaScriptEnabled(false);
        getPaging(webClient, "https://vinabiz.org/categories/tinhthanh/ha-noi/310030003100");
//        HtmlPage containerIndustryPage = webClient.getPage("https://vinabiz.org/categories/tinhthanh");
//        List<HtmlAnchor> links = containerIndustryPage.getByXPath("//div[@class='widget-body no-padding padding-left-5 padding-bottom-5 padding-right-5 padding-top-5']//a");
//        for(HtmlAnchor link: links) {
//            System.out.println(link);
//        }
    }
    private static void handleSpecifyIndustry(String url, String category) {
        System.out.println("Processing an industry " + url );
        WebClient webClient = new WebClient(BrowserVersion.CHROME);
        webClient.getOptions().setJavaScriptEnabled(false);
        //MySQLUtils util = new MySQLUtils();
        try {
            System.out.println(url);
            Integer maxPageNumber; //= getPaging(webClient, url);
            maxPageNumber = 1;
            System.out.println(maxPageNumber);
            for (int i = 1; i <= maxPageNumber; i++) {
               handleEachPageInIndustry(webClient, url, i, category);

            }
        }
        catch (IOException ex) {System.out.println(ex.getMessage());}
        catch (SQLException ex) {System.out.println(ex.getMessage());}
        finally {
            webClient.close();
        }
        System.out.println("x");
    }

    private static boolean handleEachPageInIndustry(WebClient webClient, String url, int pageNumber, String category) throws IOException, SQLException {
        if (url.substring(url.length() - 1).equalsIgnoreCase("/"))
            url = url + pageNumber;
        else
            url = url + "/" + pageNumber;

        String vinaBiz = "https://vinabiz.org/";

        HtmlPage containerIndustryPage = webClient.getPage(url);
        List<HtmlAnchor> links  = containerIndustryPage.getByXPath("//div[@class='col-md-12 padding-left-0']//h4//a");
//        for(HtmlAnchor link : links) {
//            getComanyDetailInfo(webClient, link.toString(), category);
//        }
        getComanyDetailInfo(webClient,"https://vinabiz.org/company/detail/cong-ty-tnhh-xay-dung-tran-quang-minh/3400360030003100350031003300390038003000", "test");

        return false;
    }

    private static void getComanyDetailInfo(WebClient webClient, String url, String category) throws IOException {
        HtmlPage comapnyPage = webClient.getPage(url);
        List<HtmlTable> tables = comapnyPage.getByXPath("//div[@class='table-responsive']//table");
        String ten_chinh_thuc = "";
        String ten_giao_dich = "";
        String ma_so_thue = "";
        String ngay_cap = "";
        String co_quan_thue = "";
        String ngay_bat_dau_hoat_dong = "";
        String trang_thai = "";
        String dia_chi_tru_so = "";
        String nganh_nghe = "";
        if (tables.size() > 0) {
            HtmlTable table = tables.get(0);
            for (HtmlTableRow row : table.getRows()) {
                List<HtmlTableCell> cells = row.getCells();
                for (int i =0; i < cells.size(); i++) {
                    if (cells.get(i).asText().toLowerCase().equalsIgnoreCase("tên chính thức")) {
                        ten_chinh_thuc = cells.get(i + 1).asText();
                    }
                    if (cells.get(i).asText().toLowerCase().equalsIgnoreCase("tên giao dịch")) {
                        ten_giao_dich = cells.get(i + 1).asText();
                    }
                    if (cells.get(i).asText().toLowerCase().equalsIgnoreCase("mã doanh nghiệp")) {
                        ma_so_thue = cells.get(i + 1).asText();
                    }
                    if (cells.get(i).asText().toLowerCase().equalsIgnoreCase("ngày cấp")) {
                        ngay_cap = cells.get(i + 1).asText();
                    }
                    if (cells.get(i).asText().toLowerCase().equalsIgnoreCase("cơ quan thuế quản lý")) {
                        co_quan_thue = cells.get(i + 1).asText();
                    }
                    if (cells.get(i).asText().toLowerCase().equalsIgnoreCase("ngày bắt đầu hoạt động")) {
                        ngay_bat_dau_hoat_dong = cells.get(i + 1).asText();
                    }
                    if (cells.get(i).asText().toLowerCase().equalsIgnoreCase("trạng thái")) {
                        trang_thai = cells.get(i + 1).asText();
                    }
                    if (cells.get(i).asText().toLowerCase().equalsIgnoreCase("địa chỉ trụ sở")) {
                        dia_chi_tru_so = cells.get(i + 1).asText();
                    }
                    if (cells.get(i).asText().toLowerCase().equalsIgnoreCase("ngành nghề chính")) {
                        nganh_nghe = cells.get(i + 1).asText();
                    }
                }
            }
        }
        System.out.println("ten chinh thuc" + ten_chinh_thuc);
        System.out.println("ten giao dich  " + ten_giao_dich);
        System.out.println("ma so thue " + ma_so_thue);
        System.out.println("ngay cap " + ngay_cap);
        System.out.println("co quan thue " + co_quan_thue);
        System.out.println("ngay_bat_dau_hoat_dong " + ngay_bat_dau_hoat_dong);
        System.out.println("trang_thai " + trang_thai);
        System.out.println("dia_chi_tru_so " + dia_chi_tru_so);
        System.out.println("nganh nghe " + nganh_nghe);
    }

    // Keep it has reason.
//    public static Integer getPaging(WebClient webClient, String url) throws IOException, SQLException {
//        HtmlPage containerPage = webClient.getPage(url);
//        List<HtmlAnchor> links = containerPage.getByXPath("//li[@class='PagedList-skipToLast']//a");
//        Integer pageNumber = Integer.parseInt(extractPagingNumberFromString(links.get(0).getHrefAttribute().toString()));
//
//        webClient.close();
//        return pageNumber;
//    }

    public static Integer getPaging(WebClient webClient, String url) throws IOException, SQLException {
        HtmlPage containerPage = webClient.getPage(url);
        List<HtmlAnchor> links = containerPage.getByXPath("//li[@class='disabled PagedList-pageCountAndLocation']//a");
        Integer pageNumber = 0;
        try {
            String tmp = links.get(0).asText().split(" ")[3].toString();
            pageNumber = Integer.parseInt(tmp.substring(0, tmp.length() - 1));
        }
        catch(Exception e) {}
        //webClient.close();
        return pageNumber;
    }

    public static String extractPagingNumberFromString(String url) {
        return url.substring(url.lastIndexOf("/") + 1, url.length());
    }
}