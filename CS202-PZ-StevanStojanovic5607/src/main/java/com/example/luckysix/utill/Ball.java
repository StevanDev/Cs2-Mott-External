package com.example.luckysix.utill;

import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Ball extends StackPane {
    public static final Color[] COLORS = {
            Color.RED, Color.GREEN, Color.BLUE,
            Color.PURPLE, Color.YELLOW, Color.ORANGE
    };

    private final Circle circle = createCircle();

    private final Label label = createLabel();

    /**
     * Konstruktor klase za predstavljanje loptice sa brojem.
     * Inicijalizuje novu lopticu postavljajući joj vrednost na 0 i postavljajući je u neutralno stanje (siva boja).
     * Loptica se sastoji od kruga i labela koja prikazuje vrednost.
     */
    public Ball() {
        super();
        super.getChildren().addAll(circle, label);
        setValue(0);
        gray();
    }

    /**
     * Konstruktor klase za predstavljanje loptice sa brojem i skaliranjem.
     * Inicijalizuje novu lopticu postavljajući joj vrednost na 0 i postavljajući je u neutralno stanje (siva boja).
     * Loptica se sastoji od kruga i labela koja prikazuje vrednost.
     * Parametar scale se koristi za skaliranje veličine kruga i fonta labela.
     */
    public Ball(double scale) {
        super();
        super.getChildren().addAll(circle, label);
        circle.setRadius(20 * scale);
        label.setStyle("-fx-font-size: " + 20 * scale + "px");
        setValue(0);
        gray();
    }

    /**
     * Konstruktor klase za predstavljanje loptice sa određenom vrednošću.
     * Inicijalizuje novu lopticu postavljajući joj vrednost na specificiranu vrednost i postavljajući je u neutralno stanje (siva boja).
     * Loptica se sastoji od kruga i labela koja prikazuje vrednost.
     */
    public Ball(int value) {
        super();
        super.getChildren().addAll(circle, label);
        setValue(value);
        gray();
    }

    /**
     * Konstruktor klase za predstavljanje loptice sa određenom vrednošću i skaliranjem.
     * Inicijalizuje novu lopticu postavljajući joj vrednost na specificiranu vrednost i postavljajući je u neutralno stanje (siva boja).
     * Loptica se sastoji od kruga i labela koja prikazuje vrednost.
     * Parametar scale se koristi za skaliranje veličine kruga i fonta labela.
     */
    public Ball(double scale, int value) {
        super();
        super.getChildren().addAll(circle, label);
        circle.setRadius(20 * scale);
        label.setStyle("-fx-font-size: " + 20 * scale + "px");
        setValue(value);
        gray();
    }

    /**
     * Vraća boju na osnovu zadatog broja.
     * Ako je broj manji ili jednak nuli, vraća sivu boju. Inače, koristi zadate boje iz niza za vraćanje boje na osnovu broja.
     */
    protected static Color getColor(final int number) {
        if (number <= 0) return Color.web("#5B6565");
        return COLORS[(number - 1) % COLORS.length];
    }

    private boolean selected = false;

    /**
     * Menja trenutni status selektovanosti loptice.
     * Ako loptica nije bila selektovana, postavlja je u selektovano stanje i primenjuje odgovarajući stil.
     * Ako je loptica već bila selektovana, menja je u neselektovano stanje i primenjuje odgovarajući stil. Ako je loptica bila u stanju hovera, primenjuje stil hovera.
     */
    public void select() {
        selected = !selected;
        if (selected) stylize();
        else {
            if (hovered) stylize();
            else gray();
        }
    }

    /**
     * Vraća informaciju da li je loptica trenutno selektovana.
     */
    public final boolean isSelected() {
        return selected;
    }

    /**
     * Primenjuje stilizaciju na lopticu.
     * Postavlja boje punjenja, ivice kruga i boje teksta labela kako bi se postigao određeni vizuelni efekat.
     */
    public void stylize() {
        circle.setFill(Color.web("#172021"));
        circle.setStroke(getColor(getValue()));
        label.setTextFill(Color.web("#F5F5F5"));
    }

    /**
     * Postavlja lopticu u sivo stanje.
     * Postavlja boje punjenja, ivice kruga i boje teksta labela kako bi loptica dobila sivu boju.
     */
    public void gray() {
        circle.setFill(Color.web("#10161B"));
        circle.setStroke(Color.web("#5B6565"));
        label.setTextFill(Color.web("#5B6565"));
    }

    private boolean hovered = false;

    /**
     * Menja trenutni status hovera loptice.
     * Ako loptica nije bila u hover stanju, postavlja je u hover stanje i primenjuje odgovarajući stil.
     * Ako je loptica već bila u hover stanju, menja je u normalno stanje i primenjuje odgovarajući stil. Ako je loptica bila selektovana, primenjuje stil selektovanosti.
     */
    public void hover() {
        hovered = !hovered;
        if (hovered) {
            stylize();
        } else {
            if (selected) stylize();
            else gray();
        }
    }

    /**
     * Postavlja vrednost loptice.
     * Ako je vrednost manja ili jednaka nuli, postavlja tekst na prazan string.
     * Inače, postavlja tekst na string reprezentaciju zadate vrednosti.
     */
    public void setValue(int value) {
        if (value <= 0) label.setText("");
        else
            label.setText(Integer.toString(value));
    }

    /**
     * Vraća vrednost loptice.
     * Ako je tekst u labeli prazan, vraća vrednost 0.
     * Inače, parsira tekst iz labela i vraća ga kao celobrojnu vrednost.
     */
    public int getValue() {
        if (label.getText().isEmpty()) return 0;
        return Integer.parseInt(label.getText());
    }

    /**
     * Kreira krug sa zadatim osobinama (postavljenim radijusom, bojom punjenja, debljinom ivice i bojom ivice).
     */
    private static Circle createCircle() {
        Circle circle = new Circle();
        circle.setRadius(20);
        circle.setFill(Color.DARKGRAY);
        circle.setStrokeWidth(1.5);
        circle.setStroke(Color.SLATEGRAY);
        return circle;
    }

    /**
     * Kreira labelu sa postavljenim tekstom i stilom fonta.
     */
    private static Label createLabel() {
        Label label = new Label();
        label.setText("0");
        label.setStyle("-fx-font-size: 20");
        return label;
    }
}