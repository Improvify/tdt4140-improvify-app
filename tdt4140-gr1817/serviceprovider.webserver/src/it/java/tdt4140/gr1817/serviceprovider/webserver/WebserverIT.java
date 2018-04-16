package tdt4140.gr1817.serviceprovider.webserver;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.filter.LoggingFilter;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.slf4j.LoggerFactory;
import tdt4140.gr1817.ecosystem.persistence.data.User;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.PropertyConnectionConfigurationSource;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.guice.MySqlRepositoryModule;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.util.HsqldbConnectionModule;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.util.HsqldbRule;
import tdt4140.gr1817.serviceprovider.webserver.util.JerseyGsonProvider;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class WebserverIT {

    @ClassRule
    public static HsqldbRule hsqldbRule = new HsqldbRule();

    private Client restClient;
    private WebTarget baseWebTarget;
    public static final int PORT = 2222;
    private static Thread serverThread;
    private static Injector injector;

    @BeforeClass
    public static void setUpServer() throws Exception {
        ((Logger) LoggerFactory.getLogger("org.eclipse.jetty")).setLevel(Level.WARN);

        PropertyConnectionConfigurationSource conf = new PropertyConnectionConfigurationSource();
        injector = Guice.createInjector(
                new HsqldbConnectionModule(hsqldbRule),
                new MySqlRepositoryModule()
        );


        serverThread = new Thread(() -> {
            App app = new App(PORT, injector);
            try {
                app.start();
            } catch (Exception ignored) {
                // We shut down the db with an InterruptedException :)
            }
        });
        serverThread.setDaemon(true);
        serverThread.start();
    }

    @AfterClass
    public static void tearDownServer() throws Exception {
        serverThread.interrupt();
        serverThread.join();
    }

    @Before
    public void setUp() throws Exception {
        hsqldbRule.clearData();
        restClient = createClient();
        baseWebTarget = restClient.target("http://localhost:" + PORT);
    }

    @Test
    public void userPostShouldCreate() throws Exception {
        // Given
        final Date birthDate = new GregorianCalendar(1995, Calendar.AUGUST, 8).getTime();
        WebTarget targetPath = baseWebTarget.path("user");
        User user = new User(1, "Kristian", "Test", 185, birthDate,
                "krissrex", "123", "myemail@cool.yolo");

        // When
        String post = targetPath.request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(user, MediaType.APPLICATION_JSON_TYPE), String.class);

        // Then
        assertThat(post, is("{\"message\":\"User added\"}"));
    }

    private static Client createClient() {
        ClientConfig config = new ClientConfig();
        config.register(LoggingFilter.class);
        config.register(JerseyGsonProvider.class);

        Client client = ClientBuilder.newClient(config);
        return client;
    }

}