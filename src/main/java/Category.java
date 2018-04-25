public class Category
{
    public String Url;
    public String CategoryId;
    public Integer MaxPageNumber;
    public String CategoryName;

    public Category(String url, String categoryId, Integer maxPageNumber, String categoryName) {
        Url = url;
        CategoryId = categoryId;
        MaxPageNumber = maxPageNumber;
        CategoryName = categoryName;
    }
}
