package com.celtican.pokemon.utils.data;

import com.badlogic.gdx.math.MathUtils;
import com.celtican.pokemon.Game;
import com.celtican.pokemon.utils.Enums;

public class PokemonPC implements Pokemon {

    private int species;
    @Override public Species getSpecies() {
        return Game.game.data.getSpecies(species);
    }
    private int experience;
    @Override public int getExperience() {
        return experience;
    }
    @Override public int getLevel() {
        return 0;
    }
    private int ability;
    @Override public Ability getAbility() {
        return getSpecies().getAbility(ability);
    }
    private int[] evs;
    @Override public int[] getEVs() {
        return evs;
    }
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
    private int[] ppUsed;
    @Override public int[] getMovesRemainingPP() {
        return new int[0]; // todo
    }
    private int[] ppUps;
    @Override public int[] getMovesPPUps() {
        return ppUps;
    }
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
    private Enums.Nature nature;
    @Override public Enums.Nature getNature() {
        return nature;
    }
    private boolean isShiny;
    @Override public boolean isShiny() {
        return isShiny;
    }

    public PokemonPC(Species species) {
        this.species = species.getIndex();
        experience = 0;
        ability = Species.getRandomWhichAbility();
        evs = new int[6];
        moves = new int[] {1, 0, 0, 0};
        ppUsed = new int[4];
        ppUps = new int[4];
        ivs = new int[6];
        nickname = null;
        nature = Enums.Nature.getRandomNature();
        isShiny = MathUtils.random(9) == 0;
    }
}
