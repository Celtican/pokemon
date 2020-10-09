package com.celtican.pokemon.battle;

import com.badlogic.gdx.utils.Array;
import com.celtican.pokemon.utils.data.Ability;
import com.celtican.pokemon.utils.data.Move;
import com.celtican.pokemon.utils.data.Pokemon;
import com.celtican.pokemon.utils.data.Species;

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

    public Action action;
    public int speed;
    public Type[] types;
    public final int[] statBoosts = new int[7]; // atk, def, spa, spd, spe, acc, eva
    public StatusCondition statusCondition = StatusCondition.HEALTHY;
    public int expGained = 0;
    public boolean leveledUp = false;
    public final Array<BattlePokemon> seen;

    public boolean hasType(Type type) {
        for (Type t : types) if (t == type) return true;
        return false;
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
    private Ability ability;
    @Override public Ability getAbility() {
        return ability;
    }
    private int abilitySpeciesIndex;
    @Override public int getAbilitySpeciesIndex() {
        return abilitySpeciesIndex;
    }
    private int[] evs;
    @Override public int[] getEVs() {
        return evs;
    }
    private int[] stats;
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
    private Move[] moves;
    @Override public Move[] getMoves() {
        return moves;
    }
    @Override public Move getMove(int move) {
        return moves[move];
    }
    private int[] ivs;
    @Override public int[] getIVs() {
        return ivs;
    }
    private String nickname;
    @Override public String getName() {
        if (nickname == null) return species.getName();
        else return nickname;
    }
    @Override public String getNickname() {
        return nickname;
    }
    private Nature nature;
    @Override public Nature getNature() {
        return nature;
    }
    private boolean isShiny;
    @Override public boolean isShiny() {
        return isShiny;
    }

    @Override public Species evolvesInto() {
        return leveledUp ? Pokemon.super.evolvesInto() : null;
    }

    public interface Action {}
    public static class MoveAction implements Action {
        public final Move move;
        public final BattleParty targetParty;
        public final int targetSlot;

        public MoveAction(Move move) {
            this(move, null, -1);
        }
        public MoveAction(Move move, BattleParty targetParty, int targetSlot) {
            this.move = move;
            this.targetParty = targetParty;
            this.targetSlot = targetSlot;
        }
    }
    public static class RunAction implements Action {}
    public static class SwitchAction implements Action {
        public final int slot;
        public SwitchAction(int to) {
            this.slot = to;
        }
    }
}
