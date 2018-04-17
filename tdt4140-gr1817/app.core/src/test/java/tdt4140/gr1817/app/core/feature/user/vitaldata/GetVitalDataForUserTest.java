package tdt4140.gr1817.app.core.feature.user.vitaldata;

import org.junit.Test;
import org.mockito.Mockito;
import tdt4140.gr1817.app.core.feature.user.HonorUserPermissions;
import tdt4140.gr1817.ecosystem.persistence.data.RestingHeartRate;
import tdt4140.gr1817.ecosystem.persistence.data.ServiceProvider;
import tdt4140.gr1817.ecosystem.persistence.data.ServiceProviderPermissions;
import tdt4140.gr1817.ecosystem.persistence.data.User;
import tdt4140.gr1817.ecosystem.persistence.data.Weight;
import tdt4140.gr1817.ecosystem.persistence.repositories.RestingHeartRateRepository;
import tdt4140.gr1817.ecosystem.persistence.repositories.ServiceProviderPermissionsRepository;
import tdt4140.gr1817.ecosystem.persistence.repositories.WeightRepository;

import java.util.Arrays;
import java.util.Collections;
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

        ServiceProviderPermissionsRepository serviceProviderPermissionsRepositoryMock = Mockito.mock(ServiceProviderPermissionsRepository.class);
        ServiceProvider serviceProvider = new ServiceProvider(1, "improvify");

        ServiceProviderPermissions permissions = new ServiceProviderPermissions(user, serviceProvider, true, true, true, true, true, true, true, true);
        Mockito.when(serviceProviderPermissionsRepositoryMock.query(Mockito.any())).thenReturn(Collections.singletonList(permissions));

        GetVitalDataForUser getVitalDataForUser = new GetVitalDataForUser(weightRepositoryMock, restingHeartRateRepositoryMock, new HonorUserPermissions(), serviceProvider, serviceProviderPermissionsRepositoryMock);

        // When
        getVitalDataForUser.setUser(user);
        GetVitalDataForUser.VitalData data = getVitalDataForUser.load();

        // Then
        assertThat(data.getUser(), is(user));
        assertThat(data.getHeartRates(), is(restingHeartRates));
        assertThat(data.getWeights(), is(weights));
    }
}