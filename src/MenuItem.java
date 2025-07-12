import java.io.Serializable;

public class MenuItem implements Serializable{
    private String name;
    private double price;
    private String category;
    private boolean isAvailable;

    public MenuItem(String name, double price, String category, boolean isAvailable){
        this.name = name;
        this.price = price;
        this.category = category;
        this.isAvailable = isAvailable;
    }

    public String getName() {return name;}
    public double getPrice() {return price;}
    public String getCategory() {return category;}
    public boolean isAvailable() {return isAvailable;}
    public void setPrice(double price) {this.price = price;}
    public void setAvailability(boolean isAvailable) {this.isAvailable = isAvailable;}

    @Override
    public boolean equals(Object obj){
        if(this == obj){
            return true;
        }
        if(obj == null || this.getClass() != obj.getClass()){
            return false;
        }
        MenuItem menuItem = (MenuItem) obj;
        return Double.compare(menuItem.price, price) == 0 && name.equals(menuItem.name) && category.equals(menuItem.category);
    }

    @Override
    public int hashCode(){
        return name.hashCode() + category.hashCode() + Double.hashCode(price);
    }
    public String toString(){
        return "Item: " + name + ", Price: " + price + ", Category: " + category + ", Available: " + (isAvailable ? "Yes":"No");
    }
}
