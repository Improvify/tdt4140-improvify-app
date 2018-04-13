package tdt4140.gr1817.serviceprovider.webserver;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import com.google.inject.Guice;
import com.google.inject.Injector;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.ServiceLocatorProvider;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.jvnet.hk2.guice.bridge.api.GuiceBridge;
import org.jvnet.hk2.guice.bridge.api.GuiceIntoHK2Bridge;
import org.slf4j.LoggerFactory;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.guice.MySqlConnectionModule;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.guice.MySqlRepositoryModule;

import javax.ws.rs.core.Feature;
import java.sql.Connection;
import java.sql.Statement;

@Slf4j
public class App {


    public static void main(String[] args) {
        // Jetty spams too much. Increase level for less spam
        ((Logger) LoggerFactory.getLogger("org.eclipse.jetty")).setLevel(Level.INFO);


        final Injector injector = createInjector();
        final App app = new App(2222, injector);

        app.start();
    }

    private static Injector createInjector() {
        //TODO document that you can set database args using VM options. -Ddb.password="" for blank password etc.

        String user = System.getProperty("db.user", "root");
        String password = System.getProperty("db.password", "root");
        String host = System.getProperty("db.host", "localhost");
        int port = Integer.valueOf(System.getProperty("db.port", "3306"), 10);

        Injector injector = Guice.createInjector(
                new MySqlConnectionModule(user, password, host, "ecosystem", port),
                new MySqlRepositoryModule());


        try (
                Connection instance = injector.getInstance(Connection.class);
                Statement statement = instance.createStatement();
        ) {
            statement.execute("SELECT 1");

        } catch (Exception e) {
            throw new IllegalStateException("Database connection is invalid! "
                    + "Verify that username and password are correct", e);
        }

        return injector;
    }

    private final Server server;
    private final Injector injector;

    App(int port, @NonNull Injector injector) {
        server = new Server(port);
        this.injector = injector;
    }

    /**
     * Starts the web server.
     * <p>
     * Blocking method, so this will not return untill the server shuts down
     */
    public void start() {
        initialize();
        try {
            server.start();
            server.join();
        } catch (Exception e) {
            try {
                server.stop();
            } catch (Exception ex) {
                log.error("Failed to stop server", ex);
            }
            server.destroy();
            log.error("Server crashed!", e);
        }
    }

    private void initialize() {
        // Enable Jersey and our resources

        ResourceConfig config = new ResourceConfig();
        config.packages("tdt4140.gr1817.serviceprovider.webserver.resource");
        config.register((Feature) context -> {
            // Jersey uses HK2 for dependency injection in resources. We replace that with Guice here,
            // since we don't want more frameworks for doing the same, and other modules use Guice.
            ServiceLocator serviceLocator = ServiceLocatorProvider.getServiceLocator(context);
            GuiceBridge.getGuiceBridge().initializeGuiceBridge(serviceLocator);
            GuiceIntoHK2Bridge guiceBridge = serviceLocator.getService(GuiceIntoHK2Bridge.class);
            guiceBridge.bridgeGuiceInjector(injector);
            return true;
        });

        ServletContainer jerseyServlet = new ServletContainer(config);
        ServletHolder servlet = new ServletHolder(jerseyServlet);

        ServletContextHandler context = new ServletContextHandler(server, "/*");
        context.addServlet(servlet, "/*");
//        context.addFilter(GuiceFilter.class, "/*", EnumSet.of(DispatcherType.REQUEST));
    }
}
