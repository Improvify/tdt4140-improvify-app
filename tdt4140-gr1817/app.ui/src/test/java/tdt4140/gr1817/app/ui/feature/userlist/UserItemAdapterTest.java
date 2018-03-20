package tdt4140.gr1817.app.ui.feature.userlist;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import tdt4140.gr1817.ecosystem.persistence.data.User;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Created by Kristian Rekstad.
 *
 * @author Kristian Rekstad
 */
public class UserItemAdapterTest {

    private User.UserBuilder userBuilder;
    private Date birthDate;
    private final int age = 22;

    @Before
    public void setUp() throws Exception {
        final GregorianCalendar calendar = new GregorianCalendar();
        calendar.add(Calendar.YEAR, -age);
        birthDate = calendar.getTime();

        userBuilder = User.builder()
                .id(1)
                .firstName("Test")
                .lastName("Person")
                .birthDate(birthDate)
                .height(185)
                .email("test@test.com")
                .password("123");
    }

    @Test
    public void shouldConvertUser() throws Exception {
        // Given
        final User user = userBuilder.build();
        User userSpy = Mockito.spy(user);


        final UserItemAdapter adapter = new UserItemAdapter();

        // When
        final UserItem userItem = adapter.adapt(userSpy);

        // Then
        assertThat(userItem.getFirstName(), is(user.getFirstName()));
        assertThat(userItem.getLastName(), is(user.getLastName()));
        assertThat(userItem.getAge(), is(age));
        assertThat(userItem.getEmail(), is(user.getEmail()));

        Mockito.verify(userSpy, Mockito.never()).getPassword();
    }

    @Test
    public void shouldHaveCorrectAgeWhenBirthdayIsTomorrow() throws Exception {
        // Given
        final GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(birthDate);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        final Date birthDayTomorrowDate = calendar.getTime();

        final User user = userBuilder
                .birthDate(birthDayTomorrowDate)
                .build();
        User userSpy = Mockito.spy(user);

        final UserItemAdapter adapter = new UserItemAdapter();

        // When
        final UserItem userItem = adapter.adapt(userSpy);

        // Then
        assertThat(userItem.getAge(), is(age - 1));

        Mockito.verify(userSpy, Mockito.never()).getPassword();
    }
}