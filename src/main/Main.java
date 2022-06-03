package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import dungeon.Dungeon;
import fighting.Armor;
import fighting.CombatAutomat;
import fighting.Fighter;
import fighting.Weapon;
import gui.Render;

public class Main {

	public static void main(String[] args) {
		new Main();
	}

	private String[] VALID_KEYS = { "w", "a", "s", "d", "i", "j", "k", "l", " " };

	private final Render render;
	private Fighter player;
	private CombatAutomat fightingField;
	@SuppressWarnings("unused")
	private Dungeon dungeon;

	private Weapon[] weapons;
	private Armor[] armor;
	private Fighter[] enemies;

	public Main() {
		readAssets(new File("assets/Dungeon/config.txt"));

		player = new Fighter("X", 1, 10, 1);
		player.setWeapon(new Weapon("Wooden Sword", 2, "Broadsword"));
		player.setArmor(new Armor("Leather Armor", 2, "Leather"));

		render = new Render(this, VALID_KEYS, player, enemies[0]);
		render.println("Welcome to our small game!");
		dungeon = new Dungeon(this, render);

	}

	/**
	 * Only for the player input.
	 * 
	 * @param key
	 */
	public void keyPressed(String key) {
		if (render.isFighting())
			fightingField.keyPressed(key, true);
		dungeon.keyPressed(key);
	}

	public Fighter getPlayer() {
		return player;
	}

	public void initFight(String enemy) {
		for (int i = 0; i < enemies.length; i++) {
			if (enemy.equals(enemies[i].NAME)) {
				render.startFight(enemies[i]);
				fightingField = new CombatAutomat(player, enemies[i], render);
				return;
			}
		}
	}

	private void readAssets(File file) {
		Scanner scanner = null;
		try {
			scanner = new Scanner(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		}

		int weaponCount = 0;
		boolean weaponLines = false;
		int armorCount = 0;
		boolean armorLines = false;
		int enemyCount = 0;
		boolean enemyLines = false;
		String line;
		while (scanner.hasNext()) {
			line = scanner.nextLine().trim();
			if (!line.substring(0, 2).equals("//")) {
				if (line.equals("Weapons:")) {
					weaponLines = true;
					armorLines = false;
					enemyLines = false;
					continue;
				}
				if (line.equals("Armor:")) {
					weaponLines = false;
					armorLines = true;
					enemyLines = false;
					continue;
				}
				if (line.equals("Enemies:")) {
					weaponLines = false;
					armorLines = false;
					enemyLines = true;
					continue;
				}
				if (weaponLines) {
					weaponCount++;
					continue;
				}
				if (armorLines) {
					armorCount++;
					continue;
				}
				if (enemyLines) {
					enemyCount++;
					continue;
				}
			}
		}
		scanner.close();

		weapons = new Weapon[weaponCount];
		armor = new Armor[armorCount];
		enemies = new Fighter[enemyCount];

		scanner = null;
		try {
			scanner = new Scanner(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		}

		weaponLines = false;
		armorLines = false;
		enemyLines = false;
		int index = -1;
		String[] lineArr;
		while (scanner.hasNext()) {
			line = scanner.nextLine().trim();
			if (!line.substring(0, 2).equals("//")) {
				if (line.equals("Weapons:")) {
					weaponLines = true;
					armorLines = false;
					enemyLines = false;
					index = 0;
					continue;
				}
				if (line.equals("Armor:")) {
					weaponLines = false;
					armorLines = true;
					enemyLines = false;
					index = 0;
					continue;
				}
				if (line.equals("Enemies:")) {
					weaponLines = false;
					armorLines = false;
					enemyLines = true;
					index = 0;
					continue;
				}
				lineArr = line.split("; ");
				if (weaponLines) {
					weapons[index] = new Weapon(lineArr[0], Integer.parseInt(lineArr[1]), lineArr[2]);
					index++;
					continue;
				}
				if (armorLines) {
					armor[index] = new Armor(lineArr[0], Integer.parseInt(lineArr[1]), lineArr[2]);
					index++;
					continue;
				}
				if (enemyLines) {
					enemies[index] = new Fighter(lineArr[0], Integer.parseInt(lineArr[1]), Integer.parseInt(lineArr[2]),
							Integer.parseInt(lineArr[3]));
					if (!lineArr[4].equals("null"))
						for (int i = 0; i < weapons.length; i++)
							if (weapons[i].name().equals(lineArr[4])) {
								enemies[index].setWeapon(weapons[i]);
								break;
							}
					if (!lineArr[5].equals("null"))
						for (int i = 0; i < armor.length; i++)
							if (armor[i].getName().equals(lineArr[5])) {
								enemies[index].setArmor(armor[i]);
								break;
							}
					index++;
					continue;
				}
			}
		}
		scanner.close();
	}

}
