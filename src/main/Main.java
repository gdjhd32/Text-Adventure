package main;

import fighting.Armor;
import fighting.Armor.ArmorType;
import fighting.CombytSystem;
import fighting.Fighter;
import fighting.Fighter.Direction;
import fighting.Weapon;
import fighting.Weapon.WeaponType;
import gui.Render;

public class Main {

	public static void main(String[] args) {
		new Main();
	}

	private String[] VALID_KEYS = { "w", "a", "s", "d", "i", "j", "k", "l", " " };

	private final Render render;
	private Fighter player, enemy;
	private CombytSystem fightingField;

	public Main() {
		player = new Fighter("X", 1, 1, 1);
		player.setWeapon(new Weapon("Wooden Sword", 1, WeaponType.Broadsword));
		player.setArmor(new Armor("Leather Armor", 1, ArmorType.Leather));
		
		enemy = new Fighter("Skeleton Soldier", 1, 1, 1);
		enemy.setWeapon(new Weapon("Bone Sword", 2, WeaponType.Broadsword));
		enemy.setArmor(new Armor("Iron Armor", 2, ArmorType.Iron));
		
		fightingField = new CombytSystem(this, player, enemy);
		
		render = new Render(this, VALID_KEYS);
		render.refreshChangeableLabel();
		render.refreshWeaponLabel();
		render.refreshArmorLabel();
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

	public Fighter getPlayer() {
		return player;
	}

	public Fighter getEnemy() {
		return enemy;
	}

}
