package com.celtican.pokemon.utils.graphics;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.celtican.pokemon.Game;

public class AnimatedTexture implements /*Json.Serializable, */Renderable {
    private Renderable renderable;

    private float millisPerFrame;
    private float curMillis = 0;

    public boolean isPaused = false;

    private AnimatedTexture() {
        renderable = new Renderable();
    }
    public AnimatedTexture(String fileName, String regionName, float millisPerFrame) {
        setSpeed(millisPerFrame);
        setTexture(fileName, regionName);
    }
    public static AnimatedTexture fromString(String s) {
        AnimatedTexture t = new AnimatedTexture();
        t.setFromString(s);
        return t;
    }
    private void setFromString(String s) {
        String[] parts = s.split(":");
        if (parts.length != 3) {
            Game.logError("Attempting to create an animated texture from string with " + parts.length + " arguments");
            return;
        }
        try {
            millisPerFrame = Float.parseFloat(parts[2]);
            setTexture(parts[0], parts[1]);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

//    @Override public void write(Json json) {
//        json.writeValue("AnimatedTexture", toString());
//    }
//    @Override public void read(Json json, JsonValue jsonMap) {
//        setFromString(jsonMap.child().asString());
//    }B

    public void render(int x, int y) {
        renderable.render(x, y);
    }
    public void render(int x, int y, int scale) {
        renderable.render(x, y, scale);
    }

    public void setTexture(String fileName, String regionName) {
        TextureAtlas atlas = Game.game.assets.get(fileName, TextureAtlas.class);
        if (atlas == null)
            renderable = new RenderablePending(fileName, regionName);
        else {
            renderable = new RenderableActive(atlas.findRegions(regionName), fileName);
        }
    }
    public void setSpeed(float millisPerFrame) {
        this.millisPerFrame = millisPerFrame;
    }

    public float getSpeed() {
        return millisPerFrame;
    }
    public int getCurFrame() {
        return renderable.getCurFrame();
    }
    public int getNumFrames() {
        return renderable.getNumFrames();
    }

    public int getWidth() {
        return renderable.getWidth();
    }
    public int getHeight() {
        return renderable.getHeight();
    }
    public boolean isLoaded() {
        return renderable.getClass() == RenderableActive.class;
    }

    public String toString() {
        return renderable.toString() + ":" + millisPerFrame;
    }

    private static class Renderable {
        public void render(int x, int y) {}
        public void render(int x, int y, int scale) {}
        public int getWidth() {
            return 0;
        }
        public int getHeight() {
            return 0;
        }
        public int getCurFrame() {
            return 0;
        }
        public int getNumFrames() {
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
        private TextureAtlas.AtlasRegion region;
        private int curFrame = 0;

        public RenderableActive(Array<TextureAtlas.AtlasRegion> regions, String fileName) {
            this.regions = regions;
            this.fileName = fileName;
            advanceFrame();
            if (region == null) region = regions.first();
        }

        private void advanceFrame() {
            if (isPaused) return;
            curMillis += Game.MILLIS_PER_FRAME;
            if (curMillis >= millisPerFrame) {
                curFrame = (curFrame + (int)(curMillis/millisPerFrame))%getNumFrames();
                curMillis %= millisPerFrame;
                region = regions.get(curFrame);
            }
        }

        @Override public void render(int x, int y) {
            Game.game.canvas.draw(region, x, y);
            advanceFrame();
        }
        @Override public void render(int x, int y, int scale) {
            Game.game.canvas.draw(region, x, y, scale);
            advanceFrame();
        }
        @Override public int getWidth() {
            return region.getRegionWidth();
        }
        @Override public int getHeight() {
            return region.getRegionHeight();
        }
        @Override public int getCurFrame() {
            return curFrame;
        }
        @Override public int getNumFrames() {
            return regions.size;
        }
        @Override public String toString() {
            return fileName + ":" + region.name;
        }
    }
}
