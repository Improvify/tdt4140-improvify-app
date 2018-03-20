package tdt4140.gr1817.ecosystem.persistence.repositories.mysql;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;
import tdt4140.gr1817.ecosystem.persistence.data.ServiceProvider;
import tdt4140.gr1817.ecosystem.persistence.data.ServiceProviderPermissions;
import tdt4140.gr1817.ecosystem.persistence.data.User;
import tdt4140.gr1817.ecosystem.persistence.repositories.ServiceProviderRepository;
import tdt4140.gr1817.ecosystem.persistence.repositories.UserRepository;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.specification.GetAllServiceProviderPermissionsSpecification;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.specification.GetServiceProviderPermissionByIdSpecification;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.util.BuilderFactory;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.util.HsqldbRule;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class MySqlServiceProviderPermissionsRepositoryTest {

    @Rule
    public HsqldbRule hsqldbRule = new HsqldbRule();
    private MySqlServiceProviderPermissionsRepository repository;
    private User user1;
    private User user2;
    private User user3;
    private ServiceProvider serviceProvider;
    private ServiceProviderPermissions.ServiceProviderPermissionsBuilder serviceProviderPermissionsBuilder;
    private UserRepository userRepositoryMock;
    private ServiceProviderRepository serviceProviderRepositoryMock;

    @Before
    public void setUp() throws Exception {
        user1 = BuilderFactory.createUser().id(1).build();
        user2 = BuilderFactory.createUser().id(2).build();
        user3 = BuilderFactory.createUser().id(3).build();
        serviceProvider = BuilderFactory.createServiceProvider().id(1).build();

        userRepositoryMock = Mockito.mock(UserRepository.class);
        when(userRepositoryMock.query(any())).thenReturn(Collections.singletonList(user1));

        serviceProviderRepositoryMock = Mockito.mock(ServiceProviderRepository.class);
        when(serviceProviderRepositoryMock.query(any()))
                .thenReturn(Collections.singletonList(serviceProvider));

        repository = new MySqlServiceProviderPermissionsRepository(
                hsqldbRule::getConnection, serviceProviderRepositoryMock, userRepositoryMock);
        serviceProviderPermissionsBuilder = BuilderFactory.createServiceProviderPermissions()
                .serviceProvider(serviceProvider)
                .user(user1);

        new MySqlUserRepository(hsqldbRule::getConnection)
                .add(user1);
        new MySqlUserRepository(hsqldbRule::getConnection)
                .add(user2);
        new MySqlUserRepository(hsqldbRule::getConnection)
                .add(user3);
        new MySqlServiceProviderRepository(hsqldbRule::getConnection)
                .add(serviceProvider);

    }

    @Test
    public void shouldAddServiceProviderPermissions() throws Exception {
        // Given
        final ServiceProviderPermissions serviceProviderPermissions = serviceProviderPermissionsBuilder
                .user(user1)
                .serviceProvider(serviceProvider)
                .build();

        // When
        repository.add(serviceProviderPermissions);
        final List<ServiceProviderPermissions> serviceProviderPermissionsList = repository.query(
                new GetServiceProviderPermissionByIdSpecification(1, 1));

        // Then
        assertThat(serviceProviderPermissionsList, hasSize(1));
        assertThat(serviceProviderPermissionsList, hasItem(serviceProviderPermissions));
    }

    @Test
    public void shouldAddAllServiceProviderPermissions() throws Exception {
        // Given
        final ServiceProviderPermissions serviceProviderPermissions1 = serviceProviderPermissionsBuilder
                .user(user1)
                .serviceProvider(serviceProvider)
                .build();
        final ServiceProviderPermissions serviceProviderPermissions2 = serviceProviderPermissionsBuilder
                .user(user2)
                .serviceProvider(serviceProvider)
                .build();
        final ServiceProviderPermissions serviceProviderPermissions3 = serviceProviderPermissionsBuilder
                .user(user3)
                .serviceProvider(serviceProvider)
                .build();

        returnUsersInSequence(userRepositoryMock, user1, user2, user3);

        // When
        repository.add(Arrays.asList(
                serviceProviderPermissions1, serviceProviderPermissions2, serviceProviderPermissions3));
        final List<ServiceProviderPermissions> serviceProviderPermissionsList = repository.query(
                new GetAllServiceProviderPermissionsSpecification());

        // Then
        assertThat(serviceProviderPermissionsList, hasSize(3));
        assertThat(serviceProviderPermissionsList, hasItems(
                serviceProviderPermissions1, serviceProviderPermissions2, serviceProviderPermissions3));
    }

    private static void returnUsersInSequence(UserRepository userRepositoryMock, User... users) {
        AtomicInteger counter = new AtomicInteger(0);
        List<User> usersList = Arrays.asList(users);

        when(userRepositoryMock.query(any())).thenAnswer(invocationOnMock -> Collections.singletonList(usersList.get(counter.getAndIncrement())));
    }

    @Test
    public void shouldUpdateServiceProviderPermissions() throws Exception {
        // Given
        final ServiceProviderPermissions serviceProviderPermissions = serviceProviderPermissionsBuilder
                .user(user1)
                .email(false)
                .username(false)
                .serviceProvider(serviceProvider)
                .build();

        final ServiceProviderPermissions updatedServiceProviderPermissions = serviceProviderPermissionsBuilder
                .user(user1)
                .serviceProvider(serviceProvider)
                .email(true)
                .username(true)
                .build();


        // When
        repository.add(serviceProviderPermissions);
        repository.update(updatedServiceProviderPermissions);

        final List<ServiceProviderPermissions> serviceProviderPermissionsList = repository.query(
                new GetServiceProviderPermissionByIdSpecification(1, 1));

        // Then
        assertThat(serviceProviderPermissionsList, hasSize(1));
        assertThat(serviceProviderPermissionsList.get(0).isEmail(), is(true));
        assertThat(serviceProviderPermissionsList.get(0).isUsername(), is(true));
        assertThat(serviceProviderPermissionsList, hasItem(updatedServiceProviderPermissions));
    }

    @Test
    public void shouldQuerySpecifiedServiceProviderPermissions() throws Exception {
        // Given
        final ServiceProviderPermissions serviceProviderPermissions1 = serviceProviderPermissionsBuilder
                .user(user1)
                .serviceProvider(serviceProvider)
                .build();
        final ServiceProviderPermissions serviceProviderPermissions2 = serviceProviderPermissionsBuilder
                .user(user2)
                .serviceProvider(serviceProvider)
                .build();
        final ServiceProviderPermissions serviceProviderPermissions3 = serviceProviderPermissionsBuilder
                .user(user3)
                .serviceProvider(serviceProvider)
                .build();

        // When
        repository.add(Arrays.asList(serviceProviderPermissions1, serviceProviderPermissions2, serviceProviderPermissions3));

        final List<ServiceProviderPermissions> serviceProviderPermissionsList = repository.query(
                new GetServiceProviderPermissionByIdSpecification(1, 1));

        // Then
        assertThat(serviceProviderPermissionsList, hasSize(1));
        assertThat(serviceProviderPermissionsList, hasItem(serviceProviderPermissions1));
    }

    @Test
    public void shouldRemoveServiceProviderPermissions() throws Exception {
        // Given
        final ServiceProviderPermissions serviceProviderPermissions = serviceProviderPermissionsBuilder
                .user(user1)
                .serviceProvider(serviceProvider)
                .build();

        // When
        repository.add(serviceProviderPermissions);
        repository.remove(serviceProviderPermissions);

        final List<ServiceProviderPermissions> serviceProviderPermissionsList = repository.query(
                new GetAllServiceProviderPermissionsSpecification());

        // Then
        assertThat(serviceProviderPermissionsList, is(empty()));
    }

    @Test
    public void shouldRemoveAllServiceProviderPermissions() throws Exception {
        // Given
        final ServiceProviderPermissions serviceProviderPermissions1 = serviceProviderPermissionsBuilder
                .user(user1)
                .serviceProvider(serviceProvider)
                .build();
        final ServiceProviderPermissions serviceProviderPermissions2 = serviceProviderPermissionsBuilder
                .user(user2)
                .serviceProvider(serviceProvider)
                .build();
        final ServiceProviderPermissions serviceProviderPermissions3 = serviceProviderPermissionsBuilder
                .user(user3)
                .serviceProvider(serviceProvider)
                .build();

        when(userRepositoryMock.query(any())).thenReturn(Collections.singletonList(user2));

        // When
        repository.add(Arrays.asList(
                serviceProviderPermissions1, serviceProviderPermissions2, serviceProviderPermissions3));
        repository.remove(Arrays.asList(serviceProviderPermissions1, serviceProviderPermissions3));

        final List<ServiceProviderPermissions> serviceProviderPermissions = repository.query(
                new GetAllServiceProviderPermissionsSpecification());

        // Then
        assertThat(serviceProviderPermissions, hasSize(1));
        assertThat(serviceProviderPermissions, hasItem(serviceProviderPermissions2));
    }

    @Test
    public void shouldRemoveSpecifiedServiceProviderPermissions() throws Exception {
        // Given
        final ServiceProviderPermissions serviceProviderPermissions1 = serviceProviderPermissionsBuilder
                .user(user1)
                .serviceProvider(serviceProvider)
                .build();
        final ServiceProviderPermissions serviceProviderPermissions2 = serviceProviderPermissionsBuilder
                .user(user2)
                .serviceProvider(serviceProvider)
                .build();
        final ServiceProviderPermissions serviceProviderPermissions3 = serviceProviderPermissionsBuilder
                .user(user3)
                .serviceProvider(serviceProvider)
                .build();

        returnUsersInSequence(userRepositoryMock, user2, user1, user3);

        // When
        repository.add(Arrays.asList(
                serviceProviderPermissions1, serviceProviderPermissions2, serviceProviderPermissions3));
        repository.remove(new GetServiceProviderPermissionByIdSpecification(2, 1));

        final List<ServiceProviderPermissions> serviceProviderPermissions = repository.query(
                new GetAllServiceProviderPermissionsSpecification());

        // Then
        assertThat(serviceProviderPermissions, hasSize(2));
        assertThat(serviceProviderPermissions, hasItems(serviceProviderPermissions1, serviceProviderPermissions3));
    }
}
