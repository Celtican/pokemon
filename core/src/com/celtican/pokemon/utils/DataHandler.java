package com.celtican.pokemon.utils;

import com.badlogic.gdx.utils.Array;
import com.celtican.pokemon.utils.data.*;

public class DataHandler {
    public final PlayerData player;
    private final Array<Species> species;
    private final Array<Move> moves;
    private final Array<Ability> abilities;

    public DataHandler() {
        player = new PlayerData();
        species = new Array<>();
        moves = new Array<>(1000);
        abilities = new Array<>(300);
    }

    public Species getRandomSpecies() {
        Species species;

        do species = this.species.random();
        while (species == null || species == getNullSpecies());

        return species;
    }
    public Species getSpecies(int index) {
        if (index > 0 && index < species.size)
            return species.get(index);
        return null;
    }
    public Species getNullSpecies() {
        return species.get(0);
    }
    public Move getMove(int index) {
        if (index > 0 && index < moves.size)
            return moves.get(index);
        return null;
    }
    public Move getRandomMove() {
        Move move;

        do move = moves.random();
        while (move == null || move.type == Pokemon.Type.DOES_NOT_EXIST);

        return move;
    }
    public Move getStruggle() {
        return moves.get(0);
    }
    public Ability getAbility(int index) {
        if (index > 0 && index < abilities.size)
            return abilities.get(index);
        return getNullAbility();
    }
    public Ability getRandomAbility() {
        Ability ability;

        do ability = abilities.random();
        while (ability == null || ability == getNullAbility());

        return ability;
    }
    public Ability getNullAbility() {
        return abilities.get(0);
    }

    public void addSpecies(String name, Pokemon.GenderRatio genderRatio, Pokemon.Type type1, Pokemon.Type type2, Pokemon.EggGroup eggGroup1, Pokemon.EggGroup eggGroup2, Pokemon.ExpGrowth expGrowth,
                           int a1, int a2, int aH, int aE, int hp, int atk, int def, int spA, int spD, int spe,
                           int captureRate, int baseHappiness, int expEarned, int evHP, int evAtk, int evDef, int evSpA, int evSpD, int evSpe,
                           int evolveIntoSpecies, int evolveAtLevel, int[] moves, int[] moveLevels) {
        species.add(new Species(species.size, name, genderRatio, captureRate, expGrowth, expEarned,
                new byte[] {(byte)evHP, (byte)evAtk, (byte)evDef, (byte)evSpA, (byte)evSpD, (byte)evSpe}, baseHappiness, eggGroup1, eggGroup2,
                type1, type2, new int[] {a1, a2, aH, aE}, new int[] {hp, atk, def, spA, spD, spe}, evolveAtLevel, evolveIntoSpecies, moves, moveLevels));
    }
    public void addMove(String name, Pokemon.Type type, Pokemon.MoveCategory category, Pokemon.ContestType contest, Pokemon.MoveTargets targets, int basePP, int basePower, int accuracy, String flags, Move.Effect effect) {
        moves.add(new Move(moves.size, name, type, category, contest, targets, basePP, basePower, accuracy, flags, effect));
    }
    public void addMove(String name) {
        moves.add(new Move(moves.size, name, Pokemon.Type.DOES_NOT_EXIST, Pokemon.MoveCategory.PHYSICAL, Pokemon.ContestType.CLEVER, Pokemon.MoveTargets.ADJACENT, 1, 50, 80, "protect, mirror", null));
    }
    public void addAbility(Ability ability) {
        abilities.add(ability);
    }
}
