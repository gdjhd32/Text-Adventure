package main;

import gui.Render;

public class Main {

	public static void main(String[] args) {
		new Main();
	}
	
	private String[] VALID_KEYS = { "w", "a", "s", "d"};
	
	private Render render;
	
	public Main() {
		render = new Render(this, VALID_KEYS);
		render.println("Welcome to our small game!");
		render.println("\n");
	}
	
	public void keyPressed(String key) {
		render.changeLast(key);
	}

}
