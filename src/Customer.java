import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.ArrayList;

public class Customer extends User{
    private static Map<String, Customer> customers = new HashMap<>();
    private final Menu menu = Menu.getInstance();
    private final Scanner scanner = new Scanner(System.in);
    private boolean isVip = false;
    private Cart cart;
    private String username;
    private final List<Order> orderHistory = new ArrayList<>();

    public Customer(String username, String password, String name){
        super(username, password, name);
        this.cart = new Cart(username);
        this.username = username;
        cart.loadCartFromFile();
        this.isVip = false;
    }

    public Cart getCart(){
        return cart;
    }

    public void setVipStatus(boolean status){
        this.isVip = status;
    }

    public boolean isVip(){
        return this.isVip;
    }

    public String getPassword(){
        return password;
    }

    public static void addCustomers(){
        Customer ram = new Customer("ram123", "pass123", "Ram");
        ram.setVipStatus(true);
        customers.put("ram123", ram);

        Customer samhita = new Customer("samhita123", "pass123", "Samhita");
        samhita.setVipStatus(true);
        customers.put("samhita123", samhita);
    }

    public static Customer getCustomer(String username){
        return customers.get(username);
    }

    public void customerMenu(){
        while(true){
            System.out.println("\nWelcome, " + name + "!");
            System.out.println("1. Browse Menu");
            System.out.println("2. View Cart");
            System.out.println("3. Track Order");
            System.out.println("4. Item Reviews");
            System.out.println("5. Logout\n");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch(choice){
                case 1:
                    browseMenu();
                    break;
                case 2:
                    viewCart();
                    break;
                case 3:
                    trackOrder();
                    break;
                case 4:
                    itemReview();
                    break;
                case 5:
                    System.out.println("Customer logged out.");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void browseMenu(){
        while(true){
            System.out.println("\nBrowse Menu:");
            System.out.println("1. View All Items");
            System.out.println("2. Search Items");
            System.out.println("3. Filter by Category");
            System.out.println("4. Sort by Price");
            System.out.println("5. Return to Main Menu");
            System.out.print("Enter your choice: ");
            int menuChoice = scanner.nextInt();

            switch(menuChoice){
                case 1:
                    menu.displayMenu();
                    break;
                case 2:
                    searchItem();
                    break;
                case 3:
                    filterByCategory();
                    break;
                case 4:
                    sortByPrice();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void searchItem(){
        System.out.print("\nEnter item name or keyword to search: ");
        scanner.nextLine();
        String keyword = scanner.nextLine().toLowerCase();
        List<MenuItem> results = menu.searchItems(keyword);

        if(results.isEmpty()){
            System.out.println("No items found with keyword " + keyword);
        } else {
            System.out.println("Search Results:");
            for(MenuItem item : results){
                System.out.println(item);
            }
        }
    }

    private void filterByCategory(){
        System.out.print("\nEnter category (e.g. Food, Drink): ");
        scanner.nextLine();
        String category = scanner.nextLine();
        List<MenuItem> filteredItems = menu.filterItemsByCategory(category);

        if(filteredItems.isEmpty()){
            System.out.println("No items found within category " + category);
        } else {
            System.out.println("Items found within the " + category + " category:");
            for(MenuItem item : filteredItems){
                System.out.println(item);
            }
        }
    }

    private void sortByPrice(){
        System.out.print("\nSort by price (1. Ascending, 2. Descending): ");
        int sortOrder = scanner.nextInt();
        List<MenuItem> sortedItems = menu.sortItemsByPrice(sortOrder == 1);

        System.out.println("Menu Sorted by Price:");
        for(MenuItem item : sortedItems){
            System.out.println(item);
        }
    }

    private void viewCart(){
        while(true){
            cart.displayCart();
            System.out.println("\n1. Add Item");
            System.out.println("2. Modify Quantity");
            System.out.println("3. Remove Item");
            System.out.println("4. View Total");
            System.out.println("5. Checkout");
            System.out.println("6. Return to Main Menu");
            System.out.print("\nEnter your choice: ");
            int choice = Integer.parseInt(scanner.nextLine());

            switch(choice){
                case 1:
                    addItemToCart();
                    break;
                case 2:
                    modifyQuantity();
                    break;
                case 3:
                    removeCartItem();
                    break;
                case 4:
                    System.out.println("\nTotal Price: " + cart.getTotalPrice());
                    break;
                case 5:
                    checkout();
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void addItemToCart() {
        System.out.print("Enter item name to add: ");
        String itemName = scanner.nextLine();

        List<MenuItem> results = menu.searchItems(itemName);
        if(results.isEmpty()){
            System.out.println("Item not found.");
            return;
        }
        MenuItem item = results.get(0);
        if(!item.isAvailable()){
            System.out.println("Sorry, " + itemName + " is currently unavailable.");
            return;
        }
        System.out.print("Enter quantity: ");
        int quantity = Integer.parseInt(scanner.nextLine());
        if(cart.addItem(item, quantity)){
            System.out.println("Added " + quantity + " " + itemName + " to cart.");
        }
    }

    private void modifyQuantity(){
        System.out.print("Enter item name to modify quantity: ");
        String itemName = scanner.nextLine();
        MenuItem item = null;

        for(MenuItem cartItem : cart.getItems().keySet()){
            if(cartItem.getName().equalsIgnoreCase(itemName)){
                item = cartItem;
                break;
            }
        }

        if(item == null) {
            System.out.println("Item not in cart.");
            return;
        }
        System.out.print("Enter new quantity: ");
        int quantity = Integer.parseInt(scanner.nextLine());
        cart.changeQuantity(item, quantity);
        System.out.println("Updated quantity for " + itemName + " to " + quantity);
    }

    private void removeCartItem(){
        System.out.print("Enter item name to remove: ");
        String itemName = scanner.nextLine();
        MenuItem item = null;

        for(MenuItem cartItem : cart.getItems().keySet()){
            if(cartItem.getName().equalsIgnoreCase(itemName)){
                item = cartItem;
                break;
            }
        }

        if(item == null){
            System.out.println("Item not in cart.");
            return;
        }
        cart.removeItem(item);
        System.out.println("Removed " + itemName + " from cart.");
    }

    private void checkout(){
        if(cart.getItems().isEmpty()){
            System.out.println("Cart is empty! Nothing to checkout.");
            return;
        }

        boolean allItemsAvailable = true;
        StringBuilder unavailableItems = new StringBuilder();
        for(Map.Entry<MenuItem, Integer> entry: cart.getItems().entrySet()){
            MenuItem item = entry.getKey();
            if(!item.isAvailable()){
                allItemsAvailable = false;
                unavailableItems.append("\n- ").append(item.getName());
            }
        }
        if(!allItemsAvailable){
            System.out.println("Some items in your cart are no longer available: " + unavailableItems.toString());
            System.out.println("Please remove these items from your cart before proceeding.");
            return;
        }

        System.out.println("Checkout:");
        double total = cart.getTotalPrice();
        System.out.println("Total amount: Rs. " + total);

        boolean isVipOrder = false;
        if(this.isVip){
            System.out.println("Would you like to have VIP priority for this order? It would require an extra payment of Rs.20, proceed? (yes/no): ");
            String response = scanner.nextLine();
            if(response.equalsIgnoreCase("yes")){
                isVipOrder = true;
                total += 20;
                System.out.println("New total with VIP priority: Rs. " + total);
            }
        }

        System.out.print("Add special request (if any): ");
        String specialRequest = scanner.nextLine();

        System.out.print("Enter Address Details: ");
        String address = scanner.nextLine();

        System.out.print("Proceed to payment? (yes/no): ");
        String response = scanner.nextLine();

        List<Map.Entry<String,Double>> items = (List<Map.Entry<String, Double>>) cart.getItemsForCheckout();
        if(response.equalsIgnoreCase("yes")){
            System.out.print("COD or UPI? (cash/upi): ");
            response = scanner.nextLine();

            if(response.equalsIgnoreCase("cash")){
                System.out.println("Order has been placed. Pay in cash when order is delivered.");
                System.out.println("------------------------------------------------------------");
                Order order = new Order(name, isVipOrder, specialRequest, address, items, "cash");
                Admin.getInstance().addOrder(order);
                orderHistory.add(order);
                System.out.println(order);
                cart.clear(userName);
            } else if(response.equalsIgnoreCase("upi")){
                System.out.println("Enter UPI Id: ");
                String upiId = scanner.nextLine();
                System.out.println("Processing payment...");
                System.out.println("Payment successful. Order has been placed.");
                System.out.println("------------------------------------------");
                Order order = new Order(name, isVipOrder, specialRequest, address, items, "upi");
                Admin.getInstance().addOrder(order);
                orderHistory.add(order);
                System.out.println(order);
                cart.clear(userName);
            } else{
                System.out.println("Invalid, please try again.");
            }
        } else {
            System.out.println("Checkout cancelled.");
        }
    }

    private void trackOrder(){
        while(true){
            System.out.println("\nTrack Order:");
            System.out.println("1. View Order Status");
            System.out.println("2. Cancel Order");
            System.out.println("3. View Order History");
            System.out.println("4. Return to Main Menu");
            System.out.print("\nEnter your choice: ");
            int choice = Integer.parseInt(scanner.nextLine());

            switch(choice){
                case 1:
                    viewOrderStatus();
                    break;
                case 2:
                    cancelOrder();
                    break;
                case 3:
                    viewOrderHistory();
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void viewOrderStatus(){
        if(orderHistory.isEmpty()){
            System.out.println("No orders to track.");
            return;
        }
        for(Order order: orderHistory){
            System.out.println("Order ID: " + order.getOrderId() + " - Status: " + order.getOrderStatus());
        }
    }

    private void cancelOrder(){
        System.out.print("\nEnter Order ID to cancel: ");
        int orderId = Integer.parseInt(scanner.nextLine());

        for(Order order: orderHistory){
            if(order.getOrderId() == orderId){
                if(order.canCancel()){
                order.setOrderStatus("CANCELLED");
                    System.out.println("Order ID " + orderId + " has been cancelled.");
                } else {
                    System.out.println("Order ID " + orderId + " cannot be cancelled. It has already been processed/prepared.");
                }
                return;
            }
        }
        System.out.println("Order ID not found.");
    }

    private void viewOrderHistory(){
        if(orderHistory.isEmpty()){
            System.out.println("No pas orders found.");
            return;
        }
        System.out.println("Order History:");
        for(Order order: orderHistory){
            System.out.println(order);
        }
    }

    private void itemReview(){
        while(true){
            System.out.println("\nItem Reviews:");
            System.out.println("1. Provide Review");
            System.out.println("2. View Item Reviews");
            System.out.println("3. Return to Main Menu");
            System.out.print("Enter your choice: ");
            int reviewChoice = scanner.nextInt();
            scanner.nextLine();

            switch(reviewChoice){
                case 1:
                    provideReview();
                    break;
                case 2:
                    viewReview();
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void provideReview(){
        System.out.print("Enter name of item to review: ");
        String itemName = scanner.nextLine();
        System.out.print("Enter review for " + itemName + ": ");
        String review = scanner.nextLine();

        ItemReview.addReview(itemName, review);
        System.out.println("Thank you for your review!");
    }

    private void viewReview(){
        System.out.print("Enter name of item to view reviews of: ");
        String itemName = scanner.nextLine();

        List<String> itemReviews = ItemReview.getReviews(itemName);
        if(itemReviews.isEmpty()){
            System.out.println("No reviews available for " + itemName);
        } else {
            System.out.println("Reviews for " + itemName + ":");
            for(String review: itemReviews){
                System.out.println("- " + review);
            }
        }
    }

    public static void loadUsersFromFile(){
        try(BufferedReader reader = new BufferedReader(new FileReader("users_data.txt"))){
            String line;
            while((line = reader.readLine()) != null){
                String[] parts = line.split(",");
                if(parts.length == 4){
                    String username = parts[0];
                    String password = parts[1];
                    String name = parts[2];
                    boolean isVip = Boolean.parseBoolean(parts[3]);
                    Customer customer = new Customer(username, password, name);
                    customer.setVipStatus(isVip);
                    customers.put(username, customer);
                }
            }
        } catch(IOException e){
            System.out.println("Error reading users from file: " + e.getMessage());
        }
    }

    public static void saveUsersToFile(Customer customer){
        try(BufferedWriter writer = new BufferedWriter(new FileWriter("users_data.txt", true))){
            String userData = customer.getUserName() + "," + customer.getPassword() + "," + customer.getName() + "," + customer.isVip();
            writer.write(userData);
            writer.newLine();
        } catch(IOException e){
            System.out.println("Error saving user to file: " + e.getMessage());
        }
    }

    public static void registerCustomer(Customer customer){
        customers.put(customer.getUserName(), customer);
    }
}
