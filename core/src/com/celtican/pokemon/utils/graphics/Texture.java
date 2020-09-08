package com.celtican.pokemon.utils.graphics;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.celtican.pokemon.Game;

public class Texture implements /*Json.Serializable, */Renderable {

    private Renderable renderable;

    public Texture() {
        renderable = new Renderable();
    }
    public Texture(String fileName) {
        setTexture(fileName);
    }
    public Texture(String fileName, String regionName) {
        setTexture(fileName, regionName);
    }
    public Texture(String fileName, String regionName, int index) {
        setTexture(fileName, regionName, index);
    }
    public Texture(String fileName, TextureAtlas.AtlasRegion region) {
        renderable = new RenderableAtlasRegion(region, fileName);
    }
    public static Texture fromString(String s) {
        Texture t = new Texture();
        t.setFromString(s);
        return t;
    }
    public void setFromString(String s) {
        String[] parts = s.split(":");
        switch (parts.length) {
            case 1:
                setTexture(parts[0]);
                break;
            case 2:
                setTexture(parts[0], parts[1]);
                break;
            case 3:
                try {
                    setTexture(parts[0], parts[1], Integer.parseInt(parts[2]));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                break;
            default:
                Game.logError("Attempting to create a texture from string with " + parts.length + " arguments");
                break;
        }
    }

//    @Override public void write(Json json) {
//        json.writeValue("Texture", toString());
//    }
//    @Override public void read(Json json, JsonValue jsonMap) {
//        setFromString(jsonMap.child().asString());
//    }

    @Override public void render(int x, int y) {
        renderable.render(x, y);
    }

    public void setTexture(String fileName) {
        com.badlogic.gdx.graphics.Texture texture = Game.game.assets.get(fileName, com.badlogic.gdx.graphics.Texture.class);
        if (texture != null)
            renderable = new RenderableTexture(texture, fileName);
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
                renderable = new RenderableAtlasRegion(region, fileName);
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
                renderable = new RenderableAtlasRegion(region, fileName);
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

    public String toString() {
        return renderable.toString();
    }
    public String toString(boolean omitFileName) {
        return renderable.toString(omitFileName);
    }

    private static class Renderable {
        public void render(int x, int y) {}
        public int getWidth() {
            return 0;
        }
        public int getHeight() {
            return 0;
        }
        public String toString() {
            return "";
        }
        public String toString(boolean omitFileName) {
            return "";
        }
    }
    private static class RenderableAtlasRegion extends Renderable {

        private final TextureAtlas.AtlasRegion region;
        private final String fileName;

        public RenderableAtlasRegion(TextureAtlas.AtlasRegion region, String fileName) {
            this.region = region;
            this.fileName = fileName;
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
        @Override public String toString() {
            String s = fileName + ":" + region.name;
            if (region.index != -1)
                s += ":" + region.index;
            return s;
        }
        @Override public String toString(boolean omitFileName) {
            if (omitFileName)
                return region.index != -1 ? region.name + ":" + region.index : region.name;
            else
                return toString();
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
                renderable = new RenderableAtlasRegion(region, fileName);
        }
        @Override public String toString() {
            return fileName + ":" + regionName;
        }
        @Override public String toString(boolean omitFileName) {
            if (omitFileName)
                return regionName;
            else
                return toString();
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
                renderable = new RenderableAtlasRegion(region, fileName);
        }

        @Override public String toString() {
            return fileName + ":" + regionName + ":" + index;
        }
        @Override public String toString(boolean omitFileName) {
            if (omitFileName)
                return regionName + ":" + index;
            else
                return toString();
        }
    }
    private static class RenderableTexture extends Renderable {
        private final com.badlogic.gdx.graphics.Texture texture;
        private final String fileLocation;

        public RenderableTexture(com.badlogic.gdx.graphics.Texture texture, String fileLocation) {
            this.texture = texture;
            this.fileLocation = fileLocation;
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
        @Override public String toString() {
            return fileLocation;
        }
        @Override public String toString(boolean omitFileName) {
            if (omitFileName)
                return "";
            else
                return toString();
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
                renderable = new RenderableTexture(texture, fileName);
        }
        @Override public String toString() {
            return fileName;
        }
        @Override public String toString(boolean omitFileName) {
            if (omitFileName)
                return "";
            else
                return toString();
        }
    }
}
