package com.celtican.pokemon.utils.graphics;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.celtican.pokemon.Game;
import com.celtican.pokemon.overworld.objects.nonabstract.Character;

public class CharacterTexture implements Renderable {

    private Renderable renderable;

    public CharacterTexture() {
        renderable = new Renderable();
    }
    public CharacterTexture(String s) {
        setFromString(s);
    }
    public CharacterTexture(String fileName, String regionName) {
        setTexture(fileName, regionName);
    }
    public static CharacterTexture fromString(String s) {
        return new CharacterTexture(s);
    }
    public void setFromString(String s) {
        String[] parts = s.split(":");
        if (parts.length == 1)
            setTexture(s);
        else if (parts.length == 2)
            setTexture(parts[0], parts[1]);
        else
            Game.logError("Attempting to create a character texture from string with " + parts.length + " parts.");
    }

    @Override public void render(int x, int y) {
        renderable.render(x, y);
    }

    public void setTexture(String fileName) {
        setTexture(fileName, "S0");
    }
    public void setTexture(String fileName, String regionName) {
        TextureAtlas atlas = Game.game.assets.get(fileName, TextureAtlas.class);
        if (atlas == null)
            renderable = new RenderablePending(fileName, regionName);
        else if (atlas.getRegions().isEmpty())
            renderable = new Renderable(fileName, regionName);
        else
            renderable = new RenderableActive(atlas, fileName, regionName);
    }
    public void setFrame(String frameName) {
        renderable.setFrame(frameName);
    }
    public void setFrame(Character.Direction dir) {
        setFrame(dir, Character.MovementStatus.STILL);
    }
    public void setFrame(Character.Direction dir, Character.MovementStatus movementStatus) {
        if (renderable instanceof RenderableActive) {
            ((RenderableActive) renderable).setFrame(dir, movementStatus);
            return;
        }
        setFrame(dir.asChar() + (movementStatus == Character.MovementStatus.RUNNING ? "R0" : "0"));
    }

    public int getWidth() {
        return renderable.getWidth();
    }
    public int getHeight() {
        return renderable.getHeight();
    }
    public String getFrame() {
        return renderable.getFrame();
    }
    public boolean isLoaded() {
        return renderable instanceof RenderableActive;
    }

    @Override public String toString() {
        return renderable.toString();
    }

    private static class Renderable {
        private final String fileName;
        private String regionName;

        public Renderable() {
            this("", "S");
        }
        public Renderable(String fileName) {
            this(fileName, "S");
        }
        public Renderable(String fileName, String regionName) {
            this.fileName = fileName;
            this.regionName = regionName;
        }

        public void render(int x, int y) {}
        public void setFrame(String s) {
            regionName = s;
        }
        public String getFrame() {return "S";}
        public int getWidth() {
            return 0;
        }
        public int getHeight() {
            return 0;
        }
        public String toString() {
            return fileName + ":S";
        }
    }
    private class RenderablePending extends Renderable {
        private final String fileName;
        private String regionName;
        public RenderablePending(String fileName) {
            this.fileName = fileName;
            regionName = "S";
        }
        public RenderablePending(String fileName, String regionName) {
            this.fileName = fileName;
            this.regionName = regionName;
        }

        @Override public void render(int x, int y) {
            TextureAtlas atlas = Game.game.assets.get(fileName, TextureAtlas.class);
            if (atlas == null)
                return;
            renderable = new RenderableActive(atlas, fileName, regionName);
        }
        @Override public void setFrame(String s) {
            regionName = s;
        }
        @Override public String getFrame() {
            return regionName;
        }
        @Override public String toString() {
            return fileName + ":" + regionName;
        }
    }
    private final static int FRAMES_PER_WALK = Game.TARGET_FRAME_RATE/4;
    private final static int FRAMES_PER_RUN = Game.TARGET_FRAME_RATE/6;
    private static class RenderableActive extends Renderable {

        private final TextureAtlas atlas;
        private final String fileName;
        private TextureAtlas.AtlasRegion region;
        private Character.Direction dir;
        private Character.MovementStatus move;
        private int frameOffset;
        private int moveStage;

        public RenderableActive(TextureAtlas atlas, String fileName, String regionName) {
            this.atlas = atlas;
            this.fileName = fileName;
            setFrame(regionName);
            if (region == null) {
                region = atlas.findRegion("S0");
                if (region == null)
                    region = atlas.getRegions().first();
            }
        }

        public void setFrame(Character.Direction dir, Character.MovementStatus movementStatus) {
            if (this.dir == dir && this.move == movementStatus)
                return;
            if (movementStatus == Character.MovementStatus.STILL) {
                setFrame(dir.asChar() + "0");
            }
            this.dir = dir;
            this.move = movementStatus;
            frameOffset = Game.game.frame;
            moveStage = 0;
        }

        @Override public void render(int x, int y) {
            if (frameOffset == -1) {
                // if simply rendering a basic still frame, as such if an npc is standing still or has a custom frame
                Game.game.canvas.draw(region, x, y);
                return;
            }
            // otherwise we're walking/running and need to update the frame periodically
            if (move == Character.MovementStatus.WALKING && ((Game.game.frame - frameOffset)%FRAMES_PER_WALK) == 0 ||
                    move == Character.MovementStatus.RUNNING && ((Game.game.frame - frameOffset)%FRAMES_PER_RUN) == 0) {
                // if it's a tick where we need to update the frame
                // super sorry for using "frame" and "tick" in weird contexts
                if (++moveStage%4 == 0) moveStage = 0;
                String frame = java.lang.Character.toString(dir.asChar());
                if (move == Character.MovementStatus.RUNNING)
                    frame += 'R';
                switch (moveStage) {
                    case 0:
                    case 2: frame += '0'; break;
                    case 1: frame += '1'; break;
                    case 3: frame += '2'; break;
                }
                TextureAtlas.AtlasRegion region =  atlas.findRegion(frame);
                if (region != null)
                    this.region = region;
            }
            Game.game.canvas.draw(region, x, y);
        }
        @Override public void setFrame(String s) {
            TextureAtlas.AtlasRegion region =  atlas.findRegion(s);
            if (region != null) {
                this.region = region;
                frameOffset = -1;
            }
        }
        @Override public String getFrame() {
            return region.name;
        }
        @Override public int getWidth() {
            return region.getRegionWidth();
        }
        @Override public int getHeight() {
            return region.getRegionHeight();
        }
        @Override public String toString() {
            return fileName + ":" + region.name;
        }
    }
}