package repository;

import model.Account;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;

import io.jsonwebtoken.*;

import java.util.Date;

import io.jsonwebtoken.Jwts;

import java.util.*;

public class FakeAccountRepository implements IAccountRepository{

    private static FakeAccountRepository instance = new FakeAccountRepository();
    private final String SECRET_KEY = "oeRaYY7Wo24sDqKSX3IM9ASGmdGPmkTd9jo1QTy4b7P9Ze5_9hKolVX8xNrQDcNRfVEdTZNOuOyqEGhXEbdJI-ZQ19k_o9MI0y3eZN2lp9jow55FfXMiINEdt1XR85VipRLSOkT6kSpzs2x-jbLDiz9iFVzkd81YKxMgPA7VfZeQUm4n-mOmnWMaVX30zGFU4L3oPBctYKkl4dYfqYWqRNfrgPJVi5DGFjywgxx0ASEiJHtV72paI3fDR2XwlSkyhhmY-ICjCRmsJN4fX1pdoL8a18-aQrvyu4j0Os6dVPYIoPvvY0SAZtWYKHfM15g7A3HD4cVREf9cUsprCRK93w";
    private final Map<String, Account> credentials = new HashMap<String, Account>();
    private List<String> _rolesAll = new ArrayList<>();
    private List<String> _bookKeeper = new ArrayList<>();

    private FakeAccountRepository(){
        _rolesAll.add("ADMIN");
        _rolesAll.add("BOOKKEEPER");
        _bookKeeper.add("BOOKKEEPER");
        var firstAccount = new Account("mrbhmr@gmail.com","1234");
        firstAccount.set_roles(_rolesAll);
        add(firstAccount);
    }

    public static FakeAccountRepository getInstance() {
        return instance;
    }

    @Override
    public String getSecret() {return SECRET_KEY;}

    @Override
    public boolean isUserExist(String username) {
        return credentials.containsKey(username.toLowerCase(Locale.ROOT));
    }

    @Override
    public List<String> get_bookKeeper() {
        return _bookKeeper;
    }

    @Override
    public boolean isValidUser(String username, String password) {
        if (isUserExist(username)) {
            var credentialPassword = credentials.get(username);
            return credentialPassword.get_password().equals(password);
        }
        return false;
    }

    @Override
    public boolean isUserAllowed(String username, Set<String> rolesSet) {
        var user = credentials.get(username);
        var userRoles = user.get_roles();
        for (String role : userRoles ) {
            if (rolesSet.contains(role))
                return true;
        }
        return false;
    }

    @Override
    public String generateJwtToken(String username) {
        //Sample method to construct a JWT
        String tokenId = UUID.randomUUID().toString();
        String issuer = "sot";
        String subject = "practical";
        long totalMillis = 1000000; // token is valid during this time (milliseconds)

        //The JWT signature algorithm we will be using to sign the token
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        //We will sign our JWT with our ApiKey secret
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECRET_KEY);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        //Let's set the JWT Claims
        JwtBuilder builder = Jwts.builder().setId(tokenId)
                .setIssuedAt(now)
                .setSubject(subject)
                .setIssuer(issuer)
                .claim("username", username) // one private claim has a key [String] and a value [object]
                .claim("date", now)  // this is an example that you can set multiple private claims
                .signWith(signatureAlgorithm, signingKey);

        //if it has been specified, let's add the expiration
        if (totalMillis >= 0) {
            long expMillis = nowMillis + totalMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }

        //Builds the JWT and serializes it to a compact, URL-safe string
        String jwtToken = builder.compact();
        return  jwtToken;
    }

    @Override
    public Account add(Account account) {
        credentials.put(account.get_userName().toLowerCase(Locale.ROOT),account);
        return account;
    }
}
