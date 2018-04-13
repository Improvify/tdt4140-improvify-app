package tdt4140.gr1817.ecosystem.persistence.repositories.mysql;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Obtains a {@link Connection} configuration from {@link System#getProperty(String)} to connect to a database.
 * Properties are read at construction time.
 *
 * <p>
 * Properties are:
 * <blockquote>
 * {@value #KEY_USER}<br/>
 * {@value #KEY_PASSWORD}<br/>
 * {@value #KEY_HOST}<br/>
 * {@value #KEY_PORT}
 * </blockquote>
 * </p>
 *
 * <p>
 * Set properties with {@code -Ddb.user=root -Ddb.password="" -db.port=3306}
 * when running the program.
 * <br/>
 * <i>Run configurations</i> in intelliJ can set this as a VM option.
 * </p>
 *
 * @author Kristian Rekstad
 */
public class PropertyConnectionConfigurationSource {

    public static final String KEY_USER = "db.user";
    public static final String KEY_PASSWORD = "db.password";
    public static final String KEY_HOST = "db.host";
    public static final String KEY_PORT = "db.port";

    public final String user = System.getProperty(KEY_USER, "root");
    public final String password = System.getProperty(KEY_PASSWORD, "root");
    public final String host = System.getProperty(KEY_HOST, "localhost");
    public final int port = Integer.valueOf(System.getProperty(KEY_PORT, "3306"), 10);

    /**
     * Throws an exception if the connection is invalid.
     * Automatically closes the provided connection.
     * <p>
     * Note: acutally obtaining the {@link Connection} can throw as well, before this method is called.
     *
     * @param connection
     * @throws IllegalStateException
     */
    public void validate(Connection connection) throws IllegalStateException {
        try {

            try (
                    Statement statement = connection.createStatement();
            ) {
                statement.execute("SELECT 1");

            } catch (Exception e) {
                throw new IllegalStateException("Database connection is invalid! "
                        + "Verify that username and password are correct", e);
            }
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException("Failed to close connection", e);
            }
        }
    }
}
