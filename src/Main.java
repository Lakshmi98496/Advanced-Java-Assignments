import java.util.*;

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