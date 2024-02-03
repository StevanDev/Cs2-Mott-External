package com.example.luckysix.scenes;

import com.example.luckysix.Application;
import com.example.luckysix.utill.Combination;
import com.example.luckysix.utill.Ticket;
import com.example.luckysix.database.Database;
import com.example.luckysix.networking.Client;
import com.example.luckysix.utill.Ball;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;

public class GameScene extends Scene {

    /**
     * Proverava da li je trenutna runda završena.
     */
    public synchronized boolean isDone() {
        return round == Ticket.MAX_COMBINATION_COUNT;
    }

    private final Label roundLabel;
    private final Ball drawnBall;
    private final Ball[] drawnBalls;
    private final Ball[] resultBalls;
    private final ProgressBar[] progressBars;
    private final int totalRound;
    private int round = 0;
    private int drawIndex = 0;

    private final int[][] combinations = new int[Ticket.MAX_COMBINATION_COUNT][Combination.MAX_VALUE - 14];

    /**
     * Konstruktor klase GameScene.
     * Inicijalizuje korisnički interfejs igre, postavlja pozadinu, prikazuje trenutnu rundu, izvlači novi tiket iz baze podataka,
     * inicijalizuje elemente interfejsa kao što su loptice, paneli i pokreće igru na klijentskoj strani.
     */
    public GameScene() {
        super(new HBox(20), Application.WIDTH, Application.HEIGHT);

        final HBox root = (HBox) getRoot();
        root.setStyle("-fx-background-color: #1E2C2C;");

        totalRound = Database.getLastRound();
        Database.addCurrentTicket();

        roundLabel = new Label("Round " + totalRound);
        roundLabel.setStyle("-fx-font-size: 20;-fx-text-fill: #32549b");


        drawnBall = new Ball(2.25);
        drawnBalls = new Ball[Combination.MAX_VALUE - 14];
        resultBalls = new Ball[Combination.MAX_VALUE - 1];
        progressBars = new ProgressBar[Ball.COLORS.length];

        root.getChildren().add(createLeftPanel());
        root.getChildren().add(createMiddlePanel());
        root.getChildren().add(createRightPanel());

        Client.startGame(this);
    }

    /**
     * Kreira levu stranicu korisničkog interfejsa igre.
     * Ova strana sadrži prikaz trenutne runde, izvučenih brojeva i njihovih kvota (bonus vrednosti).
     * Stranu čini vertikalna kutija (VBox) koja uključuje naslov, glavnu lopticu za prikaz izvučenog broja,
     * šest loptica za prikaz prvih šest izvučenih brojeva, i mrežu loptica sa kvotama za ostale izvučene brojeve.
     */
    private Node createLeftPanel() {
        final VBox left = new VBox(50);
        left.setAlignment(Pos.CENTER);

        // Dodajemo naslov
        left.getChildren().add(roundLabel);

        // Dodajemo glavnu lopticu za prikaz izvucenog broja
        left.getChildren().add(drawnBall);

        // Dodajemo 6 loptica za prvih 6 izvucenih brojeva
        HBox sixBalls = new HBox(10);
        sixBalls.setAlignment(Pos.CENTER);
        for (int i = 0; i < 6; i++)
            sixBalls.getChildren().add(drawnBalls[i] = new Ball(1.15));
        left.getChildren().add(sixBalls);

        // Pravimo grid sa ostalim lopticama za ostale izvucene brojeve
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(60);
        grid.setVgap(15);

        final Color bonusFontColor = Color.web("#F5F5F5");
        final Font bonusFont = Font.font("Arial", 20);

        // Dodajemo loptice za ostale izvucene brojeve u grid
        for (int i = 6; i < Combination.MAX_VALUE - 14; i++) {
            // Uzimamo red i kolonu
            final int row = i / 6;
            final int col = i % 6;

            // Uzimamo kvotu za tu poziciju u grid-u
            final int bonusValue = (Combination.MAX_VALUE - 13) - i;

            // Dodajemo kvotu kao labelu
            Label bonus = new Label(Integer.toString(bonusValue));
            bonus.setAlignment(Pos.CENTER);
            bonus.setTextFill(bonusFontColor);
            bonus.setTextAlignment(TextAlignment.JUSTIFY);
            bonus.setFont(bonusFont);

            // Kreiramo polje sa lopticom i kvotom(bonusom)
            HBox field = new HBox(5, drawnBalls[i] = new Ball(1.15), bonus);
            field.setAlignment(Pos.CENTER);

            // Dodeljujemo polje u grid
            grid.add(field, row, col);
        }

        left.getChildren().add(grid);

        return left;
    }

    /**
     * Kreira srednju stranicu korisničkog interfejsa igre.
     * Ova strana sadrži prikaz rezultata sa svim mogućim izvučenim brojevima, naslov "Results" i progres bar-ove za svaku boju loptice.
     * Strana čini vertikalna kutija (VBox) sa stilizovanim izgledom.
     */
    private Node createMiddlePanel() {
        final VBox middle = new VBox(5);
        middle.setAlignment(Pos.CENTER);
        middle.setStyle("-fx-background-color: #2F3D3D;");

        // Dodajemo naslov
        Label title = new Label("Results");
        title.setStyle("-fx-font-size: 30;-fx-text-fill: #87dc4e");
        middle.getChildren().add(title);

        // Pravimo grid sa svim mogucim lopticama koje mogu biti izvucene
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(5));
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        // Dodajemo loptice za sve brojeve
        for (int i = 0; i < Combination.MAX_VALUE - 1; i++) {
            // Uzimamo red i kolonu
            final int row = i / 6;
            final int col = i % 6;

            // Dodajemo lopticu u grid
            gridPane.add(resultBalls[i] = new Ball(1.25, i + 1), row, col);
        }
        middle.getChildren().add(gridPane);

        // Pravimo progress bar-e za svaku boju
        for (int i = 0; i < Ball.COLORS.length; i++) {
            final Color color = Ball.COLORS[i];

            ProgressBar progressBar = new ProgressBar(0);
            progressBar.setPrefSize(300, 30);
            progressBar.setStyle("-fx-accent: " + toHexCode(color) + ";" +
                    "-fx-control-inner-background: transparent;" +
                    "-fx-border-width: 0");
            middle.getChildren().add(progressBars[i] = progressBar);
        }

        return middle;
    }

    /**
     * Kreira desnu stranicu korisničkog interfejsa igre.
     * Ova strana sadrži prikaz trenutnog tiketa sa kombinacijama brojeva. Sastoji se od naslova "Ticket" i redova loptica za svaku kombinaciju.
     * Svaka kombinacija ima naslov (redni broj kombinacije) i red loptica sa brojevima koji čine tu kombinaciju.
     * Strana je stilizovana sa određenim bojama i razmakom između elemenata.
     */
    private Node createRightPanel() {
        final VBox right = new VBox(5);
        right.setAlignment(javafx.geometry.Pos.CENTER);
        right.setStyle("-fx-background-color: #2F3D3D;");

        // Dodajemo naslov
        final Label title = new Label("Ticket");
        title.setStyle("-fx-font-size: 30;-fx-text-fill: #87dc4e");
        right.getChildren().add(title);

        // Pravimo grid sa kombinacijama
        for (int i = 0; i < Ticket.MAX_COMBINATION_COUNT; i++) {
            final Combination combination = Ticket.getCombination(i);

            // Dodajemo naslov kombinacije (redni broj)
            final Label combinationTitle = new Label("Combination " + (totalRound + i) + ":");
            combinationTitle.setStyle("-fx-font-size: 16;-fx-text-fill: #F5F5F5");
            right.getChildren().add(combinationTitle);

            // Pravimo red loptica kombinacije
            HBox row = new HBox(5);
            final ArrayList<Integer> numbers = combination.getNumbers();
            for (Integer number : numbers) {
                Ball ball = new Ball(1.25, number);
                ball.setOnMouseClicked(event -> {
                    ball.select();
                });
                ball.setOnMouseEntered(event -> {
                    ball.hover();
                });
                ball.setOnMouseExited(event -> {
                    ball.hover();
                });
                row.getChildren().add(ball);
            }

            right.getChildren().add(row);
        }

        return right;
    }

    /**
     * Pretvara boju iz formata objekta klase Color u heksadecimalni kod boje.
     * Heksadecimalni kod boje se koristi u stilizaciji elemenata korisničkog interfejsa.
     */
    private static String toHexCode(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }

    /**
     * Obrada događaja kada je izvučen novi broj tokom igre.
     * Metoda se izvršava na JavaFX glavnom thread-u koristeći Platform.runLater() kako bi se obezbedila sigurna manipulacija grafičkih elemenata.
     * Ako je dostignut maksimalni broj izvučenih brojeva za trenutnu rundu, poziva se metoda za pokretanje nove runde.
     * Dodaje izvučeni broj u niz kombinacija, ažurira prikaz izvučenih i rezultujućih brojeva, i ažurira progres bar za određenu boju.
     */
    public synchronized void onNumberDrawn(final int number) {
        Platform.runLater(() -> {
            if (drawIndex == Combination.MAX_VALUE - 14) {
                newRound();
            }

            combinations[round][drawIndex] = number;
            Database.addDraw(number);

            drawnBall.setValue(number);
            drawnBall.stylize();

            drawnBalls[drawIndex].setValue(number);
            drawnBalls[drawIndex].stylize();

            resultBalls[number - 1].stylize();
            progressBars[(number - 1) % Ball.COLORS.length].setProgress(
                    progressBars[(number - 1) % Ball.COLORS.length].getProgress() + (1.0 / 8)
            );

            drawIndex++;
        });
    }

    /**
     * Proverava i prikazuje informaciju o osvojenim kombinacijama na kraju runde.
     * Metoda prolazi kroz sve kombinacije tiketa i proverava da li su se poklopile sa izvučenim brojevima.
     * Broji uspešne kombinacije i prikazuje dijalog sa informacijom o ukupnom broju osvojenih kombinacija.
     */
    private void checkWin() {
        int winCount = 0;
        for (int i = 0; i < Ticket.MAX_COMBINATION_COUNT; i++) {
            if (Ticket.getCombination(i).check(combinations[i]))
                winCount++;
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Round Ended");
        alert.setContentText("Total wins : " + winCount);
        alert.show();
        if (winCount == Ticket.MAX_COMBINATION_COUNT) {
            Database.addWin();
        }
    }

    /**
     * Inicira novu rundu igre.
     * Metoda proverava osvojene kombinacije, a zatim resetuje prikaz izvučenih brojeva, rezultata i progres bar-ova
     * kako bi se pripremila za sledeću rundu. Ažurira labelu sa informacijama o trenutnoj rundi.
     */
    public synchronized void newRound() {
        checkWin();

        drawnBall.setValue(0);
        drawnBall.gray();

        Database.addResult(combinations[round], totalRound + round);

        for (Ball ball : drawnBalls) {
            ball.setValue(0);
            ball.gray();
        }

        for (Ball resultBall : resultBalls) {
            resultBall.gray();
        }

        for (ProgressBar progressBar : progressBars) {
            progressBar.setProgress(0);
        }

        round++;
        drawIndex = 0;

        roundLabel.setText("Round " + (totalRound + round));
    }
}