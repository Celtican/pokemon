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

        buttonTexture = new Texture("spritesheets/battle.atlas", "button");
        buttons = new Array<>();
        makeMenu(MenuType.MAIN);
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
        Game.game.audio.playMusic("bgm/wildBattle.ogg");
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
            @Override public void hover() {
                Game.game.audio.playSound("sfx/guiCursor.ogg");
            }
            @Override public void clicked() {
                if (runnable != null) {
                    runnable.run();
                    Game.game.audio.playSound("sfx/guiSelect.ogg");
                }
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
