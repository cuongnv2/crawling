import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.css.StyleElement;
import com.gargoylesoftware.htmlunit.html.*;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YellowPages {
    /*
    * 1. Lay danh sach industry (hay la category)
    * 2. Voi moi industry lay ra danh sach cac city
    * 3. Voi moi city lay ra tat ca cong ty
    * */
    static Integer count = 0;
    public static void main(String[] args) throws Exception {
        //getIndustryName();
        Helper.setupLogging();
        handleSpecifyIndustry("http://trangvangvietnam.com/categories/486229/album_anh_vat_tu_va_thiet_bi.html", "album anh");
        //WebClient webClient = new WebClient();
        //getComanyDetailInfo(webClient,"http://trangvangvietnam.com/listings/1187759913/chi_nhanh_cong_ty_tnhh_tm_dv_hoa_binh_phat.html");
    }

    private static void handleIndustries() throws SQLException {
        MySQLUtils util = new MySQLUtils();
        ArrayList<Category> industries = util.getAllIndustries();
        for(Category c : industries) {
            handleSpecifyIndustry(c.Url, c.CategoryName);
        }

    }

    private static void handleSpecifyIndustry(String url, String categoryName) {
        WebClient webClient = new WebClient(BrowserVersion.CHROME);
        webClient.getOptions().setJavaScriptEnabled(false);
        MySQLUtils util = new MySQLUtils();
        try {
            System.out.println(url);
            ArrayList<City> cityUrls = getCitiesForEachIndustry(webClient, url);
            for(City cityUrl: cityUrls) {
                handleEachCityInSpecificIndustry(webClient, cityUrl.Url, cityUrl.CityName, categoryName);
            }

//            if(crawledPageNumber > maxPageNumber)
//                return;
//            System.out.println(maxPageNumber);
//            for (int i = 1; i <= maxPageNumber - crawledPageNumber; i++) {
//                if( i == maxPageNumber - crawledPageNumber)
//                    util.updateMaxPage(maxPageNumber, category);
//                if(handleEachPageInIndustry(webClient, url, i, category)) {
//                    util.updateMaxPage(maxPageNumber, category);
//                    return;
//                }
//
//            }
        }
        catch (IOException ex) {System.out.println(ex.getMessage());}
        //catch (SQLException ex) {System.out.println(ex.getMessage());}
        catch (SQLException e) {
            e.printStackTrace();
        } finally {
            webClient.close();
        }
        System.out.println("x");
    }

    private static void handleEachCityInSpecificIndustry(WebClient webClient, String cityUrl, String cityName, String category) throws IOException, SQLException {
        Integer maxPage = getPaging(webClient, cityUrl);
        for (int i = 1; i <= maxPage; i++)
            handleEachPageInCity(webClient, cityUrl, i, cityName, category);
        System.out.println("max page" + cityUrl + " " + maxPage);
    }

    private static boolean handleEachPageInCity(WebClient webClient, String url, int pageNumber, String cityName, String category) throws IOException, SQLException {
        HtmlPage containerIndustryPage = webClient.getPage(url + "?page=" + pageNumber);
        DomNodeList<DomElement> h2Tags = containerIndustryPage.getElementsByTagName("h2");

        for(DomElement h2 : h2Tags) {
            HtmlHeading2 h2Tag = (HtmlHeading2) h2;
            if (h2Tag.getAttribute("class").equalsIgnoreCase("company_name")){
                Iterable<DomElement> links = h2Tag.getChildElements();
                for(DomElement link : links) {
                    count += 1;
                    String company_link = ((HtmlAnchor) link).getHrefAttribute();
                    getComanyDetailInfo(webClient, company_link, category);
                    System.out.println("downloaded " + count);
                }
            }
        }
        return false;
    }

    private static ArrayList<City> getCitiesForEachIndustry(WebClient webClient, String url) throws IOException {
        HtmlPage containerIndustryPage = webClient.getPage(url);
        List<DomNode> elements = containerIndustryPage.getElementById("newlocnganhnghe").getChildNodes();
        LinkedHashMap<Integer, City> elementsMap = new LinkedHashMap<Integer, City>(); // Makesure order.
        int index = 0;
        int cityIndex = 0;

        for(DomNode element : elements) {
            if (!element.asText().isEmpty()) {
                index += 1;

                try {
                    HtmlAnchor link = (HtmlAnchor) element.getChildNodes().get(0);
                    elementsMap.put(index, new City(link.asText(), link.getHrefAttribute()));
                } catch (Exception e) {
                    elementsMap.put(index, new City(element.asText(), ""));
                }
            }
        }
        for(Map.Entry<Integer, City> entry : elementsMap.entrySet()) {
            if (entry.getValue().CityName.toLowerCase().equalsIgnoreCase("tỉnh/ thành phố")) {
                cityIndex = entry.getKey();
                break;
            }
        }

        ArrayList<City> cityUrls = new ArrayList<City>();
        for(Map.Entry<Integer, City> entry : elementsMap.entrySet()) {
            if (entry.getKey() > cityIndex && !entry.getValue().Url.contains("http:"))
                break;
            if (entry.getKey() > cityIndex && entry.getValue().Url.contains("http:"))
                cityUrls.add(new City(entry.getValue().CityName, entry.getValue().Url));
        }
        return cityUrls;
    }



    private static void getComanyDetailInfo(WebClient webClient, String companyLink, String category) throws IOException, SQLException {
        try {
            String companyId = extractDigitsFromString(companyLink);
            MySQLUtils util = new MySQLUtils();
//            if (util.checkCompanyExists(companyId))
//                return ;
            HtmlPage companyDetailPage = webClient.getPage(companyLink);
            Iterable<DomElement> basic_info = companyDetailPage.getElementById("listing_basic_info").getChildElements();
            String companyName = "";
            String address = "";
            String phone = "";
            String fax = "";
            String hotline = "";
            String[] emailWebsites;
            String email = "";
            String website = "";
            String thitruongchinh = "";
            String masothue = "";

            for (DomElement elem : basic_info) {
                if (elem.getAttribute("class").equalsIgnoreCase("tencongty")) {
                    companyName = elem.asText();
                    System.out.println(elem.asText());
                }
                if (elem.getAttribute("class").equalsIgnoreCase("logo_diachi_xacthuc")) {
                    Iterable<DomElement> diachi_xacthuc = elem.getChildElements();
                    for (DomElement xac_thuc : diachi_xacthuc) {
                        if (xac_thuc.getAttribute("class").equalsIgnoreCase("diachi_chitietcongty")) {
                            List<DomNode> tmp = xac_thuc.getByXPath("//div[@class='diachi_chitiet_li']");
                            for (DomNode d : tmp) {
                                if (d.asText().toLowerCase().contains("đ/c:")) {
                                    address = (((HtmlDivision) d).getFirstElementChild()).getNextSibling().getNextSibling().asText();
                                }
                                if (d.asText().toLowerCase().contains("tel:")) {
                                    phone = (((HtmlDivision) d).getFirstElementChild()).getNextSibling().getNextSibling().asText();
                                }
                                if (d.asText().toLowerCase().contains("fax:")) {
                                    fax = (((HtmlDivision) d).getFirstElementChild()).getNextSibling().getNextSibling().asText();
                                }
                            }
                            //address = xac_thuc.asText().replace("\n", " ").replace("\r", " ");
                            //System.out.println(xac_thuc.asText().replace("\n", " ").replace("\r", " "));
                        }
                    }
                }
                if (elem.getAttribute("class").equalsIgnoreCase("email_website")) {
                    System.out.println(elem.asText().replace("\n", " ").replace("\r", " "));
                    emailWebsites = elem.asText().replace("\n", " ").replace("\r", " ").split("\\s+");
                    if (emailWebsites.length > 0) {
                        email = emailWebsites[0];
                    }
                    if (emailWebsites.length > 1) {
                        website = emailWebsites[1];
                    }
                }
            }
            List<DomElement> divs = companyDetailPage.getByXPath("//div[@class='hosocongty_title']");
            String namthanhlap = "";
            String soluongnhanvien = "";
            String loaihinhcongty = "";
            for (DomElement div : divs) {
                if (div.asText().toLowerCase().contains("mã số thuế:")) {
                    masothue = div.getNextSibling().getNextSibling().asText();
                }
                if (div.asText().toLowerCase().contains("loại hình công ty:")) {
                    loaihinhcongty = div.getNextSibling().getNextSibling().asText();
                }
                if (div.asText().toLowerCase().contains("thị trường chính:")) {
                    thitruongchinh = div.getNextSibling().getNextSibling().asText();
                }
                if (div.asText().toLowerCase().contains("năm thành lập:")) {
                    namthanhlap = div.getNextSibling().getNextSibling().asText();
                }
                if (div.asText().toLowerCase().contains("số lượng nhân viên:")) {
                    soluongnhanvien = div.getNextSibling().getNextSibling().asText();
                }
            }
//            util.insertCompanyDetail(companyName, email, phone, fax, website, masothue, address, namthanhlap, thitruongchinh,
//                    soluongnhanvien, loaihinhcongty, companyLink, companyId);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return ;
    }

    public static String extractDigitsFromString(String url) {
        String pattern = "(\\d)+";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(url);
        if (m.find()) {
            return m.group();
        }
        return "";
    }

    public static boolean isInt(String s)
    {
        try
        {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException ex)
        {
            return false;
        }
    }

    public static Integer getPaging(WebClient webClient, String url) throws IOException, SQLException {
        HtmlPage containerPage = webClient.getPage(url);
        DomElement pagingDiv = containerPage.getElementById("paging");
        Iterable<DomElement> pagingLinks = pagingDiv.getChildElements();
        List<Integer> pageNumber = new ArrayList<Integer>();
        for(DomElement pagingLink: pagingLinks) {
            HtmlAnchor link = (HtmlAnchor) pagingLink;

            if (StringUtils.isNumeric(link.asText())) {
                String href = link.getHrefAttribute();
                if (!extractDigitsFromString(href).equalsIgnoreCase("")) {
                    pageNumber.add(Integer.parseInt(extractDigitsFromString(href)));
                }
            }
        }
        webClient.close();
        return Collections.max(pageNumber);
    }

    private static void handleListIndustry(String industryIndexPageUrl, int pageNumber) throws IOException, SQLException{
        WebClient webClient = new WebClient();
        HtmlPage containerIndustryPage = webClient.getPage(industryIndexPageUrl + "?page=" + pageNumber);
        List<HtmlAnchor> links = containerIndustryPage.getAnchors();
        MySQLUtils util = new MySQLUtils();
        String url = "";
        String category = "";
        for(HtmlAnchor link : links) {
            StyleElement style = link.getStyleMap().get("color");
            if (style != null &&
                    (style.getValue().equalsIgnoreCase("#00C") ||
                    style.getValue().equalsIgnoreCase("#333"))) {
                url = link.getHrefAttribute().toString();
                category = extractDigitsFromString(url);
                System.out.println(url);
                if(!util.checkCategoryExists(category))
                    util.insertCategory(category, url);
            }
        }
    }

    private static void getIndustryName() throws IOException, SQLException {
        WebClient webClient = new WebClient(BrowserVersion.CHROME);
        webClient.getOptions().setJavaScriptEnabled(false);

        try {
            webClient.getOptions().setJavaScriptEnabled(false);
            webClient.getOptions().setThrowExceptionOnScriptError(false);
            String industryIndexPageUrl = "http://trangvangvietnam.com/findex.asp";
            Integer maxPageNumber = getPaging(webClient, industryIndexPageUrl);
            for (int i = 1; i <= maxPageNumber; i++) {
                handleListIndustry(industryIndexPageUrl, i);
            }
            handleIndustries();
        }
        catch (IOException ex) {}
        catch (SQLException ex) {}
        finally {
            webClient.close();
        }

    }
}
