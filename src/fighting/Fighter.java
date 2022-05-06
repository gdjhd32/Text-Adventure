package fighting;

import fighting.Weapon.WeaponType;

public class Fighter {

	private Weapon leftHand;
	private Weapon rightHand;
	private Armor armor;

	private int init; // initiative
	private int hp; // health points
	private int str; // strength
	
	public final String NAME;
	
	private Position position;
	
	public static enum Position {
		LeftUp, MiddleUp, RightUp, LeftCenter, MiddleCenter, RightCenter, LeftDown, MiddleDown, RightDown
	}
	
	public static enum Direction {
		Up, Down, Left, Right
	}

	public Fighter(String name, int str, int hp, int init) {
		NAME = name;
		this.setStr(str);
		this.setHp(hp);
		this.setInit(init);
		setPosition(Position.MiddleCenter);
	}

	public void setLeftHand(Weapon w) {
		if (leftHand == rightHand) {
			if (w.TYPE != WeaponType.Broadsword) {
				leftHand = w;
				rightHand = null;
				return;
			}
			leftHand = w;
			rightHand = w;
			return;
		}
		if (w.TYPE != WeaponType.Broadsword) {
			leftHand = w;
			return;
		}
		leftHand = w;
		rightHand = w;
	}

	public void setRightHand(Weapon w) {
		if (leftHand == rightHand) {
			if (w.TYPE != WeaponType.Broadsword) {
				rightHand = w;
				leftHand = null;
				return;
			}
			rightHand = w;
			leftHand = w;
			return;
		}
		if (w.TYPE != WeaponType.Broadsword) {
			rightHand = w;
			return;
		}
		rightHand = w;
		leftHand = w;
	}

	public void setArmor(Armor a) {
		armor = a;
	}

	public Weapon leftHand() {
		return leftHand;
	}

	public Weapon getRightHand() {
		return rightHand;
	}

	public Armor getArmor() {
		return armor;
	}

	public int getHp() {
		return hp;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}

	public int getInit() {
		return init;
	}

	public void setInit(int init) {
		this.init = init;
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

}
