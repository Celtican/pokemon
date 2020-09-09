package com.celtican.pokemon.screens;

import com.celtican.pokemon.Game;
import com.celtican.pokemon.utils.data.Pokemon;
import com.celtican.pokemon.utils.data.PokemonPC;
import com.celtican.pokemon.utils.graphics.AnimatedTexture;

public class BattleScreen extends Screen {

    // prepare for battle!

    private Pokemon p1;
    private AnimatedTexture pA1;

    public BattleScreen() {
        p1 = new PokemonPC(Game.game.data.getSpecies(1));
        pA1 = p1.getAnimatedTexture(true, 1000/12);
    }

    @Override public void render() {
        pA1.render(Game.game.canvas.getWidth()/2 - pA1.getWidth()/2,
                Game.game.canvas.getHeight()/2 - pA1.getHeight()/2);
    }

}
