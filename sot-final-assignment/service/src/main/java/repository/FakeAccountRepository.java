package repository;

import model.UserAccount;

import java.util.*;

public class FakeAccountRepository implements IAccountRepository{

    private static FakeAccountRepository instance = new FakeAccountRepository();

    private Map<String, UserAccount> credentials = new HashMap<String, UserAccount>();
    private List<String> _rolesAll = new ArrayList<>();
    private List<String> _bookKeeper = new ArrayList<>();

    private FakeAccountRepository(){
        _rolesAll.add("ADMIN");
        _rolesAll.add("BOOKKEEPER");
        _bookKeeper.add("BOOKKEEPER");
        credentials.put("mrbhmr@gmail.com",new UserAccount("mrbhmr@gmail.com","1234",_rolesAll));
    }

    public static FakeAccountRepository getInstance() {
        return instance;
    }

    @Override
    public boolean isValidUser(String username, String password) {
        if (credentials.containsKey(username)) {
            var credentialPassword = credentials.get(username);
            return credentialPassword.get_password().equals(password);
        }
        return false;
    }

    @Override
    public boolean isUserAllowed(String username, String password, Set<String> rolesSet) {
        var user = credentials.get(username);
        var userRoles = user.get_roles();
        for (String role : userRoles ) {
            if (_rolesAll.contains(role))
                return true;
        }
        return false;
    }
}
