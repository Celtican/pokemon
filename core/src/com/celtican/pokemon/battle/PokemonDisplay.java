package com.celtican.pokemon.battle;

import com.celtican.pokemon.Game;
import com.celtican.pokemon.utils.data.Pokemon;
import com.celtican.pokemon.utils.data.Vector2Int;
import com.celtican.pokemon.utils.graphics.AnimatedTexture;

public class PokemonDisplay {

    public AnimatedTexture texture;
    public Pokemon pokemon;

    public boolean isFoe;
    public HealthBar healthBar;

    public PokemonDisplay(Pokemon pokemon, boolean isFoe) {
        this.pokemon = pokemon;
        texture = pokemon.getAnimatedTexture(isFoe);
        this.isFoe = isFoe;
        new HealthBar(this);
    }

    public void update() {
        healthBar.update();
    }
    public void renderTexture() {
        Vector2Int pos = getScreenPos();
        if (isFoe) texture.render(pos.x - texture.getWidth()/2, pos.y);
        else texture.render(pos.x - texture.getWidth(), pos.y, 2);
    }
    public void renderHealth() {
        healthBar.render();
    }

    public Vector2Int getScreenPos() {
        if (isFoe)
            return new Vector2Int(Game.game.canvas.getWidth() * 2/3,
                    Game.game.canvas.getHeight()/2);
        else
            return new Vector2Int(Game.game.canvas.getWidth()/3,
                    Game.game.canvas.getHeight()/4);
//        float length = ((BattleScreen)Game.game.screen).displayLength;
//        float squish = ((BattleScreen)Game.game.screen).squish;
//        Vector2Int pos = new Vector2Int(MathUtils.round(MathUtils.cos(angle)*length*depth),
//                MathUtils.round(MathUtils.sin(angle)*length*depth*squish));
//        pos.add(Game.game.canvas.getWidth()/2, Game.game.canvas.getHeight()/3);
//        return pos;
    }
}
