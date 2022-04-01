package resources;

import model.Book;
import repository.FakeBookRepository;
import repository.IBookRepository;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.Collection;

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
    public Response getFirstBook() {
        var book = bookRepository.getBookByIndex(0);
        if (book == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(book).build();
    }

    @GET //GET at http:localhost:XXXX/BookStore/books/first
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBookById(@PathParam("id") int bookId) {
        var book = bookRepository.getById(bookId);
        if (book == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(book).build();
    }

    @GET //Get at http://localhost:XXXX/BookStore/books?name=Calculus I
    //@RolesAllowed({"TEACHER","ADMIN"})
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllBooks(@QueryParam("name") String name) {

        if (!uriInfo.getQueryParameters().containsKey("name")) {
            GenericEntity<Collection<Book>> entity = new GenericEntity<>(bookRepository.getAll()) {
            };
            return Response.ok(entity).build();
        }

        // if query param is present, filter students based on name
        Collection<Book> filtered = bookRepository.filterBooksByName(name);
        if (filtered.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Please provide a valid name").build();
        }

        GenericEntity<Collection<Book>> entity = new GenericEntity<>(filtered) {
        };
        return Response.ok(entity).build();
    }


}
