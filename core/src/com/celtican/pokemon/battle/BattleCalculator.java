package com.celtican.pokemon.battle;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.StringBuilder;
import com.celtican.pokemon.Game;
import com.celtican.pokemon.battle.results.HealthbarResult;
import com.celtican.pokemon.battle.results.Result;
import com.celtican.pokemon.battle.results.SoundResult;
import com.celtican.pokemon.battle.results.TextResult;
import com.celtican.pokemon.utils.data.Ability;
import com.celtican.pokemon.utils.data.Move;
import com.celtican.pokemon.utils.data.Pokemon;

public class BattleCalculator {

    public final static boolean DEBUG_DAMAGE = true;
    public final static boolean DEBUG_POKEMON_GENERATION = true;
    public final static boolean DEBUG_POKEMON_PERFECT_STATS = true;

    private final BattleParty[] parties;
    private Array<BattlePokemon> speedArray = new Array<>();

    private Array<Result> results;

    public BattleCalculator(BattleParty[] parties) {
        this.parties = parties;
    }

    public Array<Result> calculateTurn() {
        results = new Array<>();
        speedArray.clear();
//        for (BattlePokemon.Action action : actions) {
//            if (action instanceof BattlePokemon.MoveAction) {
//                BattlePokemon.MoveAction a = (BattlePokemon.MoveAction)action;
//                results.add(new TextResult(parties[0].members[0].getName() + " used " + a.move.name + "!"));
//                inflictDamage(parties[1].members[0], 5);
//            }
//        }

        for (BattleParty battleParty : parties)
            for (int j = 0; j < battleParty.numBattling; j++)
                if (battleParty.members[j] != null && battleParty.members[j].getCurHP() > 0)
                    speedArray.add(battleParty.members[j]);

        speedArray.forEach(pokemon -> {if (pokemon.party != 0) pokemon.action = new BattlePokemon.MoveAction(pokemon.getMove(0));});

        speedArray.shuffle();
        speedArray.forEach(pokemon -> pokemon.speed = calcSpeed(pokemon, true));
        speedArray.sort((o1, o2) -> o2.speed - o1.speed);

        while (speedArray.notEmpty()) {
            BattlePokemon pokemon = speedArray.pop();
            if (pokemon.action instanceof BattlePokemon.MoveAction)
                useMove(pokemon);

            speedArray.forEach(p -> p.speed = calcSpeed(p, true));
            speedArray.sort((o1, o2) -> o2.speed - o1.speed); // in gen 8, speed updates mid turn
                                        // (e.g. a drizzle user switches in while a swift swim user is on the field)
        }

        return results;
    }

    private void useMove(BattlePokemon user) {
        if (user.action instanceof BattlePokemon.MoveAction)
            useMove(user, ((BattlePokemon.MoveAction)user.action).move);
        else Game.logError("A pokemon attempted to use a move without direction. It's action was not a MoveAction and no move was passed into useMove().");
    }
    private void useMove(BattlePokemon user, Move move) {
        BattleParty party = null;
        int targetSlot = -1;
        if (user.action instanceof BattlePokemon.MoveAction) {
            BattlePokemon.MoveAction action = (BattlePokemon.MoveAction) user.action;
            if (action.targetParty != null) party = action.targetParty;
            if (action.targetSlot != -1) targetSlot = action.targetSlot;
        }
        if (party == null) party = getRandomOtherParty(user);
        if (targetSlot == -1) targetSlot = getRandomOtherTarget(user, party).partyMemberSlot;
        useMove(user, move, party, targetSlot);
    }
    private void useMove(BattlePokemon user, Move move, BattleParty targetParty, int targetSlot) {
        if (user.getCurHP() <= 0)
            return;
        results.add(new TextResult(user.getName() + " used " + move.name + "!"));
        BattlePokemon defender = targetParty.members[targetSlot];
        // wonder guard, harsh sun with water, heavy rain with fire, ground immunity, sky drop too heavy, synchronoise fail
        if (defender == null || defender.getCurHP() <= 0) {
            results.add(new TextResult("But it failed!"));
            return;
        }
        DamageResult damage = calcDamage(user, defender, move);
        inflictDamage(defender, damage);
        if (damage.isCrit) results.add(new TextResult("Critical hit!"));
        if (damage.effectiveness > 0) results.add(new TextResult("It's super effective!"));
        else if (damage.effectiveness < 0) results.add(new TextResult("It's not very effective..."));
    }

    // calculations
    private DamageResult calcDamage(BattlePokemon attacker, BattlePokemon defender, Move move) {

        // special damage cases
        {
            switch (move.index) {
                case 69: case 101: // seismic toss, night shade
                    return new DamageResult(attacker.getLevel());
                case 12: // guillotine
                    return new DamageResult(defender.getStat(0));
            }
        }

        int defenderAbility = getDefendersAbility(attacker, defender, move).getIndex();
        int attackerAbility = attacker.getAbility().getIndex();

        boolean isCrit;
        {
            if (defenderAbility == 4 || defenderAbility == 75) { // battle armor, shell armor
                isCrit = false;
            } else {
                int critStage = 0;
                if (move.crit)
                    critStage++;
                switch (critStage) {
                    case 0: isCrit = MathUtils.random(23) == 0; break;
                    case 1: isCrit = MathUtils.random(7) == 0; break;
                    case 2: isCrit = MathUtils.randomBoolean(); break;
                    default: isCrit = true; break;
                }
            }
        }

        // type changes. weather ball, judgment, natural gift, nature power, aura wheel
        // aerilate, pixilate, refrigerate, galvanize, normalize. doesn't apply to z-moves (but does apply to max moves?)

        int efficiency = move.type.getEfficiencyAgainst(defender.types);

        int bp = move.basePower;
        // bp mods

        int atk;
        {
            if (move.category == Pokemon.MoveCategory.PHYSICAL)
                atk = calcStatWithStage(attacker.getStat(1), isCrit ? Math.max(attacker.statBoosts[0], 0) : attacker.statBoosts[0]);
            else
                atk = calcStatWithStage(attacker.getStat(3), isCrit ? Math.max(attacker.statBoosts[2], 0) : attacker.statBoosts[2]);
        }

        int def;
        {
            if (move.category == Pokemon.MoveCategory.PHYSICAL)
                def = calcStatWithStage(defender.getStat(2), isCrit ? Math.min(defender.statBoosts[1], 0) : defender.statBoosts[1]);
            else
                def = calcStatWithStage(defender.getStat(4), isCrit ? Math.min(defender.statBoosts[3], 0) : defender.statBoosts[3]);
        }

        // damage
        {
            int baseDamage = (2*attacker.getLevel()/5 + 2)*bp*atk/def/50 + 2;
            if (isCrit) baseDamage = baseDamage * 3 / 2;

            int[] damages = new int[DEBUG_DAMAGE ? 16 : 1];
            if(DEBUG_DAMAGE) {
                for (int i = 0; i < 16; i++) {
                    damages[i] = baseDamage * (85 + i) / 100;
                }
            } else {
                damages[0] = baseDamage * (85 + MathUtils.random(15)) / 100;
            }

            boolean stab = move.type.within(attacker.types);
            if (stab) for (int i = 0; i < damages.length; i++) damages[i] = roundDown(damages[i] * (float)0x1800 / 0x1000);
            if (efficiency > 0) for (int i = 0; i < damages.length; i++) damages[i] = damages[i] * (int)Math.pow(2, efficiency);
            if (efficiency < 0) for (int i = 0; i < damages.length; i++) damages[i] = damages[i] / (int)Math.pow(2, efficiency);
            for (int i = 0; i < damages.length; i++) if (damages[i] < 1) damages[i] = 1;

            if (DEBUG_DAMAGE) {
                StringBuilder builder = new StringBuilder(attacker.getName()).append(" uses ").append(move.name)
                        .append(" against ").append(defender.getName()).append(". Damage: [");
                for (int i = 0; i < 16; i++) {
                    builder.append(damages[i]);
                    if (i < 15) builder.append(", ");
                    else builder.append("]. isCrit: ").append(isCrit);
                }
                Game.logInfo(builder.toString());
            }
            return new DamageResult(DEBUG_DAMAGE ? damages[MathUtils.random(15)] : damages[0], isCrit, efficiency);
        }
    }
    private int calcSpeed(BattlePokemon pokemon, boolean considerAction) { // considerAction additionally considers trick room
        /*
        tailwind
        speed swap
        trick room
        chlorophyll
        quick feet
        sand rush
        swift swim
        unburden
        slush rush
        surge surfer
        choice scarf
        quick powder
        slow start
        paralysis
        ... and probably some others
         */

        int speed = pokemon.getStat(5);
        if (pokemon.statBoosts[4] != 0) {
            if (pokemon.statBoosts[4] > 0)
                speed = speed * (pokemon.statBoosts[4] + 2) / 2;
            else speed = speed * 2 / (2 - pokemon.statBoosts[4]);
        }

        if (pokemon.statusCondition == Pokemon.StatusCondition.PARALYSIS) speed /= 2;

        if (speed > 9999) speed = 9999;

        if (considerAction) {
            /*
             * format: APSBBBB
             * A: action priority (switching is faster than using a move)
             * P: move priority (quick attack)
             * S: move sub priority (for moves which add "half priority")
             * BBBB: speed with modifiers
             */

            /*
            if action is useItem, add 1000000
            if action is switch, add 2000000
            etc
            move action adds nothing
             */
            if (pokemon.action instanceof BattlePokemon.RunAction) speed += 9000000;
        }

        return speed;
    }
    private int calcStatWithStage(int stat, int stage) {
        if (stage == 0) return stat;
        else if (stage < 0) return stat * 2 / (2 - stage);
        else return stat * (2 + stage) / 2;
    }
    private int calcStatWithStageAccuracy(int stat, int stage) {
        if (stage == 0) return stat;
        else if (stage < 0) return stat * 3 / (3 - stage);
        else return stat * (3 + stage) / 3;
    }
    private int roundDown(float f) {
        return (f % 1 > 0.5f) ? MathUtils.ceil(f) : MathUtils.floor(f);
    }

    // results
    private void inflictDamage(BattlePokemon pokemon, int damage) {
        pokemon.setCurHP(pokemon.getCurHP() - damage);
        results.add(new SoundResult("sfx/battleDamage.ogg"));
        results.add(new HealthbarResult(pokemon));
    }
    private void inflictDamage(BattlePokemon pokemon, DamageResult damage) {
        pokemon.setCurHP(pokemon.getCurHP() - damage.damage);
        if (damage.isCrit || damage.effectiveness > 0) results.add(new SoundResult("sfx/battleDamageSuper.ogg"));
        else if (damage.effectiveness < 0) results.add(new SoundResult("sfx/battleDamageWeak.ogg"));
        else results.add(new SoundResult("sfx/battleDamage.ogg"));
        results.add(new HealthbarResult(pokemon));
    }

    // getters
    public BattleParty getUserParty() {
        return parties[0];
    }
    private BattleParty getRandomOtherParty(BattlePokemon pokemon) {
        int random = MathUtils.random(parties.length-2);
        return parties[random < pokemon.party ? random : random+1];
    }
    private BattlePokemon getRandomOtherTarget(BattlePokemon pokemon) {
        return getRandomOtherTarget(pokemon, getRandomOtherParty(pokemon));
    }
    private BattlePokemon getRandomOtherTarget(BattlePokemon pokemon, BattleParty fromSpecificParty) {
        Array<BattlePokemon> choices = new Array<>();
        for (int i = 0; i < fromSpecificParty.numBattling; i++)
            if (!(fromSpecificParty.members[i] == null || fromSpecificParty.members[i] == pokemon))
                choices.add(fromSpecificParty.members[i]);
        return choices.random();
    }
    private Ability getDefendersAbility(BattlePokemon attacker, BattlePokemon defender) {
        return getDefendersAbility(attacker, defender, null);
    }
    private Ability getDefendersAbility(BattlePokemon attacker, BattlePokemon defender, Move move) {
        int a = attacker.getAbility().getIndex();
        int d = defender.getAbility().getIndex();
        int m = move != null ? move.index : 0;
        boolean isNegated = false;
        switch (a) {case 104: case 164: case 163: isNegated = true;} // mold breaker, teravolt, turboblaze
        switch (m) {case 713: case 714: case 722: case 723: case 724: case 725: isNegated = true;} // sunsteel strike, moongeist beam, photon geyser, light that burns the sky, searing sunraze smash, menacing moonraze maelstrom
        if (isNegated) switch (d) { case 230: case 231: case 232: isNegated = false;} // full metal body, shadow shield, prism armor
        return isNegated ? Game.game.data.getNullAbility() : defender.getAbility();
    }

    private class DamageResult {
        private final int damage;
        private final boolean isCrit;
        private final int effectiveness;

        public DamageResult(int damage) {
            this(damage, false, 0);
        }
        public DamageResult(int damage, boolean isCrit, int effectiveness) {
            this.damage = damage;
            this.isCrit = isCrit;
            this.effectiveness = effectiveness;
        }
    }
}
