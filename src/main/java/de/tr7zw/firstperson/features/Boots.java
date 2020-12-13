package de.tr7zw.firstperson.features;

public enum Boots {
VANILLA(0),
BOOT1(1),

;
	
	private int id;
	
	Boots(int id){
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	
	public static Boots getBoots(int id) {
		for(Boots c : values()) {
			if(c.id == id)return c;
		}
		return VANILLA;
	}

}
