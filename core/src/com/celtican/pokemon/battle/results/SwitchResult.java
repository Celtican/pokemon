package com.celtican.pokemon.battle.results;

import com.celtican.pokemon.battle.BattlePokemon;
import com.celtican.pokemon.battle.DisplayPokemon;

public class SwitchResult extends Result {
    private final int party;
    private final int slot;
    private final BattlePokemon newActivePokemon;

    public SwitchResult(BattlePokemon newActivePokemon) {
        this(newActivePokemon, false, true);
    }
    public SwitchResult(BattlePokemon newActivePokemon, boolean hide) {
        this(newActivePokemon, hide, true);
    }
    public SwitchResult(BattlePokemon newActivePokemon, boolean hide, boolean addToArray) {
        super(addToArray);
        party = newActivePokemon.party;
        slot = newActivePokemon.partyMemberSlot;
        this.newActivePokemon = hide ? null : newActivePokemon;
    }

    @Override public boolean start() {
        DisplayPokemon display = parent.screen.parties[party].displayMembers.get(slot);
        if (newActivePokemon == null) {
            display.hide = true;
        } else {
            display.hide = false;
            display.changePokemon(newActivePokemon);
        }
        return true;
    }
}
