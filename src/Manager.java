// Inherits user - Manager Class
public class Manager extends User {
    public Manager(String username, String password) {
        super(username, password);
    }

    @Override
    public String getRole() {
        return "manager";
    }
}
