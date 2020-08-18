package com.celtican.pokemon.screens;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.celtican.pokemon.Game;
import com.celtican.pokemon.utils.Enums;
import com.celtican.pokemon.utils.graphics.TextureArray;

public class LoadingScreen extends Screen {

    private final TextureArray loadingAnimation;

    private int stage = 0;

    public LoadingScreen() {
        loadingAnimation = new TextureArray("spritesheets/loading.atlas", "loading");
        Game.game.assets.preLoad("misc/pixel.png", Texture.class);
    }

    @Override public void show() {
        Game.game.canvas.setClearColor(0, 0, 0, 1);
    }
    @Override public void hide() {
        Game.game.canvas.resetClearColor();
    }
    @Override public void render() {
        int x = Game.game.canvas.getWidth()/2 - loadingAnimation.getWidth()/2;
        int y = Game.game.canvas.getHeight()/2 - loadingAnimation.getHeight()/2;
        int frame;
        switch (Game.game.frame/4 % 20) {
            case 1:
            case 3:
            case 14:
                x += 1;
                frame = 3;
                break;
            case 2:
                x += 2;
                frame = 4;
                break;
            case 4:
            case 10:
            case 12:
                x += -1;
                frame = 1;
                break;
            case 11:
                x += -2;
                frame = 0;
                break;
            default:
                frame = 2;
        }
        loadingAnimation.setFrame(frame);
        loadingAnimation.render(x, y);

        Game.game.canvas.drawRect(Game.game.canvas.getWidth()/2 - 5, Game.game.canvas.getHeight()/2 - 6, 11, 1, Color.DARK_GRAY);
        float progress;
        switch (stage) {
            case 0:
                progress = 0;
                break;
            case 1:
                progress = Game.game.assets.getProgress();
                break;
            default:
                progress = 1;
        }
        Game.game.canvas.drawRect(Game.game.canvas.getWidth()/2 - 5, Game.game.canvas.getHeight()/2 - 6,
                MathUtils.round(progress*11), 1, Color.WHITE);
    }
    @Override public void update() {
        switch (stage) {
            case 0:
                if (Game.game.assets.heavyUpdate()) {
                    Game.game.canvas.setup();

                    Game.game.assets.preLoad("sfx/battleExpRise.ogg", Sound.class);
                    Game.game.assets.preLoad("sfx/battleExpFull.ogg", Sound.class);
                    Game.game.assets.preLoad("sfx/battleBallClick.ogg", Sound.class);
                    Game.game.assets.preLoad("sfx/battleBallShake.ogg", Sound.class);
                    Game.game.assets.preLoad("sfx/battleBallEscape.ogg", Sound.class);
                    Game.game.assets.preLoad("sfx/battleFlee.ogg", Sound.class);
                    Game.game.assets.preLoad("sfx/battleDamage.ogg", Sound.class);
                    Game.game.assets.preLoad("sfx/battleDamageSuper.ogg", Sound.class);
                    Game.game.assets.preLoad("sfx/battleDamageWeak.ogg", Sound.class);
                    Game.game.assets.preLoad("sfx/guiCursor.ogg", Sound.class);
                    Game.game.assets.preLoad("sfx/guiSelect.ogg", Sound.class);

                    Game.game.assets.preLoad("bgm/evolutionStart.ogg", Music.class);
                    Game.game.assets.preLoad("bgm/evolution.ogg", Music.class);
                    Game.game.assets.preLoad("bgm/evolutionFanfare.ogg", Music.class);
                    Game.game.assets.preLoad("bgm/route29.ogg", Music.class);
                    Game.game.assets.preLoad("bgm/wildBattle.ogg", Music.class);
                    Game.game.assets.preLoad("bgm/trainerBattle.ogg", Music.class);

                    for (int i = 1; i <= 20; i++) {
                        Game.game.assets.preLoad("sfx/cries/" + i + ".ogg", Sound.class);
                        Game.game.assets.preLoad("spritesheets/pokemon/" + i + ".atlas", TextureAtlas.class);
                    }
                    Game.game.assets.preLoad("spritesheets/overworldItems.atlas", TextureAtlas.class);
//                    game.assets.load("spritesheets/watch.atlas", TextureAtlas.class);
                    Game.game.assets.preLoad("spritesheets/forest.atlas", TextureAtlas.class);
//                    game.assets.load("spritesheets/jungle.atlas", TextureAtlas.class);
//                    game.assets.load("spritesheets/testTiles.atlas", TextureAtlas.class);
                    Game.game.assets.preLoad("spritesheets/indoor.atlas", TextureAtlas.class);
                    Game.game.assets.preLoad("spritesheets/overworld.atlas", TextureAtlas.class);
                    Game.game.assets.preLoad("spritesheets/juniper.atlas", TextureAtlas.class);
                    Game.game.assets.preLoad("spritesheets/player.atlas", TextureAtlas.class);
                    Game.game.assets.preLoad("spritesheets/nurse.atlas", TextureAtlas.class);
                    Game.game.assets.preLoad("spritesheets/battle.atlas", TextureAtlas.class);

                    Game.game.assets.preLoad("misc/fontSmall.fnt", BitmapFont.class);

                    stage++;
                }
                break;
            case 1:
                if (Game.game.assets.heavyUpdate()) {
                    Game.game.data.addSpecies("Nullomon", Enums.GenderRatio.GENDERLESS, Enums.Type.NORMAL, Enums.Type.NORMAL, Enums.EggGroup.UNDISCOVERED, Enums.EggGroup.UNDISCOVERED, Enums.ExpGrowth.SLOW,
                            0, 0, 0, 0, 20, 20, 20, 20, 20, 20, 1, 1, 1, 0, 0, 0, 0, 0, 0);
                    Game.game.data.addSpecies("Bulbasaur", Enums.GenderRatio.FEMALE_IS_RARE, Enums.Type.GRASS, Enums.Type.POISON, Enums.EggGroup.MONSTER, Enums.EggGroup.GRASS, Enums.ExpGrowth.MEDIUM_SLOW,
                            65, 65, 34, 34, 45, 49, 49, 65, 65, 45, 45, 50, 46, 0, 0, 0, 1, 0, 0);
                    Game.game.switchScreens(new OverworldScreen());
                    stage++;
                }
        }
    }
}
