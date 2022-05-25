package fighting;

public class Fighter {

	private Weapon weapon;
	private Armor armor;

	private int ag; // agility
	private int maxHp; // health points
	private double currentHp;
	private int str; // strength

	public final String NAME;

	public Fighter(String name, int str, int maxHp, int ag) {
		NAME = name;
		this.str = str;
		this.maxHp = maxHp;
		currentHp = maxHp;
		this.ag = ag;
	}

	public Weapon getWeapon() {
		return weapon;
	}

	public void setWeapon(Weapon w) {
		weapon = w;
	}

	public Armor getArmor() {
		return armor;
	}

	public void setArmor(Armor a) {
		armor = a;
	}

	public int getMaxHp() {
		return maxHp;
	}

	public void setMaxHp(int hp) {
		this.maxHp = hp;
	}

	public double getCurrentHp() {
		return currentHp;
	}

	public void setCurrentHp(double hp) {
		this.currentHp = hp;
	}

	public int getAg() {
		return ag;
	}

	public void setAg(int ag) {
		this.ag = ag;
	}

	public int getStr() {
		return str;
	}

	public void setStr(int str) {
		this.str = str;
	}

}
