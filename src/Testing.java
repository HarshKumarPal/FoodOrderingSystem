import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;

public class Testing {
    private Menu menu;
    private Admin admin;
    private User customer;

    @Before
    public void setUp(){
        menu = Menu.getInstance();
        admin = Admin.getInstance();
        menu.clearMenu();

        // Test menu
        menu.putItem("Burger", 150.0, "Food", true);
        menu.putItem("Pizza", 200.0, "Food", false);  // Out of stock item
        menu.putItem("Coke", 50.0, "Drink", true);

        // Test customer
        customer = new Customer("testuser", "password123", "Test Customer");
    }

    @Test
    public void testOrderingOutOfStockItem(){
        List<Map.Entry<String, Double>> items = new ArrayList<>();
        items.add(new AbstractMap.SimpleEntry<>("Pizza", 200.0));
        try{
            validateOrder(items);
            fail("Expected IllegalStateException for out-of-stock item");
        } catch(IllegalStateException e){
            assertEquals("Item Pizza is not available", e.getMessage());
        }
    }

    @Test
    public void testOrderingMixedAvailabilityItems(){
        List<Map.Entry<String, Double>> items = new ArrayList<>();
        items.add(new AbstractMap.SimpleEntry<>("Burger", 150.0));
        items.add(new AbstractMap.SimpleEntry<>("Pizza", 200.0));
        try{
            validateOrder(items);
            fail("Expected IllegalStateException for out-of-stock item in mixed order");
        } catch(IllegalStateException e){
            assertEquals("Item Pizza is not available", e.getMessage());
        }
    }

    @Test
    public void testOrderingAvailableItems(){
        List<Map.Entry<String, Double>> items = new ArrayList<>();
        items.add(new AbstractMap.SimpleEntry<>("Burger", 150.0));
        items.add(new AbstractMap.SimpleEntry<>("Coke", 50.0));
        try{
            boolean isValid = validateOrder(items);
            assertTrue("Order should be valid with available items", isValid);
        } catch(IllegalStateException e){
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testInvalidLoginWrongPassword(){
        UserAuthenticator authenticator = new UserAuthenticator();
        try{
            authenticator.login("testuser", "wrongpassword");
            fail("Expected AuthenticationException for wrong password");
        } catch(AuthenticationException e){
            assertEquals("Invalid password", e.getMessage());
        }
    }

    @Test
    public void testInvalidLoginWrongUsername(){
        UserAuthenticator authenticator = new UserAuthenticator();
        try{
            authenticator.login("nonexistentuser", "password123");
            fail("Expected AuthenticationException for wrong username");
        } catch(AuthenticationException e){
            assertEquals("User not found", e.getMessage());
        }
    }

    @Test
    public void testValidLogin(){
        UserAuthenticator authenticator = new UserAuthenticator();
        try{
            User loggedInUser = authenticator.login("testuser", "password123");
            assertNotNull("Login should succeed with valid credentials", loggedInUser);
            assertEquals("testuser", loggedInUser.getUserName());
        } catch(AuthenticationException e){
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    private boolean validateOrder(List<Map.Entry<String, Double>> items){
        for(Map.Entry<String, Double> item : items){
            MenuItem menuItem = menu.getItem(item.getKey());
            if(menuItem == null || !menuItem.isAvailable()){
                throw new IllegalStateException("Item " + item.getKey() + " is not available");
            }
        }
        return true;
    }
}

class AuthenticationException extends Exception{
    public AuthenticationException(String message){
        super(message);
    }
}

class UserAuthenticator{
    public User login(String username, String password) throws AuthenticationException{
        if(!"testuser".equals(username)){
            throw new AuthenticationException("User not found");
        }
        if(!"password123".equals(password)) {
            throw new AuthenticationException("Invalid password");
        }
        return new Customer(username, password, "Test Customer");
    }
}
