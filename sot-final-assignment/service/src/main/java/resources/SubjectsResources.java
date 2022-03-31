package resources;
import repository.FakeSubjectRepository;
import repository.SubjectRepository;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

import java.awt.*;
import java.net.URI;
import java.util.Collection;
import java.util.ResourceBundle;

@Path("/subjects")
public class SubjectsResources {
    @Context
    private UriInfo uriInfo;
    private SubjectRepository subjectRepository;

    public SubjectsResources() {
        //this.studentRepository = FakeBookRepository.getInstance();
    }


    @GET //GET at http://localhost:XXXX/books/hello
    @Path("/hello")
    @PermitAll
    @Produces(MediaType.TEXT_PLAIN)
    public Response sayHello() {
        String msg = "Hello, your subject endpoint works!";
        return Response.status(Response.Status.OK).entity(msg).build();
    }
}
