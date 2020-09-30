package com.celtican.pokemon.battle;

public abstract class Bar {

    public float targetValue;
    public float curValue;

    protected Bar(float startValue) {
        setValue(startValue);
        curValue = targetValue;
    }

    public void update() {
        if (targetValue == curValue) return;
        curValue = curValue < targetValue ? Math.min(curValue+getDelta(), targetValue) : Math.max(curValue-getDelta(), targetValue);
    }
    abstract public void render();

    public void setValue(float value) {
        if (value > 1) targetValue = 1;
        else if (value < 0) targetValue = 0;
        else targetValue = value;
    }

    abstract protected float getDelta();
}
