package com.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.game.Game;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		boolean borderLess = false;
		config.useGL30 = true;
		if (borderLess) {
			config.width = LwjglApplicationConfiguration.getDesktopDisplayMode().width ;
			config.height = LwjglApplicationConfiguration.getDesktopDisplayMode().height ;
		} else {
			config.width = LwjglApplicationConfiguration.getDesktopDisplayMode().width / 2;
			config.height = LwjglApplicationConfiguration.getDesktopDisplayMode().height / 2;
		}
		//config.fullscreen=true;
		config.vSyncEnabled = true;
		config.resizable = false;
		config.samples = 2;
		//System.setProperty("org.lwjgl.opengl.Window.undecorated", "true");

		new LwjglApplication(new Game(), config);
	}
}
