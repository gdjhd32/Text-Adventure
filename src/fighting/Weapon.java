package fighting;

public class Weapon {

	public final int ATK; // attack
	public final String NAME;
	public final String TYPE;

	//instead of Broadsword maybe Longsword?
	public static enum WeaponType {
		Broadsword, Spear, Shortsword_Shield
	}

	public Weapon(String name, int atk, String type) {
		NAME = name;
		ATK = atk;
		TYPE = type;
	}

}
