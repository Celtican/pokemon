package com.celtican.pokemon.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.celtican.pokemon.Game;
import com.celtican.pokemon.utils.data.Vector2Int;

public class RenderHandler {

    private final SpriteBatch batch;
    private final OrthographicCamera camera;
    private final ScreenViewport viewport;

    private int width, height;
    private Color clearColor = new Color();
    private Texture pixel;
    private FreeTypeFontGenerator fontGenerator;
    private BitmapFont font;
    private BitmapFont fontSmall;
    private GlyphLayout layout;
    private int frameRate;

    public RenderHandler() {
        resetClearColor();
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new ScreenViewport(camera);
        viewport.apply();
        camera.position.set(camera.viewportWidth/2, camera.viewportHeight/2, 0);
        layout = new GlyphLayout();
    }
    public void setup() {
        pixel = Game.game.assets.get("misc/pixel.png", Texture.class);
    }
    public void setupFont() {
        BitmapFont f = Game.game.assets.get("misc/fontSmall.fnt", BitmapFont.class);
        if (f == null) return;
        fontSmall = f;
        fontSmall.getData().setScale(Game.game.pixelSize / 8f);

        if (fontGenerator == null)
            fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("misc/font.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParameter.size = 16 * Game.game.pixelSize;
        fontParameter.kerning = false;
        fontParameter.shadowOffsetX = Game.game.pixelSize;
        fontParameter.shadowOffsetY = Game.game.pixelSize;
        font = fontGenerator.generateFont(fontParameter);
        font.setColor(Color.WHITE);
    }

    public void update() {
        if (Game.game.screen == null)
            return;

        layout.reset();

        Gdx.gl.glClearColor(clearColor.r, clearColor.g, clearColor.b, clearColor.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        if (Game.game.screen != null)
            try {
                Game.game.screen.render();
                if (fontSmall != null) Game.game.canvas.drawSmallText(2, height-10,
                        Integer.toString(Gdx.graphics.getFramesPerSecond()),
                        Game.game.isFastForward ? Color.RED : Color.WHITE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        batch.end();
    }

    public void resize(int width, int height) {
        viewport.update(width, height);
        camera.position.set(camera.viewportWidth/2, camera.viewportHeight/2, 0);
        this.width = MathUtils.ceil((float)width/Game.game.pixelSize);
        this.height = MathUtils.ceil((float)height/Game.game.pixelSize);
        if (Game.game.screen != null)
            Game.game.screen.resize(this.width, this.height);
    }

    public void draw(Texture texture, int x, int y) {
        batch.draw(texture, x * Game.game.pixelSize, y * Game.game.pixelSize,
                texture.getWidth() * Game.game.pixelSize, texture.getHeight() * Game.game.pixelSize);
    }
    public void draw(Texture texture, int x, int y, int scale) {
        batch.draw(texture, x * Game.game.pixelSize, y * Game.game.pixelSize,
                texture.getWidth() * Game.game.pixelSize * scale, texture.getHeight() * Game.game.pixelSize * scale);
    }
    public void draw(TextureAtlas.AtlasRegion region, int x, int y) {
        batch.draw(region, x * Game.game.pixelSize, y * Game.game.pixelSize,
                region.getRegionWidth() * Game.game.pixelSize, region.getRegionHeight() * Game.game.pixelSize);
    }
    public void draw(TextureAtlas.AtlasRegion texture, int x, int y, int scale) {
        batch.draw(texture, x * Game.game.pixelSize, y * Game.game.pixelSize,
                texture.getRegionWidth() * Game.game.pixelSize * scale, texture.getRegionHeight() * Game.game.pixelSize * scale);
    }
    public void drawRect(int x, int y, int width, int height) {
        if (pixel != null)
            batch.draw(pixel, x*Game.game.pixelSize, y*Game.game.pixelSize,
                    width*Game.game.pixelSize, height*Game.game.pixelSize);
    }
    public void drawRect(int x, int y, int width, int height, Color color) {
        setColor(color);
        drawRect(x, y, width, height);
        resetColor();
    }
    public void drawRect(int x, int y, int width, int height, float r, float g, float b) {
        drawRect(x, y, width, height, r, g, b, 1);
    }
    public void drawRect(int x, int y, int width, int height, float r, float g, float b, float a) {
        setColor(r, g, b, a);
        drawRect(x, y, width, height);
        resetColor();
    }
    public void drawBox(int x, int y, int width, int height) {
        drawRect(x, y, width, 1);
        drawRect(x, y, 1, height);
        drawRect(x, y+height-1, width, 1);
        drawRect(x+width-1, y, 1, height);
    }
    public void drawBox(int x, int y, int width, int height, Color color) {
        setColor(color);
        drawBox(x, y, width, height);
        resetColor();
    }
    public void drawBox(int x, int y, int width, int height, float r, float g, float b) {
        drawBox(x, y, width, height, r, g, b, 1);
    }
    public void drawBox(int x, int y, int width, int height, float r, float g, float b, float a) {
        setColor(r, g, b, a);
        drawBox(x, y, width, height);
        resetColor();
    }
    public void drawText(int x, int y, String text) {
        font.draw(batch, text, x * Game.game.pixelSize, (y+11) * Game.game.pixelSize);
    }
    public void drawText(int x, int y, String text, Color color) {
        if (color == null)
            drawText(x, y, text);
        else {
            font.setColor(color);
            drawText(x, y, text);
            font.setColor(Color.WHITE);
        }
    }
    public void drawSmallText(int x, int y, String text) {
        fontSmall.draw(batch, text.replace(" ", "  "), x * Game.game.pixelSize, (y+9) * Game.game.pixelSize);
    }
    public void drawSmallText(int x, int y, String text, Color color) {
        if (color == null)
            drawSmallText(x, y, text);
        else {
            fontSmall.setColor(color);
            drawSmallText(x, y, text);
            fontSmall.setColor(Color.WHITE);
        }
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
    public void setColor(float r, float g, float b) {
        setColor(r, g, b, 1);
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
    public int getWidthOfText(String text) {
        layout.setText(font, text);
        return MathUtils.ceil(layout.width/Game.game.pixelSize);
    }
    public int getHeightOfText(String text) {
        layout.setText(font, text);
        return MathUtils.ceil(layout.height/Game.game.pixelSize);
    }
    public Vector2Int getBoundsOfText(String text) {
        layout.setText(font, text);
        return new Vector2Int(MathUtils.ceil(layout.width/Game.game.pixelSize),
                MathUtils.ceil(layout.height/Game.game.pixelSize));
    }
    public int getWidthOfSmallText(String text) {
        layout.setText(fontSmall, text);
        return MathUtils.ceil(layout.width/Game.game.pixelSize);
    }
    public int getHeightOfSmallText(String text) {
        layout.setText(fontSmall, text);
        return MathUtils.ceil(layout.height/Game.game.pixelSize);
    }
    public Vector2Int getBoundsOfSmallText(String text) {
        layout.setText(fontSmall, text);
        return new Vector2Int(MathUtils.ceil(layout.width/Game.game.pixelSize),
                MathUtils.ceil(layout.height/Game.game.pixelSize));
    }

    public void dispose() {
        if (batch != null)
            batch.dispose();
    }
}
