package com.celtican.pokemon.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.celtican.pokemon.Game;

public class DesktopLauncher {
	public static void main (String[] args) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.forceExit = false;
		config.width = 1366;
		config.height = 768;
		Game game = new Game();
		for (String arg : args) {
			if (arg.equals("-debug")) {
				game.isDebug = true;
				break;
			}
		}
		game.isIDE = System.getProperty("user.dir").endsWith("\\Pokemon\\core\\assets");
		new LwjglApplication(game, config);
	}
}
