package com.celtican.pokemon.utils.data;

import com.badlogic.gdx.math.MathUtils;
import com.celtican.pokemon.Game;
import com.celtican.pokemon.battle.BattleCalculator;

public class PCPokemon implements Pokemon {

    public PCPokemon(Pokemon base) {
        species = base.getSpecies().getIndex();
        experience = base.getExperience();
        abilitySpeciesIndex = base.getAbilitySpeciesIndex();
        evs = base.getEVs();
        Move[] moves = base.getMoves();
        this.moves = new int[4];
        for (int i = 0; i < 4; i++)
            this.moves[i] = moves[i] == null ? -1 : moves[i].index;
//        ppUsed = pokemon.getMovesRemainingPP();
//        ppUps = pokemon.getMovesPPUps();
        ivs = base.getIVs();
        nickname = base.getNickname();
        nature = base.getNature();
        isShiny = base.isShiny();
    }
    public PCPokemon(Species species, int level) {
        this.species = species.getIndex();
        experience = species.getExperienceGrowth().getExpFromLevel(level);
        abilitySpeciesIndex = Species.getRandomWhichAbility();
        evs = new int[6];
        moves = new int[4];
        moves[0] = 7;
        for (int i = 1; i < 4; i++)
            moves[i] = Game.game.data.getRandomMove().index;
//        ppUsed = new int[4];
//        ppUps = new int[4];
        ivs = new int[6];
        if (BattleCalculator.DEBUG_POKEMON_PERFECT_STATS) {
            for (int i = 0; i < 6; i++) ivs[i] = 31;
            nature = Nature.SERIOUS;
        } else {
            for (int i = 0; i < 6; i++) ivs[i] = MathUtils.random(31);
            nature = Pokemon.Nature.getRandomNature();
        }
        nickname = null;
        isShiny = MathUtils.random(9) == 0;
        if (BattleCalculator.DEBUG_POKEMON_GENERATION) {
            Game.logInfo("Generated a level " + level + " " + species.getName() + ". ivs: [" +
                    ivs[0] + ", " + ivs[1] + ", " + ivs[2] + ", " + ivs[3] + ", " + ivs[4] + ", " + ivs[5] +
                    "]. nature: " + nature.toString());
        }
    }

    private int species;
    @Override public Species getSpecies() {
        return Game.game.data.getSpecies(species);
    }
    private int experience;
    @Override public int getExperience() {
        return experience;
    }
    @Override public int getLevel() {
        return getSpecies().getExperienceGrowth().getLevelFromExp(experience);
    }
    private int abilitySpeciesIndex;
    @Override public Ability getAbility() {
        return getSpecies().getAbility(abilitySpeciesIndex);
    }
    @Override public int getAbilitySpeciesIndex() {
        return abilitySpeciesIndex;
    }
    private int[] evs;
    @Override public int[] getEVs() {
        return evs;
    }
    @Override public int[] getStats() {
        int[] stats = new int[6];
        Species s = getSpecies();
        int level = getLevel();
        for (int i = 0; i < 6; i++)
            stats[i] = Pokemon.getStat(i, s.getStat(i), getIVs()[i], getEVs()[i], level, getNature());
        return stats;
    }
    @Override public int getStat(int stat) {
        return Pokemon.getStat(stat, getSpecies().getStat(stat), getIVs()[stat], getEVs()[stat], getLevel(), getNature());
    }
    @Override public int getHP() {
        return getStat(0);
    } // pokemonpc discards hp information
    @Override public void setHP(int hp) {}
    private int[] moves;
    @Override public Move[] getMoves() {
        Move[] moves = new Move[4];
        for (int i = 0; i < 4; i++)
            if (this.moves[i] == 0)
                moves[i] = null;
            else
                moves[i] = Game.game.data.getMove(this.moves[i]);
        return moves;
    }
    @Override public Move getMove(int move) {
        return Game.game.data.getMove(moves[move]);
    }
//    private int[] ppUsed;
//    @Override public int[] getMovesPPUsed() {}
//    @Override public int[] getMovesRemainingPP() {
//        return new int[0]; // todo
//    }
//    private int[] ppUps;
//    @Override public int[] getMovesPPUps() {
//        return ppUps;
//    }
    private int[] ivs;
    @Override public int[] getIVs() {
        return ivs;
    }
    private String nickname;
    @Override public String getName() {
        if (nickname == null)
            return Game.game.data.getSpecies(species).getName();
        return nickname;
    }
    @Override public String getNickname() {
        return nickname;
    }
    private Nature nature;
    @Override public Pokemon.Nature getNature() {
        return nature;
    }
    private boolean isShiny;
    @Override public boolean isShiny() {
        return isShiny;
    }
}
