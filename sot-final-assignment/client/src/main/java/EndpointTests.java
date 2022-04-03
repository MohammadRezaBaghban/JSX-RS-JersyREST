import model.Book;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;

public class EndpointTests {

    public static class BookTest {

        public static void callHello(WebTarget service) {
            Invocation.Builder requestBuilder = service.path("hello").request().accept(MediaType.TEXT_PLAIN);
            Response response = requestBuilder.get();

            if (response.getStatus() == 200) {
                String entity = response.readEntity(String.class);
                System.out.println("The resources response is: " + entity);
            } else {
                printError(response);
            }
        }

        public static void getNumberObject(WebTarget service) {
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

        public static void getFirstObject(WebTarget service) {
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

        public static void getAllObjects(WebTarget service) {
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
            } else printError(response);
        }

        public static void getObjectById(WebTarget service, String id) {
            Invocation.Builder requestBuilder = service.path(id).request().accept(MediaType.APPLICATION_JSON);
            Response response = requestBuilder.get();
            String endPointName = endPointName(service);

            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                Book entity = response.readEntity(Book.class);
                System.out.println("The resources response of " + endPointName + "is: " + entity);
            } else printError(response);
        }

        public static void getAllBooksByQueryParameter(WebTarget service, String subjectName) {
            Invocation.Builder requestBuilder = service
                    .queryParam("subject", subjectName)
                    .request()
                    .accept(MediaType.APPLICATION_JSON);
            Response response = requestBuilder.get();

            if (response.getStatus() == Response.Status.OK.getStatusCode())
                handleCollectionOfObjects(response);
            else printError(response);
        }

        public static void searchBooksBySubjectAndMaxPrice(WebTarget service, String subjectName, double MaxPrice) {
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

        public static void deleteBookById(WebTarget service, String id) {
            Invocation.Builder requestBuilder = service.path(id).request().accept(MediaType.TEXT_PLAIN);
            Response response = requestBuilder.delete();

            if (response.getStatus() == Response.Status.NO_CONTENT.getStatusCode())
                System.out.println("Deleted book with given ID successfully");
            else printError(response);
        }

        public static void printError(Response response) {
            System.err.println("Error: Cannot get Hello: " + response);
            String entity = response.readEntity(String.class);
            System.err.println(entity);
        }

        public static String endPointName(WebTarget service) {
            if (service.getUri().toString().contains("books"))
                return "books endpoint";
            else
                return "subjects endpoint";
        }

        public static void handleCollectionOfObjects(Response response) {
            GenericType<ArrayList<Book>> genericType = new GenericType<>() {
            };
            ArrayList<Book> entity = response.readEntity(genericType);
            System.out.println("The resources response is: ");
            for (Book book : entity) {
                System.out.println("\t" + book);
            }
        }

        public static void createBookByName(WebTarget serviceTarget, String name, String subject, double price) {
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

        public static void updateBook(WebTarget serviceTarget, int id, String name, String subject, double price) {
            Book book = new Book(name, subject,price);
            book.setBookId(id);
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

        public static void testDelete(WebTarget service, int id){
            var idStr = String.valueOf(id);
            getObjectById(service, idStr);
            deleteBookById(service,idStr);
            getObjectById(service, idStr);
        }

        public static void testUpdate(WebTarget service, int id, String name, String subject, double price){
            var idStr = String.valueOf(id);
            getObjectById(service, idStr);
            updateBook(service,id,name,subject,price);
            getObjectById(service, idStr);
        }

        public static void testCreate(WebTarget serviceTarget, String name, String subject, double price){
            getNumberObject(serviceTarget);
            createBookByName(serviceTarget,name,subject,price);
            getNumberObject(serviceTarget);
        }

    }

    public static class SubjectTest {

    }

}
