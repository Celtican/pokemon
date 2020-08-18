package com.celtican.pokemon.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.celtican.pokemon.Game;

public class RenderHandler {

    private final SpriteBatch batch;
    private final OrthographicCamera camera;
    private final ScreenViewport viewport;

    private int width, height;
    private Color clearColor = new Color();
    private Texture pixel;

    public RenderHandler() {
        resetClearColor();
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new ScreenViewport(camera);
        viewport.apply();
        camera.position.set(camera.viewportWidth/2, camera.viewportHeight/2, 0);
    }
    public void setup() {
        pixel = Game.game.assets.get("misc/pixel.png", Texture.class);
    }

    public void update() {
        if (Game.game.screen == null)
            return;
        Gdx.gl.glClearColor(clearColor.r, clearColor.g, clearColor.b, clearColor.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        if (Game.game.screen != null)
            try {
                Game.game.screen.render();
            } catch (Exception e) {
                e.printStackTrace();
            }
        batch.end();
    }

    public void resize(int width, int height) {
        viewport.update(width, height);
        camera.position.set(camera.viewportWidth/2, camera.viewportHeight/2, 0);
        this.width = MathUtils.ceil((float)width/Game.PIXEL_SIZE);
        this.height = MathUtils.ceil((float)height/Game.PIXEL_SIZE);
        if (Game.game.screen != null)
            Game.game.screen.resize(width, height);
    }

    public void draw(Texture texture, int x, int y) {
        batch.draw(texture, x * Game.PIXEL_SIZE, y * Game.PIXEL_SIZE,
                texture.getWidth() * Game.PIXEL_SIZE, texture.getHeight() * Game.PIXEL_SIZE);
    }
    public void draw(TextureAtlas.AtlasRegion region, int x, int y) {
        batch.draw(region, x * Game.PIXEL_SIZE, y * Game.PIXEL_SIZE,
                region.getRegionWidth() * Game.PIXEL_SIZE, region.getRegionHeight() * Game.PIXEL_SIZE);
    }
    private void drawRect(int x, int y, int width, int height) {
        batch.draw(pixel, x*Game.PIXEL_SIZE, y*Game.PIXEL_SIZE,
                width*Game.PIXEL_SIZE, height*Game.PIXEL_SIZE);
    }
    public void drawRect(int x, int y, int width, int height, Color color) {
        if (pixel == null)
            return;
        setColor(color);
        drawRect(x, y, width, height);
        resetColor();
    }
    public void drawRect(int x, int y, int width, int height, float r, float g, float b) {
        drawRect(x, y, width, height, r, g, b, 1);
    }
    public void drawRect(int x, int y, int width, int height, float r, float g, float b, float a) {
        if (pixel == null)
            return;
        setColor(r, g, b, a);
        drawRect(x, y, width, height);
        resetColor();
    }

    public void setClearColor(Color color) {
        clearColor = color;
    }
    public void setClearColor(float r, float g, float b, float a) {
        clearColor.r = r;
        clearColor.g = g;
        clearColor.b = b;
        clearColor.a = a;
    }
    public void resetClearColor() {
        setClearColor(0.5f, 0.5f, 0.5f, 1);
    }
    public void setColor(float r, float g, float b, float a) {
        batch.setColor(r, g, b, a);
    }
    public void setColor(Color color) {
        batch.setColor(color);
    }
    public void resetColor() {
        batch.setColor(Color.WHITE);
    }

    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }

    public void dispose() {
        if (batch != null)
            batch.dispose();
    }
}
