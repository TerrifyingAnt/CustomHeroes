package jg.actionfigures.server.DTO;

public class UserLoginRequest {

    private String login;
    private String password;

    // Constructors
    public UserLoginRequest() {}

    public UserLoginRequest(String login, String password) {
        this.login = login;
        this.password = password;
    }

    // Getters and setters
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
