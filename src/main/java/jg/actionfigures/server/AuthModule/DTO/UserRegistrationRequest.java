package jg.actionfigures.server.AuthModule.DTO;


// * Класс передачи информации о пользователе для регистрации
public class UserRegistrationRequest {

    private String name;
    private String login;
    private String password;
    private String phoneNumber;
    private String avatarSourcePath;
    private String type;


    public UserRegistrationRequest() {}

    public UserRegistrationRequest(String name, String login, String password, String phoneNumber, String avatarSourcePath, String type) {
        this.name = name;
        this.login = login;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.avatarSourcePath = avatarSourcePath;
        this.type = type;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAvatarSourcePath() {
        return avatarSourcePath;
    }

    public void setAvatarSourcePath(String avatarSourcePath) {
        this.avatarSourcePath = avatarSourcePath;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}