package tdt4140.gr1817.serviceprovider.webserver.resource;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import tdt4140.gr1817.ecosystem.persistence.data.RestingHeartRate;
import tdt4140.gr1817.ecosystem.persistence.data.User;
import tdt4140.gr1817.ecosystem.persistence.repositories.RestingHeartRateRepository;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class RestingHeartRateResourceTest {
    RestingHeartRateRepository rep;

    @Before
    public void setUp() throws Exception {
        rep = Mockito.mock(RestingHeartRateRepository.class);
    }

    @Test
    public void shouldAddRestingHeartRate() {
        // Given
        Gson gson = new Gson();
        RestingHeartRateResource restingHeartRateResource = new RestingHeartRateResource(rep, gson);
        RestingHeartRate restingHeartRate = createRestingHeartRate();
        String json = gson.toJson(restingHeartRate);

        // When
        restingHeartRateResource.createRestingHeartRate(json);

        // Then
        verify(rep).add(Mockito.eq(restingHeartRate));
        verifyNoMoreInteractions(rep);
    }

    @Test(expected = JsonSyntaxException.class)
    public void shouldFailToAddRestingHeartRate(){
        // Given
        Gson gson = new Gson();
        RestingHeartRateResource restingHeartRateResource = new RestingHeartRateResource(rep, gson);
        RestingHeartRate restingHeartRate = createRestingHeartRate();
        String s = restingHeartRate.toString();

        // When
        restingHeartRateResource.createRestingHeartRate(s);
    }

    private static RestingHeartRate createRestingHeartRate() {
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.MILLISECOND, 0); // JSON doesnt serialize milliseconds
        Date date = calendar.getTime();
        User user = new User(1,"hei", "bu", 2.5f, date, "hellu", "hshs", "123@hotmail.com");
        return new RestingHeartRate(1, date, 140, user);
    }
}
