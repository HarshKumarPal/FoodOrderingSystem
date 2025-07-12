import java.util.*;

public class Menu{
    private TreeMap<String, List<MenuItem>> items = new TreeMap<>();
    private static Menu instance = null;
    public Menu(){
        initializeMenu();
    }
    private List<MenuUpdateListener> listeners = new ArrayList<>();

    public interface MenuUpdateListener{
        void onMenuUpdated();
    }

    public void addUpdateListener(MenuUpdateListener listener){
        listeners.add(listener);
    }

    public void removeUpdateListener(MenuUpdateListener listener){
        listeners.remove(listener);
    }

    private void notifyListeners(){
        for (MenuUpdateListener listener : listeners){
            listener.onMenuUpdated();
        }
    }

    public static Menu getInstance(){
        if(instance == null){
            instance = new Menu();
        }
        return instance;
    }

    private void initializeMenu(){
        if(items.isEmpty()){
            putItem("Samosa", 10, "Food", true);
            putItem("Maggi", 25, "Food", true);
            putItem("Pav Bhaji", 50, "Food", true);
            putItem("Pizza", 100, "Food", true);
            putItem("Burger", 50, "Food", true);
            putItem("Cold Coffee", 30, "Drink", true);
            putItem("Tea", 10, "Drink", true);
            putItem("Masala Tea", 25, "Drink", true);
            putItem("Water", 20, "Drink", true);
        }
    }

    public void putItem(String name, double price, String category, boolean isAvailable){
        MenuItem item = new MenuItem(name, price, category, isAvailable);
        items.computeIfAbsent(category, k -> new ArrayList<>()).add(item);
        items.get(category).sort(Comparator.comparingDouble(MenuItem::getPrice));
        notifyListeners();
    }

    public void updateItem(String itemName, double newPrice,  String newCategory, boolean newAvailability){
        MenuItem itemToUpdate = null;
        String currentCategory = null;

        for (Map.Entry<String, List<MenuItem>> entry : items.entrySet()){
            for (MenuItem item : entry.getValue()){
                if (item.getName().equalsIgnoreCase(itemName)){
                    itemToUpdate = item;
                    currentCategory = entry.getKey();
                    break;
                }
            }
            if (itemToUpdate != null) break;
        }

        if (itemToUpdate != null){
            items.get(currentCategory).remove(itemToUpdate);

            MenuItem updatedItem = new MenuItem(itemName, newPrice, newCategory, newAvailability);
            items.computeIfAbsent(newCategory, k -> new ArrayList<>()).add(updatedItem);
            items.get(newCategory).sort(Comparator.comparingDouble(MenuItem::getPrice));

            System.out.println(itemName + " has been updated.");
        } else {
            System.out.println("Item not found in the menu.");
        }
    }

    public void removeItem(String itemName){
        for(Map.Entry<String, List<MenuItem>> entry : items.entrySet()){
            entry.getValue().removeIf(item -> item.getName().equalsIgnoreCase(itemName));
        }
        System.out.println(itemName + " removed from menu.");
        notifyListeners();
    }

    public void displayMenu(){
        if(items.isEmpty()){
            System.out.println("Nothing in on the menu today.");
        } else{
            System.out.println("\nToday's Menu:");
            for(Map.Entry<String, List<MenuItem>> entry : items.entrySet()){
                System.out.println("Category: " + entry.getKey());
                for(MenuItem item : entry.getValue()){
                    System.out.println(" " + item);
                }
            }
        }
    }

    public List<MenuItem> searchItems(String keyword){
        List<MenuItem> matchingItems = new ArrayList<>();

        for(List<MenuItem> categoryItems : items.values()){
            for(MenuItem item : categoryItems) {
                if(item.getName().toLowerCase().contains(keyword.toLowerCase())) {
                    matchingItems.add(item);
                }
            }
        }
        return matchingItems;
    }

    public List<MenuItem> filterItemsByCategory(String category){
        List<MenuItem> filteredItems = new ArrayList<>();
        if (items.containsKey(category)){
            filteredItems.addAll(items.get(category));
        }
        return filteredItems;
    }

    public List<MenuItem> sortItemsByPrice(boolean ascending){
        List<MenuItem> allItems = new ArrayList<>();
        for(List<MenuItem> categoryItems: items.values()){
            allItems.addAll(categoryItems);
        }
        allItems.sort((item1,item2) -> ascending ? Double.compare(item1.getPrice(), item2.getPrice()) : Double.compare(item2.getPrice(), item1.getPrice()));
        return allItems;
    }

    public Map<String, List<MenuItem>> getAllItems(){
        return items;
    }

    public void clearMenu(){
        Map<String, List<MenuItem>> items = getAllItems();
        items.clear();
    }

    public MenuItem getItem(String name){
        Map<String, List<MenuItem>> items = getAllItems();
        for(List<MenuItem> categoryItems : items.values()){
            for(MenuItem item : categoryItems){
                if(item.getName().equals(name)){
                    return item;
                }
            }
        }
        return null;
    }
}
