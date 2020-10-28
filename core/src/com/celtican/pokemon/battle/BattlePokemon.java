package com.celtican.pokemon.battle;

import com.badlogic.gdx.utils.Array;
import com.celtican.pokemon.Game;
import com.celtican.pokemon.utils.data.Ability;
import com.celtican.pokemon.utils.data.Move;
import com.celtican.pokemon.utils.data.Pokemon;
import com.celtican.pokemon.utils.data.Species;

import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;

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
        removeEffectsFromMap(onlyEndOnSwitchEffects, effectBooleans);
        removeEffectsFromMap(onlyEndOnSwitchEffects, effectIntegers);
        removeEffectsFromMap(onlyEndOnSwitchEffects, effectParty);
        removeEffectsFromMap(onlyEndOnSwitchEffects, effectPokemon);
        Arrays.fill(statBoosts, 0);
    }
    private void removeEffectsFromMap(boolean onlyEndOnSwitchEffects, HashMap<Effect, ?> map) {
        AtomicReference<Array<Effect>> effectsToRemove = new AtomicReference<>();
        map.forEach((BiConsumer<Effect, Object>) (effect, o) -> {
            if (!onlyEndOnSwitchEffects || effect.endsOnSwitch) {
                if (effectsToRemove.get() == null) effectsToRemove.set(new Array<>());
                effectsToRemove.get().add(effect);
            }
        });
        if (effectsToRemove.get() != null) effectsToRemove.get().forEach(map::remove);
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
    public Array<Effect> removeEffectsWithFlags(EffectFlag flag) {
        AtomicReference<Array<Effect>> effects = new AtomicReference<>();
        for (Effect effect : effectIntegers.keySet()) {
            if (effect.hasEffectFlag(flag)) {
                if (effects.get() == null) effects.set(new Array<>());
                effectIntegers.remove(effect);
                effects.get().add(effect);
            }
        }
        for (Effect effect : effectPokemon.keySet()) {
            if (effect.hasEffectFlag(flag)) {
                if (effects.get() == null) effects.set(new Array<>());
                effectPokemon.remove(effect);
                effects.get().add(effect);
            }
        }
        for (Effect effect : effectBooleans.keySet()) {
            if (effect.hasEffectFlag(flag)) {
                if (effects.get() == null) effects.set(new Array<>());
                effectBooleans.remove(effect);
                effects.get().add(effect);
            }
        }
        for (Effect effect : effectParty.keySet()) {
            if (effect.hasEffectFlag(flag)) {
                if (effects.get() == null) effects.set(new Array<>());
                effectParty.remove(effect);
                effects.get().add(effect);
            }
        }
        return effects.get();
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
    public int getMaxHP() {
        return getStat(0);
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

    public enum EffectFlag {
        RAPID_SPIN("rapidspin"),
        END_TURN("endturn");

        public final String name;
        EffectFlag(String name) {
            this.name = name;
        }
    }
    public enum Effect {
        FLINCH(EffectType.BOOLEAN, true, "EndTurn"),
        CONFUSED(EffectType.INTEGER, true, null),
        CHANGE_ABILITY(EffectType.INTEGER, true, null),
        CHARGE(EffectType.INTEGER, true, null),
        PROTECT_USES(EffectType.INTEGER, true, null),
        PROTECTED(EffectType.BOOLEAN, true, "EndTurn"),
        LEECH_SEED_SAPPER_SLOT(EffectType.INTEGER, true, "RapidSpin"),
        LEECH_SEED_SAPPER_PARTY(EffectType.PARTY, true, "RapidSpin"),
        FIRE_SPIN_TRAPPER_SLOT(EffectType.INTEGER, true, "RapidSpin"),
        FIRE_SPIN_TURNS_LEFT(EffectType.INTEGER, true, "RapidSpin"),
        FIRE_SPIN_TRAPPER_PARTY(EffectType.PARTY, true, "RapidSpin"),
        RAGE(EffectType.BOOLEAN, true, null),
        FOCUS_ENERGY(EffectType.BOOLEAN, true, null);

        public final EffectType type;
        public final boolean endsOnSwitch;
        public EffectFlag[] flags;
        Effect(EffectType type, boolean endsOnSwitch, String flags) {
            this.type = type;
            this.endsOnSwitch = endsOnSwitch;
            if (flags == null) {
                this.flags = new EffectFlag[0];
                return;
            }
            String[] flagsArray = flags.split(", ");
            this.flags = new EffectFlag[flagsArray.length];
            for (String flag : flagsArray) {
                String f = flag.toLowerCase();
                for (EffectFlag e : EffectFlag.values()) {
                    if (f.equals(e.name)) {
                        for (int i = 0; i < this.flags.length; i++) {
                            if (this.flags[i] == null) {
                                this.flags[i] = e;
                                break;
                            }
                        }
                        break;
                    }
                }
            }
            if (this.flags[this.flags.length-1] == null) Game.logError("Issue parsing flags of: \"" + flags + "\"");
        }
        public boolean hasEffectFlag(EffectFlag flag) {
            for (EffectFlag f : flags)
                if (f == flag)
                    return true;
            return false;
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
