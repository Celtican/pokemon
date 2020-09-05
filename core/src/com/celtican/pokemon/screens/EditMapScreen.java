package com.celtican.pokemon.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.Json;
import com.celtican.pokemon.Game;
import com.celtican.pokemon.overworld.Map;
import com.celtican.pokemon.overworld.Tile;
import com.celtican.pokemon.overworld.Tileset;
import com.celtican.pokemon.overworld.objects.MapObject;
import com.celtican.pokemon.utils.data.Vector2Int;
import com.celtican.pokemon.utils.graphics.Button;

public class EditMapScreen extends Screen {

    private static final String[] MAP_OBJECT_CLASS_NAMES = new String[] {"MapObjectWithTexture"};
    private static final int SCREEN_WIDTH = 60;

    private Map map;

    private LeftSideScreen leftScreen;
    private LeftSideScreen leftScreenToSwitchTo;
    private RightSideScreen rightScreen;
    private RightSideScreen rightScreenToSwitchTo;

    public EditMapScreen() {
        new NewMapInitializer();
    }
    public EditMapScreen(Map map) {
        this.map = map;
    }

    @Override public void update() {
        if (map == null)
            return;
        map.update();
        if (leftScreenToSwitchTo != null) {
            if (leftScreen != null)
                leftScreen.hide();
            leftScreen = leftScreenToSwitchTo;
            leftScreenToSwitchTo = null;
            leftScreen.show();
            leftScreen.resize(Game.game.canvas.getWidth(), Game.game.canvas.getHeight());
        }
        if (rightScreenToSwitchTo != null) {
            if (rightScreen != null)
                rightScreen.hide();
            rightScreen = rightScreenToSwitchTo;
            rightScreenToSwitchTo = null;
            rightScreen.show();
            rightScreen.resize(Game.game.canvas.getWidth(), Game.game.canvas.getHeight());
        }
        if (leftScreen != null)
            leftScreen.update();
        if (rightScreen != null)
            rightScreen.update();
    }
    @Override public void render() {
        if (map == null)
            return;
        Vector2Int start = map.worldPosToScreenPos(0, 0);
        Game.game.canvas.drawBox(start.x, start.y, map.chunksX*Game.CHUNK_SIZE, map.chunksY*Game.CHUNK_SIZE);
        map.render();
        if (leftScreen != null)
            leftScreen.render();
        if (rightScreen != null)
            rightScreen.render();
    }
    @Override public void show() {
        Game.game.map = map;
        if (leftScreen != null)
            leftScreen.show();
        if (rightScreen != null)
            rightScreen.show();
    }
    @Override public void hide() {
        if (leftScreen != null)
            leftScreen.hide();
        if (rightScreen != null)
            rightScreen.hide();
    }
    @Override public void resize(int width, int height) {
        if (leftScreen != null)
            leftScreen.resize(width, height);
        if (rightScreen != null)
            rightScreen.resize(width, height);
    }

    private void switchTo(LeftSideScreen screen) {
        leftScreenToSwitchTo = screen;
    }
    private void switchTo(RightSideScreen screen) {
        rightScreenToSwitchTo = screen;
    }
    private boolean mouseIsOverAMenu() {
        return (leftScreen != null && leftScreen.isMouseOver()) || (rightScreen != null && rightScreen.isMouseOver());
    }

    private class NewMapInitializer implements Input.TextInputListener {
        private int width;
        private int height;
        public NewMapInitializer() {
            getTextInput("height");
        }

        @Override public void input(String text) {
            try {
                int i = Integer.parseInt(text);
                if (i > 0) {
                    if (width == 0) {
                        width = i;
                        getTextInput("height");
                    } else if (height == 0) {
                        height = i;
                        getTextInput("layers");
                    } else {
                        map = new Map(width, height, i);
                        Game.game.map = map;
                        switchTo(new MainScreen());
                    }
                    return;
                }
            } catch (NumberFormatException ignored) {}
            getTextInput(width == 0 ? "width" : height == 0 ? "height" : "layers");
        }
        @Override public void canceled() {
            Game.game.switchScreens(new MainMenuScreen());
        }

        private void getTextInput(String forWhat) {
            Gdx.input.getTextInput(this, "Enter number of chunks by " + forWhat,
                    "2", "2");
        }
    }
    private class TilesetFetcher implements Input.TextInputListener {
        public TilesetFetcher() {
            Gdx.input.getTextInput(this, "Enter local location of tileset json", "overworld\\tilesets\\sample.json", "");
        }

        @Override public void input(String text) {
            Tileset tileset = Tileset.fromFileName(text);
            if (tileset != null) {
                map.addTileset(tileset);
                if (rightScreen instanceof TilesetSelectScreen) {
                    ((TilesetSelectScreen) rightScreen).getTilesets();
                    switchTo(new TilesetScreen(tileset));
                }
            }
        }
        @Override public void canceled() {}
    }
    private class SaveMap implements Input.TextInputListener {
        public SaveMap() {
            Gdx.input.getTextInput(this, "Input exact file location to save map",
                    "overworld/maps/sample.json", "");
        }

        @Override public void input(String text) {
            if (text.equals(""))
                return;
            String json = Game.JSON_PRETTY_PRINT ? new Json().prettyPrint(map) : new Json().toJson(map);
            FileHandle file = new FileHandle(text);
            file.writeString(json, false);
        }
        @Override public void canceled() {}
    }
    private class LoadMap implements Input.TextInputListener {
        public LoadMap() {
            Gdx.input.getTextInput(this, "Input internal file location to load map",
                    "overworld/maps/sample.json", "");
        }

        @Override public void input(String text) {
            if (text.equals(""))
                return;
            FileHandle file = Gdx.files.internal(text);
            if (file.exists()) {
                map = new Json().fromJson(Map.class, file.readString());
                Game.game.map = map;
            }
        }
        @Override public void canceled() {}
    }

    private abstract static class SideScreen {
        public int width;
        public final Array<Button> buttons;

        public SideScreen() {
            this(SCREEN_WIDTH);
        }
        public SideScreen(int width) {
            this.width = width;
            buttons = new Array<>();
        }

        public void update() {
            buttons.forEach(Button::update);
        }
        public void render() {
            buttons.forEach(Button::render);
        }

        public void show() {
            buttons.forEach(Button::show);
        }
        public void hide() {
            buttons.forEach(Button::hide);
        }

        public void resize(int width, int height) {
            int x = getX();
            int textHeight = Game.game.canvas.getHeightOfSmallText("Test") + 4;
            for (int i = 0; i < buttons.size; i++) {
                Button button = buttons.get(i);
                button.x = x;
                button.y = height - ((i+1)*textHeight);
            }
        }

        public void addButton(String name, Runnable onClick) {
            int height = Game.game.canvas.getHeightOfSmallText("Test") + 4;
            Vector2Int bounds = Game.game.canvas.getBoundsOfSmallText(name);
            bounds.add(4, 4);
            int y;
            if (buttons.notEmpty())
                y = buttons.peek().y - height;
            else
                y = Game.game.canvas.getHeight() - height;
            buttons.add(new Button(getX(), y, bounds.x, height) {
                @Override public void clicked() {
                    if (onClick != null)
                        onClick.run();
                }
                @Override public void render() {
                    super.render();
                    Game.game.canvas.drawSmallText(x+2, y+2, name);
                }
            });
        }

        public abstract int getX();
        public abstract boolean isMouseOver();
    }
    private abstract static class LeftSideScreen extends SideScreen {
        public LeftSideScreen() {}
        public LeftSideScreen(int width) {
            super(width);
        }
        @Override public void render() {
            Game.game.canvas.drawRect(0, 0, width, Game.game.canvas.getHeight());
            Game.game.canvas.drawRect(width, 0, 1, Game.game.canvas.getHeight(), Color.LIGHT_GRAY);
            super.render();
        }
        @Override public int getX() {
            return 0;
        }
        @Override public boolean isMouseOver() {
            return Game.game.input.getX() <= width+1;
        }
    }
    private abstract static class RightSideScreen extends SideScreen {
        public RightSideScreen() {}
        public RightSideScreen(int width) {
            super(width);
        }
        @Override public void render() {
            Game.game.canvas.drawRect(Game.game.canvas.getWidth()-width, 0,
                    width, Game.game.canvas.getHeight());
            Game.game.canvas.drawRect(Game.game.canvas.getWidth()-width-1, 0,
                    1, Game.game.canvas.getHeight(), Color.LIGHT_GRAY);
            super.render();
        }
        @Override public int getX() {
            return Game.game.canvas.getWidth() - width;
        }
        @Override public boolean isMouseOver() {
            return Game.game.input.getX() >= Game.game.canvas.getWidth()-width-1;
        }
    }
    private class MainScreen extends RightSideScreen {
        public MainScreen() {
            addButton("Tilesets", () -> switchTo(new TilesetSelectScreen()));
            addButton("Objects", () -> switchTo(new ObjectScreen()));
            addButton("Save Map", SaveMap::new);
            addButton("Load Map", LoadMap::new);
        }
    }
    private class TilesetSelectScreen extends RightSideScreen {
        public int layer;
        public TilesetSelectScreen() {
            addButton("Back", () -> switchTo(new MainScreen()));
            addButton("Add Tileset", TilesetFetcher::new);
            for (int i = 0; i < map.layers; i++) {
                int finalI = i;
                addButton("Layer " + i, () -> layer = finalI);
            }
            getTilesets();
        }
        public void getTilesets() {
            if (buttons.size > map.layers + 2)
                buttons.removeRange(map.layers+1, buttons.size-1);
            map.tilesets.forEach(tileset -> addButton(tileset.getName(true), () -> switchTo(new TilesetScreen(tileset))));
            show();
        }
        @Override public void hide() {
            if (leftScreen instanceof TilesetScreen) {
                leftScreen.hide();
                leftScreen = null;
            }
        }
    }
    private class TilesetScreen extends LeftSideScreen {
        private Tile tile;
        private Button selectedButton;

        public TilesetScreen(Tileset tileset) {
            for (int i = 0; i < tileset.size(); i++) {
                int finalI = i;
                buttons.add(new Button(0, 0, Game.TILE_SIZE, Game.TILE_SIZE) {
                    @Override public void clicked() {
                        tile = tileset.get(finalI);
                        selectedButton = this;
                    }
                    @Override public void render() {
                        tileset.get(finalI).renderDebug(x, y);
                    }
                });
            }
        }

        @Override public void update() {
            super.update();
            if (tile != null && Gdx.input.isButtonPressed(Input.Buttons.LEFT) && !mouseIsOverAMenu()) {
                int layer = 0;
                if (rightScreen instanceof TilesetSelectScreen)
                    layer = ((TilesetSelectScreen) rightScreen).layer;
                Vector2Int pos = map.screenPosToTilePos(Game.game.input.getX(), Game.game.input.getY());
                map.setTile(pos.x, pos.y, layer, tile);
            }
        }
        @Override public void render() {
            super.render();
            Game.game.canvas.setColor(0, 0.8f, 1);
            if (selectedButton != null)
                Game.game.canvas.drawBox(selectedButton.x-1, selectedButton.y-1,
                        selectedButton.width+2, selectedButton.height+2);
//            Vector2Int pos = map.screenPosToTilePos(Game.game.input.getX(), Game.game.input.getY());
//            Game.game.canvas.drawBox(pos.x, pos.y, Game.TILE_SIZE, Game.TILE_SIZE);
            Game.game.canvas.resetColor();
        }
        @Override public void resize(int width, int height) {
            int totalTileHeight = buttons.size * (Game.TILE_SIZE + 1);
            int columns = MathUtils.ceil((float) totalTileHeight / height);
            this.width = columns * (Game.TILE_SIZE+1);
            for (int i = 0; i < buttons.size; i++) {
                Button button = buttons.get(i);
                button.x = (Game.TILE_SIZE+1) * (i% columns);
                button.y = (Game.TILE_SIZE+1) * (i/ columns);
            }
        }
    }
    private class ObjectScreen extends RightSideScreen {
        public ObjectScreen() {
            addButton("Back", () -> switchTo(new MainScreen()));
            addButton("New Object", () -> switchTo(new CreateObjectScreen()));
        }
    }
    private class CreateObjectScreen extends LeftSideScreen {
        public CreateObjectScreen() {
            for (String className : MAP_OBJECT_CLASS_NAMES) {
                addButton(className, () -> {
                    try {
                        MapObject object = (MapObject) Class.forName("com.celtican.pokemon.overworld" +
                                ".objects.nonabstract." + className).newInstance();
                        if (object.chunk != null)
                            switchTo(new EditObjectScreen(object));
                    } catch (InstantiationException | IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        Game.logWarning("Attempted to create a button which creates a non-existent \"" +
                                className + "\" map object class.");
                    }
                });
            }
        }
    }
    private class EditObjectScreen extends LeftSideScreen {
        public final MapObject object;
        public EditObjectScreen(MapObject object) {
            if (object.chunk == null) {
                this.object = null;
                return;
            }
            this.object = object;
            ArrayMap<String, Object> values = object.getValues();
            values.forEach(entry -> addValueButton(entry.key, entry.value));
        }

        private void addValueButton(String key, Object value) {
            int height = Game.game.canvas.getHeightOfSmallText("Test") + 4;
            Vector2Int bounds = Game.game.canvas.getBoundsOfSmallText(key);
            bounds.add(4, 4);
            int y;
            if (buttons.notEmpty())
                y = buttons.peek().y - height;
            else
                y = Game.game.canvas.getHeight() - height;
            buttons.add(new Button(width, y, bounds.x, height) {
                private final String valueString = value.toString();
                @Override public void clicked() {
                    new ValueSetter(key, value);
                }
                @Override public void render() {
                    super.render();
                    Game.game.canvas.drawSmallText(x+2, y+2, key + " " + valueString);
                }
            });
        }

        @Override public void render() {
            Game.game.canvas.drawBox((int)object.hitbox.x + map.renderOffset.x,
                    (int)object.hitbox.y + map.renderOffset.y, object.hitbox.width, object.hitbox.height,
                    new Color(0.5f, 1, 1, 1));
            super.render();
        }

        private class ValueSetter implements Input.TextInputListener {

            private final String key;
            private final Object value;

            public ValueSetter(String key, Object value) {
                this.key = key;
                this.value = value;
                Gdx.input.getTextInput(this, "", value.toString(), "");
            }

            @Override public void input(String text) {
                if (value instanceof String) {
                    object.setValue(key, text);
                } else if (value instanceof Integer) {
                    try {
                        object.setValue(key, Integer.parseInt(text));
                    } catch (NumberFormatException ignored) {}
                } else if (value instanceof Float) {
                    try {
                        object.setValue(key, Float.parseFloat(text));
                    } catch (NumberFormatException ignored) {}
                } else if (value instanceof Boolean) {
                    object.setValue(key, Boolean.parseBoolean(text));
                }
            }
            @Override public void canceled() {}
        }
    }
}
