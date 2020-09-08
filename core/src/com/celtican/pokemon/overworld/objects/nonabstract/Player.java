package com.celtican.pokemon.overworld.objects.nonabstract;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.celtican.pokemon.Game;

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
    }
}
