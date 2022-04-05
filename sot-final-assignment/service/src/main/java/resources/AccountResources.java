package resources;


import model.UserAccount;

import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import java.util.HashMap;
import java.util.Map;

@Path("/Auth")
public class AccountResources {

    @Context
    private UriInfo uriInfo;
    private static Map<String, UserAccount> credentials = new HashMap<String, UserAccount>();

}
