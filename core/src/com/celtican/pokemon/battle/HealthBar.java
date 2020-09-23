package com.celtican.pokemon.battle;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.celtican.pokemon.Game;
import com.celtican.pokemon.utils.data.Vector2Int;
import com.celtican.pokemon.utils.graphics.Texture;

public class HealthBar extends Bar {
    private static final Color LIGHT_GREEN  = new Color(0x00ff4aff);
    private static final Color DARK_GREEN   = new Color(0x00bd21ff);
    private static final Color LIGHT_YELLOW = new Color(0xffad00ff);
    private static final Color DARK_YELLOW  = new Color(0xa58429ff);
    private static final Color LIGHT_RED    = new Color(0xff4273ff);
    private static final Color DARK_RED     = new Color(0xa54a5aff);

    private final static float delta = 1f/Game.TARGET_FRAME_RATE;

    public final PokemonDisplay master;
    private String name;
    private final Texture texture;

    protected HealthBar(PokemonDisplay master) {
        super(1);
        this.master = master;
        master.healthBar = this;
        texture = new Texture("spritesheets/battle.atlas", master.isFoe ? "foeHealthBar" : "allyHealthBar");
        name = master.pokemon.getName();
    }

    @Override public void render() {
        if (master.isFoe) renderFoe();
        else renderAlly();
    }

    @Override protected float getDelta() {
        return delta;
    }

    public void setTargetHP(int hp) {
        if (hp <= 0) {
            setValue(0);
            return;
        }
        int maxHP = master.pokemon.getStat(0);
        if (hp >= maxHP) {
            setValue(1);
            return;
        }
        setValue((float)hp/maxHP);
    }

    private void renderAlly() {
        Vector2Int pos = master.getScreenPos();
        pos.x -= texture.getWidth()/2;
        pos.y += master.texture.getHeight()*2 + 2;
        texture.render(pos.x, pos.y);

        Color colorDark;
        if (curValue <= 0.2f) {
            Game.game.canvas.setColor(LIGHT_RED);
            colorDark = DARK_RED;
        } else if (curValue <= 0.5f) {
            Game.game.canvas.setColor(LIGHT_YELLOW);
            colorDark = DARK_YELLOW;
        } else {
            Game.game.canvas.setColor(LIGHT_GREEN);
            colorDark = DARK_GREEN;
        }

        int fill = MathUtils.round(curValue*100);
        if (curValue > 0 && fill == 0) fill = 1;
        else if (curValue < 1 && fill == 100) fill = 0;
        Game.game.canvas.drawRect(pos.x+1, pos.y+6, Math.min(fill, 20), 1);
        Game.game.canvas.drawRect(pos.x+2, pos.y+5, Math.min(fill, 20), 1);
        Game.game.canvas.drawRect(pos.x+3, pos.y+4, fill, 1);
        Game.game.canvas.setColor(colorDark);
        Game.game.canvas.drawRect(pos.x+4, pos.y+3, fill, 1);
        Game.game.canvas.drawRect(pos.x+85, pos.y+2, Math.max(fill-80, 0), 1);
        Game.game.canvas.drawRect(pos.x+86, pos.y+1, Math.max(fill-80, 0), 1);
        Game.game.canvas.resetColor();
        Game.game.canvas.drawSmallText(pos.x+23, pos.y+6, name);
    }
    private void renderFoe() {
        Vector2Int pos = master.getScreenPos();
        pos.x -= texture.getWidth()/2;
        pos.y += master.texture.getHeight() + 1;
        texture.render(pos.x, pos.y);

        Color colorDark;
        if (curValue <= 0.2f) {
            Game.game.canvas.setColor(LIGHT_RED);
            colorDark = DARK_RED;
        } else if (curValue <= 0.5f) {
            Game.game.canvas.setColor(LIGHT_YELLOW);
            colorDark = DARK_YELLOW;
        } else {
            Game.game.canvas.setColor(LIGHT_GREEN);
            colorDark = DARK_GREEN;
        }

        int fill = MathUtils.round(curValue * 75);
        if (curValue > 0 && fill == 0) fill = 1;
        else if (curValue < 1 && fill == 75) fill = 0;
        Game.game.canvas.drawRect(pos.x+1, pos.y+4, Math.min(fill, 15), 1);
        Game.game.canvas.drawRect(pos.x+2, pos.y+3, Math.min(fill, 15), 1);
        Game.game.canvas.drawRect(pos.x+3, pos.y+2, fill, 1);
        Game.game.canvas.setColor(colorDark);
        Game.game.canvas.drawRect(pos.x+4, pos.y+1, fill, 1);
        Game.game.canvas.resetColor();
        Game.game.canvas.drawSmallText(pos.x+18, pos.y+4, name);
    }

//    private PokemonDisplay master;
//    private Texture texture;
//
//    private float percentFull = 1;
//    private String name;
//
//    public HealthBar(PokemonDisplay pokemonDisplay) {
//        pokemonDisplay.healthBar = this;
//        master = pokemonDisplay;
//        texture = new Texture("spritesheets/battle.atlas", master.isFoe ? "foeHealthBar" : "allyHealthBar");
//        name = master.pokemon.getName();
//    }
//
//    public void update() {
//        percentFull -= 0.01f;
//        if (percentFull < 0) percentFull = 1;
//    }
//    public void render() {
//        Vector2Int pos = master.getScreenPos();
//        pos.x -= texture.getWidth()/2;
//        if (master.isFoe) pos.y += master.texture.getHeight() + 1;
//        else pos.y += master.texture.getHeight()*2 + 2;
//        texture.render(pos.x, pos.y);
//
//        if (percentFull <= 0.2f) Game.game.canvas.setColor(Color.RED);
//        else if (percentFull <= 0.5f) Game.game.canvas.setColor(Color.YELLOW);
//        else Game.game.canvas.setColor(Color.GREEN);
//
//        if (master.isFoe) {
//            int fill = MathUtils.round(percentFull * 75);
//            if (percentFull > 0 && fill == 0) fill = 1;
//            else if (percentFull < 1 && fill == 75) fill = 0;
//            Game.game.canvas.drawRect(pos.x+1, pos.y+4, Math.min(fill, 15), 1);
//            Game.game.canvas.drawRect(pos.x+2, pos.y+3, Math.min(fill, 15), 1);
//            Game.game.canvas.drawRect(pos.x+3, pos.y+2, fill, 1);
//            Game.game.canvas.drawRect(pos.x+4, pos.y+1, fill, 1);
//            Game.game.canvas.resetColor();
//            Game.game.canvas.drawSmallText(pos.x+18, pos.y+4, name);
//        } else {
//            int fill = MathUtils.round(percentFull*100);
//            if (percentFull > 0 && fill == 0) fill = 1;
//            else if (percentFull < 1 && fill == 100) fill = 0;
//            Game.game.canvas.drawRect(pos.x+1, pos.y+6, Math.min(fill, 20), 1);
//            Game.game.canvas.drawRect(pos.x+2, pos.y+5, Math.min(fill, 20), 1);
//            Game.game.canvas.drawRect(pos.x+3, pos.y+4, fill, 1);
//            Game.game.canvas.drawRect(pos.x+4, pos.y+3, fill, 1);
//            Game.game.canvas.drawRect(pos.x+85, pos.y+2, Math.max(fill-80, 0), 1);
//            Game.game.canvas.drawRect(pos.x+86, pos.y+1, Math.max(fill-80, 0), 1);
//            Game.game.canvas.resetColor();
//            Game.game.canvas.drawSmallText(pos.x+23, pos.y+6, name);
//        }
//    }
}
