package tdt4140.gr1817.ecosystem.persistence.repositories.mysql.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class MySqlConnectionModule extends AbstractModule {

    private final String username;
    private final String password;
    private final String host;
    private final String databaseName;
    private final int port;

    public MySqlConnectionModule(String username, String password, String host, String databaseName, int port) {
        this.username = username;
        this.password = password;
        this.host = host;
        this.databaseName = databaseName;
        this.port = port;
    }

    @Override
    protected void configure() {
        // If a connection pool is ever needed for performance reasons,
        // check out c3p0 https://github.com/swaldman/c3p0

    }

    @Provides
    public DataSource provideDataSource() {
        System.out.println("Created data source");
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setUser(username);
        dataSource.setPassword(password);
        dataSource.setDatabaseName(databaseName);
        dataSource.setServerName(host);
        dataSource.setPort(port);

        return dataSource;
    }

    @Provides
    public Connection provideConnection(DataSource dataSource) throws SQLException {
        System.out.println("Created connection");
        return dataSource.getConnection();
    }

}
