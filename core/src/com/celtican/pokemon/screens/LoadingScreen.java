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
import com.celtican.pokemon.utils.data.Pokemon.*;
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
        Game.game.data.addSpecies("Nullomon", GenderRatio.GENDERLESS, Type.NORMAL, Type.NORMAL, EggGroup.UNDISCOVERED, EggGroup.UNDISCOVERED, ExpGrowth.SLOW,
                0, 0, 0, 0, 20, 20, 20, 20, 20, 20, 1, 1, 1, 0, 0, 0, 0, 0, 0);
        Game.game.data.addSpecies("Bulbasaur", GenderRatio.FEMALE_IS_RARE, Type.GRASS, Type.POISON, EggGroup.MONSTER, EggGroup.GRASS, ExpGrowth.MEDIUM_SLOW,
                65, 65, 34, 34, 45, 49, 49, 65, 65, 45, 45, 50, 46, 0, 0, 0, 1, 0, 0);
        Game.game.data.addSpecies("Ivysaur", GenderRatio.FEMALE_IS_RARE, Type.GRASS, Type.POISON, EggGroup.MONSTER, EggGroup.GRASS, ExpGrowth.MEDIUM_SLOW,
                65, 65, 34, 34, 60, 62, 63, 80, 80, 60, 45, 50, 142, 0, 0, 0, 1, 0, 0);
        Game.game.data.addSpecies("Venusaur", GenderRatio.FEMALE_IS_RARE, Type.GRASS, Type.POISON, EggGroup.MONSTER, EggGroup.GRASS, ExpGrowth.MEDIUM_SLOW,
                65, 65, 34, 34, 80, 82, 83, 100, 100, 80, 45, 50, 236, 0, 0, 0, 2, 1, 0);
    }
    private void addMoves() {
        Game.game.data.addMove(new Move(0, "Null Blitz", Type.NORMAL, MoveCategory.PHYSICAL, ContestType.CLEVER, MoveTargets.ADJACENT, 8, 40, 90,
                new int[0], "protect, mirror"));
        Game.game.data.addMove(new Move(1, "Pound", Type.NORMAL, MoveCategory.PHYSICAL, ContestType.TOUGH, MoveTargets.ADJACENT, 7, 40, 100,
                new int[0], "contact, protect, mirror"));
        Game.game.data.addMove(new Move(2, "Karate Chop", Type.FIGHTING, MoveCategory.PHYSICAL, ContestType.TOUGH, MoveTargets.ADJACENT, 5, 50, 100,
                new int[0], "contact, protect, mirror, crit"));
        Game.game.data.addMove(new Move(3, "Double Slap", Type.NORMAL, MoveCategory.PHYSICAL, ContestType.CUTE, MoveTargets.ADJACENT, 2, 15, 85,
                new int[0], "contact, protect, mirror, multi"));
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

        Game.game.switchScreens(new MainMenuScreen());
    }
}
