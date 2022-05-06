package fighting;

public class Armor {

	public final int DEF;
	public final String NAME;
	public final ArmorType TYPE;

	public static enum ArmorType {
		Leather, Iron
	}
	
	public Armor(String name, int def, ArmorType type) {
		NAME = name;
		DEF = def;
		TYPE = type;
	}

}
