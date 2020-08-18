package com.celtican.pokemon.utils.graphics;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.celtican.pokemon.Game;

public class TextureArray {

    private Renderable renderable;
    private int curFrame;

    public TextureArray(String fileName, String regionName) {
        setTexture(fileName, regionName);
    }
    public TextureArray(String fileName, String regionName, int startFrame) {
        setTexture(fileName, regionName, startFrame);
    }

    public void render(int x, int y) {
        renderable.render(x, y);
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
                renderable = new RenderableActive(regions);
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

        public RenderableActive(Array<TextureAtlas.AtlasRegion> regions) {
            this.regions = regions;
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

    }
}
