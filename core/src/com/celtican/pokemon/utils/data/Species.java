package com.celtican.pokemon.utils.data;

import com.badlogic.gdx.math.MathUtils;
import com.celtican.pokemon.Game;

public class Species {
    private final int index;
    public int getIndex() {
        return index;
    }
    private final String name;
    public String getName() {
        return name;
    }
    // classification (bulbasaur, seed pokemon)
    // height in meters
    // weight in kilograms

    private final Pokemon.GenderRatio genderRatio;
    public Pokemon.GenderRatio getGenderRatio() {
        return genderRatio;
    }
    private final int captureRate;
    public int getCaptureRate() {
        return captureRate;
    }
//    private int eggCycles
    private final Pokemon.ExpGrowth expGrowth;
    public Pokemon.ExpGrowth getExperienceGrowth() {
        return expGrowth;
    }
    private final int expEarned;
    public int getExpEarned() {
        return expEarned;
    }
    private final byte[] earnedEVs;
    public byte[] getEarnedEVs() {
        return earnedEVs;
    }
    private final int baseHappiness;
    public int getBaseHappiness() {
        return baseHappiness;
    }
    private final Pokemon.EggGroup eggGroup1, eggGroup2;
    public Pokemon.EggGroup getEggGroup1() {
        return eggGroup1;
    }
    public Pokemon.EggGroup getEggGroup2() {
        return eggGroup2;
    }
    public boolean eggGroupsMatch(Species species) {
        return eggGroup1 != Pokemon.EggGroup.UNDISCOVERED && eggGroup2 != Pokemon.EggGroup.UNDISCOVERED &&
                (eggGroup1 == species.eggGroup1 ||
                        eggGroup1 == species.eggGroup2 ||
                        eggGroup2 == species.eggGroup1 ||
                        eggGroup2 == species.eggGroup2);
    }
    private final Pokemon.Type type1, type2;
    public Pokemon.Type getType1() {
        return type1;
    }
    public Pokemon.Type getType2() {
        return type2;
    }
    public Pokemon.Type[] getTypes() {
        return new Pokemon.Type[] {type1, type2};
    }
    private final int[] abilities;
    public Ability getAbility(int which) {
        if (0 <= which && which < 4)
            return Game.game.data.getAbility(abilities[which]);
        return Game.game.data.getNullAbility();
    }
    public Ability getRandomAbility() {
        return getRandomAbility(500);
    }
    public Ability getRandomAbility(int chanceOfHiddenAbility) {
        return getAbility(getRandomWhichAbility(chanceOfHiddenAbility));
    }
    public static int getRandomWhichAbility() {
        return getRandomWhichAbility(500);
    }
    public static int getRandomWhichAbility(int chanceOfHiddenAbility) {
        if (chanceOfHiddenAbility > 0)
            if (MathUtils.random(chanceOfHiddenAbility-1) == 0)
                return 2;
        return MathUtils.randomBoolean() ? 0 : 1;
    }
    private final int[] stats;
    public int[] getStats() {
        return stats;
    }
    public int getStat(int index) {
        return stats[index];
    }
    // moves
    // evolution

    public Species(int index, String name, Pokemon.GenderRatio genderRatio, int captureRate, Pokemon.ExpGrowth expGrowth,
                   int expEarned, byte[] earnedEVs, int baseHappiness, Pokemon.EggGroup eggGroup1, Pokemon.EggGroup eggGroup2,
                   Pokemon.Type type1, Pokemon.Type type2, int[] abilities, int[] stats) {
        this.index = index;
        this.name = name;
        this.genderRatio = genderRatio;
        this.captureRate = captureRate;
        this.expGrowth = expGrowth;
        this.expEarned = expEarned;
        this.earnedEVs = earnedEVs;
        this.baseHappiness = baseHappiness;
        this.eggGroup1 = eggGroup1;
        this.eggGroup2 = eggGroup2;
        this.type1 = type1;
        this.type2 = type2;
        this.abilities = abilities;
        this.stats = stats;
    }
}
