import java.util.List;
import java.util.Map;

public class Order {
    private static int idCounter = 1;
    private final int orderId;
    private final String customerName;
    private String orderStatus;
    private final boolean isVip;
    private final String specialRequest;
    private final List<Map.Entry<String, Double>> items;
    private final double totalAmount;
    private String address;
    private boolean needsRefund;
    private String refundStatus;
    private String paymentMethod;

    public Order(String customerName, boolean isVip, String specialRequest, String address, List<Map.Entry<String, Double>> items, String paymentMethod){
        this.orderId = idCounter++;
        this.customerName = customerName;
        this.orderStatus = "PENDING";
        this.isVip = isVip;
        this.specialRequest = specialRequest;
        this.items = items;
        this.totalAmount = calculateTotal(items);
        this.address = address;
        this.needsRefund = false;
        this.refundStatus = "NONE";
        this.paymentMethod = paymentMethod;
    }

    private double calculateTotal(List<Map.Entry<String, Double>> items){
        double total = 0.0;
        for(Map.Entry<String, Double> item: items){
            total += item.getValue();
        }
        return total;
    }
    public int getOrderId(){return orderId;}
    public String getCustomerName(){return customerName;}
    public String getOrderStatus(){return orderStatus;}
    public boolean isVip(){return isVip;}
    public String getSpecialRequest(){return specialRequest;}
    public void setOrderStatus(String orderStatus){
        this.orderStatus = orderStatus;
        if(orderStatus.equals("CANCELLED")){
            this.needsRefund = true;
            this.refundStatus = "PENDING";
        }
    }
    public double getTotalAmount(){return totalAmount;}
    public List<Map.Entry<String, Double>> getItems(){return items;}
    public String getAddress(){return address;}
    public boolean canCancel(){
        return orderStatus.equals("PENDING") || orderStatus.equals("PREPARING");
    }
    public boolean needsRefund(){return needsRefund;}
    public String getRefundStatus(){return refundStatus;}
    public String getPaymentMethod(){return paymentMethod;}
    public void processRefund(){
        this.refundStatus = "REFUNDED";
        this.needsRefund = false;
    }

    @Override
    public String toString(){
        String result = "\nOrder ID: " + orderId +
                "\nCustomer Name: " + customerName +
                "\nOrder Status: " + orderStatus +
                (isVip ? " (VIP)" : "") + "\nPayment Method: " + paymentMethod +
                "\nSpecial Request: " + specialRequest +
                "\nAddress: " + address +
                "\nTotal Amount: " + totalAmount +
                "\nItems Ordered: ";
        for(Map.Entry<String, Double> item: items){
            result += "\n - " + item.getKey() + ": Rs." + item.getValue();
        }

        if(!refundStatus.equals("NONE")){
            result += "\nRefund Status: " + refundStatus;
        }
        return result;
    }

}
