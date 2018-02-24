package tdt4140.gr1817.ecosystem.persistence.repositories.mysql;

import lombok.extern.slf4j.Slf4j;
import tdt4140.gr1817.ecosystem.persistence.Specification;
import tdt4140.gr1817.ecosystem.persistence.data.User;
import tdt4140.gr1817.ecosystem.persistence.repositories.UserRepository;

import javax.inject.Inject;
import javax.inject.Provider;
import java.sql.Connection;
import java.sql.Date;
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
    public void add(User item) {
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
                preparedStatement.setString(1, item.getFirstName());
                preparedStatement.setString(2, item.getLastName());
                preparedStatement.setFloat(3, item.getHeight());
                preparedStatement.setDate(4, new Date(item.getBirthDate().getTime()));
                preparedStatement.setString(5, item.getUsername());
                preparedStatement.setString(6, item.getPassword());
                preparedStatement.setString(7, item.getEmail());
                preparedStatement.setInt(8, item.getId());

                preparedStatement.execute();
            }

        } catch (SQLException e) {
//            log.error("Failed to add user", e);
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
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void remove(Specification specification) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public List<User> query(Specification specification) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
