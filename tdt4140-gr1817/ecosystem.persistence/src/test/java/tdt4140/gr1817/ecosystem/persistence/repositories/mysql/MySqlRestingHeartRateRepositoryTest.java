package tdt4140.gr1817.ecosystem.persistence.repositories.mysql;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;
import tdt4140.gr1817.ecosystem.persistence.data.RestingHeartRate;
import tdt4140.gr1817.ecosystem.persistence.data.User;
import tdt4140.gr1817.ecosystem.persistence.repositories.UserRepository;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.specification.GetAllRestingHeartRatesSpecification;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.specification.GetRestingHeartRateByIdSpecification;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.util.BuilderFactory;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.util.HsqldbRule;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class MySqlRestingHeartRateRepositoryTest {

    @Rule
    public HsqldbRule hsqldbRule = new HsqldbRule();

    private MySqlRestingHeartRateRepository repository;
    private RestingHeartRate.RestingHeartRateBuilder restingHeartRateBuilder;
    private User user;
    private UserRepository userRepositoryMock;

    @Before
    public void setUp() throws Exception {
        user = BuilderFactory.createUser().id(1).build();

        userRepositoryMock = Mockito.mock(UserRepository.class);
        Mockito.when(userRepositoryMock.query(Mockito.any())).thenReturn(Collections.singletonList(user));

        repository = new MySqlRestingHeartRateRepository(hsqldbRule::getConnection, userRepositoryMock);

        restingHeartRateBuilder = BuilderFactory.createRestingHeartRate()
                .measuredBy(user);


        new MySqlUserRepository(hsqldbRule::getConnection).add(user);
    }

    @Test
    public void shouldAddRestingHeartRate() throws Exception {
        // Given
        final RestingHeartRate restingHeartRate = restingHeartRateBuilder.id(1).build();

        // When
        repository.add(restingHeartRate);
        final List<RestingHeartRate> heartRates = repository.query(new GetRestingHeartRateByIdSpecification(1));

        // Then
        assertThat(heartRates, hasSize(1));
        assertThat(heartRates, hasItem(restingHeartRate));
    }

    @Test
    public void shouldAddAllHeartRates() throws Exception {
        // Given
        final RestingHeartRate heartRate1 = restingHeartRateBuilder.id(1).build();
        final RestingHeartRate heartRate2 = restingHeartRateBuilder.id(2).build();
        final RestingHeartRate heartRate3 = restingHeartRateBuilder.id(3).build();

        // When
        repository.add(Arrays.asList(heartRate1, heartRate2, heartRate3));
        final List<RestingHeartRate> heartRates = repository.query(new GetAllRestingHeartRatesSpecification());

        // Then
        assertThat(heartRates, hasSize(3));
        assertThat(heartRates, hasItems(heartRate1, heartRate2, heartRate3));
    }

    @Test
    public void shouldUpdateItem() throws Exception {
        // Given
        final RestingHeartRate heartRate = restingHeartRateBuilder.id(1).build();
        final RestingHeartRate updatedHeartRate = restingHeartRateBuilder
                .id(1)
                .heartRate(heartRate.getHeartRate() + 10)
                .build();

        // When
        repository.add(heartRate);
        repository.update(updatedHeartRate);

        final List<RestingHeartRate> heartRates = repository.query(new GetRestingHeartRateByIdSpecification(heartRate.getId()));

        // Then
        assertThat(heartRates, hasSize(1));
        assertThat(heartRates.get(0).getId(), is(1));
        assertThat(heartRates, hasItem(updatedHeartRate));
    }

    @Test
    public void shouldQuerySpecifiedItem() throws Exception {
        // Given
        final RestingHeartRate heartRate1 = restingHeartRateBuilder.id(1).build();
        final RestingHeartRate heartRate2 = restingHeartRateBuilder.id(2).build();
        final RestingHeartRate heartRate3 = restingHeartRateBuilder.id(3).build();

        // When
        repository.add(Arrays.asList(heartRate1, heartRate2, heartRate3));
        final List<RestingHeartRate> heartRates = repository.query(new GetRestingHeartRateByIdSpecification(heartRate2.getId()));

        // Then
        assertThat(heartRates, hasSize(1));
        assertThat(heartRates, hasItem(heartRate2));
    }

    @Test
    public void shouldRemoveItem() throws Exception {
        // Given
        final RestingHeartRate heartRate = restingHeartRateBuilder.id(1).build();

        // When
        repository.add(heartRate);
        repository.remove(heartRate);

        final List<RestingHeartRate> heartRates = repository.query(new GetAllRestingHeartRatesSpecification());

        // Then
        assertThat(heartRates, is(empty()));
    }

    @Test
    public void shouldRemoveAllItems() throws Exception {
        // Given
        final RestingHeartRate heartRate1 = restingHeartRateBuilder.id(1).build();
        final RestingHeartRate heartRate2 = restingHeartRateBuilder.id(2).build();
        final RestingHeartRate heartRate3 = restingHeartRateBuilder.id(3).build();

        // When
        repository.add(Arrays.asList(heartRate1, heartRate2, heartRate3));
        repository.remove(Arrays.asList(heartRate1, heartRate3));

        final List<RestingHeartRate> heartRates = repository.query(new GetAllRestingHeartRatesSpecification());

        // Then
        assertThat(heartRates, hasSize(1));
        assertThat(heartRates, hasItem(heartRate2));
    }

    @Test
    public void shouldRemoveSpecifiedItem() throws Exception {
        // Given
        final RestingHeartRate heartRate1 = restingHeartRateBuilder.id(1).build();
        final RestingHeartRate heartRate2 = restingHeartRateBuilder.id(2).build();
        final RestingHeartRate heartRate3 = restingHeartRateBuilder.id(3).build();

        // When
        repository.add(Arrays.asList(heartRate1, heartRate2, heartRate3));
        repository.remove(new GetRestingHeartRateByIdSpecification(3));

        final List<RestingHeartRate> heartRates = repository.query(new GetAllRestingHeartRatesSpecification());

        // Then
        assertThat(heartRates, hasSize(2));
        assertThat(heartRates, hasItems(heartRate1, heartRate2));
    }


}