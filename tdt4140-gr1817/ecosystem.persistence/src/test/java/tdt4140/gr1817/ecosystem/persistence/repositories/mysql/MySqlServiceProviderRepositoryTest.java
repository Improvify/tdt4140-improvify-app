package tdt4140.gr1817.ecosystem.persistence.repositories.mysql;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import tdt4140.gr1817.ecosystem.persistence.data.ServiceProvider;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.specification.GetAllServiceProvidersSpecification;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.specification.GetServiceProviderByIdSpecification;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.util.BuilderFactory;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.util.HsqldbRule;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

public class MySqlServiceProviderRepositoryTest {

    @Rule
    public HsqldbRule hsqldbRule = new HsqldbRule();
    private MySqlServiceProviderRepository repository;
    private ServiceProvider.ServiceProviderBuilder serviceProviderBuilder;

    @Before
    public void setUp() throws Exception {
        serviceProviderBuilder = BuilderFactory.createServiceProvider();
        repository = new MySqlServiceProviderRepository(hsqldbRule::getConnection);
    }

    @Test
    public void shouldAddServiceProvider() throws Exception {
        // Given
        final ServiceProvider serviceProvider = serviceProviderBuilder.id(1).build();

        // When
        repository.add(serviceProvider);
        final List<ServiceProvider> serviceProviders = repository.query(new GetServiceProviderByIdSpecification(1));

        // Then
        assertThat(serviceProviders, hasSize(1));
        assertThat(serviceProviders, hasItem(serviceProvider));
    }

    @Test
    public void shouldAddAllServiceProviders() throws Exception {
        // Given
        final ServiceProvider serviceProvider1 = serviceProviderBuilder.id(1).build();
        final ServiceProvider serviceProvider2 = serviceProviderBuilder.id(2).build();
        final ServiceProvider serviceProvider3 = serviceProviderBuilder.id(3).build();

        // When
        repository.add(Arrays.asList(serviceProvider1, serviceProvider2, serviceProvider3));
        final List<ServiceProvider> serviceProviders = repository.query(new GetAllServiceProvidersSpecification());

        // Then
        assertThat(serviceProviders, hasSize(3));
        assertThat(serviceProviders, hasItems(serviceProvider1, serviceProvider2, serviceProvider3));
    }

    @Test
    public void shouldUpdateServiceProvider() throws Exception {
        // Given
        final ServiceProvider serviceProvider = serviceProviderBuilder
                .id(1)
                .name("test1")
                .build();

        final ServiceProvider updatedServiceProvider = serviceProviderBuilder
                .id(1)
                .name("test2")
                .build();

        // When
        repository.add(serviceProvider);
        repository.update(updatedServiceProvider);

        final List<ServiceProvider> serviceProviders = repository.query(
                new GetServiceProviderByIdSpecification(serviceProvider.getId()));

        // Then
        assertThat(serviceProviders, hasSize(1));
        assertThat(serviceProviders.get(0).getId(), is(1));
        assertThat(serviceProviders, hasItem(updatedServiceProvider));
    }

    @Test
    public void shouldQuerySpecifiedServiceProvider() throws Exception {
        // Given
        final ServiceProvider serviceProvider1 = serviceProviderBuilder.id(1).build();
        final ServiceProvider serviceProvider2 = serviceProviderBuilder.id(2).build();
        final ServiceProvider serviceProvider3 = serviceProviderBuilder.id(3).build();

        // When
        repository.add(Arrays.asList(serviceProvider1, serviceProvider2, serviceProvider3));

        final List<ServiceProvider> serviceProviders = repository.query(
                new GetServiceProviderByIdSpecification(serviceProvider2.getId()));

        // Then
        assertThat(serviceProviders, hasSize(1));
        assertThat(serviceProviders, hasItem(serviceProvider2));
    }

    @Test
    public void shouldRemoveServiceProvider() throws Exception {
        // Given
        final ServiceProvider serviceProvider = serviceProviderBuilder.id(1).build();

        // When
        repository.add(serviceProvider);
        repository.remove(serviceProvider);

        final List<ServiceProvider> serviceProviders = repository.query(new GetAllServiceProvidersSpecification());

        // Then
        assertThat(serviceProviders, is(empty()));
    }

    @Test
    public void shouldRemoveAllServiceProviders() throws Exception {
        // Given
        final ServiceProvider serviceProvider1 = serviceProviderBuilder.id(1).build();
        final ServiceProvider serviceProvider2 = serviceProviderBuilder.id(2).build();
        final ServiceProvider serviceProvider3 = serviceProviderBuilder.id(3).build();

        // When
        repository.add(Arrays.asList(serviceProvider1, serviceProvider2, serviceProvider3));
        repository.remove(Arrays.asList(serviceProvider1, serviceProvider3));

        final List<ServiceProvider> serviceProvider = repository.query(new GetAllServiceProvidersSpecification());

        // Then
        assertThat(serviceProvider, hasSize(1));
        assertThat(serviceProvider, hasItem(serviceProvider2));
    }

    @Test
    public void shouldRemoveSpecifiedServiceProvider() throws Exception {
        // Given
        final ServiceProvider serviceProvider1 = serviceProviderBuilder.id(1).build();
        final ServiceProvider serviceProvider2 = serviceProviderBuilder.id(2).build();
        final ServiceProvider serviceProvider3 = serviceProviderBuilder.id(3).build();

        // When
        repository.add(Arrays.asList(serviceProvider1, serviceProvider2, serviceProvider3));
        repository.remove(new GetServiceProviderByIdSpecification(3));

        final List<ServiceProvider> serviceProvider = repository.query(
                new GetAllServiceProvidersSpecification());

        // Then
        assertThat(serviceProvider, hasSize(2));
        assertThat(serviceProvider, hasItems(serviceProvider1, serviceProvider2));
    }
}
