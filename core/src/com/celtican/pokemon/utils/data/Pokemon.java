package com.celtican.pokemon.utils.data;

import com.badlogic.gdx.math.MathUtils;
import com.celtican.pokemon.Game;
import com.celtican.pokemon.battle.BattlePokemon;
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
    int getAbilitySpeciesIndex();
    // markings
    // language
    int[] getEVs();
    // contestStats
    // sheen?
    // sinnoh ribbons set 1

    int[] getStats();
    int getStat(int stat);

    int getCurHP();
    void setCurHP(int hp);
    Move[] getMoves();
    Move getMove(int move);
//    int[] getMovesPPUsed();
//    int[] getMovesRemainingPP();
//    int[] getMovesPPUps();
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

    default AnimatedTexture getAnimatedTexture(boolean forward) {
        return new AnimatedTexture("spritesheets/pokemon/" + getSpecies().getIndex() + ".atlas",
                (forward ? "F" : "B") + (isShiny() ? "S" : ""), 1000f/12);
    }

    static int getStat(int stat, int base, int iv, int ev, int level, Nature nature) {
        if (base <= 1) return 1;
        int baseStat = (2 * base + iv + ev / 4) * level / 100;
        if (stat == 0) return baseStat + level + 10; // hp
        else return (int)((baseStat + 5) * (nature.getBoostedStat() == stat ? 1.1f : nature.getHinderedStat() == stat ? 0.9f : 1));
    }

    enum EggGroup {
        MONSTER, HUMAN_LIKE, WATER1, WATER2, WATER3, BUG, MINERAL, FLYING, AMORPHOUS, FIELD, FAIRY, DITTO, GRASS, DRAGON, UNDISCOVERED
    }
    enum ExpGrowth {
        ERRATIC(new int[] {0, 15, 52, 122, 237, 406, 637, 942, 1326, 1800, 2369, 3041, 3822, 4719, 5737, 6881, 8155,
                9564, 11111, 12800, 14632, 16610, 18737, 21012, 23437, 26012, 28737, 31610, 34632, 37800, 41111, 44564,
                48155, 51881, 55737, 59719, 63822, 68041, 72369, 76800, 81326, 85942, 90637, 95406, 100237, 105122,
                110052, 115015, 120001, 125000, 131324, 137795, 144410, 151165, 158056, 165079, 172229, 179503, 186894,
                194400, 202013, 209728, 217540, 225443, 233431, 241496, 249633, 257834, 267406, 276458, 286328, 296358,
                305767, 316074, 326531, 336255, 346965, 357812, 367807, 378880, 390077, 400293, 411686, 423190, 433572,
                445239, 457001, 467489, 479378, 491346, 501878, 513934, 526049, 536557, 548720, 560922, 571333, 583539,
                591882, 600000}),
        FAST(new int[] {0, 6, 21, 51, 100, 172, 274, 409, 583, 800, 1064, 1382, 1757, 2195, 2700, 3276, 3930, 4665,
                5487, 6400, 7408, 8518, 9733, 11059, 12500, 14060, 15746, 17561, 19511, 21600, 23832, 26214, 28749,
                31443, 34300, 37324, 40522, 43897, 47455, 51200, 55136, 59270, 63605, 68147, 72900, 77868, 83058, 88473,
                94119, 100000, 106120, 112486, 119101, 125971, 133100, 140492, 148154, 156089, 164303, 172800, 181584,
                190662, 200037, 209715, 219700, 229996, 240610, 251545, 262807, 274400, 286328, 298598, 311213, 324179,
                337500, 351180, 365226, 379641, 394431, 409600, 425152, 441094, 457429, 474163, 491300, 508844, 526802,
                545177, 563975, 583200, 602856, 622950, 643485, 664467, 685900, 707788, 730138, 752953, 776239, 800000}),
        MEDIUM_FAST(new int[] {0, 8, 27, 64, 125, 216, 343, 512, 729, 1000, 1331, 1728, 2197, 2744, 3375, 4096, 4913,
                5832, 6859, 8000, 9261, 10648, 12167, 13824, 15625, 17576, 19683, 21952, 24389, 27000, 29791, 32768,
                35937, 39304, 42875, 46656, 50653, 54872, 59319, 64000, 68921, 74088, 79507, 85184, 91125, 97336,
                103823, 110592, 117649, 125000, 132651, 140608, 148877, 157464, 166375, 175616, 185193, 195112, 205379,
                216000, 226981, 238328, 250047, 262144, 274625, 287496, 300763, 314432, 328509, 343000, 357911, 373248,
                389017, 405224, 421875, 438976, 456533, 474552, 493039, 512000, 531441, 551368, 571787, 592704, 614125,
                636056, 658503, 681472, 704969, 729000, 753571, 778688, 804357, 830584, 857375, 884736, 912673, 941192,
                970299, 1000000}),
        MEDIUM_SLOW(new int[] {0, 9, 57, 96, 135, 179, 236, 314, 419, 560, 742, 973, 1261, 1612, 2035, 2535, 3120, 3798,
                4575, 5460, 6458, 7577, 8825, 10208, 11735, 13411, 15244, 17242, 19411, 21760, 24294, 27021, 29949,
                33084, 36435, 40007, 43808, 47846, 52127, 56660, 61450, 66505, 71833, 77440, 83335, 89523, 96012, 102810,
                109923, 117360, 125126, 133229, 141677, 150476, 159635, 169159, 179056, 189334, 199999, 211060, 222522,
                234393, 246681, 259392, 272535, 286115, 300140, 314618, 329555, 344960, 360838, 377197, 394045, 411388,
                429235, 447591, 466464, 485862, 505791, 526260, 547274, 568841, 590969, 613664, 636935, 660787, 685228,
                710266, 735907, 762160, 789030, 816525, 844653, 873420, 902835, 932903, 963632, 995030, 1027103, 1059860}),
        SLOW(new int[] {0, 10, 33, 80, 156, 270, 428, 640, 911, 1250, 1663, 2160, 2746, 3430, 4218, 5120, 6141, 7290,
                8573, 10000, 11576, 13310, 15208, 17280, 19531, 21970, 24603, 27440, 30486, 33750, 37238, 40960, 44921,
                49130, 53593, 58320, 63316, 68590, 74148, 80000, 86151, 92610, 99383, 106480, 113906, 121670, 129778,
                138240, 147061, 156250, 165813, 175760, 186096, 196830, 207968, 219520, 231491, 243890, 256723, 270000,
                283726, 297910, 312558, 327680, 343281, 359370, 375953, 393040, 410636, 428750, 447388, 466560, 486271,
                506530, 527343, 548720, 570666, 593190, 616298, 640000, 664301, 689210, 714733, 740880, 767656, 795070,
                823128, 851840, 881211, 911250, 941963, 973360, 1005446, 1038230, 1071718, 1105920, 1140841, 1176490,
                1212873, 1250000}),
        FLUCTUATING(new int[] {0, 4, 13, 32, 65, 112, 178, 276, 393, 540, 745, 967, 1230, 1591, 1957, 2457, 3046, 3732,
                4526, 5440, 6482, 7666, 9003, 10506, 12187, 14060, 16140, 18439, 20974, 23760, 26811, 30146, 33780,
                37731, 42017, 46656, 50653, 55969, 60505, 66560, 71677, 78533, 84277, 91998, 98415, 107069, 114205,
                123863, 131766, 142500, 151222, 163105, 172697, 185807, 196322, 210739, 222231, 238036, 250562, 267840,
                281456, 300293, 315059, 335544, 351520, 373744, 390991, 415050, 433631, 459620, 479600, 507617, 529063,
                559209, 582187, 614566, 639146, 673863, 700115, 737280, 765275, 804997, 834809, 877201, 908905, 954084,
                987754, 1035837, 1071552, 1122660, 1160499, 1214753, 1254796, 1312322, 1354652, 1415577, 1460276,
                1524731, 1571884, 1640000});

        private final int[] expTable;

        ExpGrowth(int[] expTable) {
            this.expTable = expTable;
        }

        public int getLevelFromExp(int exp) {
            for (int i = 0; i < 100; i++) if (exp < expTable[i]) return i;
            return 100;
        }
        public int getExpFromLevel(int level) {
            return 0 < level && level <= 100 ? expTable[level-1] : 0;
        }
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
        public boolean within(Type[] types) {
            for (Type type : types) if (type == this) return true;
            return false;
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
    enum StatusCondition {
        HEALTHY, BURN, FREEZE, PARALYSIS, SLEEP, POISON, TOXIC_1, TOXIC_2, TOXIC_3, TOXIC_4, TOXIC_5, TOXIC_6, TOXIC_7, TOXIC_8,
        TOXIC_9, TOXIC_10, TOXIC_11, TOXIC_12, TOXIC_13, TOXIC_14, TOXIC_15, TOXIC_16;

        public void addToxicCounter(BattlePokemon pokemon) {
            if (pokemon.statusCondition.isToxic()) {
                if (pokemon.statusCondition.ordinal() + 1 < StatusCondition.values().length)
                    pokemon.statusCondition = StatusCondition.values()[pokemon.statusCondition.ordinal() + 1];
            } else
                Game.logWarning("Attempted to increment toxic counter when pokemon isn't under the effects of toxic!");
        }
        public boolean isToxic() {
            switch (this) {
                case HEALTHY: case BURN: case FREEZE: case PARALYSIS: case SLEEP: case POISON: return false;
                default: return true;
            }
        }
    }
}
