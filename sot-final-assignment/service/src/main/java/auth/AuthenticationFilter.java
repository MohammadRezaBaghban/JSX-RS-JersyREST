package auth;

import model.UserAccount;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;

public class AuthenticationFilter implements ContainerRequestFilter {

    /**
     * - resourceInfo contains information about the requested operation (GET, PUT, POST ...).
     * - resourceInfo will be assigned/set automatically by the Jersey framework, you do not need to assign/set it.
     */
    private static Map<String, UserAccount> credentials = new HashMap<String, UserAccount>();

    @Context
    private ResourceInfo resourceInfo;

    /*
     Get information about the service method which is being called.
     This information includes the annotated/permitted roles.
     */


    // requestContext contains information about the HTTP request message
    // Here we will perform Authentication and authorization
        /* if we want to abort HTTP request, we do this:
            Response response  = Response.status(Response.status.UNAUTHORIZED).build();
            requestContext.abortwith(response);
        */
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

        final String AUTHORIZATION_PROPERTY = "Authorization";
        final String AUTHENTICATION_SCHEME = "Basic";

        Method method = resourceInfo.getResourceMethod();
        // if access is allowed for all -> do not check anything further : access is approved for all
        if (method.isAnnotationPresent(PermitAll.class))
            return;

        // if access is denied for all: deny access
        if (method.isAnnotationPresent(DenyAll.class)) {
            Response response = Response.status(Response.Status.FORBIDDEN).build();
            requestContext.abortWith(response);
            return;
        }

        // Authentication
        // 1. extract username and password from requestContext
        // 2. validate username and password (e.g. database)
        // 3. if invalid user, abort requestContext with Unauthized response

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

        // Authorization
        // 1. extract allowed roles for requested operation from resourceInfo
        // 2. check if the user has one these roles
        // 3. if not, abort requestContext with FROBIDDEN response

        if (method.isAnnotationPresent(RolesAllowed.class)) {
            // Get Allowed Roles for this method
            RolesAllowed rolesAnnotation = method.getAnnotation(RolesAllowed.class);
            Set<String> rolesSet = new HashSet<String>(Arrays.asList(rolesAnnotation.value()));

            /*
                isUserAllowed: implement this method to check if this user has nay of the roles in the rolesSet
                if not isUserAllowed abort the requestContext with FORBIDDEN response
             */

            if (!isUserAllowed(username, password, rolesSet)) {
                Response response = Response.status(Response.Status.FORBIDDEN).build();
                requestContext.abortWith(response);
            }
        }
    }

    private boolean isValidUser(String username, String password) {
        setup();
        if (credentials.containsKey(username)) {
            var credentialPassword = credentials.get(username);
            return credentialPassword.get_password().equals(password);
        }
        return false;
    }

    private boolean isUserAllowed(String username, String password, Set<String> rolesSet) {
        var user = credentials.get(username);
        var userRoles = user.get_roles();
        for (String role : userRoles ) {
            if (rolesSet.contains(role))
                return true;
        }
        return false;
    }

    private void setup() {
        if (!credentials.containsKey("mrbhmr@gmail.com")) {
            var user = new UserAccount("mrbhmr@gmail.com", "1234", new ArrayList<String>());
            user.get_roles().add("TEACHER");
            credentials.put("mrbhmr@gmail.com", user);
        }
    }


}
