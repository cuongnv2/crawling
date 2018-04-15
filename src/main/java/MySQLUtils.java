import java.math.BigInteger;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by CuongNguyen on 05/07/2017.
 */
public class MySQLUtils {
//    String url = "jdbc:mysql://127.0.0.1:3306/cp";
//    //String url = "jdbc:mysql://127.0.0.1:3306/crawling";
//    String driver = "com.mysql.jdbc.Driver";
//    String username = "root";
//    String password = "123@123a";

    String url = "jdbc:mysql://127.0.0.1:3306/crawling";
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

    public int insertCategory(String category_id, String url_category) throws SQLException {
        Connection connection = DriverManager.getConnection(url, username, password);
        try {
            Class.forName(driver);
            String sql ="INSERT category(category_id, url) values('" + category_id+ "', '" + url_category+ "')";
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
                                    String namthanhlap, String thitruongchinh, String soluongnhanvien, String loaihinhconty, String link, String companyId) throws SQLException {
        address = address.replaceAll("'", "");
        Connection connection = DriverManager.getConnection(url, username, password);
        try {
            Class.forName(driver);
            String sql ="INSERT company_detail(name, email, phone, fax, website, ma_so_thue, address, nam_thanh_lap, thi_truong_chinh, so_luong_nhan_vien, loai_hinh_cong_ty, url, company_id) " +
                    "values('" + companyName + "', '" + email + "', '" + phone
                    + "', '" + fax
                    + "', '" + website + "', '" + masothue
                    + "', '" + address
                    + "', '" + namthanhlap
                    + "', '" + thitruongchinh
                    + "', '" + soluongnhanvien
                    + "', '" + loaihinhconty
                    + "', '" + link
                    + "', '" + companyId +"' )";
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

    public boolean checkCompanyExists(String companyId) throws SQLException {
        Connection con = DriverManager.getConnection(url, username, password);
        try {
            PreparedStatement ps =
                    con.prepareStatement
                            ("SELECT 1 FROM company_detail WHERE company_id = ?");
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
            String sql ="SELECT url,category_id, max_page FROM category where max_page=0";
            PreparedStatement statement =
                    connection.prepareStatement(sql);
            ResultSet result = statement.executeQuery();
            while (result.next())
                 categories.add(new Category(result.getString(1), result.getString(2), result.getInt(3)));

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
}
