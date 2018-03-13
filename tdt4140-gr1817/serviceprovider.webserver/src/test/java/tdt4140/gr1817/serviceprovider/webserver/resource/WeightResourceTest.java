package tdt4140.gr1817.serviceprovider.webserver.resource;

import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import tdt4140.gr1817.ecosystem.persistence.Specification;
import tdt4140.gr1817.ecosystem.persistence.data.User;
import tdt4140.gr1817.ecosystem.persistence.data.Weight;
import tdt4140.gr1817.ecosystem.persistence.repositories.WeightRepository;
import tdt4140.gr1817.serviceprovider.webserver.validation.WeightValidator;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class WeightResourceTest {
    private WeightRepository rep;
    private Gson gson = new Gson();
    private WeightResource weightResource;

    @Before
    public void setUp() throws Exception {
        rep = Mockito.mock(WeightRepository.class);
        final WeightValidator validator = new WeightValidator(gson);
        weightResource = new WeightResource(rep, gson, validator);
    }

    @Test
    public void shouldAddWeight() throws Exception {
        // Given
        Weight weight = createWeight();
        String json = gson.toJson(weight);

        // When
        weightResource.createWeight(json);

        // Then
        verify(rep).add(Mockito.eq(weight));
        verifyNoMoreInteractions(rep);
    }

    @Test
    public void shouldNotAddWhenInvalidWeight() throws Exception {
        // Given
        Weight weight = createWeight();
        String invalidJson = weight.toString();

        // When
        weightResource.createWeight(invalidJson);

        // Then
        verifyNoMoreInteractions(rep);
    }

    @Test
    public void shouldRemoveWeight() {
        // Given
        int id = 1;

        // When
        weightResource.deleteGoal(id);

        // Then
        verify(rep).remove(any(Specification.class));
        verifyNoMoreInteractions(rep);
    }

    private static Weight createWeight() {
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.MILLISECOND, 0); // JSON doesnt serialize milliseconds
        Date date = calendar.getTime();
        User user = new User(1, "hei", "bu", 2.5f, date, "hellu", "hshs", "123@hotmail.com");
        return new Weight(1, 140.4f, date, user);
    }
}
