import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.ssl.SSLContextConfigurator;
import org.glassfish.grizzly.ssl.SSLEngineConfigurator;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;

import javax.ws.rs.core.UriBuilder;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Publisher {

    private static final URI BASE_URI = URI.create("https://localhost:800/BookStore/");
    private static final String _password = "asdfgh";

    private static File getFile(String fileName) throws URISyntaxException {
        ClassLoader classLoader = Publisher.class.getClassLoader();
        URL url = classLoader.getResource(fileName);
        if (url == null) {
            throw new RuntimeException("Cannot open file " + fileName);
        }
        return new File(url.toURI());
    }

    public static void main(String[] args) {
        try {
            final File keystoreFile  = getFile("keystore_service");
            final File truststoreFile  = getFile("truststore_service");

            // Grizzly ssl configuration
            SSLContextConfigurator sslContext = new SSLContextConfigurator();

            // set up security context
            sslContext.setKeyStoreFile(keystoreFile.getAbsolutePath()); // contains server keypair
            sslContext.setKeyStorePass(_password);

            sslContext.setTrustStoreFile(truststoreFile.getAbsolutePath()); // contains client key
            sslContext.setTrustStorePass(_password);

            sslContext.setKeyPass(_password);

            SSLEngineConfigurator sslEngineConfigurator = new SSLEngineConfigurator(sslContext).setClientMode(false).setNeedClientAuth(true);

            CustomApplicationConfig config = new CustomApplicationConfig();
            URI uri = URI.create("https://localhost:800/BookStore/");
            HttpServer server = GrizzlyHttpServerFactory.createHttpServer(uri,config,true,sslEngineConfigurator);

            System.out.println("Hosting resources at " + uri.toURL());

        } catch (IOException ex) {
            System.out.println("Some error");
            Logger.getLogger(Publisher.class.getName()).log(Level.SEVERE, null, ex);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }



}
