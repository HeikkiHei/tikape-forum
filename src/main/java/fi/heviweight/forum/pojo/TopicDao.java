package fi.heviweight.forum.pojo;

import fi.heviweight.forum.db.Database;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

public class TopicDao {
    private Database db;

    public TopicDao(Database db) {
        this.db = db;
    }
    
    public List<Topic> getTopics(int boardId) throws SQLException{
        PreparedStatement stmt = db.getConnection().prepareStatement(
                "SELECT Board.id AS boardId, " +
                "Board.name AS boardName, Topic.id As topicId, " +
                "Topic.name AS topicName, COUNT(Post.id) AS Viestejä, " +
                "Post.timestamp AS 'Viimeisin viesti' \n" +
                    "FROM Board, Topic, Post \n" +
                    "WHERE Board.id = Topic.board_id \n" +
                    "AND Board.id = ? " + 
                    "AND Post.topic_id = Topic.id \n" +
                    "GROUP BY Topic.name \n" +
                    "ORDER BY Post.timestamp DESC LIMIT 10;");
        stmt.setInt(1, boardId);
        return db.queryAndCollect(stmt, rs -> {
            return new Topic(
                    rs.getInt("boardId"), 
                    rs.getInt("topicId"),
                    rs.getString("topicName"),
                    rs.getString("boardName"),
                    rs.getInt("Viestejä"),
                    rs.getString("Viimeisin viesti"));
                });
    }
    
    public int addTopic(String message, int boardId) throws SQLException {
        PreparedStatement stmt = db.getConnection().prepareStatement(
                "INSERT INTO Topic (board_id, name) VALUES (?, ?);");
        stmt.setInt(1, boardId);
        stmt.setString(2, message);
        db.execute(stmt);
        Topic t = lastTopic();
        return t.getId();
    }

    private Topic lastTopic() throws SQLException {
        PreparedStatement stmt = db.getConnection().prepareStatement(
                "SELECT topic.id FROM Topic ORDER BY topic.id DESC LIMIT 1");
        List<Topic> topics = db.queryAndCollect(stmt, rs -> {
                    return new Topic(
                    rs.getInt("boardId"), 
                    rs.getInt("topicId"),
                    rs.getString("topicName"),
                    rs.getString("boardName"),
                    rs.getInt("Viestejä"),
                    rs.getString("Viimeisin viesti"));
                });
        return topics.get(0);
    }
}
