package org.alvarowau.model;

public class Configuration {
    private String name;
    private int cells;
    private int hypotenochas;

    public Configuration(String name, int cells, int hypotenochas) {
        this.name = name;
        this.cells = cells;
        this.hypotenochas = hypotenochas;
    }

    public String getName() {
        return name;
    }

    public int getCells() {
        return cells;
    }

    public int getHypotenochas() {
        return hypotenochas;
    }
}
