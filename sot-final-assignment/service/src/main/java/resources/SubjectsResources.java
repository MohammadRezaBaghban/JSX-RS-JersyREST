package resources;

import model.Book;
import model.Subject;
import repository.FakeSubjectRepository;
import repository.ISubjectRepository;

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
    private ISubjectRepository subjectRepository;

    public SubjectsResources() {
        this.subjectRepository = FakeSubjectRepository.getInstance();
    }


    @GET //GET at http://localhost:XXXX/books/hello
    @Path("/hello")
    @PermitAll
    @Produces(MediaType.TEXT_PLAIN)
    public Response sayHello() {
        String msg = "Hello, your subject endpoint works!";
        return Response.status(Response.Status.OK).entity(msg).build();
    }

    @GET //GET at http://localhost:XXXX/BookStore/subjects/count
    @Path("count")
    @PermitAll
    @Produces(MediaType.TEXT_PLAIN)
    public Response getCount() {
        return Response.ok(subjectRepository.count()).build();
    }

    @GET //GET at http:localhost:XXXX/BookStore/subjects/first
    @Path("first")
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFirstSubject() {
        var book = subjectRepository.getSubjectByIndex(0);
        if (book == null)
            return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok(book).build();
    }

    @GET //GET at http:localhost:XXXX/BookStore/books/first
    @Path("{id}")
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSubjectById(@PathParam("id") int subjectId) {
        var subject = subjectRepository.getById(subjectId);
        if (subject == null)   return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok(subject).build();
    }

    @GET //Get at http://localhost:XXXX/BookStore/subjects?subject=Computer Science
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllSubject(@QueryParam("subject") String name) {

        if (!uriInfo.getQueryParameters().containsKey("subject")) {
            GenericEntity<Collection<Subject>> entity = new GenericEntity<>(subjectRepository.getAll()) {
            };
            return Response.ok(entity).build();
        }

        // if query param is present, filter students based on name
        Collection<Subject> filtered = subjectRepository.filterSubjectByName(name);
        if (filtered.isEmpty())
            return Response.status(Response.Status.BAD_REQUEST).entity("Please provide a valid subject name").build();

        GenericEntity<Collection<Subject>> entity = new GenericEntity<>(filtered) {
        };
        return Response.ok(entity).build();
    }

    @DELETE //Delete at http://XXXX/BookStore/Subjects/3
    @RolesAllowed({"BOOKKEEPER","ADMIN"})
    @Path("{id}")
    public Response deleteStudent(@PathParam("id") int subjectId) {
        subjectRepository.deleteById(subjectId);
        return Response.noContent().build();
    }

    @POST //Post at http://localhost:XXXX:/BookStore/subjects/
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @RolesAllowed({"BOOKKEEPER","ADMIN"})
    public Response createSubject(
            @FormParam("name") String name) {
        var subject = new Subject(name);
        subjectRepository.add(subject);

        // url of the created book
        String url = uriInfo.getAbsolutePath() + "/" + subject.getId();
        URI uri = URI.create(url);
        return Response.created(uri).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"BOOKKEEPER","ADMIN"})
    public Response create(Subject subject) {
        subjectRepository.add(subject);

        String url = uriInfo.getAbsolutePath() + "/" + subject.getId();
        URI uri = URI.create(url);
        return Response.created(uri).build();
    }

    @PUT //PUT at http://localhost:XXXX/BookStore/Subjects/id
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({"BOOKKEEPER","ADMIN"})
    @Path("{id}")
    public Response updateSubject(@PathParam("id") int subjectId, Subject subject) {
        if (subjectRepository.getById(subjectId) == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Please provide a valid subject id.")
                    .build();
        }
        subjectRepository.update(subjectId, subject);
        return Response.noContent().build();
    }

}
