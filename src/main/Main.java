package main;

import fighting.Fighter;
import fighting.Fighter.Direction;
import fighting.FightingField;
import gui.Render;

public class Main {

	public static void main(String[] args) {
		new Main();
	}

	private String[] VALID_KEYS = { "w", "a", "s", "d", "i", "j", "k", "l", " " };

	private final Render render;
	private Fighter player, enemy;
	private FightingField fightingField;

	public Main() {
		render = new Render(this, VALID_KEYS);
		initGame();
	}

	private void initGame() {
		player = new Fighter("X", 0, 1, 0);
		fightingField = new FightingField(this, player, enemy);
		render.println("Welcome to our small game!");
	}

	public void keyPressed(String key) {
		switch (key) {
		case "w":
			fightingField.movePlayer(Direction.Up);
			break;
		case "s":
			fightingField.movePlayer(Direction.Down);
			break;
		case "a":
			fightingField.movePlayer(Direction.Left);
			break;
		case "d":
			fightingField.movePlayer(Direction.Right);
			break;
		default:
			return;
		}
	}

	public void println(String arg) {
		render.println(arg);
	}

}
