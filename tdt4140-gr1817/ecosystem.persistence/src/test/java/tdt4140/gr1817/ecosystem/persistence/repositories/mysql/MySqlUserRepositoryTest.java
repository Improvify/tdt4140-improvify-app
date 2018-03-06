package tdt4140.gr1817.ecosystem.persistence.repositories.mysql;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;
import tdt4140.gr1817.ecosystem.persistence.data.User;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.specification.GetUserByIdSpecification;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.specification.SqlSpecification;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.util.HsqldbRule;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
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

    @Test
    public void shouldQueryUsingSpecification() throws Exception {
        // Given
        SqlSpecification specification = new GetUserByIdSpecification(1);
        final MySqlUserRepository repository = new MySqlUserRepository(() -> hsqldb.getConnection());
        final User user = createUser().build();

        // When
        repository.add(user);
        final List<User> users = repository.query(specification);

        // Then
        assertThat(users, hasSize(1));
        assertThat(users, hasItem(user));
    }

    private static User.UserBuilder createUser() {
        final Date birthDate = new GregorianCalendar(1995, Calendar.AUGUST, 25).getTime();
        return User.builder()
                .id(1)
                .firstName("Test")
                .lastName("Person")
                .birthDate(birthDate)
                .username("testuser")
                .password("123")
                .email("test@test.com")
                .height(175.5f);
    }
}