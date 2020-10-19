package com.celtican.pokemon.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;
import com.celtican.pokemon.Game;
import com.celtican.pokemon.battle.*;
import com.celtican.pokemon.utils.data.*;
import com.celtican.pokemon.utils.graphics.Button;
import com.celtican.pokemon.utils.graphics.Texture;

import java.util.Objects;

public class BattleScreen extends Screen {

    private final static int BUTTON_WIDTH = 100;

    public final BattleCalculator calculator;
    public final ResultHandler resultHandler;
    public boolean endBattle = false;
    private int actionI = 0;
    private boolean playingResults = false;
    private boolean waitingForCalculator = false;

    private int buttonSlide = 0;
    private final Texture buttonTexture;
    public final Array<Button> buttons;

    public final BattleParty[] parties;

    public BattleScreen() {
        resultHandler = new ResultHandler(this);

        BattleParty userParty = new BattleParty(new BattlePokemon[Game.game.data.player.party.length], 1, 0);
        for (int i = 0; i < userParty.members.length; i++)
            if (Game.game.data.player.party[i] != null)
                userParty.members[i] = new BattlePokemon(Game.game.data.player.party[i], 0, i);
        BattleParty compParty = new BattleParty(new BattlePokemon[6], 1, 1);
        for (int i = 0, n = compParty.numBattling; i < n; i++)
            compParty.members[i] = new BattlePokemon(new PCPokemon(((userParty.members.length > i && userParty.members[i] != null) ? (userParty.members[i].getLevel()-2) : (Objects.requireNonNull(userParty.members[0]).getLevel()-2))), 1, i);
        parties = new BattleParty[] {userParty, compParty};
        calculator = new BattleCalculator(this, parties);
        for (int i = 0; i < parties.length; i++)
            for (int j = 0; j < parties[i].numBattling; j++)
                if (parties[i].members[j] != null)
                    parties[i].displayMembers.add(new DisplayPokemon(parties[i].members[j], i != 0));

        buttonTexture = new Texture("spritesheets/battle.atlas", "button");
        buttons = new Array<>();
        makeMenu(MenuType.MAIN);
    }

    @Override public void update() {
        for (BattleParty party : parties) party.displayMembers.forEach(DisplayPokemon::update);
        if (playingResults) {
            if (resultHandler.hasResults()) {
                resultHandler.update();
            } else {
                Game.logInfo("-- End Turn --");
                playingResults = false;
                buttonSlide = 0;
                buttons.forEach(Button::show);
                addAction(null);
            }
        } else if (!waitingForCalculator && endBattle) {
            switchScreen();
        }
    }
    @Override public void render() {
        for (int i = parties.length-1; i >= 0; i--) parties[i].displayMembers.forEach(DisplayPokemon::renderTexture);
        for (int i = parties.length-1; i >= 0; i--) parties[i].displayMembers.forEach(DisplayPokemon::renderHealth);
        if (playingResults) {
            if (resultHandler.hasResults()) {
                resultHandler.render();
            }
        } else if (!waitingForCalculator){
            buttonSlide++;
            buttons.forEach(Button::render);
        }
    }

    @Override public void hide() {
        buttons.forEach(Button::hide);
    }
    @Override public void show() {
        Game.game.audio.playMusic("bgm/wildBattle.ogg");
        buttons.forEach(Button::show);
    }
    @Override public void resize(int width, int height) {
        for (int i = 0; i < buttons.size; i++) {
            Button button = buttons.get(i);
            button.x = Game.game.canvas.getWidth()-BUTTON_WIDTH + 10;
            button.y = i == 0 ? 0 : buttons.get(i-1).y + buttons.peek().height;
        }
    }

    public void receiveResults() {
        Game.logInfo("-- New Turn --");
        for (BattleParty party : parties) {
            for (BattlePokemon pokemon : party.members) {
                if (pokemon == null || pokemon.getHP() <= 0) continue;
                pokemon.targetingParty = -1;
                pokemon.targetingSlot = -1;
            }
        }
        playingResults = true;
        waitingForCalculator = false;
    }

    private void switchScreen() {
        Array<Pokemon> pokemonToEvolve = null;
        Array<Species> speciesToEvolveInto = null;
        if (parties[0].members.length != Game.game.data.player.party.length) {
            Game.logError("Battle party and player party do not have the same length.");
            return;
        }
        for (int i = 0; i < parties[0].members.length; i++) {
            if (parties[0].members[i] != null) {
                PartyPokemon p = new PartyPokemon(parties[0].members[i]);
                Game.game.data.player.party[parties[0].members[i].originalPartyMemberSlot] = p;
                Species s = parties[0].members[i].evolvesInto();
                if (s != null) {
                    if (pokemonToEvolve == null) {
                        pokemonToEvolve = new Array<>();
                        speciesToEvolveInto = new Array<>();
                    }
                    pokemonToEvolve.add(p);
                    speciesToEvolveInto.add(s);
                }
            }
        }
        if (pokemonToEvolve != null) Game.game.switchScreens(new EvolutionScreen(pokemonToEvolve, speciesToEvolveInto));
        else if (Game.game.map != null) Game.game.switchScreens(new OverworldScreen());
        else Game.game.switchScreens(new TitleScreen());
    }
    private void addButton(String name, Runnable runnable) {
        Button b = new Button(Game.game.canvas.getWidth()- BUTTON_WIDTH+10,
                buttons.isEmpty() ? 0 : buttons.peek().y + buttons.peek().height,
                BUTTON_WIDTH, buttonTexture.getHeight()+1, true) {
            @Override public void hover() {
                if (runnable != null) Game.game.audio.playSound("sfx/guiCursor.ogg");
            }
            @Override public void clicked() {
                if (runnable != null) {
                    runnable.run();
                    Game.game.audio.playSound("sfx/guiSelect.ogg");
                }
            }
            @Override public void render() {
                int renderX;
                if (runnable == null) {
                    renderX = x+15;
                    Game.game.canvas.setColor(Color.GRAY);
                } else {
                    renderX = justSelected ? x - 15 : justUnselected ? x + 5 : selected ? x - 10 : x;
                }
                int slideOff = Math.max(0, 150 - buttonSlide*25 + y);
                buttonTexture.render(renderX+slideOff, y+1);
                Game.game.canvas.drawText(renderX+slideOff+10, y+3, name);
                if (runnable == null) Game.game.canvas.resetColor();
            }
        };
        if (buttons.notEmpty()) {
            buttons.peek().upButton = b;
            b.downButton = buttons.peek();
        }
        buttons.add(b);
    }
    private void addAction(BattlePokemon.Action action) {
        addAction(action, false);
    }
    private void addAction(BattlePokemon.Action action, boolean forceSend) {
        makeMenu(MenuType.MAIN);
        if (action != null) parties[0].members[actionI].action = action;
        if (action != null && (++actionI >= parties[0].numBattling || forceSend)) {
            for (int i = actionI; i < parties[0].members.length; i++)
                if (parties[0].members[i] != null)
                    parties[0].members[i].action = null;
            actionI = 0;
            waitingForCalculator = true;
            buttons.forEach(Button::hide);
            calculator.beginCalculateTurn();
        } else {
            BattlePokemon nextPokemon = parties[0].members[actionI];
            if (nextPokemon == null || nextPokemon.getHP() <= 0) return;
            if (nextPokemon.hasEffect(BattlePokemon.Effect.CHARGE)) {
                addAction(new BattlePokemon.MoveAction(Game.game.data.getMove(nextPokemon.getEffectInt(BattlePokemon.Effect.CHARGE))));
            }
        }
    }
    private void makeMenu(MenuType menuType) {
        buttonSlide = -1;
        buttons.forEach(Button::hide);
        buttons.clear();
        switch (menuType) {
            case MAIN:
                addButton("End and Heal", () -> {
                    parties[0].members[0].setHP(9999999);
                    switchScreen();
                });
                if (actionI == 0) addButton("Run", () -> addAction(new BattlePokemon.RunAction(), true));
                else addButton("Back", () -> {actionI--; makeMenu(MenuType.MAIN);});
                addButton("Party", () -> makeMenu(MenuType.PARTY));
                addButton("Bag", null);
                addButton("Fight", () -> makeMenu(MenuType.FIGHT));
                break;
            case FIGHT:
                addButton("Back", () -> makeMenu(MenuType.MAIN));
                Move[] moves = parties[0].members[actionI].getMoves();
                for (int i = moves.length-1; i >= 0; i--) {
                    int finalI = i;
                    if (moves[i] == null) addButton("", null);
                    else addButton(moves[i].name, () -> addAction(new BattlePokemon.MoveAction(moves[finalI])));
                }
                break;
            case PARTY:
                addButton("Back", () -> makeMenu(MenuType.MAIN));
                for (int i = parties[0].members.length-1; i >= 0; i--) {
                    int finalI = i;
                    if (parties[0].members[i] == null) addButton("", null);
                    else if (i < parties[0].numBattling) addButton(parties[0].members[i].getName(), null);
                    else {
                        boolean available = true;
                        if (!calculator.canSwitch(parties[0].members[actionI])) available = false;
                        else for (int j = 0; j < actionI; j++) {
                            if (parties[0].members[j].action instanceof BattlePokemon.SwitchAction &&
                                    ((BattlePokemon.SwitchAction)parties[0].members[j].action).slot == i) {
                                available = false;
                                break;
                            }
                        }
                        addButton(parties[0].members[i].getName(), !available ? null : () -> addAction(new BattlePokemon.SwitchAction(finalI)));
                    }
                }
                break;
        }
        buttons.peek().upButton = buttons.first();
        buttons.first().downButton = buttons.peek();
    }

    private enum MenuType {
        MAIN, FIGHT, PARTY
    }
}
