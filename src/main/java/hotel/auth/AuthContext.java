package hotel.auth;

public class AuthContext {

    private static AuthContext instance;
    private Role currentRole;

    private AuthContext() {
        currentRole = Role.USER;
    }

    public static AuthContext getInstance() {
        if (instance == null) {
            instance = new AuthContext();
        }
        return instance;
    }

    public Role getCurrentRole() {
        return currentRole;
    }

    public void setCurrentRole(Role role) {
        this.currentRole = role;
    }
}