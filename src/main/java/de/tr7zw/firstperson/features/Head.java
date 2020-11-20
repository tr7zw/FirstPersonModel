package de.tr7zw.firstperson.features;

public enum Head {
VANILLA(0)

;
	
	private int id;
	
	Head(int id){
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	
	public static Head getChest(int id) {
		for(Head c : values()) {
			if(c.id == id)return c;
		}
		return VANILLA;
	}

}
