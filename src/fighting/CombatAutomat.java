package fighting;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;

import gui.Render;

public class CombatAutomat {
	
	private Timer timer;

	private final Render render;
	private final Fighter enemy, player;

	CombatSituation[] situations;
	CombatSituation currentSituation;

	private String[] damageMultiplierNames;
	private double[] damageMultiplier;

	public CombatAutomat(Fighter player, Fighter enemy, Render render) {
		this.render = render;
		this.player = player;
		this.enemy = enemy;
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
			if (actions[i].key.equals(key)) {
				if (actions[i].isTimerActive)
					changeSituation(actions[i].nextSituation);
				break;
			}
		}

	}

	private void changeSituation(CombatSituation newSituation) {
		currentSituation = newSituation;

		if(timer != null) 
			timer.interrupt();
		
		CombatAction[] actions = currentSituation.actions();
		for (int i = 0; i < actions.length; i++) {
			actions[i].isTimerActive = true;
		}
		
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
				render.endFight();
			} else if (enemy.getCurrentHp() <= 0) {
				enemy.setCurrentHp(0);
//				output = currentSituation.deathMessage();
				output = "<the enemy> died, you won!";
				render.endFight();
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
								isTimerActive = false;
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
								isTimerActive = false;
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

		public boolean isTimerActive = true;
		public int maximumReactionTime;
		public CombatSituation nextSituation;

		private String nextSituationName; // for one time use
		
		public abstract void timerEnd();
		
		public int timerLength() {
			return maximumReactionTime;
		}
	}
	
	private class Timer extends Thread {

		private LinkedList<CombatAction> actions;
		private CombatAction lastTimer;

		public Timer(CombatAction[] actions) {
			this.actions = new LinkedList<CombatAction>();
			for (int i = 0; i < actions.length; i++) {
				if (actions[i].timerLength() == -1)
					lastTimer = actions[i];
				else if (actions[i].timerLength() == 0)
					continue; // because the timer only marks, if a key cannot be pressed any more, which is
							  // not the case with infinite timers
				else
					this.actions.add(actions[i]);
			}
		}

		@SuppressWarnings("static-access")
		@Override
		public void run() {
			try {
				int sleptTime = 0;
				while (!actions.isEmpty()) {
					
					int enemyWaitTime = (int) (Math.random() * 7 + 0.5) * 1000;
					
					CombatAction[] currentTimer = shortestTimer();
					int timeToSleep = currentTimer[0].timerLength() - sleptTime;
					
					if((sleptTime + timeToSleep) < (sleptTime + enemyWaitTime)) {
						this.sleep(timeToSleep);
						enemyTurn();
						sleptTime += enemyWaitTime;
						timeToSleep = currentTimer[0].timerLength() - sleptTime; // can lead to error
					} 
					
					this.sleep(timeToSleep);
					sleptTime += timeToSleep;
					
					for (int i = 0; i < currentTimer.length; i++) {
						currentTimer[i].timerEnd();
					}
				}
				if (lastTimer != null)
					lastTimer.timerEnd();

			} catch (InterruptedException e) {
				;
			}
		}
		
		private void enemyTurn() {
			int current = (int) (Math.random() * (actions.size() - 1));
			for(int i = 0; i < actions.size(); i++) {
				if(actions.get(current).isTimerActive && actions.get(current).isPlayer) {
					keyPressed(actions.get(current).key, false);
				}
			}
		}

		private CombatAction[] shortestTimer() {
			LinkedList<CombatAction> shortestTimer = new LinkedList<CombatAction>();
			shortestTimer.add(actions.get(0));
			for (int i = 0; i < actions.size(); i++) {
				if (actions.get(i).timerLength() < shortestTimer.get(0).timerLength()) {
					shortestTimer.clear();
					shortestTimer.add(actions.get(i));
				} else if (actions.get(i).timerLength() == shortestTimer.get(0).timerLength())
					shortestTimer.add(actions.get(i));

			}
			for (int i = 0; i < shortestTimer.size(); i++) {
				actions.remove(shortestTimer.get(i));
			}
			return shortestTimer.toArray(new CombatAction[shortestTimer.size()]);
		}

	}

}
