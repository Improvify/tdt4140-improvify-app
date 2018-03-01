package tdt4140.gr1817.ecosystem.persistence.repositories.mysql;

import lombok.extern.slf4j.Slf4j;
import org.hsqldb.jdbcDriver;
import org.hsqldb.server.Server;
import org.junit.*;
import org.slf4j.LoggerFactory;
import tdt4140.gr1817.ecosystem.persistence.data.User;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.util.HsqldbRule;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Date;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class MySqlUserRepositoryTest {

    @Rule
    HsqldbRule hsqldb = new HsqldbRule();

    @Test
    public void shouldAddUser() throws Exception {
        // BAD TEST. Just written as an example. Maybe use integration tests instead.

        // Given
        Connection connection = mock(Connection.class);
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        MySqlUserRepository repository = new MySqlUserRepository(() -> connection);
        User user = createUser(1);

        // When
        repository.add(user);

        // Then
        verify(connection).prepareStatement(anyString());
        verify(preparedStatement, never()).setObject(eq(0), any());
        verify(preparedStatement).setObject(anyInt(), eq("Test"));
        verify(preparedStatement).setObject(anyInt(), eq("Person"));
        verify(preparedStatement).setObject(anyInt(), eq(1));
    }

    private static User createUser(int id) {
        return User.builder()
                .id(id)
                .firstName("Test")
                .lastName("Person")
                .birthDate(new Date())
                .build();
    }
}