package repository;

import model.Account;

import java.util.Set;

public interface IAccountRepository {

    boolean isValidUser(String username, String password);
    boolean isUserAllowed(String username, String password, Set<String> rolesSet);
    String generateJwtToken(String username);
    Account add(Account account);
    String getSecret();
}
