package com.celtican.pokemon.battle.results;

import com.celtican.pokemon.battle.BattlePokemon;
import com.celtican.pokemon.battle.PokemonDisplay;

public class SwitchResult extends Result {
    private final int party;
    private final int slot;
    private final BattlePokemon newActivePokemon;

    public SwitchResult(BattlePokemon newActivePokemon) {
        this(newActivePokemon, false);
    }
    public SwitchResult(BattlePokemon newActivePokemon, boolean hide) {
        party = newActivePokemon.party;
        slot = newActivePokemon.partyMemberSlot;
        this.newActivePokemon = hide ? null : newActivePokemon;
    }

    @Override public boolean start() {
        PokemonDisplay display = parent.screen.parties[party].displayMembers.get(slot);
        if (newActivePokemon == null) {
            display.hide = true;
        } else {
            display.hide = false;
            display.changePokemon(newActivePokemon);
        }
        return true;
    }
}
