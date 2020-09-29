package com.celtican.pokemon.battle.results;

import com.celtican.pokemon.Game;
import com.celtican.pokemon.battle.BattlePokemon;
import com.celtican.pokemon.battle.PokemonDisplay;

public class HealthbarResult extends WaitResult {
    private final int party;
    private final int partyMemberSlot;
    private final int targetHP;

    public HealthbarResult(BattlePokemon pokemon) {
        this(pokemon, pokemon.getHP());
    }
    public HealthbarResult(BattlePokemon pokemon, int curHP) {
        super(Game.TARGET_FRAME_RATE);
        this.party = pokemon.party;
        this.partyMemberSlot = pokemon.partyMemberSlot;
        targetHP = curHP;
    }

    @Override public boolean start() {
        PokemonDisplay display = parent.screen.parties[party].displayMembers.get(partyMemberSlot);
        display.healthBar.setTargetHP(targetHP);
        display.pokemon.hp = targetHP;
        return false;
    }
}
