package tdt4140.gr1817.ecosystem.persistence.repositories.mysql;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.Statement;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Created by Kristian Rekstad.
 *
 * @author Kristian Rekstad
 */
public class PropertyConnectionConfigurationSourceTest {

    @Before
    public void setUp() throws Exception {
        System.clearProperty(PropertyConnectionConfigurationSource.KEY_USER);
        System.clearProperty(PropertyConnectionConfigurationSource.KEY_PASSWORD);
        System.clearProperty(PropertyConnectionConfigurationSource.KEY_HOST);
        System.clearProperty(PropertyConnectionConfigurationSource.KEY_PORT);
    }

    @Test
    public void shouldSetSaneDefaults() throws Exception {
        // Given
        System.clearProperty(PropertyConnectionConfigurationSource.KEY_USER);
        System.clearProperty(PropertyConnectionConfigurationSource.KEY_PASSWORD);
        System.clearProperty(PropertyConnectionConfigurationSource.KEY_HOST);
        System.clearProperty(PropertyConnectionConfigurationSource.KEY_PORT);
        PropertyConnectionConfigurationSource conf = new PropertyConnectionConfigurationSource();

        // Then
        assertThat(conf.user, is("root"));
        assertThat(conf.password, is("root"));
        assertThat(conf.host, is("localhost"));
        assertThat(conf.port, is(3306));
    }

    @Test
    public void shouldReadProperties() throws Exception {
        // Given
        System.setProperty(PropertyConnectionConfigurationSource.KEY_USER, "testU");
        System.setProperty(PropertyConnectionConfigurationSource.KEY_PASSWORD, "testP");
        System.setProperty(PropertyConnectionConfigurationSource.KEY_HOST, "testH");
        System.setProperty(PropertyConnectionConfigurationSource.KEY_PORT, "3306");
        PropertyConnectionConfigurationSource conf = new PropertyConnectionConfigurationSource();

        // Then
        assertThat(conf.user, is("testU"));
        assertThat(conf.password, is("testP"));
        assertThat(conf.host, is("testH"));
        assertThat(conf.port, is(3306));
    }

    @Test(expected = NumberFormatException.class)
    public void shouldThrowOnInvalidPort() throws Exception {
        System.setProperty(PropertyConnectionConfigurationSource.KEY_PORT, "string is not a number? Lies!");

        // When
        PropertyConnectionConfigurationSource conf = new PropertyConnectionConfigurationSource();
    }

    @Test
    public void shouldCloseValidConnection() throws Exception {
        // Given
        Connection connectionMock = Mockito.mock(Connection.class);
        Mockito.when(connectionMock.createStatement())
                .thenReturn(Mockito.mock(Statement.class));
        PropertyConnectionConfigurationSource conf = new PropertyConnectionConfigurationSource();

        // When
        try {
            conf.validate(connectionMock);
        } catch (NullPointerException ignored) {
        }

        // Then
        Mockito.verify(connectionMock).close();
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowInInvalidConnection() throws Exception {
        // Given
        Connection connectionMock = Mockito.mock(Connection.class);
        PropertyConnectionConfigurationSource conf = new PropertyConnectionConfigurationSource();

        // When
        conf.validate(connectionMock);
    }
}