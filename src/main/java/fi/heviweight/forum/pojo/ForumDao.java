package fi.heviweight.forum.pojo;

import fi.heviweight.forum.db.Database;
import java.sql.*;
import java.util.List;

public class ForumDao {

    private Database db;

    public ForumDao(Database db) {
        this.db = db;
    }

    public List<Forum> findAll() throws SQLException {
        try (Connection con = db.getConnection()) {
            PreparedStatement stmt = con.prepareStatement(
                    "SELECT board.id AS boardId, "
                    + "board.name AS boardName, board.description AS boardDesc,"
                    + "topic.id As topicId, topic.name AS topicName, "
                    + "COUNT(Post.id) AS Viesteja, "
                    + "post.timestamp AS Viimeisin "
                    + "FROM board, Topic, post "
                    + "WHERE topic.board_id = board.id "
                    + "AND topic.id = post.topic_id "
                    + "GROUP BY board.id "
                    + "ORDER BY post.timestamp DESC;");
            return db.queryAndCollect(stmt, rs -> {
                return new Forum(
                        rs.getInt("boardId"),
                        rs.getInt("topicId"),
                        rs.getString("boardName"),
                        rs.getString("boardDesc"),
                        rs.getString("topicName"),
                        rs.getString("Viimeisin"),
                        rs.getInt("Viesteja"));
            });
        }
    }
}
