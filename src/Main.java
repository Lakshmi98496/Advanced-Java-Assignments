import java.util.*;

public class Main{

    // pageUrl -> visit count
    static HashMap<String, Integer> pageViews = new HashMap<>();

    // pageUrl -> unique visitors
    static HashMap<String, Set<String>> uniqueVisitors = new HashMap<>();

    // traffic source -> count
    static HashMap<String, Integer> trafficSources = new HashMap<>();


    // Process incoming page view event
    public static void processEvent(String url, String userId, String source) {

        // Update page view count
        pageViews.put(url, pageViews.getOrDefault(url, 0) + 1);

        // Track unique visitors
        uniqueVisitors.putIfAbsent(url, new HashSet<>());
        uniqueVisitors.get(url).add(userId);

        // Track traffic source
        trafficSources.put(source, trafficSources.getOrDefault(source, 0) + 1);
    }


    // Get top 10 pages
    public static void getTopPages() {

        List<Map.Entry<String, Integer>> list = new ArrayList<>(pageViews.entrySet());

        list.sort((a, b) -> b.getValue() - a.getValue());

        System.out.println("\nTop Pages:");

        int count = 0;

        for (Map.Entry<String, Integer> entry : list) {

            String page = entry.getKey();
            int views = entry.getValue();
            int unique = uniqueVisitors.get(page).size();

            System.out.println((count + 1) + ". " + page +
                    " - " + views + " views (" + unique + " unique)");

            count++;

            if (count == 10)
                break;
        }
    }


    // Display traffic sources
    public static void showTrafficSources() {

        System.out.println("\nTraffic Sources:");

        for (String source : trafficSources.keySet()) {

            System.out.println(source + " → " + trafficSources.get(source));
        }
    }


    // Dashboard
    public static void getDashboard() {

        getTopPages();
        showTrafficSources();
    }


    public static void main(String[] args) {

        // Simulating incoming events

        processEvent("/article/breaking-news", "user_123", "google");
        processEvent("/article/breaking-news", "user_456", "facebook");
        processEvent("/sports/championship", "user_111", "google");
        processEvent("/sports/championship", "user_222", "direct");
        processEvent("/sports/championship", "user_333", "facebook");
        processEvent("/tech/ai-future", "user_123", "google");
        processEvent("/tech/ai-future", "user_789", "google");
        processEvent("/tech/ai-future", "user_456", "facebook");
        processEvent("/article/breaking-news", "user_999", "direct");
        processEvent("/article/breaking-news", "user_888", "google");

        // Display dashboard
        getDashboard();
    }
}