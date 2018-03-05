package serviceproviderwebserver;

import com.google.gson.Gson;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.servlet.GuiceFilter;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.ServiceLocatorProvider;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.jvnet.hk2.guice.bridge.api.GuiceBridge;
import org.jvnet.hk2.guice.bridge.api.GuiceIntoHK2Bridge;
import tdt4140.gr1817.ecosystem.persistence.repositories.UserRepository;
import tdt4140.gr1817.ecosystem.persistence.repositories.WorkoutSessionRepository;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.MySqlUserRepository;

import javax.servlet.DispatcherType;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.EnumSet;

public class App {

    private App() {}

    public static void main(String[] args) throws Exception {
        Injector injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(UserRepository.class).to(MySqlUserRepository.class);
                //bind(WorkoutSessionRepository.class).toInstance(null);
                bind(Gson.class).toInstance(new Gson());
            }

            @Provides
            public Connection provideConnection() throws SQLException {
                return new MysqlDataSource().getConnection("root", "root");
            }
        });
/*
        GuiceServletContextListener guiceServletContextListener = new GuiceServletContextListener() {
            @Override
            protected Injector getInjector() {
                return injector;
            }
        };
*/


        ResourceConfig config = new ResourceConfig();
        config.packages("serviceproviderwebserver");
        config.register(new Feature() {
            @Override
            public boolean configure(FeatureContext context) {
                ServiceLocator serviceLocator = ServiceLocatorProvider.getServiceLocator(context);
                GuiceBridge.getGuiceBridge().initializeGuiceBridge(serviceLocator);
                GuiceIntoHK2Bridge guiceBridge = serviceLocator.getService(GuiceIntoHK2Bridge.class);
                guiceBridge.bridgeGuiceInjector(injector);
                return true;
            }
        });
        ServletContainer jerseyServlet = new ServletContainer(config);

        ServletHolder servlet = new ServletHolder(jerseyServlet);

        Server server = new Server(2222);
        ServletContextHandler context = new ServletContextHandler(server, "/*");

        context.addFilter(GuiceFilter.class, "/*", EnumSet.of(DispatcherType.REQUEST));
        context.addServlet(servlet, "/*");


        //context.addEventListener(guiceServletContextListener);

        try {
            server.start();
            server.join();
        } catch (Exception e){
            server.stop();
            server.destroy();
        }

    }

}
