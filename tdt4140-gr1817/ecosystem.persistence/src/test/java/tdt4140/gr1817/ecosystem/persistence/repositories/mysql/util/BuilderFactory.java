package tdt4140.gr1817.ecosystem.persistence.repositories.mysql.util;

import tdt4140.gr1817.ecosystem.persistence.data.RestingHeartRate;
import tdt4140.gr1817.ecosystem.persistence.data.User;
import tdt4140.gr1817.ecosystem.persistence.data.Weight;
import tdt4140.gr1817.ecosystem.persistence.data.WorkoutSession;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class BuilderFactory {


    public static User.UserBuilder createUser() {
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

    public static WorkoutSession.WorkoutSessionBuilder createWorkoutSession() {
        final User user = createUser().id(1).build();
        final Date time = new GregorianCalendar(2018, Calendar.JANUARY, 4).getTime();
        return WorkoutSession.builder()
                .id(1)
                .time(time)
                .intensity(2)
                .kiloCalories(700)
                .averageHeartRate(180)
                .maxHeartRate(200)
                .distanceRun(5)
                .user(user);
    }

    public static Weight.WeightBuilder createWeight() {
        final User user = createUser().id(1).build();
        final Date date = new GregorianCalendar(2018, Calendar.JANUARY, 4).getTime();
        return Weight.builder()
                .id(1)
                .date(date)
                .currentWeight(69f)
                .user(user);
    }

    public static RestingHeartRate.RestingHeartRateBuilder createRestingHeartRate() {
        final Date date = new GregorianCalendar(2018, Calendar.JANUARY, 5).getTime();
        final User user = createUser().id(1).build();

        return RestingHeartRate.builder()
                .id(1)
                .heartRate(60)
                .measuredAt(date)
                .measuredBy(user);
    }
}