package com.celtican.pokemon.utils.data;

public class Move {

    public final boolean crit         ; // has 1 stage higher chance to crit
    public final boolean multi        ; // hits 2-5 times, same rates as Rock Blast and friends
    public final boolean doubleHit    ; // hits 2 times, e.g. dual chop
    public final boolean powder       ; // grass types, pokemon with overcoat, and pokemon holding safety goggles are immune. e.g. spore, poison powder

    public final boolean authentic    ; // Ignores a target's substitute.
    public final boolean charge       ; // The user is unable to make a move between turns.
    public final boolean contact      ; // Makes contact.
    public final boolean defrost      ; // Thaws the user if executed successfully while the user is frozen.
    public final boolean distance     ; // Can target a Pokemon positioned anywhere in a Triple Battle.
    public final boolean gravity      ; // Prevented from being executed or selected during Gravity's effect.
    public final boolean heal         ; // Prevented from being executed or selected during Heal Block's effect.
    public final boolean mirror       ; // Can be copied by Mirror Move.
    public final boolean nonsky       ; // Prevented from being executed or selected in a Sky Battle.
    public final boolean protect      ; // Blocked by Detect, Protect, Spiky Shield, and if not a Status move, King's Shield.
    public final boolean punch        ; // Power is multiplied by 1.2 when used by a Pokemon with the Ability Iron Fist.
    public final boolean recharge     ; // If this move is successful, the user must recharge on the following turn and cannot make a move.
    public final boolean reflectable  ; // Bounced back to the original user by Magic Coat or the Ability Magic Bounce.
    public final boolean snatch       ; // Can be stolen from the original user and instead used by another Pokemon using Snatch.
    public final boolean sound        ; // Has no effect on Pokemon with the Ability Soundproof.
    public final boolean dance        ; // Can be copied by Dancer.
    public final boolean sideanim     ; // This move animates even when used against an ally.
    public final boolean bullet       ; // has no affect on pokemon with bulletproof
    public final boolean pulse        ; // whether or not this move is boosted by Aura whatever ability

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
    public final int[] effects;

    /*

    (chance)(effect)(chance)(effect)

    chance: value from 1-100 that the following effect will activate
    effect:
    - -3, flinch
    - -2, confuse
    - -1,
    - 0, burn
    - 1, frozen
    - 2, paralyze
    - 3, asleep
    - 4, poison
    - 5, toxic
        The following are negative if the changes are negative
        Add/subtract 20 for each magnitude greater than 1
    - 6, self Atk change
    - 7, self Def change
    - 8, self SpA change
    - 9, self SpD change
    - 10, self Spe change
    - 11, self Acc change
    - 12, self Eva change
    - 13, target Atk change
    - 14, target Def change
    - 15, target SpA change
    - 16, target SpD change
    - 17, target Spe change
    - 18, target Acc change
    - 19, target Eva change

    */

    public Move(int index, String name, Pokemon.Type type, Pokemon.MoveCategory category, Pokemon.ContestType contest,
                Pokemon.MoveTargets targets, int basePP, int basePower, int accuracy, int[] effects,
                boolean crit, boolean multi, boolean doubleHit, boolean authentic, boolean charge, boolean contact,
                boolean defrost, boolean distance, boolean gravity, boolean heal, boolean mirror, boolean nonsky,
                boolean protect, boolean punch, boolean recharge, boolean reflectable, boolean snatch, boolean sound,
                boolean dance, boolean sideanim, boolean powder, boolean bullet, boolean pulse) {
        this.index = index;
        this.name = name;
        this.type = type;
        this.category = category;
        this.contest = contest;
        this.targets = targets;
        this.basePP = basePP;
        this.basePower = basePower;
        this.accuracy = accuracy;
        this.effects = effects;
        this.crit = crit;
        this.multi = multi;
        this.doubleHit = doubleHit;
        this.authentic = authentic;
        this.charge = charge;
        this.contact = contact;
        this.defrost = defrost;
        this.distance = distance;
        this.gravity = gravity;
        this.heal = heal;
        this.mirror = mirror;
        this.nonsky = nonsky;
        this.protect = protect;
        this.punch = punch;
        this.recharge = recharge;
        this.reflectable = reflectable;
        this.snatch = snatch;
        this.sound = sound;
        this.dance = dance;
        this.sideanim = sideanim;
        this.powder = powder;
        this.bullet = bullet;
        this.pulse = pulse;
    }
}
