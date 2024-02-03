package com.example.luckysix.utill;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Combination {

    public static class MissingValuesException extends Exception {
    }

    public static class ValueNotUniqueException extends Exception {
    }

    public static final int MIN_VALUE = 1;
    public static final int MAX_VALUE = 49;

    public static final int COMBINATION_VALUES_COUNT = 6;

    private final Set<Integer> numbers = new HashSet<>();

    /**
     * Prazan konstruktor za kombinaciju.
     */
    public Combination() {
    }

    /**
     * Konstruktor koji kreira kombinaciju na osnovu niza vrednosti.
     * Prima niz celobrojnih vrednosti i proverava da li su sve vrednosti unutar dozvoljenog opsega.
     * Ako se utvrdi da postoji nedozvoljena vrednost, baca se izuzetak.
     * Ako niz ne sadrži sve potrebne vrednosti, baca se izuzetak.
     * Nakon provere, vrednosti se dodaju u listu brojeva, sortiraju i kreirana je kombinacija.
     */
    public Combination(final int[] values) throws MissingValuesException, ValueNotUniqueException {
        for (final int value : values) {
            if (value < MIN_VALUE || value > MAX_VALUE) {
                throw new ValueNotUniqueException();
            }
            numbers.add(value);
        }
        if (!isDone()) {
            throw new MissingValuesException();
        }
        sort();
    }

    /**
     * Sortira vrednosti u kombinaciji u rastućem redosledu.
     * Koristi se algoritam sortiranja za sortiranje vrednosti u kombinaciji.
     */
    public void sort() {
        ArrayList<Integer> list = new ArrayList<>(numbers);
        Collections.sort(list);
        numbers.clear();
        numbers.addAll(list);
    }

    /**
     * Vraća kopiju liste brojeva u kombinaciji.
     */
    public ArrayList<Integer> getNumbers() {
        return new ArrayList<>(numbers);
    }

    /**
     * Proverava da li kombinacija sadrži određenu vrednost.
     */
    public boolean contains(final int value) {
        return numbers.contains(value);
    }

    /**
     * Dodaje vrednost u kombinaciju.
     */
    public boolean add(final int value) throws ValueNotUniqueException {
        if (value < MIN_VALUE || value > MAX_VALUE) {
            throw new ValueNotUniqueException();
        }
        if (isDone()) return false;
        return numbers.add(value);
    }

    /**
     * Uklanja vrednost iz kombinacije, ako postoji.
     */
    public boolean remove(final int value) {
        return numbers.remove(value);
    }

    /**
     * Proverava da li je kombinacija potpuno popunjena, odnosno da li sadrži sve potrebne vrednosti.
     */
    public boolean isDone() {
        return numbers.size() == COMBINATION_VALUES_COUNT;
    }

    /**
     * Prazni kombinaciju, uklanjajući sve vrednosti iz nje.
     */
    public void clear() {
        numbers.clear();
    }

    /**
     * Generiše nasumične vrednosti i popunjava kombinaciju.
     */
    public void random() {
        clear();
        while (!isDone()) {
            numbers.add((int) (Math.random() * (MAX_VALUE - 1) + MIN_VALUE));
        }
    }

    /**
     * Proverava da li kombinacija sadrži sve zadate vrednosti.
     */
    public boolean check(final int[] values) {
        for (final int value : values) {
            if (!numbers.contains(value)) {
                return false;
            }
        }
        return true;
    }
}