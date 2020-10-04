package com.celtican.pokemon.utils.data;

public class PartyPokemon extends PCPokemon {
    public PartyPokemon(Pokemon base) {
        super(base);
        hp = base.getHP();
    }
    public PartyPokemon(int level) {
        super(level);
        hp = getStat(0);
    }
    public PartyPokemon(Species species, int level) {
        super(species, level);
        hp = getStat(0); // set cur hp to max hp, because it's a new pokemon
    }

    private int hp;
    @Override public int getHP() {
        return hp;
    }
    @Override public void setHP(int hp) {
        this.hp = hp;
    }
}
