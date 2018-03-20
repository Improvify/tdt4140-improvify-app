package tdt4140.gr1817.ecosystem.persistence.repositories.mysql.improvify;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;
import tdt4140.gr1817.ecosystem.persistence.data.User;
import tdt4140.gr1817.ecosystem.persistence.data.improvify.WorkoutPlan;
import tdt4140.gr1817.ecosystem.persistence.data.improvify.WorkoutPlanRow;
import tdt4140.gr1817.ecosystem.persistence.repositories.improvify.WorkoutPlanRepository;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.MySqlUserRepository;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.specification.GetWorkoutPlanRowByIdSpecification;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.specification.improvify.GetAllWorkoutPlanRowsSpecification;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.util.BuilderFactory;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.util.HsqldbRule;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Created by Kristian Rekstad.
 *
 * @author Kristian Rekstad
 */
public class MySqlWorkoutPlanRowRepositoryTest {
    
    @Rule
    public HsqldbRule hsqldbRule = new HsqldbRule();
    private MySqlWorkoutPlanRowRepository repository;
    private WorkoutPlanRow.WorkoutPlanRowBuilder workoutPlanRowBuilder;
    private WorkoutPlan workoutPlan;

    @Before
    public void setUp() throws Exception {
        User user = BuilderFactory.createUser().build();

        workoutPlan = BuilderFactory.createWorkoutPlan().build();
        WorkoutPlanRepository workoutPlanRepositoryMock = Mockito.mock(WorkoutPlanRepository.class);
        when(workoutPlanRepositoryMock.query(any())).thenReturn(Collections.singletonList(workoutPlan));

        repository = new MySqlWorkoutPlanRowRepository(hsqldbRule::getConnection, workoutPlanRepositoryMock);
        workoutPlanRowBuilder = BuilderFactory.createWorkoutPlanRow()
                .workoutPlan(workoutPlan);

        // Prevent foreign key issues
        final MySqlUserRepository mySqlUserRepository = new MySqlUserRepository(hsqldbRule::getConnection);
        mySqlUserRepository.add(user);
        new MySqlWorkoutPlanRepository(hsqldbRule::getConnection, mySqlUserRepository).add(workoutPlan);
    }

    @Test
    public void shouldAddWorkoutPlanRow() throws Exception {
        // Given
        final WorkoutPlanRow workoutPlanRow = workoutPlanRowBuilder.id(1).build();

        // When
        repository.add(workoutPlanRow);

        final List<WorkoutPlanRow> workoutPlanRows = repository.query(new GetWorkoutPlanRowByIdSpecification(1));

        // Then
        assertThat(workoutPlanRows, hasSize(1));
        assertThat(workoutPlanRows, hasItem(workoutPlanRow));
    }

    @Test
    public void shouldAddAllWorkoutPlanRows() throws Exception {
        // Given
        final WorkoutPlanRow workoutPlanRow1 = workoutPlanRowBuilder.id(1).build();
        final WorkoutPlanRow workoutPlanRow2 = workoutPlanRowBuilder.id(2).build();
        final WorkoutPlanRow workoutPlanRow3 = workoutPlanRowBuilder.id(3).build();


        // When
        repository.add(Arrays.asList(workoutPlanRow1, workoutPlanRow2, workoutPlanRow3));
        final List<WorkoutPlanRow> workoutPlanRows = repository.query(new GetAllWorkoutPlanRowsSpecification());


        // Then
        assertThat(workoutPlanRows, hasSize(3));
        assertThat(workoutPlanRows, hasItems(workoutPlanRow1, workoutPlanRow2, workoutPlanRow3));
    }

    @Test
    public void shouldUpdateItem() throws Exception {
        // Given
        final WorkoutPlanRow workoutPlanRow = workoutPlanRowBuilder
                .id(1)
                .description("My first description")
                .build();
        final WorkoutPlanRow updatedWorkoutPlanRow = workoutPlanRowBuilder
                .id(1)
                .description("This changed!")
                .build();


        // When
        repository.add(workoutPlanRow);
        repository.update(updatedWorkoutPlanRow);

        final List<WorkoutPlanRow> workoutPlanRows = repository.query(new GetWorkoutPlanRowByIdSpecification(workoutPlanRow.getId()));

        // Then
        assertThat(workoutPlanRows, hasSize(1));
        assertThat(workoutPlanRows.get(0).getId(), is(workoutPlanRow.getId()));
        assertThat(workoutPlanRows, hasItem(updatedWorkoutPlanRow));
        assertThat(workoutPlanRows.get(0).getDescription(), is("This changed!"));
    }

    @Test
    public void shouldRemoveWorkoutPlanRow() throws Exception {
        // Given
        final WorkoutPlanRow workoutPlanRow1 = workoutPlanRowBuilder.id(1).build();
        final WorkoutPlanRow workoutPlanRow2 = workoutPlanRowBuilder.id(2).build();

        // When
        repository.add(Arrays.asList(workoutPlanRow1, workoutPlanRow2));
        repository.remove(workoutPlanRow1);

        final List<WorkoutPlanRow> workoutPlanRows = repository.query(new GetAllWorkoutPlanRowsSpecification());

        // Then
        assertThat(workoutPlanRows, hasSize(1));
        assertThat(workoutPlanRows, hasItem(workoutPlanRow2));
    }

    @Test
    public void shouldRemoveAllWorkoutPlanRows() throws Exception {
        // Given
        final WorkoutPlanRow workoutPlanRow1 = workoutPlanRowBuilder.id(1).build();
        final WorkoutPlanRow workoutPlanRow2 = workoutPlanRowBuilder.id(2).build();
        final WorkoutPlanRow workoutPlanRow3 = workoutPlanRowBuilder.id(3).build();

        // When
        repository.add(Arrays.asList(workoutPlanRow1, workoutPlanRow2, workoutPlanRow3));
        repository.remove(Arrays.asList(workoutPlanRow1, workoutPlanRow3));

        final List<WorkoutPlanRow> workoutPlanRows = repository.query(new GetAllWorkoutPlanRowsSpecification());

        // Then
        assertThat(workoutPlanRows, hasSize(1));
        assertThat(workoutPlanRows, hasItem(workoutPlanRow2));
    }

    @Test
    public void shouldRemoveSpecifiedWorkoutPlanRows() throws Exception {
        // Given
        final WorkoutPlanRow workoutPlanRow1 = workoutPlanRowBuilder.id(1).build();
        final WorkoutPlanRow workoutPlanRow2 = workoutPlanRowBuilder.id(2).build();

        // When
        repository.add(Arrays.asList(workoutPlanRow1, workoutPlanRow2));
        repository.remove(new GetWorkoutPlanRowByIdSpecification(2));

        final List<WorkoutPlanRow> workoutPlanRows = repository.query(new GetAllWorkoutPlanRowsSpecification());

        // Then
        assertThat(workoutPlanRows, hasSize(1));
        assertThat(workoutPlanRows, hasItem(workoutPlanRow1));
    }

    @Test
    public void shouldSelectSpecifiedItems() throws Exception {
        // Given
        final WorkoutPlanRow workoutPlanRow1 = workoutPlanRowBuilder.id(1).build();
        final WorkoutPlanRow workoutPlanRow2 = workoutPlanRowBuilder.id(2).build();
        final WorkoutPlanRow workoutPlanRow3 = workoutPlanRowBuilder.id(3).build();

        // When
        repository.add(Arrays.asList(workoutPlanRow1, workoutPlanRow2, workoutPlanRow3));
        final List<WorkoutPlanRow> workoutPlanRows = repository.query(new GetWorkoutPlanRowByIdSpecification(2));

        // Then
        assertThat(workoutPlanRows, hasSize(1));
        assertThat(workoutPlanRows, hasItem(workoutPlanRow2));
    }

}