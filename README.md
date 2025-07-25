README
------------------------------------------------------------------------------------------------
SoulSpice is a food ordering system designed for a college canteen which allows
students to order food from canteen from the comfort of their hostel rooms
or wherever they are on campus. The admin can manage these orders using
this system and ensure that all the operations run efficiently.
-------------------------------------------------------------------------------------------------
CLASSES AND THEIR USAGE
* User: This class stores common information that is used by both the Admin and
        Customer class.
* Admin: Inherits the User class and is responsible for handling all the admin
         functionalities such as add/remove items, update menu, and manage orders.
* Customer: Inherits the User class and is responsible for handling customer
            functionalities such as browsing menu, managing cart, place orders, and
            leaving reviews:
* Menu/MenuItem: Stores information regarding the food items, such as their name, price,
                 availability, etc.
* Cart: Responsible for managing items a customer wants to buy.
* Order: Responsible for storing order details, such as order Id, total price, customer name,
         items ordered, address details, special requests, order status and whether it's a
         VIP order or not.
* ItemReview: Responsible for allowing customer to leave feedback for specific food items
              and view feedback other customers have left behind for other food items.
--------------------------------------------------------------------------------------------------
USAGE OF OOPS CONCEPTS:
1. Encapsulation: Each class (like Menu, Order, Customer, and Cart) has private data with methods
to access or change it, keeping data safe from direct changes outside the class.

2. Inheritance: The Admin and Customer class both inherit from the User class, meaning that they
have common properties like username and password.

3. Polymorphism: Polymorphism lets us use different versions of methods in the Admin and Customer
classes, which allows each class to handle tasks in its own way.

4. Abstraction: The Cart and Order classes are used to keep the code simple by hiding complex details
and only showing the essential functions.
----------------------------------------------------------------------------------------------------
ASSUMPTIONS
- I have hard-coded the login credentials for admin and customers, so there will be no signup option.

- All customers are treated as regular customers and will be given priority based on their order the
orders come in.

- A customer can have VIP status if they pay an extra fee of Rs.20. They can do this when they are
checking out. The customer will be asked if they want their order to have VIP status and if so, they
will be guided on what the next steps to follow are. They will be given priority in order completion
if the customer opts for VIP status.

- If the customer wants to reorder one of their past orders, they can view it in their order history
and reorder it by putting those items in their cart again by using the other functionalities.

- In the GUI, there will be two screens, one displaying the canteen menu and the other displaying the
pending orders. As changes are being made in the CLI, the GUI will reflect those changes accordingly
automatically.

- For I/O stream management, I have implemented temporary cart storage and managing users by retrieving
their data or registering new users. Previously, I had hard-coded the login credentials for the customers,
but now there is an option for new users to register (make a new username and password) and use the food
ordering system as a customer.

- To do the JUnit testing, run the Testing.java class. If a green tick appears, it means that all the
required testcases have been passed. I have implemented JUnit tests for ordering out-of-stock items and
for invalid login attempts.
