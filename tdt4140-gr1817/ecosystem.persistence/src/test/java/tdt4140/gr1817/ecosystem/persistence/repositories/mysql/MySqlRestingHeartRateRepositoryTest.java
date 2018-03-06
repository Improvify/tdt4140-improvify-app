package tdt4140.gr1817.ecosystem.persistence.repositories.mysql;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;
import tdt4140.gr1817.ecosystem.persistence.data.RestingHeartRate;
import tdt4140.gr1817.ecosystem.persistence.data.User;
import tdt4140.gr1817.ecosystem.persistence.repositories.UserRepository;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.specification.GetRestingHeartRateById;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.util.BuilderFactory;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.util.HsqldbRule;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;

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
        final List<RestingHeartRate> heartRates = repository.query(new GetRestingHeartRateById(1));

        // Then
        assertThat(heartRates, hasSize(1));
        assertThat(heartRates, hasItem(restingHeartRate));
    }

    @Test
    public void add1() throws Exception {
    }

    @Test
    public void update() throws Exception {
    }

    @Test
    public void remove() throws Exception {
    }

    @Test
    public void remove1() throws Exception {
    }

    @Test
    public void remove2() throws Exception {
    }

    @Test
    public void query() throws Exception {
    }
}