package jg.actionfigures.server.Models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="user_table")
public class User {
    

    @Id
    @Column(name="user_id")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @Column(name="user_full_name")
    private String name;

    @Column(name="user_login")
    private String login;

    @Column(name="user_password")
    private String password;

    @Column(name="user_phone_number")
    private String phoneNumber;

    @Column(name="user_avatar_source_path")
    private String avatarSourcePath;

    @Column(name="user_type")
    private String type;

    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
        return null;
    }

    
}
