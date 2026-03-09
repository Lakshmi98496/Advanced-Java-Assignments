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
import java.util.*;

class TokenBucket {

    int maxTokens;
    double tokens;
    double refillRate; // tokens per second
    long lastRefillTime;

    public TokenBucket(int maxTokens, double refillRate) {
        this.maxTokens = maxTokens;
        this.tokens = maxTokens;
        this.refillRate = refillRate;
        this.lastRefillTime = System.currentTimeMillis();
    }

    // Refill tokens based on elapsed time
    private void refill() {

        long currentTime = System.currentTimeMillis();
        double seconds = (currentTime - lastRefillTime) / 1000.0;

        double tokensToAdd = seconds * refillRate;

        tokens = Math.min(maxTokens, tokens + tokensToAdd);

        lastRefillTime = currentTime;
    }

    // Check if request allowed
    public synchronized boolean allowRequest() {

        refill();

        if (tokens >= 1) {
            tokens--;
            return true;
        }

        return false;
    }

    public int getRemainingTokens() {
        return (int) tokens;
    }
}

public class Main
{
    // clientId -> TokenBucket
    static HashMap<String, TokenBucket> clientBuckets = new HashMap<>();

    static int MAX_REQUESTS = 1000;
    static double REFILL_RATE = 1000.0 / 3600; // tokens per second


    // Check rate limit
    public static void checkRateLimit(String clientId) {

        clientBuckets.putIfAbsent(clientId,
                new TokenBucket(MAX_REQUESTS, REFILL_RATE));

        TokenBucket bucket = clientBuckets.get(clientId);

        if (bucket.allowRequest()) {

            System.out.println("Allowed (" +
                    bucket.getRemainingTokens() +
                    " requests remaining)");

        } else {

            System.out.println("Denied (0 requests remaining, retry later)");
        }
    }


    // Show status
    public static void getRateLimitStatus(String clientId) {

        TokenBucket bucket = clientBuckets.get(clientId);

        if (bucket == null) {
            System.out.println("Client not found");
            return;
        }

        int used = MAX_REQUESTS - bucket.getRemainingTokens();

        System.out.println("{used: " + used +
                ", limit: " + MAX_REQUESTS +
                ", remaining: " + bucket.getRemainingTokens() + "}");
    }


    public static void main(String[] args) {

        String client = "abc123";

        // simulate requests
        for (int i = 0; i < 5; i++) {
            checkRateLimit(client);
        }

        getRateLimitStatus(client);
    }
}