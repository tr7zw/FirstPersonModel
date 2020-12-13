package de.tr7zw.firstperson.features;

public enum Hat {
VANILLA(0),
DEADMAU5(1),
ENDROD(2),
FEATHER(3),
PLUNGER(4),

;
	
	private int id;
	
	Hat(int id){
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	
	public static Hat getHat(int id) {
		for(Hat c : values()) {
			if(c.id == id)return c;
		}
		return VANILLA;
	}

}
