package fi.heviweight.forum.pojo;

import fi.heviweight.forum.db.Database;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

public class UserDao {

    private Database db;

    public UserDao(Database db) {
        this.db = db;
    }

    public User getUser(String name) throws SQLException {
        try (Connection con = db.getConnection()) {

            PreparedStatement stmt = con.prepareStatement(
                    "SELECT * FROM user WHERE kayttaja.name == ?;");
            stmt.setString(1, name);
            List<User> u = db.queryAndCollect(stmt, rs -> {
                return new User(
                        rs.getInt("id"),
                        rs.getString("name"));
            });
            if (u.isEmpty()) {
                PreparedStatement insert = con.prepareStatement(
                        "INSERT INTO kayttaja(name) Values(?);");
                insert.setString(1, name);
                db.execute(insert);
                return getUser(name);
            }
            return u.get(0);
        }
    }
}
