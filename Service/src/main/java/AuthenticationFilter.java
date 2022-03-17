import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import java.io.IOException;

public class AuthenticationFilter implements ContainerRequestFilter {

    /**
     * - resourceInfo contains information about the requested operation (GET, PUT, POST ...).
     * - resourceInfo will be assigned/set automatically by the Jersey framework, you do not need to assign/set it.
     */

    @Context
    private ResourceInfo resourceInfo;

    // requestContext contains information about the HTTP request message
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        // Here we will perform Authentication and authorization
        /* if we want to abort HTTP request, we do this:
            Response response  = Response.status(Response.status.UNAUTHORIZED).build();
            requestContext.abortwith(response);
        */

        // Authentication
        // 1. extract username and password from requestContext
        // 2. validate username and password (e.g. database)
        // 3. if invalid user, abort requestContext with Unauthized response

        // Authorization
        // 1. extract allowed roles for requested operation from resourceInfo
        // 2. check if the user has one these roles
        // 3. if not, abort requestContext with FROBIDDEN response
    }

}
