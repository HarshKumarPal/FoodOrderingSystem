public abstract class User {
    protected String userName;
    protected String password;
    protected String name;
    protected boolean isAdmin;

    public User(String userName, String password, String name){
        this.userName = userName;
        this.password = password;
        this.name = name;
        this.isAdmin = isAdmin;
    }

    public String getUserName(){return userName;}
    public String getName(){return name;}
    public boolean isAdmin(){return isAdmin;}
    public boolean checkPassword(String password){
        return this.password.equals(password);
    }

    public boolean login(String username, String password){
        return this.userName.equals(username) && this.password.equals(password);
    }
    public void logout(){
        System.out.println(getName() + " has been logged out.");
    }
}
