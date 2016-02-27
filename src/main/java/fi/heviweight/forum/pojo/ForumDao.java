
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
        PreparedStatement stmt = db.getConnection().prepareStatement(
                "SELECT Board.id AS boardId, " + 
                "Board.name AS boardName, Board.description AS boardDesc," + 
                "Topic.id As topicId, Topic.name AS topicName, " + 
                "COUNT(Post.id) AS 'Viestejä Yhteensä', " + 
                "Post.timestamp AS 'Viimeisin Viesti' \n" +
                    "FROM Board, Topic, Post\n" +
                    "WHERE Topic.board_id = Board.id\n" +
                    "AND Topic.id = Post.topic_id\n" +
                    "GROUP BY Board.name\n" +
                    "ORDER BY Post.timestamp DESC;");
        return db.queryAndCollect(stmt, rs -> {
            return new Forum(
                    rs.getInt("boardId"),
                    rs.getInt("topicId"),
                    rs.getString("boardName"),
                    rs.getString("boardDesc"),
                    rs.getString("topicName"),
                    rs.getString("Viimeisin Viesti"),
                    rs.getInt("Viestejä Yhteensä"));
        });
    }
}
