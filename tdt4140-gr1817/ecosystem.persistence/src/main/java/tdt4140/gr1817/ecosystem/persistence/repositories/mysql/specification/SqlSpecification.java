package tdt4140.gr1817.ecosystem.persistence.repositories.mysql.specification;

import tdt4140.gr1817.ecosystem.persistence.Specification;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface SqlSpecification extends Specification {
    PreparedStatement toStatement(Connection connection) throws SQLException;
}
