package com.example.luckysix.utill;

public abstract class Ticket {
    public static final int MAX_COMBINATION_COUNT = 5;
    private static final Combination[] combinations = createCombinations();

    /**
     * Kreira niz kombinacija za tiket.
     */
    private static Combination[] createCombinations() {
        Combination[] combinations = new Combination[MAX_COMBINATION_COUNT];
        for (int i = 0; i < MAX_COMBINATION_COUNT; i++) {
            combinations[i] = new Combination();
        }
        return combinations;
    }

    /**
     * Vraća kombinaciju iz tiketa na određenom indeksu.
     */
    public static Combination getCombination(int index) {
        return combinations[index];
    }

    /**
     * Prazni sve kombinacije u tiketu.
     */
    public static void clear() {
        for (Combination combination : combinations) {
            combination.clear();
        }
    }

    /**
     * Generiše nasumične vrednosti i popunjava sve kombinacije u tiketu.
     */
    public static void random() {
        for (Combination combination : combinations) {
            combination.random();
        }
    }

    /**
     * Proverava da li su sve kombinacije u tiketu završene.
     */
    public static boolean isDone() {
        for (Combination combination : combinations) {
            if (!combination.isDone()) {
                return false;
            }
        }
        return true;
    }
}