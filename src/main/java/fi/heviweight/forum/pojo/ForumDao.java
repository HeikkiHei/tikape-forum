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
                    + "COUNT(Post.id) AS 'Viesteja Yhteensa', "
                    + "post.timestamp AS 'Viimeisin Viesti' \n"
                    + "FROM board, Topic, post\n"
                    + "WHERE topic.board_id = board.id\n"
                    + "AND topic.id = post.topic_id\n"
                    + "GROUP BY board.name\n"
                    + "ORDER BY post.timestamp DESC;");
            return db.queryAndCollect(stmt, rs -> {
                return new Forum(
                        rs.getInt("boardId"),
                        rs.getInt("topicId"),
                        rs.getString("boardName"),
                        rs.getString("boardDesc"),
                        rs.getString("topicName"),
                        rs.getString("Viimeisin Viesti"),
                        rs.getInt("Viesteja Yhteensa"));
            });
        }
    }
}
