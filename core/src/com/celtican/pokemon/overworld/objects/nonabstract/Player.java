package com.celtican.pokemon.overworld.objects.nonabstract;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.celtican.pokemon.Game;
import com.celtican.pokemon.overworld.Tile;
import com.celtican.pokemon.screens.BattleScreen;

public class Player extends Character {
    public Player() {
        super();
        Game.game.map.player = this;
    }

    @Override public void update() {
        boolean isRunning = Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT);
        int speed = isRunning ? 2 : 1;
        Vector2 delta = new Vector2();
        if (Gdx.input.isKeyPressed(Input.Keys.W))
            delta.y += speed;
        if (Gdx.input.isKeyPressed(Input.Keys.S))
            delta.y -= speed;
        if (Gdx.input.isKeyPressed(Input.Keys.A))
            delta.x -= speed;
        if (Gdx.input.isKeyPressed(Input.Keys.D))
            delta.x += speed;
        changeMove(delta, isRunning);
        super.update();
        Game.game.map.camera.set((int)hitbox.x + hitbox.width/2, (int)hitbox.y + hitbox.height/2);

        if (delta.isZero() || Game.game.frame % Game.TARGET_FRAME_RATE != 0)
            return;
        Array<Tile> tiles = Game.game.map.getTiles(Game.game.map.worldPosToTilePos(
                (int)(hitbox.x + hitbox.width/2), (int)(hitbox.y + hitbox.height/2)));
        if (tiles != null)
            for (int i = 0; i < tiles.size; i++)
                if (tiles.get(i).type == Tile.Type.GRASS) {
                    if (MathUtils.random(4) == 0)
                        Game.game.switchScreens(new BattleScreen());
                    return;
                }
    }
}
