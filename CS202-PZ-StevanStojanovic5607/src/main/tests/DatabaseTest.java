import com.example.luckysix.database.Database;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DatabaseTest {
    @Test
    public void testConnection() {
        try {
            Database.connect();
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }

        assertTrue(Database.isConnected());
    }

    @Test
    public void testDisconnect() {
        try {
            Database.connect();
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
        assertTrue(Database.isConnected());

        Database.disconnect();
        assertFalse(Database.isConnected());
    }
}
