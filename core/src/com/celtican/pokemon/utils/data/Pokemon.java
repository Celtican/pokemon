package com.celtican.pokemon.utils.data;

import com.badlogic.gdx.math.MathUtils;
import com.celtican.pokemon.utils.graphics.AnimatedTexture;

public interface Pokemon {
    Species getSpecies();
//    int getHeldItem();
//    int getOTID();
//    int getSOTID();
    int getExperience();
    int getLevel();
//    int getFriendship();
    Ability getAbility();
    // markings
    // language
    int[] getEVs();
    // contestStats
    // sheen?
    // sinnoh ribbons set 1

    Move[] getMoves();
    int[] getMovesRemainingPP();
    int[] getMovesPPUps();
    int[] getIVs();
    // hoenn ribbon set\
    // gender/forms
    // shiny leaves
    // unused
    // platinum egg location
    // platinum met at location

    String getName();
    String getNickname();
    // game of origin
    // sinnoh ribbons 2

    Nature getNature();
    boolean isShiny();

    // OT name
    // date egg recieved
    // date met
    // diamond pearl egg location
    // diamond pearl met at location
    // pokerus stage
    // pokeball
    // met at level/female ot gender?
    // encounter type
    // HGSS pokeball
    // performance

    AnimatedTexture getAnimatedTexture(boolean forward, float millisPerFrame);


    enum EggGroup {
        MONSTER, HUMAN_LIKE, WATER1, WATER2, WATER3, BUG, MINERAL, FLYING, AMORPHOUS, FIELD, FAIRY, DITTO, GRASS, DRAGON, UNDISCOVERED
    }
    enum ExpGrowth {
        ERRATIC, FAST, MEDIUM_FAST, MEDIUM_SLOW, SLOW, FLUCTUATING
    }
    enum Gender {
        MALE, FEMALE, GENDERLESS;

        public boolean isOppositeOf(Gender gender) {
            return (this == MALE && gender == FEMALE) || (this == FEMALE && gender == MALE);
        }
    }
    enum GenderRatio {
        MALE_ONLY, FEMALE_IS_RARE, FEMALE_IS_UNCOMMON, EVEN, MALE_IS_UNCOMMON, MALE_IS_RARE, FEMALE_ONLY, GENDERLESS;

        public Gender generateGender() {
            switch (this) {
                case MALE_ONLY:
                    return Gender.MALE;
                case FEMALE_IS_RARE:
                    return MathUtils.random(7) == 0 ? Gender.FEMALE : Gender.MALE;
                case FEMALE_IS_UNCOMMON:
                    return MathUtils.random(3) == 0 ? Gender.FEMALE : Gender.MALE;
                case EVEN:
                    return MathUtils.randomBoolean() ? Gender.MALE : Gender.FEMALE;
                case MALE_IS_UNCOMMON:
                    return MathUtils.random(3) == 0 ? Gender.MALE : Gender.FEMALE;
                case MALE_IS_RARE:
                    return MathUtils.random(7) == 0 ? Gender.MALE : Gender.FEMALE;
                case FEMALE_ONLY:
                    return Gender.FEMALE;
                default:
                case GENDERLESS:
                    return Gender.GENDERLESS;
            }
        }
    }

    enum ContestType {
        COOL, BEAUTIFUL, CUTE, CLEVER, TOUGH
    }

    enum MoveCategory {
        PHYSICAL, SPECIAL, STATUS
    }

    enum MoveTargets {
        ALL_ADJACENT_FOES,      // All foes adjacent to user
        ALL_ADJACENT_ALLIES,    // All allies adjacent to user
        ALL_FOES,               // All foes
        ALL_ALLIES,             // All allies, including self
        ALL_OTHER_ALLIES,       // All allies, EXCEPT self
        ALL_ADJACENT,           // All pokemon adjacent to user
        ALL_OTHERS,             // All pokemon, EXCEPT self
        FIELD,                  // All pokemon, including self
        ANY,                    // Target any pokemon, including self
        ANY_OTHER,              // Target any pokemon, EXCEPT self
        ADJACENT_ALLY,          // Target any ally adjacent to the user
        ADJACENT_FOE,           // Target any foe adjacent to user
        ADJACENT,               // Target any pokemon adjacent to user
        SELF,                   // Only self
        SELF_OR_ADJACENT_ALLY,  // Target either self or an ally adjacent to user
    }

    enum Nature {
        HARDY, LONELY, ADAMANT, NAUGHT, BRAVE, BOLD, DOCILE, IMPISH, LAX, RELAXED, MODEST, MILD, BASHFUL,
        RASH, QUIET, CALM, GENTLE, CAREFUL, QUIRKY, SASSY, TIMID, HASTY, JOLLY, NAIVE, SERIOUS;

        @Override public String toString() {
            return name().charAt(0) + name().substring(1).toLowerCase();
        }

        public int getBoostedStat() {
            return isNeutral() ? -1 : ordinal()/5 + 1;
        }
        public int getHinderedStat() {
            return isNeutral() ? -1 : ordinal()%5 + 1;
        }
        public boolean isNeutral() {
            return ordinal()/5 == ordinal()%5;
        }

        public static Nature getRandomNature() {
            return values()[MathUtils.random(values().length-1)];
        }
    }

    enum Type {

        NORMAL, FIGHTING, FLYING, POISON, GROUND, ROCK, BUG, GHOST, STEEL, FIRE, WATER,
        GRASS, ELECTRIC, PSYCHIC, ICE, DRAGON, DARK, FAIRY, TYPELESS, ROOST;

        private Type[] weakAgainst;
        private Type[] superEffectiveAgainst;

        public int getEfficiencyAgainst(Type type) {
            for (Type t : weakAgainst)
                if (t == type)
                    return -1;
            for (Type t : superEffectiveAgainst)
                if (t == type)
                    return 1;
            return 0;
        }
        public int getEfficiencyAgainst(Type[] types) {
            int efficiency = 0;
            for (Type type : types)
                efficiency += getEfficiencyAgainst(type);
            return efficiency;
        }

        @Override public String toString() {
            return name().charAt(0) + name().substring(1).toLowerCase();
        }

        private void setWeakAgainst(Type[] types) {
            weakAgainst = types;
        }
        private void setSuperEffectiveAgainst(Type[] types) {
            superEffectiveAgainst = types;
        }

        public static void setupTypes() {
            Type[] nullArray = new Type[0];

            NORMAL.setWeakAgainst(new Type[]{ROCK, STEEL});
            FIGHTING.setWeakAgainst(new Type[]{FLYING, POISON, BUG, PSYCHIC, FAIRY});
            FLYING.setWeakAgainst(new Type[]{ROCK, STEEL, ELECTRIC});
            POISON.setWeakAgainst(new Type[]{POISON, GROUND, ROCK, GHOST});
            GROUND.setWeakAgainst(new Type[]{BUG, GRASS});
            ROCK.setWeakAgainst(new Type[]{FIGHTING, GROUND, STEEL});
            BUG.setWeakAgainst(new Type[]{FIGHTING, FLYING, POISON, GHOST, STEEL, FIRE, FAIRY});
            GHOST.setWeakAgainst(new Type[]{DARK});
            STEEL.setWeakAgainst(new Type[]{STEEL, FIRE, WATER, ELECTRIC});
            FIRE.setWeakAgainst(new Type[]{ROCK, FIRE, WATER, DRAGON});
            WATER.setWeakAgainst(new Type[]{WATER, GRASS, DRAGON});
            GRASS.setWeakAgainst(new Type[]{FLYING, POISON, BUG, STEEL, FIRE, GRASS, DRAGON});
            ELECTRIC.setWeakAgainst(new Type[]{GRASS, ELECTRIC, DRAGON});
            PSYCHIC.setWeakAgainst(new Type[]{STEEL, PSYCHIC});
            ICE.setWeakAgainst(new Type[]{STEEL, FIRE, WATER, ICE});
            DRAGON.setWeakAgainst(new Type[]{STEEL});
            DARK.setWeakAgainst(new Type[]{FIGHTING, DARK, FAIRY});
            FAIRY.setWeakAgainst(new Type[]{POISON, STEEL, FIRE});

            NORMAL.setSuperEffectiveAgainst(nullArray);
            FIGHTING.setSuperEffectiveAgainst(new Type[]{NORMAL, ROCK, STEEL, ICE, DARK});
            FLYING.setSuperEffectiveAgainst(new Type[]{FIGHTING, BUG, GRASS});
            POISON.setSuperEffectiveAgainst(new Type[]{GRASS, FAIRY});
            GROUND.setSuperEffectiveAgainst(new Type[]{POISON, ROCK, STEEL, FIRE, ELECTRIC});
            ROCK.setSuperEffectiveAgainst(new Type[]{FLYING, BUG, FIRE, ICE});
            BUG.setSuperEffectiveAgainst(new Type[]{GRASS, PSYCHIC, DARK});
            GHOST.setSuperEffectiveAgainst(new Type[]{GHOST, PSYCHIC});
            STEEL.setSuperEffectiveAgainst(new Type[]{ROCK, ICE, FAIRY});
            FIRE.setSuperEffectiveAgainst(new Type[]{BUG, STEEL, GRASS, ICE});
            WATER.setSuperEffectiveAgainst(new Type[]{GROUND, ROCK, FIRE});
            GRASS.setSuperEffectiveAgainst(new Type[]{GROUND, ROCK, WATER});
            ELECTRIC.setSuperEffectiveAgainst(new Type[]{FLYING, WATER});
            PSYCHIC.setSuperEffectiveAgainst(new Type[]{FIGHTING, POISON});
            ICE.setSuperEffectiveAgainst(new Type[]{FLYING, GROUND, GRASS, DRAGON});
            DRAGON.setSuperEffectiveAgainst(new Type[]{DRAGON});
            DARK.setSuperEffectiveAgainst(new Type[]{GHOST, PSYCHIC});
            FAIRY.setSuperEffectiveAgainst(new Type[]{FIGHTING, DRAGON, DARK});

            TYPELESS.setWeakAgainst(nullArray);
            TYPELESS.setSuperEffectiveAgainst(nullArray);
            ROOST.setWeakAgainst(nullArray);
            ROOST.setSuperEffectiveAgainst(nullArray);
        }
    }
}
