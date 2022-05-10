package fighting;

public class CombatAutomat {
	
	CombatSituation[] situations;
	CombatAction[][] matrix;
	
	public CombatAutomat() {
		tempCreateAutomat();
	}
	
	private void tempCreateAutomat() {
		situations = new CombatSituation[2];
		
		matrix = new CombatAction[situations.length][situations.length];
//		matrix[0][1] = new CombatAction();
		
	}
	
	private class CombatAction {
		
		String[] keys;
		int[] timesForKeys;
		
		public CombatAction(String[] keys, int[] timesForKeys) {
			keys = keys;
			timesForKeys = timesForKeys;
		}
		
	}
	
	private class CombatSituation {
		
		public CombatSituation() {
			
		}
		
	}
}
