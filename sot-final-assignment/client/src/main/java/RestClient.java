import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;

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

    public static void testBooks(){
        callHello(serviceBooks);
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

    private static void printError(Response response) {
        System.err.println("Error: Cannot get Hello: " + response);
        String entity = response.readEntity(String.class);
        System.err.println(entity);
    }
}
