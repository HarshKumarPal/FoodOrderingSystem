import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

public class ItemReview {
    private static Map<String, List<String>> reviews = new HashMap<>();

    public static void addReview(String itemName, String review){
        reviews.putIfAbsent(itemName, new ArrayList<>());
        reviews.get(itemName).add(review);
    }

    public static List<String> getReviews(String itemName){
        return reviews.getOrDefault(itemName, new ArrayList<>());
    }
}
