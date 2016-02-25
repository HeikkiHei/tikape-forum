package fi.heviweight.forum.pojo;

import fi.heviweight.forum.db.Database;
import java.sql.SQLException;
import java.util.*;

public class UserDao {
    private Database db;

    public UserDao(Database db) {
        this.db = db;
    }
    
    public User getUser(String name) throws SQLException {
        List<User> u = db.queryAndCollect("SELECT * FROM User "
                + "WHERE User.name == '" + name + "';", rs -> {
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
