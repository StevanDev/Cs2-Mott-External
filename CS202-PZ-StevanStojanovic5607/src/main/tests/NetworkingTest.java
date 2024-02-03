import com.example.luckysix.networking.Client;
import com.example.luckysix.networking.Server;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class NetworkingTest {

    @Test
    public void testConnection() {
        new Thread(Server::start).start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        try {
            Client.connect();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        assertTrue(Client.isConnected());

        Client.disconnect();

        assertFalse(Client.isConnected());
    }
}
