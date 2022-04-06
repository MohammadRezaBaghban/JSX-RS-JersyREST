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
    private static WebTarget serviceOrders;
    private static WebTarget serviceAuth;
    public static String _jwtToken = "";

    public static void main(String[] args) {

        String username = "mrbhmr@gmail.com";
        String password = "1234";
        ClientConfig config = new ClientConfig();
        config.register(HttpAuthenticationFeature.basic(username,password));

        Client client = ClientBuilder.newClient(new ClientConfig());
        serviceBooks = client.target(URI.create("http://localhost:800/BookStore/books"));
        serviceSubjects = client.target(URI.create("http://localhost:800/BookStore/subjects"));
        serviceOrders = client.target(URI.create("http://localhost:800/BookStore/orders"));
        serviceAuth = client.target(URI.create("http://localhost:800/BookStore/auth"));

        testAuth();
        testOrder();
        testSubjects();
        testBooks();
    }

    public static void testBooks() {
        EndpointTests.callHello(serviceBooks);
        EndpointTests.getNumberObject(serviceBooks);
        EndpointTests.BookTest.getFirstObject(serviceBooks);
        EndpointTests.BookTest.getAllObjects(serviceBooks);
        EndpointTests.getObjectById(serviceBooks, "2");
        EndpointTests.getObjectById(serviceBooks, "10");
        EndpointTests.BookTest.getAllBooksByQueryParameter(serviceBooks, "Computer Science");
        EndpointTests.BookTest.searchBooksBySubjectAndMaxPrice(serviceBooks, "Natural Science", 20);

        // Update Book
        EndpointTests.BookTest.testUpdate(serviceBooks,3,"Introduction to user interface", "UX Design",38.7);
        // Delete Book
        EndpointTests.testDelete(serviceBooks,3);
        // Create Book
        EndpointTests.BookTest.testCreate(serviceBooks,"Logic & Set Theory","Mathematics",46.0);
    }

    public static void testSubjects() {
        EndpointTests.callHello(serviceSubjects);
        EndpointTests.getNumberObject(serviceSubjects);
        EndpointTests.SubjectTest.getFirstObject(serviceSubjects);
        EndpointTests.SubjectTest.getAllObjects(serviceSubjects);
        EndpointTests.getObjectById(serviceSubjects, "2");
        EndpointTests.getObjectById(serviceSubjects, "10");
        EndpointTests.SubjectTest.getAllSubjectsByQueryParameter(serviceSubjects, "Computer Science");

        // Update Book
        EndpointTests.SubjectTest.testUpdate(serviceSubjects,3,"Politics");
        // Delete Book
        EndpointTests.testDelete(serviceSubjects,3);
        // Create Book
        EndpointTests.SubjectTest.testCreate(serviceSubjects,"Sociology");
    }

    public static void testOrder(){
        EndpointTests.callHello(serviceOrders);
        EndpointTests.getNumberObject(serviceOrders);
        // Invalid creation
        EndpointTests.OrderTest.createBookOrder(serviceOrders,"Probability & statistics","Mathematics");
        EndpointTests.OrderTest.createBookOrder(serviceOrders,"Probability and statistics","Mathematic");
        EndpointTests.OrderTest.createBookOrder(serviceOrders,"Probability and statistics","Medical");
        // Correct Creation
        EndpointTests.OrderTest.createBookOrder(serviceOrders,"Probability and statistics","Mathematics");
        EndpointTests.OrderTest.createBookOrder(serviceOrders,"Linear Algebra & Application","Mathematics");
        EndpointTests.OrderTest.createBookOrder(serviceOrders,"Database & Data Modelling","Computer Science");
        EndpointTests.getNumberObject(serviceOrders); // Should be 3
        EndpointTests.OrderTest.getAllOrderObjects(serviceOrders);
    }

    public static void testAuth(){
        EndpointTests.callHello(serviceAuth);
        EndpointTests.AuthTest.register(serviceAuth,"test@gmail.com","1234");
        _jwtToken = EndpointTests.AuthTest.login(serviceAuth,"mrbhmr@gmail.com","1234");
    }

}
