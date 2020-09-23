package com.celtican.pokemon.battle;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.celtican.pokemon.Game;
import com.celtican.pokemon.battle.results.HealthbarResult;
import com.celtican.pokemon.battle.results.Result;
import com.celtican.pokemon.battle.results.TextResult;
import com.celtican.pokemon.utils.data.Move;
import com.celtican.pokemon.utils.data.Pokemon;

public class BattleCalculator {

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
        speedArray.forEach(pokemon -> pokemon.speed = calculateSpeed(pokemon, true));
        speedArray.sort((o1, o2) -> o2.speed - o1.speed);

        while (speedArray.notEmpty()) {
            BattlePokemon pokemon = speedArray.pop();
            if (pokemon.action instanceof BattlePokemon.MoveAction)
                useMove(pokemon);

            speedArray.forEach(p -> p.speed = calculateSpeed(p, true));
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
        if (defender == null || defender.getCurHP() <= 0) {
            results.add(new TextResult("But it failed!"));
            return;
        }
        inflictDamage(defender, calcDamage(user, defender, move));
    }

    // calculations
    private int calcDamage(BattlePokemon attacker, BattlePokemon defender, Move move) {
        return move.basePower/5;
    }
    private int calculateSpeed(BattlePokemon pokemon, boolean considerAction) { // considerAction additionally considers trick room
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

    // results
    private void inflictDamage(BattlePokemon pokemon, int amountOfDamage) {
        pokemon.setCurHP(pokemon.getCurHP() - amountOfDamage);
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
}
