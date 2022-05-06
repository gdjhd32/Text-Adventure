package fighting;

public class Weapon {

	public final int ATK;
	public final String NAME;
	public final WeaponType TYPE;

	public static enum WeaponType {
		Broadsword, Spear, Shortsword, Shield
	}
	
	public Weapon(String name, int atk, WeaponType type) {
		NAME = name;
		ATK = atk;
		TYPE = type;
	}

}
