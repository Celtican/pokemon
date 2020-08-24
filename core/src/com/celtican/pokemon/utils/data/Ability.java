package com.celtican.pokemon.utils.data;

public class Ability {
    private final int index;
    private final String name, description;

    public Ability(int index, String name, String description) {
        this.index = index;
        this.name = name;
        this.description = description;
    }

    public int getIndex() {
        return index;
    }
    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
}
