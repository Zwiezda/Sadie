package pl.techdra.api;

import java.awt.*;
import java.util.Date;

public class PMSUser {
    private String userID = "";
    private String userLogin = "";
    private String firstName = "";
    private String lastName = "";
    private String email = "";
    private Date lastLogged = null;
    private Date created = null;
    private Image avatar = null;

    public PMSUser() {
    }

    public PMSUser(String userLogin, String firstName, String lastName, String email) {
        this.userLogin = userLogin;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public String getUserID() { return userID; }

    public void setUserID(String userID) { this.userID = userID; }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() { return lastName; }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getLastLogged() {
        return lastLogged;
    }

    public void setLastLogged(Date lastLogged) {
        this.lastLogged = lastLogged;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Image getAvatar() {
        return avatar;
    }

    public void setAvatar(Image avatar) {
        this.avatar = avatar;
    }
}
