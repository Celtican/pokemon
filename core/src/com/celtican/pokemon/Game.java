package com.celtican.pokemon;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.celtican.pokemon.overworld.Map;
import com.celtican.pokemon.screens.LoadingScreen;
import com.celtican.pokemon.screens.Screen;
import com.celtican.pokemon.utils.AssetHandler;
import com.celtican.pokemon.utils.DataHandler;
import com.celtican.pokemon.utils.InputHandler;
import com.celtican.pokemon.utils.RenderHandler;

public class Game extends ApplicationAdapter {

	public static final String version = "PreAlpha v0";

	public static final boolean JSON_PRETTY_PRINT = true;
	public static final byte TARGET_FRAME_RATE = 60;
	public static final float MILLIS_PER_FRAME = (1f/TARGET_FRAME_RATE*1000);
	public static final byte PIXEL_SIZE = 4;
	public static final byte TILE_SIZE = 16;
	public static final int CHUNK_SIZE = TILE_SIZE * 8;
	public static Game game;

	public AssetHandler assets;
	public RenderHandler canvas;
	public DataHandler data;
	public InputHandler input;

	public Map map;

	public int frame = 0;
	public Screen screen;
	private Screen screenToSwitchTo;

	@Override public void create () {
		logInfo("Launching Pokemon Game " + version);

		game = this;
		assets = new AssetHandler();
		canvas = new RenderHandler();
		data = new DataHandler();
		input = new InputHandler();

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
			if (screen != null)
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
