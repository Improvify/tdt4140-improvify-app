package tdt4140.gr1817.app.ui.feature.userlist;

import tdt4140.gr1817.ecosystem.persistence.data.User;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Adapts (converts) a {@link User} object from persistance to a {@link UserItem} from ui.
 *
 * @author Kristian Rekstad
 */
public class UserItemAdapter {

    public UserItem adapt(User user) {
        final GregorianCalendar birthDate = new GregorianCalendar();
        birthDate.setTime(user.getBirthDate());

        final LocalDate localBirthDate = LocalDate.of(birthDate.get(Calendar.YEAR), birthDate.get(Calendar.MONTH) + 1,
                birthDate.get(Calendar.DAY_OF_MONTH));
        final int age = (int) ChronoUnit.YEARS.between(localBirthDate, LocalDate.now());

        return new UserItem(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), age);
    }
}
