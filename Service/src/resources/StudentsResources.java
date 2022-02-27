package resources;
import repository.FakeStudentRepository;
import repository.StudentRepository;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
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

    @GET //GET at http:localhost:XXXX/school/students/first
    @Path("first")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFirstStudent(){
        var student = studentRepository.getStudentByIndex(0);
        if(student==null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(student).build();
    }

    @GET //GET at http:localhost:XXXX/school/students/first
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFirstStudent(@PathParam("id") int studentId) {
        var student = studentRepository.get(studentId);
        if(student==null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(student).build();
    }
}
