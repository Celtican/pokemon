package com.celtican.pokemon.utils.graphics;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.celtican.pokemon.Game;

public class AnimatedTexture {
    private Renderable renderable;

    private float millisPerSecond;
    private float curMillis = 0;

    public AnimatedTexture(String fileName, String regionName, float millisPerSecond) {
        setTexture(fileName, regionName);
        setSpeed(millisPerSecond);
    }

    public void render(int x, int y) {
        renderable.render(x, y);
    }

    public void setTexture(String fileName, String regionName) {
        TextureAtlas atlas = Game.game.assets.get(fileName, TextureAtlas.class);
        if (atlas == null)
            renderable = new RenderablePending(fileName, regionName);
    }
    public void setSpeed(float millisPerSecond) {
        this.millisPerSecond = millisPerSecond;
    }

    public float getSpeed() {
        return millisPerSecond;
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

    private static class Renderable {
        public void render(int x, int y) {}
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
                renderable = new RenderableActive(regions);
        }
    }
    private class RenderableActive extends Renderable {
        private final Array<TextureAtlas.AtlasRegion> regions;
        private TextureAtlas.AtlasRegion region;
        private int curFrame = 0;

        public RenderableActive(Array<TextureAtlas.AtlasRegion> regions) {
            this.regions = regions;
            advanceFrame();
        }

        private void advanceFrame() {
            curMillis += Game.MILLIS_PER_FRAME;
            curFrame = (int)(curMillis / millisPerSecond) % getNumFrames();
            region = regions.get(curFrame);
        }

        @Override public void render(int x, int y) {
            Game.game.canvas.draw(region, x, y);

            curMillis += Game.MILLIS_PER_FRAME;
            curFrame = (int)(curMillis / millisPerSecond) % getNumFrames();
            region = regions.get(curFrame);
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
    }
}
