package com.celtican.pokemon.utils.graphics;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.celtican.pokemon.Game;

public class Texture {

    private Renderable renderable;

    public Texture(String fileName) {
        setTexture(fileName);
    }
    public Texture(String fileName, String regionName) {
        setTexture(fileName, regionName);
    }
    public Texture(String fileName, String regionName, int index) {
        setTexture(fileName, regionName, index);
    }

    public void render(int x, int y) {
        renderable.render(x, y);
    }

    public void setTexture(String fileName) {
        com.badlogic.gdx.graphics.Texture texture = Game.game.assets.get(fileName, com.badlogic.gdx.graphics.Texture.class);
        if (texture != null)
            renderable = new RenderableTexture(texture);
        else
            renderable = new RenderableTexturePending(fileName);
    }
    public void setTexture(String fileName, String regionName) {
        TextureAtlas atlas = Game.game.assets.get(fileName, TextureAtlas.class);
        if (atlas == null)
            renderable = new RenderablePendingAtlasRegion(fileName, regionName);
        else {
            TextureAtlas.AtlasRegion region = atlas.findRegion(regionName);
            if (region == null)
                renderable = new Renderable();
            else
                renderable = new RenderableAtlasRegion(region);
        }
    }
    public void setTexture(String fileName, String regionName, int index) {
        TextureAtlas atlas = Game.game.assets.get(fileName, TextureAtlas.class);
        if (atlas == null)
            renderable = new RenderablePendingAtlasRegionIndex(fileName, regionName, index);
        else {
            TextureAtlas.AtlasRegion region = atlas.findRegion(regionName, index);
            if (region == null)
                renderable = new Renderable();
            else
                renderable = new RenderableAtlasRegion(region);
        }
    }

    public int getWidth() {
        return renderable.getWidth();
    }
    public int getHeight() {
        return renderable.getHeight();
    }

    public boolean isLoaded() {
        return renderable.getClass() == RenderableAtlasRegion.class ||
                renderable.getClass() == RenderableTexture.class;
    }

    private static class Renderable {
        public void render(int x, int y) {}
        public int getWidth() {
            return 0;
        }
        public int getHeight() {
            return 0;
        }
    }
    private static class RenderableAtlasRegion extends Renderable {

        private final TextureAtlas.AtlasRegion region;

        public RenderableAtlasRegion(TextureAtlas.AtlasRegion region) {
            this.region = region;
        }

        @Override public void render(int x, int y) {
            Game.game.canvas.draw(region, x, y);
        }
        @Override public int getWidth() {
            return region.getRegionWidth();
        }
        @Override public int getHeight() {
            return region.getRegionHeight();
        }
    }
    private class RenderablePendingAtlasRegion extends Renderable {

        private final String fileName, regionName;

        public RenderablePendingAtlasRegion(String fileName, String regionName) {
            this.fileName = fileName;
            this.regionName = regionName;
        }

        @Override public void render(int x, int y) {
            TextureAtlas atlas = Game.game.assets.get(fileName, TextureAtlas.class);
            if (atlas == null)
                return;
            TextureAtlas.AtlasRegion region = atlas.findRegion(regionName);
            if (region == null)
                renderable = new Renderable();
            else
                renderable = new RenderableAtlasRegion(region);
        }
    }
    private class RenderablePendingAtlasRegionIndex extends Renderable {
        private final String fileName, regionName;
        private final int index;

        public RenderablePendingAtlasRegionIndex(String fileName, String regionName, int index) {
            this.fileName = fileName;
            this.regionName = regionName;
            this.index = index;
        }

        @Override public void render(int x, int y) {
            TextureAtlas atlas = Game.game.assets.get(fileName, TextureAtlas.class);
            if (atlas == null)
                return;
            TextureAtlas.AtlasRegion region = atlas.findRegion(regionName, index);
            if (region == null)
                renderable = new Renderable();
            else
                renderable = new RenderableAtlasRegion(region);
        }
    }
    private static class RenderableTexture extends Renderable {
        private final com.badlogic.gdx.graphics.Texture texture;

        public RenderableTexture(com.badlogic.gdx.graphics.Texture texture) {
            this.texture = texture;
        }

        @Override public void render(int x, int y) {
            Game.game.canvas.draw(texture, x, y);
        }
        @Override public int getWidth() {
            return texture.getWidth();
        }
        @Override public int getHeight() {
            return texture.getHeight();
        }
    }
    private class RenderableTexturePending extends Renderable {

        private final String fileName;

        public RenderableTexturePending(String fileName) {
            this.fileName = fileName;
        }

        @Override public void render(int x, int y) {
            com.badlogic.gdx.graphics.Texture texture = Game.game.assets.get(fileName, com.badlogic.gdx.graphics.Texture.class);
            if (texture != null)
                renderable = new RenderableTexture(texture);
        }
    }
}
