package tdt4140.gr1817.ecosystem.persistence.repositories.mysql;

import lombok.extern.slf4j.Slf4j;
import tdt4140.gr1817.ecosystem.persistence.Specification;
import tdt4140.gr1817.ecosystem.persistence.data.User;
import tdt4140.gr1817.ecosystem.persistence.repositories.UserRepository;

import javax.inject.Inject;
import javax.inject.Provider;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
        for (User user : items) {
            this.add(user);
        }
        ;
    }


    /*@Override
    public User getUser(int idNr){
        try (
                Connection connection = this.connection.get();
                String query = "SELECT * FROM user WHERE id = idNr";
                ){

        }   catch (SQLException e){
            throw new RuntimeException();
        }



        return user
    }
*/
    public User getUser(int idNr) {
        try {
            String mySQLgetter = "SELECT id, birthdate, email, firstname, lastname, height,"
                    + " username, password FROM useraccount WHERE ID = ?";
            try (
                    Connection connection = this.connection.get();
                    PreparedStatement preparedStatement = connection.prepareStatement(mySQLgetter)
            ) {


                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        int userId = resultSet.getInt(1);

                    }
                }


            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get user", e);
        }
        throw new UnsupportedOperationException("Not fully implemented");
//        User user = new User();
//        return user;
    }

    /*
        public User getUser (int idNr){
            try{
                Connection connection = this.connection.get();
                Statement st = connection.createStatement();
                String sql = ("SELECT * FROM posts ORDER BY id DESC LIMIT 1;");
                ResultSet rs = st.executeQuery(sql);
                int id = rs.getInt("first_column_name");
                String str1 = rs.getString("second_column_name");
            } catch (SQLException e){
                throw new RuntimeException();
            }

            con.close();
        }*/
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


    private static void setParameters(PreparedStatement statement, Object... parameters) throws SQLException {
        for (int i = 0; i < parameters.length; i++) {
            // Parameters are 1-indexed
            statement.setObject(i + 1, parameters[i]);
        }
    }
}
