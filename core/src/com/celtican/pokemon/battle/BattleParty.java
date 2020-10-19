package com.celtican.pokemon.battle;

import com.badlogic.gdx.utils.Array;

import java.util.HashMap;

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

    private final HashMap<Effect, Integer> effects = new HashMap<>();
    public boolean hasEffect(Effect effect) {
        return effects.containsKey(effect);
    }
    public void setEffect(Effect effect, int value) {
        effects.put(effect, value);
    }
    public int getEffect(Effect effect) {
        return effects.get(effect);
    }
    public void removeEffect(Effect effect) {
        effects.remove(effect);
    }

    public enum Effect {
        SAFEGUARD, TAILWIND
    }
}
