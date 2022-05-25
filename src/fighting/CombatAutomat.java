package fighting;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;

import gui.Render;

public class CombatAutomat {
	
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

	public void keyPressed(String key) {
		CombatAction[] actions = currentSituation.pActions();
		for(int i = 0; i < actions.length; i++) {
			if(actions[i].key.equals(key)) {
				changeSituation(actions[i].nextSituation);
				break;
			}
		}
	}
	
	private void changeSituation(CombatSituation newSituation) {
		currentSituation = newSituation;
		
		System.out.println(currentSituation.name + ": " + currentSituation.deathMessage());
		
		String output = currentSituation.description();
		
		//damage calculation
		if(currentSituation.damageMultiplier != 0) {
			double damage;
			if(currentSituation.isPlayerHit) {
				damage = player.getCurrentHp() - enemy.getStr() * enemy.getWeapon().atk() * player.getArmor().getCurrentDef() * currentSituation.damageMultiplier;
				player.setCurrentHp(damage);
			} else {
				damage = enemy.getCurrentHp() - player.getStr() * player.getWeapon().atk() * enemy.getArmor().getCurrentDef() * currentSituation.damageMultiplier;
				enemy.setCurrentHp(damage);
			}
			
			if(player.getCurrentHp() <= 0) {
				player.setCurrentHp(0);
				output = currentSituation.deathMessage();
			} else if(enemy.getCurrentHp() <= 0) {
				enemy.setCurrentHp(0);
				output = currentSituation.deathMessage();
			}
			
			output = output.replaceAll("<DMG>", (damage * -1) + "");
		}
		
		//output
		render.println(output);
		render.refreshChangeableLabel();
	}
	
	/**
	 * @param file The file that will be used to create the CombatAutomat.
	 * @throws FileNotFoundException If the handed over File was not found.
	 */
	private void createAutomat(File file) throws FileNotFoundException {
		Scanner scanner = new Scanner(file);

		//counting the number of lines
		int length = 0;
		while (scanner.hasNextLine()) {
			if(!scanner.nextLine().startsWith("//"))
				length++;
		}
		
		situations = new CombatSituation[length - 1]; //-1, because the first line is reserved for damage multipliers
		scanner.close();
		scanner = new Scanner(file);
		
		String l = scanner.nextLine();
		String[] line = l.split(";");
		//removes, if necessary, the blank at the beginning of the strings
		removeBlankInFront(line);
		
		//reads the damage multiplier at the beginning of the text file
		damageMultiplierNames = new String[line.length];
		damageMultiplier = new double[line.length];
		for(int i = 0; i < line.length; i++) {
			String[] section = line[i].split(" ");
			damageMultiplierNames[i] = section[0];
			damageMultiplier[i] = Double.parseDouble(section[1]);
		}
		
		
		for (int i = 0; scanner.hasNextLine(); i++) {
			l = scanner.nextLine();
			//Comments will be ignored
			if(l.startsWith("//")) {
				i--;
				continue;
			}
			LinkedList<CombatAction> pCombatActions = new LinkedList<CombatAction>();
			LinkedList<CombatAction> eCombatActions = new LinkedList<CombatAction>();
			
			line = l.split(";");
			
			String description;
			String name;
			String deathMessage = "";
			double damageMultiplier = 0;
			boolean isPlayerHit = false;
			
			
			name = line[0];
			description = line[line.length -1].trim();
			
			for(int o = 1; o < line.length - 1; o++) {
				line[o] = line[o].trim();
				String[] section = line[o].split(" ");
				if(section[0].substring(1).startsWith("DMG")) {
					damageMultiplier = 1;
					if(section[0].charAt(0) == 'P') 
						isPlayerHit = true;
					else 
						isPlayerHit = false;

					for(int p = 1; p < section.length; p++) {
						if(section[p].charAt(0) != '<')
							//find the right value for the damage multiplier
							for(int k = 0; k < damageMultiplierNames.length; k++) {
								if(section[p].equals(damageMultiplierNames[k])) {
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
							System.out.println(deathMessage);
						}
					}
				} else if(section[0].length() <= 2) {
					CombatAction combatAction = new CombatAction();
					if(section[0].charAt(0) == '_') {
						combatAction.nextSituationName = section[1];
						continue;
					} 
					
					combatAction.key = section[0].substring(1).toLowerCase();
					System.out.println(combatAction.key);
					combatAction.maximumReactionTime = Double.parseDouble(section[1]);
					combatAction.nextSituationName = section[2];
					
					if(section[0].charAt(0) == 'P') 
						pCombatActions.add(combatAction);
					else
						eCombatActions.add(combatAction);
				}
								
			}
						
			CombatSituation s = new CombatSituation(description, name, damageMultiplier, isPlayerHit, deathMessage, pCombatActions.toArray(new CombatAction[pCombatActions.size()]), eCombatActions.toArray(new CombatAction[eCombatActions.size()]));
			
			situations[i] = s;
		
		}
		
		//uses the name of the next situation in action to find the reference to the next situation
		for(int i = 0; i < situations.length; i++) {
			CombatAction[] actions = situations[i].pActions;
			for(int o = 0; o < actions.length; o++) {
				String name = actions[o].nextSituationName;
				for(int p = 0; p < situations.length; p++) {
					if(name.equals(situations[p].name())) {
						actions[o].nextSituation = situations[p];
						break;
					}
				}
			}
		}
		
		scanner.close();
	}
	
	private void removeBlankInFront(String[] strings) {
		for(int i = 0; i < strings.length; i++) {
			strings[i] = strings[i].trim();
		}
	}
	
	private File getAutomatType(Fighter player, Fighter enemy) {
		//BroadswordVsBroadsword
		if(player.getWeapon().type().equals(enemy.getWeapon().type())) {
			return new File("CombatAutomaten/BroadswordVsBroadsword.txt");
		}
		
		//temporary
		System.out.println("No AutomatType found");
		return null;
	}
	
	private record CombatSituation(String description, String name, double damageMultiplier, boolean isPlayerHit, String deathMessage, CombatAction[] pActions, CombatAction[] eActions) {}
	
	private class CombatAction {
		
		public String key;
		
		public double maximumReactionTime;
		public CombatSituation nextSituation;
		
		private String nextSituationName; // for one time use
		
	}
}
