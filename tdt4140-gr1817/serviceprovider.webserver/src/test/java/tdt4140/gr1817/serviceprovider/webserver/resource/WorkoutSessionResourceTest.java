package tdt4140.gr1817.serviceprovider.webserver.resource;

import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import tdt4140.gr1817.ecosystem.persistence.Specification;
import tdt4140.gr1817.ecosystem.persistence.data.User;
import tdt4140.gr1817.ecosystem.persistence.data.WorkoutSession;
import tdt4140.gr1817.ecosystem.persistence.repositories.UserRepository;
import tdt4140.gr1817.ecosystem.persistence.repositories.WorkoutSessionRepository;
import tdt4140.gr1817.serviceprovider.webserver.security.AuthBasicAuthenticator;
import tdt4140.gr1817.serviceprovider.webserver.validation.WorkoutSessionValidator;
import tdt4140.gr1817.serviceprovider.webserver.validation.util.AuthBasicUtil;

import javax.ws.rs.core.Response;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class WorkoutSessionResourceTest {
    private WorkoutSessionRepository workoutSessionRepository;
    private final Gson gson = new Gson();
    private WorkoutSessionResource workoutSessionResource;

    private final String gpx = "<trk>\n" +
            "    <name>Trondheim maraton 10km</name>\n" +
            "    <type>running</type>\n" +
            "    <trkseg>\n" +
            "      <trkpt lat=\"63.43078055419027805328369140625\" lon=\"10.39632654748857021331787109375\">\n" +
            "        <ele>17.200000762939453125</ele>\n" +
            "        <time>2017-09-02T14:00:00.000Z</time>\n" +
            "        <extensions>\n" +
            "          <ns3:TrackPointExtension>\n" +
            "            <ns3:hr>131</ns3:hr>\n" +
            "            <ns3:cad>0</ns3:cad>\n" +
            "          </ns3:TrackPointExtension>\n" +
            "        </extensions>\n" +
            "      </trkpt>\n" +
            "      <trkpt lat=\"63.43078189529478549957275390625\" lon=\"10.39632436819374561309814453125\">\n" +
            "        <ele>17.200000762939453125</ele>\n" +
            "        <time>2017-09-02T14:00:01.000Z</time>\n" +
            "        <extensions>\n" +
            "          <ns3:TrackPointExtension>\n" +
            "            <ns3:hr>135</ns3:hr>\n" +
            "            <ns3:cad>0</ns3:cad>\n" +
            "          </ns3:TrackPointExtension>\n" +
            "        </extensions>\n" +
            "      </trkpt>\n" +
            "    <trkseg>\n" +
            "<trk>\n";

    @Before
    public void setUp() throws Exception {
        workoutSessionRepository = Mockito.mock(WorkoutSessionRepository.class);
        UserRepository userRepository = Mockito.mock(UserRepository.class);

        final WorkoutSession workoutSession = createWorkoutSession();
        when(workoutSessionRepository.query(Mockito.any())).thenReturn(Collections.singletonList(workoutSession));
        when(userRepository.query(Mockito.any())).thenReturn(Collections.singletonList(workoutSession.getUser()));

        final WorkoutSessionValidator validator = new WorkoutSessionValidator(gson);
        final AuthBasicAuthenticator authenticator = new AuthBasicAuthenticator();
        workoutSessionResource = new WorkoutSessionResource(workoutSessionRepository, userRepository, gson, validator,
                authenticator);
    }

    @Test
    public void shouldGetWorkoutSessions() throws Exception {
        // Given
        String username = "test";

        // When
        final Response response = workoutSessionResource.getWorkoutSessions(username, AuthBasicUtil.HEADER_TEST_123);

        // Then
        assertThat(response.getStatus(), is(200));
        verify(workoutSessionRepository).query(any(Specification.class));
        verifyNoMoreInteractions(workoutSessionRepository);
    }

    @Test
    public void shouldNotGetWorkoutSessionsWhenWrongAuthorization() {
        // Given
        String username = "test";

        // When
        final Response response = workoutSessionResource.getWorkoutSessions(username, AuthBasicUtil.HEADER_DEFAULT);

        // Then
        assertThat(response.getStatus(), is(401));
        verifyNoMoreInteractions(workoutSessionRepository);
    }

    @Test
    public void shouldNotGetWorkoutSessionsWhenIllegalHeader() {
        // Given
        String username = "test";

        // When
        final Response response = workoutSessionResource.getWorkoutSessions(username, AuthBasicUtil.HEADER_ILLEGAL);

        // Then
        assertThat(response.getStatus(), is(401));
        verifyNoMoreInteractions(workoutSessionRepository);
    }

    @Test
    public void shouldAddWorkoutSession() throws Exception {
        // Given
        WorkoutSession workoutSession = createWorkoutSession();
        String json = gson.toJson(workoutSession);

        // When
        final Response response = workoutSessionResource.createWorkoutSession(json, AuthBasicUtil.HEADER_TEST_123);

        // Then
        assertThat(response.getStatus(), is(200));
        verify(workoutSessionRepository).add(Mockito.eq(workoutSession));
        verifyNoMoreInteractions(workoutSessionRepository);
    }

    @Test
    public void shouldAddWorkoutSessionByGpxFile() throws Exception {
        // Given
        InputStream inputStream = new ByteArrayInputStream(gpx.getBytes("UTF-8"));
        WorkoutSession workoutSession = createWorkoutSession();
        String json = gson.toJson(workoutSession);

        // When
        final Response response = workoutSessionResource.createWorkoutSessionByGpxFile(inputStream, json,
                AuthBasicUtil.HEADER_TEST_123);

        // Then
        assertThat(response.getStatus(), is(200));
        verify(workoutSessionRepository).add(any(WorkoutSession.class));
        verifyNoMoreInteractions(workoutSessionRepository);
    }

    @Test
    public void shouldNotAddWhenInvalidWorkoutSession() throws Exception {
        // Given
        WorkoutSession workoutSession = createWorkoutSession();
        String invalidJson = workoutSession.toString();

        // When
        final Response response = workoutSessionResource.createWorkoutSession(invalidJson, AuthBasicUtil.HEADER_TEST_123);

        //Then
        assertThat(response.getStatus(), is(400));
        verifyNoMoreInteractions(workoutSessionRepository);
    }

    @Test
    public void shouldNotAddWorkoutSessionWhenWrongAuthorization() throws Exception {
        // Given
        WorkoutSession workoutSession = createWorkoutSession();
        String json = gson.toJson(workoutSession);

        // When
        final Response response = workoutSessionResource.createWorkoutSession(json, AuthBasicUtil.HEADER_DEFAULT);

        // Then
        assertThat(response.getStatus(), is(401));
        verifyNoMoreInteractions(workoutSessionRepository);
    }

    @Test
    public void shouldNotAddWorkoutSessionByGpxFileWhenWrongAuthorization() throws Exception {
        // Given
        InputStream inputStream = new ByteArrayInputStream(gpx.getBytes("UTF-8"));
        WorkoutSession workoutSession = createWorkoutSession();
        String json = gson.toJson(workoutSession);

        // When
        final Response response = workoutSessionResource.createWorkoutSessionByGpxFile(inputStream, json,
                AuthBasicUtil.HEADER_DEFAULT);

        // Then
        assertThat(response.getStatus(), is(401));
        verifyNoMoreInteractions(workoutSessionRepository);
    }

    @Test
    public void shouldNotAddWorkoutSessionWhenIllegalHeader() {
        // Given
        WorkoutSession workoutSession = createWorkoutSession();
        String json = gson.toJson(workoutSession);

        // When
        final Response response = workoutSessionResource.createWorkoutSession(json, AuthBasicUtil.HEADER_ILLEGAL);

        // Then
        assertThat(response.getStatus(), is(401));
        verifyNoMoreInteractions(workoutSessionRepository);
    }

    @Test
    public void shouldNotAddWorkoutSessionByGpxFileWhenIllegalGpx() throws Exception {
        // Given
        String illegalGpx = "illegal";
        InputStream inputStream = new ByteArrayInputStream(illegalGpx.getBytes("UTF-8"));
        WorkoutSession workoutSession = createWorkoutSession();
        String json = gson.toJson(workoutSession);

        // When
        final Response response = workoutSessionResource.createWorkoutSessionByGpxFile(inputStream, json,
                AuthBasicUtil.HEADER_TEST_123);

        // Then
        assertThat(response.getStatus(), is(400));
        verifyNoMoreInteractions(workoutSessionRepository);
    }

    @Test
    public void shouldUpdateWorkoutSession() throws Exception {
        // Given
        WorkoutSession workoutSession = createWorkoutSession();
        String json = gson.toJson(workoutSession);

        // When
        final Response response = workoutSessionResource.updateWorkoutSession(json, AuthBasicUtil.HEADER_TEST_123);

        // Then
        assertThat(response.getStatus(), is(200));
        verify(workoutSessionRepository).update(Mockito.eq(workoutSession));
        verifyNoMoreInteractions(workoutSessionRepository);
    }

    @Test
    public void shouldNotUpdateWhenInvalidWorkoutSession() throws Exception {
        // Given
        WorkoutSession workoutSession = createWorkoutSession();
        String invalidJson = workoutSession.toString();

        // When
        final Response response = workoutSessionResource.updateWorkoutSession(invalidJson, AuthBasicUtil.HEADER_TEST_123);

        //Then
        assertThat(response.getStatus(), is(400));
        verifyNoMoreInteractions(workoutSessionRepository);
    }

    @Test
    public void shouldNotUpdateWorkoutSessionWhenWrongAuthorization() throws Exception {
        // Given
        WorkoutSession workoutSession = createWorkoutSession();
        String json = gson.toJson(workoutSession);

        // When
        final Response response = workoutSessionResource.updateWorkoutSession(json, AuthBasicUtil.HEADER_DEFAULT);

        // Then
        assertThat(response.getStatus(), is(401));
        verifyNoMoreInteractions(workoutSessionRepository);
    }

    @Test
    public void shouldNotUpdateWorkoutSessionWhenIllegalHeader() {
        // Given
        WorkoutSession workoutSession = createWorkoutSession();
        String json = gson.toJson(workoutSession);

        // When
        final Response response = workoutSessionResource.updateWorkoutSession(json, AuthBasicUtil.HEADER_ILLEGAL);

        // Then
        assertThat(response.getStatus(), is(401));
        verifyNoMoreInteractions(workoutSessionRepository);
    }

    @Test
    public void shouldRemoveWorkoutSession() {
        // Given
        int id = 1;

        // When
        final Response response = workoutSessionResource.deleteWorkoutSession(id, AuthBasicUtil.HEADER_TEST_123);

        // Then
        assertThat(response.getStatus(), is(200));
        verify(workoutSessionRepository).query(any(Specification.class));
        verify(workoutSessionRepository).remove(any(Specification.class));
        verifyNoMoreInteractions(workoutSessionRepository);
    }

    @Test
    public void shouldNotRemoveWorkoutSessionWhenWrongAuthorization() {
        // Given
        int id = 1;

        // When
        final Response response = workoutSessionResource.deleteWorkoutSession(id, AuthBasicUtil.HEADER_DEFAULT);

        // Then
        assertThat(response.getStatus(), is(401));
        verify(workoutSessionRepository).query(any(Specification.class));
        verifyNoMoreInteractions(workoutSessionRepository);
    }

    @Test
    public void shouldNotRemoveWorkoutSessionWhenIllegalHeader() {
        // Given
        int id = 1;

        // When
        final Response response = workoutSessionResource.deleteWorkoutSession(id, AuthBasicUtil.HEADER_ILLEGAL);

        // Then
        assertThat(response.getStatus(), is(401));
        verify(workoutSessionRepository).query(any(Specification.class));
        verifyNoMoreInteractions(workoutSessionRepository);
    }

    private static WorkoutSession createWorkoutSession() {
        Calendar calendar = new GregorianCalendar(2000, 1, 1);
        calendar.set(Calendar.MILLISECOND, 0); // JSON doesnt serialize milliseconds
        Date date = calendar.getTime();
        User user = new User(1, "Test", "User", 1.8f, date, "test", "123", "123@hotmail.com");
        return new WorkoutSession(1, date, 1, 12.5f, 140.4f, 170.3f, 12.3f, 60 * 30, user);
    }
}
