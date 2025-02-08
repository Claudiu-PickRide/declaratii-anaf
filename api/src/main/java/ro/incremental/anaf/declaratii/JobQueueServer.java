package ro.incremental.anaf.declaratii;

/**
 * Created by Alex Proca <alex.proca@gmail.com> on 18/03/16.
 */
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import jakarta.servlet.Servlet;

public class JobQueueServer {

    public static void main(String[] args) throws Exception {

        int portNumber = 5000;
        String port = System.getenv("PORT");
        if(port != null) {
            portNumber = Integer.parseInt(port);
        }

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
        context.setContextPath("/");

        Server jettyServer = new Server(portNumber);
        jettyServer.setHandler(context);

        // Create ResourceConfig
        ResourceConfig config = new ResourceConfig();
        config.packages("ro.incremental.anaf.declaratii");
        config.register(MultiPartFeature.class);

        // Create ServletContainer with ResourceConfig and cast to Servlet
        Servlet servletContainer = new ServletContainer(config);
        ServletHolder jerseyServlet = new ServletHolder(servletContainer);
        context.addServlet(jerseyServlet, "/*");

        try {
            jettyServer.start();
            jettyServer.join();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jettyServer.destroy();
        }
    }

}