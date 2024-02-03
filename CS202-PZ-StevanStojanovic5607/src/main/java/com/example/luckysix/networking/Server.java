package com.example.luckysix.networking;

import com.example.luckysix.utill.Combination;
import com.example.luckysix.utill.Ticket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class Server {
    public static final int DRAW_TIME = 1000;
    public static final int TIMEOUT_TIME = 4000;

    public static final int PORT = 8080;
    public static final String HOST = "localhost";

    public static void main(String[] args) {
        start();
    }

    /**
     * Pokreće server i očekuje konekciju klijenta na određenom portu.
     */
    public static void start() {
        // Podizemo server
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started on port " + serverSocket.getLocalPort());
            System.out.println("Waiting for connection...");
            // Cekamo na konekciju klijenta
            final Socket client = serverSocket.accept();
            System.out.println("Client connected!");

            // Pravimo strimove
            ObjectInputStream ois = new ObjectInputStream(client.getInputStream());
            ObjectOutputStream oos = new ObjectOutputStream(client.getOutputStream());

            // Citamo poruke
            while (true) {
                final int type = ois.readInt();

                // Ako je poruka 0, klijent se diskonektuje
                if (type == 0) {
                    break;
                }
                // Ako je poruka 1, klijent je usao u igru
                else if (type == 1) {
                    playGame(oos);
                }
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Pokreće igru izvlačenja brojeva i komunicira sa klijentom putem ObjectOutputStream-a.
     */
    private static void playGame(final ObjectOutputStream oos) throws IOException, InterruptedException {
        int round = 0;
        int drawIndex = 0;

        Set<Integer> numbers = new HashSet<>();

        do {
            // Izvlacimo random broj
            final int randomNumber = generateRandom(Combination.MIN_VALUE, Combination.MAX_VALUE, numbers);

            // Saljemo broj klijentu
            oos.writeInt(randomNumber);
            oos.flush();

            // Podizemo poziciju izvlacenja
            drawIndex++;

            // Cekamo za sledece izvlacenje
            Thread.sleep(DRAW_TIME);

            // Proveravamo kraj runde
            if (drawIndex == Combination.MAX_VALUE - 14) {
                // Cekamo za sledecu rundu
                Thread.sleep(TIMEOUT_TIME);

                round++;
                drawIndex = 0;

                numbers.clear();
            }
        } while (!(round == Ticket.MAX_COMBINATION_COUNT));
    }

    /**
     * Generiše slučajan broj između zadatih vrednosti (min i max), izbegavajući ponavljanje brojeva iz skupa.
     */
    private static int generateRandom(final int min, final int max, final Set<Integer> set) {
        int number;
        do {
            number = (int) (Math.random() * (max - 1) + min);
        } while (!set.add(number));
        return number;
    }
}