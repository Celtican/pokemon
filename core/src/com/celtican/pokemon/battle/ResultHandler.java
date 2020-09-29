package com.celtican.pokemon.battle;

import com.badlogic.gdx.utils.Array;
import com.celtican.pokemon.battle.results.Result;
import com.celtican.pokemon.screens.BattleScreen;

public class ResultHandler {

    public final BattleScreen screen;
    public Array<Result> results;
    private boolean goToNextResult = false;

    public ResultHandler(BattleScreen battleScreen) {
        this.screen = battleScreen;
        results = new Array<>();
    }

    public boolean hasResults() {
        return results.notEmpty();
    }
    public void nextResult() {
        goToNextResult = true;
    }
    public void addResult(Result result) {
        results.add(result);
    }
    public void setResults(Array<Result> results) {
        this.results = results;
    }

    public void update() {
        while (goToNextResult) {
            goToNextResult = false;
            results.removeIndex(0);
            if (results.notEmpty() && results.first().start()) goToNextResult = true;
        }
        if (hasResults()) results.first().update();
    }

    public void render() {
        if (hasResults()) results.first().render();
    }
}
