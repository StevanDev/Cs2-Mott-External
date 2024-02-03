package com.example.luckysix.networking;

import com.example.luckysix.scenes.GameScene;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client {

    private static Socket socket;

    private static ObjectOutputStream oos;
    private static ObjectInputStream ois;

    private static boolean connected = false;

    /**
     * Povezuje se sa serverom na određenoj adresi i portu.
     */
    public static void connect() throws IOException {
        if (connected) return;
        socket = new Socket(Server.HOST, Server.PORT);

        oos = new ObjectOutputStream(socket.getOutputStream());
        ois = new ObjectInputStream(socket.getInputStream());

        connected = true;
    }

    /**
     * Pokreće igru na osnovu prethodno uspostavljene veze sa serverom.
     */
    public static void startGame(final GameScene scene) {
        if (!connected) return;
        try {
            oos.writeInt(1);
            oos.flush();

            new Thread(() -> {
                do {
                    try {
                        final int number = ois.readInt();
                        scene.onNumberDrawn(number);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } while (!scene.isDone());
            }).start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Prekida vezu sa serverom.
     */
    public static void disconnect() {
        if (!connected) return;
        try {
            oos.writeInt(0);
            oos.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        connected = false;
    }

    /**
     * Proverava da li je veza sa serverom uspostavljena.
     */
    public static boolean isConnected() {
        return connected;
    }
}