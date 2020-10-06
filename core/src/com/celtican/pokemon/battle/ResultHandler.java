package com.celtican.pokemon.battle;

import com.badlogic.gdx.utils.Array;
import com.celtican.pokemon.battle.results.Result;
import com.celtican.pokemon.screens.BattleScreen;

public class ResultHandler {

    public final BattleScreen screen;
    public Array<Result> results;
    public Result curResult;
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
        if (curResult == null && results.notEmpty()) curResult = results.first();
        while (goToNextResult) {
            goToNextResult = false;
            if (results.notEmpty()) {
                curResult = results.removeIndex(0);
                if (curResult.start()) goToNextResult = true;
            } else curResult = null;
        }
        if (curResult != null) curResult.update();
    }

    public void render() {
        if (curResult != null) curResult.render();
    }
}
