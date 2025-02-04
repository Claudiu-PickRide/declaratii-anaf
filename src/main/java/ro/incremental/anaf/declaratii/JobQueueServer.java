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

public class JobQueueServer {

    public static void main(String[] args) {

        int portNumber = 5000;
        String port = System.getenv("PORT");
        if(port != null) {
            portNumber = Integer.parseInt(port);
        }

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
        context.setContextPath("/");

        Server jettyServer = new Server(portNumber);
        jettyServer.setHandler(context);

        ServletHolder jerseyServlet = context.addServlet(ServletContainer.class, "/*");
        jerseyServlet.setInitOrder(0);

        // Tells the Jersey Servlet which REST service/class to load.
        jerseyServlet.setInitParameter("jersey.config.server.provider.packages", "ro.incremental.anaf.declaratii");

        jerseyServlet.setInitParameter("jersey.config.server.provider.classnames", MultiPartFeature.class.getName());


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