package fighting;

public class Inventory {

	private ArmorList armors;
	private WeaponList weapons;

	public Inventory() {
		armors = new ArmorList();
		weapons = new WeaponList();
	}

	public void addWeapon(Weapon w) {
		weapons.add(w);
	}

	public Weapon getWeapon(int i) {
		return weapons.get(i);
	}

	public void addArmor(Armor a) {
		armors.add(a);
	}

	public Armor getArmor(int i) {
		return armors.get(i);
	}

	private class WeaponList {

		private WeaponElement first;
		private WeaponElement current;

		public WeaponList() {
			first = null;
			current = first;
		}

		public void add(Weapon w) {
			if (first == null) {
				first = new WeaponElement(w);
				return;
			}
			current = first;
			if (current.weapon.atk() < w.atk()) {
				first = new WeaponElement(w);
				first.setNext(current);
				current.setPrevious(first);
				return;
			}
			WeaponElement tmp;
			while (current.getNext() != null) {
				current = current.getNext();
				if (current.weapon.atk() < w.atk()) {
					tmp = new WeaponElement(w);
					current.getPrevious().setNext(tmp);
					tmp.setPrevious(current.getPrevious());
					tmp.setNext(current);
					current.setPrevious(tmp);
					return;
				}
			}
			current.setNext(new WeaponElement(w));
			current.getNext().setPrevious(current);
		}

		public Weapon get(int i) {
			current = first;
			try {
				for (int j = 0; j < i; j++) {
					current = current.getNext();
				}
				return current.weapon;
			} catch (Exception e) {
				return null;
			}
		}

		private class WeaponElement {

			public final Weapon weapon;
			private WeaponElement next, previous;

			public WeaponElement(Weapon w) {
				weapon = w;
				next = null;
				previous = null;
			}

			public void setNext(WeaponElement we) {
				next = we;
			}

			public WeaponElement getNext() {
				return next;
			}
			
			public void setPrevious(WeaponElement we) {
				previous = we;
			}
			
			public WeaponElement getPrevious() {
				return previous;
			}

		}

	}

	private class ArmorList {

		private ArmorElement first;
		private ArmorElement current;

		public ArmorList() {
			first = null;
			current = first;
		}

		public void add(Armor a) {
			if (first == null) {
				first = new ArmorElement(a);
				return;
			}
			current = first;
			if (current.armor.getMaxDef() < a.getMaxDef()) {
				first = new ArmorElement(a);
				first.setNext(current);
				current.setPrevious(first);
				return;
			}
			ArmorElement tmp;
			while (current.getNext() != null) {
				current = current.getNext();
				if (current.armor.getMaxDef() < a.getMaxDef()) {
					tmp = new ArmorElement(a);
					current.getPrevious().setNext(tmp);
					tmp.setPrevious(current.getPrevious());
					tmp.setNext(current);
					current.setPrevious(tmp);
					return;
				}
			}
			current.setNext(new ArmorElement(a));
		}

		public Armor get(int i) {
			current = first;
			try {
				for (int j = 0; j < i; j++) {
					current = current.getNext();
				}
				return current.armor;
			} catch (Exception e) {
				return null;
			}
		}

		private class ArmorElement {

			public final Armor armor;
			private ArmorElement next, previous;

			public ArmorElement(Armor a) {
				armor = a;
				next = null;
				previous = null;
			}

			public void setNext(ArmorElement ae) {
				next = ae;
			}

			public ArmorElement getNext() {
				return next;
			}
			
			public void setPrevious(ArmorElement ae) {
				previous = ae;
			}
			
			public ArmorElement getPrevious() {
				return previous;
			}

		}

	}

}
