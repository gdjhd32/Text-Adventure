package fighting;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class CombatAutomat {

	CombatSituation[] situations;

	public CombatAutomat() {
		try {
			tempCreateAutomat();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// temporary
	private void tempCreateAutomat() throws IOException {
		situations = new CombatSituation[2];
		situations[0] = new CombatSituation("Du stehst dem Monster gegenüber.");
		situations[1] = new CombatSituation(null);
		
	}

	private class CombatSituation {
		//String[] keys, int[] timesForKeys, String[] cKeys, int[] cTimesForKeys, String description

		String description;

		public CombatSituation(String description) {
			this.description = description;
		}

	}
}
