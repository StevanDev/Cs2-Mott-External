package com.example.luckysix.database;

import com.example.luckysix.utill.Combination;
import com.example.luckysix.utill.Ticket;

import java.sql.*;
import java.util.ArrayList;

public class Database {
    private static boolean connected = false;
    private static final String URL = "jdbc:mysql://localhost/luckySix";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    private static Connection connection;

    /**
     * Uspostavlja vezu sa bazom podataka koristeći MySQL JDBC drajver.
     */
    public static void connect() throws ClassNotFoundException, SQLException {
        if (connected) return;

        Class.forName("com.mysql.cj.jdbc.Driver");

        connection = DriverManager.getConnection(URL, USER, PASSWORD);

        connected = true;
    }

    /**
     * Prekida vezu sa bazom podataka.
     */
    public static void disconnect() {
        if (!connected) return;

        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        connected = false;
    }

    /**
     * Dobavlja poslednju odigranu rundu iz baze podataka.
     */
    public static int getLastRound() {
        if (!connected) return -1;
        final String query = """ 
                SELECT *
                   FROM result
                   ORDER BY resultID DESC LIMIT 1
                   """;

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("round") + Ticket.MAX_COMBINATION_COUNT;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return -1;
    }

    /**
     * Dodaje trenutni tiket u bazu podataka.
     * Tiket se sastoji od kombinacija brojeva, a ova metoda dodaje kombinacije u tabelu "combination" i tiket u tabelu "ticket".
     * Svaka kombinacija se zatim povezuje sa odgovarajućim tiketom.
     * Ako veza sa bazom podataka nije uspostavljena, metoda baca RuntimeException.
     */
    public static void addCurrentTicket() {
        final String query = """
                INSERT
                INTO combination(number1, number2, number3, number4, number5, number6)
                VALUES (?, ?, ?, ?, ?, ?);
                """;
        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            final int[] IDs = new int[Ticket.MAX_COMBINATION_COUNT];
            for (int i = 0; i < Ticket.MAX_COMBINATION_COUNT; i++) {
                final Combination combination = Ticket.getCombination(i);
                final ArrayList<Integer> numbers = combination.getNumbers();

                statement.setInt(1, numbers.get(0));
                statement.setInt(2, numbers.get(1));
                statement.setInt(3, numbers.get(2));
                statement.setInt(4, numbers.get(3));
                statement.setInt(5, numbers.get(4));
                statement.setInt(6, numbers.get(5));

                statement.executeUpdate();

                final ResultSet resultSet = statement.getGeneratedKeys();
                if (resultSet.next()) {
                    IDs[i] = resultSet.getInt(1);
                }
            }

            final String ticketQuery = """
                    INSERT
                    INTO ticket(round, combination1, combination2, combination3, combination4, combination5)
                    VALUES (?,?, ?, ?, ?, ?);
                       """;
            try (PreparedStatement statement1 = connection.prepareStatement(ticketQuery)) {
                statement1.setInt(1, getLastRound());
                statement1.setInt(2, IDs[0]);
                statement1.setInt(3, IDs[1]);
                statement1.setInt(4, IDs[2]);
                statement1.setInt(5, IDs[3]);
                statement1.setInt(6, IDs[4]);
                statement1.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Dodaje rezultate određene runde u bazu podataka.
     * Rezultati se čuvaju u tabeli "result" sa informacijama o rundi i nizom brojeva.
     * Ako veza sa bazom podataka nije uspostavljena, metoda baca RuntimeException.
     */
    public static void addResult(final int[] numbers, final int round) {
        final String query = """
                INSERT
                INTO result(round, numbers)
                VALUES (?, ?);
                """;
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, round);

            StringBuilder hash = new StringBuilder();
            for (int n : numbers) {
                hash.append(n).append(",");
            }
            statement.setString(2, hash.toString());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Dodaje zabeležen dobitak u bazu podataka.
     * Informacija o dobitku se čuva u tabeli "win".
     * Ako veza sa bazom podataka nije uspostavljena, metoda baca RuntimeException.
     */
    public static void addWin() {
        final String query = """
                INSERT
                INTO win
                VALUES ();
                """;
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Dodaje informaciju o izvučenom broju u bazu podataka.
     * Informacija se čuva u tabeli "draw" sa nazivom i opisom izvučenog broja.
     * Ako veza sa bazom podataka nije uspostavljena, metoda baca RuntimeException.
     */
    public static void addDraw(final int value) {
        final String query = """
                INSERT INTO draw(name, description)
                VALUES (?, ?)
                """;
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, "Server has drawn number");
            statement.setString(2, "Number drawn : " + value);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * Proverava da li je veza sa serverom uspostavljena.
     */
    public static boolean isConnected() {
        return connected;
    }
}