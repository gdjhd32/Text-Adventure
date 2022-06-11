package fighting;

public class Fighter {

	private Weapon weapon;
	private Armor armor;

	private int ag; // agility
	private int maxHp; // health points
	private double currentHp;
	private int str; // strength

	public final String NAME;
	
	private DropList drops;

	public Fighter(String name, int str, int maxHp, int ag) {
		NAME = name;
		this.str = str;
		this.maxHp = maxHp;
		currentHp = maxHp;
		this.ag = ag;
		drops = new DropList();
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
	
	public void addDrop(String name, int dropChance) {
		drops.add(name, dropChance);
		checkDrops();
	}
	
	public String getDropName(int id) {
		return drops.get(id).NAME;
	}
	
	public int getDropChance(int id) {
		return drops.get(id).DROP_CHANCE;
	}
	
	public int getDropListLength() {
		return drops.getLength();
	}
	
	private void checkDrops() {
		int totalDropChances = 0;
		for (int i = 0; i < drops.length; i++) {
			totalDropChances += drops.get(i).DROP_CHANCE;
		}
		if (totalDropChances > 10000) {
			System.err.println("More than 100% total drop chance at the enemy " + NAME + "! Please fix that!");
			System.exit(0);
		}
	}

	private class DropList {
		
		private Drop first;
		private int length;
		
		public DropList() {
			first = null;
		}
		
		public void add(String name, int dropChance) {
			if (first == null) {
				first = new Drop(name, dropChance);
				length++;
				return;
			}
			Drop current = first;
			while (current.getNext() != null) {
				current = current.getNext();
			}
			current.setNext(new Drop(name, dropChance));
			length++;
		}
		
		public Drop get(int id) {
			try {
				Drop current = first;
				for (int i = 0; i < id; i++) {
					current = current.getNext();
				}
				return current;
			} catch (Exception e) {
				return null;
			}
		}
		
		public int getLength() {
			return length;
		}
		
		private class Drop {
			
			public final String NAME;
			public final int DROP_CHANCE;
			
			private Drop next;
			
			public Drop(String name, int dropChance) {
				NAME = name;
				DROP_CHANCE = dropChance;
				next = null;
			}
			
			public void setNext(Drop next) {
				this.next = next;
			}
			
			public Drop getNext() {
				return next;
			}
			
		}
		
	}
	
}
