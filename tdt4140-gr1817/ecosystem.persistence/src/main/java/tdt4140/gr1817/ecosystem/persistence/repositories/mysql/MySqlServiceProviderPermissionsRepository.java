package tdt4140.gr1817.ecosystem.persistence.repositories.mysql;

import lombok.extern.slf4j.Slf4j;
import tdt4140.gr1817.ecosystem.persistence.Specification;
import tdt4140.gr1817.ecosystem.persistence.data.ServiceProvider;
import tdt4140.gr1817.ecosystem.persistence.data.ServiceProviderPermissions;
import tdt4140.gr1817.ecosystem.persistence.data.User;
import tdt4140.gr1817.ecosystem.persistence.repositories.ServiceProviderPermissionsRepository;
import tdt4140.gr1817.ecosystem.persistence.repositories.ServiceProviderRepository;
import tdt4140.gr1817.ecosystem.persistence.repositories.UserRepository;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.specification.GetServiceProviderByIdSpecification;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.specification.GetUserByIdSpecification;
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
public class MySqlServiceProviderPermissionsRepository implements ServiceProviderPermissionsRepository {

    private final Provider<Connection> connection;
    private final ServiceProviderRepository serviceProviderRepository;
    private final UserRepository userRepository;

    @Inject
    public MySqlServiceProviderPermissionsRepository(Provider<Connection> connection,
                                                     ServiceProviderRepository serviceProviderRepository,
                                                     UserRepository userRepository) {
        this.connection = connection;
        this.serviceProviderRepository = serviceProviderRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void add(ServiceProviderPermissions serviceProviderPermissions) {
        final String sql = "INSERT INTO serviceproviderpermissions(UserAccount_id, ServiceProvider_id, Weight,"
                + "Height, Email, Name, Username, RestingHeartRate, WorkoutSession, Birthdate)"
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (
                Connection connection = this.connection.get();
                PreparedStatement statement = connection.prepareStatement(sql);
        ) {
            setParameters(statement, serviceProviderPermissions.getUser().getId(),
                    serviceProviderPermissions.getServiceProvider().getId(),
                    serviceProviderPermissions.isWeight(),
                    serviceProviderPermissions.isHeight(),
                    serviceProviderPermissions.isEmail(),
                    serviceProviderPermissions.isName(),
                    serviceProviderPermissions.isUsername(),
                    serviceProviderPermissions.isRestingHeartRate(),
                    serviceProviderPermissions.isWorkoutSession(),
                    serviceProviderPermissions.isBirthDate());

            statement.execute();

        } catch (SQLException ex) {
            throw new RuntimeException("Failed to add service provider permissions", ex);
        }
    }

    @Override
    public void add(Iterable<ServiceProviderPermissions> serviceProviderPermissionsIterable) {
        for (ServiceProviderPermissions serviceProviderPermissions : serviceProviderPermissionsIterable) {
            add(serviceProviderPermissions);
        }
    }

    @Override
    public void update(ServiceProviderPermissions serviceProviderPermissions) {
        final String sql = "UPDATE serviceproviderpermissions SET Weight=?,"
                + "Height=?, Email=?, Name=?, Username=?, RestingHeartRate=?, WorkoutSession=?, Birthdate=?"
                + "WHERE UserAccount_id=? AND ServiceProvider_id=?";

        try (
                Connection connection = this.connection.get();
                PreparedStatement statement = connection.prepareStatement(sql);
        ) {
            setParameters(statement, serviceProviderPermissions.isWeight(),
                    serviceProviderPermissions.isHeight(),
                    serviceProviderPermissions.isEmail(),
                    serviceProviderPermissions.isName(),
                    serviceProviderPermissions.isUsername(),
                    serviceProviderPermissions.isRestingHeartRate(),
                    serviceProviderPermissions.isWorkoutSession(),
                    serviceProviderPermissions.isBirthDate(),
                    serviceProviderPermissions.getUser().getId(),
                    serviceProviderPermissions.getServiceProvider().getId());
            statement.execute();

        } catch (SQLException ex) {
            throw new RuntimeException("Unable to update service provider permissions", ex);
        }

    }

    @Override
    public void remove(ServiceProviderPermissions serviceProviderPermissions) {
        final String sql = "DELETE FROM serviceproviderpermissions WHERE UserAccount_id = ? AND ServiceProvider_id = ?";

        try (
                Connection connection = this.connection.get();
                PreparedStatement statement = connection.prepareStatement(sql);
        ) {
            setParameters(statement, serviceProviderPermissions.getUser().getId(),
                    serviceProviderPermissions.getServiceProvider().getId());
            statement.execute();

        } catch (SQLException ex) {
            throw new RuntimeException("Unable to delete service provider permissions", ex);
        }

    }

    @Override
    public void remove(Iterable<ServiceProviderPermissions> serviceProviderPermissionsIterable) {
        for (ServiceProviderPermissions serviceProviderPermissions : serviceProviderPermissionsIterable) {
            remove(serviceProviderPermissions);
        }
    }

    @Override
    public void remove(Specification specification) {
        List<ServiceProviderPermissions> serviceProviderPermissionsList = query(specification);
        remove(serviceProviderPermissionsList);
    }

    @Override
    public List<ServiceProviderPermissions> query(Specification specification) {
        final SqlSpecification sqlSpecification = (SqlSpecification) specification;

        try (
                Connection connection = this.connection.get();
                PreparedStatement statement = sqlSpecification.toStatement(connection);
                ResultSet resultSet = statement.executeQuery();
        ) {
            final ArrayList<ServiceProviderPermissions> results = new ArrayList<>();
            while (resultSet.next()) {
                results.add(createServiceProviderPermissionsFromResultSet(resultSet));
            }
            return results;
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to get service providers permissions", ex);
        }
    }

    private ServiceProviderPermissions createServiceProviderPermissionsFromResultSet(ResultSet resultSet) {
        try {
            final int serviceProviderID = resultSet.getInt("ServiceProvider_id");
            final int userAccountID = resultSet.getInt("UserAccount_id");
            final boolean weight = resultSet.getBoolean("Weight");
            final boolean height = resultSet.getBoolean("Height");
            final boolean email = resultSet.getBoolean("Email");
            final boolean name = resultSet.getBoolean("Name");
            final boolean username = resultSet.getBoolean("Username");
            final boolean restingHeartRate = resultSet.getBoolean("RestingHeartRate");
            final boolean workoutSession = resultSet.getBoolean("WorkoutSession");
            final boolean birthdate = resultSet.getBoolean("Birthdate");


            ServiceProvider serviceProvider = serviceProviderRepository.query(
                    new GetServiceProviderByIdSpecification(serviceProviderID)).get(0);
            User user = userRepository.query(new GetUserByIdSpecification(userAccountID)).get(0);

            return new ServiceProviderPermissions(user, serviceProvider, weight, height,
                    email, name, username, restingHeartRate, workoutSession, birthdate);

        } catch (SQLException ex) {
            throw new RuntimeException("Unable to create service provider permissions", ex);
        }
    }

    private static void setParameters(PreparedStatement statement, Object... parameters) throws SQLException {
        for (int i = 0; i < parameters.length; i++) {
            // Parameters are 1-indexed
            statement.setObject(i + 1, parameters[i]);
        }
    }
}
