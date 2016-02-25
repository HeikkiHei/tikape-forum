/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
        return db.queryAndCollect("SELECT Board.id AS boardId, "
                + "Board.name AS boardName, Board.description AS boardDesc,"
                + "Topic.id As topicId, Topic.name AS topicName, "
                + "COUNT(Post.id) AS 'Viestejä Yhteensä', "
                + "Post.timestamp AS 'Viimeisin Viesti' \n" +
                "FROM Board, Topic, Post\n" +
                "WHERE Topic.board_id = Board.id\n" +
                "AND Topic.id = Post.topic_id\n" +
                "GROUP BY Board.name\n" +
                "ORDER BY Post.timestamp DESC;", rs -> {
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
