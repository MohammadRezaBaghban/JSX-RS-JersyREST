import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import model.Student;
import org.glassfish.jersey.client.ClientConfig;
import jakarta.ws.rs.core.Response;

import javax.ws.rs.core.Form;
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
        getStudentById(serviceTarget, "2");
        getAllStudentsByQueryParameter(serviceTarget, "Ann");
        deleteStudentById(serviceTarget, "4");
        deleteStudentById(serviceTarget, "Mohammad");
        createStudentByName(serviceTarget, "MohammadReza");
        updateStudent(serviceTarget);

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

    private static void deleteStudentById(WebTarget serviceTarget, String id) {
        Invocation.Builder requestBuilder = serviceTarget.path(id).request().accept(MediaType.TEXT_PLAIN);
        Response response = requestBuilder.get();

        if (response.getStatus() == Response.Status.NO_CONTENT.getStatusCode()) {
            System.out.println("Deleted student with given ID successfully");
        } else {
            printError(response);
        }
    }

    private static void createStudentByName(WebTarget serviceTarget, String name) {
        Form form = new Form();
        form.param("name", name);
        Entity<Form> entity = Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED);

        Response response = serviceTarget.request().accept(MediaType.TEXT_PLAIN).post(entity);
        if (response.getStatus() == Response.Status.CREATED.getStatusCode()) {
            String studentUrl = response.getHeaderString("Location");
            System.out.println("POST student is created and can be accessed at: " + studentUrl);
        } else {
            printError(response);
        }
    }

    private static void updateStudent(WebTarget serviceTarget) {

        Student student = new Student(3, "Frank");
        Entity<Student> entity = Entity.entity(student, MediaType.APPLICATION_JSON);

        Response response = serviceTarget
                .path(Integer.toString(student.getId()))
                .request().accept(MediaType.TEXT_PLAIN)
                .put(entity);

        if (response.getStatus() == Response.Status.NO_CONTENT.getStatusCode()) {
            System.out.println("PUT student in updated");
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
