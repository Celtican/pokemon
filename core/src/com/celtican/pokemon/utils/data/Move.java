package com.celtican.pokemon.utils.data;

import com.celtican.pokemon.Game;
import com.celtican.pokemon.battle.BattlePokemon;

public class Move {

    public boolean crit         ; // has 1 stage higher chance to crit
    public boolean multi        ; // hits 2-5 times, same rates as Rock Blast and friends
    public boolean doubleHit    ; // hits 2 times, e.g. dual chop
    public boolean powder       ; // grass types, pokemon with overcoat, and pokemon holding safety goggles are immune. e.g. spore, poison powder

    public boolean authentic    ; // Ignores a target's substitute.
    public boolean charge       ; // The user is unable to make a move between turns.
    public boolean contact      ; // Makes contact.
    public boolean defrost      ; // Thaws the user if executed successfully while the user is frozen.
    public boolean distance     ; // Can target a Pokemon positioned anywhere in a Triple Battle.
    public boolean gravity      ; // Prevented from being executed or selected during Gravity's effect.
    public boolean heal         ; // Prevented from being executed or selected during Heal Block's effect.
    public boolean mirror       ; // Can be copied by Mirror Move.
    public boolean nonsky       ; // Prevented from being executed or selected in a Sky Battle.
    public boolean protect      ; // Blocked by Detect, Protect, Spiky Shield, and if not a Status move, King's Shield.
    public boolean punch        ; // Power is multiplied by 1.2 when used by a Pokemon with the Ability Iron Fist.
    public boolean recharge     ; // If this move is successful, the user must recharge on the following turn and cannot make a move.
    public boolean reflectable  ; // Bounced back to the original user by Magic Coat or the Ability Magic Bounce.
    public boolean snatch       ; // Can be stolen from the original user and instead used by another Pokemon using Snatch.
    public boolean sound        ; // Has no effect on Pokemon with the Ability Soundproof.
    public boolean dance        ; // Can be copied by Dancer.
    public boolean sideanim     ; // This move animates even when used against an ally.
    public boolean bullet       ; // has no affect on pokemon with bulletproof
    public boolean pulse        ; // whether or not this move is boosted by Aura whatever ability

    // todo change int to byte
    public final int index;
    public final String name;
    public final Pokemon.Type type;
    public final Pokemon.MoveCategory category;
    public final Pokemon.ContestType contest;
    public final Pokemon.MoveTargets targets;
    public final int basePP; // this is the final PP divided by 5. For example, Mega Punch's final PP is 20 without PP Ups, so its basePP is 4
    public final int basePower;
    public final int accuracy;
    public final Effect effect;

    public boolean isOHKO() {
        switch (index) {
            case 12: return true; // guillotine
            default: return false;
        }
    }
    public int getPriority() {
        switch (index) {
            case 182: // protect
                return 4;
            default: // everything else
                return 0;
            case 18: // whirlwind
                return -6;
        }
    }

    public Move(int index, String name, Pokemon.Type type, Pokemon.MoveCategory category, Pokemon.ContestType contest,
                Pokemon.MoveTargets targets, int basePP, int basePower, int accuracy, String flags, Effect effect) {
        this.index = index;
        this.name = name;
        this.type = type;
        this.category = category;
        this.contest = contest;
        this.targets = targets;
        this.basePP = basePP;
        this.basePower = basePower;
        this.accuracy = accuracy;
        this.effect = effect;
        if (flags == null) return;
        for (String s : flags.split(",")) switch (s.trim().toLowerCase()) {
            case "crit": crit = true; break;
            case "multi": multi = true; break;
            case "doublehit": doubleHit = true; break;
            case "powder": powder = true; break;

            case "authentic": authentic = true; break;
            case "charge": charge = true; break;
            case "contact": contact = true; break;
            case "defrost": defrost = true; break;
            case "distance": distance = true; break;
            case "gravity": gravity = true; break;
            case "heal": heal = true; break;
            case "mirror": mirror = true; break;
            case "nonsky": nonsky = true; break;
            case "protect": protect = true; break;
            case "punch": punch = true; break;
            case "recharge": recharge = true; break;
            case "reflectable": reflectable = true; break;
            case "snatch": snatch = true; break;
            case "sound": sound = true; break;
            case "dance": dance = true; break;
            case "sideanim": sideanim = true; break;
            case "bullet": bullet = true; break;
            case "pulse": pulse = true; break;

            default: Game.logError("Move flag \"" + s + "\" does not exist.");
        }
//        this.crit = crit;
//        this.multi = multi;
//        this.doubleHit = doubleHit;
//        this.authentic = authentic;
//        this.charge = charge;
//        this.contact = contact;
//        this.defrost = defrost;
//        this.distance = distance;
//        this.gravity = gravity;
//        this.heal = heal;
//        this.mirror = mirror;
//        this.nonsky = nonsky;
//        this.protect = protect;
//        this.punch = punch;
//        this.recharge = recharge;
//        this.reflectable = reflectable;
//        this.snatch = snatch;
//        this.sound = sound;
//        this.dance = dance;
//        this.sideanim = sideanim;
//        this.powder = powder;
//        this.bullet = bullet;
//        this.pulse = pulse;
    }

    public static abstract class Effect {
        public final int chance;
        public Effect(int chance) {
            this.chance = chance;
        }
    }
    public static class EffectMultiple extends Effect {
        public final Effect[] effects;
        public EffectMultiple(int chance, Effect effect1, Effect effect2) {
            this(chance, new Effect[] {effect1, effect2});
        }
        public EffectMultiple(int chance, Effect[] effects) {
            super(chance);
            this.effects = effects;
        }
    }
    public static class EffectStatusCondition extends Effect {
        public final Pokemon.StatusCondition statusCondition;
        public EffectStatusCondition(int chance, Pokemon.StatusCondition statusCondition) {
            super(chance);
            this.statusCondition = statusCondition;
        }
    }
    public static class EffectBoostSelfStats extends Effect {
        public final int atk;
        public final int def;
        public final int spa;
        public final int spd;
        public final int spe;
        public final int acc;
        public final int eva;
        public EffectBoostSelfStats(int chance, int atk, int def, int spa, int spd, int spe, int acc, int eva) {
            super(chance);
            this.atk = atk;
            this.def = def;
            this.spa = spa;
            this.spd = spd;
            this.spe = spe;
            this.acc = acc;
            this.eva = eva;
        }
    }
    public static class EffectBoostTargetStats extends Effect {
        public final int atk;
        public final int def;
        public final int spa;
        public final int spd;
        public final int spe;
        public final int acc;
        public final int eva;
        public EffectBoostTargetStats(int chance, int atk, int def, int spa, int spd, int spe, int acc, int eva) {
            super(chance);
            this.atk = atk;
            this.def = def;
            this.spa = spa;
            this.spd = spd;
            this.spe = spe;
            this.acc = acc;
            this.eva = eva;
        }
    }
    public static class EffectRemoveDefenderEffectsWithFlag extends Effect {
        public final BattlePokemon.EffectFlag flag;
        public EffectRemoveDefenderEffectsWithFlag(int chance, BattlePokemon.EffectFlag flag) {
            super(chance);
            this.flag = flag;
        }
    }
    public static class EffectRemoveUserEffectsWithFlag extends Effect {
        public final BattlePokemon.EffectFlag flag;
        public EffectRemoveUserEffectsWithFlag(int chance, BattlePokemon.EffectFlag flag) {
            super(chance);
            this.flag = flag;
        }
    }
    public static class EffectAddEffectToDefender extends Effect {
        public final BattlePokemon.Effect effect;
        public EffectAddEffectToDefender(int chance, BattlePokemon.Effect effect) {
            super(chance);
            this.effect = effect;
        }
    }
    public static class EffectConfuse extends Effect {
        public EffectConfuse(int chance) {
            super(chance);
        }
    }
}
