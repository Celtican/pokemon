package com.celtican.pokemon.screens;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.celtican.pokemon.Game;
import com.celtican.pokemon.utils.data.Ability;
import com.celtican.pokemon.utils.data.Move;
import com.celtican.pokemon.utils.data.PartyPokemon;
import com.celtican.pokemon.utils.data.Pokemon.EggGroup;
import com.celtican.pokemon.utils.data.Pokemon.StatusCondition;
import com.celtican.pokemon.utils.graphics.TextureArray;

import static com.celtican.pokemon.utils.data.Pokemon.ContestType.*;
import static com.celtican.pokemon.utils.data.Pokemon.ExpGrowth.ERRATIC;
import static com.celtican.pokemon.utils.data.Pokemon.ExpGrowth.MEDIUM_SLOW;
import static com.celtican.pokemon.utils.data.Pokemon.GenderRatio.FEMALE_IS_RARE;
import static com.celtican.pokemon.utils.data.Pokemon.GenderRatio.GENDERLESS;
import static com.celtican.pokemon.utils.data.Pokemon.MoveCategory.PHYSICAL;
import static com.celtican.pokemon.utils.data.Pokemon.MoveCategory.SPECIAL;
import static com.celtican.pokemon.utils.data.Pokemon.MoveTargets.ADJACENT;
import static com.celtican.pokemon.utils.data.Pokemon.Type.*;

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
                    startLoadAssets();
                    stage++;
                }
                break;
            case 1:
                if (Game.game.assets.heavyUpdate()) {
                    addSpecies();
                    addMoves();
                    addAbilities();
                    addItems();
                    stage++;
                }
                break;
            case 2:
                finalizeLoad();
                stage++;
                break;
        }
    }

    private void startLoadAssets() {
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
    }
    private void addSpecies() {
        Game.game.data.addSpecies("Nullomon", GENDERLESS, NORMAL, NORMAL, EggGroup.UNDISCOVERED, EggGroup.UNDISCOVERED, ERRATIC,
                0, 0, 0, 0, 20, 20, 20, 20, 20, 20, 1, 1, 1, 0, 0, 0, 0, 0, 0);
        Game.game.data.addSpecies("Bulbasaur", FEMALE_IS_RARE, GRASS, POISON, EggGroup.MONSTER, EggGroup.GRASS, MEDIUM_SLOW,
                65, 65, 34, 34, 45, 49, 49, 65, 65, 45, 45, 50, 46, 0, 0, 0, 1, 0, 0);
        Game.game.data.addSpecies("Ivysaur", FEMALE_IS_RARE, GRASS, POISON, EggGroup.MONSTER, EggGroup.GRASS, MEDIUM_SLOW,
                65, 65, 34, 34, 60, 62, 63, 80, 80, 60, 45, 50, 142, 0, 0, 0, 1, 0, 0);
        Game.game.data.addSpecies("Venusaur", FEMALE_IS_RARE, GRASS, POISON, EggGroup.MONSTER, EggGroup.GRASS, MEDIUM_SLOW,
                65, 65, 34, 34, 80, 82, 83, 100, 100, 80, 45, 50, 236, 0, 0, 0, 2, 1, 0);
        Game.game.data.addSpecies("Charmander", FEMALE_IS_RARE, FIRE, NONE, EggGroup.MONSTER, EggGroup.DRAGON, MEDIUM_SLOW,
                66, 66, 94, 94, 39, 52, 43, 60, 50, 65, 45, 50, 62, 0, 0, 0, 0, 0, 1);
        Game.game.data.addSpecies("Charmeleon", FEMALE_IS_RARE, FIRE, NONE, EggGroup.MONSTER, EggGroup.DRAGON, MEDIUM_SLOW,
                66, 66, 94, 94, 58, 64, 58, 80, 65, 80, 45, 50, 142, 0, 0, 0, 1, 0, 1);
        Game.game.data.addSpecies("Charizard", FEMALE_IS_RARE, FIRE, FLYING, EggGroup.MONSTER, EggGroup.DRAGON, MEDIUM_SLOW,
                66, 66, 94, 94, 78, 84, 78, 109, 85, 100, 45, 50, 240, 0, 0, 0, 3, 0, 0);
        Game.game.data.addSpecies("Squirtle", FEMALE_IS_RARE, WATER, NONE, EggGroup.MONSTER, EggGroup.WATER1, MEDIUM_SLOW,
                67, 67, 44, 44, 44, 48, 65, 50, 64, 43, 45, 50, 63, 0, 0, 1, 0, 0, 0);
        Game.game.data.addSpecies("Wartortle", FEMALE_IS_RARE, WATER, NONE, EggGroup.MONSTER, EggGroup.WATER1, MEDIUM_SLOW,
                67, 67, 44, 44, 59, 63, 80, 65, 80, 58, 45, 50, 142, 0, 0, 1, 0, 1, 0);
        Game.game.data.addSpecies("Blastoise", FEMALE_IS_RARE, WATER, NONE, EggGroup.MONSTER, EggGroup.WATER1, MEDIUM_SLOW,
                67, 67, 44, 44, 79, 83, 100,85, 105, 78, 45, 50, 239, 0, 0, 0, 0, 3, 0);
    }
    private void addMoves() {
        Move struggle = new Move(0, "Struggle", NONE, PHYSICAL, TOUGH, ADJACENT, -1, 50, -1, "protect, mirror", null);
        Game.game.data.addMove(struggle);
        Game.game.data.addMove(new Move(1, "Pound", NORMAL, PHYSICAL, TOUGH, ADJACENT, 7, 40, 100, "contact, protect, mirror", null));
        Game.game.data.addMove(new Move(2, "Karate Chop", FIGHTING, PHYSICAL, TOUGH, ADJACENT, 5, 50, 100, "contact, protect, mirror, crit", null));
        Game.game.data.addMove(new Move(3, "Double Slap", NORMAL, PHYSICAL, CUTE, ADJACENT, 2, 15, 85, "contact, protect, mirror, multi", null));
        Game.game.data.addMove(new Move(4, "Comet Punch", NORMAL, PHYSICAL, TOUGH, ADJACENT, 3, 18, 85, "contact, mirror, punch, multi", null));
        Game.game.data.addMove(new Move(5, "Mega Punch", NORMAL, PHYSICAL, TOUGH, ADJACENT, 4, 80, 85, "contact, protect, mirror, punch", null));
        Game.game.data.addMove(new Move(6, "Pay Day", NORMAL, PHYSICAL, CLEVER, ADJACENT, 4, 40, 100, "protect, mirror", null)); // todo make Pay Day add pokedollars to funds
        Game.game.data.addMove(new Move(7, "Fire Punch", FIRE, PHYSICAL, TOUGH, ADJACENT, 3 ,75, 100, "contact, protect, mirror, punch", new Move.EffectStatusCondition(10, StatusCondition.BURN)));
        Game.game.data.addMove(new Move(8, "Ice Punch", FIRE, PHYSICAL, BEAUTIFUL, ADJACENT, 3, 75, 100, "contact, protect, mirror, punch", new Move.EffectStatusCondition(10, StatusCondition.FREEZE)));
        Game.game.data.addMove(new Move(9, "Thunder Punch", ELECTRIC, PHYSICAL, COOL, ADJACENT, 3, 75, 100, "contact, protect, mirror, punch", new Move.EffectStatusCondition(10, StatusCondition.PARALYSIS)));
        Game.game.data.addMove(new Move(10, "Scratch", NORMAL, PHYSICAL, TOUGH, ADJACENT, 7, 40, 100, "contact, protect, mirror", null));
        Game.game.data.addMove(new Move(11, "Vise Grip", NORMAL, PHYSICAL, TOUGH, ADJACENT, 6, 55, 100, "contact, protect, mirror", null));
        Game.game.data.addMove(new Move(12, "Guillotine", NORMAL, PHYSICAL, COOL, ADJACENT, 1, -1, 30, "contact, protect, mirror", null));
        Game.game.data.addMove(struggle); // Razor Wind
        Game.game.data.addMove(struggle); // Swords Dance
        Game.game.data.addMove(struggle); // Cut
        Game.game.data.addMove(struggle); // Gust*
        Game.game.data.addMove(struggle); //         Wing Attack
        Game.game.data.addMove(struggle); //         Whirlwind
        Game.game.data.addMove(struggle); // Fly
        Game.game.data.addMove(struggle); //         Bind
        Game.game.data.addMove(struggle); // Slam
        Game.game.data.addMove(struggle); // Vine Whip
        Game.game.data.addMove(struggle); // Stomp
        Game.game.data.addMove(struggle); // Double Kick
        Game.game.data.addMove(struggle); // Mega Kick
        Game.game.data.addMove(struggle); // Jump Kick
        Game.game.data.addMove(struggle); // Rolling Kick
        Game.game.data.addMove(struggle); // Sand Attack*
        Game.game.data.addMove(struggle); // Headbutt
        Game.game.data.addMove(struggle); // Horn Attack
        Game.game.data.addMove(struggle); // Fury Attack
        Game.game.data.addMove(struggle); // Horn Drill
        Game.game.data.addMove(struggle); // Tackle
        Game.game.data.addMove(struggle); // Body Slam
        Game.game.data.addMove(struggle); // Wrap
        Game.game.data.addMove(struggle); // Take Down
        Game.game.data.addMove(struggle); // Thrash
        Game.game.data.addMove(struggle); // Double-Edge
        Game.game.data.addMove(struggle); // Tail Whip
        Game.game.data.addMove(struggle); // Poison Sting
        Game.game.data.addMove(struggle); // Twineedle
        Game.game.data.addMove(struggle); // Pin Missile
        Game.game.data.addMove(struggle); // Leer
        Game.game.data.addMove(struggle); // Bite*
        Game.game.data.addMove(struggle); //         Growl
        Game.game.data.addMove(struggle); // Roar
        Game.game.data.addMove(struggle); //         Sing
        Game.game.data.addMove(struggle); // Supersonic
        Game.game.data.addMove(struggle); // Sonic Boom
        Game.game.data.addMove(struggle); // Disable
        Game.game.data.addMove(struggle); //         Acid
        Game.game.data.addMove(struggle); // Ember
        Game.game.data.addMove(struggle); //         Flamethrower
        Game.game.data.addMove(struggle); // Mist
        Game.game.data.addMove(struggle); // Water Gun
        Game.game.data.addMove(struggle); // Hydro Pump
        Game.game.data.addMove(struggle); // Surf
        Game.game.data.addMove(struggle); // Ice Beam
        Game.game.data.addMove(struggle); // Blizzard
        Game.game.data.addMove(struggle); //         Psybeam
        Game.game.data.addMove(struggle); // Bubble Beam
        Game.game.data.addMove(struggle); // Aurora Beam
        Game.game.data.addMove(struggle); // Hyper Beam
        Game.game.data.addMove(struggle); // Peck
        Game.game.data.addMove(struggle); // Drill Peck
        Game.game.data.addMove(struggle); // Submission
        Game.game.data.addMove(struggle); // Low Kick
        Game.game.data.addMove(struggle); // Counter
        Game.game.data.addMove(new Move(69, "Seismic Toss", FIGHTING, PHYSICAL, TOUGH, ADJACENT, 4, -1, 100, "contact, protect, mirror, nonsky", null)); // Seismic Toss
        Game.game.data.addMove(struggle); // Strength
        Game.game.data.addMove(struggle); //         Absorb
        Game.game.data.addMove(struggle); // Mega Drain
        Game.game.data.addMove(struggle); // Leech Seed
        Game.game.data.addMove(struggle); // Growth
        Game.game.data.addMove(struggle); // Razor Leaf
        Game.game.data.addMove(struggle); // Solar Beam
        Game.game.data.addMove(struggle); // Poison Powder
        Game.game.data.addMove(struggle); // Stun Spore
        Game.game.data.addMove(struggle); // Sleep Powder
        Game.game.data.addMove(struggle); // Petal Dance
        Game.game.data.addMove(struggle); // String Shot
        Game.game.data.addMove(struggle); // Dragon Rage
        Game.game.data.addMove(struggle); // Fire Spin
        Game.game.data.addMove(struggle); // Thunder Shock
        Game.game.data.addMove(struggle); // Thunderbolt
        Game.game.data.addMove(struggle); // Thunder Wave
        Game.game.data.addMove(struggle); // Thunder
        Game.game.data.addMove(struggle); // Rock Throw
        Game.game.data.addMove(struggle); // Earthquake
        Game.game.data.addMove(struggle); //         Fissure
        Game.game.data.addMove(struggle); // Dig
        Game.game.data.addMove(struggle); //         Toxic
        Game.game.data.addMove(struggle); // Confusion
        Game.game.data.addMove(struggle); //         Psychic
        Game.game.data.addMove(struggle); // Hypnosis
        Game.game.data.addMove(struggle); //         Meditate
        Game.game.data.addMove(struggle); // Agility
        Game.game.data.addMove(struggle); // Quick Attack
        Game.game.data.addMove(struggle); // Rage
        Game.game.data.addMove(struggle); //         Teleport
        Game.game.data.addMove(new Move(101, "Night Shade", GHOST, SPECIAL, CLEVER, ADJACENT, 3, -1, 100, "protect, mirror", null));
        Game.game.data.addMove(struggle); // Mimic
        Game.game.data.addMove(struggle); //         Screech
        Game.game.data.addMove(struggle); // Double Team
        Game.game.data.addMove(struggle); // Recover
        Game.game.data.addMove(struggle); //         Harden
        Game.game.data.addMove(struggle); // Minimize
        Game.game.data.addMove(struggle); //         Smokescreen
        Game.game.data.addMove(struggle); // Confuse Ray
        Game.game.data.addMove(struggle); // Withdraw
        Game.game.data.addMove(struggle); // Defense Curl
        Game.game.data.addMove(struggle); // Barrier
        Game.game.data.addMove(struggle); // Light Screen
        Game.game.data.addMove(struggle); // Haze
        Game.game.data.addMove(struggle); //         Reflect
        Game.game.data.addMove(struggle); // Focus Energy
        Game.game.data.addMove(struggle); // Bide
        Game.game.data.addMove(struggle); //         Metronome
        Game.game.data.addMove(struggle); // Mirror Move
        Game.game.data.addMove(struggle); // Self-Destruct
        Game.game.data.addMove(struggle); // Egg Bomb
        Game.game.data.addMove(struggle); // Lick
        Game.game.data.addMove(struggle); //         Smog
        Game.game.data.addMove(struggle); // Sludge
        Game.game.data.addMove(struggle); // Bone Club
        Game.game.data.addMove(struggle); // Fire Blast
        Game.game.data.addMove(struggle); // Waterfall
        Game.game.data.addMove(struggle); //         Clamp
        Game.game.data.addMove(struggle); // Swift
        Game.game.data.addMove(struggle); // Skull Bash
        Game.game.data.addMove(struggle); // Spike Cannon
        Game.game.data.addMove(struggle); // Constrict
        Game.game.data.addMove(struggle); //         Amnesia
        Game.game.data.addMove(struggle); // Kinesis
        Game.game.data.addMove(struggle); // Soft-Boiled
        Game.game.data.addMove(struggle); // High Jump Kick
        Game.game.data.addMove(struggle); //         Glare
        Game.game.data.addMove(struggle); // Dream Eater
        Game.game.data.addMove(struggle); // Poison Gas
        Game.game.data.addMove(struggle); // Barrage
        Game.game.data.addMove(struggle); // Leech Life
        Game.game.data.addMove(struggle); // Lovely Kiss
        Game.game.data.addMove(struggle); // Sky Attack
        Game.game.data.addMove(struggle); // Transform
        Game.game.data.addMove(struggle); //         Bubble
        Game.game.data.addMove(struggle); // Dizzy Punch
        Game.game.data.addMove(struggle); // Spore
        Game.game.data.addMove(struggle); //         Flash
        Game.game.data.addMove(struggle); // Psywave
        Game.game.data.addMove(struggle); //         Splash
        Game.game.data.addMove(struggle); // Acid Armor
        Game.game.data.addMove(struggle); // Crabhammer
        Game.game.data.addMove(struggle); //         Explosion
        Game.game.data.addMove(struggle); // Fury Swipes
        Game.game.data.addMove(struggle); // Bonemerang
        Game.game.data.addMove(struggle); //         Rest
        Game.game.data.addMove(struggle); // Rock Slide
        Game.game.data.addMove(struggle); // Hyper Fang
        Game.game.data.addMove(struggle); // Sharpen
        Game.game.data.addMove(struggle); //         Conversion
        Game.game.data.addMove(struggle); // Tri Attack
        Game.game.data.addMove(struggle); // Super Fang
        Game.game.data.addMove(struggle); // Slash
        Game.game.data.addMove(struggle); //         Substitute
        Game.game.data.addMove(struggle); // Struggle
        Game.game.data.addMove(struggle); //         Sketch
        Game.game.data.addMove(struggle); // Triple Kick
        Game.game.data.addMove(struggle); // Thief
        Game.game.data.addMove(struggle); // Spider Web
        Game.game.data.addMove(struggle); // Mind Reader
        Game.game.data.addMove(struggle); // Nightmare
        Game.game.data.addMove(struggle); // Flame Wheel
        Game.game.data.addMove(struggle); // Snore
        Game.game.data.addMove(struggle); // Curse*
        Game.game.data.addMove(struggle); //         Flail
        Game.game.data.addMove(struggle); // Conversion 2
        Game.game.data.addMove(struggle); // Aeroblast
        Game.game.data.addMove(struggle); // Cotton Spore
        Game.game.data.addMove(struggle); // Reversal
        Game.game.data.addMove(struggle); //         Spite
        Game.game.data.addMove(struggle); // Powder Snow
        Game.game.data.addMove(struggle); // Protect
        Game.game.data.addMove(struggle); // Mach Punch
        Game.game.data.addMove(struggle); // Scary Face
        Game.game.data.addMove(struggle); // Feint Attack
        Game.game.data.addMove(struggle); // Sweet Kiss*
        Game.game.data.addMove(struggle); // Belly Drum
        Game.game.data.addMove(struggle); // Sludge Bomb
        Game.game.data.addMove(struggle); // Mud-Slap
        Game.game.data.addMove(struggle); // Octazooka
        Game.game.data.addMove(struggle); //         Spikes
        Game.game.data.addMove(struggle); // Zap Cannon
        Game.game.data.addMove(struggle); // Foresight
        Game.game.data.addMove(struggle); // Destiny Bond
        Game.game.data.addMove(struggle); // Perish Song
        Game.game.data.addMove(struggle); // Icy Wind
        Game.game.data.addMove(struggle); // Detect
        Game.game.data.addMove(struggle); // Bone Rush
        Game.game.data.addMove(struggle); // Lock-On
        Game.game.data.addMove(struggle); // Outrage
        Game.game.data.addMove(struggle); //         Sandstorm
        Game.game.data.addMove(struggle); // Giga Drain
        Game.game.data.addMove(struggle); // Endure
        Game.game.data.addMove(struggle); // Charm*
        Game.game.data.addMove(struggle); //         Rollout
        Game.game.data.addMove(struggle); // False Swipe
        Game.game.data.addMove(struggle); // Swagger
        Game.game.data.addMove(struggle); // Milk Drink
        Game.game.data.addMove(struggle); // Spark
        Game.game.data.addMove(struggle); // Fury Cutter
        Game.game.data.addMove(struggle); // Steel Wing
        Game.game.data.addMove(struggle); // Mean Look
        Game.game.data.addMove(struggle); // Attract
        Game.game.data.addMove(struggle); // Sleep Talk
        Game.game.data.addMove(struggle); // Heal Bell
        Game.game.data.addMove(struggle); // Return
        Game.game.data.addMove(struggle); //         Present
        Game.game.data.addMove(struggle); // Frustration
        Game.game.data.addMove(struggle); //         Safeguard
        Game.game.data.addMove(struggle); // Pain Split
        Game.game.data.addMove(struggle); // Sacred Fire
        Game.game.data.addMove(struggle); // Magnitude
        Game.game.data.addMove(struggle); // Dynamic Punch
        Game.game.data.addMove(struggle); // Megahorn
        Game.game.data.addMove(struggle); // Dragon Breath
        Game.game.data.addMove(struggle); // Baton Pass
        Game.game.data.addMove(struggle); // Encore
        Game.game.data.addMove(struggle); //         Pursuit
        Game.game.data.addMove(struggle); // Rapid Spin
        Game.game.data.addMove(struggle); // Sweet Scent
        Game.game.data.addMove(struggle); // Iron Tail
        Game.game.data.addMove(struggle); // Metal Claw
        Game.game.data.addMove(struggle); // Vital Throw
        Game.game.data.addMove(struggle); // Morning Sun
        Game.game.data.addMove(struggle); // Synthesis
        Game.game.data.addMove(struggle); // Moonlight*
        Game.game.data.addMove(struggle); //         Hidden Power
        Game.game.data.addMove(struggle); // Cross Chop
        Game.game.data.addMove(struggle); // Twister
        Game.game.data.addMove(struggle); // Rain Dance
        Game.game.data.addMove(struggle); // Sunny Day
        Game.game.data.addMove(struggle); // Crunch
        Game.game.data.addMove(struggle); // Mirror Coat
        Game.game.data.addMove(struggle); // Psych Up
        Game.game.data.addMove(struggle); // Extreme Speed
        Game.game.data.addMove(struggle); // Ancient Power
        Game.game.data.addMove(struggle); // Shadow Ball
        Game.game.data.addMove(struggle); // Future Sight
        Game.game.data.addMove(struggle); // Rock Smash
        Game.game.data.addMove(struggle); // Whirlpool
        Game.game.data.addMove(struggle); // Beat Up
        Game.game.data.addMove(struggle); // Fake Out
        Game.game.data.addMove(struggle); // Uproar
        Game.game.data.addMove(struggle); //         Stockpile
        Game.game.data.addMove(struggle); // Spit Up
        Game.game.data.addMove(struggle); // Swallow
        Game.game.data.addMove(struggle); // Heat Wave
        Game.game.data.addMove(struggle); // Hail
        Game.game.data.addMove(struggle); //         Torment
        Game.game.data.addMove(struggle); // Flatter
        Game.game.data.addMove(struggle); // Will-O-Wisp
        Game.game.data.addMove(struggle); // Memento
        Game.game.data.addMove(struggle); //         Facade
        Game.game.data.addMove(struggle); // Focus Punch
        Game.game.data.addMove(struggle); // Smelling Salts
        Game.game.data.addMove(struggle); // Follow Me
        Game.game.data.addMove(struggle); // Nature Power
        Game.game.data.addMove(struggle); // Charge
        Game.game.data.addMove(struggle); //         Taunt
        Game.game.data.addMove(struggle); // Helping Hand
        Game.game.data.addMove(struggle); // Trick
        Game.game.data.addMove(struggle); // Role Play
        Game.game.data.addMove(struggle); // Wish
        Game.game.data.addMove(struggle); //         Assist
        Game.game.data.addMove(struggle); // Ingrain
        Game.game.data.addMove(struggle); //         Superpower
        Game.game.data.addMove(struggle); // Magic Coat
        Game.game.data.addMove(struggle); // Recycle
        Game.game.data.addMove(struggle); //         Revenge
        Game.game.data.addMove(struggle); // Brick Break
        Game.game.data.addMove(struggle); // Yawn
        Game.game.data.addMove(struggle); // Knock Off
        Game.game.data.addMove(struggle); // Endeavor
        Game.game.data.addMove(struggle); //         Eruption
        Game.game.data.addMove(struggle); // Skill Swap
        Game.game.data.addMove(struggle); // Imprison
        Game.game.data.addMove(struggle); //         Refresh
        Game.game.data.addMove(struggle); // Grudge
        Game.game.data.addMove(struggle); //         Snatch
        Game.game.data.addMove(struggle); // Secret Power
        Game.game.data.addMove(struggle); // Dive
        Game.game.data.addMove(struggle); // Arm Thrust
        Game.game.data.addMove(struggle); // Camouflage
        Game.game.data.addMove(struggle); // Tail Glow
        Game.game.data.addMove(struggle); // Luster Purge
        Game.game.data.addMove(struggle); // Mist Ball
        Game.game.data.addMove(struggle); // Feather Dance
        Game.game.data.addMove(struggle); // Teeter Dance
        Game.game.data.addMove(struggle); // Blaze Kick
        Game.game.data.addMove(struggle); // Mud Sport
        Game.game.data.addMove(struggle); // Ice Ball
        Game.game.data.addMove(struggle); // Needle Arm
        Game.game.data.addMove(struggle); // Slack Off
        Game.game.data.addMove(struggle); // Hyper Voice
        Game.game.data.addMove(struggle); // Poison Fang
        Game.game.data.addMove(struggle); // Crush Claw
        Game.game.data.addMove(struggle); // Blast Burn
        Game.game.data.addMove(struggle); // Hydro Cannon
        Game.game.data.addMove(struggle); // Meteor Mash
        Game.game.data.addMove(struggle); // Astonish
        Game.game.data.addMove(struggle); // Weather Ball
        Game.game.data.addMove(struggle); // Aromatherapy
        Game.game.data.addMove(struggle); // Fake Tears
        Game.game.data.addMove(struggle); // Air Cutter
        Game.game.data.addMove(struggle); // Overheat
        Game.game.data.addMove(struggle); // Odor Sleuth
        Game.game.data.addMove(struggle); // Rock Tomb
        Game.game.data.addMove(struggle); // Silver Wind
        Game.game.data.addMove(struggle); // Metal Sound
        Game.game.data.addMove(struggle); // Grass Whistle
        Game.game.data.addMove(struggle); // Tickle
        Game.game.data.addMove(struggle); // Cosmic Power
        Game.game.data.addMove(struggle); // Water Spout
        Game.game.data.addMove(struggle); // Signal Beam
        Game.game.data.addMove(struggle); // Shadow Punch
        Game.game.data.addMove(struggle); // Extrasensory
        Game.game.data.addMove(struggle); // Sky Uppercut
        Game.game.data.addMove(struggle); // Sand Tomb
        Game.game.data.addMove(struggle); // Sheer Cold
        Game.game.data.addMove(struggle); // Muddy Water
        Game.game.data.addMove(struggle); // Bullet Seed
        Game.game.data.addMove(struggle); // Aerial Ace
        Game.game.data.addMove(struggle); // Icicle Spear
        Game.game.data.addMove(struggle); // Iron Defense
        Game.game.data.addMove(struggle); // Block
        Game.game.data.addMove(struggle); //         Howl
        Game.game.data.addMove(struggle); // Dragon Claw
        Game.game.data.addMove(struggle); // Frenzy Plant
        Game.game.data.addMove(struggle); // Bulk Up
        Game.game.data.addMove(struggle); // Bounce
        Game.game.data.addMove(struggle); // Mud Shot
        Game.game.data.addMove(struggle); // Poison Tail
        Game.game.data.addMove(struggle); // Covet
        Game.game.data.addMove(struggle); // Volt Tackle
        Game.game.data.addMove(struggle); // Magical Leaf
        Game.game.data.addMove(struggle); // Water Sport
        Game.game.data.addMove(struggle); // Calm Mind
        Game.game.data.addMove(struggle); // Leaf Blade
        Game.game.data.addMove(struggle); // Dragon Dance
        Game.game.data.addMove(struggle); // Rock Blast
        Game.game.data.addMove(struggle); // Shock Wave
        Game.game.data.addMove(struggle); // Water Pulse
        Game.game.data.addMove(struggle); // Doom Desire
        Game.game.data.addMove(struggle); // Psycho Boost
        Game.game.data.addMove(struggle); // Roost
        Game.game.data.addMove(struggle); //         Gravity
        Game.game.data.addMove(struggle); // Miracle Eye
        Game.game.data.addMove(struggle); // Wake-Up Slap
        Game.game.data.addMove(struggle); // Hammer Arm
        Game.game.data.addMove(struggle); // Gyro Ball
        Game.game.data.addMove(struggle); // Healing Wish
        Game.game.data.addMove(struggle); // Brine
        Game.game.data.addMove(struggle); // Natural Gift
        Game.game.data.addMove(struggle); // Feint
        Game.game.data.addMove(struggle); //         Pluck
        Game.game.data.addMove(struggle); // Tailwind
        Game.game.data.addMove(struggle); //         Acupressure
        Game.game.data.addMove(struggle); // Metal Burst
        Game.game.data.addMove(struggle); // U-turn
        Game.game.data.addMove(struggle); // Close Combat
        Game.game.data.addMove(struggle); // Payback
        Game.game.data.addMove(struggle); //         Assurance
        Game.game.data.addMove(struggle); // Embargo
        Game.game.data.addMove(struggle); //         Fling
        Game.game.data.addMove(struggle); // Psycho Shift
        Game.game.data.addMove(struggle); // Trump Card
        Game.game.data.addMove(struggle); // Heal Block
        Game.game.data.addMove(struggle); // Wring Out
        Game.game.data.addMove(struggle); // Power Trick
        Game.game.data.addMove(struggle); // Gastro Acid
        Game.game.data.addMove(struggle); // Lucky Chant
        Game.game.data.addMove(struggle); // Me First
        Game.game.data.addMove(struggle); // Copycat
        Game.game.data.addMove(struggle); // Power Swap
        Game.game.data.addMove(struggle); // Guard Swap
        Game.game.data.addMove(struggle); // Punishment
        Game.game.data.addMove(struggle); // Last Resort
        Game.game.data.addMove(struggle); // Worry Seed
        Game.game.data.addMove(struggle); // Sucker Punch
        Game.game.data.addMove(struggle); // Toxic Spikes
        Game.game.data.addMove(struggle); // Heart Swap
        Game.game.data.addMove(struggle); // Aqua Ring
        Game.game.data.addMove(struggle); // Magnet Rise
        Game.game.data.addMove(struggle); // Flare Blitz
        Game.game.data.addMove(struggle); // Force Palm
        Game.game.data.addMove(struggle); // Aura Sphere
        Game.game.data.addMove(struggle); // Rock Polish
        Game.game.data.addMove(struggle); // Poison Jab
        Game.game.data.addMove(struggle); // Dark Pulse
        Game.game.data.addMove(struggle); // Night Slash
        Game.game.data.addMove(struggle); // Aqua Tail
        Game.game.data.addMove(struggle); // Seed Bomb
        Game.game.data.addMove(struggle); // Air Slash
        Game.game.data.addMove(struggle); // X-Scissor
        Game.game.data.addMove(struggle); // Bug Buzz
        Game.game.data.addMove(struggle); // Dragon Pulse
        Game.game.data.addMove(struggle); // Dragon Rush
        Game.game.data.addMove(struggle); // Power Gem
        Game.game.data.addMove(struggle); // Drain Punch
        Game.game.data.addMove(struggle); // Vacuum Wave
        Game.game.data.addMove(struggle); // Focus Blast
        Game.game.data.addMove(struggle); // Energy Ball
        Game.game.data.addMove(struggle); // Brave Bird
        Game.game.data.addMove(struggle); // Earth Power
        Game.game.data.addMove(struggle); // Switcheroo
        Game.game.data.addMove(struggle); // Giga Impact
        Game.game.data.addMove(struggle); // Nasty Plot
        Game.game.data.addMove(struggle); // Bullet Punch
        Game.game.data.addMove(struggle); // Avalanche
        Game.game.data.addMove(struggle); // Ice Shard
        Game.game.data.addMove(struggle); // Shadow Claw
        Game.game.data.addMove(struggle); // Thunder Fang
        Game.game.data.addMove(struggle); // Ice Fang
        Game.game.data.addMove(struggle); // Fire Fang
        Game.game.data.addMove(struggle); // Shadow Sneak
        Game.game.data.addMove(struggle); // Mud Bomb
        Game.game.data.addMove(struggle); // Psycho Cut
        Game.game.data.addMove(struggle); // Zen Headbutt
        Game.game.data.addMove(struggle); // Mirror Shot
        Game.game.data.addMove(struggle); // Flash Cannon
        Game.game.data.addMove(struggle); // Rock Climb
        Game.game.data.addMove(struggle); // Defog
        Game.game.data.addMove(struggle); // Trick Room
        Game.game.data.addMove(struggle); // Draco Meteor
        Game.game.data.addMove(struggle); // Discharge
        Game.game.data.addMove(struggle); // Lava Plume
        Game.game.data.addMove(struggle); // Leaf Storm
        Game.game.data.addMove(struggle); // Power Whip
        Game.game.data.addMove(struggle); // Rock Wrecker
        Game.game.data.addMove(struggle); // Cross Poison
        Game.game.data.addMove(struggle); // Gunk Shot
        Game.game.data.addMove(struggle); // Iron Head
        Game.game.data.addMove(struggle); // Magnet Bomb
        Game.game.data.addMove(struggle); // Stone Edge
        Game.game.data.addMove(struggle); // Captivate
        Game.game.data.addMove(struggle); // Stealth Rock
        Game.game.data.addMove(struggle); // Grass Knot
        Game.game.data.addMove(struggle); // Chatter
        Game.game.data.addMove(struggle); //         Judgment
        Game.game.data.addMove(struggle); // Bug Bite
        Game.game.data.addMove(struggle); // Charge Beam
        Game.game.data.addMove(struggle); // Wood Hammer
        Game.game.data.addMove(struggle); // Aqua Jet
        Game.game.data.addMove(struggle); // Attack Order
        Game.game.data.addMove(struggle); // Defend Order
        Game.game.data.addMove(struggle); // Heal Order
        Game.game.data.addMove(struggle); // Head Smash
        Game.game.data.addMove(struggle); // Double Hit
        Game.game.data.addMove(struggle); // Roar of Time
        Game.game.data.addMove(struggle); // Spacial Rend
        Game.game.data.addMove(struggle); // Lunar Dance
        Game.game.data.addMove(struggle); // Crush Grip
        Game.game.data.addMove(struggle); // Magma Storm
        Game.game.data.addMove(struggle); // Dark Void
        Game.game.data.addMove(struggle); // Seed Flare
        Game.game.data.addMove(struggle); // Ominous Wind
        Game.game.data.addMove(struggle); // Shadow Force
        Game.game.data.addMove(struggle); // Hone Claws
        Game.game.data.addMove(struggle); // Wide Guard
        Game.game.data.addMove(struggle); // Guard Split
        Game.game.data.addMove(struggle); // Power Split
        Game.game.data.addMove(struggle); // Wonder Room
        Game.game.data.addMove(struggle); // Psyshock
        Game.game.data.addMove(struggle); //         Venoshock
        Game.game.data.addMove(struggle); // Autotomize
        Game.game.data.addMove(struggle); // Rage Powder
        Game.game.data.addMove(struggle); // Telekinesis
        Game.game.data.addMove(struggle); // Magic Room
        Game.game.data.addMove(struggle); // Smack Down
        Game.game.data.addMove(struggle); // Storm Throw
        Game.game.data.addMove(struggle); // Flame Burst
        Game.game.data.addMove(struggle); // Sludge Wave
        Game.game.data.addMove(struggle); // Quiver Dance
        Game.game.data.addMove(struggle); // Heavy Slam
        Game.game.data.addMove(struggle); // Synchronoise
        Game.game.data.addMove(struggle); // Electro Ball
        Game.game.data.addMove(struggle); // Soak
        Game.game.data.addMove(struggle); // Flame Charge
        Game.game.data.addMove(struggle); // Coil
        Game.game.data.addMove(struggle); // Low Sweep
        Game.game.data.addMove(struggle); // Acid Spray
        Game.game.data.addMove(struggle); // Foul Play
        Game.game.data.addMove(struggle); // Simple Beam
        Game.game.data.addMove(struggle); // Entrainment
        Game.game.data.addMove(struggle); // After You
        Game.game.data.addMove(struggle); // Round
        Game.game.data.addMove(struggle); // Echoed Voice
        Game.game.data.addMove(struggle); // Chip Away
        Game.game.data.addMove(struggle); // Clear Smog
        Game.game.data.addMove(struggle); // Stored Power
        Game.game.data.addMove(struggle); // Quick Guard
        Game.game.data.addMove(struggle); // Ally Switch
        Game.game.data.addMove(struggle); // Scald
        Game.game.data.addMove(struggle); // Shell Smash
        Game.game.data.addMove(struggle); // Heal Pulse
        Game.game.data.addMove(struggle); // Hex
        Game.game.data.addMove(struggle); // Sky Drop
        Game.game.data.addMove(struggle); // Shift Gear
        Game.game.data.addMove(struggle); // Circle Throw
        Game.game.data.addMove(struggle); // Incinerate
        Game.game.data.addMove(struggle); //         Quash
        Game.game.data.addMove(struggle); // Acrobatics
        Game.game.data.addMove(struggle); // Reflect Type
        Game.game.data.addMove(struggle); // Retaliate
        Game.game.data.addMove(struggle); // Final Gambit
        Game.game.data.addMove(struggle); // Bestow
        Game.game.data.addMove(struggle); //         Inferno
        Game.game.data.addMove(struggle); // Water Pledge
        Game.game.data.addMove(struggle); // Fire Pledge
        Game.game.data.addMove(struggle); // Grass Pledge
        Game.game.data.addMove(struggle); // Volt Switch
        Game.game.data.addMove(struggle); // Struggle Bug
        Game.game.data.addMove(struggle); // Bulldoze
        Game.game.data.addMove(struggle); // Frost Breath
        Game.game.data.addMove(struggle); // Dragon Tail
        Game.game.data.addMove(struggle); // Work Up
        Game.game.data.addMove(struggle); // Electroweb
        Game.game.data.addMove(struggle); // Wild Charge
        Game.game.data.addMove(struggle); // Drill Run
        Game.game.data.addMove(struggle); // Dual Chop
        Game.game.data.addMove(struggle); // Heart Stamp
        Game.game.data.addMove(struggle); // Horn Leech
        Game.game.data.addMove(struggle); // Sacred Sword
        Game.game.data.addMove(struggle); // Razor Shell
        Game.game.data.addMove(struggle); // Heat Crash
        Game.game.data.addMove(struggle); // Leaf Tornado
        Game.game.data.addMove(struggle); // Steamroller
        Game.game.data.addMove(struggle); // Cotton Guard
        Game.game.data.addMove(struggle); // Night Daze
        Game.game.data.addMove(struggle); // Psystrike
        Game.game.data.addMove(struggle); // Tail Slap
        Game.game.data.addMove(struggle); // Hurricane
        Game.game.data.addMove(struggle); // Head Charge
        Game.game.data.addMove(struggle); // Gear Grind
        Game.game.data.addMove(struggle); // Searing Shot
        Game.game.data.addMove(struggle); // Techno Blast
        Game.game.data.addMove(struggle); // Relic Song
        Game.game.data.addMove(struggle); // Secret Sword
        Game.game.data.addMove(struggle); // Glaciate
        Game.game.data.addMove(struggle); // Bolt Strike
        Game.game.data.addMove(struggle); // Blue Flare
        Game.game.data.addMove(struggle); // Fiery Dance
        Game.game.data.addMove(struggle); // Freeze Shock
        Game.game.data.addMove(struggle); // Ice Burn
        Game.game.data.addMove(struggle); // Snarl
        Game.game.data.addMove(struggle); // Icicle Crash
        Game.game.data.addMove(struggle); // V-create
        Game.game.data.addMove(struggle); // Fusion Flare
        Game.game.data.addMove(struggle); // Fusion Bolt
        Game.game.data.addMove(struggle); // Flying Press
        Game.game.data.addMove(struggle); // Mat Block
        Game.game.data.addMove(struggle); // Belch
        Game.game.data.addMove(struggle); //         Rototiller
        Game.game.data.addMove(struggle); // Sticky Web
        Game.game.data.addMove(struggle); // Fell Stinger
        Game.game.data.addMove(struggle); // Phantom Force
        Game.game.data.addMove(struggle); // Trick-or-Treat
        Game.game.data.addMove(struggle); // Noble Roar
        Game.game.data.addMove(struggle); // Ion Deluge
        Game.game.data.addMove(struggle); // Parabolic Charge
        Game.game.data.addMove(struggle); // Forest's Curse
        Game.game.data.addMove(struggle); // Petal Blizzard
        Game.game.data.addMove(struggle); // Freeze-Dry
        Game.game.data.addMove(struggle); // Disarming Voice
        Game.game.data.addMove(struggle); // Parting Shot
        Game.game.data.addMove(struggle); // Topsy-Turvy
        Game.game.data.addMove(struggle); // Draining Kiss
        Game.game.data.addMove(struggle); // Crafty Shield
        Game.game.data.addMove(struggle); // Flower Shield
        Game.game.data.addMove(struggle); // Grassy Terrain
        Game.game.data.addMove(struggle); // Misty Terrain
        Game.game.data.addMove(struggle); // Electrify
        Game.game.data.addMove(struggle); // Play Rough
        Game.game.data.addMove(struggle); // Fairy Wind
        Game.game.data.addMove(struggle); // Moonblast
        Game.game.data.addMove(struggle); //         Boomburst
        Game.game.data.addMove(struggle); // Fairy Lock
        Game.game.data.addMove(struggle); // King's Shield
        Game.game.data.addMove(struggle); // Play Nice
        Game.game.data.addMove(struggle); // Confide
        Game.game.data.addMove(struggle); // Diamond Storm
        Game.game.data.addMove(struggle); // Steam Eruption
        Game.game.data.addMove(struggle); // Hyperspace Hole
        Game.game.data.addMove(struggle); // Water Shuriken*
        Game.game.data.addMove(struggle); // Mystical Fire
        Game.game.data.addMove(struggle); // Spiky Shield
        Game.game.data.addMove(struggle); // Aromatic Mist
        Game.game.data.addMove(struggle); // Eerie Impulse
        Game.game.data.addMove(struggle); // Venom Drench
        Game.game.data.addMove(struggle); // Powder
        Game.game.data.addMove(struggle); //         Geomancy
        Game.game.data.addMove(struggle); // Magnetic Flux
        Game.game.data.addMove(struggle); // Happy Hour
        Game.game.data.addMove(struggle); // Electric Terrain
        Game.game.data.addMove(struggle); // Dazzling Gleam
        Game.game.data.addMove(struggle); // Celebrate
        Game.game.data.addMove(struggle); // Hold Hands
        Game.game.data.addMove(struggle); // Baby-Doll Eyes
        Game.game.data.addMove(struggle); //         Nuzzle
        Game.game.data.addMove(struggle); // Hold Back
        Game.game.data.addMove(struggle); // Infestation
        Game.game.data.addMove(struggle); // Power-Up Punch
        Game.game.data.addMove(struggle); // Oblivion Wing
        Game.game.data.addMove(struggle); // Thousand Arrows
        Game.game.data.addMove(struggle); // Thousand Waves
        Game.game.data.addMove(struggle); // Land's Wrath
        Game.game.data.addMove(struggle); // Light of Ruin
        Game.game.data.addMove(struggle); // Origin Pulse
        Game.game.data.addMove(struggle); // Precipice Blades
        Game.game.data.addMove(struggle); // Dragon Ascent
        Game.game.data.addMove(struggle); // Hyperspace Fury
        Game.game.data.addMove(struggle); // Breakneck Blitz
        Game.game.data.addMove(struggle); // Breakneck Blitz
        Game.game.data.addMove(struggle); // All-Out Pummeling
        Game.game.data.addMove(struggle); // All-Out Pummeling
        Game.game.data.addMove(struggle); // Supersonic Skystrike
        Game.game.data.addMove(struggle); // Supersonic Skystrike
        Game.game.data.addMove(struggle); // Acid Downpour
        Game.game.data.addMove(struggle); // Acid Downpour
        Game.game.data.addMove(struggle); // Tectonic Rage
        Game.game.data.addMove(struggle); // Tectonic Rage
        Game.game.data.addMove(struggle); // Continental Crush
        Game.game.data.addMove(struggle); // Continental Crush
        Game.game.data.addMove(struggle); // Savage Spin-Out
        Game.game.data.addMove(struggle); // Savage Spin-Out
        Game.game.data.addMove(struggle); // Never-Ending Nightmare
        Game.game.data.addMove(struggle); // Never-Ending Nightmare
        Game.game.data.addMove(struggle); // Corkscrew Crash
        Game.game.data.addMove(struggle); // Corkscrew Crash
        Game.game.data.addMove(struggle); // Inferno Overdrive
        Game.game.data.addMove(struggle); // Inferno Overdrive
        Game.game.data.addMove(struggle); // Hydro Vortex
        Game.game.data.addMove(struggle); // Hydro Vortex
        Game.game.data.addMove(struggle); // Bloom Doom
        Game.game.data.addMove(struggle); // Bloom Doom
        Game.game.data.addMove(struggle); // Gigavolt Havoc
        Game.game.data.addMove(struggle); // Gigavolt Havoc
        Game.game.data.addMove(struggle); // Shattered Psyche
        Game.game.data.addMove(struggle); // Shattered Psyche
        Game.game.data.addMove(struggle); // Subzero Slammer
        Game.game.data.addMove(struggle); // Subzero Slammer
        Game.game.data.addMove(struggle); // Devastating Drake
        Game.game.data.addMove(struggle); // Devastating Drake
        Game.game.data.addMove(struggle); // Black Hole Eclipse
        Game.game.data.addMove(struggle); // Black Hole Eclipse
        Game.game.data.addMove(struggle); // Twinkle Tackle
        Game.game.data.addMove(struggle); // Twinkle Tackle
        Game.game.data.addMove(struggle); // Catastropika
        Game.game.data.addMove(struggle); // Shore Up
        Game.game.data.addMove(struggle); // First Impression
        Game.game.data.addMove(struggle); // Baneful Bunker
        Game.game.data.addMove(struggle); // Spirit Shackle
        Game.game.data.addMove(struggle); // Darkest Lariat
        Game.game.data.addMove(struggle); // Sparkling Aria
        Game.game.data.addMove(struggle); // Ice Hammer
        Game.game.data.addMove(struggle); // Floral Healing
        Game.game.data.addMove(struggle); // High Horsepower
        Game.game.data.addMove(struggle); // Strength Sap
        Game.game.data.addMove(struggle); // Solar Blade
        Game.game.data.addMove(struggle); // Leafage
        Game.game.data.addMove(struggle); //         Spotlight
        Game.game.data.addMove(struggle); // Toxic Thread
        Game.game.data.addMove(struggle); // Laser Focus
        Game.game.data.addMove(struggle); // Gear Up
        Game.game.data.addMove(struggle); // Throat Chop
        Game.game.data.addMove(struggle); // Pollen Puff
        Game.game.data.addMove(struggle); // Anchor Shot
        Game.game.data.addMove(struggle); // Psychic Terrain
        Game.game.data.addMove(struggle); // Lunge
        Game.game.data.addMove(struggle); // Fire Lash
        Game.game.data.addMove(struggle); // Power Trip
        Game.game.data.addMove(struggle); // Burn Up
        Game.game.data.addMove(struggle); // Speed Swap
        Game.game.data.addMove(struggle); // Smart Strike
        Game.game.data.addMove(struggle); // Purify
        Game.game.data.addMove(struggle); // Revelation Dance
        Game.game.data.addMove(struggle); // Core Enforcer
        Game.game.data.addMove(struggle); // Trop Kick
        Game.game.data.addMove(struggle); // Instruct
        Game.game.data.addMove(struggle); // Beak Blast
        Game.game.data.addMove(struggle); // Clanging Scales
        Game.game.data.addMove(struggle); // Dragon Hammer
        Game.game.data.addMove(struggle); // Brutal Swing
        Game.game.data.addMove(struggle); // Aurora Veil
        Game.game.data.addMove(struggle); // Sinister Arrow Raid
        Game.game.data.addMove(struggle); // Malicious Moonsault
        Game.game.data.addMove(struggle); // Oceanic Operetta
        Game.game.data.addMove(struggle); // Guardian of Alola
        Game.game.data.addMove(struggle); // Soul-Stealing 7-Star Strike
        Game.game.data.addMove(struggle); // Stoked Sparksurfer
        Game.game.data.addMove(struggle); // Pulverizing Pancake
        Game.game.data.addMove(struggle); // Extreme Evoboost
        Game.game.data.addMove(struggle); // Genesis Supernova
        Game.game.data.addMove(struggle); // Shell Trap
        Game.game.data.addMove(struggle); // Fleur Cannon
        Game.game.data.addMove(struggle); // Psychic Fangs
        Game.game.data.addMove(struggle); // Stomping Tantrum
        Game.game.data.addMove(struggle); // Shadow Bone
        Game.game.data.addMove(struggle); // Accelerock
        Game.game.data.addMove(struggle); //         Liquidation
        Game.game.data.addMove(struggle); // Prismatic Laser
        Game.game.data.addMove(struggle); // Spectral Thief
        Game.game.data.addMove(struggle); // Sunsteel Strike
        Game.game.data.addMove(struggle); // Moongeist Beam
        Game.game.data.addMove(struggle); // Tearful Look
        Game.game.data.addMove(struggle); // Zing Zap
        Game.game.data.addMove(struggle); // Nature's Madness
        Game.game.data.addMove(struggle); // Multi-Attack
        Game.game.data.addMove(struggle); // 10,000,000 Volt Thunderbolt
        Game.game.data.addMove(struggle); // Mind Blown
        Game.game.data.addMove(struggle); // Plasma Fists
        Game.game.data.addMove(struggle); // Photon Geyser
        Game.game.data.addMove(struggle); // Light That Burns the Sky
        Game.game.data.addMove(struggle); // Searing Sunraze Smash
        Game.game.data.addMove(struggle); // Menacing Moonraze Maelstrom
        Game.game.data.addMove(struggle); // Let's Snuggle Forever
        Game.game.data.addMove(struggle); // Splintered Stormshards
        Game.game.data.addMove(struggle); // Clangorous Soulblaze
        Game.game.data.addMove(struggle); // Zippy Zap
        Game.game.data.addMove(struggle); // Splishy Splash
        Game.game.data.addMove(struggle); // Floaty Fall
        Game.game.data.addMove(struggle); // Pika Papow
        Game.game.data.addMove(struggle); // Bouncy Bubble
        Game.game.data.addMove(struggle); // Buzzy Buzz
        Game.game.data.addMove(struggle); // Sizzly Slide
        Game.game.data.addMove(struggle); // Glitzy Glow
        Game.game.data.addMove(struggle); // Baddy Bad
        Game.game.data.addMove(struggle); // Sappy Seed
        Game.game.data.addMove(struggle); // Freezy Frost
        Game.game.data.addMove(struggle); // Sparkly Swirl
        Game.game.data.addMove(struggle); // Veevee Volley
        Game.game.data.addMove(struggle); // Double Iron Bash
        Game.game.data.addMove(struggle); // Max Guard
        Game.game.data.addMove(struggle); // Dynamax Cannon
        Game.game.data.addMove(struggle); // Snipe Shot
        Game.game.data.addMove(struggle); // Jaw Lock
        Game.game.data.addMove(struggle); // Stuff Cheeks
        Game.game.data.addMove(struggle); // No Retreat
        Game.game.data.addMove(struggle); // Tar Shot
        Game.game.data.addMove(struggle); // Magic Powder
        Game.game.data.addMove(struggle); // Dragon Darts
        Game.game.data.addMove(struggle); // Teatime
        Game.game.data.addMove(struggle); //         Octolock
        Game.game.data.addMove(struggle); // Bolt Beak
        Game.game.data.addMove(struggle); // Fishious Rend
        Game.game.data.addMove(struggle); // Court Change
        Game.game.data.addMove(struggle); // Max Flare
        Game.game.data.addMove(struggle); // Max Flutterby
        Game.game.data.addMove(struggle); // Max Lightning
        Game.game.data.addMove(struggle); // Max Strike
        Game.game.data.addMove(struggle); // Max Knuckle
        Game.game.data.addMove(struggle); // Max Phantasm
        Game.game.data.addMove(struggle); // Max Hailstorm
        Game.game.data.addMove(struggle); // Max Ooze
        Game.game.data.addMove(struggle); // Max Geyser
        Game.game.data.addMove(struggle); // Max Airstream
        Game.game.data.addMove(struggle); // Max Starfall
        Game.game.data.addMove(struggle); // Max Wyrmwind
        Game.game.data.addMove(struggle); // Max Mindstorm
        Game.game.data.addMove(struggle); // Max Rockfall
        Game.game.data.addMove(struggle); // Max Quake
        Game.game.data.addMove(struggle); // Max Darkness
        Game.game.data.addMove(struggle); // Max Overgrowth
        Game.game.data.addMove(struggle); // Max Steelspike
        Game.game.data.addMove(struggle); // Clangorous Soul
        Game.game.data.addMove(struggle); // Body Press
        Game.game.data.addMove(struggle); // Decorate
        Game.game.data.addMove(struggle); // Drum Beating
        Game.game.data.addMove(struggle); // Snap Trap
        Game.game.data.addMove(struggle); // Pyro Ball
        Game.game.data.addMove(struggle); // Behemoth Blade
        Game.game.data.addMove(struggle); // Behemoth Bash
        Game.game.data.addMove(struggle); // Aura Wheel
        Game.game.data.addMove(struggle); // Breaking Swipe
        Game.game.data.addMove(struggle); // Branch Poke
        Game.game.data.addMove(struggle); // Overdrive
        Game.game.data.addMove(struggle); // Apple Acid
        Game.game.data.addMove(struggle); // Grav Apple
        Game.game.data.addMove(struggle); // Spirit Break
        Game.game.data.addMove(struggle); // Strange Steam
        Game.game.data.addMove(struggle); // Life Dew
        Game.game.data.addMove(struggle); // Obstruct
        Game.game.data.addMove(struggle); // False Surrender
        Game.game.data.addMove(struggle); // Meteor Assault
        Game.game.data.addMove(struggle); // Eternabeam
        Game.game.data.addMove(struggle); // Steel Beam
    }
    private void addAbilities() {
        Game.game.data.addAbility(new Ability(1, "Stench", "By releasing stench when attacking, this Pokémon may cause the target to flinch."));
        Game.game.data.addAbility(new Ability(2, "Drizzle", "The Pokémon makes it rain when it enters a battle."));
        Game.game.data.addAbility(new Ability(3, "Speed Boost", "Its Speed stat is boosted every turn."));
        Game.game.data.addAbility(new Ability(4, "Battle Armor", "Hard armor protects the Pokémon from critical hits."));
        Game.game.data.addAbility(new Ability(5, "Sturdy", "It cannot be knocked out with one hit. One-hit KO moves cannot knock it out, either."));
        Game.game.data.addAbility(new Ability(6, "Damp", "Prevents the use of explosive moves, such as Self-Destruct, by dampening its surroundings."));
        Game.game.data.addAbility(new Ability(7, "Limber", "Its limber body protects the Pokémon from paralysis."));
        Game.game.data.addAbility(new Ability(8, "Sand Veil", "Boosts the Pokémon's evasiveness in a sandstorm."));
        Game.game.data.addAbility(new Ability(9, "Static", "The Pokémon is charged with static electricity, so contact with it may cause paralysis."));
        Game.game.data.addAbility(new Ability(10, "Volt Absorb", "Restores HP if hit by an Electric-type move instead of taking damage."));
        Game.game.data.addAbility(new Ability(11, "Water Absorb", "Restores HP if hit by a Water-type move instead of taking damage."));
        Game.game.data.addAbility(new Ability(12, "Oblivious", "The Pokémon is oblivious, and that keeps it from being infatuated or falling for taunts."));
        Game.game.data.addAbility(new Ability(13, "Cloud Nine", "Eliminates the effects of weather."));
        Game.game.data.addAbility(new Ability(14, "Compound Eyes", "The Pokémon's compound eyes boost its accuracy."));
        Game.game.data.addAbility(new Ability(15, "Insomnia", "The Pokémon is suffering from insomnia and cannot fall asleep."));
        Game.game.data.addAbility(new Ability(16, "Color Change", "The Pokémon's type becomes the type of the move used on it."));
        Game.game.data.addAbility(new Ability(17, "Immunity", "The immune system of the Pokémon prevents it from getting poisoned."));
        Game.game.data.addAbility(new Ability(18, "Flash Fire", "Powers up the Pokémon's Fire-type moves if it's hit by one."));
        Game.game.data.addAbility(new Ability(19, "Shield Dust", "This Pokémon's dust blocks the additional effects of attacks taken."));
        Game.game.data.addAbility(new Ability(20, "Own Tempo", "This Pokémon has its own tempo, and that prevents it from becoming confused."));
        Game.game.data.addAbility(new Ability(21, "Suction Cups", "This Pokémon uses suction cups to stay in one spot to negate all moves and items that force switching out."));
        Game.game.data.addAbility(new Ability(22, "Intimidate", "The Pokémon intimidates opposing Pokémon upon entering battle, lowering their Attack stat."));
        Game.game.data.addAbility(new Ability(23, "Shadow Tag", "This Pokémon steps on the opposing Pokémon's shadow to prevent it from escaping."));
        Game.game.data.addAbility(new Ability(24, "Rough Skin", "This Pokémon inflicts damage with its rough skin to the attacker on contact."));
        Game.game.data.addAbility(new Ability(25, "Wonder Guard", "Its mysterious power only lets supereffective moves hit the Pokémon."));
        Game.game.data.addAbility(new Ability(26, "Levitate", "By floating in the air, the Pokémon receives full immunity to all Ground-type moves."));
        Game.game.data.addAbility(new Ability(27, "Effect Spore", "Contact with the Pokémon may inflict poison, sleep, or paralysis on its attacker."));
        Game.game.data.addAbility(new Ability(28, "Synchronize", "The attacker will receive the same status condition if it inflicts a burn, poison, or paralysis to the Pokémon."));
        Game.game.data.addAbility(new Ability(29, "Clear Body", "Prevents other Pokémon's moves or Abilities from lowering the Pokémon's stats."));
        Game.game.data.addAbility(new Ability(30, "Natural Cure", "All status conditions heal when the Pokémon switches out."));
        Game.game.data.addAbility(new Ability(31, "Lightning Rod", "The Pokémon draws in all Electric-type moves. Instead of being hit by Electric-type moves, it boosts its Sp. Atk."));
        Game.game.data.addAbility(new Ability(32, "Serene Grace", "Boosts the likelihood of additional effects occurring when attacking."));
        Game.game.data.addAbility(new Ability(33, "Swift Swim", "Boosts the Pokémon's Speed stat in rain."));
        Game.game.data.addAbility(new Ability(34, "Chlorophyll", "Boosts the Pokémon's Speed stat in harsh sunlight."));
        Game.game.data.addAbility(new Ability(35, "Illuminate", "Raises the likelihood of meeting wild Pokémon by illuminating the surroundings."));
        Game.game.data.addAbility(new Ability(36, "Trace", "When it enters a battle, the Pokémon copies an opposing Pokémon's Ability."));
        Game.game.data.addAbility(new Ability(37, "Huge Power", "Doubles the Pokémon's Attack stat."));
        Game.game.data.addAbility(new Ability(38, "Poison Point", "Contact with the Pokémon may poison the attacker."));
        Game.game.data.addAbility(new Ability(39, "Inner Focus", "The Pokémon's intensely focused, and that protects the Pokémon from flinching."));
        Game.game.data.addAbility(new Ability(40, "Magma Armor", "The Pokémon is covered with hot magma, which prevents the Pokémon from becoming frozen."));
        Game.game.data.addAbility(new Ability(41, "Water Veil", "The Pokémon is covered with a water veil, which prevents the Pokémon from getting a burn."));
        Game.game.data.addAbility(new Ability(42, "Magnet Pull", "Prevents Steel-type Pokémon from escaping using its magnetic force."));
        Game.game.data.addAbility(new Ability(43, "Soundproof", "Soundproofing gives the Pokémon full immunity to all sound-based moves."));
        Game.game.data.addAbility(new Ability(44, "Rain Dish", "The Pokémon gradually regains HP in rain."));
        Game.game.data.addAbility(new Ability(45, "Sand Stream", "The Pokémon summons a sandstorm when it enters a battle."));
        Game.game.data.addAbility(new Ability(46, "Pressure", "By putting pressure on the opposing Pokémon, it raises their PP usage."));
        Game.game.data.addAbility(new Ability(47, "Thick Fat", "The Pokémon is protected by a layer of thick fat, which halves the damage taken from Fire- and Ice-type moves."));
        Game.game.data.addAbility(new Ability(48, "Early Bird", "The Pokémon awakens from sleep twice as fast as other Pokémon."));
        Game.game.data.addAbility(new Ability(49, "Flame Body", "Contact with the Pokémon may burn the attacker."));
        Game.game.data.addAbility(new Ability(50, "Run Away", "Enables a sure getaway from wild Pokémon."));
        Game.game.data.addAbility(new Ability(51, "Keen Eye", "Keen eyes prevent other Pokémon from lowering this Pokémon's accuracy."));
        Game.game.data.addAbility(new Ability(52, "Hyper Cutter", "The Pokémon's proud of its powerful pincers. They prevent other Pokémon from lowering its Attack stat."));
        Game.game.data.addAbility(new Ability(53, "Pickup", "The Pokémon may pick up the item an opposing Pokémon used during a battle. It may pick up items outside of battle, too."));
        Game.game.data.addAbility(new Ability(54, "Truant", "The Pokémon can't use a move if it had used a move on the previous turn."));
        Game.game.data.addAbility(new Ability(55, "Hustle", "Boosts the Attack stat, but lowers accuracy."));
        Game.game.data.addAbility(new Ability(56, "Cute Charm", "Contact with the Pokémon may cause infatuation."));
        Game.game.data.addAbility(new Ability(57, "Plus", "Boosts the Sp. Atk stat of the Pokémon if an ally with the Plus or Minus Ability is also in battle."));
        Game.game.data.addAbility(new Ability(58, "Minus", "Boosts the Sp. Atk stat of the Pokémon if an ally with the Plus or Minus Ability is also in battle."));
        Game.game.data.addAbility(new Ability(59, "Forecast", "The Pokémon transforms with the weather to change its type to Water, Fire, or Ice."));
        Game.game.data.addAbility(new Ability(60, "Sticky Hold", "Items held by the Pokémon are stuck fast and cannot be removed by other Pokémon."));
        Game.game.data.addAbility(new Ability(61, "Shed Skin", "The Pokémon may heal its own status conditions by shedding its skin."));
        Game.game.data.addAbility(new Ability(62, "Guts", "It's so gutsy that having a status condition boosts the Pokémon's Attack stat."));
        Game.game.data.addAbility(new Ability(63, "Marvel Scale", "The Pokémon's marvelous scales boost the Defense stat if it has a status condition."));
        Game.game.data.addAbility(new Ability(64, "Liquid Ooze", "The oozed liquid has a strong stench, which damages attackers using any draining move."));
        Game.game.data.addAbility(new Ability(65, "Overgrow", "Powers up Grass-type moves when the Pokémon's HP is low."));
        Game.game.data.addAbility(new Ability(66, "Blaze", "Powers up Fire-type moves when the Pokémon's HP is low."));
        Game.game.data.addAbility(new Ability(67, "Torrent", "Powers up Water-type moves when the Pokémon's HP is low."));
        Game.game.data.addAbility(new Ability(68, "Swarm", "Powers up Bug-type moves when the Pokémon's HP is low."));
        Game.game.data.addAbility(new Ability(69, "Rock Head", "Protects the Pokémon from recoil damage."));
        Game.game.data.addAbility(new Ability(70, "Drought", "Turns the sunlight harsh when the Pokémon enters a battle."));
        Game.game.data.addAbility(new Ability(71, "Arena Trap", "Prevents opposing Pokémon from fleeing."));
        Game.game.data.addAbility(new Ability(72, "Vital Spirit", "The Pokémon is full of vitality, and that prevents it from falling asleep."));
        Game.game.data.addAbility(new Ability(73, "White Smoke", "The Pokémon is protected by its white smoke, which prevents other Pokémon from lowering its stats."));
        Game.game.data.addAbility(new Ability(74, "Pure Power", "Using its pure power, the Pokémon doubles its Attack stat."));
        Game.game.data.addAbility(new Ability(75, "Shell Armor", "A hard shell protects the Pokémon from critical hits."));
        Game.game.data.addAbility(new Ability(76, "Air Lock", "Eliminates the effects of weather."));
        Game.game.data.addAbility(new Ability(77, "Tangled Feet", "Raises evasiveness if the Pokémon is confused."));
        Game.game.data.addAbility(new Ability(78, "Motor Drive", "Boosts its Speed stat if hit by an Electric-type move instead of taking damage."));
        Game.game.data.addAbility(new Ability(79, "Rivalry", "Becomes competitive and deals more damage to Pokémon of the same gender, but deals less to Pokémon of the opposite gender."));
        Game.game.data.addAbility(new Ability(80, "Steadfast", "The Pokémon's determination boosts the Speed stat each time the Pokémon flinches."));
        Game.game.data.addAbility(new Ability(81, "Snow Cloak", "Boosts evasiveness in a hailstorm."));
        Game.game.data.addAbility(new Ability(82, "Gluttony", "Makes the Pokémon eat a held Berry when its HP drops to half or less, which is sooner than usual."));
        Game.game.data.addAbility(new Ability(83, "Anger Point", "The Pokémon is angered when it takes a critical hit, and that maxes its Attack stat."));
        Game.game.data.addAbility(new Ability(84, "Unburden", "Boosts the Speed stat if the Pokémon's held item is used or lost."));
        Game.game.data.addAbility(new Ability(85, "Heatproof", "The heatproof body of the Pokémon halves the damage from Fire-type moves that hit it."));
        Game.game.data.addAbility(new Ability(86, "Simple", "The stat changes the Pokémon receives are doubled."));
        Game.game.data.addAbility(new Ability(87, "Dry Skin", "Restores HP in rain or when hit by Water-type moves. Reduces HP in harsh sunlight, and increases the damage received from Fire-type moves."));
        Game.game.data.addAbility(new Ability(88, "Download", "Compares an opposing Pokémon's Defense and Sp. Def stats before raising its own Attack or Sp. Atk stat—whichever will be more effective."));
        Game.game.data.addAbility(new Ability(89, "Iron Fist", "Powers up punching moves."));
        Game.game.data.addAbility(new Ability(90, "Poison Heal", "Restores HP if the Pokémon is poisoned instead of losing HP."));
        Game.game.data.addAbility(new Ability(91, "Adaptability", "Powers up moves of the same type as the Pokémon."));
        Game.game.data.addAbility(new Ability(92, "Skill Link", "Maximizes the number of times multistrike moves hit."));
        Game.game.data.addAbility(new Ability(93, "Hydration", "Heals status conditions if it's raining."));
        Game.game.data.addAbility(new Ability(94, "Solar Power", "Boosts the Sp. Atk stat in harsh sunlight, but HP decreases every turn."));
        Game.game.data.addAbility(new Ability(95, "Quick Feet", "Boosts the Speed stat if the Pokémon has a status condition."));
        Game.game.data.addAbility(new Ability(96, "Normalize", "All the Pokémon's moves become Normal type. The power of those moves is boosted a little."));
        Game.game.data.addAbility(new Ability(97, "Sniper", "Powers up moves if they become critical hits when attacking."));
        Game.game.data.addAbility(new Ability(98, "Magic Guard", "The Pokémon only takes damage from attacks."));
        Game.game.data.addAbility(new Ability(99, "No Guard", "The Pokémon employs no-guard tactics to ensure incoming and outgoing attacks always land."));
        Game.game.data.addAbility(new Ability(100, "Stall", "The Pokémon moves after all other Pokémon do."));
        Game.game.data.addAbility(new Ability(101, "Technician", "Powers up the Pokémon's weaker moves."));
        Game.game.data.addAbility(new Ability(102, "Leaf Guard", "Prevents status conditions in harsh sunlight."));
        Game.game.data.addAbility(new Ability(103, "Klutz", "The Pokémon can't use any held items."));
        Game.game.data.addAbility(new Ability(104, "Mold Breaker", "Moves can be used on the target regardless of its Abilities."));
        Game.game.data.addAbility(new Ability(105, "Super Luck", "The Pokémon is so lucky that the critical-hit ratios of its moves are boosted."));
        Game.game.data.addAbility(new Ability(106, "Aftermath", "Damages the attacker if it contacts the Pokémon with a finishing hit."));
        Game.game.data.addAbility(new Ability(107, "Anticipation", "The Pokémon can sense an opposing Pokémon's dangerous moves."));
        Game.game.data.addAbility(new Ability(108, "Forewarn", "When it enters a battle, the Pokémon can tell one of the moves an opposing Pokémon has."));
        Game.game.data.addAbility(new Ability(109, "Unaware", "When attacking, the Pokémon ignores the target Pokémon's stat changes."));
        Game.game.data.addAbility(new Ability(110, "Tinted Lens", "The Pokémon can use \"not very effective\" moves to deal regular damage."));
        Game.game.data.addAbility(new Ability(111, "Filter", "Reduces the power of supereffective attacks taken."));
        Game.game.data.addAbility(new Ability(112, "Slow Start", "For five turns, the Pokémon's Attack and Speed stats are halved."));
        Game.game.data.addAbility(new Ability(113, "Scrappy", "The Pokémon can hit Ghost-type Pokémon with Normal- and Fighting-type moves."));
        Game.game.data.addAbility(new Ability(114, "Storm Drain", "Draws in all Water-type moves. Instead of being hit by Water-type moves, it boosts its Sp. Atk."));
        Game.game.data.addAbility(new Ability(115, "Ice Body", "The Pokémon gradually regains HP in a hailstorm."));
        Game.game.data.addAbility(new Ability(116, "Solid Rock", "Reduces the power of supereffective attacks taken."));
        Game.game.data.addAbility(new Ability(117, "Snow Warning", "The Pokémon summons a hailstorm when it enters a battle."));
        Game.game.data.addAbility(new Ability(118, "Honey Gather", "The Pokémon may gather Honey after a battle."));
        Game.game.data.addAbility(new Ability(119, "Frisk", "When it enters a battle, the Pokémon can check an opposing Pokémon's held item."));
        Game.game.data.addAbility(new Ability(120, "Reckless", "Powers up moves that have recoil damage."));
        Game.game.data.addAbility(new Ability(121, "Multitype", "Changes the Pokémon's type to match the Plate or Z-Crystal it holds."));
        Game.game.data.addAbility(new Ability(122, "Flower Gift", "Boosts the Attack and Sp. Def stats of itself and allies in harsh sunlight."));
        Game.game.data.addAbility(new Ability(123, "Bad Dreams", "Reduces the HP of sleeping opposing Pokémon."));
        Game.game.data.addAbility(new Ability(124, "Pickpocket", "Steals an item from an attacker that made direct contact."));
        Game.game.data.addAbility(new Ability(125, "Sheer Force", "Removes additional effects to increase the power of moves when attacking."));
        Game.game.data.addAbility(new Ability(126, "Contrary", "Makes stat changes have an opposite effect."));
        Game.game.data.addAbility(new Ability(127, "Unnerve", "Unnerves opposing Pokémon and makes them unable to eat Berries."));
        Game.game.data.addAbility(new Ability(128, "Defiant", "Boosts the Pokémon's Attack stat sharply when its stats are lowered."));
        Game.game.data.addAbility(new Ability(129, "Defeatist", "Halves the Pokémon's Attack and Sp. Atk stats when its HP becomes half or less."));
        Game.game.data.addAbility(new Ability(130, "Cursed Body", "May disable a move used on the Pokémon."));
        Game.game.data.addAbility(new Ability(131, "Healer", "Sometimes heals an ally's status condition."));
        Game.game.data.addAbility(new Ability(132, "Friend Guard", "Reduces damage done to allies."));
        Game.game.data.addAbility(new Ability(133, "Weak Armor", "Physical attacks to the Pokémon lower its Defense stat but sharply raise its Speed stat."));
        Game.game.data.addAbility(new Ability(134, "Heavy Metal", "Doubles the Pokémon's weight."));
        Game.game.data.addAbility(new Ability(135, "Light Metal", "Halves the Pokémon's weight."));
        Game.game.data.addAbility(new Ability(136, "Multiscale", "Reduces the amount of damage the Pokémon takes while its HP is full."));
        Game.game.data.addAbility(new Ability(137, "Toxic Boost", "Powers up physical attacks when the Pokémon is poisoned."));
        Game.game.data.addAbility(new Ability(138, "Flare Boost", "Powers up special attacks when the Pokémon is burned."));
        Game.game.data.addAbility(new Ability(139, "Harvest", "May create another Berry after one is used."));
        Game.game.data.addAbility(new Ability(140, "Telepathy", "Anticipates an ally's attack and dodges it."));
        Game.game.data.addAbility(new Ability(141, "Moody", "Raises one stat sharply and lowers another every turn."));
        Game.game.data.addAbility(new Ability(142, "Overcoat", "Protects the Pokémon from things like sand, hail, and powder."));
        Game.game.data.addAbility(new Ability(143, "Poison Touch", "May poison a target when the Pokémon makes contact."));
        Game.game.data.addAbility(new Ability(144, "Regenerator", "Restores a little HP when withdrawn from battle."));
        Game.game.data.addAbility(new Ability(145, "Big Pecks", "Protects the Pokémon from Defense-lowering effects."));
        Game.game.data.addAbility(new Ability(146, "Sand Rush", "Boosts the Pokémon's Speed stat in a sandstorm."));
        Game.game.data.addAbility(new Ability(147, "Wonder Skin", "Makes status moves more likely to miss."));
        Game.game.data.addAbility(new Ability(148, "Analytic", "Boosts move power when the Pokémon moves last."));
        Game.game.data.addAbility(new Ability(149, "Illusion", "Comes out disguised as the Pokémon in the party's last spot."));
        Game.game.data.addAbility(new Ability(150, "Imposter", "The Pokémon transforms itself into the Pokémon it's facing."));
        Game.game.data.addAbility(new Ability(151, "Infiltrator", "Passes through the opposing Pokémon's barrier, substitute, and the like and strikes."));
        Game.game.data.addAbility(new Ability(152, "Mummy", "Contact with the Pokémon changes the attacker's Ability to Mummy."));
        Game.game.data.addAbility(new Ability(153, "Moxie", "The Pokémon shows moxie, and that boosts the Attack stat after knocking out any Pokémon."));
        Game.game.data.addAbility(new Ability(154, "Justified", "Being hit by a Dark-type move boosts the Attack stat of the Pokémon, for justice."));
        Game.game.data.addAbility(new Ability(155, "Rattled", "Dark-, Ghost-, and Bug-type moves scare the Pokémon and boost its Speed stat."));
        Game.game.data.addAbility(new Ability(156, "Magic Bounce", "Reflects status moves instead of getting hit by them."));
        Game.game.data.addAbility(new Ability(157, "Sap Sipper", "Boosts the Attack stat if hit by a Grass-type move instead of taking damage."));
        Game.game.data.addAbility(new Ability(158, "Prankster", "Gives priority to a status move."));
        Game.game.data.addAbility(new Ability(159, "Sand Force", "Boosts the power of Rock-, Ground-, and Steel-type moves in a sandstorm."));
        Game.game.data.addAbility(new Ability(160, "Iron Barbs", "Inflicts damage on the attacker upon contact with iron barbs."));
        Game.game.data.addAbility(new Ability(161, "Zen Mode", "Changes the Pokémon's shape when HP is half or less."));
        Game.game.data.addAbility(new Ability(162, "Victory Star", "Boosts the accuracy of its allies and itself."));
        Game.game.data.addAbility(new Ability(163, "Turboblaze", "Moves can be used on the target regardless of its Abilities."));
        Game.game.data.addAbility(new Ability(164, "Teravolt", "Moves can be used on the target regardless of its Abilities."));
        Game.game.data.addAbility(new Ability(165, "Aroma Veil", "Protects itself and its allies from attacks that limit their move choices."));
        Game.game.data.addAbility(new Ability(166, "Flower Veil", "Ally Grass-type Pokémon are protected from status conditions and the lowering of their stats."));
        Game.game.data.addAbility(new Ability(167, "Cheek Pouch", "Restores HP as well when the Pokémon eats a Berry."));
        Game.game.data.addAbility(new Ability(168, "Protean", "Changes the Pokémon's type to the type of the move it's about to use."));
        Game.game.data.addAbility(new Ability(169, "Fur Coat", "Halves the damage from physical moves."));
        Game.game.data.addAbility(new Ability(170, "Magician", "The Pokémon steals the held item of a Pokémon it hits with a move."));
        Game.game.data.addAbility(new Ability(171, "Bulletproof", "Protects the Pokémon from some ball and bomb moves."));
        Game.game.data.addAbility(new Ability(172, "Competitive", "Boosts the Sp. Atk stat sharply when a stat is lowered."));
        Game.game.data.addAbility(new Ability(173, "Strong Jaw", "The Pokémon's strong jaw boosts the power of its biting moves."));
        Game.game.data.addAbility(new Ability(174, "Refrigerate", "Normal-type moves become Ice-type moves. The power of those moves is boosted a little."));
        Game.game.data.addAbility(new Ability(175, "Sweet Veil", "Prevents itself and ally Pokémon from falling asleep."));
        Game.game.data.addAbility(new Ability(176, "Stance Change", "The Pokémon changes its form to Blade Forme when it uses an attack move and changes to Shield Forme when it uses King's Shield."));
        Game.game.data.addAbility(new Ability(177, "Gale Wings", "Gives priority to Flying-type moves when the Pokémon's HP is full."));
        Game.game.data.addAbility(new Ability(178, "Mega Launcher", "Powers up aura and pulse moves."));
        Game.game.data.addAbility(new Ability(179, "Grass Pelt", "Boosts the Pokémon's Defense stat on Grassy Terrain."));
        Game.game.data.addAbility(new Ability(180, "Symbiosis", "The Pokémon passes its item to an ally that has used up an item."));
        Game.game.data.addAbility(new Ability(181, "Tough Claws", "Powers up moves that make direct contact."));
        Game.game.data.addAbility(new Ability(182, "Pixilate", "Normal-type moves become Fairy-type moves. The power of those moves is boosted a little."));
        Game.game.data.addAbility(new Ability(183, "Gooey", "Contact with the Pokémon lowers the attacker's Speed stat."));
        Game.game.data.addAbility(new Ability(184, "Aerilate", "Normal-type moves become Flying-type moves. The power of those moves is boosted a little."));
        Game.game.data.addAbility(new Ability(185, "Parental Bond", "Parent and child each attacks."));
        Game.game.data.addAbility(new Ability(186, "Dark Aura", "Powers up each Pokémon's Dark-type moves."));
        Game.game.data.addAbility(new Ability(187, "Fairy Aura", "Powers up each Pokémon's Fairy-type moves."));
        Game.game.data.addAbility(new Ability(188, "Aura Break", "The effects of \"Aura\" Abilities are reversed to lower the power of affected moves."));
        Game.game.data.addAbility(new Ability(189, "Primordial Sea", "The Pokémon changes the weather to nullify Fire-type attacks."));
        Game.game.data.addAbility(new Ability(190, "Desolate Land", "The Pokémon changes the weather to nullify Water-type attacks."));
        Game.game.data.addAbility(new Ability(191, "Delta Stream", "The Pokémon changes the weather to eliminate all of the Flying type's weaknesses."));
        Game.game.data.addAbility(new Ability(192, "Stamina", "Boosts the Defense stat when hit by an attack."));
        Game.game.data.addAbility(new Ability(193, "Wimp Out", "The Pokémon cowardly switches out when its HP becomes half or less."));
        Game.game.data.addAbility(new Ability(194, "Emergency Exit", "The Pokémon, sensing danger, switches out when its HP becomes half or less."));
        Game.game.data.addAbility(new Ability(195, "Water Compaction", "Boosts the Pokémon's Defense stat sharply when hit by a Water-type move."));
        Game.game.data.addAbility(new Ability(196, "Merciless", "The Pokémon's attacks become critical hits if the target is poisoned."));
        Game.game.data.addAbility(new Ability(197, "Shields Down", "When its HP becomes half or less, the Pokémon's shell breaks and it becomes aggressive."));
        Game.game.data.addAbility(new Ability(198, "Stakeout", "Doubles the damage dealt to the target's replacement if the target switches out."));
        Game.game.data.addAbility(new Ability(199, "Water Bubble", "Lowers the power of Fire-type moves done to the Pokémon and prevents the Pokémon from getting a burn."));
        Game.game.data.addAbility(new Ability(200, "Steelworker", "Powers up Steel-type moves."));
        Game.game.data.addAbility(new Ability(201, "Berserk", "Boosts the Pokémon's Sp. Atk stat when it takes a hit that causes its HP to become half or less."));
        Game.game.data.addAbility(new Ability(202, "Slush Rush", "Boosts the Pokémon's Speed stat in a hailstorm."));
        Game.game.data.addAbility(new Ability(203, "Long Reach", "The Pokémon uses its moves without making contact with the target."));
        Game.game.data.addAbility(new Ability(204, "Liquid Voice", "All sound-based moves become Water-type moves."));
        Game.game.data.addAbility(new Ability(205, "Triage", "Gives priority to a healing move."));
        Game.game.data.addAbility(new Ability(206, "Galvanize", "Normal-type moves become Electric-type moves. The power of those moves is boosted a little."));
        Game.game.data.addAbility(new Ability(207, "Surge Surfer", "Doubles the Pokémon's Speed stat on Electric Terrain."));
        Game.game.data.addAbility(new Ability(208, "Schooling", "When it has a lot of HP, the Pokémon forms a powerful school. It stops schooling when its HP is low."));
        Game.game.data.addAbility(new Ability(209, "Disguise", "Once per battle, the shroud that covers the Pokémon can protect it from an attack."));
        Game.game.data.addAbility(new Ability(210, "Battle Bond", "Defeating an opposing Pokémon strengthens the Pokémon's bond with its Trainer, and it becomes Ash-Greninja. Water Shuriken gets more powerful."));
        Game.game.data.addAbility(new Ability(211, "Power Construct", "Other Cells gather to aid when its HP becomes half or less. Then the Pokémon changes its form to Complete Forme."));
        Game.game.data.addAbility(new Ability(212, "Corrosion", "The Pokémon can poison the target even if it's a Steel or Poison type."));
        Game.game.data.addAbility(new Ability(213, "Comatose", "It's always drowsing and will never wake up. It can attack without waking up."));
        Game.game.data.addAbility(new Ability(214, "Queenly Majesty", "Its majesty pressures the opposing Pokémon, making it unable to attack using priority moves."));
        Game.game.data.addAbility(new Ability(215, "Innards Out", "Damages the attacker landing the finishing hit by the amount equal to its last HP."));
        Game.game.data.addAbility(new Ability(216, "Dancer", "When another Pokémon uses a dance move, it can use a dance move following it regardless of its Speed."));
        Game.game.data.addAbility(new Ability(217, "Battery", "Powers up ally Pokémon's special moves."));
        Game.game.data.addAbility(new Ability(218, "Fluffy", "Halves the damage taken from moves that make direct contact, but doubles that of Fire-type moves."));
        Game.game.data.addAbility(new Ability(219, "Dazzling", "Surprises the opposing Pokémon, making it unable to attack using priority moves."));
        Game.game.data.addAbility(new Ability(220, "Soul-Heart", "Boosts its Sp. Atk stat every time a Pokémon faints."));
        Game.game.data.addAbility(new Ability(221, "Tangling Hair", "Contact with the Pokémon lowers the attacker's Speed stat."));
        Game.game.data.addAbility(new Ability(222, "Receiver", "The Pokémon copies the Ability of a defeated ally."));
        Game.game.data.addAbility(new Ability(223, "Power of Alchemy", "The Pokémon copies the Ability of a defeated ally."));
        Game.game.data.addAbility(new Ability(224, "Beast Boost", "The Pokémon boosts its most proficient stat each time it knocks out a Pokémon."));
        Game.game.data.addAbility(new Ability(225, "RKS System", "Changes the Pokémon's type to match the memory disc it holds."));
        Game.game.data.addAbility(new Ability(226, "Electric Surge", "Turns the ground into Electric Terrain when the Pokémon enters a battle."));
        Game.game.data.addAbility(new Ability(227, "Psychic Surge", "Turns the ground into Psychic Terrain when the Pokémon enters a battle."));
        Game.game.data.addAbility(new Ability(228, "Misty Surge", "Turns the ground into Misty Terrain when the Pokémon enters a battle."));
        Game.game.data.addAbility(new Ability(229, "Grassy Surge", "Turns the ground into Grassy Terrain when the Pokémon enters a battle."));
        Game.game.data.addAbility(new Ability(230, "Full Metal Body", "Prevents other Pokémon's moves or Abilities from lowering the Pokémon's stats."));
        Game.game.data.addAbility(new Ability(231, "Shadow Shield", "Reduces the amount of damage the Pokémon takes while its HP is full."));
        Game.game.data.addAbility(new Ability(232, "Prism Armor", "Reduces the power of supereffective attacks taken."));
        Game.game.data.addAbility(new Ability(233, "Neuroforce", "Powers up moves that are super effective."));
        Game.game.data.addAbility(new Ability(234, "Intrepid Sword", "Boosts the Pokémon's Attack stat when the Pokémon enters a battle."));
        Game.game.data.addAbility(new Ability(235, "Dauntless Shield", "Boosts the Pokémon's Defense stat when the Pokémon enters a battle."));
        Game.game.data.addAbility(new Ability(236, "Libero", "Changes the Pokémon's type to the type of the move it's about to use."));
        Game.game.data.addAbility(new Ability(237, "Ball Fetch", "If the Pokémon is not holding an item, it will fetch the Poké Ball from the first failed throw of the battle."));
        Game.game.data.addAbility(new Ability(238, "Cotton Down", "When the Pokémon is hit by an attack, it scatters cotton fluff around and lowers the Speed stat of all Pokémon except itself."));
        Game.game.data.addAbility(new Ability(239, "Propeller Tail", "Ignores the effects of opposing Pokémon's Abilities and moves that draw in moves."));
        Game.game.data.addAbility(new Ability(240, "Mirror Armor", "Bounces back only the stat-lowering effects that the Pokémon receives."));
        Game.game.data.addAbility(new Ability(241, "Gulp Missile", "When the Pokémon uses Surf or Dive, it will come back with prey. When it takes damage, it will spit out the prey to attack."));
        Game.game.data.addAbility(new Ability(242, "Stalwart", "Ignores the effects of opposing Pokémon's Abilities and moves that draw in moves."));
        Game.game.data.addAbility(new Ability(243, "Steam Engine", "Boosts the Pokémon's Speed stat drastically if hit by a Fire- or Water-type move."));
        Game.game.data.addAbility(new Ability(244, "Punk Rock", "Boosts the power of sound-based moves. The Pokémon also takes half the damage from these kinds of moves."));
        Game.game.data.addAbility(new Ability(245, "Sand Spit", "The Pokémon creates a sandstorm when it's hit by an attack."));
        Game.game.data.addAbility(new Ability(246, "Ice Scales", "The Pokémon is protected by ice scales, which halve the damage taken from special moves."));
        Game.game.data.addAbility(new Ability(247, "Ripen", "Ripens Berries and doubles their effect."));
        Game.game.data.addAbility(new Ability(248, "Ice Face", "The Pokémon's ice head can take a physical attack as a substitute, but the attack also changes the Pokémon's appearance. The ice will be restored when it hails."));
        Game.game.data.addAbility(new Ability(249, "Power Spot", "Just being next to the Pokémon powers up moves."));
        Game.game.data.addAbility(new Ability(250, "Mimicry", "Changes the Pokémon's type depending on the terrain."));
        Game.game.data.addAbility(new Ability(251, "Screen Cleaner", "When the Pokémon enters a battle, the effects of Light Screen, Reflect, and Aurora Veil are nullified for both opposing and ally Pokémon."));
        Game.game.data.addAbility(new Ability(252, "Steely Spirit", "Powers up ally Pokémon's Steel-type moves."));
        Game.game.data.addAbility(new Ability(253, "Perish Body", "When hit by a move that makes direct contact, the Pokémon and the attacker will faint after three turns unless they switch out of battle."));
        Game.game.data.addAbility(new Ability(254, "Wandering Spirit", "The Pokémon exchanges Abilities with a Pokémon that hits it with a move that makes direct contact."));
        Game.game.data.addAbility(new Ability(255, "Gorilla Tactics", "Boosts the Pokémon's Attack stat but only allows the use of the first selected move."));
        Game.game.data.addAbility(new Ability(256, "Neutralizing Gas", "If the Pokémon with Neutralizing Gas is in the battle, the effects of all Pokémon's Abilities will be nullified or will not be triggered."));
        Game.game.data.addAbility(new Ability(257, "Pastel Veil", "Protects the Pokémon and its ally Pokémon from being poisoned."));
        Game.game.data.addAbility(new Ability(258, "Hunger Switch", "The Pokémon changes its form, alternating between its Full Belly Mode and Hangry Mode after the end of each turn."));
        Game.game.data.addAbility(new Ability(259, "Cacophony", "Avoids sound-based moves. (not implemented in game)"));
        Game.game.data.addAbility(new Ability(260, "Unseen Fist", "Currently unknown."));
    }
    private void addItems() {

    }
    private void finalizeLoad() {
        Game.game.canvas.setupFont();
        Game.game.data.player.party[0] = new PartyPokemon(Game.game.data.getRandomSpecies(), 15);
        Game.game.data.player.party[1] = new PartyPokemon(Game.game.data.getRandomSpecies(), 15);

        Game.game.switchScreens(new TitleScreen());
    }
}
