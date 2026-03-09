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
    }
}