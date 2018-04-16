package tdt4140.gr1817.ecosystem.persistence.repositories.mysql;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;
import tdt4140.gr1817.ecosystem.persistence.data.Goal;
import tdt4140.gr1817.ecosystem.persistence.data.User;
import tdt4140.gr1817.ecosystem.persistence.repositories.UserRepository;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.specification.GetAllGoalsSpecification;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.specification.GetGoalByIdSpecification;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.util.BuilderFactory;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.util.HsqldbRule;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class MySqlGoalRepositoryTest {

    @Rule
    public HsqldbRule hsqldbRule = new HsqldbRule();

    private MySqlGoalRepository repository;
    private Goal.GoalBuilder goalBuilder;
    private User user;
    private UserRepository userRepositoryMock;

    @Before
    public void setUp() throws Exception {
        user = BuilderFactory.createUser().id(1).build();

        userRepositoryMock = Mockito.mock(UserRepository.class);
        Mockito.when(userRepositoryMock.query(Mockito.any())).thenReturn(Collections.singletonList(user));

        repository = new MySqlGoalRepository(hsqldbRule::getConnection, userRepositoryMock);


        goalBuilder = BuilderFactory.createGoal()
                .user(user);


        new MySqlUserRepository(hsqldbRule::getConnection).add(user);
    }

    @Test
    public void shouldAddGoal() throws Exception {
        // Given

        final Goal goal = goalBuilder.id(1).build();

        // When
        repository.add(goal);
        final List<Goal> goals = repository.query(new GetGoalByIdSpecification(1));

        // Then
        assertThat(goals, hasSize(1));
        assertThat(goals, hasItem(goal));
    }

    @Test
    public void shouldAddAllGoals() throws Exception {
        // Given
        final Goal goal1 = goalBuilder.id(1).build();
        final Goal goal2 = goalBuilder.id(2).build();
        final Goal goal3 = goalBuilder.id(3).build();

        // When
        repository.add(Arrays.asList(goal1, goal2, goal3));
        final List<Goal> goals = repository.query(new GetAllGoalsSpecification());

        // Then
        assertThat(goals, hasSize(3));
        assertThat(goals, hasItems(goal1, goal2, goal3));
    }

    @Test
    public void shouldUpdateItem() throws Exception {
        // Given
        final Goal goal = goalBuilder
                .id(1)
                .isCompleted(false)
                .build();

        final Goal updatedHeartRate = goalBuilder
                .id(1)
                .isCompleted(true)
                .build();

        // When
        repository.add(goal);
        repository.update(updatedHeartRate);

        final List<Goal> goals = repository.query(new GetGoalByIdSpecification(goal.getId()));

        // Then
        assertThat(goals, hasSize(1));
        assertThat(goals.get(0).getId(), is(1));
        assertThat(goals, hasItem(updatedHeartRate));
    }

    @Test
    public void shouldQuerySpecifiedItem() throws Exception {
        // Given
        final Goal goal1 = goalBuilder.id(1).build();
        final Goal goal2 = goalBuilder.id(2).build();
        final Goal goal3 = goalBuilder.id(3).build();

        // When
        repository.add(Arrays.asList(goal1, goal2, goal3));
        final List<Goal> goals = repository.query(new GetGoalByIdSpecification(goal2.getId()));

        // Then
        assertThat(goals, hasSize(1));
        assertThat(goals, hasItem(goal2));
    }

    @Test
    public void shouldRemoveItem() throws Exception {
        // Given
        final Goal goal = goalBuilder.id(1).build();

        // When
        repository.add(goal);
        repository.remove(goal);

        final List<Goal> goals = repository.query(new GetAllGoalsSpecification());

        // Then
        assertThat(goals, is(empty()));
    }

    @Test
    public void shouldRemoveAllItems() throws Exception {
        // Given
        final Goal goal1 = goalBuilder.id(1).build();
        final Goal goal2 = goalBuilder.id(2).build();
        final Goal goal3 = goalBuilder.id(3).build();

        // When
        repository.add(Arrays.asList(goal1, goal2, goal3));
        repository.remove(Arrays.asList(goal1, goal3));

        final List<Goal> goals = repository.query(new GetAllGoalsSpecification());

        // Then
        assertThat(goals, hasSize(1));
        assertThat(goals, hasItem(goal2));
    }

    @Test
    public void shouldRemoveSpecifiedItem() throws Exception {
        // Given
        final Goal goal1 = goalBuilder.id(1).build();
        final Goal goal2 = goalBuilder.id(2).build();
        final Goal goal3 = goalBuilder.id(3).build();

        // When
        repository.add(Arrays.asList(goal1, goal2, goal3));
        repository.remove(new GetGoalByIdSpecification(3));

        final List<Goal> goals = repository.query(new GetAllGoalsSpecification());

        // Then
        assertThat(goals, hasSize(2));
        assertThat(goals, hasItems(goal1, goal2));
    }

}