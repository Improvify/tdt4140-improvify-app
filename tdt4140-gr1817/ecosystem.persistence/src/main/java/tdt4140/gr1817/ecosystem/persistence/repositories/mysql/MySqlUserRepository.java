package tdt4140.gr1817.ecosystem.persistence.repositories.mysql;

import lombok.extern.slf4j.Slf4j;
import tdt4140.gr1817.ecosystem.persistence.Specification;
import tdt4140.gr1817.ecosystem.persistence.data.User;
import tdt4140.gr1817.ecosystem.persistence.repositories.UserRepository;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.specification.SqlSpecification;

import javax.inject.Inject;
import javax.inject.Provider;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

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
        for (User user : items) {
            this.add(user);
        }

    }

    @Override
    public void update(User item) {
        String firstName = item.getFirstName();
        String lastName = item.getLastName();
        float height = item.getHeight();
        Date birthDate = item.getBirthDate();
        String userName = item.getUsername();
        String password = item.getPassword();
        String email = item.getEmail();

        String updateUserSql = "UPDATE useraccount SET firstName = ?, lastName = ?,"
                + " height = ?, birthDate = ?, userName = ?, password = ?, email = ?";

        try (
                Connection connection = this.connection.get();
                PreparedStatement pst = connection.prepareStatement(updateUserSql)
        ) {
            setParameters(pst, firstName, lastName, height, birthDate, userName, password, email);
            pst.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Update not successful");
        }
    }

    @Override
    public void remove(User user) {
        try (
                Connection connection = this.connection.get();
                PreparedStatement statement = connection.prepareStatement("DELETE FROM useraccount WHERE id = ?")
        ) {
            setParameters(statement, user.getId());
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Delete not successful");
        }
    }

    @Override
    public void remove(Iterable<User> items) {
        for (User user : items) {
            this.remove(user);
        }
    }

    @Override
    public void remove(Specification specification) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public List<User> query(Specification specification) {
        SqlSpecification sqlSpecification = (SqlSpecification) specification;
        try (
                Connection connection = this.connection.get();
                PreparedStatement statement = sqlSpecification.toStatement(connection);
                ResultSet resultSet = statement.executeQuery()
        ) {
            ArrayList<User> results = new ArrayList<>();
            if (resultSet.next()) {
                results.add(createUserFromResultSet(resultSet));
            }
            return results;
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to get user", ex);
        }
    }

    private static User createUserFromResultSet(ResultSet resultSet) {
        try {
            int id = resultSet.getInt("id");
            String firstname = resultSet.getString("firstname");
            String lastname = resultSet.getString("lastname");
            String email = resultSet.getString("email");
            String username = resultSet.getString("username");
            String password = resultSet.getString("password");
            float height = resultSet.getFloat("height");
            Date birthdate = resultSet.getDate("birthdate");

            return new User(id, firstname, lastname, height, birthdate, username, password, email);
        } catch (SQLException e) {
            throw new RuntimeException("Unable to create user", e);
        }
    }

    private static void setParameters(PreparedStatement statement, Object... parameters) throws SQLException {
        for (int i = 0; i < parameters.length; i++) {
            // Parameters are 1-indexed
            statement.setObject(i + 1, parameters[i]);
        }
    }
}
