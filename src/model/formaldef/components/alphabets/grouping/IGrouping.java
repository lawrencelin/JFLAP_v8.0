package model.formaldef.components.alphabets.grouping;


public interface IGrouping {

	public abstract void setGrouping(GroupingPair gp);
	
	public abstract GroupingPair getGrouping();

	public abstract boolean usingGrouping();

}