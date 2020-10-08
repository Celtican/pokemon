package com.celtican.pokemon.battle;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.celtican.pokemon.Game;
import com.celtican.pokemon.utils.data.Pokemon;
import com.celtican.pokemon.utils.data.Vector2Int;
import com.celtican.pokemon.utils.graphics.Texture;

public class HealthBar extends Bar {
    private static final Color LIGHT_GREEN  = new Color(0x00ff4aff);
    private static final Color DARK_GREEN   = new Color(0x00bd21ff);
    private static final Color LIGHT_YELLOW = new Color(0xffad00ff);
    private static final Color DARK_YELLOW  = new Color(0xa58429ff);
    private static final Color LIGHT_RED    = new Color(0xff4273ff);
    private static final Color DARK_RED     = new Color(0xa54a5aff);

    private final static float DELTA = 1f/Game.TARGET_FRAME_RATE;

    public int overrideLevel = -1;
    public final DisplayPokemon master;
    private final Texture texture;
    public ExpBar expBar;

    protected HealthBar(DisplayPokemon master) {
        super(1);
        this.master = master;
        master.healthBar = this;
        texture = new Texture("spritesheets/battle.atlas", master.isFoe ? "foeHealthBar" : "allyHealthBar");
        changePokemon();
    }

    public void changePokemon() {
        curValue = 0;
        setTargetHP(master.pokemon.hp);
    }

    @Override public void update() {
        if (expBar != null) expBar.update();
        super.update();
    }
    @Override public void render() {
        if (expBar != null) expBar.render();
        if (master.isFoe) renderFoe();
        else renderAlly();
    }

    @Override protected float getDelta() {
        return DELTA;
    }

    public void setTargetHP(int hp) {
        if (hp <= 0) {
            setValue(0);
            return;
        }
        int maxHP = master.pokemon.stats[0];
        if (hp >= maxHP) {
            setValue(1);
            return;
        }
        setValue((float)hp/maxHP);
    }

    private void renderAlly() {
        Vector2Int pos = getPos();
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
        else if (curValue < 1 && fill == 100) fill = 99;
        Game.game.canvas.drawRect(pos.x+1, pos.y+6, Math.min(fill, 20), 1);
        Game.game.canvas.drawRect(pos.x+2, pos.y+5, Math.min(fill, 20), 1);
        Game.game.canvas.drawRect(pos.x+3, pos.y+4, fill, 1);
        Game.game.canvas.setColor(colorDark);
        Game.game.canvas.drawRect(pos.x+4, pos.y+3, fill, 1);
        Game.game.canvas.drawRect(pos.x+85, pos.y+2, Math.max(fill-80, 0), 1);
        Game.game.canvas.drawRect(pos.x+86, pos.y+1, Math.max(fill-80, 0), 1);
        Game.game.canvas.resetColor();
        Color color;
        if (master.pokemon.status == Pokemon.StatusCondition.HEALTHY)
            color = null;
        else {
            switch (master.pokemon.status) {
                case BURN: color = Color.RED; break;
                case POISON: color = Color.PINK; break;
                case FREEZE: color = Color.BLUE; break;
                case PARALYSIS: color = Color.YELLOW; break;
                default:
                    if (master.pokemon.status.isToxic()) color = Color.PURPLE;
                    else if (master.pokemon.status.isSleep()) color = Color.GRAY;
                    else {
                        Game.logError("Unhandled status condition: " + master.pokemon.status);
                        color = null;
                    }
            }
        }
        Game.game.canvas.drawSmallText(pos.x+23, pos.y+6, master.pokemon.name, color);
        int levelWidth = Game.game.canvas.getWidthOfSmallText(Integer.toString(master.pokemon.pokemon.getLevel()))/2;
        Game.game.canvas.drawSmallText(pos.x+9 - levelWidth/2, pos.y+8, Integer.toString(overrideLevel > 0 ? overrideLevel : master.pokemon.pokemon.getLevel()));
    }
    private void renderFoe() {
        Vector2Int pos = getPos();
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
        else if (curValue < 1 && fill == 75) fill = 74;
        Game.game.canvas.drawRect(pos.x+1, pos.y+4, Math.min(fill, 15), 1);
        Game.game.canvas.drawRect(pos.x+2, pos.y+3, Math.min(fill, 15), 1);
        Game.game.canvas.drawRect(pos.x+3, pos.y+2, fill, 1);
        Game.game.canvas.setColor(colorDark);
        Game.game.canvas.drawRect(pos.x+4, pos.y+1, fill, 1);
        Game.game.canvas.resetColor();
        Color color;
        if (master.pokemon.status == Pokemon.StatusCondition.HEALTHY)
            color = null;
        else {
            switch (master.pokemon.status) {
                case BURN: color = Color.RED; break;
                case POISON: color = Color.PINK; break;
                case FREEZE: color = Color.BLUE; break;
                case PARALYSIS: color = Color.YELLOW; break;
                default:
                    if (master.pokemon.status.isToxic()) color = Color.PURPLE;
                    else if (master.pokemon.status.isSleep()) color = Color.GRAY;
                    else {
                        Game.logError("Unhandled status condition: " + master.pokemon.status);
                        color = null;
                    }
            }
        }
        Game.game.canvas.drawSmallText(pos.x+18, pos.y+4, master.pokemon.name, color);
        int levelWidth = Game.game.canvas.getWidthOfSmallText(Integer.toString(master.pokemon.pokemon.getLevel()))/2;
        Game.game.canvas.drawSmallText(pos.x+6 - levelWidth/2, pos.y+6, Integer.toString(overrideLevel > 0 ? overrideLevel : master.pokemon.pokemon.getLevel()));
    }

    public Vector2Int getPos() {
        Vector2Int pos = master.getScreenPos();
        pos.x -= texture.getWidth()/2;
        if (master.isFoe) pos.y += master.texture.getHeight() + 1;
        else pos.y += master.texture.getHeight() + master.texture.getHeight() + 1;
        return pos;
    }
}
