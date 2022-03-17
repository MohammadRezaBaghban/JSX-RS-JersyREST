import resources.StudentsResources;
import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.server.ResourceConfig;

import java.util.logging.Level;
import java.util.logging.Logger;

public class CustomApplicationConfig extends ResourceConfig {
    public CustomApplicationConfig() {
        register(StudentsResources.class); // register endpoint StudentsResources

        // register Logging of exchanged http messages
        Logger logger = Logger.getLogger(LoggingFeature.DEFAULT_LOGGER_NAME);
        register(new LoggingFeature(logger, Level.INFO, LoggingFeature.Verbosity.PAYLOAD_ANY, LoggingFeature.DEFAULT_MAX_ENTITY_SIZE));
        register(AuthenticationFilter.class);
    }
}
