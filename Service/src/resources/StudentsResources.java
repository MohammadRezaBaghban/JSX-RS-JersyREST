package resources;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.awt.*;
import java.util.ResourceBundle;

@Path("/students")
public class StudentsResources {
    @GET //GET at http://localhost:XXXX/students/hello
    @Path("/hello")
    @Produces(MediaType.TEXT_PLAIN)
    public Response sayHello(){
        String msg = "Hello, your service works!";
        //return Response.status(Response.Status.OK).entity(msg).build();
         return Response.ok(msg).build(); //This is another way to create a 200 ok response
    }
}
