package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import dungeon.Dungeon;
import fighting.Armor;
import fighting.CombatAutomat;
import fighting.Fighter;
import fighting.Inventory;
import fighting.Weapon;
import gui.Render;

public class Main {

	public static void main(String[] args) {
		new Main();
	}

	private String[] VALID_KEYS = { "w", "a", "s", "d", "i", "j", "k", "l", " ", "e", "\n" };

	private final Render render;
	private Fighter player;
	private CombatAutomat fightingField;
	private Dungeon dungeon;

	private Weapon[] weapons;
	private Armor[] armor;
	private Fighter[] enemies;

	private final Inventory INVENTORY;

	public Main() {
		readAssets(new File("assets/Dungeon/config.txt"));

		INVENTORY = new Inventory();
		INVENTORY.addWeapon(getWeapon("Wooden Sword"));
		INVENTORY.addArmor(getArmor("Leather Armor"));

		player = new Fighter("X", 1, 10, 1);
		player.setWeapon(INVENTORY.getWeapon(0));
		player.setArmor(INVENTORY.getArmor(0));

		render = new Render(this, VALID_KEYS, player, enemies[0], INVENTORY);
		render.refreshArmorLabel();
		render.refreshWeaponLabel();
		render.println("Welcome to our small game!");
		dungeon = new Dungeon(this, render);
	}

	public void pickedUpItem(String itemName) {
		Weapon weapon = getWeapon(itemName);
		if (weapon != null) {
			INVENTORY.addWeapon(weapon);
			render.println("You have picked up the weapon " + itemName + "!");
			return;
		}
		Armor armor = getArmor(itemName);
		INVENTORY.addArmor(armor);
		render.println("You have picked up the armor " + itemName + "!");
		return;
	}

	/**
	 * Only for the player input.
	 * 
	 * @param key
	 */
	public void keyPressed(String key) {
		if (render.isFighting()) {
			fightingField.keyPressed(key, true);
			return;
		}
		if (key.equals("e") && !render.isInventoryOpen()) {
			render.openInventory();
			return;
		}
		if (render.isInventoryOpen()) {
			if (key.equals("e")) {
				render.closeInventory();
			}
			render.inventoryControll(key);
			return;
		}
		dungeon.keyPressed(key);
	}

	public Weapon getWeapon(String name) {
		for (int i = 0; i < weapons.length; i++)
			if (weapons[i].name().equals(name))
				return weapons[i];
		return null;
	}

	public Armor getArmor(String name) {
		for (int i = 0; i < armor.length; i++)
			if (armor[i].getName().equals(name))
				return armor[i];
		return null;
	}
	
	public Fighter getEnemy(String name) {
		for (int i = 0; i < enemies.length; i++)
			if (enemies[i].NAME.equals(name))
				return enemies[i];
		return null;
	}

	public void initFight(String enemy) {
		render.startFight(getEnemy(enemy));
		fightingField = new CombatAutomat(player, getEnemy(enemy), render, this);
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
					try {
						String dropList = lineArr[6].substring(1, lineArr[6].length() - 1);
						String[] drops = dropList.split(" & ");
						for (int i = 0; i < drops.length; i++) {
							enemies[index].addDrop(drops[i].split(", ")[0],
									(int) (Double.parseDouble(drops[i].split(", ")[1]) * 100));
						}
					} catch (Exception e) {
						if (lineArr.length == 7) {
							System.err.println("Something wrong with the loot of " + enemies[index].NAME
									+ ". But the program will continue whilst ignoring the error!");
						}
					}
					index++;
					continue;
				}
			}
		}
		scanner.close();
	}

}
