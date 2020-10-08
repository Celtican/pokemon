package com.celtican.pokemon.battle;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.StringBuilder;
import com.celtican.pokemon.Game;
import com.celtican.pokemon.battle.results.*;
import com.celtican.pokemon.screens.BattleScreen;
import com.celtican.pokemon.utils.data.Ability;
import com.celtican.pokemon.utils.data.Move;
import com.celtican.pokemon.utils.data.Pokemon;

import java.util.Comparator;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class BattleCalculator {

    private final BattleScreen screen;

    private final BattleParty[] parties;
    private final Array<BattlePokemon> speedArray = new Array<>();

    public BattleCalculator(BattleScreen screen, BattleParty[] parties) {
        this.screen = screen;
        this.parties = parties;
        forEachPokemonOnField(false, compPokemon -> forEachPokemonInPartyOnField(parties[0], compPokemon.seen::add));
    }

    public void beginCalculateTurn() {
        new Thread(this::calculateTurn).start();
    }
    private void calculateTurn() {
        populateSpeedArray();
        speedArray.forEach(pokemon -> {
            if (pokemon.party != 0) {
                Array<Move> possibleMoves = new Array<>();
                for (Move move : pokemon.getMoves()) if (move != null) possibleMoves.add(move);
                pokemon.action = new BattlePokemon.MoveAction(possibleMoves.isEmpty() ? Game.game.data.getStruggle() : possibleMoves.random());
            }
        });

        AtomicBoolean endBattle = new AtomicBoolean(false);
        forEachPokemon(true, pokemon -> {
            if (endBattle.get()) return;
            if (pokemon.action instanceof BattlePokemon.MoveAction) {
                useMove(pokemon);
            } else if (pokemon.action instanceof BattlePokemon.RunAction) {
                if (attemptFlee(pokemon)) endBattle.set(true);
            } else if (pokemon.action instanceof BattlePokemon.SwitchAction) {
                attemptSwitch(pokemon, ((BattlePokemon.SwitchAction) pokemon.action).slot);
            }
            if (endBattle.get() || attemptEndBattle()) {
                screen.receiveResults();
                endBattle.set(true);
            }
        });
        if (endBattle.get()) {
            screen.receiveResults();
            return;
        }

        populateSpeedArray();
        // End of Turn Resolution Order as of Gen VI
        // https://bulbapedia.bulbagarden.net/wiki/User:SnorlaxMonster/End-turn_resolution_order
        // Weather subsiding
        // Hail damage/sandstorm damage/Rain Dish/Dry Skin/Solar Power/Ice Body
        // Self-curing a status condition due to high Affection
        // Future Sight/Doom Desire
        // Wish
        // Block A [
        //      Sea of Fire
        //      Grassy Terrain
        //      Hydration/Shed Skin
        //      Leftovers/Black Sludge
        //      Healer ]
        // Aqua Ring
        // Ingrain
        // Leech Seed
        // Poison/Poison Heal
        forEachPokemon(false, pokemon -> {
            if (pokemon.statusCondition == Pokemon.StatusCondition.POISON) {
                inflictDamage(pokemon, pokemon.getStat(0)/8);
                new TextResult(pokemon.getName() + " was hurt by its poison!");
                handleFaint(pokemon);
            } else if (pokemon.statusCondition.isToxic()) {
                inflictDamage(pokemon, pokemon.getStat(0)*(pokemon.statusCondition.ordinal()-8)/16);
                Pokemon.StatusCondition.incrementToxic(pokemon);
                setStatusCondition(pokemon, pokemon.statusCondition);
                new TextResult(pokemon.getName() + " was hurt by its poison!");
                handleFaint(pokemon);
            }
        });
        // Burn
        forEachPokemon(false, pokemon -> {
            if (pokemon.statusCondition == Pokemon.StatusCondition.BURN) {
                inflictDamage(pokemon, pokemon.getStat(0)/16);
                new TextResult(pokemon.getName() + " is hurt by its burn!");
                handleFaint(pokemon);
            }
        });
        // Nightmare
        // Curse
        // Bind/Clamp/Fire Spin/Infestation/Magma Storm/Sand Tomb/Whirlpool/Wrap
        // Taunt fading
        // Encore fading
        // Disable fading
        // Magnet Rise fading
        // Telekinesis fading
        // Heal Block fading
        // Embargo fading
        // Yawn
        // Perish count
        // Roost fading
        // Reflect dissipating
        // Light Screen dissipating
        // Safeguard dissipating
        // Mist dissipating
        // Tailwind dissipating
        // Lucky Chant dissipating
        // Rainbow (Water Pledge + Fire Pledge) dissipating
        // Sea of fire (Fire Pledge + Grass Pledge) dissipating
        // Swamp (Grass Pledge + Water Pledge) dissipating
        // Trick Room dissipating
        // Water Sport dissipating
        // Mud Sport dissipating
        // Wonder Room dissipating
        // Magic Room dissipating
        // Gravity dissipating
        // Terrain dissipating
        // Block B [
        //      Uproar
        //      Speed Boost/Moody/Bad Dreams* (*for the pokemon that has the ability)
        //      Flame Orb/Toxic Orb/Sticky Barb
        //      Harvest/Pickup ]
        // Zen Mode
        attemptEndBattle();
        screen.receiveResults();
    }

    private void forEachPokemon(boolean considerAction, Consumer<? super BattlePokemon> action) {
        speedArray.shuffle();
        speedArray.forEach(pokemon -> pokemon.speed = calcSpeed(pokemon, considerAction));
        speedArray.sort(Comparator.comparingInt(o -> o.speed));
        if (considerAction) while (speedArray.notEmpty()) {
            action.accept(speedArray.pop());
            speedArray.forEach(p -> p.speed = calcSpeed(p, true));
            speedArray.sort(Comparator.comparingInt(o -> o.speed)); // in gen 8, speed updates mid turn
            // (e.g. a drizzle user switches in while a swift swim user is on the field)
        } else speedArray.forEach(action);
    }
    private void forEachPokemonOnField(boolean considerUserParty, Consumer<? super BattlePokemon> action) {
        if (considerUserParty) {
            for (BattleParty party : parties)
                for (int i = 0; i < party.numBattling; i++)
                    if (party.members[i] != null)
                        action.accept(party.members[i]);
        } else {
            for (int i = 1; i < parties.length; i++)
                for (int j = 0; j < parties[i].numBattling; j++)
                    if (parties[i].members[j] != null)
                        action.accept(parties[i].members[j]);
        }
    }
    private void forEachPokemonInPartyOnField(BattleParty party, Consumer<? super BattlePokemon> action) {
        for (int i = 0; i < party.numBattling; i++)
            action.accept(party.members[i]);
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
        if (user.getHP() <= 0)
            return;
        if (user.statusCondition == Pokemon.StatusCondition.FREEZE) {
            if (MathUtils.random(4) == 0) {
                setStatusCondition(user, Pokemon.StatusCondition.HEALTHY);
                new TextResult(user.getName() + " thawed out!");
            } else {
                new TextResult(user.getName() + " is frozen solid!");
                return;
            }
        } else if (user.statusCondition == Pokemon.StatusCondition.PARALYSIS) {
            if (MathUtils.random(3) == 0) {
                new TextResult(user.getName() + " is paralyzed! It can't move!");
                return;
            }
        }
        new TextResult(user.getName() + " used " + move.name + "!");
        BattlePokemon defender = targetParty != null ? targetParty.members[targetSlot] : null;

        // wonder guard, harsh sun with water, heavy rain with fire, ground immunity, sky drop too heavy, synchronoise fail
        if (defender == null || defender.getHP() <= 0) {
            new TextResult("But it failed!");
            return;
        }

        if (move.type == Pokemon.Type.DOES_NOT_EXIST) {
            new TextResult("Not yet implemented");
            return;
        }

        if (move.accuracy > 0) {
            boolean missed = false;
            if (move.index == 12) { // guillotine
                if (user.getLevel() < defender.getLevel() || MathUtils.random(99) >= (user.getLevel() - defender.getLevel() + 30))
                    missed = true;
            } else {
                float accuracy = (calcStatWithStageAccuracy(move.accuracy/100f, user.statBoosts[5], defender.statBoosts[6]));
                if (MathUtils.random() >= accuracy)
                    missed = true;
            }
            if (missed) {
                new TextResult(defender.getName() + " avoided the attack!");
                return;
            }
        }

        if (move.category == Pokemon.MoveCategory.STATUS) {
            switch (move.index) {
                default:
                    Game.logError(move.name + " does not have the DOES_NOT_EXIST type but is not implemented in useMove().");
                    break;
                case 45:
                    new TextResult("Lower attack, etc. etc.");
            }
            return;
        }

        if (move.multi || move.doubleHit) {
            int maxHits;
            if (move.doubleHit) maxHits = 2;
            else switch (MathUtils.random(5)) {
                default: case 0: case 1: maxHits = 2; break;
                case 2: case 3: maxHits = 3; break;
                case 4: maxHits = 4; break;
                case 5: maxHits = 5; break;
            }
            int numHits = 0;
            DamageResult damage = null;
            while (numHits < maxHits) {
                numHits++;
                damage = calcDamage(user, defender, move);
                inflictDamage(defender, damage);
                if (damage.isCrit) new TextResult("Critical hit!");
                if (defender.getHP() <= 0)
                    break;
                attemptActivateMoveEffect(user, defender, move);
            }
            if (damage.effectiveness > 0) new TextResult("It's super effective!");
            else if (damage.effectiveness < 0) new TextResult("It's not very effective...");
            new TextResult("Hit " + numHits + (numHits == 1 ? " time!" : " times!"));
            handleFaint(defender);
        } else {
            DamageResult damage = calcDamage(user, defender, move);
            inflictDamage(defender, damage);
            if (damage.effectiveness > 0) new TextResult("It's super effective!");
            else if (damage.effectiveness < 0) new TextResult("It's not very effective...");
            if (damage.isCrit) new TextResult("Critical hit!");
            if (!handleFaint(defender)) {
                attemptActivateMoveEffect(user, defender, move);
            }
        }
    }
    private boolean attemptFlee(BattlePokemon pokemon) {
        if (canSwitch(pokemon)) {
            int preySpeed = -1;
            for (int i = 0; i < parties[pokemon.party].numBattling; i++)
                if (preySpeed == -1 || parties[pokemon.party].members[i].getStat(5) < preySpeed)
                    preySpeed = parties[pokemon.party].members[i].getStat(5);
            int predatorSpeed = -1;
            for (BattleParty party : parties) {
                if (party == parties[pokemon.party])
                    continue;
                for (int i = 0; i < party.numBattling; i++)
                    if (predatorSpeed == -1 || party.members[i].getStat(5) < predatorSpeed)
                        predatorSpeed = party.members[i].getStat(5);
            }
            int escapeAttempts = ++parties[pokemon.party].escapeAttempts;
            if (MathUtils.random(255) < preySpeed * 128 / predatorSpeed + 30 * escapeAttempts) { // actual formula is %255 due to use of bytes
                if (pokemon.party == 0) new TextResult("Ran away safely!");
                else new TextResult(pokemon.getName() + " got away!");
                new EndBattleResult();
                return true;
            }
        }
        if (pokemon.party == 0) new TextResult("Couldn't get away!");
        else new TextResult(pokemon.getName() + " couldn't get away!");
        return false;
    }
    private boolean attemptSwitch(BattlePokemon pokemon, int targetSlot) {
        BattlePokemon target = parties[pokemon.party].members[targetSlot];
        if (pokemon.getHP() <= 0 || targetSlot < parties[pokemon.party].numBattling || target.getHP() <= 0 ||
                !canSwitch(pokemon) || !canSwitch(target)) return false;

        if (pokemon.party == 0) new TextResult(pokemon.getName() + ", switch out! Come back!");
        else new TextResult("Go, " + target.getName() + "!");

        // pursuit here

        target.partyMemberSlot = pokemon.partyMemberSlot;
        pokemon.partyMemberSlot = targetSlot;
        parties[pokemon.party].members[target.partyMemberSlot] = target;
        parties[pokemon.party].members[targetSlot] = pokemon;
        new SwitchResult(target, true);

        if (pokemon.party == 0) {
            new TextResult("Go, " + target.getName() + "!");
            forEachPokemonOnField(false, compPokemon -> compPokemon.seen.add(target));
        } else {
            new TextResult("Trainer sent out " + target.getName() + "!");
            pokemon.seen.clear();
        }
        new SwitchResult(target);

        return true;
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

            int[] damages = new int[Game.game.doLogDamage ? 16 : 1];
            if(Game.game.doLogDamage) {
                for (int i = 0; i < 16; i++) {
                    damages[i] = baseDamage * (85 + i) / 100;
                }
            } else {
                damages[0] = baseDamage * (85 + MathUtils.random(15)) / 100;
            }


            int stabMod = move.type.within(attacker.types) ? attackerAbility == 91 ? 4 : 3 : 2; // adaptability
            int efficiencyMultiplier = efficiency != 0 ? (int)Math.pow(2, Math.abs(efficiency)) : 1;
            boolean applyBurn = attacker.statusCondition == Pokemon.StatusCondition.BURN && move.category == Pokemon.MoveCategory.PHYSICAL;

            for (int i = 0; i < damages.length; i++) {
                if (stabMod != 2) damages[i] = roundDown(damages[i] * stabMod/2f); // 0x1800 / 0x1000
                if (efficiency > 0) damages[i] *= efficiencyMultiplier;
                else if (efficiency < 0) damages[i] /= efficiencyMultiplier;
                if (applyBurn) damages[i] /= 2;
                if (damages[i] <= 0) damages[i] = 1;
            }

            if (Game.game.doLogDamage) {
                StringBuilder builder = new StringBuilder(attacker.getName()).append(" uses ").append(move.name)
                        .append(" against ").append(defender.getName()).append(". Damage: [");
                for (int i = 0; i < 16; i++) {
                    builder.append(damages[i]);
                    if (i < 15) builder.append(", ");
                    else builder.append("]. isCrit: ").append(isCrit);
                }
                Game.logInfo(builder.toString());
            }
            return new DamageResult(Game.game.doLogDamage ? damages[MathUtils.random(15)] : damages[0], isCrit, efficiency);
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
    private float calcStatWithStageAccuracy(float accuracy, int stage) {
        if (stage == 0) return accuracy;
        else if (stage < 0) return accuracy * 3 / (3 - stage);
        else return accuracy * (3 + stage) / 3;
    }
    private float calcStatWithStageAccuracy(float accuracy, int accStage, int evaStage) {
        return calcStatWithStageAccuracy(calcStatWithStageAccuracy(accuracy, accStage), evaStage);
    }
    private int roundDown(float f) {
        return (f % 1 > 0.5f) ? MathUtils.ceil(f) : MathUtils.floor(f);
    }
    private int calcExpGain(BattlePokemon fainted, BattlePokemon victor) {
        float trainerMultiplier = 1; // or 1.5 if it's a trainer battle
        int expYield = fainted.getSpecies().getExpEarned();
        float luckyEggMultiplier = 1; // or 1.5 if victor is holding a lucky egg
        float affectionMultiplier = 1; // or 1.2 if affection is 2 hearts or more
        int faintedLevel = fainted.getLevel();
        int victorLevel = victor.getLevel();
        float genericMultiplier = Game.game.expMultiplier; // for pass powers, o-powers, rotom powers, etc.
        int expShareDivider = 1; // or 2 if victor did not participate but got exp through an exp share (the higher this number, the less exp given)
        float tradeMultiplier = 1; // or 1.5/1.7 if victor is from domestic/international trade
        float postEvolutionMultiplier = 1; // or 1.2 if the victor's level is higher than needed to evolve

        if (Game.USE_SCALED_EXP_FORMULA) {
            return (int)(((int)(trainerMultiplier * expYield * faintedLevel / expShareDivider / 5) *
                    (int)(Math.pow(2 * faintedLevel + 10, 2.5)/Math.pow(faintedLevel + victorLevel + 10, 2.5)) + 1) *
                    tradeMultiplier * luckyEggMultiplier * genericMultiplier * affectionMultiplier * postEvolutionMultiplier);
            // note: the games that use the scaled exp formula do not implement affectionMultiplier or postEvolutionMultiplier, but I figure they should be added anyways
        } else {
            return (int)(trainerMultiplier * tradeMultiplier * expYield * luckyEggMultiplier * faintedLevel *
                    genericMultiplier * affectionMultiplier * postEvolutionMultiplier) / 7 / expShareDivider;
            // note: the games that use the flat formula do not implement the victor's level (that's the point of the scaled formula)
        }
    }

    // results
    private void inflictDamage(BattlePokemon pokemon, int damage) {
        pokemon.setHP(pokemon.getHP() - Math.max(damage, 1));
        new SoundResult("sfx/battleDamage.ogg");
        new HealthBarResult(pokemon);
    }
    private void inflictDamage(BattlePokemon pokemon, DamageResult damage) {
        pokemon.setHP(pokemon.getHP() - (Math.max(damage.damage, 1)));
        if (damage.isCrit || damage.effectiveness > 0) new SoundResult("sfx/battleDamageSuper.ogg");
        else if (damage.effectiveness < 0) new SoundResult("sfx/battleDamageWeak.ogg");
        else new SoundResult("sfx/battleDamage.ogg");
        new HealthBarResult(pokemon);
    }
    private void heal(BattlePokemon pokemon, int heal) {
        pokemon.setHP(pokemon.getHP() + Math.max(1, heal));
        new HealthBarResult(pokemon);
    }
    private boolean handleFaint(BattlePokemon pokemon) {
        if (pokemon.getHP() > 0)
            return false;
        new TextResult(pokemon.getName() + " fainted!");
        if (pokemon.party != 0) {
            // this is a comp pokemon. each pokemon it saw since it was on the field gains exp
            pokemon.seen.forEach(victor -> victor.expGained += calcExpGain(pokemon, victor));
        } else {
            // this is a user pokemon. each pokemon that saw it no longer gives the fainted pokemon exp if that pokemon faints
            forEachPokemonOnField(false, compPokemon -> compPokemon.seen.removeValue(pokemon, true));
            pokemon.expGained = 0;
        }
        return true;
    }
    private void populateSpeedArray() {
        speedArray.clear();
        for (BattleParty battleParty : parties)
            for (int j = 0; j < battleParty.numBattling; j++)
                if (battleParty.members[j] != null && battleParty.members[j].getHP() > 0)
                    speedArray.add(battleParty.members[j]);
    }
    private boolean attemptEndBattle() {
        boolean canEndBattle = true;
        for (BattleParty party : parties) {
            canEndBattle = true;
            for (BattlePokemon pokemon : party.members) {
                if (pokemon != null && pokemon.getHP() > 0) {
                    canEndBattle = false;
                    break;
                }
            }
            if (canEndBattle) break;
        }
        if (canEndBattle) {
            new EndBattleResult();
        }
        return canEndBattle;
    }
    private boolean attemptInflictStatusCondition(BattlePokemon pokemon, Pokemon.StatusCondition status) {
        if (pokemon.statusCondition != Pokemon.StatusCondition.HEALTHY && status != Pokemon.StatusCondition.HEALTHY) return false;
        switch (status) {
            case HEALTHY:
                if (pokemon.statusCondition == Pokemon.StatusCondition.HEALTHY) return false;
                setStatusCondition(pokemon, status);
                new TextResult(pokemon.getName() + " was cured of its status condition! ... somehow.");
                return true;
            case BURN:
                if (pokemon.hasType(Pokemon.Type.FIRE)) return false;
                setStatusCondition(pokemon, status);
                new TextResult(pokemon.getName() + " was burned!");
                return true;
            case FREEZE:
                if (pokemon.hasType(Pokemon.Type.ICE)) return false;
                setStatusCondition(pokemon, status);
                new TextResult(pokemon.getName() + " was frozen solid!");
                return true;
            case PARALYSIS:
                if (pokemon.hasType(Pokemon.Type.ELECTRIC)) return false;
                setStatusCondition(pokemon, status);
                new TextResult(pokemon.getName() + " is paralyzed!");
                return true;
            case POISON:
                if (pokemon.hasType(Pokemon.Type.POISON) || pokemon.hasType(Pokemon.Type.STEEL)) return false;
                setStatusCondition(pokemon, status);
                new TextResult(pokemon.getName() +  " was poisoned!");
                return true;
            default:
                if (status.isToxic()) {
                    if (pokemon.hasType(Pokemon.Type.POISON) || pokemon.hasType(Pokemon.Type.STEEL)) return false;
                    setStatusCondition(pokemon, Pokemon.StatusCondition.TOXIC_1);
                    new TextResult(pokemon.getName() +  " was badly poisoned!");
                    return true;
                } else if (status.isSleep()) {
                    setStatusCondition(pokemon, Pokemon.StatusCondition.getRandomSleepCount());
                    new TextResult(pokemon.getName() + " fell asleep!");
                    return true;
                } else {
                    Game.logError("Unhandled status condition: " + status);
                    return false;
                }
        }
    }
    private void setStatusCondition(BattlePokemon pokemon, Pokemon.StatusCondition status) {
        new SetValueResult(pokemon, SetValueResult.Type.STATUS, status);
        pokemon.statusCondition = status;
    }
    private void attemptActivateMoveEffect(BattlePokemon user, BattlePokemon defender, Move move) {
        if (move.effect == null) return;
        if (move.effect.chance == 100 || MathUtils.random(99) < move.effect.chance) {
            if (move.effect instanceof Move.EffectStatusCondition) {
                attemptInflictStatusCondition(defender, ((Move.EffectStatusCondition) move.effect).statusCondition);
            }
        }
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
    private boolean canSwitch(BattlePokemon pokemon) {
        return true;
    }

    private static class DamageResult {
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
