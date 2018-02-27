package tdt4140.gr1817.ecosystem.persistence.data;

import org.junit.Test;

import static org.junit.Assert.*;

public class ServiceProviderTest {


    @Test(expected = NullPointerException.class)
    public void constructorShouldThrowOnNullName() throws Exception {
        new ServiceProvider(null);
    }
}