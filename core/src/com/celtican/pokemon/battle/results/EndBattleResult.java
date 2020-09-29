package com.celtican.pokemon.battle.results;

import com.celtican.pokemon.Game;
import com.celtican.pokemon.screens.OverworldScreen;
import com.celtican.pokemon.screens.TitleScreen;

public class EndBattleResult extends Result {
    @Override public boolean start() {
        if (Game.game.map != null) Game.game.switchScreens(new OverworldScreen(Game.game.map));
        else Game.game.switchScreens(new TitleScreen());
        return true;
    }
}
