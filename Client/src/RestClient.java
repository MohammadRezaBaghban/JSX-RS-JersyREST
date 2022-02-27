import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import model.Student;
import org.glassfish.jersey.client.ClientConfig;
import jakarta.ws.rs.core.Response;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.*;
import java.net.URI;
import java.util.ArrayList;

public class RestClient {

    public static void main(String[] args) {
        ClientConfig config = new ClientConfig();
        jakarta.ws.rs.client.Client client = ClientBuilder.newClient(config);
        URI baseURI = URI.create("http://localhost:800/school/students");
        ;
        WebTarget serviceTarget = client.target(baseURI);

        callHello(serviceTarget);
        getNumberStudents(serviceTarget);
        getFirstStudent(serviceTarget);
        getStudentById(serviceTarget,"2");
        getAllStudentsByQueryParameter(serviceTarget,"Ann");
        deleteStudentById(serviceTarget,"4");

    }

    private static void callHello(WebTarget serviceTarget) {
        Invocation.Builder requestBuilder = serviceTarget.path("hello").request().accept(MediaType.TEXT_PLAIN);
        Response response = requestBuilder.get();

        if (response.getStatus() == 200) {
            String entity = response.readEntity(String.class);
            System.out.println("The resources response is: " + entity);
        } else {
            printError(response);
        }
    }

    private static void getNumberStudents(WebTarget serviceTarget) {
        Invocation.Builder requestBuilder = serviceTarget.path("count").request().accept(MediaType.TEXT_PLAIN);
        Response response = requestBuilder.get();

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            Integer entity = response.readEntity(Integer.class);
            System.out.println("The resources response is: " + entity);
        } else {
            printError(response);
        }
    }

    private static void getFirstStudent(WebTarget serviceTarget) {
        Invocation.Builder requestBuilder = serviceTarget.path("first").request().accept(MediaType.APPLICATION_JSON);
        Response response = requestBuilder.get();

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            Student entity = response.readEntity(Student.class);
            System.out.println("The resources response is: " + entity);
        } else {
            printError(response);
        }
    }

    private static void getAllStudents(WebTarget serviceTarget) {
        Invocation.Builder requestBuilder = serviceTarget.request().accept(MediaType.APPLICATION_JSON);
        Response response = requestBuilder.get();

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            GenericType<ArrayList<Student>> genericType = new GenericType<>() {
            };
            ArrayList<Student> entity = response.readEntity(genericType);
            System.out.println("The resources response is: ");
            for (Student student : entity) {
                System.out.println("\t" + student);
            }
        } else {
            printError(response);
        }
    }

    private static void getAllStudentsByQueryParameter(WebTarget serviceTarget, String name) {
        Invocation.Builder requestBuilder = serviceTarget
                .queryParam("name", name)
                .request()
                .accept(MediaType.APPLICATION_JSON);
        Response response = requestBuilder.get();

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            GenericType<ArrayList<Student>> genericType = new GenericType<>() {
            };
            ArrayList<Student> entity = response.readEntity(genericType);
            System.out.println("The resources response is: ");
            for (Student student : entity) {
                System.out.println("\t" + student);
            }
        } else {
            printError(response);
        }
    }

    private static void getStudentById(WebTarget serviceTarget, String id) {
        Invocation.Builder requestBuilder = serviceTarget.path(id).request().accept(MediaType.APPLICATION_JSON);
        Response response = requestBuilder.get();

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            Student entity = response.readEntity(Student.class);
            System.out.println("The resources response is: " + entity);
        } else {
            printError(response);
        }
    }

    private static void deleteStudentById(WebTarget serviceTarget, String id){
        Invocation.Builder requestBuilder = serviceTarget.path(id).request().accept(MediaType.TEXT_PLAIN);
        Response response = requestBuilder.get();

        if (response.getStatus() == Response.Status.NO_CONTENT.getStatusCode()) {
            System.out.println("Deleted student with given ID successfully");
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
