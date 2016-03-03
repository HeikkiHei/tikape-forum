package fi.heviweight.forum.db;

import fi.heviweight.forum.pojo.Forum;
import java.net.URI;
import java.sql.*;
import java.util.*;

public class Database {

    private String databaseAddress;

    public Database(String address) throws Exception {
        this.databaseAddress = address;
        List<String> lauseet = null;
        if (this.databaseAddress.contains("postgres")) {
            lauseet = postgreLauseet();
        } 

        // "try with resources" sulkee resurssin automaattisesti lopuksi
        try (Connection conn = getConnection()) {
            Statement st = conn.createStatement();

            // suoritetaan komennot
            for (String lause : lauseet) {
                System.out.println("Running command >> " + lause);
                st.executeUpdate(lause);
            }

        } catch (Throwable t) {
            // jos tietokantataulu on jo olemassa, ei komentoja suoriteta
            System.out.println("Error >> " + t.getMessage());
        }
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

    public void execute(PreparedStatement stmt) throws SQLException {
        int changes = stmt.executeUpdate();
        System.out.println("Kyselyn vaikuttamia rivejä: " + changes);
        stmt.close();
    }

    public Connection getConnection() throws SQLException {
        if (this.databaseAddress.contains("postgres")) {
            try {
                URI dbUri = new URI(databaseAddress);

                String username = dbUri.getUserInfo().split(":")[0];
                String password = dbUri.getUserInfo().split(":")[1];
                String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();

                return DriverManager.getConnection(dbUrl, username, password);
            } catch (Throwable t) {
                System.out.println("Error: " + t.getMessage());
                t.printStackTrace();
            }
        }

        return DriverManager.getConnection(databaseAddress);
    }

    private List<String> postgreLauseet() {
        ArrayList<String> lista = new ArrayList<>();

        // tietokantataulujen luomiseen tarvittavat komennot suoritusjärjestyksessä
//        lista.add("DROP TABLE Tuote;");
        // heroku käyttää SERIAL-avainsanaa uuden tunnuksen automaattiseen luomiseen
//        lista.add("CREATE TABLE Tuote (id SERIAL PRIMARY KEY, nimi varchar(255));");
//        lista.add("INSERT INTO Tuote (nimi) VALUES ('postgresql-tuote');");

        return lista;
    }

}
