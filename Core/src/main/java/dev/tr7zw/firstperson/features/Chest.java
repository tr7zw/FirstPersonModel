package dev.tr7zw.firstperson.features;

public enum Chest {
VANILLA(0),
FEMALE1(1),
FEMALE2(2),
;
	private int id;
	
	Chest(int id){
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	
	public static Chest getChest(int id) {
		for(Chest c : values()) {
			if(c.id == id)return c;
		}
		return VANILLA;
	}
}
