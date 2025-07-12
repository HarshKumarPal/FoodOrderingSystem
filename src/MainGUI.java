import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class MainGUI implements Menu.MenuUpdateListener, Admin.OrderUpdateListener{
    private static JTable menuTable;
    private static JTable ordersTable;
    private static DefaultTableModel ordersModel;
    private static Admin admin = Admin.getInstance();
    private static Menu menu = Menu.getInstance();
    private static MainGUI instance;

    public static MainGUI getInstance(){
        if(instance == null){
            instance = new MainGUI();
        }
        return instance;
    }

    private MainGUI(){
        admin.addOrderListener(this);
        menu.addUpdateListener(this);
        createAndShowGUI();
        refreshOrdersTable();
    }

    private void createAndShowGUI(){
        JFrame frame = new JFrame("Byte Me!");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        CardLayout cardLayout = new CardLayout();
        JPanel cardPanel = new JPanel(cardLayout);

        JPanel menuPage = createMenuPage();
        cardPanel.add(menuPage, "Menu");

        JPanel ordersPage = createOrdersPage();
        cardPanel.add(ordersPage, "Orders");

        JPanel navPanel = new JPanel();
        navPanel.setBackground(new Color(80, 0, 80));
        navPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JButton menuButton = createStyledButton("Menu");
        JButton ordersButton = createStyledButton("Pending Orders");

        menuButton.addActionListener(e -> cardLayout.show(cardPanel, "Menu"));
        ordersButton.addActionListener(e -> cardLayout.show(cardPanel, "Orders"));

        navPanel.add(menuButton);
        navPanel.add(ordersButton);

        frame.setLayout(new BorderLayout());
        frame.add(navPanel, BorderLayout.NORTH);
        frame.add(cardPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private static JButton createStyledButton(String text){
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setBackground(new Color(150, 50, 150));
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter(){
            public void mouseEntered(java.awt.event.MouseEvent evt){
                button.setBackground(new Color(180, 70, 180));
            }
            public void mouseExited(java.awt.event.MouseEvent evt){
                button.setBackground(new Color(150, 50, 150));
            }
        });
        return button;
    }

    private static JPanel createMenuPage(){
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(240, 230, 250));
        JLabel label = new JLabel("Canteen Menu", SwingConstants.CENTER);
        label.setFont(new Font("SansSerif", Font.BOLD, 24));
        label.setForeground(new Color(80, 0, 80));

        String[] columnNames = {"Item Name", "Price", "Category", "Availability"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0){
            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };
        menuTable = new JTable(model);
        styleTable(menuTable);
        JScrollPane scrollPane = new JScrollPane(menuTable);
        panel.add(label, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        refreshMenuTable();
        return panel;
    }

    private static JPanel createOrdersPage(){
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(240, 230, 250));
        JLabel label = new JLabel("Pending Orders", SwingConstants.CENTER);
        label.setFont(new Font("SansSerif", Font.BOLD, 24));
        label.setForeground(new Color(80, 0, 80));

        String[] columnNames = {"Order ID", "Customer", "Items", "Total Amount", "Status", "VIP", "Special Requests"};
        ordersModel = new DefaultTableModel(columnNames, 0){
            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };
        ordersTable = new JTable(ordersModel);
        styleTable(ordersTable);
        JScrollPane scrollPane = new JScrollPane(ordersTable);
        panel.add(label, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private static void styleTable(JTable table){
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.setRowHeight(25);

        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
        headerRenderer.setBackground(new Color(150, 50, 150));
        headerRenderer.setForeground(Color.WHITE);
        headerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        for(int i = 0; i < table.getColumnCount(); i++){
            table.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
        }

        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
        cellRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        table.setDefaultRenderer(Object.class, cellRenderer);
    }

    private static void refreshMenuTable(){
        if(menuTable == null) return;
        DefaultTableModel model = (DefaultTableModel) menuTable.getModel();
        model.setRowCount(0);

        List<MenuItem> allItems = new ArrayList<>();
        for(List<MenuItem> categoryItems : menu.getAllItems().values()){
            allItems.addAll(categoryItems);
        }
        for(MenuItem item : allItems){
            model.addRow(new Object[]{
                    item.getName(), item.getPrice(), item.getCategory(), item.isAvailable() ? "Yes" : "No"
            });
        }
    }

    private static void refreshOrdersTable(){
        if(ordersModel == null) return;
        ordersModel.setRowCount(0);
        Queue<Order> orders = admin.getOrdersQueue();
        for (Order order : orders){
            String itemsSummary = order.getItems().stream()
                    .map(Map.Entry::getKey)
                    .reduce((a, b) -> a + ", " + b)
                    .orElse("");
            ordersModel.addRow(new Object[]{
                    order.getOrderId(),
                    order.getCustomerName(),
                    itemsSummary,
                    String.format("%.2f", order.getTotalAmount()),
                    order.getOrderStatus(),
                    order.isVip() ? "Yes" : "No",
                    order.getSpecialRequest()
            });
        }
    }

    @Override
    public void onMenuUpdated() {
        SwingUtilities.invokeLater(() -> refreshMenuTable());
    }

    @Override
    public void onOrdersUpdated() {
        SwingUtilities.invokeLater(() -> refreshOrdersTable());
    }
}
