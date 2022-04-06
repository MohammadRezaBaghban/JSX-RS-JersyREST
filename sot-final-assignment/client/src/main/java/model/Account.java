package model;

import java.util.List;
import java.util.Objects;

public class Account {

    private String username;
    private String password;
    private List<String> roles;

    public Account() {
    }

    public Account(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account that = (Account) o;
        return Objects.equals(username, that.username) && Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password);
    }

    public String get_userName() {
        return username;
    }

    public void set_userName(String username) {
        this.username = username;
    }

    public String get_password() {
        return password;
    }

    public void set_password(String password) {
        this.password = password;
    }

    public List<String> get_roles() {
        return roles;
    }

    public void set_roles(List<String> _roles) {
        this.roles = _roles;
    }

    @Override
    public String toString() {
        return "Account{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", _roles=" + roles +
                '}';
    }
}
