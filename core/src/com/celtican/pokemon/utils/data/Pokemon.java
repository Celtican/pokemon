package com.celtican.pokemon.utils.data;

import com.celtican.pokemon.utils.Enums;
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

    Enums.Nature getNature();
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

    AnimatedTexture getAnimatedTexture(boolean forward, float millisPerSecond);
}
