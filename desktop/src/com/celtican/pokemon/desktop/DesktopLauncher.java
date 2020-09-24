package com.celtican.pokemon.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.celtican.pokemon.Game;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.forceExit = false;
		config.width = 1366;
		config.height = 768;
		Game game = new Game();
		game.isIDE = System.getProperty("user.dir").endsWith("\\Pokemon\\core\\assets");
		new LwjglApplication(game, config);
	}
}
