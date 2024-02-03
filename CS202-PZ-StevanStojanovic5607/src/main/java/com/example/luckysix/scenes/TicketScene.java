package com.example.luckysix.scenes;

import com.example.luckysix.Application;
import com.example.luckysix.utill.Combination;
import com.example.luckysix.utill.Ticket;
import com.example.luckysix.utill.Ball;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Collections;
public class TicketScene extends Scene {

    private int selectedIndex = -1;

    private final Ball[] balls;
    private final Ball[][] combinationBalls = new Ball[Ticket.MAX_COMBINATION_COUNT][Combination.COMBINATION_VALUES_COUNT];

    /**
     * Konstruktor koji inicijalizuje scenu za izbor kombinacija na tiketu.
     * Ova scena omogućava korisniku da bira brojeve za svaku kombinaciju tiketa, pritiskom na odgovarajuće loptice.
     * Korisnik može birati brojeve klikom na loptice, a svaka kombinacija se prikazuje u obliku redova loptica tiketa.
     * Prilikom odabira kombinacije, pozadina reda se menja kako bi korisniku indikovala odabranu kombinaciju.
     * Takođe, pruža opcije za generisanje nasumičnih kombinacija tiketa i pokretanje igre.
     */
    public TicketScene() {
        super(new HBox(30), Application.WIDTH, Application.HEIGHT);

        balls = new Ball[Combination.MAX_VALUE - 1];

        final HBox root = (HBox) getRoot();
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #1E2C2C;");

        VBox numbers = new VBox(20);
        numbers.setAlignment(Pos.CENTER);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        for (int i = Combination.MIN_VALUE; i < Combination.MAX_VALUE; i++) {
            final int row = (i - 1) / 6;
            final int col = (i - 1) % 6;

            Ball ball = new Ball(2.0, i);
            balls[i - 1] = ball;
            final int value = i;
            ball.setOnMouseClicked(event -> {
                onClick(value);
            });
            ball.setOnMouseEntered(mouseEvent -> {
                if (selectedIndex != -1)
                    ball.hover();
            });
            ball.setOnMouseExited(mouseEvent -> {
                if (selectedIndex != -1)
                    ball.hover();
            });

            grid.add(ball, col, row);
        }

        numbers.getChildren().addAll(grid, clearButton(), randomButton());

        root.getChildren().add(numbers);

        VBox tickets = new VBox(10);
        tickets.setAlignment(Pos.CENTER);
        for (int i = 0; i < Ticket.MAX_COMBINATION_COUNT; i++) {

            HBox hBox = new HBox(10);

            for (int j = 0; j < Combination.COMBINATION_VALUES_COUNT; j++) {
                Ball ball = new Ball(1.75);
                combinationBalls[i][j] = ball;
                ball.setMouseTransparent(true);
                ball.stylize();
                hBox.getChildren().add(ball);
            }

            final int index = i;
            hBox.setOnMouseClicked(event -> {
                if (selectedIndex != -1) {
                    tickets.getChildren().get(selectedIndex).setStyle("-fx-background-color: #1E2C2C");
                }
                selectedIndex = index;
                tickets.getChildren().get(selectedIndex).setStyle("-fx-background-color: #4d7070");

                for (int k = 0; k < balls.length; k++) {
                    if (balls[k].isSelected()) balls[k].select();
                    if (Ticket.getCombination(selectedIndex).contains(k + 1)) {
                        balls[k].select();
                    }
                }
            });

            tickets.getChildren().add(hBox);
        }

        tickets.getChildren().addAll(randomTicketButton(), playButton());
        root.getChildren().add(tickets);
    }

    /**
     * Kreira dugme za brisanje odabrane kombinacije tiketa.
     * Dugme omogućava korisniku da obriše sve odabrane brojeve u trenutnoj kombinaciji tiketa.
     * Prilikom pritiska na dugme, proverava se da li je neka kombinacija označena.
     * Ako nije, ne preduzima se nikakva akcija. U suprotnom, svi odabrani brojevi u označenoj kombinaciji se brišu,
     * a prikaz loptica se ažurira.
     */
    private Node clearButton() {
        final Button button = new Button("Clear");
        button.setStyle("-fx-background-color: #2F3D3D; -fx-text-fill: #87dc4e; -fx-font-size: 16;");
        button.setOnAction(actionEvent -> {
            if (selectedIndex == -1) return;
            Ticket.getCombination(selectedIndex).clear();
            updateBalls();
        });
        return button;
    }

    /**
     * Kreira dugme za generisanje nasumične kombinacije brojeva u odabranoj kombinaciji tiketa.
     * Dugme omogućava korisniku da generiše nasumične brojeve za odabranu kombinaciju tiketa.
     * Prilikom pritiska na dugme, proverava se da li je neka kombinacija označena.
     * Ako nije, ne preduzima se nikakva akcija. U suprotnom, generišu se nasumični brojevi za označenu kombinaciju,
     * a prikaz loptica se ažurira.
     */
    private Node randomButton() {
        final Button button = new Button("Random");
        button.setStyle("-fx-background-color: #2F3D3D; -fx-text-fill: #87dc4e; -fx-font-size: 16;");
        button.setOnAction(actionEvent -> {
            if (selectedIndex == -1) return;
            Ticket.getCombination(selectedIndex).random();
            updateBalls();
        });
        return button;
    }

    /**
     * Kreira dugme za generisanje nasumičnih kombinacija brojeva za ceo tiket.
     * Dugme omogućava korisniku da generiše nasumične brojeve za sve kombinacije na tiketu.
     * Prilikom pritiska na dugme, nasumične vrednosti se generišu za svaku kombinaciju,
     * a prikaz loptica se ažurira za svaku od generisanih kombinacija.
     * Nakon generisanja, vraća se na prethodno označenu kombinaciju (ako postoji) i ažurira njen prikaz.
     */
    private Node randomTicketButton() {
        final Button button = new Button("Random Ticket");
        button.setStyle("-fx-background-color: #2F3D3D; -fx-text-fill: #87dc4e; -fx-font-size: 16;");
        button.setOnAction(actionEvent -> {
            Ticket.random();
            final int currentIndex = selectedIndex;
            for (int i = 0; i < Ticket.MAX_COMBINATION_COUNT; i++) {
                selectedIndex = i;
                updateBalls();
            }
            selectedIndex = currentIndex;
            updateBalls();
        });
        return button;
    }

    /**
     * Kreira dugme za pokretanje igre na osnovu odabranih kombinacija tiketa.
     * Dugme omogućava korisniku da pokrene igru na osnovu trenutnih odabranih kombinacija tiketa.
     * Prilikom pritiska na dugme, postavlja se scena igre sa odabranim kombinacijama tiketa.
     */
    private Node playButton() {
        final Button button = new Button("Play");
        button.setStyle("-fx-background-color: #2F3D3D; -fx-text-fill: #87dc4e; -fx-font-size: 16;");
        button.setOnAction(actionEvent -> {
            Application.mainStage.setScene(new GameScene());
        });
        return button;
    }

    /**
     * Ažurira prikaz loptica na osnovu trenutno odabrane kombinacije tiketa.
     * Metoda proverava da li je neka kombinacija tiketa označena.
     * Ako nije, ne preduzima se nikakva akcija. U suprotnom, ažurira prikaz loptica na osnovu trenutno odabrane kombinacije.
     * Prikazuje koje brojeve korisnik ima odabrao na trenutnoj kombinaciji tiketa, a ostale loptice ostavlja u neutralnom stanju.
     */
    private void updateBalls() {
        if (selectedIndex == -1) return;
        updateCombinationBalls(Ticket.getCombination(selectedIndex));
        for (int k = 0; k < balls.length; k++) {
            if (balls[k].isSelected())
                balls[k].select();
            if (Ticket.getCombination(selectedIndex).contains(k + 1)) {
                balls[k].select();
            }
        }
    }

    /**
     * Obrada događaja pritiska na lopticu sa određenom vrednošću.
     * Metoda se poziva kada korisnik pritisne lopticu sa određenom vrednošću.
     * Proverava se da li je neka kombinacija tiketa označena.
     * Ako nije, ne preduzima se nikakva akcija. U suprotnom, proverava se da li je vrednost već dodata u odabranu kombinaciju.
     * Ako jeste, uklanja se iz kombinacije, loptica se označava kao neodabrana i ažurira se prikaz loptica za odabranu kombinaciju.
     * Ako vrednost nije već dodata u kombinaciju, pokušava je dodati.
     * Ako dodavanje uspe, loptica se označava kao odabrana, a prikaz loptica za odabranu kombinaciju se ažurira.
     * Ako vrednost nije jedinstvena u kombinaciji, baca se RuntimeException.
     */
    private void onClick(int value) {
        if (selectedIndex == -1) return;

        final Combination combination = Ticket.getCombination(selectedIndex);

        if (combination.contains(value)) {
            combination.remove(value);
            balls[value - 1].select();
            updateCombinationBalls(combination);
            return;
        }

        try {
            if (combination.add(value)) {
                balls[value - 1].select();
                updateCombinationBalls(combination);
            }
        } catch (Combination.ValueNotUniqueException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Ažurira prikaz loptica za odabranu kombinaciju tiketa.
     * Metoda se koristi za ažuriranje prikaza loptica koje predstavljaju brojeve na odabranoj kombinaciji tiketa.
     * Brojevi se sortiraju, a potom se postavljaju na odgovarajuće loptice.
     * Ako kombinacija sadrži manje brojeva nego što ima loptica, preostale loptice ostaju sa vrednošću 0.
     */
    private void updateCombinationBalls(final Combination combination) {
        ArrayList<Integer> values = combination.getNumbers();
        Collections.sort(values);

        for (int i = 0; i < Combination.COMBINATION_VALUES_COUNT; i++) {
            combinationBalls[selectedIndex][i].setValue(
                    values.size() > i ? values.get(i) : 0
            );
        }
    }
}