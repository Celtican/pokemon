package com.celtican.pokemon.battle.results;

import com.celtican.pokemon.Game;
import com.celtican.pokemon.battle.BattlePokemon;
import com.celtican.pokemon.battle.ExpBar;

public class ExpResult extends Result {

    // todo this is horrible. rewrite this.

    private ExpBar expBar;
    private final BattlePokemon pokemon;
    private final TextResult text;
    private Stage stage = Stage.DISPLAY_GAINED_EXP_TEXT;
    private boolean leveledUp;

    public ExpResult(BattlePokemon pokemon) {
        this(pokemon, true);
    }
    public ExpResult(BattlePokemon pokemon, boolean addToArray) {
        super(addToArray);
        this.pokemon = pokemon;
        text = new TextResult(false);
    }

    @Override public boolean start() {
        text.setText(pokemon.getName() + " gained " + pokemon.expGained + " exp!");
        if (pokemon.partyMemberSlot < parent.screen.parties[pokemon.party].numBattling) {
            float from = pokemon.getSpecies().getExperienceGrowth().getProgressToNextLevel(pokemon.getExperience());
            pokemon.gainExp();
            float to;
            if (pokemon.getLevel() != pokemon.getSpecies().getExperienceGrowth().getLevelFromExp(pokemon.getExperience())) {
                leveledUp = true;
                to = 1;
            } else {
                to = pokemon.getSpecies().getExperienceGrowth().getProgressToNextLevel(pokemon.getExperience());
            }
            expBar = new ExpBar(parent.screen.parties[pokemon.party].displayMembers.get(pokemon.partyMemberSlot), from, to);
        } else {
            pokemon.gainExp();
            if (pokemon.getLevel() != pokemon.getSpecies().getExperienceGrowth().getLevelFromExp(pokemon.getExperience())) {
                leveledUp = true;
            }
        }
        pokemon.setLevel();
        return false;
//        if (pokemon.getHP() > 0 && pokemon.partyMemberSlot < parent.screen.parties[0].numBattling) {
//            expBar = new ExpBar(parent.screen.parties[0].displayMembers.get(pokemon.originalPartyMemberSlot));
//            return false;
//        } else {
//            new TextResult(pokemon.getName() + " gained " + pokemon.expGained + " exp!");
//            int newLevel = pokemon.gainExp();
//            if (pokemon.getLevel() != newLevel) new TextResult(pokemon.getName() + " leveled up!");
//            return true;
//        }
    }

    @Override public void update() {
        switch (stage) {
            case DISPLAY_GAINED_EXP_TEXT:
                text.update();
                if (text.isFinished()) {
                    if (expBar != null) {
                        expBar.canRise = true;
                        stage = Stage.EXP_RISE;
                        Game.game.audio.playSound("sfx/battleExpRise.ogg");
                    } else {
                        if (leveledUp) {
                            text.setText(pokemon.getName() + " leveled up!");
                            stage = Stage.DISPLAY_LEVEL_UP_TEXT;
                        } else {
                            parent.nextResult();
                        }
                    }
                }
                break;
            case EXP_RISE:
                if (expBar.isFinished()) {
                    Game.game.audio.stopSound("sfx/battleExpRise.ogg");
                    if (leveledUp) {
                        text.setText("Level up!");
                        stage = Stage.DISPLAY_LEVEL_UP_TEXT;
                    } else {
                        expBar.hide();
                        stage = Stage.HIDE_EXP;
                    }
                }
                break;
            case DISPLAY_LEVEL_UP_TEXT:
                text.update();
                leveledUp = false;
                if (text.isFinished()) {
                    if (expBar != null) {
                        expBar.curValue = 0;
                        expBar.setValue(pokemon.getSpecies().getExperienceGrowth().getProgressToNextLevel(pokemon.getExperience()));
                        stage = Stage.EXP_RISE;
                    } else {
                        parent.nextResult();
                    }
                }
                break;
            case HIDE_EXP:
                if (expBar.isFinished()) {
                    new WaitResult(Game.TARGET_FRAME_RATE/2);
                    parent.nextResult();
                    expBar.healthBar.expBar = null;
                }
        }
    }
    @Override public void render() {
        switch (stage) {
            case DISPLAY_GAINED_EXP_TEXT:
            case DISPLAY_LEVEL_UP_TEXT:
                text.render();
        }
    }

    private enum Stage {
        DISPLAY_GAINED_EXP_TEXT, EXP_RISE, DISPLAY_LEVEL_UP_TEXT, HIDE_EXP
    }
}
