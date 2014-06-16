package model.formaldef.components.alphabets.grouping;

public class GroupingPair {

	
	private Character OPEN, CLOSE;

	public GroupingPair(Character open, Character close){
		OPEN = open;
		CLOSE = close;
	}

	public GroupingPair() {
		this(null, null);
	}

	public Character getOpenGroup() {
		return OPEN;
	}

	public Character getCloseGroup() {
		return CLOSE;
	}

	public String toString(){
		return "Grouping: " + this.toShortString();
	}
	
	public String toShortString(){
		return this.OPEN + " " + this.CLOSE;
	}
	
	public boolean equals(GroupingPair o){
		return this.toShortString().equals(o.toShortString());
	}

	public boolean isUsable() {
		return OPEN != null && CLOSE != null;
	}

	public String creatGroupedString(String string) {
		return this.getOpenGroup() + string + this.getCloseGroup();
	}

	public boolean isGrouped(String s) {
		return s.startsWith(OPEN +"") && s.endsWith(CLOSE + "");
	}
	
}
