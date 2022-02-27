import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import org.glassfish.jersey.client.ClientConfig;
import jakarta.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.*;
import java.net.URI;

public class RestClient {

    public static void main(String[] args){
        ClientConfig config = new ClientConfig();
        jakarta.ws.rs.client.Client client =  ClientBuilder.newClient(config);
        URI baseURI = URI.create("http://localhost:800/school/students");;
        WebTarget serviceTarget = client.target(baseURI);

        Invocation.Builder requestBuilder = serviceTarget.path("hello").request().accept(MediaType.TEXT_PLAIN);
        Response response = requestBuilder.get();

        if (response.getStatus()==200) {
            String entity =response.readEntity(String.class);
            System.out.println("The resources response is: " + entity);
        } else {
            System.err.println("Error: Cannot get Hello: " + response);
            String entity = response.readEntity(String.class);
            System.err.println(entity);
        }

    }
}
