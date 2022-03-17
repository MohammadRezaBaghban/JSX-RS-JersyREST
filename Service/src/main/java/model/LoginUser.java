package model;

import java.util.List;

public class LoginUser {

    public String get_userName() {
        return _userName;
    }

    public void set_userName(String _userName) {
        this._userName = _userName;
    }

    public String get_password() {
        return _password;
    }

    public void set_password(String _password) {
        this._password = _password;
    }

    public List<String> get_roles() {
        return _roles;
    }

    public void set_roles(List<String> _roles) {
        this._roles = _roles;
    }

    private String _userName;
    private String _password;
    private List<String> _roles;


    public LoginUser(String userName, String password, List<String> roles) {
        _userName = userName;
        _password = password;
        _roles = roles;
    }
}
