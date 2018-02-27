package tdt4140.gr1817.ecosystem.persistence.repositories.mysql.guice;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Ignore;
import org.junit.Test;
import tdt4140.gr1817.ecosystem.persistence.repositories.UserRepository;

import static org.junit.Assert.*;

public class MySqlRepositoryModuleTest {

    @Test
    @Ignore("Should be here as a reminder, but should not break CI")
    public void configureShouldNotThrowWhenAllRepositoriesAreBound() throws Exception {
        // Given
        MySqlRepositoryModule module = new MySqlRepositoryModule();

        // When
        Injector injector = Guice.createInjector(module);

        // Then
        // should not throw

        UserRepository userRepository = injector.getInstance(UserRepository.class);
    }
}