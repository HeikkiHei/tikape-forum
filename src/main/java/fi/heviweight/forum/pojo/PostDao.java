package fi.heviweight.forum.pojo;

import fi.heviweight.forum.db.Database;
import java.sql.SQLException;
import java.util.*;

public class PostDao {
    private Database db;

    public PostDao(Database db) {
        this.db = db;
    }
    
    public List<Post> getPosts(int topicID) throws SQLException {
        return db.queryAndCollect("SELECT Board.id AS boardId, "
                + "Board.name AS boardName, Topic.id As topicId, "
                + "Topic.name AS topicName, Post.post AS post, "
                + "Post.timestamp AS timestamp, User.name AS Nimi "
                + "FROM Topic, Board, Post, User \n" +
                "WHERE Topic.id = " + topicID +
                " AND Board.id = Topic.board_id \n" +
                "AND User.id = Post.user_id\n" +
                "AND Post.topic_id = Topic.id ORDER BY timestamp;", rs -> {
            return new Post(
                    rs.getInt("topicId"),
                    rs.getInt("boardId"),
                    rs.getString("Nimi"),
                    rs.getString("post"),
                    rs.getString("topicName"),
                    rs.getString("boardName"),
                    rs.getString("timestamp"));
                });
    }
    
    public void addPost(String nick, String message, int topicId) throws SQLException {
        User u = new UserDao(db).getUser(nick);
        db.execute("INSERT INTO Post (topic_id, user_id, post) "
                + "VALUES (" + topicId + ", " + u.getId() + ", '" + message + "');");
    }
}
