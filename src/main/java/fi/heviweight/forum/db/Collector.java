
package fi.heviweight.forum.db;

import java.sql.*;

public interface Collector<T> {
    
    T collect(ResultSet rs) throws SQLException;
    
}
