public class Category
{
    public String Url;
    public String CategoryId;
    public Integer MaxPageNumber;

    public Category(String url, String categoryId, Integer maxPageNumber) {
        Url = url;
        CategoryId = categoryId;
        MaxPageNumber = maxPageNumber;
    }
}
