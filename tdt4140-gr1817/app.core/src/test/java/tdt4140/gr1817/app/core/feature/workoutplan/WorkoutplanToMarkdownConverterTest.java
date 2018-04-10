package tdt4140.gr1817.app.core.feature.workoutplan;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import tdt4140.gr1817.ecosystem.persistence.data.User;
import tdt4140.gr1817.ecosystem.persistence.data.improvify.WorkoutPlan;
import tdt4140.gr1817.ecosystem.persistence.data.improvify.WorkoutPlanRow;
import tdt4140.gr1817.ecosystem.persistence.repositories.improvify.WorkoutPlanRowRepository;

import java.sql.Connection;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Ask Sommervoll.
 *
 * @author Ask Sommervoll
 */
public class WorkoutplanToMarkdownConverterTest {


    private Connection conn;
    //private List<User> users;
    private WorkoutPlan wop;
    private WorkoutPlanRow wopr1;
    private WorkoutPlanRow wopr2;

    @Before
    public void setUp() throws Exception {
        wop = WorkoutPlan.builder().id(1).build();
        wopr1 = WorkoutPlanRow.builder().id(1).build();
        wopr2 = WorkoutPlanRow.builder().id(2).build();

        conn = mock(Connection.class);

    }

    @Test
    public void shouldCreateValidMarkdown() throws Exception {
        // Given
        WorkoutPlanRowRepository repositoryMock = mock(WorkoutPlanRowRepository.class);
        User user = new User(3, "Test", "Person", 185, new Date(), "topkek", "123", "");
        WorkoutPlan workoutPlan = new WorkoutPlan(1, "Test plan", user);
        List<WorkoutPlanRow> rows = Arrays.asList(
                new WorkoutPlanRow(1, "Desc", 60, "80% 1RM", "Do you even lift bro", workoutPlan),
                new WorkoutPlanRow(3, "Løp i ring", 1337, ">9000", "Ingefær er bedre enn bringebær", workoutPlan)
        );
        when(repositoryMock.query(any())).thenReturn(rows);

        final WorkoutplanToMarkdownConverter workoutplanToMarkdownConverter = new WorkoutplanToMarkdownConverter(repositoryMock);

        // When
        final String markdownString = workoutplanToMarkdownConverter.createMarkdownString(workoutPlan);

        assertThat(markdownString, is("Description|Duration|Intensity|Comment\n"
                + "-----------|--------|---------|-------\n"
                + "Desc | 60 | 80% 1RM | Do you even lift bro \n"
                + "Løp i ring | 1337 | >9000 | Ingefær er bedre enn bringebær \n"));

    }


    @Test(expected = NullPointerException.class)
    public void constructorShouldThrowOnNull() throws Exception {
        new WorkoutplanToMarkdownConverter( null);
    }

}