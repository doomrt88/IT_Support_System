package models.dao;

import models.entity.Comment;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class CommentRepository extends BaseRepository<Comment> {

    @Override
    protected String getTableName() {
        return "comments";
    }

    @Override
    protected Comment mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        Comment comment = new Comment();
        comment.setId(resultSet.getInt("comment_id"));
        comment.setRemarks(resultSet.getString("remarks"));
        comment.setIssueId(resultSet.getInt("issue_id"));
        comment.setCreatedBy(resultSet.getInt("created_by"));
        comment.setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime());
        comment.setUpdatedBy(resultSet.getInt("updated_by"));
        comment.setUpdatedAt(resultSet.getTimestamp("updated_at").toLocalDateTime());
        return comment;
    }

    @Override
    protected String getIdColumnName() {
        return "comment_id";
    }

    @Override
    protected String getInsertSql() {
        return "INSERT INTO comments (remarks, issue_id, created_by, created_at, updated_by, updated_at) VALUES (?, ?, ?, ?, ?, ?)";
    }

    @Override
    protected String getUpdateSql() {
        return "UPDATE comments SET remarks = ?, issue_id = ?, updated_by = ?, updated_at = ? WHERE comment_id = ?";
    }

    @Override
    protected void setInsertParameters(PreparedStatement preparedStatement, Comment entity) throws SQLException {
        preparedStatement.setString(1, entity.getRemarks());
        preparedStatement.setInt(2, entity.getIssueId());
        preparedStatement.setInt(3, entity.getCreatedBy());
        preparedStatement.setTimestamp(4, Timestamp.valueOf(entity.getCreatedAt()));
        preparedStatement.setInt(5, entity.getUpdatedBy());
        preparedStatement.setTimestamp(6, Timestamp.valueOf(entity.getUpdatedAt()));
    }

    @Override
    protected void setUpdateParameters(PreparedStatement preparedStatement, Comment entity) throws SQLException {
        preparedStatement.setString(1, entity.getRemarks());
        preparedStatement.setInt(2, entity.getIssueId());
        preparedStatement.setInt(3, entity.getUpdatedBy());
        preparedStatement.setTimestamp(4, Timestamp.valueOf(entity.getUpdatedAt()));
        preparedStatement.setInt(5, entity.getId());
    }
}
