package tdt4140.gr1817.ecosystem.persistence.repositories.mysql.guice;

import com.google.inject.*;
import org.junit.Ignore;
import org.junit.Test;

import java.sql.Connection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@Ignore("CI does not have a database yet")
public class MySqlConnectionModuleIT {


    @Test
    public void shouldProvideUsableConnection() throws Exception {
        // Given
        MySqlConnectionModule module = new MySqlConnectionModule("root", "", "localhost", "ecosystem", 3306);
        Injector injector = Guice.createInjector(module);
        TypeLiteral<Provider<Connection>> providerTypeLiteral = new TypeLiteral<Provider<Connection>>() {;};

        // When / Then
        try (Connection connection1 = module.provideDataSource().getConnection()) {
            assertThat(connection1.isValid(5), is(true));
        }

        try (Connection instance = injector.getInstance(Connection.class)) {
            assertThat(instance.isValid(5), is(true));
        }

        Provider<Connection> connectionProvider = injector.getInstance(Key.get(providerTypeLiteral));
        try (Connection connection = connectionProvider.get()) {
            assertThat(connection.isValid(5), is(true));
        }
    }
}