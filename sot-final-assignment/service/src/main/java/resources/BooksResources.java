package resources;

import repository.FakeBookRepository;
import repository.IBookRepository;

import javax.annotation.security.PermitAll;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

@Path("/books")
public class BooksResources {

    @Context
    private UriInfo uriInfo;
    private IBookRepository bookRepository;

    public BooksResources() {
        this.bookRepository = FakeBookRepository.getInstance();
    }

    @GET //GET at http://localhost:XXXX/BookStore/books/hello
    @Path("/hello")
    @PermitAll
    @Produces(MediaType.TEXT_PLAIN)
    public Response sayHello() {
        String msg = "Hello, your book endpoint works!";
        return Response.status(Response.Status.OK).entity(msg).build();
    }

    @GET //GET at http://localhost:XXXX/BookStore/books/count
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getCount() {
        return Response.ok(bookRepository.count()).build();
    }

    @GET //GET at http:localhost:XXXX/BookStore/books/first
    @Path("first")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFirstStudent() {
        var book = bookRepository.getBookByIndex(0);
        if (book == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(book).build();
    }
}
