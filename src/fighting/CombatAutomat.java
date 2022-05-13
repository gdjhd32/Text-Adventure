package fighting;

import org.apache.poi.xssf.usermodel.*;

public class CombatAutomat {

	CombatSituation[] situations;

	public CombatAutomat() {
		tempCreateAutomat();
	}

	// temporary
	private void tempCreateAutomat() {
		situations = new CombatSituation[2];
		situations[0] = new CombatSituation("Du stehst dem Monster gegenüber.");
		situations[1] = new CombatSituation(null);
		
		
		XSSFWorkbook s = new XSSFWorkbook();
		XSSFSheet g = s.getSheetAt(0);

	}

	private class CombatSituation {
		//String[] keys, int[] timesForKeys, String[] cKeys, int[] cTimesForKeys, String description

		String description;

		public CombatSituation(String description) {
			this.description = description;
		}

	}
}
