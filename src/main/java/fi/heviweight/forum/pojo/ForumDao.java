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
                    "SELECT b.id AS boardId, b.name AS boardName, "
                    + "b.description AS boardDesc, t.Viesteja, "
                    + "t.Viimeisin FROM (SELECT board.id AS boardId, " 
                    + "COUNT(Post.id) AS Viesteja, " 
                    + "MAX(post.TIMESTAMP) AS Viimeisin " 
                    + "FROM board, Topic, post "
                    + "WHERE topic.board_id = board.id " 
                    + "AND topic.id = post.topic_id " 
                    + "GROUP BY board.id) t RIGHT JOIN board b ON t.boardId = b.id;");
            return db.queryAndCollect(stmt, rs -> {
                return new Forum(
                        rs.getInt("boardId"),
                        rs.getString("boardName"),
                        rs.getString("boardDesc"),
                        rs.getString("Viimeisin"),
                        rs.getInt("Viesteja"));
            });
        }
    }
}
