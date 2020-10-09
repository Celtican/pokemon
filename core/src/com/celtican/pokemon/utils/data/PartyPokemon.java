package com.celtican.pokemon.utils.data;

public class PartyPokemon extends PCPokemon {
    public PartyPokemon(Pokemon base) {
        super(base);
    }
    public PartyPokemon(int level) {
        super(level);
    }
    public PartyPokemon(Species species, int level) {
        super(species, level);
    }

    private int missingHP = 0;
    @Override public int getHP() {
        return getStat(0) - missingHP;
    }
    @Override public void setHP(int hp) {
        missingHP = getStat(0) - hp;
    }
}
