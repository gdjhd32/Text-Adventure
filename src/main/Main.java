package main;

import gui.Render;

public class Main {

	public static void main(String[] args) {
		new Main();
	}
	
	private Render render;
	
	public Main() {
		render = new Render(this);
		render.println("Welcome to our small game!");
		render.println("\n");
	}
	
	public void keyPressed(String key) {
		render.changeLast(key);
	}

}
