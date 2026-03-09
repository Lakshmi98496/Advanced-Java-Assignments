import java.util.*;

public class Assignment1 {

    // productId -> stock count
    static HashMap<String, Integer> inventory = new HashMap<>();

    // waiting list (productId -> list of users)
    static HashMap<String, LinkedList<Integer>> waitingList = new HashMap<>();

    // Check stock availability
    public static int checkStock(String productId) {

        if (inventory.containsKey(productId)) {
            return inventory.get(productId);
        }

        return 0;
    }

    // Purchase item (thread safe)
    public synchronized static void purchaseItem(String productId, int userId) {

        int stock = inventory.getOrDefault(productId, 0);

        if (stock > 0) {

            stock--;
            inventory.put(productId, stock);

            System.out.println("User " + userId +
                    " purchase SUCCESS. Remaining stock: " + stock);

        } else {

            waitingList.putIfAbsent(productId, new LinkedList<>());

            waitingList.get(productId).add(userId);

            int position = waitingList.get(productId).size();

            System.out.println("User " + userId +
                    " added to waiting list. Position #" + position);
        }
    }

    // Display waiting list
    public static void showWaitingList(String productId) {

        if (!waitingList.containsKey(productId)) {
            System.out.println("No waiting list.");
            return;
        }

        System.out.println("Waiting list for " + productId + ": "
                + waitingList.get(productId));
    }

    public static void main(String[] args) {

        String product = "IPHONE15_256GB";

        // initial stock
        inventory.put(product, 100);

        System.out.println("Stock Available: " + checkStock(product));

        // simulate purchases
        for (int i = 1; i <= 105; i++) {

            purchaseItem(product, i);
        }

        // show waiting list
        showWaitingList(product);
    }
}