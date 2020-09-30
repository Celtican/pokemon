package com.celtican.pokemon.battle.results;

import com.celtican.pokemon.Game;
import com.celtican.pokemon.battle.ResultHandler;
import com.celtican.pokemon.screens.BattleScreen;

public abstract class Result {
    protected final ResultHandler parent;
    public boolean inArray;

    public Result() {
        this(true);
    }
    public Result(boolean addToArray) {
        parent = ((BattleScreen)Game.game.screen).resultHandler;
        inArray = addToArray;
        if (addToArray) parent.addResult(this);
    }

    public boolean start() {
        return false;
    } // returns true if immediately moving to next result
    public void update() {}
    public void render() {}
}
