package tdt4140.gr1817.ecosystem.persistence.data;

import org.junit.Test;

import static org.junit.Assert.*;

public class WeightTest {


    @Test(expected = IllegalArgumentException.class)
    public void constructorShouldThrowWhenNegativeWeight() throws Exception {
        // When
        new Weight(0, -1, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowWhenSettingNegativeWeight() throws Exception {
        // When
        Weight weight = new Weight(0, 2, null, null);

        // When
        weight.setCurrentWeight(-1);
    }
}