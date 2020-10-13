package com.celtican.pokemon.battle.results;

import com.badlogic.gdx.utils.Array;
import com.celtican.pokemon.Game;
import com.celtican.pokemon.battle.BattlePokemon;
import com.celtican.pokemon.utils.data.Move;
import com.celtican.pokemon.utils.data.Vector2Int;
import com.celtican.pokemon.utils.graphics.Button;

public class LearnMovesResult extends Result {

    private final BattlePokemon pokemon;
    private final Move move;
    private final TextResult text;
    private Stage stage;
    private int moveToReplace = -1;
    private Array<Button> buttons;

    private LearnMovesResult(BattlePokemon pokemon, Move move) {
        this.pokemon = pokemon;
        this.move = move;
        this.text = new TextResult(false);
    }
    public LearnMovesResult(BattlePokemon pokemon, Array<Move> moves) {
        this(pokemon, moves.first());
        for (int i = 1; i < moves.size; i++) {
            new LearnMovesResult(pokemon, moves.get(i));
        }
    }

    @Override public boolean start() {
        switchTo(Stage.LEARN_FIRST_MOVE);
        return false;
    }

    @Override public void update() {
        text.update();
        if (text.text.isFinished()) {
            switch (stage) {
                case LEARN_FIRST_MOVE:
                    Move[] curMoves = pokemon.getMoves();
                    if (moveToReplace == -1) {
                        boolean learnedMove = false;
                        for (int i = 0; i < 4; i++) {
                            if (curMoves[i] == null) {
                                pokemon.setMove(move, i);
                                learnedMove = true;
                                break;
                            }
                        }
                        if (learnedMove) {
                            switchTo(Stage.DISPLAY_LEARNED_MOVE_TEXT);
                        } else {
                            switchTo(Stage.DISPLAY_WANTS_TO_LEARN_MOVE_TEXT);
                        }
                    } else {
                        pokemon.setMove(move, moveToReplace);
                        switchTo(Stage.DISPLAY_LEARNED_MOVE_TEXT);
                    }
                    break;
                case DISPLAY_LEARNED_MOVE_TEXT:
                case DISPLAY_DID_NOT_LEARN_TEXT:
                    parent.nextResult(); break;
                case DISPLAY_WANTS_TO_LEARN_MOVE_TEXT: switchTo(Stage.DISPLAY_HOWEVER_TEXT); break;
                case DISPLAY_HOWEVER_TEXT: switchTo(Stage.DISPLAY_WHAT_MOVE_TEXT); break;
                case DISPLAY_WHAT_MOVE_TEXT: switchTo(Stage.DISPLAY_MOVES_BUTTONS); break;
                case DISPLAY_MOVES_BUTTONS:
                case DISPLAY_CONFIRM_BUTTONS:
                    break;
                case DISPLAY_ARE_YOU_SURE_TEXT:
                case DISPLAY_ARE_YOU_SURE_CANCEL_TEXT:
                    switchTo(Stage.DISPLAY_CONFIRM_BUTTONS); break;
                case DISPLAY_321_TEXT: switchTo(Stage.DISPLAY_POOF_TEXT); break;
                case DISPLAY_POOF_TEXT: switchTo(Stage.DISPLAY_FORGOT_TEXT); break;
                case DISPLAY_FORGOT_TEXT: switchTo(Stage.LEARN_FIRST_MOVE); break;
                default: Game.logError("Unhandled stage: " + stage + "."); break;
            }
        }
    }
    @Override public void render() {
        text.render();
        if (buttons != null) {
            setButtonPositions();
            buttons.forEach(Button::render);
        }
    }

    private void switchTo(Stage stage) {
        String s = null;
        this.stage = stage;
        if (buttons != null) {
            buttons.forEach(Button::hide);
            buttons.clear();
        }
        switch (stage) {
            case DISPLAY_LEARNED_MOVE_TEXT: s = pokemon.getName() + " learned " + move.name + "!"; break;
            case DISPLAY_WANTS_TO_LEARN_MOVE_TEXT: s = pokemon.getName() + " wants to learn " + move.name + "."; break;
            case DISPLAY_HOWEVER_TEXT: s = "However, " + pokemon.getName() + " already knows four moves."; break;
            case DISPLAY_WHAT_MOVE_TEXT: s = "What move should " + pokemon.getName() + " forget to learn " + move.name + "?"; break;
            case DISPLAY_ARE_YOU_SURE_TEXT: s = "Are you sure you want " + pokemon.getName() + " to forget " + pokemon.getMove(moveToReplace).name + " to learn " + move.name + "?"; break;
            case DISPLAY_321_TEXT: s = "3... 2... 1... and..."; break;
            case DISPLAY_POOF_TEXT: s = "Poof!"; break;
            case DISPLAY_FORGOT_TEXT: s = pokemon.getName() + " forgot " + pokemon.getMove(moveToReplace).name + ", and..."; break;
            case DISPLAY_ARE_YOU_SURE_CANCEL_TEXT: s = "Are you sure you want " + pokemon.getName() + " to stop learning " + move.name + "?"; break;
            case DISPLAY_DID_NOT_LEARN_TEXT: s = pokemon.getName() + " skipped learning " + move.name + "."; break;
            case DISPLAY_MOVES_BUTTONS:
                addButton(move.name, () -> {
                    moveToReplace = -1;
                    switchTo(Stage.DISPLAY_ARE_YOU_SURE_CANCEL_TEXT);
                });
                for (int i = 3; i >= 0; i--) {
                    int finalI = i;
                    addButton(pokemon.getMove(i).name, () -> {
                        moveToReplace = finalI;
                        switchTo(Stage.DISPLAY_ARE_YOU_SURE_TEXT);
                    });
                }
                break;
            case DISPLAY_CONFIRM_BUTTONS:
                addButton("Yes", () -> switchTo(moveToReplace == -1 ? Stage.DISPLAY_DID_NOT_LEARN_TEXT : Stage.LEARN_FIRST_MOVE));
                addButton("No", () -> switchTo(Stage.DISPLAY_WANTS_TO_LEARN_MOVE_TEXT));
                break;
            default: Game.logError("Unhandled stage: " + stage); break;
        }
        if (s != null) text.text.setText(s);
    }
    private void addButton(String text, Runnable runnable) {
        if (buttons == null) buttons = new Array<>();
        Vector2Int bounds = Game.game.canvas.getBoundsOfText(text);
        buttons.add(new Button(0, 0, bounds.x+4, bounds.y+4, true) {
            @Override public void clicked() {
                if (runnable != null) runnable.run();
            }
            @Override public void render() {
                super.render();
                Game.game.canvas.drawText(x + 2, y + 3, text);
            }
        });
    }
    private void setButtonPositions() {
        if (buttons == null || buttons.isEmpty()) return;
        int totalButtonHeight = buttons.get(0).height;
        for (int i = 1; i < buttons.size; i++)
            totalButtonHeight += buttons.get(i).height + 1;
        int middleCanvas = Game.game.canvas.getWidth()/2;
        int y = Game.game.canvas.getHeight()/2 - totalButtonHeight/2;
        for (int i = 0; i < buttons.size; i++) {
            Button button = buttons.get(i);
            button.x = middleCanvas - button.width/2;
            button.y = y;
            y += button.height + 1;
        }
    }

    public static Array<Move> canLearnNewMoves(BattlePokemon pokemon, int oldLevel) {
        if (oldLevel == pokemon.getLevel()) return null;
        int[] moveLevels = pokemon.getSpecies().getMoveLevels();
        int[] moveIndexes = pokemon.getSpecies().getMoves();
        Array<Move> moves = null;
        for (int i = moveLevels.length - 1; i >= 0; i--) {
            if (moveLevels[i] <= pokemon.getLevel() && moveLevels[i] > oldLevel) {
                boolean alreadyKnowsMove = false;
                for (int j = 0; j < 4; j++) {
                    Move knownMove = pokemon.getMove(j);
                    if (knownMove != null && moveIndexes[i] == knownMove.index) {
                        alreadyKnowsMove = true;
                        break;
                    }
                }
                if (!alreadyKnowsMove) {
                    if (moves == null) moves = new Array<>();
                    moves.add(Game.game.data.getMove(moveIndexes[i]));
                }
            }
        }
        return moves;
    }

    private enum Stage {
        LEARN_FIRST_MOVE, DISPLAY_LEARNED_MOVE_TEXT, DISPLAY_WANTS_TO_LEARN_MOVE_TEXT, DISPLAY_HOWEVER_TEXT,
        DISPLAY_WHAT_MOVE_TEXT, DISPLAY_MOVES_BUTTONS, DISPLAY_ARE_YOU_SURE_TEXT, DISPLAY_CONFIRM_BUTTONS,
        DISPLAY_321_TEXT, DISPLAY_POOF_TEXT, DISPLAY_FORGOT_TEXT, DISPLAY_ARE_YOU_SURE_CANCEL_TEXT,
        DISPLAY_DID_NOT_LEARN_TEXT
    }
}
