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
        EndpointTests.BookTest.callHello(serviceBooks);
        EndpointTests.BookTest.getNumberObject(serviceBooks);
        EndpointTests.BookTest.getFirstObject(serviceBooks);
        EndpointTests.BookTest.getAllObjects(serviceBooks);
        EndpointTests.BookTest.getObjectById(serviceBooks, "2");
        EndpointTests.BookTest.getObjectById(serviceBooks, "10");
        EndpointTests.BookTest.getAllBooksByQueryParameter(serviceBooks, "Computer Science");
        EndpointTests.BookTest.searchBooksBySubjectAndMaxPrice(serviceBooks, "Natural Science", 20);

        // Update Book
        EndpointTests.BookTest.testUpdate(serviceBooks,3,"Introduction to user interface", "UX Design",38.7);
        // Delete Book
        EndpointTests.BookTest.testDelete(serviceBooks,3);
        // Create Book
        EndpointTests.BookTest.testCreate(serviceBooks,"Logic & Set Theory","Mathematics",46.0);
    }

    public static void testSubjects() {
        //callHello(serviceSubjects);
    }

}
