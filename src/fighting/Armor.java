package fighting;

public class Armor {

	private final String name, type;
	private final double maxDef;
	private double durability = 1;

	public Armor(String name, int maxDef, String type) {
		this.name = name;
		this.type = type;
		this.maxDef = maxDef;
	}

	public double getDurability() {
		return this.durability;
	}

	/**
	 * Adds the change value to the current durability value. If the resulting value
	 * should be smaller than 0, the durability value will be set to 0.
	 * 
	 * @param change
	 */
	public void changeDurability(double change) {
		this.durability = durability + change;
		if(this.durability < 0) 
			this.durability = 0;
	}

	/**
	 * @return The current durability value multiplied by the maxDef value.
	 */
	public double getCurrentDef() {
		return this.durability * this.maxDef;
	}

	public String getName() {
		return this.name;
	}

	public String getType() {
		return this.type;
	}

	public double getMaxDef() {
		return this.maxDef;
	}

}
