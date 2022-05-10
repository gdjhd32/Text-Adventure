package fighting;

public class CombatAutomat {

	CombatSituation[] situations;
	CombatAction[][] matrix;

	public CombatAutomat() {
		tempCreateAutomat();
	}

	// temporary
	private void tempCreateAutomat() {
		situations = new CombatSituation[2];
		situations[0] = new CombatSituation("Du stehst dem Monster gegenüber.");
		situations[1] = new CombatSituation(null);

		matrix = new CombatAction[situations.length][situations.length];
		matrix[0][1] = new CombatAction(new String[] { "k" }, null, null, null,
				"Du stichst dein Schwert in die Richtung des Monsters.");
		matrix[0][1] = new CombatAction(null, null, new String[] {" "}, new int[] { 1 },
				"");

	}

	private class CombatAction {

		String description;
		String[] keys;
		int[] timesForKeys;

		String[] cKeys;
		int[] cTimesForKeys;

		/**
		 * @param keys
		 * @param timesForKeys All values in seconds.
		 */
		/**
		 * c = computer, meaning the actions the computer can take.
		 * 
		 * @param keys
		 * @param timesForKeys  Time in seconds.
		 * @param cKeys
		 * @param cTimesForKeys Time in seconds.
		 * @param description
		 */
		public CombatAction(String[] keys, int[] timesForKeys, String[] cKeys, int[] cTimesForKeys,
				String description) {
			this.keys = keys;
			this.timesForKeys = timesForKeys;
			this.description = description;
			this.cKeys = cKeys;
		}

		public String[] getKeys() {
			return keys;
		}

		public int[] getTimesForKeys() {
			return timesForKeys;
		}

	}

	private class CombatSituation {

		String description;

		public CombatSituation(String description) {
			this.description = description;
		}

	}
}
