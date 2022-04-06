package repository;

import model.Account;

import java.util.List;
import java.util.Set;

public interface IAccountRepository {

    boolean isUserExist(String username);

    boolean isValidUser(String username, String password);

    boolean isUserAllowed(String username, Set<String> rolesSet);

    String generateJwtToken(String username);

    List<String> get_bookKeeper();

    Account add(Account account);

    String getSecret();

}
