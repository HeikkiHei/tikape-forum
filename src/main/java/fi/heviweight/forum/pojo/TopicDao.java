package fi.heviweight.forum.pojo;

import fi.heviweight.forum.db.Database;
import java.sql.SQLException;
import java.util.*;

public class TopicDao {
    private Database db;

    public TopicDao(Database db) {
        this.db = db;
    }
    
    public List<Topic> getTopics(int boardId) throws SQLException{
        return db.queryAndCollect("SELECT Board.id AS boardId, "
                + "Board.name AS boardName, Topic.id As topicId, "
                + "Topic.name AS topicName, COUNT(Post.id) AS Viestejä, "
                + "Post.timestamp AS 'Viimeisin viesti' \n" +
                "FROM Board, Topic, Post \n" +
                "WHERE Board.id = Topic.board_id \n" +
                "AND Board.id = " + boardId + 
                " AND Post.topic_id = Topic.id \n" +
                "GROUP BY Topic.name \n" +
                "ORDER BY Post.timestamp DESC LIMIT 10;", rs -> {
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
        db.execute("INSERT INTO Topic (board_id, name) VALUES (" + boardId + ", '" + message + "');");
        Topic t = lastTopic();
        return t.getId();
    }

    private Topic lastTopic() throws SQLException {
        List<Topic> topics = db.queryAndCollect("SELECT topic.id "
                + "FROM Topic ORDER BY topic.id DESC LIMIT 1", rs -> {
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
