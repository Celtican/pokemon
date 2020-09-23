package com.celtican.pokemon.screens;

import com.badlogic.gdx.utils.Array;
import com.celtican.pokemon.Game;
import com.celtican.pokemon.battle.*;
import com.celtican.pokemon.utils.data.Move;
import com.celtican.pokemon.utils.data.PokemonPC;
import com.celtican.pokemon.utils.graphics.Button;
import com.celtican.pokemon.utils.graphics.Texture;

public class BattleScreen extends Screen {

    // prepare for battle!

    public final BattleCalculator calculator;
    public final ResultHandler resultHandler;
    private int actionI = 0;

    private final Texture buttonTexture;
    public final Array<Button> buttons;

    public final BattleParty[] parties;

    public BattleScreen() {
        resultHandler = new ResultHandler(this);

        parties = new BattleParty[] {
                new BattleParty(new BattlePokemon[] {new BattlePokemon(new PokemonPC(Game.game.data.getRandomSpecies(), 15), 0, 0), null, null, null, null, null}, 1, 0),
                new BattleParty(new BattlePokemon[] {new BattlePokemon(new PokemonPC(Game.game.data.getRandomSpecies(), 15), 1, 0), null, null, null, null, null}, 1, 1)
        };
        calculator = new BattleCalculator(parties);
        for (int i = 0; i < parties.length; i++)
            for (int j = 0; j < parties[i].numBattling; j++)
                if (parties[i].members[j] != null)
                    parties[i].displayMembers.add(new PokemonDisplay(parties[i].members[j], i != 0));
//        userPokemon = new Array<>();
//        userPokemonDisplay = new Array<>();
//        userPokemon.add(new BattlePokemon(new PokemonPC(Game.game.data.getSpecies(1), 15)));
//        userPokemonDisplay.add(new PokemonDisplay(userPokemon.first(), false));
//        compPokemon = new Array<>();
//        compPokemonDisplay = new Array<>();
//        compPokemon.add(new BattlePokemon(new PokemonPC(Game.game.data.getSpecies(1), 15)));
//        compPokemonDisplay.add(new PokemonDisplay(userPokemon.first(), true));
//
//        calculator = new BattleCalculator(new BattleParty[] {
//                new BattleParty(new BattlePokemon[] {userPokemon.first(), null, null, null, null, null}, 1),
//                new BattleParty(new BattlePokemon[] {compPokemon.first(), null, null, null, null, null}, 1)
//        });

        buttonTexture = new Texture("spritesheets/battle.atlas", "button");
        buttons = new Array<>();
        makeMenu(MenuType.MAIN);
//        p1 = new PokemonPC(Game.game.data.getSpecies(1));
//        pD1 = new PokemonDisplay(p1, true);
//        p2 = new PokemonPC(Game.game.data.getSpecies(1));
//        pD2 = new PokemonDisplay(p2, false);
    }

    @Override public void update() {
        for (BattleParty party : parties) party.displayMembers.forEach(PokemonDisplay::update);
        if (resultHandler.hasResults()) resultHandler.update();
    }
    @Override public void render() {
        for (int i = parties.length-1; i >= 0; i--) parties[i].displayMembers.forEach(PokemonDisplay::renderTexture);
        for (int i = parties.length-1; i >= 0; i--) parties[i].displayMembers.forEach(PokemonDisplay::renderHealth);
        if (resultHandler.hasResults()) resultHandler.render();
        else buttons.forEach(Button::render);
    }

    @Override public void hide() {
        buttons.forEach(Button::hide);
    }
    @Override public void show() {
        buttons.forEach(Button::show);
    }
    @Override public void resize(int width, int height) {
        for (int i = 0; i < buttons.size; i++) {
            Button button = buttons.get(i);
            button.x = Game.game.canvas.getWidth()- buttonTexture.getWidth() + 10;
            button.y = i == 0 ? 0 : buttons.get(i-1).y + buttons.peek().height;
        }
    }

    private void addButton(String name, Runnable runnable) {
        Button b = new Button(Game.game.canvas.getWidth()- buttonTexture.getWidth()+10,
                buttons.isEmpty() ? 0 : buttons.peek().y + buttons.peek().height,
                buttonTexture.getWidth(), buttonTexture.getHeight()+1, true) {
            @Override public void clicked() {
                if (runnable != null) runnable.run();
            }
            @Override public void render() {
                int renderX = justSelected ? x - 15 : justUnselected ? x + 5 : selected ? x - 10 : x;
                buttonTexture.render(renderX, y+1);
                Game.game.canvas.drawText(renderX+10, y+3, name);
            }
        };
        if (buttons.notEmpty()) {
            buttons.peek().upButton = b;
            b.downButton = buttons.peek();
        }
        buttons.add(b);
    }
    private void addAction(BattlePokemon.Action action) {
//        for (int i = 0; i < actionQueue.length; i++) {
//            if (actionQueue[i] == null) {
//                actionQueue[i] = action;
//                if (i == actionQueue.length-1) {
//                    resultHandler.setResults(calculator.calculateTurn(actionQueue));
//                    Arrays.fill(actionQueue, null);
//                    makeMenu(MenuType.MAIN);
//                    buttons.forEach(Button::hide);
//                }
//                break;
//            }
//        }
        parties[0].members[actionI].action = action;
        makeMenu(MenuType.MAIN);
        if (++actionI >= parties[0].numBattling) {
            actionI = 0;
            resultHandler.setResults(calculator.calculateTurn());
            buttons.forEach(Button::hide);
        }
    }
    private void makeMenu(MenuType menuType) {
        buttons.forEach(Button::hide);
        buttons.clear();
        switch (menuType) {
            case MAIN:
                addButton("Run", null);
                addButton("Party", null);
                addButton("Bag", null);
                addButton("Fight", () -> makeMenu(MenuType.FIGHT));
                break;
            case FIGHT:
                addButton("Back", () -> makeMenu(MenuType.MAIN));
                for (int i = 3; i >= 0; i--) {
                    Move move = calculator.getUserParty().members[actionI].getMove(i);
                    if (move == null) addButton("", null);
                    else addButton(move.name, () -> addAction(new BattlePokemon.MoveAction(move)));
                }
                break;
        }
        buttons.peek().upButton = buttons.first();
        buttons.first().downButton = buttons.peek();
    }

    private enum MenuType {
        MAIN, FIGHT
    }
}
