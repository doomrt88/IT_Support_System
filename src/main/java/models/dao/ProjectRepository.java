package models.dao;

import models.entity.Project;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class ProjectRepository extends BaseRepository<Project> {

    @Override
    protected String getTableName() {
        return "projects";
    }

    @Override
    protected Project mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        Project project = new Project();
        project.setId(resultSet.getInt("project_id"));
        project.setName(resultSet.getString("name"));
        project.setDescription(resultSet.getString("description"));
        
        LocalDateTime startDate = convertUtcToLocal(resultSet.getTimestamp("start_date"));
        LocalDateTime endDate = convertUtcToLocal(resultSet.getTimestamp("end_date"));
        
        project.setStartDate(startDate);
        project.setEndDate(endDate);
        project.setCreatedBy(resultSet.getInt("created_by"));
        project.setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime());
        project.setUpdatedBy(resultSet.getInt("updated_by"));
        project.setUpdatedAt(resultSet.getTimestamp("updated_at").toLocalDateTime());
        return project;
    }

    @Override
    protected String getIdColumnName() {
        return "project_id";
    }

    @Override
    protected String getInsertSql() {
        return "INSERT INTO projects (name, description, start_date, end_date, created_by, created_at, updated_by, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    }

    @Override
    protected String getUpdateSql() {
        return "UPDATE projects SET name = ?, description = ?, start_date = ?, end_date = ?, updated_by = ?, updated_at = ? WHERE project_id = ?";
    }

    @Override
    protected void setInsertParameters(PreparedStatement preparedStatement, Project entity) throws SQLException {
        preparedStatement.setString(1, entity.getName());
        preparedStatement.setString(2, entity.getDescription());
        
        preparedStatement.setObject(3, entity.getStartDate());
        preparedStatement.setObject(4, entity.getEndDate());
        
//        preparedStatement.setTimestamp(3, Timestamp.valueOf(entity.getStartDate()));
//        preparedStatement.setTimestamp(4, Timestamp.valueOf(entity.getEndDate()));
        preparedStatement.setInt(5, entity.getCreatedBy());
        preparedStatement.setTimestamp(6, Timestamp.valueOf(entity.getCreatedAt()));
        preparedStatement.setInt(7, entity.getUpdatedBy());
        preparedStatement.setTimestamp(8, Timestamp.valueOf(entity.getUpdatedAt()));
    }

    @Override
    protected void setUpdateParameters(PreparedStatement preparedStatement, Project entity) throws SQLException {
        preparedStatement.setString(1, entity.getName());
        preparedStatement.setString(2, entity.getDescription());        
        preparedStatement.setObject(3, entity.getStartDate());
        preparedStatement.setObject(4, entity.getEndDate());
        
//        preparedStatement.setTimestamp(3, Timestamp.valueOf(entity.getStartDate()));
//        preparedStatement.setTimestamp(4, Timestamp.valueOf(entity.getEndDate()));
        preparedStatement.setInt(5, entity.getUpdatedBy());
        preparedStatement.setTimestamp(6, Timestamp.valueOf(entity.getUpdatedAt()));
        preparedStatement.setInt(7, entity.getId());
    }
    
    public Project getByName(int id, String name) {
        String sql = "SELECT * FROM projects WHERE name = ? AND project_id <> ?";
        Connection connection = null;
        try {
        	connection = DbContext.getInstance().getConnection();
        	PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToEntity(resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if (connection != null) {
                DbContext.getInstance().releaseConnection(connection);
            }
        }
        
        return null;
    }
    
    public static Timestamp convertToUTC(LocalDateTime localDateTime) {
        LocalDateTime utcDateTime = localDateTime.atZone(ZoneId.systemDefault())
                                                .withZoneSameInstant(ZoneOffset.UTC)
                                                .toLocalDateTime();
        return Timestamp.valueOf(utcDateTime);
    }
    
    public static LocalDateTime convertUtcToLocal(Timestamp utcTimestamp) {
        LocalDateTime utcDateTime = utcTimestamp.toLocalDateTime();
        ZoneId utcZoneId = ZoneId.of("UTC");
        //ZonedDateTime utcZonedDateTime = ZonedDateTime.of(utcDateTime, utcZoneId);
        //ZonedDateTime localZonedDateTime = utcZonedDateTime.withZoneSameInstant(ZoneId.systemDefault());
        
        ZonedDateTime utcZonedDateTime = ZonedDateTime.of(utcDateTime, utcZoneId);
        ZoneId targetZoneId = ZoneId.of("UTC+8"); // Set target time zone to UTC+8
        ZonedDateTime localZonedDateTime = utcZonedDateTime.withZoneSameInstant(targetZoneId);
        return localZonedDateTime.toLocalDateTime();
    }
}
