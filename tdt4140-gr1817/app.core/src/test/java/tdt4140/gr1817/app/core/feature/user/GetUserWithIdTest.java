package tdt4140.gr1817.app.core.feature.user;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import tdt4140.gr1817.ecosystem.persistence.data.ServiceProvider;
import tdt4140.gr1817.ecosystem.persistence.data.User;
import tdt4140.gr1817.ecosystem.persistence.repositories.ServiceProviderPermissionsRepository;
import tdt4140.gr1817.ecosystem.persistence.repositories.UserRepository;

import java.util.Collections;
import java.util.Date;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Created by Kristian Rekstad.
 *
 * @author Kristian Rekstad
 */
public class GetUserWithIdTest {


    private UserRepository userRepositoryMock;
    private HonorUserPermissions honorUserPermissionsMock;
    private ServiceProvider serviceProviderMock;
    private ServiceProviderPermissionsRepository serviceProviderPermissionsRepositoryMock;

    @Before
    public void setUp() throws Exception {
        userRepositoryMock = Mockito.mock(UserRepository.class);
        honorUserPermissionsMock = Mockito.mock(HonorUserPermissions.class);
        serviceProviderMock = Mockito.mock(ServiceProvider.class);
        serviceProviderPermissionsRepositoryMock = Mockito.mock(ServiceProviderPermissionsRepository.class);


    }

    @Test
    public void shouldGetUserWithSpecifiedId() throws Exception {
        // Given
        User user = new User(5, "", "", 5f, new Date(), "", "", "");
        when(userRepositoryMock.query(any()))
                .thenReturn(Collections.singletonList(user));

        GetUserWithId getUserWithId = new GetUserWithId(userRepositoryMock,honorUserPermissionsMock, serviceProviderMock,serviceProviderPermissionsRepositoryMock);

        // When
        User result = getUserWithId.getUserWithId(5);

        // Then
        Mockito.verify(userRepositoryMock).query(any());
        assertThat(result, Matchers.equalTo(user));
    }

    @Test
    public void shouldReturnNullWhenNotFound() throws Exception {
        // Given
        when(userRepositoryMock.query(any()))
                .thenReturn(Collections.emptyList());

        GetUserWithId getUserWithId = new GetUserWithId(userRepositoryMock,honorUserPermissionsMock, serviceProviderMock,serviceProviderPermissionsRepositoryMock);

        // When
        User result = getUserWithId.getUserWithId(-1);

        // Then
        assertThat(result, is(nullValue()));
    }
}