/**
 * Created by cuongnguyen on 4/25/18.
 */
public class City {
    public String CityName;
    public String Url;
    public Integer CrawledNumber;
    public City(String cityName, String url) {
        CityName = cityName;
        Url = url;
    }
    public City(String cityName, String url, Integer crawledNumber) {
        CityName = cityName;
        Url = url;
        CrawledNumber = crawledNumber;
    }
}
