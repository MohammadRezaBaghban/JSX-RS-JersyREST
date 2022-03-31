package resources;

import repository.FakeBookRepository;
import repository.BookRepository;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

import java.awt.*;
import java.net.URI;
import java.util.Collection;
import java.util.ResourceBundle;

@Path("/books")
public class BooksResources {

    @Context
    private UriInfo uriInfo;
    private BookRepository bookRepository;

    public BooksResources() {
        //this.studentRepository = FakeBookRepository.getInstance();
    }


    @GET //GET at http://localhost:XXXX/books/hello
    @Path("/hello")
    @PermitAll
    @Produces(MediaType.TEXT_PLAIN)
    public Response sayHello() {
        String msg = "Hello, your book endpoint works!";
        return Response.status(Response.Status.OK).entity(msg).build();
    }

}
