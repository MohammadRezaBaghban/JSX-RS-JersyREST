package resources;

import model.Book;
import repository.FakeBookRepository;
import repository.IBookRepository;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.Collection;
import java.util.stream.Collectors;

@Path("/books")
public class BooksResources {

    private final IBookRepository bookRepository;
    @Context
    private UriInfo uriInfo;

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
    @PermitAll
    @Produces(MediaType.TEXT_PLAIN)
    public Response getCount() {
        return Response.ok(bookRepository.count()).build();
    }

    @GET //GET at http:localhost:XXXX/BookStore/books/first
    @Path("first")
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFirstBook() {
        var book = bookRepository.getBookByIndex(0);
        if (book == null)
            return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok(book).build();
    }

    @GET //GET at http:localhost:XXXX/BookStore/books/first
    @Path("{id}")
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBookById(@PathParam("id") int bookId) {
        var book = bookRepository.getById(bookId);
        if (book == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(book).build();
    }

    @GET //Get at http://localhost:XXXX/BookStore/books?subject=Computer Science
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllBooks(@QueryParam("subject") String name) {

        if (!uriInfo.getQueryParameters().containsKey("subject")) {
            GenericEntity<Collection<Book>> entity = new GenericEntity<>(bookRepository.getAll()) {
            };
            return Response.ok(entity).build();
        }

        // if query param is present, filter students based on name
        Collection<Book> filtered = bookRepository.filterBooksBySubject(name);
        if (filtered.isEmpty())
            return Response.status(Response.Status.BAD_REQUEST).entity("Please provide a valid subject name").build();

        GenericEntity<Collection<Book>> entity = new GenericEntity<>(filtered) {
        };
        return Response.ok(entity).build();
    }

    @GET //GET at http://localhost:XXXX/BookStore/books/Computer Science?x=17
    @Path("search/{subject}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response exampleQueryAndPATHParameters(
            @QueryParam("maxPrice") double maxPrice,
            @PathParam("subject") String p1Subject) {

        var result = bookRepository.filterBooksBySubject(p1Subject);
        result.addAll(bookRepository.getAll().stream()
                .filter(book -> book.getPrice() <= maxPrice)
                .collect(Collectors.toList()));
        var distinctResult = result.stream().distinct().collect(Collectors.toList());

        if (distinctResult.size() > 0)
            return Response.ok(distinctResult).build();
        else
            return Response.status(Response.Status.OK).entity("No book with given filter found").build();
    }

    @DELETE //Delete at http://XXXX/BookStore/Books/3
    @RolesAllowed({"BOOKKEEPER","ADMIN"})
    @Path("{id}")
    public Response deleteBook(@PathParam("id") int bookId) {
        bookRepository.deleteById(bookId);
        return Response.noContent().build();
    }


    @POST //Post at http://localhost:XXXX:/BookStore/books/
    @RolesAllowed({"BOOKKEEPER","ADMIN"})
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response createBook(
            @FormParam("name") String name,
            @FormParam("subject") String subject,
            @FormParam("price") double price
    ) {
        var book = new Book(name, subject, price);
        bookRepository.add(book);

        // url of the created book
        String url = uriInfo.getAbsolutePath() + "/" + book.getId();
        URI uri = URI.create(url);
        return Response.created(uri).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"BOOKKEEPER","ADMIN"})
    public Response create(Book book) {

        Book bookObj = new Book(book.getName(), book.getSubject(), book.getPrice());
        bookRepository.add(bookObj);

        String url = uriInfo.getAbsolutePath() + "/" + bookObj.getId();
        URI uri = URI.create(url);
        return Response.created(uri).build();
    }

    @PUT //PUT at http://localhost:XXXX/BookStore/Book/id
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({"BOOKKEEPER","ADMIN"})
    @Path("{id}")
    public Response updateBook(@PathParam("id") int bookId, Book book) {
        if (bookRepository.getById(bookId) == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Please provide a valid book id.")
                    .build();
        }
        bookRepository.update(bookId, book);
        return Response.noContent().build();
    }



}
