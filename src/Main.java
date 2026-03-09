import java.util.*;


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
}