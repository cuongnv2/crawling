import java.math.BigInteger;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by CuongNguyen on 05/07/2017.
 */
public class MySQLUtils {
//    String url = "jdbc:mysql://127.0.0.1:3306/cp";
     String url = "jdbc:mysql://127.0.0.1:3306/crawling";
//    String driver = "com.mysql.jdbc.Driver";
//    String username = "root";
//    String password = "123@123a";

    //String url = "jdbc:mysql://127.0.0.1:3306/crawling?useUnicode=yes&characterEncoding=UTF-8";
    String driver = "com.mysql.jdbc.Driver";
    String username = "root";
    String password = "123456";

    public void insertShare(int companyId,
                            int session,
                            Double open_fix,
                            Double high_fix,
                            Double low_fix,
                            Double close_fix,
                            BigInteger volume,
                            Double open,
                            Double high,
                            Double low,
                            Double close,
                            int volume_deal,
                            int volume_fb,
                            int volume_fs

    ) throws SQLException {
        Connection connection = DriverManager.getConnection(url, username, password);
        try {
            Class.forName(driver);
            String sql ="INSERT share(company_id, session, open_fix, high_fix, low_fix, close_fix, volume,open,high, low, close, volume_deal, volume_fb, volume_fs ) " +
                    " values(" + companyId + "," + session + "," + open_fix +
                    "," + high_fix
                    + "," + low_fix
                    + "," + close_fix
                    + "," + volume
                    + "," + open
                    + "," + high
                    + "," + low
                    + "," + close
                    + "," + volume_deal
                    + "," + volume_fb
                    + "," + volume_fs +
                    ")";
            PreparedStatement statement =
                    connection.prepareStatement(sql);
            statement.execute();

        }
        catch (SQLException e) {
            throw e;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally  {    connection.close(); }
    }

    public void insertDailyShare(int companyId,
                            int session,
                            Double open,
                            Double high,
                            Double low,
                            Double close,
                            BigInteger volume

    ) throws SQLException {
        Connection connection = DriverManager.getConnection(url, username, password);
        try {
            Class.forName(driver);
            String sql ="INSERT share(company_id, session, open,high, low, close, volume ) " +
                    " values(" + companyId + "," + session
                    + "," + open
                    + "," + high
                    + "," + low
                    + "," + close
                    + "," + volume +
                    ")";
            System.out.println(sql);
            PreparedStatement statement =
                    connection.prepareStatement(sql);
            statement.execute();

        }
        catch (SQLException e) {
            throw e;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally  {    connection.close(); }
    }

    public int getCompanyId(String name) throws SQLException {
        Connection connection = DriverManager.getConnection(url, username, password);
        try {
            Class.forName(driver);
            String sql ="SELECT id FROM company WHERE name='" + name + "'";
            PreparedStatement statement =
                    connection.prepareStatement(sql);
            ResultSet result = statement.executeQuery();
            while (result.next())
                return result.getInt(1);

        }
        catch (SQLException e) {
            throw e;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally  {    connection.close(); }
        return 0;
    }

    public int insertCompany(String companyName) throws SQLException {
        Connection connection = DriverManager.getConnection(url, username, password);
        try {
            Class.forName(driver);
            String sql ="INSERT company(name) values('" + companyName+ "')";
            PreparedStatement statement =
                    connection.prepareStatement(sql);
            statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = statement.getGeneratedKeys();
            rs.next();
            return rs.getInt(1);
        }
        catch (SQLException e) {
            throw e;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally  {    connection.close(); }
        return 0;
    }

    public int insertCompany2(String companyName) throws SQLException {
        Connection connection = DriverManager.getConnection(url, username, password);
        try {
            Class.forName(driver);
            String sql ="INSERT company2(name) values('" + companyName+ "')";
            PreparedStatement statement =
                    connection.prepareStatement(sql);
            int companyId = statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
            return companyId;
        }
        catch (SQLException e) {
            throw e;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally  {    connection.close(); }
        return 0;
    }

    public int insertCategory(String category_id, String url_category, String name) throws SQLException {
        Connection connection = DriverManager.getConnection(url, username, password);
        try {
            Class.forName(driver);
            String sql ="INSERT category(category_id, url, name) values('" + category_id+ "', '" + url_category  + "', '" + name +"')";
            PreparedStatement statement =
                    connection.prepareStatement(sql);
            int categoryId = statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
            return categoryId;
        }
        catch (SQLException e) {
            throw e;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally  {    connection.close(); }
        return 0;
    }

    public void insertCompanyDetail(String companyName,String email, String phone, String fax, String website, String masothue, String address,
                                    String namthanhlap, String thitruongchinh, String soluongnhanvien, String loaihinhconty, String link,
                                    String companyId, String category, String cityName, String nguoi_lien_he, String chuc_vu, String di_dong,
                                    String email_lh, String dien_thoai) throws SQLException {
        address = address.replaceAll("'", "");
        Connection connection = DriverManager.getConnection(url, username, password);
        try {
            Class.forName(driver);
            String sql ="INSERT company_detail(name, email, phone, fax, website, ma_so_thue, address, nam_thanh_lap, " +
                    "thi_truong_chinh, so_luong_nhan_vien, loai_hinh_cong_ty, url, company_id, category, city, " +
                    "nguoi_lien_he, chuc_vu, di_dong, email_lh, dien_thoai) " +
                    "values('" + companyName + "', '" + email + "', '" + phone
                    + "', '" + fax
                    + "', '" + website + "', '" + masothue
                    + "', '" + address
                    + "', '" + namthanhlap
                    + "', '" + thitruongchinh
                    + "', '" + soluongnhanvien
                    + "', '" + loaihinhconty
                    + "', '" + link
                    + "', '" + companyId
                    + "', '" + category
                    + "', '" + cityName
                    + "', '" + nguoi_lien_he
                    + "', '" + chuc_vu
                    + "', '" + di_dong
                    + "', '" + email_lh
                    + "', '" + dien_thoai
                    +"' )";
            PreparedStatement statement =
                    connection.prepareStatement(sql);
            statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
        }
        catch (SQLException e) {
            throw e;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally  {    connection.close(); }

    }

    public void insertVinaBizCompanyDetail(String ten_chinh_thuc, String ten_giao_dich, String ma_so_thue, String ngay_cap,
                                           String co_quan_thue, String ngay_bat_dau_hoat_dong, String trang_thai,
                                           String dia_chi_tru_so, String nganh_nghe, String company_id, String cityName) throws SQLException {
        Connection con = DriverManager.getConnection(url, username, password);
        String query = "INSERT INTO vinabiz_company_detail(ten_chinh_thuc, ten_giao_dich, ma_so_thue, ngay_cap, " +
                "co_quan_thue, ngay_bat_dau_hoat_dong, trang_thai, dia_chi_tru_so, nganh_nghe, company_id, city_name) " +
                "values(?, ?, ?, ?, ?, ?, ? , ?, ?, ?, ?)";
        try {
            PreparedStatement ps =
                    con.prepareStatement
                            (query);
            ps.setString(1, ten_chinh_thuc);
            ps.setString(2, ten_giao_dich);
            ps.setString(3, ma_so_thue);
            ps.setString(4, ngay_cap);
            ps.setString(5, co_quan_thue);
            ps.setString(6, ngay_bat_dau_hoat_dong);
            ps.setString(7, trang_thai);
            ps.setString(8, dia_chi_tru_so);
            ps.setString(9, nganh_nghe);
            ps.setString(10, company_id);
            ps.setString(11, cityName);

            ps.execute();
        }
        catch (SQLException e) { throw e; }
        finally  {    con.close(); }
    }



    public boolean checkCategoryExists(String category) throws SQLException {
        Connection con = DriverManager.getConnection(url, username, password);
        try {
            PreparedStatement ps =
                    con.prepareStatement
                            ("SELECT 1 FROM category WHERE category_id = ?");
            ps.setString(1, category);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return true;
            } else {
                return false;
            }
        }
        catch (SQLException e) { throw e; }
        finally  {    con.close(); }
    }

    public boolean checkCompanyExists(String companyId, String tableName) throws SQLException {
        Connection con = DriverManager.getConnection(url, username, password);
        try {
            PreparedStatement ps =
                    con.prepareStatement
                            ("SELECT 1 FROM " + tableName + " WHERE company_id = ?");
            ps.setString(1, companyId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return true;
            } else {
                return false;
            }
        }
        catch (SQLException e) { throw e; }
        finally  {    con.close(); }
    }

    public ArrayList<Category> getAllIndustries() throws SQLException {

        Connection connection = DriverManager.getConnection(url, username, password);
        ArrayList<Category> categories = new ArrayList<Category>();
        try {
            Class.forName(driver);
            String sql ="SELECT url,category_id, max_page, name FROM category where id  > 137"; // 128, 137 need recrawl
            PreparedStatement statement =
                    connection.prepareStatement(sql);
            ResultSet result = statement.executeQuery();
            while (result.next())
                 categories.add(new Category(result.getString(1), result.getString(2)
                         , result.getInt(3), result.getString(4)));

        }
        catch (SQLException e) {
            throw e;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally  {    connection.close(); }
        return categories;
    }


    public void updateMaxPage(Integer maxPageNumber, String category) throws SQLException {
        Connection connection = DriverManager.getConnection(url, username, password);
        try {
            Class.forName(driver);
            String sql ="update category set max_page=" + maxPageNumber + " where category_id='" + category + "'";
            PreparedStatement statement =
                    connection.prepareStatement(sql);
            statement.execute(sql);
        }
        catch (SQLException e) {
            throw e;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally  {    connection.close(); }
    }

    public ArrayList<Company> getCompanies() throws SQLException {

        Connection connection = DriverManager.getConnection(url, username, password);
        ArrayList<Company> companies = new ArrayList<Company>();
        try {
            Class.forName(driver);
            String sql ="SELECT id, url FROM company_detail where id> 9996";
            PreparedStatement statement =
                    connection.prepareStatement(sql);
            ResultSet result = statement.executeQuery();
            while (result.next())
                companies.add(new Company(result.getInt(1), result.getString(2)));
        }
        catch (SQLException e) {
            throw e;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally  {    connection.close(); }
        return companies;
    }

    public ArrayList<City> getCities() throws SQLException {

        Connection connection = DriverManager.getConnection(url, username, password);
        ArrayList<City> cities = new ArrayList<City>();
        try {
            Class.forName(driver);
            String sql ="SELECT name, url, crawled_page FROM city";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet result = statement.executeQuery();
            while (result.next())
                cities.add(new City(result.getString(1), result.getString(2), result.getInt(3)));
        }
        catch (SQLException e) {
            throw e;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally  {    connection.close(); }
        return cities;
    }

    public void updateCompanyDetail(Integer id, String nguoi_lien_he, String chuc_vu, String di_dong,
                                    String email, String dien_thoai) throws SQLException {
        Connection connection = DriverManager.getConnection(url, username, password);
        try {
            Class.forName(driver);
            String sql ="update company_detail set nguoi_lien_he=?, chuc_vu=?, di_dong=?, email=?, dien_thoai=? where id=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, nguoi_lien_he);
            ps.setString(2, chuc_vu);
            ps.setString(3, di_dong);
            ps.setString(4, email);
            ps.setString(5, dien_thoai);
            ps.setInt(6, id);
            ps.executeUpdate();
        }
        catch (SQLException e) {
            throw e;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally  {    connection.close(); }
    }

    public void insertCity(String name, String cityUrl) throws SQLException {
        Connection connection = DriverManager.getConnection(url, username, password);
        try {
            Class.forName(driver);
            String sql ="INSERT city(name, url) values(?, ?)";
            PreparedStatement ps =
                    connection.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, cityUrl);
            ps.execute();
        }
        catch (SQLException e) {
            throw e;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally  {    connection.close(); }
    }

    public void updateCrawledPageNumber(String cityUrl, int pageNumber) throws SQLException {
        Connection connection = DriverManager.getConnection(url, username, password);
        try {
            Class.forName(driver);
            String sql ="UPDATE city SET crawled_page=? where url=?";
            PreparedStatement ps =
                    connection.prepareStatement(sql);
            ps.setInt(1, pageNumber);
            ps.setString(2, cityUrl);
            ps.execute();
        }
        catch (SQLException e) {
            throw e;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally  {    connection.close(); }
    }
}
