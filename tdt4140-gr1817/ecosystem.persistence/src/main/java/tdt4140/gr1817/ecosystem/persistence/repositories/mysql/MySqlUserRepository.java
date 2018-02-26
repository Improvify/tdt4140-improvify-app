package tdt4140.gr1817.ecosystem.persistence.repositories.mysql;

import lombok.extern.slf4j.Slf4j;
import tdt4140.gr1817.ecosystem.persistence.Specification;
import tdt4140.gr1817.ecosystem.persistence.data.User;
import tdt4140.gr1817.ecosystem.persistence.repositories.UserRepository;

import javax.inject.Inject;
import javax.inject.Provider;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Slf4j
public class MySqlUserRepository implements UserRepository {

    private Provider<Connection> connection;

    @Inject
    public MySqlUserRepository(Provider<Connection> connection) {
        this.connection = connection;
    }


    @Override
    public void add(User user) {
        /*
        Currently just a test implementation.
        This code is ugly, please find a cleaner way to do this.
         */
        try {
            String insertSql = "INSERT INTO useraccount(firstname, lastname, height, birthdate, "
                    + "username, password, email, id) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            try (
                    Connection connection = this.connection.get();
                    PreparedStatement preparedStatement = connection.prepareStatement(insertSql)
            ) {
                setParameters(preparedStatement, user.getFirstName(), user.getLastName(), user.getHeight(),
                        user.getBirthDate(), user.getUsername(), user.getPassword(), user.getEmail(), user.getId());

                preparedStatement.execute();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to add user", e);
        }
    }

    @Override
    public void add(Iterable<User> items) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void update(User item) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void remove(User item) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void remove(Iterable<User> items) {

    }

    @Override
    public void remove(Specification specification) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public List<User> query(Specification specification) {
        throw new UnsupportedOperationException("Not implemented");
    }


    private static void setParameters(PreparedStatement statement, Object... parameters) throws SQLException {
        for (int i = 0; i < parameters.length; i++) {
            // Parameters are 1-indexed
            statement.setObject(i + 1, parameters[i]);
        }
    }
}
