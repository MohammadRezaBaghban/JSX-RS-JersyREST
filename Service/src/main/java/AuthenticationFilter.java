import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.*;

import static javax.ws.rs.client.Entity.entity;

public class AuthenticationFilter implements ContainerRequestFilter {

    /**
     * - resourceInfo contains information about the requested operation (GET, PUT, POST ...).
     * - resourceInfo will be assigned/set automatically by the Jersey framework, you do not need to assign/set it.
     */

    private static Map<String, String> credentials = new HashMap<String, String>();


    @Context
    private ResourceInfo resourceInfo;

    // requestContext contains information about the HTTP request message
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

        final String AUTHORIZATION_PROPERTY = "Authorization";
        final String AUTHENTICATION_SCHEME = "Basic";

        // Get Response Header
        final MultivaluedMap<String, String> headers = requestContext.getHeaders();

        // Fetch Authorization Header
        final List<String> authorization = headers.get(AUTHORIZATION_PROPERTY);

        if (authorization == null || authorization.isEmpty()) {
            Response response = Response
                    .status(Response.Status.UNAUTHORIZED)
                    .entity("Missing username and/or password.")
                    .build();
            requestContext.abortWith(response);
            return;
        }

        // Get encoded username and password
        final String encodedCredentials = authorization.get(0).replaceFirst(AUTHENTICATION_SCHEME + " ", "");

        //Decode username and password into one string
        String credentials = new String(Base64.getDecoder().decode(encodedCredentials.getBytes()));

        // Split username and password into one string
        final StringTokenizer tokenizer = new StringTokenizer(credentials, ":");
        final String username = tokenizer.nextToken();
        final String password = tokenizer.nextToken();

        // Check if username and password are valid (e.g., database)
        // If not valid: abort with UNAUTHORIZED and stop
        if (!isValidUser(username, password)) {
            Response response = Response
                    .status(Response.Status.UNAUTHORIZED)
                    .entity("Inavlid username and/or password.")
                    .build();
            requestContext.abortWith(response);
            return;
        }

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

    private boolean isValidUser(String username, String password) {
        credentials.put("mrbhmr@gmail.com", "1234");
        if (credentials.containsKey(username)) {
            var credentialPassword = credentials.get(username);
            return credentialPassword.equals(password);
        }
        return false;
    }


}
