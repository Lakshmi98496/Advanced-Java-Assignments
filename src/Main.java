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
=======

public class Main{

    // n-gram size
    static int N = 5;

    // HashMap<n-gram, Set<DocumentId>>
    static HashMap<String, Set<String>> ngramIndex = new HashMap<>();

    // Store documents
    static HashMap<String, String> documents = new HashMap<>();


    // Generate n-grams
    public static List<String> generateNGrams(String text) {

        String[] words = text.toLowerCase().split("\\s+");

        List<String> ngrams = new ArrayList<>();

        for (int i = 0; i <= words.length - N; i++) {

            StringBuilder gram = new StringBuilder();

            for (int j = 0; j < N; j++) {
                gram.append(words[i + j]).append(" ");
            }

            ngrams.add(gram.toString().trim());
        }

        return ngrams;
    }


    // Add document to system
    public static void addDocument(String docId, String text) {

        documents.put(docId, text);

        List<String> grams = generateNGrams(text);

        for (String gram : grams) {

            ngramIndex.putIfAbsent(gram, new HashSet<>());

            ngramIndex.get(gram).add(docId);
        }
    }


    // Analyze document for plagiarism
    public static void analyzeDocument(String docId) {

        String text = documents.get(docId);

        List<String> grams = generateNGrams(text);

        HashMap<String, Integer> matchCount = new HashMap<>();


        for (String gram : grams) {

            if (ngramIndex.containsKey(gram)) {

                for (String otherDoc : ngramIndex.get(gram)) {

                    if (!otherDoc.equals(docId)) {

                        matchCount.put(otherDoc,
                                matchCount.getOrDefault(otherDoc, 0) + 1);
                    }
                }
            }
        }

        System.out.println("Analyzing Document: " + docId);
        System.out.println("Extracted " + grams.size() + " n-grams\n");

        for (String doc : matchCount.keySet()) {

            int matches = matchCount.get(doc);

            double similarity = (matches * 100.0) / grams.size();

            System.out.println("Found " + matches +
                    " matching n-grams with \"" + doc + "\"");

            System.out.println("Similarity: " +
                    String.format("%.2f", similarity) + "%");

            if (similarity > 60) {
                System.out.println("⚠ PLAGIARISM DETECTED\n");
            } else if (similarity > 15) {
                System.out.println("Suspicious similarity\n");
            } else {
                System.out.println("Low similarity\n");
            }
        }
    }


    public static void main(String[] args) {

        String essay1 = "Artificial intelligence is transforming the world with advanced machine learning techniques";
        String essay2 = "Artificial intelligence is transforming the world using powerful machine learning algorithms";
        String essay3 = "Climate change is one of the biggest challenges facing humanity today";

        addDocument("essay_089.txt", essay1);
        addDocument("essay_092.txt", essay2);
        addDocument("essay_123.txt", essay3);

        analyzeDocument("essay_123.txt");
=======
class DNSEntry {
    String domain;
    String ipAddress;
    long expiryTime;

    DNSEntry(String domain, String ipAddress, int ttlSeconds) {
        this.domain = domain;
        this.ipAddress = ipAddress;
        this.expiryTime = System.currentTimeMillis() + (ttlSeconds * 1000);
    }

    boolean isExpired() {
        return System.currentTimeMillis() > expiryTime;
    }
}

public class Main {

    // Cache size limit
    static final int MAX_CACHE_SIZE = 5;

    // LRU Cache using LinkedHashMap
    static LinkedHashMap<String, DNSEntry> cache =
            new LinkedHashMap<String, DNSEntry>(MAX_CACHE_SIZE, 0.75f, true) {

                protected boolean removeEldestEntry(Map.Entry<String, DNSEntry> eldest) {
                    return size() > MAX_CACHE_SIZE;
                }
            };

    static int cacheHits = 0;
    static int cacheMisses = 0;

    // Simulated upstream DNS query
    public static String queryUpstreamDNS(String domain) {

        // In real system DNS server would be called
        String ip = "172.217." + new Random().nextInt(255) + "." + new Random().nextInt(255);

        System.out.println("Querying upstream DNS for " + domain);

        return ip;
    }

    // Resolve domain
    public static String resolve(String domain) {

        DNSEntry entry = cache.get(domain);

        if (entry != null && !entry.isExpired()) {

            cacheHits++;
            System.out.println("Cache HIT → " + entry.ipAddress);

            return entry.ipAddress;
        }

        if (entry != null && entry.isExpired()) {
            System.out.println("Cache EXPIRED for " + domain);
            cache.remove(domain);
 main
        }

        cacheMisses++;

        String ip = queryUpstreamDNS(domain);

        DNSEntry newEntry = new DNSEntry(domain, ip, 5); // TTL = 5 seconds

        cache.put(domain, newEntry);

        System.out.println("Cache MISS → Stored " + ip);

        return ip;
    }

    // Show cache statistics
    public static void getCacheStats() {

        int total = cacheHits + cacheMisses;

        double hitRate = (total == 0) ? 0 : ((double) cacheHits / total) * 100;

        System.out.println("\nCache Statistics:");
        System.out.println("Cache Hits: " + cacheHits);
        System.out.println("Cache Misses: " + cacheMisses);
        System.out.println("Hit Rate: " + String.format("%.2f", hitRate) + "%");
    }

    public static void main(String[] args) throws InterruptedException {

        resolve("google.com");
        resolve("facebook.com");

        // Cache hit
        resolve("google.com");

        // Wait for TTL to expire
        System.out.println("\nWaiting for TTL expiry...");
        Thread.sleep(6000);

        // Expired entry
        resolve("google.com");

        // More requests
        resolve("amazon.com");
        resolve("youtube.com");
        resolve("openai.com");
        resolve("github.com"); // triggers LRU eviction

        getCacheStats();

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

public class Main {

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

public class Main {

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

public class Main {

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

public class Main {

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