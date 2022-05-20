package fighting;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

public class CombatAutomat {

	CombatSituation[] situations;

	public String[] damageMultiplierNames;
	public double[] damageMultiplier;
	
	private enum AutomatType {
		BroadswordVsBroadsword
	}

	public CombatAutomat(Fighter player, Fighter enemy) {
		try {
			createAutomat(getAutomatType(player, enemy));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
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
		
		situations = new CombatSituation[length - 1]; //-1, because the first line is reserved for damage multiplier
		scanner.close();
		scanner = new Scanner(file);
		
		String l = scanner.nextLine();
		//Comments will be ignored
		while(l.startsWith("//")) {
			scanner.nextLine();
		}
		String[] line = l.split(";");
		//removes, if necessary, the blank at the beginning of the strings
//		for(int i = 0; i < line.length; i++) {
//			if(line[i].charAt(0) == ' ')
//				line[i] = line[i].substring(1);
//		}
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
			LinkedList<CombatAction> pCombatActions = new LinkedList<CombatAction>();
			LinkedList<CombatAction> eCombatActions = new LinkedList<CombatAction>();
					
			l = scanner.nextLine();
			//Comments will be ignored
			if(!l.startsWith("//")) {
				line = l.split(";");
				
				String description;
				String name;
				double damageMultiplier = 1;
				double damage = 0;
				boolean isPlayerHit = false;
				
				
				name = line[0];
				//removes, if necessary, the blank at the beginning of the string
				if(line[line.length -1].charAt(0) == ' ') line[line.length -1] = line[line.length -1].substring(1);
				description = line[line.length -1];
				
				for(int o = 1; o < line.length - 1; o++) {
					line[o] = removeBlankInFront(line[o]);
					String[] section = line[o].split(" ");
					if(section[0].substring(1).startsWith("DMG")) {
						if(section[0].charAt(0) == 'P') 
							isPlayerHit = true;
						else 
							isPlayerHit = false;

						for(int p = 1; p < section.length; p++) {
							//find the right value for the damage multiplier
							for(int k = 0; k < damageMultiplierNames.length; k++) {
								if(section[p].equals(damageMultiplierNames[k])) {
									damageMultiplier = damageMultiplier * this.damageMultiplier[k];
									break;
								}
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
					
					ArrayList<CombatAction> temp = new ArrayList<CombatAction>();
					
				}
				
				CombatSituation s = new CombatSituation(description, name, damageMultiplier, damage, isPlayerHit, pCombatActions.toArray(new CombatAction[pCombatActions.size()]), eCombatActions.toArray(new CombatAction[eCombatActions.size()]));
				
				System.out.println(s.description);
				situations[i] = s;
			} 
		}
		
		scanner.close();
	}
	
	private String removeBlankInFront(String string) {
		if(string.charAt(0) == ' ') 
			string = string.substring(1);
		return string;
	}
	
	private void removeBlankInFront(String[] strings) {
		for(int i = 0; i < strings.length; i++) {
			if(strings[i].charAt(0) == ' ')
				strings[i] = strings[i].substring(1);
		}
	}
		
	private File getAutomatType(Fighter player, Fighter enemy) {
		//BroadswordVsBroadsword
		if(player.getWeapon().TYPE.equals(enemy.getWeapon().TYPE)) {
			return new File("CombatAutomaten/BroadswordVsBroadsword.txt");
		}
		
		//temporary
		System.out.println("No AutomatType found");
		return null;
	}

//	private class CombatSituation {
//		
//		public String description; //
//		public String name; //
//		public double damageMultiplier; //
//		
//		public double damage; // necessary?
//		public boolean isPlayerHit; // if true, the damage is calculated for the player, if false for the enemy
//
//		public CombatAction[] pActions;
//		public CombatAction[] eActions;
//		
//		public CombatSituation() {
//
//		}
//	}
	
	private record CombatSituation(String description, String name, double damageMultiplier, double damage, boolean isPlayerHit, CombatAction[] pActions, CombatAction[] eActions) {}
	
	private class CombatAction {
		
		public String key;
		public double maximumReactionTime;
		public CombatSituation nextSituation;
		
		private String nextSituationName; // for one time use
		
	}
}
