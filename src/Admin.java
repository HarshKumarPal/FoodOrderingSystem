import java.util.*;

public class Admin extends User{
    private static Admin instance;
    private final Menu menu = Menu.getInstance();
    private final Scanner scanner;
    private final PriorityQueue<Order> orders;
    private final Map<String,Integer> itemSales;
    private double totalSales;
    private final List<Order> completedOrders;
    private final List<OrderUpdateListener> orderListerners;

    public interface OrderUpdateListener{
        void onOrdersUpdated();
    }

    private Admin(){
        super("admin123", "pass123", "Admin");
        this.scanner = new Scanner(System.in);
        this.orders = new PriorityQueue<>((o1,o2) -> Boolean.compare(o2.isVip(),o1.isVip()));
        this.itemSales = new HashMap<>();
        this.completedOrders = new ArrayList<>();
        this.totalSales = 0.0;
        this.orderListerners = new ArrayList<>();
    }

    public void addOrderListener(OrderUpdateListener listener){
        orderListerners.add(listener);
    }

    public void removeOrderListener(OrderUpdateListener listener){
        orderListerners.remove(listener);
    }

    private void notifyOrderListeners(){
        for(OrderUpdateListener listener : orderListerners){
            listener.onOrdersUpdated();
        }
    }

    public static Admin getInstance(){
        if(instance == null){
            synchronized(Admin.class){
                if(instance == null){
                    instance = new Admin();
                }
            }
        }
        return instance;
    }

    public Queue<Order> getOrdersQueue(){
        return this.orders;
    }

    public void addOrder(Order order){
        orders.add(order);
        notifyOrderListeners();
    }

    public void adminMenu(){
        while(true){
            System.out.println("\nWelcome, Admin!");
            System.out.println("1. Manage Items");
            System.out.println("2. Manage Orders");
            System.out.println("3. Generate Report");
            System.out.println("4. Logout\n");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    manageMenu();
                    break;
                case 2:
                    manageOrders();
                    break;
                case 3:
                    generateReport();
                    break;
                case 4:
                    System.out.println("Admin logged out.");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void manageMenu(){
        while(true){
            System.out.println("\nManage Items:");
            System.out.println("1. View Current Menu");
            System.out.println("2. Add Item");
            System.out.println("3. Update Item");
            System.out.println("4. Remove Item");
            System.out.println("5. Go Back");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch(choice){
                case 1:
                    menu.displayMenu();
                    break;
                case 2:
                    addItem();
                    break;
                case 3:
                    updateItem();
                    break;
                case 4:
                    removeItem();
                    break;
                case 5:
                    System.out.println("Returning to main menu");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void addItem(){
        System.out.print("\nEnter item name: ");
        String name = scanner.nextLine();

        System.out.print("Enter item price: ");
        double price = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("Enter item category (e.g., Food, Drink): ");
        String category = scanner.nextLine();

        System.out.print("Is the item available? (true/false): ");
        boolean isAvailable = scanner.nextBoolean();
        scanner.nextLine();

        menu.putItem(name, price, category, isAvailable);
        System.out.println("Item added successfully.");
    }

    private void updateItem(){
        System.out.print("\nEnter the name of the item to update: ");
        String name = scanner.nextLine();

        System.out.print("Enter the new price: ");
        double newPrice = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("Enter the new category: ");
        String newCategory = scanner.nextLine();

        System.out.print("Is the item available? (true/false): ");
        boolean newAvailability = scanner.nextBoolean();
        scanner.nextLine();

        menu.updateItem(name, newPrice, newCategory, newAvailability);
        System.out.println("Item updated successfully.");
    }

    private void removeItem(){
        System.out.print("\nEnter the name of the item to remove: ");
        String name = scanner.nextLine();

        menu.removeItem(name);
        System.out.println("Item removed successfully.");
    }

    private void manageOrders(){
        while(true){
            System.out.println("\nManage Orders:");
            System.out.println("1. View Pending Orders");
            System.out.println("2. Update Order Status");
            System.out.println("3. Process Refunds");
            System.out.println("4. Go Back");
            System.out.print("\nEnter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch(choice){
                case 1:
                    viewPendingOrders();
                    break;
                case 2:
                    updateOrderStatus();
                    break;
                case 3:
                    processRefunds();
                    break;
                case 4:
                    System.out.println("Returning to main menu");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void viewPendingOrders(){
        System.out.println("\nPending Orders:");
        if(orders.isEmpty()){
            System.out.println("No pending orders.");
        } else{
            for(Order order: orders){
                System.out.println(order);
            }
        }
    }

    private void updateOrderStatus(){
        if(orders.isEmpty()){
            System.out.println("No order available to update currently.");
            return;
        }
        System.out.print("\nEnter ORDER ID to update: ");
        int orderId = scanner.nextInt();
        scanner.nextLine();

        Order orderToUpdate = findOrderById(orderId);
        if(orderToUpdate != null){
            System.out.println("Current Status: " + orderToUpdate.getOrderStatus());
            System.out.println("Enter new status (PREPARING, DELIVERING, COMPLETED, CANCELLED): ");
            String newStatus = scanner.nextLine().toUpperCase();
            orderToUpdate.setOrderStatus(newStatus);

            if(newStatus.equals("CANCELLED")){
                if(orderToUpdate.canCancel()){
                    orderToUpdate.setOrderStatus(newStatus);
                    if(orderToUpdate.getPaymentMethod().equalsIgnoreCase("upi")){
                        System.out.println("Order cancelled. Refund process has began.");
                    } else{
                        System.out.println("Order cancelled. No refund needed for COD order.");
                    }
                    notifyOrderListeners();
                }
            } else {
                orderToUpdate.setOrderStatus(newStatus);
                System.out.println("Order status updated successfully.");

                if(newStatus.equals("COMPLETED")){
                    markOrderAsCompleted(orderToUpdate);
                } else {
                    notifyOrderListeners();
                }
            }
        } else {
            System.out.println("Order ID not found.");
        }
    }

    private void markOrderAsCompleted(Order order){
        completedOrders.add(order);
        totalSales += order.getTotalAmount();
        List<Map.Entry<String, Double>> orderItems = order.getItems();
        for (Map.Entry<String, Double> item : orderItems){
            String itemName = item.getKey();
            itemSales.put(itemName, itemSales.getOrDefault(itemName,0)+1);
        }
        orders.remove(order);
        System.out.println("Order marked as completed.");
        notifyOrderListeners();
    }

    private Order findOrderById(int orderId){
        for(Order order: orders){
            if(order.getOrderId() == orderId){
                return order;
            }
        }
        return null;
    }

    private void processRefunds(){
        System.out.println("\nPending Refunds:");
        boolean hasRefunds = false;

        List<Order> refundOrders = new ArrayList<>();
        for(Order order: orders){
            if(order.needsRefund()){
                hasRefunds = true;
                System.out.println(order);
                refundOrders.add(order);
            }
        }
        if(!hasRefunds){
            System.out.println("No pending refunds to process.");
            return;
        }
        System.out.print("\nEnter Order ID to process refund: ");
        int orderId = scanner.nextInt();
        scanner.nextLine();

        for(Order order: refundOrders){
            if(order.getOrderId() == orderId){
                System.out.println("Amount to be refunded: Rs. " + order.getTotalAmount());
                System.out.print("Confirm refund process? (yes/no): ");
                String confirm = scanner.nextLine();

                if(confirm.equalsIgnoreCase("yes")){
                    order.processRefund();
                    System.out.println("Refund processed successfully.");
                    orders.remove(order);
                    notifyOrderListeners();
                }else{
                    System.out.println("Refund process cancelled.");
                }
                return;
            }
        }
        System.out.println("Order ID not found or no refund required.");
    }

    private void generateReport() {
        if(completedOrders.isEmpty()){
            System.out.println("No sales made today.");
            return;
        }
        System.out.println("\nDaily Sales Report:");
        System.out.println("Total Sales: " + totalSales);
        System.out.println("Total Orders " + completedOrders.size());

        String popularItem = null;
        int maxSales = 0;
        for(Map.Entry<String, Integer> entry: itemSales.entrySet()){
            if(entry.getValue() > maxSales){
                popularItem = entry.getKey();
                maxSales = entry.getValue();
            }
        }
        System.out.println("Most Popular Item: " + (popularItem != null ? popularItem: "No items sold."));
        System.out.println("\nOrder Details:");
        for(Order order: completedOrders){
            System.out.println(order);
        }
    }


    @Override
    public void logout(){
        System.out.println(name + " has logged out.");

    }
}
