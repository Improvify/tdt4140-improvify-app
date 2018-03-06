package tdt4140.gr1817.ecosystem.persistence.repositories.mysql;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;
import tdt4140.gr1817.ecosystem.persistence.data.User;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.util.HsqldbRule;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;

public class MySqlUserRepositoryTest {

    @Rule
    public HsqldbRule hsqldb = new HsqldbRule();

    @Test
    public void shouldAddUser() throws Exception {
        // Given
        final Connection connectionSpy = Mockito.spy(hsqldb.getConnection()); // Will be closed
        MySqlUserRepository repository = new MySqlUserRepository(() -> connectionSpy);
        final Date birthDate = new GregorianCalendar(1995, 8, 20).getTime();

        User user = createUser()
                .id(1)
                .birthDate(birthDate)
                .build();

        // When
        repository.add(user);

        // Then
        verify(connectionSpy).prepareStatement(anyString());
        verify(connectionSpy).close();

        try (Connection connection = hsqldb.getConnection()) {
            try (ResultSet resultSet = connection.createStatement().executeQuery("SELECT * FROM useraccount WHERE  id = 1")) {
                while (resultSet.next()) {
                    assertThat(resultSet.getString("firstname"), is("Test"));
                    assertThat(resultSet.getString("lastname"), is("Person"));
                    assertThat(resultSet.getString("email"), is("test@test.com"));
                    assertThat(resultSet.getString("password"), is("123"));
                    final long birthDate1 = resultSet.getDate("birthDate").getTime();
                    assertThat(birthDate1, is(birthDate.getTime()));
                }
            }
        }
    }

    private static User.UserBuilder createUser() {
        return User.builder()
                .id(1)
                .firstName("Test")
                .lastName("Person")
                .birthDate(new Date())
                .username("testuser")
                .password("123")
                .email("test@test.com")
                .height(175.5f);
    }
}