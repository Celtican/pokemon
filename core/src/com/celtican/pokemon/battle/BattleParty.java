package com.celtican.pokemon.battle;

import com.badlogic.gdx.utils.Array;

public class BattleParty {
    public final int i;
    public Array<DisplayPokemon> displayMembers;
    public BattlePokemon[] members;
    public int numBattling;
    public int escapeAttempts = 0;

    public BattleParty(BattlePokemon[] members, int numBattling, int i) {
        this.i = i;
        this.members = members;
        this.numBattling = numBattling;
        displayMembers = new Array<>();
    }
}
