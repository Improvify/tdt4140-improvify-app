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



    public User getUser(int idNr) {
        try {
            String mySQLgetter = "SELECT id, birthdate, email, firstname, lastname, height,"
                    + " username, password FROM useraccount WHERE ID = ?";
            try (
                    Connection connection = this.connection.get();
                    PreparedStatement preparedStatement = connection.prepareStatement(mySQLgetter)
            ) {
                preparedStatement.setInt(1, idNr);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        int userId = resultSet.getInt(1);
                        Date birthdate = resultSet.getDate(2);
                        String email = resultSet.getString(3);
                        String firstname = resultSet.getString(4);
                        String lastname = resultSet.getString(5);
                        float height = resultSet.getFloat(6);
                        String username = resultSet.getString(7);
                        String password = resultSet.getString(8);

                        return new User(userId, firstname, lastname, height, birthdate, username,
                                password, email);
                    }
                }


            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get user", e);
        }

        return null;
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
    public void remove(User item) {
        try {
            int id = item.getId();
            String deleteUser = "DELETE FROM useraccount WHERE id = '" + id + "'";
            Connection connection = this.connection.get();
            PreparedStatement pst = connection.prepareStatement((deleteUser));
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
            int idIndex = resultSet.findColumn("id");
            int firstnameIndex = resultSet.findColumn("firstname");
            int lastnameIndex = resultSet.findColumn("lastname");
            int emailIndex = resultSet.findColumn("email");
            int heightIndex = resultSet.findColumn("height");
            int usernameIndex = resultSet.findColumn("username");
            int passwordIndex = resultSet.findColumn("password");
            int birthdateIndex = resultSet.findColumn("birthdate");


            int id = resultSet.getInt(idIndex);
            String firstname = resultSet.getString(firstnameIndex);
            String lastname = resultSet.getString(lastnameIndex);
            String email = resultSet.getString(emailIndex);
            String username = resultSet.getString(usernameIndex);
            String password = resultSet.getString(passwordIndex);
            float height = resultSet.getFloat(heightIndex);
            Date birthdate = resultSet.getDate(birthdateIndex);
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
