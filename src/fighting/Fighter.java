package fighting;

public class Fighter {

	private Weapon weapon;
	private Armor armor;

	private int ag; // agility
	private int maxHp; // health points
	private int currentHp;
	private int str; // strength
	private int atk = 0; // attack
	private int maxDef = 0; // defense
	private int currentDef = maxDef;

	public final String NAME;

	private Position position;

	public static enum Position {
		LeftUp, MiddleUp, RightUp, LeftCenter, MiddleCenter, RightCenter, LeftDown, MiddleDown, RightDown
	}

	public static enum Direction {
		Up, Down, Left, Right
	}

	public Fighter(String name, int str, int maxHp, int ag) {
		NAME = name;
		this.str = str;
		this.maxHp = maxHp;
		currentHp = maxHp;
		this.ag = ag;
		setPosition(Position.MiddleCenter);
	}

	public Weapon getWeapon() {
		return weapon;
	}

	public void setWeapon(Weapon w) {
		weapon = w;
		setAtk(weapon.ATK);
	}

	public Armor getArmor() {
		return armor;
	}

	public void setArmor(Armor a) {
		armor = a;
		maxDef = armor.DEF;
		currentDef = maxDef;
	}

	public int getMaxHp() {
		return maxHp;
	}

	public void setMaxHp(int hp) {
		this.maxHp = hp;
	}

	public int getCurrentHp() {
		return currentHp;
	}

	public void setCurrentHp(int hp) {
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

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public int getMaxDef() {
		return maxDef;
	}
	
	public int getCurrentDef() {
		return currentDef;
	}

	public void setCurrentDef(int def) {
		this.currentDef = def;
	}

	public int getAtk() {
		return atk;
	}

	public void setAtk(int atk) {
		this.atk = atk;
	}

}
