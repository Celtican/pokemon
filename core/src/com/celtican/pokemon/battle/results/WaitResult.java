package com.celtican.pokemon.battle.results;

public class WaitResult extends Result {
    int time;

    public WaitResult(int time) {
        this.time = time;
    }

    @Override public void update() {
        if (time-- == 0) parent.nextResult();
    }
}
