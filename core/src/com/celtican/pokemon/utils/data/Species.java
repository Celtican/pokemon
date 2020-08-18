package com.celtican.pokemon.utils.data;

import com.badlogic.gdx.math.MathUtils;
import com.celtican.pokemon.Game;
import com.celtican.pokemon.utils.Enums;

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

    private final Enums.GenderRatio genderRatio;
    public Enums.GenderRatio getGenderRatio() {
        return genderRatio;
    }
    private final int captureRate;
    public int getCaptureRate() {
        return captureRate;
    }
//    private int eggCycles
    private final Enums.ExpGrowth expGrowth;
    public Enums.ExpGrowth getExperienceGrowth() {
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
    private final Enums.EggGroup eggGroup1, eggGroup2;
    public Enums.EggGroup getEggGroup1() {
        return eggGroup1;
    }
    public Enums.EggGroup getEggGroup2() {
        return eggGroup2;
    }
    public boolean eggGroupsMatch(Species species) {
        return eggGroup1 != Enums.EggGroup.UNDISCOVERED && eggGroup2 != Enums.EggGroup.UNDISCOVERED &&
                (eggGroup1 == species.eggGroup1 ||
                        eggGroup1 == species.eggGroup2 ||
                        eggGroup2 == species.eggGroup1 ||
                        eggGroup2 == species.eggGroup2);
    }
    private final Enums.Type type1, type2;
    public Enums.Type getType1() {
        return type1;
    }
    public Enums.Type getType2() {
        return type2;
    }
    public Enums.Type[] getTypes() {
        return new Enums.Type[] {type1, type2};
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

    public Species(int index, String name, Enums.GenderRatio genderRatio, int captureRate, Enums.ExpGrowth expGrowth,
                   int expEarned, byte[] earnedEVs, int baseHappiness, Enums.EggGroup eggGroup1, Enums.EggGroup eggGroup2,
                   Enums.Type type1, Enums.Type type2, int[] abilities, int[] stats) {
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
