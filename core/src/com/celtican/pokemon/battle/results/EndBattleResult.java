package com.celtican.pokemon.battle.results;

import com.celtican.pokemon.battle.BattlePokemon;

public class EndBattleResult extends Result {
    public EndBattleResult() {
        this(true);
    }
    public EndBattleResult(boolean addToArray) {
        super(addToArray);
    }

    @Override public boolean start() {
        for (BattlePokemon pokemon : parent.screen.parties[0].members) {
            if (pokemon != null && pokemon.expGained > 0) {
                new ExpResult(pokemon);
                break;
            }
        }
        parent.screen.endBattle = true;
        return true;
    }
}
