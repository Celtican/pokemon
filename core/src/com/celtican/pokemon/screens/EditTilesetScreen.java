package com.celtican.pokemon.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.celtican.pokemon.Game;
import com.celtican.pokemon.overworld.Tile;
import com.celtican.pokemon.overworld.Tileset;
import com.celtican.pokemon.utils.data.Vector2Int;
import com.celtican.pokemon.utils.graphics.Button;

public class EditTilesetScreen extends Screen implements Input.TextInputListener {

    private Tile selectedTile;
    private Tileset tileset;
    private Array<Button> tileButtons;
    private Array<Button> buttons;

    private boolean loaded = false;
    private boolean inputToLoad;

    public EditTilesetScreen() {
        askForInput();
    }

    @Override public void update() {
        if (!loaded)
            return;
        tileButtons.forEach(Button::update);
        buttons.forEach(Button::update);
    }
    @Override public void render() {
        if (!loaded)
            return;
        tileButtons.forEach(Button::render);
        String name = selectedTile.texture.toString();
        Vector2Int dim = Game.game.canvas.getBoundsOfText(name);
        Game.game.canvas.drawText(Game.game.canvas.getWidth()-dim.x, Game.game.canvas.getHeight()-dim.y, name);
        buttons.forEach(Button::render);
    }
    @Override public void show() {
        if (!loaded)
            return;
        tileButtons.forEach(Button::show);
        buttons.forEach(Button::show);
    }
    @Override public void hide() {
        if (!loaded)
            return;
        tileButtons.forEach(Button::hide);
        buttons.forEach(Button::hide);
    }
    @Override public void resize(int width, int height) {
        if (!loaded)
            return;
        int x = 0, y = Game.game.canvas.getHeight()-Game.TILE_SIZE;
        for (Button button : new Array.ArrayIterator<>(tileButtons)) {
            button.x = x;
            button.y = y;
            y -= Game.TILE_SIZE;
            if (y < 0) {
                y = Game.game.canvas.getHeight()-Game.TILE_SIZE;
                x += Game.TILE_SIZE;
            }
        }
        y = Game.game.canvas.getHeight() - Game.game.canvas.getHeightOfText(selectedTile.texture.toString()) - 2;
        for (Button button : new Array.ArrayIterator<>(buttons)) {
            y -= button.height - 1;
            button.y = y;
            button.x = Game.game.canvas.getWidth() - button.width;
        }
    }

    private void askForInput() {
        askForInput(true);
    }
    private void askForInput(boolean inputToLoad) {
        this.inputToLoad = inputToLoad;
        if (inputToLoad)
            Gdx.input.getTextInput(this, "Input file location for tileset or atlas", "", "");
        else
            Gdx.input.getTextInput(this, "Input file location to save tileset", "", "");
    }
    @Override public void input(String text) {
        if (inputToLoad) {
            String[] parts = text.split("\\.");
            switch (parts[parts.length - 1]) {
                case "atlas":
                    TextureAtlas atlas = Game.game.assets.get(text, TextureAtlas.class);
                    if (atlas != null)
                        createButtons(atlas, text);
                    else
                        askForInput();
                    break;
                case "json":
                    Tileset t = Tileset.fromFileName(text);
                    if (t != null)
                        createButtons(t);
                    else
                        askForInput();
                    break;
                default:
                    askForInput();
                    break;
            }
        } else {
            if (text.equals(""))
                return;
            String json = Game.JSON_PRETTY_PRINT ? new Json().prettyPrint(tileset) : new Json().toJson(tileset);
            FileHandle file = new FileHandle(text);
            file.writeString(json, false);
        }
    }
    @Override public void canceled() {
        Game.game.switchScreens(new MainMenuScreen());
    }

    private void createButtons(TextureAtlas atlas, String fileName) {
        createButtons(new Tileset(fileName, atlas));
    }
    private void createButtons(final Tileset tileset) {
        this.tileset = tileset;
        tileButtons = new Array<>();
        buttons = new Array<>();
        for (int i = 0; i < tileset.size(); i++) {
            int finalI = i;
            tileButtons.add(new Button(0, 0, Game.TILE_SIZE, Game.TILE_SIZE) {
                @Override public void update() {
                    if (isMouseJustPressed())
                        clickTile(finalI);
                }
                @Override public void render() {
                    tileset.get(finalI).renderDebug(x, y);
                }
            });
        }
        Vector2Int dim = Game.game.canvas.getBoundsOfSmallText("Change Type");
        buttons.add(new Button(0, 0, dim.x+4, dim.y+4) {
            @Override public void clicked() {
                Tile.Type[] types = Tile.Type.values();
                int ordinal = selectedTile.type.ordinal();
                if (ordinal + 1 >= types.length)
                    selectedTile.type = types[0];
                else
                    selectedTile.type = types[ordinal+1];
            }
            @Override public void render() {
                super.render();
                Game.game.canvas.drawSmallText(x+2, y+2, "Change Type");
            }
        });
        dim = Game.game.canvas.getBoundsOfSmallText("Save");
        buttons.add(new Button(0, 0, dim.x+4, dim.y+4) {
            @Override public void clicked() {
                askForInput(false);
            }
            @Override public void render() {
                super.render();
                Game.game.canvas.drawSmallText(x+2, y+2, "Save");
            }
        });
        loaded = true;
        clickTile(0);
        show();
        resize(Game.game.canvas.getWidth(), Game.game.canvas.getHeight());
    }
    private void clickTile(int i) {
        if (i < 0 || i >= tileset.size()) {
            if (tileset.size() != 0)
                clickTile(0);
            Game.logWarning("test");
            return;
        }
        selectedTile = tileset.get(i);
    }
}
