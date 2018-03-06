package tdt4140.gr1817.ecosystem.persistence.repositories.mysql;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;
import tdt4140.gr1817.ecosystem.persistence.data.User;
import tdt4140.gr1817.ecosystem.persistence.data.WorkoutSession;
import tdt4140.gr1817.ecosystem.persistence.repositories.UserRepository;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.specification.improvify.GetAllWorkoutSessionsSpecification;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.specification.improvify.GetWorkoutSessionByIdSpecification;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.util.BuilderFactory;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.util.HsqldbRule;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class MySqlWorkoutSessionRepositoryTest {

    @Rule
    public HsqldbRule hsqldbRule = new HsqldbRule();

    private MySqlWorkoutSessionRepository repository;
    private WorkoutSession.WorkoutSessionBuilder workoutSessionBuilder;
    private User user;
    private UserRepository userRepositoryMock;

    @Before
    public void setUp() throws Exception {
        user = BuilderFactory.createUser().id(1).build();
        workoutSessionBuilder = BuilderFactory.createWorkoutSession().user(user);

        userRepositoryMock = Mockito.mock(UserRepository.class);
        Mockito.when(userRepositoryMock.query(Mockito.any())).thenReturn(Collections.singletonList(user));

        repository = new MySqlWorkoutSessionRepository(hsqldbRule::getConnection, userRepositoryMock);

        new MySqlUserRepository(hsqldbRule::getConnection)
                .add(user);
    }



    @Test
    public void shouldAddSession() throws Exception {
        // Given
        final WorkoutSession workoutSession = workoutSessionBuilder.build();

        // When
        repository.add(workoutSession);
        final List<WorkoutSession> sessions = repository.query(new GetWorkoutSessionByIdSpecification(1));

        // Then
        assertThat(sessions, hasItem(workoutSession));
    }

    @Test
    public void shouldSelectSpecifiedItems() throws Exception {
        // Given
        final WorkoutSession workoutSession = workoutSessionBuilder.id(1).build();
        final WorkoutSession workoutSession2 = workoutSessionBuilder
                .id(2)
                .user(user)
                .build();

        // When
        repository.add(workoutSession);
        repository.add(workoutSession2);

        final List<WorkoutSession> sessions = repository.query(new GetWorkoutSessionByIdSpecification(2));

        // Then
        assertThat(sessions, hasSize(1));
        assertThat(sessions, hasItem(workoutSession2));
        assertThat(sessions.get(0).getUser().getId(), is(user.getId()));
    }

    @Test
    public void shouldAddAllSessions() throws Exception {
        // Given
        final WorkoutSession session1 = workoutSessionBuilder.id(1).build();
        final WorkoutSession session2 = workoutSessionBuilder.id(2).build();
        final WorkoutSession session3 = workoutSessionBuilder.id(3).build();

        // When
        repository.add(Arrays.asList(session1, session2, session3));
        final List<WorkoutSession> sessions = repository.query(new GetAllWorkoutSessionsSpecification());

        // Then
        assertThat(sessions, hasSize(3));
        assertThat(sessions, hasItems(session1, session2, session3));
    }

    @Test
    public void shouldUpdateSession() throws Exception {
        // Given
        final WorkoutSession session = workoutSessionBuilder.id(1).build();
        final WorkoutSession updatedSession = workoutSessionBuilder.id(1).distanceRun(session.getDistanceRun() + 10).build();

        // When
        repository.add(session);
        repository.update(updatedSession);

        final List<WorkoutSession> sessions = repository.query(new GetWorkoutSessionByIdSpecification(session.getId()));

        // Then
        assertThat(sessions, hasSize(1));
        assertThat(sessions.get(0).getId(), is(session.getId()));
        assertThat(sessions, hasItems(updatedSession));
    }

    @Test
    public void shouldRemoveSession() throws Exception {
    }

    @Test
    public void shouldRemoveAllSessions() throws Exception {
    }

    @Test
    public void shouldRemoveSpecifiedSessions() throws Exception {
    }




}