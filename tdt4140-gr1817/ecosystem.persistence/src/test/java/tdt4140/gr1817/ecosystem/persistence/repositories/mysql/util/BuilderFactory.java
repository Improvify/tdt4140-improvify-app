package tdt4140.gr1817.ecosystem.persistence.repositories.mysql.util;

import tdt4140.gr1817.ecosystem.persistence.data.Goal;
import tdt4140.gr1817.ecosystem.persistence.data.RestingHeartRate;
import tdt4140.gr1817.ecosystem.persistence.data.User;
import tdt4140.gr1817.ecosystem.persistence.data.Weight;
import tdt4140.gr1817.ecosystem.persistence.data.WorkoutSession;
import tdt4140.gr1817.ecosystem.persistence.data.improvify.WorkoutPlan;
import tdt4140.gr1817.ecosystem.persistence.data.improvify.WorkoutPlanRow;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;

public class BuilderFactory {

    public static final Random random = new Random();

    public static User.UserBuilder createUser() {
        final Date birthDate = new GregorianCalendar(1995, Calendar.AUGUST, 25).getTime();
        return User.builder()
                .id(1)
                .firstName("Test")
                .lastName("Person")
                .birthDate(birthDate)
                .username("testuser-" + random.nextInt()) // Unique value per user
                .password("123")
                .email("test@test.com")
                .height(175.5f);
    }

    public static WorkoutSession.WorkoutSessionBuilder createWorkoutSession() {
        final User user = createUser().id(1).build();
        final Date time = new GregorianCalendar(2018, Calendar.JANUARY, 4).getTime();
        return WorkoutSession.builder()
                .id(1)
                .startTime(time)
                .durationSeconds(60*30)
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

    public static Goal.GoalBuilder createGoal() {

        final User user = createUser().id(1).build();

        return Goal.builder()
                .id(1)
                .description("test")
                .isCompleted(false)
                .isCurrent(true)
                .user(user);
    }

    public static WorkoutPlan.WorkoutPlanBuilder createWorkoutPlan() {
        final User user = createUser().id(1).build();
        return WorkoutPlan.builder()
                .id(1)
                .description(" kappa ")
                .createdForUser(user);
    }

    public static WorkoutPlanRow.WorkoutPlanRowBuilder createWorkoutPlanRow() {
        final WorkoutPlan workoutPlan = createWorkoutPlan().id(1).build();
        return WorkoutPlanRow.builder()
                .id(1)
                .description("It's like my father used to say: 'Jet fuel can't melt steel beems'")
                .durationSeconds(420)
                .intensity("0.8 MAX hr")
                .comment("Don't let your dreams be memes")
                .workoutPlan(workoutPlan);
    }
}
