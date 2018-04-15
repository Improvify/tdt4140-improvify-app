package tdt4140.gr1817.app.core.feature.user.vitaldata;

import org.junit.Test;
import org.mockito.Mockito;
import tdt4140.gr1817.ecosystem.persistence.data.RestingHeartRate;
import tdt4140.gr1817.ecosystem.persistence.data.User;
import tdt4140.gr1817.ecosystem.persistence.data.Weight;
import tdt4140.gr1817.ecosystem.persistence.repositories.RestingHeartRateRepository;
import tdt4140.gr1817.ecosystem.persistence.repositories.WeightRepository;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;

/**
 * Created by Kristian Rekstad.
 *
 * @author Kristian Rekstad
 */
public class GetVitalDataForUserTest {

    @Test
    public void shouldLoadVitalData() throws Exception {
        // Given
        User user = User.builder()
                .id(1)
                .build();

        WeightRepository weightRepositoryMock = Mockito.mock(WeightRepository.class);
        List<Weight> weights = Arrays.asList(new Weight(1, 60, new Date(), user));
        Mockito.when(weightRepositoryMock.query(any())).thenReturn(weights);

        RestingHeartRateRepository restingHeartRateRepositoryMock = Mockito.mock(RestingHeartRateRepository.class);
        List<RestingHeartRate> restingHeartRates = Arrays.asList(new RestingHeartRate(1, new Date(), 60, user));
        Mockito.when(restingHeartRateRepositoryMock.query(any())).thenReturn(restingHeartRates);

        GetVitalDataForUser getVitalDataForUser = new GetVitalDataForUser(weightRepositoryMock, restingHeartRateRepositoryMock);

        // When
        getVitalDataForUser.setUser(user);
        GetVitalDataForUser.VitalData data = getVitalDataForUser.load();

        // Then
        assertThat(data.getUser(), is(user));
        assertThat(data.getHeartRates(), is(restingHeartRates));
        assertThat(data.getWeights(), is(weights));
    }
}