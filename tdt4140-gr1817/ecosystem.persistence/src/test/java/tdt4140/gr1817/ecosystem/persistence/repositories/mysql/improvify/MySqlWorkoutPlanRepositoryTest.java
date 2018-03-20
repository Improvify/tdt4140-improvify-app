package tdt4140.gr1817.ecosystem.persistence.repositories.mysql.improvify;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;
import tdt4140.gr1817.ecosystem.persistence.data.User;
import tdt4140.gr1817.ecosystem.persistence.data.improvify.WorkoutPlan;
import tdt4140.gr1817.ecosystem.persistence.repositories.UserRepository;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.MySqlUserRepository;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.specification.GetAllWorkoutPlansSpecification;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.specification.GetWorkoutPlanByIdSpecification;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.util.BuilderFactory;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.util.HsqldbRule;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


/**
 * Created by Kristian Rekstad.
 *
 * @author Kristian Rekstad
 */
public class MySqlWorkoutPlanRepositoryTest {

    @Rule
    public HsqldbRule hsqldbRule = new HsqldbRule();
    private MySqlWorkoutPlanRepository repository;
    private User user;
    private WorkoutPlan.WorkoutPlanBuilder workoutPlanBuilder;
    private UserRepository userRepositoryMock;

    @Before
    public void setUp() throws Exception {
        user = BuilderFactory.createUser().build();

        userRepositoryMock = Mockito.mock(UserRepository.class);
        Mockito.when(userRepositoryMock.query(Mockito.any())).thenReturn(Collections.singletonList(user));

        repository = new MySqlWorkoutPlanRepository(hsqldbRule::getConnection, userRepositoryMock);
        workoutPlanBuilder = BuilderFactory.createWorkoutPlan()
                .createdForUser(user);

        new MySqlUserRepository(hsqldbRule::getConnection)
                .add(user); // Prevent foreign key issues
    }

    @Test
    public void shouldAddWorkoutPlan() throws Exception {
        // Given
        final WorkoutPlan workoutPlan = workoutPlanBuilder.id(1).build();

        // When
        repository.add(workoutPlan);

        final List<WorkoutPlan> workoutPlans = repository.query(new GetWorkoutPlanByIdSpecification(1));

        // Then
        assertThat(workoutPlans, hasSize(1));
        assertThat(workoutPlans, hasItem(workoutPlan));
    }

    @Test
    public void shouldAddAllWorkoutPlans() throws Exception {
        // Given
        final WorkoutPlan workoutPlan1 = workoutPlanBuilder.id(1).build();
        final WorkoutPlan workoutPlan2 = workoutPlanBuilder.id(2).build();
        final WorkoutPlan workoutPlan3 = workoutPlanBuilder.id(3).build();


        // When
        repository.add(Arrays.asList(workoutPlan1, workoutPlan2, workoutPlan3));
        final List<WorkoutPlan> workoutPlans = repository.query(new GetAllWorkoutPlansSpecification());


        // Then
        assertThat(workoutPlans, hasSize(3));
        assertThat(workoutPlans, hasItems(workoutPlan1, workoutPlan2, workoutPlan3));
    }

    @Test
    public void shouldUpdateItem() throws Exception {
        // Given
        final WorkoutPlan workoutPlan = workoutPlanBuilder
                .id(1)
                .description("My first description")
                .build();
        final WorkoutPlan updatedWorkoutPlan = workoutPlanBuilder
                .id(1)
                .description("This changed!")
                .build();


        // When
        repository.add(workoutPlan);
        repository.update(updatedWorkoutPlan);

        final List<WorkoutPlan> workoutPlans = repository.query(new GetWorkoutPlanByIdSpecification(workoutPlan.getId()));

        // Then
        assertThat(workoutPlans, hasSize(1));
        assertThat(workoutPlans.get(0).getId(), is(workoutPlan.getId()));
        assertThat(workoutPlans, hasItem(updatedWorkoutPlan));
        assertThat(workoutPlans.get(0).getDescription(), is("This changed!"));
    }

    @Test
    public void shouldRemoveWorkoutPlan() throws Exception {
        // Given
        final WorkoutPlan workoutPlan1 = workoutPlanBuilder.id(1).build();
        final WorkoutPlan workoutPlan2 = workoutPlanBuilder.id(2).build();

        // When
        repository.add(Arrays.asList(workoutPlan1, workoutPlan2));
        repository.remove(workoutPlan1);

        final List<WorkoutPlan> workoutPlans = repository.query(new GetAllWorkoutPlansSpecification());

        // Then
        assertThat(workoutPlans, hasSize(1));
        assertThat(workoutPlans, hasItem(workoutPlan2));
    }

    @Test
    public void shouldRemoveAllWorkoutPlans() throws Exception {
        // Given
        final WorkoutPlan workoutPlan1 = workoutPlanBuilder.id(1).build();
        final WorkoutPlan workoutPlan2 = workoutPlanBuilder.id(2).build();
        final WorkoutPlan workoutPlan3 = workoutPlanBuilder.id(3).build();

        // When
        repository.add(Arrays.asList(workoutPlan1, workoutPlan2, workoutPlan3));
        repository.remove(Arrays.asList(workoutPlan1, workoutPlan3));

        final List<WorkoutPlan> workoutPlans = repository.query(new GetAllWorkoutPlansSpecification());

        // Then
        assertThat(workoutPlans, hasSize(1));
        assertThat(workoutPlans, hasItem(workoutPlan2));
    }

    @Test
    public void shouldRemoveSpecifiedWorkoutPlans() throws Exception {
        // Given
        final WorkoutPlan workoutPlan1 = workoutPlanBuilder.id(1).build();
        final WorkoutPlan workoutPlan2 = workoutPlanBuilder.id(2).build();

        // When
        repository.add(Arrays.asList(workoutPlan1, workoutPlan2));
        repository.remove(new GetWorkoutPlanByIdSpecification(2));

        final List<WorkoutPlan> workoutPlans = repository.query(new GetAllWorkoutPlansSpecification());

        // Then
        assertThat(workoutPlans, hasSize(1));
        assertThat(workoutPlans, hasItem(workoutPlan1));
    }

    @Test
    public void shouldSelectSpecifiedItems() throws Exception {
        // Given
        final WorkoutPlan workoutPlan1 = workoutPlanBuilder.id(1).build();
        final WorkoutPlan workoutPlan2 = workoutPlanBuilder.id(2).build();
        final WorkoutPlan workoutPlan3 = workoutPlanBuilder.id(3).build();

        // When
        repository.add(Arrays.asList(workoutPlan1, workoutPlan2, workoutPlan3));
        final List<WorkoutPlan> workoutPlans = repository.query(new GetWorkoutPlanByIdSpecification(2));

        // Then
        assertThat(workoutPlans, hasSize(1));
        assertThat(workoutPlans, hasItem(workoutPlan2));
    }


}