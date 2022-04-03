package resources;

import model.Book;
import model.Order;
import repository.FakeOrderRepository;
import repository.IBookRepository;
import repository.IOrderRepository;

import javax.annotation.security.PermitAll;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

@Path("/orders")
public class OrderResources {
    private final IOrderRepository orderRepository;
    @Context
    private UriInfo uriInfo;

    public OrderResources() {
        this.orderRepository = FakeOrderRepository.getInstance();
    }

    @GET //GET at http://localhost:XXXX/BookStore/orders/hello
    @Path("/hello")
    @PermitAll
    @Produces(MediaType.TEXT_PLAIN)
    public Response sayHello() {
        String msg = "Hello, your order endpoint works!";
        return Response.status(Response.Status.OK).entity(msg).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(Order order) {
        var book = orderRepository.getBookByName(order.getBookName());
        var subject = orderRepository.getSubjectByName(order.getSubjectName());
        if (book == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Please provide a valid book name\n Check books endpoints").build();
        } else if (subject == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Please provide a valid subject name\n Check subjects endpoints").build();
        } else if (!book.getSubject().equals(subject.getName())) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Please provide a valid subject name as your desired book\n").build();
        }

        var orderObj = orderRepository.add(order);
        String url = uriInfo.getAbsolutePath() + "/" + orderObj.getId();
        URI uri = URI.create(url);
        return Response.created(uri).build();
    }

}
