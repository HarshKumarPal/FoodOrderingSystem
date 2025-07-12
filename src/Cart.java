import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

public class Cart {
    private Map<MenuItem, Integer> items = new HashMap<>();
    private static final String CART_FILE = "cart_data.txt";
    private String username;

    public Cart(String username){
        this.username = username;
    }

    public boolean addItem(MenuItem item, int quantity){
        if(!item.isAvailable()){
            System.out.println("Sorry, " + item.getName() + " is currently unavailable");
            return false;
        }
        items.put(item, items.getOrDefault(item, 0) + quantity);
        saveCartToFile(username);
        return true;
    }

    public void changeQuantity(MenuItem item, int quantity){
        if(items.containsKey(item)){
            if((quantity <= 0)){
                items.remove(item);
            } else {
                items.put(item, quantity);
            }
            saveCartToFile(username);
        }
    }

    public void removeItem(MenuItem item){
        items.remove(item);
        saveCartToFile(username);
    }

    public double getTotalPrice(){
        double total = 0.0;

        for(Map.Entry<MenuItem, Integer> entry : items.entrySet()){
            MenuItem item = entry.getKey();
            int quantity = entry.getValue();
            total = total + item.getPrice() * quantity;
        }
        return total;
    }

    public void displayCart(){
        if(items.isEmpty()){
            System.out.println("\nCart currently empty.");
        } else{
            System.out.println("\nItems in Cart:");
            items.forEach((item, qty) -> System.out.println(item.getName() + "(Qty: " + qty + ") - " + item.getPrice()*qty + " total"));
        }
    }

    public Map<MenuItem, Integer> getItems(){
        return items;
    }

    public List<Map.Entry<String, Double>> getItemsForCheckout() {
        List<Map.Entry<String, Double>> itemList = new ArrayList<>();
        for (Map.Entry<MenuItem, Integer> entry : items.entrySet()) {
            MenuItem item = entry.getKey();
            int quantity = entry.getValue();
            double totalPriceForItem = item.getPrice() * quantity;
            itemList.add(Map.entry(item.getName(), totalPriceForItem));
        }
        return itemList;
    }

    public void clear(String username){
        items.clear();
        saveCartToFile(username);
    }

    private void saveCartToFile(String username){
        try{
            List<String> existingCarts = new ArrayList<>();
            File file = new File(CART_FILE);

            if(file.exists()){
                try(BufferedReader reader = new BufferedReader(new FileReader(file))){
                    String line;
                    while((line = reader.readLine()) != null){
                        String[] parts = line.split(",");
                        if(!parts[0].equals(username)){
                            existingCarts.add(line);
                        }
                    }
                }
            }
            try(BufferedWriter writer = new BufferedWriter(new FileWriter(CART_FILE))){
                for(String line: existingCarts){
                    writer.write(line);
                    writer.newLine();
                }

                for(Map.Entry<MenuItem, Integer> entry : items.entrySet()){
                    MenuItem item = entry.getKey();
                    writer.write(username + "," + item.getName() + "," + entry.getValue() + "," + item.getPrice() +
                            "," + item.getCategory());
                    writer.newLine();
                }
            }
        } catch(IOException e){
            System.err.println("Error saving cart to file: " + e.getMessage());
        }
    }

    public void loadCartFromFile(){
        items.clear();

        try(BufferedReader reader = new BufferedReader(new FileReader(CART_FILE))){
            String line;
            while((line = reader.readLine()) != null){
                String[] parts = line.split(",");
                if(parts.length == 5 && parts[0].equals(username)){
                    String itemName = parts[1];
                    int quantity = Integer.parseInt(parts[2]);
                    double price = Double.parseDouble(parts[3]);
                    String category = parts[4];

                    MenuItem item = new MenuItem(itemName, price, category, true);
                    items.put(item, quantity);
                }
            }
        } catch(IOException e){
            System.out.println("No previous cart data found.");
        }
    }
}
