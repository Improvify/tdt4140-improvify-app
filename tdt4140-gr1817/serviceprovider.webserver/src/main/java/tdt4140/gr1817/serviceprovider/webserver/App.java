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
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.jvnet.hk2.guice.bridge.api.GuiceBridge;
import org.jvnet.hk2.guice.bridge.api.GuiceIntoHK2Bridge;
import org.slf4j.LoggerFactory;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.PropertyConnectionConfigurationSource;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.guice.MySqlConnectionModule;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.guice.MySqlRepositoryModule;

import javax.ws.rs.core.Feature;
import java.sql.Connection;

@Slf4j
public class App {


    /**
     * Database configuration can be set with VM options. See {@link PropertyConnectionConfigurationSource}.
     * @param args command line args
     * @see PropertyConnectionConfigurationSource
     */
    public static void main(String[] args) {
        // Jetty spams too much. Increase level for less spam
        ((Logger) LoggerFactory.getLogger("org.eclipse.jetty")).setLevel(Level.INFO);


        final Injector injector = createInjector();
        final App app = new App(2222, injector);

        app.start();
    }

    /**
     * @return a configured injector
     * @see PropertyConnectionConfigurationSource
     */
    private static Injector createInjector() {
        PropertyConnectionConfigurationSource conf = new PropertyConnectionConfigurationSource();
        Injector injector = Guice.createInjector(
                new MySqlConnectionModule(conf.user, conf.password, conf.host, "ecosystem", conf.port),
                new MySqlRepositoryModule());

        conf.validate(injector.getInstance(Connection.class));

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
        config.register(MultiPartFeature.class);
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
