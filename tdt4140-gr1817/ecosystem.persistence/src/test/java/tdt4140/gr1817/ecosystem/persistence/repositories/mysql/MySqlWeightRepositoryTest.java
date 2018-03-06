package tdt4140.gr1817.ecosystem.persistence.repositories.mysql;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;
import tdt4140.gr1817.ecosystem.persistence.data.User;
import tdt4140.gr1817.ecosystem.persistence.data.Weight;
import tdt4140.gr1817.ecosystem.persistence.repositories.UserRepository;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.specification.GetAllWeightsSpecification;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.specification.GetWeightByIdSpecification;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.util.BuilderFactory;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.util.HsqldbRule;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class MySqlWeightRepositoryTest {

    @Rule
    public HsqldbRule hsqldbRule = new HsqldbRule();
    private MySqlWeightRepository repository;
    private User user;
    private Weight.WeightBuilder weightBuilder;
    private UserRepository userRepositoryMock;

    @Before
    public void setUp() throws Exception {
        user = BuilderFactory.createUser().build();

        userRepositoryMock = Mockito.mock(UserRepository.class);
        Mockito.when(userRepositoryMock.query(Mockito.any())).thenReturn(Collections.singletonList(user));

        repository = new MySqlWeightRepository(hsqldbRule::getConnection, userRepositoryMock);
        weightBuilder = BuilderFactory.createWeight().user(user);

        new MySqlUserRepository(hsqldbRule::getConnection)
                .add(user);
    }

    @Test
    public void shouldAddWeight() throws Exception {
        // Given
        final Weight weight = weightBuilder.id(1).build();

        // When
        repository.add(weight);

        final List<Weight> weights = repository.query(new GetWeightByIdSpecification(1));

        // Then
        assertThat(weights, hasSize(1));
        assertThat(weights, hasItem(weight));
    }

    @Test
    public void shouldAddAllWeights() throws Exception {
        // Given
        final Weight weight1 = weightBuilder.id(1).build();
        final Weight weight2 = weightBuilder.id(2).build();
        final Weight weight3 = weightBuilder.id(3).build();


        // When
        repository.add(Arrays.asList(weight1, weight2, weight3));
        final List<Weight> weights = repository.query(new GetAllWeightsSpecification());


        // Then
        assertThat(weights, hasSize(3));
        assertThat(weights, hasItems(weight1, weight2, weight3));
    }

    @Test
    public void shouldUpdateItem() throws Exception {
        // Given
        final Weight weight = weightBuilder.id(1).build();
        final Weight updatedWeight = weightBuilder
                .id(1)
                .currentWeight(weight.getCurrentWeight() + 25f)
                .build();


        // When
        repository.add(weight);
        repository.update(updatedWeight);

        final List<Weight> weights = repository.query(new GetWeightByIdSpecification(weight.getId()));

        // Then
        assertThat(weights, hasSize(1));
        assertThat(weights.get(0).getId(), is(weight.getId()));
        assertThat(weights, hasItem(updatedWeight));
    }

    @Test
    public void shouldRemoveWeight() throws Exception {
        // Given
        final Weight weight1 = weightBuilder.id(1).build();
        final Weight weight2 = weightBuilder.id(2).build();

        // When
        repository.add(Arrays.asList(weight1, weight2));
        repository.remove(weight1);

        final List<Weight> weights = repository.query(new GetAllWeightsSpecification());

        // Then
        assertThat(weights, hasSize(1));
        assertThat(weights, hasItem(weight2));
    }

    @Test
    public void shouldRemoveAllWeights() throws Exception {
        // Given
        final Weight weight1 = weightBuilder.id(1).build();
        final Weight weight2 = weightBuilder.id(2).build();
        final Weight weight3 = weightBuilder.id(3).build();

        // When
        repository.add(Arrays.asList(weight1, weight2, weight3));
        repository.remove(Arrays.asList(weight1, weight3));

        final List<Weight> weights = repository.query(new GetAllWeightsSpecification());

        // Then
        assertThat(weights, hasSize(1));
        assertThat(weights, hasItem(weight2));
    }

    @Test
    public void shouldRemoveSpecifiedWeights() throws Exception {
        // Given
        final Weight weight1 = weightBuilder.id(1).build();
        final Weight weight2 = weightBuilder.id(2).build();

        // When
        repository.add(Arrays.asList(weight1, weight2));
        repository.remove(new GetWeightByIdSpecification(2));

        final List<Weight> weights = repository.query(new GetAllWeightsSpecification());

        // Then
        assertThat(weights, hasSize(1));
        assertThat(weights, hasItem(weight1));
    }

    @Test
    public void shouldSelectSpecifiedItems() throws Exception {
        // Given
        final Weight weight1 = weightBuilder.id(1).build();
        final Weight weight2 = weightBuilder.id(2).build();
        final Weight weight3 = weightBuilder.id(3).build();

        // When
        repository.add(Arrays.asList(weight1, weight2, weight3));
        final List<Weight> weights = repository.query(new GetWeightByIdSpecification(2));

        // Then
        assertThat(weights, hasSize(1));
        assertThat(weights, hasItem(weight2));
    }
}