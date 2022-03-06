import com.sun.net.httpserver.HttpServer;
import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;

import java.io.IOException;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Publisher {

    private static final URI BASE_URI = URI.create("http://localhost:800/school/");

    public static void main(String[] args) {
        try {
            CustomApplicationConfig config = new CustomApplicationConfig();
            HttpServer server = JdkHttpServerFactory.createHttpServer(BASE_URI, config, true);
            System.out.println("Hosting resources at " + BASE_URI.toURL());

        } catch (IOException ex) {
            System.out.println("Some error");
            Logger.getLogger(Publisher.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
