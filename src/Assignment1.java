import java.util.*;

public class Assignment1 {

    // HashMap to store username -> userId
    static HashMap<String, Integer> userDatabase = new HashMap<>();

    // HashMap to store username attempt frequency
    static HashMap<String, Integer> attemptFrequency = new HashMap<>();

    // Function to check username availability
    public static boolean checkAvailability(String username) {

        // increase attempt count
        attemptFrequency.put(username,
                attemptFrequency.getOrDefault(username, 0) + 1);

        // check if username already exists
        if (userDatabase.containsKey(username)) {
            return false; // already taken
        }

        return true; // available
    }

    // Function to suggest alternative usernames
    public static List<String> suggestAlternatives(String username) {

        List<String> suggestions = new ArrayList<>();

        // append numbers
        for (int i = 1; i <= 3; i++) {
            String newName = username + i;

            if (!userDatabase.containsKey(newName)) {
                suggestions.add(newName);
            }
        }

        // replace underscore with dot
        String dotName = username.replace("_", ".");

        if (!userDatabase.containsKey(dotName)) {
            suggestions.add(dotName);
        }

        return suggestions;
    }

    // Function to get most attempted username
    public static String getMostAttempted() {

        String most = "";
        int max = 0;

        for (String user : attemptFrequency.keySet()) {

            int count = attemptFrequency.get(user);

            if (count > max) {
                max = count;
                most = user;
            }
        }

        return most + " (" + max + " attempts)";
    }

    public static void main(String[] args) {

        // Pre-existing usernames
        userDatabase.put("john_doe", 1001);
        userDatabase.put("admin", 1002);
        userDatabase.put("alex_23", 1003);

        // Sample checks
        System.out.println("Check username john_doe: " + checkAvailability("john_doe"));
        System.out.println("Check username jane_smith: " + checkAvailability("jane_smith"));

        // Suggestions
        System.out.println("Suggestions for john_doe: " + suggestAlternatives("john_doe"));

        // Simulating many attempts
        for (int i = 0; i < 5; i++) {
            checkAvailability("admin");
        }

        for (int i = 0; i < 3; i++) {
            checkAvailability("john_doe");
        }

        // Most attempted username
        System.out.println("Most attempted username: " + getMostAttempted());
    }
}