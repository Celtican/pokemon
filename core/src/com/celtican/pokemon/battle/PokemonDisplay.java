package com.celtican.pokemon.battle;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.celtican.pokemon.Game;
import com.celtican.pokemon.utils.data.Pokemon;
import com.celtican.pokemon.utils.data.Vector2Int;
import com.celtican.pokemon.utils.graphics.AnimatedTexture;

public class PokemonDisplay {

    public AnimatedTexture texture;
    public PokemonData pokemon;

    public boolean isFoe;
    public HealthBar healthBar;
    public boolean hide = false;

    public PokemonDisplay(BattlePokemon pokemon, boolean isFoe) {
        this.isFoe = isFoe;
        changePokemon(pokemon);
    }

    public void changePokemon(BattlePokemon pokemon) {
        if (this.pokemon == null) this.pokemon = new PokemonData(pokemon);
        else this.pokemon.changePokemon(pokemon);
        if (this.healthBar == null) healthBar = new HealthBar(this);
        else healthBar.changePokemon();
        texture = pokemon.getAnimatedTexture(isFoe);
    }

    public void update() {
        healthBar.update();
    }
    public void renderTexture() {
        if (hide || healthBar.curValue == 0) return;
        Vector2Int pos = getScreenPos();
        Color color = null;
        if (pokemon.status != Pokemon.StatusCondition.HEALTHY) {
            switch (pokemon.status) {
                case BURN:
                    float gb = MathUtils.sin(Game.game.frame/3f/MathUtils.PI2)*0.3f + 0.5f;
                    color = new Color(MathUtils.cos(Game.game.frame/2f/MathUtils.PI2)*0.1f + 0.95f, gb, gb, 1);
                    texture.setSpeed(Pokemon.animationSpeed/2);
                    break;
                case PARALYSIS:
                    texture.isPaused = MathUtils.random(MathUtils.sin(Game.game.frame/3f/MathUtils.PI2)*0.5f + 0.5f) > 0.3f;
                    float rg = MathUtils.random(0.2f) + 0.8f;
                    color = new Color(rg, rg, MathUtils.random(0.8f), 1);
                    break;
            }
        } else {
            texture.isPaused = false;
            texture.setSpeed(Pokemon.animationSpeed);
        }
        if (color != null) Game.game.canvas.setColor(color);
        if (isFoe) texture.render(pos.x - texture.getWidth()/2, pos.y);
        else texture.render(pos.x - texture.getWidth(), pos.y, 2);
        if (color != null) Game.game.canvas.resetColor();
    }
    public void renderHealth() {
        if (hide || healthBar.curValue == 0) return;
        healthBar.render();
    }

    public Vector2Int getScreenPos() {
        if (isFoe) return new Vector2Int(Game.game.canvas.getWidth() * 2/3,
                    Game.game.canvas.getHeight()/2);
        else return new Vector2Int(Game.game.canvas.getWidth()/3,
                    Game.game.canvas.getHeight()/4);
    }

    public static class PokemonData {
        public BattlePokemon pokemon;

        public int[] stats;
        public int hp;
        public Pokemon.StatusCondition status;
        public String name;

        public PokemonData() {}
        public PokemonData(BattlePokemon pokemon) {
            changePokemon(pokemon);
        }

        public void changePokemon(BattlePokemon pokemon) {
            this.pokemon = pokemon;
            stats = pokemon.getStats();
            hp = pokemon.getHP();
            status = pokemon.statusCondition;
            name = pokemon.getName();
        }
    }
}
