package com.celtican.pokemon.battle;

import com.badlogic.gdx.utils.Array;
import com.celtican.pokemon.Game;
import com.celtican.pokemon.utils.data.Ability;
import com.celtican.pokemon.utils.data.Move;
import com.celtican.pokemon.utils.data.Pokemon;
import com.celtican.pokemon.utils.data.Species;

import java.util.Arrays;
import java.util.HashMap;

public class BattlePokemon implements Pokemon {

    public BattlePokemon(Pokemon base, int party, int partyMemberSlot) {
        species = base.getSpecies();
        experience = base.getExperience();
        level = base.getLevel();
        ability = base.getAbility();
        evs = base.getEVs();
        stats = base.getStats();
        curHP = base.getHP();
        moves = base.getMoves();
        ivs = base.getIVs();
        nickname = base.getNickname();
        nature = base.getNature();
        isShiny = base.isShiny();

        this.party = party;
        this.partyMemberSlot = partyMemberSlot;
        this.originalPartyMemberSlot = partyMemberSlot;
        types = species.getTypes().clone();
        seen = party == 0 ? null : new Array<>();
    }

    public int party;
    public final int originalPartyMemberSlot;
    public int partyMemberSlot;

    public int targetingParty;
    public int targetingSlot;

    public Action action;
    public int speed;
    public Type[] types;
    public final int[] statBoosts = new int[7]; // atk, def, spa, spd, spe, acc, eva
    public StatusCondition statusCondition = StatusCondition.HEALTHY;
    public int expGained = 0;
    public boolean leveledUp = false;
    public final Array<BattlePokemon> seen;

    @Override public boolean hasType(Type type) {
        for (Type t : types) if (t == type) return true;
        return false;
    }

    public void removeAllEffects(boolean onlyEndOnSwitchEffects) {
        effectIntegers.forEach((effect, value) -> {
            if (!onlyEndOnSwitchEffects || effect.endsOnSwitch) effectIntegers.remove(effect);
        });
        effectBooleans.forEach((effect, value) -> {
            if (!onlyEndOnSwitchEffects || effect.endsOnSwitch) effectBooleans.remove(effect);
        });
        effectPokemon.forEach((effect, value) -> {
            if (!onlyEndOnSwitchEffects || effect.endsOnSwitch) effectPokemon.remove(effect);
        });
        effectParty.forEach((effect, value) -> {
            if (!onlyEndOnSwitchEffects || effect.endsOnSwitch) effectPokemon.remove(effect);
        });
        Arrays.fill(statBoosts, 0);
    }
    public void removeEffect(Effect effect) {
        switch (effect.type) {
            default: Game.logError("Unhandled type: " + effect.type);
            case INTEGER: effectIntegers.remove(effect);
            case BOOLEAN: effectBooleans.remove(effect);
            case POKEMON: effectPokemon.remove(effect);
            case PARTY: effectParty.remove(effect);
        }
    }
    public boolean hasEffect(Effect effect) {
        switch (effect.type) {
            default: Game.logError("Unhandeled type: " + effect.type); return false;
            case INTEGER: return effectIntegers.containsKey(effect);
            case BOOLEAN: return effectBooleans.containsKey(effect);
            case POKEMON: return effectPokemon.containsKey(effect);
            case PARTY: return effectParty.containsKey(effect);
        }
    }
    private final HashMap<Effect, Integer> effectIntegers = new HashMap<>();
    public void addEffect(Effect effect, int value) {
        if (effect.type != EffectType.INTEGER) Game.logError("Attempted to assign an integer to " + effect + " when it should have a " + effect.type + ".");
        effectIntegers.put(effect, value);
    }
    public int getEffectInt(Effect effect) {
        return effectIntegers.getOrDefault(effect, -1);
    }
    private final HashMap<Effect, Boolean> effectBooleans = new HashMap<>();
    public void addEffect(Effect effect, boolean value) {
        if (effect.type != EffectType.BOOLEAN) Game.logError("Attempted to assign a boolean to " + effect + " when it should have a " + effect.type + ".");
        effectBooleans.put(effect, value);
    }
    public boolean getEffectBoolean(Effect effect) {
        return effectBooleans.getOrDefault(effect, false);
    }
    private final HashMap<Effect, BattlePokemon> effectPokemon = new HashMap<>();
    public void addEffect(Effect effect, BattlePokemon pokemon) {
        if (effect.type != EffectType.POKEMON) Game.logError("Attempted to assign a pokemon to " + effect + " when it should have a " + effect.type + ".");
        effectPokemon.put(effect, pokemon);
    }
    public BattlePokemon getEffectPokemon(Effect effect) {
        return effectPokemon.get(effect);
    }

    private final HashMap<Effect, BattleParty> effectParty = new HashMap<>();
    public void addEffect(Effect effect, BattleParty party) {
        if (effect.type != EffectType.PARTY) Game.logError("Attempted o assign a party to " + effect + " when it should have a " + effect.type + ".");
        effectParty.put(effect, party);
    }
    public BattleParty getEffectParty(Effect effect) {
        return effectParty.get(effect);
    }

    private Species species;
    @Override public Species getSpecies() {
        return species;
    }
    @Override public void setSpecies(Species species) {
        this.species = species;
    }
    private int experience;
    @Override public int getExperience() {
        return experience;
    }
    public int gainExp() {
        experience += expGained;
        expGained = 0;
        return species.getExperienceGrowth().getLevelFromExp(experience);
    }
    private int level;
    @Override public int getLevel() {
        return level;
    }
    public void setLevel() {
        level = species.getExperienceGrowth().getLevelFromExp(experience);
    }
    private final Ability ability;
    @Override public Ability getAbility() {
        int i = getEffectInt(Effect.CHANGE_ABILITY);
        if (i > 0) return Game.game.data.getAbility(i);
        else return ability;
    }
    private int abilitySpeciesIndex;
    @Override public int getAbilitySpeciesIndex() {
        return abilitySpeciesIndex;
    }
    private final int[] evs;
    @Override public int[] getEVs() {
        return evs;
    }
    private final int[] stats;
    @Override public int[] getStats() {
        return stats;
    }
    @Override public int getStat(int stat) {
        return stats[stat];
    }
    private int curHP;
    @Override public int getHP() {
        return curHP;
    }
    @Override public void setHP(int hp) {
        if (hp < 0) curHP = 0;
        else if (hp > getStat(0)) curHP = getStat(0);
        else curHP = hp;
    }
    private final Move[] moves;
    @Override public Move[] getMoves() {
        return moves;
    }
    @Override public Move getMove(int move) {
        return moves[move];
    }
    @Override public void setMove(Move move, int slot) {
        moves[slot] = move;
    }
    private final int[] ivs;
    @Override public int[] getIVs() {
        return ivs;
    }
    private final String nickname;
    @Override public String getName() {
        if (nickname == null) return species.getName();
        else return nickname;
    }
    @Override public String getNickname() {
        return nickname;
    }
    private final Nature nature;
    @Override public Nature getNature() {
        return nature;
    }
    private final boolean isShiny;
    @Override public boolean isShiny() {
        return isShiny;
    }

    @Override public Species evolvesInto() {
        return leveledUp ? Pokemon.super.evolvesInto() : null;
    }

    public interface Action {}
    public static class MoveAction implements Action {
        public final Move move;

        public MoveAction(Move move) {
            this.move = move;
        }
    }
    public static class RunAction implements Action {}
    public static class SwitchAction implements Action {
        public final int slot;
        public SwitchAction(int to) {
            this.slot = to;
        }
    }

    public enum Effect {
        CHANGE_ABILITY(EffectType.INTEGER, true),
        CHARGE(EffectType.INTEGER, true),
        LEECH_SEED_SAPPER_SLOT(EffectType.INTEGER, true),
        LEECH_SEED_SAPPER_PARTY(EffectType.PARTY, true);

        public final EffectType type;
        public final boolean endsOnSwitch;
        Effect(EffectType type, boolean endsOnSwitch) {
            this.type = type;
            this.endsOnSwitch = endsOnSwitch;
        }
    }
    private enum EffectType {
        INTEGER, BOOLEAN, POKEMON, PARTY;

        @Override public String toString() {
            String name = super.toString();
            return name.charAt(0) + name.substring(1).toLowerCase();
        }
    }
}
