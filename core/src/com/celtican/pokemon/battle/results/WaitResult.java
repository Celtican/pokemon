package com.celtican.pokemon.battle.results;

public class WaitResult extends Result {
    int time;

    public WaitResult(int time) {
        this(time, true);
    }
    public WaitResult(int time, boolean addToArray) {
        super(addToArray);
        this.time = time;
    }

    @Override public void update() {
        if (time-- == 0) parent.nextResult();
    }
}
