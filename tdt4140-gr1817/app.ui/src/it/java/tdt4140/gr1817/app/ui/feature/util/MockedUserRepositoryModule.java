package tdt4140.gr1817.app.ui.feature.util;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import tdt4140.gr1817.ecosystem.persistence.repositories.UserRepository;

/**
 * Created by Kristian Rekstad.
 *
 * @author Kristian Rekstad
 */
public class MockedUserRepositoryModule extends AbstractModule {

    @Mock
    private UserRepository userRepositoryMock;

    public MockedUserRepositoryModule() {
        resetMock();
    }

    /**
     * Reset all mocks, in case you used {@link Mockito#when(Object)} to alter them.
     */
    public void resetMock() {
        MockitoAnnotations.initMocks(this);;
    }

    /**
     * Get the mocked instance, so you can alter it or verify it.
     * The instance will change when {@link #resetMock()} is called, so use this method again to obtain the new mock.
     * @return the mocked instance.
     * @see Mockito#verify(Object)
     * @see Mockito#when(Object)
     */
    public UserRepository getUserRepositoryMock() {
        return userRepositoryMock;
    }

    @Override
    protected void configure() {
    }

    @Provides
    protected UserRepository provideMockedUserRepository() {
        return userRepositoryMock;
    }
}
