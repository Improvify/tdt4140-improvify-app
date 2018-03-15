package tdt4140.gr1817.ecosystem.persistence.repositories.mysql.util;

import lombok.extern.slf4j.Slf4j;
import org.hsqldb.jdbcDriver;
import org.hsqldb.server.Server;
import org.junit.Rule;
import org.junit.rules.ExternalResource;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Starts an in-memory {@link Server} using JUnit {@link Rule}
 * <p>
 * <p>
 * Add to a test using rule annotation: <br/>
 * {@code @Rule public HsqldbRule hsqldb = new HsqldbRule(); }
 * </p>
 * <p>
 * <p>
 * Get a {@link Connection} using {@link #getConnection() hsqldb.getConnection()}
 * </p>
 * <p>
 * <p>For more info about HSQLDB, see <a href="http://www.hsqldb.org">http://www.hsqldb.org</a></p>
 */
@Slf4j
public class HsqldbRule extends ExternalResource {

    public static final String HSQLDB_DEFAULT_USER = "SA";
    public static final String HSQLDB_DEFAULT_PASSWORD = "";

    static {
        try {
            DriverManager.registerDriver(jdbcDriver.driverInstance);
        } catch (SQLException e) {
            throw new RuntimeException("Unable to load HSQLDB driver", e);
        }
    }

    private final String host = "localhost";
    private final int port = 3305;

    private Server server;
    private final boolean logStatements;

    /**
     * Creates a new rule with logging disabled
     */
    public HsqldbRule() {
        this(false);
    }

    /**
     * Create a new rule
     *
     * @param logStatements log statements to console. Creates a lot of spam when <code>true</code>
     */
    public HsqldbRule(boolean logStatements) {
        this.logStatements = logStatements;
    }

    /**
     * Initializes a new in-memory Hsqldb server instance.
     *
     * @throws Throwable
     */
    @Override
    protected void before() throws Throwable {
        loadSchema();
    }

    /**
     * Shuts down the database, deleting all schemas and data.
     */
    @Override
    protected void after() {
        clearData();
    }

    public void clearData() {
        try (Connection connection = getConnection()) {
            connection.createStatement().execute("TRUNCATE SCHEMA public RESTART IDENTITY AND COMMIT NO CHECK");
        } catch (Exception ex) {
            throw new RuntimeException("Unable to empty db", ex);
        }
    }

    public Connection getConnection() {
        try {
            String url = "jdbc:hsqldb:mem:ecosystem;sql.syntax_mys=true";
            if (logStatements) {
                url += ";hsqldb.sqllog=3";
            }
            return DriverManager.getConnection(url, HSQLDB_DEFAULT_USER, HSQLDB_DEFAULT_PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException("Unable to get HSQLDB connection", e);
        }
    }

    private static String[] statementCache = null;

    /**
     * Loads the databse schema into the database
     */
    private void loadSchema() {
        try (
                Connection connection = getConnection();
                Statement statement = connection.createStatement()
        ) {
            if (statementCache == null) {
                final String sql = readSchemaSql();
                final String[] statements = sql.split(";");
                statementCache = statements;
            }
            for (String statementSql : statementCache) {
//                System.out.println("Executing statement: " + statementSql);
                statement.execute(statementSql);
            }
        } catch (Exception ex) {
            throw new RuntimeException("Unable to update schema", ex);
        }

    }

    /**
     * Reads the database schema from file
     *
     * @return A String containing all statements in the sql file
     */
    private String readSchemaSql() {
        final String sqlFile = "/mainDB create ecosystem.sql";

        try (
                BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(sqlFile)))
        ) {
            Stream<String> lineStream = reader.lines();
            lineStream = convertMysqlToHsqlSchema(lineStream);
            final List<String> lines = lineStream
                    .collect(Collectors.toList());

            final StringJoiner joiner = new StringJoiner("\n");
            for (final String line : lines) {
                joiner.add(line);
            }

            return joiner.toString();
        } catch (Exception ex) {
            throw new RuntimeException("Unable to load SQL", ex);
        }
    }

    /**
     * Removes or changes statements that are valid in MySQL but not HSQLDB
     *
     * @param sqlStream stream of lines with statements
     * @return Altered stream
     */
    private static Stream<String> convertMysqlToHsqlSchema(Stream<String> sqlStream) {
        return sqlStream
                .map(String::trim)
                .filter(line -> !(
                        line.startsWith("SET ")
                                || line.isEmpty()
                                || line.startsWith("-- ")
                                || line.startsWith("DROP SCHEMA")
                                || line.startsWith("CREATE SCHEMA")
                                || line.startsWith("USE ")
                                || "ENGINE = InnoDB".equals(line)
                                || line.startsWith("INDEX "))
                )
                .map(line -> {
                    if ("ENGINE = InnoDB;".equals(line)) {
                        return ";";
                    }
                    return line
                            .replace("`", "")
                            //.replace("IF NOT EXISTS", "")
//                            .replace("INT", "INTEGER") // bugs with CONSTRAINT
                            .replace("AUTO_INCREMENT", "GENERATED BY DEFAULT AS IDENTITY")
                            .replace("TINYINT(1)", "BOOLEAN")
                            .replace("TEXT", "LONGVARCHAR")
                            .replace("TIMESTAMP NOT NULL DEFAULT NOW()", "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
                            .replace("TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP", "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
                            .replace("BOOLEAN NOT NULL DEFAULT 0", "BOOLEAN DEFAULT FALSE")
                            .replaceAll("FLOAT\\(\\d+,\\d+\\)", "FLOAT")
                            .replaceAll("COMMENT (= )*'[^']*'", "");
                });
    }

}
