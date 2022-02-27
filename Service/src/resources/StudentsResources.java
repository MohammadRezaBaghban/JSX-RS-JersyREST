package resources;

import model.Student;
import repository.FakeStudentRepository;
import repository.StudentRepository;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import java.awt.*;
import java.util.Collection;
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
    public Response sayHello() {
        String msg = "Hello, your service works!";
        return Response.status(Response.Status.OK).entity(msg).build();
    }

    @GET //GET at http://localhost:XXXX/school/students/count
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getCount() {
        return Response.ok(studentRepository.count()).build();
    }

    @GET //GET at http:localhost:XXXX/school/students/first
    @Path("first")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFirstStudent() {
        var student = studentRepository.getStudentByIndex(0);
        if (student == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(student).build();
    }

    @GET //GET at http:localhost:XXXX/school/students/first
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFirstStudent(@PathParam("id") int studentId) {
        var student = studentRepository.getById(studentId);
        if (student == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(student).build();
    }

    @Context
    private UriInfo uriInfo;

    @GET //Get at http://localhost:XXXX/school/students?name=Ann
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllStudents(@QueryParam("name") String name) {

        if (!uriInfo.getQueryParameters().containsKey("name")) {
            GenericEntity<Collection<Student>> entity = new GenericEntity<>(studentRepository.getAll()) {
            };
            return Response.ok(entity).build();
        }

        // if query param is present, filter students based on name
        Collection<Student> filtered = studentRepository.filterStudentsByName(name);
        if (filtered.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Please provide a valid name").build();
        }

        GenericEntity<Collection<Student>> entity = new GenericEntity<>(filtered) {
        };
        return Response.ok(entity).build();
    }

    @GET //GET at http://localhost:XXXX/students/me/25/myself/donal/and/i?x=sot&y=8
    @Path("me/{a}/myself/{b}/and/i")
    @Produces(MediaType.TEXT_PLAIN)
    public String exampleQueryAndPATHParameters(
            @QueryParam("x") String q1,
            @PathParam("a") int p1,
            @QueryParam("y") int q2,
            @PathParam("b") String p2) {
        return "x - Q1 = " + q1 + "\n" +
                "y - Q2 = " + q2 + "\n" +
                "a - P1 = " + p1 + "\n" +
                "b - P2 = " + p2;

    }

    @DELETE //Delete at http://XXXX/school.students/3
    @Path("{id}")
    public Response deleteStudent(@PathParam("id") int studentId){
        studentRepository.deleteById(studentId);
        return Response.noContent().build();
    }
}
