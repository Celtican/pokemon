package com.celtican.pokemon;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.celtican.pokemon.overworld.Map;
import com.celtican.pokemon.screens.LoadingScreen;
import com.celtican.pokemon.screens.Screen;
import com.celtican.pokemon.utils.*;
import com.celtican.pokemon.utils.data.Pokemon;

public class Game extends ApplicationAdapter {

	public static final String version = "PreAlpha v0";

	public final static boolean USE_SCALED_EXP_FORMULA = false;
	public static final boolean JSON_PRETTY_PRINT = true;
	public static final byte TARGET_FRAME_RATE = 60;
	public static final float MILLIS_PER_FRAME = (1f/TARGET_FRAME_RATE*1000);
	public static final byte TILE_SIZE = 16;
	public static final int CHUNK_SIZE = TILE_SIZE * 8;
	public static Game game;

	public AssetHandler assets;
	public RenderHandler canvas;
	public DataHandler data;
	public InputHandler input;
	public AudioHandler audio;

	public Map map;

	public boolean isDebug;
	public boolean isIDE;
	public boolean doLogDamage;
	public boolean doLogPokemonGeneration;
	public boolean doGeneratePokemonWithPerfectStats;

	public byte pixelSize = 0;
	public int frame = 0;
	public Screen screen;
	private Screen screenToSwitchTo;
	public boolean isFastForward = false;

	@Override public void create () {
		logInfo("Launching Pokemon Game " + version);

		game = this;
		assets = new AssetHandler();
		canvas = new RenderHandler();
		data = new DataHandler();
		input = new InputHandler();
		audio = new AudioHandler();

		doLogDamage = isDebug;
		doLogPokemonGeneration = isDebug;

		Pokemon.Type.setupTypes(); // set up type weaknesses and whatnot

		switchScreens(new LoadingScreen());
	}

	@Override public void render () {
		game = this;
		frame++;
		try {
			input.update();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (screenToSwitchTo != null) {
			try {
				if (screen != null)
					screen.hide();
				screen = screenToSwitchTo;
				screenToSwitchTo = null;
				screen.show();
				screen.resize(canvas.getWidth(), canvas.getHeight());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		try {
			assets.update();
			audio.update();
			if (screen != null)
				if (isFastForward)
					for (int i = 0; i < 10; i++)
						screen.update();
				else
					screen.update();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			canvas.update();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Override public void resize(int width, int height) {
		try {
			changePixelSize((byte)Math.max(1, MathUtils.round(height/200f)));
			canvas.resize(width, height);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Override public void dispose () {
		canvas.dispose();
		assets.dispose();
		if (screen != null)
			screen.dispose();
		if (screenToSwitchTo != null)
			screenToSwitchTo.dispose();
	}

	private void changePixelSize(byte newPixelSize) {
		// 1080p = 5
		// 786 = 4
		if (newPixelSize == pixelSize) return;
		pixelSize = newPixelSize;
		canvas.setupFont();
	}

	public void switchScreens(Screen screen) {
		if (screenToSwitchTo != null)
			screenToSwitchTo.dispose();
		screenToSwitchTo = screen;
	}

	public static void logWarning(String message) {
		Gdx.app.log("WARN", message);
	}
	public static void logInfo(String message) {
		Gdx.app.log("info", message);
	}
	public static void logError(String message) {
		new Exception(message).printStackTrace();
	}
}
