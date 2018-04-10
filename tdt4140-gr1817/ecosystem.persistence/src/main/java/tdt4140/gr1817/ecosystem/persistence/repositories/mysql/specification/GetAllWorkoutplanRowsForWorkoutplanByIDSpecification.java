package tdt4140.gr1817.ecosystem.persistence.repositories.mysql.specification;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class GetAllWorkoutplanRowsForWorkoutplanByIDSpecification implements SqlSpecification {


    private final int id;

    public GetAllWorkoutplanRowsForWorkoutplanByIDSpecification(int id) {
        this.id = id;
    }


    @Override
    public PreparedStatement toStatement(Connection connection) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM workoutplan where "
                + "WorkoutPlan_id = ?");
        preparedStatement.setInt(1, id);
        return preparedStatement;
    }
}
