package com.celtican.pokemon.utils;

import com.badlogic.gdx.utils.Array;
import com.celtican.pokemon.utils.data.Ability;
import com.celtican.pokemon.utils.data.Move;
import com.celtican.pokemon.utils.data.Species;

public class DataHandler {
    private final Array<Species> species;
    private final Array<Move> moves;
    private final Array<Ability> abilities;

    public DataHandler() {
        species = new Array<>();
        moves = new Array<>();
        abilities = new Array<>();
    }

    public Species getSpecies(int index) {
        if (index > 0 && index < species.size)
            return species.get(index);
        return getNullSpecies();
    }
    public Species getNullSpecies() {
        return species.get(0);
    }
    public Move getMove(int index) {
        if (index > 0 && index < moves.size)
            return moves.get(index);
        return getNullMove();
    }
    public Move getNullMove() {
        return moves.get(0);
    }
    public Ability getAbility(int index) {
        if (index > 0 && index < abilities.size)
            return abilities.get(index);
        return getNullAbility();
    }
    public Ability getNullAbility() {
        return abilities.get(0);
    }

    public void addSpecies(Species species) {
        this.species.add(species);
    }
    public void addMove(Move move) {
        moves.add(move);
    }
    public void addAbility(Ability ability) {
        abilities.add(ability);
    }
}
