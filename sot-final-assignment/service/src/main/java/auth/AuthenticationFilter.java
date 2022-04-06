package auth;

import io.jsonwebtoken.*;
import repository.FakeAccountRepository;
import repository.IAccountRepository;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;

public class AuthenticationFilter implements ContainerRequestFilter {

    /**
     * - resourceInfo contains information about the requested operation (GET, PUT, POST ...).
     * - resourceInfo will be assigned/set automatically by the Jersey framework, you do not need to assign/set it.
     */
    @Context
    private ResourceInfo resourceInfo;
    private String errorInJwtProcessing = "";
    private IAccountRepository accountRepository = FakeAccountRepository.getInstance();

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
        final String AUTHENTICATION_SCHEME = "Bearer";
        errorInJwtProcessing = "Error in parsing JWT";

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
        final String jwtEncodedCredentials = authorization.get(0).replaceFirst(AUTHENTICATION_SCHEME + " ", "");

        String username = processJwtToken(jwtEncodedCredentials);

        if (!errorInJwtProcessing.equals("") || username == null) {
            Response response = Response
                    .status(Response.Status.UNAUTHORIZED)
                    .entity(errorInJwtProcessing)
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

            if (!accountRepository.isUserAllowed(username, password, rolesSet)) {
                Response response = Response.status(Response.Status.FORBIDDEN).build();
                requestContext.abortWith(response);
            }
        }
    }

    public String processJwtToken(String jwtToken) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(accountRepository.getSecret()))
                    .parseClaimsJws(jwtToken).getBody();

            return claims.get("username").toString();//  get the username
        } catch (ExpiredJwtException e) {
            //Exception indicating that a JWT was accepted after it expired and must be rejected.
            errorInJwtProcessing = "Your JWT token has been expired, please login again";
        } catch (MalformedJwtException e) {
            //Exception indicating that a JWT was not correctly constructed and should be rejected.
            errorInJwtProcessing = "Your JWT token is not correctly constructed.";
        } catch (PrematureJwtException e) {
            //Exception indicating that a JWT was accepted before it is allowed to be accessed and must be rejected.
            errorInJwtProcessing = "Your JWT token is not yet valid. Please try again later";
        } catch (SignatureException e) {
            //Exception indicating that either calculating a signature or verifying an existing signature of a JWT failed.
            errorInJwtProcessing = "Your JWT token cannot be processed correctly";
        } catch (UnsupportedJwtException e) {
            //Exception thrown when receiving a JWT in a particular format/configuration that does not match the format expected by the application.
            //For example, this exception would be thrown if parsing an unsigned plaintext JWT when the application requires a cryptographically signed Claims JWS instead.
            errorInJwtProcessing = "Your JWT token is not in correct format that application accepting";
        }
        return null;
    }
}