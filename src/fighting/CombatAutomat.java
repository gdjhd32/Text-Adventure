package fighting;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;

import gui.Render;
import main.Main;

public class CombatAutomat {
	
	private Timer timer;

	private final Render render;
	private final Main PARENT;
	private final Fighter enemy, player;

	CombatSituation[] situations;
	CombatSituation currentSituation;

	private String[] damageMultiplierNames;
	private double[] damageMultiplier;
	
	public CombatAutomat(Fighter player, Fighter enemy, Render render, Main parent) {
		this.render = render;
		this.player = player;
		this.enemy = enemy;
		PARENT = parent;
		try {
			createAutomat(getAutomatType(player, enemy));
			changeSituation(situations[0]);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void keyPressed(String key, boolean isPlayer) {
		CombatAction[] actions = currentSituation.actions();

		for (int i = 0; i < actions.length; i++) {
			if (actions[i].key == null)
				continue;
			if (actions[i].key.equals(key) && actions[i].isPlayer == isPlayer) {
				if (actions[i].isTimerActive())
					changeSituation(actions[i].nextSituation);
				break;
			}
		}

	}

	private void changeSituation(CombatSituation newSituation) {
		System.out.println("-----------------------------------");
		currentSituation = newSituation;
		
		for(int i = 0; i < currentSituation.actions.length; i++) {
			currentSituation.actions[i].startTime = System.currentTimeMillis();
		}

		if(timer != null) 
			timer.interrupt();
		
		CombatAction[] actions = currentSituation.actions();
//		for (int i = 0; i < actions.length; i++) {
//			actions[i].isTimerActive = true;
//		}
		
		String output = currentSituation.description();

		// damage calculation : (STR * ATK * DMG-multiplier) - enemy DEF; < 0 -> DMG = 0
		if (currentSituation.damageMultiplier != 0) {
			double damage;
			if (currentSituation.isPlayerHit) {
				damage = (int) (enemy.getStr() * enemy.getWeapon().atk() * currentSituation.damageMultiplier)
						- player.getArmor().getCurrentDef() < 0 ? 0
								: (enemy.getStr() * enemy.getWeapon().atk() * currentSituation.damageMultiplier)
										- player.getArmor().getCurrentDef();
				player.setCurrentHp(player.getCurrentHp() - damage);
			} else {
				damage = (int) (player.getStr() * player.getWeapon().atk() * currentSituation.damageMultiplier)
						- enemy.getArmor().getCurrentDef() < 0 ? 0
								: (player.getStr() * player.getWeapon().atk() * currentSituation.damageMultiplier)
										- enemy.getArmor().getCurrentDef();
				enemy.setCurrentHp(enemy.getCurrentHp() - damage);
			}

			if (player.getCurrentHp() <= 0) {
				player.setCurrentHp(0);
//				output = currentSituation.deathMessage();
				output = "You died, try again.";
				render.println(output);
				render.endFight();
				System.exit(0);
			} else if (enemy.getCurrentHp() <= 0) {
				enemy.setCurrentHp(0);
//				output = currentSituation.deathMessage();
				output = "<the enemy> died, you won!";
				output = output.replaceAll("<the enemy>", enemy.NAME);
				render.println(output);
				render.endFight();
				//has no loot
				if (enemy.getDropListLength() == 0) {
					return;
				}
				//has loot
				String[] itemName = new String[enemy.getDropListLength()];
				int[] dropChance = new int[enemy.getDropListLength()];
				for (int i = 0; i < itemName.length; i++) {
					itemName[i] = enemy.getDropName(i);
					dropChance[i] = enemy.getDropChance(i);
				}
				int dropNumber = (int) (10000 * Math.random()) + 1;//<- "number" of the loot in the loot pool
				int wholeDropNumber = 0;	//used to specify the drop in the "loot pool"
				for (int i = 0; i < itemName.length; i++) {
					wholeDropNumber += dropChance[i];
					if (dropNumber <= wholeDropNumber) {
						render.println("Oh look! " + enemy.NAME + " dropped something!");
						PARENT.pickedUpItem(itemName[i]);
						break;
					}
				}
				return;
			}

			output = output.replaceAll("<DMG>", damage + "");
			output = output.replaceAll("<the enemy>", enemy.NAME);
		}

		// output
		render.println(output);
		render.refreshStatLabel();
		
		timer = new Timer(currentSituation.actions());
		timer.start();
		System.out.println("End new Situation");
	}

	/**
	 * @param file The file that will be used to create the CombatAutomat.
	 * @throws FileNotFoundException If the handed over File was not found.
	 */
	private void createAutomat(File file) throws FileNotFoundException {
		Scanner scanner = new Scanner(file);

		// counting the number of lines
		int length = 0;
		while (scanner.hasNextLine()) {
			if (!scanner.nextLine().startsWith("//"))
				length++;
		}

		situations = new CombatSituation[length - 1]; // -1, because the first line is reserved for damage multipliers
		scanner.close();
		scanner = new Scanner(file);

		String l = scanner.nextLine();
		String[] line = l.split(";");

		removeBlankInFront(line);

		// reads the damage multiplier at the beginning of the text file
		damageMultiplierNames = new String[line.length];
		damageMultiplier = new double[line.length];
		for (int i = 0; i < line.length; i++) {
			String[] section = line[i].split(" ");
			damageMultiplierNames[i] = section[0];
			damageMultiplier[i] = Double.parseDouble(section[1]);
		}

		for (int i = 0; scanner.hasNextLine(); i++) {
			l = scanner.nextLine();
			// Comments will be ignored
			if (l.startsWith("//")) {
				i--;
				continue;
			}
			LinkedList<CombatAction> combatActions = new LinkedList<CombatAction>();

			line = l.split(";");

			String description;
			String name;
			String deathMessage = "";
			double damageMultiplier = 0;
			boolean isPlayerHit = false;

			name = line[0];
			description = line[line.length - 1].trim();

			for (int o = 1; o < line.length - 1; o++) {
				line[o] = line[o].trim();
				String[] section = line[o].split(" ");
				if (section[0].substring(1).startsWith("DMG")) {
					damageMultiplier = 1;
					if (section[0].charAt(0) == 'P')
						isPlayerHit = true;
					else
						isPlayerHit = false;

					for (int p = 1; p < section.length; p++) {
						if (section[p].charAt(0) != '<')
							// find the right value for the damage multiplier
							for (int k = 0; k < damageMultiplierNames.length; k++) {
								if (section[p].equals(damageMultiplierNames[k])) {
									damageMultiplier = damageMultiplier * this.damageMultiplier[k];
									break;
								}
							}
						else {
							int k = 0;
							while (line[o].charAt(k) != '<') {
								k++;
							}
							deathMessage = line[o].substring(k + 1, line[o].length() - 1);
						}
					}
				} else if (section[0].length() <= 2) {
					CombatAction combatAction;
					if (section[0].charAt(0) == '_') {

						combatAction = new CombatAction() {
							@Override
							public void timerEnd() {
								changeSituation(this.nextSituation);
							}
						};

						if (section.length == 3) {
							combatAction.maximumReactionTime = Integer.parseInt(section[1]) * 1000;
							combatAction.nextSituationName = section[2];
						} else {
							combatAction.maximumReactionTime = -1;
							combatAction.nextSituationName = section[1];
						}
						combatAction.key = "_";

						// has to be changed
						combatActions.add(combatAction);
						continue;
					} else
						combatAction = new CombatAction() {
							public void timerEnd() {
//								isTimerActive = false;
							}
						};

					combatAction.key = section[0].substring(1).toLowerCase();
					combatAction.maximumReactionTime = Integer.parseInt(section[1]) * 1000;
					combatAction.nextSituationName = section[2];

					if (section[0].charAt(0) == 'P')
						combatAction.isPlayer = true;
					else
						combatAction.isPlayer = false;
					combatActions.add(combatAction);
				}

			}

			CombatSituation s = new CombatSituation(description, name, damageMultiplier, isPlayerHit, deathMessage,
					combatActions.toArray(new CombatAction[combatActions.size()]));
			situations[i] = s;

		}
		
		// uses the name of the next situation in action to find the reference to the
		// next situation
		for (int i = 0; i < situations.length; i++) {
			CombatAction[] actions = situations[i].actions;
			for (int o = 0; o < actions.length; o++) {
				String name = actions[o].nextSituationName;
				for (int p = 0; p < situations.length; p++) {
					if (name.equals(situations[p].name())) {
						actions[o].nextSituation = situations[p];
						break;
					}
				}
			}
		}

		scanner.close();
	}

	private void removeBlankInFront(String[] strings) {
		for (int i = 0; i < strings.length; i++) {
			strings[i] = strings[i].trim();
		}
	}

	private File getAutomatType(Fighter player, Fighter enemy) {		
		// BroadswordVsBroadsword
		if (player.getWeapon().type().equals(enemy.getWeapon().type())) {
			return new File("assets/CombatAutomaten/BroadswordVsBroadsword.txt");
		}

		// temporary
		System.out.println("No AutomatType found");
		return null;
	}

	private record CombatSituation(String description, String name, double damageMultiplier, boolean isPlayerHit,
			String deathMessage, CombatAction[] actions) {
	}

	private abstract class CombatAction {

		public boolean isPlayer;
		public String key;

//		private boolean isTimerActive = true;
		public int maximumReactionTime;
		public CombatSituation nextSituation;
		public long startTime;

		private String nextSituationName; // for one time use
		
		public abstract void timerEnd();
		
		public boolean isTimerActive() {
//			System.out.println(key + ": " + maximumReactionTime);
//			System.out.println(System.currentTimeMillis() - startTime + maximumReactionTime);
			if(startTime + maximumReactionTime > System.currentTimeMillis() - 20 || maximumReactionTime == 0) // -20 to account for computing
				return true;
			else 
				return false;
		}
	}
	
	private class Timer extends Thread {

		private LinkedList<CombatAction> actions;
		private CombatAction lastTimer;

		public Timer(CombatAction[] actions) {
			this.actions = new LinkedList<CombatAction>();
			for (int i = 0; i < actions.length; i++) {
				if (actions[i].maximumReactionTime == -1)
					lastTimer = actions[i];
				else
					this.actions.add(actions[i]);
			}
		}

		@SuppressWarnings("static-access")
		@Override
		public void run() {
			try {
//				if(lastTimer != null)
//					System.out.println(lastTimer.);
				
				int sleptTime = 0;
					
//				int enemyWaitTime = ((int) (Math.random() * 7 + 0.5) * 100) * 10;
				int enemyWaitTime = 2000;
				int lastTimerTime = longestTime();
				
				if(lastTimer != null && lastTimerTime < enemyWaitTime) {
//					System.out.println(lastTimerTime);
					this.sleep(lastTimerTime);
					sleptTime = lastTimerTime;
					lastTimer.timerEnd();
				} 
				
				this.sleep(enemyWaitTime - sleptTime);
				enemyTurn();
				
			} catch (InterruptedException e) {
				;
			}
		}
		
		private void enemyTurn() {
			LinkedList<CombatAction> enemyActionList = new LinkedList<CombatAction>();
			for(int i = 0; i < actions.size(); i++) {
				if(!actions.get(i).isPlayer)
					enemyActionList.add(actions.get(i));
			}
			double randomNumber = (Math.random() * (enemyActionList.size()));
			System.out.println("Random number: " + randomNumber);
			int current = (int) randomNumber; //!!!!!!!!!!!!!!!!!!
			System.out.println("Current: " + current + " Size: " + enemyActionList.size());
			for(int i = 0; i < enemyActionList.size(); i++) {
				if(enemyActionList.get(current).isTimerActive()) {
					keyPressed(enemyActionList.get(current).key, false);
					System.out.println("Selected: " + enemyActionList.get(current).key);
					return;
				}
				if(current == actions.size() - 1)
					current = 0;
				else 
					current++;
			}
		}

		private int longestTime() {
			if(actions.isEmpty())
				return 0;
			int longestTime = actions.get(0).maximumReactionTime;
			for(int i = 1; i < actions.size(); i++) {
				if(longestTime < actions.get(i).maximumReactionTime)
					longestTime = actions.get(i).maximumReactionTime;
			}
			System.out.println("longestTime: " + longestTime);
			return longestTime;
		}
		
		private CombatAction[] shortestTimer() {
			LinkedList<CombatAction> shortestTimer = new LinkedList<CombatAction>();
			shortestTimer.add(actions.get(0));
			for (int i = 0; i < actions.size(); i++) {
				if(actions.get(i).maximumReactionTime == 0)
					continue;
				if (actions.get(i).maximumReactionTime < shortestTimer.get(0).maximumReactionTime) {
					shortestTimer.clear();
					shortestTimer.add(actions.get(i));
				} else if (actions.get(i).maximumReactionTime == shortestTimer.get(0).maximumReactionTime)
					shortestTimer.add(actions.get(i));
			}
			for (int i = 0; i < shortestTimer.size(); i++) {
				actions.remove(shortestTimer.get(i));
			}
			return shortestTimer.toArray(new CombatAction[shortestTimer.size()]);
		}

	}

}
