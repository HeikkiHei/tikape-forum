package fi.heviweight.forum.pojo;

import fi.heviweight.forum.db.Database;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

public class TopicDao {

    private Database db;

    public TopicDao(Database db) {
        this.db = db;
    }

    public List<Topic> getTopics(int boardId) throws SQLException {
        try (Connection con = db.getConnection()) {
            PreparedStatement stmt = con.prepareStatement(
                    "SELECT board.id AS boardId, "
                    + "board.name AS boardName, topic.id As topicId, "
                    + "topic.name AS topicName, COUNT(Post.id) AS Viesteja, "
                    + "post.timestamp AS Viimeisin \n"
                    + "FROM board, topic, post \n"
                    + "WHERE board.id = topic.board_id \n"
                    + "AND board.id = ? "
                    + "AND post.topic_id = topic.id \n"
                    + "GROUP BY topic.name \n"
                    + "ORDER BY post.timestamp DESC LIMIT 10;");
            stmt.setInt(1, boardId);
            return db.queryAndCollect(stmt, rs -> {
                return new Topic(
                        rs.getInt("boardId"),
                        rs.getInt("topicId"),
                        rs.getString("topicName"),
                        rs.getString("boardName"),
                        rs.getInt("Viesteja"),
                        rs.getString("Viimeisin"));
            });
        }
    }

    public List<Topic> getTopic(int topicId) throws SQLException {
        try (Connection con = db.getConnection()) {
            PreparedStatement stmt = con.prepareStatement(
                    "SELECT board.id AS boardId, "
                    + "board.name AS boardName, topic.id As topicId, "
                    + "topic.name AS topicName, COUNT(Post.id) AS Viesteja, "
                    + "post.timestamp AS Viimeisin \n"
                    + "FROM board, topic, post \n"
                    + "WHERE board.id = topic.board_id \n"
                    + "AND topic.id = ? "
                    + "AND post.topic_id = ? \n"
                    + "GROUP BY topic.name \n"
                    + "ORDER BY post.timestamp DESC LIMIT 10;");
            stmt.setInt(1, topicId);
            stmt.setInt(2, topicId);
            return db.queryAndCollect(stmt, rs -> {
                return new Topic(
                        rs.getInt("boardId"),
                        rs.getInt("topicId"),
                        rs.getString("topicName"),
                        rs.getString("boardName"),
                        rs.getInt("Viesteja"),
                        rs.getString("Viimeisin"));
            });
        }
    }

    public int addTopic(String message, int boardId) throws SQLException {
        try (Connection con = db.getConnection()) {
            PreparedStatement stmt = con.prepareStatement(
                    "INSERT INTO topic (board_id, name) VALUES (?, ?);");
            stmt.setInt(1, boardId);
            stmt.setString(2, message);
            db.execute(stmt);
            return lastTopic();
        }
    }

    private Integer lastTopic() throws SQLException {
        try (Connection con = db.getConnection()) {
            PreparedStatement stmt = con.prepareStatement(
                    "SELECT topic.id FROM topic ORDER BY topic.id DESC LIMIT 1;");
            List<Integer> topics = db.queryAndCollect(stmt, rs -> {
                return new Integer(rs.getInt("id"));
            });
            return topics.get(0);
        }
    }
}
