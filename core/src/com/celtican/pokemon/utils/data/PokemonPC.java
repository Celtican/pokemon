package com.celtican.pokemon.utils.data;

import com.badlogic.gdx.math.MathUtils;
import com.celtican.pokemon.Game;

public class PokemonPC implements Pokemon {

    public PokemonPC(Pokemon base) {
        species = base.getSpecies().getIndex();
        experience = base.getExperience();
        ability = base.getAbility().getIndex();
        evs = base.getEVs();
        Move[] moves = base.getMoves();
        this.moves = new int[4];
        for (int i = 0; i < 4; i++)
            this.moves[i] = moves[i].index;
//        ppUsed = pokemon.getMovesRemainingPP();
//        ppUps = pokemon.getMovesPPUps();
        ivs = base.getIVs();
        nickname = base.getNickname();
        nature = base.getNature();
        isShiny = base.isShiny();
    }
    public PokemonPC(Species species, int level) {
        this.species = species.getIndex();
        experience = species.getExperienceGrowth().getExpFromLevel(level);
        ability = Species.getRandomWhichAbility();
        evs = new int[6];
        moves = new int[] {1, 0, 0, 0};
        ppUsed = new int[4];
//        ppUps = new int[4];
        ivs = new int[6];
        nickname = null;
        nature = Pokemon.Nature.getRandomNature();
        isShiny = MathUtils.random(9) == 0;
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
    private int ability;
    @Override public Ability getAbility() {
        return getSpecies().getAbility(ability);
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
    @Override public int getCurHP() {
        return getStat(0);
    } // pokemonpc discards hp information
    @Override public void setCurHP(int hp) {}
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
    private int[] ppUsed;
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
