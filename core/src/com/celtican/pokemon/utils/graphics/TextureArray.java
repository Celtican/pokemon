package com.celtican.pokemon.utils.graphics;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.celtican.pokemon.Game;

public class TextureArray implements Json.Serializable, Renderable {

    private Renderable renderable;
    private int curFrame;

    private TextureArray() {
        renderable = new Renderable();
    }
    public TextureArray(String fileName, String regionName) {
        setTexture(fileName, regionName);
    }
    public TextureArray(String fileName, String regionName, int startFrame) {
        setTexture(fileName, regionName, startFrame);
    }
    public static TextureArray fromString(String s) {
        TextureArray t = new TextureArray();
        t.setFromString(s);
        return t;
    }
    private void setFromString(String s) {
        String[] parts = s.split(":");
        if (parts.length != 3) {
            Game.logError("Attempting to create a texture array from string with " + parts.length + " arguments");
            return;
        }
        try {
            setTexture(parts[0], parts[1]);
            setFrame(Integer.parseInt(parts[2]));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    @Override public void write(Json json) {
        json.writeValue("TextureArray", toString());
    }
    @Override public void read(Json json, JsonValue jsonMap) {
        setFromString(jsonMap.child().asString());
    }

    public void render(float x, float y) {
        renderable.render((int)x, (int)y);
    }

    public void setTexture(String fileName, String regionName, int startFrame) {
        curFrame = startFrame;
        setTexture(fileName, regionName);
    }
    public void setTexture(String fileName, String regionName) {
        TextureAtlas atlas = Game.game.assets.get(fileName, TextureAtlas.class);
        if (atlas == null)
            renderable = new RenderablePending(fileName, regionName);
        else {
            Array<TextureAtlas.AtlasRegion> regions = atlas.findRegions(regionName);
            if (regions.isEmpty())
                renderable = new Renderable();
            else
                renderable = new RenderableActive(regions, fileName);
        }
    }

    public void setFrame(int frame) {
        renderable.setFrame(frame);
    }

    public int getWidth() {
        return renderable.getWidth();
    }
    public int getHeight() {
        return renderable.getHeight();
    }
    public int getFrame() {
        return curFrame;
    }
    public int getMaxFrames() {
        return renderable.getMaxFrames();
    }
    public boolean isLoaded() {
        return renderable.getClass() == RenderableActive.class;
    }

    public String toString() {
        return renderable.toString() + ":" + curFrame;
    }

    private class Renderable {
        public void render(int x, int y) {}
        public void setFrame(int frame) {
            curFrame = frame;
        }
        public int getWidth() {
            return 0;
        }
        public int getHeight() {
            return 0;
        }
        public int getMaxFrames() {
            return 0;
        }
        public String toString() {
            return "";
        }
    }
    private class RenderablePending extends Renderable {

        private final String fileName, regionName;

        public RenderablePending(String fileName, String regionName) {
            this.fileName = fileName;
            this.regionName = regionName;
        }

        @Override public void render(int x, int y) {
            TextureAtlas atlas = Game.game.assets.get(fileName, TextureAtlas.class);
            if (atlas == null)
                return;
            Array<TextureAtlas.AtlasRegion> regions = atlas.findRegions(regionName);
            if (regions.isEmpty())
                renderable = new Renderable();
            else
                renderable = new RenderableActive(regions, fileName);
        }
        @Override public String toString() {
            return fileName + ":" + regionName;
        }
    }
    private class RenderableActive extends Renderable {

        private final Array<TextureAtlas.AtlasRegion> regions;
        private final String fileName;

        public RenderableActive(Array<TextureAtlas.AtlasRegion> regions, String fileName) {
            this.regions = regions;
            this.fileName = fileName;
            setFrame(curFrame);
        }

        @Override public void render(int x, int y) {
            Game.game.canvas.draw(regions.get(curFrame), x, y);
        }
        @Override public void setFrame(int frame) {
            if (frame < 0)
                curFrame = 0;
            else
                curFrame = frame % getMaxFrames();
        }
        @Override public int getWidth() {
            return regions.get(curFrame).getRegionWidth();
        }
        @Override public int getHeight() {
            return regions.get(curFrame).getRegionHeight();
        }
        @Override public int getMaxFrames() {
            return regions.size;
        }
        @Override public String toString() {
            return fileName + ":" + regions.get(0).name;
        }
    }
}
