import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import model.Book;
import javax.ws.rs.client.*;
import javax.ws.rs.core.*;
import javax.ws.rs.*;
import java.net.URI;
import java.util.ArrayList;

public class RestClient {

    private static WebTarget serviceBooks;
    private static WebTarget serviceSubjects;

    public static void main(String[] args) {

        String username = "mrbhmr@gmail.com";
        String password = "1234";
        //config.register(HttpAuthenticationFeature.basic(username,password));

        Client client = ClientBuilder.newClient(new ClientConfig());
        serviceBooks = client.target(URI.create("http://localhost:900/BookStore/books"));
        serviceSubjects = client.target(URI.create("http://localhost:900/BookStore/subjects"));

        testBooks();
        testSubjects();
    }

    public static void testBooks() {

        getObjectById(serviceBooks, "3");
        updateBook(serviceBooks);
        getObjectById(serviceBooks, "3");

        callHello(serviceBooks);
        getNumberObject(serviceBooks);
        getFirstObject(serviceBooks);
        getAllObjects(serviceBooks);
        getObjectById(serviceBooks, "2");
        getObjectById(serviceBooks, "10");
        getAllBooksByQueryParameter(serviceBooks, "Computer Science");
        searchBooksBySubjectAndMaxPrice(serviceBooks, "Natural Science", 20);

        // Delete Book
        getObjectById(serviceBooks, "3");
        deleteBookById(serviceBooks,"3");
        getObjectById(serviceBooks, "3");

        // Create Book
        getNumberObject(serviceBooks);
        createBookByName(serviceBooks,"Logic & Set Theory","Mathematics",46.0);
        getNumberObject(serviceBooks);
    }

    public static void testSubjects() {
        callHello(serviceSubjects);
    }

    private static void callHello(WebTarget service) {
        Invocation.Builder requestBuilder = service.path("hello").request().accept(MediaType.TEXT_PLAIN);
        Response response = requestBuilder.get();

        if (response.getStatus() == 200) {
            String entity = response.readEntity(String.class);
            System.out.println("The resources response is: " + entity);
        } else {
            printError(response);
        }
    }

    private static void getNumberObject(WebTarget service) {
        Invocation.Builder requestBuilder = service.path("count").request().accept(MediaType.TEXT_PLAIN);
        Response response = requestBuilder.get();
        String endPointName = endPointName(service);

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            Integer entity = response.readEntity(Integer.class);
            System.out.println("The resources response of " + endPointName + "is: " + entity);
        } else {
            printError(response);
        }
    }

    private static void getFirstObject(WebTarget service) {
        Invocation.Builder requestBuilder = service.path("first").request().accept(MediaType.APPLICATION_JSON);
        Response response = requestBuilder.get();
        String endPointName = endPointName(service);

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            Book entity = response.readEntity(Book.class);
            System.out.println("The resources response of " + endPointName + "is: " + entity);
        } else {
            printError(response);
        }
    }

    private static void getAllObjects(WebTarget service) {
        Invocation.Builder requestBuilder = service.request().accept(MediaType.APPLICATION_JSON);
        Response response = requestBuilder.get();
        String endPointName = endPointName(service);

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            GenericType<ArrayList<Book>> genericType = new GenericType<>() {
            };
            ArrayList<Book> entity = response.readEntity(genericType);
            System.out.println("The resources response of " + endPointName + "is: " + entity);
            for (Book book : entity) {
                System.out.println("\t" + book.toString());
            }
        } else {
            printError(response);
        }
    }

    private static void getObjectById(WebTarget service, String id) {
        Invocation.Builder requestBuilder = service.path(id).request().accept(MediaType.APPLICATION_JSON);
        Response response = requestBuilder.get();
        String endPointName = endPointName(service);

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            Book entity = response.readEntity(Book.class);
            System.out.println("The resources response of " + endPointName + "is: " + entity);
        } else {
            printError(response);
        }
    }

    private static void getAllBooksByQueryParameter(WebTarget service, String subjectName) {
        Invocation.Builder requestBuilder = service
                .queryParam("subject", subjectName)
                .request()
                .accept(MediaType.APPLICATION_JSON);
        Response response = requestBuilder.get();

        if (response.getStatus() == Response.Status.OK.getStatusCode())
            handleCollectionOfObjects(response);
        else printError(response);
    }

    private static void searchBooksBySubjectAndMaxPrice(WebTarget service, String subjectName, double MaxPrice) {
        Invocation.Builder requestBuilder = service
                .path("search/" + subjectName)
                .queryParam("maxPrice", MaxPrice)
                .request().accept(MediaType.APPLICATION_JSON);
        Response response = requestBuilder.get();
        String endPointName = endPointName(service);

        if (response.getStatus() == Response.Status.OK.getStatusCode())
            handleCollectionOfObjects(response);
        else printError(response);
    }

    private static void deleteBookById(WebTarget service, String id) {
        Invocation.Builder requestBuilder = service.path(id).request().accept(MediaType.TEXT_PLAIN);
        Response response = requestBuilder.delete();

        if (response.getStatus() == Response.Status.NO_CONTENT.getStatusCode())
            System.out.println("Deleted book with given ID successfully");
        else printError(response);
    }

    private static void printError(Response response) {
        System.err.println("Error: Cannot get Hello: " + response);
        String entity = response.readEntity(String.class);
        System.err.println(entity);
    }

    private static String endPointName(WebTarget service) {
        if (service.getUri().toString().contains("books"))
            return "books endpoint";
        else
            return "subjects endpoint";
    }

    private static void handleCollectionOfObjects(Response response) {
        GenericType<ArrayList<Book>> genericType = new GenericType<>() {
        };
        ArrayList<Book> entity = response.readEntity(genericType);
        System.out.println("The resources response is: ");
        for (Book book : entity) {
            System.out.println("\t" + book);
        }
    }

    private static void createBookByName(WebTarget serviceTarget, String name, String subject, double price) {
        Form form = new Form();
        form.param("name", name);
        form.param("subject",subject);
        form.param("price", String.valueOf(price));
        Entity<Form> entity = Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED);

        Response response = serviceTarget.request().accept(MediaType.TEXT_PLAIN).post(entity);
        if (response.getStatus() == Response.Status.CREATED.getStatusCode()) {
            String bookUrl = response.getHeaderString("Location");
            System.out.println("POST book is created and can be accessed at: " + bookUrl);
        } else printError(response);
    }

    private static void updateBook(WebTarget serviceTarget) {

        Book book = new Book("Introduction to user interface", "UX Design",38.7);
        book.setBookId(3);
        Entity<Book> entity = Entity.entity(book, MediaType.APPLICATION_JSON);

        Response response = serviceTarget
                .path(Integer.toString(book.getId()))
                .request().accept(MediaType.TEXT_PLAIN)
                .put(entity);

        if (response.getStatus() == Response.Status.NO_CONTENT.getStatusCode()) {
            System.out.println("PUT book is updated");
        } else {
            printError(response);
        }

    }
}
