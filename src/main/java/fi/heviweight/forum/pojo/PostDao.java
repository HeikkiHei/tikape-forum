package fi.heviweight.forum.pojo;

import fi.heviweight.forum.db.Database;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

public class PostDao {

    private Database db;

    public PostDao(Database db) {
        this.db = db;
    }

    public List<Post> getPosts(int topicID) throws SQLException {
        try (Connection con = db.getConnection()) {
            PreparedStatement stmt = con.prepareStatement(
                    "SELECT board.id AS boardId, "
                    + "board.name AS boardName, topic.id As topicId, "
                    + "topic.name AS topicName, post.post AS post, "
                    + "post.timestamp AS timestamp, kayttaja.name AS Nimi "
                    + "FROM topic, board, post, kayttaja \n"
                    + "WHERE topic.id = ? "
                    + "AND board.id = topic.board_id \n"
                    + "AND kayttaja.id = post.user_id\n"
                    + "AND post.topic_id = topic.id ORDER BY timestamp;");
            stmt.setInt(1, topicID);
            return db.queryAndCollect(stmt, rs -> {
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
    }

    public void addPost(String nick, String message, int topicId) throws SQLException {
        User u = new UserDao(db).getUser(nick);
        PreparedStatement stmt = db.getConnection().prepareStatement(
                "INSERT INTO post (topic_id, user_id, post) "
                + "VALUES (?, ?, ?);");
        stmt.setInt(1, topicId);
        stmt.setInt(2, u.getId());
        stmt.setString(3, message);
        db.execute(stmt);
    }
}
