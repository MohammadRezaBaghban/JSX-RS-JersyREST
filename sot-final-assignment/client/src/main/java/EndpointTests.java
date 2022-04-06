import model.*;
import model.Subject;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;

public class EndpointTests {


    public static void callHello(WebTarget service) {
        Response response = requestBuilder(service,"hello").accept(MediaType.TEXT_PLAIN).get();

        if (response.getStatus() == 200) {
            String entity = response.readEntity(String.class);
            System.out.println("The resources response is: " + entity);
        } else {
            printError(response);
        }
    }

    public static void getNumberObject(WebTarget service) {
        var requestBuilder = requestBuilder(service,"count").accept(MediaType.TEXT_PLAIN);
        var response = requestBuilder.get();
        var endPointName = endPointName(service);

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            Integer entity = response.readEntity(Integer.class);
            System.out.println("The resources response of " + endPointName.toString() + "is: " + entity);
        } else printError(response);
    }

    public static void getObjectById(WebTarget service, String id) {
        var requestBuilder = requestBuilder(service,id).accept(MediaType.APPLICATION_JSON);
        endPointType endPointName = endPointName(service);
        Response response = requestBuilder.get();

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            Object entity = null;
            if (endPointName == endPointType.Subjects)
                entity = response.readEntity(Subject.class);
            else if (endPointName == endPointType.Books)
                entity = response.readEntity(Book.class);
            if (entity != null)
                System.out.println("The resources response of " + endPointName + "is: " + entity);
        } else printError(response);
    }

    public static void deleteObjectById(WebTarget service, String id) {
        Invocation.Builder requestBuilder = requestBuilder(service,id).accept(MediaType.TEXT_PLAIN);
        Response response = requestBuilder.delete();

        if (response.getStatus() == Response.Status.NO_CONTENT.getStatusCode())
            System.out.println("Deleted book with given ID successfully");
        else printError(response);
    }

    public static void testDelete(WebTarget service, int id) {
        var idStr = String.valueOf(id);
        getObjectById(service, idStr);
        deleteObjectById(service, idStr);
        getObjectById(service, idStr);
    }

    public static void printError(Response response) {
        System.err.println("Error: Cannot get Hello: " + response);
        String entity = response.readEntity(String.class);
        System.err.println(entity);
    }

    public static Invocation.Builder requestBuilder(WebTarget service, String path){
        return service.path(path).request().header("Authorization",RestClient._jwtToken);
    }

    public static endPointType endPointName(WebTarget service) {
        if (service.getUri().toString().contains("books"))
            return endPointType.Books;
        else return endPointType.Subjects;
    }

    public static class BookTest {

        public static void getFirstObject(WebTarget service) {
            Invocation.Builder requestBuilder = requestBuilder(service,"first").accept(MediaType.APPLICATION_JSON);
            Response response = requestBuilder.get();
            var endPointName = endPointName(service);

            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                Subject entity = response.readEntity(Subject.class);
                System.out.println("The resources response of " + endPointName.toString() + "is: " + entity);
            } else printError(response);
        }

        public static void getAllObjects(WebTarget service) {
            Invocation.Builder requestBuilder = requestBuilder(service,"").accept(MediaType.APPLICATION_JSON);
            Response response = requestBuilder.get();

            if (response.getStatus() == Response.Status.OK.getStatusCode())
                handleCollectionOfBooks(response);
            else printError(response);
        }

        public static void getAllBooksByQueryParameter(WebTarget service, String subjectName) {
            Invocation.Builder requestBuilder = service
                    .queryParam("subject", subjectName)
                    .request()
                    .header("Authorization",RestClient._jwtToken)
                    .accept(MediaType.APPLICATION_JSON);
            Response response = requestBuilder.get();

            if (response.getStatus() == Response.Status.OK.getStatusCode())
                handleCollectionOfBooks(response);
            else printError(response);
        }

        public static void searchBooksBySubjectAndMaxPrice(WebTarget service, String subjectName, double MaxPrice) {
            Invocation.Builder requestBuilder = service
                    .path("search/" + subjectName)
                    .queryParam("maxPrice", MaxPrice)
                    .request()
                    .header("Authorization",RestClient._jwtToken)
                    .accept(MediaType.APPLICATION_JSON);
            Response response = requestBuilder.get();

            if (response.getStatus() == Response.Status.OK.getStatusCode())
                handleCollectionOfBooks(response);
            else printError(response);
        }

        public static void handleCollectionOfBooks(Response response) {
            GenericType<ArrayList<Subject>> genericType = new GenericType<>() {
            };
            ArrayList<Subject> entity = response.readEntity(genericType);
            System.out.println("The resources response is: ");
            for (Subject book : entity) {
                System.out.println("\t" + book);
            }
        }

        public static void createBookByName(WebTarget serviceTarget, String name, String subject, double price) {
            Form form = new Form();
            form.param("name", name);
            form.param("subject", subject);
            form.param("price", String.valueOf(price));
            Entity<Form> entity = Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED);

            Response response = requestBuilder(serviceTarget,"").accept(MediaType.TEXT_PLAIN).post(entity);
            if (response.getStatus() == Response.Status.CREATED.getStatusCode()) {
                String bookUrl = response.getHeaderString("Location");
                System.out.println("POST book is created and can be accessed at: " + bookUrl);
            } else printError(response);
        }

        public static void updateBook(WebTarget serviceTarget, int id, String name, String subject, double price) {
            Book book = new Book(name, subject, price);
            book.setBookId(id);
            Entity<Book> entity = Entity.entity(book, MediaType.APPLICATION_JSON);

            Response response = requestBuilder(serviceTarget,Integer.toString(book.getId()))
                    .accept(MediaType.TEXT_PLAIN)
                    .put(entity);

            if (response.getStatus() == Response.Status.NO_CONTENT.getStatusCode()) {
                System.out.println("PUT book is updated");
            } else printError(response);
        }

        public static void testUpdate(WebTarget service, int id, String name, String subject, double price) {
            var idStr = String.valueOf(id);
            getObjectById(service, idStr);
            updateBook(service, id, name, subject, price);
            getObjectById(service, idStr);
        }

        public static void testCreate(WebTarget serviceTarget, String name, String subject, double price) {
            getNumberObject(serviceTarget);
            createBookByName(serviceTarget, name, subject, price);
            getNumberObject(serviceTarget);
        }

    }

    public static class SubjectTest {
        public static void getFirstObject(WebTarget service) {
            Invocation.Builder requestBuilder = requestBuilder(service,"first").accept(MediaType.APPLICATION_JSON);
            Response response = requestBuilder.get();

            if (response.getStatus() == Response.Status.OK.getStatusCode())
                handleSubjectEntity(response, endPointName(service));
            else printError(response);
        }

        public static void getAllObjects(WebTarget service) {
            Invocation.Builder requestBuilder = requestBuilder(service,"").accept(MediaType.APPLICATION_JSON);
            Response response = requestBuilder.get();

            if (response.getStatus() == Response.Status.OK.getStatusCode())
                handleCollectionOfSubjects(response);
            else printError(response);
        }

        public static void getObjectById(WebTarget service, String id) {
            Invocation.Builder requestBuilder = requestBuilder(service,id).accept(MediaType.APPLICATION_JSON);
            Response response = requestBuilder.get();

            if (response.getStatus() == Response.Status.OK.getStatusCode())
                handleSubjectEntity(response, endPointName(service));
            else printError(response);
        }

        public static void getAllSubjectsByQueryParameter(WebTarget service, String subjectName) {
            Invocation.Builder requestBuilder = service
                    .queryParam("subject", subjectName)
                    .request()
                    .header("Authorization",RestClient._jwtToken)
                    .accept(MediaType.APPLICATION_JSON);
            Response response = requestBuilder.get();

            if (response.getStatus() == Response.Status.OK.getStatusCode())
                handleCollectionOfSubjects(response);
            else printError(response);
        }

        public static void handleCollectionOfSubjects(Response response) {
            GenericType<ArrayList<Subject>> genericType = new GenericType<>() {
            };
            ArrayList<Subject> entity = response.readEntity(genericType);
            System.out.println("The resources response is: ");
            for (Subject subject : entity) {
                System.out.println("\t" + subject);
            }
        }

        private static void handleSubjectEntity(Response response, endPointType endPointName) {
            Subject entity = response.readEntity(Subject.class);
            System.out.println("The resources response of " + endPointName.toString() + "is: " + entity);
        }

        public static void createSubjectByName(WebTarget serviceTarget, String name) {
            Form form = new Form();
            form.param("name", name);
            Entity<Form> entity = Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED);

            Response response = requestBuilder(serviceTarget,"").accept(MediaType.TEXT_PLAIN).post(entity);
            if (response.getStatus() == Response.Status.CREATED.getStatusCode()) {
                String subjectUrl = response.getHeaderString("Location");
                System.out.println("POST subject is created and can be accessed at: " + subjectUrl);
            } else printError(response);
        }

        public static void updateSubject(WebTarget serviceTarget, int id, String name) {
            Subject subject = new Subject(name);
            subject.setId(id);
            Entity<Subject> entity = Entity.entity(subject, MediaType.APPLICATION_JSON);

            Response response = requestBuilder(serviceTarget,Integer.toString(subject.getId()))
                    .accept(MediaType.TEXT_PLAIN)
                    .put(entity);

            if (response.getStatus() == Response.Status.NO_CONTENT.getStatusCode()) {
                System.out.println("PUT subject is updated");
            } else printError(response);
        }

        public static void testUpdate(WebTarget service, int id, String name) {
            var idStr = String.valueOf(id);
            getObjectById(service, idStr);
            updateSubject(service, id, name);
            getObjectById(service, idStr);
        }

        public static void testCreate(WebTarget service, String name) {
            getNumberObject(service);
            createSubjectByName(service, name);
            getNumberObject(service);
        }
    }

    public static class OrderTest {

        public static void getAllOrderObjects(WebTarget service) {
            Invocation.Builder requestBuilder = requestBuilder(service,"").accept(MediaType.APPLICATION_JSON);
            Response response = requestBuilder.get();

            if (response.getStatus() == Response.Status.OK.getStatusCode())
                handleCollectionOfOrders(response);
            else printError(response);
        }

        public static void handleCollectionOfOrders(Response response) {
            GenericType<ArrayList<Order>> genericType = new GenericType<>() {
            };
            ArrayList<Order> entity = response.readEntity(genericType);
            System.out.println("The resources response is: ");
            for (Order order : entity) {
                System.out.println("\t" + order);
            }
        }

        public static void createBookOrder(WebTarget service, String BookName, String subjectName) {
            Order order = new Order(BookName, subjectName);
            Entity<Order> entity = Entity.entity(order, MediaType.APPLICATION_JSON);

            Response response = requestBuilder(service,"")
                    .accept(MediaType.APPLICATION_JSON)
                    .post(entity);

            if (response.getStatus() == Response.Status.NO_CONTENT.getStatusCode()) {
                System.out.println("POST order is updated");
            } else printError(response);
        }

        public static void testCreate(WebTarget service, String BookName, String subjectName) {
            getNumberObject(service);
            createBookOrder(service, BookName, subjectName);
            getNumberObject(service);
        }
    }

    public static class AuthTest {

        public static void register(WebTarget service, String username, String password) {
            Account account = new Account(username, password);
            Entity<Account> entity = Entity.entity(account, MediaType.APPLICATION_JSON);

            Response response = requestBuilder(service,"register")
                    .accept(MediaType.APPLICATION_JSON)
                    .post(entity);

            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                System.out.println(response.getEntity());
            } else printError(response);
        }

        public static String login(WebTarget service, String username, String password) {
            String token = null;
            Account account = new Account(username, password);
            Entity<Account> entity = Entity.entity(account, MediaType.APPLICATION_JSON);

            Response response = requestBuilder(service,"login").accept(MediaType.APPLICATION_JSON)
                    .post(entity);

            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                token = response.readEntity(String.class);
                String authorizationHeader = "Bearer " + token;
                Invocation.Builder requestBuilder = service.path("hello").request()
                        .header("Authorization", authorizationHeader)
                        .accept(MediaType.TEXT_PLAIN);

                System.out.println("Login was successful");
            } else printError(response);
            return token;
        }
    }

}
