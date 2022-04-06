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

        var isValidUser = accountRepository.isValidUser(account.get_userName(),account.get_password());
        if(!isValidUser){
            return Response
                    .status(Response.Status.UNAUTHORIZED)
                    .entity("Inavlid username and/or password.")
                    .build();
        } else {
            var jwtToken = accountRepository.generateJwtToken(account.get_userName());
            return Response.status(Response.Status.OK).entity(jwtToken).build();
        }
    }
}
