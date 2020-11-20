package de.tr7zw.firstperson.features;

public enum Back {
VANILLA(0)

;
	
	private int id;
	
	Back(int id){
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	
	public static Back getChest(int id) {
		for(Back c : values()) {
			if(c.id == id)return c;
		}
		return VANILLA;
	}

}