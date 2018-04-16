package tdt4140.gr1817.ecosystem.persistence.repositories.mysql.specification.improvify;

import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.specification.SqlSpecification;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.Date;
import java.util.Locale;

public class GetWorkoutSessionByLoggedByAndDateSpecification implements SqlSpecification {

    private final int loggedBy;
    private final String startDate;
    private final String stopDate;

    public GetWorkoutSessionByLoggedByAndDateSpecification(int loggedBy, String startDate, String stopDate) {
        this.loggedBy = loggedBy;
        this.startDate = startDate;
        this.stopDate = stopDate;
    }

    @Override
    public PreparedStatement toStatement(Connection connection) throws SQLException {
        final String sql = "SELECT * FROM workoutsession WHERE loggedBy = ? AND ? < workoutsession.startTime < ?";
        final PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, loggedBy);
        DateFormat format = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
        try {
            java.util.Date date1 = format.parse(startDate);
            java.util.Date date2 = format.parse(stopDate);
            Date date01 = new java.sql.Date(date1.getTime());
            Date date02 = new java.sql.Date(date2.getTime());
            preparedStatement.setDate(2, date01);
            preparedStatement.setDate(3, date02);
        } catch (ParseException e) {
            System.out.println("Unable to find date");
        }
        return preparedStatement;
    }
}
