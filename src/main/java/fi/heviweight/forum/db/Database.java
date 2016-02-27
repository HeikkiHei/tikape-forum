package fi.heviweight.forum.db;

import fi.heviweight.forum.pojo.Forum;
import java.sql.*;
import java.util.*;

public class Database {

    private Connection connection;

    public Database(String address) throws Exception {
        this.connection = DriverManager.getConnection(address);
    }

    public <T> List<T> queryAndCollect(String query, Collector col) throws SQLException {
//        System.out.println("Terve!");
        List<T> rows = new ArrayList<>();
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            rows.add((T) col.collect(rs));
        }
        rs.close();
        stmt.close();
        return rows;
    }

    public <T> List<T> queryAndCollect(PreparedStatement stmt, Collector col) throws SQLException {
        List<T> rows = new ArrayList<>();
        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                rows.add((T) col.collect(rs));
            }
        }
        stmt.close();
        return rows;
    }

    public void execute(String statement) throws SQLException {
        Statement stmt = connection.createStatement();
        int changes = stmt.executeUpdate(statement);
        System.out.println("Kyselyn vaikuttamia rivejä: " + changes);
        stmt.close();
    }

    public void execute(PreparedStatement stmt) throws SQLException {
        int changes = stmt.executeUpdate();
        System.out.println("Kyselyn vaikuttamia rivejä: " + changes);
        stmt.close();
    }

    public Connection getConnection() {
        return connection;
    }
}
