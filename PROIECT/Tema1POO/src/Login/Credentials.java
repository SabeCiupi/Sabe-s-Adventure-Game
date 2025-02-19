package Login;

public class Credentials {
    private final String email;
    private final String password;

    // Constructor
    public Credentials(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Getters
    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    // Method to display the email
    @Override
    public String toString() {
        return "Email: " + email;
    }
}
