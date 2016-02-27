package fi.heviweight.forum.pojo;

import fi.heviweight.forum.db.Database;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

public class UserDao {
    private Database db;

    public UserDao(Database db) {
        this.db = db;
    }
    
    public User getUser(String name) throws SQLException {
        PreparedStatement stmt = db.getConnection().prepareStatement(
                "SELECT * FROM User WHERE User.name == ?;");
        stmt.setString(1, name);
        List<User> u = db.queryAndCollect(stmt, rs -> {
                    return new User(
                        rs.getInt("id"),
                        rs.getString("name"));
                });
        if (u.isEmpty()) {
            return null;
        }
        return u.get(0);
    }
}
