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
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.LoggerFactory;
import tdt4140.gr1817.ecosystem.persistence.data.User;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.guice.MySqlConnectionModule;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.guice.MySqlRepositoryModule;
import tdt4140.gr1817.serviceprovider.webserver.util.JerseyGsonProvider;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Ignore
public class WebserverIT {

    private Client restClient;
    private WebTarget baseWebTarget;
    public static final int PORT = 2222;
    private static Thread serverThread;
    private static Injector injector;

    @BeforeClass
    public static void setUpServer() throws Exception {
        ((Logger) LoggerFactory.getLogger("org.eclipse.jetty")).setLevel(Level.WARN);

        // TODO: Set up HSQLDB using HsqldbRule from ecosystem.persistence.
        // TODO: Add all modules needed for webserver, or user MysqlModule when it is ready
        injector = Guice.createInjector(
                new MySqlConnectionModule("root", "", "localhost", "ecosystem", 3306),
                new MySqlRepositoryModule());


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
        try (
                Connection connection = injector.getInstance(Connection.class);
                Statement statement = connection.createStatement();
        ){
            try {
                statement.execute("START TRANSACTION ");
                statement.execute("SET FOREIGN_KEY_CHECKS=0");
                final ArrayList<String> truncateStatements = new ArrayList<>();

                try (ResultSet resultSet = statement.executeQuery("SELECT Concat('TRUNCATE TABLE ',table_schema,'.',TABLE_NAME, ';') \n"
                        + "FROM INFORMATION_SCHEMA.TABLES WHERE  table_schema IN ('ecosystem');")) {
                    while (resultSet.next()) {
                        final String truncateQuery = resultSet.getString(1);
                        truncateStatements.add(truncateQuery);
                    }
                }
                for (String truncateStatement : truncateStatements) {
                    statement.execute(truncateStatement);
                }

                statement.execute("COMMIT");
            } catch (Exception ex) {
                statement.execute("ROLLBACK");
                statement.execute("SET FOREIGN_KEY_CHECKS=1");
            }
        }


        restClient = createClient();
        baseWebTarget = restClient.target("http://localhost:" + PORT);
    }

    @Test
    public void userPostShouldCreate() throws Exception {
        // Given
        final Date birthDate = new GregorianCalendar(1995, Calendar.AUGUST, 8).getTime();
        WebTarget targetPath = baseWebTarget.path("user");
        final User user = new User(1, "Kristian", "Test", 185, birthDate,
                "krissrex", "123", "krirek@stud.ntnu.no");

        String post = targetPath.request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(user, MediaType.APPLICATION_JSON_TYPE), String.class);

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