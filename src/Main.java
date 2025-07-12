import javax.xml.transform.Source;
import java.sql.SQLOutput;
import java.util.Scanner;
import javax.swing.SwingUtilities;

public class Main {
    private static Scanner scanner = new Scanner(System.in);

    public static void initialize(){
        Admin.getInstance();
        Customer.addCustomers();
        Customer.loadUsersFromFile();
        SwingUtilities.invokeLater(() -> {
            MainGUI.getInstance();
        });
    }

    public static void main(String[] args){
        initialize();

        while (true) {
            System.out.println("\n----------------------");
            System.out.println("Welcome to Byte Me!");
            System.out.println("----------------------");
            System.out.println("1. Login as Admin");
            System.out.println("2. Login as Customer");
            System.out.println("3. Exit");
            System.out.print("\nChoose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    adminLogin();
                    break;
                case 2:
                    customerLogin();
                    break;
                case 3:
                    System.out.println("\nThank you for using Byte Me!");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void adminLogin(){
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        Admin admin = Admin.getInstance();
        if(admin.login(username,password)){
            admin.adminMenu();
        }else{
            System.out.println("Invalid username or password");
        }
    }

    private static void customerLogin(){
        while(true){
            System.out.println("\n1. Login");
            System.out.println("2. Register");
            System.out.println("3. Go Back");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch(choice){
                case 1:
                    System.out.print("\nEnter username: ");
                    String username = scanner.nextLine();
                    System.out.print("Enter password: ");
                    String password = scanner.nextLine();

                    Customer customer = Customer.getCustomer(username);
                    if(customer != null && customer.login(username,password)){
                        customer.getCart().loadCartFromFile();
                        customer.customerMenu();
                        return;
                    } else {
                        System.out.println("Invalid username or password, try again.");
                    }
                    break;
                case 2:
                    System.out.print("Enter new username: ");
                    String newUsername = scanner.nextLine();
                    if(Customer.getCustomer(newUsername) != null){
                        System.out.println("Username already exists, make yourself unique!");
                        break;
                    }
                    System.out.print("Enter new password: ");
                    String newPassword = scanner.nextLine();
                    System.out.print("Enter name: ");
                    String name = scanner.nextLine();

                    Customer newCustomer = new Customer(newUsername, newPassword, name);
                    Customer.registerCustomer(newCustomer);
                    Customer.saveUsersToFile(newCustomer);
                    System.out.println("Registration successful!");
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
}
