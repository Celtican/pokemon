package com.celtican.pokemon.utils;

import com.badlogic.gdx.utils.Array;
import com.celtican.pokemon.utils.data.Ability;
import com.celtican.pokemon.utils.data.Move;
import com.celtican.pokemon.utils.data.Pokemon;
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

    public Species getRandomSpecies() {
        Species species = null;
        while (species == null || species == getNullSpecies()) {
            species = this.species.random();
        }
        return species;
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
        return null;
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

    public void addSpecies(String name, Pokemon.GenderRatio genderRatio, Pokemon.Type type1, Pokemon.Type type2, Pokemon.EggGroup eggGroup1, Pokemon.EggGroup eggGroup2, Pokemon.ExpGrowth expGrowth,
                           int a1, int a2, int aH, int aE, int hp, int atk, int def, int spA, int spD, int spe,
                           int captureRate, int baseHappiness, int expEarned, int evHP, int evAtk, int evDef, int evSpA, int evSpD, int evSpe) {
        species.add(new Species(species.size, name, genderRatio, captureRate, expGrowth, expEarned,
                new byte[] {(byte)evHP, (byte)evAtk, (byte)evDef, (byte)evSpA, (byte)evSpD, (byte)evSpe}, baseHappiness, eggGroup1, eggGroup2,
                type1, type2, new int[] {a1, a2, aH, aE}, new int[] {hp, atk, def, spA, spD, spe}));
    }
    public void addMove(Move move) {
        moves.add(move);
    }
    public void addAbility(Ability ability) {
        abilities.add(ability);
    }
}
