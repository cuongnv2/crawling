import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;

import java.io.*;
import java.math.BigInteger;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by CuongNguyen on 03/07/2017.
 */
public class HttpUrlConnectionExample {

    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            System.out.println("Nhập folder chứa file và kiểu xử lý (full or daily)");
        }
        String fileFolder = args[0];
        String processingType = args[1];
        Date today = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = dateFormat.format(today);

        File folder = new File(fileFolder + "/" + currentDate);
        if(!folder.exists())
        {
            folder.mkdir();
        }

        downloadFiles(folder.getAbsolutePath());
        processFileInFolder(folder.getAbsolutePath(), processingType, currentDate.replaceAll("-", ""));
    }

    private static void processFileInFolder(String directory, String processingType, String currentDate) throws IOException, SQLException {
        File folder = new File(directory);

        File[] listOfFiles = folder.listFiles();
        int count = 0;
        for (File file : listOfFiles) {
            System.out.println("processing " + file.getName().trim());
            processCsvFile(directory + "/" + file.getName().trim(), file.getName().trim(), processingType, currentDate);
        }
    }

    private static String getDateFromDailyDownload(String url) {
        String date = url.substring(url.indexOf("=") + 1, url.length());
        String[] parts = date.split("-");
        return parts[2] + parts[1] + parts[0];
    }

    private static void downloadDaily() throws IOException, SQLException {
        WebClient webClient = new WebClient();
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        HtmlPage loginPage = webClient.getPage("http://www.cophieu68.vn/account/login.php");
        HtmlForm loginForm = loginPage.getFormByName("frm");

        HtmlSubmitInput loginButton = loginForm.getInputByName("login");
        HtmlTextInput userName = loginForm.getInputByName("username");
        userName.setValueAttribute("vietcuong175@gmail.com");
        HtmlPasswordInput password = loginForm.getInputByName("tpassword");
        password.setValueAttribute("mmmmmm");

        HtmlPage landingPage = loginButton.click();
        landingPage.getWebResponse().getResponseHeaders();

        HtmlPage exportPage = webClient.getPage("http://www.cophieu68.vn/stockdaily.php");

        List<HtmlAnchor> links = exportPage.getAnchors();
        for (HtmlAnchor link : links) {
            // http://www.cophieu68.vn/export/dailyexcel.php?date=11-07-2017
            if (link.getHrefAttribute().contains("dailyexcel.php")) {
                String href = link.getHrefAttribute();
                String fileName = getDateFromDailyDownload(href);
                String fileContent = webClient.getPage(href).getWebResponse().getContentAsString();
                PrintWriter pw = new PrintWriter(new File("D:\\X\\cophieu68\\daily\\" + fileName + ".csv"));
                pw.write(fileContent);
                pw.close();
                processDailyCsvFileOld("D:\\X\\cophieu68\\daily\\" + fileName + ".csv", fileName);
                break;
            }
        }
        webClient.close();
    }

    private static void processDailyCsvFileOld(String filePath, String fileName) throws SQLException, IOException {
        String line = "";
        String cvsSplitBy = ",";

        BufferedReader br = new BufferedReader(new FileReader(filePath));
        MySQLUtils util = new MySQLUtils();
        int count = 0;
        int companyId = 0;
        while ((line = br.readLine()) != null) {
            if (count > 0) { // Read from 2nd record
                // use comma as separator
                String[] share = line.split(cvsSplitBy);
                companyId = util.getCompanyId(share[0].trim());
                if(companyId > 0) {
                    util.insertDailyShare(companyId,
                            Integer.parseInt(share[1]), // session
                            Double.parseDouble(share[2]), // open
                            Double.parseDouble(share[3]), // high
                            Double.parseDouble(share[4]), // low
                            Double.parseDouble(share[5]), // close
                            new BigInteger(share[6]) // volume
                    );
                }
            }
            count += 1;
        }
    }

    private static void downloadFiles(String folder) throws IOException, SQLException {
        WebClient webClient = new WebClient();
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        HtmlPage loginPage = webClient.getPage("http://www.cophieu68.vn/account/login.php");
        HtmlForm loginForm = loginPage.getFormByName("frm");

        HtmlSubmitInput loginButton = loginForm.getInputByName("login");
        HtmlTextInput userName = loginForm.getInputByName("username");
        userName.setValueAttribute("vietcuong175@gmail.com");
        HtmlPasswordInput password = loginForm.getInputByName("tpassword");
        password.setValueAttribute("mmmmmm");

        HtmlPage landingPage = loginButton.click();
        landingPage.getWebResponse().getResponseHeaders();

        HtmlPage exportPage = webClient.getPage("http://www.cophieu68.vn/export.php");
        List<HtmlAnchor> links = exportPage.getAnchors();
        int count = 1;
        for (HtmlAnchor link : links) {
            if (link.getHrefAttribute().contains("excelfull.php")) {
                String href = link.getHrefAttribute();
                String fileName = href.substring(24, href.length()).replace(".csv", "");
                if (fileName.contains("^")) continue;
                String fileContent = webClient.getPage("http://www.cophieu68.vn/" + href).getWebResponse().getContentAsString();
                PrintWriter pw = new PrintWriter(new File(folder + "/" + fileName + ".csv"));
                pw.write(fileContent);
                pw.close();
                System.out.println("downloaded " + count);
                count +=1;
                //processCsvFile("D:\\X\\cophieu68\\full\\" + fileName + ".csv", fileName);
            }
        }
        webClient.close();
    }
    private static void processCsvFile(String filePath, String fileName, String processingType, String currentDate) throws IOException, SQLException {
            String line = "";
            String cvsSplitBy = ",";

            BufferedReader br = new BufferedReader(new FileReader(filePath));
            MySQLUtils util = new MySQLUtils();
            int count = 0;
            int companyId = util.getCompanyId(fileName.trim().replace(".csv", ""));
            while ((line = br.readLine()) != null) {
                if (count > 0) { // Read from 2nd record
                    // use comma as separator
                    String[] share = line.split(cvsSplitBy);
                    if (companyId == 0)
                        companyId = util.insertCompany(fileName.trim().replace(".csv", ""));

//                    if (processingType.equalsIgnoreCase("daily") && count > 1) return;
//                    if (processingType.equalsIgnoreCase("daily") && count == 1 && !share[1].equalsIgnoreCase(currentDate)) return;
//                    //if (processingType.equalsIgnoreCase("daily") && count == 2 && !share[1].equalsIgnoreCase("20180404")) return;
                    if (processingType.equalsIgnoreCase("daily") && count > 2) return;
                    if (processingType.equalsIgnoreCase("daily") && count == 1 && !share[1].equalsIgnoreCase(currentDate)) return;
                    if (processingType.equalsIgnoreCase("daily") && count == 2 && !share[1].equalsIgnoreCase("20180426")) return;
                    util.insertShare(companyId,
                            Integer.parseInt(share[1]), // session
                            Double.parseDouble(share[2]), // open_fix
                            Double.parseDouble(share[3]), // high_fix
                            Double.parseDouble(share[4]), // low_fix
                            Double.parseDouble(share[5]), // close_fix
                            new BigInteger(share[6]), // volume
                            Double.parseDouble(share[7]),// open
                            Double.parseDouble(share[8]),// high
                            Double.parseDouble(share[9]),// low
                            Double.parseDouble(share[10]),// close
                            Integer.parseInt(share[11]),// volume_deal
                            Integer.parseInt(share[12]),// volume_fb
                            Integer.parseInt(share[13])// volume_fs
                    );
                }
                count += 1;
            }
        }
}
