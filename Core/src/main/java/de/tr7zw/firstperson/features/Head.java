package de.tr7zw.firstperson.features;

public enum Head {
VANILLA(0),
BONE(1),
LEAD(2),

;
	
	private int id;
	
	Head(int id){
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	
	public static Head getHead(int id) {
		for(Head c : values()) {
			if(c.id == id)return c;
		}
		return VANILLA;
	}

}
