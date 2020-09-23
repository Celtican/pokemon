package com.celtican.pokemon.battle.results;

import com.celtican.pokemon.Game;
import com.celtican.pokemon.battle.BattlePokemon;

public class HealthbarResult extends WaitResult {
    final int party;
    final int partyMemberSlot;
    final int targetHP;

    public HealthbarResult(BattlePokemon pokemon) {
        this(pokemon, pokemon.getCurHP());
    }
    public HealthbarResult(BattlePokemon pokemon, int curHP) {
        super(Game.TARGET_FRAME_RATE);
        this.party = pokemon.party;
        this.partyMemberSlot = pokemon.partyMemberSlot;
        targetHP = curHP;
    }

    @Override public boolean start() {
        parent.screen.parties[party].displayMembers.get(partyMemberSlot).healthBar.setTargetHP(targetHP);
        return false;
    }
}
