package com.celtican.pokemon.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.celtican.pokemon.Game;
import com.celtican.pokemon.utils.data.Pokemon;
import com.celtican.pokemon.utils.data.Species;
import com.celtican.pokemon.utils.data.Vector2Int;
import com.celtican.pokemon.utils.graphics.AnimatedTexture;
import com.celtican.pokemon.utils.graphics.Text;

public class EvolutionScreen extends Screen {

    private static final int POKEMON_SCALE = 3;

    private final Array<Pokemon> pokemonQueue;
    private final Array<Species> speciesQueue;
    private String previousName;
    private Pokemon pokemon;
    private Species species;

    private AnimatedTexture texture;
    private Text text;
    private Stage stage;
    private int frame;

    public EvolutionScreen(Pokemon pokemon, Species species) {
        this(new Array<>(new Pokemon[]{pokemon}), new Array<>(new Species[]{species}));
    }
    public EvolutionScreen(Array<Pokemon> pokemon, Array<Species> species) {
        if (pokemon.size != species.size) {
            Game.logError("Attempted to create an EvolutionScreen when the pokemon array and species array have different sizes.");
        }
        pokemonQueue = pokemon;
        speciesQueue = species;
        text = new Text();
        nextPokemon();
    }

    @Override public void update() {
        text.update();
        if (text.isFinished()) {
            switch (stage) {
                case TEXT_OH: if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) switchTo(Stage.EVOLUTION); break;
                case EVOLUTION:
                    if (--frame == Game.TARGET_FRAME_RATE*-3)
                        switchTo(Stage.CRY);
                    else if (frame == 0) {
                        pokemon.setSpecies(species);
                        texture = pokemon.getAnimatedTexture();
                    }
                    break;
                case CRY: if (--frame == 0) switchTo(Stage.CONGRATULATIONS); break;
                case CONGRATULATIONS: nextPokemon(); break;
            }
        }
    }
    @Override public void render() {
        Vector2Int pos = getPos();
        texture.render(pos.x, pos.y, POKEMON_SCALE);
        text.render();
    }

    private void nextPokemon() {
        if (pokemonQueue.isEmpty()) {
            Game.game.switchScreens(Game.game.map == null ? new TitleScreen() : new OverworldScreen(Game.game.map));
            return;
        }

        pokemon = pokemonQueue.removeIndex(0);
        species = speciesQueue.removeIndex(0);
        previousName = pokemon.getName();
        texture = pokemon.getAnimatedTexture();
        switchTo(Stage.TEXT_OH);
    }

    private Vector2Int getPos() {
        int hw = Game.game.canvas.getWidth()/2;
        Vector2Int pos = new Vector2Int(hw,
                Game.game.canvas.getHeight()/2 - texture.getHeight()*POKEMON_SCALE/2);
        if (stage == Stage.EVOLUTION) {
            float percent = (Math.abs(frame) - Game.TARGET_FRAME_RATE*3) / (3f * Game.TARGET_FRAME_RATE);
            percent = Math.abs(MathUtils.cos(MathUtils.PI * percent) - 1)/2;
            if (frame%2 == 0) pos.x += percent*(hw + texture.getWidth()*POKEMON_SCALE/2);
            else pos.x -= percent*(hw + texture.getWidth()*POKEMON_SCALE/2);
        }
        pos.x -= texture.getWidth()*POKEMON_SCALE/2;
        return pos;
    }
    private void switchTo(Stage stage) {
        this.stage = stage;
        switch (stage) {
            case TEXT_OH:
                Game.game.audio.playMusic("bgm/evolutionStart.ogg", false);
                text.setText("Oh?");
                text.idleTicks = 0;
                break;
            case EVOLUTION:
                text.setText(null);
                Game.game.audio.playMusic("bgm/evolution.ogg");
                frame = Game.TARGET_FRAME_RATE * 3;
                break;
            case CRY:
                Game.game.audio.stopMusic();
                text.setText(null);
                pokemon.cry();
                frame = Game.TARGET_FRAME_RATE * 5 / 2;
                break;
            case CONGRATULATIONS:
                Game.game.audio.playMusic("bgm/evolutionFanfare.ogg", false);
                text.setText("Congratulations! Your " + previousName + " evolved into a " + species.getName() + "!");
                text.idleTicks = Game.TARGET_FRAME_RATE*4;
                break;
        }
    }

    private enum Stage {
        TEXT_OH, EVOLUTION, CRY, CONGRATULATIONS
    }
}
