package com.celtican.pokemon.battle;

import com.badlogic.gdx.graphics.Color;
import com.celtican.pokemon.Game;
import com.celtican.pokemon.utils.data.Vector2Int;
import com.celtican.pokemon.utils.graphics.Texture;

public class ExpBar extends Bar {

    private static final float DELTA = 0.5f / Game.TARGET_FRAME_RATE; // 75 pixels over 2 seconds

    public boolean canRise = false;
    public final HealthBar healthBar;
    private final Texture texture;
    private int frame = 0;

    public ExpBar(DisplayPokemon display, float from, float to) {
        super(from);
        texture = new Texture("spritesheets/battle.atlas", "expBar");
        display.pokemon.pokemon.gainExp();
        setValue(to);
        this.healthBar = display.healthBar;
        healthBar.expBar = this;
    }

    public boolean isFinished() {
        return curValue == targetValue;
    }
    public void hide() {
        if (frame < 4) frame = 4;
    }

    @Override public void update() {
        if (frame == 3 && canRise) super.update();
    }
    @Override
    public void render() {
        Vector2Int pos = healthBar.getPos();
        pos.add(7, -2);
        switch (frame) {
            case 0: case 6: pos.add(-3, 3); frame++; break;
            case 1: case 5: pos.add(-2, 2); frame++; break;
            case 2: case 4: pos.add(-1, 1); frame++; break;
        }
        texture.render(pos.x, pos.y);
        Game.game.canvas.setColor(new Color(0, 1, 0.7f, 1));
        Game.game.canvas.drawRect(pos.x+1, pos.y+1, (int) (curValue*75), 1);
        Game.game.canvas.resetColor();
    }

    @Override
    protected float getDelta() {
        return DELTA;
    }
}
