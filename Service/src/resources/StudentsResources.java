package resources;
import repository.FakeStudentRepository;
import repository.StudentRepository;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.awt.*;
import java.util.ResourceBundle;

@Path("/students")
public class StudentsResources {

    private StudentRepository studentRepository;

    public StudentsResources() {
        this.studentRepository = new FakeStudentRepository();
    }

    @GET //GET at http://localhost:XXXX/students/hello
    @Path("/hello")
    @Produces(MediaType.TEXT_PLAIN)
    public Response sayHello(){
        String msg = "Hello, your service works!";
        return Response.status(Response.Status.OK).entity(msg).build();
    }

    @GET //GET at http://localhost:XXXX/school/students/count
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getCount(){
        return Response.ok(studentRepository.count()).build();
    }
}
