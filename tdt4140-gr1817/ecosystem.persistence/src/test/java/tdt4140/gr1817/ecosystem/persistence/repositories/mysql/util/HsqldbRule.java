package tdt4140.gr1817.ecosystem.persistence.repositories.mysql.util;

import org.hsqldb.jdbcDriver;
import org.hsqldb.server.Server;
import org.junit.rules.ExternalResource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Starts an in-memory {@link Server}.
 */
public class HsqldbRule extends ExternalResource {

    private Server server;

    static {
        try {
            DriverManager.registerDriver(jdbcDriver.driverInstance);
        } catch (SQLException e) {
            throw new RuntimeException("Unable to load HSQLDB driver", e);
        }
    }

    @Override
    protected void before() throws Throwable {
        server = new Server();
        server.setAddress("localhost");
        server.setPort(1234);
        server.setDatabaseName(0, "ecosystem");
        server.setDatabasePath(0, "mem:ecosystem"); // in-memory database
        server.setTrace(true);
        server.start();

        try (Connection connection = getConnection()) {
            connection.createStatement().execute("TRUNCATE SCHEMA ecosystem RESTART IDENTITY AND COMMIT NO CHECK");
        }
    }

    @Override
    protected void after() {
        server.shutdown();
    }

    public Connection getConnection() {
        final String HSQLDB_DEFAULT_USER = "SA";
        try {
            DriverManager.getConnection("jdbc:hsqldb:hsql://localhost:1234/ecosystem", HSQLDB_DEFAULT_USER, "");
        } catch (SQLException e) {
            throw new RuntimeException("Unable to get HSQLDB connection", e);
        }

    }
}
