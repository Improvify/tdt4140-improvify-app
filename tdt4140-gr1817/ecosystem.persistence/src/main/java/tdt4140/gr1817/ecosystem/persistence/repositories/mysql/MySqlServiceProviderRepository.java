package tdt4140.gr1817.ecosystem.persistence.repositories.mysql;

import lombok.extern.slf4j.Slf4j;
import tdt4140.gr1817.ecosystem.persistence.Specification;
import tdt4140.gr1817.ecosystem.persistence.data.ServiceProvider;
import tdt4140.gr1817.ecosystem.persistence.repositories.ServiceProviderRepository;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.specification.SqlSpecification;

import javax.inject.Inject;
import javax.inject.Provider;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class MySqlServiceProviderRepository implements ServiceProviderRepository {

    private final Provider<Connection> connection;

    @Inject
    public MySqlServiceProviderRepository(Provider<Connection> connection) {
        this.connection = connection;
    }

    @Override
    public void add(ServiceProvider serviceProvider) {
        final String sql = "INSERT INTO serviceprovider(id, Name) VALUES (?, ?)";
        try (
                Connection connection = this.connection.get();
                PreparedStatement statement = connection.prepareStatement(sql);
        ) {
            setParameters(statement, serviceProvider.getId(), serviceProvider.getName());
            statement.execute();

        } catch (SQLException ex) {
            throw new RuntimeException("Failed to add service provider", ex);
        }
    }

    @Override
    public void add(Iterable<ServiceProvider> serviceProviders) {
        for (ServiceProvider serviceProvider : serviceProviders) {
            add(serviceProvider);
        }
    }

    @Override
    public void update(ServiceProvider serviceProvider) {
        final String sql = "UPDATE serviceprovider SET Name=? WHERE id = ?";

        try (
                Connection connection = this.connection.get();
                PreparedStatement statement = connection.prepareStatement(sql);
        ) {
            setParameters(statement, serviceProvider.getName(), serviceProvider.getId());
            statement.execute();

        } catch (SQLException ex) {
            throw new RuntimeException("Unable to update service provider", ex);
        }

    }

    @Override
    public void remove(ServiceProvider serviceProvider) {
        final String sql = "DELETE FROM serviceprovider WHERE id = ?";

        try (
                Connection connection = this.connection.get();
                PreparedStatement statement = connection.prepareStatement(sql);
        ) {
            setParameters(statement, serviceProvider.getId());
            statement.execute();

        } catch (SQLException ex) {
            throw new RuntimeException("Unable to delete service provider", ex);
        }

    }

    @Override
    public void remove(Iterable<ServiceProvider> serviceProviders) {
        for (ServiceProvider serviceProvider : serviceProviders) {
            remove(serviceProvider);
        }
    }

    @Override
    public void remove(Specification specification) {
        List<ServiceProvider> serviceProviders = query(specification);
        remove(serviceProviders);
    }

    @Override
    public List<ServiceProvider> query(Specification specification) {
        final SqlSpecification sqlSpecification = (SqlSpecification) specification;

        try (
                Connection connection = this.connection.get();
                PreparedStatement statement = sqlSpecification.toStatement(connection);
                ResultSet resultSet = statement.executeQuery();
        ) {
            final ArrayList<ServiceProvider> results = new ArrayList<>();
            while (resultSet.next()) {
                results.add(createServiceProviderFromResultSet(resultSet));
            }
            return results;
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to get service providers", ex);
        }
    }

    private ServiceProvider createServiceProviderFromResultSet(ResultSet resultSet) {
        try {
            final int id = resultSet.getInt("id");
            final String name = resultSet.getString("Name");

            return new ServiceProvider(id, name);
        } catch (SQLException ex) {
            throw new RuntimeException("Unable to create service provider", ex);
        }
    }

    private static void setParameters(PreparedStatement statement, Object... parameters) throws SQLException {
        for (int i = 0; i < parameters.length; i++) {
            // Parameters are 1-indexed
            statement.setObject(i + 1, parameters[i]);
        }
    }
}
