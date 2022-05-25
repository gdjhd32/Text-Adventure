package main;

import fighting.Armor;
import fighting.CombatAutomat;
import fighting.Fighter;
import fighting.Timer;
import fighting.Weapon;
import gui.Render;

public class Main {

	public static void main(String[] args) {
		new Main();
	}

	private String[] VALID_KEYS = { "w", "a", "s", "d", "i", "j", "k", "l", " " };

	private final Render render;
	private Fighter player, enemy;
	private CombatAutomat fightingField;

	public Main() {
		player = new Fighter("X", 1, 10, 1);

    player.setWeapon(new Weapon("Wooden Sword", 2, "Broadsword"));
		player.setArmor(new Armor("Leather Armor", 2, "Leather"));
		
		enemy = new Fighter("Skeleton Soldier", 2, 2, 2);

		enemy.setWeapon(new Weapon("Bone Sword", 2, "Broadsword"));
		enemy.setArmor(new Armor("Iron Armor", 2, "Iron"));
		
		render = new Render(this, VALID_KEYS);
		render.refreshChangeableLabel();
		render.refreshWeaponLabel();
		render.refreshArmorLabel();
		render.println("Welcome to our small game!");
		
		fightingField = new CombatAutomat(player, enemy, render);
	}

	/**
	 * Only for the player input.
	 * 
	 * @param key
	 */
	public void keyPressed(String key) {
		fightingField.keyPressed(key, true);
	}

	public Fighter getPlayer() {
		return player;
	}

	public Fighter getEnemy() {
		return enemy;
	}

}
