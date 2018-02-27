package tdt4140.gr1817.ecosystem.persistence.data.improvify;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import tdt4140.gr1817.ecosystem.persistence.data.RestingHeartRate;
import tdt4140.gr1817.ecosystem.persistence.data.User;
import tdt4140.gr1817.ecosystem.persistence.data.Weight;
import tdt4140.gr1817.ecosystem.persistence.data.WorkoutSession;

/**
 * <p>The permissions for a service provider,
 * indicating what {@link #serviceProvider} can read about
 * {@link #user}.
 * </p>
 *
 *<p>Currently every permission accessor starts with {@code is},
 * which should probably be {@code canRead} instead.
 * But adding every setter bloats the class too much, so it's a tradeoff.
 * </p>
 *
 * <table>
 *     <tr><th>Permission</th><th>Data</th></tr>
 *
 *     <tr><td>{@link #isBirthDate()}</td><td>{@link User#getBirthDate()}</td></tr>
 *     <tr><td>{@link #isEmail()}</td><td>{@link User#getEmail()}</td></tr>
 *     <tr><td>{@link #isHeight()}</td><td>{@link User#getHeight()}</td></tr>
 *     <tr><td>{@link #isName()}</td><td>{@link User#getFirstName()} + {@link User#getLastName()}</td></tr>
 *     <tr><td>{@link #isRestingHeartRate()}</td><td>{@link RestingHeartRate}</td></tr>
 *     <tr><td>{@link #isUsername()}</td><td>{@link User#getUsername()}</td></tr>
 *     <tr><td>{@link #isWeight()}</td><td>{@link Weight}</td></tr>
 *     <tr><td>{@link #isWorkoutSession()}</td><td>{@link WorkoutSession}</td></tr>
 * </table>
 */
@Data
@Builder
@RequiredArgsConstructor
public class ServiceProviderPermissions {
    private @NonNull User user;
    private @NonNull ServiceProvider serviceProvider;

    private boolean weight,
            height,
            email,
            name,
            username,
            restingHeartRate,
            workoutSession,
            birthDate;
}
