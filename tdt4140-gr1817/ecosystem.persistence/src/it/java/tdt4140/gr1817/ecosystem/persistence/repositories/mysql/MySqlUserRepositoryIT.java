package tdt4140.gr1817.ecosystem.persistence.repositories.mysql;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import tdt4140.gr1817.ecosystem.persistence.data.User;

import javax.inject.Provider;
import java.sql.*;
import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@Ignore("CI cant run this without a mysql service")
public class MySqlUserRepositoryIT {

    private Provider<Connection> connection;

    @Before
    public void setUp() throws Exception {
        connection = new Provider<Connection>() {
            @Override
            public Connection get() {
                try {
                    return DriverManager.getConnection("jdbc:mysql://localhost:3306/ecosystem", "root", "");
                } catch (SQLException e) {
                    throw new RuntimeException("Failed to connect", e);
                }
            }
        };


        cleanup();
    }


    @After
    public void tearDown() throws Exception {
        cleanup();
    }

    @Test
    public void shouldInsertUserIntoDatabase() throws Exception {
        // Given
        MySqlUserRepository repository = new MySqlUserRepository(connection);
        User user = User.builder()
                .id(1)
                .firstName("Test")
                .lastName("Person")
                .birthDate(new Date())
                .username("testuser")
                .password("123")
                .email("test@user.com")
                .build();

        // When
        repository.add(user);

        // Then
        try (
                Connection con = connection.get();
                ResultSet resultSet = con.createStatement().executeQuery("SELECT * FROM useraccount WHERE id = 1")
        ) {

            String username = null;
            if (resultSet.next()) {
                username = resultSet.getString("firstname");
            }

            assertThat(username, is("Test"));
        }
    }

    private void cleanup() throws SQLException {
        try (Connection con = connection.get(); Statement statement = con.createStatement()) {
            statement.execute("DELETE FROM useraccount WHERE 1");
        }
    }

}