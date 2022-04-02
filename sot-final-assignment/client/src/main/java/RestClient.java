import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;

import javax.ws.rs.client.*;
import javax.ws.rs.core.*;
import javax.ws.rs.*;
import java.awt.print.Book;
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

    public static void testBooks(){
        callHello(serviceBooks);
        getNumberObject(serviceBooks);
        getFirstObject(serviceBooks);
        getAllObjects(serviceBooks);
        getObjectById(serviceBooks,"2");
        getObjectById(serviceBooks,"10");
        getAllBooksByQueryParameter(serviceBooks,"Computer Science");
    }

    public static void testSubjects(){
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
            System.out.println("The resources response of "+ endPointName + "is: " + entity);
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
            System.out.println("The resources response of "+ endPointName + "is: " + entity);
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
            System.out.println("The resources response of "+ endPointName + "is: " + entity);
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
            System.out.println("The resources response of "+ endPointName + "is: " + entity);
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

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            GenericType<ArrayList<Book>> genericType = new GenericType<>() {
            };
            ArrayList<Book> entity = response.readEntity(genericType);
            System.out.println("The resources response is: ");
            for (Book book : entity) {
                System.out.println("\t" + book);
            }
        } else {
            printError(response);
        }
    }


    private static void printError(Response response) {
        System.err.println("Error: Cannot get Hello: " + response);
        String entity = response.readEntity(String.class);
        System.err.println(entity);
    }

    private static String endPointName(WebTarget service){
        if (service.getUri().toString().contains("books"))
            return "books endpoint";
        else
            return "subjects endpoint";
    }
}
