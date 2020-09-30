package com.celtican.pokemon.battle.results;

import com.celtican.pokemon.Game;
import com.celtican.pokemon.battle.BattlePokemon;
import com.celtican.pokemon.battle.DisplayPokemon;
import com.celtican.pokemon.utils.data.Pokemon;

public class SetValueResult extends Result {
    private final int party;
    private final int partySlot;
    private final Type type;
    private final Object value;

    public SetValueResult(BattlePokemon pokemon, Type type, Object value) {
        this(pokemon, type, value, true);
    }
    public SetValueResult(BattlePokemon pokemon, Type type, Object value, boolean addToArray) {
        super(addToArray);
        party = pokemon.party;
        partySlot = pokemon.partyMemberSlot;
        this.type = type;
        this.value = value;
    }

    @Override public boolean start() {
        DisplayPokemon display = parent.screen.parties[party].displayMembers.get(partySlot);
        switch (type) {
            default:
                Game.logError("Unhandled type for SetValueResult.");
                break;
            case POKEMON:
                if (value instanceof BattlePokemon) display.pokemon.changePokemon((BattlePokemon)value);
                else Game.logError("Attempted to use a non-BattlePokemon value when setting pokemon in SetValueResult.");
                break;
            case STATS:
                if (value instanceof int[]) display.pokemon.stats = (int[])value;
                else Game.logError("Attempted to use a non-int[] value when setting stats in SetValueResult.");
                break;
            case HP:
                if (value instanceof Integer) display.pokemon.hp = (Integer)value;
                else Game.logError("Attempted to use a non-Integer value when setting hp in SetValueResult.");
                break;
            case STATUS:
                if (value instanceof Pokemon.StatusCondition) display.pokemon.status = (Pokemon.StatusCondition)value;
                else Game.logError("Attempted to use a non-Pokemon.StatusCondition value when setting status in SetValueResult.");
                break;
            case NAME:
                if (value instanceof String) display.pokemon.name = (String)value;
                else Game.logError("Attempted to use a non-String value when setting name in SetValueResult.");
                break;
        }
        return true;
    }

    public enum Type {
        POKEMON, STATS, HP, STATUS, NAME
    }
}
