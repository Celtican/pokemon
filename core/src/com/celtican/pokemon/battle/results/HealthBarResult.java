package com.celtican.pokemon.battle.results;

import com.celtican.pokemon.Game;
import com.celtican.pokemon.battle.BattlePokemon;
import com.celtican.pokemon.battle.DisplayPokemon;

public class HealthBarResult extends WaitResult {
    private final int party;
    private final int partyMemberSlot;
    private final int targetHP;

    public HealthBarResult(BattlePokemon pokemon) {
        this(pokemon, pokemon.getHP(), true);
    }
    public HealthBarResult(BattlePokemon pokemon, boolean addToArray) {
        this(pokemon, pokemon.getHP(), addToArray);
    }
    public HealthBarResult(BattlePokemon pokemon, int curHP) {
        this(pokemon, curHP, true);
    }
    public HealthBarResult(BattlePokemon pokemon, int curHP, boolean addToArray) {
        super(Game.TARGET_FRAME_RATE, addToArray);
        this.party = pokemon.party;
        this.partyMemberSlot = pokemon.partyMemberSlot;
        targetHP = curHP;
    }

    @Override public boolean start() {
        DisplayPokemon display = parent.screen.parties[party].displayMembers.get(partyMemberSlot);
        display.healthBar.setTargetHP(targetHP);
        display.pokemon.hp = targetHP;
        return false;
    }
}
