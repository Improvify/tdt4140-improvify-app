package tdt4140.gr1817.serviceprovider.webserver;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import com.google.gson.Gson;
import com.google.inject.AbstractModule;
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
import tdt4140.gr1817.ecosystem.persistence.repositories.RestingHeartRateRepository;
import tdt4140.gr1817.ecosystem.persistence.repositories.UserRepository;
import tdt4140.gr1817.ecosystem.persistence.repositories.WeightRepository;
import tdt4140.gr1817.ecosystem.persistence.repositories.WorkoutSessionRepository;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.MySqlRestingHeartRateRepository;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.MySqlUserRepository;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.MySqlWeightRepository;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.MySqlWorkoutSessionRepository;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.guice.MySqlConnectionModule;

import javax.ws.rs.core.Feature;

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
        //TODO get database configuration from system env or args

        Injector injector = Guice.createInjector(
                new MySqlConnectionModule("root", "root", "localhost",
                        "ecosystem", 3306),
//                new MySqlRepositoryModule(),
                new AbstractModule() {
                    @Override
                    protected void configure() {
                        // FIXME: replace this module with MySqlRepistoryModule when it's done
                        bind(RestingHeartRateRepository.class).to(MySqlRestingHeartRateRepository.class);
                        bind(UserRepository.class).to(MySqlUserRepository.class);
                        bind(WeightRepository.class).to(MySqlWeightRepository.class);
                        bind(WorkoutSessionRepository.class).to(MySqlWorkoutSessionRepository.class);
                    }
                },
                new AbstractModule() {
                    @Override
                    protected void configure() {
                        bind(Gson.class).toInstance(new Gson());
                    }
                });

        return injector;
    }

    private final Server server;
    private final Injector injector;

    private App(int port, @NonNull Injector injector) {
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
