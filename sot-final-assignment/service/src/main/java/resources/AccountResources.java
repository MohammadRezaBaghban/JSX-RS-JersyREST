package resources;


import model.Account;
import repository.FakeAccountRepository;
import repository.IAccountRepository;

import javax.annotation.security.PermitAll;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;


@Path("/auth")
public class AccountResources {

    private final IAccountRepository accountRepository;
    @Context
    private UriInfo uriInfo;

    public AccountResources() {
        accountRepository = FakeAccountRepository.getInstance();
    }

    @GET //GET at http://localhost:XXXX/BookStore/auth/hello
    @Path("/hello")
    @PermitAll
    @Produces(MediaType.TEXT_PLAIN)
    public Response sayHello() {
        String msg = "Hello, your auth endpoint works!";
        return Response.status(Response.Status.OK).entity(msg).build();
    }

    @POST
    @PermitAll
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(Account account) {

        var isValidUser = accountRepository.isValidUser(account.get_userName(), account.get_password());
        if (!isValidUser) {
            return Response
                    .status(Response.Status.UNAUTHORIZED)
                    .entity("Inavlid username and/or password.")
                    .build();
        } else {
            var jwtToken = accountRepository.generateJwtToken(account.get_userName());
            return Response.status(Response.Status.OK).entity(jwtToken).build();
        }
    }

    @POST
    @PermitAll
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response register(Account account) {
        var username = account.get_userName();
        var password = account.get_password();
        var userExist = accountRepository.isUserExist(username);
        if (userExist) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("Given username exist." + "\nPlease try to login")
                    .build();
        } else {
            if (password.isEmpty() || password.length() < 4) {
                return Response.status(Response.Status.BAD_REQUEST).entity("Password can not be empty or less than 4 characters").build();
            } else {
                account.set_roles(accountRepository.get_bookKeeper());
                accountRepository.add(account);
                return Response.status(Response.Status.OK).entity("Username has been registered successfully. Please login.").build();
            }
        }
    }
}
